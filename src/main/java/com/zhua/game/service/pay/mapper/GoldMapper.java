package com.zhua.game.service.pay.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhua.game.entity.GoldEntity;

public interface GoldMapper {

	public List<GoldEntity> listGoldEntity();
	
	public GoldEntity getGoldEntityById(@Param("id") String id);
	
}
