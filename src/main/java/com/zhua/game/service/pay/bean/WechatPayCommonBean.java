package com.zhua.game.service.pay.bean;
/**
 * 微信统一下单请求bean
 * @author liuyijiang
 *
 */
public class WechatPayCommonBean {

	
    /**
     * 以下为微信支付表单的所有参数
     * 注释掉的为非必填
     */

	private String appid; //微信分配的小程序ID 
	private String mch_id; //微信支付分配的商户号
	private String device_info; //终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
	private String nonce_str;//随机字符串，不长于32位。推荐随机数生成算法
	private String sign;
	private String sign_type;//签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
	private String body;//商品简单描述，该字段须严格按照规范传递，具体请见参数规定
	//private String detail;//商品详情 单品优惠字段(暂未上线)
	//private String attach;//附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
	private String out_trade_no;//商户订单号 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
	private String fee_type; //CNY 符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	private int    total_fee;//订单总金额，单位为分，详见支付金额
	private String spbill_create_ip;//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
	private String time_start;//订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
    private String time_expire;//订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则注意：最短失效时间间隔必须大于5分钟
    //private String goods_tag;//商品标记，代金券或立减优惠功能的参数，说明详见代金券或立减优惠
    private String notify_url;//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
    private String trade_type;//JSAPI 小程序取值如下：JSAPI，详细说明见参数规定
    //private String limit_pay;//no_credit 	no_credit--指定不能使用信用卡支付
    private String openid;//trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getDevice_info() {
		return device_info;
	}
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public int getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	public String getTime_start() {
		return time_start;
	}
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}
	public String getTime_expire() {
		return time_expire;
	}
	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
    
    

}
