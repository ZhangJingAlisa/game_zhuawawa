package com.zhua.game.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.izhaowo.code.spring.plus.interceptor.account.LoginRequired;
import com.zhua.game.base.GameBaseController;
import com.zhua.game.entity.GoodsEntity;
import com.zhua.game.service.goods.mapper.GoodsMapper;
import com.zhua.game.service.goods.service.GoodsService;

@RestController
public class BackgroundController extends GameBaseController {

	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private GoodsMapper goodsMapper;
	
	@LoginRequired(request=false)
	@RequestMapping(value="/createGoods")
	public boolean createGoods(@RequestParam("name") String name,
			@RequestParam("tag") String tag,//规格
			@RequestParam("type") int type, //0精品 1彩妆 2饰品
			@RequestParam("gold") int gold,//需要金币
			@RequestParam("price") int price,//价格
			@RequestParam("memo") String memo,//描述材质
			@RequestParam("probability") int probability,//基础概率
			@RequestParam(value="showImage",required=false) String showImage,
			@RequestParam(value="image",required=false) String image,
			@RequestParam("buy") String buy){ //淘宝链接
		goodsService.createGoodEntity(name, tag, type, gold, price, memo, probability, buy, image, showImage);
	    return true;
	}
	
//	private String id;
//	private String name; 
//	private String tag;   //规格
//	private int    type;	//0精品 1彩妆 2饰品
//	private int    gold;    //需要金币
//	private int    price;   //价格
//	private String memo;   //描述材质
//	private String image;   //图片
//	private String showImage; //展示图片
//	private int    probability;
//	private int    status;//0 上架  1下架 
//	private String buy;
	
	@LoginRequired(request=false)
	@RequestMapping(value="/queryGoodsEntity")
	public List<GoodsEntity> queryGoodsEntity(@RequestParam(value="type",required=false,defaultValue="-1") int type,//-1所有 0精品 1彩妆 2饰品
			@RequestParam(value="status",required=false,defaultValue="-1") int status,//-1所有 0上架 1下架
			@RequestParam(value="start",required=false,defaultValue="0") int start,
			@RequestParam(value="rows",required=false,defaultValue="500") int rows){
		return goodsMapper.listGoodsEntityByPage(type, status, start, rows);
	}
	
	@LoginRequired(request=false)
	@RequestMapping(value="/updateGoodsStatus")
	public boolean updateGoodsStats(@RequestParam(value="id") String id,
			@RequestParam(value="status",required=false,defaultValue="-1") int status){
		goodsMapper.updateGoodsStatus(id, status);
		return true;
	}
	
	@LoginRequired(request=false)
	@RequestMapping(value="/deleteGoods")
	public boolean deleteGoods(@RequestParam(value="id") String id){
		goodsMapper.deleteGoodsById(id);
		return true;
	}
}
