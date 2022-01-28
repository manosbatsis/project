package com.topideal.order.service.bill.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.google.common.base.Joiner;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.RectangleReadOnly;
import com.topideal.api.nc.NcClientUtils;
import com.topideal.api.nc.nc08.ReceiveHcInvalidRoot;
import com.topideal.api.nc.nc10.Details;
import com.topideal.api.nc.nc10.InvoiceUpdateRoot;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.enums.SourceStatusEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.*;
import com.topideal.dao.common.FileTempDao;
import com.topideal.dao.common.SettlementConfigDao;
import com.topideal.dao.purchase.PurchaseSdOrderSditemDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.common.ReminderEmailUserDTO;
import com.topideal.entity.dto.common.SettlementConfigDTO;
import com.topideal.entity.dto.sale.SaleReturnIdepotDTO;
import com.topideal.entity.dto.sale.SaleReturnOrderDTO;
import com.topideal.entity.vo.bill.*;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.entity.vo.common.SettlementConfigModel;
import com.topideal.entity.vo.purchase.PurchaseSdOrderSditemModel;
import com.topideal.entity.vo.sale.*;
import com.topideal.enums.NcAPIEnum;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.bill.ReceiveBillService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.tools.ExcelConvertHTMLUtils;
import com.topideal.order.tools.pdf.Excel2PdfUtils;
import com.topideal.order.tools.pdf.FreemakerUtils;
import com.topideal.order.tools.pdf.PdfUtils;
import com.topideal.order.webapi.bill.form.*;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultDefaultValueProcessor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @Description: 应收账单service实现类
 * @Author: Chen Yiluan
 * @Date: 2020/07/27 15:01
 **/
@Service
public class ReceiveBillServiceImpl implements ReceiveBillService {

    @Autowired
    private ReceiveBillDao receiveBillDao;
    @Autowired
    private ReceiveBillItemDao receiveBillItemDao;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;
    @Autowired
    private SaleOrderDao saleOrderDao;
    @Autowired
    private SaleShelfDao saleShelfDao;
    @Autowired
    private BillOutinDepotItemDao billOutinDepotItemDao;
    @Autowired
    private ReceiveBillOperateDao receiveBillOperateDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private CustomerMerchantRelMongoDao customerMerchantRelMongoDao;
    @Autowired
    private ReceiveBillVerifyItemDao receiveBillVerifyItemDao;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private ReceiveBillCostItemDao receiveBillCostItemDao;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    @Autowired
    private BrandMongoDao brandMongoDao;
    @Autowired
    private FileTempDataMongoDao fileTempDataMongoDao;
    @Autowired
    private FileTempDao fileTempDao;
    @Autowired
    private ReceiveBillInvoiceDao receiveBillInvoiceDao;
    @Autowired
    private ReceiveInvoicenoDao receiveInvoicenoDao;
    @Autowired
    private PlatformCostOrderDao platformCostOrderDao;
    @Autowired
    private ShelfDao shelfDao;
    @Autowired
    private ReceiveExternalCodeDao receiveExternalCodeDao;
    @Autowired
    private SettlementConfigDao settlementConfigDao;
    @Autowired
    private ReceivePaymentSubjectDao receivePaymentSubjectDao;
    @Autowired
    private ProductInfoMongoDao productInfoMongoDao;
    @Autowired
    private UnitMongoDao unitMongoDao;
    @Autowired
    private PlatformStatementOrderDao platformStatementOrderDao;
    @Autowired
    private AttachmentInfoMongoDao attachmentInfoMongoDao;
    @Autowired
    private BrandSuperiorMongoDao brandSuperiorMongoDao;
    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao;
    @Autowired
    private SaleReturnOrderItemDao saleReturnOrderItemDao;
    @Autowired
    private PreSaleOrderItemDao preSaleOrderItemDao;
    @Autowired
    private SaleOrderItemDao saleOrderItemDao;
    @Autowired
    private PurchaseSdOrderSditemDao purchaseSdOrderSditemDao;
    @Autowired
    private SaleReturnIdepotItemDao saleReturnIdepotItemDao;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao;
    @Autowired
    private BillOutinDepotDao billOutinDepotDao;
    @Autowired
    private PreSaleOrderDao preSaleOrderDao;
    @Autowired
    private SaleReturnIdepotDao saleReturnIdepotDao;
    @Autowired
    private SaleReturnOrderDao saleReturnOrderDao;
    @Autowired
    private ReceiveCloseAccountsDao receiveCloseAccountsDao;


    @Override
    public ReceiveBillDTO listReceiveBillByPage(ReceiveBillDTO dto, User user) throws SQLException {
        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }
        ReceiveBillDTO receiveBillDTO = receiveBillDao.listReceiveBillByPage(dto);
        List<ReceiveBillDTO> receiveBillDTOS = receiveBillDTO.getList();
        List<Long> ids = new ArrayList<>();
        Map<Long, ReceiveBillDTO> receiveBillDTOMap = new LinkedHashMap<>();
        for (ReceiveBillDTO billDTO : receiveBillDTOS) {
            ids.add(billDTO.getId());
            billDTO.setReceivablePrice(BigDecimal.ZERO);
            receiveBillDTOMap.put(billDTO.getId(), billDTO);
        }

        if (!ids.isEmpty()) {
            //获取应收账单的应收明细金额
            List<Map<String, Object>> itemPriceList = receiveBillItemDao.listItemPrice(ids);
            for (Map<String, Object> map : itemPriceList) {
                Long billId = (Long) map.get("billId");
                BigDecimal totalItemPrice = (BigDecimal) map.get("totalPrice");
                ReceiveBillDTO receiveBillTemp = receiveBillDTOMap.get(billId);
                if (totalItemPrice != null) {
                    receiveBillTemp.setReceivablePrice(totalItemPrice);
                }
            }
            //获取应收账单的费用金额
            List<Map<String, Object>> costPriceList = receiveBillCostItemDao.listCostPrice(ids);
            for (Map<String, Object> map : costPriceList) {
                Long billId = (Long) map.get("billId");
                BigDecimal totalCostPrice = (BigDecimal) map.get("totalPrice");
                ReceiveBillDTO receiveBillTemp = receiveBillDTOMap.get(billId);
                if (totalCostPrice != null) {
                    BigDecimal receivablePrice = receiveBillTemp.getReceivablePrice().add(totalCostPrice);
                    receiveBillTemp.setReceivablePrice(receivablePrice);
                }
            }
            //获取应收账单的已核销金额
            List<Map<String, Object>> verifyPriceList = receiveBillVerifyItemDao.listVerifyPrice(ids);
            for (Map<String, Object> map : verifyPriceList) {
                Long billId = (Long) map.get("billId");
                BigDecimal totalCostPrice = (BigDecimal) map.get("totalPrice");
                ReceiveBillDTO receiveBillTemp = receiveBillDTOMap.get(billId);
                receiveBillTemp.setReceivedPrice(totalCostPrice);
            }
        }
        List<ReceiveBillDTO> receiveBillDTOList = new ArrayList<>(receiveBillDTOMap.values());
        receiveBillDTO.setList(receiveBillDTOList);
        return receiveBillDTO;
    }

    @Override
    public ReceiveBillDTO listAddOrderByPage(ReceiveBillDTO dto, User user) throws SQLException {
        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }
        if (StringUtils.isNotBlank(dto.getPoNo())) {
            String poNoStr = dto.getPoNo();
            poNoStr = poNoStr.replaceAll("(\r\n|\r|\n|\n\r)", "&");
            List<String> poNos = Arrays.asList(poNoStr.split("&"));
            dto.setPoNos(poNos);
        }

        if (StringUtils.isNotBlank(dto.getRelCode())) {
            String relCodeStr = dto.getRelCode();
            relCodeStr = relCodeStr.replaceAll("(\r\n|\r|\n|\n\r)", "&");
            List<String> relCodes = Arrays.asList(relCodeStr.split("&"));
            dto.setRelCodes(relCodes);
        }

        if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_1.equals(dto.getOrderType())) { //上架单
            ReceiveBillDTO receiveBillDTO = receiveBillDao.listAddShelfOrderByPage(dto);
            List<ReceiveBillDTO> receiveBillDTOList = receiveBillDTO.getList();
            List<String> orderCodeList = new ArrayList<>();
            List<String> poNoList = new ArrayList<>();
            for (ReceiveBillDTO billDTO : receiveBillDTOList) {
                String relCode = billDTO.getRelCode();
                String poNo = billDTO.getPoNo();
                orderCodeList.add(relCode);
                poNoList.add(poNo);
            }

            Map<String, Map<String, Object>> shelfItemMap = new HashMap<>();
            if (!orderCodeList.isEmpty()) {
                List<Map<String, Object>> shelfItemInfos = shelfDao.getShelfItemByCodes(orderCodeList);
                for (Map<String, Object> map : shelfItemInfos) {
                    String code = (String) map.get("code");
                    shelfItemMap.put(code, map);
                }
            }


            Map<String, Map<String, Object>> verifyItemMap = new HashMap<>();
            //判断该“上架单”是否已有创建应收单，若有则查询“To B暂估核销表”，找到剩余未核销暂估应收的SKU明细金额
            if (!orderCodeList.isEmpty()) {
                List<Map<String, Object>> verifyItems = receiveBillItemDao.verifyItems(orderCodeList);
                for (Map<String, Object> map : verifyItems) {
                    String code = (String) map.get("code");
                    verifyItemMap.put(code, map);
                }
            }

            for (ReceiveBillDTO billDTO : receiveBillDTOList) {
                String relCode = billDTO.getRelCode();
                String poNo = billDTO.getPoNo();
                BigDecimal totalNum = new BigDecimal("0");
                BigDecimal totalAmount = new BigDecimal("0");
                String currency = null;

                Map<String, Object> shelfMap = shelfItemMap.get(relCode);
                if (shelfMap != null) {
                    BigDecimal num = (BigDecimal) shelfMap.get("num");
                    BigDecimal amount = (BigDecimal) shelfMap.get("amount");
                    amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
                    currency = (String) shelfMap.get("currency");
                    totalNum = totalNum.add(num);
                    totalAmount = totalAmount.add(amount);
                    if (DERP_ORDER.RECEIVEBILL_ISADDWORN_0.equals(dto.getIsAddWorn())) {
                        BigDecimal damagedNum = (BigDecimal) shelfMap.get("damagedNum");
                        BigDecimal damagedAmount = (BigDecimal) shelfMap.get("damagedAmount");
                        totalNum = totalNum.add(damagedNum);
                        totalAmount = totalAmount.add(damagedAmount);
                    }
                }

                Map<String, Object> verifyItem = verifyItemMap.get(relCode);
                if (verifyItem != null) {
                    BigDecimal verifiyAmount = (BigDecimal) verifyItem.get("verifiyAmount");
                    verifiyAmount = verifiyAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
                    totalAmount = totalAmount.subtract(verifiyAmount);
                }

                billDTO.setNum(totalNum);
                billDTO.setAmount(totalAmount);
                billDTO.setCurrency(currency);
            }
            return receiveBillDTO;
        } else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_2.equals(dto.getOrderType())) { //账单出库单
            ReceiveBillDTO receiveBillDTO = receiveBillDao.listAddBillOutOrderByPage(dto);
            List<ReceiveBillDTO> receiveBillDTOList = receiveBillDTO.getList();
            for (ReceiveBillDTO billDTO : receiveBillDTOList) {
                String relCode = billDTO.getRelCode();
                String currency = billDTO.getCurrency();
                Long buId = billDTO.getBuId();
                List<Map<String, Object>> totalInfo = billOutinDepotItemDao.getTotalByBillCode(relCode, currency, dto.getMerchantId(), buId);
                BigDecimal totalNum = new BigDecimal("0");
                BigDecimal totalAmount = new BigDecimal("0");
                for (Map<String, Object> map : totalInfo) {
                    BigDecimal num = (BigDecimal) map.get("num");
                    BigDecimal amount = (BigDecimal) map.get("amount");
                    totalNum = totalNum.add(num);
                    totalAmount = totalAmount.add(amount);
                }
                billDTO.setNum(totalNum);
                billDTO.setAmount(totalAmount);
            }
            return receiveBillDTO;
        } else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_3.equals(dto.getOrderType())) { //预售单
            ReceiveBillDTO receiveBillDTO = receiveBillDao.listAddPreOrderByPage(dto);
            return receiveBillDTO;
        } else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_4.equals(dto.getOrderType())) { //销售订单
            ReceiveBillDTO receiveBillDTO = receiveBillDao.listAddSaleOrderByPage(dto);
            //销售单关联查询到的则默认取销售退货单整单数据；
            List<ReceiveBillDTO> receiveBillDTOList = receiveBillDTO.getList();
            List<String> orderCodeList = new ArrayList<>();
            for (ReceiveBillDTO billDTO : receiveBillDTOList) {
                String relCode = billDTO.getRelCode();
                orderCodeList.add(relCode);
            }
            if (!orderCodeList.isEmpty()) {
                List<SaleReturnOrderItemModel> statisticsList = saleReturnOrderItemDao.statisticsBySaleCode(orderCodeList);
                Map<String, List<SaleReturnOrderItemModel>> returnSaleOrderMap = new HashMap<>();
                for (SaleReturnOrderItemModel model : statisticsList) {
                    String saleOrderCode = model.getSaleOrderCode();
                    List<String> returnCodeList = Arrays.asList(saleOrderCode.split(","));
                    for (String code : returnCodeList) {
                        if (orderCodeList.contains(code)) {
                            List<SaleReturnOrderItemModel> existSaleReturnModels = new ArrayList<>();
                            existSaleReturnModels.add(model);
                            if (returnSaleOrderMap.containsKey(code)) {
                                existSaleReturnModels.addAll(returnSaleOrderMap.get(code));
                            }
                            returnSaleOrderMap.put(code, existSaleReturnModels);
                        }
                    }
                }
                for (ReceiveBillDTO billDTO : receiveBillDTOList) {
                    String relCode = billDTO.getRelCode();
                    BigDecimal totalNum = billDTO.getNum() == null ? BigDecimal.ZERO : billDTO.getNum();
                    BigDecimal totalAmount = billDTO.getAmount() == null ? BigDecimal.ZERO : billDTO.getAmount();
                    List<SaleReturnOrderItemModel> saleReturnItemList = returnSaleOrderMap.get(relCode);
                    if (saleReturnItemList != null) {
                        for (SaleReturnOrderItemModel itemModel : saleReturnItemList) {
                            Integer returnNum = itemModel.getReturnNum();
                            Integer badGoodsNum = itemModel.getBadGoodsNum();
                            BigDecimal price = itemModel.getPrice() == null ? BigDecimal.ZERO : itemModel.getPrice();
                            Integer returnOrderNum = returnNum + badGoodsNum;
                            totalNum = totalNum.subtract(new BigDecimal(returnOrderNum));
                            totalAmount = totalAmount.subtract(price.multiply(new BigDecimal(returnOrderNum)).setScale(2, BigDecimal.ROUND_HALF_UP));
                        }
                    }
                    billDTO.setNum(totalNum);
                    billDTO.setAmount(totalAmount);
                }
            }


            return receiveBillDTO;
        } else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_5.equals(dto.getOrderType())) { //采购SD单
            ReceiveBillDTO receiveBillDTO = receiveBillDao.listAddPurchaseSDOrderByPage(dto);
            return receiveBillDTO;
        } else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_7.equals(dto.getOrderType())) { //销售退款单
            SaleReturnIdepotDTO saleReturnIdepotDTO = new SaleReturnIdepotDTO();
            BeanUtils.copyProperties(dto, saleReturnIdepotDTO);
            SaleReturnIdepotDTO returnOrderDTO = receiveBillDao.listAddSaleReturnOrderByPage(saleReturnIdepotDTO);

            if (returnOrderDTO.getList().size() == 0) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }

            List<SaleReturnIdepotDTO> returnIdepotDTOS = returnOrderDTO.getList();

            List<Long> idepotIds = new ArrayList<>();
            List<Long> returnIds = new ArrayList<>();
            for (SaleReturnIdepotDTO idepotDTO : returnIdepotDTOS) {
                idepotIds.add(idepotDTO.getId());
                returnIds.add(idepotDTO.getOrderId());
            }


            //销售退订单表体
            List<SaleReturnOrderItemModel> saleReturnOrderItemModels = saleReturnOrderItemDao.listByOrderIds(returnIds);
            Map<String, BigDecimal> returnIdPoGoodsIdMap = new HashMap<>();
            for (SaleReturnOrderItemModel itemModel : saleReturnOrderItemModels) {
                String key = itemModel.getOrderId() + "_" + itemModel.getPoNo() + "_" + itemModel.getInGoodsId();
                returnIdPoGoodsIdMap.put(key, itemModel.getPrice());
            }

            //销售退货入库单表体
            List<SaleReturnIdepotItemModel> saleReturnIdepotItemModels = saleReturnIdepotItemDao.listBySreturnIdepotIds(idepotIds);

            Map<Long, List<SaleReturnIdepotItemModel>> saleReturnIdepotMap = new HashMap<>();
            for (SaleReturnIdepotItemModel itemModel : saleReturnIdepotItemModels) {
                List<SaleReturnIdepotItemModel> itemModels = new ArrayList<>();

                if (saleReturnIdepotMap.containsKey(itemModel.getSreturnIdepotId())) {
                    itemModels.addAll(saleReturnIdepotMap.get(itemModel.getSreturnIdepotId()));
                }
                itemModels.add(itemModel);
                saleReturnIdepotMap.put(itemModel.getSreturnIdepotId(), itemModels);
            }

            List<ReceiveBillDTO> receiveBillDTOS = new ArrayList<>();
            for (SaleReturnIdepotDTO idepotDTO : returnIdepotDTOS) {
                ReceiveBillDTO receiveBillDTO = new ReceiveBillDTO();
                receiveBillDTO.setOrderType(DERP_ORDER.RECEIVEBILL_ORDERTYPE_7);
                receiveBillDTO.setRelCode(idepotDTO.getCode());
                receiveBillDTO.setBuId(idepotDTO.getBuId());
                receiveBillDTO.setBuName(idepotDTO.getBuName());
                receiveBillDTO.setCustomerId(idepotDTO.getCustomerId());
                receiveBillDTO.setCustomerName(idepotDTO.getCustomerName());
                receiveBillDTO.setPoNo(idepotDTO.getPoNo());
                receiveBillDTO.setCurrency(idepotDTO.getCurrency());

                List<SaleReturnIdepotItemModel> idepotItemModels = saleReturnIdepotMap.get(idepotDTO.getId());

                BigDecimal amount = new BigDecimal("0");
                Integer num = 0;

                for (SaleReturnIdepotItemModel itemModel : idepotItemModels) {
                    String key = idepotDTO.getOrderId() + "_" + itemModel.getPoNo() + "_" + itemModel.getInGoodsId();
                    BigDecimal price = returnIdPoGoodsIdMap.get(key) == null ? BigDecimal.ZERO : returnIdPoGoodsIdMap.get(key);
                    Integer returnNum = itemModel.getReturnNum() == null ? 0 : itemModel.getReturnNum();
                    Integer wornNum = itemModel.getWornNum() == null ? 0 : itemModel.getWornNum();
                    returnNum += wornNum;

                    amount = amount.add(new BigDecimal(returnNum).multiply(price));
                    num += returnNum;
                }

                receiveBillDTO.setAmount(amount);
                receiveBillDTO.setNum(new BigDecimal(num));
                receiveBillDTOS.add(receiveBillDTO);
            }

            ReceiveBillDTO receiveBillDTO = new ReceiveBillDTO();
            BeanUtils.copyProperties(returnOrderDTO, receiveBillDTO);
            receiveBillDTO.setList(receiveBillDTOS);
            return receiveBillDTO;
        } /* else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_6.equals(dto.getOrderType())) { //融资申请单
            ReceiveBillDTO receiveBillDTO = receiveBillDao.listAddFinancingOrderByPage(dto);
            return receiveBillDTO;
        }*/ else {
            dto.setList(new ArrayList<>());
            dto.setTotal(0);
            return dto;
        }
    }

    @Override
    public Map<String, String> isExistOrder(ReceiveBillDTO dto) throws SQLException {
        Map<String, String> result = new HashMap<>();
        List<Map<String, Object>> orderList = dto.getOrderList();
        String existCurrency = null;
        Integer existBuId = null;
        Integer existCustomerId = null;
        int index = 0;
        for (Map<String, Object> itemMap : orderList) {
            String relCode = (String) itemMap.get("relCode");
            String orderType = (String) itemMap.get("orderType");
            String currency = (String) itemMap.get("currency");
            Integer buId = (Integer) itemMap.get("buId");
            Integer customerId = (Integer) itemMap.get("customerId");
            if (index == 0) {
                existCurrency = currency;
                existBuId = buId;
                existCustomerId = customerId;
            } else {
                if (!existCurrency.equals(currency)) {
                    result.put("code", "01");
                    result.put("message", "勾选的记录必须为相同的币种!");
                    return result;
                }
                if (!existBuId.equals(buId)) {
                    result.put("code", "01");
                    result.put("message", "勾选的记录必须为相同的事业部!");
                    return result;
                }
                if (!existCustomerId.equals(customerId)) {
                    result.put("code", "01");
                    result.put("message", "勾选的记录必须为相同的客户!");
                    return result;
                }
            }
            ReceiveExternalCodeModel externalCodeModel = new ReceiveExternalCodeModel();
            externalCodeModel.setBuId(Long.valueOf(buId));
            externalCodeModel.setExternalCode(relCode);
            externalCodeModel.setOrderType(orderType);
            externalCodeModel.setCurrency(currency);
            ReceiveExternalCodeModel exist = receiveExternalCodeDao.searchByModel(externalCodeModel);
            if (exist != null) {
                result.put("code", "01");
                result.put("message", "该业务单号：" + relCode + "关联的应收账单已经存在");
                return result;
            }
        }
        result.put("code", "00");
        return result;
    }

    @Override
    public Map<String, String> saveReceiveBill(ReceiveBillDTO dto, User user) throws Exception {
        Map<String, String> result = new HashMap<>();
        String billCode = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_YSZD);
        List<Map<String, Object>> orderList = dto.getOrderList();
        List<String> relCodes = new ArrayList<>();
        Integer buId = null;
        String orderType = null;
        String currency = null;
        String isAddWorn = null;
        for (Map<String, Object> itemMap : orderList) {
            String relCode = (String) itemMap.get("relCode");
            buId = (Integer) itemMap.get("buId");
            orderType = (String) itemMap.get("orderType");
            currency = (String) itemMap.get("currency");
            isAddWorn = (String) itemMap.get("isAddWorn");
            relCodes.add(relCode);
            if (StringUtils.isBlank(orderType)) {
                result.put("code", "01");
                result.put("message", "单据类型不能为空！");
                return result;
            }
        }
        Map<String, Object> receiveMap = new HashMap<String, Object>();
        receiveMap.put("orderType", orderType);
        receiveMap.put("merchantId", user.getMerchantId());
        receiveMap.put("merchantName", user.getMerchantName());
        receiveMap.put("currency", currency);
        receiveMap.put("buId", buId);
        receiveMap.put("billCode", billCode);
        receiveMap.put("relCodes", StringUtils.join(relCodes.toArray(), "&"));
        receiveMap.put("userId", user.getId());
        receiveMap.put("userName", user.getName());
        receiveMap.put("isAddWorn", isAddWorn);

        JSONObject receiveJson = JSONObject.fromObject(receiveMap);
        SendResult send = rocketMQProducer.send(receiveJson.toString(), MQOrderEnum.RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.RECEIVE_BILL_GENERATE.getTags());
        System.out.println(send);
        result.put("code", "00");
        result.put("billCode", billCode);
        result.put("message", "保存成功");
        return result;
    }

    @Override
    public Map<String, String> verifyAndSaveReceiveBill(List<ReceiveBillAddForm> addForms, User user) throws Exception {
        Map<String, String> result = new HashMap<>();
        List<String> existCurrencyList = new ArrayList<>();
        List<Long> existBuIdList = new ArrayList<>();
        List<Long> existCustomerIdList = new ArrayList<>();
        List<String> relCodes = new ArrayList<>();
        String orderType = null;
        String isAddWorn = null;
        for (ReceiveBillAddForm addForm : addForms) {
            String relCode = addForm.getRelCode();
            String currency = addForm.getCurrency();
            Long buId = addForm.getBuId();
            Long customerId = addForm.getCustomerId();
            orderType = addForm.getOrderType();
            isAddWorn = addForm.getIsAddWorn();

            relCodes.add(relCode);

            if (!existCurrencyList.contains(currency)) {
                existCurrencyList.add(currency);
            }
            if (!existBuIdList.contains(buId)) {
                existBuIdList.add(buId);
            }
            if (!existCustomerIdList.contains(customerId)) {
                existCustomerIdList.add(customerId);
            }

            if (existCurrencyList.size() > 1) {
                result.put("code", "01");
                result.put("message", "勾选的记录必须为相同的币种!");
                return result;
            }
            if (existCurrencyList.size() > 1) {
                result.put("code", "01");
                result.put("message", "勾选的记录必须为相同的事业部!");
                return result;
            }
            if (existCustomerIdList.size() > 1) {
                result.put("code", "01");
                result.put("message", "勾选的记录必须为相同的客户!");
                return result;
            }

            if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_2.equals(orderType)
                    || DERP_ORDER.RECEIVEBILL_ORDERTYPE_3.equals(orderType)
                    || DERP_ORDER.RECEIVEBILL_ORDERTYPE_5.equals(orderType)) {
                ReceiveExternalCodeModel externalCodeModel = new ReceiveExternalCodeModel();
                externalCodeModel.setBuId(buId);
                externalCodeModel.setExternalCode(relCode);
                externalCodeModel.setOrderType(orderType);
                externalCodeModel.setCurrency(currency);
                ReceiveExternalCodeModel exist = receiveExternalCodeDao.searchByModel(externalCodeModel);
                if (exist != null) {
                    result.put("code", "01");
                    result.put("message", "该业务单号：" + relCode + "关联的应收账单已经存在");
                    return result;
                }
            }

        }

        String billCode = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_YSZD);
        Map<String, Object> receiveMap = new HashMap<String, Object>();
        receiveMap.put("orderType", orderType);
        receiveMap.put("merchantId", user.getMerchantId());
        receiveMap.put("merchantName", user.getMerchantName());
        receiveMap.put("currency", existCurrencyList.get(0));
        receiveMap.put("buId", existBuIdList.get(0));
        receiveMap.put("billCode", billCode);
        receiveMap.put("relCodes", StringUtils.join(relCodes.toArray(), "&"));
        receiveMap.put("userId", user.getId());
        receiveMap.put("userName", user.getName());
        receiveMap.put("isAddWorn", isAddWorn);

        JSONObject receiveJson = JSONObject.fromObject(receiveMap);
        SendResult send = rocketMQProducer.send(receiveJson.toString(), MQOrderEnum.RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.RECEIVE_BILL_GENERATE.getTags());
        System.out.println(send);
        result.put("code", "00");
        result.put("billCode", billCode);
        result.put("message", "保存成功");
        return result;
    }


    @Override
    public ReceiveBillDTO searchDTOById(Long id) throws SQLException {
        ReceiveBillDTO dto = receiveBillDao.searchDTOById(id);
        Map<String, Object> relParams = new HashMap<>();
        relParams.put("customerId", dto.getCustomerId());
        relParams.put("merchantId", dto.getMerchantId());
        CustomerMerchantRelMongo relMongo = customerMerchantRelMongoDao.findOne(relParams);
        if (relMongo != null) {
            dto.setAccountPeriod(DERP_SYS.getLabelByKey(DERP_SYS.customerInfo_accountPeriodList, relMongo.getAccountPeriod()));
        }
        return dto;
    }

    @Override
    public List<Map<String, Object>> receiveTotalById(Long id) throws SQLException {
        return receiveBillDao.receiveTotalById(id);
    }

    @Override
    public List<ReceiveBillCostItemDTO> deductionTotalById(Long id) throws SQLException {
        List<Map<String, Object>> deductionMapList = receiveBillDao.deductionTotalById(id);
        Map<Long, ReceiveBillCostItemDTO> resultMap = new HashMap<>();
        List<ReceiveBillCostItemDTO> resultList = new ArrayList<>();
        for (Map<String, Object> map : deductionMapList) {
            Long projectId = (Long) map.get("project_id");
            String projectName = (String) map.get("project_name");
            String billType = (String) map.get("bill_type");
            BigDecimal totalNum = map.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalNum");
            BigDecimal totalTaxAmount = map.get("totalTaxAmount") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalTaxAmount");
            BigDecimal totalTax = map.get("totalTax") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalTax");
            BigDecimal totalPrice = (BigDecimal) map.get("totalPrice");
            if (resultMap.containsKey(projectId)) {
                ReceiveBillCostItemDTO costItemModel = resultMap.get(projectId);
                costItemModel.setNum(costItemModel.getNum() + totalNum.intValue());
                if (DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_0.equals(billType)) {
                    BigDecimal total = costItemModel.getPrice().add(totalPrice);
                    BigDecimal totalTA = costItemModel.getTaxAmount().add(totalTaxAmount);
                    BigDecimal totalT = costItemModel.getTax().add(totalTax);
                    costItemModel.setPrice(total);
                    costItemModel.setTax(totalT);
                    costItemModel.setTaxAmount(totalTA);
                } else {
                    BigDecimal total = costItemModel.getPrice().subtract(totalPrice);
                    BigDecimal totalTA = costItemModel.getTaxAmount().subtract(totalTaxAmount);
                    BigDecimal totalT = costItemModel.getTax().subtract(totalTax);
                    costItemModel.setPrice(total);
                    costItemModel.setTax(totalT);
                    costItemModel.setTaxAmount(totalTA);
                }
            } else {
                ReceiveBillCostItemDTO costItemModel = new ReceiveBillCostItemDTO();
                SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(projectId);
                if (settlementConfigModel != null) {
                    costItemModel.setParentProjectName(settlementConfigModel.getParentProjectName());
                }
                costItemModel.setProjectId(projectId);
                costItemModel.setProjectName(projectName);
                costItemModel.setNum(totalNum.intValue());
                if (DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_0.equals(billType)) {
                    costItemModel.setPrice(totalPrice);
                    costItemModel.setTax(totalTax);
                    costItemModel.setTaxAmount(totalTaxAmount);
                } else {
                    BigDecimal total = totalPrice.negate();
                    BigDecimal totalTA = totalTaxAmount.negate();
                    BigDecimal totalT = totalTax.negate();
                    costItemModel.setPrice(total);
                    costItemModel.setTax(totalT);
                    costItemModel.setTaxAmount(totalTA);
                }
                resultMap.put(projectId, costItemModel);
            }
        }
        for (Long key : resultMap.keySet()) {
            resultList.add(resultMap.get(key));
        }
        return resultList;
    }

    @Override
    public Map<String, String> confirmReceiveBill(ReceiveBillSubmitForm form, User user) throws Exception {
        Map<String, String> retMap = new HashMap<String, String>();
        ReceiveBillModel receiveBillModel = receiveBillDao.searchById(form.getBillId());
        if (!DERP_ORDER.RECEIVEBILL_BILLSTATUS_00.equals(receiveBillModel.getBillStatus())) {
            retMap.put("code", "01");
            retMap.put("message", "此账单账单状态不是待提交");
            return retMap;
        }

        List<ReceiveBillItemDTO> itemList = form.getItemList();
        List<ReceiveBillItemModel> itemModels = new ArrayList<>();
        List<String> relCodes = new ArrayList<>();

        BigDecimal itemPrice = new BigDecimal("0");
        for (ReceiveBillItemDTO itemDTO : itemList) {
            Map<String, String> map = validateItems(itemDTO, receiveBillModel);

            if ("01".equals(map.get("code"))) {
                return map;
            }

            itemPrice = itemPrice.add(itemDTO.getPrice());

            ReceiveBillItemModel itemModel = new ReceiveBillItemModel();
            BeanUtils.copyProperties(itemDTO, itemModel);
            itemModels.add(itemModel);

            if (!relCodes.contains(itemDTO.getCode())) {
                relCodes.add(itemDTO.getCode());
            }
        }

        List<ReceiveBillVerifyItemModel> verifyItemModels = new ArrayList<>();
        BigDecimal verifyAmount = new BigDecimal("0"); // 核销金额
        BigDecimal receiveAmount = new BigDecimal("0"); // 应收金额
//        BigDecimal itemPrice = receiveBillItemDao.getTotalReceivePrice(receiveBillModel.getId());
        BigDecimal costPrice = receiveBillCostItemDao.getTotalReceivePrice(receiveBillModel.getId());

        /*if (itemPrice == null) {
            itemPrice = new BigDecimal(0);
        }*/

        if (costPrice == null) {
            costPrice = new BigDecimal(0);
        }

        receiveAmount = itemPrice.add(costPrice).setScale(2, BigDecimal.ROUND_HALF_UP);

        /*if (StringUtils.isNotBlank(form.getAdvanceIds())) {

            List<Long> advanceIdList = StrUtils.parseIds(form.getAdvanceIds());
            //勾选核销的预收单明细
            List<AdvanceBillItemModel> advanceBillItemModels = advanceBillItemDao.listByIds(advanceIdList);

            List<Long> advanceIds = new ArrayList<>();
            for (AdvanceBillItemModel advanceBillItemModel : advanceBillItemModels) {
                advanceIds.add(advanceBillItemModel.getAdvanceId());
                verifyAmount = verifyAmount.add(advanceBillItemModel.getAmount());
            }
            //勾选核销的预收单
            List<AdvanceBillModel> advanceBillModels = advanceBillDao.listByIds(advanceIds);

            ReceiveBillVerifyItemModel receiveBillVerifyItemModel = new ReceiveBillVerifyItemModel();
            receiveBillVerifyItemModel.setType(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_1);
            List<ReceiveBillVerifyItemModel> reAdvanceItemModels = receiveBillVerifyItemDao.list(receiveBillVerifyItemModel);
            //已勾稽的预收订单
            List<Long> reAdvanceIdList = new ArrayList<>();
            for (ReceiveBillVerifyItemModel itemModel : reAdvanceItemModels) {
                if (!itemModel.getBillId().equals(form.getBillId())) {
                    reAdvanceIdList.add(itemModel.getAdvanceId());
                }
            }

            Map<Long, AdvanceBillModel> advanceBillModelMap = new HashMap<>();

            //校验勾稽的预收单状态
            for (AdvanceBillModel advanceBillModel : advanceBillModels) {
                if (!(DERP_ORDER.RECEIVEBILL_BILLSTATUS_02.equals(advanceBillModel.getBillStatus()) ||
                        DERP_ORDER.RECEIVEBILL_BILLSTATUS_03.equals(advanceBillModel.getBillStatus()) ||
                        DERP_ORDER.RECEIVEBILL_BILLSTATUS_04.equals(advanceBillModel.getBillStatus()))) {
                    retMap.put("code", "01");
                    retMap.put("message", "预收单：" + advanceBillModel.getCode() + "状态不为“待核销”或“部分核销”或“已核销”");
                    return retMap;
                }

                advanceBillModelMap.put(advanceBillModel.getId(), advanceBillModel);
            }

            for (AdvanceBillItemModel advanceBillItemModel : advanceBillItemModels) {
                if (reAdvanceIdList.contains(advanceBillItemModel.getId())) {
                    retMap.put("code", "01");
                    retMap.put("message", "预收单：" + advanceBillItemModel.getRelCode() + "已被勾稽，不能重复勾稽");
                    return retMap;
                }
            }

            if (verifyAmount.compareTo(receiveAmount) > 0) {
                retMap.put("code", "01");
                retMap.put("message", "核销金额不能大于应收账单金额");
                return retMap;
            }

            Map<Long, AdvanceBillOperateItemModel> auditDateMap = new HashMap<>();
            Map<Long, AdvanceBillVerifyItemModel> verifyDateMap = new HashMap<>();
            if (!advanceIds.isEmpty()) {

                //预收款单的审核通过的最新时间
                List<AdvanceBillOperateItemModel> operateItemModels = advanceBillOperateItemDao.getLatestAuditModelByAdvanceIds(advanceIds);

                for (AdvanceBillOperateItemModel operateItemModel : operateItemModels) {
                    auditDateMap.put(operateItemModel.getAdvanceId(), operateItemModel);
                }

                //预收款单的核销收款的最新时间
                List<AdvanceBillVerifyItemModel> verifyModelByAdvanceIds = advanceBillVerifyItemDao.getLatestVerifyModelByAdvanceIds(advanceIds);

                for (AdvanceBillVerifyItemModel verifyItemModel : verifyModelByAdvanceIds) {
                    verifyDateMap.put(verifyItemModel.getAdvanceId(), verifyItemModel);
                }

            }

            for (AdvanceBillItemModel advanceBillItemModel : advanceBillItemModels) {
                AdvanceBillModel advanceBillModel = advanceBillModelMap.get(advanceBillItemModel.getAdvanceId());
                ReceiveBillVerifyItemModel verifyItemModel = new ReceiveBillVerifyItemModel();
                verifyItemModel.setAdvanceId(advanceBillItemModel.getId());
                verifyItemModel.setAdvanceCode(advanceBillModel.getCode());
                verifyItemModel.setPrice(advanceBillItemModel.getAmount());
                verifyItemModel.setType(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_1);
                verifyItemModel.setReceiceNo(advanceBillItemModel.getRelCode());
                verifyItemModel.setBillId(form.getBillId());

                AdvanceBillOperateItemModel operateItemModel = auditDateMap.get(advanceBillItemModel.getAdvanceId());
                if (operateItemModel != null) {
                    verifyItemModel.setVerifyDate(new Timestamp(operateItemModel.getOperateDate().getTime()));
                    verifyItemModel.setVerifyId(operateItemModel.getOperateId());
                    verifyItemModel.setVerifier(operateItemModel.getOperater());
                }

                AdvanceBillVerifyItemModel advanceBillVerifyItemModel = verifyDateMap.get(advanceBillItemModel.getAdvanceId());
                if (advanceBillVerifyItemModel != null) {
                    verifyItemModel.setReceiveDate(new Timestamp(verifyItemModel.getVerifyDate().getTime()));
                }
                verifyItemModels.add(verifyItemModel);
            }
        }

        if (!form.getAdvanceIds().isEmpty()) {
            //删除原来的勾稽关系
            ReceiveBillVerifyItemModel verifyItemModel = new ReceiveBillVerifyItemModel();
            verifyItemModel.setType(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_1);
            verifyItemModel.setBillId(form.getBillId());
            receiveBillVerifyItemDao.delVerify(verifyItemModel);

            for (ReceiveBillVerifyItemModel receiveBillVerifyItemModel : verifyItemModels) {
                receiveBillVerifyItemDao.save(receiveBillVerifyItemModel);
            }
        }*/

        receiveBillItemDao.delItems(receiveBillModel.getId(), null);
        if (!itemModels.isEmpty()) {
            receiveBillItemDao.batchSave(itemModels);
        }

        if (!DERP_ORDER.RECEIVEBILL_ORDERTYPE_2.equals(receiveBillModel.getOrderType())) {
            receiveBillModel.setRelCode(StringUtils.join(relCodes.toArray(), "&"));
        }
        receiveBillModel.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_01);
        receiveBillDao.modify(receiveBillModel);

        ReceiveBillOperateModel receiveBillOperateModel = new ReceiveBillOperateModel();
        receiveBillOperateModel.setBillId(form.getBillId());
        receiveBillOperateModel.setOperateId(user.getId());
        receiveBillOperateModel.setOperator(user.getName());
        receiveBillOperateModel.setOperateDate(TimeUtils.getNow());
        receiveBillOperateModel.setOperateNode(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_0);
        receiveBillOperateModel.setCreateDate(TimeUtils.getNow());
        receiveBillOperateDao.save(receiveBillOperateModel);

        /**
         * 发送审核提醒邮件
         */
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

        emailUserDTO.setBuId(receiveBillModel.getBuId());
        emailUserDTO.setBuName(receiveBillModel.getBuName());
        emailUserDTO.setMerchantId(receiveBillModel.getMerchantId());
        emailUserDTO.setMerchantName(receiveBillModel.getMerchantName());
        emailUserDTO.setOrderCode(receiveBillModel.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1));
        emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1);
        emailUserDTO.setSupplier(receiveBillModel.getCustomerName());
        emailUserDTO.setAmount(receiveBillModel.getCurrency() + "&nbsp;" + receiveAmount.toPlainString());
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setSubmitId(Arrays.asList(String.valueOf(user.getId())));
        emailUserDTO.setCreateId(receiveBillModel.getCreaterId());

        JSONObject emailJson = JSONObject.fromObject(emailUserDTO) ;

        rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                MQErpEnum.SEND_REMINDER_MAIL.getTags());

        retMap.put("code", "00");
        retMap.put("message", "成功");
        return retMap;
    }

    @Override
    public Map<String, String> auditReceiveBill(ReceiveBillAuditForm form, User user) throws Exception {
        Map<String, String> retMap = new HashMap<String, String>();

        ReceiveBillModel receiveBillModel = receiveBillDao.searchById(form.getBillId());
        if (!(DERP_ORDER.RECEIVEBILL_BILLSTATUS_01.equals(receiveBillModel.getBillStatus())
                || DERP_ORDER.RECEIVEBILL_BILLSTATUS_05.equals(receiveBillModel.getBillStatus()))) {
            retMap.put("code", "01");
            retMap.put("message", "此账单账单状态不是待审核/作废审批");
            return retMap;
        }

        if (DERP_ORDER.RECEIVEBILL_BILLSTATUS_01.equals(receiveBillModel.getBillStatus()) &&
                DERP_ORDER.RECEIVEBILLAUDIT_AUDITRESULT_00.equals(form.getAuditResult())) {
            if (StringUtils.isBlank(form.getCreditDate())) {
                retMap.put("code", "01");
                retMap.put("message", "入账日期不能为空");
                return retMap;
            }

            String today = TimeUtils.formatDay(new Date());
            if (today.compareTo(form.getCreditDate()) < 0) {
                retMap.put("code", "01");
                retMap.put("message", "入账日期必须小于等于当前日期");
                return retMap;
            }

            ReceiveCloseAccountsModel model = new ReceiveCloseAccountsModel();
            model.setMonth(form.getCreditDate().substring(0,7));
            model.setBuId(receiveBillModel.getBuId());
            model.setMerchantId(receiveBillModel.getMerchantId());
            model.setStatus(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_030);
            ReceiveCloseAccountsModel latestClose = receiveCloseAccountsDao.getLatestByModel(model);

            if (latestClose!=null) {
                retMap.put("code", "01");
                retMap.put("message", "入账月份:" + form.getCreditDate() + "不能小于应收关帐月份");
                return retMap;
            }

        }

        Timestamp auditTime = TimeUtils.getNow();

        ReceiveBillInvoiceModel invoiceModel = null;

        String auditType = null; //审批类型

        List<Long> relIdList = new ArrayList<>();
        List<String> relCodeList = new ArrayList<>(); //关联的其他应收发票单号集合

        boolean isSyn = false;
        boolean isSubmitAudit = true;

        String operateNode = null; //审核节点

        SettlementConfigDTO settlementConfigDTO = new SettlementConfigDTO();

        settlementConfigDTO.setCustomerId(receiveBillModel.getCustomerId());
        settlementConfigDTO.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);

        List<String> typeList = new ArrayList<>();
        typeList.add(DERP_ORDER.SETTLEMENTCONFIG_TYPE1);
        typeList.add(DERP_ORDER.SETTLEMENTCONFIG_TYPE3);
        settlementConfigDTO.setTypes(typeList);

        //校验费项配置是否存在
        for(ReceiveBillItemDTO itemDTO : form.getItemDTOS()) {
            settlementConfigDTO.setId(itemDTO.getProjectId());
            SettlementConfigDTO exist = settlementConfigDao.getDetailByCustomer(settlementConfigDTO);
            if (exist == null) {
                retMap.put("code", "01");
                retMap.put("message", "费项配置：" + itemDTO.getProjectName() + "不存在");
                return retMap;
            }
            itemDTO.setPaymentSubjectId(exist.getPaymentSubjectId());
            itemDTO.setPaymentSubjectName(exist.getPaymentSubjectName());
            ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(exist.getPaymentSubjectId());
            if (paymentSubjectModel != null) {
                //科目
                itemDTO.setSubjectCode(paymentSubjectModel.getSubCode());
                itemDTO.setSubjectName(paymentSubjectModel.getSubName());
            }
        }

        for(ReceiveBillCostItemDTO itemDTO : form.getCostItemDTOS()) {
            settlementConfigDTO.setId(itemDTO.getProjectId());
            SettlementConfigDTO exist = settlementConfigDao.getDetailByCustomer(settlementConfigDTO);
            if (exist == null) {
                retMap.put("code", "01");
                retMap.put("message", "费项配置：" + itemDTO.getProjectName() + "不存在");
                return retMap;
            }
            itemDTO.setPaymentSubjectId(exist.getPaymentSubjectId());
            itemDTO.setPaymentSubjectName(exist.getPaymentSubjectName());
            ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(exist.getPaymentSubjectId());
            if (paymentSubjectModel != null) {
                //科目
                itemDTO.setSubjectCode(paymentSubjectModel.getSubCode());
                itemDTO.setSubjectName(paymentSubjectModel.getSubName());
            }
        }

        BigDecimal itemPrice = receiveBillItemDao.getTotalReceivePrice(receiveBillModel.getId());
        BigDecimal costPrice = receiveBillCostItemDao.getTotalReceivePrice(receiveBillModel.getId());
        if (itemPrice == null) {
            itemPrice = new BigDecimal(0);
        }
        if (costPrice == null) {
            costPrice = new BigDecimal(0);
        }

        itemPrice = itemPrice.add(costPrice);

        //审核
        if (DERP_ORDER.RECEIVEBILL_BILLSTATUS_01.equals(receiveBillModel.getBillStatus())) {
            auditType = DERP_ORDER.RECEIVEBILLAUDIT_AUDITTYPE_0;

            relIdList.add(form.getBillId());
            //通过
            if (DERP_ORDER.RECEIVEBILLAUDIT_AUDITRESULT_00.equals(form.getAuditResult())) {
                operateNode = DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_1;
                receiveBillModel.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_02);

                //当账单来源为账单出库单时，查询该账单关联的平台结算单是否已开票，若开票则将发票号回填到应收账单，并更新发票状态
                if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_2.equals(receiveBillModel.getOrderType())) {
                    List<String> statementCodes = Arrays.asList(receiveBillModel.getRelCode().split("&"));
                    List<ReceiveBillInvoiceModel> existInvoices = receiveBillInvoiceDao.searchByRelCodes(statementCodes);
                    for (ReceiveBillInvoiceModel existInvoice : existInvoices) {
                        List<String> statementRelCodes = Arrays.asList(existInvoice.getRelStatementCodes().split(","));
                        List<String> intersection = statementCodes.stream().filter(item -> statementRelCodes.contains(item)).collect(toList());
                        if (!intersection.isEmpty()) {
                            receiveBillModel.setInvoiceStatus(existInvoice.getStatus());
                            receiveBillModel.setInvoiceId(existInvoice.getId());
                            ReceiveBillInvoiceModel updateInvoice = new ReceiveBillInvoiceModel();
                            updateInvoice.setId(existInvoice.getId());
                            if (StringUtils.isNotBlank(existInvoice.getInvoiceRelIds())) {
                                List<String> invoiceIds = Arrays.asList(existInvoice.getInvoiceRelIds().split(","));
                                List<String> invoiceCodes = Arrays.asList(existInvoice.getInvoiceRelCodes().split(","));
                                List invoiceIdsList = new ArrayList(invoiceIds);
                                List invoiceCodesList = new ArrayList(invoiceCodes);
                                if (!invoiceIds.contains(receiveBillModel.getId())) {
                                    invoiceIdsList.add(String.valueOf(receiveBillModel.getId()));
                                    invoiceCodesList.add(receiveBillModel.getCode());
                                }
                                updateInvoice.setInvoiceRelIds(Joiner.on(",").join(invoiceIdsList));
                                updateInvoice.setInvoiceRelCodes(Joiner.on(",").join(invoiceCodesList));
                            } else {
                                updateInvoice.setInvoiceRelIds(String.valueOf(receiveBillModel.getId()));
                                updateInvoice.setInvoiceRelCodes(receiveBillModel.getCode());
                            }
                            receiveBillInvoiceDao.modify(updateInvoice);

                            break;
                        }
                    }

                }
            } else { //驳回
                operateNode = DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_2;
                receiveBillModel.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_00);

                if (receiveBillModel.getInvoiceId() != null) {
                    receiveBillModel.setInvoiceStatus(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_02);
                    invoiceModel = receiveBillInvoiceDao.searchById(receiveBillModel.getInvoiceId());

                    if (invoiceModel == null || invoiceModel.getInvoiceRelIds() == null) {
                        retMap.put("code", "01");
                        retMap.put("message", "找不到应收账单对应的发票");
                        return retMap;
                    }
                }


            }
        } else { //作废审核
            auditType = DERP_ORDER.RECEIVEBILLAUDIT_AUDITTYPE_1;
            isSubmitAudit = false;
            //通过
            if (DERP_ORDER.RECEIVEBILLAUDIT_AUDITRESULT_00.equals(form.getAuditResult())) {
                operateNode = DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_4;
                receiveBillModel.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_06);

                /**作废审批类型的逻辑为
                 1. 若操作驳回，则更新账单状态为原始状态“待核销”，开票状态不必更新
                 2. 若作废的应收单审核通过，且发票状态为未开票，NC同步状态为“已同步”，则更新当前该应收账单状态为“已作废”，触发NC作废状态接口推送，对应接口为：4.8 结算账单红冲/作废接口
                 若NC同步状态为“未同步”则不触发接口
                 3. 若操作审核通过，当应收账单的开票状态为已开票时，检查该发票是否关联多张应收账单
                 3.1 若只有当前这张账单，则更新账单状态为“已作废”，开票状态由“已开票”更新为“已作废”，在发票文件增加已作废图标。
                 且当该发票的NC同步状态为“已同步”时，触发NC作废状态接口推送，对应接口为：4.8 结算账单红冲/作废接口
                 若NC同步状态不为“已同步”，则不触发NC作废状态接口推送。
                 3.2 若关联多张应收账单，则更新当前该应收账单账单状态、发票状态均为“已作废”，同张发票的多个应收单（非作废）的开票状态由“已开票”更新为“待开票”，在发票文件增加已作废图标（只增加一次）。
                 且当该发票的NC同步状态为“已同步”时，触发NC作废状态接口推送，对应接口为：4.8 结算账单红冲/作废接口，
                 同时检查该发票下是否存在非“已作废”状态的应收账单，若有则触发推送NC接口同步该账单的发票状态更新，对应接口为：4.10 发票更新接口
                 若NC同步状态不为“已同步”，则不触发NC作废状态接口推送。
                 */
                if (DERP_ORDER.RECEIVEBILL_INVOICESTATUS_01.equals(receiveBillModel.getInvoiceStatus()) ||
                        DERP_ORDER.RECEIVEBILL_INVOICESTATUS_03.equals(receiveBillModel.getInvoiceStatus())) {
                    invoiceModel = receiveBillInvoiceDao.searchById(receiveBillModel.getInvoiceId());
                    if (invoiceModel == null || invoiceModel.getInvoiceRelIds() == null) {
                        retMap.put("code", "01");
                        retMap.put("message", "找不到应收账单对应的发票");
                        return retMap;
                    }
                    ReceiveBillModel billModel = new ReceiveBillModel();
                    billModel.setInvoiceId(receiveBillModel.getInvoiceId());
                    List<ReceiveBillModel> relInvoiceModels = receiveBillDao.list(billModel);
                    for (ReceiveBillModel relModel : relInvoiceModels) {
                        relIdList.add(relModel.getId());
                        relCodeList.add(relModel.getCode());
                    }
                    if (relInvoiceModels != null) { //更新审核账单的发票状态为已作废
                        receiveBillModel.setInvoiceStatus(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_02);
                    }
                    if (relInvoiceModels != null && relInvoiceModels.size() > 0) {
                        relIdList.remove(form.getBillId());
                        relCodeList.remove(receiveBillModel.getCode());
                        if (relIdList.size() > 0) {
                            //批量更新关联应收账单的开票状态更新为“待开票”, 清空发票关联id
                            receiveBillDao.batchUpdateInvoiceStatus(relIdList, DERP_ORDER.RECEIVEBILL_INVOICESTATUS_00);
                        }
                    }
                }
            } else { //驳回
                operateNode = DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_5;
                receiveBillModel.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_02);
            }
        }

        if (StringUtils.isNotBlank(form.getCreditDate())) {
            receiveBillModel.setCreditDate(TimeUtils.parseDay(form.getCreditDate()));
        }

        //1.更新应收账单/更新应收明细和费用明细费项
        receiveBillDao.modify(receiveBillModel);
        if (!form.getItemDTOS().isEmpty()) {
            receiveBillItemDao.batchUpdate(form.getItemDTOS());
        }

        if (!form.getCostItemDTOS().isEmpty()) {
            receiveBillCostItemDao.batchUpdate(form.getCostItemDTOS());
        }

        //2.保存操作记录表
        ReceiveBillOperateModel receiveBillOperateModel = new ReceiveBillOperateModel();
        receiveBillOperateModel.setBillId(form.getBillId());
        receiveBillOperateModel.setOperateId(user.getId());
        receiveBillOperateModel.setOperator(user.getName());
        receiveBillOperateModel.setOperateDate(auditTime);
        receiveBillOperateModel.setRemark(form.getAuditRemark());
        receiveBillOperateModel.setOperateNode(operateNode);
        receiveBillOperateDao.save(receiveBillOperateModel);

        //3. 审核驳回存在发票则作废
        if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_2.equals(operateNode)) {
            if (invoiceModel != null) {

                //在发票文件增加已作废图标
                String updateUrl = addWatermark(invoiceModel.getInvoiceNo(), user, invoiceModel.getInvoicePath());

                ReceiveBillInvoiceModel receiveBillInvoiceModel = new ReceiveBillInvoiceModel();
                receiveBillInvoiceModel.setId(receiveBillModel.getInvoiceId());
                receiveBillInvoiceModel.setStatus(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_02);
                receiveBillInvoiceModel.setInvoicePath(updateUrl);
                if (isSyn) {
                    receiveBillInvoiceModel.setSynchronizerId(user.getId());
                    receiveBillInvoiceModel.setSynchronizer(user.getName());
                }
                receiveBillInvoiceDao.modify(receiveBillInvoiceModel);
            }
        }

        /**4.作废通过：
         * 1). 删除唯一表数据(销售订单不唯一不用删除)
         * 2).若开票，则作废发票；
         * 3).若已同步，则触发NC作废状态接口，对应接口为：4.8 结算账单红冲/作废接口*/
        if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_4.equals(operateNode)) {
            //1). 删除唯一表数据(销售订单不唯一不用删除)
            List<Long> externalCodeIds = new ArrayList<>();
            if (StringUtils.isNotBlank(receiveBillModel.getOrderType()) && !DERP_ORDER.RECEIVEBILL_ORDERTYPE_4.equals(receiveBillModel.getOrderType())) {
                List<String> relCodes = Arrays.asList(receiveBillModel.getRelCode().split("&"));
                for (String relCode : relCodes) {
                    ReceiveExternalCodeModel externalCodeModel = new ReceiveExternalCodeModel();
                    externalCodeModel.setExternalCode(relCode);
                    externalCodeModel.setBuId(receiveBillModel.getBuId());
                    externalCodeModel.setOrderType(receiveBillModel.getOrderType());
                    externalCodeModel.setCurrency(receiveBillModel.getCurrency());
                    ReceiveExternalCodeModel exist = receiveExternalCodeDao.searchByModel(externalCodeModel);
                    if (exist != null) {
                        externalCodeIds.add(exist.getId());
                    }
                }
                if(externalCodeIds.size()>0){
                    receiveExternalCodeDao.delete(externalCodeIds);
                }
            }

            //2).更新账单出库单关联平台费用单单据的状态
            List<Long> platformOrderIds = new ArrayList<>();
            if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_2.equals(receiveBillModel.getOrderType())) {
                PlatformCostOrderModel platformCostOrderModel = new PlatformCostOrderModel();
                platformCostOrderModel.setBillCode(receiveBillModel.getRelCode());
                platformCostOrderModel.setBuId(receiveBillModel.getBuId());
                platformCostOrderModel.setStatus(DERP_ORDER.PLATFORM_COST_ORDER_STATUS_3);
                List<PlatformCostOrderModel> platformCostOrderModels = platformCostOrderDao.list(platformCostOrderModel);
                for (PlatformCostOrderModel pModel : platformCostOrderModels) {
                    platformOrderIds.add(pModel.getId());
                }
            }
            if (!platformOrderIds.isEmpty()) {
                PlatformCostOrderModel platformCostOrderModel = new PlatformCostOrderModel();
                platformCostOrderModel.setStatus(DERP_ORDER.PLATFORM_COST_ORDER_STATUS_2);
                platformCostOrderModel.setTransferSlipDate(null);
                platformCostOrderModel.setTransferSliper(null);
                platformCostOrderModel.setTransferSlipName(null);
                platformCostOrderDao.batchUpdateOrderStatus(platformCostOrderModel, platformOrderIds);
            }
            if (!DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10.equals(receiveBillModel.getNcStatus())) {
                isSyn = true;
            }

            //3).删除预收核销
            ReceiveBillVerifyItemModel verifyItemModel = new ReceiveBillVerifyItemModel();
            verifyItemModel.setBillId(form.getBillId());
            verifyItemModel.setType(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_1);
            receiveBillVerifyItemDao.delVerify(verifyItemModel);

            //4).更新应收发票状态为“已作废”，并在对应发票增加已作废图标
            if (invoiceModel != null) {

                //在发票文件增加已作废图标
                String updateUrl = addWatermark(invoiceModel.getInvoiceNo(), user, invoiceModel.getInvoicePath());

                ReceiveBillInvoiceModel receiveBillInvoiceModel = new ReceiveBillInvoiceModel();
                receiveBillInvoiceModel.setId(receiveBillModel.getInvoiceId());
                receiveBillInvoiceModel.setStatus(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_02);
                receiveBillInvoiceModel.setInvoicePath(updateUrl);
                if (isSyn) {
                    receiveBillInvoiceModel.setSynchronizerId(user.getId());
                    receiveBillInvoiceModel.setSynchronizer(user.getName());
                }
                receiveBillInvoiceDao.modify(receiveBillInvoiceModel);
            }

            //5).已同步——触发NC作废状态接口推送，对应接口为：4.8 结算账单红冲/作废接口
            if (!DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10.equals(receiveBillModel.getNcStatus())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                ReceiveHcInvalidRoot invalidRoot = new ReceiveHcInvalidRoot();
                invalidRoot.setConfirmBillId(receiveBillModel.getCode());
                invalidRoot.setSourceCode(ApolloUtil.ncSourceType);
                invalidRoot.setState("1");
                invalidRoot.setCancelTime(sdf.format(auditTime));
                invalidRoot.setRemark(form.getAuditRemark());
                JSONObject json = JSONObject.fromObject(invalidRoot);
                String clearText = json.toString();
                //提交NC
                String ncResult = NcClientUtils.sendNc(ApolloUtil.ncApi + NcAPIEnum.NcApi_08.getUri(), clearText);

                JSONObject resultJson = JSONObject.fromObject(ncResult);
                if (resultJson.containsKey("code") && resultJson.getString("code").equals("1002")) {
                    throw new RuntimeException(resultJson.getString("msg"));
                }
                //4.10 发票更新接口
                if (relCodeList.size() > 0) {
                    for (String relCode : relCodeList) {
                        InvoiceUpdateRoot updateRoot = new InvoiceUpdateRoot();
                        updateRoot.setSourceCode(ApolloUtil.ncSourceType);
                        updateRoot.setConfirmBillId(relCode);
                        List<Details> detailsList = new ArrayList<>();
                        Details details = new Details();
                        details.setSindex("1");
                        details.setInvoiceCode(invoiceModel.getInvoiceNo());
                        details.setType("3"); //1=蓝票，2=红冲，3=作废
                        details.setInvoiceDate(dateFormat.format(invoiceModel.getInvoiceDate()));
                        detailsList.add(details);
                        updateRoot.setDetails(detailsList);
                        JSONObject updateJson = JSONObject.fromObject(updateRoot);
                        String updateText = updateJson.toString();
                        //提交NC
                        String updateResult = NcClientUtils.sendNc(ApolloUtil.ncApi + NcAPIEnum.NcApi_10.getUri(), updateText);

                        JSONObject updateResultJson = JSONObject.fromObject(updateResult);
                        if (updateResultJson.containsKey("code") && updateResultJson.getString("code").equals("1002")) {
                            throw new RuntimeException(updateResultJson.getString("msg"));
                        }
                    }
                }
            }

        }

        //5.审核通过：触发收款核销跟踪
        if (DERP_ORDER.RECEIVEBILLAUDIT_AUDITRESULT_00.equals(form.getAuditResult())) {
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("receiveCodes", receiveBillModel.getCode());

            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_1.equals(operateNode)) {
                body.put("orderType", DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_0);
                body.put("operateType", "0");
                body.put("notes", "应收账单审核");
            }

            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_4.equals(operateNode)) {
                body.put("orderType", DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_0);
                body.put("operateType", "1");
                body.put("notes", "应收账单作废审核通过");
            }

            JSONObject json = JSONObject.fromObject(body);
            rocketMQProducer.send(json.toString(), MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTopic(), MQOrderEnum.RECEIVE_BILL_VERIFICATION.getTags());
        }


        //6.发送邮件
        itemPrice = itemPrice.setScale(2, BigDecimal.ROUND_HALF_UP);

        //发送审核完成提醒
        ReceiveBillOperateModel operateModel = null;
        if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_1.equals(operateNode) ||
                DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_2.equals(operateNode)) {
            operateModel = receiveBillOperateDao.getLatestOperate(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_0, Arrays.asList(form.getBillId()));
        } else {
            operateModel = receiveBillOperateDao.getLatestOperate(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_3, Arrays.asList(form.getBillId()));
        }

        //封装发送邮件JSON
        ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

        emailUserDTO.setBuId(receiveBillModel.getBuId());
        emailUserDTO.setBuName(receiveBillModel.getBuName());
        emailUserDTO.setMerchantId(receiveBillModel.getMerchantId());
        emailUserDTO.setMerchantName(receiveBillModel.getMerchantName());
        emailUserDTO.setOrderCode(receiveBillModel.getCode());
        emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1);
        emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1));
        emailUserDTO.setSupplier(receiveBillModel.getCustomerName());
        emailUserDTO.setAmount(receiveBillModel.getCurrency() + "&nbsp;" + itemPrice.toPlainString());
        emailUserDTO.setAuditorId(user.getId());
        emailUserDTO.setUserName(user.getName());
        emailUserDTO.setCreateId(receiveBillModel.getCreaterId());


        if (isSubmitAudit) {
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_2);
        } else {//发送作废完成提醒
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_6);
        }

        if (DERP_ORDER.RECEIVEBILLAUDIT_AUDITRESULT_01.equals(form.getAuditResult())) {
            emailUserDTO.setStatus("已驳回");
        } else {
            emailUserDTO.setStatus("已通过");
        }

        JsonConfig jsonConfig = new JsonConfig();
        getReceiveOperators(receiveBillModel.getId(), emailUserDTO, jsonConfig);

        JSONObject emailJson = JSONObject.fromObject(emailUserDTO, jsonConfig);

        rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                MQErpEnum.SEND_REMINDER_MAIL.getTags());

        //仅对“上架单”、“账单出库单”、“销售订单”、“销售退款单”创建的应收单审核通过时，进行核销ToB暂估应收、暂估返利，非此类单据创建的应收单均不做核销处理；
        if ((DERP_ORDER.RECEIVEBILL_ORDERTYPE_1.equals(receiveBillModel.getOrderType()) ||
                DERP_ORDER.RECEIVEBILL_ORDERTYPE_2.equals(receiveBillModel.getOrderType()) ||
                DERP_ORDER.RECEIVEBILL_ORDERTYPE_7.equals(receiveBillModel.getOrderType()) ||
                DERP_ORDER.RECEIVEBILL_ORDERTYPE_4.equals(receiveBillModel.getOrderType())) &&
                DERP_ORDER.RECEIVEBILLAUDIT_AUDITRESULT_00.equals(form.getAuditResult())) {
            Map<String, Object> verifyBody = new HashMap<String, Object>();
            verifyBody.put("receiveCode", receiveBillModel.getCode());
            verifyBody.put("sourceType", "2");
            verifyBody.put("auditType", auditType);

            JSONObject verifyJson = JSONObject.fromObject(verifyBody);
            rocketMQProducer.send(verifyJson.toString(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTags());

        }

        retMap.put("code", "00");
        retMap.put("message", "成功");
        return retMap;
    }

    @Override
    public Map<String, String> refreshReceiveBill(Long id, User user) throws Exception {
        Map<String, String> result = new HashMap<>();

        ReceiveBillModel receiveBillModel = receiveBillDao.searchById(id);
        if (StringUtils.isBlank(receiveBillModel.getOrderType())) {
            result.put("code", "01");
            result.put("message", "手工创建的应收账单不能刷新");
            return result;
        }
        if (!DERP_ORDER.RECEIVEBILL_BILLSTATUS_00.equals(receiveBillModel.getBillStatus())) {
            result.put("code", "01");
            result.put("message", "此账单账单状态不是待提交状态，不能刷新");
            return result;
        }

        Map<String, Object> receiveMap = new HashMap<String, Object>();
        receiveMap.put("orderType", receiveBillModel.getOrderType());
        receiveMap.put("merchantId", user.getMerchantId());
        receiveMap.put("merchantName", user.getMerchantName());
        receiveMap.put("currency", receiveBillModel.getCurrency());
        receiveMap.put("buId", receiveBillModel.getBuId());
        receiveMap.put("billCode", receiveBillModel.getCode());
        receiveMap.put("relCodes", receiveBillModel.getRelCode());
        receiveMap.put("userId", user.getId());
        receiveMap.put("userName", user.getName());
        receiveMap.put("isAddWorn", receiveBillModel.getIsAddWorn());
        receiveMap.put("billId", receiveBillModel.getId());

        JSONObject receiveJson = JSONObject.fromObject(receiveMap);
        SendResult send = rocketMQProducer.send(receiveJson.toString(), MQOrderEnum.RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.RECEIVE_BILL_GENERATE.getTags());
        System.out.println(send);

        result.put("code", "00");
        result.put("billId", receiveBillModel.getId().toString());
        result.put("message", "保存成功");
        return result;
    }

    @Override
    public boolean delReceiveBill(List<Long> ids) throws Exception {
        List<Long> platformOrderIds = new ArrayList<>();
        List<Long> externalCodeIds = new ArrayList<>();
        for (Long id : ids) {
            // 根据id获取信息
            ReceiveBillModel model = receiveBillDao.searchById(id);
            // 判断状态是否待提交
            if (!model.getBillStatus().equals(DERP_ORDER.RECEIVEBILL_BILLSTATUS_00)) {
                throw new RuntimeException("应收账单：" + model.getCode() + "不是待提交状态，不能删除");
            }
            if (StringUtils.isNotBlank(model.getRelCode())) {
                PlatformCostOrderModel platformCostOrderModel = new PlatformCostOrderModel();
                platformCostOrderModel.setBillCode(model.getRelCode());
                platformCostOrderModel.setBuId(model.getBuId());
                platformCostOrderModel.setStatus(DERP_ORDER.PLATFORM_COST_ORDER_STATUS_3);
                List<PlatformCostOrderModel> platformCostOrderModels = platformCostOrderDao.list(platformCostOrderModel);
                for (PlatformCostOrderModel pModel : platformCostOrderModels) {
                    platformOrderIds.add(pModel.getId());
                }
                String relCodes = model.getRelCode();
                List<String> relCodeList = Arrays.asList(relCodes.split("&"));
                for (String rel : relCodeList) {
                    ReceiveExternalCodeModel externalCodeModel = new ReceiveExternalCodeModel();
                    externalCodeModel.setExternalCode(rel);
                    externalCodeModel.setBuId(model.getBuId());
                    externalCodeModel.setOrderType(model.getOrderType());
                    externalCodeModel.setCurrency(model.getCurrency());
                    externalCodeModel = receiveExternalCodeDao.searchByModel(externalCodeModel);
                    if (externalCodeModel != null) {
                        externalCodeIds.add(externalCodeModel.getId());
                    }
                }
            }
        }
        int num = receiveBillDao.delete(ids);
        if (num > 0) {
            for (Long id : ids) {
                receiveBillItemDao.delItems(id, null);
                receiveBillCostItemDao.delCostItem(id);

                ReceiveBillVerifyItemModel verifyItemModel = new ReceiveBillVerifyItemModel();
                verifyItemModel.setBillId(id);
                receiveBillVerifyItemDao.delVerify(verifyItemModel);

            }
            if (!platformOrderIds.isEmpty()) {
                PlatformCostOrderModel model = new PlatformCostOrderModel();
                model.setStatus(DERP_ORDER.PLATFORM_COST_ORDER_STATUS_2);
                model.setTransferSlipDate(null);
                model.setTransferSliper(null);
                model.setTransferSlipName(null);
                platformCostOrderDao.batchUpdateOrderStatus(model, platformOrderIds);
            }
            if (externalCodeIds.size() > 0) {
                receiveExternalCodeDao.delete(externalCodeIds);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> listItemInfos(List<Long> ids, Long merchantId, User user) throws Exception {
        List<Map<String, Object>> mapList = new LinkedList<>();
        List<Map<String, Object>> noGoodsNoMapList = new LinkedList<>();
        BigDecimal totalAllAmount = new BigDecimal("0");
        BigDecimal totalAllNum = new BigDecimal("0");

        Map<String, Map<String, Object>> invoiceMap = new HashMap<>();
        //应收明细
        List<Map<String, Object>> listInvoiceItem = receiveBillItemDao.listInvoiceItems(ids);
        mergeItemByKey(listInvoiceItem, invoiceMap, false, user);

        //费用明细
        List<Map<String, Object>> listInvoiceCostItem = receiveBillCostItemDao.listInvoiceCostItem(ids);
        mergeItemByKey(listInvoiceCostItem, invoiceMap, false, user);

        int index = 1;
        for (String key : invoiceMap.keySet()) {
            Map<String, Object> map = invoiceMap.get(key);
            String goodsNo = (String) map.get("goodsNo");
            BigDecimal totalPrice = (BigDecimal) map.get("totalPrice");
            BigDecimal totalNum = (BigDecimal) map.get("totalNum");
            BigDecimal price = (BigDecimal) map.get("price");

            map.put("totalPrice", "-");
            map.put("price", "-");
            map.put("totalNum", "-");
            if (totalPrice != null) {
                totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
                map.put("totalPrice", StrUtils.doubleFormatString(totalPrice.doubleValue()));
                totalAllAmount = totalAllAmount.add(totalPrice);
            }

            if (price != null) {
                map.put("price", StrUtils.doubleFormatString(price.doubleValue()));
            }

            if (totalNum != null) {
                map.put("totalNum", StrUtils.intFormatString(totalNum.intValue()));
                totalAllNum = totalAllNum.add(totalNum);
            }

            if (StringUtils.isNotBlank(goodsNo)) {
                map.put("index", index);
                mapList.add(map);
                index++;
            } else {
                noGoodsNoMapList.add(map);
            }
        }

        for (Map<String, Object> map : noGoodsNoMapList) {
            map.put("index", index);
            mapList.add(map);
            index++;
        }
        totalAllAmount = totalAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("totalAllAmount", StrUtils.doubleFormatString(totalAllAmount.doubleValue()));
        totalMap.put("totalAllNum", StrUtils.intFormatString(totalAllNum.intValue()));
        mapList.add(totalMap);
        return mapList;
    }

    /**
     * 根据sku/发票描述/费项+po合并明细
     **/
    private void mergeItemByKey(List<Map<String, Object>> itemList, Map<String, Map<String, Object>> keyMap, boolean isGroupBrand, User user) throws Exception {
        /**1）若应收单中存在商品SKU，先以SKU维度汇总金额显示发票金额；
         （2）若无商品SKU的，先看“发票描述”字段（本期补扣款页面新增字段）是否有值，有值则按照相同发票描述做应收与费用的正负相抵，汇总金额显示；
         （3）若无发票描述，再以相应的”费项名称+PO“做金额显示*/
        for (Map<String, Object> map : itemList) {
            String key = null;
            BigDecimal totalPrice = (BigDecimal) map.get("totalPrice");
            BigDecimal totalNum = (BigDecimal) map.get("totalNum");
            Long goodsId = (Long) map.get("goodsId");
            String goodsNo = (String) map.get("goodsNo");
            String platformSku = null;
            if (map.containsKey("platformSku")) {
                platformSku = (String) map.get("platformSku");
            }
            String invoiceDescription = (String) map.get("invoiceDescription");
            Long projectId = (Long) map.get("projectId");
            String poNo = (String) map.get("poNo");
            Long parentBrandId = (Long) map.get("parentBrandId");

            if (isGroupBrand && parentBrandId != null) {
                Map<String, Object> brandParam = new HashMap<>();
                brandParam.put("brandSuperiorId", parentBrandId);
                BrandSuperiorMongo brandSuperiorMongo = brandSuperiorMongoDao.findOne(brandParam);
                if (brandSuperiorMongo != null) {
                    map.put("parentBrandName", brandSuperiorMongo.getName());
                }
            }

            if (StringUtils.isNotBlank(goodsNo)) {
                if (isGroupBrand) {
                    key = goodsNo + "_" + platformSku + "_" + parentBrandId;
                } else {
                    key = goodsNo + "_" + platformSku;
                }
                Map<String, Object> goodsParam = new HashMap<>();
                goodsParam.put("goodsNo", goodsNo);
                goodsParam.put("merchantId", user.getMerchantId());
                MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(goodsParam);
                if (merchandiseInfoMogo != null) {
                    map.put("barcode", merchandiseInfoMogo.getBarcode());
                    map.put("goodsName", merchandiseInfoMogo.getName());
                    if (merchandiseInfoMogo.getProductId() != null) {
                        Map<String, Object> productParam = new HashMap<>();
                        productParam.put("productId", merchandiseInfoMogo.getProductId());
                        ProductInfoMongo productInfoMongo = productInfoMongoDao.findOne(productParam);
                        if (productInfoMongo != null && productInfoMongo.getUnit() != null && productInfoMongo.getUnit() != 0) {
                            Map<String, Object> unitParam = new HashMap<>();
                            unitParam.put("unitId", productInfoMongo.getUnit());
                            UnitMongo unitMongo = unitMongoDao.findOne(unitParam);
                            map.put("unit", unitMongo.getName());
                        }
                    }
                }
                BrandMongo brand = brandMongoDao.getBrandMongo(goodsId);
                if (brand != null) {
                    map.put("brandId", brand.getBrandId());
                    map.put("brandName", brand.getName());
                }
                if (StringUtils.isNotBlank(platformSku)) {
                    map.put("goodsNo", platformSku);
                }
            } else {
                String description = null;
                if (StringUtils.isNotBlank(invoiceDescription)) {
                    key = invoiceDescription;
                    description = invoiceDescription;
                } else {
                    key = projectId + "_" + poNo;
                    SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(projectId);
                    description = settlementConfigModel.getProjectName();
                    if (StringUtils.isNotBlank(poNo)) {
                        description = description + "_" + poNo;
                    }
                }
                if (isGroupBrand) {
                    key = key + "_" + parentBrandId;
                }
                map.put("goodsNo", "");
                map.put("barcode", "");
                map.put("goodsName", description);
                map.put("unit", "");
                map.put("brandId", "");
                map.put("brandName", "");
            }

            if (keyMap.containsKey(key)) {
                Map<String, Object> existMap = keyMap.get(key);
                BigDecimal existPrice = (BigDecimal) existMap.get("totalPrice");
                BigDecimal existNum = (BigDecimal) existMap.get("totalNum");
                if (existPrice != null) {
                    if (totalPrice != null) {
                        totalPrice = totalPrice.add(existPrice);
                    } else {
                        totalPrice = existPrice;
                    }
                }

                if (existNum != null) {
                    if (totalNum != null) {
                        totalNum = totalNum.add(existNum);
                    } else {
                        totalNum = existNum;
                    }
                }

            }
            if (totalNum != null && totalPrice != null && totalNum.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal price = totalPrice.divide(totalNum, 2, BigDecimal.ROUND_HALF_UP);
                map.put("price", price);
                totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
            } else {
                map.put("price", null);
            }

            map.put("totalPrice", totalPrice);
            map.put("totalNum", totalNum);
            keyMap.put(key, map);
        }
    }

    @Override
    public MerchantInfoMongo getMerchantInfo(Long merchantId) throws SQLException {
        Map<String, Object> param = new HashMap<>();
        param.put("merchantId", merchantId);
        return merchantInfoMongoDao.findOne(param);
    }

    @Override
    public CustomerInfoMogo getCustomer(Long id) throws SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("customerid", id);
        params.put("cusType", "1");
        CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(params);
        return customerInfo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void modifyJSONContract(Map<String, Object> map, String invoiceSource) throws Exception {
        synchronized (this) {
            ByteArrayOutputStream bos = null;
            try {
                User user = ShiroUtils.getUser();
                Set<String> keys = map.keySet();
                JSONObject mongoJSON = new JSONObject();
                JSONObject dataJson = new JSONObject();
                JSONArray goodList = new JSONArray();
                JSONArray costList = new JSONArray();
                for (String key : keys) {
                    if (key.indexOf(".") > -1) {
                        String[] org_param = key.split("\\.");
                        if (org_param[0].indexOf("detail") > -1) {
                            dataJson.put(org_param[1], map.get(key));
                        } else if (org_param[0].indexOf("goods") > -1) {
                            List<String> vals = (ArrayList<String>) map.get(key);
                            if (vals.isEmpty()) {
                                continue;
                            }
                            for (int i = 0; i < vals.size(); i++) {
                                JSONObject goods = null;
                                if (goodList.isEmpty() || goodList.size() < vals.size()) {
                                    goods = new JSONObject();
                                    goods.put(org_param[1], toNullStrWithReplace(vals.get(i)));
                                    goodList.add(goods);
                                } else {
                                    goods = (JSONObject) goodList.get(i);
                                    goods.put(org_param[1], toNullStrWithReplace(vals.get(i)));
                                }
                            }
                        } else if (org_param[0].indexOf("costItem") > -1) {
                            List<String> vals = (ArrayList<String>) map.get(key);
                            if (vals.isEmpty()) {
                                continue;
                            }
                            for (int i = 0; i < vals.size(); i++) {
                                JSONObject goods = null;
                                if (costList.isEmpty() || costList.size() < vals.size()) {
                                    goods = new JSONObject();
                                    goods.put(org_param[1], toNullStrWithReplace(vals.get(i)));
                                    costList.add(goods);
                                } else {
                                    goods = (JSONObject) costList.get(i);
                                    goods.put(org_param[1], toNullStrWithReplace(vals.get(i)));
                                }
                            }
                        }
                    } else {
                        mongoJSON.put(key, toNullStrWithReplace(map.get(key)));
                    }
                }
                Map<String, Double> costMap = new LinkedHashMap<>();
                for (int i = 0; i < costList.size(); i++) {
                    JSONObject cost = (JSONObject) costList.get(i);
                    String projectName = cost.getString("projectName");
                    Double totalPrice = cost.getDouble("totalPrice");
                    if (costMap.containsKey(projectName)) {
                        Double price = costMap.get(projectName);
                        costMap.put(projectName, price + totalPrice);
                    } else {
                        costMap.put(projectName, totalPrice);
                    }
                }
                JSONArray costListMerge = new JSONArray();
                for (String key : costMap.keySet()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("projectName", key);
                    jsonObject.put("totalPrice", costMap.get(key));
                    costListMerge.add(jsonObject);
                }
                String ids = (String) dataJson.get("ids");
                String currency = (String) dataJson.get("currency");
                String codes = (String) dataJson.get("codes");
                String fileTempCode = (String) dataJson.get("fileTempCode");
                String customerName = (String) dataJson.get("customerName");
                String customerId = (String) dataJson.get("customerId");
                String totalAllAmount = (String) dataJson.get("totalAllAmount");
                double totalAllAmountDouble = new DecimalFormat().parse(totalAllAmount).doubleValue();
                //生成发票编码
                Calendar calendar = Calendar.getInstance();
                String year = String.valueOf(calendar.get(Calendar.YEAR));
                int month = calendar.get(Calendar.MONTH) + 1;
                String monthStr = month > 9 ? String.valueOf(month) : "0" + month;
                String invoiceNoPrefix = null;
                if ("0000138".equals(user.getTopidealCode())) { //宝信
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_HNFH + year.substring(2);
                } else if ("1000000204".equals(user.getTopidealCode())) { //健太
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_QTOP + year.substring(2);
                } else if ("0000134".equals(user.getTopidealCode())) { //卓烨
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_TWKL + year.substring(2);
                } else if ("1000004669".equals(user.getTopidealCode())) { //元森泰
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_YSTA + year.substring(2);
                } else if ("1000000645".equals(user.getTopidealCode())) { //广旺
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_ABHG + year.substring(2);
                } else if ("1000000626".equals(user.getTopidealCode())) { //润佰
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_RYBZ + year.substring(2);
                } else if ("1000005377".equals(user.getTopidealCode())) { //万代
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_WAMD + year.substring(2);
                } else if ("1000052958".equals(user.getTopidealCode())) { //轩盛有限公司
                    invoiceNoPrefix = DERP.UNIQUEID_PREFIX_HIGH + year.substring(2);
                }
                Long invoiceValue = receiveInvoicenoDao.getMaxValue(invoiceNoPrefix);
                if (invoiceValue == null) {
                    invoiceValue = 1L;
                } else {
                    invoiceValue++;
                }
                String invoiceValueStr = String.format("%04d", invoiceValue);
                String invoiceNo = invoiceNoPrefix + monthStr + invoiceValueStr;
                //保存发票号当前值
                ReceiveInvoicenoModel receiveInvoicenoModel = new ReceiveInvoicenoModel();
                receiveInvoicenoModel.setInvoiceNoPrefix(invoiceNoPrefix);
                receiveInvoicenoModel.setValue(invoiceValue);
                receiveInvoicenoDao.save(receiveInvoicenoModel);

                dataJson.put("invoiceNo", invoiceNo);
                dataJson.put("goodsList", goodList);
                dataJson.put("costList", costListMerge);
                mongoJSON.put("dataJson", dataJson.toString());
                String[] codeArr = codes.split(",");

                for (String code : codeArr) {
                    mongoJSON.put("code", code);
                    mongoJSON.put("fileTempCode", fileTempCode);
                    Map<String, Object> removeMap = new HashMap<String, Object>();
                    removeMap.put("code", code);
                    fileTempDataMongoDao.remove(removeMap);
                    fileTempDataMongoDao.insertJson(mongoJSON.toString());
                }
                //生成发票文件
                bos = exportTempDateFile(codeArr[0], fileTempCode);
                AttachmentInfoMongo invoiceAttach = saveAttachmentFile(bos, invoiceNo, user);

                Timestamp invoiceDate = TimeUtils.getNow();
                ReceiveBillInvoiceModel receiveBillInvoiceModel = new ReceiveBillInvoiceModel();
                receiveBillInvoiceModel.setMerchantId(user.getMerchantId());
                receiveBillInvoiceModel.setMerchantName(user.getMerchantName());
                receiveBillInvoiceModel.setCreaterId(user.getId());
                receiveBillInvoiceModel.setCreater(user.getName());
                receiveBillInvoiceModel.setCurrency(currency);

                receiveBillInvoiceModel.setInvoiceNo(invoiceNo);
                receiveBillInvoiceModel.setCustomerId(Long.valueOf(customerId));
                receiveBillInvoiceModel.setCustomerName(customerName);
                receiveBillInvoiceModel.setInvoicePath(invoiceAttach.getAttachmentUrl());
                receiveBillInvoiceModel.setInvoiceAmount(new BigDecimal(String.valueOf(totalAllAmountDouble)));
                receiveBillInvoiceModel.setInvoiceDate(invoiceDate);
                receiveBillInvoiceModel.setStatus(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_01);
                receiveBillInvoiceModel.setInvoiceType(DERP_ORDER.RECEIVEBILLINVOICE_INVOICETYPE_0);
                if (DERP_ORDER.RECEIVEINVOICE_SOURCE_1.equals(invoiceSource)) {
                    receiveBillInvoiceModel.setInvoiceRelCodes(codes);
                    receiveBillInvoiceModel.setInvoiceRelIds(ids);
                } else {
                    receiveBillInvoiceModel.setRelStatementCodes(codes);
                }
                Long id = receiveBillInvoiceDao.save(receiveBillInvoiceModel);
                for (String code : codeArr) {
                    /*mongoJSON.put("code", code);
                    mongoJSON.put("fileTempCode", fileTempCode);
                    Map<String, Object> removeMap = new HashMap<String, Object>();
                    removeMap.put("code", code) ;
                    fileTempDataMongoDao.remove(removeMap);
                    fileTempDataMongoDao.insertJson(mongoJSON.toString());*/
                    if (DERP_ORDER.RECEIVEINVOICE_SOURCE_1.equals(invoiceSource)) {
                        ReceiveBillModel receiveBillModel = new ReceiveBillModel();
                        receiveBillModel.setCode(code);
                        ReceiveBillModel model = receiveBillDao.searchByModel(receiveBillModel);
                        model.setInvoiceId(id);
                        model.setInvoiceStatus(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_01);
                        receiveBillDao.modify(model);

                        //操作日志节点
                        ReceiveBillOperateModel receiveBillOperateModel = new ReceiveBillOperateModel();
                        receiveBillOperateModel.setBillId(model.getId());
                        receiveBillOperateModel.setOperateDate(invoiceDate);
                        receiveBillOperateModel.setCreateDate(invoiceDate);
                        receiveBillOperateModel.setOperateId(user.getId());
                        receiveBillOperateModel.setOperator(user.getName());
                        receiveBillOperateModel.setOperateNode(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_6);
                        receiveBillOperateDao.save(receiveBillOperateModel);

                    } else {
                        PlatformStatementOrderModel orderModel = new PlatformStatementOrderModel();
                        orderModel.setBillCode(code);
                        PlatformStatementOrderModel platformStatement = platformStatementOrderDao.searchByModel(orderModel);
                        platformStatement.setIsInvoice(DERP_ORDER.PLATFORM_STATEMENT_IS_INVOICE_1);
                        platformStatement.setInvoiceDate(receiveBillInvoiceModel.getInvoiceDate());
                        platformStatement.setInvoiceDrawer(user.getName());
                        platformStatement.setInvoiceDrawerId(user.getId());
                        platformStatement.setInvoiceNo(invoiceNo);
                        platformStatement.setInvoiceId(id);
                        platformStatementOrderDao.modify(platformStatement);
                    }
                }

                //应收单确认开票和平台结算单确认开票 即触发邮件提醒；
                net.sf.json.JSONArray attArray = new net.sf.json.JSONArray();
                for (String code : codeArr) {
                    Map<String, Object> attQueryMap = new HashMap<String, Object>();
                    attQueryMap.put("relationCode", code);
                    attQueryMap.put("creatorName", "账单同步");
                    List<AttachmentInfoMongo> attList = attachmentInfoMongoDao.findAll(attQueryMap);
                    for (AttachmentInfoMongo att : attList) {
                        if (!DERP.DEL_CODE_006.equals(att.getStatus())) {
                            Map<String, Object> tempMap = new HashMap<String, Object>();
                            tempMap.put("attachmentName", att.getAttachmentName());
                            String attachmentUrl = ApolloUtil.orderWebhost + "/attachment/downloadFile.asyn?fileName="
                                    + att.getAttachmentName() + "&url=" + URLEncoder.encode(att.getAttachmentUrl());
                            tempMap.put("attachmentUrl", attachmentUrl);
                            JSONObject attJson = new JSONObject();
                            attJson.putAll(tempMap);
                            attArray.add(attJson);
                        }
                    }
                }
                Map<String, Object> invoiceMap = new HashMap<String, Object>();
                invoiceMap.put("attachmentName", invoiceNo + "应收账单发票.pdf");
                String invoiceUrl = ApolloUtil.orderWebhost + "/attachment/downloadFile.asyn?fileName="
                        + invoiceAttach.getAttachmentName() + "&url=" + URLEncoder.encode(invoiceAttach.getAttachmentUrl());
                invoiceMap.put("attachmentUrl", invoiceUrl);
                JSONObject attJson = new JSONObject();
                attJson.putAll(invoiceMap);
                attArray.add(attJson);
                ReceiveBillModel receiveBillModel = new ReceiveBillModel();
                receiveBillModel.setCode(codeArr[0]);
                ReceiveBillModel receiveBill = receiveBillDao.searchByModel(receiveBillModel);

                if (DERP_ORDER.RECEIVEINVOICE_SOURCE_1.equals(invoiceSource)) {
                    //封装发送邮件JSON
                    ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

                    emailUserDTO.setBuId(receiveBill.getBuId());
                    emailUserDTO.setBuName(receiveBill.getBuName());
                    emailUserDTO.setMerchantId(user.getMerchantId());
                    emailUserDTO.setMerchantName(user.getMerchantName());
                    emailUserDTO.setOrderCode(invoiceNo);
                    emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1);
                    emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                            DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1));
                    emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_5);
                    emailUserDTO.setSupplier(customerName);
                    emailUserDTO.setAmount(currency + "&nbsp;" + totalAllAmount);
                    emailUserDTO.setDrawerId(user.getId());
                    emailUserDTO.setUserName(user.getName());
                    emailUserDTO.setAttArray(attArray);
                    JsonConfig jsonConfig = new JsonConfig();
                    getReceiveOperators(receiveBillModel.getId(), emailUserDTO, jsonConfig);
                    JSONObject emailJson = JSONObject.fromObject(emailUserDTO, jsonConfig);

                    rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                            MQErpEnum.SEND_REMINDER_MAIL.getTags());
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            } finally {
                bos.close();
            }
        }
    }

    @Override
    public List<String> getCodes(List<Long> ids) throws SQLException {
        List<String> codes = new ArrayList<>();
        for (Long id : ids) {
            ReceiveBillModel model = receiveBillDao.searchById(id);
            codes.add(model.getCode());
        }
        return codes;
    }

    @Override
    public List<String> getRelCodes(List<Long> ids) throws SQLException {
        List<String> codes = new ArrayList<>();
        for (Long id : ids) {
            ReceiveBillModel model = receiveBillDao.searchById(id);
            if (!codes.contains(model.getRelCode())) {
                codes.add(model.getRelCode());
            }
        }
        return codes;
    }

    /**
     * 根据模版导出pdf
     */
    @SuppressWarnings("unchecked")
    @Override
    public ByteArrayOutputStream exportTempDateFile(String orderCode, String fileTempCode) throws Exception {

        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("code", orderCode);
        queryMap.put("fileTempCode", fileTempCode);
        FileTempDataMongo fileMongo = fileTempDataMongoDao.findOne(queryMap);

        String dataJson = fileMongo.getDataJson();
        Map<String, Object> dataMap = com.alibaba.fastjson.JSONObject.parseObject(dataJson, Map.class);
        FileTempModel fileTempModel = new FileTempModel();
        fileTempModel.setCode(fileTempCode);
        FileTempModel existFileTemp = fileTempDao.searchByModel(fileTempModel);

        /*
         * 获取文件模版
         */
        FileTempModel tempModel = new FileTempModel();
        tempModel.setCode(fileTempCode);
        tempModel = fileTempDao.searchByModel(tempModel);

        String contentBody = tempModel.getContentBody();

        contentBody = FreemakerUtils.formatReplacementHtml(contentBody);
        contentBody = FreemakerUtils.genHtmlStrWithTemplate(dataMap, contentBody);
        contentBody = FreemakerUtils.ConvertLineBreaks(contentBody);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        if (existFileTemp != null && existFileTemp.getToUrl().contains("receive-bill-weiping-invoice-details")) {
            String IMAGE_PATH = "/image/vip.png";
            bos = PdfUtils.html2PdfWithHeader(contentBody, IMAGE_PATH, PdfUtils.VERTICAL);
        } else {
            bos = PdfUtils.html2Pdf(contentBody, PdfUtils.VERTICAL);
        }
        return bos;
    }

    @Override
    public Map<String, String> validate(List<Long> ids) throws SQLException {
        Map<String, String> resultMap = new HashMap<>();
        Long customerId = null;
        String currency = null;
        for (int i = 0; i < ids.size(); i++) {
            ReceiveBillModel receiveBillModel = receiveBillDao.searchById(ids.get(i));
            if (!(receiveBillModel.getInvoiceStatus().equals(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_00) ||
                    receiveBillModel.getInvoiceStatus().equals(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_02))) {
                resultMap.put("code", "01");
                resultMap.put("msg", "开票的应收账单开票状态只能为“待开票”或“已作废");
                return resultMap;
            }
            if (ids.size() == 1) {
                if (receiveBillModel.getBillStatus().equals(DERP_ORDER.RECEIVEBILL_BILLSTATUS_05)
                        || receiveBillModel.getBillStatus().equals(DERP_ORDER.RECEIVEBILL_BILLSTATUS_06)) {
                    resultMap.put("code", "01");
                    resultMap.put("msg", "开票的应收账单状态不能为“作废待审”、“已作废”！");
                    return resultMap;
                }
            } else {
                if (!(receiveBillModel.getBillStatus().equals(DERP_ORDER.RECEIVEBILL_BILLSTATUS_02)
                        || receiveBillModel.getBillStatus().equals(DERP_ORDER.RECEIVEBILL_BILLSTATUS_03)
                        || receiveBillModel.getBillStatus().equals(DERP_ORDER.RECEIVEBILL_BILLSTATUS_04))) {
                    resultMap.put("code", "01");
                    resultMap.put("msg", "合并开票时账单状态需为“待核销”、“部分核销”、“已核销”！");
                    return resultMap;
                }
            }
            if (i == 0) {
                customerId = receiveBillModel.getCustomerId();
                currency = receiveBillModel.getCurrency();
            }
            if (!customerId.equals(receiveBillModel.getCustomerId())) {
                resultMap.put("code", "01");
                resultMap.put("msg", "仅能对同客户的应收账单进行同时合并开票");
                return resultMap;
            }

            if (!currency.equals(receiveBillModel.getCurrency())) {
                resultMap.put("code", "01");
                resultMap.put("msg", "仅能对同结算币种的应收账单进行同时合并开票");
                return resultMap;
            }
        }

        resultMap.put("code", "00");
        resultMap.put("msg", "校验通过");
        return resultMap;
    }

    @Override
    public List<Map<String, Object>> listWPItemInfos(List<Long> ids, Long merchantId) throws SQLException {
        List<Map<String, Object>> mapList = new LinkedList<>();
        BigDecimal totalAllAmount = new BigDecimal("0");
        BigDecimal anotherTotalAllAmount = new BigDecimal("0");
        BigDecimal customerReturnBrandAmount = new BigDecimal("0");
        BigDecimal promotionDiscountsBrandAmount = new BigDecimal("0");
        BigDecimal extraDiscountBrandAmount = new BigDecimal("0");
        BigDecimal promotionDiscountsCustBrandAmount = new BigDecimal("0");
        BigDecimal extraAmountBrandAmount = new BigDecimal("0");
        //费用项目按商品和费用项目维度统计
        List<Map<String, Object>> listInvoiceCostItem = receiveBillCostItemDao.listInvoiceCostItemByGoodsNo(ids, merchantId, "weiping");
        Map<String, Map<String, BigDecimal>> parentNameCostMap = new HashMap<>();
        for (Map<String, Object> map : listInvoiceCostItem) {
            BigDecimal totalPrice = (BigDecimal) map.get("totalPrice");
            String projectName = (String) map.get("projectName");
            if (!"客退补贴".equals(projectName) && !"活动折扣".equals(projectName) &&
                    !"补偿折扣".equals(projectName) && !"客退折让".equals(projectName) &&
                    !"返利金额".equals(projectName)) {
                continue;
            }
            String goodsNo = (String) map.get("goodsNo");
            String parentBrandCode = map.get("parentBrandCode") != null ? String.valueOf(map.get("parentBrandCode")) : "";
            String key = goodsNo + "__" + parentBrandCode;
            if (StringUtils.isNotBlank(key)) {
                Map<String, BigDecimal> costMap = new HashMap<>();
                if (parentNameCostMap.containsKey(key)) {
                    costMap.putAll(parentNameCostMap.get(key));
                }
                if ("客退补贴".equals(projectName)) {
                    costMap.put("customerReturn", totalPrice);
                } else if ("活动折扣".equals(projectName)) {
                    costMap.put("promotionDiscounts", totalPrice);
                } else if ("补偿折扣".equals(projectName)) {
                    costMap.put("extraDiscount", totalPrice);
                } else if ("客退折让".equals(projectName)) {
                    costMap.put("promotionDiscountsCust", totalPrice);
                } else if ("返利金额".equals(projectName)) {
                    costMap.put("extraAmount", totalPrice);
                }
                parentNameCostMap.put(key, costMap);
            }
            if (totalPrice != null) {
                anotherTotalAllAmount = anotherTotalAllAmount.add(totalPrice);
            }
        }

        List<Map<String, Object>> listInvoiceItem = receiveBillItemDao.listInvoiceItemGroupByParentBrand(ids);
        //已统计品牌对应的商品数量
        Map<String, List<Map<String, Object>>> existBrandMap = new HashMap<>();
        for (int i = 0; i < listInvoiceItem.size(); i++) {
            Map<String, Object> map = listInvoiceItem.get(i);
            BigDecimal totalPrice = map.get("totalPrice") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalPrice");
            BigDecimal totalNum = map.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalNum");
            String goodsNo = (String) map.get("goodsNo");
            String brandName = map.get("parentBrandCode") != null ? String.valueOf(map.get("parentBrandCode")) : "";
            if (totalNum.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal price = totalPrice.divide(totalNum, 4, BigDecimal.ROUND_HALF_UP);
                BigDecimal exactPrice = totalPrice.divide(totalNum, 8, BigDecimal.ROUND_HALF_UP);
                map.put("price", StrUtils.doubleFormatStringByFour(price.doubleValue()));
                map.put("exactPrice", exactPrice);
            }
            String key = goodsNo + "__" + brandName;
            if (parentNameCostMap.containsKey(key)) {
                if (parentNameCostMap.get(key).containsKey("customerReturn")) {
                    BigDecimal tempAmount = parentNameCostMap.get(key).get("customerReturn");
                    map.put("customerReturn", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (parentNameCostMap.get(key).containsKey("promotionDiscounts")) {
                    BigDecimal tempAmount = parentNameCostMap.get(key).get("promotionDiscounts");
                    map.put("promotionDiscounts", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (parentNameCostMap.get(key).containsKey("extraDiscount")) {
                    BigDecimal tempAmount = parentNameCostMap.get(key).get("extraDiscount");
                    map.put("extraDiscount", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (parentNameCostMap.get(key).containsKey("promotionDiscountsCust")) {
                    BigDecimal tempAmount = parentNameCostMap.get(key).get("promotionDiscountsCust");
                    map.put("promotionDiscountsCust", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (parentNameCostMap.get(key).containsKey("extraAmount")) {
                    BigDecimal tempAmount = parentNameCostMap.get(key).get("extraAmount");
                    map.put("extraAmount", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                parentNameCostMap.remove(key);
            } else {
                map.put("customerReturn", new BigDecimal("0"));
                map.put("promotionDiscounts", new BigDecimal("0"));
                map.put("extraDiscount", new BigDecimal("0"));
                map.put("promotionDiscountsCust", new BigDecimal("0"));
                map.put("extraAmount", new BigDecimal("0"));
            }
            map.put("totalPrice", totalPrice);
            if (existBrandMap.containsKey(brandName)) {
                existBrandMap.get(brandName).add(map);
            } else {
                List<Map<String, Object>> goodsList = new ArrayList<>();
                goodsList.add(map);
                existBrandMap.put(brandName, goodsList);
            }

            anotherTotalAllAmount = anotherTotalAllAmount.add(totalPrice);
        }

        Map<String, List<Map<String, Object>>> noExistBrandMap = new HashMap<>(); //未统计的品牌商品信息
        //遍历未统计的品牌商品信息并小计
        for (String key : parentNameCostMap.keySet()) {
            String[] keys = key.split("__");
            Map<String, Object> map = new HashMap<>();
            map.put("goodsNo", keys[0]);
            if (parentNameCostMap.get(key).containsKey("customerReturn")) {
                BigDecimal tempAmount = parentNameCostMap.get(key).get("customerReturn");
                map.put("customerReturn", tempAmount);
            }
            if (parentNameCostMap.get(key).containsKey("promotionDiscounts")) {
                BigDecimal tempAmount = parentNameCostMap.get(key).get("promotionDiscounts");
                map.put("promotionDiscounts", tempAmount);
            }
            if (parentNameCostMap.get(key).containsKey("extraDiscount")) {
                BigDecimal tempAmount = parentNameCostMap.get(key).get("extraDiscount");
                map.put("extraDiscount", tempAmount);
            }
            if (parentNameCostMap.get(key).containsKey("promotionDiscountsCust")) {
                BigDecimal tempAmount = parentNameCostMap.get(key).get("promotionDiscountsCust");
                map.put("promotionDiscountsCust", tempAmount);
            }
            if (parentNameCostMap.get(key).containsKey("extraAmount")) {
                BigDecimal tempAmount = parentNameCostMap.get(key).get("extraAmount");
                map.put("extraAmount", tempAmount);
            }

            String brandName = keys.length == 2 ? keys[1] : "";
            if (noExistBrandMap.containsKey(brandName)) {
                noExistBrandMap.get(brandName).add(map);
            } else {
                List<Map<String, Object>> goodsList = new ArrayList<>();
                goodsList.add(map);
                noExistBrandMap.put(brandName, goodsList);
            }
        }

        //遍历并合并未统计到的品牌信息
        for (String brandName : noExistBrandMap.keySet()) {
            BigDecimal totalBrandAmount = new BigDecimal("0");
            List<Map<String, Object>> goodsList = noExistBrandMap.get(brandName);
            if (existBrandMap.containsKey(brandName)) {
                List<Map<String, Object>> existGoodsList = existBrandMap.get(brandName);
                for (Map<String, Object> exist : existGoodsList) {
                    String goodsNo = (String) exist.get("goodsNo");
                    BigDecimal customerReturn = exist.get("customerReturn") == null ? new BigDecimal("0") : (BigDecimal) exist.get("customerReturn");
                    customerReturnBrandAmount = customerReturnBrandAmount.add(customerReturn);
                    BigDecimal promotionDiscounts = exist.get("promotionDiscounts") == null ? new BigDecimal("0") : (BigDecimal) exist.get("promotionDiscounts");
                    promotionDiscountsBrandAmount = promotionDiscountsBrandAmount.add(promotionDiscounts);
                    BigDecimal extraDiscount = exist.get("extraDiscount") == null ? new BigDecimal("0") : (BigDecimal) exist.get("extraDiscount");
                    extraDiscountBrandAmount = extraDiscountBrandAmount.add(extraDiscount);
                    BigDecimal promotionDiscountsCust = exist.get("promotionDiscountsCust") == null ? new BigDecimal("0") : (BigDecimal) exist.get("promotionDiscountsCust");
                    promotionDiscountsCustBrandAmount = promotionDiscountsCustBrandAmount.add(promotionDiscountsCust);
                    BigDecimal extraAmount = exist.get("extraAmount") == null ? new BigDecimal("0") : (BigDecimal) exist.get("extraAmount");
                    extraAmountBrandAmount = extraAmountBrandAmount.add(extraAmount);
                    BigDecimal totalPrice = (BigDecimal) exist.get("totalPrice");
                    totalBrandAmount = totalBrandAmount.add(totalPrice);
                    exist.put("customerReturn", StrUtils.doubleFormatString(customerReturn.doubleValue()));
                    exist.put("promotionDiscounts", StrUtils.doubleFormatString(promotionDiscounts.doubleValue()));
                    exist.put("extraDiscount", StrUtils.doubleFormatString(extraDiscount.doubleValue()));
                    exist.put("promotionDiscountsCust", StrUtils.doubleFormatString(promotionDiscountsCust.doubleValue()));
                    exist.put("extraAmount", StrUtils.doubleFormatString(extraAmount.doubleValue()));
                    exist.put("totalPrice", StrUtils.doubleFormatString(totalPrice.doubleValue()));
                    if (StringUtils.isNotBlank(goodsNo) && !goodsNo.equals("null")) {
                        Map<String, Object> merParams = new HashMap<>();
                        merParams.put("goodsNo", goodsNo);
                        merParams.put("merchantId", merchantId);
                        MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merParams);
                        exist.put("goodsName", merchandiseInfoMogo.getName());
                    }
                    mapList.add(exist);
                }
                existBrandMap.remove(brandName);
            }
            for (int i = 0; i < goodsList.size(); i++) {
                BigDecimal totalPrice = new BigDecimal("0");
                Map<String, Object> goodsMap = goodsList.get(i);
                String goodsNo = (String) goodsMap.get("goodsNo");
                if (goodsMap.containsKey("customerReturn")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("customerReturn");
                    goodsMap.put("customerReturn", StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    customerReturnBrandAmount = customerReturnBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsMap.containsKey("promotionDiscounts")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("promotionDiscounts");
                    goodsMap.put("promotionDiscounts", StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    promotionDiscountsBrandAmount = promotionDiscountsBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsMap.containsKey("extraDiscount")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("extraDiscount");
                    goodsMap.put("extraDiscount", StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    extraDiscountBrandAmount = extraDiscountBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsMap.containsKey("promotionDiscountsCust")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("promotionDiscountsCust");
                    goodsMap.put("promotionDiscountsCust", StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    promotionDiscountsCustBrandAmount = promotionDiscountsCustBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsMap.containsKey("extraAmount")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("extraAmount");
                    goodsMap.put("extraAmount", StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    extraAmountBrandAmount = extraAmountBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (StringUtils.isNotBlank(goodsNo) && !goodsNo.equals("null")) {
                    Map<String, Object> merParams = new HashMap<>();
                    merParams.put("goodsNo", goodsNo);
                    merParams.put("merchantId", merchantId);
                    MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merParams);
                    goodsMap.put("goodsName", merchandiseInfoMogo.getName());
                }
                totalBrandAmount = totalBrandAmount.add(totalPrice);
                goodsMap.put("totalPrice", StrUtils.doubleFormatString(totalPrice.doubleValue()));
                mapList.add(goodsMap);
            }
            //品牌小计
            String parentBrandName = "";
            if (StringUtils.isNotBlank(brandName)) {
                Map<String, Object> brandParam = new HashMap<>();
                brandParam.put("brandSuperiorId", Long.valueOf(brandName));
                BrandSuperiorMongo brandSuperiorMongo = brandSuperiorMongoDao.findOne(brandParam);
                if (brandSuperiorMongo != null) {
                    parentBrandName = brandSuperiorMongo.getName();
                }
            }
            Map<String, Object> brandMap = new HashMap<>();
            brandMap.put("brandName", parentBrandName + "-合计");
            brandMap.put("totalPrice", StrUtils.doubleFormatString(totalBrandAmount.doubleValue()));
            brandMap.put("customerReturn", StrUtils.doubleFormatString(customerReturnBrandAmount.doubleValue()));
            brandMap.put("promotionDiscounts", StrUtils.doubleFormatString(promotionDiscountsBrandAmount.doubleValue()));
            brandMap.put("extraDiscount", StrUtils.doubleFormatString(extraDiscountBrandAmount.doubleValue()));
            brandMap.put("promotionDiscountsCust", StrUtils.doubleFormatString(promotionDiscountsCustBrandAmount.doubleValue()));
            brandMap.put("extraAmount", StrUtils.doubleFormatString(extraAmountBrandAmount.doubleValue()));
            totalAllAmount = totalAllAmount.add(totalBrandAmount);
            customerReturnBrandAmount = new BigDecimal("0");
            promotionDiscountsBrandAmount = new BigDecimal("0");
            extraDiscountBrandAmount = new BigDecimal("0");
            promotionDiscountsCustBrandAmount = new BigDecimal("0");
            extraAmountBrandAmount = new BigDecimal("0");
            mapList.add(brandMap);
        }

        //添加已统计的品牌信息
        for (String brandName : existBrandMap.keySet()) {
            BigDecimal totalBrandAmount = new BigDecimal("0");
            List<Map<String, Object>> existGoodsList = existBrandMap.get(brandName);
            for (Map<String, Object> exist : existGoodsList) {
                String goodsNo = (String) exist.get("goodsNo");
                BigDecimal customerReturn = exist.get("customerReturn") == null ? new BigDecimal("0") : (BigDecimal) exist.get("customerReturn");
                customerReturnBrandAmount = customerReturnBrandAmount.add(customerReturn);
                BigDecimal promotionDiscounts = exist.get("promotionDiscounts") == null ? new BigDecimal("0") : (BigDecimal) exist.get("promotionDiscounts");
                promotionDiscountsBrandAmount = promotionDiscountsBrandAmount.add(promotionDiscounts);
                BigDecimal extraDiscount = exist.get("extraDiscount") == null ? new BigDecimal("0") : (BigDecimal) exist.get("extraDiscount");
                extraDiscountBrandAmount = extraDiscountBrandAmount.add(extraDiscount);
                BigDecimal promotionDiscountsCust = exist.get("promotionDiscountsCust") == null ? new BigDecimal("0") : (BigDecimal) exist.get("promotionDiscountsCust");
                promotionDiscountsCustBrandAmount = promotionDiscountsCustBrandAmount.add(promotionDiscountsCust);
                BigDecimal extraAmount = exist.get("extraAmount") == null ? new BigDecimal("0") : (BigDecimal) exist.get("extraAmount");
                extraAmountBrandAmount = extraAmountBrandAmount.add(extraAmount);
                BigDecimal totalPrice = (BigDecimal) exist.get("totalPrice");
                totalBrandAmount = totalBrandAmount.add(totalPrice);
                exist.put("customerReturn", StrUtils.doubleFormatString(customerReturn.doubleValue()));
                exist.put("promotionDiscounts", StrUtils.doubleFormatString(promotionDiscounts.doubleValue()));
                exist.put("extraDiscount", StrUtils.doubleFormatString(extraDiscount.doubleValue()));
                exist.put("promotionDiscountsCust", StrUtils.doubleFormatString(promotionDiscountsCust.doubleValue()));
                exist.put("extraAmount", StrUtils.doubleFormatString(extraAmount.doubleValue()));
                exist.put("totalPrice", StrUtils.doubleFormatString(totalPrice.doubleValue()));
                if (StringUtils.isNotBlank(goodsNo) && !goodsNo.equals("null")) {
                    Map<String, Object> merParams = new HashMap<>();
                    merParams.put("goodsNo", goodsNo);
                    merParams.put("merchantId", merchantId);
                    MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merParams);
                    exist.put("goodsName", merchandiseInfoMogo.getName());
                }
                mapList.add(exist);
            }
            //品牌小计
            String parentBrandName = "";
            if (StringUtils.isNotBlank(brandName)) {
                Map<String, Object> brandParam = new HashMap<>();
                brandParam.put("brandSuperiorId", Long.valueOf(brandName));
                BrandSuperiorMongo brandSuperiorMongo = brandSuperiorMongoDao.findOne(brandParam);
                if (brandSuperiorMongo != null) {
                    parentBrandName = brandSuperiorMongo.getName();
                }
            }
            Map<String, Object> brandMap = new HashMap<>();
            brandMap.put("brandName", parentBrandName + "-合计");
            brandMap.put("totalPrice", StrUtils.doubleFormatString(totalBrandAmount.doubleValue()));
            brandMap.put("customerReturn", StrUtils.doubleFormatString(customerReturnBrandAmount.doubleValue()));
            brandMap.put("promotionDiscounts", StrUtils.doubleFormatString(promotionDiscountsBrandAmount.doubleValue()));
            brandMap.put("promotionDiscountsCust", StrUtils.doubleFormatString(promotionDiscountsCustBrandAmount.doubleValue()));
            brandMap.put("extraDiscount", StrUtils.doubleFormatString(extraDiscountBrandAmount.doubleValue()));
            brandMap.put("extraAmount", StrUtils.doubleFormatString(extraAmountBrandAmount.doubleValue()));
            totalAllAmount = totalAllAmount.add(totalBrandAmount);
            customerReturnBrandAmount = new BigDecimal("0");
            promotionDiscountsBrandAmount = new BigDecimal("0");
            extraDiscountBrandAmount = new BigDecimal("0");
            mapList.add(brandMap);
        }

        totalAllAmount = totalAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        Map<String, Object> totalMap = new HashMap<>();

        if (totalAllAmount.compareTo(new BigDecimal("0")) != 0) {
            totalMap.put("totalAllAmount", StrUtils.doubleFormatString(totalAllAmount.doubleValue()));
        } else {
            totalMap.put("totalAllAmount", StrUtils.doubleFormatString(anotherTotalAllAmount.doubleValue()));
        }

        mapList.add(totalMap);
        return mapList;
    }

    @Override
    public List<Map<String, Object>> listSaleShelfInfo(String orderCode) throws SQLException {
        if (StringUtils.isBlank(orderCode)) {
            throw new RuntimeException("业务单号为空！");
        }
        List<Map<String, Object>> shelfList = new ArrayList<>();
        SaleOrderModel saleOrderModel = new SaleOrderModel();
        saleOrderModel.setCode(orderCode);
        SaleOrderModel orderModel = saleOrderDao.searchByModel(saleOrderModel);

        if (orderModel != null) {
            List<Map<String, Object>> saleShelfList = saleShelfDao.listByPoNoAndOrderId(orderModel.getId());
            Map<String, BigDecimal> priceMap = new HashMap<>();
            Map<String, BigDecimal> totalShelfNumMap = new HashMap<>();
            for (Map<String, Object> map : saleShelfList) {
                String poNo = (String) map.get("po_no");
                BigDecimal price = (BigDecimal) map.get("price");
                BigDecimal totalShelfNum = (BigDecimal) map.get("total_shelf_num");
                BigDecimal amount = price.multiply(totalShelfNum);
                if (priceMap.containsKey(poNo)) {
                    totalShelfNum = totalShelfNumMap.get(poNo).add(totalShelfNum);
                    amount = priceMap.get(poNo).add(amount);
                }
                priceMap.put(poNo, amount);
                totalShelfNumMap.put(poNo, totalShelfNum);
            }
            for (String poNo : priceMap.keySet()) {
                Map<String, Object> shelfMap = new HashMap<>();
                shelfMap.put("poNo", poNo);
                shelfMap.put("totalShelfNum", totalShelfNumMap.get(poNo));
                shelfMap.put("amount", priceMap.get(poNo));
                shelfList.add(shelfMap);
            }
        }
        return shelfList;
    }

    @Override
    public Map<String, String> isExistRelInvoice(String id) throws SQLException {
        Map<String, String> result = new HashMap<>();
        ReceiveBillModel receiveBillModel = receiveBillDao.searchById(Long.valueOf(id));
        if (receiveBillModel == null) {
            result.put("code", "01");
            result.put("msg", "应收账单不存在！");
            return result;
        }
        if (DERP_ORDER.RECEIVEBILL_INVOICESTATUS_02.equals(receiveBillModel.getInvoiceStatus())) {
            result.put("code", "01");
            result.put("msg", "应收账单已作废不能多次作废！");
            return result;
        }
        //如果单据为开票状态则查询关联的发票是否关联其他应收单
        if (DERP_ORDER.RECEIVEBILL_INVOICESTATUS_01.equals(receiveBillModel.getInvoiceStatus())
                || DERP_ORDER.RECEIVEBILL_INVOICESTATUS_03.equals(receiveBillModel.getInvoiceStatus())) {
            if (receiveBillModel.getInvoiceId() == null) {
                result.put("code", "01");
                result.put("msg", "应收账单关联发票不存在！");
                return result;
            }
            ReceiveBillInvoiceModel invoiceModel = receiveBillInvoiceDao.searchById(receiveBillModel.getInvoiceId());
            String relCodes = invoiceModel.getInvoiceRelCodes();
            List<String> relCodeList = Arrays.asList(relCodes.split(","));
            if (relCodeList.size() > 1) {
                result.put("code", "02");
                result.put("msg", "提交作废的发票号{" + invoiceModel.getInvoiceNo() + "}关联多张账单,请一并提交");
                result.put("msg", "发票{" + invoiceModel.getInvoiceNo() + "}关联的{" + relCodes + "}”将一并作废，是否继续？");
                return result;
            }
        }
        result.put("code", "00");
        return result;
    }

    @Override
    public Map<String, String> saveInvalidBill(String ids, String invalidRemark, User user) throws Exception {
        String[] idArr = ids.split(",");
        List<Long> relBillIds = new ArrayList();
        for (String id : idArr) {
            relBillIds.add(Long.valueOf(id));
        }
        Map<String, String> result = new HashMap<>();
        List<ReceiveBillDTO> receiveBillModels = receiveBillDao.listBillByRelIds(relBillIds);
        if (receiveBillModels == null || receiveBillModels.size() == 0) {
            result.put("code", "01");
            result.put("message", "应收账单不存在！");
            return result;
        }

        List<String> billCodes = new ArrayList<>();
        for (ReceiveBillDTO receiveBillModel : receiveBillModels) {
            if (!DERP_ORDER.RECEIVEBILL_BILLSTATUS_02.equals(receiveBillModel.getBillStatus())) {
                result.put("code", "01");
                result.put("message", "此账单账单状态不是待核销");
                return result;
            }
            billCodes.add(receiveBillModel.getCode());
        }
        //更新账单状态为“作废待审”
        receiveBillDao.batchUpdateBillStatus(relBillIds, DERP_ORDER.RECEIVEBILL_BILLSTATUS_05, null);
        for (Long id : relBillIds) {
            ReceiveBillOperateModel receiveBillOperateModel = new ReceiveBillOperateModel();
            receiveBillOperateModel.setBillId(id);
            receiveBillOperateModel.setOperateId(user.getId());
            receiveBillOperateModel.setOperator(user.getName());
            receiveBillOperateModel.setOperateDate(TimeUtils.getNow());
            receiveBillOperateModel.setCreateDate(TimeUtils.getNow());
            receiveBillOperateModel.setRemark(invalidRemark);
            receiveBillOperateModel.setOperateNode(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_3);
            receiveBillOperateDao.save(receiveBillOperateModel);

        }

        /**
         * 发送审核提醒邮件
         */
        for (ReceiveBillDTO dto : receiveBillModels) {
            BigDecimal totalItemPrice = new BigDecimal("0");
            BigDecimal itemPrice = receiveBillItemDao.getTotalReceivePrice(dto.getId());
            BigDecimal costPrice = receiveBillCostItemDao.getTotalReceivePrice(dto.getId());

            if (itemPrice == null) {
                itemPrice = new BigDecimal(0);
            }

            if (costPrice == null) {
                costPrice = new BigDecimal(0);
            }

            totalItemPrice = totalItemPrice.add(itemPrice).add(costPrice);
            totalItemPrice = totalItemPrice.setScale(2, BigDecimal.ROUND_HALF_UP);

            ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO();

            emailUserDTO.setBuId(dto.getBuId());
            emailUserDTO.setBuName(dto.getBuName());
            emailUserDTO.setMerchantId(dto.getMerchantId());
            emailUserDTO.setMerchantName(dto.getMerchantName());
            emailUserDTO.setOrderCode(dto.getCode());
            emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1);
            emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                    DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1));
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_16);
            emailUserDTO.setSupplier(receiveBillModels.get(0).getCustomerName());
            emailUserDTO.setAmount(receiveBillModels.get(0).getCurrency() + "&nbsp;" + totalItemPrice.toPlainString());
            emailUserDTO.setUserName(user.getName());
            emailUserDTO.setCancelId(user.getId());
            JsonConfig jsonConfig = new JsonConfig();
            getReceiveOperators(dto.getId(), emailUserDTO, jsonConfig);

            JSONObject emailJson = JSONObject.fromObject(emailUserDTO, jsonConfig);

            rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                    MQErpEnum.SEND_REMINDER_MAIL.getTags());
        }


        result.put("code", "00");
        return result;
    }

    @Override
    public List<ReceiveBillDTO> listByIds(List<Long> ids) throws SQLException {
        return receiveBillDao.listBillByRelIds(ids);
    }

    @Override
    public Map<String, String> validateRelCodeInfo(List<Long> ids) throws SQLException {
        Map<String, String> resultMap = new HashMap<>();
        List<ReceiveBillDTO> receiveBillDTOS = receiveBillDao.listBillByRelIds(ids);
        List<String> relCodeList = new ArrayList<>();
        for (ReceiveBillDTO dto : receiveBillDTOS) {
            //校验开票应收单关联业务单号的所有单据状态是否是“待核销/部分核销/已核销",且开票状态是”未开票“
            ReceiveBillModel temp = new ReceiveBillModel();
            temp.setMerchantId(dto.getMerchantId());
            temp.setRelCode(dto.getRelCode());
            temp.setInvoiceStatus(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_00);
            temp.setOrderType(DERP_ORDER.RECEIVEBILL_ORDERTYPE_2);
            List<ReceiveBillModel> relList = receiveBillDao.list(temp);
            for (ReceiveBillModel rel : relList) {
                if (rel.getBillStatus().equals(DERP_ORDER.RECEIVEBILL_BILLSTATUS_00)
                        || rel.getBillStatus().equals(DERP_ORDER.RECEIVEBILL_BILLSTATUS_01)) {
                    resultMap.put("code", "01");
                    resultMap.put("msg", "开票的应收账单关联业务单号{" + rel.getRelCode() + "}关联的应收账单状态不能为“待提交”或“待审核”！");
                    return resultMap;
                }
                if (!(rel.getBillStatus().equals(DERP_ORDER.RECEIVEBILL_BILLSTATUS_05)
                        || rel.getBillStatus().equals(DERP_ORDER.RECEIVEBILL_BILLSTATUS_06)) && !ids.contains(rel.getId())) {
                    ids.add(rel.getId());
                }
            }
        }
        resultMap.put("code", "00");
        resultMap.put("ids", org.apache.commons.lang3.StringUtils.join(ids, ","));
        resultMap.put("msg", "校验通过");
        return resultMap;
    }

    @Override
    public Map<String, String> isBillSave(String billCode) throws SQLException {
        Map<String, String> resultMap = new HashMap<>();
        ReceiveBillModel receiveBillModel = new ReceiveBillModel();
        receiveBillModel.setCode(billCode);
        ReceiveBillModel exist = receiveBillDao.searchByModel(receiveBillModel);
        if (exist == null) {
            resultMap.put("code", "01");
            return resultMap;
        }
        resultMap.put("code", "00");
        resultMap.put("billId", exist.getId().toString());
        resultMap.put("msg", "校验通过");
        return resultMap;
    }

    @Override
    public Map<Long, ReceiveBillDTO> listForExport(ReceiveBillDTO dto, User user) throws SQLException {
        Map<Long, ReceiveBillDTO> receiveBillDTOMap = new LinkedHashMap<>();
        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return receiveBillDTOMap;
            }
            dto.setBuList(buList);
        }

        List<ReceiveBillDTO> receiveBillDTOs = receiveBillDao.listReceiveBill(dto);
        List<Long> ids = new ArrayList<>();

        for (ReceiveBillDTO billDTO : receiveBillDTOs) {
            ids.add(billDTO.getId());
            billDTO.setReceivablePrice(BigDecimal.ZERO);
            receiveBillDTOMap.put(billDTO.getId(), billDTO);
        }

        if (!ids.isEmpty()) {
            //获取应收账单的应收明细金额
            List<Map<String, Object>> itemPriceList = receiveBillItemDao.listItemPrice(ids);
            for (Map<String, Object> map : itemPriceList) {
                Long billId = (Long) map.get("billId");
                BigDecimal totalItemPrice = (BigDecimal) map.get("totalPrice");
                ReceiveBillDTO receiveBillTemp = receiveBillDTOMap.get(billId);
                if (totalItemPrice != null) {
                    receiveBillTemp.setReceivablePrice(totalItemPrice);
                }
            }
            //获取应收账单的费用金额
            List<Map<String, Object>> costPriceList = receiveBillCostItemDao.listCostPrice(ids);
            for (Map<String, Object> map : costPriceList) {
                Long billId = (Long) map.get("billId");
                BigDecimal totalCostPrice = (BigDecimal) map.get("totalPrice");
                ReceiveBillDTO receiveBillTemp = receiveBillDTOMap.get(billId);
                if (totalCostPrice != null) {
                    BigDecimal receivablePrice = receiveBillTemp.getReceivablePrice().add(totalCostPrice);
                    receiveBillTemp.setReceivablePrice(receivablePrice);
                }
            }
            //获取应收账单的已核销金额
            List<Map<String, Object>> verifyPriceList = receiveBillVerifyItemDao.listVerifyPrice(ids);
            for (Map<String, Object> map : verifyPriceList) {
                Long billId = (Long) map.get("billId");
                BigDecimal totalCostPrice = (BigDecimal) map.get("totalPrice");
                ReceiveBillDTO receiveBillTemp = receiveBillDTOMap.get(billId);
                receiveBillTemp.setReceivedPrice(totalCostPrice);
            }
        }
        return receiveBillDTOMap;
    }

    @Override
    public List<Map<String, Object>> listJDItemInfos(List<Long> ids, Long merchantId, User user) throws Exception {
        List<Map<String, Object>> mapList = new LinkedList<>();
        BigDecimal totalAllAmount = new BigDecimal("0");
        BigDecimal totalAllNum = new BigDecimal("0");
        Map<String, Map<String, Object>> invoiceMap = new HashMap<>();

        //应收明细
        List<Map<String, Object>> listInvoiceItem = receiveBillItemDao.listInvoiceItems(ids);
        mergeItemByKey(listInvoiceItem, invoiceMap, true, user);
        //费用明细
        List<Map<String, Object>> listInvoiceCostItem = receiveBillCostItemDao.listInvoiceCostItem(ids);
        mergeItemByKey(listInvoiceCostItem, invoiceMap, true, user);

        //以品牌维度合计
        Map<Long, List<Map<String, Object>>> brandMap = new HashMap<>();
        for (String key : invoiceMap.keySet()) {
            Map<String, Object> map = invoiceMap.get(key);
            Long parentBrandId = (Long) map.get("parentBrandId");
            if (brandMap.containsKey(parentBrandId)) {
                brandMap.get(parentBrandId).add(map);
            } else {
                List<Map<String, Object>> costList = new ArrayList<>();
                costList.add(map);
                brandMap.put(parentBrandId, costList);
            }
        }

        //应收明细和费项明细按母品牌维度归类
        for (Long brandId : brandMap.keySet()) {
            List<Map<String, Object>> noGoodsNoList = new LinkedList<>();
            BigDecimal totalBrandAmount = new BigDecimal("0");
            BigDecimal totalBrandNum = new BigDecimal("0");
            List<Map<String, Object>> itemList = brandMap.get(brandId);
            for (Map<String, Object> itemMap : itemList) {
                String goodsNo = (String) itemMap.get("goodsNo");
                BigDecimal totalPrice = (BigDecimal) itemMap.get("totalPrice");
                BigDecimal totalNum = (BigDecimal) itemMap.get("totalNum");

                itemMap.put("price", "-");
                itemMap.put("totalPrice", "-");
                itemMap.put("totalNum", "-");
                if (totalPrice != null && totalNum != null && totalNum.compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal price = totalPrice.divide(totalNum, 2, BigDecimal.ROUND_HALF_UP);
                    itemMap.put("price", StrUtils.doubleFormatString(price.doubleValue()));
                }

                if (totalPrice != null) {
                    itemMap.put("totalPrice", StrUtils.doubleFormatString(totalPrice.doubleValue()));
                    totalBrandAmount = totalBrandAmount.add(totalPrice);
                }

                if (totalNum != null) {
                    itemMap.put("totalNum", StrUtils.intFormatString(totalNum.intValue()));
                    totalBrandNum = totalBrandNum.add(totalNum);
                }

                if (StringUtils.isNotBlank(goodsNo)) {
                    mapList.add(itemMap);
                } else {
                    noGoodsNoList.add(itemMap);
                }
            }

            for (Map<String, Object> noGoodsMap : noGoodsNoList) {
                mapList.add(noGoodsMap);
            }
            Map<String, Object> brandParam = new HashMap<>();
            brandParam.put("brandSuperiorId", brandId);
            BrandSuperiorMongo brandSuperiorMongo = brandSuperiorMongoDao.findOne(brandParam);
            String parentBrandName = "";
            if (brandSuperiorMongo != null) {
                parentBrandName = brandSuperiorMongo.getName();
            }

            //品牌小计
            Map<String, Object> brandSuperiorMap = new HashMap<>();
            brandSuperiorMap.put("goodsName", parentBrandName + "-合计");
            brandSuperiorMap.put("totalPrice", StrUtils.doubleFormatString(totalBrandAmount.doubleValue()));
            brandSuperiorMap.put("totalNum", StrUtils.intFormatString(totalBrandNum.intValue()));
            brandSuperiorMap.put("barcode", "");
            brandSuperiorMap.put("goodsNo", "");
            mapList.add(brandSuperiorMap);
            totalAllAmount = totalAllAmount.add(totalBrandAmount);
            totalAllNum = totalAllNum.add(totalBrandNum);
        }

        totalAllAmount = totalAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("totalAllAmount", StrUtils.doubleFormatString(totalAllAmount.doubleValue()));
        totalMap.put("totalAllNum", StrUtils.intFormatString(totalAllNum.intValue()));
        mapList.add(totalMap);
        return mapList;
    }

    @Override
    public Map<String, String> updateVoucher(List<Long> ids) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        List<ReceiveBillDTO> receiveBillDTOS = receiveBillDao.listBillByRelIds(ids);
        List<String> billCodeList = new ArrayList<>();
        for (ReceiveBillDTO receiveBillDTO : receiveBillDTOS) {
            billCodeList.add(receiveBillDTO.getCode());
        }
        String billCodes = org.apache.commons.lang3.StringUtils.join(billCodeList.toArray(), ",");
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("datasource", "1");
        body.put("billCodes", billCodes);
        JSONObject json = JSONObject.fromObject(body);
        System.out.println(json.toString());
        SendResult result = rocketMQProducer.send(json.toString(), MQOrderEnum.TIMER_RECEVICE_BILL_VOUCHER_BACKFILL.getTopic(), MQOrderEnum.TIMER_RECEVICE_BILL_VOUCHER_BACKFILL.getTags());
        System.out.println(result);
        if (result == null || !result.getSendStatus().name().equals("SEND_OK")) {
            resultMap.put("code", "01");
            resultMap.put("message", "更新凭证信息消息发送失败");
            return resultMap;
        }
        resultMap.put("code", "00");
        resultMap.put("message", "成功");
        return resultMap;
    }

    @Override
    public List<Map<String, Object>> listForItemExport(List<Long> ids, Map<Long, String> idsMap, Map<Long, String> customerNameMap) throws SQLException {
        Map<Long, MerchandiseInfoMogo> merchandiseInfoMogoMap = new HashMap<>();
        List<Map<String, Object>> receiveBillList = new LinkedList<>();
        List<ReceiveBillItemDTO> itemModels = receiveBillItemDao.itemListGroupByBillId(ids);
        for (ReceiveBillItemDTO receiveBillItemDto : itemModels) {
            Map<String, Object> receiveBillItemMap = new HashMap<>();
            if (receiveBillItemDto.getGoodsId() != null) {
                MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoMap.get(receiveBillItemDto.getGoodsId());
                if (merchandiseInfoMogo == null) {
                    Map<String, Object> merchandiseParam = new HashMap<>();
                    merchandiseParam.put("merchandiseId", receiveBillItemDto.getGoodsId());
                    merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseParam);
                    merchandiseInfoMogoMap.put(merchandiseInfoMogo.getMerchandiseId(), merchandiseInfoMogo);
                }

                receiveBillItemDto.setGoodsName(merchandiseInfoMogo.getName());
                receiveBillItemDto.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
                receiveBillItemDto.setCommbarcode(merchandiseInfoMogo.getCommbarcode());
                receiveBillItemMap.put("commbarcode", merchandiseInfoMogo.getCommbarcode());
                receiveBillItemMap.put("goodsNo", merchandiseInfoMogo.getGoodsNo());
                receiveBillItemMap.put("goodsName", merchandiseInfoMogo.getName());
            }
            receiveBillItemMap.put("billCode", idsMap.get(receiveBillItemDto.getBillId()));
            receiveBillItemMap.put("projectName", receiveBillItemDto.getProjectName());
            receiveBillItemMap.put("poNo", receiveBillItemDto.getPoNo());
            receiveBillItemMap.put("platformSku", receiveBillItemDto.getPlatformSku());
            receiveBillItemMap.put("num", receiveBillItemDto.getNum());
            receiveBillItemMap.put("price", receiveBillItemDto.getPrice());
            receiveBillItemMap.put("code", receiveBillItemDto.getCode());
            receiveBillItemMap.put("parentBrandName", receiveBillItemDto.getParentBrandName());
            receiveBillItemMap.put("paymentSubjectName", receiveBillItemDto.getPaymentSubjectName());
            receiveBillItemMap.put("subjectName", receiveBillItemDto.getSubjectName());
            receiveBillItemMap.put("customerName", customerNameMap.get(receiveBillItemDto.getBillId()));
            receiveBillItemMap.put("invoiceDescription", receiveBillItemDto.getInvoiceDescription());
            receiveBillItemMap.put("verifyPlatformCode", DERP.getLabelByKey(DERP.storePlatformList, receiveBillItemDto.getVerifyPlatformCode()));
            receiveBillList.add(receiveBillItemMap);
        }
        return receiveBillList;
    }

    @Override
    public List<Map<String, Object>> listForCostItemExport(List<Long> ids, Map<Long, String> idsMap, Map<Long, String> customerNameMap) throws SQLException {
        List<Map<String, Object>> receiveBillList = new LinkedList<>();
        Map<Long, MerchandiseInfoMogo> merchandiseInfoMogoMap = new HashMap<>();
        List<ReceiveBillCostItemDTO> itemModels = receiveBillCostItemDao.itemListByBillIds(ids);
        for (ReceiveBillCostItemDTO costItemDTO : itemModels) {
            Map<String, Object> receiveBillItemMap = new HashMap<>();
            if (costItemDTO.getGoodsId() != null) {
                MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoMap.get(costItemDTO.getGoodsId());
                if (merchandiseInfoMogo == null) {
                    Map<String, Object> merchandiseParam = new HashMap<>();
                    merchandiseParam.put("merchandiseId", costItemDTO.getGoodsId());
                    merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseParam);
                    merchandiseInfoMogoMap.put(merchandiseInfoMogo.getMerchandiseId(), merchandiseInfoMogo);
                }

                costItemDTO.setGoodsName(merchandiseInfoMogo.getName());
                costItemDTO.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
                costItemDTO.setCommbarcode(merchandiseInfoMogo.getCommbarcode());
                receiveBillItemMap.put("commbarcode", merchandiseInfoMogo.getCommbarcode());
                receiveBillItemMap.put("goodsNo", merchandiseInfoMogo.getGoodsNo());
                receiveBillItemMap.put("goodsName", merchandiseInfoMogo.getName());
            }
            receiveBillItemMap.put("billCode", idsMap.get(costItemDTO.getBillId()));
            receiveBillItemMap.put("projectName", costItemDTO.getProjectName());
            receiveBillItemMap.put("poNo", costItemDTO.getPoNo());
            receiveBillItemMap.put("num", costItemDTO.getNum());
            receiveBillItemMap.put("price", costItemDTO.getPrice());
            receiveBillItemMap.put("billType", costItemDTO.getBillTypeLabel());
            receiveBillItemMap.put("parentBrandName", costItemDTO.getParentBrandName());
            receiveBillItemMap.put("paymentSubjectName", costItemDTO.getPaymentSubjectName());
            receiveBillItemMap.put("remark", costItemDTO.getRemark());
            receiveBillItemMap.put("customerName", customerNameMap.get(costItemDTO.getBillId()));
            receiveBillItemMap.put("invoiceDescription", costItemDTO.getInvoiceDescription());
            receiveBillItemMap.put("verifyPlatformCode", DERP.getLabelByKey(DERP.storePlatformList, costItemDTO.getVerifyPlatformCode()));
            receiveBillList.add(receiveBillItemMap);
        }
        return receiveBillList;
    }

    @Override
    public Map<String, Object> saveApiAddReceiveBill(ReceiveBillDTO dto,User user) throws Exception {
        Map<String, Object> retMap = new HashMap<>();
        if (StringUtils.isBlank(dto.getSortType())) {
            retMap.put("code", "01");
            retMap.put("message", "分类不能为空！");
            return retMap;
        }
        if (StringUtils.isBlank(dto.getBillMonth())) {
            retMap.put("code", "01");
            retMap.put("message", "账单月份不能为空！");
            return retMap;
        }

        if (StringUtils.isBlank(dto.getCurrency())) {
            retMap.put("code", "01");
            retMap.put("message", "结算币种不能为空！");
            return retMap;
        }

        if (dto.getCustomerId() == null) {
            retMap.put("code", "01");
            retMap.put("message", "客户不能为空！");
            return retMap;
        }

        if (dto.getBuId() == null) {
            retMap.put("code", "01");
            retMap.put("message", "事业部不能为空！");
            return retMap;
        }
        List<ReceiveBillCostItemModel> costItemModels = new ArrayList<>();
        List<ReceiveBillItemModel> itemModels = new ArrayList<>();
        List<Map<String, Object>> costItemList = dto.getCostItemList();
        for (Map<String, Object> itemMap : costItemList) {
            String projectId = (String) itemMap.get("projectId");
            String projectName = (String) itemMap.get("projectName");
            String goodsId = (String) itemMap.get("goodsId");
            String goodsNo = (String) itemMap.get("goodsNo");
            String goodsName = (String) itemMap.get("goodsName");
            String brandParent = (String) itemMap.get("brandParent");
            String brandParentName = (String) itemMap.get("brandParentName");
            String poNo = (String) itemMap.get("poNo");
            String num = (String) itemMap.get("num");
            String taxRate = (String) itemMap.get("taxRate");
            String price = (String) itemMap.get("price");
            String billType = (String) itemMap.get("billType");
            String remark = (String) itemMap.get("remark");
            String storePlatformCode = (String) itemMap.get("storePlatformCode");
            String storePlatformName = (String) itemMap.get("storePlatformName");
            String invoiceDescription = (String) itemMap.get("invoiceDescription");

            SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(Long.valueOf(projectId));
            if (settlementConfigModel == null) {
                retMap.put("code", "01");
                retMap.put("message", "费用项目不存在！");
                return retMap;
            }

            ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());
            if (paymentSubjectModel == null) {
                retMap.put("code", "01");
                retMap.put("message", "NC收支费项不存在");
                return retMap;
            }

            if (StringUtils.isBlank(brandParent)) {
                retMap.put("code", "01");
                retMap.put("message", "母品牌不能为空！");
                return retMap;
            }

            if (projectName.equals("经销业务TO B收入") && StringUtils.isBlank(goodsNo)) {
                retMap.put("code", "01");
                retMap.put("msg", "费用项目为“经销业务TO B收入”时，商品不能为空！");
                return retMap;
            }

            if (projectName.equals("经销业务TO B收入") && StringUtils.isBlank(num)) {
                retMap.put("code", "01");
                retMap.put("msg", "费用项目为“经销业务TO B收入”时，数量不能为空！");
                return retMap;
            }

            //只要是对应“经销业务TO B收入”均对应进入应收明细
            if (projectName.equals("经销业务TO B收入")) {
                ReceiveBillItemModel itemModel = new ReceiveBillItemModel();
                itemModel.setDataSource(DERP_ORDER.RECEIVEBILLITEM_DATASOURCE_1);
                itemModel.setProjectId(Long.valueOf(projectId));
                itemModel.setProjectName(projectName);
                itemModel.setInvoiceDescription(invoiceDescription);
                if (DERP_ORDER.RECEIVEBILL_SORTTYPE_2.equals(dto.getSortType())) {
                    itemModel.setVerifyPlatformCode(storePlatformCode);
                }

                //nc
                itemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
                itemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
                //科目
                itemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                itemModel.setSubjectName(paymentSubjectModel.getSubName());
                if (StringUtils.isNotBlank(goodsId)&&Integer.parseInt(goodsId)>0) {
                    itemModel.setGoodsId(Long.valueOf(goodsId));
                    itemModel.setGoodsNo(goodsNo);
                    itemModel.setGoodsName(goodsName);
                }
                if (StringUtils.isNotBlank(brandParent)&&Integer.parseInt(brandParent)>0) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("brandSuperiorId", Long.valueOf(brandParent));
                    BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(params);
                    if (brandSuperior != null) {
                        itemModel.setParentBrandName(brandSuperior.getName());
                        itemModel.setParentBrandId(brandSuperior.getBrandSuperiorId());
                        itemModel.setParentBrandCode(brandSuperior.getNcCode());
                    }
                }
                itemModel.setPoNo(poNo);
                if (StringUtils.isNotBlank(num)) {
                    itemModel.setNum(Integer.valueOf(num));
                }
                if (DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_1.equals(billType)) {
                    itemModel.setPrice(new BigDecimal(price).negate());
                } else {
                    itemModel.setPrice(new BigDecimal(price));
                }

                itemModel.setTaxRate(Double.valueOf(taxRate));
                BigDecimal tax = new BigDecimal("0");
                if (StringUtils.isNotBlank(taxRate) && StringUtils.isNotBlank(price)) {
                    tax = new BigDecimal(taxRate).multiply(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP);
                }

                BigDecimal taxAmount = tax.add(new BigDecimal(price));
                itemModel.setTax(tax);
                itemModel.setTaxAmount(taxAmount);
                itemModels.add(itemModel);
            } else {
                ReceiveBillCostItemModel receiveBillCostItemModel = new ReceiveBillCostItemModel();
                receiveBillCostItemModel.setProjectId(Long.valueOf(projectId));
                receiveBillCostItemModel.setProjectName(projectName);
                receiveBillCostItemModel.setRemark(remark);
                receiveBillCostItemModel.setInvoiceDescription(invoiceDescription);
                if (DERP_ORDER.RECEIVEBILL_SORTTYPE_2.equals(dto.getSortType())) {
                    receiveBillCostItemModel.setVerifyPlatformCode(storePlatformCode);
                }
                //nc
                receiveBillCostItemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
                receiveBillCostItemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
                //科目
                receiveBillCostItemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                receiveBillCostItemModel.setSubjectName(paymentSubjectModel.getSubName());
                if (StringUtils.isNotBlank(goodsId)&&Integer.parseInt(goodsId)>0) {
                    receiveBillCostItemModel.setGoodsId(Long.valueOf(goodsId));
                    receiveBillCostItemModel.setGoodsNo(goodsNo);
                    receiveBillCostItemModel.setGoodsName(goodsName);
                }
                if (StringUtils.isNotBlank(brandParent)&&Integer.parseInt(brandParent)>0) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("brandSuperiorId", Long.valueOf(brandParent));
                    BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(params);
                    if (brandSuperior != null) {
                        receiveBillCostItemModel.setParentBrandName(brandSuperior.getName());
                        receiveBillCostItemModel.setParentBrandId(brandSuperior.getBrandSuperiorId());
                        receiveBillCostItemModel.setParentBrandCode(brandSuperior.getNcCode());
                    }
                }
                receiveBillCostItemModel.setPoNo(poNo);
                if (StringUtils.isNotBlank(num)) {
                    receiveBillCostItemModel.setNum(Integer.valueOf(num));
                }
                receiveBillCostItemModel.setPrice(new BigDecimal(price));
                receiveBillCostItemModel.setBillType(billType);
                receiveBillCostItemModel.setTaxRate(Double.valueOf(taxRate));
                BigDecimal tax = new BigDecimal("0");
                if (StringUtils.isNotBlank(taxRate) && StringUtils.isNotBlank(price)) {
                    tax = new BigDecimal(taxRate).multiply(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP);
                }

                BigDecimal taxAmount = tax.add(new BigDecimal(price));
                receiveBillCostItemModel.setTax(tax);
                receiveBillCostItemModel.setTaxAmount(taxAmount);
                costItemModels.add(receiveBillCostItemModel);
            }

        }
        Map<String, Object> params = new HashMap<>();
        params.put("customerid", dto.getCustomerId());
        params.put("cusType", "1");
        CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(params);
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("customerId", dto.getCustomerId());
        customerParams.put("merchantId", user.getMerchantId());
        CustomerMerchantRelMongo customerRel = customerMerchantRelMongoDao.findOne(customerParams);
        if (customerRel == null) {
            retMap.put("code", "01");
            retMap.put("message", "客户关联不存在！");
            return retMap;
        }
        Map<String, Object> buMap = new HashMap<>();
        buMap.put("buId", dto.getBuId());
        buMap.put("merchantId", user.getMerchantId());
        buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
        MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(buMap);

        ReceiveBillModel model = new ReceiveBillModel();
        model.setCustomerId(dto.getCustomerId());
        model.setCustomerName(customerInfo.getName());
        model.setBuId(dto.getBuId());
        model.setBuName(merchantBuRelMongo.getBuName());
        model.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_00);
        model.setIsAddWorn(DERP_ORDER.RECEIVEBILL_ISADDWORN_1);
        model.setCurrency(dto.getCurrency());
        model.setBillDate(TimeUtils.parse(dto.getBillMonth(), "yyyy-MM"));
        model.setCreater(user.getName());
        model.setCreaterId(user.getId());
        model.setCreateDate(TimeUtils.getNow());
        model.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_YSZD));
        model.setMerchantId(user.getMerchantId());
        model.setMerchantName(user.getMerchantName());
        model.setInvoiceStatus(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_00);
        model.setNcStatus(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10);
        model.setSaleModel(DERP_ORDER.RECEIVEBILL_SALEMODEL_009);
        model.setSortType(dto.getSortType());
        model.setNcChannelCode(customerInfo.getNcChannelCode());
        model.setSettlementType("1");
        Long id = receiveBillDao.save(model);
        for (ReceiveBillItemModel itemModel : itemModels) {
            itemModel.setBillId(id);
        }
        for (ReceiveBillCostItemModel itemModel : costItemModels) {
            itemModel.setBillId(id);
        }

        int pageSize = 1000;
        for (int i = 0; i < itemModels.size(); ) {
            int pageSub = (i + pageSize) < itemModels.size() ? (i + pageSize) : itemModels.size();
            receiveBillItemDao.batchSave(itemModels.subList(i, pageSub));
            i = pageSub;
        }
        for (int i = 0; i < costItemModels.size(); ) {
            int pageSub = (i + pageSize) < costItemModels.size() ? (i + pageSize) : costItemModels.size();
            receiveBillCostItemDao.batchSave(costItemModels.subList(i, pageSub));
            i = pageSub;
        }
        retMap.put("code", "00");
        retMap.put("msg", "保存成功");
        retMap.put("billId", id);
        return retMap;
    }

    @Override
    public List<Map<String, Object>> listKLItemInfos(List<Long> ids, Long merchantId) throws Exception {

        List<Map<String, Object>> itemMapList = new ArrayList<>();
        Map<String, BigDecimal> poNoMap = new HashMap<>();
        BigDecimal totalAmount = new BigDecimal("0");

        List<Map<String, Object>> listInvoiceItem = receiveBillItemDao.listByPoNo(ids);

        if (listInvoiceItem != null) {
            for (Map<String, Object> map : listInvoiceItem) {
                String poNo = (String) map.get("poNo");
                BigDecimal amount = map.get("amount") == null ? BigDecimal.ZERO : (BigDecimal) map.get("amount");
                poNoMap.put(poNo, amount);
            }
        }

        //合并相同po号或费项的金额
        List<Map<String, Object>> listInvoiceCostItem = receiveBillCostItemDao.listByPoNoAndProject(ids);
        if (listInvoiceCostItem != null) {
            for (Map<String, Object> map : listInvoiceCostItem) {
                String poNo = (String) map.get("poNo");
                String projectName = (String) map.get("projectName");
                BigDecimal amount = map.get("amount") == null ? BigDecimal.ZERO : (BigDecimal) map.get("amount");

                if (StringUtils.isBlank(poNo)) {
                    poNo = projectName;
                }

                if (poNoMap.containsKey(poNo)) {
                    BigDecimal existAmount = poNoMap.get(poNo);
                    amount = amount.add(existAmount);
                }
                poNoMap.put(poNo, amount);
            }
        }

        for (String poNo : poNoMap.keySet()) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("poNo", poNo);
            itemMap.put("totalPrice", poNoMap.get(poNo));
            itemMapList.add(itemMap);
            totalAmount = totalAmount.add(poNoMap.get(poNo));
        }

        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("totalAllAmount", totalAmount);
        itemMapList.add(itemMap);
        return itemMapList;
    }

    @Override
    public List<Map<String, Object>> listYZItemInfos(List<Long> ids, Long merchantId, User user) throws Exception {
        List<Map<String, Object>> mapList = new LinkedList<>();
        List<Map<String, Object>> noGoodsNoMapList = new LinkedList<>();
        BigDecimal totalAllAmount = new BigDecimal("0");
        BigDecimal totalAllNum = new BigDecimal("0");

        Map<String, Map<String, Object>> invoiceMap = new HashMap<>();
        //应收明细
        List<Map<String, Object>> listInvoiceItem = receiveBillItemDao.listInvoiceItems(ids);
        mergeItemByKey(listInvoiceItem, invoiceMap, true, user);

        //费用明细
        List<Map<String, Object>> listInvoiceCostItem = receiveBillCostItemDao.listInvoiceCostItem(ids);
        mergeItemByKey(listInvoiceCostItem, invoiceMap, true, user);

        int index = 1;
        for (String key : invoiceMap.keySet()) {
            Map<String, Object> map = invoiceMap.get(key);
            String goodsNo = (String) map.get("goodsNo");
            BigDecimal totalPrice = (BigDecimal) map.get("totalPrice");
            BigDecimal totalNum = (BigDecimal) map.get("totalNum");
            BigDecimal price = (BigDecimal) map.get("price");

            map.put("totalPrice", "-");
            map.put("price", "-");
            map.put("totalNum", "-");
            if (totalPrice != null) {
                totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
                map.put("totalPrice", StrUtils.doubleFormatString(totalPrice.doubleValue()));
                totalAllAmount = totalAllAmount.add(totalPrice);
            }

            if (price != null) {
                map.put("price", StrUtils.doubleFormatString(price.doubleValue()));
            }

            if (totalNum != null) {
                map.put("totalNum", StrUtils.intFormatString(totalNum.intValue()));
                totalAllNum = totalAllNum.add(totalNum);
            }

            if (StringUtils.isNotBlank(goodsNo)) {
                map.put("index", index);
                mapList.add(map);
                index++;
            } else {
                noGoodsNoMapList.add(map);
            }
        }

        for (Map<String, Object> map : noGoodsNoMapList) {
            map.put("index", index);
            mapList.add(map);
            index++;
        }
        totalAllAmount = totalAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("totalAllAmount", StrUtils.doubleFormatString(totalAllAmount.doubleValue()));
        totalMap.put("totalAllNum", StrUtils.intFormatString(totalAllNum.intValue()));
        mapList.add(totalMap);
        return mapList;
    }

    @Override
    public List<ReceiveBillItemDTO> listReceiveItem(ReceiveBillDTO dto) throws SQLException {
        List<ReceiveBillItemDTO> itemDTOList = receiveBillItemDao.listReceiveItem(dto);

        List<Long> goodsIds = new ArrayList<>();
        for (ReceiveBillItemDTO receiveBillItemDTO : itemDTOList) {
            if (receiveBillItemDTO.getGoodsId() != null) {
                goodsIds.add(receiveBillItemDTO.getGoodsId());
            }
        }

        List<MerchandiseInfoMogo> merchandiseInfos = merchandiseInfoMogoDao.findAllByIn("merchandiseId", goodsIds);

        Map<Long, MerchandiseInfoMogo> merchandiseInfoMogoMap = new HashMap<>();
        for (MerchandiseInfoMogo merchandiseInfo : merchandiseInfos) {
            merchandiseInfoMogoMap.put(merchandiseInfo.getMerchandiseId(), merchandiseInfo);
        }

        for (ReceiveBillItemDTO receiveBillItemDTO : itemDTOList) {
            if (receiveBillItemDTO.getGoodsId() != null) {
                MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoMap.get(receiveBillItemDTO.getGoodsId());

                if (merchandiseInfoMogo != null) {
                    receiveBillItemDTO.setGoodsName(merchandiseInfoMogo.getName());
                    receiveBillItemDTO.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
                    receiveBillItemDTO.setCommbarcode(merchandiseInfoMogo.getCommbarcode());
                    receiveBillItemDTO.setBarcode(merchandiseInfoMogo.getBarcode());
                }

            }
        }
        return itemDTOList;
    }

    @Override
    public List<ReceiveBillCostItemDTO> listReceiveCostItem(ReceiveBillDTO dto) throws SQLException {
        List<ReceiveBillCostItemDTO> itemDTOList = receiveBillCostItemDao.listReceiveCostItem(dto);

        List<Long> goodsIds = new ArrayList<>();
        for (ReceiveBillCostItemDTO receiveBillItemDTO : itemDTOList) {
            if (receiveBillItemDTO.getGoodsId() != null) {
                goodsIds.add(receiveBillItemDTO.getGoodsId());
            }
        }

        List<MerchandiseInfoMogo> merchandiseInfos = merchandiseInfoMogoDao.findAllByIn("merchandiseId", goodsIds);

        Map<Long, MerchandiseInfoMogo> merchandiseInfoMogoMap = new HashMap<>();
        for (MerchandiseInfoMogo merchandiseInfo : merchandiseInfos) {
            merchandiseInfoMogoMap.put(merchandiseInfo.getMerchandiseId(), merchandiseInfo);
        }

        for (ReceiveBillCostItemDTO receiveBillItemDTO : itemDTOList) {
            if (receiveBillItemDTO.getGoodsId() != null) {
                MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoMap.get(receiveBillItemDTO.getGoodsId());

                if (merchandiseInfoMogo != null) {
                    receiveBillItemDTO.setGoodsName(merchandiseInfoMogo.getName());
                    receiveBillItemDTO.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
                    receiveBillItemDTO.setCommbarcode(merchandiseInfoMogo.getCommbarcode());
                }

            }
        }
        return itemDTOList;
    }

    @Override
    public Map importReceiveItems(List<List<Map<String, String>>> data, Long billId, User user) throws SQLException {

        List<ImportErrorMessage> resultList = new ArrayList<>();

        Integer success = 0;
        Integer failure = 0;

        ReceiveBillModel receiveBillModel = receiveBillDao.searchById(billId);

        if (receiveBillModel == null) {
            setErrorMsg(0, "应收账单没有找到", resultList);
            failure += 1;
        }

        List<Map<String, String>> objects = data.get(0);

        if (failure == 0) {
            for (int j = 0; j < objects.size(); j++) {

                Map<String, String> map = objects.get(j);

                String code = map.get("系统业务单号");
                if (StringUtils.isBlank(code)) {
                    setErrorMsg(j + 1, "系统业务单号不能为空", resultList);
                    failure += 1;
                    continue;
                }

                if (StringUtils.isNotBlank(code)) {
                    if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_1.equals(receiveBillModel.getOrderType()) && !code.contains(DERP.UNIQUE_ID_SJD)) {
                        setErrorMsg(j + 1, "系统业务单号不符合当前创建应收的单据类型", resultList);
                        failure += 1;
                        continue;
                    }

                    if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_2.equals(receiveBillModel.getOrderType()) && !code.contains(DERP.UNIQUEID_PREFIX_ZDCK)) {
                        setErrorMsg(j + 1, "系统业务单号不符合当前创建应收的单据类型", resultList);
                        failure += 1;
                        continue;
                    }

                    if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_3.equals(receiveBillModel.getOrderType()) && !code.contains(DERP.UNIQUEID_PREFIX_YSD)) {
                        setErrorMsg(j + 1, "系统业务单号不符合当前创建应收的单据类型", resultList);
                        failure += 1;
                        continue;
                    }

                    if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_4.equals(receiveBillModel.getOrderType()) && !code.contains(DERP.UNIQUEID_PREFIX_XSO)) {
                        setErrorMsg(j + 1, "系统业务单号不符合当前创建应收的单据类型", resultList);
                        failure += 1;
                        continue;
                    }

                    if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_5.equals(receiveBillModel.getOrderType()) && !code.contains(DERP.UNIQUEID_PREFIX_CGSD)) {
                        setErrorMsg(j + 1, "系统业务单号不符合当前创建应收的单据类型", resultList);
                        failure += 1;
                        continue;
                    }

                    if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_7.equals(receiveBillModel.getOrderType()) && !code.contains(DERP.UNIQUEID_PREFIX_XSTR)) {
                        setErrorMsg(j + 1, "系统业务单号不符合当前创建应收的单据类型", resultList);
                        failure += 1;
                        continue;
                    }
                }

                String projectCode = map.get("费项编码");
                if (StringUtils.isBlank(projectCode)) {
                    setErrorMsg(j + 1, "费项编码不能为空", resultList);
                    failure += 1;
                    continue;
                }

                String poNo = map.get("PO号");
                if (StringUtils.isBlank(poNo)) {
                    setErrorMsg(j + 1, "PO号不能为空", resultList);
                    failure += 1;
                    continue;
                }

                String goodsNo = map.get("商品货号");
                if (StringUtils.isBlank(goodsNo)) {
                    setErrorMsg(j + 1, "商品货号不能为空", resultList);
                    failure += 1;
                    continue;
                }

                String taxRate = map.get("税率");
                if (StringUtils.isBlank(taxRate)) {
                    setErrorMsg(j + 1, "税率不能为空", resultList);
                    failure += 1;
                    continue;
                }

                String num = map.get("数量");
                if (StringUtils.isBlank(num)) {
                    setErrorMsg(j + 1, "数量不能为空", resultList);
                    failure += 1;
                    continue;
                }

                String price = map.get("结算金额（不含税）");
                if (StringUtils.isBlank(price)) {
                    setErrorMsg(j + 1, "结算金额（不含税）不能为空", resultList);
                    failure += 1;
                    continue;
                }
            }
        }

        List<ReceiveBillItemDTO> receiveBillItemDTOS = new ArrayList<>();
        if (failure == 0) {

            for (int j = 0; j < objects.size(); j++) {

                Map<String, String> map = objects.get(j);

                String code = map.get("系统业务单号");
                String projectCode = map.get("费项编码");
                String poNo = map.get("PO号");
                String platformSku = map.get("平台SKU编码");
                String goodsNo = map.get("商品货号");
                String taxRate = map.get("税率");
                String num = map.get("数量");
                String price = map.get("结算金额（不含税）");


                ReceiveBillItemDTO receiveBillItemDTO = new ReceiveBillItemDTO();
                receiveBillItemDTO.setCode(code);
                receiveBillItemDTO.setPoNo(poNo);
                receiveBillItemDTO.setPlatformSku(platformSku);
                receiveBillItemDTO.setGoodsNo(goodsNo);
                receiveBillItemDTO.setTaxRate(Double.valueOf(taxRate));
                receiveBillItemDTO.setNum(Integer.valueOf(num));
                receiveBillItemDTO.setPrice(new BigDecimal(price));
                receiveBillItemDTO.setProjectCode(projectCode);

                BigDecimal tax = new BigDecimal(price).multiply(new BigDecimal(taxRate)).setScale(2, BigDecimal.ROUND_HALF_UP);
                BigDecimal taxAmount = new BigDecimal(price).add(tax);
                receiveBillItemDTO.setTax(tax);
                receiveBillItemDTO.setTaxAmount(taxAmount);

                Map<String, String> validateMap = validateItems(receiveBillItemDTO, receiveBillModel);
                if ("01".equals(validateMap.get("code"))) {
                    setErrorMsg(j + 1, validateMap.get("message"), resultList);
                    failure += 1;
                    continue;
                }

                receiveBillItemDTOS.add(receiveBillItemDTO);
            }
        }

        if (failure == 0) {
            success = objects.size();
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msgList", resultList);
        map.put("itemList", receiveBillItemDTOS);
        map.put("success", success);
        map.put("failure", failure);
        return map;
    }

    @Override
    public List<ReceiveBillItemDTO> exportListItem(String json) {
        List<ReceiveBillItemDTO> itemList = new ArrayList<>();
        // 解析json
        net.sf.json.JSONObject jsonObj = net.sf.json.JSONObject.fromObject(json);
        // 解析表体数据
        net.sf.json.JSONArray itemArr = net.sf.json.JSONArray.fromObject(jsonObj.get("itemList"));
        for (int i = 0; i < itemArr.size(); i++) {
            net.sf.json.JSONObject job = itemArr.getJSONObject(i);
            if (job.isNullObject() || job.isEmpty()) {
                continue;
            }

            String code = (String) job.get("code");
            String projectCode = (String) job.get("projectCode");
            String projectName = (String) job.getString("projectName");
            String poNo = (String) job.get("poNo");
            String platformSku = (String) job.get("platformSku");
            String goodsNo = (String) job.get("goodsNo");
            String taxRateStr = (String) job.get("taxRate");
            Integer num = (Integer) job.get("num");
            String priceStr = (String) job.get("price");
            String barcode = (String) job.get("barcode");

//            Integer num = StringUtils.isBlank(numStr) ? null : Integer.valueOf(numStr.trim());
            Double taxRate = StringUtils.isBlank(taxRateStr.trim()) ? null : Double.valueOf(taxRateStr.trim());
            BigDecimal price = StringUtils.isBlank(priceStr) ? new BigDecimal("0") : new BigDecimal(priceStr);

            // 注入数据
            ReceiveBillItemDTO itemDTO = new ReceiveBillItemDTO();
            itemDTO.setCode(code);
            itemDTO.setProjectCode(projectCode);
            itemDTO.setProjectName(projectName);
            itemDTO.setPoNo(poNo);
            itemDTO.setPlatformSku(platformSku);
            itemDTO.setGoodsNo(goodsNo);
            itemDTO.setTaxRate(taxRate);
            itemDTO.setNum(num);
            itemDTO.setPrice(price);
            itemDTO.setBarcode(barcode);

            itemList.add(itemDTO);

        }
        return itemList;
    }

    @Override
    public Map<String, String> saveReceiveBillFromEdit(ReceiveBillSubmitForm form, User user) throws Exception {
        Map<String, String> retMap = new HashMap<String, String>();
        ReceiveBillModel receiveBillModel = receiveBillDao.searchById(form.getBillId());
        if (!DERP_ORDER.RECEIVEBILL_BILLSTATUS_00.equals(receiveBillModel.getBillStatus())) {
            retMap.put("code", "01");
            retMap.put("message", "此账单账单状态不是待提交");
            return retMap;
        }

        List<ReceiveBillItemDTO> itemList = form.getItemList();
        List<ReceiveBillItemModel> itemModels = new ArrayList<>();
        List<String> relCodes = new ArrayList<>();

        BigDecimal itemPrice = new BigDecimal("0");
        for (ReceiveBillItemDTO itemDTO : itemList) {
            Map<String, String> map = validateItems(itemDTO, receiveBillModel);

            if ("01".equals(map.get("code"))) {
                return map;
            }

            itemPrice = itemPrice.add(itemDTO.getPrice());

            ReceiveBillItemModel itemModel = new ReceiveBillItemModel();
            BeanUtils.copyProperties(itemDTO, itemModel);
            itemModels.add(itemModel);
            if (!relCodes.contains(itemDTO.getCode())) {
                relCodes.add(itemDTO.getCode());
            }
        }

        List<ReceiveBillVerifyItemModel> verifyItemModels = new ArrayList<>();
        BigDecimal verifyAmount = new BigDecimal("0"); // 核销金额
        BigDecimal receiveAmount = new BigDecimal("0"); // 应收金额
//        BigDecimal itemPrice = receiveBillItemDao.getTotalReceivePrice(receiveBillModel.getId());
        BigDecimal costPrice = receiveBillCostItemDao.getTotalReceivePrice(receiveBillModel.getId());

        /*if (itemPrice == null) {
            itemPrice = new BigDecimal(0);
        }*/

        if (costPrice == null) {
            costPrice = new BigDecimal(0);
        }

        receiveAmount = itemPrice.add(costPrice).setScale(2, BigDecimal.ROUND_HALF_UP);

//        if (StringUtils.isNotBlank(form.getAdvanceIds())) {
//
//            List<Long> advanceIdList = StrUtils.parseIds(form.getAdvanceIds());
//            //勾选核销的预收单明细
//            List<AdvanceBillItemModel> advanceBillItemModels = advanceBillItemDao.listByIds(advanceIdList);
//
//            List<Long> advanceIds = new ArrayList<>();
//            for (AdvanceBillItemModel advanceBillItemModel : advanceBillItemModels) {
//                advanceIds.add(advanceBillItemModel.getAdvanceId());
//                verifyAmount = verifyAmount.add(advanceBillItemModel.getAmount());
//            }
//            //勾选核销的预收单
//            List<AdvanceBillModel> advanceBillModels = advanceBillDao.listByIds(advanceIds);
//
//            ReceiveBillVerifyItemModel receiveBillVerifyItemModel = new ReceiveBillVerifyItemModel();
//            receiveBillVerifyItemModel.setType(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_1);
//            List<ReceiveBillVerifyItemModel> reAdvanceItemModels = receiveBillVerifyItemDao.list(receiveBillVerifyItemModel);
//            //已勾稽的预收订单
//            List<Long> reAdvanceIdList = new ArrayList<>();
//            for (ReceiveBillVerifyItemModel itemModel : reAdvanceItemModels) {
//                if (!itemModel.getBillId().equals(form.getBillId())) {
//                    reAdvanceIdList.add(itemModel.getAdvanceId());
//                }
//            }
//
//            Map<Long, AdvanceBillModel> advanceBillModelMap = new HashMap<>();
//
//            //校验勾稽的预收单状态
//            for (AdvanceBillModel advanceBillModel : advanceBillModels) {
//                if (!(DERP_ORDER.RECEIVEBILL_BILLSTATUS_02.equals(advanceBillModel.getBillStatus()) ||
//                        DERP_ORDER.RECEIVEBILL_BILLSTATUS_03.equals(advanceBillModel.getBillStatus()) ||
//                        DERP_ORDER.RECEIVEBILL_BILLSTATUS_04.equals(advanceBillModel.getBillStatus()))) {
//                    retMap.put("code", "01");
//                    retMap.put("message", "预收单：" + advanceBillModel.getCode() + "状态不为“待核销”或“部分核销”或“已核销”");
//                    return retMap;
//                }
//
//                advanceBillModelMap.put(advanceBillModel.getId(), advanceBillModel);
//            }
//
//            for (AdvanceBillItemModel advanceBillItemModel : advanceBillItemModels) {
//                if (reAdvanceIdList.contains(advanceBillItemModel.getId())) {
//                    retMap.put("code", "01");
//                    retMap.put("message", "预收单：" + advanceBillItemModel.getRelCode() + "已被勾稽，不能重复勾稽");
//                    return retMap;
//                }
//            }
//
//            if (verifyAmount.compareTo(receiveAmount) > 0) {
//                retMap.put("code", "01");
//                retMap.put("message", "核销金额不能大于应收账单金额");
//                return retMap;
//            }
//
//            Map<Long, AdvanceBillOperateItemModel> auditDateMap = new HashMap<>();
//            Map<Long, AdvanceBillVerifyItemModel> verifyDateMap = new HashMap<>();
//            if (!advanceIds.isEmpty()) {
//
//                //预收款单的审核通过的最新时间
//                List<AdvanceBillOperateItemModel> operateItemModels = advanceBillOperateItemDao.getLatestAuditModelByAdvanceIds(advanceIds);
//
//                for (AdvanceBillOperateItemModel operateItemModel : operateItemModels) {
//                    auditDateMap.put(operateItemModel.getAdvanceId(), operateItemModel);
//                }
//
//                //预收款单的核销收款的最新时间
//                List<AdvanceBillVerifyItemModel> verifyModelByAdvanceIds = advanceBillVerifyItemDao.getLatestVerifyModelByAdvanceIds(advanceIds);
//
//                for (AdvanceBillVerifyItemModel verifyItemModel : verifyModelByAdvanceIds) {
//                    verifyDateMap.put(verifyItemModel.getAdvanceId(), verifyItemModel);
//                }
//
//            }
//
//            for (AdvanceBillItemModel advanceBillItemModel : advanceBillItemModels) {
//                AdvanceBillModel advanceBillModel = advanceBillModelMap.get(advanceBillItemModel.getAdvanceId());
//                ReceiveBillVerifyItemModel verifyItemModel = new ReceiveBillVerifyItemModel();
//                verifyItemModel.setAdvanceId(advanceBillItemModel.getId());
//                verifyItemModel.setAdvanceCode(advanceBillModel.getCode());
//                verifyItemModel.setPrice(advanceBillItemModel.getAmount());
//                verifyItemModel.setType(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_1);
//                verifyItemModel.setReceiceNo(advanceBillItemModel.getRelCode());
//                verifyItemModel.setBillId(form.getBillId());
//
//                AdvanceBillOperateItemModel operateItemModel = auditDateMap.get(advanceBillItemModel.getAdvanceId());
//                if (operateItemModel != null) {
//                    verifyItemModel.setVerifyDate(new Timestamp(operateItemModel.getOperateDate().getTime()));
//                    verifyItemModel.setVerifyId(operateItemModel.getOperateId());
//                    verifyItemModel.setVerifier(operateItemModel.getOperater());
//                }
//
//                AdvanceBillVerifyItemModel advanceBillVerifyItemModel = verifyDateMap.get(advanceBillItemModel.getAdvanceId());
//                if (advanceBillVerifyItemModel != null) {
//                    verifyItemModel.setReceiveDate(new Timestamp(verifyItemModel.getVerifyDate().getTime()));
//                }
//                verifyItemModels.add(verifyItemModel);
//            }
//        }

//        if (!form.getAdvanceIds().isEmpty()) {
        //删除原来的勾稽关系
//        ReceiveBillVerifyItemModel verifyItemModel = new ReceiveBillVerifyItemModel();
//        verifyItemModel.setType(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_1);
//        verifyItemModel.setBillId(form.getBillId());
//        receiveBillVerifyItemDao.delVerify(verifyItemModel);
//
//        for (ReceiveBillVerifyItemModel receiveBillVerifyItemModel : verifyItemModels) {
//            receiveBillVerifyItemDao.save(receiveBillVerifyItemModel);
//        }
//        }

        if (!DERP_ORDER.RECEIVEBILL_ORDERTYPE_2.equals(receiveBillModel.getOrderType())) {
            receiveBillModel.setRelCode(StringUtils.join(relCodes.toArray(), "&"));
        }
        receiveBillDao.modify(receiveBillModel);

        receiveBillItemDao.delItems(receiveBillModel.getId(), null);
        if (!itemModels.isEmpty()) {
            receiveBillItemDao.batchSave(itemModels);
        }

        retMap.put("code", "00");
        retMap.put("message", "成功");
        return retMap;
    }

    @Override
    public Map importReceiveCostItems(List<List<Map<String, String>>> data, Long billId, User user) throws SQLException {
        List<ImportErrorMessage> resultList = new ArrayList<>();

        Integer success = 0;
        Integer failure = 0;

        ReceiveBillModel receiveBillModel = receiveBillDao.searchById(billId);

        if (receiveBillModel == null) {
            setErrorMsg(0, "应收账单没有找到", resultList);
            failure += 1;
        }

        List<Map<String, String>> objects = data.get(0);

        if (failure == 0) {
            for (int j = 0; j < objects.size(); j++) {

                Map<String, String> map = objects.get(j);

                String projectCode = map.get("费项编码");
                if (StringUtils.isBlank(projectCode)) {
                    setErrorMsg(j + 1, "费项编码不能为空", resultList);
                    failure += 1;
                    continue;
                }

                String billType = map.get("类型");
                if (StringUtils.isBlank(billType)) {
                    setErrorMsg(j + 1, "类型不能为空", resultList);
                    failure += 1;
                    continue;
                }

                if (!("补款".equals(billType) || "扣款".equals(billType))) {
                    setErrorMsg(j + 1, "类型错误", resultList);
                    failure += 1;
                    continue;
                }

                String parentBrandCode = map.get("母品牌编码");
                if (StringUtils.isBlank(parentBrandCode)) {
                    setErrorMsg(j + 1, "母品牌编码不能为空", resultList);
                    failure += 1;
                    continue;
                }

                String taxAmount = map.get("费用金额（含税）");
                if (StringUtils.isBlank(taxAmount)) {
                    setErrorMsg(j + 1, "结算金额（含税）不能为空", resultList);
                    failure += 1;
                    continue;
                }

            }
        }

        List<ReceiveBillCostItemDTO> costItemDTOS = new ArrayList<>();
        if (failure == 0) {

            for (int j = 0; j < objects.size(); j++) {

                Map<String, String> map = objects.get(j);

                String projectCode = map.get("费项编码");
                String billTypeLabel = map.get("类型");
                String poNo = map.get("PO号");
                String goodsNo = map.get("商品货号");
                String parentBrandCode = map.get("母品牌编码");
                String num = map.get("数量");
                String taxAmount = map.get("费用金额（含税）");
                String taxRate = map.get("税率");
                String invoiceDescription = map.get("发票描述");
                String remark = map.get("备注");
                String platformGoodsCode = map.get("平台商品编码");

                String billType = (String) DERP_ORDER.getKeyByLabel(DERP_ORDER.receiveBillCost_billTypeList, billTypeLabel);

                ReceiveBillCostItemDTO costItemDTO = new ReceiveBillCostItemDTO();
                costItemDTO.setBillType(billType);
                costItemDTO.setPoNo(poNo);
                costItemDTO.setGoodsNo(goodsNo);
                costItemDTO.setParentBrandCode(parentBrandCode);
                costItemDTO.setInvoiceDescription(invoiceDescription);
                costItemDTO.setRemark(remark);
                costItemDTO.setProjectCode(projectCode);
                costItemDTO.setPlatformGoodsCode(platformGoodsCode);

                if (StringUtils.isNotBlank(num)) {
                    costItemDTO.setNum(Integer.valueOf(num));
                }else{
                    costItemDTO.setNum(0);
                }

                if (StringUtils.isBlank(taxRate)) {
                    taxRate = "0";
                }
                costItemDTO.setTaxRate(Double.valueOf(taxRate));

                BigDecimal price = new BigDecimal(taxAmount).divide(new BigDecimal("1").add(new BigDecimal(taxRate)), 2, BigDecimal.ROUND_HALF_UP);
                BigDecimal tax = new BigDecimal(taxAmount).subtract(price);
                costItemDTO.setTax(tax);
                costItemDTO.setTaxAmount(new BigDecimal(taxAmount));
                costItemDTO.setPrice(price);

                Map<String, String> validateMap = validateCostItems(costItemDTO, receiveBillModel);
                if ("01".equals(validateMap.get("code"))) {
                    setErrorMsg(j + 1, validateMap.get("message"), resultList);
                    failure += 1;
                    continue;
                }

                costItemDTOS.add(costItemDTO);
            }
        }

        if (failure == 0) {
            success = objects.size();
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msgList", resultList);
        map.put("costItemList", costItemDTOS);
        map.put("success", success);
        map.put("failure", failure);
        return map;
    }

    @Override
    public Map importAddReceiveItems(List<List<Map<String, String>>> data, Long customerId, User user) throws SQLException {
        {
            List<ImportErrorMessage> resultList = new ArrayList<>();

            Integer success = 0;
            Integer failure = 0;

            ReceiveBillModel receiveBillModel = new ReceiveBillModel();
            receiveBillModel.setCustomerId(customerId);
            receiveBillModel.setMerchantId(user.getMerchantId());
            receiveBillModel.setMerchantName(user.getMerchantName());

            List<Map<String, String>> objects = data.get(0);

            if (failure == 0) {
                for (int j = 0; j < objects.size(); j++) {

                    Map<String, String> map = objects.get(j);

                    String projectCode = map.get("费项编码");
                    if (StringUtils.isBlank(projectCode)) {
                        setErrorMsg(j + 1, "费项编码不能为空", resultList);
                        failure += 1;
                        continue;
                    }

                    String billType = map.get("类型");
                    if (StringUtils.isBlank(billType)) {
                        setErrorMsg(j + 1, "类型不能为空", resultList);
                        failure += 1;
                        continue;
                    }

                    if (!("补款".equals(billType) || "扣款".equals(billType))) {
                        setErrorMsg(j + 1, "类型错误", resultList);
                        failure += 1;
                        continue;
                    }

                    String parentBrandCode = map.get("母品牌编码");
                    if (StringUtils.isBlank(parentBrandCode)) {
                        setErrorMsg(j + 1, "母品牌编码不能为空", resultList);
                        failure += 1;
                        continue;
                    }

                    String taxAmount = map.get("费用金额（含税）");
                    if (StringUtils.isBlank(taxAmount)) {
                        setErrorMsg(j + 1, "结算金额（不含税）不能为空", resultList);
                        failure += 1;
                        continue;
                    }

                }
            }

            List<ReceiveBillCostItemDTO> costItemDTOS = new ArrayList<>();
            if (failure == 0) {

                for (int j = 0; j < objects.size(); j++) {

                    Map<String, String> map = objects.get(j);

                    String projectCode = map.get("费项编码");
                    String billTypeLabel = map.get("类型");
                    String poNo = map.get("PO号");
                    String goodsNo = map.get("商品货号");
                    String parentBrandCode = map.get("母品牌编码");
                    String num = map.get("数量");
                    String taxAmount = map.get("费用金额（含税）");
                    String taxRate = map.get("税率");
                    String invoiceDescription = map.get("发票描述");
                    String remark = map.get("备注");
                    String platformGoodsCode = map.get("平台商品编码");

                    String billType = (String) DERP_ORDER.getKeyByLabel(DERP_ORDER.receiveBillCost_billTypeList, billTypeLabel);

                    ReceiveBillCostItemDTO costItemDTO = new ReceiveBillCostItemDTO();
                    costItemDTO.setBillType(billType);
                    costItemDTO.setPoNo(poNo);
                    costItemDTO.setGoodsNo(goodsNo);
                    costItemDTO.setParentBrandCode(parentBrandCode);
                    costItemDTO.setInvoiceDescription(invoiceDescription);
                    costItemDTO.setRemark(remark);
                    costItemDTO.setProjectCode(projectCode);
                    costItemDTO.setPlatformGoodsCode(platformGoodsCode);

                    if (StringUtils.isNotBlank(num)) {
                        costItemDTO.setNum(Integer.valueOf(num));
                    }

                    if (StringUtils.isBlank(taxRate)) {
                        taxRate = "0";
                    }
                    costItemDTO.setTaxRate(Double.valueOf(taxRate));
                    costItemDTO.setTaxRate(Double.valueOf(taxRate));

                    BigDecimal price = new BigDecimal(taxAmount).divide(new BigDecimal("1").add(new BigDecimal(taxRate)), 2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal tax = new BigDecimal(taxAmount).subtract(price);
                    costItemDTO.setTax(tax);
                    costItemDTO.setTaxAmount(new BigDecimal(taxAmount));
                    costItemDTO.setPrice(price);

                    Map<String, String> validateMap = validateCostItems(costItemDTO, receiveBillModel);
                    if ("01".equals(validateMap.get("code"))) {
                        setErrorMsg(j + 1, validateMap.get("message"), resultList);
                        failure += 1;
                        continue;
                    }

                    costItemDTOS.add(costItemDTO);
                }
            }

            if (failure == 0) {
                success = objects.size();
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("msgList", resultList);
            map.put("costItemList", costItemDTOS);
            map.put("success", success);
            map.put("failure", failure);
            return map;
        }
    }

    @Override
    public List<ReceiveBillCostItemDTO> exportListCostItem(String json) throws SQLException {
        List<ReceiveBillCostItemDTO> itemList = new ArrayList<>();
        // 解析json
        net.sf.json.JSONObject jsonObj = net.sf.json.JSONObject.fromObject(json);
        // 解析表体数据
        net.sf.json.JSONArray itemArr = net.sf.json.JSONArray.fromObject(jsonObj.get("itemList"));
        for (int i = 0; i < itemArr.size(); i++) {
            net.sf.json.JSONObject job = itemArr.getJSONObject(i);
            if (job.isNullObject() || job.isEmpty()) {
                continue;
            }

            Integer projectId = (Integer) job.get("projectId");
            String billType = (String) job.get("billType");
            String poNo = (String) job.get("poNo");
            String goodsNo = (String) job.get("goodsNo");
            String taxRateStr = (String) job.get("taxRate");
            String numStr = (String) job.get("num");
            String taxAmountStr = (String) job.get("taxAmount");
            String parentBrandId = (String) job.get("brandParent");
            String invoiceDescription = (String) job.get("invoiceDescription");
            String remark = (String) job.get("remark");
            String platformGoodsCode = (String) job.get("platformGoodsCode");

            Integer num = StringUtils.isBlank(numStr) ? null : Integer.valueOf(numStr.trim());
            Double taxRate = StringUtils.isBlank(taxRateStr.trim()) ? null : Double.valueOf(taxRateStr.trim());
            BigDecimal taxAmount = StringUtils.isBlank(taxAmountStr) ? new BigDecimal("0") : new BigDecimal(taxAmountStr);

            // 注入数据
            ReceiveBillCostItemDTO itemDTO = new ReceiveBillCostItemDTO();
            if (projectId != null) {
                SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(Long.valueOf(projectId));
                itemDTO.setProjectCode(settlementConfigModel.getProjectCode());
            }

            if (StringUtils.isNotBlank(parentBrandId)) {
                Map<String, Object> params = new HashMap<>();
                params.put("brandSuperiorId", Long.valueOf(parentBrandId));
                BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(params);
                itemDTO.setParentBrandCode(brandSuperior.getNcCode());
            }

            itemDTO.setBillType(DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBillCost_billTypeList, billType));
            itemDTO.setPoNo(poNo);
            itemDTO.setGoodsNo(goodsNo);
            itemDTO.setTaxRate(taxRate);
            itemDTO.setNum(num);
            itemDTO.setTaxAmount(taxAmount);
            itemDTO.setInvoiceDescription(invoiceDescription);
            itemDTO.setRemark(remark);
            itemDTO.setPlatformGoodsCode(platformGoodsCode);


            itemList.add(itemDTO);

        }
        return itemList;
    }

    @Override
    public String generateInvoiceHtml(Long tempId, String ids, String invoiceSource, User user) throws Exception {

        FileTempModel fileTempModel = fileTempDao.searchById(tempId);

        if (fileTempModel == null) {
            throw new RuntimeException("发票模板不存在！");
        }

        if (StringUtils.isBlank(fileTempModel.getToUrl())) {
            throw new RuntimeException("发票模板地址不能为空！");
        }

        List<Long> billIds = StrUtils.parseIds(ids);

        List<ReceiveBillDTO> billDTOS = receiveBillDao.listBillByRelIds(billIds);

        String codes = billDTOS.stream().map(ReceiveBillDTO::getCode).collect(Collectors.joining());
        String poNos = billDTOS.stream().map(ReceiveBillDTO::getPoNo).collect(Collectors.joining());
        String relCodes = billDTOS.stream().map(ReceiveBillDTO::getRelCode).collect(Collectors.joining());

        Map<String, Object> params = new HashMap<>();
        params.put("customerid", billDTOS.get(0).getCustomerId());
        params.put("cusType", "1");
        CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(params);

        Map<String, Object> param = new HashMap<>();
        param.put("merchantId", user.getMerchantId());
        MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(param);

        InvoiceTemplateDTO templateDTO = new InvoiceTemplateDTO();
        templateDTO.setFileTempId(tempId);
        templateDTO.setFileTempCode(fileTempModel.getCode());
        templateDTO.setIds(ids);
        templateDTO.setCurrency(billDTOS.get(0).getCurrency());
        templateDTO.setPoNos(poNos);
        templateDTO.setCodes(codes);
        templateDTO.setRelCodes(relCodes);
        templateDTO.setMerchantId(merchantInfo.getMerchantId());
        templateDTO.setMerchantEnglishName(merchantInfo.getEnglishName());
        templateDTO.setMerchantInvoiceName(merchantInfo.getFullName());
        templateDTO.setEnglishRegisteredAddress(merchantInfo.getEnglishRegisteredAddress());
        templateDTO.setBankAccount(merchantInfo.getBankAccount());
        templateDTO.setSwiftCode(merchantInfo.getSwiftCode());
        templateDTO.setBankAddress(merchantInfo.getBankAddress());
        templateDTO.setBeneficiaryName(merchantInfo.getBeneficiaryName());
        templateDTO.setDepositBank(merchantInfo.getDepositBank());
        templateDTO.setCompanyAddr(customerInfo.getCompanyAddr());
        templateDTO.setCustomerId(customerInfo.getCustomerid());
        templateDTO.setCustomerEnName(customerInfo.getEnName());
        templateDTO.setCustomerName(customerInfo.getName());
        templateDTO.setCusBankAccount(customerInfo.getBankAccount());
        templateDTO.setCusDepositBank(customerInfo.getDepositBank());
        templateDTO.setTaxNo(customerInfo.getTaxNo());
        templateDTO.setEnBusinessAddress(customerInfo.getEnBusinessAddress());
        templateDTO.setInvoiceStatus(invoiceSource);

        ReceiveBillOperateModel operateModel = receiveBillOperateDao.getLatestOperate(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_1, billIds);
        if (operateModel != null) {
            templateDTO.setInvoiceDate(TimeUtils.format(operateModel.getOperateDate(), "yyyy/MM/dd"));
        }

        if (fileTempModel.getToUrl().contains("WEIPIN")) {//唯品发票取值
            listApiWPItemInfos(billIds, templateDTO, user);
        } else if (fileTempModel.getToUrl().contains("KAOLA")) { //考拉
            listApiKLItemInfos(billIds, templateDTO);
        } else if (fileTempModel.getToUrl().contains("YIZI")) { //亿滋
            listApiYLItemInfos(billIds, templateDTO ,user);
        } else if (fileTempModel.getToUrl().contains("JINGDONG")) { //京东
            listApiJDItemInfos(billIds, templateDTO, user);
        } else {
            listApiItemInfos(billIds, templateDTO, user);
        }

        String templatePath = "classpath:/customsTemplate/" + fileTempModel.getToUrl() +".xlsx";
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource=resolver.getResource(templatePath);
        InputStream is = resource.getInputStream();
        Context context = new Context();
        context.putVar("dto", templateDTO);

        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        PoiTransformer transformer  = (PoiTransformer) jxlsHelper.createTransformer(is, null);
        transformer.setFullFormulaRecalculationOnOpening(true);//打开文件时将重新计算所有公式
        //添加自定义功能
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> funcs = new HashMap<String, Object>();
        funcs.put("timeUtils", new TimeUtils());
        JexlBuilder jb = new JexlBuilder();
        jb.namespaces(funcs);
        JexlEngine je = jb.create();
        evaluator.setJexlEngine(je);

        AreaBuilder areaBuilder = new XlsCommentAreaBuilder();
        areaBuilder.setTransformer(transformer);
        List<Area> xlsAreaList = areaBuilder.build();
        jxlsHelper.setHideTemplateSheet(true);
        for (Area xlsArea : xlsAreaList) {
            xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
        }
        Workbook wb = transformer.getWorkbook();

        String htmlExcel="";
        if (wb instanceof XSSFWorkbook) {
            XSSFWorkbook xWb = (XSSFWorkbook) wb;
            htmlExcel = ExcelConvertHTMLUtils.getExcelInfo(xWb,true);
        }else if(wb instanceof HSSFWorkbook){
            HSSFWorkbook hWb = (HSSFWorkbook) wb;
            htmlExcel = ExcelConvertHTMLUtils.getExcelInfo(hWb,true);
        }

        /*Document doc = Jsoup.parse(htmlExcel);

        Elements tables = doc.getElementsByTag("table");

        Element tabel = tables.get(0);

        Elements trs = tabel.getElementsByTag("tr");

        boolean itemTrFlag = false ;

        for (Element tr : trs) {

            tr.removeAttr("style") ;

            if(tr.html().contains("data-dom-type='goodsItem'")) {

                tr.remove();

                itemTrFlag = true ;

                continue ;
            }

            if(itemTrFlag) {
                tr.attr("data-dom-type", "goodsItem");
            }

        }*/

        return htmlExcel;
    }

    @Override
    public void saveContract(User user, InvoiceTemplateForm templateForm) throws Exception {

        List<Long> billIds = StrUtils.parseIds(templateForm.getIds());
        List<String> codes = Arrays.asList(templateForm.getCodes());

        List<ReceiveBillDTO> receiveBillDTOS = new ArrayList<>();
        List<PlatformStatementOrderModel> platformStatementOrderModels = new ArrayList<>();

        if (DERP_ORDER.RECEIVEINVOICE_SOURCE_1.equals(templateForm.getInvoiceStatus())) {

            receiveBillDTOS = receiveBillDao.listBillByRelIds(billIds);

            for (ReceiveBillDTO dto : receiveBillDTOS) {
                if (DERP_ORDER.RECEIVEBILL_INVOICESTATUS_01.equals(dto.getInvoiceStatus()) ||
                        DERP_ORDER.RECEIVEBILL_INVOICESTATUS_03.equals(dto.getInvoiceStatus())) {
                    throw new RuntimeException("应收账单：" + dto.getCode() + "已开票，不能重复开票");
                }
            }
        } else {
            for (String code : codes) {
                PlatformStatementOrderModel orderModel = new PlatformStatementOrderModel();
                orderModel.setBillCode(code);
                PlatformStatementOrderModel platformStatement = platformStatementOrderDao.searchByModel(orderModel);
                if (!DERP_ORDER.PLATFORM_STATEMENT_IS_INVOICE_0.equals(platformStatement.getIsInvoice())) {
                    throw new RuntimeException("平台结算单：" + platformStatement.getBillCode() + "已开票，不能重复开票");
                }
                platformStatementOrderModels.add(platformStatement);
            }
        }

        FileTempModel fileTempModel = fileTempDao.searchById(templateForm.getFileTempId());

        if (fileTempModel == null) {
            throw new RuntimeException("发票模板不存在！");
        }

        if (StringUtils.isBlank(fileTempModel.getToUrl())) {
            throw new RuntimeException("发票模板地址不能为空！");
        }

        //1.生成发票编码
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthStr = month > 9 ? String.valueOf(month) : "0" + month;
        String invoiceNoPrefix = null;
        if ("0000138".equals(user.getTopidealCode())) { //宝信
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_HNFH + year.substring(2);
        } else if ("1000000204".equals(user.getTopidealCode())) { //健太
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_QTOP + year.substring(2);
        } else if ("0000134".equals(user.getTopidealCode())) { //卓烨
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_TWKL + year.substring(2);
        } else if ("1000004669".equals(user.getTopidealCode())) { //元森泰
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_YSTA + year.substring(2);
        } else if ("1000000645".equals(user.getTopidealCode())) { //广旺
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_ABHG + year.substring(2);
        } else if ("1000000626".equals(user.getTopidealCode())) { //润佰
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_RYBZ + year.substring(2);
        } else if ("1000005377".equals(user.getTopidealCode())) { //万代
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_WAMD + year.substring(2);
        } else if ("1000052958".equals(user.getTopidealCode())) { //轩盛有限公司
            invoiceNoPrefix = DERP.UNIQUEID_PREFIX_HIGH + year.substring(2);
        }
        Long invoiceValue = receiveInvoicenoDao.getMaxValue(invoiceNoPrefix);
        if (invoiceValue == null) {
            invoiceValue = 1L;
        } else {
            invoiceValue++;
        }
        String invoiceValueStr = String.format("%04d", invoiceValue);
        String invoiceNo = invoiceNoPrefix + monthStr + invoiceValueStr;
        //保存发票号当前值
        ReceiveInvoicenoModel receiveInvoicenoModel = new ReceiveInvoicenoModel();
        receiveInvoicenoModel.setInvoiceNoPrefix(invoiceNoPrefix);
        receiveInvoicenoModel.setValue(invoiceValue);
        receiveInvoicenoDao.save(receiveInvoicenoModel);

        templateForm.setInvoiceNo(invoiceNo);

        //2.生成pdf并保存
        String templatePath = "classpath:/customsTemplate/" + fileTempModel.getToUrl() +"_INVOICE.xlsx";
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource(templatePath);
        InputStream is = resource.getInputStream();
        Context context = new Context();
        context.putVar("dto", templateForm);

        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        PoiTransformer transformer  = (PoiTransformer) jxlsHelper.createTransformer(is, null);
        transformer.setFullFormulaRecalculationOnOpening(true);//打开文件时将重新计算所有公式
        //添加自定义功能
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> funcs = new HashMap<String, Object>();
        funcs.put("timeUtils", new TimeUtils());
        JexlBuilder jb = new JexlBuilder();
        jb.namespaces(funcs);
        JexlEngine je = jb.create();
        evaluator.setJexlEngine(je);

        AreaBuilder areaBuilder = new XlsCommentAreaBuilder();
        areaBuilder.setTransformer(transformer);
        List<Area> xlsAreaList = areaBuilder.build();
        jxlsHelper.setHideTemplateSheet(true);
        for (Area xlsArea : xlsAreaList) {
            xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()), context);
        }
        Workbook wb = transformer.getWorkbook();

        //设置导出的页面的大小
        RectangleReadOnly pageSize = (RectangleReadOnly) PageSize.A4;
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Excel2PdfUtils.Excel2Pdf(wb, os, true, pageSize);

        AttachmentInfoMongo invoiceAttach = saveAttachmentFile(os, invoiceNo, user);

        //3.保存发票信息
        double totalAllAmountDouble = new DecimalFormat().parse(templateForm.getTotalAllAmount()).doubleValue();
        Timestamp invoiceDate = TimeUtils.getNow();
        ReceiveBillInvoiceModel receiveBillInvoiceModel = new ReceiveBillInvoiceModel();
        receiveBillInvoiceModel.setMerchantId(user.getMerchantId());
        receiveBillInvoiceModel.setMerchantName(user.getMerchantName());
        receiveBillInvoiceModel.setCreaterId(user.getId());
        receiveBillInvoiceModel.setCreater(user.getName());

        receiveBillInvoiceModel.setInvoiceNo(invoiceNo);
        if (DERP_ORDER.RECEIVEINVOICE_SOURCE_1.equals(templateForm.getInvoiceStatus())) {
            receiveBillInvoiceModel.setCustomerId(receiveBillDTOS.get(0).getCustomerId());
            receiveBillInvoiceModel.setCustomerName(receiveBillDTOS.get(0).getCustomerName());
            receiveBillInvoiceModel.setCurrency(receiveBillDTOS.get(0).getCurrency());
        } else {
            receiveBillInvoiceModel.setCustomerId(platformStatementOrderModels.get(0).getCustomerId());
            receiveBillInvoiceModel.setCustomerName(platformStatementOrderModels.get(0).getCustomerName());
            receiveBillInvoiceModel.setCurrency(platformStatementOrderModels.get(0).getCurrency());
        }

        receiveBillInvoiceModel.setInvoicePath(invoiceAttach.getAttachmentUrl());
        receiveBillInvoiceModel.setInvoiceAmount(new BigDecimal(String.valueOf(totalAllAmountDouble)));
        receiveBillInvoiceModel.setInvoiceDate(invoiceDate);
        receiveBillInvoiceModel.setStatus(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_01);
        receiveBillInvoiceModel.setInvoiceType(DERP_ORDER.RECEIVEBILLINVOICE_INVOICETYPE_0);
        if (DERP_ORDER.RECEIVEINVOICE_SOURCE_1.equals(templateForm.getInvoiceStatus())) {
            receiveBillInvoiceModel.setInvoiceRelCodes(templateForm.getCodes());
            receiveBillInvoiceModel.setInvoiceRelIds(templateForm.getIds());
        } else {
            receiveBillInvoiceModel.setRelStatementCodes(templateForm.getCodes());
        }
        Long id = receiveBillInvoiceDao.save(receiveBillInvoiceModel);

        //4.修改应收账单状态
        if (DERP_ORDER.RECEIVEINVOICE_SOURCE_1.equals(templateForm.getInvoiceStatus())) {
            for (ReceiveBillDTO dto : receiveBillDTOS) {
                ReceiveBillModel model = new ReceiveBillModel();
                model.setId(dto.getId());
                model.setInvoiceId(id);
                model.setInvoiceStatus(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_01);
                receiveBillDao.modify(model);

                //操作日志节点
                ReceiveBillOperateModel receiveBillOperateModel = new ReceiveBillOperateModel();
                receiveBillOperateModel.setBillId(dto.getId());
                receiveBillOperateModel.setOperateDate(invoiceDate);
                receiveBillOperateModel.setCreateDate(invoiceDate);
                receiveBillOperateModel.setOperateId(user.getId());
                receiveBillOperateModel.setOperator(user.getName());
                receiveBillOperateModel.setOperateNode(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_6);
                receiveBillOperateDao.save(receiveBillOperateModel);
            }
        } else {
            //平台结算单开票
            for (PlatformStatementOrderModel orderModel : platformStatementOrderModels) {
                PlatformStatementOrderModel platformStatement = new PlatformStatementOrderModel();
                platformStatement.setId(orderModel.getId());
                platformStatement.setIsInvoice(DERP_ORDER.PLATFORM_STATEMENT_IS_INVOICE_1);
                platformStatement.setInvoiceDate(receiveBillInvoiceModel.getInvoiceDate());
                platformStatement.setInvoiceDrawer(user.getName());
                platformStatement.setInvoiceDrawerId(user.getId());
                platformStatement.setInvoiceNo(invoiceNo);
                platformStatement.setInvoiceId(id);
                platformStatementOrderDao.modify(platformStatement);
            }
        }

        //5.保存发票文件到附件
        net.sf.json.JSONArray attArray = new net.sf.json.JSONArray();
        for (String code : codes) {
            Map<String, Object> attQueryMap = new HashMap<String, Object>();
            attQueryMap.put("relationCode", code);
            attQueryMap.put("creatorName", "账单同步");
            List<AttachmentInfoMongo> attList = attachmentInfoMongoDao.findAll(attQueryMap);
            for (AttachmentInfoMongo att : attList) {
                if (!DERP.DEL_CODE_006.equals(att.getStatus())) {
                    Map<String, Object> tempMap = new HashMap<String, Object>();
                    tempMap.put("attachmentName", att.getAttachmentName());
                    String attachmentUrl = ApolloUtil.orderWebhost + "/attachment/downloadFile.asyn?fileName="
                            + att.getAttachmentName() + "&url=" + URLEncoder.encode(att.getAttachmentUrl());
                    tempMap.put("attachmentUrl", attachmentUrl);
                    JSONObject attJson = new JSONObject();
                    attJson.putAll(tempMap);
                    attArray.add(attJson);
                }
            }
        }

        //6.发送邮件
        if (DERP_ORDER.RECEIVEINVOICE_SOURCE_1.equals(templateForm.getInvoiceStatus())) {

            Map<String, Object> invoiceMap = new HashMap<String, Object>();
            invoiceMap.put("attachmentName", invoiceNo + "应收账单发票.pdf");
            String invoiceUrl = ApolloUtil.orderWebhost + "/attachment/downloadFile.asyn?fileName="
                    + invoiceAttach.getAttachmentName() + "&url=" + URLEncoder.encode(invoiceAttach.getAttachmentUrl());
            invoiceMap.put("attachmentUrl", invoiceUrl);
            JSONObject attJson = new JSONObject();
            attJson.putAll(invoiceMap);
            attArray.add(attJson);

            //封装发送邮件JSON
            ReminderEmailUserDTO emailUserDTO = new ReminderEmailUserDTO() ;

            JsonConfig jsonConfig = new JsonConfig();
            getReceiveOperators(receiveBillDTOS.get(0).getId(), emailUserDTO, jsonConfig);

            emailUserDTO.setBuId(receiveBillDTOS.get(0).getBuId());
            emailUserDTO.setBuName(receiveBillDTOS.get(0).getBuName());
            emailUserDTO.setMerchantId(user.getMerchantId());
            emailUserDTO.setMerchantName(user.getMerchantName());
            emailUserDTO.setOrderCode(invoiceNo);
            emailUserDTO.setBusinessModuleType(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1);
            emailUserDTO.setTypeName(DERP_SYS.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_BuisList,
                    DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1));
            emailUserDTO.setType(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_5);
            emailUserDTO.setSupplier(receiveBillInvoiceModel.getCustomerName());
            emailUserDTO.setAmount(templateForm.getCurrency() + "&nbsp;" + templateForm.getTotalAllAmount());
            emailUserDTO.setDrawerId(user.getId());
            emailUserDTO.setUserName(user.getName());
            emailUserDTO.setAttArray(attArray);

            JSONObject emailJson = JSONObject.fromObject(emailUserDTO, jsonConfig);

            rocketMQProducer.send(emailJson.toString(), MQErpEnum.SEND_REMINDER_MAIL.getTopic(),
                    MQErpEnum.SEND_REMINDER_MAIL.getTags());
        }
    }

    @Override
    public Map<String, String> saveApiAddReceiveBill(ReceiveBillForm form, User user) throws Exception {
        Map<String, String> retMap = new HashMap<>();
        if (StringUtils.isBlank(form.getSortType())) {
            retMap.put("code", "01");
            retMap.put("msg", "分类不能为空！");
            return retMap;
        }
        if (StringUtils.isBlank(form.getBillMonth())) {
            retMap.put("code", "01");
            retMap.put("msg", "账单月份不能为空！");
            return retMap;
        }

        if (StringUtils.isBlank(form.getCurrency())) {
            retMap.put("code", "01");
            retMap.put("msg", "结算币种不能为空！");
            return retMap;
        }

        if (form.getCustomerId() == null) {
            retMap.put("code", "01");
            retMap.put("msg", "客户不能为空！");
            return retMap;
        }

        if (form.getBuId() == null) {
            retMap.put("code", "01");
            retMap.put("msg", "事业部不能为空！");
            return retMap;
        }
        List<ReceiveBillCostItemModel> costItemModels = new ArrayList<>();
        List<ReceiveBillItemModel> itemModels = new ArrayList<>();
        List<ReceiveBillCostItemForm> costItemList = form.getCostItemList();
        for (ReceiveBillCostItemForm costItemForm : costItemList) {

            SettlementConfigModel settlementConfigModel = settlementConfigDao.searchById(Long.valueOf(costItemForm.getProjectId()));
            if (settlementConfigModel == null) {
                retMap.put("code", "01");
                retMap.put("msg", "费用项目不存在！");
                return retMap;
            }

            ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());
            if (paymentSubjectModel == null) {
                retMap.put("code", "01");
                retMap.put("msg", "NC收支费项不存在");
                return retMap;
            }

            if (StringUtils.isBlank(costItemForm.getBrandParent())) {
                retMap.put("code", "01");
                retMap.put("msg", "母品牌不能为空！");
                return retMap;
            }

            if (settlementConfigModel.getProjectName().equals("经销业务TO B收入") && StringUtils.isBlank(costItemForm.getGoodsId())) {
                retMap.put("code", "01");
                retMap.put("msg", "费用项目为“经销业务TO B收入”时，商品不能为空！");
                return retMap;
            }

            if (settlementConfigModel.getProjectName().equals("经销业务TO B收入") && costItemForm.getNum() == null) {
                retMap.put("code", "01");
                retMap.put("msg", "费用项目为“经销业务TO B收入”时，数量不能为空！");
                return retMap;
            }

            //只要是对应“经销业务TO B收入”均对应进入应收明细
            if (settlementConfigModel.getProjectName().equals("经销业务TO B收入")) {
                ReceiveBillItemModel itemModel = new ReceiveBillItemModel();
                itemModel.setDataSource(DERP_ORDER.RECEIVEBILLITEM_DATASOURCE_1);
                itemModel.setProjectId(settlementConfigModel.getId());
                itemModel.setProjectName(settlementConfigModel.getProjectName());
                itemModel.setInvoiceDescription(costItemForm.getInvoiceDescription());
                if (DERP_ORDER.RECEIVEBILL_SORTTYPE_2.equals(form.getSortType())) {
                    itemModel.setVerifyPlatformCode(costItemForm.getStorePlatformCode());
                }

                //nc
                itemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
                itemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
                //科目
                itemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                itemModel.setSubjectName(paymentSubjectModel.getSubName());
                if (costItemForm.getGoodsId() != null) {
                    itemModel.setGoodsId(Long.valueOf(costItemForm.getGoodsId()));
                    itemModel.setGoodsNo(costItemForm.getGoodsNo());
                    itemModel.setGoodsName(costItemForm.getGoodsName());
                }

                if (costItemForm.getBrandParent() != null) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("brandSuperiorId", Long.valueOf(costItemForm.getBrandParent()));
                    BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(params);
                    if (brandSuperior != null) {
                        itemModel.setParentBrandName(brandSuperior.getName());
                        itemModel.setParentBrandId(brandSuperior.getBrandSuperiorId());
                        itemModel.setParentBrandCode(brandSuperior.getNcCode());
                    }
                }

                itemModel.setPoNo(costItemForm.getPoNo());
                if (costItemForm.getNum() != null) {
                    itemModel.setNum(costItemForm.getNum());
                }


                itemModel.setTaxRate(Double.valueOf(costItemForm.getTaxRate()));
                BigDecimal price = new BigDecimal("0");
                if (StringUtils.isNotBlank(costItemForm.getTaxRate()) && StringUtils.isNotBlank(costItemForm.getTaxAmount())) {
                    price = new BigDecimal(costItemForm.getTaxAmount()).divide(new BigDecimal("1").add(new BigDecimal(costItemForm.getTaxRate())), 2, BigDecimal.ROUND_HALF_UP);
                }

                BigDecimal tax = new BigDecimal(costItemForm.getTaxAmount()).subtract(price);
                itemModel.setTax(tax);

                if (DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_1.equals(costItemForm.getBillType())) {
                    itemModel.setTaxAmount(new BigDecimal(costItemForm.getTaxAmount()).negate());
                    itemModel.setPrice(price.negate());
                } else {
                    itemModel.setTaxAmount(new BigDecimal(costItemForm.getTaxAmount()));
                    itemModel.setPrice(price);
                }
                itemModels.add(itemModel);
            } else {
                ReceiveBillCostItemModel receiveBillCostItemModel = new ReceiveBillCostItemModel();
                receiveBillCostItemModel.setProjectId(settlementConfigModel.getId());
                receiveBillCostItemModel.setProjectName(settlementConfigModel.getProjectName());
                receiveBillCostItemModel.setRemark(costItemForm.getRemark());
                receiveBillCostItemModel.setPlatformGoodsCode(costItemForm.getPlatformGoodsCode());
                receiveBillCostItemModel.setInvoiceDescription(costItemForm.getInvoiceDescription());
                if (DERP_ORDER.RECEIVEBILL_SORTTYPE_2.equals(form.getSortType())) {
                    receiveBillCostItemModel.setVerifyPlatformCode(costItemForm.getStorePlatformCode());
                }
                //nc
                receiveBillCostItemModel.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
                receiveBillCostItemModel.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
                //科目
                receiveBillCostItemModel.setSubjectCode(paymentSubjectModel.getSubCode());
                receiveBillCostItemModel.setSubjectName(paymentSubjectModel.getSubName());
                if (StringUtils.isNotBlank(costItemForm.getGoodsId())) {
                    receiveBillCostItemModel.setGoodsId(Long.valueOf(costItemForm.getGoodsId()));
                    receiveBillCostItemModel.setGoodsNo(costItemForm.getGoodsNo());
                    receiveBillCostItemModel.setGoodsName(costItemForm.getGoodsName());
                }
                if (StringUtils.isNotBlank(costItemForm.getBrandParent())) {
                    Map<String, Object> params = new HashMap<>();
                    params.put("brandSuperiorId", Long.valueOf(costItemForm.getBrandParent()));
                    BrandSuperiorMongo brandSuperior = brandSuperiorMongoDao.findOne(params);
                    if (brandSuperior != null) {
                        receiveBillCostItemModel.setParentBrandName(brandSuperior.getName());
                        receiveBillCostItemModel.setParentBrandId(brandSuperior.getBrandSuperiorId());
                        receiveBillCostItemModel.setParentBrandCode(brandSuperior.getNcCode());
                    }
                }
                receiveBillCostItemModel.setPoNo(costItemForm.getPoNo());
                if (costItemForm.getNum() != null) {
                    receiveBillCostItemModel.setNum(costItemForm.getNum());
                }
                receiveBillCostItemModel.setTaxAmount(new BigDecimal(costItemForm.getTaxAmount()));
                receiveBillCostItemModel.setBillType(costItemForm.getBillType());
                receiveBillCostItemModel.setTaxRate(Double.valueOf(costItemForm.getTaxRate()));
                BigDecimal price = new BigDecimal("0");
                if (StringUtils.isNotBlank(costItemForm.getTaxRate()) && StringUtils.isNotBlank(costItemForm.getTaxAmount())) {
                    price = new BigDecimal(costItemForm.getTaxAmount()).divide(new BigDecimal("1").add(new BigDecimal(costItemForm.getTaxRate())), 2, BigDecimal.ROUND_HALF_UP);
                }

                BigDecimal tax = new BigDecimal(costItemForm.getTaxAmount()).subtract(price);
                receiveBillCostItemModel.setTax(tax);
                receiveBillCostItemModel.setPrice(price);
                costItemModels.add(receiveBillCostItemModel);
            }

        }
        Map<String, Object> params = new HashMap<>();
        params.put("customerid", form.getCustomerId());
        params.put("cusType", "1");
        CustomerInfoMogo customerInfo = customerInfoMongoDao.findOne(params);
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("customerId", form.getCustomerId());
        customerParams.put("merchantId", user.getMerchantId());
        CustomerMerchantRelMongo customerRel = customerMerchantRelMongoDao.findOne(customerParams);
        if (customerRel == null) {
            retMap.put("code", "01");
            retMap.put("msg", "客户关联不存在！");
            return retMap;
        }
        Map<String, Object> buMap = new HashMap<>();
        buMap.put("buId", form.getBuId());
        buMap.put("merchantId", user.getMerchantId());
        buMap.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);
        MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(buMap);

        ReceiveBillModel model = new ReceiveBillModel();
        model.setCustomerId(form.getCustomerId());
        model.setCustomerName(customerInfo.getName());
        model.setBuId(form.getBuId());
        model.setBuName(merchantBuRelMongo.getBuName());
        model.setBillStatus(DERP_ORDER.RECEIVEBILL_BILLSTATUS_00);
        model.setIsAddWorn(DERP_ORDER.RECEIVEBILL_ISADDWORN_1);
        model.setCurrency(form.getCurrency());
        model.setBillDate(TimeUtils.parse(form.getBillMonth(), "yyyy-MM"));
        model.setCreater(user.getName());
        model.setCreaterId(user.getId());
        model.setCreateDate(TimeUtils.getNow());
        model.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_YSZD));
        model.setMerchantId(user.getMerchantId());
        model.setMerchantName(user.getMerchantName());
        model.setInvoiceStatus(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_00);
        model.setNcStatus(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10);
        model.setSaleModel(DERP_ORDER.RECEIVEBILL_SALEMODEL_009);
        model.setSortType(form.getSortType());
        model.setNcChannelCode(customerInfo.getNcChannelCode());
        model.setSettlementType("1");
        Long id = receiveBillDao.save(model);
        for (ReceiveBillItemModel itemModel : itemModels) {
            itemModel.setBillId(id);
        }
        for (ReceiveBillCostItemModel itemModel : costItemModels) {
            itemModel.setBillId(id);
        }

        int pageSize = 1000;
        for (int i = 0; i < itemModels.size(); ) {
            int pageSub = (i + pageSize) < itemModels.size() ? (i + pageSize) : itemModels.size();
            receiveBillItemDao.batchSave(itemModels.subList(i, pageSub));
            i = pageSub;
        }
        for (int i = 0; i < costItemModels.size(); ) {
            int pageSub = (i + pageSize) < costItemModels.size() ? (i + pageSize) : costItemModels.size();
            receiveBillCostItemDao.batchSave(costItemModels.subList(i, pageSub));
            i = pageSub;
        }
        retMap.put("code", "00");
        retMap.put("msg", "保存成功");
        retMap.put("billId", id.toString());
        return retMap;
    }

    @Override
    public List<ReceiveBillItemDTO> chooseOrder(User user, Long billId, String relCodes) throws SQLException {

        ReceiveBillModel receiveBillModel = receiveBillDao.searchById(billId);

        SettlementConfigModel configModel = new SettlementConfigModel();
        configModel.setProjectName("经销业务TO B收入");
        configModel.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
        configModel.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
        SettlementConfigModel toBSettlementConfigModel = settlementConfigDao.searchByModel(configModel);
        if (toBSettlementConfigModel == null) {
            throw new RuntimeException("没有找到“经销业务TO B收入”的费项配置");
        }

        ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(toBSettlementConfigModel.getPaymentSubjectId());

        Map<Long, BrandParentMongo> brandParentMongoMap = new HashMap<>();

        List<String> relCodeList = Arrays.asList(relCodes.split(","));
        List<ReceiveBillItemDTO> itemDTOS = new ArrayList<>();

        if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_1.equals(receiveBillModel.getOrderType())) {

            for (String relCode : relCodeList) {
                //上架单
                ShelfModel shelfModel = new ShelfModel();
                shelfModel.setCode(relCode);
                shelfModel.setMerchantId(user.getMerchantId());
                ShelfModel existModel = shelfDao.searchByModel(shelfModel);
                if (existModel == null) {
                    throw new RuntimeException("业务单号:" + relCode + "关联的上架单不存在");
                }

                SaleShelfModel saleShelfModel = new SaleShelfModel();
                saleShelfModel.setShelfId(existModel.getId());
                List<SaleShelfModel> saleShelfModels = saleShelfDao.list(saleShelfModel);

                SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
                saleOrderItemModel.setOrderId(existModel.getSaleOrderId());
                //查询销售订单表体（取上架商品对应销售金额）
                List<SaleOrderItemModel> saleOrderItemModels = saleOrderItemDao.list(saleOrderItemModel);
                Map<Long, BigDecimal> goodsMap = new HashMap<>();
                Map<Long, Double> taxMap = new HashMap<>();
                for (SaleOrderItemModel itemModel : saleOrderItemModels) {
                    goodsMap.put(itemModel.getId(), itemModel.getPrice());
                    taxMap.put(itemModel.getId(), itemModel.getTaxRate());
                }

                List<Map<String, Object>> alreadyVerifyItems = receiveBillItemDao.verifyItemList(existModel.getCode());

                Map<Long, Map<String, Object>> goodsIdsAlreadyVerifyMap = new HashMap<>();
                for (Map<String, Object> map : alreadyVerifyItems) {
                    Long goodsId = (Long) map.get("goodsId");
                    goodsIdsAlreadyVerifyMap.put(goodsId, map);
                }

                for (SaleShelfModel saleShelf : saleShelfModels) {
                    BigDecimal verifyAmount = new BigDecimal("0");
                    Integer verifyNum = 0;
                    if (goodsIdsAlreadyVerifyMap.containsKey(saleShelf.getGoodsId())) {
                        Map<String, Object> map = goodsIdsAlreadyVerifyMap.get(saleShelf.getGoodsId());
                        verifyAmount = map.get("verifyAmount") != null ? (BigDecimal) map.get("verifyAmount") : new BigDecimal("0");
                        BigDecimal verifyNumBD = map.get("verifyNum") != null ? (BigDecimal) map.get("verifyNum") : new BigDecimal("0");
                        verifyNum = verifyNumBD.intValue();
                    }

                    Integer shelfNum = 0;
                    if (saleShelf.getShelfNum() != null && saleShelf.getShelfNum() != 0) {
                        shelfNum += saleShelf.getShelfNum();
                    }

                    shelfNum -= verifyNum;
                    if (shelfNum != 0) {
                        BigDecimal price = goodsMap.get(saleShelf.getSaleItemId()) == null ? BigDecimal.ZERO : goodsMap.get(saleShelf.getSaleItemId());
                        BigDecimal amount = price.multiply(new BigDecimal(shelfNum));

                       /* if (amount.compareTo(BigDecimal.ZERO) == 0) {
                            continue;
                        }*/

                        ReceiveBillItemDTO receiveBillItemDTO = new ReceiveBillItemDTO();
                        receiveBillItemDTO.setCode(existModel.getCode());
                        receiveBillItemDTO.setProjectName(toBSettlementConfigModel.getProjectName());
                        receiveBillItemDTO.setProjectId(toBSettlementConfigModel.getId());
                        receiveBillItemDTO.setProjectCode(toBSettlementConfigModel.getProjectCode());
                        receiveBillItemDTO.setPaymentSubjectId(toBSettlementConfigModel.getPaymentSubjectId());
                        receiveBillItemDTO.setPaymentSubjectName(toBSettlementConfigModel.getPaymentSubjectName());
                        receiveBillItemDTO.setSubjectCode(paymentSubjectModel.getSubCode());
                        receiveBillItemDTO.setSubjectName(paymentSubjectModel.getSubName());
                        receiveBillItemDTO.setPoNo(saleShelf.getPoNo());
                        receiveBillItemDTO.setGoodsId(saleShelf.getGoodsId());
                        receiveBillItemDTO.setGoodsNo(saleShelf.getGoodsNo());
                        receiveBillItemDTO.setGoodsName(saleShelf.getGoodsName());
                        receiveBillItemDTO.setPrice(amount);
                        receiveBillItemDTO.setNum(shelfNum);
                        BrandMongo brand = brandMongoDao.getBrandMongo(saleShelf.getGoodsId());
                        if (brand != null) {
                            receiveBillItemDTO.setBrandId(brand.getBrandId());
                            receiveBillItemDTO.setBrandName(brand.getName());
                        }

                        if (taxMap.get(saleShelf.getSaleItemId()) != null) {
                            Double taxRate = taxMap.get(saleShelf.getSaleItemId());
                            receiveBillItemDTO.setTaxRate(taxRate);

                            BigDecimal tax = amount.multiply(new BigDecimal(taxRate.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal taxAmount = amount.add(tax);
                            receiveBillItemDTO.setTax(tax);
                            receiveBillItemDTO.setTaxAmount(taxAmount);
                        }


                        BrandParentMongo brandParent = brandParentMongoMap.get(saleShelf.getGoodsId());
                        if (brandParent == null) {
                            brandParent = brandParentMongoDao.getBrandParentMongo(saleShelf.getGoodsId());
                        }

                        if (brandParent != null) {
                            receiveBillItemDTO.setParentBrandName(brandParent.getSuperiorParentBrand());
                            receiveBillItemDTO.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                            receiveBillItemDTO.setParentBrandId(brandParent.getSuperiorParentBrandId());
                            brandParentMongoMap.put(saleShelf.getGoodsId(), brandParent);
                        }
                        itemDTOS.add(receiveBillItemDTO);
                    }
                }
            }

        } else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_2.equals(receiveBillModel.getOrderType())) {
            for (String relCode : relCodeList) {
                //账单出库单
                BillOutinDepotModel billOutinDepotModel = new BillOutinDepotModel();
                billOutinDepotModel.setMerchantId(user.getMerchantId());
                billOutinDepotModel.setBillCode(relCode);
                billOutinDepotModel.setBuId(receiveBillModel.getBuId());
                billOutinDepotModel.setCurrency(receiveBillModel.getCurrency());
                List<BillOutinDepotModel> billOutinDepotModels = billOutinDepotDao.list(billOutinDepotModel);
                if (billOutinDepotModels == null || billOutinDepotModels.size() == 0) {
                    throw new RuntimeException("业务单号" + relCode + "关联的账单出入库单不存在");
                }

                for (BillOutinDepotModel billOutinDepot : billOutinDepotModels) {
                    //不取国检出库和报废
                    if (DERP_ORDER.PROCESSINGTYPE_GJC.equals(billOutinDepot.getProcessingType()) ||
                            DERP_ORDER.PROCESSINGTYPE_BFC.equals(billOutinDepot.getProcessingType())) {
                        continue;
                    }
                    BillOutinDepotItemModel billOutinDepotItemModel = new BillOutinDepotItemModel();
                    billOutinDepotItemModel.setOutinDepotId(billOutinDepot.getId());

                    List<BillOutinDepotItemModel> itemModelList = billOutinDepotItemDao.list(billOutinDepotItemModel);
                    for (BillOutinDepotItemModel itemModel : itemModelList) {
                        BigDecimal price = itemModel.getAmount();
                        ReceiveBillItemDTO receiveBillIteDTO = new ReceiveBillItemDTO();
                        receiveBillIteDTO.setCode(billOutinDepot.getCode());
                        receiveBillIteDTO.setPoNo(itemModel.getPoNo());
                        receiveBillIteDTO.setGoodsId(itemModel.getGoodsId());
                        receiveBillIteDTO.setGoodsNo(itemModel.getGoodsNo());
                        receiveBillIteDTO.setGoodsName(itemModel.getGoodsName());
                        receiveBillIteDTO.setPlatformSku(itemModel.getPlatformSku());
                        receiveBillIteDTO.setProjectId(toBSettlementConfigModel.getId());
                        receiveBillIteDTO.setProjectName(toBSettlementConfigModel.getProjectName());
                        receiveBillIteDTO.setProjectCode(toBSettlementConfigModel.getProjectCode());
                        receiveBillIteDTO.setPaymentSubjectId(toBSettlementConfigModel.getPaymentSubjectId());
                        receiveBillIteDTO.setPaymentSubjectName(toBSettlementConfigModel.getPaymentSubjectName());
                        receiveBillIteDTO.setSubjectCode(paymentSubjectModel.getSubCode());
                        receiveBillIteDTO.setSubjectName(paymentSubjectModel.getSubName());
                        if (billOutinDepot.getProcessingType().matches(DERP_ORDER.PROCESSINGTYPE_PYR + "|" + DERP_ORDER.PROCESSINGTYPE_KTR)) {
                            receiveBillIteDTO.setPrice(price.multiply(new BigDecimal("-1")));
                            receiveBillIteDTO.setNum(itemModel.getNum() * (-1));
                        } else {
                            receiveBillIteDTO.setPrice(price);
                            receiveBillIteDTO.setNum(itemModel.getNum());
                        }

                        receiveBillIteDTO.setTaxAmount(receiveBillIteDTO.getPrice());
                        Double taxRate = itemModel.getTaxRate();
                        receiveBillIteDTO.setTaxRate(taxRate);

                        BigDecimal tax = receiveBillIteDTO.getPrice().multiply(new BigDecimal(taxRate.toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
                        BigDecimal taxAmount = receiveBillIteDTO.getPrice().add(tax);
                        receiveBillIteDTO.setTax(tax);
                        receiveBillIteDTO.setTaxAmount(taxAmount);

                        BrandMongo brand = brandMongoDao.getBrandMongo(itemModel.getGoodsId());
                        if (brand != null) {
                            receiveBillIteDTO.setBrandId(brand.getBrandId());
                            receiveBillIteDTO.setBrandName(brand.getName());
                        }
                        BrandParentMongo brandParent = brandParentMongoMap.get(itemModel.getGoodsId());
                        if (brandParent == null) {
                            brandParent = brandParentMongoDao.getBrandParentMongo(itemModel.getGoodsId());
                        }

                        if (brandParent != null) {
                            receiveBillIteDTO.setParentBrandName(brandParent.getSuperiorParentBrand());
                            receiveBillIteDTO.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                            receiveBillIteDTO.setParentBrandId(brandParent.getSuperiorParentBrandId());
                            brandParentMongoMap.put(itemModel.getGoodsId(), brandParent);
                        }
                        itemDTOS.add(receiveBillIteDTO);
                    }
                }
            }
        } else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_3.equals(receiveBillModel.getOrderType())) {
            for (String relCode : relCodeList) {
                //预售单
                PreSaleOrderModel preSaleOrderModel = new PreSaleOrderModel();
                preSaleOrderModel.setCode(relCode);
                preSaleOrderModel.setMerchantId(user.getMerchantId());
                PreSaleOrderModel preSaleOrder = preSaleOrderDao.searchByModel(preSaleOrderModel);
                if (preSaleOrder == null) {
                    throw new RuntimeException("业务单号" + relCode + "关联的预售单不存在");
                } else if (DERP_ORDER.PRESALEORDER_STATE_001.equals(preSaleOrder.getState())) {
                    throw new RuntimeException("业务单号:" + relCode + "关联的预售单状态不能为“待审核”");
                }

                PreSaleOrderItemModel preSaleOrderItemModel = new PreSaleOrderItemModel();
                preSaleOrderItemModel.setOrderId(preSaleOrder.getId());
                List<PreSaleOrderItemModel> preSaleOrderItemModels = preSaleOrderItemDao.list(preSaleOrderItemModel);
                for (PreSaleOrderItemModel itemModel : preSaleOrderItemModels) {
                    BigDecimal price = itemModel.getAmount();
                    ReceiveBillItemDTO receiveBillItemDTO = new ReceiveBillItemDTO();
                    receiveBillItemDTO.setCode(preSaleOrder.getCode());
                    receiveBillItemDTO.setProjectName(toBSettlementConfigModel.getProjectName());
                    receiveBillItemDTO.setProjectId(toBSettlementConfigModel.getId());
                    receiveBillItemDTO.setProjectCode(toBSettlementConfigModel.getProjectCode());
                    receiveBillItemDTO.setPaymentSubjectId(toBSettlementConfigModel.getPaymentSubjectId());
                    receiveBillItemDTO.setPaymentSubjectName(toBSettlementConfigModel.getPaymentSubjectName());
                    receiveBillItemDTO.setSubjectCode(paymentSubjectModel.getSubCode());
                    receiveBillItemDTO.setSubjectName(paymentSubjectModel.getSubName());
                    receiveBillItemDTO.setPoNo(preSaleOrder.getPoNo());
                    receiveBillItemDTO.setGoodsId(itemModel.getGoodsId());
                    receiveBillItemDTO.setGoodsNo(itemModel.getGoodsNo());
                    receiveBillItemDTO.setGoodsName(itemModel.getGoodsName());
                    receiveBillItemDTO.setPrice(price);
                    receiveBillItemDTO.setNum(itemModel.getNum());
                    receiveBillItemDTO.setTaxRate(itemModel.getTaxRate());
                    receiveBillItemDTO.setTax(itemModel.getTax());
                    receiveBillItemDTO.setTaxAmount(itemModel.getTaxAmount());
                    BrandMongo brand = brandMongoDao.getBrandMongo(itemModel.getGoodsId());
                    if (brand != null) {
                        receiveBillItemDTO.setBrandId(brand.getBrandId());
                        receiveBillItemDTO.setBrandName(brand.getName());
                    }
                    BrandParentMongo brandParent = brandParentMongoMap.get(itemModel.getGoodsId());
                    if (brandParent == null) {
                        brandParent = brandParentMongoDao.getBrandParentMongo(itemModel.getGoodsId());
                    }

                    if (brandParent != null) {
                        receiveBillItemDTO.setParentBrandName(brandParent.getSuperiorParentBrand());
                        receiveBillItemDTO.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                        receiveBillItemDTO.setParentBrandId(brandParent.getSuperiorParentBrandId());
                        brandParentMongoMap.put(itemModel.getGoodsId(), brandParent);
                    }
                    itemDTOS.add(receiveBillItemDTO);
                }
            }
        } else if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_7.equals(receiveBillModel.getOrderType())) {
            SettlementConfigModel pConfigModel = new SettlementConfigModel();
            pConfigModel.setProjectName("采购退货");
            pConfigModel.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);
            pConfigModel.setStatus(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1);
            SettlementConfigModel settlementConfigModel = settlementConfigDao.searchByModel(pConfigModel);
            ReceivePaymentSubjectModel pPaymentSubjectModel = receivePaymentSubjectDao.searchById(settlementConfigModel.getPaymentSubjectId());
            for (String relCode : relCodeList) {
                //销售退入库单
                SaleReturnIdepotModel saleReturnIdepotModel = new SaleReturnIdepotModel();
                saleReturnIdepotModel.setCode(relCode);
                SaleReturnIdepotModel returnIdepotModel = saleReturnIdepotDao.searchByModel(saleReturnIdepotModel);
                if (returnIdepotModel == null) {
                    throw new RuntimeException("业务单号" + relCode + "关联的销售退货入库单不存在");
                }

                SaleReturnOrderModel saleReturnOrderModel = saleReturnOrderDao.searchById(returnIdepotModel.getOrderId());

                if (saleReturnOrderModel == null) {
                    throw new RuntimeException("业务单号" + relCode + "关联的销售退订单不存在");
                }

                if (!(DERP_ORDER.SALERETURNORDER_STATUS_012.equals(saleReturnOrderModel.getStatus()) ||
                        DERP_ORDER.SALERETURNORDER_STATUS_007.equals(saleReturnOrderModel.getStatus()))) {
                    throw new RuntimeException("业务单号" + relCode + "关联的销售退订单状态不为“已入库”/“已完结”");
                }

                //销售退货单表体
                List<SaleReturnOrderItemModel> returnOrderItemModels = saleReturnOrderItemDao.searchByOrderId(saleReturnOrderModel.getId());
                //po+商品id对应的销售退明细
                Map<String , SaleReturnOrderItemModel> poGoodsPriceMap = new HashMap<>();
                for (SaleReturnOrderItemModel saleReturnOrderItemModel : returnOrderItemModels) {
                    String key = saleReturnOrderItemModel.getPoNo() + "_" + saleReturnOrderItemModel.getInGoodsId();
                    poGoodsPriceMap.put(key, saleReturnOrderItemModel);
                }

                //对应销售退货入库单表体
                SaleReturnIdepotItemModel idepotItemModel = new SaleReturnIdepotItemModel();
                idepotItemModel.setSreturnIdepotId(returnIdepotModel.getId());
                List<SaleReturnIdepotItemModel> idepotItemModels = saleReturnIdepotItemDao.list(idepotItemModel);

                for (SaleReturnIdepotItemModel saleReturnIdepotItemModel : idepotItemModels) {
                    ReceiveBillItemDTO receiveBillItemDTO = new ReceiveBillItemDTO();
                    receiveBillItemDTO.setCode(returnIdepotModel.getCode());
                    receiveBillItemDTO.setDataSource(DERP_ORDER.RECEIVEBILLITEM_DATASOURCE_0);
                    receiveBillItemDTO.setPoNo(saleReturnIdepotItemModel.getPoNo());
                    receiveBillItemDTO.setProjectId(settlementConfigModel.getId());
                    receiveBillItemDTO.setProjectName(settlementConfigModel.getProjectName());
                    receiveBillItemDTO.setProjectCode(settlementConfigModel.getProjectCode());
                    receiveBillItemDTO.setPaymentSubjectId(settlementConfigModel.getPaymentSubjectId());
                    receiveBillItemDTO.setPaymentSubjectName(settlementConfigModel.getPaymentSubjectName());
                    if (pPaymentSubjectModel != null) {
                        //科目
                        receiveBillItemDTO.setSubjectCode(pPaymentSubjectModel.getSubCode());
                        receiveBillItemDTO.setSubjectName(pPaymentSubjectModel.getSubName());
                    }
                    receiveBillItemDTO.setGoodsId(saleReturnIdepotItemModel.getInGoodsId());
                    receiveBillItemDTO.setGoodsNo(saleReturnIdepotItemModel.getInGoodsNo());
                    receiveBillItemDTO.setGoodsName(saleReturnIdepotItemModel.getInGoodsName());

                    Integer num = saleReturnIdepotItemModel.getReturnNum() == null ? 0 : saleReturnIdepotItemModel.getReturnNum();
                    Integer wornNum = saleReturnIdepotItemModel.getWornNum() == null ? 0 : saleReturnIdepotItemModel.getWornNum();
                    num += wornNum;

                    SaleReturnOrderItemModel returnOrderItemModel = poGoodsPriceMap.get(saleReturnIdepotItemModel.getPoNo() + "_" + saleReturnIdepotItemModel.getInGoodsId());

                    BigDecimal amount = returnOrderItemModel.getPrice().multiply(new BigDecimal(num)).negate();

                    BigDecimal tax = amount.multiply(new BigDecimal(returnOrderItemModel.getTaxRate().toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal taxAmount = amount.add(tax);

                    receiveBillItemDTO.setNum(num);
                    receiveBillItemDTO.setPrice(amount);
                    receiveBillItemDTO.setTaxRate(returnOrderItemModel.getTaxRate());
                    receiveBillItemDTO.setTax(tax);
                    receiveBillItemDTO.setTaxAmount(taxAmount);

                    BrandMongo brand = brandMongoDao.getBrandMongo(saleReturnIdepotItemModel.getInGoodsId());
                    if (brand != null) {
                        receiveBillItemDTO.setBrandId(brand.getBrandId());
                        receiveBillItemDTO.setBrandName(brand.getName());
                    }

                    BrandParentMongo brandParent = brandParentMongoMap.get(saleReturnIdepotItemModel.getInGoodsId());
                    if (brandParent == null) {
                        brandParent = brandParentMongoDao.getBrandParentMongo(saleReturnIdepotItemModel.getInGoodsId());
                    }

                    if (brandParent != null) {
                        receiveBillItemDTO.setParentBrandName(brandParent.getSuperiorParentBrand());
                        receiveBillItemDTO.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
                        receiveBillItemDTO.setParentBrandId(brandParent.getSuperiorParentBrandId());
                        brandParentMongoMap.put(saleReturnIdepotItemModel.getInGoodsId(), brandParent);
                    }
                    itemDTOS.add(receiveBillItemDTO);
                }
            }
        } else {
            throw new RuntimeException("单据类型不存在");
        }

        return itemDTOS;
    }

    /**
     * 错误时，设置错误内容
     *
     * @param index
     * @param msg
     * @param resultList
     */
    private void setErrorMsg(int index, String msg, List<ImportErrorMessage> resultList) {
        ImportErrorMessage message = new ImportErrorMessage();
        message.setRows(index + 1);
        message.setMessage(msg);
        resultList.add(message);
    }


    private Object toNullStrWithReplace(Object str) {
        if (str instanceof String) {
            String s = (String) str;
            if (StringUtils.isBlank(s)) {
                return "";
            }
            return s.replace("&", "&amp;");
        }
        return str;
    }

    /**
     * 保存附件信息
     *
     * @param bos       文件流
     * @param invoiceNo 目标文件名称
     * @param user      用户
     */
    private AttachmentInfoMongo saveAttachmentFile(ByteArrayOutputStream bos, String invoiceNo, User user) throws Exception {
        byte[] fileBytes = bos.toByteArray();
        String ext = "pdf";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fileTypeCode", SmurfsAPICodeEnum.UPLOAD_FILE.getCode());
        jsonObject.put("fileName", invoiceNo + "应收账单发票." + ext);
        jsonObject.put("fileBytes", Base64.encodeBase64String(fileBytes));
        jsonObject.put("fileExt", ext);
        jsonObject.put("fileSize", String.valueOf(fileBytes.length));
        String resultJson = SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UPLOAD_FILE);

        JSONObject jsonObj = JSONObject.fromObject(resultJson);

        if (jsonObj.getInt("code") != 200) {
            throw new RuntimeException("保存发票失败！");
        }
        //返回文件服务器存储URL
        String saveUrl = jsonObj.getString("url");
        //附件信息写入MongoDB
        AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo();
        attachmentInfoMongo.setAttachmentName(invoiceNo + "应收账单发票." + ext);        //附件名
        attachmentInfoMongo.setAttachmentSize(Long.valueOf(fileBytes.length));        //附件大小
        attachmentInfoMongo.setAttachmentType(ext);                //附件类型
        attachmentInfoMongo.setCreator(user.getId());
        attachmentInfoMongo.setCreatorName(user.getName());
        attachmentInfoMongo.setCreateDate(TimeUtils.getNow());
        attachmentInfoMongo.setRelationCode(invoiceNo + "-invoice");              //关联订单编码
        attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
        attachmentInfoMongo.setAttachmentUrl(saveUrl);              //设置Url
        attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
        attachmentInfoMongo.setModule(SourceStatusEnum.YSZD.getKey());
        attachmentInfoMongoDao.insert(attachmentInfoMongo);
        return attachmentInfoMongo;
    }

    private String addWatermark(String invoiceNo, User user, String invoicePath) throws Exception {
        InputStream in = null;
        ByteArrayOutputStream output = null;
        try {
            String ext = "pdf";
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource resource = resolver.getResource("classpath:/image/abolished.png");
            in = resource.getInputStream();
            output = new ByteArrayOutputStream();
            IOUtils.copyLarge(in, output);

            String tempPath = ApolloUtilDB.orderBasepath + "/temp/" + System.currentTimeMillis();
            Map<String, Object> map = PdfUtils.setWatermark(invoicePath, output.toByteArray(), invoiceNo + "应收账单发票." + ext, tempPath);

            String saveUrl = (String) map.get("saveUrl");
            Integer fileSize = (Integer) map.get("fileSize");

            //逻辑删除之前的发票文件
            Map<String, Object> params = new HashMap<String, Object>();
            Map<String, Object> data = new HashMap<String, Object>();
            params.put("relationCode", invoiceNo + "-invoice");

            data.put("status", DERP_ORDER.ATTACHMENT_STATUS_006);
            data.put("modifyDate", new Date());

            attachmentInfoMongoDao.update(params, data);

            //附件信息写入MongoDB
            AttachmentInfoMongo attachmentInfoMongo = new AttachmentInfoMongo();
            attachmentInfoMongo.setAttachmentName(invoiceNo + "应收账单发票." + ext);        //附件名
            attachmentInfoMongo.setAttachmentSize(Long.valueOf(fileSize));        //附件大小
            attachmentInfoMongo.setAttachmentType(ext);                //附件类型
            attachmentInfoMongo.setCreator(user.getId());            //上传者
            attachmentInfoMongo.setCreatorName(user.getName());
            attachmentInfoMongo.setCreateDate(new Date());
            attachmentInfoMongo.setRelationCode(invoiceNo + "-invoice");
            attachmentInfoMongo.setStatus(DERP_ORDER.ATTACHMENT_STATUS_001);  //状态
            attachmentInfoMongo.setAttachmentUrl(saveUrl);              //设置Url
            attachmentInfoMongo.setAttachmentCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ATT));
            attachmentInfoMongo.setModule(SourceStatusEnum.YSZD.getKey());

            attachmentInfoMongoDao.insert(attachmentInfoMongo);
            return saveUrl;
        } catch (Exception e) {
            throw new RuntimeException("发票文件增加已作废水印失败");
        } finally {
            in.close();
            output.close();
        }
    }

    /**
     * 校验 费用明细
     * @param itemDTO 费用明细
     * @param model   应收账单
     */
    private Map<String, String> validateCostItems(ReceiveBillCostItemDTO itemDTO, ReceiveBillModel model) throws SQLException {
        Map<String, String> resultMap = new HashMap<>();
        SettlementConfigDTO settlementConfigDTO = new SettlementConfigDTO();

        settlementConfigDTO.setCustomerId(model.getCustomerId());
        settlementConfigDTO.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);

        List<String> typeList = new ArrayList<>();
        typeList.add(DERP_ORDER.SETTLEMENTCONFIG_TYPE1);
        typeList.add(DERP_ORDER.SETTLEMENTCONFIG_TYPE3);
        settlementConfigDTO.setTypes(typeList);
        settlementConfigDTO.setProjectCode(itemDTO.getProjectCode());
        SettlementConfigDTO exist = settlementConfigDao.getDetailByCustomer(settlementConfigDTO);

        if (exist == null) {
            resultMap.put("code", "01");
            resultMap.put("message", "费项编码不存在！");
            return resultMap;
        }

        MerchandiseInfoMogo merchandiseInfo = null;
        if (StringUtils.isNotBlank(itemDTO.getGoodsNo())) {
            Map<String, Object> merParams = new HashMap<>();
            merParams.put("goodsNo", itemDTO.getGoodsNo());
            merParams.put("merchantId", model.getMerchantId());
            merchandiseInfo = merchandiseInfoMogoDao.findOne(merParams);
            if(merchandiseInfo==null){
                resultMap.put("code", "01");
                resultMap.put("message", "该商品货号不存在当前公司主体下！");
                return resultMap;
            }
        }

        if (merchandiseInfo != null) {
            itemDTO.setGoodsId(merchandiseInfo.getMerchandiseId());
            itemDTO.setGoodsName(merchandiseInfo.getName());
            itemDTO.setCommbarcode(merchandiseInfo.getCommbarcode());
        }

        itemDTO.setBillId(model.getId());
        itemDTO.setProjectCode(exist.getProjectCode());
        itemDTO.setProjectName(exist.getProjectName());
        itemDTO.setProjectId(exist.getId());
        itemDTO.setPaymentSubjectName(exist.getPaymentSubjectName());
        itemDTO.setPaymentSubjectId(exist.getPaymentSubjectId());

        BrandSuperiorMongo brandSuperior = null;

        if (itemDTO.getParentBrandId() != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("brandSuperiorId", itemDTO.getParentBrandId());
            brandSuperior = brandSuperiorMongoDao.findOne(params);
        }

        if (StringUtils.isNotBlank(itemDTO.getParentBrandCode())) {
            Map<String, Object> params = new HashMap<>();
            params.put("ncCode", itemDTO.getParentBrandCode());
            brandSuperior = brandSuperiorMongoDao.findOne(params);
        }

        if (brandSuperior != null) {
            itemDTO.setParentBrandCode(brandSuperior.getNcCode());
            itemDTO.setParentBrandName(brandSuperior.getName());
        }
        itemDTO.setParentBrandId(brandSuperior==null?0L:brandSuperior.getBrandSuperiorId());

        resultMap.put("code", "00");
        return resultMap;

    }

    /**
     * 校验 应收明细
     * @param itemDTO 应收明细
     * @param model   应收账单
     */
    private Map<String, String> validateItems(ReceiveBillItemDTO itemDTO, ReceiveBillModel model) throws SQLException {
        Map<String, String> resultMap = new HashMap<>();
        SettlementConfigDTO settlementConfigDTO = new SettlementConfigDTO();

        settlementConfigDTO.setCustomerId(model.getCustomerId());
        settlementConfigDTO.setLevel(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2);

        List<String> typeList = new ArrayList<>();
        typeList.add(DERP_ORDER.SETTLEMENTCONFIG_TYPE1);
        typeList.add(DERP_ORDER.SETTLEMENTCONFIG_TYPE3);
        settlementConfigDTO.setTypes(typeList);
        settlementConfigDTO.setProjectCode(itemDTO.getProjectCode());
        SettlementConfigDTO exist = settlementConfigDao.getDetailByCustomer(settlementConfigDTO);

        if (exist == null) {
            resultMap.put("code", "01");
            resultMap.put("message", "费项编码不存在！");
            return resultMap;
        }

        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("code", itemDTO.getCode());
        orderMap.put("poNo", itemDTO.getPoNo());
        orderMap.put("goodsNo", itemDTO.getGoodsNo());

        if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_1.equals(model.getOrderType())) {

            List<SaleShelfModel> saleShelfModels = saleShelfDao.getShelfInfoByCode(orderMap);

            if (saleShelfModels == null || saleShelfModels.size() == 0) {
                resultMap.put("code", "01");
                resultMap.put("message", "根据系统业务单号：" + itemDTO.getCode() + "，po号：" + itemDTO.getPoNo() + "，商品货号：" + itemDTO.getGoodsNo() + "找不到订单信息！");
                return resultMap;
            }

        }

        if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_2.equals(model.getOrderType())) {
            List<BillOutinDepotItemModel> billOutinDepotItemModels = billOutinDepotItemDao.getDetailsByReceive(orderMap);

            if (billOutinDepotItemModels == null || billOutinDepotItemModels.size() == 0) {
                resultMap.put("code", "01");
                resultMap.put("message", "根据系统业务单号：" + itemDTO.getCode() + "，po号：" + itemDTO.getPoNo() + "，商品货号：" + itemDTO.getGoodsNo() + "找不到订单信息！");
                return resultMap;
            }
        }

        if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_3.equals(model.getOrderType())) {

            List<PreSaleOrderItemModel> preSaleOrderItemModels = preSaleOrderItemDao.getDetailsByReceive(orderMap);

            if (preSaleOrderItemModels == null || preSaleOrderItemModels.size() == 0) {
                resultMap.put("code", "01");
                resultMap.put("message", "根据系统业务单号：" + itemDTO.getCode() + "，po号：" + itemDTO.getPoNo() + "，商品货号：" + itemDTO.getGoodsNo() + "找不到订单信息！");
                return resultMap;
            }

        }

        if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_4.equals(model.getOrderType())) {

            List<SaleOrderItemModel> saleOrderItemModels = saleOrderItemDao.getDetailsByReceive(orderMap);

            if (saleOrderItemModels == null || saleOrderItemModels.size() == 0) {
                resultMap.put("code", "01");
                resultMap.put("message", "根据系统业务单号：" + itemDTO.getCode() + "，po号：" + itemDTO.getPoNo() + "，商品货号：" + itemDTO.getGoodsNo() + "找不到订单信息！");
                return resultMap;
            }

        }

        if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_5.equals(model.getOrderType())) {

            List<PurchaseSdOrderSditemModel> purchaseSdOrderSditemModels = purchaseSdOrderSditemDao.getDetailsByReceive(orderMap);

            if (purchaseSdOrderSditemModels == null || purchaseSdOrderSditemModels.size() == 0) {
                resultMap.put("code", "01");
                resultMap.put("message", "根据系统业务单号：" + itemDTO.getCode() + "，po号：" + itemDTO.getPoNo() + "，商品货号：" + itemDTO.getGoodsNo() + "找不到订单信息！");
                return resultMap;
            }

        }

        if (DERP_ORDER.RECEIVEBILL_ORDERTYPE_7.equals(model.getOrderType())) {

            List<SaleReturnIdepotItemModel> saleReturnIdepotItemModels = saleReturnIdepotItemDao.getDetailsByReceive(orderMap);

            if (saleReturnIdepotItemModels == null || saleReturnIdepotItemModels.size() == 0) {
                resultMap.put("code", "01");
                resultMap.put("message", "根据系统业务单号：" + itemDTO.getCode() + "，po号：" + itemDTO.getPoNo() + "，商品货号：" + itemDTO.getGoodsNo() + "找不到订单信息！");
                return resultMap;
            }

        }

        ReceivePaymentSubjectModel paymentSubjectModel = receivePaymentSubjectDao.searchById(exist.getPaymentSubjectId());
        if (paymentSubjectModel != null) {
            //科目
            itemDTO.setSubjectCode(paymentSubjectModel.getSubCode());
            itemDTO.setSubjectName(paymentSubjectModel.getSubName());
        }


        Map<String, Object> merParams = new HashMap<>();
        merParams.put("goodsNo", itemDTO.getGoodsNo());
        merParams.put("merchantId", model.getMerchantId());
        MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merParams);

        itemDTO.setBillId(model.getId());
        itemDTO.setProjectCode(exist.getProjectCode());
        itemDTO.setProjectName(exist.getProjectName());
        itemDTO.setProjectId(exist.getId());
        itemDTO.setGoodsId(merchandiseInfo.getMerchandiseId());
        itemDTO.setGoodsName(merchandiseInfo.getName());
        itemDTO.setCommbarcode(merchandiseInfo.getCommbarcode());
        itemDTO.setBarcode(merchandiseInfo.getBarcode());
        itemDTO.setPaymentSubjectName(exist.getPaymentSubjectName());
        itemDTO.setPaymentSubjectId(exist.getPaymentSubjectId());

        BigDecimal tax = itemDTO.getPrice().multiply(new BigDecimal(itemDTO.getTaxRate().toString())).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal taxAmount = itemDTO.getPrice().add(tax);
        itemDTO.setTax(tax);
        itemDTO.setTaxAmount(taxAmount);

        BrandSuperiorMongo brandSuperior = null;

        if (itemDTO.getParentBrandId() == null) {
            brandSuperior = brandSuperiorMongoDao.getBrandSuperiorByGoodsId(merchandiseInfo.getMerchandiseId());
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("brandSuperiorId", itemDTO.getParentBrandId());
            brandSuperior = brandSuperiorMongoDao.findOne(params);
        }

        if (brandSuperior != null) {
            itemDTO.setParentBrandCode(brandSuperior.getNcCode());
            itemDTO.setParentBrandId(brandSuperior.getBrandSuperiorId());
            itemDTO.setParentBrandName(brandSuperior.getName());
        }

        resultMap.put("code", "00");
        return resultMap;

    }


    /**
     * 获取应收账单各个节点的操作人
     *
     * @param billId       应收id
     * @param emailUserDTO
     */
    private void getReceiveOperators(Long billId, ReminderEmailUserDTO emailUserDTO, JsonConfig jsonConfig) throws SQLException {
        ReceiveBillOperateModel operateModel = new ReceiveBillOperateModel();
        operateModel.setBillId(billId);
        List<ReceiveBillOperateModel> receiveBillOperateModels = receiveBillOperateDao.list(operateModel);

        Collections.sort(receiveBillOperateModels, new Comparator<ReceiveBillOperateModel>() {
            @Override
            public int compare(ReceiveBillOperateModel o1, ReceiveBillOperateModel o2) {
                return o2.getOperateDate().compareTo(o1.getOperateDate());
            }
        });

        for (ReceiveBillOperateModel model : receiveBillOperateModels) {
            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_0.equals(model.getOperateNode())) {
                if (emailUserDTO.getSubmitId() == null || emailUserDTO.getSubmitId().size() == 0) {
                    if (model.getOperateId() != null) {
                        emailUserDTO.setSubmitId(Arrays.asList(model.getOperateId().toString()));
                    }
                }
            }
            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_1.equals(model.getOperateNode())) {
                if (emailUserDTO.getAuditorId() == null) {
                    emailUserDTO.setAuditorId(model.getOperateId());
                }
            }

            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_3.equals(model.getOperateNode())) {
                if (emailUserDTO.getCancelId() == null) {
                    emailUserDTO.setCancelId(model.getOperateId());
                }
            }

            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_4.equals(model.getOperateNode())) {
                if (emailUserDTO.getCancelCompleteId() == null) {
                    emailUserDTO.setCancelCompleteId(model.getOperateId());
                }
            }

            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_6.equals(model.getOperateNode())) {
                if (emailUserDTO.getDrawerId() == null) {
                    emailUserDTO.setDrawerId(model.getOperateId());
                }
            }

            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_7.equals(model.getOperateNode())) {
                if (emailUserDTO.getReminderOrgId() == null) {
                    emailUserDTO.setReminderOrgId(model.getOperateId());
                }
            }

            if (DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_8.equals(model.getOperateNode())) {
                if (emailUserDTO.getVerificationId() == null) {
                    emailUserDTO.setVerificationId(model.getOperateId());
                }
            }
        }

        jsonConfig.registerDefaultValueProcessor(Long.class, new DefaultDefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return "";
            }
        });
    }

    /**
     * 通用模板取数逻辑
     * @param ids 应收id集合
     * @param templateDTO
     */
    public void listApiItemInfos(List<Long> ids, InvoiceTemplateDTO templateDTO,User user) throws Exception {

        List<InvoiceTemplateItemDTO> itemList = new LinkedList<>();

        List<InvoiceTemplateItemDTO> noGoodsItemList = new LinkedList<>();

        BigDecimal totalAllAmount = new BigDecimal("0");
        BigDecimal totalAllNum = new BigDecimal("0");

        Map<String, Map<String, Object>> invoiceMap = new HashMap<>();
        //应收明细
        List<Map<String, Object>> listInvoiceItem = receiveBillItemDao.listInvoiceItems(ids);
        mergeItemByKey(listInvoiceItem, invoiceMap, false, user);

        //费用明细
        List<Map<String, Object>> listInvoiceCostItem = receiveBillCostItemDao.listInvoiceCostItem(ids);
        mergeItemByKey(listInvoiceCostItem, invoiceMap, false, user);

        Integer index = 1;
        for (String key : invoiceMap.keySet()) {
            InvoiceTemplateItemDTO itemDTO = new InvoiceTemplateItemDTO();

            Map<String, Object> map = invoiceMap.get(key);
            String goodsNo = (String) map.get("goodsNo");
            String goodsName = (String) map.get("goodsName");
            String barcode = (String) map.get("barcode");
            String platformSku = (String) map.get("platformSku");
            String invoiceDescription = (String) map.get("invoiceDescription");
            String poNo = (String) map.get("poNo");
            Long goodsId = (Long) map.get("goodsId");
            Long projectId = (Long) map.get("projectId");
            Long parentBrandId = (Long) map.get("parentBrandId");
            String parentBrandName = (String) map.get("parentBrandName");
            String brandName = (String) map.get("brandName");
            String unit = (String) map.get("unit");
            BigDecimal totalPrice = (BigDecimal) map.get("totalPrice");
            BigDecimal totalNum = (BigDecimal) map.get("totalNum");
            BigDecimal price = (BigDecimal) map.get("price");

            itemDTO.setGoodsId(goodsId);
            itemDTO.setGoodsNo(goodsNo);
            itemDTO.setGoodsName(goodsName);
            itemDTO.setBarcode(barcode);
            itemDTO.setPlatformSku(platformSku);
            itemDTO.setInvoiceDescription(invoiceDescription);
            itemDTO.setPoNo(poNo);
            itemDTO.setProjectId(projectId);
            itemDTO.setParentBrandId(parentBrandId);
            itemDTO.setUnit(unit);
            itemDTO.setParentBrandName(parentBrandName);
            itemDTO.setBrandName(brandName);
            itemDTO.setPrice("-");
            itemDTO.setTotalNum("-");
            itemDTO.setTotalPrice("-");

            if (totalPrice != null) {
                totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
                itemDTO.setTotalPrice(StrUtils.doubleFormatString(totalPrice.doubleValue()));
                totalAllAmount = totalAllAmount.add(totalPrice);
            }

            if (price != null) {
                itemDTO.setPrice(StrUtils.doubleFormatString(price.doubleValue()));
            }

            if (totalNum != null) {
                if (totalNum.compareTo(new BigDecimal("0")) != 0) {
                    itemDTO.setTotalNum(StrUtils.intFormatString(totalNum.intValue()));
                }
                totalAllNum = totalAllNum.add(totalNum);
            }

            if (StringUtils.isNotBlank(goodsNo)) {
                itemDTO.setIndex(String.valueOf(index));
                itemList.add(itemDTO);
                index++;
            } else {
                noGoodsItemList.add(itemDTO);
            }
        }

        for (InvoiceTemplateItemDTO templateItemDTO : noGoodsItemList) {
            templateItemDTO.setIndex(String.valueOf(index));
            itemList.add(templateItemDTO);
            index++;
        }
        totalAllAmount = totalAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        templateDTO.setTotalAllAmount(StrUtils.doubleFormatString(totalAllAmount.doubleValue()));
        templateDTO.setTotalAllNum(StrUtils.doubleFormatString(totalAllNum.doubleValue()));

        templateDTO.setItemList(itemList);
    }

    /**
     * 唯品模板取数逻辑
     * @param ids 应收id集合
     * @param templateDTO
     */
    public void listApiWPItemInfos(List<Long> ids, InvoiceTemplateDTO templateDTO, User user) throws Exception {

        List<InvoiceWeiPinTemplateItemDTO> wpItemList = new LinkedList<>();
        BigDecimal totalAllAmount = new BigDecimal("0");
        BigDecimal anotherTotalAllAmount = new BigDecimal("0");
        BigDecimal customerReturnBrandAmount = new BigDecimal("0");
        BigDecimal promotionDiscountsBrandAmount = new BigDecimal("0");
        BigDecimal extraDiscountBrandAmount = new BigDecimal("0");
        BigDecimal promotionDiscountsCustBrandAmount = new BigDecimal("0");
        BigDecimal extraAmountBrandAmount = new BigDecimal("0");
        BigDecimal customerReturnAllAmount = new BigDecimal("0");
        BigDecimal promotionDiscountsAllAmount = new BigDecimal("0");
        BigDecimal extraDiscountAllAmount = new BigDecimal("0");
        BigDecimal promotionDiscountsCustAllAmount = new BigDecimal("0");
        BigDecimal extraAmountAllAmount = new BigDecimal("0");
        //费用项目按商品和费用项目维度统计
        List<Map<String, Object>> listInvoiceCostItem = receiveBillCostItemDao.listInvoiceCostItemByGoodsNo(ids, user.getMerchantId(), "weiping");
        Map<String, Map<String, BigDecimal>> parentNameCostMap = new HashMap<>();
        for (Map<String, Object> map : listInvoiceCostItem) {
            BigDecimal totalPrice = (BigDecimal) map.get("totalPrice");
            String projectName = (String) map.get("projectName");
            if (!"客退补贴".equals(projectName) && !"活动折扣".equals(projectName) &&
                    !"补偿折扣".equals(projectName) && !"客退折让".equals(projectName) &&
                    !"返利金额".equals(projectName)) {
                continue;
            }
            String goodsNo = (String) map.get("goodsNo");
            String parentBrandCode = map.get("parentBrandCode") != null ? String.valueOf(map.get("parentBrandCode")) : "";
            String key = goodsNo + "__" + parentBrandCode;
            if (StringUtils.isNotBlank(key)) {
                Map<String, BigDecimal> costMap = new HashMap<>();
                if (parentNameCostMap.containsKey(key)) {
                    costMap.putAll(parentNameCostMap.get(key));
                }
                if ("客退补贴".equals(projectName)) {
                    costMap.put("customerReturn", totalPrice);
                } else if ("活动折扣".equals(projectName)) {
                    costMap.put("promotionDiscounts", totalPrice);
                } else if ("补偿折扣".equals(projectName)) {
                    costMap.put("extraDiscount", totalPrice);
                } else if ("客退折让".equals(projectName)) {
                    costMap.put("promotionDiscountsCust", totalPrice);
                } else if ("返利金额".equals(projectName)) {
                    costMap.put("extraAmount", totalPrice);
                }
                parentNameCostMap.put(key, costMap);
            }
            if (totalPrice != null) {
                anotherTotalAllAmount = anotherTotalAllAmount.add(totalPrice);
            }
        }

        List<Map<String, Object>> listInvoiceItem = receiveBillItemDao.listInvoiceItemGroupByParentBrand(ids);
        //已统计品牌对应的商品数量
        Map<String, List<Map<String, Object>>> existBrandMap = new HashMap<>();
        for (int i = 0; i < listInvoiceItem.size(); i++) {
            Map<String, Object> map = listInvoiceItem.get(i);
            BigDecimal totalPrice = map.get("totalPrice") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalPrice");
            BigDecimal totalNum = map.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalNum");
            String goodsNo = (String) map.get("goodsNo");
            String brandName = map.get("parentBrandCode") != null ? String.valueOf(map.get("parentBrandCode")) : "";
            if (totalNum.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal price = totalPrice.divide(totalNum, 4, BigDecimal.ROUND_HALF_UP);
                BigDecimal exactPrice = totalPrice.divide(totalNum, 8, BigDecimal.ROUND_HALF_UP);
                map.put("price", StrUtils.doubleFormatStringByFour(price.doubleValue()));
                map.put("exactPrice", exactPrice);
            }
            String key = goodsNo + "__" + brandName;
            if (parentNameCostMap.containsKey(key)) {
                if (parentNameCostMap.get(key).containsKey("customerReturn")) {
                    BigDecimal tempAmount = parentNameCostMap.get(key).get("customerReturn");
                    map.put("customerReturn", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (parentNameCostMap.get(key).containsKey("promotionDiscounts")) {
                    BigDecimal tempAmount = parentNameCostMap.get(key).get("promotionDiscounts");
                    map.put("promotionDiscounts", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (parentNameCostMap.get(key).containsKey("extraDiscount")) {
                    BigDecimal tempAmount = parentNameCostMap.get(key).get("extraDiscount");
                    map.put("extraDiscount", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (parentNameCostMap.get(key).containsKey("promotionDiscountsCust")) {
                    BigDecimal tempAmount = parentNameCostMap.get(key).get("promotionDiscountsCust");
                    map.put("promotionDiscountsCust", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (parentNameCostMap.get(key).containsKey("extraAmount")) {
                    BigDecimal tempAmount = parentNameCostMap.get(key).get("extraAmount");
                    map.put("extraAmount", tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                parentNameCostMap.remove(key);
            } else {
                map.put("customerReturn", new BigDecimal("0"));
                map.put("promotionDiscounts", new BigDecimal("0"));
                map.put("extraDiscount", new BigDecimal("0"));
                map.put("promotionDiscountsCust", new BigDecimal("0"));
                map.put("extraAmount", new BigDecimal("0"));
            }
            map.put("totalPrice", totalPrice);
            if (existBrandMap.containsKey(brandName)) {
                existBrandMap.get(brandName).add(map);
            } else {
                List<Map<String, Object>> goodsList = new ArrayList<>();
                goodsList.add(map);
                existBrandMap.put(brandName, goodsList);
            }

            anotherTotalAllAmount = anotherTotalAllAmount.add(totalPrice);
        }

        Map<String, List<Map<String, Object>>> noExistBrandMap = new HashMap<>(); //未统计的品牌商品信息
        //遍历未统计的品牌商品信息并小计
        for (String key : parentNameCostMap.keySet()) {
            String[] keys = key.split("__");
            Map<String, Object> map = new HashMap<>();
            map.put("goodsNo", keys[0]);
            if (parentNameCostMap.get(key).containsKey("customerReturn")) {
                BigDecimal tempAmount = parentNameCostMap.get(key).get("customerReturn");
                map.put("customerReturn", tempAmount);
            }
            if (parentNameCostMap.get(key).containsKey("promotionDiscounts")) {
                BigDecimal tempAmount = parentNameCostMap.get(key).get("promotionDiscounts");
                map.put("promotionDiscounts", tempAmount);
            }
            if (parentNameCostMap.get(key).containsKey("extraDiscount")) {
                BigDecimal tempAmount = parentNameCostMap.get(key).get("extraDiscount");
                map.put("extraDiscount", tempAmount);
            }
            if (parentNameCostMap.get(key).containsKey("promotionDiscountsCust")) {
                BigDecimal tempAmount = parentNameCostMap.get(key).get("promotionDiscountsCust");
                map.put("promotionDiscountsCust", tempAmount);
            }
            if (parentNameCostMap.get(key).containsKey("extraAmount")) {
                BigDecimal tempAmount = parentNameCostMap.get(key).get("extraAmount");
                map.put("extraAmount", tempAmount);
            }

            String brandName = keys.length == 2 ? keys[1] : "";
            if (noExistBrandMap.containsKey(brandName)) {
                noExistBrandMap.get(brandName).add(map);
            } else {
                List<Map<String, Object>> goodsList = new ArrayList<>();
                goodsList.add(map);
                noExistBrandMap.put(brandName, goodsList);
            }
        }

        //遍历并合并未统计到的品牌信息
        for (String brandName : noExistBrandMap.keySet()) {
            BigDecimal totalBrandAmount = new BigDecimal("0");
            List<Map<String, Object>> goodsList = noExistBrandMap.get(brandName);
            if (existBrandMap.containsKey(brandName)) {
                List<Map<String, Object>> existGoodsList = existBrandMap.get(brandName);
                for (Map<String, Object> exist : existGoodsList) {
                    String goodsNo = (String) exist.get("goodsNo");
                    String price = (String) exist.get("price");
                    BigDecimal totalNum = exist.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) exist.get("totalNum");
                    BigDecimal customerReturn = exist.get("customerReturn") == null ? new BigDecimal("0") : (BigDecimal) exist.get("customerReturn");
                    customerReturnBrandAmount = customerReturnBrandAmount.add(customerReturn);
                    BigDecimal promotionDiscounts = exist.get("promotionDiscounts") == null ? new BigDecimal("0") : (BigDecimal) exist.get("promotionDiscounts");
                    promotionDiscountsBrandAmount = promotionDiscountsBrandAmount.add(promotionDiscounts);
                    BigDecimal extraDiscount = exist.get("extraDiscount") == null ? new BigDecimal("0") : (BigDecimal) exist.get("extraDiscount");
                    extraDiscountBrandAmount = extraDiscountBrandAmount.add(extraDiscount);
                    BigDecimal promotionDiscountsCust = exist.get("promotionDiscountsCust") == null ? new BigDecimal("0") : (BigDecimal) exist.get("promotionDiscountsCust");
                    promotionDiscountsCustBrandAmount = promotionDiscountsCustBrandAmount.add(promotionDiscountsCust);
                    BigDecimal extraAmount = exist.get("extraAmount") == null ? new BigDecimal("0") : (BigDecimal) exist.get("extraAmount");
                    extraAmountBrandAmount = extraAmountBrandAmount.add(extraAmount);
                    BigDecimal totalPrice = (BigDecimal) exist.get("totalPrice");
                    totalBrandAmount = totalBrandAmount.add(totalPrice);

                    InvoiceWeiPinTemplateItemDTO templateItemDTO = new InvoiceWeiPinTemplateItemDTO();
                    templateItemDTO.setCustomerReturn(StrUtils.doubleFormatString(customerReturn.doubleValue()));
                    templateItemDTO.setPromotionDiscounts(StrUtils.doubleFormatString(promotionDiscounts.doubleValue()));
                    templateItemDTO.setExtraDiscount(StrUtils.doubleFormatString(extraDiscount.doubleValue()));
                    templateItemDTO.setPromotionDiscountsCust(StrUtils.doubleFormatString(promotionDiscountsCust.doubleValue()));
                    templateItemDTO.setExtraAmount(StrUtils.doubleFormatString(extraAmount.doubleValue()));
                    templateItemDTO.setTotalPrice(StrUtils.doubleFormatString(totalPrice.doubleValue()));
                    templateItemDTO.setPrice(price);
                    templateItemDTO.setTotalNum(totalNum.toString());
                    if (StringUtils.isNotBlank(goodsNo) && !goodsNo.equals("null")) {
                        Map<String, Object> merParams = new HashMap<>();
                        merParams.put("goodsNo", goodsNo);
                        merParams.put("merchantId", user.getMerchantId());
                        MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merParams);
                        templateItemDTO.setGoodsName(merchandiseInfoMogo.getName());
                    }
                    wpItemList.add(templateItemDTO);
                }
                existBrandMap.remove(brandName);
            }
            for (int i = 0; i < goodsList.size(); i++) {
                BigDecimal totalPrice = new BigDecimal("0");
                Map<String, Object> goodsMap = goodsList.get(i);
                String goodsNo = (String) goodsMap.get("goodsNo");
                String price = (String) goodsMap.get("price");
                BigDecimal totalNum = goodsMap.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) goodsMap.get("totalNum");
                InvoiceWeiPinTemplateItemDTO templateItemDTO = new InvoiceWeiPinTemplateItemDTO();
                templateItemDTO.setTotalNum(totalNum.toString());
                templateItemDTO.setPrice(price);
                if (goodsMap.containsKey("customerReturn")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("customerReturn");
                    templateItemDTO.setCustomerReturn(StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    customerReturnBrandAmount = customerReturnBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsMap.containsKey("promotionDiscounts")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("promotionDiscounts");
                    templateItemDTO.setPromotionDiscounts(StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    promotionDiscountsBrandAmount = promotionDiscountsBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsMap.containsKey("extraDiscount")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("extraDiscount");
                    templateItemDTO.setExtraDiscount(StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    extraDiscountBrandAmount = extraDiscountBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsMap.containsKey("promotionDiscountsCust")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("promotionDiscountsCust");
                    templateItemDTO.setPromotionDiscountsCust(StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    promotionDiscountsCustBrandAmount = promotionDiscountsCustBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (goodsMap.containsKey("extraAmount")) {
                    BigDecimal tempAmount = (BigDecimal) goodsMap.get("extraAmount");
                    templateItemDTO.setExtraAmount(StrUtils.doubleFormatString(tempAmount.doubleValue()));
                    extraAmountBrandAmount = extraAmountBrandAmount.add(tempAmount);
                    totalPrice = totalPrice.add(tempAmount);
                }
                if (StringUtils.isNotBlank(goodsNo) && !goodsNo.equals("null")) {
                    Map<String, Object> merParams = new HashMap<>();
                    merParams.put("goodsNo", goodsNo);
                    merParams.put("merchantId", user.getMerchantId());
                    MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merParams);
                    templateItemDTO.setGoodsName(merchandiseInfoMogo.getName());
                }
                totalBrandAmount = totalBrandAmount.add(totalPrice);
                templateItemDTO.setTotalPrice(StrUtils.doubleFormatString(totalPrice.doubleValue()));
                wpItemList.add(templateItemDTO);
            }
            //品牌小计
            String parentBrandName = "";
            if (StringUtils.isNotBlank(brandName)) {
                Map<String, Object> brandParam = new HashMap<>();
                brandParam.put("brandSuperiorId", Long.valueOf(brandName));
                BrandSuperiorMongo brandSuperiorMongo = brandSuperiorMongoDao.findOne(brandParam);
                if (brandSuperiorMongo != null) {
                    parentBrandName = brandSuperiorMongo.getName();
                }
            }

            InvoiceWeiPinTemplateItemDTO templateItemDTO = new InvoiceWeiPinTemplateItemDTO();
            templateItemDTO.setGoodsName(parentBrandName + "-合计");
            templateItemDTO.setCustomerReturn(StrUtils.doubleFormatString(customerReturnBrandAmount.doubleValue()));
            templateItemDTO.setPromotionDiscounts(StrUtils.doubleFormatString(promotionDiscountsBrandAmount.doubleValue()));
            templateItemDTO.setExtraDiscount(StrUtils.doubleFormatString(extraDiscountBrandAmount.doubleValue()));
            templateItemDTO.setPromotionDiscountsCust(StrUtils.doubleFormatString(promotionDiscountsCustBrandAmount.doubleValue()));
            templateItemDTO.setExtraAmount(StrUtils.doubleFormatString(extraAmountBrandAmount.doubleValue()));
            templateItemDTO.setTotalPrice(StrUtils.doubleFormatString(totalBrandAmount.doubleValue()));

            customerReturnAllAmount = customerReturnAllAmount.add(customerReturnBrandAmount);
            promotionDiscountsAllAmount = promotionDiscountsAllAmount.add(promotionDiscountsBrandAmount);
            extraDiscountAllAmount = extraDiscountAllAmount.add(extraDiscountBrandAmount);
            promotionDiscountsCustAllAmount = promotionDiscountsCustAllAmount.add(promotionDiscountsCustBrandAmount);
            extraAmountAllAmount = extraAmountAllAmount.add(extraAmountBrandAmount);
            totalAllAmount = totalAllAmount.add(totalBrandAmount);
            customerReturnBrandAmount = new BigDecimal("0");
            promotionDiscountsBrandAmount = new BigDecimal("0");
            extraDiscountBrandAmount = new BigDecimal("0");
            promotionDiscountsCustBrandAmount = new BigDecimal("0");
            extraAmountBrandAmount = new BigDecimal("0");
            wpItemList.add(templateItemDTO);
        }

        //添加已统计的品牌信息
        for (String brandName : existBrandMap.keySet()) {
            BigDecimal totalBrandAmount = new BigDecimal("0");
            List<Map<String, Object>> existGoodsList = existBrandMap.get(brandName);
            for (Map<String, Object> exist : existGoodsList) {
                String goodsNo = (String) exist.get("goodsNo");
                String price = (String) exist.get("price");
                BigDecimal totalNum = exist.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) exist.get("totalNum");

                BigDecimal customerReturn = exist.get("customerReturn") == null ? new BigDecimal("0") : (BigDecimal) exist.get("customerReturn");
                customerReturnBrandAmount = customerReturnBrandAmount.add(customerReturn);
                BigDecimal promotionDiscounts = exist.get("promotionDiscounts") == null ? new BigDecimal("0") : (BigDecimal) exist.get("promotionDiscounts");
                promotionDiscountsBrandAmount = promotionDiscountsBrandAmount.add(promotionDiscounts);
                BigDecimal extraDiscount = exist.get("extraDiscount") == null ? new BigDecimal("0") : (BigDecimal) exist.get("extraDiscount");
                extraDiscountBrandAmount = extraDiscountBrandAmount.add(extraDiscount);
                BigDecimal promotionDiscountsCust = exist.get("promotionDiscountsCust") == null ? new BigDecimal("0") : (BigDecimal) exist.get("promotionDiscountsCust");
                promotionDiscountsCustBrandAmount = promotionDiscountsCustBrandAmount.add(promotionDiscountsCust);
                BigDecimal extraAmount = exist.get("extraAmount") == null ? new BigDecimal("0") : (BigDecimal) exist.get("extraAmount");
                extraAmountBrandAmount = extraAmountBrandAmount.add(extraAmount);
                BigDecimal totalPrice = (BigDecimal) exist.get("totalPrice");
                totalBrandAmount = totalBrandAmount.add(totalPrice);

                InvoiceWeiPinTemplateItemDTO templateItemDTO = new InvoiceWeiPinTemplateItemDTO();
                templateItemDTO.setCustomerReturn(StrUtils.doubleFormatString(customerReturn.doubleValue()));
                templateItemDTO.setPromotionDiscounts(StrUtils.doubleFormatString(promotionDiscounts.doubleValue()));
                templateItemDTO.setExtraDiscount(StrUtils.doubleFormatString(extraDiscount.doubleValue()));
                templateItemDTO.setPromotionDiscountsCust(StrUtils.doubleFormatString(promotionDiscountsCust.doubleValue()));
                templateItemDTO.setExtraAmount(StrUtils.doubleFormatString(extraAmount.doubleValue()));
                templateItemDTO.setTotalPrice(StrUtils.doubleFormatString(totalPrice.doubleValue()));
                templateItemDTO.setPrice(price);
                templateItemDTO.setTotalNum(totalNum.toString());
                if (StringUtils.isNotBlank(goodsNo) && !goodsNo.equals("null")) {
                    Map<String, Object> merParams = new HashMap<>();
                    merParams.put("goodsNo", goodsNo);
                    merParams.put("merchantId", user.getMerchantId());
                    MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merParams);
                    templateItemDTO.setGoodsName(merchandiseInfoMogo.getName());
                }
                wpItemList.add(templateItemDTO);
            }
            //品牌小计
            String parentBrandName = "";
            if (StringUtils.isNotBlank(brandName)) {
                Map<String, Object> brandParam = new HashMap<>();
                brandParam.put("brandSuperiorId", Long.valueOf(brandName));
                BrandSuperiorMongo brandSuperiorMongo = brandSuperiorMongoDao.findOne(brandParam);
                if (brandSuperiorMongo != null) {
                    parentBrandName = brandSuperiorMongo.getName();
                }
            }
            InvoiceWeiPinTemplateItemDTO templateItemDTO = new InvoiceWeiPinTemplateItemDTO();
            templateItemDTO.setGoodsName(parentBrandName + "-合计");
            templateItemDTO.setCustomerReturn(StrUtils.doubleFormatString(customerReturnBrandAmount.doubleValue()));
            templateItemDTO.setPromotionDiscounts(StrUtils.doubleFormatString(promotionDiscountsBrandAmount.doubleValue()));
            templateItemDTO.setExtraDiscount(StrUtils.doubleFormatString(extraDiscountBrandAmount.doubleValue()));
            templateItemDTO.setPromotionDiscountsCust(StrUtils.doubleFormatString(promotionDiscountsCustBrandAmount.doubleValue()));
            templateItemDTO.setExtraAmount(StrUtils.doubleFormatString(extraAmountBrandAmount.doubleValue()));
            templateItemDTO.setTotalPrice(StrUtils.doubleFormatString(totalBrandAmount.doubleValue()));

            customerReturnAllAmount = customerReturnAllAmount.add(customerReturnBrandAmount);
            promotionDiscountsAllAmount = promotionDiscountsAllAmount.add(promotionDiscountsBrandAmount);
            extraDiscountAllAmount = extraDiscountAllAmount.add(extraDiscountBrandAmount);
            promotionDiscountsCustAllAmount = promotionDiscountsCustAllAmount.add(promotionDiscountsCustBrandAmount);
            extraAmountAllAmount = extraAmountAllAmount.add(extraAmountBrandAmount);
            totalAllAmount = totalAllAmount.add(totalBrandAmount);
            customerReturnBrandAmount = new BigDecimal("0");
            promotionDiscountsBrandAmount = new BigDecimal("0");
            extraDiscountBrandAmount = new BigDecimal("0");
            wpItemList.add(templateItemDTO);
        }

        totalAllAmount = totalAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

        if (totalAllAmount.compareTo(new BigDecimal("0")) != 0) {
            templateDTO.setTotalAllAmount(StrUtils.doubleFormatString(totalAllAmount.doubleValue()));
        } else {
            templateDTO.setTotalAllAmount(StrUtils.doubleFormatString(anotherTotalAllAmount.doubleValue()));
        }

        templateDTO.setPromotionDiscountsAll(StrUtils.doubleFormatString(promotionDiscountsAllAmount.doubleValue()));
        templateDTO.setPromotionDiscountsCustAll(StrUtils.doubleFormatString(promotionDiscountsCustAllAmount.doubleValue()));
        templateDTO.setExtraAmountAll(StrUtils.doubleFormatString(extraAmountAllAmount.doubleValue()));
        templateDTO.setExtraDiscountAll(StrUtils.doubleFormatString(extraDiscountAllAmount.doubleValue()));
        templateDTO.setCustomerReturnAll(StrUtils.doubleFormatString(customerReturnAllAmount.doubleValue()));
        templateDTO.setWpItemList(wpItemList);
    }

    /**
     * 考拉模板取数逻辑
     * @param ids 应收id集合
     * @param templateDTO
     */
    public void listApiKLItemInfos(List<Long> ids, InvoiceTemplateDTO templateDTO) throws Exception {

        Map<String, BigDecimal> poNoMap = new HashMap<>();
        BigDecimal totalAmount = new BigDecimal("0");

        List<Map<String, Object>> listInvoiceItem = receiveBillItemDao.listByPoNo(ids);

        if (listInvoiceItem != null) {
            for (Map<String, Object> map : listInvoiceItem) {
                String poNo = (String) map.get("poNo");
                BigDecimal amount = map.get("amount") == null ? BigDecimal.ZERO : (BigDecimal) map.get("amount");
                poNoMap.put(poNo, amount);
            }
        }

        //合并相同po号或费项的金额
        List<Map<String, Object>> listInvoiceCostItem = receiveBillCostItemDao.listByPoNoAndProject(ids);
        if (listInvoiceCostItem != null) {
            for (Map<String, Object> map : listInvoiceCostItem) {
                String poNo = (String) map.get("poNo");
                String projectName = (String) map.get("projectName");
                BigDecimal amount = map.get("amount") == null ? BigDecimal.ZERO : (BigDecimal) map.get("amount");

                if (StringUtils.isBlank(poNo)) {
                    poNo = projectName;
                }

                if (poNoMap.containsKey(poNo)) {
                    BigDecimal existAmount = poNoMap.get(poNo);
                    amount = amount.add(existAmount);
                }
                poNoMap.put(poNo, amount);
            }
        }

        List<InvoiceTemplateItemDTO> itemList = new LinkedList<>();

        for (String poNo : poNoMap.keySet()) {
            InvoiceTemplateItemDTO itemDTO = new InvoiceTemplateItemDTO();
            if (poNo.contains("\"")) {
                poNo.replace("\"", "&quot;");
            }
            itemDTO.setPoNo(poNo);
            itemDTO.setTotalPrice("");
            if (poNoMap.get(poNo) != null) {
                itemDTO.setTotalPrice(String.valueOf(poNoMap.get(poNo)));
            }

            itemList.add(itemDTO);
            totalAmount = totalAmount.add(poNoMap.get(poNo));
        }
        templateDTO.setItemList(itemList);
        templateDTO.setTotalAllAmount(totalAmount.toString());
    }

    /**
     * 亿滋模板取数逻辑
     * @param ids 应收id集合
     * @param templateDTO
     */
    public void listApiYLItemInfos(List<Long> ids, InvoiceTemplateDTO templateDTO, User user) throws Exception {

        List<InvoiceTemplateItemDTO> itemList = new LinkedList<>();
        List<InvoiceTemplateItemDTO> noGoodsNoMapList = new LinkedList<>();
        BigDecimal totalAllAmount = new BigDecimal("0");
        BigDecimal totalAllNum = new BigDecimal("0");

        Map<String, Map<String, Object>> invoiceMap = new HashMap<>();
        //应收明细
        List<Map<String, Object>> listInvoiceItem = receiveBillItemDao.listInvoiceItems(ids);
        mergeItemByKey(listInvoiceItem, invoiceMap, true, user);

        //费用明细
        List<Map<String, Object>> listInvoiceCostItem = receiveBillCostItemDao.listInvoiceCostItem(ids);
        mergeItemByKey(listInvoiceCostItem, invoiceMap, true, user);

        int index = 1;
        for (String key : invoiceMap.keySet()) {
            Map<String, Object> map = invoiceMap.get(key);
            String goodsNo = (String) map.get("goodsNo");
            String parentBrandName = (String) map.get("parentBrandName");
            String goodsName = (String) map.get("goodsName");
            BigDecimal totalPrice = (BigDecimal) map.get("totalPrice");
            BigDecimal totalNum = (BigDecimal) map.get("totalNum");
            BigDecimal price = (BigDecimal) map.get("price");

            InvoiceTemplateItemDTO itemDTO = new InvoiceTemplateItemDTO();
            itemDTO.setBrandName(parentBrandName);
            itemDTO.setGoodsName(goodsName);
            itemDTO.setTotalPrice("-");
            itemDTO.setPrice("-");
            itemDTO.setTotalNum("-");

            if (totalPrice != null) {
                totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
                itemDTO.setTotalPrice(StrUtils.doubleFormatString(totalPrice.doubleValue()));
                totalAllAmount = totalAllAmount.add(totalPrice);
            }

            if (price != null) {
                itemDTO.setPrice(StrUtils.doubleFormatString(price.doubleValue()));
            }

            if (totalNum != null) {
                itemDTO.setTotalNum(StrUtils.intFormatString(totalNum.intValue()));
                totalAllNum = totalAllNum.add(totalNum);
            }

            if (StringUtils.isNotBlank(goodsNo)) {
                itemDTO.setIndex(String.valueOf(index));
                itemList.add(itemDTO);
                index++;
            } else {
                noGoodsNoMapList.add(itemDTO);
            }
        }

        for (InvoiceTemplateItemDTO itemDTO : noGoodsNoMapList) {
            itemDTO.setIndex(String.valueOf(index));
            itemList.add(itemDTO);
            index++;
        }

        totalAllAmount = totalAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        templateDTO.setTotalAllNum(StrUtils.intFormatString(totalAllNum.intValue()));
        templateDTO.setTotalAllAmount(StrUtils.doubleFormatString(totalAllAmount.doubleValue()));
        templateDTO.setItemList(itemList);
    }

    /**
     * 京东模板取数逻辑
     * @param ids 应收id集合
     * @param templateDTO
     */
    public void listApiJDItemInfos(List<Long> ids, InvoiceTemplateDTO templateDTO, User user) throws Exception {

        List<InvoiceTemplateItemDTO> mapList = new LinkedList<>();

        BigDecimal totalAllAmount = new BigDecimal("0");
        BigDecimal totalAllNum = new BigDecimal("0");
        Map<String, Map<String, Object>> invoiceMap = new HashMap<>();

        //应收明细
        List<Map<String, Object>> listInvoiceItem = receiveBillItemDao.listInvoiceItems(ids);
        mergeJDItemByKey(listInvoiceItem, invoiceMap, true, user);
        //费用明细
        List<Map<String, Object>> listInvoiceCostItem = receiveBillCostItemDao.listInvoiceCostItem(ids);
        mergeItemByKey(listInvoiceCostItem, invoiceMap, true, user);

        //以品牌维度合计
        Map<Long, List<Map<String, Object>>> brandMap = new HashMap<>();
        for (String key : invoiceMap.keySet()) {
            Map<String, Object> map = invoiceMap.get(key);
            Long parentBrandId = (Long) map.get("parentBrandId");
            if (brandMap.containsKey(parentBrandId)) {
                brandMap.get(parentBrandId).add(map);
            } else {
                List<Map<String, Object>> costList = new ArrayList<>();
                costList.add(map);
                brandMap.put(parentBrandId, costList);
            }
        }

        //应收明细和费项明细按母品牌维度归类
        for (Long brandId : brandMap.keySet()) {
            List<InvoiceTemplateItemDTO> noGoodsNoList = new LinkedList<>();
            BigDecimal totalBrandAmount = new BigDecimal("0");
            BigDecimal totalBrandNum = new BigDecimal("0");
            List<Map<String, Object>> itemList = brandMap.get(brandId);
            for (Map<String, Object> itemMap : itemList) {
                String goodsNo = (String) itemMap.get("goodsNo");
                String goodsName = (String) itemMap.get("goodsName");
                String barcode = (String) itemMap.get("barcode");
                BigDecimal totalPrice = (BigDecimal) itemMap.get("totalPrice");
                BigDecimal totalNum = (BigDecimal) itemMap.get("totalNum");

                InvoiceTemplateItemDTO itemDTO = new InvoiceTemplateItemDTO();
                itemDTO.setPrice("-");
                itemDTO.setTotalPrice("-");
                itemDTO.setTotalNum("-");

                if (totalPrice != null && totalNum != null && totalNum.compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal price = totalPrice.divide(totalNum, 2, BigDecimal.ROUND_HALF_UP);
                    itemDTO.setPrice(StrUtils.doubleFormatString(price.doubleValue()));
                }

                if (totalPrice != null) {
                    itemDTO.setTotalPrice(StrUtils.doubleFormatString(totalPrice.doubleValue()));
                    totalBrandAmount = totalBrandAmount.add(totalPrice);
                }

                if (totalNum != null) {
                    itemDTO.setTotalNum(StrUtils.intFormatString(totalNum.intValue()));
                    totalBrandNum = totalBrandNum.add(totalNum);
                }

                itemDTO.setGoodsNo(goodsNo);
                itemDTO.setGoodsName(goodsName);
                itemDTO.setBarcode(barcode);
                if (StringUtils.isNotBlank(goodsNo)) {
                    mapList.add(itemDTO);
                } else {
                    noGoodsNoList.add(itemDTO);
                }
            }

            for (InvoiceTemplateItemDTO noGoodsDto : noGoodsNoList) {
                mapList.add(noGoodsDto);
            }
            String parentBrandName = "";
            Map<String, Object> brandParam = new HashMap<>();
            if (brandId != null) {
                brandParam.put("brandSuperiorId", brandId);
                BrandSuperiorMongo brandSuperiorMongo = brandSuperiorMongoDao.findOne(brandParam);
                if (brandSuperiorMongo != null) {
                    parentBrandName = brandSuperiorMongo.getName();
                }
            }

            //品牌小计
            InvoiceTemplateItemDTO brandDto = new InvoiceTemplateItemDTO();
            brandDto.setGoodsName(parentBrandName + "-合计");
            brandDto.setTotalPrice(StrUtils.doubleFormatString(totalBrandAmount.doubleValue()));
            brandDto.setTotalNum(StrUtils.intFormatString(totalBrandNum.intValue()));
            brandDto.setBarcode("");
            brandDto.setGoodsNo("");
            mapList.add(brandDto);
            totalAllAmount = totalAllAmount.add(totalBrandAmount);
            totalAllNum = totalAllNum.add(totalBrandNum);
        }

        templateDTO.setItemList(mapList);
        totalAllAmount = totalAllAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        templateDTO.setTotalAllAmount(StrUtils.doubleFormatString(totalAllAmount.doubleValue()));
        templateDTO.setTotalAllNum(StrUtils.intFormatString(totalAllNum.intValue()));
    }

    /**
     * 根据sku/发票描述/费项+po合并明细
     **/
    private void mergeJDItemByKey(List<Map<String, Object>> itemList, Map<String, Map<String, Object>> keyMap, boolean isGroupBrand, User user) throws Exception {

        Map<Long, SettlementConfigModel> settlementConfigModelMap = new HashMap<>();

        List<Map<String, Object>> purchaseItemList = new ArrayList<>();
        List<Map<String, Object>> tobItemList = new ArrayList<>();


        //注：若费项为“采购退货”类型，则发票模板上按照费项合并金额，无需体现退货商品明细
        for (Map<String, Object> map : itemList) {
            Long projectId = (Long) map.get("projectId");

            SettlementConfigModel settlementConfigModel = settlementConfigModelMap.get(projectId);
            if (settlementConfigModel == null) {
                settlementConfigModel = settlementConfigDao.searchById(projectId);
            }

            settlementConfigModelMap.put(projectId, settlementConfigModel);

            if (("采购退货").equals(settlementConfigModel.getProjectName())) {
                purchaseItemList.add(map);
            } else {
                tobItemList.add(map);
            }
        }

        /**1）若应收单中存在商品SKU，先以SKU维度汇总金额显示发票金额；
         （2）若无商品SKU的，先看“发票描述”字段（本期补扣款页面新增字段）是否有值，有值则按照相同发票描述做应收与费用的正负相抵，汇总金额显示；
         （3）若无发票描述，再以相应的”费项名称+PO“做金额显示*/
        for (Map<String, Object> map : tobItemList) {
            String key = null;
            BigDecimal totalPrice = (BigDecimal) map.get("totalPrice");
            BigDecimal totalNum = (BigDecimal) map.get("totalNum");
            Long goodsId = (Long) map.get("goodsId");
            String goodsNo = (String) map.get("goodsNo");
            String platformSku = null;
            if (map.containsKey("platformSku")) {
                platformSku = (String) map.get("platformSku");
            }
            String invoiceDescription = (String) map.get("invoiceDescription");
            Long projectId = (Long) map.get("projectId");
            String poNo = (String) map.get("poNo");
            Long parentBrandId = (Long) map.get("parentBrandId");

            if (isGroupBrand && parentBrandId != null) {
                Map<String, Object> brandParam = new HashMap<>();
                brandParam.put("brandSuperiorId", parentBrandId);
                BrandSuperiorMongo brandSuperiorMongo = brandSuperiorMongoDao.findOne(brandParam);
                if (brandSuperiorMongo != null) {
                    map.put("parentBrandName", brandSuperiorMongo.getName());
                }
            }

            if (StringUtils.isNotBlank(goodsNo)) {
                if (isGroupBrand) {
                    key = goodsNo + "_" + platformSku + "_" + parentBrandId;
                } else {
                    key = goodsNo + "_" + platformSku;
                }
                Map<String, Object> goodsParam = new HashMap<>();
                goodsParam.put("goodsNo", goodsNo);
                goodsParam.put("merchantId", user.getMerchantId());
                MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(goodsParam);
                if (merchandiseInfoMogo != null) {
                    map.put("barcode", merchandiseInfoMogo.getBarcode());
                    map.put("goodsName", merchandiseInfoMogo.getName());
                    if (merchandiseInfoMogo.getProductId() != null) {
                        Map<String, Object> productParam = new HashMap<>();
                        productParam.put("productId", merchandiseInfoMogo.getProductId());
                        ProductInfoMongo productInfoMongo = productInfoMongoDao.findOne(productParam);
                        if (productInfoMongo != null && productInfoMongo.getUnit() != null && productInfoMongo.getUnit() != 0) {
                            Map<String, Object> unitParam = new HashMap<>();
                            unitParam.put("unitId", productInfoMongo.getUnit());
                            UnitMongo unitMongo = unitMongoDao.findOne(unitParam);
                            map.put("unit", unitMongo.getName());
                        }
                    }
                }
                BrandMongo brand = brandMongoDao.getBrandMongo(goodsId);
                if (brand != null) {
                    map.put("brandId", brand.getBrandId());
                    map.put("brandName", brand.getName());
                }
                if (StringUtils.isNotBlank(platformSku)) {
                    map.put("goodsNo", platformSku);
                }
            } else {
                String description = null;
                if (StringUtils.isNotBlank(invoiceDescription)) {
                    key = invoiceDescription;
                    description = invoiceDescription;
                } else {
                    key = projectId + "_" + poNo;
                    SettlementConfigModel settlementConfigModel = settlementConfigModelMap.get(projectId);
                    description = settlementConfigModel.getProjectName();
                    if (StringUtils.isNotBlank(poNo)) {
                        description = description + "_" + poNo;
                    }
                }
                if (isGroupBrand) {
                    key = key + "_" + parentBrandId;
                }
                map.put("goodsNo", "");
                map.put("barcode", "");
                map.put("goodsName", description);
                map.put("unit", "");
                map.put("brandId", "");
                map.put("brandName", "");
            }

            if (keyMap.containsKey(key)) {
                Map<String, Object> existMap = keyMap.get(key);
                BigDecimal existPrice = (BigDecimal) existMap.get("totalPrice");
                BigDecimal existNum = (BigDecimal) existMap.get("totalNum");
                if (existPrice != null) {
                    if (totalPrice != null) {
                        totalPrice = totalPrice.add(existPrice);
                    } else {
                        totalPrice = existPrice;
                    }
                }

                if (existNum != null) {
                    if (totalNum != null) {
                        totalNum = totalNum.add(existNum);
                    } else {
                        totalNum = existNum;
                    }
                }

            }
            if (totalNum != null && totalPrice != null && totalNum.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal price = totalPrice.divide(totalNum, 2, BigDecimal.ROUND_HALF_UP);
                map.put("price", price);
                totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
            } else {
                map.put("price", null);
            }

            map.put("totalPrice", totalPrice);
            map.put("totalNum", totalNum);
            keyMap.put(key, map);
        }

        //注：若费项为“采购退货”类型，则发票模板上按照费项合并金额，无需体现退货商品明细
        BigDecimal purchasePrice = new BigDecimal("0");
        BigDecimal purchaseNum = new BigDecimal("0");
        SettlementConfigModel purchaseSettlementConfig = null;
        for (Map<String, Object> map : purchaseItemList) {
            BigDecimal totalPrice = (BigDecimal) map.get("totalPrice");
            BigDecimal totalNum = (BigDecimal) map.get("totalNum");
            Long projectId = (Long) map.get("projectId");

            purchasePrice = purchasePrice.add(totalPrice);
            purchaseNum = purchaseNum.add(totalNum);
            purchaseSettlementConfig = settlementConfigModelMap.get(projectId);
        }

        if (purchaseItemList.size() > 0) {
            Map<String, Object> purchaseMap = new HashMap<>();

            purchaseMap.put("totalPrice", purchasePrice.setScale(2, BigDecimal.ROUND_HALF_UP));
            purchaseMap.put("totalNum", purchaseNum.setScale(2, BigDecimal.ROUND_HALF_UP));
            purchaseMap.put("goodsName", purchaseSettlementConfig.getProjectName());

            keyMap.put(purchaseSettlementConfig.getProjectName(), purchaseMap);
        }

    }

}
