package com.zhua.game.service.user.mapper;

import org.apache.ibatis.annotations.Param;

import com.zhua.game.entity.UserEntity;

public interface UserMapper {

	public void saveUserEntity(UserEntity entity);
	
	public UserEntity getUserEntityById(@Param("id") String id);
	
	public UserEntity getUserEntityByOpenId(@Param("openId") String openId);
	
	public void updateUserEntity(UserEntity entity);
	
	public int checkUser(@Param("userId") String userId);
	
}
