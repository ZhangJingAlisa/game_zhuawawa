package com.zhua.game.base;

import com.zhua.game.service.user.vo.UserVO;

public abstract class GameBaseController {

    private static final ThreadLocal<UserVO> LOCAL = new ThreadLocal<UserVO>();
	
	public UserVO getUserVO(){
		return LOCAL.get();
	}
	
	public void setUserVO(UserVO vo ){
		LOCAL.set(vo);
	}
	
	public void clean(){
		LOCAL.remove();
	}
}
