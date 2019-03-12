package com.zhua.game.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.izhaowo.code.base.view.ResultText;
import com.izhaowo.code.spring.plus.interceptor.account.LoginRequired;
import com.zhua.game.base.GameBaseController;
import com.zhua.game.service.pay.service.GoldService;
import com.zhua.game.service.pay.service.PayService;
import com.zhua.game.service.pay.vo.GoldVO;
import com.zhua.game.service.pay.vo.PayVO;
import com.zhua.game.service.user.vo.UserVO;

@RestController
public class GoldController extends GameBaseController {

	@Autowired
	private GoldService goldService;
	
	@Autowired
	private PayService payService;
	 
	/**
	 * 金币购买列表
	 * 需要加缓存
	 * TODO
	 * @return
	 */
	@RequestMapping(value="/queryGoldAll")
	public List<GoldVO> queryGoldALl(){
		return goldService.queryGoldALl();
	}
	
	@RequestMapping(value="/payGold")
	public PayVO payGold(@RequestParam(value="goldId") String goldId){
		UserVO vo = getUserVO();
		return payService.payGold(vo.getId(), goldId);
	}
	
	/**
	 * 查询支付状态
	 * @param code
	 * @return
	 */
	@RequestMapping(value="/queryGoldPayStatus")
	public boolean queryGoldPayStatus(@RequestParam(value="code") String code){
		return payService.getUserGoldPayEntityByCode(code);
	}
	
	@LoginRequired(request=false)
	@RequestMapping(value="/payGoldcallback")
	public ResultText callback(HttpServletRequest request){
		System.out.println("RUN -here - yeyeyeyeyeye!!!!");
		return new ResultText(payService.callBack(request));
	}
}
