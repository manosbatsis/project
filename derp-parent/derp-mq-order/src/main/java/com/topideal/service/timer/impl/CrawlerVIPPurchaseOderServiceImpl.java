package com.topideal.service.timer.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.PlatformPurchaseOrderDao;
import com.topideal.dao.sale.PlatformPurchaseOrderItemDao;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderDTO;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderItemModel;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderModel;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.service.timer.CrawlerVIPPurchaseOderService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

@Service
public class CrawlerVIPPurchaseOderServiceImpl implements CrawlerVIPPurchaseOderService {

    @Autowired
    private ReptileConfigMongoDao reptileConfigMongoDao;
    @Autowired
    private PlatformPurchaseOrderDao platformPurchaseOrderDao ;
    @Autowired
    private PlatformPurchaseOrderItemDao platformPurchaseOrderItemDao ;


    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201148601,model=DERP_LOG_POINT.POINT_13201148601_Label)
    public void insertVIPPurchaseOder(String json, String keys, String topics, String tags) throws Exception {

        // 获取上个月1号时间
        Timestamp getfirstDate = TimeUtils.getfirstDate();
        String userCode = null ;

        // 获取指定日期前同步
        JSONObject jsonObj = JSONObject.fromObject(json);
        if(jsonObj.get("date") != null) {
            String date = jsonObj.getString("date") ;

            getfirstDate = TimeUtils.parse(date, "yyyy-MM-dd") ;
        }

        if(jsonObj.get("userCode") != null) {
            userCode = jsonObj.getString("userCode") ;
        }

        String lastMonthDay = TimeUtils.formatFullTime(getfirstDate);// 上个月第一天时间

        Class.forName(ApolloUtilDB.jdbforName);
        Connection conn = DriverManager.getConnection(ApolloUtilDB.crawlerUrl, ApolloUtilDB.crawlerUserName,ApolloUtilDB.crawlerPassword);

        if (conn==null) {
            throw new RuntimeException ("jdbc 链接失败");
        }

        /**
         * 新增或更新平台采购单
         */
        saveOrUpdatePurchaseOrder(conn, lastMonthDay, userCode) ;

        /**
         * 更新平台采购单入库信息
         */
        saveOrUpdateReceiptInfo(conn, lastMonthDay, userCode) ;

        conn.close();
    }

    private void saveOrUpdateReceiptInfo(Connection conn, String lastMonthDay, String userCode) throws SQLException {

        lastMonthDay = "'"+lastMonthDay+"'";
        String queryTotalStr =  "SELECT COUNT(*) as totalNum from vip_receipt_info where userCode = ? and (createdAt >= " + lastMonthDay + " or modifiedAt>="+lastMonthDay+")";
        String queryStr = "SELECT *  from vip_receipt_info where  userCode = ? and (createdAt >= "+lastMonthDay + " or modifiedAt>="+lastMonthDay+")  order by storageTime desc limit ?, ? ";
        String queryItemStr = "SELECT sum(quantity)  as quantity from vip_receipt_detail where reId = ? and barcode = ? ";

        Map<String, Object> queryMap = new HashMap<String, Object>() ;

        queryMap.put("platformType", DERP.CRAWLER_TYPE_2);

        if(StringUtils.isNotBlank(userCode)) {

            queryMap.put("loginName", userCode);

        }

        List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(queryMap);

        Map<String, PlatformPurchaseOrderModel> reIdPlatfromModelMap = new HashMap<String, PlatformPurchaseOrderModel>() ;

        PreparedStatement totalStatement = conn.prepareStatement(queryTotalStr);
        PreparedStatement resultStatement = conn.prepareStatement(queryStr);
        PreparedStatement resultItemStatement = conn.prepareStatement(queryItemStr);

        for (ReptileConfigMongo reptileConfigMongo : reptileConfigList) {

            totalStatement.setString(1, reptileConfigMongo.getLoginName());

            ResultSet resultMap = totalStatement.executeQuery();

            while (resultMap.next()) {

                Integer totalNum = resultMap.getInt("totalNum");
                Integer pageSize = 1000;
                Integer pageNum = totalNum / pageSize;

                if (totalNum % 1000 != 0) {
                    pageNum += 1;
                }

                for (int i = 0; i < pageNum; i++) {

                    int start = i * pageSize;

                    //设置userCode
                    resultStatement.setString(1, reptileConfigMongo.getLoginName());
                    //设置页数
                    resultStatement.setInt(2, start);
                    resultStatement.setInt(3, pageSize);

                    ResultSet resultSet = resultStatement.executeQuery();

                    while (resultSet.next()) {

                        String poNo = resultSet.getString("poNo");

                        PlatformPurchaseOrderModel queryModel = new PlatformPurchaseOrderModel() ;
                        queryModel.setMerchantId(reptileConfigMongo.getMerchantId());
                        queryModel.setCustomerId(reptileConfigMongo.getCustomerId());
                        queryModel.setPoNo(poNo);

                        queryModel = platformPurchaseOrderDao.searchByModel(queryModel) ;

                        if(queryModel == null){
                            continue;
                        }

                        Timestamp deliverDate = queryModel.getDeliverDate();
                        Timestamp storageTime = resultSet.getTimestamp("storageTime");

                        /**
                         * 1、当平台采购单发货时间为空，唯品爬虫入库不为空时，更新发货时间
                         *
                         * 2、当唯品爬虫发货时间大于平台采购单入库时间
                         */
                        if((deliverDate == null && storageTime != null)
                            || (storageTime != null && (storageTime.compareTo(deliverDate) > 0))){
                            //更新入库时间
                            queryModel.setDeliverDate(storageTime) ;
                            queryModel.setModifyDate(TimeUtils.getNow());

                            platformPurchaseOrderDao.modify(queryModel) ;
                        }



                        reIdPlatfromModelMap.put(resultSet.getString("reId"), queryModel) ;

                    }

                }

            }
        }

        for (String reId: reIdPlatfromModelMap.keySet()) {

            PlatformPurchaseOrderModel platformPurchaseOrderModel = reIdPlatfromModelMap.get(reId);

            PlatformPurchaseOrderItemModel queryItemModel = new PlatformPurchaseOrderItemModel() ;
            queryItemModel.setOrderId(platformPurchaseOrderModel.getId());

            List<PlatformPurchaseOrderItemModel> itemList = platformPurchaseOrderItemDao.list(queryItemModel);

            for (PlatformPurchaseOrderItemModel item: itemList) {

                String barcode = item.getPlatformBarcode();

                resultItemStatement.setString(1, reId);
                resultItemStatement.setString(2, barcode);

                ResultSet resultSet = resultItemStatement.executeQuery() ;

                while (resultSet.next()){

                    Integer quantity = resultSet.getInt("quantity");

                    if(quantity == 0){
                        continue;
                    }

                    Integer receiptOkNum = item.getReceiptOkNum();
                    quantity += receiptOkNum ;

                    item.setReceiptOkNum(quantity);
                    item.setModifyDate(TimeUtils.getNow());

                    platformPurchaseOrderItemDao.modify(item) ;

                }

            }

        }

        resultItemStatement.close();
        resultStatement.close();
        totalStatement.close();
    }

    /***
     * 构造表体
     * @param poNo
     * @return
     * @throws SQLException
     */
    private List<PlatformPurchaseOrderItemModel> generateItem(ResultSet itemResultSet, ReptileConfigMongo reptileConfigMongo) throws SQLException {

        List<PlatformPurchaseOrderItemModel> itemList = new ArrayList<PlatformPurchaseOrderItemModel>() ;

        while (itemResultSet.next()) {

            PlatformPurchaseOrderItemModel itemModel = new PlatformPurchaseOrderItemModel() ;

            itemModel.setMerchantId(reptileConfigMongo.getMerchantId());
            itemModel.setUserCode(reptileConfigMongo.getLoginName());
            itemModel.setPlatformBarcode(itemResultSet.getString("barcode"));
            itemModel.setPlatformGoodsName(itemResultSet.getString("goodsNameCN"));
            itemModel.setNum(itemResultSet.getInt("purchaseQuantity"));
            itemModel.setPrice(itemResultSet.getBigDecimal("supplyPrice"));
            itemModel.setAmount(itemResultSet.getBigDecimal("amount"));
            itemModel.setReceiptOkNum(0);
            itemModel.setReceiptBadNum(0);

            itemList.add(itemModel) ;

        }

        return itemList;
    }

    /**
     * 新增或更新平台采购单
     *
     * @param conn
     * @param lastMonthDay
     * @param userCode
     * @throws Exception
     */
    private void saveOrUpdatePurchaseOrder(Connection conn, String lastMonthDay, String userCode) throws Exception {

        lastMonthDay = "'"+lastMonthDay+"'";
        String queryTotalStr =  "SELECT COUNT(*) as totalNum from vip_purchase_order where userCode = ? and (createdAt >= " + lastMonthDay + " or modifiedAt>="+lastMonthDay+")";
        String queryStr = "SELECT *  from vip_purchase_order where  userCode = ? and (createdAt >= "+lastMonthDay + " or modifiedAt>="+lastMonthDay+")  limit ?, ? ";
        String queryItemStr = "SELECT *  from vip_purchase_order_detail where poNo = ? ";

        Map<String, Object> queryMap = new HashMap<String, Object>() ;

        queryMap.put("platformType", DERP.CRAWLER_TYPE_2);

        if(StringUtils.isNotBlank(userCode)) {

            queryMap.put("loginName", userCode);

        }

        List<ReptileConfigMongo> reptileConfigList = reptileConfigMongoDao.findAll(queryMap);

        PreparedStatement totalStatement = conn.prepareStatement(queryTotalStr);
        PreparedStatement resultStatement = conn.prepareStatement(queryStr);
        PreparedStatement resultItemStatement = conn.prepareStatement(queryItemStr);

        for (ReptileConfigMongo reptileConfigMongo : reptileConfigList) {

            List<PlatformPurchaseOrderDTO> dtoList = new ArrayList<PlatformPurchaseOrderDTO>() ;

            totalStatement.setString(1, reptileConfigMongo.getLoginName());

            ResultSet resultMap = totalStatement.executeQuery();

            while (resultMap.next()) {

                Integer totalNum = resultMap.getInt("totalNum");
                Integer pageSize = 1000;
                Integer pageNum = totalNum / pageSize ;

                if(totalNum % 1000 != 0){
                    pageNum +=1;
                }

                for(int i = 0; i < pageNum; i++){

                    int start = i * pageSize ;

                    //设置userCode
                    resultStatement.setString(1, reptileConfigMongo.getLoginName());
                    //设置页数
                    resultStatement.setInt(2, start);
                    resultStatement.setInt(3, pageSize);

                    ResultSet resultSet = resultStatement.executeQuery();

                    while(resultSet.next()) {

                        String poNo = resultSet.getString("poNo");

                        PlatformPurchaseOrderDTO platformPurchaseOrderModel = new PlatformPurchaseOrderDTO() ;

                        platformPurchaseOrderModel.setOrderSource(DERP.CRAWLER_TYPE_2);
                        platformPurchaseOrderModel.setCustomerId(reptileConfigMongo.getCustomerId());
                        platformPurchaseOrderModel.setCustomerName(reptileConfigMongo.getCustomerName());
                        platformPurchaseOrderModel.setMerchantId(reptileConfigMongo.getMerchantId());
                        platformPurchaseOrderModel.setMerchantName(reptileConfigMongo.getMerchantName());
                        platformPurchaseOrderModel.setCurrency(resultSet.getString("currencyCode"));
                        platformPurchaseOrderModel.setPrNo(resultSet.getString("prId"));
                        platformPurchaseOrderModel.setOrderTime(resultSet.getTimestamp("createTime"));
                        platformPurchaseOrderModel.setPoNo(poNo);
                        platformPurchaseOrderModel.setPlatformDepotName(resultSet.getString("warehouseName"));

                        String stateName = resultSet.getString("statusName");
                        //stateName = String.valueOf(DERP_ORDER.getKeyByLabel(DERP_ORDER.platformPurchase_platformStatusList, stateName)) ;

                        platformPurchaseOrderModel.setPlatformStatus(stateName);

                        // 构造表体
                        resultItemStatement.setString(1, poNo);
                        ResultSet itemResultSet = resultItemStatement.executeQuery();

                        List<PlatformPurchaseOrderItemModel> itemList = generateItem(itemResultSet ,reptileConfigMongo) ;
                        platformPurchaseOrderModel.setItemList(itemList);

                        //汇总数量
                        BigDecimal totalAmount = itemList.stream().map(PlatformPurchaseOrderItemModel::getAmount)
                                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

                        Integer num = itemList.stream().mapToInt(PlatformPurchaseOrderItemModel::getNum).sum() ;

                        platformPurchaseOrderModel.setAmount(totalAmount);
                        platformPurchaseOrderModel.setNum(num);

                        dtoList.add(platformPurchaseOrderModel) ;
                    }

                }
            }

            for (PlatformPurchaseOrderDTO dto : dtoList) {

                PlatformPurchaseOrderModel queryModel = new PlatformPurchaseOrderModel() ;
                queryModel.setMerchantId(dto.getMerchantId());
                queryModel.setCustomerId(dto.getCustomerId());
                queryModel.setPoNo(dto.getPoNo());

                queryModel = platformPurchaseOrderDao.searchByModel(queryModel) ;

                PlatformPurchaseOrderModel model = new PlatformPurchaseOrderModel() ;

                BeanUtils.copyProperties(dto, model);

                if(queryModel == null) {

                    model.setStatus(DERP_ORDER.PLATFORM_PURCHASE_STATUS_2);
                    model.setCreateDate(TimeUtils.getNow());
                    model.setUserCode(reptileConfigMongo.getLoginName());
                    platformPurchaseOrderDao.save(model) ;

                }else {

                    model.setId(queryModel.getId());
                    model.setModifyDate(TimeUtils.getNow());
                    platformPurchaseOrderDao.modify(model) ;

                }

                List<PlatformPurchaseOrderItemModel> itemList = dto.getItemList();

                for (PlatformPurchaseOrderItemModel itemModel : itemList) {

                    PlatformPurchaseOrderItemModel queryItemModel = new PlatformPurchaseOrderItemModel() ;
                    queryItemModel.setOrderId(model.getId());
                    queryItemModel.setPlatformBarcode(itemModel.getPlatformBarcode());

                    queryItemModel = platformPurchaseOrderItemDao.searchByModel(queryItemModel) ;

                    if(queryItemModel == null) {

                        itemModel.setOrderId(model.getId());
                        itemModel.setCreateDate(TimeUtils.getNow());

                        platformPurchaseOrderItemDao.save(itemModel) ;

                    }else {

                        itemModel.setId(queryItemModel.getId());
                        itemModel.setModifyDate(TimeUtils.getNow());

                        platformPurchaseOrderItemDao.modify(itemModel) ;
                    }

                }
            }


        }

        resultItemStatement.close();
        resultStatement.close();
        totalStatement.close();

    }
}
