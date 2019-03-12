package com.zhua.game.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.izhaowo.code.spring.plus.interceptor.account.LoginRequired;
import com.zhua.game.base.GameBaseController;
import com.zhua.game.service.game.service.GameService;
import com.zhua.game.service.user.vo.UserVO;
/**
 * 
 * @author liuyijiang
 *
 */
@RestController
public class GameController extends GameBaseController {

	@Autowired
	private GameService gameService;
	 
	/**
	 * 点击开始游戏的时候先获取游戏token
	 * @param goodsId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/getStartToken")
	public String getStartToken(@RequestParam(value="goodsId") String goodsId){
		UserVO vo = getUserVO();
		return gameService.getStartToken(goodsId, vo.getId(),vo.getToken());
	}
	
	/**
	 * 获取游戏规则
	 * @return
	 */
	@LoginRequired(request=false)
	@RequestMapping(value="/getGameRule")
	public String getGameRule(){
		return gameService.getGameRule();
	}
	
	
	/**
	 * 客户端判断当 用户对准了娃娃的时候调用接口 是否能抓取 否则直接调用fail
	 * @param gameTokenId
	 * @return
	 */
	@RequestMapping(value="/gameAction")
	public int gameAction(@RequestParam(value="gameTokenId") String gameTokenId){
		UserVO vo = getUserVO();
		return gameService.gameAction(gameTokenId, vo.getId());
	}
	
	/**
	 * 客户端判断是否抓到娃娃
	 * @param gameTokenId
	 * @return
	 */
	@RequestMapping(value="/gameTokenFail")
	public boolean gameTokenFail(@RequestParam(value="gameTokenId") String gameTokenId){
		UserVO vo = getUserVO();
		return gameService.gameTokenFail(gameTokenId, vo.getId());
	}
	
}
