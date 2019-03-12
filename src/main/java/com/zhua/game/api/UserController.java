package com.zhua.game.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.izhaowo.code.spring.plus.interceptor.account.LoginRequired;
import com.zhua.game.base.GameBaseController;
import com.zhua.game.service.user.service.UserAddressService;
import com.zhua.game.service.user.service.UserRecommendService;
import com.zhua.game.service.user.service.UserService;
import com.zhua.game.service.user.vo.UserAddressVO;
import com.zhua.game.service.user.vo.UserBonusVO;
import com.zhua.game.service.user.vo.UserEnergyVO;
import com.zhua.game.service.user.vo.UserRecommendVO;
import com.zhua.game.service.user.vo.UserStatus;
import com.zhua.game.service.user.vo.UserVO;
import com.zhua.game.service.user.vo.WechatToken;

/**
 * 
 * @author liuyijiang
 *
 */
@RestController
public class UserController extends GameBaseController {

	@Autowired
	private UserAddressService userAddressService;
	 
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRecommendService userRecommendService;
	
	@LoginRequired(request=false)
	@RequestMapping(value="/getWechatTokenTicket")
	public WechatToken getWechatTokenTicket(@RequestParam(value="url") String url){
		return userService.getWechatTokenTicket(url);
	}
	
//	public static void main(String[] args) {
//		String str = "promotion#xxx-dsda-faff";
//		System.out.println(str.contains("#"));
//		String[] s = str.split("#");
//		System.out.println(s[0]);
//		System.out.println(s[1]);
//	}
	
	/**
	 * 1 先登陆并创建用户
	 * state	否	重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
	 */
	@LoginRequired(request=false)
	@RequestMapping(value="/createUserAndLoginFlow")
	public void createUserAndLoginFlow(@RequestParam(value="code") String code,
			@RequestParam(value="url",required=false) String url,
			@RequestParam(value="userId",required=false) String userId,
			@RequestParam(value="state",required=false) String state,HttpServletResponse response,HttpServletRequest request){
//		String url = null;
//		String userId = null;
//		if(state != null && state.contains("#")){
//			String[] s = state.split("#");
//			url=s[0];
//			userId=s[1];
//		}else if(state != null){
//			url=state;
//		}
		System.out.println("@@@@request"+ request.getParameter("userId"));
		System.out.println("@@@@@@@userId:" + userId);
		System.out.println("@@@@@@@url:" + url);
		UserStatus status = userService.createUserAndLoginFlow(code,userId);
		try {
			//state={url}#推荐id
			//例如 state=promotion#xx-dsda-faff	
			if(url != null){
				response.sendRedirect("http://game.520ban.com/"+ url +"?token="+status.getToken());
			}else{
				response.sendRedirect("http://game.520ban.com?token="+status.getToken());
			}
			System.out.println("token########################:"+status.getToken());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@LoginRequired(request=false)
	@RequestMapping(value="/test")
	public void test(@RequestParam(value="code") String code,HttpServletResponse response){
		UserStatus status = userService.createUserAndLoginFlowTest();
		try {
			response.sendRedirect("http://www.baidu.com?token="+status.getToken());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 查询用户地址 ok
	 * 需要加缓存 TODO
	 * @return
	 */
	@RequestMapping(value="/getUserAddressByUserId")
	public UserAddressVO getUserAddressByUserId(){
		UserVO vo = getUserVO();
		return userAddressService.getUserAddressByUserId(vo.getId());
	}
	
	/**
	 * 查询用户对应商品能量
	 * @param userId
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value="/getUserEnergyByUserAndGoodsId")
	public UserEnergyVO getUserEnergyByUserAndGoodsId(@RequestParam(value="goodsId") String goodsId){
		UserVO vo = getUserVO();
		return userService.getUserEnergyByUserAndGoodsId(vo.getId(), goodsId);
	}
	
	/**
	 * 查询在线用户
	 * @return
	 */
	@RequestMapping(value="/getOnlineUser")
	public int getOnlineUser(){
		return userService.getOnlineUser();
	}
	
	/**
	 * 获取用户信息
	 * @return
	 */
	@RequestMapping(value="/getUserInfo")
	public UserVO getUserInfo(){
		UserVO vo = getUserVO();
		return vo;
	}
	
	/**
	 * 送金币
	 * @return
	 */
	@RequestMapping(value="/updateUserGold")
	public boolean updateUserGold(){
		UserVO vo = getUserVO();
		return userService.updateUserGold(vo.getId());
	}
	
	
	@RequestMapping(value="/queryUserRecommendVOByUserId")
	public UserRecommendVO queryUserRecommendVOByUserId(){
		UserVO vo = getUserVO();
		return userRecommendService.queryUserRecommendVOByUserId(vo.getId());
	}
	
	@LoginRequired(request=false)
	@RequestMapping(value="/queryUserRecommendRanking")
	public List<UserRecommendVO> queryUserRecommendRanking(){
		return userRecommendService.queryUserRecommendRanking();
	}
	
	@RequestMapping(value="/queryUserBonusVOByPage")
	public List<UserBonusVO> queryUserBonusVOByPage(@RequestParam(value="start",required=false,defaultValue="0") int start,
			@RequestParam(value="rows",required=false,defaultValue="50") int rows){
		UserVO vo = getUserVO();
		return userRecommendService.queryUserBonusVO(vo.getId(), start, rows);
	}
	
	@RequestMapping(value="/applyForBonus")
	public boolean applyForBonus(){
		UserVO vo = getUserVO();
		userRecommendService.applyForBonus(vo.getId());
		return true;
	}
	
//	@LoginRequired(request=false)
//	@RequestMapping(value="/t3")
//	public boolean test3(@RequestParam(value="userId") String userId){
//		userRecommendService.applyForBonus(userId);
//		return true;
//	}
	
//	@LoginRequired(request=false)
//	@RequestMapping(value="/t2")
//	public List<UserBonusVO> test(@RequestParam(value="userId") String userId,
//			@RequestParam(value="start") int start,
//			@RequestParam(value="rows") int rows){
//		return userRecommendService.queryUserBonusVO(userId, start, rows);
//	}
//	
//	@LoginRequired(request=false)
//	@RequestMapping(value="/t1")
//	public UserRecommendVO test(@RequestParam(value="userId") String userId){
//		return userRecommendService.queryUserRecommendVOByUserId(userId);
//	}
	
	
}
