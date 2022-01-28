package com.topideal.order.service.bill.impl;


import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.dao.bill.*;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.vo.bill.ReceiveAgingReportItemModel;
import com.topideal.entity.vo.bill.ReceiveAgingReportModel;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.bill.ReceiveAgingReportService;
import com.topideal.order.tools.DownloadExcelUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReceiveAgingReportServiceImpl implements ReceiveAgingReportService {

    @Autowired
    private ReceiveAgingReportDao receiveAgingReportDao;
    @Autowired
    private ReceiveAgingReportItemDao receiveAgingReportItemDao;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private TobTemporaryReceiveBillCostItemMonthlyDao tobTemporaryReceiveBillCostItemMonthlyDao;
    @Autowired
    private TobTemporaryReceiveBillItemMonthlyDao tobTemporaryReceiveBillItemMonthlyDao;
    @Autowired
    private BillMonthlySnapshotDao billMonthlySnapshotDao;
    @Autowired
    private TocTemporaryReceiveBillCostItemMonthlyDao tocTemporaryReceiveBillCostItemMonthlyDao;
    @Autowired
    private TocTemporaryReceiveBillItemMonthlyDao tocTemporaryReceiveBillItemMonthlyDao;
    @Autowired
    private TobTemporaryReceiveBillItemDao tobTemporaryReceiveBillItemDao;
    @Autowired
    private TobTemporaryReceiveBillRebateItemDao tobTemporaryReceiveBillRebateItemDao;
    @Autowired
    private ExchangeRateMongoDao exchangeRateMongoDao;
    @Autowired
    private AccountingReminderItemDao accountingReminderItemDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private BusinessUnitMongoDao businessUnitMongoDao;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;



    @Override
    public ReceiveAgingReportDTO listReceiveBillByPage(ReceiveAgingReportDTO dto, User user) throws SQLException {

        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }

        return receiveAgingReportDao.listReceiveBillByPage(dto);
    }

    @Override
    public Map<String, Object> getMaxRefrshDate( Long merchantId) throws SQLException {
        Map<String, Object> map=receiveAgingReportDao.getMaxRefrshDate(merchantId);

        return map;
    }

    @Override
    public List<ReceiveAgingReportDTO> listForExport(ReceiveAgingReportDTO dto, User user) {
        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                return new ArrayList<>();
            }
            dto.setBuList(buList);
        }

        return receiveAgingReportDao.listForExport(dto);
    }

    @Override
    public Map<String, Object> refreshReceiveAgingReport(ReceiveAgingReportDTO dto) {
        Map<String, Object> resultMap=new HashMap<>();

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("buId", dto.getBuId()==null?"":dto.getBuId());
        body.put("customerId", dto.getCustomerId()==null?"":dto.getCustomerId()) ;
        body.put("merchantId", dto.getMerchantId()) ;
        body.put("currency", dto.getCurrency()) ;
        body.put("channelType",dto.getChannelType());

        try{
            JSONObject json = JSONObject.fromObject(body);
            System.out.println(json.toString());
            SendResult result = rocketMQProducer.send(json.toString(), MQOrderEnum.TIMER_RECEIVEAGING_REPORT_ORDER.getTopic(),MQOrderEnum.TIMER_RECEIVEAGING_REPORT_ORDER.getTags());
            System.out.println(result);
            if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
                resultMap.put("code", "01");
                resultMap.put("message", "刷新消息发送失败");
            }else{
                resultMap.put("code", "00");
                resultMap.put("message", "刷新成功");
            }
            return resultMap;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public ReceiveAgingReportDTO getDetailsById(Long id) throws SQLException {
        ReceiveAgingReportModel receiveAgingReportDTO=receiveAgingReportDao.searchById(id);

        ReceiveAgingReportDTO dto=new ReceiveAgingReportDTO();
        BeanUtils.copyProperties(receiveAgingReportDTO,dto);

        List<ReceiveAgingReportItemDTO> itemList=receiveAgingReportItemDao.getAgingReportId(id);
        dto.setItemList(itemList);

        BigDecimal amount=new BigDecimal(0);
        for(ReceiveAgingReportItemDTO item:itemList){
            amount=amount.add(item.getWrittenOffAmount());
        }
        dto.setAmount(amount);
        return dto;
    }

    @Override
    public String createMonthlyReportExcel(FileTaskMongo task, String basePath) throws Exception {

        String TYPE_0 = "0"; //0~30天
        String TYPE_1 = "1"; //30~60天
        String TYPE_2 = "2"; //60~90天
        String TYPE_3 = "3"; //90~120天
        String TYPE_4 = "4"; //大于120天
        String[] typeArr = { TYPE_0, TYPE_1, TYPE_2, TYPE_3, TYPE_4 };

        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> paramMap = jsonData;
        String month = (String) paramMap.get("month");
        Integer merchantId = (Integer) paramMap.get("merchantId");
        Integer buId = (Integer) paramMap.get("buId");

        //公司
        Map<String, Object> merchantParam = new HashMap<>();
        merchantParam.put("merchantId", Long.valueOf(merchantId));
        MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchantParam);

        Map<Long, BusinessUnitMongo> buMap = new HashMap<>();
        Map<Long, CustomerInfoMogo> customerMap = new HashMap<>();

        basePath = basePath + "/" + task.getTaskType() + "/" + merchantId + "/" + month;

        String pathSuffix = "all";
        Long buIdLong = null;
        if (buId != null) {
            buIdLong = Long.valueOf(buId);
            pathSuffix = String.valueOf(buId);
        }
        basePath = basePath + "/" + pathSuffix;

        System.out.println("商家Id=" + merchantId + ",月份=" + month + "," + buId + "，类型：toc暂估收入明细---生成excel文件开始----------");

        boolean isExist = false;

        if (new File(basePath).exists()) {
            isExist = true;
        }

        //月结月份最后一天
        String lastDay = TimeUtils.getLastDayEndTime(month, "yyyy-MM-dd");

        List<Map<String, Object>> mapList = new ArrayList<>();

        //tob 类型
        Map<String, Map<String, Object>> tobMonthlyMap = new HashMap<>();
        /**
         * 应收费用（原币）：按照“公司+事业部+客户+销售币种+月份”维度汇总 “Tob 暂估核销表 - 费用月结快照” 中  暂估明细对应的 “未核销暂估金额”，
         * 并加上相同维度“事业部+客户+销售币种”下 “ToC 暂估核销列表 - 费用月结快照” 中运营类型为“一件代发“的 “未核销金额”汇总
         */
        TobTemporaryReceiveBillCostItemMonthlyDTO tobTemporaryReceiveBillCostItemMonthlyDTO = new TobTemporaryReceiveBillCostItemMonthlyDTO();
        tobTemporaryReceiveBillCostItemMonthlyDTO.setLastDay(lastDay);
        tobTemporaryReceiveBillCostItemMonthlyDTO.setBuId(buIdLong);
        tobTemporaryReceiveBillCostItemMonthlyDTO.setMerchantId(Long.valueOf(merchantId));
        tobTemporaryReceiveBillCostItemMonthlyDTO.setMonth(month);
        List<TobTemporaryReceiveBillCostItemMonthlyDTO> costItemMonthlyDTOS = tobTemporaryReceiveBillCostItemMonthlyDao.getMonthlyVerify(tobTemporaryReceiveBillCostItemMonthlyDTO);

        for (TobTemporaryReceiveBillCostItemMonthlyDTO costItemMonthlyDTO : costItemMonthlyDTOS) {
            String key = costItemMonthlyDTO.getMerchantId() + "_" + costItemMonthlyDTO.getBuId() + "_" + costItemMonthlyDTO.getCustomerId() + "_"
                    + costItemMonthlyDTO.getCurrency() + "_" + costItemMonthlyDTO.getMonth();

            BigDecimal receiveAmount = tobTemporaryReceiveBillRebateItemDao.listRebateItemPriceByIds(costItemMonthlyDTO);

            BigDecimal nonVerifyAmount = receiveAmount.subtract(costItemMonthlyDTO.getVerifiedAmount());

            Map<String, Object> tempItemMap = tobMonthlyMap.get(key);
            if (tempItemMap == null) {
                tempItemMap = new HashMap<>();
                tempItemMap.put("merchantId", costItemMonthlyDTO.getMerchantId());
                tempItemMap.put("merchantName", merchant.getName());
                tempItemMap.put("customerId", costItemMonthlyDTO.getCustomerId());
                tempItemMap.put("buId", costItemMonthlyDTO.getBuId());
                tempItemMap.put("currency", costItemMonthlyDTO.getCurrency());
                tempItemMap.put("month", costItemMonthlyDTO.getMonth());
                tempItemMap.put("receiveAmount", BigDecimal.ZERO);
                tempItemMap.put("type0Amount", BigDecimal.ZERO);
                tempItemMap.put("type1Amount", BigDecimal.ZERO);
                tempItemMap.put("type2Amount", BigDecimal.ZERO);
                tempItemMap.put("type3Amount", BigDecimal.ZERO);
                tempItemMap.put("type4Amount", BigDecimal.ZERO);
            }

            if (tempItemMap.containsKey("receiveCost")) {
                BigDecimal receiveCost = (BigDecimal) tempItemMap.get("receiveCost");
                nonVerifyAmount = nonVerifyAmount.add(receiveCost);
            }

            tempItemMap.put("receiveCost", nonVerifyAmount);
            tobMonthlyMap.put(key, tempItemMap);
        }

        TocTempCostBillItemMonthlyDTO tocTempCostBillItemMonthlyDTO = new TocTempCostBillItemMonthlyDTO();
        tocTempCostBillItemMonthlyDTO.setMerchantId(Long.valueOf(merchantId));
        tocTempCostBillItemMonthlyDTO.setBuId(buIdLong);
        tocTempCostBillItemMonthlyDTO.setLastDay(lastDay);
        tocTempCostBillItemMonthlyDTO.setMonth(month);
        tocTempCostBillItemMonthlyDTO.setShopTypeCode(DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002);
        List<TocTempCostBillItemMonthlyDTO> tocTempCostBillItemMonthlyDTOS = tocTemporaryReceiveBillCostItemMonthlyDao.getMonthlyNonVerify(tocTempCostBillItemMonthlyDTO);
        for (TocTempCostBillItemMonthlyDTO costItemMonthlyDTO : tocTempCostBillItemMonthlyDTOS) {
            String key = costItemMonthlyDTO.getMerchantId() + "_" + costItemMonthlyDTO.getBuId() + "_" + costItemMonthlyDTO.getCustomerId() + "_"
                    + costItemMonthlyDTO.getTemporaryCurrency() + "_" + costItemMonthlyDTO.getMonth();

            BigDecimal nonVerifyAmount = costItemMonthlyDTO.getLastReceiveAmount();
            Map<String, Object> tempItemMap = tobMonthlyMap.get(key);
            if (tempItemMap == null) {
                tempItemMap = new HashMap<>();
                tempItemMap.put("merchantId", costItemMonthlyDTO.getMerchantId());
                tempItemMap.put("merchantName", merchant.getName());
                tempItemMap.put("customerId", costItemMonthlyDTO.getCustomerId());
                tempItemMap.put("buId", costItemMonthlyDTO.getBuId());
                tempItemMap.put("currency", costItemMonthlyDTO.getTemporaryCurrency());
                tempItemMap.put("month", costItemMonthlyDTO.getMonth());
                tempItemMap.put("receiveCost", BigDecimal.ZERO);
                tempItemMap.put("receiveAmount", BigDecimal.ZERO);
                tempItemMap.put("type0Amount", BigDecimal.ZERO);
                tempItemMap.put("type1Amount", BigDecimal.ZERO);
                tempItemMap.put("type2Amount", BigDecimal.ZERO);
                tempItemMap.put("type3Amount", BigDecimal.ZERO);
                tempItemMap.put("type4Amount", BigDecimal.ZERO);
            }

            if (tempItemMap.containsKey("receiveCost")) {
                BigDecimal receiveCost = (BigDecimal) tempItemMap.get("receiveCost");
                nonVerifyAmount = nonVerifyAmount.add(receiveCost);
            }

            tempItemMap.put("receiveCost", nonVerifyAmount);
            tobMonthlyMap.put(key, tempItemMap);
        }


        /**
         * 应收收入 按照“事业部+客户+渠道类型+销售币种”维度汇总 以下各天数区间的金额（0~30天、30~60天、60~90天、90~120天、120天以上）这几个区间的金额
         * 1、按照“公司+事业部+客户+销售币种+月份”维度汇总 “Tob 暂估核销表-收入月结快照” 中 “月结月份最后一天日期”-“上架日期”小于等于30天的 “未核销暂估应收金额”（该表无现成数据需要计算出未核销金额）；
         * 2、按照“公司+事业部+客户+销售币种+月结月份”维度汇总“收款核销跟踪表-月结快照TOB”中 “月结月份最后一天日期”-“账单日期”小于等于30天的 “未核金额”；
         * 3、按照“公司+事业部+客户+销售币种+月结月份”维度汇总 “ToC 暂估应收表- 收入月结快照” 中运营类型为“一件代发“且“月结月份最后一天日期”-“暂估月份（默认每月最后一天）”小于等于30天的“未核销金额”；
         * 4、按照“公司+事业部+客户+结算币种+月结月份”维度汇总 “收款核销跟踪表-月结快照TOC”中运营类型为“一件代发且 “月结月份最后一天日期”-“结算日期”小于等于30天的 “未核金额”；
         * 5、基于以上4点取值按照相同维度“公司+事业部+客户+销售币种+月份+ToB”进行合计金额；
         */
        for (int i = 0; i < typeArr.length; i++) {
            //1、按照“公司+事业部+客户+销售币种+月份”维度汇总 “Tob 暂估核销表-收入月结快照” 中 “月结月份最后一天日期”-“上架日期”小于等于30天的 “未核销暂估应收金额”（该表无现成数据需要计算出未核销金额）；
            TobTemporaryReceiveBillItemMonthlyDTO tobTemporaryReceiveBillItemMonthlyDTO = new TobTemporaryReceiveBillItemMonthlyDTO();
            tobTemporaryReceiveBillItemMonthlyDTO.setMerchantId(Long.valueOf(merchantId));
            tobTemporaryReceiveBillItemMonthlyDTO.setBuId(buIdLong);
            tobTemporaryReceiveBillItemMonthlyDTO.setLastDay(lastDay);
            tobTemporaryReceiveBillItemMonthlyDTO.setType(typeArr[i]);
            tobTemporaryReceiveBillItemMonthlyDTO.setMonth(month);
            List<TobTemporaryReceiveBillItemMonthlyDTO> tobTemporaryReceiveBillItemMonthlyDTOS = tobTemporaryReceiveBillItemMonthlyDao.getMonthlyVerify(tobTemporaryReceiveBillItemMonthlyDTO);

            for (TobTemporaryReceiveBillItemMonthlyDTO itemMonthlyDTO : tobTemporaryReceiveBillItemMonthlyDTOS) {
                String key = itemMonthlyDTO.getMerchantId() + "_" + itemMonthlyDTO.getBuId() + "_" + itemMonthlyDTO.getCustomerId() + "_"
                        + itemMonthlyDTO.getCurrency() + "_" + itemMonthlyDTO.getMonth();

                itemMonthlyDTO.setType(typeArr[i]);
                itemMonthlyDTO.setLastDay(lastDay);
                BigDecimal receiveAmount = tobTemporaryReceiveBillItemDao.listItemPriceByIds(itemMonthlyDTO);

                BigDecimal nonVerifyAmount = receiveAmount.subtract(itemMonthlyDTO.getVerifiedAmount());

                Map<String, Object> tempItemMap = tobMonthlyMap.get(key);
                if (tempItemMap == null) {
                    tempItemMap = new HashMap<>();
                    tempItemMap.put("merchantId", itemMonthlyDTO.getMerchantId());
                    tempItemMap.put("merchantName", merchant.getName());
                    tempItemMap.put("customerId", itemMonthlyDTO.getCustomerId());
                    tempItemMap.put("buId", itemMonthlyDTO.getBuId());
                    tempItemMap.put("currency", itemMonthlyDTO.getCurrency());
                    tempItemMap.put("month", itemMonthlyDTO.getMonth());
                    tempItemMap.put("receiveCost", BigDecimal.ZERO);
                    tempItemMap.put("type0Amount", BigDecimal.ZERO);
                    tempItemMap.put("type1Amount", BigDecimal.ZERO);
                    tempItemMap.put("type2Amount", BigDecimal.ZERO);
                    tempItemMap.put("type3Amount", BigDecimal.ZERO);
                    tempItemMap.put("type4Amount", BigDecimal.ZERO);
                }

                //应收收入
                BigDecimal tempReceiveAmount = tempItemMap.get("receiveAmount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("receiveAmount");

                BigDecimal type0Amount = tempItemMap.get("type0Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type0Amount");
                BigDecimal type1Amount = tempItemMap.get("type1Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type1Amount");
                BigDecimal type2Amount = tempItemMap.get("type2Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type2Amount");
                BigDecimal type3Amount = tempItemMap.get("type3Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type3Amount");
                BigDecimal type4Amount = tempItemMap.get("type4Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type4Amount");

                if (TYPE_0.equals(typeArr[i])) {
                    type0Amount = nonVerifyAmount.add(type0Amount);
                    tempItemMap.put("type0Amount", type0Amount);
                }

                if (TYPE_1.equals(typeArr[i])) {
                    type1Amount = nonVerifyAmount.add(type1Amount);
                    tempItemMap.put("type1Amount", type1Amount);
                }

                if (TYPE_2.equals(typeArr[i])) {
                    type2Amount = nonVerifyAmount.add(type2Amount);
                    tempItemMap.put("type2Amount", type2Amount);
                }

                if (TYPE_3.equals(typeArr[i])) {
                    type3Amount = nonVerifyAmount.add(type3Amount);
                    tempItemMap.put("type3Amount", type3Amount);
                }

                if (TYPE_4.equals(typeArr[i])) {
                    type4Amount = nonVerifyAmount.add(type4Amount);
                    tempItemMap.put("type4Amount", type4Amount);
                }

                tempReceiveAmount = nonVerifyAmount.add(tempReceiveAmount);
                tempItemMap.put("receiveAmount", tempReceiveAmount);
                tobMonthlyMap.put(key, tempItemMap);

            }

            //2、按照“公司+事业部+客户+销售币种+月结月份”维度汇总“收款核销跟踪表-月结快照TOB”中 “月结月份最后一天日期”-“账单日期”小于等于30天的 “未核金额”；
            BillMonthlySnapshotDTO billMonthlySnapshotDTO = new BillMonthlySnapshotDTO();
            billMonthlySnapshotDTO.setMerchantId(Long.valueOf(merchantId));
            billMonthlySnapshotDTO.setBuId(buIdLong);
            billMonthlySnapshotDTO.setLastDay(lastDay);
            billMonthlySnapshotDTO.setType(typeArr[i]);
            billMonthlySnapshotDTO.setBillType(DERP_ORDER.BILLMONTHLYSNAPSHOT_BILLTYPE_0);
            billMonthlySnapshotDTO.setMonth(month);
            List<BillMonthlySnapshotDTO> revenueBillMonthlySnapshots = billMonthlySnapshotDao.getMonthlyNonVerify(billMonthlySnapshotDTO);

            for (BillMonthlySnapshotDTO revenueBillMonthlySnapshot : revenueBillMonthlySnapshots) {
                String key = revenueBillMonthlySnapshot.getMerchantId() + "_" + revenueBillMonthlySnapshot.getBuId() + "_" + revenueBillMonthlySnapshot.getCustomerId() + "_"
                        + revenueBillMonthlySnapshot.getCurrency() + "_" + revenueBillMonthlySnapshot.getMonth();

                BigDecimal nonVerifyAmount = revenueBillMonthlySnapshot.getNonverifyAmount();
                Map<String, Object> tempItemMap = tobMonthlyMap.get(key);
                if (tempItemMap == null) {
                    tempItemMap = new HashMap<>();
                    tempItemMap.put("merchantId", revenueBillMonthlySnapshot.getMerchantId());
                    tempItemMap.put("merchantName", merchant.getName());
                    tempItemMap.put("customerId", revenueBillMonthlySnapshot.getCustomerId());
                    tempItemMap.put("buId", revenueBillMonthlySnapshot.getBuId());
                    tempItemMap.put("currency", revenueBillMonthlySnapshot.getCurrency());
                    tempItemMap.put("month", revenueBillMonthlySnapshot.getMonth());
                    tempItemMap.put("receiveCost", BigDecimal.ZERO);
                    tempItemMap.put("type0Amount", BigDecimal.ZERO);
                    tempItemMap.put("type1Amount", BigDecimal.ZERO);
                    tempItemMap.put("type2Amount", BigDecimal.ZERO);
                    tempItemMap.put("type3Amount", BigDecimal.ZERO);
                    tempItemMap.put("type4Amount", BigDecimal.ZERO);
                }

                //应收收入
                BigDecimal tempReceiveAmount = tempItemMap.get("receiveAmount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("receiveAmount");

                BigDecimal type0Amount = tempItemMap.get("type0Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type0Amount");
                BigDecimal type1Amount = tempItemMap.get("type1Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type1Amount");
                BigDecimal type2Amount = tempItemMap.get("type2Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type2Amount");
                BigDecimal type3Amount = tempItemMap.get("type3Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type3Amount");
                BigDecimal type4Amount = tempItemMap.get("type4Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type4Amount");

                if (TYPE_0.equals(typeArr[i])) {
                    type0Amount = nonVerifyAmount.add(type0Amount);
                    tempItemMap.put("type0Amount", type0Amount);
                }

                if (TYPE_1.equals(typeArr[i])) {
                    type1Amount = nonVerifyAmount.add(type1Amount);
                    tempItemMap.put("type1Amount", type1Amount);
                }

                if (TYPE_2.equals(typeArr[i])) {
                    type2Amount = nonVerifyAmount.add(type2Amount);
                    tempItemMap.put("type2Amount", type2Amount);
                }

                if (TYPE_3.equals(typeArr[i])) {
                    type3Amount = nonVerifyAmount.add(type3Amount);
                    tempItemMap.put("type3Amount", type3Amount);
                }

                if (TYPE_4.equals(typeArr[i])) {
                    type4Amount = nonVerifyAmount.add(type4Amount);
                    tempItemMap.put("type4Amount", type4Amount);
                }
                tempReceiveAmount = nonVerifyAmount.add(tempReceiveAmount);
                tempItemMap.put("receiveAmount", tempReceiveAmount);
                tobMonthlyMap.put(key, tempItemMap);
            }

            //4、按照“公司+事业部+客户+结算币种+月结月份”维度汇总 “收款核销跟踪表-月结快照TOC”中运营类型为“一件代发且 “月结月份最后一天日期”-“结算日期”小于等于30天的 “未核金额”；
            billMonthlySnapshotDTO.setBillType(DERP_ORDER.BILLMONTHLYSNAPSHOT_BILLTYPE_1);
            billMonthlySnapshotDTO.setShopTypeCode(DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002);
            List<BillMonthlySnapshotDTO> costBillMonthlySnapshots = billMonthlySnapshotDao.getMonthlyNonVerify(billMonthlySnapshotDTO);

            for (BillMonthlySnapshotDTO costBillMonthlySnapshot : costBillMonthlySnapshots) {
                String key = costBillMonthlySnapshot.getMerchantId() + "_" + costBillMonthlySnapshot.getBuId() + "_" + costBillMonthlySnapshot.getCustomerId() + "_"
                        + costBillMonthlySnapshot.getCurrency() + "_" + costBillMonthlySnapshot.getMonth();

                BigDecimal nonVerifyAmount = costBillMonthlySnapshot.getNonverifyAmount();
                Map<String, Object> tempItemMap = tobMonthlyMap.get(key);
                if (tempItemMap == null) {
                    tempItemMap = new HashMap<>();
                    tempItemMap.put("merchantId", costBillMonthlySnapshot.getMerchantId());
                    tempItemMap.put("merchantName", merchant.getName());
                    tempItemMap.put("customerId", costBillMonthlySnapshot.getCustomerId());
                    tempItemMap.put("buId", costBillMonthlySnapshot.getBuId());
                    tempItemMap.put("currency", costBillMonthlySnapshot.getCurrency());
                    tempItemMap.put("month", costBillMonthlySnapshot.getMonth());
                    tempItemMap.put("receiveCost", BigDecimal.ZERO);
                    tempItemMap.put("type0Amount", BigDecimal.ZERO);
                    tempItemMap.put("type1Amount", BigDecimal.ZERO);
                    tempItemMap.put("type2Amount", BigDecimal.ZERO);
                    tempItemMap.put("type3Amount", BigDecimal.ZERO);
                    tempItemMap.put("type4Amount", BigDecimal.ZERO);
                }

                //应收收入
                BigDecimal tempReceiveAmount = tempItemMap.get("receiveAmount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("receiveAmount");

                BigDecimal type0Amount = tempItemMap.get("type0Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type0Amount");
                BigDecimal type1Amount = tempItemMap.get("type1Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type1Amount");
                BigDecimal type2Amount = tempItemMap.get("type2Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type2Amount");
                BigDecimal type3Amount = tempItemMap.get("type3Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type3Amount");
                BigDecimal type4Amount = tempItemMap.get("type4Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type4Amount");

                if (TYPE_0.equals(typeArr[i])) {
                    type0Amount = nonVerifyAmount.add(type0Amount);
                    tempItemMap.put("type0Amount", type0Amount);
                }

                if (TYPE_1.equals(typeArr[i])) {
                    type1Amount = nonVerifyAmount.add(type1Amount);
                    tempItemMap.put("type1Amount", type1Amount);
                }

                if (TYPE_2.equals(typeArr[i])) {
                    type2Amount = nonVerifyAmount.add(type2Amount);
                    tempItemMap.put("type2Amount", type2Amount);
                }

                if (TYPE_3.equals(typeArr[i])) {
                    type3Amount = nonVerifyAmount.add(type3Amount);
                    tempItemMap.put("type3Amount", type3Amount);
                }

                if (TYPE_4.equals(typeArr[i])) {
                    type4Amount = nonVerifyAmount.add(type4Amount);
                    tempItemMap.put("type4Amount", type4Amount);
                }

                tempReceiveAmount = nonVerifyAmount.add(tempReceiveAmount);
                tempItemMap.put("receiveAmount", tempReceiveAmount);
                tobMonthlyMap.put(key, tempItemMap);
            }


            //按照“公司+事业部+客户+销售币种+月结月份”维度汇总 “ToC 暂估应收表- 收入月结快照” 中运营类型为“一件代发“且“月结月份最后一天日期”-“暂估月份（默认每月最后一天）”小于等于30天的“未核销金额”；
            TocTempReceiveBillItemMonthlyDTO tocTempReceiveBillItemMonthlyDTO = new TocTempReceiveBillItemMonthlyDTO();
            tocTempReceiveBillItemMonthlyDTO.setMerchantId(Long.valueOf(merchantId));
            tocTempReceiveBillItemMonthlyDTO.setBuId(buIdLong);
            tocTempReceiveBillItemMonthlyDTO.setLastDay(lastDay);
            tocTempReceiveBillItemMonthlyDTO.setShopTypeCode(DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002);
            tocTempReceiveBillItemMonthlyDTO.setType(typeArr[i]);
            tocTempReceiveBillItemMonthlyDTO.setMonth(month);
            List<TocTempReceiveBillItemMonthlyDTO> tocTempReceiveBillItemMonthlyDTOS = tocTemporaryReceiveBillItemMonthlyDao.getMonthlyNonVerify(tocTempReceiveBillItemMonthlyDTO);

            for (TocTempReceiveBillItemMonthlyDTO costItemMonthlyDTO : tocTempReceiveBillItemMonthlyDTOS) {
                String key = costItemMonthlyDTO.getMerchantId() + "_" + costItemMonthlyDTO.getBuId() + "_" + costItemMonthlyDTO.getCustomerId() + "_"
                        + costItemMonthlyDTO.getTemporaryCurrency() + "_" + costItemMonthlyDTO.getMonth();

                BigDecimal nonVerifyAmount = costItemMonthlyDTO.getLastReceiveAmount();
                Map<String, Object> tempItemMap = tobMonthlyMap.get(key);
                if (tempItemMap == null) {
                    tempItemMap = new HashMap<>();
                    tempItemMap.put("merchantId", costItemMonthlyDTO.getMerchantId());
                    tempItemMap.put("merchantName", merchant.getName());
                    tempItemMap.put("customerId", costItemMonthlyDTO.getCustomerId());
                    tempItemMap.put("buId", costItemMonthlyDTO.getBuId());
                    tempItemMap.put("currency", costItemMonthlyDTO.getTemporaryCurrency());
                    tempItemMap.put("month", costItemMonthlyDTO.getMonth());
                    tempItemMap.put("receiveCost", BigDecimal.ZERO);
                    tempItemMap.put("type0Amount", BigDecimal.ZERO);
                    tempItemMap.put("type1Amount", BigDecimal.ZERO);
                    tempItemMap.put("type2Amount", BigDecimal.ZERO);
                    tempItemMap.put("type3Amount", BigDecimal.ZERO);
                    tempItemMap.put("type4Amount", BigDecimal.ZERO);
                }

                //应收收入
                BigDecimal tempReceiveAmount = tempItemMap.get("receiveAmount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("receiveAmount");


                BigDecimal type0Amount = tempItemMap.get("type0Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type0Amount");
                BigDecimal type1Amount = tempItemMap.get("type1Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type1Amount");
                BigDecimal type2Amount = tempItemMap.get("type2Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type2Amount");
                BigDecimal type3Amount = tempItemMap.get("type3Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type3Amount");
                BigDecimal type4Amount = tempItemMap.get("type4Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type4Amount");

                if (TYPE_0.equals(typeArr[i])) {
                    type0Amount = nonVerifyAmount.add(type0Amount);
                    tempItemMap.put("type0Amount", type0Amount);
                }

                if (TYPE_1.equals(typeArr[i])) {
                    type1Amount = nonVerifyAmount.add(type1Amount);
                    tempItemMap.put("type1Amount", type1Amount);
                }

                if (TYPE_2.equals(typeArr[i])) {
                    type2Amount = nonVerifyAmount.add(type2Amount);
                    tempItemMap.put("type2Amount", type2Amount);
                }

                if (TYPE_3.equals(typeArr[i])) {
                    type3Amount = nonVerifyAmount.add(type3Amount);
                    tempItemMap.put("type3Amount", type3Amount);
                }

                if (TYPE_4.equals(typeArr[i])) {
                    type4Amount = nonVerifyAmount.add(type4Amount);
                    tempItemMap.put("type4Amount", type4Amount);
                }

                tempReceiveAmount = nonVerifyAmount.add(tempReceiveAmount);
                tempItemMap.put("receiveAmount", tempReceiveAmount);
                tobMonthlyMap.put(key, tempItemMap);
            }
        }

        //生成tob集合
        generateMapList(mapList, tobMonthlyMap, DERP_ORDER.RECEIVEAGING_CHANNEL_TYPE_1, month, lastDay, buMap, customerMap);

        //toc 类型
        Map<String, Map<String, Object>> tocMonthlyMap = new HashMap<>();

        tocTempCostBillItemMonthlyDTO.setShopTypeCode(DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001);
        List<TocTempCostBillItemMonthlyDTO> costBillItemMonthlyDTOS = tocTemporaryReceiveBillCostItemMonthlyDao.getMonthlyNonVerify(tocTempCostBillItemMonthlyDTO);
        for (TocTempCostBillItemMonthlyDTO costItemMonthlyDTO : costBillItemMonthlyDTOS) {
            String key = costItemMonthlyDTO.getMerchantId() + "_" + costItemMonthlyDTO.getBuId() + "_" + costItemMonthlyDTO.getCustomerId() + "_"
                    + costItemMonthlyDTO.getTemporaryCurrency() + "_" + costItemMonthlyDTO.getMonth();

            BigDecimal nonVerifyAmount = costItemMonthlyDTO.getLastReceiveAmount();
            Map<String, Object> tempItemMap = tocMonthlyMap.get(key);
            if (tempItemMap == null) {
                tempItemMap = new HashMap<>();
                tempItemMap.put("merchantId", costItemMonthlyDTO.getMerchantId());
                tempItemMap.put("merchantName", merchant.getName());
                tempItemMap.put("customerId", costItemMonthlyDTO.getCustomerId());
                tempItemMap.put("buId", costItemMonthlyDTO.getBuId());
                tempItemMap.put("currency", costItemMonthlyDTO.getTemporaryCurrency());
                tempItemMap.put("month", costItemMonthlyDTO.getMonth());
                tempItemMap.put("receiveCost", BigDecimal.ZERO);
                tempItemMap.put("receiveAmount", BigDecimal.ZERO);
                tempItemMap.put("type0Amount", BigDecimal.ZERO);
                tempItemMap.put("type1Amount", BigDecimal.ZERO);
                tempItemMap.put("type2Amount", BigDecimal.ZERO);
                tempItemMap.put("type3Amount", BigDecimal.ZERO);
                tempItemMap.put("type4Amount", BigDecimal.ZERO);
            }

            if (tempItemMap.containsKey("receiveCost")) {
                BigDecimal receiveCost = (BigDecimal) tempItemMap.get("receiveCost");
                nonVerifyAmount = nonVerifyAmount.add(receiveCost);
            }

            tempItemMap.put("receiveCost", nonVerifyAmount);
            tocMonthlyMap.put(key, tempItemMap);
        }

        /**
         * 1、按照 “公司+事业部+客户+结算币种+月结月份” 维度汇总 “ToC 暂估应收表- 收入月结快照” 中运营类型为“POP“ 且“月结月份最后一天日期”-“应收月份（默认每月最后一天）”小于等于30天的“未核销金额”；
         * 2、按照“公司+事业部+客户+结算币种+月结月份”维度汇总  “收款核销跟踪表-月结快照TOC”中运营类型为“POP“且“月结月份最后一天日期”-“结算日期”小于等于30天的 “未核金额”；
         * 3、基于以上2点取值按照相同维度“公司+事业部+客户+销售币种+月份+ToC”进行合计金额；
         */
        for (int i = 0; i < typeArr.length; i++) {
            //按照 “公司+事业部+客户+结算币种+月结月份” 维度汇总 “ToC 暂估应收表- 收入月结快照” 中运营类型为“POP“ 且“月结月份最后一天日期”-“应收月份（默认每月最后一天）”小于等于30天的“未核销金额”
            TocTempReceiveBillItemMonthlyDTO tocTempReceiveBillItemMonthlyDTO = new TocTempReceiveBillItemMonthlyDTO();
            tocTempReceiveBillItemMonthlyDTO.setMerchantId(Long.valueOf(merchantId));
            tocTempReceiveBillItemMonthlyDTO.setBuId(buIdLong);
            tocTempReceiveBillItemMonthlyDTO.setLastDay(lastDay);
            tocTempReceiveBillItemMonthlyDTO.setShopTypeCode(DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001);
            tocTempReceiveBillItemMonthlyDTO.setType(typeArr[i]);
            tocTempReceiveBillItemMonthlyDTO.setMonth(month);
            List<TocTempReceiveBillItemMonthlyDTO> tocTempReceiveBillItemMonthlyDTOS = tocTemporaryReceiveBillItemMonthlyDao.getMonthlyNonVerify(tocTempReceiveBillItemMonthlyDTO);

            for (TocTempReceiveBillItemMonthlyDTO costItemMonthlyDTO : tocTempReceiveBillItemMonthlyDTOS) {
                String key = costItemMonthlyDTO.getMerchantId() + "_" + costItemMonthlyDTO.getBuId() + "_" + costItemMonthlyDTO.getCustomerId() + "_"
                        + costItemMonthlyDTO.getTemporaryCurrency() + "_" + costItemMonthlyDTO.getMonth();

                BigDecimal nonVerifyAmount = costItemMonthlyDTO.getLastReceiveAmount();
                Map<String, Object> tempItemMap = tocMonthlyMap.get(key);
                if (tempItemMap == null) {
                    tempItemMap = new HashMap<>();
                    tempItemMap.put("merchantId", costItemMonthlyDTO.getMerchantId());
                    tempItemMap.put("merchantName", merchant.getName());
                    tempItemMap.put("customerId", costItemMonthlyDTO.getCustomerId());
                    tempItemMap.put("buId", costItemMonthlyDTO.getBuId());
                    tempItemMap.put("currency", costItemMonthlyDTO.getTemporaryCurrency());
                    tempItemMap.put("month", costItemMonthlyDTO.getMonth());
                    tempItemMap.put("receiveCost", BigDecimal.ZERO);
                    tempItemMap.put("type0Amount", BigDecimal.ZERO);
                    tempItemMap.put("type1Amount", BigDecimal.ZERO);
                    tempItemMap.put("type2Amount", BigDecimal.ZERO);
                    tempItemMap.put("type3Amount", BigDecimal.ZERO);
                    tempItemMap.put("type4Amount", BigDecimal.ZERO);
                }

                //应收收入
                BigDecimal tempReceiveAmount = tempItemMap.get("receiveAmount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("receiveAmount");


                BigDecimal type0Amount = tempItemMap.get("type0Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type0Amount");
                BigDecimal type1Amount = tempItemMap.get("type1Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type1Amount");
                BigDecimal type2Amount = tempItemMap.get("type2Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type2Amount");
                BigDecimal type3Amount = tempItemMap.get("type3Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type3Amount");
                BigDecimal type4Amount = tempItemMap.get("type4Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type4Amount");

                if (TYPE_0.equals(typeArr[i])) {
                    type0Amount = nonVerifyAmount.add(type0Amount);
                    tempItemMap.put("type0Amount", type0Amount);
                }

                if (TYPE_1.equals(typeArr[i])) {
                    type1Amount = nonVerifyAmount.add(type1Amount);
                    tempItemMap.put("type1Amount", type1Amount);
                }

                if (TYPE_2.equals(typeArr[i])) {
                    type2Amount = nonVerifyAmount.add(type2Amount);
                    tempItemMap.put("type2Amount", type2Amount);
                }

                if (TYPE_3.equals(typeArr[i])) {
                    type3Amount = nonVerifyAmount.add(type3Amount);
                    tempItemMap.put("type3Amount", type3Amount);
                }

                if (TYPE_4.equals(typeArr[i])) {
                    type4Amount = nonVerifyAmount.add(type4Amount);
                    tempItemMap.put("type4Amount", type4Amount);
                }
                tempReceiveAmount = nonVerifyAmount.add(tempReceiveAmount);
                tempItemMap.put("receiveAmount", tempReceiveAmount);
                tocMonthlyMap.put(key, tempItemMap);
            }

            //按照“公司+事业部+客户+结算币种+月结月份”维度汇总  “收款核销跟踪表-月结快照TOC”中运营类型为“POP“且“月结月份最后一天日期”-“结算日期”小于等于30天的 “未核金额”；
            BillMonthlySnapshotDTO billMonthlySnapshotDTO = new BillMonthlySnapshotDTO();
            billMonthlySnapshotDTO.setMerchantId(Long.valueOf(merchantId));
            billMonthlySnapshotDTO.setBuId(buIdLong);
            billMonthlySnapshotDTO.setLastDay(lastDay);
            billMonthlySnapshotDTO.setType(typeArr[i]);
            billMonthlySnapshotDTO.setMonth(month);
            billMonthlySnapshotDTO.setBillType(DERP_ORDER.BILLMONTHLYSNAPSHOT_BILLTYPE_1);
            billMonthlySnapshotDTO.setShopTypeCode(DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001);
            List<BillMonthlySnapshotDTO> costBillMonthlySnapshots = billMonthlySnapshotDao.getMonthlyNonVerify(billMonthlySnapshotDTO);

            for (BillMonthlySnapshotDTO revenueBillMonthlySnapshot : costBillMonthlySnapshots) {
                String key = revenueBillMonthlySnapshot.getMerchantId() + "_" + revenueBillMonthlySnapshot.getBuId() + "_" + revenueBillMonthlySnapshot.getCustomerId() + "_"
                        + revenueBillMonthlySnapshot.getCurrency() + "_" + revenueBillMonthlySnapshot.getMonth();

                BigDecimal nonVerifyAmount = revenueBillMonthlySnapshot.getNonverifyAmount();
                Map<String, Object> tempItemMap = tocMonthlyMap.get(key);
                if (tempItemMap == null) {
                    tempItemMap = new HashMap<>();
                    tempItemMap.put("merchantId", revenueBillMonthlySnapshot.getMerchantId());
                    tempItemMap.put("merchantName", merchant.getName());
                    tempItemMap.put("customerId", revenueBillMonthlySnapshot.getCustomerId());
                    tempItemMap.put("buId", revenueBillMonthlySnapshot.getBuId());
                    tempItemMap.put("currency", revenueBillMonthlySnapshot.getCurrency());
                    tempItemMap.put("month", revenueBillMonthlySnapshot.getMonth());
                    tempItemMap.put("receiveCost", BigDecimal.ZERO);
                    tempItemMap.put("type0Amount", BigDecimal.ZERO);
                    tempItemMap.put("type1Amount", BigDecimal.ZERO);
                    tempItemMap.put("type2Amount", BigDecimal.ZERO);
                    tempItemMap.put("type3Amount", BigDecimal.ZERO);
                    tempItemMap.put("type4Amount", BigDecimal.ZERO);
                }

                //应收收入
                BigDecimal tempReceiveAmount = tempItemMap.get("receiveAmount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("receiveAmount");

                BigDecimal type0Amount = tempItemMap.get("type0Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type0Amount");
                BigDecimal type1Amount = tempItemMap.get("type1Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type1Amount");
                BigDecimal type2Amount = tempItemMap.get("type2Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type2Amount");
                BigDecimal type3Amount = tempItemMap.get("type3Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type3Amount");
                BigDecimal type4Amount = tempItemMap.get("type4Amount") == null ? BigDecimal.ZERO : (BigDecimal) tempItemMap.get("type4Amount");

                if (TYPE_0.equals(typeArr[i])) {
                    type0Amount = nonVerifyAmount.add(type0Amount);
                    tempItemMap.put("type0Amount", type0Amount);
                }

                if (TYPE_1.equals(typeArr[i])) {
                    type1Amount = nonVerifyAmount.add(type1Amount);
                    tempItemMap.put("type1Amount", type1Amount);
                }

                if (TYPE_2.equals(typeArr[i])) {
                    type2Amount = nonVerifyAmount.add(type2Amount);
                    tempItemMap.put("type2Amount", type2Amount);
                }

                if (TYPE_3.equals(typeArr[i])) {
                    type3Amount = nonVerifyAmount.add(type3Amount);
                    tempItemMap.put("type3Amount", type3Amount);
                }

                if (TYPE_4.equals(typeArr[i])) {
                    type4Amount = nonVerifyAmount.add(type4Amount);
                    tempItemMap.put("type4Amount", type4Amount);
                }
                tempReceiveAmount = nonVerifyAmount.add(tempReceiveAmount);
                tempItemMap.put("receiveAmount", tempReceiveAmount);
                tocMonthlyMap.put(key, tempItemMap);
            }
        }
        //生成toC集合
        generateMapList(mapList, tocMonthlyMap, DERP_ORDER.RECEIVEAGING_CHANNEL_TYPE_2, month, lastDay, buMap, customerMap);

        //表头信息
        String mainSheetName = "应收账龄报告";
        String[] mainColumns = {"公司", "事业部", "应收月份", "客户名称", "客户简称", "渠道类型", "币种", "应收账面余额（原币）",
                "应收收入（原币）", "应收费用（原币）", "折算汇率", "汇率日期", "应收账面余额（人民币）", "应收收入（人民币）", "应收费用（人民币）",
                "0~30天", "30~60天", "60~90天", "90~120天", "120天以上", "正常账期天数"};
        String[] mainKeys = {"merchantName","buName", "month", "customerName", "simpleName", "channelType", "currency", "billAmount", "receiveAmount", "receiveCost",
                "rate", "rateDate", "billAmountOri", "receiveAmountOri", "receiveCostOri", "type0Amount", "type1Amount", "type2Amount", "type3Amount", "type4Amount",
                "accountDay"};

        //生成表格
        ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, mainColumns, mainKeys, mapList);

        List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();
        sheets.add(mainSheet);

        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);
        //导出文件
        String fileName = task.getRemark() + ".xlsx";

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

        return basePath;
    }

    private ExchangeRateMongo getRate(String currency, String month) {
        Map<String, Object> queryRateMap = new HashMap<String, Object>();
        queryRateMap.put("origCurrencyCode",currency);
        queryRateMap.put("tgtCurrencyCode", DERP.CURRENCYCODE_CNY);
        queryRateMap.put("effectiveDate", TimeUtils.getLastDayEndTime(month, "yyyy-MM-dd"));
        queryRateMap.put("status", "1") ;
        ExchangeRateMongo exchangeRateMongo = exchangeRateMongoDao.findLastRateByParams(queryRateMap);

        return exchangeRateMongo;
    }

    private void generateMapList(List<Map<String, Object>> mapList, Map<String, Map<String, Object>> tobMonthlyMap, String channelType, String month, String lastDay,
                                 Map<Long, BusinessUnitMongo> buMap, Map<Long, CustomerInfoMogo> customerMap) {
        for (String key : tobMonthlyMap.keySet()) {
            Map<String, Object> itemMap = tobMonthlyMap.get(key);
            itemMap.put("channelType", DERP_ORDER.getLabelByKey(DERP_ORDER.receiveAging_channelTypeList, channelType));
            Long merId = (Long) itemMap.get("merchantId");

            //事业部
            Long bId = (Long) itemMap.get("buId");
            BusinessUnitMongo bu = buMap.get(bId);
            if (bu == null) {
                Map<String, Object> buParam = new HashMap<>();
                buParam.put("businessUnitId", bId);
                bu = businessUnitMongoDao.findOne(buParam);
            }

            if (bu != null) {
                itemMap.put("buName", bu.getName());
                buMap.put(bId, bu);
            }

            //客户
            Long customerId = (Long) itemMap.get("customerId");
            CustomerInfoMogo customer = customerMap.get(customerId);

            if (customer == null) {
                Map<String, Object> customerParam = new HashMap<>();
                customerParam.put("customerid", customerId);
                customer = customerInfoMongoDao.findOne(customerParam);
            }

            if(customer!=null){
                itemMap.put("customerName", customer.getName());
                itemMap.put("simpleName", customer.getSimpleName());
                customerMap.put(customerId, customer);
            }

            //币种
            String currency = (String) itemMap.get("currency");
            itemMap.put("currency", DERP.getLabelByKey(DERP.currencyCodeList, currency));

            //应收账面余额（原币）
            BigDecimal receiveAmount = itemMap.get("receiveAmount") == null ? BigDecimal.ZERO : (BigDecimal) itemMap.get("receiveAmount");
            BigDecimal receiveCost = itemMap.get("receiveCost") == null ? BigDecimal.ZERO : (BigDecimal) itemMap.get("receiveCost");
            BigDecimal billAmount = receiveAmount.subtract(receiveCost);
            itemMap.put("billAmount", billAmount);

            //获取汇率，汇率日期以及计算应收账面余额（RMB）
            if(DERP.CURRENCYCODE_CNY.equals(currency)){
                itemMap.put("billAmountOri", billAmount);
                itemMap.put("receiveAmountOri", receiveAmount);
                itemMap.put("receiveCostOri", receiveCost);
                itemMap.put("rate", 1d);
            }else{
                ExchangeRateMongo rate = getRate(currency, month);
                if (rate != null) {
                    BigDecimal receiveAmountOri = receiveAmount.multiply(new BigDecimal(rate.getAvgRate())).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal receiveCostOri = receiveCost.multiply(new BigDecimal(rate.getAvgRate())).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal billAmountOri = receiveAmountOri.subtract(receiveCostOri);
                    itemMap.put("receiveAmountOri", receiveAmountOri);
                    itemMap.put("receiveCostOri", receiveCostOri);
                    itemMap.put("billAmountOri", billAmountOri);
                    itemMap.put("rate", rate.getAvgRate());
                }
            }

            //汇率日期
            itemMap.put("rateDate", lastDay);

            //所有计划节点时效天数
            Integer accountDay = accountingReminderItemDao.getByBuIdCustomerId(bId, customerId, merId);
            itemMap.put("accountDay", accountDay);

            mapList.add(itemMap);

        }
    }
}
