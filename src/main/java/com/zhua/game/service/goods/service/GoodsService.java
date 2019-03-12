package com.zhua.game.service.goods.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhua.game.base.BaseService;
import com.zhua.game.entity.GoodsEntity;
import com.zhua.game.service.goods.bean.GoodsType;
import com.zhua.game.service.goods.mapper.GoodsMapper;
import com.zhua.game.service.goods.vo.GoodsTypeVO;
import com.zhua.game.service.goods.vo.GoodsVO;

@Service
public class GoodsService extends BaseService {

	@Autowired
	private GoodsMapper goodsMapper;
	
	//缓存
	public List<GoodsTypeVO> queryGoodsByAll(){
		List<GoodsTypeVO> list = new ArrayList<>();
		Stream<GoodsType> types =  Stream.of(GoodsType.values());
		types.forEach(type -> {
			GoodsTypeVO vo = new GoodsTypeVO();
			vo.setTypeName(type.getShow());
			vo.setTypeNumber(type.getId());
			vo.setGoods(queryGoodsVOList(type.getId()));
			list.add(vo);
		});
		return list;
	}
	
	public List<GoodsVO> queryGoodsVOList(int type){
		return goodsMapper.listGoodsEntityByTypeAll(type).stream().map(this::buildGoodsVO).collect(Collectors.toList());
	}
	
	private GoodsVO buildGoodsVO(GoodsEntity entity){
		GoodsVO vo = new GoodsVO();
		vo.setGold(entity.getGold());
		vo.setId(entity.getId());
		vo.setImage(entity.getImage());
		vo.setMemo(entity.getMemo());
		vo.setName(entity.getName());
		vo.setTag(entity.getTag());
		vo.setType(entity.getType());
		vo.setShowImage(entity.getShowImage());
		vo.setPrice(entity.getPrice());
		return vo;
	}
	
	
	public void createGoodEntity(String name,String tag,int type, int gold,
			int price,String memo,//描述材质
			int probability, String buy,String image,String showImage){
		GoodsEntity entity = new GoodsEntity();
		entity.setBuy(buy);
		entity.setCtime(new Date());
		entity.setGold(gold);
		entity.setId(generatID());
		entity.setImage(image);
		entity.setMemo(memo);
		entity.setName(name);
		entity.setPrice(price);
		entity.setProbability(probability);
		entity.setShowImage(showImage);
		entity.setStatus(0);
		entity.setTag(tag);
		entity.setType(type);
		entity.setUtime(new Date());
		goodsMapper.saveGoodsEntity(entity);
	}
	
	
}
