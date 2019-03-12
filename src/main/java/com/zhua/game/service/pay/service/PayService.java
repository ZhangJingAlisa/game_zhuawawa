package com.zhua.game.service.pay.service;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.izhaowo.code.base.exception.GeneralBusinessException;
import com.izhaowo.code.base.utils.AssertUtil;
import com.izhaowo.code.base.utils.DateUtil;
import com.izhaowo.code.base.utils.HttpUtil;
import com.izhaowo.code.base.utils.StringUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.zhua.game.base.BaseService;
import com.zhua.game.config.WechatAppletConfigBean;
import com.zhua.game.entity.GoldEntity;
import com.zhua.game.entity.UserEntity;
import com.zhua.game.entity.UserGoldPayEntity;
import com.zhua.game.entity.UserLevelEntity;
import com.zhua.game.service.pay.bean.WechatAppletNotifyResponse;
import com.zhua.game.service.pay.bean.WechatAppletPayTokenVO;
import com.zhua.game.service.pay.bean.WechatPayCommonBean;
import com.zhua.game.service.pay.bean.WechatPayCommonResponse;
import com.zhua.game.service.pay.mapper.GoldMapper;
import com.zhua.game.service.pay.mapper.UserGoldPayMapper;
import com.zhua.game.service.pay.vo.PayVO;
import com.zhua.game.service.user.mapper.UserLevelMapper;
import com.zhua.game.service.user.mapper.UserMapper;
import com.zhua.game.service.user.service.UserRecommendService;
import com.zhua.game.service.user.service.UserService;
import com.zhua.game.service.user.vo.UserVO;

@Service
public class PayService extends BaseService {

	//加密方式
	private static final String MD5 = "MD5";
	
	//微信统一下单状态
	private static final String SUCCESS = "SUCCESS";
	
	//微信回调成功响应内容
	private static final String WECHAT_SUCCESS = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
	
	//微信回调失败响应内容
	private static final String WECHAT_FAIL  = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[FAIL]]></return_msg></xml>";
		 
	
	//微信统一下单地址
	private static final String WECHAT_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
	@Autowired
	private GoldMapper goldMapper;
	
	@Autowired
	private UserGoldPayMapper userGoldPayMapper;
	
	@Autowired
	private WechatAppletConfigBean wechatSmallProgramConfigBean;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserLevelMapper userLevelMapper;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private UserRecommendService userRecommendService;
	
	public boolean getUserGoldPayEntityByCode(String code){
		UserGoldPayEntity entity = userGoldPayMapper.getUserGoldPayEntityByCode(code);
		if(entity != null){
			return entity.getStatus() == 1;
		}else{
			return false;
		}
	}
	
	private boolean checkUser(String userId){
		return userMapper.checkUser(userId) != 0; //没有中彩妆的用户
	}
	
	@Transactional
	public PayVO payGold(String userId,String goldId){
		//检查用户是否在2018年-1-28日之前中了彩妆 中过的人都无法充值
		if(checkUser(userId)){ //中了彩妆的所有都抓不住了
			System.out.println("2018年-1-26 ~ 28中彩妆的都无法充值了" + userId);
			throw new GeneralBusinessException("购买金币失败");
		}
		GoldEntity entity = goldMapper.getGoldEntityById(goldId);
		UserEntity user = userMapper.getUserEntityById(userId);
		if(entity == null || user == null){
			throw new GeneralBusinessException("error");
		}
		//user.setOpenId("oHWd31RFm1UE1G0oyZlX3EKx7gOc");
		UserGoldPayEntity pay = initUserGoldPayEntity(entity.getGoldNumber(), userId,entity.getPrice());
		userGoldPayMapper.saveUserGoldPayEntity(pay);
		WechatPayCommonBean bean = initSimleWechatSmallProgramBean(pay.getCode(), "购买金币", user.getOpenId(), entity.getPrice());
		String xml = signWechatSmallProgramSumbitFirstTime(bean);
		System.out.println(xml);
		//System.out.println(xml);
		//解析微信统一下单响应
		WechatPayCommonResponse response = analysisWechatSmallProgramResponse(xml);
		
		WechatAppletPayTokenVO vo = signWechatSmallProgramSumbitSecondTime(response);
		
		PayVO payVO = new PayVO();
		payVO.setAppId(bean.getAppid());
		payVO.setCode(pay.getCode());
		payVO.setNonceStr(vo.getNonceStr());
		payVO.setPack("prepay_id="+vo.getToken());
		payVO.setPaySign(vo.getPaySign());
		payVO.setSignType(vo.getSignType());
		payVO.setTimeStamp(vo.getTimeStamp());
		payVO.setTotalFee(pay.getPrice());
		return payVO;
	}
	
	/**
	 * 小程序调起支付API 需要再次签名
	 * @return 
	 */
	private WechatAppletPayTokenVO signWechatSmallProgramSumbitSecondTime(WechatPayCommonResponse respone){
		WechatAppletPayTokenVO vo = new WechatAppletPayTokenVO();
		vo.setNonceStr(StringUtil.randomStr(10));
		vo.setSignType(MD5);
		vo.setTimeStamp(String.valueOf(System.currentTimeMillis()));
		vo.setToken(respone.getPrepay_id());
		//此处已经排好序
		String paySignTmp = "appId="+ wechatSmallProgramConfigBean.getAppId() +"&nonceStr="+ vo.getNonceStr() +"&package=prepay_id="+ vo.getToken() + "&signType="+ vo.getSignType()  +"&timeStamp=" + vo.getTimeStamp() + "&key=" + wechatSmallProgramConfigBean.getKey();
		System.out.println(paySignTmp);
		String signMD5 = md5Hex(paySignTmp.getBytes(UTF_8)).toUpperCase();	
		vo.setPaySign(signMD5);
		return vo;
	}
	
//	public String wechatAppletPayNotify(HttpServletRequest request){
//		String responseXml = WECHAT_FAIL;
//		try(InputStream in = request.getInputStream()){
//			//解析微信小程序支付成功回调
//			WechatAppletNotifyResponse response = analysisWechatAppletNotifyResponse(in);
//			if(validate(response)){ //验证回调请求是否签名是否正确
//				//System.out.println(JSON.toJSONString(response));
//				//调用支付系统修改订单状态
//				PaymentNotfiyVO vo = paymentNotifyControllerService.paymentNotify(response.getOut_trade_no(), PaymentChannel.WECHAT_APPLET_PAY, response.getReturn_code());
//			    responseXml = vo.isStatus() ? WECHAT_SUCCESS : WECHAT_FAIL;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return responseXml;
//	}
	
	
	public String callBack(HttpServletRequest request){
		String responseXml = WECHAT_FAIL;
		try(InputStream in = request.getInputStream()){
			//解析微信小程序支付成功回调
			WechatAppletNotifyResponse response = analysisWechatAppletNotifyResponse(in);
			if(validate(response)){ //验证回调请求是否签名是否正确
				System.out.println("######################验证成功##############################");
				//System.out.println(JSON.toJSONString(response));
				//调用支付系统修改订单状态
				String code = response.getOut_trade_no();
				//System.out.println("490901727040461234");
				UserGoldPayEntity entity = userGoldPayMapper.getUserGoldPayEntityByCode(code);
				if(entity == null || entity.getStatus() != 0){
					return WECHAT_FAIL;
				}else{
					System.out.println("UserGoldPayEntity:"+JSON.toJSONString(entity));
					UserEntity user = userMapper.getUserEntityById(entity.getUserId());
					System.out.println("UserEntity:"+JSON.toJSONString(user));
					if(user == null){
						return WECHAT_FAIL;
					}else{
						user.setUtime(new Date());
						user.setGoldNumber(user.getGoldNumber() + entity.getGoldNumber());
						userMapper.updateUserEntity(user);
						
						
						entity.setStatus(1);
						userGoldPayMapper.updateUserGoldPayEntity(entity);
						
						
						//更新缓存
						UserVO userVO = userService.getUserVOByToken(user.getId());
						System.out.println("UserVO:"+JSON.toJSONString(userVO));
						if(userVO != null){
							userVO.setGold(user.getGoldNumber());
							stringRedisTemplate.boundValueOps(user.getId()).set(JSON.toJSONString(userVO), 2, TimeUnit.HOURS);
						}
						registAsyncTask(() -> {
							updateLeavl(user.getId(),entity.getPrice(),user.getName());
						});
						return WECHAT_SUCCESS;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseXml;
	}
	
	/**
	 * 充值<=50 充值一次 强力值不增加   充值50<100  增加1%  100<=充值 增加2...最多增加5%...
	 */
	private void updateLeavl(String userId,int money,String userName){
		System.out.println("#updateLeavl:"+userId);
		UserLevelEntity lev = userLevelMapper.getUserLevelEntityByUserId(userId);
		if(lev != null){
			System.out.println("#updateLeavl --- in");
			lev.setPayCount(lev.getPayCount() + 1);
			lev.setUtime(new Date());
			lev.setBizhong(0);
			int up = 3;
			int all = userGoldPayMapper.countUserGoldPayEntityByUserId(userId);
			if(all >= 10000 && all < 20000){
				up = up + 1;
			}else if(all >= 20000 && all < 30000){
				up = up + 2;
			}else if(all >= 30000 && all < 40000){
				up = up + 3;
			}else if(all >= 40000 && all < 50000){
				up = up + 4;
			}else if(all >= 60000){
				up = up + 5;
			}
			lev.setProbability(up);
			userLevelMapper.updateUserLevelEntity(lev);
			
			//更新推荐者金额
			userRecommendService.updateUserRecommendBonus(userId, money,userName);
		}
	}
	
	
	private boolean validate(WechatAppletNotifyResponse response) throws Exception {
		boolean validateFlag = false;
		if(!SUCCESS.equals(response.getReturn_code())){
			return validateFlag;
		}
		List<String> parameterList = acquireParameters(response);
		String content = Joiner.on("&").join(parameterList);
		System.out.println("content:"+content);
		System.out.println("\n");
		String strTemp = content + "&key=" + wechatSmallProgramConfigBean.getKey();
		System.out.println("strTemp:"+content);
		//String strTemp = content + "&key=izhaowowxpaywxc14a970ea5cb3df2ab";
		//System.out.println(strTemp);
		String signMD5 = md5Hex(strTemp.getBytes(UTF_8)).toUpperCase();		
//		System.out.println(signMD5);
//		System.out.println(response.getSign());
//		System.out.println(signMD5.equals(response.getSign()));
		if(signMD5.equals(response.getSign())){
			validateFlag = true;
		}
		return validateFlag;
	}
	
	public List<String> acquireParameters(WechatAppletNotifyResponse response) throws Exception {
		List<String> parameterList = new  ArrayList<>();
		Field[] fields = WechatAppletNotifyResponse.class.getDeclaredFields();
		for(Field field : fields){
			//sign不参与签名
			if(field.getName().equals("sign") ){ //|| field.getName().equals("sign_type")
				continue;
			}
			field.setAccessible(true);
			if(!AssertUtil.isNull(field.get(response))){
				parameterList.add(field.getName() + "=" + field.get(response));
			}
		}
		Collections.sort(parameterList);
		return parameterList;
	}
	
	private WechatAppletNotifyResponse analysisWechatAppletNotifyResponse(InputStream in) throws IOException{
		WechatAppletNotifyResponse respone = null;
		String temp = StreamUtils.copyToString(in,Charset.forName("UTF-8"));
		System.out.println(temp); //此处先转换为string 可以看到xml内容
		XStream xstream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
		xstream.alias("xml",WechatAppletNotifyResponse.class);
		respone = (WechatAppletNotifyResponse) xstream.fromXML(temp);
		return respone;
	}
	
	private WechatPayCommonResponse analysisWechatSmallProgramResponse(String xml){
		String rxml = HttpUtil.postWithXML(WECHAT_PAY_URL, xml, null);
		//System.out.println(rxml);
		XStream xstream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
		xstream.alias("xml",WechatPayCommonResponse.class);
		WechatPayCommonResponse respone = (WechatPayCommonResponse) xstream.fromXML(rxml);
		if(!SUCCESS.equals(respone.getReturn_code())){
			throw new GeneralBusinessException("微信小程序支付签名失败");
		}
		return respone;
	}
	
	private  UserGoldPayEntity initUserGoldPayEntity(int goldNumber,String userId,int price){
		UserGoldPayEntity entity = new UserGoldPayEntity();
		entity.setCode(generateCode15());
		entity.setCtime(new Date());
		entity.setGoldNumber(goldNumber);
		entity.setId(generatID());
		entity.setStatus(0);
		entity.setUserId(userId);
		entity.setUtime(new Date());
		entity.setPrice(price);
		return entity;
	}
	
	/**
	 * 第一次微信表单提交
	 * 微信统一下单
	 * 商户在小程序中先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易后调起支付。 
	 * 返回xml
	 * @return
	 */
	private String signWechatSmallProgramSumbitFirstTime(WechatPayCommonBean bean)  {
		List<String> parameterList = new  ArrayList<>();
		Field[] fields = WechatPayCommonBean.class.getDeclaredFields();
		for(Field field : fields){
			if(field.getName().equals("sign") ){ //|| field.getName().equals("sign_type")
				continue;
			}
			try{
				field.setAccessible(true);
				parameterList.add(field.getName()+"="+field.get(bean));
			}catch(Exception e){
				throw new GeneralBusinessException("微信小程序支付签名失败");
			}
		}
		Collections.sort(parameterList);
		String content = Joiner.on("&").join(parameterList);
		String strTemp = content + "&key=" + wechatSmallProgramConfigBean.getKey();
		String signMD5 = md5Hex(strTemp.getBytes(UTF_8)).toUpperCase();		
		bean.setSign(signMD5);
		XStream xstream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
		xstream.alias("xml",WechatPayCommonBean.class);
		String xml = xstream.toXML(bean);
		return xml;
	}
	
	private WechatPayCommonBean initSimleWechatSmallProgramBean(String tradeNumber,String subject,String openId,int amount){
		Date stime = new Date();
		Date etime = DateUtil.dateAddHour(stime, 1000 * 60 * 60 * 2); //支付2小时候过期
		WechatPayCommonBean bean = new WechatPayCommonBean();
		bean.setAppid(wechatSmallProgramConfigBean.getAppId());
		bean.setBody(subject);
		bean.setDevice_info(wechatSmallProgramConfigBean.getDeviceInfo());
		bean.setFee_type(wechatSmallProgramConfigBean.getFeeType());
		bean.setMch_id(wechatSmallProgramConfigBean.getMchId());
		bean.setNonce_str(StringUtil.randomStr(10)); //生成随机字符串
		bean.setNotify_url(wechatSmallProgramConfigBean.getNotifyUrl()); //回调地址
		bean.setOpenid(openId); //"oJqP50BY3-TaifvV3dqyPn003UDY"
		bean.setOut_trade_no(tradeNumber); //订单号
		bean.setSign_type(wechatSmallProgramConfigBean.getSignType());
		bean.setSpbill_create_ip(getHostIp());
		bean.setTime_expire(DateUtil.dateToString(etime, "yyyyMMddHHmmss"));
		bean.setTime_start(DateUtil.dateToString(stime, "yyyyMMddHHmmss"));
		bean.setTotal_fee(amount);
		bean.setTrade_type(wechatSmallProgramConfigBean.getTradeType());
		return bean;
	}
	
	//获取ip
	private String getHostIp(){
		String ip = "127.0.0.1";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}
}
