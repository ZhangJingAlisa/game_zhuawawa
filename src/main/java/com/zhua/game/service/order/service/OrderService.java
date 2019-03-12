package com.zhua.game.service.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.izhaowo.code.base.exception.GeneralBusinessException;
import com.izhaowo.code.base.utils.AssertUtil;
import com.zhua.game.base.BaseService;
import com.zhua.game.entity.OrderEntity;
import com.zhua.game.entity.UserAddressEntity;
import com.zhua.game.service.order.mapper.OrderMapper;
import com.zhua.game.service.order.vo.OrderVO;
import com.zhua.game.service.user.mapper.UserAddressMapper;

@Service
public class OrderService extends BaseService {

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private UserAddressMapper userAddressMapper;
	
	@Transactional
	public void saveOrderEntity(String userId,String goodsId,String goodsImage,String goodsName,String tag,int type){
		OrderEntity entity = initOrderEntity(userId, goodsId, goodsImage, goodsName);
		entity.setTag(tag);
		entity.setType(type);
		orderMapper.saveOrderEntity(entity);
	}
	
	public void pickupOrderAll(String userId,List<String> orderIds,String address,String contactName,String contactPhone){
		int i = orderMapper.countOrderEntityStatusByUserId(userId, 0);
		if(i < 2){
			throw new GeneralBusinessException("两个起提");
		}
		for(String orderId : orderIds){
			pickupOrder(userId, orderId, address, contactName, contactPhone);
		}
	}
	
	@Transactional
	public void pickupOrder(String userId,String orderId,String address,String contactName,String contactPhone){
		//查询是否有2个以上的未提货的订单 才能提货
//		int i = orderMapper.countOrderEntityStatusByUserId(userId, 0);
//		if(i < 2){
//			throw new GeneralBusinessException("两个起提");
//		}
		OrderEntity entity = orderMapper.getOrderEntityById(orderId);
		if(entity != null && entity.getUserId().equals(userId)){
			UserAddressEntity ua =  userAddressMapper.getUserAddressEntityByUserId(userId);
			if(ua == null){
				ua = new UserAddressEntity();
				ua.setAddress(address);
				ua.setContactName(contactName);
				ua.setContactPhone(contactPhone);
				ua.setCtime(new Date());
				ua.setUserId(userId);
				ua.setUtime(new Date());
				ua.setId(generatID());
				userAddressMapper.saveUserAddressEntity(ua);
			}else{
				ua.setAddress(address);
				ua.setContactName(contactName);
				ua.setContactPhone(contactPhone);
				userAddressMapper.updateUserAddressEntity(ua);
			}
			entity.setAddress(address);
			entity.setContactName(contactName);
			entity.setContactPhone(contactPhone);
			entity.setStatus(1);
			orderMapper.updateOrderEntity(entity);
		}
	}
	
	private OrderEntity initOrderEntity(String userId,String goodsId,String goodsImage,String goodsName){
		OrderEntity entity = new OrderEntity();
		entity.setCtime(new Date());
		entity.setGoodsId(goodsId);
		entity.setGoodsImage(goodsImage);
		entity.setGoodsName(goodsName);
		entity.setId(generatID());
		entity.setStatus(0);
		entity.setUserId(userId);
		entity.setUtime(new Date());
		return entity;
	} 
	
	
	public List<OrderVO> queryOrderList(String userId,int start, int rows){
		List<OrderVO> rlist = new ArrayList<OrderVO>();
		List<OrderEntity> list = orderMapper.listOrderEntityByUserId(userId, start, rows);
		if(!AssertUtil.isNull(list) ){
			rlist = list.stream().map(this::buildOrderVO).collect(Collectors.toList());
		}
		return rlist;
	}
	
	private OrderVO buildOrderVO(OrderEntity entity){
		OrderVO vo = new OrderVO();
		vo.setGoodsId(entity.getGoodsId());
		vo.setGoodsImage(entity.getGoodsImage());
		vo.setGoodsName(entity.getGoodsName());
		vo.setId(entity.getId());
		vo.setStatus(entity.getStatus());
		vo.setUserId(entity.getUserId());
		vo.setContactName(entity.getContactName());
		vo.setContactPhone(entity.getContactPhone());
		vo.setAddress(entity.getAddress());
		return vo;
	}
	
}
