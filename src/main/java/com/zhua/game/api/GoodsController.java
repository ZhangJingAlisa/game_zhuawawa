package com.zhua.game.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhua.game.base.GameBaseController;
import com.zhua.game.service.goods.service.GoodsService;
import com.zhua.game.service.goods.vo.GoodsTypeVO;
/**
 * 
 * @author liuyijiang
 *
 */
@RestController
public class GoodsController extends GameBaseController {

	@Autowired
	private GoodsService goodsService;
	
	/**
	 * 查询商品列表
	 * 需要加缓存
	 * OK
	 * @return
	 */
	@RequestMapping(value="/queryGoodsByAll")
	public List<GoodsTypeVO> queryGoodsByAll(){
		return goodsService.queryGoodsByAll();
	}
}
