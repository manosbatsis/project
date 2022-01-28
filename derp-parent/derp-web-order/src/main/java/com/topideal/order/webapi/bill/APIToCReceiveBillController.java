package com.topideal.order.webapi.bill;

import com.alibaba.fastjson.JSON;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.*;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillAuditItemModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillCostItemModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillVerifyItemModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.MerchantShopRelMongoDao;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.order.service.bill.ToCReceiveBillAuditItemService;
import com.topideal.order.service.bill.ToCReceiveBillCostItemService;
import com.topideal.order.service.bill.ToCReceiveBillService;
import com.topideal.order.service.bill.ToCReceiveBillVerifyItemService;
import com.topideal.order.service.filetemp.FileTempService;
import com.topideal.order.service.timer.FileTaskService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.bill.form.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/webapi/order/toCReceiveBill")
@Api(tags = "To C应收账单")
public class APIToCReceiveBillController {
    private static final Logger LOG = Logger.getLogger(APIToCReceiveBillController.class) ;

    @Autowired
    private ToCReceiveBillService toCReceiveBillService;
    @Autowired
    private ToCReceiveBillCostItemService toCReceiveBillCostItemService;
    @Autowired
    private ToCReceiveBillAuditItemService toCReceiveBillAuditItemService;
    @Autowired
    private ToCReceiveBillVerifyItemService toCReceiveBillVerifyItemService;
    @Autowired
    private MerchantShopRelMongoDao merchantShopRelMongoDao;
    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao;
    @Autowired
    private CustomerInfoMongoDao customerInfoMongoDao;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private FileTempService fileTempService;
    @Autowired
    private FileTaskService fileTaskService;


    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listTocReceiveBill.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<TocSettlementReceiveBillDTO> listTocReceiveBill(TocSettlementReceiveBillForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            TocSettlementReceiveBillDTO dto = new TocSettlementReceiveBillDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());
            dto = toCReceiveBillService.listReceiveBillByPage(user,dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
        }
    }

    /**
     * 预新增应收账单
     * @return
     */
    @ApiOperation(value = "预新增应收账单")
    @GetMapping(value = "/saveAdvanceTocSettlementBill.asyn")
    public ResponseBean saveAdvanceTocSettlementBill() {
        try {
            String code = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_PTJS);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, code);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    /**
     * 新增Toc应收账单
     * @return
     */
    @ApiOperation(value = "新增Toc应收账单")
    @PostMapping(value = "/saveTocSettlementBill.asyn")
    public ResponseBean saveOrModifyAdvancePaymentBill(TocSettlementReceiveBillSaveForm form, @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());

            TocSettlementReceiveBillDTO dto = new TocSettlementReceiveBillDTO();
            BeanUtils.copyProperties(form, dto);

            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils().addObject(dto.getBuId())
                    .addObject(dto.getStorePlatformCode()).addObject(dto.getSettlementDateStr())
                    .addObject(dto.getShopTypeCode())
                    .addObject(dto.getOriCurrency())
                    .addObject(dto.getSettlementCurrency()).addObject(dto.getCustomerId())
                    .addObject(file).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(dto.getShopTypeCode())) {
                if (StringUtils.isBlank(dto.getShopCode())) {
                    return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "店铺不能为空！");
                }
            }

            List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());

            // 数据为空
            if (data == null) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            Long id = dto.getId();
            TocSettlementReceiveBillModel billModel = new TocSettlementReceiveBillModel();
            if (id == null) {
                if(StringUtils.isBlank(dto.getCode())) {
                    return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "获取结算编码失败");
                }

                MerchantShopRelMongo shop = null;
                if (StringUtils.isNotBlank(dto.getShopCode())) {
                    Map<String, Object> shopParams = new HashMap<String, Object>();
                    shopParams.put("status", "1");
                    shopParams.put("shopCode", dto.getShopCode());
                    shop = merchantShopRelMongoDao.findOne(shopParams);
                    if (shop == null) {
                        return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "店铺不能为空！");
                    }
                }
                Map<String, Object> buParams = new HashMap<String, Object>();
                buParams.put("status", "1");
                buParams.put("merchantId", user.getMerchantId());
                buParams.put("buId", dto.getBuId());
                MerchantBuRelMongo bu = merchantBuRelMongoDao.findOne(buParams);
                if (bu == null) {
                    return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "事业部不能为空！");
                }
                Map<String, Object> customerParams = new HashMap<String, Object>();
                customerParams.put("status", "1");
                customerParams.put("customerid", dto.getCustomerId());
                CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerParams);
                if (customer == null) {
                    return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "客户不存在！");
                }

//                billModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_PTJS));
                billModel.setCode(dto.getCode());
                billModel.setSettlementDate(TimeUtils.strToSqlDate(dto.getSettlementDateStr()));
                billModel.setShopTypeCode(dto.getShopTypeCode());
                billModel.setStorePlatformCode(dto.getStorePlatformCode());
                billModel.setExternalCode(dto.getExternalCode());
                billModel.setOriCurrency(dto.getOriCurrency());
                if (shop != null) {
                    billModel.setShopName(shop.getShopName());
                    billModel.setShopCode(shop.getShopCode());
                }
                billModel.setBuId(bu.getBuId());
                billModel.setBuName(bu.getBuName());
                billModel.setCustomerId(customer.getCustomerid());
                billModel.setCustomerName(customer.getName());
                billModel.setSettlementCurrency(dto.getSettlementCurrency());
                billModel.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_07);
                billModel.setCreaterId(user.getId());
                billModel.setCreater(user.getName());
                billModel.setMerchantId(user.getMerchantId());
                billModel.setMerchantName(user.getMerchantName());
                billModel.setNcStatus(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_10);
                billModel.setReceivableAmount(BigDecimal.ZERO);
                billModel.setInvoiceStatus(DERP_ORDER.TOCRECEIVEBILL_INVOICESTATUS_00);
            } else {
                billModel.setBillStatus(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_07);
                billModel.setId(id);
            }
            toCReceiveBillService.saveTocSettlementBill(billModel, file);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常);
        }
    }

    /**
     * 获取店铺关联客户列表
     */
    @ApiOperation(value = "获取店铺关联客户列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "storePlatformCode", value = "电商平台编码", required = false),
            @ApiImplicitParam(name = "shopTypeCode", value = "运营类型值编码", required = false),
            @ApiImplicitParam(name = "shopCode", value = "店铺编码", required = false)})
    @PostMapping("/getCustomerListByShop.asyn")
    private ResponseBean getCustomerListByShop(@RequestParam(value = "token", required = true) String token,
                                               @RequestParam(value = "storePlatformCode", required = false) String storePlatformCode,
                                               @RequestParam(value = "shopTypeCode", required = false) String shopTypeCode,
                                               @RequestParam(value = "shopCode", required = false) String shopCode) {
        List<SelectBean> customerSelectBean = new ArrayList<>();
        try {
            MerchantShopRelMongo dto = new MerchantShopRelMongo();
            dto.setStoreTypeCode(shopTypeCode);
            dto.setStorePlatformCode(storePlatformCode);
            dto.setShopCode(shopCode);
            // 响应结果集
            customerSelectBean = toCReceiveBillService.getCustomerListByShop(dto);
        } catch (Exception e) {

            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, customerSelectBean);//成功
    }


    /**
     * 根据id查看详情
     *
     * @param id
     * @param token
     * @return
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "根据id查看详情")
    @PostMapping(value = "/detail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "id", required = true)})
    public ResponseBean detail(long id, String token) throws SQLException {

        Map resultMap = new HashMap<String, Object>();

        String month = null;
        TocSettlementReceiveBillDTO tocSettlementReceiveBillDTO = toCReceiveBillService.searchDTOById(id);
        if (tocSettlementReceiveBillDTO.getSettlementDate() != null) {
            month = TimeUtils.formatMonth(tocSettlementReceiveBillDTO.getSettlementDate()).replace("-", "年");
        }
        BigDecimal totalPrice = new BigDecimal("0");
        BigDecimal totalRmbPrice = new BigDecimal("0");
        BigDecimal totalNum = new BigDecimal("0");
        //应收结算
        List<Map<String, Object>> receiveMap = toCReceiveBillService.receiveTotalById(id);
        for (Map<String, Object> map : receiveMap) {
            BigDecimal price = (BigDecimal) map.get("totalPrice");
            BigDecimal rmbPrice = (BigDecimal) map.get("totalRmbPrice");
            BigDecimal num = (BigDecimal) map.get("totalNum");
            if (price != null) {
                totalPrice = totalPrice.add(price);
            }
            if (num != null) {
                totalNum = totalNum.add(num);
            }
            if (rmbPrice != null) {
                totalRmbPrice = totalRmbPrice.add(rmbPrice);
            }
        }
        //抵扣费用
        List<Map<String, Object>> deductionMap = toCReceiveBillService.deductionTotalById(id);
        for (Map<String, Object> map : deductionMap) {
            BigDecimal price = (BigDecimal) map.get("totalPrice");
            BigDecimal num = (BigDecimal) map.get("totalNum");
            BigDecimal rmbPrice = (BigDecimal) map.get("totalRmbPrice");
            if (price != null) {
                totalPrice = totalPrice.add(price);
            }
            if (num != null) {
                totalNum = totalNum.add(num);
            }
            if (rmbPrice != null) {
                totalRmbPrice = totalRmbPrice.add(rmbPrice);
            }
        }
        //审核记录
        List<TocSettlementReceiveBillAuditItemModel> receiveBillAuditItemModels = toCReceiveBillAuditItemService.listByBillId(id);
        //核算记录
        List<TocSettlementReceiveBillVerifyItemModel> verifyItemModels = toCReceiveBillVerifyItemService.listByBillId(id);
        BigDecimal alreadyPrice = new BigDecimal("0");
        for (TocSettlementReceiveBillVerifyItemModel itemModel : verifyItemModels) {
            BigDecimal price = itemModel.getPrice();
            alreadyPrice = alreadyPrice.add(price);
        }
        TocSettlementReceiveBillDetailDTO dto = new TocSettlementReceiveBillDetailDTO();
        dto.setMonth(month);
        dto.setBill(tocSettlementReceiveBillDTO);
        dto.setTotalPrice(totalPrice);
        dto.setTotalRmbPrice(totalRmbPrice);
        dto.setTotalNum(totalNum);
        dto.setTotalPriceLabel(NumberToCN.number2CNMontrayUnit(totalPrice));
        dto.setDeductionMap(deductionMap);
        dto.setReceiveMap(receiveMap);
        dto.setReceiveBillAuditItemModels(receiveBillAuditItemModels);
        dto.setVerifyItems(verifyItemModels);
        dto.setAlreadyPrice(alreadyPrice);

        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
    }

    /**
     * 获取toc应收明细分页数据
     * @param token
     * @return
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取toc应收明细分页数据")
    @PostMapping(value = "/listReceiveItem.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "billId", value = "billId", required = true),
            @ApiImplicitParam(name = "begin", value = "begin", required = true),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", required = true)})
    public ResponseBean listReceiveItem(long billId, String token, Integer pageSize, Integer begin) throws SQLException {
        TocSettlementReceiveBillItemDTO dto = new TocSettlementReceiveBillItemDTO();
        try {
            dto.setBillId(billId);
            dto.setBegin(begin);
            dto.setPageSize(pageSize);
            // 响应结果集
            dto = toCReceiveBillService.listReceiveItemByPage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
    }


    /**
     * 获取toc费用明细分页数据
     * @param billId
     * @param token
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取toc费用明细分页数据")
    @PostMapping(value = "/listReceiveCostItem.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "billId", value = "billId", required = true),
            @ApiImplicitParam(name = "begin", value = "begin", required = true),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", required = true)})
    public ResponseBean listReceiveCostItem(long billId, String token, Integer pageSize, Integer begin) throws SQLException {
        TocSettlementReceiveBillCostItemDTO dto = new TocSettlementReceiveBillCostItemDTO();
        try {
            dto.setBillId(billId);
            dto.setBegin(begin);
            dto.setPageSize(pageSize);
            // 响应结果集
            dto = toCReceiveBillService.listReceiveCostItemByPage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
    }

    /**
     * 获取店铺
     * @param token
     * @param storePlatformCode
     * @param shopTypeCode
     * @param shopCode
     * @return
     */
    @ApiOperation(value = "获取店铺")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "storePlatformCode", value = "电商平台编码", required = false),
            @ApiImplicitParam(name = "shopTypeCode", value = "运营类型值编码", required = false),
            @ApiImplicitParam(name = "shopCode", value = "店铺编码", required = false)})
    @PostMapping("/getShopInfo.asyn")
    private ResponseBean getShopInfo(@RequestParam(value = "token", required = true) String token,
                                     @RequestParam(value = "storePlatformCode", required = false) String storePlatformCode,
                                     @RequestParam(value = "shopTypeCode", required = false) String shopTypeCode,
                                     @RequestParam(value = "shopCode", required = false) String shopCode) {
        List<MerchantShopRelMongo> mongoList = new ArrayList<>();
        try {
            Map<String, Object> params = new HashMap<>();
            MerchantShopRelMongo merchantShopRelMongo = new MerchantShopRelMongo();
            merchantShopRelMongo.setShopCode(shopCode);
            merchantShopRelMongo.setShopTypeCode(shopTypeCode);
            merchantShopRelMongo.setStorePlatformCode(storePlatformCode);


            params.put("status", DERP_SYS.MERCHANTSHOPREL_STATUS_1);
            if (StringUtils.isNotBlank(merchantShopRelMongo.getShopTypeCode())) {
                params.put("shopTypeCode", merchantShopRelMongo.getShopTypeCode());
            }
            if (StringUtils.isNotBlank(merchantShopRelMongo.getStorePlatformCode())) {
                params.put("storePlatformCode", merchantShopRelMongo.getStorePlatformCode());
            }
            if (StringUtils.isNotBlank(merchantShopRelMongo.getShopCode())) {
                params.put("shopCode", merchantShopRelMongo.getShopCode());
            }
            mongoList = merchantShopRelMongoDao.findAll(params);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, mongoList);//成功
    }


    /**
     * 导出失败原因
     * @param id
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "导出失败原因")
    @GetMapping(value = "/downLoad.asyn")
    public ResponseBean downLoad(Long id,String token, HttpServletResponse response) throws Exception {
        try {
            //查询该任务
            if (id == null) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            TocSettlementReceiveBillDTO tocSettlementReceiveBillDTO = toCReceiveBillService.searchDTOById(id);
            if (StringUtils.isEmpty(tocSettlementReceiveBillDTO.getErrorMsgPath())) {
                return null;
            }
            File file = new File(tocSettlementReceiveBillDTO.getErrorMsgPath());

            //转换中文否则可能会产生乱码
            String downloadFilename = URLEncoder.encode(tocSettlementReceiveBillDTO.getErrorMsgPath().substring(tocSettlementReceiveBillDTO.getErrorMsgPath().lastIndexOf("/") + 1), "UTF-8");
            // 指明response的返回对象是文件流
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;fileName=" + downloadFilename);// 设置文件名
            InputStream in = new FileInputStream(file);
            BufferedInputStream inb = new BufferedInputStream(in);
            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[1024];

            int i = inb.read(buffer);
            while (i != -1) {
                out.write(buffer, 0, i);
                i = inb.read(buffer);
            }
            out.close();
            inb.close();
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
    }

    /**
     * 删除
     * @return
     * @throws
     */
    @ApiOperation(value = "删除")
    @PostMapping(value = "/delToCReceiveBill.asyn")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "ids", required = true)})
    public ResponseBean delToCReceiveBill(String token,String ids) {
        try {
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = toCReceiveBillService.delToCReceiveBill(list);
            if (!b) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017);
            }
        }  catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
    }

    /**
     * 访问增加补扣款页面
     * @param id
     * @return
     */
    @ApiOperation(value = "访问增加补扣款页面")
    @GetMapping(value = "/toFreeADDPage.asyn")
    public ResponseBean toFreeADDPage(Long id,String token) throws SQLException {
        Map<String,Object> map=new HashMap<>();
        TocSettlementReceiveBillDTO tocSettlementReceiveBillDTO = toCReceiveBillService.searchDTOById(id);
        //费用明细
        List<TocSettlementReceiveBillCostItemModel> receiveBillCostItemDTOS = toCReceiveBillCostItemService.listCostByHandAdd(id);
        List<SelectBean> brandSelectBeans = toCReceiveBillService.getBrandSelectBeans();
        map.put("bill", tocSettlementReceiveBillDTO);
        map.put("orderItems", receiveBillCostItemDTOS);
        map.put("brandSelectBeans", brandSelectBeans);
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,map);
    }

    /**
     * 保存补扣款信息
     * @return
     * @throws SQLException
     */
    @ApiOperation(value = "保存补扣款信息")
    @PostMapping(value = "/saveToCReceiveBillCost.asyn", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBean saveToCReceiveBillCost(@RequestBody ToCReceiveBillCostSaveForm form) throws SQLException {
        Map<String, Object> map=new HashMap<>();
       try{
           User user=ShiroUtils.getUserByToken(form.getToken());
           map=toCReceiveBillCostItemService.saveAPIReceiveBillCost(user,form.getId(),form.getCostItem());
           String message=(String)map.get("msg");
           String code=(String)map.get("code");
           if("00".equals(code)){
               return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);
           }
           return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);
       }catch (Exception e) {
           e.printStackTrace();
           LOG.error(e.getMessage(), e);
           return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
       }
    }


    /**
     * 提交
     * @param id
     * @return
     */
    @ApiOperation(value = "提交")
    @GetMapping(value = "/submitReceiveBill.asyn")
    public ResponseBean submitReceiveBill(Long id,String token) {
        Map<String, String> result = new HashMap<>();
        try {
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user=ShiroUtils.getUserByToken(token);
            // 响应结果集
            result = toCReceiveBillService.confirmReceiveBill(user,id);
            String message=(String)result.get("message");
            String code=(String)result.get("code");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message);
        }catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    /**
     * 审核
     * @return
     */
    @ApiOperation(value = "审核")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "billId", value = "id", required = true),
            @ApiImplicitParam(name = "auditResult", value = "审核结果00-审批通过 01-驳回", required = true),
            @ApiImplicitParam(name = "auditRemark", value = "审核备注", required = false),
            @ApiImplicitParam(name = "billInDate", value = "入账月份", required = false)})
    @PostMapping(value = "/auditItem.asyn")
    private ResponseBean auditItem(String token, Long billId, String auditResult, String auditRemark, String billInDate){
        Map<String, String> result = new HashMap<>();
        try {
            TocSettlementReceiveBillAuditItemModel model =new TocSettlementReceiveBillAuditItemModel();
            model.setBillId(billId);
            model.setAuditRemark(auditRemark);
            model.setAuditResult(auditResult);

            User user=ShiroUtils.getUserByToken(token);
            // 响应结果集
            result = toCReceiveBillService.auditReceiveBill(user,model, billInDate);

            //toc暂估应收单核销
            if ("00".equals(result.get("code")) &&
                    DERP_ORDER.TOCRECEIVEBILLAUDIT_AUDITRESULT_00.equals(model.getAuditResult())) {
                Map<String, Object> tocTempMap = new HashMap<String, Object>();
                tocTempMap.put("billCode", result.get("billCode"));
                JSONObject tocTempJson = JSONObject.fromObject(tocTempMap);
                rocketMQProducer.send(tocTempJson.toString(), MQOrderEnum.TOC_RECEIVE_BILL_VERIFY.getTopic(), MQOrderEnum.TOC_RECEIVE_BILL_VERIFY.getTags());
            }

            if("01".equals(result.get("code"))) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), result.get("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, result);
    }

    /**
     * 获取提交NC信息
     * @param ids
     */
    @ApiOperation(value = "获取提交NC信息")
    @PostMapping("/listReceiveBillsToNC.asyn")
    private ResponseBean listReceiveBillsToNC(String ids,String token) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(ids).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            // 响应结果集
            List<ToCReceiveBillToNCDTO> receiveBillToNCDTOs = toCReceiveBillService.listReceiveBillsToNCById(ids);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,receiveBillToNCDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    /**
     * 同步NC
     * @param ids
     * @param token
     * @return
     */
    @ApiOperation(value = "同步NC")
    @PostMapping("/synNC.asyn")
    private ResponseBean synNC(String ids,String token) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(ids).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user=ShiroUtils.getUserByToken(token);
            // 响应结果集
            Map<String, String> result = toCReceiveBillService.synNC(user,ids);
            String code=(String)result.get("code");
            String message=(String)result.get("message");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "ids", value = "结算id，多个以“，“隔开", required = true)})
    @PostMapping("/exportBill.asyn")
    private ResponseBean exportBill(String ids,String token) throws Exception {

        User user = ShiroUtils.getUserByToken(token);
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("code", "00");
        retMap.put("message", "成功");

        try {
            if(StringUtils.isBlank(ids)) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "平台编码不能为空");
            }

            TocSettlementReceiveBillDTO dto = new TocSettlementReceiveBillDTO();
            dto.setIds(ids);
            dto.setMerchantId(user.getMerchantId());

            List<Map<String, Object>> maps = toCReceiveBillService.listForExport(dto, user);

            if (maps == null || maps.size() == 0) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前查询条件下数据为空");
            }

            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_TOCSETTLEMENT);
            taskModel.setTaskName("ToC平台结算单导出");
            taskModel.setMerchantId(user.getMerchantId());
//            taskModel.setOwnMonth();
            taskModel.setState("1");
            taskModel.setRemark("ToC平台结算单导出");
            taskModel.setUsername(user.getName());
            JSONObject paramJson = new JSONObject();
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("billIds", ids);
            taskModel.setParam(paramJson.toString());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
            taskModel.setModule(DERP_REPORT.FILETASK_MODULE_2);
            taskModel.setUserId(user.getId());
            fileTaskService.save(taskModel);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", "01");
            retMap.put("message", "网络故障");
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, retMap);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, retMap);
    }

    /**
     * 提交作废申请
     */
    @ApiOperation(value = "提交作废申请")
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "id", required = true),
            @ApiImplicitParam(name = "invalidRemark", value = "作废备注", required = true)})
    @PostMapping("/saveInvalidBill.asyn")
    private ResponseBean saveInvalidBill(String id, String invalidRemark,String token) {
        Map<String, String> result = new HashMap<>();
        try {
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils()
                    .addObject(id).addObject(invalidRemark).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user=ShiroUtils.getUserByToken(token);
            // 响应结果集
            result = toCReceiveBillService.saveInvalidBill(user,id, invalidRemark);
            String code=(String)result.get("code");
            String message=(String)result.get("message");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    /**
     * 校验toc结算单是否满足开票条件
     * @param ids
     */
    @ApiOperation(value="校验toc结算单是否满足开票条件")
    @PostMapping("/validateInfo.asyn")
    public ResponseBean validateInfo(String ids,String token) {
        try {
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            Map<String, String> resultMap = toCReceiveBillService.validate(list);
            String code=(String)resultMap.get("code");
            String message=(String)resultMap.get("msg");
            if("00".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    }


    /**
     * 访问开票页面
     * @param ids
     */
//    @ApiOperation(value="访问开票页面")
//    @GetMapping("/toInvoicePage.asyn")
//    public ResponseBean toInvoicePage(String ids, String tempId, String token) {
//        Map<String, Object> map=new HashMap<>();
//
//        try {
//            FileTempDTO fileTempDTO= fileTempService.searchById(Long.valueOf(tempId));
//            Map<String, Object> resultMap = toCReceiveBillService.getInvoiceInfo(ids);
//            MerchantInfoMongo merchantInfo = (MerchantInfoMongo) resultMap.get("merchantInfo");
//            String currency = (String) resultMap.get("currency");
//            String billDate = (String) resultMap.get("billDate");
//            BigDecimal totalAmount = (BigDecimal) resultMap.get("totalAllAmount");
//            List<Map<String, Object>> items = (List<Map<String, Object>>) resultMap.get("items");
//
//            map.put("merchantInfo",merchantInfo);
//            map.put("ids",ids);
//            map.put("tempId",tempId);
//            map.put("fileTempCode",fileTempDTO.getCode());
//            map.put("currency",currency);
//            map.put("billDate",billDate);
//            map.put("totalAllAmount",totalAmount);
//            map.put("itemLists",items);
//            map.put("currency",currency);
//            //map.put("url",fileTempModel.getToUrl());
//
//            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,map);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
//        }
//    }

    /**
     * 访问开票页面
     * @param form
     */
    @ApiOperation(value = "访问开票页面")
    @PostMapping(value = "/toInvoicePage.asyn")
    public ResponseBean toInvoicePage(TocReceiveBillInvoiceForm form) {

        try {
            LOG.info("toInvoicePage form:" + JSON.toJSONString(form));
            User user = ShiroUtils.getUserByToken(form.getToken());
            //返回发票模板的html
            String excelHtml = toCReceiveBillService.generateInvoiceHtml(form, user);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, excelHtml);

        } catch(Exception e){
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }


    /**
     * 提交保存发票
     * @throws SQLException
     */
    @ApiOperation(value="提交保存发票")
    @PostMapping("/saveSubmitInvoice.asyn")
    public ResponseBean saveSubmitInvoice(@RequestBody TocReceiveBillInvoiceForm form) {

        try {
            LOG.info("saveSubmitInvoice form:" + JSON.toJSONString(form));
            if (form == null) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user = ShiroUtils.getUserByToken(form.getToken());
            // 响应结果集
            toCReceiveBillService.modifyInvoiceContract(form, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    }


    /**
     * 导出PDF
     * @param ids
     */
    @ApiOperation(value="导出PDF")
    @GetMapping("/exportPDF.asyn")
    public ResponseBean exportPDF(String token,String ids, HttpServletResponse response) {
        try {
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            String pdfFile = toCReceiveBillService.exportNcBillPDF(list);
            File filePath = new File(pdfFile);
            //转换中文否则可能会产生乱码
            String downloadFilename = URLEncoder.encode("应收账单打印模板.zip", "UTF-8");
            // 指明response的返回对象是文件流
            response.setContentType("application/octet-stream");
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            File[] pdfFiles = filePath.listFiles();
            if(pdfFiles != null && pdfFiles.length > 0){
                for(File file : pdfFiles){
                    //获取模板,压缩到zip文件中
                    InputStream in = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    byte[] buffer = new byte[1024];
                    int r = 0;
                    while ((r = in.read(buffer)) != -1) {
                        zos.write(buffer, 0, r);
                    }
                    in.close();
                }
                deleteDir(pdfFile);
            }
            zos.flush();
            zos.close();
            return null;
        }  catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    }


    private void deleteDir(String dirPath) {
        File file = new File(dirPath);
        if(file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if(files == null) {
                file.delete();
            } else  {
                for (int i = 0; i < files.length; i++) {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }

    @ApiOperation(value="获取核销明细")
    @PostMapping("/getVerifyItems.asyn")
    public ResponseBean getVerifyItems(Long id) {
        try {
            BigDecimal totalRmbPrice = toCReceiveBillService.statisticsRmbAmount(id);
            //核算记录
            List<TocSettlementReceiveBillVerifyItemModel> verifyItemModels = toCReceiveBillService.listVerifyByBillId(id);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("totalRmbAmount", totalRmbPrice);
            resultMap.put("verifyItemModels", verifyItemModels);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, verifyItemModels);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    }


    @ApiOperation(value="保存核销信息")
    @PostMapping(value = "/saveReceiveBillVerify.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean saveReceiveBillVerify(TocSettlementReceiveBillVerifyItemForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            TocSettlementReceiveBillVerifyItemDTO dto = new TocSettlementReceiveBillVerifyItemDTO();
            BeanUtils.copyProperties(form, dto);
            Map<String, String> resultMap = toCReceiveBillService.saveVerifyItem(dto, user);
            String code = resultMap.get("code");
            String message = resultMap.get("message");
            if("01".equals(code)){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(),message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
    }

//    @ApiOperation(value="导出结算单汇总")
//    @GetMapping("/exportSummaryBill.asyn")
//    private void exportSummaryBill(HttpServletResponse response, HttpServletRequest request, TocSettlementReceiveBillForm form) throws Exception{
//        try {
//            User user = ShiroUtils.getUserByToken(form.getToken());
//            TocSettlementReceiveBillDTO dto = new TocSettlementReceiveBillDTO();
//            BeanUtils.copyProperties(form, dto);
//            dto.setMerchantId(user.getMerchantId());
//            //表头信息
//            List<Map<String,Object>> billExportList = toCReceiveBillService.listForExport(dto, user);
//
//            //表头信息
//            String mainSheetName = "平台结算单列表";
//            String[] mainColumns = {"公司","平台结算单号", "外部结算单号","事业部","运营类型","客户名称","平台名称","店铺名称",
//                    "应收金额(RMB)", "应收金额(原币)", "平台结算原币种","账单月份","入账月份","账单状态","创建人","创建时间","NC单号","NC状态","凭证名称","凭证状态"};
//            String[] mainKeys = {"merchantName","billCode","externalCode","buName", "shopTypeName","customerName","storePlatformName","shopName",
//                    "receivablePrice", "oriReceivableAmount", "oriCurrency","month","billInDate","billStatus", "creater","createTime", "ncCode", "ncStatus","voucherName","voucherStatus"};
//
//            //表体信息
//            List<Map<String,Object>> itemList = toCReceiveBillService.listForSummaryExport(dto, user);
//
//            String itemSheetName = "应收详情";
//            String[] itemColumns = {"平台结算单","外部结算单号","结算费项", "平台费项名称", "母品牌", "补扣款类型", "结算金额（结算原币）","结算金额（RMB）"};
//            String[] itemKeys = {"billCode", "externalCode", "projectName", "PlatformSettlementName", "parentBrandName", "billType", "receivePrice", "receiveRmbPrice"};
//            //生成表格
//            ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, mainColumns, mainKeys, billExportList);
//            ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, itemList);
//
//            List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
//            sheets.add(mainSheet) ;
//            sheets.add(itemSheet) ;
//
//            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
//            //导出文件
//            String fileName = "平台结算单汇总导出";
//            FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @ApiOperation(value="导出结算单汇总")
    @PostMapping("/exportSummaryBill.asyn")
    private ResponseBean exportSummaryBill(HttpServletResponse response, HttpServletRequest request, TocSettlementReceiveBillForm form) throws Exception{
        Map<String, Object> retMap = new HashMap<String, Object>(4);
        retMap.put("code", "00");
        retMap.put("message", "成功");
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            TocSettlementReceiveBillDTO dto = new TocSettlementReceiveBillDTO();
            BeanUtils.copyProperties(form, dto);
            dto.setMerchantId(user.getMerchantId());

            if(StringUtils.isBlank(dto.getIds())) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "请勾选汇总导出的记录");
            }

            //表头信息
            List<Map<String,Object>> billExportList = toCReceiveBillService.listForExport(dto, user);
            if (billExportList == null || billExportList.isEmpty()) {
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), "当前查询条件下数据为空");
            }

            FileTaskMongo taskModel = new FileTaskMongo();
            taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
            taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_TOCSETTLEMENT_SUMMARY);
            taskModel.setTaskName("平台结算单汇总导出");
            taskModel.setMerchantId(user.getMerchantId());
            String month = TimeUtils.formatMonth(new Date());
            taskModel.setOwnMonth(month);
            taskModel.setState("1");
            taskModel.setRemark("平台结算单汇总导出");
            taskModel.setUsername(user.getName());
            JSONObject paramJson = new JSONObject();
            paramJson.put("merchantId", user.getMerchantId());
            paramJson.put("billIds", dto.getIds());
            taskModel.setParam(paramJson.toString());
            taskModel.setCreateDate(TimeUtils.formatFullTime());
            taskModel.setModule(DERP_REPORT.FILETASK_MODULE_2);
            taskModel.setUserId(user.getId());
            fileTaskService.save(taskModel);
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("code", "01");
            retMap.put("message", "网络故障");
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, retMap);
    }

}


