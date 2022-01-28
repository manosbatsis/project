package com.topideal.service.order.impl;

import com.alibaba.fastjson.JSON;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.SourceStatusEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.dao.bill.TocSettlementReceiveBillCostItemDao;
import com.topideal.dao.bill.TocSettlementReceiveBillDao;
import com.topideal.dao.bill.TocSettlementReceiveBillItemDao;
import com.topideal.dao.common.PlatformSettlementConfigDao;
import com.topideal.dao.sale.OrderDao;
import com.topideal.dao.sale.OrderItemDao;
import com.topideal.entity.dto.api.TocSettleBillDTO;
import com.topideal.entity.dto.api.TocSettleBillDataDTO;
import com.topideal.entity.dto.api.TocSettleBillResponseDTO;
import com.topideal.entity.dto.api.TocSettleOrderListDTO;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillItemDTO;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillCostItemModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillItemModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillModel;
import com.topideal.entity.vo.common.PlatformSettlementConfigModel;
import com.topideal.entity.vo.platformdata.PlatformSettleDetailModel;
import com.topideal.entity.vo.platformdata.PlatformSettleInfoModel;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.service.order.SaveToCReceiveBillService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description: 生成Toc结算单
 * @Author: Chen Yiluan
 * @Date: 2021/03/08 14:07
 **/
@Service
public class SaveToCReceiveBillServiceImpl implements SaveToCReceiveBillService {

    public static final Logger LOGGER = LoggerFactory.getLogger(SaveToCReceiveBillServiceImpl.class);


    @Autowired
    private TocSettlementReceiveBillDao tocSettlementReceiveBillDao;
    @Autowired
    private TocSettlementReceiveBillCostItemDao tocSettlementReceiveBillCostItemDao;
    @Autowired
    private TocSettlementReceiveBillItemDao tocSettlementReceiveBillItemDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private PlatformSettlementConfigDao platformSettlementConfigDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    @Autowired
    private MerchantShopRelMongoDao merchantShopRelMongoDao;
    @Autowired
    private BrandSuperiorMongoDao brandSuperiorMongoDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private AttachmentInfoMongoDao attachmentInfoMongoDao;
    @Autowired
    private UserInfoMongoDao userInfoMongoDao;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201160005,model=DERP_LOG_POINT.POINT_13201160005_Label, keyword = "billCode")
    public boolean saveToCReceiveBill(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        String billCode = (String) jsonData.get("billCode");
        if (!StringUtils.isNotBlank(billCode)) {
            throw new RuntimeException("ToC结算单号：" + billCode + "不能为空！");
        }

        TocSettlementReceiveBillModel billModel = new TocSettlementReceiveBillModel();
        billModel.setCode(billCode);
        TocSettlementReceiveBillModel exist = tocSettlementReceiveBillDao.searchByModel(billModel);

        if (exist == null) {
            throw new RuntimeException("ToC结算单号：" + billCode + "对应的结算单不存在！");
        }

        String filePath = exist.getFilePath();
        if (!StringUtils.isNotBlank(filePath)) {
            TocSettlementReceiveBillModel update = new TocSettlementReceiveBillModel();
            update.setId(exist.getId());
            update.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_08);
            tocSettlementReceiveBillDao.modify(update);
            LOGGER.error("ToC结算单号：" + billCode + "对应的结算单的导入文件路径为空！");
            return false;
//            throw new RuntimeException("ToC结算单号：" + billCode + "对应的结算单的导入文件路径为空！");
        }

        //获取源文件
        List<Map<String, String>> sheetList = ExcelUtilXlsx.parseSheetOne(filePath);

        //查询成功订单号
        List<String> successOrderList = new ArrayList<>();
        //查询失败订单号
        List<String> failureOrderList = new ArrayList<>();
        //查询不存在订单号
        List<String> noExistOrderList = new ArrayList<>();

        Map<String, OrderModel> orderCodeMap = new HashMap<>();


        Long superiorParentBrandId = null;
        String superiorParentBrandName = null;
        String superiorParentBrandNcCode = null;

        if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(exist.getShopTypeCode())) {
            //根据店铺编码取平台编码
            Map<String, Object> shopParams = new HashMap<>();
            shopParams.put("shopCode", exist.getShopCode());
            MerchantShopRelMongo shop = merchantShopRelMongoDao.findOne(shopParams);
            if (shop == null) {
                TocSettlementReceiveBillModel update = new TocSettlementReceiveBillModel();
                update.setId(exist.getId());
                update.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_08);
                tocSettlementReceiveBillDao.modify(update);
                LOGGER.error("店铺为空");
                return false;
//                throw new RuntimeException("店铺为空");
            }

            if (StringUtils.isBlank(shop.getSuperiorParentBrandNcCode())) {
                TocSettlementReceiveBillModel update = new TocSettlementReceiveBillModel();
                update.setId(exist.getId());
                update.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_08);
                tocSettlementReceiveBillDao.modify(update);
                LOGGER.error("店铺的专营母品牌未维护");
                return false;
//                throw new RuntimeException("店铺的专营母品牌未维护");
            }
            superiorParentBrandId = shop.getSuperiorParentBrandId();
            superiorParentBrandName = shop.getSuperiorParentBrandName();
            superiorParentBrandNcCode = shop.getSuperiorParentBrandNcCode();
        } else {
            //查询母品牌为“其他”的母品牌
            Map<String, Object> params = new HashMap<>();
            params.put("ncCode", "999");
            BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(params);
            if(brandSuperior == null) {
                TocSettlementReceiveBillModel update = new TocSettlementReceiveBillModel();
                update.setId(exist.getId());
                update.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_08);
                tocSettlementReceiveBillDao.modify(update);
                LOGGER.error("其他的母品牌未维护");
                return false;
            }
//            throw new RuntimeException("其他的母品牌未维护");
            superiorParentBrandId = brandSuperior.getBrandSuperiorId();
            superiorParentBrandName = brandSuperior.getName();
            superiorParentBrandNcCode = brandSuperior.getNcCode();

        }

        boolean isError = false;
        List<TocSettlementReceiveBillItemDTO> itemDTOS = new ArrayList<TocSettlementReceiveBillItemDTO>();
        List<TocSettlementReceiveBillItemDTO> rightItemDTOS = new ArrayList<TocSettlementReceiveBillItemDTO>();

        Map<Long, List<TocSettlementReceiveBillCostItemDTO>> rightCostItemDTOSMap = new HashMap<>();
        List<TocSettlementReceiveBillCostItemModel> costItemModels = new ArrayList<TocSettlementReceiveBillCostItemModel>();
        List<TocSettlementReceiveBillItemModel> itemModels = new ArrayList<TocSettlementReceiveBillItemModel>();

        for (int j = 1 ; j <= sheetList.size() ; j ++) {
            Map<String, String> orderMap = sheetList.get(j - 1);
            TocSettlementReceiveBillItemDTO itemDTO = new TocSettlementReceiveBillItemDTO() ;
            String exportCode = orderMap.get("电商订单号") ;
            if (StringUtils.isNotBlank(exportCode)) {
                itemDTO.setExternalCode(exportCode.trim());
            }
            String remark = orderMap.get("备注") ;
            if (StringUtils.isNotBlank(remark)) {
                itemDTO.setRemark(remark.trim());
            }
            String platformProjectName = orderMap.get("结算费项");
            if (StringUtils.isNotBlank(platformProjectName)) {
                itemDTO.setPlatformProjectName(platformProjectName);
            }

            String originalAmountStr = orderMap.get("订单结算金额\n（结算原币）");
            if(StringUtils.isNotBlank(originalAmountStr)) {
                if (!isNumeric(originalAmountStr)) {
                    setExceptionMsg(itemDTO, itemDTOS, "是", "订单结算金额（结算原币）必须为数值");
                    isError = true;
                    continue;
                }
                BigDecimal originalAmount = new BigDecimal(originalAmountStr) ;
                itemDTO.setOriginalAmount(originalAmount);
            }
            String rmbAmountStr = orderMap.get("订单结算金额\n（RMB）");
            if(StringUtils.isNotBlank(rmbAmountStr)) {
                if (!isNumeric(rmbAmountStr)) {
                    setExceptionMsg(itemDTO, itemDTOS, "是", "订单结算金额（RMB）必须为数值");
                    isError = true;
                    continue;
                }
                BigDecimal rmbAmount = new BigDecimal(rmbAmountStr) ;
                itemDTO.setRmbAmount(rmbAmount);
            }
            String rateStr = orderMap.get("结算汇率");
            if(StringUtils.isNotBlank(rateStr)) {
                if (!isNumeric(rateStr)) {
                    setExceptionMsg(itemDTO, itemDTOS, "是", "结算汇率必须为数值");
                    isError = true;
                    continue;
                }
                BigDecimal rate = new BigDecimal(rateStr) ;
                itemDTO.setSettlementRate(rate);
            }

            //1.必填项校验
            String externalCode = orderMap.get("电商订单号") ;

            String projectName = itemDTO.getPlatformProjectName();
            if(StringUtils.isBlank(projectName)) {
                setExceptionMsg(itemDTO, itemDTOS, "是", "结算费项不能为空");
                isError = true;
                continue;
            }

            BigDecimal originalAmount = itemDTO.getOriginalAmount();
            if(originalAmount == null) {
                setExceptionMsg(itemDTO, itemDTOS, "是", "订单结算金额（结算原币）不能为空");
                isError = true;
                continue;
            }

            BigDecimal rmbAmount = itemDTO.getRmbAmount();
            if(rmbAmount == null) {
                setExceptionMsg(itemDTO, itemDTOS, "是", "订单结算金额（RMB）不能为空");
                isError = true;
                continue;
            }

            if (StringUtils.isNotBlank(externalCode)) {
                if (failureOrderList.contains(externalCode)) {
                    setExceptionMsg(itemDTO, itemDTOS, "是", "系统根据该外部交易单号找到了多条电商订单");
                    isError = true;
                    failureOrderList.add(externalCode);
                    continue;
                }
                if (!(noExistOrderList.contains(externalCode) || successOrderList.contains(externalCode))) {
                    //2.校验导入的外部订单号是否存在系统已发货订单，先以导入的电商订单号查找系统的外部订单号、找不到再找系统的平台订单号；若均校验不存在则提示“导入订单号不存在”；
                    OrderModel orderModelParam = new OrderModel();
                    orderModelParam.setExternalCode(itemDTO.getExternalCode());
                    orderModelParam.setMerchantId(exist.getMerchantId());
                    List<OrderModel> orderList = orderDao.list(orderModelParam);
                    if (orderList.size() > 1) {
                        setExceptionMsg(itemDTO, itemDTOS, "是", "系统根据该外部交易单号找到了多条电商订单");
                        isError = true;
                        failureOrderList.add(externalCode);
                        continue;
                    }

                    if (orderList.isEmpty()) {
                        OrderModel orderModel = new OrderModel();
                        orderModel.setExOrderId(itemDTO.getExternalCode());
                        orderModel.setMerchantId(exist.getMerchantId());
                        orderList = orderDao.list(orderModel);
                        if (orderList.size() > 1) {
                            setExceptionMsg(itemDTO, itemDTOS, "是", "系统根据该外部交易单号找到了多条电商订单");
                            isError = true;
                            failureOrderList.add(externalCode);
                            continue;
                        }

                        if (orderList.isEmpty()) {
                            noExistOrderList.add(externalCode);
                        }
                    }

                    if (!orderList.isEmpty()) {
                        orderCodeMap.put(externalCode, orderList.get(0));
                        successOrderList.add(externalCode);
                    }
                }

            }

            //3.校验结算费项是否存在
            PlatformSettlementConfigModel platformSettlementConfigModel = new PlatformSettlementConfigModel();
            platformSettlementConfigModel.setStorePlatformCode(exist.getStorePlatformCode());
            platformSettlementConfigModel.setName(projectName);
            List<PlatformSettlementConfigModel> platformSettlementConfigModels = platformSettlementConfigDao.list(platformSettlementConfigModel);
            if (platformSettlementConfigModels.isEmpty()) {
                setExceptionMsg(itemDTO, itemDTOS, "是", "导入的费项名称不存在映射关系");
                isError = true;
                continue;
            }
            if (platformSettlementConfigModels.size() > 1) {
                setExceptionMsg(itemDTO, itemDTOS, "是", "导入的费项名称存在多条映射关系");
                isError = true;
                continue;
            }

            //判断是应收明细还是费项明细
            if ("经销业务TO C收入".equals(platformSettlementConfigModels.get(0).getSettlementConfigName()) ||
                    "经销业务TO B收入".equals(platformSettlementConfigModels.get(0).getSettlementConfigName())) {
                itemDTO.setProjectId(platformSettlementConfigModels.get(0).getSettlementConfigId());
                itemDTO.setProjectName(platformSettlementConfigModels.get(0).getSettlementConfigName());
                itemDTO.setPlatformProjectId(platformSettlementConfigModels.get(0).getId());
                itemDTO.setPlatformProjectName(platformSettlementConfigModels.get(0).getName());
                itemDTO.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_0);
                if (itemDTO.getOriginalAmount().compareTo(BigDecimal.ZERO) == -1) {
                    itemDTO.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_1);
                    itemDTO.setRmbAmount(itemDTO.getRmbAmount().abs());
                    itemDTO.setOriginalAmount(itemDTO.getOriginalAmount().abs());
                }
                if (StringUtils.isBlank(externalCode) || !orderCodeMap.containsKey(externalCode)) {
                    TocSettlementReceiveBillItemModel billItemModel = new TocSettlementReceiveBillItemModel();
                    BeanUtils.copyProperties(itemDTO, billItemModel);
                    billItemModel.setBillId(exist.getId());
                    billItemModel.setParentBrandName(superiorParentBrandName);
                    billItemModel.setParentBrandCode(superiorParentBrandNcCode);
                    billItemModel.setParentBrandId(superiorParentBrandId);
                    itemModels.add(billItemModel);
                } else {
                    rightItemDTOS.add(itemDTO);
                }
            } else {
                TocSettlementReceiveBillCostItemDTO costItemDTO = new TocSettlementReceiveBillCostItemDTO();
                costItemDTO.setExternalCode(itemDTO.getExternalCode());
                costItemDTO.setProjectId(platformSettlementConfigModels.get(0).getSettlementConfigId());
                costItemDTO.setProjectName(platformSettlementConfigModels.get(0).getSettlementConfigName());
                costItemDTO.setPlatformProjectId(platformSettlementConfigModels.get(0).getId());
                costItemDTO.setPlatformProjectName(platformSettlementConfigModels.get(0).getName());
                costItemDTO.setSettlementRate(itemDTO.getSettlementRate());
                costItemDTO.setRmbAmount(itemDTO.getRmbAmount().abs());
                costItemDTO.setOriginalAmount(itemDTO.getOriginalAmount().abs());
                costItemDTO.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_0);
                costItemDTO.setDataSource(DERP_ORDER.TOCRECEIVEBILLCOST_DATASOURCE_0);
                costItemDTO.setBillId(exist.getId());
                if(StringUtils.isNotBlank(itemDTO.getRemark())){
                    costItemDTO.setRemark(itemDTO.getRemark());
                }
                if (itemDTO.getOriginalAmount().compareTo(BigDecimal.ZERO) == -1) {
                    costItemDTO.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_1);
                }
                if (StringUtils.isBlank(externalCode) || !orderCodeMap.containsKey(externalCode)) {
                    TocSettlementReceiveBillCostItemModel billItemModel = new TocSettlementReceiveBillCostItemModel();
                    BeanUtils.copyProperties(costItemDTO, billItemModel);
                    billItemModel.setBillId(exist.getId());
                    billItemModel.setParentBrandName(superiorParentBrandName);
                    billItemModel.setParentBrandCode(superiorParentBrandNcCode);
                    billItemModel.setParentBrandId(superiorParentBrandId);
                    costItemModels.add(billItemModel);
                } else {
                    List<TocSettlementReceiveBillCostItemDTO> rightCostItemDTOS = new ArrayList<TocSettlementReceiveBillCostItemDTO>();

                    OrderModel orderModel = orderCodeMap.get(itemDTO.getExternalCode());
                    costItemDTO.setOrderCode(orderModel.getCode());
                    if (rightCostItemDTOSMap.containsKey(orderModel.getId())) {
                        rightCostItemDTOS.addAll(rightCostItemDTOSMap.get(orderModel.getId()));
                    }
                    rightCostItemDTOS.add(costItemDTO);

                    rightCostItemDTOSMap.put(orderModel.getId(), rightCostItemDTOS);
                }
            }
        }

        String basePath = ApolloUtilDB.orderBasepath;//excel文件存放目录
        SXSSFWorkbook wb = new SXSSFWorkbook();
        //如果存在错误信息则生成错误信息excel
        if (isError) {
            List<ExportExcelSheet> errorSheetList = new ArrayList<>();
            if (!itemDTOS.isEmpty()) {
                ExportExcelSheet sheet = new ExportExcelSheet();
                String sheetName = "订单总结算生成失败信息";
                String[] mainKey = { "externalCode", "platformProjectName", "originalAmount", "rmbAmount", "settlementRate", "isException", "excetionResult"};
                String[] mainColumns = { "外部订单号", "结算费项", "订单结算金额\n（结算原币）","订单结算金额\n（RMB）","结算汇率","是否异常", "异常原因" };
                sheet.setSheetNames(sheetName);
                sheet.setKeys(mainKey);
                sheet.setColums(mainColumns);
                sheet.setResultList(itemDTOS);
                errorSheetList.add(sheet);
            }

            wb = ExcelUtilXlsx.createMutiSheetExcel(errorSheetList);
            // 写出文件
            basePath = basePath + "/TOCBILL/" + exist.getMerchantId();
            // 创建目录
            new File(basePath).mkdirs();
            String fileName = basePath + "/" + exist.getCode() + "平台结算单生成失败信息.xlsx";
            FileOutputStream fileOut = new FileOutputStream(fileName);
            wb.write(fileOut);
            fileOut.close();
            //更新账单状态
            TocSettlementReceiveBillModel update = new TocSettlementReceiveBillModel();
            update.setErrorMsgPath(fileName);
            update.setId(exist.getId());
            update.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_08);
            tocSettlementReceiveBillDao.modify(update);
        } else {
            //获取所有母品牌
            Map<String, BrandParentMongo> brandParentMap = new HashMap<>();
            List<Long> goodsIdList = new ArrayList<>();
            Map<Long, MerchandiseInfoMogo> merchandiseInfoMogoMap = new HashMap<>();

            /**导入模板为订单维度的结算金额，生成系统的平台结算单应收明细需到”订单+商品货号“维度，故根据本次导入成功的订单号查询系统拿到对应订单的商品货号，
             * 以导入的订单总结算金额按权重比例分摊到订单中每个商品上；
             * 分摊公式为：前N-1个商品分摊的结算金额= （系统订单商品结算总额/订单所有商品结算总额合计）*导入的订单结算金额，金额四舍五入，保留2位小数；
             * 最后一个商品采用倒减=导入的订单结算金额-前面已分摊的商品结算金额（已做四舍五入的金额）汇总；*/

            for (TocSettlementReceiveBillItemDTO itemDTO : rightItemDTOS) {
                OrderModel orderModel = orderCodeMap.get(itemDTO.getExternalCode());
                OrderItemModel orderItemModel = new OrderItemModel();
                orderItemModel.setOrderId(orderModel.getId());
                List<OrderItemModel> orderItemModels  = orderItemDao.list(orderItemModel);

                Map<String, BigDecimal> tempPriceMap = calculateTempPrice(orderItemModels, itemDTO.getOriginalAmount());
                Map<String, BigDecimal> tempRMBPriceMap = null;
                if (itemDTO.getRmbAmount() == null) {
                    tempRMBPriceMap = calculateTempPrice(orderItemModels, orderModel.getPayment());
                } else {
                    tempRMBPriceMap = calculateTempPrice(orderItemModels, itemDTO.getRmbAmount());
                }

                for (OrderItemModel itemModel : orderItemModels) {
                    TocSettlementReceiveBillItemModel billItemModel = new TocSettlementReceiveBillItemModel();
                    BeanUtils.copyProperties(itemDTO, billItemModel);
                    billItemModel.setOrderCode(orderModel.getCode());
                    billItemModel.setBillId(exist.getId());
                    billItemModel.setGoodsId(itemModel.getGoodsId());
                    billItemModel.setGoodsNo(itemModel.getGoodsNo());
                    billItemModel.setGoodsName(itemModel.getGoodsName());
                    billItemModel.setNum(itemModel.getNum());

                    String key = itemModel.getOrderId() + "_" + itemModel.getId();
                    billItemModel.setOriginalAmount(tempPriceMap.get(key));
                    billItemModel.setRmbAmount(tempRMBPriceMap.get(key));
                    itemModels.add(billItemModel);

                    if (!goodsIdList.contains(itemModel.getGoodsId())) {
                        goodsIdList.add(itemModel.getGoodsId());
                    }
                }
            }

            int pageSize = 1000;

            /**以“订单+费项”的维度记录结算金额即可；若订单存在于系统，则母品牌取系统订单中结算金额最高的商品对应的母品牌即可，
             * 若订单不存在系统则根据当前应收单选择的店铺id 查询店铺信息表获取已维护的“专营母品牌”，存值为该订单的母品牌信息*/
            List<Long> orderIds = new ArrayList<>(rightCostItemDTOSMap.keySet());

            for (int i = 0; i < orderIds.size(); ) {
                int pageSub = (i+pageSize) < orderIds.size() ? (i+pageSize) : orderIds.size();
                //根据外部订单号查询对应的结算金额最高的商品明细
                List<OrderItemModel> orderItemModels = orderItemDao.getMaxPriceByOrderId(orderIds.subList(i, pageSub), false);

                for (OrderItemModel itemModel : orderItemModels) {
                    List<TocSettlementReceiveBillCostItemDTO> costItemDTOS = rightCostItemDTOSMap.get(itemModel.getOrderId());

                    for (TocSettlementReceiveBillCostItemDTO costItemDTO : costItemDTOS) {
                        costItemDTO.setGoodsId(itemModel.getGoodsId());
                        costItemDTO.setNum(itemModel.getNum());
                    }

                    if (!goodsIdList.contains(itemModel.getGoodsId())) {
                        goodsIdList.add(itemModel.getGoodsId());
                    }
                }

                i = pageSub;
            }

            if (!goodsIdList.isEmpty()) {
                List<MerchandiseInfoMogo> merchandiseInfoMogos = merchandiseInfoMogoDao.findAllByIn("merchandiseId", goodsIdList);
                for (MerchandiseInfoMogo merchandiseInfo : merchandiseInfoMogos) {
                    if (StringUtils.isNotBlank(merchandiseInfo.getCommbarcode())) {
                        merchandiseInfoMogoMap.put(merchandiseInfo.getMerchandiseId(), merchandiseInfo);
                    }
                }
            }

            //批量保存保存应收明细
            for (int i = 0; i < itemModels.size(); ) {
                int pageSub = (i+pageSize) < itemModels.size() ? (i+pageSize) : itemModels.size();

                for (TocSettlementReceiveBillItemModel itemModel : itemModels.subList(i, pageSub)) {
                    MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoMap.get(itemModel.getGoodsId());
                    if (merchandiseInfo != null) {
                        BrandParentMongo brandParent = brandParentMap.get(merchandiseInfo.getCommbarcode());
                        if (brandParent == null) {
                            brandParent = brandParentMongoDao.getBrandParentMongoByCommbarcode(merchandiseInfo.getCommbarcode());
                        }

                        if (brandParent != null) {
                            itemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                            itemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                            itemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                            brandParentMap.put(merchandiseInfo.getCommbarcode(), brandParent);
                        }
                    }

                }

                tocSettlementReceiveBillItemDao.batchSave(itemModels.subList(i, pageSub));
                i = pageSub;
            }

            //批量保存保存费项明细
            for (Long key : rightCostItemDTOSMap.keySet()) {
                List<TocSettlementReceiveBillCostItemDTO> costItemDTOS = rightCostItemDTOSMap.get(key);

                BrandParentMongo brandParent = null;
                MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoMap.get(costItemDTOS.get(0).getGoodsId());
                if (merchandiseInfo != null) {
                    brandParent = brandParentMap.get(merchandiseInfo.getCommbarcode());
                    if (brandParent == null) {
                        brandParent = brandParentMongoDao.getBrandParentMongoByCommbarcode(merchandiseInfo.getCommbarcode());
                    }

                }

                for (TocSettlementReceiveBillCostItemDTO costItemDTO : costItemDTOS) {
                    TocSettlementReceiveBillCostItemModel costItemModel = new TocSettlementReceiveBillCostItemModel();
                    BeanUtils.copyProperties(costItemDTO, costItemModel);

                    if (brandParent != null) {
                        costItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                        costItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                        costItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                        brandParentMap.put(merchandiseInfo.getCommbarcode(), brandParent);
                    }

                    costItemModels.add(costItemModel);
                }
            }

            for (int i = 0; i < costItemModels.size(); ) {
                int pageSub = (i + pageSize) < costItemModels.size() ? (i + pageSize) : costItemModels.size();

                tocSettlementReceiveBillCostItemDao.batchSave(costItemModels.subList(i, pageSub));
                i = pageSub;
            }

            //更新账单状态
            BigDecimal oriAmount = new BigDecimal("0");
            BigDecimal receiveAmount = new BigDecimal("0");
            BigDecimal oriReceivablePrice = tocSettlementReceiveBillItemDao.getTotalSettlementReceivePrice(exist.getId());
            BigDecimal receivablePrice = tocSettlementReceiveBillItemDao.getTotalReceivePrice(exist.getId());
            BigDecimal costFree = tocSettlementReceiveBillCostItemDao.getTotalReceivePrice(exist.getId());
            BigDecimal oriCostFree = tocSettlementReceiveBillCostItemDao.getTotalSettlementPrice(exist.getId());
            receivablePrice = receivablePrice == null ? new BigDecimal("0") : receivablePrice;
            oriReceivablePrice = oriReceivablePrice == null ? new BigDecimal("0") : oriReceivablePrice;
            costFree = costFree == null ? new BigDecimal("0") : costFree;
            oriCostFree = oriCostFree == null ? new BigDecimal("0") : oriCostFree;
            receiveAmount = receivablePrice.add(costFree);
            oriAmount = oriReceivablePrice.add(oriCostFree);
            TocSettlementReceiveBillModel update = new TocSettlementReceiveBillModel();
            update.setId(exist.getId());
            update.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_00);
            update.setReceivableAmount(receiveAmount);
            update.setErrorMsgPath("");
            update.setOriReceivableAmount(oriAmount);
            tocSettlementReceiveBillDao.updateWithNull(update);

            //删除导入文件
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }

            //删除错误文件
            if (StringUtils.isNotBlank(exist.getErrorMsgPath())) {
                File errorFile = new File(exist.getErrorMsgPath());
                if(errorFile.exists()) {
                    errorFile.delete();
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_20100000007,model=DERP_LOG_POINT.POINT_20100000007_Label, keyword = "settleNo")
    public void saveToCReceiveBillBySmurfs(String json, String keys, String topics, String tags) throws Exception {
        JSONObject jsonData = JSONObject.fromObject(json);
        jsonData.put("method", "/001/1.0");
        String platformCode = (String) jsonData.get("platformCode");
        String sellerCode = (String) jsonData.get("sellerCode");
        String settleNo = (String) jsonData.get("settleNo");
        //创建时间（起始） 格式：yyyy-MM-dd HH:mm:ss
        String startDate = (String) jsonData.get("startDate");
        //创建时间（截止） 格式：yyyy-MM-dd HH:mm:ss
        String endDate = (String) jsonData.get("endDate");
        String pageNo = (String) jsonData.get("pageNo");
        String pageSize = (String) jsonData.get("pageSize");

        //记录失败,用于发送邮件通知
        List<Map<String, String>> failList = new ArrayList<>();

        if(StringUtils.isBlank(platformCode)) {
            throw new RuntimeException("平台编码不能为空");
        }

        if(StringUtils.isBlank(sellerCode)) {
            throw new RuntimeException("卓志编码不能为空");
        }

        if(StringUtils.isBlank(startDate)) {
            startDate = TimeUtils.getNowBeforeHourDate(2);
            jsonData.put("startDate", startDate);
        }

        if(StringUtils.isBlank(endDate)) {
            endDate = TimeUtils.formatFullTime(new Date());
            jsonData.put("endDate", endDate);
        }

        if(StringUtils.isBlank(pageNo)) {
            pageNo = "1";
            jsonData.put("pageNo", pageNo);
        }

        if(StringUtils.isBlank(pageSize)) {
            jsonData.put("pageSize", "100");
        }

        while (true) {
            String resultJson = SmurfsUtils.send(jsonData, SmurfsAPIEnum.SMURFS_GET_TOC_SETTLEMENT);
            JSONObject resultObject = JSONObject.fromObject(resultJson);
            TocSettleBillResponseDTO resultDTO = (TocSettleBillResponseDTO) JSONObject.toBean(resultObject, TocSettleBillResponseDTO.class);
            Integer state = resultDTO.getState();
            if(state == null || state == 0) {
                LOGGER.error("访问蓝精灵获取Toc结算单服务接口异常, state：" + state);
                throw new RuntimeException("访问蓝精灵获取Toc结算单服务接口异常, state：" + state);
            }
            Integer code = resultDTO.getCode();
            String msg = resultDTO.getMsg();
            if(code == null || code != 200) {
                LOGGER.error("访问蓝精灵获取Toc结算单服务接口异常, code:" + code + ", msg：" + msg);
                throw new RuntimeException("访问蓝精灵获取Toc结算单服务接口异常, code:" + code + ", msg：" + msg);
            }

            TocSettleOrderListDTO orderList = resultDTO.getOrderList();
            if(orderList == null || orderList.getList() == null) {
                break;
            }

            Object object = orderList.getList();
            JSONArray jsonObject = JSONArray.fromObject(object);
            List<TocSettleBillDTO> list = (List<TocSettleBillDTO>) JSONArray.toCollection(jsonObject, TocSettleBillDTO.class);
            if(list == null || list.isEmpty()) {
                break;
            }
            Object savePoint = null;
            for (TocSettleBillDTO dto : list){
                //用于存放最后发送邮件预警的表头内容
                Map<String,Object> infoMap = new HashMap<>();
                infoMap.put("id", dto.getId());
                infoMap.put("platformCode", dto.getPlatformCode());
                infoMap.put("sellerCode", dto.getSellerCode());
                infoMap.put("currency", dto.getCurrency());
                infoMap.put("settleNo", dto.getSettleNo());
                infoMap.put("storeCode", dto.getStoreCode());
                infoMap.put("settleTime", dto.getSettleTime());

                //设置回滚点
                savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();

                TocSettlementReceiveBillModel model = new TocSettlementReceiveBillModel();
                model.setExternalCode(dto.getSettleNo());
                model.setShopCode(dto.getStoreCode());
                TocSettlementReceiveBillModel searchByModel = tocSettlementReceiveBillDao.searchByModel(model);

                if(searchByModel != null && !StringUtils.equals(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_06, searchByModel.getBillStatus())) {
                    LOGGER.info("外部结算单号settleNo:{},shopCode:{}已存在于经分销", dto.getSettleNo(), dto.getStoreCode());
                    continue;
                }

                //校验
                Map<String, Object> shopParams = new HashMap<>();
                if(StringUtils.isBlank(dto.getStoreCode())) {
                    LOGGER.error("外部结算单号settleNo:{},店铺编码为空", dto.getSettleNo());
                    Map<String, String> errMap = new HashMap<>();
                    errMap.put("jsonInfo", JSON.toJSONString(infoMap));
                    errMap.put("msg", "店铺编码为空");
                    failList.add(errMap);
                    continue;
                }
                shopParams.put("shopCode", dto.getStoreCode());
                MerchantShopRelMongo shop = merchantShopRelMongoDao.findOne(shopParams);
                if (shop == null) {
                    LOGGER.error("外部结算单号settleNo:{},店铺为空,shopCode:{}", dto.getSettleNo(), dto.getStoreCode());
                    Map<String, String> errMap = new HashMap<>();
                    errMap.put("jsonInfo", JSON.toJSONString(infoMap));
                    errMap.put("msg", "主体与店铺关联关系未维护");
                    failList.add(errMap);
                    continue;
                }

                //保存Toc结算单表头
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("userId", 100476L);
                UserInfoMongo creater = userInfoMongoDao.findOne(userMap);
                TocSettlementReceiveBillModel billModel = new TocSettlementReceiveBillModel();
                billModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_PTJS));
                billModel.setSettlementDate(TimeUtils.strToSqlDate(dto.getSettleTime()));
                billModel.setShopTypeCode(shop.getShopTypeCode());
                billModel.setStorePlatformCode(dto.getPlatformCode());
                billModel.setExternalCode(dto.getSettleNo());
                billModel.setOriCurrency(dto.getCurrency());
                billModel.setShopName(shop.getShopName());
                billModel.setShopCode(shop.getShopCode());
                billModel.setBuId(shop.getBuId());
                billModel.setBuName(shop.getBuName());
                billModel.setCustomerId(shop.getCustomerId());
                billModel.setCustomerName(shop.getCustomerName());
                billModel.setSettlementCurrency("CNY");
                billModel.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_00);
                billModel.setCreaterId(creater.getUserId());
                billModel.setCreater(creater.getName());
                billModel.setMerchantId(shop.getMerchantId());
                billModel.setMerchantName(shop.getMerchantName());
                billModel.setNcStatus(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_10);
                billModel.setReceivableAmount(BigDecimal.ZERO);
                billModel.setInvoiceStatus(DERP_ORDER.TOCRECEIVEBILL_INVOICESTATUS_00);
                Long billId = tocSettlementReceiveBillDao.save(billModel);

                //保存表体
                billModel.setId(billId);
                Object itemObject = dto.getData();
                JSONArray itemJsonObject = JSONArray.fromObject(itemObject);
                List<TocSettleBillDataDTO> itemList = (List<TocSettleBillDataDTO>) JSONArray.toCollection(itemJsonObject, TocSettleBillDataDTO.class);

                if(itemList == null || itemList.size() <= 0) {
                    continue;
                }

                List<Map<String, Object>> itemMapList = itemList.stream().map(entity -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("externalCode", entity.getOrderNo());
                    map.put("platformProjectName", entity.getExpensesItem());
                    map.put("rmbAmount", new BigDecimal(entity.getSettleAmountRMB()));
                    map.put("originalAmount", new BigDecimal(entity.getSettleAmount()));
                    map.put("settlementRate", new BigDecimal(entity.getExchangeRate()));
                    return map;
                }).collect(Collectors.toList());

                boolean isSuccess = subSaveBillItem(billModel, itemMapList, shop, failList, infoMap);

                if(!isSuccess) {
                    //回滚业务库的事务
                    LOGGER.error("生成Toc结算单不成功,外部结算单号settleNo:{}", dto.getSettleNo());
                    TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
                    continue;
                }
            }

            // 设置页码,进入下一次循环
            int nextPage = orderList.getPageNo();
            nextPage++;
            jsonData.put("pageNo", Integer.toString(nextPage));
        }

        //生成文件并发送预警邮件
        generateAndSendAlertEmail(failList);
    }

    /**
     * 生成失败文件并发送邮件预警
     * @param failList
     */
    private void generateAndSendAlertEmail(List<Map<String, String>> failList) throws Exception{
        if(failList == null || failList.isEmpty()) {
            return;
        }
        String basePath = ApolloUtilDB.orderBasepath;//excel文件存放目录
        SXSSFWorkbook wb = new SXSSFWorkbook();
        List<ExportExcelSheet> errorSheetList = new ArrayList<>();

        ExportExcelSheet sheet = new ExportExcelSheet();
        String sheetName = "订单总结算生成失败信息";
        String[] mainKey = { "jsonInfo", "jsonDetail", "msg"};
        String[] mainColumns = { "表头", "明细", "异常原因" };
        sheet.setSheetNames(sheetName);
        sheet.setKeys(mainKey);
        sheet.setColums(mainColumns);
        sheet.setResultList(failList);
        errorSheetList.add(sheet);
        wb = ExcelUtilXlsx.createMutiSheetExcel(errorSheetList);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            wb.write(bos);
        } finally {
            bos.close();
        }

        byte[] bytes = bos.toByteArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileTypeCode",SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
        jsonObject.put("fileName", "Toc结算单生成失败信息");
        jsonObject.put("fileBytes", Base64.encodeBase64String(bytes));
        jsonObject.put("fileExt","xlsx");
        jsonObject.put("fileSize",String.valueOf(bytes.length));
        String resultJson=SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);
        JSONObject jsonObj = JSONObject.fromObject(resultJson);

        LOGGER.info("蓝精灵上传附件返回结果：" + jsonObj);
        String fileUrl = "" ;
        if(jsonObj.getInt("code") == 200) {
            fileUrl = jsonObj.getString("url") ;
        }

        jsonObject.clear();
        JSONObject paramJson=new JSONObject();//存储所有数据的结果

        paramJson.put("fileUrl", fileUrl) ;
        jsonObject.put("recipients", ApolloUtil.smurfsRecipients);
        jsonObject.put("paramJson",paramJson);
        jsonObject.put("mailCode",SmurfsAPICodeEnum.EMAIL_W038.getCode());

        //调用外部接口发送邮件
        String resultMsg=SmurfsUtils.send(jsonObject,SmurfsAPIEnum.SMURFS_EMAIL);
        LOGGER.info("蓝精灵发邮件返回结果：" + resultMsg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_20100000008,model=DERP_LOG_POINT.POINT_20100000008_Label, keyword = "settleNo")
    public void SaveToCReceiveBillByCrawler(String json, String keys, String topics, String tags) throws Exception{
        JSONObject jsonData = JSONObject.fromObject(json);

        String shopCodeStr = (String) jsonData.get("shopCode");
        String gelCodeStr = (String) jsonData.get("gelCode");
        String settleNoStr = (String) jsonData.get("settleNo");
        String startDate = (String) jsonData.get("startDate");
        String endDate = (String) jsonData.get("endDate");
        String pageNo = (String) jsonData.get("pageNo");
        String pageSize = (String) jsonData.get("pageSize");

        //记录失败,用于发送邮件通知
        List<Map<String, String>> failList = new ArrayList<>();

        /**查询SQL*/
        StringBuilder queryInfoDataStr = new StringBuilder();
        queryInfoDataStr.append(" select * from platform_settle_info where hasReceived = '0'" );
        if(StringUtils.isNotBlank(shopCodeStr)) {
            queryInfoDataStr.append(" and shopCode = '").append(shopCodeStr).append("'");
        }

        if(StringUtils.isNotBlank(settleNoStr)) {
            queryInfoDataStr.append(" and settleNo = '").append(settleNoStr).append("'");
        }

        if(StringUtils.isNotBlank(gelCodeStr)) {
            queryInfoDataStr.append(" and gelCode = '").append(gelCodeStr).append("'");
        }

        if(StringUtils.isNotBlank(startDate)) {
            queryInfoDataStr.append(" and settleDate >= '").append(startDate).append("'");
        }

        if(StringUtils.isNotBlank(endDate)) {
            queryInfoDataStr.append(" and settleDate <= '").append(endDate).append("'");
        }

        if(StringUtils.isNotBlank(pageNo) && StringUtils.isNotBlank(pageSize)) {
            queryInfoDataStr.append(" limit ").append(pageNo).append(",").append(pageSize);
        }

        //查询爬虫结算账单链接
        Savepoint sp = null;
        Connection conn = null;
        PreparedStatement queryInfoDataPst = null;
        PreparedStatement queryFileDataPst = null;
        PreparedStatement queryDetailDataPst = null;
        PreparedStatement updateInfoDataPst = null;
        ResultSet queryInfoDataRs = null;
        ResultSet queryDetailDataRs = null;
        ResultSet queryFileDataRs = null;
        Object savePoint = null;

        try {
            conn = createSQLConn() ;
            conn.setAutoCommit(false);
            queryInfoDataPst = conn.prepareStatement(queryInfoDataStr.toString());
            queryInfoDataRs = queryInfoDataPst.executeQuery(queryInfoDataStr.toString());
            LOGGER.info("查询爬虫结算单:" + queryInfoDataStr );

            while(queryInfoDataRs.next()) {
                sp = conn.setSavepoint();
                //设置回滚点
                savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();

                // 生成结算单号
                String settlementCode = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_PTJS);

                //结算单号
                Long settleId = queryInfoDataRs.getLong("id");
                String settleNo = queryInfoDataRs.getString("settleNo");
                String shopCode = queryInfoDataRs.getString("shopCode");
                String shopName = queryInfoDataRs.getString("shopName");
                String gelCode = queryInfoDataRs.getString("gelCode");
                Timestamp settleDate = queryInfoDataRs.getTimestamp("settleDate");

                //用于存放最后放到邮件预警中表头信息
                Map<String, Object> infoMap = new HashMap<>();
                infoMap.put("settleId", settleId);
                infoMap.put("settleNo", settleNo);
                infoMap.put("shopCode", shopCode);
                infoMap.put("shopName", shopName);
                infoMap.put("gelCode", gelCode);
                infoMap.put("settleDate", settleDate);

                PlatformSettleInfoModel infoModel = new PlatformSettleInfoModel();
                infoModel.setId(settleId);
                infoModel.setSettleNo(settleNo);
                infoModel.setShopCode(shopCode);
                infoModel.setSettleDate(settleDate);
                infoModel.setShopName(shopName);
                infoModel.setGelCode(gelCode);

                //校验卓志编码
                Map<String, Object> param = new HashMap<>();
                param.put("merchantId", Long.valueOf(gelCode));
                MerchantInfoMongo one = merchantInfoMongoDao.findOne(param);
                if(one == null) {
                    LOGGER.error("公司主体不存在,gelCode:{}", gelCode);
                    Map<String, String> errMap = new HashMap<>();
                    errMap.put("jsonInfo", JSON.toJSONString(infoMap));
                    errMap.put("msg", "公司主体不存在");
                    failList.add(errMap);
                    continue;
                }

                TocSettlementReceiveBillModel model = new TocSettlementReceiveBillModel();
                model.setExternalCode(settleNo);
                model.setShopCode(shopCode);
                TocSettlementReceiveBillModel searchByModel = tocSettlementReceiveBillDao.searchByModel(model);

                if(searchByModel != null && !StringUtils.equals(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_06, searchByModel.getBillStatus())) {
                    LOGGER.info("结算单settleNo:{},shopCode:{}已存在于经分销", settleNo, shopCode);
                    continue;
                }

                //校验
                Map<String, Object> shopParams = new HashMap<>();
                shopParams.put("shopCode", shopCode);
                MerchantShopRelMongo shop = merchantShopRelMongoDao.findOne(shopParams);
                if (shop == null) {
                    LOGGER.error("店铺为空,shopCode:{}", shopCode);
                    Map<String, String> errMap = new HashMap<>();
                    errMap.put("jsonInfo", JSON.toJSONString(infoMap));
                    errMap.put("msg", "店铺编码为空");
                    failList.add(errMap);
                    continue;
                }

                //获取爬虫结算单明细
                String queryDetailStr = "select settleId, onlineOrderNo, settlementFee, settlementAmount, rmbAmount, exchangeRate, id "
                        + " from platform_settle_detail where settleId ='" + settleId + "'" ;
                queryDetailDataPst = conn.prepareStatement(queryDetailStr);
                queryDetailDataRs = queryDetailDataPst.executeQuery(queryDetailStr);

                String queryFileStr = "select fileUrl, fileKey from sis_file_data where settleNo = '" + settleNo + "'";
                LOGGER.debug("爬虫查询结算单对应的文件记录:{}", queryFileStr);
                queryFileDataPst = conn.prepareStatement(queryFileStr);
                queryFileDataRs = queryFileDataPst.executeQuery(queryFileStr);
                String url = null;
                String fileKey = null;
                String fileName = null;
                List<AttachmentInfoMongo> attachmentList = new ArrayList<>();
                AttachmentInfoMongo attachmentInfoMongo = null;
                while (queryFileDataRs.next()) {
                    url = queryFileDataRs.getString("fileUrl");
                    fileKey = queryFileDataRs.getString("fileKey");
                    if(StringUtils.isBlank(url) || StringUtils.isBlank(fileKey)) {
                        continue;
                    }
                    fileName = fileKey.substring(fileKey.lastIndexOf("/") + 1);
                    attachmentInfoMongo = new AttachmentInfoMongo() ;
                    attachmentInfoMongo.setAttachmentName(fileName); 		//附件名
                    attachmentInfoMongo.setAttachmentSize(null); 		//附件大小
                    attachmentInfoMongo.setAttachmentType(fileName.substring(fileName.lastIndexOf(".")+1));		       	//附件类型
                    attachmentInfoMongo.setCreator(null);			//上传者
                    attachmentInfoMongo.setCreatorName("爬虫库");
                    attachmentInfoMongo.setCreateDate(new Date());
                    attachmentInfoMongo.setRelationCode(settlementCode);              //关联订单编码
                    attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
                    attachmentInfoMongo.setAttachmentUrl(url + fileKey);              //设置Url
                    attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
                    attachmentInfoMongo.setModule(SourceStatusEnum.TOCJSD.getKey());
                    attachmentInfoMongoDao.insert(attachmentInfoMongo);
                }

                List<PlatformSettleDetailModel> detailModels = new ArrayList<>();
                while (queryDetailDataRs.next()) {
                    Long detailId = queryDetailDataRs.getLong("id");
                    Long detailSettleId = queryDetailDataRs.getLong("settleId");
                    String detailOnlineOrderNo = queryDetailDataRs.getString("onlineOrderNo");
                    String detailSettlementFee = queryDetailDataRs.getString("settlementFee");
                    BigDecimal detailSettlementAmount = queryDetailDataRs.getBigDecimal("settlementAmount");
                    BigDecimal detailRmbAmount = queryDetailDataRs.getBigDecimal("rmbAmount");
                    BigDecimal detailExchangeRate = queryDetailDataRs.getBigDecimal("exchangeRate");

                    PlatformSettleDetailModel detailModel = new PlatformSettleDetailModel();
                    detailModel.setSettleId(detailSettleId);
                    detailModel.setOnlineOrderNo(detailOnlineOrderNo);
                    detailModel.setSettlementFee(detailSettlementFee);
                    detailModel.setSettlementAmount(detailSettlementAmount);
                    detailModel.setRmbAmount(detailRmbAmount);
                    detailModel.setExchangeRate(detailExchangeRate);
                    detailModels.add(detailModel);
                }

                //保存Toc结算单表头
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("userId", 100476L);
                UserInfoMongo creater = userInfoMongoDao.findOne(userMap);
                TocSettlementReceiveBillModel billModel = new TocSettlementReceiveBillModel();
                billModel.setCode(settlementCode);
                billModel.setSettlementDate(TimeUtils.strToSqlDate(TimeUtils.format(infoModel.getSettleDate(), "yyyy-MM-dd")));
                billModel.setShopTypeCode(shop.getShopTypeCode());
                billModel.setStorePlatformCode(shop.getStorePlatformCode());
                billModel.setExternalCode(infoModel.getSettleNo());
                billModel.setOriCurrency(shop.getCurrency());
                billModel.setShopName(shop.getShopName());
                billModel.setShopCode(shop.getShopCode());
                billModel.setBuId(shop.getBuId());
                billModel.setBuName(shop.getBuName());
                billModel.setCustomerId(shop.getCustomerId());
                billModel.setCustomerName(shop.getCustomerName());
                billModel.setSettlementCurrency("CNY");
                billModel.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_00);
                billModel.setCreaterId(creater.getUserId());
                billModel.setCreater(creater.getName());
                billModel.setMerchantId(shop.getMerchantId());
                billModel.setMerchantName(shop.getMerchantName());
                billModel.setNcStatus(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_10);
                billModel.setReceivableAmount(BigDecimal.ZERO);
                billModel.setInvoiceStatus(DERP_ORDER.TOCRECEIVEBILL_INVOICESTATUS_00);
                Long billId = tocSettlementReceiveBillDao.save(billModel);

                //保存表体
                billModel.setId(billId);

                List<Map<String, Object>> itemMapList = detailModels.stream().map(entity -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("externalCode", entity.getOnlineOrderNo());
                    map.put("platformProjectName", entity.getSettlementFee());
                    map.put("rmbAmount", entity.getRmbAmount());
                    map.put("originalAmount", entity.getSettlementAmount());
                    map.put("settlementRate", entity.getExchangeRate());
                    return map;
                }).collect(Collectors.toList());

                boolean isSuccess = subSaveBillItem(billModel, itemMapList, shop, failList, infoMap);
                if(!isSuccess) {
                    if(conn != null) {
                        conn.rollback(sp);
                    }
                    //回滚业务库的事务
                    TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
                    continue;
                }

                //修改platform_settle_info 的 hasReceived字段
                int updateCount = updatePlatformSettleInfo(infoModel, conn, null, updateInfoDataPst);
                conn.commit();
                itemMapList.clear();
            }

            if(updateInfoDataPst != null) {updateInfoDataPst.close();}
            closeSQLConn(null, null, queryFileDataPst, queryFileDataRs);
            closeSQLConn(null, null, queryDetailDataPst, queryDetailDataRs);
            closeSQLConn(conn, null, queryInfoDataPst, queryInfoDataRs);

            //生成文件并发送预警邮件
            generateAndSendAlertEmail(failList);
            failList = null;
        } catch (Exception e) {
            if(conn != null) {
                conn.rollback();
            }
            closeSQLConn(null, null, queryFileDataPst, queryFileDataRs);
            closeSQLConn(null, null, queryDetailDataPst, queryDetailDataRs);
            closeSQLConn(conn, null, queryInfoDataPst, queryInfoDataRs);

            LOGGER.error("读取爬虫库的平台结算数据生成Toc应收账单失败 :" + e.getMessage(), e);
            throw new RuntimeException("读取爬虫库的平台结算数据生成Toc应收账单失败", e) ;
        }
    }

    private int updatePlatformSettleInfo(PlatformSettleInfoModel infoModel, Connection queryDataConn, Statement updateInfoDataSt, PreparedStatement updateInfoDataPst) throws Exception {
        if(infoModel == null || infoModel.getId() == null) {
            return 0;
        }
        String updateSql = "update platform_settle_info set hasReceived = '1' where id = " + infoModel.getId() + " and hasReceived = '0'";
        LOGGER.debug("更新爬虫库平台结算状态:{}", updateSql);
        updateInfoDataPst = queryDataConn.prepareStatement(updateSql);
        int updateCount = updateInfoDataPst.executeUpdate(updateSql);
        return updateCount;
    }

    /**
     * 生成Toc应收表体
     * @param billModel
     * @param itemMapList
     * @param shop
     * @return
     * @throws SQLException
     */
    private boolean subSaveBillItem(TocSettlementReceiveBillModel billModel, List<Map<String, Object>> itemMapList, MerchantShopRelMongo shop
            , List<Map<String, String>> failList, Map<String, Object> infoMap) throws SQLException {
        if(itemMapList == null || itemMapList.size() <= 0) {
            return false;
        }

        //查询成功订单号
        List<String> successOrderList = new ArrayList<>();
        //查询失败订单号
//        List<String> failureOrderList = new ArrayList<>();
        //查询不存在订单号
        List<String> noExistOrderList = new ArrayList<>();

        Map<String, OrderModel> orderCodeMap = new HashMap<>();

        Long superiorParentBrandId = null;
        String superiorParentBrandName = null;
        String superiorParentBrandNcCode = null;
        if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(billModel.getShopTypeCode())) {
            superiorParentBrandId = shop.getSuperiorParentBrandId();
            superiorParentBrandName = shop.getSuperiorParentBrandName();
            superiorParentBrandNcCode = shop.getSuperiorParentBrandNcCode();
        } else {
            //查询母品牌为“其他”的母品牌
            Map<String, Object> params = new HashMap<>();
            params.put("ncCode", "999");
            BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(params);
            if(brandSuperior == null) {
                LOGGER.error("其他的母品牌未维护");
                Map<String, String> errMap = new HashMap<>();
                errMap.put("jsonInfo", JSON.toJSONString(infoMap));
                errMap.put("msg", "其他的母品牌未维护");
                failList.add(errMap);
                return false;
            }
            superiorParentBrandId = brandSuperior.getBrandSuperiorId();
            superiorParentBrandName = brandSuperior.getName();
            superiorParentBrandNcCode = brandSuperior.getNcCode();
        }

        List<TocSettlementReceiveBillItemDTO> itemDTOS = new ArrayList<TocSettlementReceiveBillItemDTO>();
        List<TocSettlementReceiveBillItemDTO> rightItemDTOS = new ArrayList<TocSettlementReceiveBillItemDTO>();
        Map<Long, List<TocSettlementReceiveBillCostItemDTO>> rightCostItemDTOSMap = new HashMap<>();
        List<TocSettlementReceiveBillCostItemModel> costItemModels = new ArrayList<TocSettlementReceiveBillCostItemModel>();
        List<TocSettlementReceiveBillItemModel> itemModels = new ArrayList<TocSettlementReceiveBillItemModel>();

        for (Map<String, Object> itemMap : itemMapList){
            TocSettlementReceiveBillItemDTO itemDTO = new TocSettlementReceiveBillItemDTO();
            String externalCode = (String) itemMap.get("externalCode");
            if (StringUtils.isNotBlank(externalCode)) {
                itemDTO.setExternalCode(externalCode);
            }
            itemDTO.setRemark("爬虫生成");
            String platformProjectName = (String) itemMap.get("platformProjectName");
            itemDTO.setPlatformProjectName(platformProjectName);

            BigDecimal rmbAmount = (BigDecimal) itemMap.get("rmbAmount");
            itemDTO.setRmbAmount(rmbAmount);

            BigDecimal originalAmount = (BigDecimal) itemMap.get("originalAmount");
            itemDTO.setOriginalAmount(originalAmount);
            BigDecimal settlementRate = (BigDecimal) itemMap.get("settlementRate");
            itemDTO.setSettlementRate(settlementRate);

            //1.必填项校验
            String projectName = itemDTO.getPlatformProjectName();
            if(StringUtils.isBlank(projectName)) {
                LOGGER.error("结算费项不能为空, entity:{}", JSON.toJSONString(itemMap));
                Map<String, String> errMap = new HashMap<>();
                errMap.put("jsonInfo", JSON.toJSONString(infoMap));
                errMap.put("jsonDetail", JSON.toJSONString(itemMap));
                errMap.put("msg", "结算费项不能为空");
                failList.add(errMap);
                return false;
            }

            if(originalAmount == null) {
                LOGGER.error("订单结算金额（结算原币）不能为空, entity:{}", JSON.toJSONString(itemMap));
                Map<String, String> errMap = new HashMap<>();
                errMap.put("jsonInfo", JSON.toJSONString(infoMap));
                errMap.put("jsonDetail", JSON.toJSONString(itemMap));
                errMap.put("msg", "订单结算金额（结算原币）不能为空");
                failList.add(errMap);
                return false;
            }

            if(rmbAmount == null) {
                LOGGER.error("订单结算金额（RMB）不能为空, entity:{}", JSON.toJSONString(itemMap));
                Map<String, String> errMap = new HashMap<>();
                errMap.put("jsonInfo", JSON.toJSONString(infoMap));
                errMap.put("jsonDetail", JSON.toJSONString(itemMap));
                errMap.put("msg", "订单结算金额（RMB）不能为空");
                failList.add(errMap);
                return false;
            }

            if (StringUtils.isNotBlank(externalCode)) {
                if (!(noExistOrderList.contains(externalCode) || successOrderList.contains(externalCode))) {
                    //2.校验导入的外部订单号是否存在系统已发货订单，先以导入的电商订单号查找系统的外部订单号、找不到再找系统的平台订单号；若均校验不存在则提示“导入订单号不存在”；
                    OrderModel orderModelParam = new OrderModel();
                    orderModelParam.setExternalCode(itemDTO.getExternalCode());
                    orderModelParam.setMerchantId(billModel.getMerchantId());
                    List<OrderModel> orderList = orderDao.list(orderModelParam);
                    if (orderList.size() > 1) {
                        LOGGER.error("系统根据该外部交易单号找到了多条电商订单, entity:{}", JSON.toJSONString(itemMap));
                        Map<String, String> errMap = new HashMap<>();
                        errMap.put("jsonInfo", JSON.toJSONString(infoMap));
                        errMap.put("jsonDetail", JSON.toJSONString(itemMap));
                        errMap.put("msg", "系统根据该外部交易单号找到了多条电商订单");
                        failList.add(errMap);
                        return false;
                    }

                    if (orderList.isEmpty()) {
                        OrderModel orderModel = new OrderModel();
                        orderModel.setExOrderId(itemDTO.getExternalCode());
                        orderModel.setMerchantId(billModel.getMerchantId());
                        orderList = orderDao.list(orderModel);
                        if (orderList.size() > 1) {
                            LOGGER.error("系统根据该外部交易单号找到了多条电商订单, entity:{}", JSON.toJSONString(itemMap));
                            Map<String, String> errMap = new HashMap<>();
                            errMap.put("jsonInfo", JSON.toJSONString(infoMap));
                            errMap.put("jsonDetail", JSON.toJSONString(itemMap));
                            errMap.put("msg", "系统根据该外部交易单号找到了多条电商订单");
                            failList.add(errMap);
                            return false;
                        }

                        if (orderList.isEmpty()) {
                            noExistOrderList.add(externalCode);
                        }
                    }

                    if (!orderList.isEmpty()) {
                        orderCodeMap.put(externalCode, orderList.get(0));
                        successOrderList.add(externalCode);
                    }
                }
            }

            //3.校验结算费项是否存在
            PlatformSettlementConfigModel platformSettlementConfigModel = new PlatformSettlementConfigModel();
            platformSettlementConfigModel.setStorePlatformCode(billModel.getStorePlatformCode());
            platformSettlementConfigModel.setName(itemDTO.getPlatformProjectName());
            List<PlatformSettlementConfigModel> platformSettlementConfigModels = platformSettlementConfigDao.list(platformSettlementConfigModel);
            if (platformSettlementConfigModels.isEmpty()) {
                LOGGER.error("导入的费项名称不存在映射关系, param:{}", JSON.toJSONString(platformSettlementConfigModel));
                Map<String, String> errMap = new HashMap<>();
                errMap.put("jsonInfo", JSON.toJSONString(infoMap));
                errMap.put("jsonDetail", JSON.toJSONString(itemMap));
                errMap.put("msg", "导入的费项名称不存在映射关系");
                failList.add(errMap);
                return false;
            }
            if (platformSettlementConfigModels.size() > 1) {
                LOGGER.error("导入的费项名称存在多条映射关系, param:{}", JSON.toJSONString(platformSettlementConfigModel));
                Map<String, String> errMap = new HashMap<>();
                errMap.put("jsonInfo", JSON.toJSONString(infoMap));
                errMap.put("jsonDetail", JSON.toJSONString(itemMap));
                errMap.put("msg", "导入的费项名称存在多条映射关系");
                failList.add(errMap);
                return false;
            }


            //判断是应收明细还是费项明细
            if ("经销业务TO C收入".equals(platformSettlementConfigModels.get(0).getSettlementConfigName()) ||
                    "经销业务TO B收入".equals(platformSettlementConfigModels.get(0).getSettlementConfigName())) {
                itemDTO.setProjectId(platformSettlementConfigModels.get(0).getSettlementConfigId());
                itemDTO.setProjectName(platformSettlementConfigModels.get(0).getSettlementConfigName());
                itemDTO.setPlatformProjectId(platformSettlementConfigModels.get(0).getId());
                itemDTO.setPlatformProjectName(platformSettlementConfigModels.get(0).getName());
                itemDTO.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_0);
                if (itemDTO.getOriginalAmount().compareTo(BigDecimal.ZERO) == -1) {
                    itemDTO.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_1);
                    itemDTO.setRmbAmount(itemDTO.getRmbAmount().abs());
                    itemDTO.setOriginalAmount(itemDTO.getOriginalAmount().abs());
                }
                if (StringUtils.isBlank(externalCode) || !orderCodeMap.containsKey(externalCode)) {
                    TocSettlementReceiveBillItemModel billItemModel = new TocSettlementReceiveBillItemModel();
                    BeanUtils.copyProperties(itemDTO, billItemModel);
                    billItemModel.setBillId(billModel.getId());
                    billItemModel.setParentBrandName(superiorParentBrandName);
                    billItemModel.setParentBrandCode(superiorParentBrandNcCode);
                    billItemModel.setParentBrandId(superiorParentBrandId);
                    itemModels.add(billItemModel);
                } else {
                    rightItemDTOS.add(itemDTO);
                }
            } else {
                TocSettlementReceiveBillCostItemDTO costItemDTO = new TocSettlementReceiveBillCostItemDTO();
                costItemDTO.setExternalCode(itemDTO.getExternalCode());
                costItemDTO.setProjectId(platformSettlementConfigModels.get(0).getSettlementConfigId());
                costItemDTO.setProjectName(platformSettlementConfigModels.get(0).getSettlementConfigName());
                costItemDTO.setPlatformProjectId(platformSettlementConfigModels.get(0).getId());
                costItemDTO.setPlatformProjectName(platformSettlementConfigModels.get(0).getName());
                costItemDTO.setSettlementRate(itemDTO.getSettlementRate());
                costItemDTO.setRmbAmount(itemDTO.getRmbAmount().abs());
                costItemDTO.setOriginalAmount(itemDTO.getOriginalAmount().abs());
                costItemDTO.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_0);
                costItemDTO.setDataSource(DERP_ORDER.TOCRECEIVEBILLCOST_DATASOURCE_0);
                costItemDTO.setBillId(billModel.getId());
                if(StringUtils.isNotBlank(itemDTO.getRemark())){
                    costItemDTO.setRemark(itemDTO.getRemark());
                }
                if (itemDTO.getOriginalAmount().compareTo(BigDecimal.ZERO) == -1) {
                    costItemDTO.setBillType(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_1);
                }
                if (StringUtils.isBlank(externalCode) || !orderCodeMap.containsKey(externalCode)) {
                    TocSettlementReceiveBillCostItemModel billItemModel = new TocSettlementReceiveBillCostItemModel();
                    BeanUtils.copyProperties(costItemDTO, billItemModel);
                    billItemModel.setBillId(billModel.getId());
                    billItemModel.setParentBrandName(superiorParentBrandName);
                    billItemModel.setParentBrandCode(superiorParentBrandNcCode);
                    billItemModel.setParentBrandId(superiorParentBrandId);
                    costItemModels.add(billItemModel);
                } else {
                    List<TocSettlementReceiveBillCostItemDTO> rightCostItemDTOS = new ArrayList<TocSettlementReceiveBillCostItemDTO>();

                    OrderModel orderModel = orderCodeMap.get(itemDTO.getExternalCode());
                    costItemDTO.setOrderCode(orderModel.getCode());
                    if (rightCostItemDTOSMap.containsKey(orderModel.getId())) {
                        rightCostItemDTOS.addAll(rightCostItemDTOSMap.get(orderModel.getId()));
                    }
                    rightCostItemDTOS.add(costItemDTO);

                    rightCostItemDTOSMap.put(orderModel.getId(), rightCostItemDTOS);
                }
            }
        }

        //获取所有母品牌
        Map<String, BrandParentMongo> brandParentMap = new HashMap<>();
        List<Long> goodsIdList = new ArrayList<>();
        Map<Long, MerchandiseInfoMogo> merchandiseInfoMogoMap = new HashMap<>();

        /**导入模板为订单维度的结算金额，生成系统的平台结算单应收明细需到”订单+商品货号“维度，故根据本次导入成功的订单号查询系统拿到对应订单的商品货号，
         * 以导入的订单总结算金额按权重比例分摊到订单中每个商品上；
         * 分摊公式为：前N-1个商品分摊的结算金额= （系统订单商品结算总额/订单所有商品结算总额合计）*导入的订单结算金额，金额四舍五入，保留2位小数；
         * 最后一个商品采用倒减=导入的订单结算金额-前面已分摊的商品结算金额（已做四舍五入的金额）汇总；*/

        for (TocSettlementReceiveBillItemDTO itemDTO : rightItemDTOS) {
            OrderModel orderModel = orderCodeMap.get(itemDTO.getExternalCode());
            OrderItemModel orderItemModel = new OrderItemModel();
            orderItemModel.setOrderId(orderModel.getId());
            List<OrderItemModel> orderItemModels  = orderItemDao.list(orderItemModel);

            Map<String, BigDecimal> tempPriceMap = calculateTempPrice(orderItemModels, itemDTO.getOriginalAmount());
            Map<String, BigDecimal> tempRMBPriceMap = null;
            if (itemDTO.getRmbAmount() == null) {
                tempRMBPriceMap = calculateTempPrice(orderItemModels, orderModel.getPayment());
            } else {
                tempRMBPriceMap = calculateTempPrice(orderItemModels, itemDTO.getRmbAmount());
            }

            for (OrderItemModel itemModel : orderItemModels) {
                TocSettlementReceiveBillItemModel billItemModel = new TocSettlementReceiveBillItemModel();
                BeanUtils.copyProperties(itemDTO, billItemModel);
                billItemModel.setOrderCode(orderModel.getCode());
                billItemModel.setBillId(billModel.getId());
                billItemModel.setGoodsId(itemModel.getGoodsId());
                billItemModel.setGoodsNo(itemModel.getGoodsNo());
                billItemModel.setGoodsName(itemModel.getGoodsName());
                billItemModel.setNum(itemModel.getNum());

                String key = itemModel.getOrderId() + "_" + itemModel.getId();
                billItemModel.setOriginalAmount(tempPriceMap.get(key));
                billItemModel.setRmbAmount(tempRMBPriceMap.get(key));
                itemModels.add(billItemModel);

                if (!goodsIdList.contains(itemModel.getGoodsId())) {
                    goodsIdList.add(itemModel.getGoodsId());
                }
            }
        }

        int pageSize = 1000;

        /**以“订单+费项”的维度记录结算金额即可；若订单存在于系统，则母品牌取系统订单中结算金额最高的商品对应的母品牌即可，
         * 若订单不存在系统则根据当前应收单选择的店铺id 查询店铺信息表获取已维护的“专营母品牌”，存值为该订单的母品牌信息*/
        List<Long> orderIds = new ArrayList<>(rightCostItemDTOSMap.keySet());

        for (int i = 0; i < orderIds.size(); ) {
            int pageSub = (i+pageSize) < orderIds.size() ? (i+pageSize) : orderIds.size();
            //根据外部订单号查询对应的结算金额最高的商品明细
            List<OrderItemModel> orderItemModels = orderItemDao.getMaxPriceByOrderId(orderIds.subList(i, pageSub), false);

            for (OrderItemModel itemModel : orderItemModels) {
                List<TocSettlementReceiveBillCostItemDTO> costItemDTOS = rightCostItemDTOSMap.get(itemModel.getOrderId());

                for (TocSettlementReceiveBillCostItemDTO costItemDTO : costItemDTOS) {
                    costItemDTO.setGoodsId(itemModel.getGoodsId());
                    costItemDTO.setNum(itemModel.getNum());
                }

                if (!goodsIdList.contains(itemModel.getGoodsId())) {
                    goodsIdList.add(itemModel.getGoodsId());
                }
            }

            i = pageSub;
        }

        if (!goodsIdList.isEmpty()) {
            List<MerchandiseInfoMogo> merchandiseInfoMogos = merchandiseInfoMogoDao.findAllByIn("merchandiseId", goodsIdList);
            for (MerchandiseInfoMogo merchandiseInfo : merchandiseInfoMogos) {
                if (StringUtils.isNotBlank(merchandiseInfo.getCommbarcode())) {
                    merchandiseInfoMogoMap.put(merchandiseInfo.getMerchandiseId(), merchandiseInfo);
                }
            }
        }

        //批量保存保存应收明细
        for (int i = 0; i < itemModels.size(); ) {
            int pageSub = (i+pageSize) < itemModels.size() ? (i+pageSize) : itemModels.size();

            for (TocSettlementReceiveBillItemModel itemModel : itemModels.subList(i, pageSub)) {
                MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoMap.get(itemModel.getGoodsId());
                if (merchandiseInfo != null) {
                    BrandParentMongo brandParent = brandParentMap.get(merchandiseInfo.getCommbarcode());
                    if (brandParent == null) {
                        brandParent = brandParentMongoDao.getBrandParentMongoByCommbarcode(merchandiseInfo.getCommbarcode());
                    }

                    if (brandParent != null) {
                        itemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                        itemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                        itemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                        brandParentMap.put(merchandiseInfo.getCommbarcode(), brandParent);
                    }
                }

            }

            tocSettlementReceiveBillItemDao.batchSave(itemModels.subList(i, pageSub));
            i = pageSub;
        }
        itemModels = null;

        //批量保存保存费项明细
        for (Long key : rightCostItemDTOSMap.keySet()) {
            List<TocSettlementReceiveBillCostItemDTO> costItemDTOS = rightCostItemDTOSMap.get(key);

            BrandParentMongo brandParent = null;
            MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoMap.get(costItemDTOS.get(0).getGoodsId());
            if (merchandiseInfo != null) {
                brandParent = brandParentMap.get(merchandiseInfo.getCommbarcode());
                if (brandParent == null) {
                    brandParent = brandParentMongoDao.getBrandParentMongoByCommbarcode(merchandiseInfo.getCommbarcode());
                }

            }

            for (TocSettlementReceiveBillCostItemDTO costItemDTO : costItemDTOS) {
                TocSettlementReceiveBillCostItemModel costItemModel = new TocSettlementReceiveBillCostItemModel();
                BeanUtils.copyProperties(costItemDTO, costItemModel);

                if (brandParent != null) {
                    costItemModel.setParentBrandName(brandParent.getSuperiorParentBrand());
                    costItemModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                    costItemModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
                    brandParentMap.put(merchandiseInfo.getCommbarcode(), brandParent);
                }

                costItemModels.add(costItemModel);
            }
        }

        for (int i = 0; i < costItemModels.size(); ) {
            int pageSub = (i + pageSize) < costItemModels.size() ? (i + pageSize) : costItemModels.size();

            tocSettlementReceiveBillCostItemDao.batchSave(costItemModels.subList(i, pageSub));
            i = pageSub;
        }

        costItemModels = null;
        rightCostItemDTOSMap = null;
        orderIds = null;
        successOrderList = null;
        noExistOrderList = null;

        //更新账单状态
        BigDecimal oriAmount = new BigDecimal("0");
        BigDecimal receiveAmount = new BigDecimal("0");
        BigDecimal oriReceivablePrice = tocSettlementReceiveBillItemDao.getTotalSettlementReceivePrice(billModel.getId());
        BigDecimal receivablePrice = tocSettlementReceiveBillItemDao.getTotalReceivePrice(billModel.getId());
        BigDecimal costFree = tocSettlementReceiveBillCostItemDao.getTotalReceivePrice(billModel.getId());
        BigDecimal oriCostFree = tocSettlementReceiveBillCostItemDao.getTotalSettlementPrice(billModel.getId());
        receivablePrice = receivablePrice == null ? new BigDecimal("0") : receivablePrice;
        oriReceivablePrice = oriReceivablePrice == null ? new BigDecimal("0") : oriReceivablePrice;
        costFree = costFree == null ? new BigDecimal("0") : costFree;
        oriCostFree = oriCostFree == null ? new BigDecimal("0") : oriCostFree;
        receiveAmount = receivablePrice.add(costFree);
        oriAmount = oriReceivablePrice.add(oriCostFree);
        TocSettlementReceiveBillModel update = new TocSettlementReceiveBillModel();
        update.setId(billModel.getId());
        update.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_00);
        update.setReceivableAmount(receiveAmount);
        update.setErrorMsgPath("");
        update.setOriReceivableAmount(oriAmount);
        tocSettlementReceiveBillDao.updateWithNull(update);
        return true;
    }

    private void closeSQLConn(Connection conn, Statement st, PreparedStatement pst,  ResultSet rs) throws SQLException {
        if(rs != null) {
            rs.close();
        }

        if(pst != null) {
            pst.close();
        }

        if(st != null) {
            st.close();
        }

        if(conn != null) {
            conn.close();
        }
    }

    /**
     * 创建数据库链接
     * @return
     * @throws Exception
     */
    private Connection createSQLConn() throws Exception {
        Class.forName(ApolloUtilDB.jdbforName);
        Connection conn = DriverManager.getConnection(ApolloUtilDB.crawlerUrl, ApolloUtilDB.crawlerUserName,ApolloUtilDB.crawlerPassword);

        return conn ;
    }

    private TocSettlementReceiveBillItemDTO excelMapExchangeItemDto(Map<String, String> map) {
        TocSettlementReceiveBillItemDTO dto = new TocSettlementReceiveBillItemDTO() ;
        String externalCode = map.get("电商订单号") ;
        if (StringUtils.isNotBlank(externalCode)) {
            dto.setExternalCode(externalCode.trim());
        }

        String projectName = map.get("结算费项");
        if (StringUtils.isNotBlank(projectName)) {
            dto.setPlatformProjectName(projectName);
        }

        String originalAmountStr = map.get("订单结算金额\n（结算原币）");
        if(StringUtils.isNotBlank(originalAmountStr)) {
            if (!StringUtils.isNumeric(originalAmountStr)) {
                throw  new RuntimeException("订单结算金额（结算原币）必须为数值");
            }
            BigDecimal originalAmount = new BigDecimal(originalAmountStr) ;
            dto.setOriginalAmount(originalAmount);
        }
        String rmbAmountStr = map.get("订单结算金额\n（RMB）");
        if(StringUtils.isNotBlank(rmbAmountStr)) {
            if (!StringUtils.isNumeric(rmbAmountStr)) {
                throw  new RuntimeException("订单结算金额（RMB）必须为数值");
            }
            BigDecimal rmbAmount = new BigDecimal(rmbAmountStr) ;
            dto.setRmbAmount(rmbAmount);
        }
        String rateStr = map.get("结算汇率");
        if(StringUtils.isNotBlank(rateStr)) {
            if (!StringUtils.isNumeric(rateStr)) {
                throw  new RuntimeException("结算汇率必须为数值");
            }
            BigDecimal rate = new BigDecimal(rateStr) ;
            dto.setSettlementRate(rate);
        }
        return dto ;
    }

    private void setExceptionMsg(TocSettlementReceiveBillItemDTO dto, List<TocSettlementReceiveBillItemDTO> dtoList, String isException, String exceptionMsg) {
        dto.setIsException(isException);
        dto.setExcetionResult(exceptionMsg);
        dtoList.add(dto);
    }

    private Map<String, BigDecimal> calculateTempPrice(List<OrderItemModel> orderItemModels, BigDecimal amount) {
        Map<String, BigDecimal> calculateTempPriceMap = new HashMap<>();
        //订单所有商品结算总额合计
        BigDecimal totalPrice = new BigDecimal("0");
        for (OrderItemModel itemModel : orderItemModels) {
            totalPrice = totalPrice.add(itemModel.getDecTotal());
        }
        //已经摊分的金额
        BigDecimal alreadyPrice = new BigDecimal("0");
        /**分摊公式为：前N-1个商品分摊的暂估应收金额= （系统订单商品结算总额/订单所有商品结算总额合计）*导入的订单结算金额，金额四舍五入，保留2位小数；
         * 最后一个商品采用倒减=订单实付总额-前面已分摊的商品结算金额（已做四舍五入的金额）汇总；*/
        for (int i = 0; i < orderItemModels.size(); i++) {
            String key = orderItemModels.get(i).getOrderId() + "_" + orderItemModels.get(i).getId();
            if (totalPrice.compareTo(new BigDecimal("0")) != 0) {
                if (i == orderItemModels.size()-1) {
                    BigDecimal tempPrice = amount.subtract(alreadyPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
                    calculateTempPriceMap.put(key, tempPrice);
                } else {
                    BigDecimal tempPrice = amount.multiply(orderItemModels.get(i).getDecTotal()).divide(totalPrice, 2, BigDecimal.ROUND_HALF_UP);
                    alreadyPrice = alreadyPrice.add(tempPrice);
                    calculateTempPriceMap.put(key, tempPrice);
                }
            } else {
                calculateTempPriceMap.put(key,new BigDecimal("0"));
            }
        }
        return calculateTempPriceMap;
    }


    public boolean isNumeric(String str){
        String pattern = "^[\\-]?[\\d]+(\\.[\\d]+)?$";
        Pattern p = Pattern.compile(pattern);
        Matcher isNum = p.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
