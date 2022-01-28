package com.topideal.service.order.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.sale.OrderDao;
import com.topideal.dao.sale.OrderItemDao;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.order.AmountCoverService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 电商订单金额更新
 */
@Service
public class AmountCoverServiceImpl implements AmountCoverService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AmountCoverServiceImpl.class);

	// 电商订单
	@Autowired
	private OrderDao orderDao;
    //电商订单表体
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;

	/**
	 * 电商订单金额更新
	 * @throws Exception
	 */
	@Override
    @SystemServiceLog(point = DERP_LOG_POINT.POINT_13201129700, model = DERP_LOG_POINT.POINT_13201129700_Label, keyword = "code")
	public boolean synsAmountCover(String json, String keys, String topics, String tags) throws Exception {

        JSONObject jsonData = JSONObject.fromObject(json);
        String topidealCode = (String) jsonData.get("topidealCode");
        String code = (String) jsonData.get("code");
        String paymentStr = jsonData.get("payment").toString();//订单实付金额
        String taxesStr = jsonData.get("taxes").toString();//税费
        String wayFrtFeeStr = jsonData.get("wayFrtFee").toString();//运费
        String discountStr = jsonData.get("discount").toString();//订单优惠减免金额

        Set<String> codeSet = new HashSet<>();

        //公司
        Map<String, Object> merParam = new HashMap<>();
        merParam.put("topidealCode", topidealCode);
        MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merParam);

        //1.检查订单是否存在
        OrderModel orderModelParam = new OrderModel();
        orderModelParam.setExternalCode(code);
        orderModelParam.setMerchantId(merchantInfo.getMerchantId());
        List<OrderModel> orderList = orderDao.list(orderModelParam);

        // 过滤已删除的订单
        orderList = orderList.stream().filter(order -> !DERP.DEL_CODE_006.equals(order.getStatus()))
                .collect(Collectors.toList()) ;

        if (orderList.isEmpty()) {
            LOGGER.error("商家卓志编码：" + topidealCode + "，外部订单号：" + code + "不存在");
            throw new RuntimeException("商家卓志编码：" + topidealCode + "，外部订单号：" + code + "不存在");

        }

        if (orderList.size() > 1) {
            LOGGER.error("商家卓志编码：" + topidealCode + "，外部订单号：" + code + "存在多条");
            throw new RuntimeException("商家卓志编码：" + topidealCode + "，外部订单号：" + code + "存在多条");
        }

        if (codeSet.contains(topidealCode + "_" + code)) {
            LOGGER.error("商家卓志编码：" + topidealCode + "，外部订单号：" + code + "导入模板存在多条");
            throw new RuntimeException("商家卓志编码：" + topidealCode + "，外部订单号：" + code + "导入模板存在多条");
        }

        codeSet.add(topidealCode + "_" + code);
        OrderModel orderModel = orderList.get(0);

		/*
		 * if(orderModel.getPayment().compareTo(new BigDecimal(paymentStr)) == 0) {
		 * LOGGER.error("商家卓志编码：" + topidealCode + "，外部订单号：" + code + "订单实付金额与原订单一致");
		 * throw new RuntimeException("商家卓志编码：" + topidealCode + "，外部订单号：" + code +
		 * "订单实付金额与原订单一致"); }
		 */

        OrderItemModel orderItemModel = new OrderItemModel();
        orderItemModel.setOrderId(orderModel.getId());
        List<OrderItemModel> orderItemModels = orderItemDao.list(orderItemModel);

        //原所有商品销售总金额
        BigDecimal oriTotalGoodsAmount = new BigDecimal("0");
        for (OrderItemModel itemModel : orderItemModels) {
            //发货时间不为空则检查月结、关账时间。
           /* FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
            closeAccountsMongo.setMerchantId(orderModel.getMerchantId());
            closeAccountsMongo.setDepotId(orderModel.getDepotId());
            closeAccountsMongo.setBuId(itemModel.getBuId());
            String maxMonth = "";
            maxMonth = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
            if(orderModel.getDeliverDate()!=null&&StringUtils.isNotBlank(maxMonth)) {
                //获取最大关账月份下一个月1日时间
                String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxMonth + "-01 00:00:00"));
                String maxCloseNextMonthDate = nextMonth + "-01 00:00:00";
                // 关账下个月日期大于 入库日期
                if (orderModel.getDeliverDate().getTime() <Timestamp.valueOf(maxCloseNextMonthDate).getTime()) {
                    LOGGER.error("商家卓志编码：" + topidealCode + "，外部订单号：" + code + "发货日期的月份已关账");
                    throw new RuntimeException("商家卓志编码：" + topidealCode + "，外部订单号：" + code + "发货日期的月份已关账");
                }
            }*/
            oriTotalGoodsAmount = oriTotalGoodsAmount.add(itemModel.getOriginalDecTotal());
        }

        List<OrderItemModel> orderItemModelList = new ArrayList<>();

        //销售总金额=订单实付金额-运费-税费+优惠减免金额
        BigDecimal totalPayment = new BigDecimal(paymentStr).subtract(new BigDecimal(wayFrtFeeStr)).subtract(new BigDecimal(taxesStr)).add(new BigDecimal(discountStr));
        //结算总金额：订单实付金额-运费-税费
        BigDecimal totalAmount = new BigDecimal(paymentStr).subtract(new BigDecimal(wayFrtFeeStr)).subtract(new BigDecimal(taxesStr));

        BigDecimal lessTotalPayment = totalPayment;
        BigDecimal lessTotalAmount = totalAmount;
        for (int i = 0; i < orderItemModels.size(); i++) {
            OrderItemModel oriItem = orderItemModels.get(i);
            if (i == orderItemModels.size()-1) {
                //销售单价
                BigDecimal price = lessTotalPayment.divide(new BigDecimal(oriItem.getNum()), 2, BigDecimal.ROUND_HALF_UP);

                //结算单价
                BigDecimal settlePrice = lessTotalAmount.divide(new BigDecimal(oriItem.getNum()),2, BigDecimal.ROUND_HALF_UP);

                OrderItemModel addItem = new OrderItemModel();
                addItem.setId(oriItem.getId());
                addItem.setOriginalDecTotal(lessTotalPayment.setScale(2, BigDecimal.ROUND_HALF_UP));
                addItem.setOriginalPrice(price);
                addItem.setDecTotal(lessTotalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
                addItem.setPrice(settlePrice);
                addItem.setGoodsDiscount(BigDecimal.ZERO);
                orderItemModelList.add(addItem);
            } else {
                //权重计算公式：权重=原该商品销售总金额/原所有商品销售总金额
                BigDecimal rate = oriItem.getOriginalPrice().multiply(new BigDecimal(oriItem.getNum())).divide(oriTotalGoodsAmount, 8, BigDecimal.ROUND_HALF_UP);

                //(销售总金额：订单实付金额-运费-税费+优惠减免金额)*权重
                BigDecimal itemPayment = totalPayment.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
                lessTotalPayment = lessTotalPayment.subtract(itemPayment);
                //销售单价
                BigDecimal price = itemPayment.divide(new BigDecimal(oriItem.getNum()), 2, BigDecimal.ROUND_HALF_UP);

                //(结算总金额：订单实付金额-运费-税费)*权重
                BigDecimal itemAmount = totalAmount.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
                lessTotalAmount = lessTotalAmount.subtract(itemAmount);

                //结算单价
                BigDecimal settlePrice = itemAmount.divide(new BigDecimal(oriItem.getNum()), 2, BigDecimal.ROUND_HALF_UP);

                OrderItemModel addItem = new OrderItemModel();
                addItem.setId(oriItem.getId());
                addItem.setOriginalDecTotal(itemPayment);
                addItem.setOriginalPrice(price);
                addItem.setDecTotal(itemAmount);
                addItem.setPrice(settlePrice);
                addItem.setGoodsDiscount(BigDecimal.ZERO);
                orderItemModelList.add(addItem);
            }
        }

        if (orderItemModelList.size() > 0) {
            orderItemDao.batchUpdate(orderItemModelList);
        }

		//更新表头金额
        orderModel.setPayment(new BigDecimal(paymentStr));//实付金额
        orderModel.setTaxes(new BigDecimal(taxesStr));//税费
        orderModel.setWayFrtFee(new BigDecimal(wayFrtFeeStr));//运费
        orderModel.setDiscount(new BigDecimal(discountStr));//优惠减免金额
        orderDao.modify(orderModel);
        return true;
	}
}