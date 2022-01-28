package com.topideal.mq;


import org.junit.Test;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;



public class Producer {
	private String producerGroupName="test_producerGroupName";//生产则组
	private String namesrvAddr="127.0.0.1:9876";//mq服务地址多个服务时用,号隔开
	//private String namesrvAddr="10.10.102.208:9876";//mq服务地址多个服务时用,号隔开
	//private String namesrvAddr="10.10.102.180:9876";//mq服务地址多个服务时用,号隔开10.10.102.180:9876
	
	
	private DefaultMQProducer producer;//生产者

	/**
	 * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
	 * 注意：ProducerGroupName需要由应用来保证唯一<br>
	 * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
	 * 因为服务器会回查这个Group下的任意一个Producer
	 * @throws MQClientException
	 */
	public void init() throws MQClientException{
		if(producer==null){
			producer = new DefaultMQProducer(producerGroupName);
			producer.setNamesrvAddr(namesrvAddr);
			producer.setInstanceName("Producer1");
			/**
			 * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
			 * 注意：切记不可以在每次发送消息时，都调用start方法
			 */
			producer.start();
		}
	}

	public void shutdown(){
		/**
		 * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己
		 * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
		 */
		//producer.shutdown();
		/**注册一个JVM关闭的钩子，这个钩子可以在一下几种场景中被调用：
		 程序正常退出
		 使用System.exit()
		 终端使用Ctrl+C触发的中断
		 系统关闭
		 OutOfMemory宕机
		 使用Kill pid命令干掉进程（注：在使用kill -9 pid时，是不会被调用的）
		 * */
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				producer.shutdown();
			}
		}));
		System.exit(0);
	}

	//发送消息
	@Test
	public void send(){
		try{
			init();
			/*String topic="derp-inventory-mq";
			String tags="inventory_real_time_snapshot";
			String body="{}";
			*/
			/*String topic="derp-order-mq";
			String tags="projucet-quato-warning-tags";
			String body="{\"date\":\"2021-10-30\"}";
			*/
			/*String topic="derp-report-mq";
			String tags="report-bu-finance-summary-tags";
			String body="{\"merchantId\":1000031,\"depotId\":\"11\",\"month\":\"2021-09\"}";*/
			/*String topic="derp-erp-mq";
			String tags="copy-merchandise-tags";
			String body="{\"merchantId\":1000031}";*/
			/*String topic="derp-order-mq";
			String tags="get-oreal-purchase-orders-tags";
			String body="{\"startDate\":\"2021-01-01\",\"endDate\":\"2021-03-09\"}";*/
			
			/*String topic="derp-erp-mq";
			String tags="send-finance-goods-tags";
			String body="{\"merchantId\":1000031,\"goodsId\":1001456,\"goodsNo\":\"1372010\",\"customerId\":1000254}";
			*/
			/*String topic="derp-erp-mq";
			String tags="send-sapience_goods-tags";
			String body="{\"merchantId\":1000031,\"goodsId\":1001456,\"goodsNo\":\"1372010\",\"tag\":\"2\"}";*/
			
			/*String topic="derp-report-mq";
			String tags="monthly-account-auto-veri";
			String body="{}";
			*/
			/*String topic="derp-order-mq";
			String tags="crawler-tmall-purchase-order-tags";
			String body="{}";*/
			/*String topic="derp-order-mq";
			String tags="crawler-vip-purchase-order-tags";
			String body="{}";*/
			
			/*String topic="derp-order-mq";
			String tags="crawler-jd-purchase-order-tags";
			String body="{}";
			*/
			/*String topic="derp-order-mq";
			String tags="crawler-tmall-alipay-data-tags";
			String body="{}";*/
			/*String topic="derp-erp-mq";
			String tags="copy-merchandise-tags";
			String body="{\"merchantId\":1000031}";
			/*String topic="derp-report-mq";
			String tags="del-report-data-tags";
			String body="{\"ids\":[6748111,6748112],\"source\":\"3\",\"describe\":\"根据采购订单表体ids删除报表采购订单表体数据(多个逗号隔开)\"}";
*/		
			String topic="derp-erp-mq";
			String tags="generate-cost-price-difference-tags";
			String body="{\"merchantId\":1000038}";
			/*String topic="derp-order-mq";
			String tags="get-oreal-purchase-orders-tags";
			String body="{\"startDate\":\"2021-02-01 20:08:16\",\"endDate\":\"2021-03-18 12:00:00\"}";
			
			/*String topic="derp-erp-mq";
			String tags="user-syn-idm-tags";
			String body="{}";*/
			
			/*String topic="derp-inventory-mq";
			String tags="inventory_refresh_monthly_bill";
			String body="{\"monthlyAccountId\":\"684\",\"tag\":\"1\"}";	*/		
			/*String topic="derp-order-mq";
			String tags="customer-quato-warning-tags";
			String body="{}";*/	
			
			/*String topic="derp-order-mq";
			String tags="platform-pur-notshalf-message-tags";
			String body="{}";	*/
			/*String topic="derp-report-mq";
			String tags="sales-target-achievement-tags";
			String body="{}";*/
			
			/*String topic="derp-report-mq";
			String tags="inventory-falling-price-reserves";
			String body="{}";*/
			/*String topic="derp-order-mq";
			String tags="getone-smurfs-order-collection-order-tags";
			String body="{\"invoiceName\":\"  \",\"orderGoodsList\":[{\"goodsNo\":\"49746266812\",\"discountFee\":\"10.00\",\"subOrderTaxFee\":0,\"numIid\":\"1969835333\",\"refundStatus\":\"\",\"goodsPrice\":\"50.83\",\"logisticsCompany\":\"JD\",\"divideOrderFee\":0,\"invoiceNo\":\"JDV003003930604\",\"goodsName\":\"Bepanthen贝乐欣 拜耳护臀膏 宝宝红屁股 护臀霜100克（效期到 2022.5）\",\"goodsNum\":\"1\",\"refundId\":\"\",\"skuId\":\"49746266812\"},{\"goodsNo\":\"49746266812\",\"discountFee\":\"10.00\",\"subOrderTaxFee\":0,\"numIid\":\"1969835333\",\"refundStatus\":\"\",\"goodsPrice\":\"52\",\"logisticsCompany\":\"JD\",\"divideOrderFee\":0,\"invoiceNo\":\"JDV003003930604\",\"goodsName\":\"Bepanthen贝乐欣 拜耳护臀膏 宝宝红屁股 护臀霜100克（效期到 2022.5）\",\"goodsNum\":\"1\",\"refundId\":\"\",\"skuId\":\"49746266812\"}],\"buyerNick\":\"jd_vvjoqmuclfiv\",\"tradeEndDate\":\"\",\"isWmly\":\"\",\"discount\":\"20.00\",\"orderStatus\":3,\"receiverCity\":\"北京市\",\"orderCreateDate\":\"2021-06-10 08:41:44\",\"receiverCountry\":\"中国\",\"receiverDistrict\":\"海淀区\",\"invoiceType\":\"\",\"orderStatusString\":\"TRADE_WAIT_BUYER_CONFIRM_GOODS\",\"payment\":\"74.00\",\"storeName\":\"健太京东POP旗舰店\",\"deliveryDate\":\"2021-06-10 11:09:26\",\"receiverZip\":\"\",\"createDate\":1623335701892,\"omniPackage\":\"\",\"orderNo\":\"yang001202086482512\",\"modifyDate\":1623335701892,\"receiverName\":\"白女士\",\"omnichannelParam\":\"\",\"receiverMobile\":\"13910068635\",\"tax\":\"6.17\",\"wayFrtFee\":\"6.33\",\"receiverAddress\":\"学院南路甲82号院\",\"orderModifyDate\":\"2021-06-10 11:09:26\",\"receiverState\":\"北京\",\"sellerMemo\":\"\",\"paymentGoods\":\"67.83\",\"payDate\":\"2021-06-10 08:41:50\",\"storeCode\":\"10002075\",\"derpShopCode\":\"10002075\",\"derpStatus\":\"3\",\"merchantName\":\"健太\"}";
*/
			/*String topic="derp-order-mq";
			String tags="platform-statement-order-tags";
			String body="{}";*/
			
			/*String topic="derp-inventory-mq";
			String tags="inventory_bu_inventory";
			String body="{\"merchantId\":1000031,\"month\":\"2021-07\",\"depotId\":11}";*/
			/*String topic="derp-inventory-mq";
			String tags="inventory_sys_data";
			String body="{\"tableName\":\"i_bu_inventory\"}";*/
			/*String topic="derp-inventory-mq";
			String tags="inventory-data-roll-back-tags";
			String body="{\"code\":\"ZDCK0010004663\"}";*/
			
			/*String topic="derp-order-mq";
			String tags="platform-purchase-order-tags";
			String body="{}";*/
			
			/*String topic="derp-inventory-mq";
			String tags="inventory_bu_inventory";
			String body="{\"merchantId\":1000038,\"month\":\"2021-06\",\"depotId\":11}";*/
			
			
			/*String topic="derp-erp-mq";
			String tags="send-reminder-mail-tags";
			String body="{\"amount\":\"\",\"auditorId\":0,\"buId\":10,\"buName\":\"保健品事业部E\",\"businessModuleType\":\"7\",\"cancelCompleteId\":0,\"cancelId\":0,\"completeId\":0,\"createId\":0,\"currency\":\"\",\"dataList\":[{\"customer\":\"唯品会（中国）有限公司\",\"orderCode\":\"XSO000040012436\",\"poNum\":\"JUN1234567\",\"status\":\"已完成\"}],\"drawerId\":0,\"merchantId\":1000031,\"merchantName\":\"健太\",\"modifyId\":0,\"orderCode\":\"\",\"poNum\":\"\",\"reminderOrgId\":0,\"reviewerId\":0,\"shelvesId\":0,\"status\":\"\",\"submitId\":[],\"supplier\":\"\",\"type\":\"3\",\"typeName\":\"上架\",\"userName\":\"\",\"verificationId\":0}";	
			
			*/
			
			/*String topic="derp-report-mq";
			String tags="sales-target-achievement-tags";
			String body="{}";*/
			
			/*String topic="derp-order-mq";
			String tags="loading-deliveries-push-back-tags-2-2";
			String body="{\"code\":\"XSCK00010000393\",\"customParam\":{\"code\":\"XSO000030004111\"}}";*/
			
			
			
			/*String topic="derp-order-mq";
			String tags="grab-ews-delivery-order-tags";
			String body="{\"tag\":\"2\",\"startTime\":\"2020-01-01 00:00:00\",\"endTime\":\"2020-02-01 00:00:00\",\"merchantCode\":\"1000000204\"}";
			*/
			

			
			/*String topic="derp-order-mq";
			String tags="grab-smurfs-order-collection-order-tags";
			String body="{}";*/
			/*String topic="derp-order-mq";
			String tags="grab-ews-delivery-order-tags";
			String body="{}";*/
			/*String topic="derp-inventory-mq";
			String tags="inventory_refresh_monthly_bill";
			String body="{}";*/
			
			/*String topic="derp-order-mq";
			String tags="epass-b2c-order-tags-2";
			String body="{\"sourceType\":\"3\",\"settleIds\":\"GYS00969202002030002\",\"userCode\":\"YJ健太有限公司(代销入仓)\"}";
			*/
		/*	String topic="derp-order-mq";
			String tags="yunji-delivery-return-detail-tags";
			String body="{\"sourceType\":\"3\",\"settleIds\":\"GYS00969202002030002\",\"userCode\":\"YJ健太有限公司(代销入仓)\"}";
			*/		
			/*String topic="derp-inventory-mq";
			String tags="inventory_quantity_add_lower";
			String body="{\"backTags\":\"storage-self-inventory-push-back-tags\",\"backTopic\":\"derp-storage-mq\",\"buId\":\"\",\"buName\":\"\",\"businessNo\":\"XQTZ20200426132201\",\"customParam\":{\"code\":\"LXTZO0000000387\"},\"depotCode\":\"WMS_360_04\",\"depotId\":\"11\",\"depotName\":\"卓志南沙保税仓\",\"depotType\":\"a\",\"divergenceDate\":\"2020-04-26 13:22:00\",\"goodsList\":[{\"barcode\":\"037000000532\",\"batchNo\":\"LOTZHC11710280000031\",\"commbarcode\":\"\",\"goodsId\":\"1002830\",\"goodsName\":\"Ivory香皂-芦荟\",\"goodsNo\":\"80204643\",\"isExpire\":\"1\",\"num\":50,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2016-07-05\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"037000000532\",\"batchNo\":\"LOTZHC11710280000031\",\"commbarcode\":\"\",\"goodsId\":\"1002830\",\"goodsName\":\"Ivory香皂-芦荟\",\"goodsNo\":\"80204643\",\"isExpire\":\"0\",\"num\":50,\"operateType\":\"0\",\"overdueDate\":\"2019-12-31\",\"productionDate\":\"2016-07-05\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"037000000532\",\"batchNo\":\"LOTZHC11909300000008\",\"commbarcode\":\"\",\"goodsId\":\"1002830\",\"goodsName\":\"Ivory香皂-芦荟\",\"goodsNo\":\"80204643\",\"isExpire\":\"1\",\"num\":6,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2017-01-15\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"037000000532\",\"batchNo\":\"LOTZHC11909300000008\",\"commbarcode\":\"\",\"goodsId\":\"1002830\",\"goodsName\":\"Ivory香皂-芦荟\",\"goodsNo\":\"80204643\",\"isExpire\":\"0\",\"num\":6,\"operateType\":\"0\",\"overdueDate\":\"2019-12-31\",\"productionDate\":\"2017-01-15\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"037000000532\",\"batchNo\":\"LOTZHC11909300000009\",\"commbarcode\":\"\",\"goodsId\":\"1002830\",\"goodsName\":\"Ivory香皂-芦荟\",\"goodsNo\":\"80204643\",\"isExpire\":\"1\",\"num\":9,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2017-01-15\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"037000000532\",\"batchNo\":\"LOTZHC11909300000009\",\"commbarcode\":\"\",\"goodsId\":\"1002830\",\"goodsName\":\"Ivory香皂-芦荟\",\"goodsNo\":\"80204643\",\"isExpire\":\"0\",\"num\":9,\"operateType\":\"0\",\"overdueDate\":\"2019-12-31\",\"productionDate\":\"2017-01-15\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"374620\",\"batchNo\":\"LOTZHC11809170000051\",\"commbarcode\":\"\",\"goodsId\":\"1002086\",\"goodsName\":\"汰渍洗衣凝珠 57颗盒装\",\"goodsNo\":\"80215510\",\"isExpire\":\"1\",\"num\":104,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-07-10\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"374620\",\"batchNo\":\"LOTZHC11809170000051\",\"commbarcode\":\"\",\"goodsId\":\"1002086\",\"goodsName\":\"汰渍洗衣凝珠 57颗盒装\",\"goodsNo\":\"80215510\",\"isExpire\":\"0\",\"num\":104,\"operateType\":\"0\",\"overdueDate\":\"2019-10-03\",\"productionDate\":\"2018-07-10\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"374620\",\"batchNo\":\"LOTZHC12002170000061\",\"commbarcode\":\"\",\"goodsId\":\"1002086\",\"goodsName\":\"汰渍洗衣凝珠 57颗盒装\",\"goodsNo\":\"80215510\",\"isExpire\":\"1\",\"num\":2,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2019-03-08\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"374620\",\"batchNo\":\"LOTZHC12002170000061\",\"commbarcode\":\"\",\"goodsId\":\"1002086\",\"goodsName\":\"汰渍洗衣凝珠 57颗盒装\",\"goodsNo\":\"80215510\",\"isExpire\":\"1\",\"num\":2,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2019-03-08\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430674973\",\"batchNo\":\"LOTZHC11909300000020\",\"commbarcode\":\"\",\"goodsId\":\"1001820\",\"goodsName\":\"JOY超浓缩除菌型洗洁精(绿茶香型）190ml\",\"goodsNo\":\"82237666\",\"isExpire\":\"1\",\"num\":39,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-12-31\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430674973\",\"batchNo\":\"LOTZHC11909300000020\",\"commbarcode\":\"\",\"goodsId\":\"1001820\",\"goodsName\":\"JOY超浓缩除菌型洗洁精(绿茶香型）190ml\",\"goodsNo\":\"82237666\",\"isExpire\":\"0\",\"num\":39,\"operateType\":\"0\",\"overdueDate\":\"2019-12-31\",\"productionDate\":\"2018-12-31\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430675109\",\"batchNo\":\"LOTZHC12002170000062\",\"commbarcode\":\"\",\"goodsId\":\"1001833\",\"goodsName\":\"JOY超浓缩洗洁精MOISTCARE 190ml\",\"goodsNo\":\"82237683\",\"isExpire\":\"1\",\"num\":6,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-03-26\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430675109\",\"batchNo\":\"LOTZHC12002170000062\",\"commbarcode\":\"\",\"goodsId\":\"1001833\",\"goodsName\":\"JOY超浓缩洗洁精MOISTCARE 190ml\",\"goodsNo\":\"82237683\",\"isExpire\":\"0\",\"num\":6,\"operateType\":\"0\",\"overdueDate\":\"2020-03-15\",\"productionDate\":\"2018-03-26\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430221511\",\"batchNo\":\"LOTZHC11909300000011\",\"commbarcode\":\"\",\"goodsId\":\"1002327\",\"goodsName\":\"Febreze织物去味除菌剂 舒缓花香补充装 320ml\",\"goodsNo\":\"82245625\",\"isExpire\":\"1\",\"num\":3,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-07-09\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430221511\",\"batchNo\":\"LOTZHC11909300000011\",\"commbarcode\":\"\",\"goodsId\":\"1002327\",\"goodsName\":\"Febreze织物去味除菌剂 舒缓花香补充装 320ml\",\"goodsNo\":\"82245625\",\"isExpire\":\"0\",\"num\":3,\"operateType\":\"0\",\"overdueDate\":\"2019-12-31\",\"productionDate\":\"2018-07-09\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430221511\",\"batchNo\":\"LOTZHC11909300000012\",\"commbarcode\":\"\",\"goodsId\":\"1002327\",\"goodsName\":\"Febreze织物去味除菌剂 舒缓花香补充装 320ml\",\"goodsNo\":\"82245625\",\"isExpire\":\"1\",\"num\":1,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-07-09\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430221511\",\"batchNo\":\"LOTZHC11909300000012\",\"commbarcode\":\"\",\"goodsId\":\"1002327\",\"goodsName\":\"Febreze织物去味除菌剂 舒缓花香补充装 320ml\",\"goodsNo\":\"82245625\",\"isExpire\":\"0\",\"num\":1,\"operateType\":\"0\",\"overdueDate\":\"2019-12-31\",\"productionDate\":\"2018-07-09\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"374620\",\"batchNo\":\"LOTZHC12002170000060\",\"commbarcode\":\"\",\"goodsId\":\"1002086\",\"goodsName\":\"汰渍洗衣凝珠 57颗盒装\",\"goodsNo\":\"80215510\",\"isExpire\":\"1\",\"num\":2,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2019-03-08\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"374620\",\"batchNo\":\"LOTZHC12002170000060\",\"commbarcode\":\"\",\"goodsId\":\"1002086\",\"goodsName\":\"汰渍洗衣凝珠 57颗盒装\",\"goodsNo\":\"80215510\",\"isExpire\":\"1\",\"num\":2,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2019-03-08\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430674966\",\"batchNo\":\"LOTZHC12002170000045\",\"commbarcode\":\"\",\"goodsId\":\"1002050\",\"goodsName\":\"JOY超浓缩除菌型洗洁精 190ml\",\"goodsNo\":\"82237665\",\"isExpire\":\"1\",\"num\":1,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2019-06-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430674966\",\"batchNo\":\"LOTZHC12002170000045\",\"commbarcode\":\"\",\"goodsId\":\"1002050\",\"goodsName\":\"JOY超浓缩除菌型洗洁精 190ml\",\"goodsNo\":\"82237665\",\"isExpire\":\"1\",\"num\":1,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2019-06-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430674980\",\"batchNo\":\"LOTZHC12002170000046\",\"commbarcode\":\"\",\"goodsId\":\"1001819\",\"goodsName\":\"JOY超浓缩除菌型洗洁精(柠檬香型) 190ml\",\"goodsNo\":\"82237667\",\"isExpire\":\"1\",\"num\":36,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2019-06-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430674980\",\"batchNo\":\"LOTZHC12002170000046\",\"commbarcode\":\"\",\"goodsId\":\"1001819\",\"goodsName\":\"JOY超浓缩除菌型洗洁精(柠檬香型) 190ml\",\"goodsNo\":\"82237667\",\"isExpire\":\"1\",\"num\":36,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2019-06-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430221511\",\"batchNo\":\"LOTZHC12002170000053\",\"commbarcode\":\"\",\"goodsId\":\"1002327\",\"goodsName\":\"Febreze织物去味除菌剂 舒缓花香补充装 320ml\",\"goodsNo\":\"82245625\",\"isExpire\":\"1\",\"num\":2,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-12-08\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430221511\",\"batchNo\":\"LOTZHC12002170000053\",\"commbarcode\":\"\",\"goodsId\":\"1002327\",\"goodsName\":\"Febreze织物去味除菌剂 舒缓花香补充装 320ml\",\"goodsNo\":\"82245625\",\"isExpire\":\"1\",\"num\":2,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2018-12-08\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430955102\",\"batchNo\":\"LOTZHC12002170000054\",\"commbarcode\":\"\",\"goodsId\":\"1002096\",\"goodsName\":\"Febreze织物去味除菌剂 绿茶香补充装 320ml\",\"goodsNo\":\"82245628\",\"isExpire\":\"1\",\"num\":7,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2017-06-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430955102\",\"batchNo\":\"LOTZHC12002170000054\",\"commbarcode\":\"\",\"goodsId\":\"1002096\",\"goodsName\":\"Febreze织物去味除菌剂 绿茶香补充装 320ml\",\"goodsNo\":\"82245628\",\"isExpire\":\"1\",\"num\":7,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2017-06-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"8001090015921\",\"batchNo\":\"LOTZHC12002180000014\",\"commbarcode\":\"\",\"goodsId\":\"1001822\",\"goodsName\":\"Fairy铂金系列洗碗凝珠18颗\",\"goodsNo\":\"82264931\",\"isExpire\":\"1\",\"num\":1753,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-12-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"8001090015921\",\"batchNo\":\"LOTZHC12002180000014\",\"commbarcode\":\"\",\"goodsId\":\"1001822\",\"goodsName\":\"Fairy铂金系列洗碗凝珠18颗\",\"goodsNo\":\"82264931\",\"isExpire\":\"1\",\"num\":1753,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2018-12-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"8001090011541\",\"batchNo\":\"LOTZHC12002180000013\",\"commbarcode\":\"\",\"goodsId\":\"1001821\",\"goodsName\":\"Fairy多效合一洗碗凝珠22颗\",\"goodsNo\":\"82264935\",\"isExpire\":\"1\",\"num\":1657,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-12-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"8001090011541\",\"batchNo\":\"LOTZHC12002180000013\",\"commbarcode\":\"\",\"goodsId\":\"1001821\",\"goodsName\":\"Fairy多效合一洗碗凝珠22颗\",\"goodsNo\":\"82264935\",\"isExpire\":\"1\",\"num\":1657,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2018-12-01\",\"type\":\"0\",\"unit\":\"\"}],\"isTopBooks\":\"0\",\"merchantId\":\"1000030\",\"merchantName\":\"嘉宝贸易\",\"orderNo\":\"LXTZO0000000387\",\"ownMonth\":\"2020-04\",\"shopCode\":\"\",\"source\":\"0014\",\"sourceDate\":\"2020-05-03 12:44:47\",\"sourceType\":\"0018\",\"storePlatformName\":\"\",\"topidealCode\":\"0000138\"}";
			*/
			
			/*String topic="derp-inventory-mq";
			String tags="inventory_quantity_add_lower";
			String body="{\"backTags\":\"storage-self-inventory-push-back-tags\",\"backTopic\":\"derp-storage-mq\",\"buId\":\"\",\"buName\":\"\",\"businessNo\":\"XQTZ20200426132201\",\"customParam\":{\"code\":\"LXTZO0000000387\"},\"depotCode\":\"WMS_360_04\",\"depotId\":\"11\",\"depotName\":\"卓志南沙保税仓\",\"depotType\":\"a\",\"divergenceDate\":\"2020-04-26 13:22:00\",\"goodsList\":[{\"barcode\":\"037000000532\",\"batchNo\":\"LOTZHC11710280000031\",\"commbarcode\":\"\",\"goodsId\":\"1002830\",\"goodsName\":\"Ivory香皂-芦荟\",\"goodsNo\":\"80204643\",\"isExpire\":\"1\",\"num\":50,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2016-07-05\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"037000000532\",\"batchNo\":\"LOTZHC11710280000031\",\"commbarcode\":\"\",\"goodsId\":\"1002830\",\"goodsName\":\"Ivory香皂-芦荟\",\"goodsNo\":\"80204643\",\"isExpire\":\"0\",\"num\":50,\"operateType\":\"0\",\"overdueDate\":\"2019-12-31\",\"productionDate\":\"2016-07-05\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"037000000532\",\"batchNo\":\"LOTZHC11909300000008\",\"commbarcode\":\"\",\"goodsId\":\"1002830\",\"goodsName\":\"Ivory香皂-芦荟\",\"goodsNo\":\"80204643\",\"isExpire\":\"1\",\"num\":6,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2017-01-15\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"037000000532\",\"batchNo\":\"LOTZHC11909300000008\",\"commbarcode\":\"\",\"goodsId\":\"1002830\",\"goodsName\":\"Ivory香皂-芦荟\",\"goodsNo\":\"80204643\",\"isExpire\":\"0\",\"num\":6,\"operateType\":\"0\",\"overdueDate\":\"2019-12-31\",\"productionDate\":\"2017-01-15\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"037000000532\",\"batchNo\":\"LOTZHC11909300000009\",\"commbarcode\":\"\",\"goodsId\":\"1002830\",\"goodsName\":\"Ivory香皂-芦荟\",\"goodsNo\":\"80204643\",\"isExpire\":\"1\",\"num\":9,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2017-01-15\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"037000000532\",\"batchNo\":\"LOTZHC11909300000009\",\"commbarcode\":\"\",\"goodsId\":\"1002830\",\"goodsName\":\"Ivory香皂-芦荟\",\"goodsNo\":\"80204643\",\"isExpire\":\"0\",\"num\":9,\"operateType\":\"0\",\"overdueDate\":\"2019-12-31\",\"productionDate\":\"2017-01-15\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"374620\",\"batchNo\":\"LOTZHC11809170000051\",\"commbarcode\":\"\",\"goodsId\":\"1002086\",\"goodsName\":\"汰渍洗衣凝珠 57颗盒装\",\"goodsNo\":\"80215510\",\"isExpire\":\"1\",\"num\":104,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-07-10\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"374620\",\"batchNo\":\"LOTZHC11809170000051\",\"commbarcode\":\"\",\"goodsId\":\"1002086\",\"goodsName\":\"汰渍洗衣凝珠 57颗盒装\",\"goodsNo\":\"80215510\",\"isExpire\":\"0\",\"num\":104,\"operateType\":\"0\",\"overdueDate\":\"2019-10-03\",\"productionDate\":\"2018-07-10\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"374620\",\"batchNo\":\"LOTZHC12002170000061\",\"commbarcode\":\"\",\"goodsId\":\"1002086\",\"goodsName\":\"汰渍洗衣凝珠 57颗盒装\",\"goodsNo\":\"80215510\",\"isExpire\":\"1\",\"num\":2,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2019-03-08\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"374620\",\"batchNo\":\"LOTZHC12002170000061\",\"commbarcode\":\"\",\"goodsId\":\"1002086\",\"goodsName\":\"汰渍洗衣凝珠 57颗盒装\",\"goodsNo\":\"80215510\",\"isExpire\":\"1\",\"num\":2,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2019-03-08\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430674973\",\"batchNo\":\"LOTZHC11909300000020\",\"commbarcode\":\"\",\"goodsId\":\"1001820\",\"goodsName\":\"JOY超浓缩除菌型洗洁精(绿茶香型）190ml\",\"goodsNo\":\"82237666\",\"isExpire\":\"1\",\"num\":39,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-12-31\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430674973\",\"batchNo\":\"LOTZHC11909300000020\",\"commbarcode\":\"\",\"goodsId\":\"1001820\",\"goodsName\":\"JOY超浓缩除菌型洗洁精(绿茶香型）190ml\",\"goodsNo\":\"82237666\",\"isExpire\":\"0\",\"num\":39,\"operateType\":\"0\",\"overdueDate\":\"2019-12-31\",\"productionDate\":\"2018-12-31\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430675109\",\"batchNo\":\"LOTZHC12002170000062\",\"commbarcode\":\"\",\"goodsId\":\"1001833\",\"goodsName\":\"JOY超浓缩洗洁精MOISTCARE 190ml\",\"goodsNo\":\"82237683\",\"isExpire\":\"1\",\"num\":6,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-03-26\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430675109\",\"batchNo\":\"LOTZHC12002170000062\",\"commbarcode\":\"\",\"goodsId\":\"1001833\",\"goodsName\":\"JOY超浓缩洗洁精MOISTCARE 190ml\",\"goodsNo\":\"82237683\",\"isExpire\":\"0\",\"num\":6,\"operateType\":\"0\",\"overdueDate\":\"2020-03-15\",\"productionDate\":\"2018-03-26\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430221511\",\"batchNo\":\"LOTZHC11909300000011\",\"commbarcode\":\"\",\"goodsId\":\"1002327\",\"goodsName\":\"Febreze织物去味除菌剂 舒缓花香补充装 320ml\",\"goodsNo\":\"82245625\",\"isExpire\":\"1\",\"num\":3,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-07-09\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430221511\",\"batchNo\":\"LOTZHC11909300000011\",\"commbarcode\":\"\",\"goodsId\":\"1002327\",\"goodsName\":\"Febreze织物去味除菌剂 舒缓花香补充装 320ml\",\"goodsNo\":\"82245625\",\"isExpire\":\"0\",\"num\":3,\"operateType\":\"0\",\"overdueDate\":\"2019-12-31\",\"productionDate\":\"2018-07-09\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430221511\",\"batchNo\":\"LOTZHC11909300000012\",\"commbarcode\":\"\",\"goodsId\":\"1002327\",\"goodsName\":\"Febreze织物去味除菌剂 舒缓花香补充装 320ml\",\"goodsNo\":\"82245625\",\"isExpire\":\"1\",\"num\":1,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-07-09\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"4902430221511\",\"batchNo\":\"LOTZHC11909300000012\",\"commbarcode\":\"\",\"goodsId\":\"1002327\",\"goodsName\":\"Febreze织物去味除菌剂 舒缓花香补充装 320ml\",\"goodsNo\":\"82245625\",\"isExpire\":\"0\",\"num\":1,\"operateType\":\"0\",\"overdueDate\":\"2019-12-31\",\"productionDate\":\"2018-07-09\",\"type\":\"1\",\"unit\":\"\"},{\"barcode\":\"374620\",\"batchNo\":\"LOTZHC12002170000060\",\"commbarcode\":\"\",\"goodsId\":\"1002086\",\"goodsName\":\"汰渍洗衣凝珠 57颗盒装\",\"goodsNo\":\"80215510\",\"isExpire\":\"1\",\"num\":2,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2019-03-08\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"374620\",\"batchNo\":\"LOTZHC12002170000060\",\"commbarcode\":\"\",\"goodsId\":\"1002086\",\"goodsName\":\"汰渍洗衣凝珠 57颗盒装\",\"goodsNo\":\"80215510\",\"isExpire\":\"1\",\"num\":2,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2019-03-08\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430674966\",\"batchNo\":\"LOTZHC12002170000045\",\"commbarcode\":\"\",\"goodsId\":\"1002050\",\"goodsName\":\"JOY超浓缩除菌型洗洁精 190ml\",\"goodsNo\":\"82237665\",\"isExpire\":\"1\",\"num\":1,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2019-06-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430674966\",\"batchNo\":\"LOTZHC12002170000045\",\"commbarcode\":\"\",\"goodsId\":\"1002050\",\"goodsName\":\"JOY超浓缩除菌型洗洁精 190ml\",\"goodsNo\":\"82237665\",\"isExpire\":\"1\",\"num\":1,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2019-06-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430674980\",\"batchNo\":\"LOTZHC12002170000046\",\"commbarcode\":\"\",\"goodsId\":\"1001819\",\"goodsName\":\"JOY超浓缩除菌型洗洁精(柠檬香型) 190ml\",\"goodsNo\":\"82237667\",\"isExpire\":\"1\",\"num\":36,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2019-06-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430674980\",\"batchNo\":\"LOTZHC12002170000046\",\"commbarcode\":\"\",\"goodsId\":\"1001819\",\"goodsName\":\"JOY超浓缩除菌型洗洁精(柠檬香型) 190ml\",\"goodsNo\":\"82237667\",\"isExpire\":\"1\",\"num\":36,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2019-06-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430221511\",\"batchNo\":\"LOTZHC12002170000053\",\"commbarcode\":\"\",\"goodsId\":\"1002327\",\"goodsName\":\"Febreze织物去味除菌剂 舒缓花香补充装 320ml\",\"goodsNo\":\"82245625\",\"isExpire\":\"1\",\"num\":2,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-12-08\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430221511\",\"batchNo\":\"LOTZHC12002170000053\",\"commbarcode\":\"\",\"goodsId\":\"1002327\",\"goodsName\":\"Febreze织物去味除菌剂 舒缓花香补充装 320ml\",\"goodsNo\":\"82245625\",\"isExpire\":\"1\",\"num\":2,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2018-12-08\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430955102\",\"batchNo\":\"LOTZHC12002170000054\",\"commbarcode\":\"\",\"goodsId\":\"1002096\",\"goodsName\":\"Febreze织物去味除菌剂 绿茶香补充装 320ml\",\"goodsNo\":\"82245628\",\"isExpire\":\"1\",\"num\":7,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2017-06-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"4902430955102\",\"batchNo\":\"LOTZHC12002170000054\",\"commbarcode\":\"\",\"goodsId\":\"1002096\",\"goodsName\":\"Febreze织物去味除菌剂 绿茶香补充装 320ml\",\"goodsNo\":\"82245628\",\"isExpire\":\"1\",\"num\":7,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2017-06-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"8001090015921\",\"batchNo\":\"LOTZHC12002180000014\",\"commbarcode\":\"\",\"goodsId\":\"1001822\",\"goodsName\":\"Fairy铂金系列洗碗凝珠18颗\",\"goodsNo\":\"82264931\",\"isExpire\":\"1\",\"num\":1753,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-12-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"8001090015921\",\"batchNo\":\"LOTZHC12002180000014\",\"commbarcode\":\"\",\"goodsId\":\"1001822\",\"goodsName\":\"Fairy铂金系列洗碗凝珠18颗\",\"goodsNo\":\"82264931\",\"isExpire\":\"1\",\"num\":1753,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2018-12-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"8001090011541\",\"batchNo\":\"LOTZHC12002180000013\",\"commbarcode\":\"\",\"goodsId\":\"1001821\",\"goodsName\":\"Fairy多效合一洗碗凝珠22颗\",\"goodsNo\":\"82264935\",\"isExpire\":\"1\",\"num\":1657,\"operateType\":\"1\",\"overdueDate\":\"2020-07-15\",\"productionDate\":\"2018-12-01\",\"type\":\"0\",\"unit\":\"\"},{\"barcode\":\"8001090011541\",\"batchNo\":\"LOTZHC12002180000013\",\"commbarcode\":\"\",\"goodsId\":\"1001821\",\"goodsName\":\"Fairy多效合一洗碗凝珠22颗\",\"goodsNo\":\"82264935\",\"isExpire\":\"1\",\"num\":1657,\"operateType\":\"0\",\"overdueDate\":\"2020-05-31\",\"productionDate\":\"2018-12-01\",\"type\":\"0\",\"unit\":\"\"}],\"isTopBooks\":\"0\",\"merchantId\":\"1000030\",\"merchantName\":\"嘉宝贸易\",\"orderNo\":\"LXTZO0000000387\",\"ownMonth\":\"2020-04\",\"shopCode\":\"\",\"source\":\"0014\",\"sourceDate\":\"2020-05-03 12:44:47\",\"sourceType\":\"0018\",\"storePlatformName\":\"\",\"topidealCode\":\"0000138\"}";
			*/
			/*String topic="derp-bxm-mq";
			String tags="del-bxm-data-tags";
			String body="{}";*/
			
			/*String topic="derp-storage-mq";
			String tags="storage-results-push-tags";
			String body="{\"merchantId\":2000031,\"merchantName\":\"健太\",\"email\":\"ziyi.zheng@topideal.com.cn;meiling.liang@topideal.com.cn;shouxin.li@topideal.com.cn\",\"inventoryResultEmail\":\"haiyi.lu@topideal.com.cn;huan.dong@topideal.com.cn;wanming.liang@topideal.com.cn;ziyi.zheng@topideal.com.cn;meiling.liang@topideal.com.cn;shouxin.li@topideal.com.cn\",\"topidealCode\":\"1000000204\",\"inventoryCode\":\"l11312321232\",\"orderCode\":\"PDO00110010000038\",\"updateDate\":\"2019-07-03 12:11:23\",\"status\":\"70\",\"depoCode\":\"WMS_360_04\",\"serverType\":\"20\",\"model\":\"10\",\"dismissRemark\":\"\",\"goodsList\":[{\"commbarcode\":\"09310160820488\",\"goodsId\":1001486,\"goodsName\":\"Elevit爱乐维 女士复合维生素专用叶酸片 100片/盒\",\"goodsCode\":\"ERP1873120101577233\",\"goodsNo\":\"81130280\",\"barcode\":\"9310160820488\",\"batchNo\":\"LOTHWHK011810160000102\",\"amount\":12,\"realqty\":27,\"diffqty\":0,\"productionDate\":\"2018-08-01\",\"overdueDate\":\"2020-12-01\",\"isDamage\":\"0\"}]}";							
			*/
			
			/*String topic="derp-report-mq";
			String tags="grab-derp-purchase-idepot-order-tags";
			String body="{}";*/
			/*String topic="derp-report-mq";
			String tags="del-report-data-tags";
			String body="{\"delMap\":{\"2000031,10,30,2020-04\":\"2000031,10,30,2020-04\"},\"source\":\"9\",\"describe\":\"根据 商家 仓库 事业部 月份 删除报表事业部库存\"}";
			 */
			
			
			
			/*String topic="derp-erp-mq";
			String tags="copy-merchandise-tags";
			String body="{}";*/
			
		/*	String topic="derp-inventory-mq";
			String tags="inventory_bu_inventory";
			String body="{\"merchantId\":1000038,\"depotId\":11,\"month\":\"2021-06\"}";*/
			
			/*String topic="derp-report-mq";
			String tags="report-bu-finance-summary-tags";
			String body="{\"merchantId\":1000031,\"month\":\"2021-07\",\"buId\":\"9\"}";*/
			//String body="{\"backTags\":\"sale-shelf-in-depot-push-back\",\"backTopic\":\"derp-order-mq\",\"businessNo\":\"SJRKtest0001000002\",\"customParam\":{\"code\":\"SJRKtest000100000291\"},\"depotCode\":\"VIP_GZNS\",\"depotId\":\"113\",\"depotName\":\"唯品会备查库\",\"depotType\":\"b\",\"divergenceDate\":\"2020-04-07 11:05:29\",\"goodsList\":[{\"barcode\":\"bar_jt20023\",\"batchNo\":\"B0001\",\"commbarcode\":\"\",\"goodsId\":\"1004732\",\"goodsName\":\"gName_jt20023\",\"goodsNo\":\"gCode_jt20023\",\"isExpire\":\"1\",\"num\":10,\"operateType\":\"1\",\"overdueDate\":\"2020-07-01\",\"productionDate\":\"2018-07-01\",\"type\":\"0\",\"unit\":\"\"}],\"isTopBooks\":\"1\",\"merchantId\":\"2000031\",\"merchantName\":\"健太\",\"orderNo\":\"SJRK00010000029\",\"ownMonth\":\"2020-04\",\"shopCode\":\"\",\"source\":\"0017\",\"sourceDate\":\"2020-04-07 11:05:29\",\"sourceType\":\"0029\",\"storePlatformName\":\"\",\"topidealCode\":\"1000000204\"}";
			//String body="{\"sourceType\":\"4\"}";
			//String body="{\"month\":\"2020-02\",\"depotId\":\"16\",\"merchantId\":2000031}";
			// 云集采销日报String body="{\"snapshotDate\":\"2020-01-09\",\"merchantId\":\"2000031\"}";
			//String body="{\"makerTel\": \"13800138000\", 	\"receiverTel\": \"13895600000\", 	\"externalCode\": \"K202001101457\", 	\"exOrderId\": \"2566122565557\", 	\"tradingDate\": \"2019-12-08 09:01:30\", 	\"goodsAmount\": \"100.01\", 	\"depotId\": 11, 	\"taxes\": \"1.30\", 	\"discount\": 2, 	\"shopName\": \"健太海外专营店\", 	\"receiverProvince\": \"浙江省\", 	\"remark\": \"精品系列\", 	\"receiverCity\": \"宁波市\", 	\"merchantName\": \"嘉宝\", 	\"storePlatformName\": \"1000000310\", 	\"receiverCountry\": \"中国\", 	\"merchantId\": 2000030, 	\"payment\": 39.3, 	\"makerName\": \"张三\", 	\"shopTypeCode\": \"001\", 	\"shopCode\": \"2001101642\", 	\"depotName\": \"综合1仓\", 	\"receiverName\": \"黄五\", 	\"orderGoods\": [{ 		\"goodsNo\": \"Z001-virtual\", 		\"originalDecTotal\": 40, 		\"originalPrice\": 20, 		\"goodsId\": 1004717, 		\"price\": 19.65, 		\"num\": \"2\", 		\"goodsCode\": \"ERP000020000027\", 		\"decTotal\": 39.3, 		\"goodsName\": \"优惠券商品\", 		\"barcode\": \"Z001-virtual\", 		\"commbarcode\": \"Z001-virtual\" 	}], 	\"receiverArea\": \"北仑区\", 	\"makerRegisterNo\": \"12001\", 	\"wayFrtFee\": 0.09, 	\"logisticsName\": \"1530\", 	\"receiverAddress\": \"广东省 中山市 翠景南路雅居乐世纪新城 大碶街道沿山河路白领公寓0820\", 	\"wayTaxFee\": 1.3, 	\"wayIndFee\": \"0.10\", 	\"wayBillNo\": \"\" }";
			//String body="{\"shopCode\":\"100044998\",\"status\":\"5\",\"orderNo\":\"105993395491\",\"startDate\":\"2019-10-30 00:00:00\",\"endDate\":\"2019-12-31 00:00:00\",\"pageNo\":1,\"pageSize\":1,\"tag\":\"2\"}";
			//String body="{\"businessNo\":\"XSO000010000367\",\"depotId\":\"10\",\"depotName\":\"卓志黄埔保税仓\",\"depotType\":\"\",\"goodsList\":[{\"divergenceDate\":\"2019-11-21 06:45:48\",\"goodsId\":\"1004704\",\"goodsName\":\"gName_jt20017\",\"goodsNo\":\"GT-1120WY\",\"num\":1,\"unit\":\"\"}],\"merchantId\":\"2000031\",\"merchantName\":\"健太\",\"operateType\":\"0\",\"orderNo\":\"XSO000010000367\",\"source\":\"002\",\"sourceDate\":\"2019-11-21 06:45:48\",\"sourceType\":\"002\"}";

			/*String topic="derp-order-mq";
			String tags="projucet-quato-warning-tags";
			String body="{}";*/
/*			String topic="derp-report-mq";
			String tags="report-data-manage-tags";
			String body="{\"merchantId\":1000031,\"month\":\"2021-07\"}";*/
			/*String topic="derp-order-mq";
			String tags="projucet-quato-warning-tags";
			String body="{}";*/
			
			
			String key=String.valueOf(System.currentTimeMillis());//标识
			Message msg = new Message(topic,tags,key,body.getBytes());
			SendResult sendResult = producer.send(msg);
			System.out.println(sendResult);
			shutdown();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}