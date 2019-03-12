package com.zhua.game.service.user.mapper;

import org.apache.ibatis.annotations.Param;

import com.zhua.game.entity.UserLevelEntity;

public interface UserLevelMapper {

	 public void saveUserLevelEntity(UserLevelEntity entity);
	 
	 public void updateUserLevelEntity(UserLevelEntity entity);
	
	 public UserLevelEntity getUserLevelEntityByUserId(@Param("userId") String userId);
	 
	 
}
