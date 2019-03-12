package com.zhua.game.service.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhua.game.entity.OrderEntity;

public interface OrderMapper {

	public void saveOrderEntity(OrderEntity entity);
	
	public void updateOrderEntity(OrderEntity entity);
	
	public List<OrderEntity> listOrderEntityByUserId(@Param("userId") String userId,
			@Param("start") int start,@Param("rows") int rows);
	
	public OrderEntity getOrderEntityById(@Param("id") String id);
	
	public int countOrderEntityStatusByUserId(@Param("userId") String userId,@Param("status") int status);
	
}
