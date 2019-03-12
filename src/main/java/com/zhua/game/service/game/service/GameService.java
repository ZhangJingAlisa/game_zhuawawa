package com.zhua.game.service.game.service;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.izhaowo.code.base.exception.GeneralBusinessException;
import com.izhaowo.code.base.utils.DateUtil;
import com.zhua.game.base.BaseService;
import com.zhua.game.entity.GameRuleEntity;
import com.zhua.game.entity.GameTokenEntity;
import com.zhua.game.entity.GoodsEntity;
import com.zhua.game.entity.UserBaseProbabilityEntity;
import com.zhua.game.entity.UserEnergyEntity;
import com.zhua.game.entity.UserEntity;
import com.zhua.game.entity.UserLevelEntity;
import com.zhua.game.service.game.mapper.GameRuleMapper;
import com.zhua.game.service.game.mapper.GameTokenMapper;
import com.zhua.game.service.goods.mapper.GoodsMapper;
import com.zhua.game.service.order.service.OrderService;
import com.zhua.game.service.pay.mapper.UserGoldPayMapper;
import com.zhua.game.service.user.mapper.UserBaseProbabilityMapper;
import com.zhua.game.service.user.mapper.UserEnergyMapper;
import com.zhua.game.service.user.mapper.UserLevelMapper;
import com.zhua.game.service.user.mapper.UserMapper;
import com.zhua.game.service.user.service.UserService;
import com.zhua.game.service.user.vo.UserVO;

@Service
public class GameService extends BaseService {

	
	@Autowired
	private GoodsMapper goodsMapper;
	
	@Autowired
	private GameRuleMapper gameRuleMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired 
	private GameTokenMapper gameTokenMapper;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate; 
	
	@Autowired
	private UserLevelMapper userLevelMapper;
	
	@Autowired
	private UserEnergyMapper userEnergyMapper;
	
	@Autowired
	private UserBaseProbabilityMapper userBaseProbabilityMapper;
	
	@Autowired
	private UserGoldPayMapper userGoldPayMapper;

	@Transactional
	public String getStartToken(String goodsId,String userId,String userToken){
		GoodsEntity goods = goodsMapper.getGoodsEntityById(goodsId);
		if(goods == null){
			throw new GeneralBusinessException("异常");
		}
		UserEntity user = userMapper.getUserEntityById(userId);
		if(user.getGoldNumber() < goods.getGold()){
			throw new GeneralBusinessException("金币不足");
		}
		user.setGoldNumber(user.getGoldNumber() - goods.getGold());
		GameTokenEntity token = initGameTokenEntity(user.getId(), goodsId);
		gameTokenMapper.saveGameTokenEntity(token);
		userMapper.updateUserEntity(user);
		//更新缓存
		UserVO userVO = userService.getUserVOByToken(userToken);
		if(userVO != null){
			userVO.setGold(user.getGoldNumber());
			stringRedisTemplate.boundValueOps(userToken).set(JSON.toJSONString(userVO), 2, TimeUnit.HOURS);
		}
		return token.getId();
	}
	
	@Transactional
	public int gameAction(String gameTokenId,String userId){
		GameTokenEntity token = gameTokenMapper.getGameTokenEntity(gameTokenId);
		GoodsEntity goods = goodsMapper.getGoodsEntityById(token.getGoodsId());
		if(token == null || token.getStatus() != 0 || !token.getUserId().equals(userId)){
			throw new GeneralBusinessException("异常");
		}
		if(goods == null){
			throw new GeneralBusinessException("异常");
		}
		token.setStatus(1);
		gameTokenMapper.updateGameTokenEntity(token);
		Random r = new Random();
		int base = goods.getProbability();
		System.out.println("商品基础概率" + base);
		int i = r.nextInt(base);
//		if(goods.getType() == 1){ //彩妆 中奖率增加100
//			i = 
//		}
		System.out.println("randon 值：" + i + " userId:" + userId);
		if(i < getProbability(userId,goods.getId(),goods.getType(),goods.getPrice(),base)){ //获奖 3 100  (String userId,String goodsId,int type,int price
			System.out.println("#用户中奖userId：" + userId +" " + goods.getName() + " " + " time:" + DateUtil.dateToString(new Date()));
			//生成订单
			orderService.saveOrderEntity(userId, goods.getId(), goods.getImage(), goods.getName(),goods.getTag(),goods.getType());
			//更新强力值
			UserEnergyEntity ue = userEnergyMapper.getUserEnergyEntity(userId, goods.getId()); 
			if(ue != null){
				ue.setEnergy(0);
				userEnergyMapper.updateUserEnergyEntity(ue);
			}
			return 100;	
		}else{ //未
			//随机加能量
			if(goods.getType() != 1){
				UserEnergyEntity ue = userEnergyMapper.getUserEnergyEntity(userId, goods.getId());
				if(ue == null){
					ue = new UserEnergyEntity();
					ue.setCtime(new Date());
					ue.setEnergy(r.nextInt(2));
					ue.setGoodsId(goods.getId());
					ue.setId(generatID());
					ue.setUserId(userId);
					ue.setUtime(new Date());
					userEnergyMapper.saveUserEnergyEntity(ue);
				}else{
					ue.setEnergy(ue.getEnergy() + r.nextInt(2) );
					ue.setUtime(new Date());
					userEnergyMapper.updateUserEnergyEntity(ue);
				}
			}else{
				System.out.println("#抓彩妆没有能量");
			}
			return 90;	
		}
	}
	
//	public static void main(String[] args) {
//		Random r = new Random();
//		while(true){
//			int i = r.nextInt(100);
//			System.out.println(i);
//			if(i==0){
//				System.out.println(i);
//				break;
//			}
//		}
//	}
	
	private boolean checkUser(String userId){
		return userMapper.checkUser(userId) != 0; //没有中彩妆的用户
	}
	
	
	/**
	 * 获取获奖概率 
	 * @return
	 */
	private int getProbability(String userId,String goodsId,int type,int price,int base){
		System.out.println("---run getProbability---- ");
		if(checkUser(userId)){ //中了彩妆的所有都抓不住了
			System.out.println("用户已经中了彩妆：userId:" + userId  + " 概率-1 逼不中");
			return -1; //不中特殊用户
		}
		
		int baseStat = 0;
		//
		//用户充值总金额大于 商品金额 才有机会抓中（彩妆）
		if(type == 1 || type == 2){
			int ccc = userGoldPayMapper.countUserGoldPayEntityByUserId(userId); //获取用户充值总金额  //0精品 1彩妆 2饰品
			System.out.println("用户充值：" + (ccc / 100) + "商品金额：" + price);
			if((ccc / 100 ) <= price){
				System.out.println("用户充值金额不够 绝对不中：userId:" + userId + " goodsId :" + goodsId);
				return -1; //绝对不中
			}else{
				System.out.println("满足彩妆条件：userId:" + userId + " goodsId :" + goodsId + "概率1%");
				return 1; //抓彩妆满足条件 只有1%的概率
			}
		}
		//一个概率的基础值
		
		
		//计算能量值 如果满了100 就必中
		UserEnergyEntity ue = userEnergyMapper.getUserEnergyEntity(userId, goodsId); //如果能量满了 就必中
		if(ue != null && ue.getEnergy() == base && type != 1){
//			ue.setEnergy(0);
//			userEnergyMapper.updateUserEnergyEntity(ue);
			return base;
		}
		
		//强力值出来
//		if(ue != null ){
//			baseStat = baseStat + ue.getEnergy();
//		}
		
		UserBaseProbabilityEntity pp = userBaseProbabilityMapper.getUserBaseProbabilityEntity();
		if(pp != null){
			baseStat = baseStat + pp.getProbability(); //用于活动期间的 增加用户抓到的几率  后台控制
		}
		
		
		
		
		//获取用户抓取的概率
		UserLevelEntity entity = userLevelMapper.getUserLevelEntityByUserId(userId);
		if(entity != null){
			//查看用户是否有必中的概率
			if(entity.getBizhong() != 0){ //必中值不为0
				Random r = new Random();
				int i = r.nextInt(2);
				if(i == 1){ //有一定几率触发必中
					entity.setBizhong(0); //清空必中值
					entity.setUtime(new Date());
					userLevelMapper.updateUserLevelEntity(entity);
					baseStat = 100;
				}else{
					baseStat =  baseStat + entity.getProbability(); 
				}
			}else{
				baseStat =  baseStat + entity.getProbability();
			}
		}
		System.out.println("#user probability:"+baseStat);
		if(baseStat >= base){
			return base;
		}else{
			return baseStat;
		}
		
	}
	
//	public static void main(String[] args) {
//		Random r = new Random();
//		int i = r.nextInt(2);
//		System.out.println(i); 
//	}
	
	public String getGameRule(){
		GameRuleEntity entity = gameRuleMapper.getGameRuleEntity();
		return entity.getRule();
	}
	
	
	public boolean gameTokenFail(String gameTokenId,String userId){
		GameTokenEntity token = gameTokenMapper.getGameTokenEntity(gameTokenId);
		if(token != null && token.getUserId().equals(userId)){
			token.setStatus(1);
			gameTokenMapper.updateGameTokenEntity(token);
		}
		return true;
	}
	
	
	private GameTokenEntity initGameTokenEntity(String userId,String goodsId){
		GameTokenEntity entity = new GameTokenEntity();
		entity.setCtime(new Date());
		entity.setGoodsId(goodsId);
		entity.setId(generatID());
		entity.setStatus(0);
		entity.setUserId(userId);
		entity.setUtime(new Date());
		return entity;
	}
	
}
