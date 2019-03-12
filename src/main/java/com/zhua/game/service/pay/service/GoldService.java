package com.zhua.game.service.pay.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhua.game.base.BaseService;
import com.zhua.game.entity.GoldEntity;
import com.zhua.game.service.pay.mapper.GoldMapper;
import com.zhua.game.service.pay.vo.GoldVO;

@Service
public class GoldService extends BaseService {

	@Autowired
	private GoldMapper goldMapper;
	
	public List<GoldVO> queryGoldALl(){
		List<GoldEntity> list =  goldMapper.listGoldEntity();
		return list.stream().map(this::buildGoldVO).collect(Collectors.toList());
	}
	
	private GoldVO buildGoldVO(GoldEntity entity){
		GoldVO vo = new GoldVO();
		vo.setGoldNumber(entity.getGoldNumber());
		vo.setId(entity.getId());
		vo.setPrice(entity.getPrice());
		vo.setUrl(entity.getUrl());
		return vo;
	}
}
