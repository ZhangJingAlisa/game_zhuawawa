package com.zhua.game.service.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhua.game.entity.GoodsEntity;

public interface GoodsMapper {

	public void saveGoodsEntity(GoodsEntity entity);
	
	public GoodsEntity getGoodsEntityById(@Param("id") String id);
	
	public List<GoodsEntity> listGoodsEntityByTypeAll(@Param("type") int type);

	public List<GoodsEntity> listGoodsEntityByPage(@Param("type") int type,
			@Param("status") int status,@Param("start") int start,@Param("rows") int rows);
	
	public void updateGoodsStatus(@Param("id") String id,@Param("status") int status);
	
	public void deleteGoodsById(@Param("id") String id);
}
