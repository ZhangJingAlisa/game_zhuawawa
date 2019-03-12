package com.zhua.game.service.user.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zhua.game.base.BaseService;
import com.zhua.game.entity.UserBonusEntity;
import com.zhua.game.entity.UserEntity;
import com.zhua.game.entity.UserRecommendEntity;
import com.zhua.game.service.user.mapper.UserBonusMapper;
import com.zhua.game.service.user.mapper.UserMapper;
import com.zhua.game.service.user.mapper.UserRecommendMapper;
import com.zhua.game.service.user.vo.UserBonusVO;
import com.zhua.game.service.user.vo.UserRecommendVO;

@Service
public class UserRecommendService extends BaseService {

	@Autowired
	private UserRecommendMapper userRecommendMapper;
	
	@Autowired
	private UserBonusMapper userBonusMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	
	@Transactional
	public void saveUserRecommendEntity(String userId,String recommendUserId){//userId 新建立的用户    recommendUserId 推荐者
		System.out.println("用户推荐# 被调用 recommendUserId:" + recommendUserId);
		UserRecommendEntity rentity = userRecommendMapper.getUserRecommendEntityByRecommendUserIdAndUserId(recommendUserId,userId);
		System.out.println("UserRecommendEntity:" + JSON.toJSONString(rentity));
		if(rentity == null){
			UserRecommendEntity entity = new UserRecommendEntity();
			entity.setCtime(new Date());
			entity.setId(generatID());
			entity.setRecommendUserId(recommendUserId);
			entity.setUserId(userId);
			entity.setUtime(new Date());
			userRecommendMapper.saveUserRecommendEntity(entity);
			System.out.println("用户推荐# 被保存");
		}
	}
	
	public void updateUserRecommendBonus(String userId,int money,String payUserName){
		System.out.println("#调用推荐分成");
		UserRecommendEntity rentity = userRecommendMapper.getUserRecommendEntityByRecommendUserId(userId);
		if(rentity != null){
			UserEntity ue = userMapper.getUserEntityById(rentity.getUserId());
			if(ue != null){
				int fengcheng = money * 5 / 100; //5%提成
				UserBonusEntity ub = new UserBonusEntity();
				ub.setCtime(new Date());
				ub.setId(generatID());
				ub.setMoney(fengcheng);
				ub.setStatus(0);
				ub.setUserId(userId);
				ub.setUtime(new Date());
				ub.setPayUserName(payUserName);
				ub.setPayMoney(money);
				userBonusMapper.saveUserBonusEntity(ub);
			}
		}
	}
	
	
	//public UserBonusMapper
	
	public UserRecommendVO queryUserRecommendVOByUserId(String userId){
		UserRecommendVO vo = new UserRecommendVO();
		UserRecommendVO vos = userRecommendMapper.queryUserRecommendVOByUserId(userId);
		return vos != null ? vos : vo;
	}
	
	public List<UserRecommendVO> queryUserRecommendRanking(){
		return userRecommendMapper.queryUserRecommendRanking();
	}
	
	
	public List<UserBonusVO> queryUserBonusVO(String userId,int start,int rows){
		List<UserBonusVO> rlist = new ArrayList<>();
		List<UserBonusEntity> list =  userBonusMapper.listUserBonusEntity(userId, start, rows);
		if(list != null && list.size() > 0){
			rlist = list.stream().map(this::toUserBonusVO).collect(Collectors.toList());
		}
		return rlist;
	}
	
	public void applyForBonus(String userId){
		int money = userBonusMapper.getBonusByStatus(userId, 0);
		if(money >= 20000){
			userBonusMapper.updateBonus(userId, 0, 1);
		}
	}
	
	private UserBonusVO toUserBonusVO(UserBonusEntity ub){
		UserBonusVO vo = new UserBonusVO();
		vo.setCtime(ub.getCtime());
		vo.setMoney(ub.getMoney());
		vo.setPayMoney(ub.getPayMoney());
		vo.setPayUserName(ub.getPayUserName());
		return vo;
	}
	
	
}
