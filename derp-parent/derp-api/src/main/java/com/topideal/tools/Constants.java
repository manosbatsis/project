package com.topideal.tools;

/**
 * E贸通门户 常量池
 */
public interface Constants {
	
	/**
	 * 系统常量
	 */
	public static class System {
		
		/**
		 * 用户登录信息保存session名称
		 */
		public static final String USER_SESSION = "LOGIN_USER_INFO";
		/**
		 * 系统保存权限列表信息
		 */
		public static final String ROLEPERMISSIONLIST = "rolePermissionList";
		
		/**
		 * 字符编码
		 */
		public static final String CHARSET_UTF8 = "utf-8";
		
	    /**
	     * 超时信息
	     */
	    public static final String TIME_OUT = "timeout";
	    
	    /**
	     * 成功信息
	     */
	    public static final String SUCCESS = "success";
	    
	    /**
	     * 失败信息
	     */
	    public static final String FAIL = "fail";
	  	
	  	/**
	  	 * xml
	  	 */
	  	public static final String XML = "xml";
		
	  	/**
		 * 币别写死为人民币
		 */
		public static final String CNY = "CNY";
		
		/**
		 * 材料成分为  100
		 */
		public static final int componentRatio = 100;
		
		/**
		 * base64 加密解密  Appsecret
		 */
		public static final  String base64Appsecret = "cc1yefociiv694mrlhk7wDD";
	  	
		/**   
		 * 用于接口 推送 回推 创建单据 的创建者
		 */ 
		public static final String DEFUAULT_CREATE_USER="系统创建";
		
		/**   
		 * 用于接口 推送 回推 创建单据 的修改者 
		 */ 
		public static final String DEFUAULT_EDIT_USER="系统修改";

		/**
		 * 天猫国际发货类型
		 */
		public static final int TIANMAO_SEND_TYPE = 4;
		
		/**
		 * 人民币编码
		 */
		public static final String RMB = "RMB";
	}

	/**
     * 报文类型
     */
	public static class ContentType {
	
		/**  
		 *  
		 */
		public final static String TEXT_XML = "text/xml;charset=utf-8";
		/**  
		 *  
		 */
		public final static String TEXT_HTML = "text/html;charset=utf-8";
		/**  
		 *  
		 */
		public final static String TEXT_PLAIN = "text/plain;charset=utf-8";
		/**  
		 *  
		 */
		public final static String APPLICATION_XML = "application/xml;charset=utf-8";
		/**  
		 *  
		 */
		public final static String APPLICATION_JSON = "application/json;charset=utf-8";
		/**  
		 *  
		 */
		public final static String APPLICATION_FROM = "application/x-www-form-urlencoded;charset=utf-8";
	
	}


	/**
     * 算法类型
     */
	public static class SignType {
	    
	    /**
	     * md5
	     */
	  	public static final String MD5 = "md5";
	  	
	}

	/**
     * Json响应状态
     */
	public static class JsonStatus {

	    /**
	     * 成功
	     */
	    public static final int SUCCESS = 1;
	    
	    /**
	     * 失败
	     */
	    public static final int FAILED = 0;


	}

	/**
     * 响应状态  int类型
     */
	public static class ResponseStatus {
		
		/**
		 * 成功
		 */
		public static final int SUCCESS = 1;
		/**
		 * 失败
		 */
		public static final int FAILED = 2;

	}
	
	/**
     * 响应状态   String类型
     */
	public static class RespStrStatus {
		
		/**
		 * 成功
		 */
		public static final String SUCCESS = "1";
		/**
		 * 失败
		 */
		public static final String FAILED = "2";

	}
	
	/**
     * 响应状态   String类型
     */
	public static class StorageDeclare {
		
		/**
		 * 推送单证系统，单据来源默认跨境宝
		 */
		public static final String docSource  = "01";

	}

	/**
     * 有效状态
     */
	public static class ActiveStatus {
		
		/**
		 * 权限状态 有效
		 */
		public static final String ACTIVE = "1";
		/**
		 * 权限状态 无效
		 */
		public static final String UNACTIVE = "0";
		
	}

    
	
    //////////////////////////与OP交互，签名字段默认值配置//////////////////////

	/**
	 * 订单自动审核使用常量
	 */
	public static class OrderAuditParam {
		/**
		 * 订单自动审核推送op 启运国写死为中国编码
		 */
		public static final String CHINA_SHIPPERN = "156";
	    /**
	     * 订单自动审核推送op,订单状态为新增(S)
	     */
		public static final String DEFAULT_ORDER_STATUS = "S";
		/**
		 * 订单审核：如果是第e仓订单，虚拟仓库货主卓志编码
		 */
		public static final String E_INVENTORY_TOPIDEACODE = "1000000581";
	}
	

    /**
     * 浮动质押回推订单类型
     */
	public static class FloatPledgeBillType {
		
		/**
		 * 电商订单
		 */
		public static final String ElEC = "10";
		
		/**
		 * 调拨单
		 */
		public static final String ALLOCATION = "20";
		
		/**
		 * 退运单
		 */
		public static final String RETURN = "30";
		
		/**
		 * BB销售出库单
		 */
		public static final String SALE_OUT = "40";
		
		/**
		 * BC订单
		 */
		public static final String BC = "50";
		
	}
    
    
    
    //////////////////////////与卓普信交互，签名字段默认值配置//////////////////////
	
	/**
	 * 系统公共参数
	 */
	public static class FinanceCommonParam {
		/** 
		 * 方法名
		 */
		public static final String METHOD = "method";
		/** 
		 * 商家key
		 */
		public static final String APPKEY = "appKey";
		/** 
		 * 时间戳
		 */
		public static final String TIMESTAMP = "timesTamp";
		/** 
		 * 签名
		 */
		public static final String SIGN = "sign";
		/** 
		 * 版本号
		 */
	    public static final String V = "1.0";
	    /**
	     * 来源系统 	默认卓志： 10
	     */
	    public static final String SOURCE_CODE = "10";
		
	}
	
	
	/**
	 * 接口名称
	 */
	public static class FinanceRequestMethod {

	    /**
	     * 融资申请接口
	     */
	    public static final String FINANCE_REQ_INTERFACE = "epass.financing.apply.push";
	    /**
	     * 融资申请状态回推接口
	     */
	    public static final String FINANCE_REQ_STATUS_INTERFACE = "epass.financing.apply.status.get";
	    /**
	     * 入库结果通知接口
	     */
	    public static final String IN_STORAGE_RESULT =  "epass.in.storage.notice.push";
	    /**
	     * 赎货申请接口_现金赎回
	     */
	    public static final String CASH_REDEMPTION_INTERFACE = "epass.redemption.apply.cash.push";
	    /**
	     * 赎货申请接口_赊销质押
	     */
	    public static final String PLEDGE_REDEMPTION_INTERFACE = "epass.redemption.apply.pledge.push";
	    /**
	     * 赎货申请状态回推接口
	     */
	    public static final String REDEMPTION_APPLY_STATUS_INTERFACE = "epass.redemption.apply.status.get";
	    /**
	     * 预结算接口-现金赎回
	     */
	    public static final String CASH_PRESETTLEMENT_INTERFACE = "epass.pre.settlement.cash.get";
	    /**
	     * 预结算接口-赊销质押
	     */
	    public static final String PLEDGE_PRESETTLEMENT_INTERFACE = "epass.pre.settlement.pledge.get";
	    /**
	     * 赎货申请_赎货出库接口
	     */
	    public static final String REDEMPTION_OUT_STORAGE_INTERFACE = "epass.redemption.out.storage.get";
	    /**
	     * 资金方调整接口
	     */
	    public static final String CAPITAL_ADJUST_INTERFACE = "epass.capital.adjust.push";
	    /**
	     * 额度利息查询接口
	     */
	    public static final String INTEREST_QUERY_INTERFACE = "epass.interest.query.push";
	    /**
	     * 融资汇总查询接口
	     */
	    public static final String Finance_Gather = "epass.bill.query.push";
	    /**
	     * 库存查询接口
	     */
	    public static final String INVENTORY_QUERY_INTERFACE = "epass.inventory.query.push";
	    /**
	     * 换货单接口
	     */
	    public static final String EXCHANGE_GOODS = "epass.exchange.goods.push";
	    /**
	     * 换货单状态接口
	     */
	    public static final String EXCHANGE_GOODS_STATUS = "epass.exchange.goods.status.push";
	    /**
	     * 押品出库申请接口和状态回推接口
	     */
	    public static final String COLLATERAL_OUTSTOCK_INTERFACE = "epass.out.storage.apply.push";
	    /**
	     * 虚拟仓入库回推接口
	     */
	  	public static final String IN_VIRTUAL_STORAGE = "epass.in.virtual.storage.get";
	    /**
	     * 质押解押
	     */
	  	public static final String PLEDGE_REMOVE= "epass.pledge.remove.get";
		/** 
		 * 卓普信商品初始化
		 */ 
		public static final String INITIALIZA_GOODS="epass.initializa.goods.get";
		/**   
		 * 同步商品到卓普信 
		 */ 
		public static final String GOODS_SYNC = "epass.add.goods.push";
		/**   
		 * 融资成本试算
		 */ 
		public static final String LOAN_COST_COUNT = "epass.loan.cost.count.push";
		
	}

	
	/**
	 * 金融产品类型
	 */
	public static class FinanceProductType {
		
		/**
		 * 现金赎回
		 */
		public static final String CASH = "1";
		/**
		 * 赊销质押
		 */
		public static final String PLEDGE = "2";

	}
	
	
	/**
	 * 融资质押类型类型
	 */
	public static class FinancePledgeType {
		
		/**
		 * 全仓质押
		 */
		public static final String ALL = "1";
		/**
		 * 部分质押
		 */
		public static final String SECTION = "2";
		
	}
	
	/**
	 * 金融模块配置信息key
	 * @date 2018年7月17日 上午10:53:13 
	 * @author Chen Qixiang
	 */
	public static class FinanceConfigureInfo{
		
		//卓普信配置信息
		public static final String[] ZHUOPUXIN_CONFIGURE = {"ZHUOPUXIN_TOPIDEACODE","APP_KEY","APP_SECRET","ZPX_UNIONINTERFACE_URL"};
		
		//柠盟配置信息
		public static final String[] NINGMENG_CONFIGURE = {"NINMENG_TOPIDEACODE","NINMENG_APPKEY","NINMENG_APP_SECRET","NINMENG_URL"};
		
		
	}
	
	

    
    
    //////////////////////////与外部平台交互，签名字段默认值配置//////////////////////	
	
	/**
	 * 外部接口名称
	 */
	public static class OuterPlatRequestMethod {
		/**
	     * 调拨指令接收接口
	     */
	    public static final String ALLOCATION_ORDER_PUSH = "epass.push.wms.CustTransferCode.Instruct";
		/**
	     * 订单接收接口
	     */
	    public static final String ORDER_ADD = "epass.orders.b2c.add";
		/**
		 * 自营库存查询
		 */
		public static final String SELF_INVENTORY_QUERY = "epass.wms.stock.inventory.get";
		/**
		 * 自营库存更新
		 */
		public static final String SELF_INVENTORY_UPDATE = "epass.push.wms.stock.inventory.get";
	    /**
	     * 订单状态审核
	     */
	    public static final String ORDER_AUDIT = "epass.orders.b2c.status.receive";
		/**   
		 * ofc 销售出仓订单 
		 */ 
		public static final String B2B_ORDERS_ADD = "epass.orders.b2b.add";
		/**   
		 * 外部平台初理货接收接口 
		 */ 
		public static final String INITIAL_TALLY = "epass.push.wms.stock.in.Initial.tally";
		/**   
		 * 外部平台理货确认接收接口 
		 */ 
		public static final String  CONFIRM_TALLY = "epass.push.wms.stock.in.confirm";
		/**   
		 * 外部品台 采购入库接收接口  
		 */ 
		public static final String PURCHASE_ADD = "epass.wms.stock.in.purchase.add";
		
		/**
		 * 跨境宝推送到外部电商平台接口
		 */
		public static final String PUSH_ORDER = "epass.push.orders.b2c.add";
		
		/**
		 * 跨境宝订单状态推送到外部电商平台接口
		 */
		public static final String ORDER_STATUS_PUSH = "epass.push.orders.status";
		/**   
		 * 入库申报回推 
		 */ 
		public static final String INSTOCK_DECLARE = "epass.push.wms.stock.in.declare";
		/**   
		 * 进仓状态回推外部商家  
		 */ 
		public static final String BILL_STATUS_ADD = "epass.push.wms.stock.in.bill.status.add";
		/**   
		 * 商品新增  只支持海外仓、直邮商品新增
		 */ 
		public static final String GOODS_ADD = "epass.recivie.goods.add";
		/**
		 * 进境推送接口
		 */
		public static final String INBOUND_METHOND = "epass.push.orders.b2c.inbound.status.add";
	    /**
	     * 装载回推接口
	     */
		public static final String LOADINGDELIVERY_PUSH = "epass.push.orders.b2c.loading.status.add";
		/**
		 * 进程节点推送
		 */
		public static final String PUSH_ESDEX_GLOBAL ="esdex.receive.global.push";
	    /**
	     * 运单号推送
	     */
	    public static final String WAY_BILL_PUSH = "epass.push.waybill";
	    /**
	     * 接收盘点指令
	     */
	    public static final String CHECK_INSTRUCT = "epass.push.wms.Check.Instruct";
	    /**
	     * 盘点结果推送
	     */
	    public static final String CHECK_RESULT = "epass.push.wms.Check.Result";
	    /**
	     * 接收盘点确认
	     */
	    public static final String CHECK_COMFIRM = "epass.push.wms.Check.Comfirm";
	    /**
	     *  退运指令推送
	     */
	    public static final String RETURMED_INSTRUCT = "epass.push.wms.Retumed.Instruct";
	    /**
	     *  调拨单回推
	     */
	    public static final String ALLOCATION_RETURN = "epass.push.wms.custTransferCode";
	    /**
	     *  调拨入库回推
	     */
	    public static final String ALLOCATION_INSTOCK_RETURN = "epass.push.wms.Retumed.Instruct";
	    /**
	     *  调拨出库回推
	     */
	    public static final String ALLOCATION_OUTSTOCK_RETURN = "epass.push.wms.Retumed.Instruct";
	    
	    /**
	     *  单据取消
	     */
	    public static final String ORDER_CANCEL = "epass.push.orders.cancelled.status";
	    
		
	}

	/**
	 * 接单|拒单
	 */
	public static class OrderAcceptStatus {
		
		/**
		 * 接单
		 */
		public static final String OMS_ACCEPT = "OMS_ACCEPT";
		/**
		 * 拒单
		 */
		public static final String REJECT = "REJECT";
		
	}
	
	/**
	 * SUCCESS & FAILURE
	 */
	public static class SuccessAndFailure {
		
		/**
		 * SUCCESS
		 */
		public static final String SUCCESS = "SUCCESS";
		
		/**
		 * FAILURE
		 */
		public static final String FAILURE = "FAILURE";


		
	}

	
	/**
	 * 订单审核类型
	 */
	public static class OrderAuditType {
		
		/**
		 * 审核
		 */
		public static final String AUDIT = "1";
		/**
		 * 取消
		 */
		public static final String CANCEL = "2";

	}

	
	/**
	 * 订单审核状态码
	 */
	public static class OrderAuditCode {
		
		//响应外部系统审核状态码:审核成功
		public static final String AUDIT_SUCCESS = "11";
		//响应外部系统审核状态码:取消成功
		public static final String CANCEL_SUCCESS = "12";
		//响应外部系统审核状态码:订单已审核，不能重复审核
		public static final String ALREADY_AUDIT = "21";
		//响应外部系统审核状态码:订单已申报，不能取消
		public static final String ALREADY_DECLARE = "22";
		//响应外部系统审核状态码:其他
		public static final String OTHER = "23";
		
	}
	
	

    
    //////////////////////////与订单100交互，签名字段默认值配置//////////////////////
	
	/**
	 * 订单100使用常量
	 */
	public static class Order100Param{
		/**
		 * 运单回推接口名称
		 */
		public static final String WAYBILLNO_BACK_METHOD = "kingdee.logistics.offline.send";
		/**
		 * 运单回推接口名称
		 */
		public static final String WAYBILLNO_BACK_TIANMAO = "kingdee.wlb.import.threepl.offline.consign";
		/**
		 * 订单100接口 新增配置 RES_ID
		 */
		public static final String RES_ID="216000000200045" ;
		/**
		 * 订单100接口 新增配置 RES_CODE
		 */
		public static final String RES_CODE="DISTRIBUTOR_13196320";
		/**
		 * 增量抓单方法名
		 */
		public static final String METHOD_INCRE = "kingdee.trades.sold.increment.get";
		/**
		 * 全量抓单方法名
		 */
		public static final String METHOD_TOTAL = "kingdee.trades.sold.get";
		/**
		 * 抓取的订单状态
		 */
		public static final String ORDER_STATUS = "TRADE_SELLER_SEND_GOODS";
	}

    
    
	
    //////////////////////////与速运系统交互，签名字段默认值配置//////////////////////
	
	/**
	 * 卓志速运状态码
	 */
	public static class EsdexErrorCode {
		/**
		 * 成功
		 */
		public static final String DEAL_SUCCESS = "10000";	
		/**
		 * 服务不可用	接口自身问题，稍后重试，可能是系统繁忙
		 */
		public static final String SERVICE_NO_USE = "20000";
		/**
		 * 无权限	是否有权限访问
		 */
		public static final String NO_AUTHORITY = "20001";
		/**
		 * 权限验证失败	请检查签名
		 */
		public static final String VALIDATE_FAILURE = "20002";	
		/**
		 * 参数错误 检查大小写，参数对照
		 */
		public static final String PARAM_ERROR = "30000";
		/**
		 * 参数缺失	缺少方法参数
		 */
		public static final String NO_PARAM = "30001";
		/**
		 * 密码错误	
		 */
		public static final String PASSWORDER_ERROR = "30002";
		/**
		 * 业务处理	请检查业务相关参数说明
		 */
		public static final String BUSINESS_PROCESS = "40000";
	}

	/**
	 * 推送速运使用常量
	 */
	public static class PushEsdexParam {
		/**
		 * 推送速运格式
		 */
		public static final String ESD_FORMAT = "json";
		/**
		 * 推送速运请求使用的编码格式
		 */
		public static final String ESD_CHARSET = "utf-8";
		/**
		 * 推送速运版本号
		 */
		public static final String ESD_VERSION = "1.0";
		/**
		 * 推送速运接口名称
		 */
		public static final String ESD_ORDER_AUDIT_METHOD = "esdex.receive.order.create";
		/**
		 * 单据取消接口名称
		 */
	    public static final String ESD_ORDER_CANCEL_METHOD ="esdex.receive.order.cancel";

		/**   
		 * 用于推送 回推 创建单据 的修改者 
		 */
		public static final String ESD_DEFAULT_CANCEL_OPERATOR = "system";
		
		/**
		 * 申报方案：广州邮关
		 */
		public static final String GZ_EC = "GZ-EC";
		
		/**
		 * 产品代码：易特快
		 */
		public static final String GZ_ETK_DP = "GZ-ETK-DP";

	}

	/**
     * 运单响应状态
     */
	public static class ESDResponseStatus {
		
		/**
		 * 成功
		 */
		public static final int SUCCESS = 1;
		/**
		 * 失败
		 */
		public static final int FAILED = -1;
		
	}
  	
    

    
    //////////////////////////与淘宝奇门交互，签名字段默认值配置//////////////////////
	
	/**
	 * 响应奇门状态
	 */
	public static class QimenResponseStatus {
		/**
		 * 成功
		 */
		public static final String SUCCESS = "success";
		/**
		 * 失败
		 */
		public static final String FAILURE = "failure";
		/**
		 * Y
		 */
		public static final String Y = "Y";
		/**
		 * N
		 */
		public static final String N = "N";
		
	}


	/**
	 * 推送奇门使用常量
	 */
	public static class PushQimenMethod {
	
		/******************************** 接收奇门推送的方法    *****************************************/
		/** 
		 * 6.26 订单取消接口名称  
		 */
		public static final String RODER_CANCEL = "order.cancel";
		/** 
		 * 6.1	商品同步接口名称  
		 */
		public static final String SINGLEITEM_SYNCHRONIZE = "singleitem.synchronize";
		/** 
		 * 6.28 库存查询接口名称  
		 */
		public static final String INVENTORY_QUERY = "inventory.query";
		/** 
		 * 6.4	入库单创建接口  
		 */
		public static final String ENTRYORDER_CREATE = "entryorder.create";
		/** 
		 * 6.7	退货入库单创建接口名称  
		 */
		public static final String RETURNORDER_CREATE = "returnorder.create";
		/** 
		 * 6.13 发货单创建接口名称 
		  */
		public static final String DELIVERYORDER_CREATE = "deliveryorder.create";
		/** 
		 * 6.10 出库单创建接口名称 
		 */
		public static final String STOCKOUT_CREATE = "stockout.create";
	
		/******************************** 推送到奇门的方法    *****************************************/
		/**
		 * 6.5  入库单确认接口
		 */ 
		public static final String ENTRY_ORDER_CONFIRM = "taobao.qimen.entryorder.confirm";
		/**
		 * 6.8  退货入库单确认接口
		 */  
		public static final String RETURN_ORDER_CONFIRM = "taobao.qimen.returnorder.confirm";
		/**
		 * 6.11  出库单确认接口
		 */  
		public static final String STOCK_OUT_CONFIRM = "taobao.qimen.stockout.confirm";
		/**
		 * 6.16  发货单确认  
		 */
		public static final String DELIVERY_ORDER_CONFIRM = "taobao.qimen.deliveryorder.confirm";

	}

	/**
	 *仓库编码
	 */
	public static class StockCode {
		/**
		 * 综合一仓
		 */
		public static final String COMPREHENSIVE_ESTOCK = "WMS_360_04";
		
		/**
		 * 香港仓
		 */
		public static final String HONGKONG_STOCK = "HK01";
		
	}
	
	/**
	 *仓库类型：a-保税仓，b-虚拟仓，c-海外仓，d-金融在途仓
	 */
	public static class StockType {
		/**
		 * 保税仓
		 */
		public static final String BONDED = "a";
		/**
		 * 虚拟仓
		 */
		public static final String FICTITIOUS = "b";
		/**
		 * 海外仓
		 */
		public static final String ABROAD = "c";
		/**
		 * 金融在途仓
		 */
		public static final String FINANCEONWAY = "d";
		
	}
    
    
    //////////////////////////与主数据交互，签名字段默认值配置//////////////////////
	
	/**
	 * 主数据使用常量
	 */
	public static class MainDataParam {
		
		/**
		 * 主数据商品同步新增标识
		 */
		public static final String GOODS_SYNC_TYPE_ADD = "1";
		
		/**
		 * 主数据商品同步更新标识
		 */
		public static final String GOODS_SYNC_TYPE_UPDATE = "2" ;
		
		/**
		 * 主数据商品同步版本号
		 */
		public static final String GOODS_SYNC_VERSION = "1.0";
		
		/**
		 * 主数据商品同步成功标识
		 */
		public static final String SUCCESS_FLAG = "SUCCESS";
		
		/**
		 * 主数据商品同步失败标识
		 */
		public static final String FAILURE_FLAG = "FAILURE";
		
	}
	
	/**
	 * 孩子王平台使用字段
	 */
	public static class HaiZiWangParams {
		
		public static final String SUCCESS_CODE = "205";
		
	}
	
	/**
	 *综合仓库
	 */
	public static class stock {
		
		public static final String FOUR = "WMS_360_04";
		public static final String FIVE = "WMS_360_05";
		public static final String SIX = "WMS_360_06";
		
	}
	
	

	
}
