package com.zhua.game.service.pay.mapper;

import org.apache.ibatis.annotations.Param;

import com.zhua.game.entity.UserGoldPayEntity;

public interface UserGoldPayMapper {

	public void saveUserGoldPayEntity(UserGoldPayEntity entity);
	
	public UserGoldPayEntity getUserGoldPayEntityByCode(@Param("code") String code);
	
	public void updateUserGoldPayEntity(UserGoldPayEntity entity);
	
	public int countUserGoldPayEntityByUserId(@Param("userId") String userId);
	
	
}
