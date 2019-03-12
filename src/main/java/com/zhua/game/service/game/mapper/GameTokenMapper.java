package com.zhua.game.service.game.mapper;

import com.zhua.game.entity.GameTokenEntity;

public interface GameTokenMapper {

	public void saveGameTokenEntity(GameTokenEntity entity);
	
	public GameTokenEntity getGameTokenEntity(String id);
	
	public void updateGameTokenEntity(GameTokenEntity entity);
	
}
