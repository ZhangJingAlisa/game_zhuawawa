package com.zhua.game.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhua.game.base.GameBaseController;
import com.zhua.game.service.order.service.OrderService;
import com.zhua.game.service.order.vo.OrderVO;
import com.zhua.game.service.user.vo.UserVO;

@RestController
public class OrderController extends GameBaseController {

	@Autowired
	private OrderService orderService;
	
	/**
	 * 订单列表
	 * @param start
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="/queryOrderList")
	public List<OrderVO> queryOrderList(@RequestParam(value="start",defaultValue="0") int start,
			@RequestParam(value="rows",defaultValue="50") int rows){
		UserVO vo = getUserVO();
		return orderService.queryOrderList(vo.getId(), start, rows);
	}
	
	/**
	 * 提取订单
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value="/pickupOrder")
	public boolean pickupOrder(@RequestParam(value="orderId") String orderId,
			@RequestParam(value="address") String address,
			@RequestParam(value="contactName") String contactName,
			@RequestParam(value="contactPhone") String contactPhone){
		UserVO vo = getUserVO();
		List<String> list = Arrays.asList(orderId.split(","));
		orderService.pickupOrderAll(vo.getId(), list, address, contactName, contactPhone);
		return true;
	}
	
	
}