package com.topideal.order.service.bill.impl;

import com.alibaba.fastjson.JSON;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.APIResponse;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.dao.bill.TocTemporaryCostBillDao;
import com.topideal.dao.bill.TocTemporaryReceiveBillCostItemDao;
import com.topideal.dao.bill.TocTemporaryReceiveBillDao;
import com.topideal.dao.bill.TocTemporaryReceiveBillItemDao;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.vo.bill.TocTemporaryCostBillModel;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillModel;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.dao.MerchantShopRelMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.order.api.bean.dto.QueryTocTempReceiveBillDTO;
import com.topideal.order.service.bill.ToCTempReceiveBillService;
import com.topideal.order.tools.DownloadExcelUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

@Service
public class ToCTempReceiveBillServiceImpl implements ToCTempReceiveBillService {

    protected Logger LOGGER = LoggerFactory.getLogger(ToCTempReceiveBillServiceImpl.class);

    @Autowired
    private TocTemporaryReceiveBillDao tocTemporaryReceiveBillDao;
    @Autowired
    private TocTemporaryReceiveBillItemDao tocTemporaryReceiveBillItemDao;
    @Autowired
    private MerchantShopRelMongoDao merchantShopRelMongoDao;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private TocTemporaryReceiveBillCostItemDao tocTemporaryReceiveBillCostItemDao;
    @Autowired
    private TocTemporaryCostBillDao tocTemporaryCostBillDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;

    @Override
    public TocTemporaryReceiveBillDTO listTocTempReceiveBillByPage(User user, TocTemporaryReceiveBillDTO dto) throws SQLException {
        List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
        if (buList.isEmpty()) {
            dto.setList(new ArrayList<>());
            dto.setTotal(0);
            return dto;
        }
        TocTemporaryReceiveBillDTO billDTO = tocTemporaryReceiveBillDao.listTocTempReceiveBillByPage(dto);
        List<TocTemporaryReceiveBillDTO> receiveBillDTOS = billDTO.getList();
        for (TocTemporaryReceiveBillDTO receiveBillDTO : receiveBillDTOS) {
            receiveBillDTO.setCurrency("CNY");
        }
        return billDTO;
    }

    @Override
    public TocTemporaryCostBillDTO listTocTempCostReceiveBillByPage(User user, TocTemporaryCostBillDTO dto) throws SQLException {
        List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
        if (buList.isEmpty()) {
            dto.setList(new ArrayList<>());
            dto.setTotal(0);
            return dto;
        }

        TocTemporaryCostBillDTO billDTO = tocTemporaryCostBillDao.listTocTempCostReceiveBillByPage(dto);
        List<TocTemporaryCostBillDTO> receiveBillDTOS = billDTO.getList();

        //获取bill_id 的集合
        List<Long> billIdList = receiveBillDTOS.stream()
                .map(entity -> entity.getId()).collect(Collectors.toList());

        for (TocTemporaryCostBillDTO receiveBillDTO : receiveBillDTOS) {
            receiveBillDTO.setCurrency("CNY");
            BigDecimal lastReceiveAmount = receiveBillDTO.getTotalReceiveAmount()
                    .subtract(receiveBillDTO.getAlreadyReceiveAmount() != null? receiveBillDTO.getAlreadyReceiveAmount() : BigDecimal.ZERO)
                    .add(receiveBillDTO.getAdjustmentRmbAmount() != null ? receiveBillDTO.getAdjustmentRmbAmount() : BigDecimal.ZERO);
            receiveBillDTO.setLastReceiveAmount(lastReceiveAmount);
        }
        return billDTO;
    }

    @Override
    public List<MerchantShopRelMongo> getMerchantShopRelList(Long merchantId) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", "1");
        params.put("merchantId", merchantId);
        List<MerchantShopRelMongo> shopList = merchantShopRelMongoDao.findAll(params);    //mongo查询参数集合
        return shopList;
    }

    @Override
    public TocTemporaryReceiveBillDTO getDetails(Long id) throws SQLException {
        return tocTemporaryReceiveBillDao.searchDTOById(id);
    }

    @Override
    public TocTemporaryCostBillDTO getCostDetails(Long id) throws SQLException {
        return tocTemporaryCostBillDao.searchDTOById(id);
    }

    @Override
    public TocTemporaryReceiveBillItemDTO listToCTempReceiveItem(User user, TocTemporaryReceiveBillItemDTO dto) throws SQLException {
        dto.setMerchantId(user.getMerchantId());
        TocTemporaryReceiveBillItemDTO itemDTO = tocTemporaryReceiveBillItemDao.getListByPage(dto);

        List<TocTemporaryReceiveBillItemDTO> receiveBillDTOS = itemDTO.getList();
        for (TocTemporaryReceiveBillItemDTO billItemDTO : receiveBillDTOS) {
            BigDecimal settlementRmbCost = billItemDTO.getSettlementRmbAmount() != null ? billItemDTO.getSettlementRmbAmount() : new BigDecimal("0");
            BigDecimal writeOffAmount = billItemDTO.getWriteOffAmount() != null ? billItemDTO.getWriteOffAmount() : new BigDecimal("0");
            BigDecimal nonVerifyAmount = billItemDTO.getTemporaryRmbAmount().subtract(settlementRmbCost).subtract(writeOffAmount);
            billItemDTO.setNonVerifyAmount(nonVerifyAmount);
        }

        return itemDTO;
    }

    @Override
    public TocTemporaryReceiveBillCostItemDTO listToCTempReceiveCostItem(User user, TocTemporaryReceiveBillCostItemDTO dto) throws Exception {

        dto.setMerchantId(user.getMerchantId());

        TocTemporaryReceiveBillCostItemDTO itemDTO = tocTemporaryReceiveBillCostItemDao.getListByPage(dto);

        List<TocTemporaryReceiveBillCostItemDTO> receiveBillDTOS = itemDTO.getList();
        // 过滤掉
        receiveBillDTOS = receiveBillDTOS.stream().filter(entity -> {
            return !DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_2.equals(entity.getOrderType()) ||
                    !DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_3.equals(entity.getOrderType());
        }).collect(Collectors.toList());
        for (TocTemporaryReceiveBillCostItemDTO billItemDTO : receiveBillDTOS) {
            BigDecimal settlementRmbCost = billItemDTO.getSettlementRmbCost() != null ? billItemDTO.getSettlementRmbCost() : new BigDecimal("0");
            BigDecimal writeOffAmount = billItemDTO.getWriteOffAmount() != null ? billItemDTO.getWriteOffAmount() : new BigDecimal("0");
            BigDecimal adjustmentAmount = billItemDTO.getAdjustmentRmbAmount() != null ? billItemDTO.getAdjustmentRmbAmount() : new BigDecimal("0");
            BigDecimal nonVerifyAmount = billItemDTO.getTemporaryRmbCost().subtract(settlementRmbCost).subtract(writeOffAmount).add(adjustmentAmount);
            billItemDTO.setNonVerifyAmount(nonVerifyAmount);
        }

        return itemDTO;
    }

    @Override
    public int countTempBillNum(User user,TocTemporaryReceiveBillItemDTO dto) throws SQLException {
        List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
        dto.setBuList(buList);
        dto.setMerchantId(user.getMerchantId());
        return tocTemporaryReceiveBillItemDao.countTempBillNum(dto);
    }

    @Override
    public int countTempCostBillNum(User user, TocTemporaryReceiveBillCostItemDTO dto) throws SQLException {
        List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
        dto.setBuList(buList);
        dto.setMerchantId(user.getMerchantId());
        return tocTemporaryReceiveBillCostItemDao.countTempBillNum(dto);
    }

    @Override
    public List<TocTemporaryReceiveBillDTO> listForExport(TocTemporaryReceiveBillDTO dto, User user) throws Exception {
        List<TocTemporaryReceiveBillDTO> tocTemporaryReceiveBillDTOS = tocTemporaryReceiveBillDao.listForExport(dto);
        return tocTemporaryReceiveBillDTOS;
    }

    @Override
    public List<TocTemporaryCostBillDTO> listCostForExport(TocTemporaryCostBillDTO dto, User user) throws Exception {
        List<TocTemporaryCostBillDTO> tocTemporaryReceiveBillDTOS = tocTemporaryCostBillDao.listForExport(dto);
        return tocTemporaryReceiveBillDTOS;
    }

    @Override
    public List<Map<String, Object>> listForExportTempItem(User user, TocTemporaryReceiveBillItemDTO dto) throws SQLException {
        List<Map<String, Object>> exportList = new ArrayList<>();
        List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
        if (buList.isEmpty()) {
            return exportList;
        }
        dto.setBuList(buList);
        dto.setMerchantId(user.getMerchantId());
        int exportItemNum = tocTemporaryReceiveBillItemDao.countTempBillNum(dto);
        int pageSize = 2000;
        for (int i = 0; i < exportItemNum; ) {
            int pageSub = (i + pageSize) < exportItemNum ? (i + pageSize) : exportItemNum;
            dto.setBegin(i);
            dto.setPageSize(pageSize);
            List<Map<String, Object>> itemDTOS = tocTemporaryReceiveBillItemDao.listForExportTempItemPage(dto);
            for (Map<String, Object> item : itemDTOS) {
                String storePlatformCodeItem = (String) item.get("store_platform_code");
                String orderType = (String) item.get("order_type");
                String temporaryCurrency = (String) item.get("temporary_currency");
                String settlementMark = (String) item.get("settlement_mark");

                item.put("settlementMark", DERP.getLabelByKey(DERP_ORDER.tocTempItemBill_settlementMarkList, settlementMark));
                item.put("temporaryCurrency", DERP.getLabelByKey(DERP.currencyCodeList, temporaryCurrency));
                item.put("storePlatformName", DERP.getLabelByKey(DERP.storePlatformList, storePlatformCodeItem));
                item.put("orderType", DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemBill_orderTypeList, orderType));
                BigDecimal temporaryRmbAmount = item.get("temporary_rmb_amount") != null ? (BigDecimal) item.get("temporary_rmb_amount") : new BigDecimal("0");
                BigDecimal settlementRmbAmount = item.get("settlement_rmb_amount") != null ? (BigDecimal) item.get("settlement_rmb_amount") : new BigDecimal("0");
                BigDecimal writeOffAmount = item.get("write_off_amount") != null ? (BigDecimal) item.get("write_off_amount") : new BigDecimal("0");
                BigDecimal nonVerifyAmount = temporaryRmbAmount.subtract(settlementRmbAmount).subtract(writeOffAmount);
                item.put("nonVerifyAmount", nonVerifyAmount);
            }
            exportList.addAll(itemDTOS);
            i = pageSub;
        }
        return exportList;

    }

    @Override
    public List<Map<String, Object>> listForExportTempCostItem(User user, TocTemporaryReceiveBillCostItemDTO dto) throws SQLException {
        List<Map<String, Object>> exportList = new ArrayList<>();
        List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
        if (buList.isEmpty()) {
            return exportList;
        }
        dto.setBuList(buList);
        dto.setMerchantId(user.getMerchantId());
        int exportItemNum = tocTemporaryReceiveBillCostItemDao.countTempBillNum(dto);
        int pageSize = 2000;
        for (int i = 0; i < exportItemNum; ) {
            int pageSub = (i + pageSize) < exportItemNum ? (i + pageSize) : exportItemNum;
            dto.setBegin(i);
            dto.setPageSize(pageSize);
            List<Map<String, Object>> itemDTOS = tocTemporaryReceiveBillCostItemDao.listForExportTempCostItemPage(dto);
            for (Map<String, Object> item : itemDTOS) {
                String storePlatformCodeItem = (String) item.get("store_platform_code");
                String orderType = (String) item.get("order_type");
                String temporaryCurrency = (String) item.get("temporary_currency");
                String settlementMark = (String) item.get("settlement_mark");

                item.put("settlementMark", DERP.getLabelByKey(DERP_ORDER.tocTempItemCostBill_settlementMarkList, settlementMark));
                item.put("temporaryCurrency", DERP.getLabelByKey(DERP.currencyCodeList, temporaryCurrency));
                item.put("storePlatformName", DERP.getLabelByKey(DERP.storePlatformList, storePlatformCodeItem));
                item.put("orderType", DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemBill_orderTypeList, orderType));
                BigDecimal temporaryRmbCost = item.get("temporary_rmb_cost") != null ? (BigDecimal) item.get("temporary_rmb_cost") : new BigDecimal("0");
                BigDecimal settlementRmbCost = item.get("settlement_rmb_cost") != null ? (BigDecimal) item.get("settlement_rmb_cost") : new BigDecimal("0");
                BigDecimal writeOffAmount = item.get("write_off_amount") != null ? (BigDecimal) item.get("write_off_amount") : new BigDecimal("0");
                BigDecimal nonVerifyAmount = temporaryRmbCost.subtract(settlementRmbCost).subtract(writeOffAmount);
                item.put("nonVerifyAmount", nonVerifyAmount);

            }
            exportList.addAll(itemDTOS);
            i = pageSub;
        }
        return exportList;
    }

    @Override
    public Map<String, Object> refreshBills(String ids, String type) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String[] idArr = ids.split(",");

        if ("0".equals(type)) {
            for (String id : idArr) {
                TocTemporaryReceiveBillModel billModel = tocTemporaryReceiveBillDao.searchById(Long.valueOf(id));
                Map<String, Object> tocTempMap = new HashMap<String, Object>();
                tocTempMap.put("merchantId", billModel.getMerchantId());
                tocTempMap.put("storePlatformCode", billModel.getStorePlatformCode());
                tocTempMap.put("shopCode", billModel.getShopCode());
                tocTempMap.put("customerId", billModel.getCustomerId());
                tocTempMap.put("month", billModel.getMonth());
                tocTempMap.put("shopTypeCode", billModel.getShopTypeCode());
                tocTempMap.put("orderType", type);
                tocTempMap.put("buId", billModel.getBuId());
                JSONObject tocTempJson = JSONObject.fromObject(tocTempMap);
                rocketMQProducer.send(tocTempJson.toString(), MQOrderEnum.TIMER_ToC_Temp_RECEVICE_BILL.getTopic(), MQOrderEnum.TIMER_ToC_Temp_RECEVICE_BILL.getTags());
            }
        }

        if ("1".equals(type)) {
            for (String id : idArr) {
                TocTemporaryCostBillModel billModel = tocTemporaryCostBillDao.searchById(Long.valueOf(id));
                Map<String, Object> tocTempMap = new HashMap<String, Object>();
                tocTempMap.put("merchantId", billModel.getMerchantId());
                tocTempMap.put("storePlatformCode", billModel.getStorePlatformCode());
                tocTempMap.put("shopCode", billModel.getShopCode());
                tocTempMap.put("customerId", billModel.getCustomerId());
                tocTempMap.put("month", billModel.getMonth());
                tocTempMap.put("shopTypeCode", billModel.getShopTypeCode());
                tocTempMap.put("orderType", type);
                tocTempMap.put("buId", billModel.getBuId());
                JSONObject tocTempJson = JSONObject.fromObject(tocTempMap);
                rocketMQProducer.send(tocTempJson.toString(), MQOrderEnum.TIMER_ToC_Temp_RECEVICE_BILL.getTopic(), MQOrderEnum.TIMER_ToC_Temp_RECEVICE_BILL.getTags());
            }
        }

        resultMap.put("code", "00");
        resultMap.put("message", "正在刷新");
        return resultMap;
    }

    @Override
    public List<ExportExcelSheet> exportSummaryBill(TocTemporaryReceiveBillDTO dto, User user) throws Exception {
        List<Map<String, Object>> exportList = new ArrayList<>();
        List<Map<String, Object>> exportYearList = new ArrayList<>();
        Map<String, List<TocTemporaryReceiveBillDTO>> yearBillDtoMap = new HashMap<>();
        TocTemporaryReceiveBillItemDTO itemDTO = new TocTemporaryReceiveBillItemDTO();
        BeanUtils.copyProperties(dto, itemDTO);
        itemDTO.setBillIds(dto.getIds());

        List<TocTemporaryReceiveBillDTO> billDTOList = tocTemporaryReceiveBillItemDao.summaryByDTO(itemDTO);

        //月份数据
        billDTOList.forEach(entity -> {
            Map<String, Object> map = new HashMap<>();
            entity.setAlreadyReceiveNum((long) entity.getTotalReceiveNum() - (long) entity.getLastReceiveNum());
            map.put("merchantName", entity.getMerchantName());
            map.put("month", entity.getMonth());
            map.put("shopTypeName", entity.getShopTypeName());
            map.put("storePlatformName", entity.getStorePlatformName());
            map.put("shopName", entity.getShopName());
            map.put("totalReceiveAmount", entity.getTotalReceiveAmount());
            map.put("billNum", entity.getTotalReceiveNum());
            map.put("alreadyReceiveAmount", entity.getAlreadyReceiveAmount());
            map.put("alreadyReceiveNum", entity.getAlreadyReceiveNum());
            map.put("noReceiveNum", entity.getLastReceiveNum());
            map.put("noReceiveAmount", entity.getLastReceiveAmount());

            if (entity.getLastReceiveAmount().equals(0L)) {
                entity.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_2);
            } else if (entity.getTotalReceiveNum().equals(entity.getLastReceiveNum())) {
                entity.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_0);
            } else {
                entity.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_1);
            }
            map.put("status", entity.getSettlementStatusLabel());
            map.put("settlementEndMonth", entity.getSettlementEndMonth());
            map.put("parentBrandName", entity.getParentBrandName());
            map.put("buName", entity.getBuName());
            exportList.add(map);

            String key = entity.getShopTypeCode() + "_" + entity.getStorePlatformCode()
                    + "_" + entity.getShopCode() + "_" + entity.getBuId() + "_" + entity.getParentBrandName()
                    + "_" + entity.getMonth().substring(0, 4);
            List<TocTemporaryReceiveBillDTO> dtoList = yearBillDtoMap.get(key);
            if (dtoList == null) {
                dtoList = new ArrayList<>();
            }
            dtoList.add(entity);
            yearBillDtoMap.put(key, dtoList);
        });

        //年份数据
        for (String key : yearBillDtoMap.keySet()) {
            List<TocTemporaryReceiveBillDTO> dtoList = yearBillDtoMap.get(key);

            BigDecimal totalYearReceiveAmount = new BigDecimal("0");
            BigDecimal totalYearAlreadyReceiveAmount = new BigDecimal("0");
            BigDecimal totalYearNoReceiveAmount = new BigDecimal("0");
            Long totalYearReceiveNum = 0L;
            Long alreadyYearReceiveNum = 0L;

            for (TocTemporaryReceiveBillDTO billDTO : dtoList) {
                BigDecimal totalReceiveAmount = billDTO.getTotalReceiveAmount() == null ? new BigDecimal("0") : billDTO.getTotalReceiveAmount();
                BigDecimal alreadyReceiveAmount = billDTO.getAlreadyReceiveAmount() == null ? new BigDecimal("0") : billDTO.getAlreadyReceiveAmount();
                BigDecimal noReceiveAmount = billDTO.getLastReceiveAmount() == null ? new BigDecimal("0") : billDTO.getLastReceiveAmount();
                Long totalReceiveNum = billDTO.getTotalReceiveNum();
                Long alreadyReceiveNum = billDTO.getAlreadyReceiveNum();

                totalYearReceiveAmount = totalYearReceiveAmount.add(totalReceiveAmount);
                totalYearAlreadyReceiveAmount = totalYearAlreadyReceiveAmount.add(alreadyReceiveAmount);
                totalYearNoReceiveAmount = totalYearNoReceiveAmount.add(noReceiveAmount);
                totalYearReceiveNum += totalReceiveNum;
                alreadyYearReceiveNum += alreadyReceiveNum;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("merchantName", dtoList.get(0).getMerchantName());
            map.put("year", dtoList.get(0).getMonth().substring(0, 4));
            map.put("shopTypeName", dtoList.get(0).getShopTypeName());
            map.put("storePlatformName", dtoList.get(0).getStorePlatformName());
            map.put("shopName", dtoList.get(0).getShopName());
            map.put("totalReceiveAmount", totalYearReceiveAmount);
            map.put("billNum", totalYearReceiveNum);
            map.put("alreadyReceiveAmount", totalYearAlreadyReceiveAmount);
            map.put("alreadyReceiveNum", alreadyYearReceiveNum);
            map.put("noReceiveNum", totalYearReceiveNum - alreadyYearReceiveNum);
            map.put("noReceiveAmount", totalYearNoReceiveAmount);
            map.put("parentBrandName", dtoList.get(0).getParentBrandName());
            map.put("buName", dtoList.get(0).getBuName());
            exportYearList.add(map);
        }

        //月份汇总
        String sheetMonthName = "月份汇总";
        String[] monthColumns = {"公司", "暂估应收月份", "运营类型", "平台", "店铺", "事业部", "母品牌", "总暂估应收单量", "暂估应收金额", "已结算订单量",
                "结算金额", "剩余暂估应收单量", "剩余暂估应收金额", "结算状态", "结算完成月份"};
        String[] monthKeys = {"merchantName", "month", "shopTypeName", "storePlatformName", "shopName", "buName", "parentBrandName", "billNum", "totalReceiveAmount",
                "alreadyReceiveNum", "alreadyReceiveAmount", "noReceiveNum", "noReceiveAmount", "status", "settlementEndMonth"};

        //年度汇总
        String yearSheetName = "年度汇总";
        String[] yearColumns = {"公司", "暂估应收年份", "运营类型", "平台", "店铺", "事业部", "母品牌", "总暂估应收单量", "暂估应收金额", "已结算订单量",
                "结算金额", "剩余暂估应收单量", "剩余暂估应收金额"};
        String[] yearKeys = {"merchantName", "year", "shopTypeName", "storePlatformName", "shopName", "buName", "parentBrandName", "billNum", "totalReceiveAmount",
                "alreadyReceiveNum", "alreadyReceiveAmount", "noReceiveNum", "noReceiveAmount"};
        //生成表格
        ExportExcelSheet monthSheet = ExcelUtilXlsx.createSheet(sheetMonthName, monthColumns, monthKeys, exportList);
        ExportExcelSheet yearSheet = ExcelUtilXlsx.createSheet(yearSheetName, yearColumns, yearKeys, exportYearList);

        List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();
        sheets.add(monthSheet);
        sheets.add(yearSheet);
        return sheets;
    }

    @Override
    public List<ExportExcelSheet> exportCostSummaryBill(TocTemporaryCostBillDTO dto, User user) throws Exception {
        List<Map<String, Object>> exportList = new ArrayList<>();
        List<Map<String, Object>> exportYearList = new ArrayList<>();
        TocTemporaryReceiveBillCostItemDTO itemDTO = new TocTemporaryReceiveBillCostItemDTO();
        BeanUtils.copyProperties(dto, itemDTO);
        itemDTO.setBillIds(dto.getIds());

        List<TocTemporaryCostBillDTO> tocTemporaryCostBillDTOS = tocTemporaryReceiveBillCostItemDao.summaryByDTO(itemDTO);
        Map<String, List<TocTemporaryCostBillDTO>> yearBillDtoMap = new HashMap<>();

        tocTemporaryCostBillDTOS.forEach(entity -> {
            Map<String, Object> map = new HashMap<>();
            entity.setAlreadyReceiveNum((long)entity.getTotalReceiveNum() - (long)entity.getLastReceiveNum());
            map.put("merchantName", entity.getMerchantName());
            map.put("month", entity.getMonth());
            map.put("shopTypeName", entity.getShopTypeName());
            map.put("storePlatformName", entity.getStorePlatformName());
            map.put("shopName", entity.getShopName());
            map.put("totalReceiveAmount", entity.getTotalReceiveAmount());
            map.put("alreadyReceiveAmount", entity.getAlreadyReceiveAmount());
            map.put("adjustmentRmbAmount", entity.getAdjustmentRmbAmount());
            map.put("noReceiveAmount", entity.getLastReceiveAmount());
            if (entity.getLastReceiveNum().equals(0L)) {
                entity.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_2);
            } else if (entity.getTotalReceiveNum().equals(entity.getLastReceiveNum())) {
                entity.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_0);
            } else {
                entity.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_1);
            }
            map.put("status", entity.getSettlementStatusLabel());
            map.put("settlementEndMonth", entity.getSettlementEndMonth());
            map.put("parentBrandName", entity.getParentBrandName());
            map.put("buName", entity.getBuName());
            map.put("platformProjectName", entity.getPlatformProjectName());
            exportList.add(map);

            String key = entity.getShopTypeCode() + "_" + entity.getStorePlatformCode()
                    + "_" + entity.getShopCode() + "_" + entity.getBuId() + "_" + entity.getParentBrandName()
                    + "_" + entity.getMonth().substring(0, 4) + "_" + entity.getPlatformProjectId();

            List<TocTemporaryCostBillDTO> dtoList = yearBillDtoMap.get(key);
            if (dtoList == null) {
                dtoList = new ArrayList<>();
            }
            dtoList.add(entity);
            yearBillDtoMap.put(key, dtoList);
        });

        for (String key : yearBillDtoMap.keySet()) {
            List<TocTemporaryCostBillDTO> dtoList = yearBillDtoMap.get(key);

            BigDecimal totalYearReceiveAmount = new BigDecimal("0");
            BigDecimal totalYearAlreadyReceiveAmount = new BigDecimal("0");
            BigDecimal totalYearNoReceiveAmount = new BigDecimal("0");
            BigDecimal totalYearAdjustmentRmbAmount = new BigDecimal("0");

            for (TocTemporaryCostBillDTO billDTO : dtoList) {
                BigDecimal totalReceiveAmount = billDTO.getTotalReceiveAmount() == null ? new BigDecimal("0") : billDTO.getTotalReceiveAmount();
                BigDecimal alreadyReceiveAmount = billDTO.getAlreadyReceiveAmount() == null ? new BigDecimal("0") : billDTO.getAlreadyReceiveAmount();
                BigDecimal adjustmentRmbAmount = billDTO.getAdjustmentRmbAmount() == null ? new BigDecimal("0") : billDTO.getAdjustmentRmbAmount();

                totalYearReceiveAmount = totalYearReceiveAmount.add(totalReceiveAmount);
                totalYearAlreadyReceiveAmount = totalYearAlreadyReceiveAmount.add(alreadyReceiveAmount);
                totalYearAdjustmentRmbAmount = totalYearAdjustmentRmbAmount.add(adjustmentRmbAmount);
            }
            totalYearNoReceiveAmount = totalYearReceiveAmount.subtract(totalYearAlreadyReceiveAmount).add(totalYearAdjustmentRmbAmount);
            Map<String, Object> map = new HashMap<>();
            map.put("merchantName", dtoList.get(0).getMerchantName());
            map.put("year", dtoList.get(0).getMonth().substring(0, 4));
            map.put("shopTypeName", dtoList.get(0).getShopTypeName());
            map.put("storePlatformName", dtoList.get(0).getStorePlatformName());
            map.put("shopName", dtoList.get(0).getShopName());
            map.put("totalReceiveAmount", totalYearReceiveAmount);
            map.put("totalYearAdjustmentRmbAmount", totalYearAdjustmentRmbAmount);
            map.put("alreadyReceiveAmount", totalYearAlreadyReceiveAmount);
            map.put("noReceiveAmount", totalYearNoReceiveAmount);
            map.put("parentBrandName", dtoList.get(0).getParentBrandName());
            map.put("buName", dtoList.get(0).getBuName());
            map.put("platformProjectName", dtoList.get(0).getPlatformProjectName());
            exportYearList.add(map);

        }
        //月份汇总
        String sheetMonthName = "月份汇总";
        String[] monthColumns = {"公司", "暂估应收月份", "运营类型", "平台", "店铺", "事业部", "母品牌", "平台费项名称", "暂估费用金额","差异调整金额",
                "结算费用金额", "剩余暂估费用金额", "结算状态", "结算完成月份"};
        String[] monthKeys = {"merchantName", "month", "shopTypeName", "storePlatformName", "shopName", "buName", "parentBrandName", "platformProjectName", "totalReceiveAmount","adjustmentRmbAmount",
                "alreadyReceiveAmount", "noReceiveAmount", "status", "settlementEndMonth"};

        //年度汇总
        String yearSheetName = "年度汇总";
        String[] yearColumns = {"公司", "暂估应收年份", "运营类型", "平台", "店铺", "事业部", "母品牌", "平台费项名称", "暂估费用金额","差异调整金额", "结算费用金额", "剩余暂估费用金额"};
        String[] yearKeys = {"merchantName", "year", "shopTypeName", "storePlatformName", "shopName", "buName", "parentBrandName", "platformProjectName", "totalReceiveAmount","totalYearAdjustmentRmbAmount",
                "alreadyReceiveAmount", "noReceiveAmount"};
        //生成表格
        ExportExcelSheet monthSheet = ExcelUtilXlsx.createSheet(sheetMonthName, monthColumns, monthKeys, exportList);
        ExportExcelSheet yearSheet = ExcelUtilXlsx.createSheet(yearSheetName, yearColumns, yearKeys, exportYearList);

        List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();
        sheets.add(monthSheet);
        sheets.add(yearSheet);
        return sheets;
    }

    @Override
    public boolean endReceiveBill(Long id, String isEndPunch, String type) throws Exception {

        if ("0".equals(type)) {
            //完结发货订单与退款订单正负红冲，待核销金额=0
            tocTemporaryReceiveBillItemDao.updateEndItemBill(id, isEndPunch);

            //指定id集合查询对应的订单数量和总金额
            List<Map<String, Object>> settleMapList = tocTemporaryReceiveBillItemDao.countByBillIds(Arrays.asList(id));
            for (Map<String, Object> settleMap : settleMapList) {
                Long billId = (Long) settleMap.get("billId");
                BigDecimal temporaryRmbAmount = (BigDecimal) settleMap.get("temporaryRmbAmount");
                Long totalNum = (Long) settleMap.get("totalNum");
                BigDecimal noSettlementAmount = (BigDecimal) settleMap.get("noSettlementAmount");
                BigDecimal alreadyReceiveAmount = (BigDecimal) settleMap.get("alreadyReceiveAmount");
                Long noSettlementNum = (Long) settleMap.get("noSettlementNum");
                Timestamp settlementDate = (Timestamp) settleMap.get("settlementDate");
                TocTemporaryReceiveBillModel billModel = new TocTemporaryReceiveBillModel();
                billModel.setId(billId);
                billModel.setTotalReceiveAmount(temporaryRmbAmount);
                billModel.setTotalReceiveNum(totalNum);
                billModel.setLastReceiveAmount(noSettlementAmount);
                billModel.setAlreadyReceiveAmount(alreadyReceiveAmount);
                billModel.setAlreadyReceiveNum(totalNum - noSettlementNum);

                if (noSettlementNum.equals(0L)) {
                    billModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_2);
                    billModel.setSettlementEndMonth(TimeUtils.formatMonth(settlementDate));
                } else if (totalNum.equals(noSettlementNum)) {
                    billModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_0);
                    billModel.setSettlementEndMonth(null);
                } else {
                    billModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_1);
                    billModel.setSettlementEndMonth(null);
                }

                tocTemporaryReceiveBillDao.modify(billModel);
            }
        }

        if ("1".equals(type)) {
            //完结发货订单与退款订单正负红冲，待核销金额=0
            tocTemporaryReceiveBillCostItemDao.updateEndItemBill(id, isEndPunch);

            List<Map<String, Object>> settleMapList = tocTemporaryReceiveBillCostItemDao.countByBillIds(Arrays.asList(id));
            for (Map<String, Object> settleMap : settleMapList) {
                Long billId = (Long) settleMap.get("billId");
                BigDecimal temporaryRmbAmount = (BigDecimal) settleMap.get("temporaryRmbAmount");
                Long totalNum = (Long) settleMap.get("totalNum");
                BigDecimal noSettlementAmount = (BigDecimal) settleMap.get("noSettlementAmount");
                BigDecimal alreadyReceiveAmount = (BigDecimal) settleMap.get("alreadyReceiveAmount");
                Long noSettlementNum = (Long) settleMap.get("noSettlementNum");
                Timestamp settlementDate = (Timestamp) settleMap.get("settlementDate");

                TocTemporaryCostBillModel billModel = new TocTemporaryCostBillModel();
                billModel.setId(billId);
                billModel.setTotalReceiveAmount(temporaryRmbAmount);
                billModel.setAlreadyReceiveAmount(alreadyReceiveAmount);

                if (noSettlementNum.equals(0L)) {
                    billModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_2);
                    billModel.setSettlementEndMonth(TimeUtils.formatMonth(settlementDate));
                } else if (totalNum.equals(noSettlementNum)) {
                    billModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_0);
                    billModel.setSettlementEndMonth(null);
                } else {
                    billModel.setSettlementStatus(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_1);
                    billModel.setSettlementEndMonth(null);
                }

                tocTemporaryCostBillDao.modify(billModel);
            }
        }


        return true;
    }

    @Override
    public Map<String, Integer> endReceiveBillNum(Long id, String type) throws Exception {

        Map<String, Integer> resultMap = new HashMap<>();
        Integer punchNum = 0;
        Integer nonPunchNum = 0;

        if ("0".equals(type)) {
            punchNum = tocTemporaryReceiveBillItemDao.countPunchNum(id, "0");

            TocTemporaryReceiveBillItemDTO itemDTO = new TocTemporaryReceiveBillItemDTO();
            itemDTO.setBillId(id);
            itemDTO.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);
            int billNum = tocTemporaryReceiveBillItemDao.countTempBillNum(itemDTO);
            nonPunchNum = billNum - punchNum;
        }

        if ("1".equals(type)) {
            punchNum = tocTemporaryReceiveBillCostItemDao.countPunchNum(id, "0");

            TocTemporaryReceiveBillCostItemDTO itemDTO = new TocTemporaryReceiveBillCostItemDTO();
            itemDTO.setBillId(id);
            itemDTO.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);
            int billNum = tocTemporaryReceiveBillCostItemDao.countTempBillNum(itemDTO);
            nonPunchNum = billNum - punchNum;
        }


        resultMap.put("punchNum", punchNum);
        resultMap.put("nonPunchNum", nonPunchNum);
        return resultMap;
    }

    @Override
    public String createItemExcel(FileTaskMongo task, String basePath) throws Exception {

        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> paramMap = jsonData;
        String month = (String) paramMap.get("month");
        Integer merchantId = (Integer) paramMap.get("merchantId");
        String billIds = (String) paramMap.get("billIds");

        basePath = basePath + "/" + task.getTaskType() + "/" + merchantId + "/" + month;

        System.out.println("商家Id=" + merchantId + ",月份=" + month + "，类型：toc暂估收入明细---生成excel文件开始----------");

        boolean isExist = false;

        if (new File(basePath).exists()) {
            isExist = true;
        }

        //表头
        List<Map<String, Object>> exportList = new ArrayList<>();
        TocTemporaryReceiveBillDTO dto = new TocTemporaryReceiveBillDTO();
        dto.setIds(billIds);

        List<TocTemporaryReceiveBillDTO> billDTOs = tocTemporaryReceiveBillDao.listForExport(dto);
        for (TocTemporaryReceiveBillDTO billDTO : billDTOs) {
            Map<String, Object> map = new HashMap<>();
            map.put("merchantName", billDTO.getMerchantName());
            map.put("customerName", billDTO.getCustomerName());
            map.put("storePlatformName", billDTO.getStorePlatformName());
            map.put("shopTypeName", billDTO.getShopTypeName());
            map.put("shopName", billDTO.getShopName());
            map.put("buName", billDTO.getBuName());
            map.put("month", billDTO.getMonth());
            map.put("totalReceiveAmount", billDTO.getTotalReceiveAmount());
            map.put("billNum", billDTO.getTotalReceiveNum());
            map.put("alreadyReceiveNum", billDTO.getAlreadyReceiveNum());
            map.put("alreadyReceiveAmount", billDTO.getAlreadyReceiveAmount());
            map.put("noReceiveNum", billDTO.getTotalReceiveNum() - billDTO.getAlreadyReceiveNum());
            map.put("noReceiveAmount", billDTO.getLastReceiveAmount());
            map.put("settlementEndMonth", billDTO.getSettlementEndMonth());
            map.put("status", billDTO.getSettlementStatusLabel());
            exportList.add(map);
        }
        billDTOs = null;
        //表体
        int pageSize = 5000; //每页5000
        int maxSize = 300000; //每个文件存放最大记录数

        TocTemporaryReceiveBillItemDTO itemDTO = new TocTemporaryReceiveBillItemDTO();
        itemDTO.setBillIds(billIds);
        int exportItemNum = tocTemporaryReceiveBillItemDao.countTempBillNum(itemDTO);

        //每30w数据生成一个excel
        for (int i = 0; i < Math.ceil((float) exportItemNum / maxSize); i++) {

            List<Map<String, Object>> exportItemList = new ArrayList<>();

            Integer total = exportItemNum > maxSize * (i + 1) ? maxSize * (i + 1) : exportItemNum;

            List<Map<String, Object>> tempList = new ArrayList<>();
            for (int j = maxSize * i; j < total; ) {
                int pageSub = (j + pageSize) < total ? (j + pageSize) : total;
                itemDTO.setBegin(j);
                itemDTO.setPageSize(pageSize);
                tempList = tocTemporaryReceiveBillItemDao.listForExportTempItemPage(itemDTO);

                for (Map<String, Object> item : tempList) {
                    String storePlatformCodeItem = (String) item.get("store_platform_code");
                    String orderType = (String) item.get("order_type");
                    String settlementMark = (String) item.get("settlement_mark");

                    item.put("settlementMark", DERP.getLabelByKey(DERP_ORDER.tocTempItemBill_settlementMarkList, settlementMark));
                    item.put("storePlatformName", DERP.getLabelByKey(DERP.storePlatformList, storePlatformCodeItem));
                    item.put("orderType", DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemBill_orderTypeList, orderType));
                    BigDecimal temporaryRmbAmount = item.get("temporary_rmb_amount") != null ? (BigDecimal) item.get("temporary_rmb_amount") : new BigDecimal("0");
                    BigDecimal settlementRmbAmount = item.get("settlement_rmb_amount") != null ? (BigDecimal) item.get("settlement_rmb_amount") : new BigDecimal("0");
                    BigDecimal writeOffAmount = item.get("write_off_amount") != null ? (BigDecimal) item.get("write_off_amount") : new BigDecimal("0");
                    BigDecimal nonVerifyAmount = temporaryRmbAmount.subtract(settlementRmbAmount).subtract(writeOffAmount);
                    item.put("nonVerifyAmount", nonVerifyAmount);
                }

                exportItemList.addAll(tempList);
                tempList.clear();
                j = pageSub;
            }

            //表头信息
            String mainSheetName = "暂估应收列表";
            String[] mainColumns = {"公司", "客户名称", "平台", "开店事业部", "运营类型", "店铺名称", "应收月份", "应收货款",
                    "订单数量", "已结算订单量", "未结算订单量", "剩余暂估应收金额", "结算完成月份", "结算状态"};
            String[] mainKeys = {"merchantName", "customerName", "storePlatformName", "buName", "shopTypeName", "shopName", "month", "totalReceiveAmount",
                    "billNum", "alreadyReceiveNum", "noReceiveNum", "noReceiveAmount", "settlementEndMonth", "status"};

            String itemSheetName = "暂估应收明细";
            String[] itemColumns = {"平台名称", "店铺名称", "应收月份", "事业部", "外部订单号", "系统订单号", "母品牌", "单据类型", "暂估应收金额\n（RMB）",
                    "平台结算货款\n（原币）", "平台结算货款\n（RMB）", "冲销金额\n（RMB）", "未核金额", "结算标识", "结算日期", "结算单号"};
            String[] itemKeys = {"storePlatformName", "shop_name", "month", "bu_name", "external_code", "order_code", "parent_brand_name", "orderType",
                    "temporary_rmb_amount", "settlement_ori_amount", "settlement_rmb_amount", "write_off_amount", "nonVerifyAmount",
                    "settlementMark", "settlement_date", "settlement_code"};

            //生成表格
            ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, mainColumns, mainKeys, exportList);
            ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, exportItemList);

            List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();
            sheets.add(mainSheet);
            sheets.add(itemSheet);

            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);
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

            exportItemList.clear();
        }

        return basePath;
    }

    @Override
    public String createCostExcel(FileTaskMongo task, String basePath) throws Exception {
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> paramMap = jsonData;
        String month = (String) paramMap.get("month");
        Integer merchantId = (Integer) paramMap.get("merchantId");
        String billIds = (String) paramMap.get("billIds");

        basePath = basePath + "/" + task.getTaskType() + "/" + merchantId + "/" + month ;

        System.out.println("商家Id=" + merchantId + ",月份=" + month + "，类型：toc暂估费用明细---生成excel文件开始----------");

        boolean isExist = false;

        if (new File(basePath).exists()) {
            isExist = true;
        }

        //表头
        TocTemporaryCostBillDTO dto = new TocTemporaryCostBillDTO();
        dto.setIds(billIds);
        List<Map<String, Object>> exportList = new ArrayList<>();
        List<TocTemporaryCostBillDTO> billDTOs = tocTemporaryCostBillDao.listForExport(dto);

        for (TocTemporaryCostBillDTO billDTO : billDTOs) {
            Map<String, Object> map = new HashMap<>();
            map.put("merchantName", billDTO.getMerchantName());
            map.put("customerName", billDTO.getCustomerName());
            map.put("storePlatformName", billDTO.getStorePlatformName());
            map.put("shopTypeName", billDTO.getShopTypeName());
            map.put("shopName", billDTO.getShopName());
            map.put("buName", billDTO.getBuName());
            map.put("month", billDTO.getMonth());
            map.put("totalReceiveAmount", billDTO.getTotalReceiveAmount());
            map.put("billNum", billDTO.getTotalReceiveNum());
            map.put("alreadyReceiveNum", billDTO.getAlreadyReceiveNum());
            map.put("alreadyReceiveAmount", billDTO.getAlreadyReceiveAmount());
            map.put("adjustmentRmbAmount", billDTO.getAdjustmentRmbAmount());
            map.put("noReceiveNum", billDTO.getTotalReceiveNum() - billDTO.getAlreadyReceiveNum());
            BigDecimal alreadyReceiveAmount = billDTO.getAlreadyReceiveAmount() != null ? billDTO.getAlreadyReceiveAmount() : new BigDecimal("0");
            BigDecimal adjustmentRmbAmount = billDTO.getAdjustmentRmbAmount() != null ? billDTO.getAdjustmentRmbAmount() : new BigDecimal("0");
            BigDecimal noReceiveAmount = billDTO.getTotalReceiveAmount().subtract(alreadyReceiveAmount).add(adjustmentRmbAmount);
            map.put("noReceiveAmount", noReceiveAmount);
            map.put("settlementEndMonth", billDTO.getSettlementEndMonth());
            map.put("status", billDTO.getSettlementStatusLabel());
            exportList.add(map);
        }
        billDTOs = null;
        //表体
        int pageSize = 10000; //每页5000
        int maxSize = 300000; //每个文件存放最大记录数

        TocTemporaryReceiveBillCostItemDTO itemDTO = new TocTemporaryReceiveBillCostItemDTO();
        itemDTO.setBillIds(billIds);
        int exportItemNum = tocTemporaryReceiveBillCostItemDao.countTempBillNum(itemDTO);


        //每30w数据生成一个excel
        for (int i = 0; i < Math.ceil((float) exportItemNum / maxSize); i++) {

            List<Map<String, Object>> exportItemList = new ArrayList<>();

            Integer total = exportItemNum > maxSize * (i + 1) ? maxSize * (i + 1) : exportItemNum;

            List<Map<String, Object>> tempList = new ArrayList<>();
            for (int j = maxSize * i; j < total; ) {
                int pageSub = (j + pageSize) < total ? (j + pageSize) : total;
                itemDTO.setBegin(j);
                itemDTO.setPageSize(pageSize);
                tempList = tocTemporaryReceiveBillCostItemDao.listForExportTempCostItemPage(itemDTO);

                for (Map<String, Object> item : tempList) {
                    String storePlatformCodeItem = (String) item.get("store_platform_code");
                    String orderType = (String) item.get("order_type");
                    String settlementMark = (String) item.get("settlement_mark");

                    item.put("settlementMark", DERP.getLabelByKey(DERP_ORDER.tocTempItemCostBill_settlementMarkList, settlementMark));
                    item.put("storePlatformName", DERP.getLabelByKey(DERP.storePlatformList, storePlatformCodeItem));
                    item.put("orderType", DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemCostBill_orderTypeList, orderType));
                    BigDecimal temporaryRmbCost = item.get("temporary_rmb_cost") != null ? (BigDecimal) item.get("temporary_rmb_cost") : new BigDecimal("0");
                    BigDecimal settlementRmbCost = item.get("settlement_rmb_cost") != null ? (BigDecimal) item.get("settlement_rmb_cost") : new BigDecimal("0");
                    BigDecimal writeOffAmount = item.get("write_off_amount") != null ? (BigDecimal) item.get("write_off_amount") : new BigDecimal("0");
                    BigDecimal adjustmentRmbAmount = item.get("adjustment_rmb_amount") != null ? (BigDecimal) item.get("adjustment_rmb_amount") : new BigDecimal("0");
                    BigDecimal nonVerifyAmount = temporaryRmbCost.subtract(settlementRmbCost).subtract(writeOffAmount).add(adjustmentRmbAmount);
                    item.put("nonVerifyAmount", nonVerifyAmount);
                }

                exportItemList.addAll(tempList);
                tempList.clear();
                j = pageSub;
            }

            //表头信息
            String mainSheetName = "暂估费用列表";
            String[] mainColumns = {"公司", "客户名称", "平台", "开店事业部", "运营类型", "店铺名称", "应收月份", "应收货款",
                    "订单数量", "已结算订单量", "未结算订单量", "费用调整金额", "剩余暂估应收金额", "结算完成月份", "结算状态"};
            String[] mainKeys = {"merchantName", "customerName", "storePlatformName", "buName", "shopTypeName", "shopName", "month", "totalReceiveAmount",
                    "billNum", "alreadyReceiveNum", "noReceiveNum", "adjustmentRmbAmount", "noReceiveAmount", "settlementEndMonth", "status"};

            String itemSheetName = "暂估费用明细";
            String[] itemColumns = {"平台名称", "店铺名称", "应收月份", "事业部", "外部订单号", "系统订单号", "母品牌", "单据类型", "系统费项名称", "平台费项名称", "暂估应收金额\n（RMB）",
                    "平台结算货款\n（原币）", "平台结算货款\n（RMB）", "冲销金额\n（RMB）", "费用调整金额\n（RMB）", "未核金额", "结算标识", "结算日期", "结算单号"};
            String[] itemKeys = {"storePlatformName", "shop_name", "month", "bu_name", "external_code", "order_code", "parent_brand_name", "orderType",
                    "project_name", "platform_project_name", "temporary_rmb_cost", "settlement_ori_cost", "settlement_rmb_cost", "write_off_amount", "adjustment_rmb_amount",
                    "nonVerifyAmount", "settlementMark", "settlement_date", "settlement_code"};

            //生成表格
            ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, mainColumns, mainKeys, exportList);
            ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, exportItemList);

            List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();
            sheets.add(mainSheet);
            sheets.add(itemSheet);

            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);
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
            exportItemList.clear();
        }

        exportList = null;
        return basePath;
    }

    @Override
    public String createTempItemExcel(FileTaskMongo task, String basePath) throws Exception {
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> paramMap = jsonData;
        String month = (String) paramMap.get("month");
        Integer merchantId = (Integer) paramMap.get("merchantId");
        String billIds = (String) paramMap.get("billIds");

        basePath = basePath + "/" + task.getTaskType() + "/" + merchantId + "/" + month;

        System.out.println("商家Id=" + merchantId + ",月份=" + month + "，类型：toc暂估收入累计暂估---生成excel文件开始----------");

        boolean isExist = false;

        if (new File(basePath).exists()) {
            isExist = true;
        }

        int pageSize = 5000; //每页5000
        int maxSize = 300000; //每个文件存放最大记录数

        TocTemporaryReceiveBillItemDTO itemDTO = new TocTemporaryReceiveBillItemDTO();
        itemDTO.setBillIds(billIds);
        itemDTO.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);

        String sheetName = "累计暂估应收";
        String[] columns = {"公司", "客户", "平台", "店铺", "应收月份", "外部订单号", "母品牌", "事业部", "商品件数", "暂估应收金额", "暂估未核销金额", "币种", "结算标识"};
        String[] keys = {"merchant_name", "customer_name", "storePlatformName", "shop_name", "month", "external_code", "parent_brand_name", "bu_name", "sale_num",
                "temporary_rmb_amount", "nonVerifyAmount", "temporaryCurrency", "settlementMark"};

        int exportItemNum = tocTemporaryReceiveBillItemDao.countTempBillNum(itemDTO);

        //每30w数据生成一个excel
        for (int i = 0; i < Math.ceil((float) exportItemNum / maxSize); i++) {

            List<Map<String, Object>> exportItemList = new ArrayList<>();

            Integer total = exportItemNum > maxSize * (i + 1) ? maxSize * (i + 1) : exportItemNum;

            for (int j = maxSize * i; j < total; ) {
                int pageSub = (j + pageSize) < total ? (j + pageSize) : total;
                itemDTO.setBegin(j);
                itemDTO.setPageSize(pageSize);
                List<Map<String, Object>> tempList = tocTemporaryReceiveBillItemDao.listForExportTempItemPage(itemDTO);

                for (Map<String, Object> item : tempList) {
                    String storePlatformCodeItem = (String) item.get("store_platform_code");
                    String orderType = (String) item.get("order_type");
                    String settlementMark = (String) item.get("settlement_mark");
                    String temporaryCurrency = (String) item.get("temporary_currency");

                    item.put("settlementMark", DERP.getLabelByKey(DERP_ORDER.tocTempItemBill_settlementMarkList, settlementMark));
                    item.put("storePlatformName", DERP.getLabelByKey(DERP.storePlatformList, storePlatformCodeItem));
                    item.put("orderType", DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemBill_orderTypeList, orderType));
                    item.put("temporaryCurrency", DERP.getLabelByKey(DERP.currencyCodeList, temporaryCurrency));
                    BigDecimal temporaryRmbAmount = item.get("temporary_rmb_amount") != null ? (BigDecimal) item.get("temporary_rmb_amount") : new BigDecimal("0");
                    BigDecimal settlementRmbAmount = item.get("settlement_rmb_amount") != null ? (BigDecimal) item.get("settlement_rmb_amount") : new BigDecimal("0");
                    BigDecimal writeOffAmount = item.get("write_off_amount") != null ? (BigDecimal) item.get("write_off_amount") : new BigDecimal("0");
                    BigDecimal nonVerifyAmount = temporaryRmbAmount.subtract(settlementRmbAmount).subtract(writeOffAmount);
                    item.put("nonVerifyAmount", nonVerifyAmount);
                }

                exportItemList.addAll(tempList);
                j = pageSub;
            }

            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel8(sheetName, columns, keys, exportItemList);
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
            exportItemList.clear();
        }
        return basePath;
    }


    @Override
    public String createTempCostExcel(FileTaskMongo task, String basePath) throws Exception {
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> paramMap = jsonData;
        String month = (String) paramMap.get("month");
        Integer merchantId = (Integer) paramMap.get("merchantId");
        String billIds = (String) paramMap.get("billIds");

        basePath = basePath + "/" + task.getTaskType() + "/" + merchantId + "/" + month;

        System.out.println("商家Id=" + merchantId + ",月份=" + month + "，类型：toc暂估费用累计暂估---生成excel文件开始----------");

        boolean isExist = false;

        if (new File(basePath).exists()) {
            isExist = true;
        }

        int pageSize = 5000; //每页5000
        int maxSize = 300000; //每个文件存放最大记录数

        TocTemporaryReceiveBillCostItemDTO itemDTO = new TocTemporaryReceiveBillCostItemDTO();
        itemDTO.setBillIds(billIds);
        itemDTO.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);

        String sheetCostName = "累计暂估费用";
        String[] costColumns = {"公司", "客户", "平台", "店铺", "应收月份", "外部订单号", "母品牌", "事业部", "平台费项名称", "系统费项名称", "暂估费用金额", "暂估未核销金额", "币种", "结算标识"};
        String[] costKeys = {"merchant_name", "customer_name", "storePlatformName", "shop_name", "month", "external_code", "parent_brand_name", "bu_name", "platform_project_name",
                "project_name", "temporary_rmb_cost", "nonVerifyAmount", "temporaryCurrency", "settlementMark"};

        int exportItemNum = tocTemporaryReceiveBillCostItemDao.countTempBillNum(itemDTO);

        //每30w数据生成一个excel
        for (int i = 0; i < Math.ceil((float) exportItemNum / maxSize); i++) {

            List<Map<String, Object>> exportItemList = new ArrayList<>();

            Integer total = exportItemNum > maxSize * (i + 1) ? maxSize * (i + 1) : exportItemNum;

            for (int j = maxSize * i; j < total; ) {
                int pageSub = (j + pageSize) < total ? (j + pageSize) : total;
                itemDTO.setBegin(j);
                itemDTO.setPageSize(pageSize);
                List<Map<String, Object>> tempList = tocTemporaryReceiveBillCostItemDao.listForExportTempCostItemPage(itemDTO);

                for (Map<String, Object> item : tempList) {
                    String storePlatformCodeItem = (String) item.get("store_platform_code");
                    String orderType = (String) item.get("order_type");
                    String settlementMark = (String) item.get("settlement_mark");
                    String temporaryCurrency = (String) item.get("temporary_currency");

                    item.put("temporaryCurrency", DERP.getLabelByKey(DERP.currencyCodeList, temporaryCurrency));
                    item.put("settlementMark", DERP.getLabelByKey(DERP_ORDER.tocTempItemCostBill_settlementMarkList, settlementMark));
                    item.put("storePlatformName", DERP.getLabelByKey(DERP.storePlatformList, storePlatformCodeItem));
                    item.put("orderType", DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemCostBill_orderTypeList, orderType));
                    BigDecimal temporaryRmbCost = item.get("temporary_rmb_cost") != null ? (BigDecimal) item.get("temporary_rmb_cost") : new BigDecimal("0");
                    BigDecimal settlementRmbCost = item.get("settlement_rmb_cost") != null ? (BigDecimal) item.get("settlement_rmb_cost") : new BigDecimal("0");
                    BigDecimal writeOffAmount = item.get("write_off_amount") != null ? (BigDecimal) item.get("write_off_amount") : new BigDecimal("0");
                    BigDecimal nonVerifyAmount = temporaryRmbCost.subtract(settlementRmbCost).subtract(writeOffAmount);
                    item.put("nonVerifyAmount", nonVerifyAmount);
                }

                exportItemList.addAll(tempList);
                j = pageSub;
            }

            //生成表格
            SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetCostName, costColumns, costKeys, exportItemList);
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
            exportItemList.clear();
        }
        return basePath;
    }

    @Override
    public APIResponse queryTocTempReceiveBill(QueryTocTempReceiveBillDTO dto) throws Exception {
        APIResponse apiResponse = new APIResponse();
        Map<String, Object> param = new HashMap<>();
        param.put("topidealCode", dto.getMerchantCode());
        MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(param);
        if(merchantInfoMongo == null) {
            apiResponse.setCode("999");
            apiResponse.setMessage("不存在此公司编码:" + dto.getMerchantCode());
            return apiResponse;
        }

        if(dto.getPageSize() == null || (dto.getPageSize() != null && dto.getPageSize() > 100)) {
            dto.setPageSize(100);
        }

        TocTemporaryReceiveBillItemDTO searchDTO = new TocTemporaryReceiveBillItemDTO();
        searchDTO.setPageNo(dto.getPageNo() == null ? 1 : dto.getPageNo());
        searchDTO.setPageSize(dto.getPageSize());
        searchDTO.setMerchantId(merchantInfoMongo.getMerchantId());
        searchDTO.setStorePlatformCode(dto.getStorePlatformCode());
        searchDTO.setSettlementMark(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0);
        searchDTO.setMonthStart(dto.getStartMonth());
        searchDTO.setMonthEnd(dto.getEndMonth());
        searchDTO.setBegin((dto.getPageNo() - 1) * dto.getPageSize());

        int count = tocTemporaryReceiveBillItemDao.countTempBillNum(searchDTO);
        int pageNo = 0;
        int totalCount = 0;
        if(count > 0) {
            TocTemporaryReceiveBillItemDTO itemDTO = tocTemporaryReceiveBillItemDao.getListByPage(searchDTO);
            pageNo = itemDTO.getPageNo();
            totalCount = itemDTO.getTotal();
            List<TocTemporaryReceiveBillItemDTO> receiveBillDTOS = itemDTO.getList();

            List<Map<String, Object>> resultList = receiveBillDTOS.stream().map(entity -> {
                BigDecimal settlementRmbCost = entity.getSettlementRmbAmount() != null ? entity.getSettlementRmbAmount() : new BigDecimal("0");
                BigDecimal writeOffAmount = entity.getWriteOffAmount() != null ? entity.getWriteOffAmount() : new BigDecimal("0");
                BigDecimal nonVerifyAmount = entity.getTemporaryRmbAmount().subtract(settlementRmbCost).subtract(writeOffAmount);

                Map<String, Object> map = new HashMap();
                map.put("orderCode", entity.getOrderCode());
                map.put("externalCode", entity.getExternalCode());
                map.put("shopCode", entity.getShopCode());
                map.put("settlementMark", entity.getSettlementMark());
                map.put("noVerifyAmount", nonVerifyAmount);
                map.put("orderType", entity.getOrderType());
                return map;
            }).collect(Collectors.toList());
            apiResponse.setDataList(resultList);
        }

        apiResponse.setTotalCount(totalCount);
        apiResponse.setCode("0000");
        apiResponse.setMessage("成功");
        apiResponse.setPageNo(pageNo);
        return apiResponse;
    }

    @Override
    public String createTempReceiveSummaryExcel(FileTaskMongo task, String basePath) throws Exception{
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        TocTemporaryReceiveBillDTO dto = (TocTemporaryReceiveBillDTO) JSONObject.toBean(jsonData, TocTemporaryReceiveBillDTO.class);
        LOGGER.debug("createTempReceiveSummaryExcel dto:{}", JSON.toJSONString(dto));
        String month = null;
        if(StringUtils.isNotBlank(dto.getMonthStart()) || StringUtils.isNotBlank(dto.getMonthEnd())) {
            month = dto.getMonthStart() + "-" + dto.getMonthEnd();
        }else {
            month = TimeUtils.formatDay(new Date());
        }

        basePath = basePath + "/" + task.getTaskType() + "/" + dto.getMerchantId() + "/" + month;

        boolean isExist = false;

        if (new File(basePath).exists()) {
            isExist = true;
        }

        List<ExportExcelSheet> sheets = exportSummaryBill(dto, null);

        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);

        //导出文件
        String fileName = task.getRemark() + ".xlsx";

        if (isExist) {
            //删除目录下的子文件
            DownloadExcelUtil.deleteFile(basePath);
        }

        //创建目录
        new File(basePath).mkdirs();

        FileOutputStream fileOut = new FileOutputStream(basePath + "/" + fileName);
        wb.write(fileOut);
        fileOut.close();

        System.out.println("文件创建结束");
        return basePath;
    }

    @Override
    public String createTempCostSummaryExcel(FileTaskMongo task, String basePath) throws Exception{
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        TocTemporaryCostBillDTO dto = (TocTemporaryCostBillDTO) JSONObject.toBean(jsonData, TocTemporaryCostBillDTO.class);
        LOGGER.debug("createTempCostSummaryExcel dto:{}", JSON.toJSONString(dto));
        String month = null;
        if(StringUtils.isNotBlank(dto.getMonthStart()) || StringUtils.isNotBlank(dto.getMonthEnd())) {
            month = dto.getMonthStart() + "-" + dto.getMonthEnd();
        }else {
            month = TimeUtils.formatDay(new Date());
        }

        basePath = basePath + "/" + task.getTaskType() + "/" + dto.getMerchantId() + "/" + month;

        boolean isExist = false;

        if (new File(basePath).exists()) {
            isExist = true;
        }

        List<ExportExcelSheet> sheets = exportCostSummaryBill(dto, null);

        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);

        //导出文件
        String fileName = task.getRemark() + ".xlsx";

        if (isExist) {
            //删除目录下的子文件
            DownloadExcelUtil.deleteFile(basePath);
        }

        //创建目录
        new File(basePath).mkdirs();

        FileOutputStream fileOut = new FileOutputStream(basePath + "/" + fileName);
        wb.write(fileOut);
        fileOut.close();

        System.out.println("文件创建结束");
        return basePath;
    }


}
