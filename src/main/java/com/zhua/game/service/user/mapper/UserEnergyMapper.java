package com.zhua.game.service.user.mapper;

import org.apache.ibatis.annotations.Param;

import com.zhua.game.entity.UserEnergyEntity;

public interface UserEnergyMapper {

	public void saveUserEnergyEntity(UserEnergyEntity entity);
	
	public void updateUserEnergyEntity(UserEnergyEntity entity);
	
	public UserEnergyEntity getUserEnergyEntity(@Param("userId")String userId,@Param("goodsId") String goodsId);
	
}
