package com.zhua.game.service.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhua.game.entity.UserRecommendEntity;
import com.zhua.game.service.user.vo.UserRecommendVO;

public interface UserRecommendMapper {

	public void saveUserRecommendEntity(UserRecommendEntity entity);
	
	public UserRecommendEntity getUserRecommendEntityByRecommendUserId(@Param("recommendUserId") String recommendUserId);
	
	public UserRecommendEntity getUserRecommendEntityByRecommendUserIdAndUserId(@Param("recommendUserId") String recommendUserId,@Param("userId") String userId);
	
	public List<UserRecommendVO>  queryUserRecommendRanking();
	
	public UserRecommendVO queryUserRecommendVOByUserId(@Param("userId") String userId);
}
