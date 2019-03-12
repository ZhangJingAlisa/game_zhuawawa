package com.zhua.game.service.user.mapper;

import org.apache.ibatis.annotations.Param;

import com.zhua.game.entity.UserAddressEntity;

public interface UserAddressMapper {

	public void saveUserAddressEntity(UserAddressEntity entity);
	
	public void updateUserAddressEntity(UserAddressEntity entity);
	
	public UserAddressEntity getUserAddressEntityByUserId(@Param("userId") String userId);
}
