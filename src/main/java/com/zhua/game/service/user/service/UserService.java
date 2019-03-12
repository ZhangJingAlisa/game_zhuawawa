package com.zhua.game.service.user.service;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.izhaowo.code.base.utils.DateUtil;
import com.izhaowo.code.base.utils.HttpUtil;
import com.izhaowo.code.base.utils.StringUtil;
import com.zhua.game.base.BaseService;
import com.zhua.game.entity.UserEnergyEntity;
import com.zhua.game.entity.UserEntity;
import com.zhua.game.entity.UserLevelEntity;
import com.zhua.game.entity.UserShellRecordEntity;
import com.zhua.game.service.user.mapper.UserEnergyMapper;
import com.zhua.game.service.user.mapper.UserLevelMapper;
import com.zhua.game.service.user.mapper.UserMapper;
import com.zhua.game.service.user.mapper.UserShellRecordMapper;
import com.zhua.game.service.user.vo.UserEnergyVO;
import com.zhua.game.service.user.vo.UserStatus;
import com.zhua.game.service.user.vo.UserVO;
import com.zhua.game.service.user.vo.WechatToken;
import com.zhua.game.util.SHA1;


/**
 * 微信登陆相关的文档
 * https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842
 * @author liuyijiang
 *
 */
@Service
public class UserService extends BaseService {

	private static final int GOLD = 60;
	 
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserLevelMapper userLevelMapper;
	
	@Autowired
	private UserEnergyMapper userEnergyMapper;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate; 
	
	@Autowired
	private UserShellRecordMapper userShellRecordMapper;
	
	@Autowired
	private UserRecommendService userRecommendService;
	
	//公众号  gh_33226daab5fe
	
	private static final String APPID = "wx00c48cc6dad5768a";
	
	private static final String SECRET = "a2bd53e4d03efec1e2b797c88a6a9437";
	
	/**
	 * {
"errcode":0,
"errmsg":"ok",
"ticket":"bxLdikRXVbTPdHSM05e5u5sUoXNKd8-41ZO3MhKoyN5OfkWITDGgnr2fwJ0m9E8NYzWKVZvdVtaUgWvsdshFKA",
"expires_in":7200
}
	 * @return
	 */
	public WechatToken getWechatTokenTicket(String url){
		WechatToken v = new WechatToken();
		String ticket = "";
		String turl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APPID+"&secret="+SECRET;
		String str = HttpUtil.get(turl,null,null);
		System.out.println(str);
		JSONObject obj1 = JSON.parseObject(str);
		if(obj1.containsKey("access_token")){
			String access_token = obj1.getString("access_token");
			String url2 = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ access_token +"&type=jsapi";
			String str2 = HttpUtil.get(url2,null,null);
			System.out.println(str2);
			JSONObject obj2 = JSON.parseObject(str2);
			if(obj2.containsKey("ticket")){
				ticket =  obj2.getString("ticket");
				/**
				 * noncestr=Wm3WZYTPz0wzccnW
jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg
timestamp=1414587457
url=http://mp.weixin.qq.com?params=value
				 */
				String n = StringUtil.randomStr(10);
				String timestampe = String.valueOf(System.currentTimeMillis());
				String tt = "jsapi_ticket="+ticket+"&noncestr="+n+"&timestamp="+timestampe+"&url="+url;
				String sign = SHA1.encode(tt);
				v.setNoncestr(n);
				v.setSign(sign);
				//v.setTicket(ticket);
				v.setTimestamp(timestampe);
				v.setUrl(url);
			}
		}
		return v;
	}
	
	/**
	 * 检查是否关注
	 * @return
	 */
	private int checkSub(String openId){
		int c = 0;
		String turl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APPID+"&secret="+SECRET;
		String str = HttpUtil.get(turl,null,null);
		System.out.println("token:" + str);
		JSONObject obj1 = JSON.parseObject(str);
		if(obj1.containsKey("access_token")){
			String access_token = obj1.getString("access_token");
			String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+ access_token +"&openid="+ openId +"&lang=zh_CN";
			String str2 = HttpUtil.get(url,null,null);
			System.out.println("user:" + str2);
			JSONObject obj2 = JSON.parseObject(str2);
			if(obj2.containsKey("subscribe")){
				c = obj2.getIntValue("subscribe");
			}
		}
		return c;
	}
	
	
//	public static void main(String[] args) {
//		String str = "jsapi_ticket=HoagFKDcsGMVCIY2vOjf9hzkZufvk3_M1GjAeE6G_bM7I3AZrscMeShLgyMxEpMQoNQEF7OVQj-ExFEeMhugmQ&noncestr=GK5lC3627D&timestamp=1516278161&url=http://game.520ban.com?id=123";
//		System.out.println(SHA1.encode(str));
//		//339b5f250fb3ca883b763fc5d4fc812a1228d58b
//		//339b5f250fb3ca883b763fc5d4fc812a1228d58b
//		/**
//		 * 0f9de62fce790f9a083d5c99e95740ceb90c27ed
//		 * 0f9de62fce790f9a083d5c99e95740ceb90c27ed
//		 */
//	}
	
	/**
	 * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx00c48cc6dad5768a&secret=a2bd53e4d03efec1e2b797c88a6a9437
	 * scope	是	应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo 
	 * （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）
	 * @param code
	 * @return
	 */
	//https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx00c48cc6dad5768a&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE
	//https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx00c48cc6dad5768a&redirect_uri=http://back.520ban.com/createUserAndLoginFlow&response_type=code&scope=snsapi_userinfo
	//https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842
	public UserStatus createUserAndLoginFlow(String code,String state){ //微信授权后返回code
		//获取token和openId
		UserStatus us = new UserStatus("ERROR"); 
		String str = HttpUtil.get("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+APPID+"&secret="+SECRET+"&code="+code+"&grant_type=authorization_code", null, null);
		System.out.println(str);
		/**
		 * 正确的返回值
		 * { "access_token":"ACCESS_TOKEN",
"expires_in":7200,
"refresh_token":"REFRESH_TOKEN",
"openid":"OPENID",
"scope":"SCOPE" }
		 */
		JSONObject obj1 = JSON.parseObject(str);
		System.out.println("\n\n");
		if(obj1.containsKey("access_token")){ //获取token
			String access_token = obj1.getString("access_token");
			String openId = obj1.getString("openid");
			String refresh_token =  obj1.getString("refresh_token");
			
			//获取用户信息
			String userStr = HttpUtil.get("https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openId+"&lang=zh_CN",null,null);
			System.out.println(userStr);
			JSONObject user = JSON.parseObject(userStr);
			/**
			 * {    "openid":" OPENID",
" nickname": NICKNAME,
"sex":"1",
"province":"PROVINCE"
"city":"CITY",
"country":"COUNTRY",
"headimgurl":    "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
"privilege":[ "PRIVILEGE1" "PRIVILEGE2"     ],
"unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
}
			 */
			if(user.containsKey("openid")){ //正确获取用户信息
				String openid = user.getString("openid");
				String nickName = user.getString("nickname");
				String province = user.getString("province");
				String city = user.getString("city");
				String country = user.getString("country");
				String sex = user.getString("sex");
				String headimgurl = user.getString("headimgurl");
				String unionid = user.getString("unionid");
				System.out.println("unionid：" + unionid);
				UserEntity entity = userMapper.getUserEntityByOpenId(openid);
				if(entity == null){
					entity = createUser(openId, nickName, headimgurl);
				}
				System.out.println("############run here################");
				UserVO v = buildUserVO(entity);
				//String token = UUID.randomUUID().toString();
				String token = entity.getId();
				v.setToken(token);
				stringRedisTemplate.boundValueOps(token).set(JSON.toJSONString(v), 2, TimeUnit.HOURS);
				us.setMeno("SUCCESS");
				us.setToken(token);
				String idUser = entity.getId();
				registAsyncTask(() -> {
					if(state != null){
						userRecommendService.saveUserRecommendEntity(idUser, state);
					}
				});
				
			}
			
			
		}
		return us;
	}
	
	
	
	public UserVO getUserVOByToken(String token){
		String str =  stringRedisTemplate.boundValueOps(token).get();
		if(str != null){
			return JSON.parseObject(str, UserVO.class);
		}else{
			return null;
		}
	}
	
	public UserEnergyVO getUserEnergyByUserAndGoodsId(String userId,String goodsId){
		UserEnergyEntity entity = userEnergyMapper.getUserEnergyEntity(userId, goodsId);
		UserEnergyVO vo = new UserEnergyVO();
		vo.setGoodsId(goodsId);
		vo.setEnergy(0);
		if(entity != null){
			vo.setEnergy(entity.getEnergy());
		}
		return vo;
	}
	
	public boolean updateUserGold(String userId){
		String s = DateUtil.dateToStringY(new Date());
		UserShellRecordEntity e = userShellRecordMapper.getUserShellRecordEntityBy(userId, s);
		UserEntity user = userMapper.getUserEntityById(userId);
		if(e == null && user != null){
			e = new UserShellRecordEntity();
			e.setId(generatID());
			e.setShellTime(s);
			e.setUserId(userId);
			userShellRecordMapper.saveUserShellRecordEntity(e);
			
			user.setUtime(new Date());
			user.setGoldNumber(user.getGoldNumber() + 20);
			userMapper.updateUserEntity(user);
			
			//更新缓存
			UserVO userVO = getUserVOByToken(user.getId());
			System.out.println("用户分析送金币UserVO:"+JSON.toJSONString(userVO));
			if(userVO != null){
				userVO.setGold(user.getGoldNumber());
				stringRedisTemplate.boundValueOps(user.getId()).set(JSON.toJSONString(userVO), 2, TimeUnit.HOURS);
			}
			System.out.println("#用户分析送金币userId:" + userId + " date: " + s);
			return true;
		}else{
			return false;
		}
	}
	
	
	public int getOnlineUser(){
		Random r = new Random();
		int i = r.nextInt(50);
		return 100 + i;
	}
	
	@Deprecated
	public UserStatus createUserAndLoginFlowTest(){
		UserStatus us = new UserStatus("ERROR"); 
		UserEntity entity = userMapper.getUserEntityByOpenId("xxxxx");
		if(entity == null){
			entity = createUser("xxxxx", "test", "dsad.jpg");
		}
		UserVO v = buildUserVO(entity);
		String token = UUID.randomUUID().toString();
		v.setToken(token);
		stringRedisTemplate.boundValueOps(token).set(JSON.toJSONString(v), 2, TimeUnit.HOURS);
		us.setMeno("SUCCESS");
		us.setToken(token);
		return us;
	}
	
	@Transactional
	public UserEntity createUser(String openId,String name,String image){
		UserEntity entity = new UserEntity();
		entity.setId(generatID());
		entity.setOpenId(openId);
		entity.setImage(image);
		entity.setName(name);
		entity.setGoldNumber(GOLD);
		entity.setCtime(new Date());
		entity.setUtime(new Date());
		userMapper.saveUserEntity(entity);
		
		//创建用户等级概率相关
		UserLevelEntity ue = new UserLevelEntity();
		ue.setCtime(new Date());
		ue.setId(generatID());
		ue.setLevel(1);
		ue.setPayCount(0);
		ue.setProbability(3); 
		ue.setUserId(entity.getId());
		ue.setBizhong(0);
		ue.setUtime(new Date());
		userLevelMapper.saveUserLevelEntity(ue);
		
		return entity;
	}
	
//	public UserVO login(String openId){
//		UserEntity entity = userMapper.getUserEntityByOpenId(openId);
//		if(entity == null){
//			throw new GeneralBusinessException("未登陆");
//		}
//		//放缓存
//		return buildUserVO(entity);
//	}
	
	private UserVO buildUserVO(UserEntity entity){
		UserVO vo = new UserVO();
		vo.setGold(entity.getGoldNumber());
		vo.setId(entity.getId());
		vo.setName(entity.getName());
		vo.setUrl(entity.getImage());
		vo.setSubscribe(checkSub(entity.getOpenId()));
		
		return vo;
	}
}
