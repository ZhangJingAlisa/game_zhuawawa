package com.zhua.game.service.user.mapper;

import org.apache.ibatis.annotations.Param;

import com.zhua.game.entity.UserShellRecordEntity;

public interface UserShellRecordMapper {

	public UserShellRecordEntity getUserShellRecordEntityBy(@Param("userId")String userId,@Param("shellTime") String shellTime);
	
	public void saveUserShellRecordEntity(UserShellRecordEntity entity);
	
}
