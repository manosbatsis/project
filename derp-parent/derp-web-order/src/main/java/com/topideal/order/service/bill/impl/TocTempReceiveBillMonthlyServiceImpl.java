package com.topideal.order.service.bill.impl;

import com.alibaba.fastjson.JSON;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.dao.bill.TocTemporaryReceiveBillCostItemMonthlyDao;
import com.topideal.dao.bill.TocTemporaryReceiveBillItemMonthlyDao;
import com.topideal.entity.dto.bill.TocTempCostBillItemMonthlyDTO;
import com.topideal.entity.dto.bill.TocTempReceiveBillItemMonthlyDTO;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.order.service.bill.TocTempReceiveBillMonthlyService;
import com.topideal.order.tools.DownloadExcelUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/17 10:43
 * @Description: Toc暂估月结
 */
@Service
public class TocTempReceiveBillMonthlyServiceImpl implements TocTempReceiveBillMonthlyService {

    protected Logger LOGGER = LoggerFactory.getLogger(TocTempReceiveBillMonthlyServiceImpl.class);

    @Autowired
    private TocTemporaryReceiveBillCostItemMonthlyDao tocTemporaryReceiveBillCostItemMonthlyDao;
    @Autowired
    private TocTemporaryReceiveBillItemMonthlyDao tocTemporaryReceiveBillItemMonthlyDao;
    private static final String ORDER_TYPE_RECEIVE = "RECEIVE";
    private static final String ORDER_TYPE_COST = "COST";

    @Override
    public TocTempReceiveBillItemMonthlyDTO listReceiveByPage(User user, TocTempReceiveBillItemMonthlyDTO dto) {
        TocTempReceiveBillItemMonthlyDTO billDTO = tocTemporaryReceiveBillItemMonthlyDao.listDTOByPage(dto);
        return billDTO;
    }

    @Override
    public TocTempCostBillItemMonthlyDTO listCostByPage(User user, TocTempCostBillItemMonthlyDTO dto) {
        TocTempCostBillItemMonthlyDTO billDTO = tocTemporaryReceiveBillCostItemMonthlyDao.listDTOByPage(dto);
        return billDTO;
    }

    @Override
    public int countByDTO(TocTempReceiveBillItemMonthlyDTO dto, String type) {
        if(StringUtils.equals(type, ORDER_TYPE_RECEIVE)) {
            //收入月结快照
            return tocTemporaryReceiveBillItemMonthlyDao.countByDTO(dto);
        }else {
            //费用月结快照
            TocTempCostBillItemMonthlyDTO costDto = new TocTempCostBillItemMonthlyDTO();
            costDto.setMerchantId(dto.getMerchantId());
            costDto.setMonth(dto.getMonth());
            return tocTemporaryReceiveBillCostItemMonthlyDao.countByDTO(costDto);
        }
    }

    @Override
    public String createReceiveBillExcel(FileTaskMongo task, String basePath) throws Exception{
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        Map<String, Object> paramMap = jsonData;
        TocTempReceiveBillItemMonthlyDTO dto = (TocTempReceiveBillItemMonthlyDTO) JSONObject.toBean(jsonData, TocTempReceiveBillItemMonthlyDTO.class);
        LOGGER.debug("createReceiveBillExcel dto:{}", JSON.toJSONString(dto));

        basePath = basePath + "/" + task.getTaskType() + "/" + dto.getMerchantId() + "/" + dto.getMonth();

        System.out.println("商家Id=" + dto.getMerchantId() + ",月份=" + dto.getMonth() + "，类型：2C暂估收入月结快照---生成excel文件开始----------");

        boolean isExist = false;

        if (new File(basePath).exists()) {
            isExist = true;
        }

        String mainSheetName = "2C月结暂估收入汇总";
        String itemSheetName = "2C月结暂估收入明细";
        String[] mainColumns = {"月结月份", "公司", "暂估月份", "运营类型", "平台", "客户名称", "店铺", "事业部", "母品牌", "剩余暂估应收金额"};
        String[] itemColumns = {"月结月份", "公司","事业部", "暂估月份", "运营类型", "平台", "客户名称", "店铺", "外部订单号", "系统订单号", "母品牌", "单据类型", "暂估应收金额（RMB）", "平台结算金额（RMB）",
                "冲销金额（RMB）", "未核金额（RMB）", "结算单号"};

        String[] mainKeys = {"month", "merchant_name", "temp_month", "shop_type_code", "store_platform_code", "customer_name", "shop_name", "bu_name", "parent_brand_name","remainAmount"};
        String[] itemKeys = {"month", "merchant_name", "bu_name", "temp_month", "shop_type_code", "store_platform_code", "customer_name", "shop_name", "external_code","order_code","parent_brand_name",
                "order_type", "temporary_rmb_amount", "settlement_rmb_amount", "write_off_amount","non_verify_amount","settlement_code"};

        //表头
        List<Map<String, Object>> sumList = tocTemporaryReceiveBillItemMonthlyDao.listSumForExport(dto);
        sumList.forEach(entity -> {
            String shopTypeCode = (String) entity.get("shop_type_code");
            String storePlatformCode = (String) entity.get("store_platform_code");
            entity.put("shop_type_code", DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode));
            entity.put("store_platform_code", DERP.getLabelByKey(DERP.storePlatformList, storePlatformCode));
        });

        int pageSize = 5000; //每页5000
        int maxSize = 300000; //每个文件存放最大记录数
        int exportItemNum = tocTemporaryReceiveBillItemMonthlyDao.countByDTO(dto);
        boolean isMutiFile = true;
        //每30w数据生成一个excel
        for (int i = 0; i < Math.ceil((float) exportItemNum / maxSize); i++) {

            List<Map<String, Object>> exportItemList = new ArrayList<>();

            Integer total = exportItemNum > maxSize * (i + 1) ? maxSize * (i + 1) : exportItemNum;

            for (int j = maxSize * i; j < total; ) {
                int pageSub = (j + pageSize) < total ? (j + pageSize) : total;
                dto.setBegin(j);
                dto.setPageSize(pageSize);
                List<Map<String, Object>> itemList=tocTemporaryReceiveBillItemMonthlyDao.listForMapItem(dto);

                for (Map<String, Object> item : itemList) {
                    String storePlatformCodeItem = (String) item.get("store_platform_code");
                    String orderType = (String) item.get("order_type");
                    String settlementMark = (String) item.get("settlement_mark");
                    String temporaryCurrency = (String) item.get("temporary_currency");
                    String shopTypeCode = (String) item.get("shop_type_code");

                    item.put("settlement_mark", DERP.getLabelByKey(DERP_ORDER.tocTempItemBill_settlementMarkList, settlementMark));
                    item.put("store_platform_code", DERP.getLabelByKey(DERP.storePlatformList, storePlatformCodeItem));
                    item.put("order_type", DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemBill_orderTypeList, orderType));
                    item.put("temporary_currency", DERP.getLabelByKey(DERP.currencyCodeList, temporaryCurrency));
                    BigDecimal nonVerifyAmount = item.get("last_receive_amount") != null ? (BigDecimal) item.get("last_receive_amount") : new BigDecimal("0");
                    item.put("shop_type_code", DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode));
                    item.put("non_verify_amount", nonVerifyAmount);
                }

                exportItemList.addAll(itemList);
                j = pageSub;
            }

            //生成表格
            List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();
            if(isMutiFile) {
                ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, mainColumns, mainKeys, sumList);
                sheets.add(mainSheet);
            }

            ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, exportItemList);
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
            isMutiFile = false;
        }
        return basePath;
    }

    @Override
    public String createCostBillExcel(FileTaskMongo task, String basePath) throws Exception{
        //解析json参数
        JSONObject jsonData = JSONObject.fromObject(task.getParam());
        TocTempCostBillItemMonthlyDTO dto = (TocTempCostBillItemMonthlyDTO) JSONObject.toBean(jsonData, TocTempCostBillItemMonthlyDTO.class);
        LOGGER.debug("createCostBillExcel dto:{}", JSON.toJSONString(dto));
        basePath = basePath + "/" + task.getTaskType() + "/" + dto.getMerchantId() + "/" + dto.getMonth();

        System.out.println("商家Id=" + dto.getMerchantId() + ",月份=" + dto.getMonth() + "，类型：2C暂估费用月结快照---生成excel文件开始----------");

        boolean isExist = false;

        if (new File(basePath).exists()) {
            isExist = true;
        }

        String mainSheetName = "2C月结暂估费用汇总";
        String itemSheetName = "2C月结暂估费用明细";
        String[] mainColumns = {"月结月份", "公司", "暂估月份", "运营类型", "平台", "客户名称", "店铺", "事业部", "母品牌", "平台费项名称", "系统费项名称", "剩余暂估应收金额"};
        String[] itemColumns = {"月结月份", "公司","事业部", "暂估月份", "运营类型", "平台", "客户名称", "店铺", "外部订单号", "系统订单号", "母品牌", "单据类型", "平台费项名称", "系统费项名称", "暂估应收金额\n（RMB）", "平台结算金额\n（RMB）",
                "冲销金额（RMB）","差异调整金额\n（RMB）", "未核金额（RMB）", "结算单号"};

        String[] mainKeys = {"month", "merchant_name", "temp_month", "shop_type_code", "store_platform_code", "customer_name", "shop_name", "bu_name", "parent_brand_name","platform_project_name", "project_name","remainAmount"};
        String[] itemKeys = {"month", "merchant_name", "bu_name", "temp_month", "shop_type_code", "store_platform_code", "customer_name", "shop_name", "external_code","order_code","parent_brand_name",
                "order_type", "platform_project_name", "project_name", "temporary_rmb_cost", "settlement_rmb_amount", "write_off_amount", "adjustment_rmb_amount","non_verify_amount","settlement_code"};

        //表头
        List<Map<String, Object>> sumList = tocTemporaryReceiveBillCostItemMonthlyDao.listSumForExport(dto);
        sumList.forEach(entity -> {
            String shopTypeCode = (String) entity.get("shop_type_code");
            String storePlatformCode = (String) entity.get("store_platform_code");
            entity.put("shop_type_code", DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode));
            entity.put("store_platform_code", DERP.getLabelByKey(DERP.storePlatformList, storePlatformCode));
        });

        int pageSize = 5000; //每页5000
        int maxSize = 300000; //每个文件存放最大记录数
        int exportItemNum = tocTemporaryReceiveBillCostItemMonthlyDao.countByDTO(dto);
        boolean isMutiFile = true; //是否多份文件

        //每30w数据生成一个excel
        for (int i = 0; i < Math.ceil((float) exportItemNum / maxSize); i++) {

            List<Map<String, Object>> exportItemList = new ArrayList<>();

            Integer total = exportItemNum > maxSize * (i + 1) ? maxSize * (i + 1) : exportItemNum;

            for (int j = maxSize * i; j < total; ) {
                int pageSub = (j + pageSize) < total ? (j + pageSize) : total;
                dto.setBegin(j);
                dto.setPageSize(pageSize);
                List<Map<String, Object>> itemList=tocTemporaryReceiveBillCostItemMonthlyDao.listForMapItem(dto);

                for (Map<String, Object> item : itemList) {
                    String storePlatformCodeItem = (String) item.get("store_platform_code");
                    String orderType = (String) item.get("order_type");
                    String settlementMark = (String) item.get("settlement_mark");
                    String shopTypeCode = (String) item.get("shop_type_code");

                    item.put("settlement_mark", DERP.getLabelByKey(DERP_ORDER.tocTempItemCostBill_settlementMarkList, settlementMark));
                    item.put("store_platform_code", DERP.getLabelByKey(DERP.storePlatformList, storePlatformCodeItem));
                    item.put("order_type", DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemCostBill_orderTypeList, orderType));
                    BigDecimal nonVerifyAmount = item.get("last_receive_amount") != null ? (BigDecimal) item.get("last_receive_amount") : new BigDecimal("0");
                    item.put("non_verify_amount", nonVerifyAmount);
                    item.put("shop_type_code", DERP_SYS.getLabelByKey(DERP_SYS.merchantShopRel_shopTypeList, shopTypeCode));
                }

                exportItemList.addAll(itemList);
                j = pageSub;
            }

            //生成表格
            List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>();
            if(isMutiFile) {
                ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, mainColumns, mainKeys, sumList);
                sheets.add(mainSheet);
            }

            ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, exportItemList);
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
            isMutiFile = false;
        }
        return basePath;
    }
}
