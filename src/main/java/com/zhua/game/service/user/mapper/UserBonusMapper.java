package com.zhua.game.service.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhua.game.entity.UserBonusEntity;

public interface UserBonusMapper {

	public void saveUserBonusEntity(UserBonusEntity entity);
	
	public List<UserBonusEntity> listUserBonusEntity(@Param("userId") String userId,@Param("start") int start,@Param("rows") int rows);
	
	public int getBonusByStatus(@Param("userId") String userId,@Param("status") int status);
	
	public void updateBonus(@Param("userId") String userId,@Param("status") int status,@Param("upstatus") int upstatus); 
}
