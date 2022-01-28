package com.topideal.order.service.sale.impl;


import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.OrderDao;
import com.topideal.dao.sale.OrderReturnIdepotDao;
import com.topideal.dao.sale.PlatformTemporaryCostOrderDao;
import com.topideal.dao.sale.PlatformTemporaryCostOrderItemDao;
import com.topideal.entity.dto.common.PlatformTemporaryCostOrderItemDTO;
import com.topideal.entity.dto.sale.PlatformTemporaryCostOrderDTO;
import com.topideal.entity.vo.sale.PlatformTemporaryCostOrderItemModel;
import com.topideal.entity.vo.sale.PlatformTemporaryCostOrderModel;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;
import com.topideal.order.service.sale.PlatformTemporaryCostOrderService;
import com.topideal.order.tools.DownloadExcelUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class PlatformTemporaryCostOrderServiceImpl implements PlatformTemporaryCostOrderService {
    /* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformTemporaryCostOrderServiceImpl.class);
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;
    @Autowired
    private PlatformTemporaryCostOrderDao platformTempCostOrderDao;
    @Autowired
    private PlatformTemporaryCostOrderItemDao platformTemporaryCostOrderItemDao;
    @Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderReturnIdepotDao orderReturnIdepotDao;

    @Override
    public PlatformTemporaryCostOrderDTO listPlatformTemporaryCost(User user, PlatformTemporaryCostOrderDTO dto) throws SQLException {
        PlatformTemporaryCostOrderDTO platformTemporaryCostOrderDTO = platformTempCostOrderDao.listDTOByPage(dto);
        List<PlatformTemporaryCostOrderDTO> list = platformTemporaryCostOrderDTO.getList();

        if(list == null || list.isEmpty()) {
            return platformTemporaryCostOrderDTO;
        }

        Set<Long> idSet = new HashSet<>();
        Map<Long, BigDecimal> orderIdAndItemMap = new HashMap<>();
        list.stream().forEach(entity -> {
            entity.setOrderTypeLabel(DERP_ORDER.getLabelByKey(DERP_ORDER.platformTempCostOrder_orderTypeList, entity.getOrderType()));
            idSet.add(entity.getId());
        });

        // 获取暂估费用金额
        List<PlatformTemporaryCostOrderItemDTO> itemList = platformTemporaryCostOrderItemDao.sumAmountByOrderIds(new ArrayList<>(idSet));
        itemList.stream().forEach(entity -> {
            orderIdAndItemMap.put(entity.getPlatformCostId(), entity.getSettlementAmount());
        });
        list.stream().forEach(entity -> {
            BigDecimal sumAmount = orderIdAndItemMap.get(entity.getId());
            entity.setSumAmount(sumAmount);
        });

        return platformTemporaryCostOrderDTO;
    }

    @Override
    public PlatformTemporaryCostOrderDTO searchDTOById(User user, Long id) throws SQLException {
        PlatformTemporaryCostOrderDTO dto = platformTempCostOrderDao.searchByDTOId(id);
        if(dto != null) {
            dto.setOrderTypeLabel(DERP_ORDER.getLabelByKey(DERP_ORDER.platformTempCostOrder_orderTypeList, dto.getOrderType()));
        }
        List<PlatformTemporaryCostOrderDTO> orderList = dto.getList();
        if(orderList != null && orderList.size() > 0) {
            orderList.stream().forEach(entity -> {
                entity.setOrderTypeLabel(DERP_ORDER.getLabelByKey(DERP_ORDER.platformTempCostOrder_orderTypeList, entity.getOrderType()));
            });
        }

        //根据表头查询表体信息
        PlatformTemporaryCostOrderItemDTO itemModel=new PlatformTemporaryCostOrderItemDTO();
        itemModel.setPlatformCostId(dto.getId());
        List<PlatformTemporaryCostOrderItemDTO> list=platformTemporaryCostOrderItemDao.listPlatformTemporaryCostItemDTO(itemModel);
        dto.setItemList(list);

        BigDecimal amount=new BigDecimal(0);//结算总金额
        BigDecimal settlementAmount=new BigDecimal(0);//暂估费用总金额
        for(PlatformTemporaryCostOrderItemDTO item:list){
            amount=amount.add(item.getAmount());
            settlementAmount=settlementAmount.add(item.getSettlementAmount());
        }
        dto.setSumAmount(settlementAmount);
        dto.setSumAmountCost(amount);
        return dto;
    }

    @Override
    public Boolean checkDate(User user, Long id) {
        boolean flag=true;
        PlatformTemporaryCostOrderDTO dto=platformTempCostOrderDao.searchByDTOId(id);
        // 判断发货日期是否小于 关账月结日期
        FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
        closeAccountsMongo.setMerchantId(dto.getMerchantId());
        closeAccountsMongo.setDepotId(dto.getDepotId());
        String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
        String maxCloseAccountsMonth = "";
        if (org.apache.commons.lang.StringUtils.isNotBlank(maxdate)) {
            // 获取该月份下月时间
            String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
            maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
        }
        if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
            // 关账下个月日期大于 入库日期
            if (dto.getDeliverDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
                flag=false;
            }
        }
        return flag;
    }

    @Override
    public Map deleteById(User user, String ids) throws SQLException {
        Map map = new HashMap();
        List<Long> list = StrUtils.parseIds(ids);

        //存储删除的表头id
        List<Long> itemIdList = new ArrayList<>();

        //删除的发货订单
        List<String> orderCodes = new ArrayList<>();
        //删除的退款单
        List<String> returnOrderCodes = new ArrayList<>();

        for (Long id : list) {
            PlatformTemporaryCostOrderDTO dto = platformTempCostOrderDao.searchByDTOId(id);
            // 判断发货日期是否小于 关账月结日期
            FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
            closeAccountsMongo.setMerchantId(dto.getMerchantId());
            closeAccountsMongo.setDepotId(dto.getDepotId());
            String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG2);
            String maxCloseAccountsMonth = "";
            if (org.apache.commons.lang.StringUtils.isNotBlank(maxdate)) {
                // 获取该月份下月时间
                String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
                maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
            }
            if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
                // 关账下个月日期大于 入库日期
                if (dto.getDeliverDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
                    map.put("code", "01");
                    map.put("message", "暂估费用单号为:" + dto.getCode() + "所在月份已经关账,不能删除！");
                    return map;
                }
            }
            itemIdList.add(dto.getId());
            if (DERP_ORDER.PLATFORMTEMPCOSTORDER_ORDERTYPE_0.equals(dto.getOrderType())) {
                orderCodes.add(dto.getOrderCode());
            }

            if (DERP_ORDER.PLATFORMTEMPCOSTORDER_ORDERTYPE_1.equals(dto.getOrderType())) {
                returnOrderCodes.add(dto.getReturnOrderCode());
            }
        }

        //删除表头
        platformTempCostOrderDao.delete(list);
        //删除表体信息
        platformTemporaryCostOrderItemDao.deleteCostOrderItemById(itemIdList);
        //批量更新电商订单是否生成暂估费用
        if (!orderCodes.isEmpty()) {
            orderDao.batchUpdateStatusByCode(DERP_ORDER.ORDER_IS_GENERATE_0, orderCodes);
        }

        //批量更新退款订单是否生成暂估费用
        if (!returnOrderCodes.isEmpty()) {
            orderReturnIdepotDao.batchUpdateStatusByCode(DERP_ORDER.ORDER_IS_GENERATE_0, returnOrderCodes);
        }

        map.put("code", "00");
        map.put("message", "删除成功！");
        return map;
    }

    @Override
    public Map updatePlatformTemporary(User user, PlatformTemporaryCostOrderDTO dto) throws SQLException {
        Map map=new HashMap();
        PlatformTemporaryCostOrderDTO model=platformTempCostOrderDao.searchByDTOId(dto.getId());
        if(model!=null){
            List<PlatformTemporaryCostOrderItemModel> list=new ArrayList<>();
            PlatformTemporaryCostOrderItemModel searchModel=new PlatformTemporaryCostOrderItemModel();
            searchModel.setPlatformCostId(model.getId());
            List<PlatformTemporaryCostOrderItemModel> searchList=platformTemporaryCostOrderItemDao.list(searchModel);
            //校验金额是否为空
            for(PlatformTemporaryCostOrderItemDTO itemDTO:dto.getItemList()){
                if (itemDTO.getSettlementAmount() == null) {
                    map.put("code", "01");
                    map.put("message", "暂估费用金额不能为空！");
                    return map;
                }
                itemDTO.setSettlementAmount(itemDTO.getSettlementAmount());
                itemDTO.setModifyDate(TimeUtils.getNow());
                itemDTO.setPlatformCostId(model.getId());
                PlatformTemporaryCostOrderItemModel modelItem=new PlatformTemporaryCostOrderItemModel();
                BeanUtils.copyProperties(itemDTO,modelItem);
                platformTemporaryCostOrderItemDao.modify(modelItem);
            }
            //删除原有的表体数据
            //platformTemporaryCostOrderItemDao.deleteCostOrderItemById(Arrays.asList(model.getId()));
            //修改
            PlatformTemporaryCostOrderModel orderModel=new PlatformTemporaryCostOrderModel();
            orderModel.setId(model.getId());
            orderModel.setModifyDate(TimeUtils.getNow());
            orderModel.setModifier(user.getId());
            orderModel.setModifyName(user.getName());
            platformTempCostOrderDao.modify(orderModel);
        }
        map.put("code","00");
        map.put("message","修改成功！");
        return map;
    }

    @Override
    public List<Map<String, Object>> listForMapItem(PlatformTemporaryCostOrderDTO dto) {
        try {
            List<Map<String, Object>> exportList = new ArrayList<>();

            int exportItemNum = platformTempCostOrderDao.getPlatFormTemporaryCount(dto);
            int pageSize=1000;
            for (int i = 0; i< exportItemNum; ) {
                int pageSub = (i + pageSize) < exportItemNum ? (i + pageSize) : exportItemNum;
                dto.setBegin(i);
                dto.setPageSize(pageSize);
                List<Map<String, Object>> itemList=platformTempCostOrderDao.listForMapItem(dto);
                for(int j=0;j<itemList.size();j++){
                    Map<String, Object> map = new HashMap<>();
                    map.put("merchantName",itemList.get(j).get("merchant_name"));
                    map.put("code",itemList.get(j).get("code"));
                    map.put("externalCode",itemList.get(j).get("external_code"));
                    map.put("orderCode",itemList.get(j).get("order_code"));
                    map.put("storePlatformName", DERP.getLabelByKey(DERP.storePlatformList, itemList.get(j).get("store_platform_code")));
                    map.put("shopName",itemList.get(j).get("shop_name"));
                    map.put("deliverDate",itemList.get(j).get("deliver_date"));
                    map.put("buName",itemList.get(j).get("bu_name"));
                    map.put("platformSettlementName",itemList.get(j).get("platform_settlement_name"));
                    map.put("amount",itemList.get(j).get("amount"));
                    map.put("ratio",itemList.get(j).get("ratio"));
                    map.put("settlementAmount",itemList.get(j).get("settlement_amount"));
                    exportList.add(map);
                }
                i = pageSub;
            }
            return exportList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map savePlatformCostOrder(User user, String month, String orderType, String storePlatformCodes) throws SQLException {
        Map map = new HashMap();

        if (StringUtils.isBlank(month) || StringUtils.isBlank(orderType)) {
            map.put("code", "01");
            map.put("message", "数据为空");
            return map;
        }
        /*调用MQ生成暂估费用单*/
        JSONObject json = new JSONObject();
        json.put("merchantId", user.getMerchantId());
        json.put("month", month);
        json.put("orderType", orderType);
        json.put("storePlatformCodes", storePlatformCodes);
        try{
            rocketMQProducer.send(json.toString(), MQOrderEnum.TIMER_PLATFORM_TEMPORARY_COST_ORDER.getTopic(),
                    MQOrderEnum.TIMER_PLATFORM_TEMPORARY_COST_ORDER.getTags()) ;
        }catch (Exception e){
            LOGGER.info("-生成暂估费用单异常-");
            LOGGER.error(e.getMessage(), e);
        }
        map.put("code","00");
        map.put("message","保存成功！");
        return map;
    }

    @Override
    public String createTempCostExcel(FileTaskMongo task, String basePath) throws Exception{
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> paramMap = jsonData;
        Integer merchantId = (Integer) paramMap.get("merchantId");
        String storePlatformCode = (String) paramMap.get("storePlatformCode");

        basePath = basePath + "/" + task.getTaskType() + "/" + merchantId + "/" + storePlatformCode;

        System.out.println("商家Id=" + merchantId + ",平台Code" + storePlatformCode + "，类型：toc平台暂估费用---生成excel文件开始----------");

        boolean isExist = false;

        if (new File(basePath).exists()) {
            isExist = true;
        }

        int pageSize = 5000; //每页5000
        int maxSize = 300000; //每个文件存放最大记录数

        PlatformTemporaryCostOrderDTO dto = new PlatformTemporaryCostOrderDTO();
        dto.setMerchantId(merchantId.longValue());
        dto.setExternalCode((String) paramMap.get("externalCode"));
        dto.setStorePlatformCode((String) paramMap.get("storePlatformCode"));
        dto.setIds((String) paramMap.get("ids"));
        dto.setOrderCode((String) paramMap.get("orderCode"));
        dto.setCode((String) paramMap.get("code"));
        dto.setDeliverStartDate((String) paramMap.get("deliverStartDate"));
        dto.setDeliverEndDate((String) paramMap.get("deliverEndDate"));

        String sheetName = "平台暂估费用单";
        String[] costColumns = {"公司","暂估费用单","外部交易单","电商订单号","平台","店铺名称","发货日期","事业部","平台费项名称","订单实付金额","费项比例","费项金额"};
        String[] costKeys = {"merchantName","code","externalCode","orderCode","storePlatformName","shopName","deliverDate",
                "buName","platformSettlementName","amount","ratio","settlementAmount"};

        int exportItemNum = platformTempCostOrderDao.getPlatFormTemporaryCount(dto);

        //每30w数据生成一个excel
        for (int i = 0; i < Math.ceil((float) exportItemNum / maxSize); i++) {

            List<Map<String, Object>> exportItemList = new ArrayList<>();

            Integer total = exportItemNum > maxSize * (i + 1) ? maxSize * (i + 1) : exportItemNum;

            for (int j = maxSize * i; j < total; ) {
                int pageSub = (j + pageSize) < total ? (j + pageSize) : total;
                dto.setBegin(j);
                dto.setPageSize(pageSize);
                List<Map<String, Object>> itemList=platformTempCostOrderDao.listForMapItem(dto);

                for (Map<String, Object> item : itemList) {
                    item.put("merchantName", item.get("merchant_name"));
                    item.put("code",item.get("code"));
                    item.put("externalCode",item.get("external_code"));
                    item.put("orderCode",item.get("order_code"));
                    item.put("storePlatformName", DERP.getLabelByKey(DERP.storePlatformList, item.get("store_platform_code")));
                    item.put("shopName",item.get("shop_name"));
                    item.put("deliverDate",item.get("deliver_date"));
                    item.put("buName",item.get("bu_name"));
                    item.put("platformSettlementName",item.get("platform_settlement_name"));
                    item.put("amount",item.get("amount"));
                    item.put("ratio",item.get("ratio"));
                    item.put("settlementAmount",item.get("settlement_amount"));
                }

                exportItemList.addAll(itemList);
                j = pageSub;
            }

            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel8(sheetName, costColumns, costKeys, exportItemList);
            //导出文件
            String fileName = task.getRemark() + i + ".xlsx";

            if (isExist) {
                //删除目录下的子文件
                DownloadExcelUtil.deleteFile(basePath);
                isExist = false;
            }

            //创建目录
            new File(basePath).mkdirs();

            FileOutputStream fileOut = new FileOutputStream(basePath + "/" + fileName);
            wb.write(fileOut);
            fileOut.close();

            System.out.println("第" + i + "个文件创建结束");
        }
        return basePath;
    }
}
