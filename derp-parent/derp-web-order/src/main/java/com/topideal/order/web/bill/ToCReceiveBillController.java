package com.topideal.order.web.bill;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.dto.common.FileTempDTO;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillAuditItemModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillCostItemModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillVerifyItemModel;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.MerchantShopRelMongoDao;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.order.service.bill.ToCReceiveBillAuditItemService;
import com.topideal.order.service.bill.ToCReceiveBillCostItemService;
import com.topideal.order.service.bill.ToCReceiveBillService;
import com.topideal.order.service.bill.ToCReceiveBillVerifyItemService;
import com.topideal.order.service.filetemp.FileTempService;
import com.topideal.order.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description: toc应收账单
 **/
@Controller
@RequestMapping("/toCReceiveBill")
public class ToCReceiveBillController {

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

    /**
     * 访问列表页面
     * @param model
     */
    @RequestMapping("/toPage.html")
    public String toPage(Model model) throws Exception {
        return "derp/bill/toc-receive-bill-list";
    }

    /**
     * 获取分页数据
     * @param dto
     */
    @RequestMapping("/listTocReceiveBill.asyn")
    @ResponseBody
    private ViewResponseBean listTocReceiveBill(TocSettlementReceiveBillDTO dto) {
        try {
            User user = ShiroUtils.getUser();
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            toCReceiveBillService.listReceiveBillByPage(user,dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(dto);
    }

    /**
     * 访问新增页面
     * @param model
     */
    @RequestMapping("/toImportPage.html")
    public String toImportPage(Model model, Long id) throws Exception {
        if (id != null) {
            TocSettlementReceiveBillDTO tocSettlementReceiveBillDTO = toCReceiveBillService.searchDTOById(id);
            model.addAttribute("bill", tocSettlementReceiveBillDTO);
        }
        return "derp/bill/toc-receive-bill-import";
    }

    /**
     * 新增
     * @param dto
     */
    @RequestMapping("/saveTocSettlementBill.asyn")
    @ResponseBody
    public ViewResponseBean saveTocSettlementBill(TocSettlementReceiveBillDTO dto , @RequestParam(value = "file", required = false) MultipartFile file) {
        try{
            User user = ShiroUtils.getUser();
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils().addObject(dto.getBuId())
                    .addObject(dto.getStorePlatformCode()).addObject(dto.getSettlementDateStr())
                    .addObject(dto.getShopTypeCode())
                    .addObject(dto.getOriCurrency())
                    .addObject(dto.getSettlementCurrency()).addObject(dto.getCustomerId())
                    .addObject(file).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }

            if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(dto.getShopTypeCode())) {
                if (StringUtils.isBlank(dto.getShopCode())) {
                    return ResponseFactory.error(StateCodeEnum.ERROR_302, new RuntimeException("店铺不能为空！"));
                }
            }

            List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());

            if (data == null) {// 数据为空
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }
            Long id = dto.getId();
            TocSettlementReceiveBillModel billModel = new TocSettlementReceiveBillModel();
            if (id == null) {
                MerchantShopRelMongo shop = null;
                if (StringUtils.isNotBlank(dto.getShopCode())) {
                    Map<String, Object> shopParams = new HashMap<String, Object>();
                    shopParams.put("status", "1");
                    shopParams.put("shopCode", dto.getShopCode());
                    shop = merchantShopRelMongoDao.findOne(shopParams);
                    if (shop == null) {
                        return ResponseFactory.error(StateCodeEnum.ERROR_302, new RuntimeException("店铺不存在"));
                    }
                }
                Map<String, Object> buParams = new HashMap<String, Object>();
                buParams.put("status", "1");
                buParams.put("merchantId", user.getMerchantId());
                buParams.put("buId", dto.getBuId());
                MerchantBuRelMongo bu = merchantBuRelMongoDao.findOne(buParams);
                if (bu == null) {
                    return ResponseFactory.error(StateCodeEnum.ERROR_302, new RuntimeException("事业部不存在"));
                }
                Map<String, Object> customerParams = new HashMap<String, Object>();
                customerParams.put("status", "1");
                customerParams.put("customerid", dto.getCustomerId());
                CustomerInfoMogo customer = customerInfoMongoDao.findOne(customerParams);
                if (customer == null) {
                    return ResponseFactory.error(StateCodeEnum.ERROR_302, new RuntimeException("客户不存在"));
                }

                billModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_PTJS));
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
        }catch(Exception e){
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
    }

    /**
     * 获取店铺关联客户列表
     * @param dto
     */
    @RequestMapping("/getCustomerListByShop.asyn")
    @ResponseBody
    private ViewResponseBean getCustomerListByShop(MerchantShopRelMongo dto) {
        List<SelectBean> customerSelectBean = new ArrayList<>();
        try {
            // 响应结果集
            customerSelectBean = toCReceiveBillService.getCustomerListByShop(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(customerSelectBean);
    }

    /**
     * 访问详情页面
     *
     * @throws SQLException
     */
    @RequestMapping("/toDetailsPage.html")
    public String toDetailsPage(Long id, String pageSource, Model model) throws SQLException {

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
        model.addAttribute("month", month);
        model.addAttribute("bill", tocSettlementReceiveBillDTO);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalRmbPrice", totalRmbPrice);
        model.addAttribute("totalPriceLabel", NumberToCN.number2CNMontrayUnit(totalPrice));
        model.addAttribute("deductionMap", deductionMap);
        model.addAttribute("receiveMap", receiveMap);
        model.addAttribute("receiveBillAuditItems", receiveBillAuditItemModels);
        model.addAttribute("verifyItems", verifyItemModels);
        model.addAttribute("pageSource", pageSource);
        model.addAttribute("alreadyPrice", alreadyPrice);
        return "derp/bill/toc-receive-bill-details";
    }

    /**
     * 获取toc应收明细分页数据
     * @param dto
     */
    @RequestMapping("/listReceiveItem.asyn")
    @ResponseBody
    private ViewResponseBean listReceiveItem(TocSettlementReceiveBillItemDTO dto) {
        try {
            // 响应结果集
            toCReceiveBillService.listReceiveItemByPage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(dto);
    }

    /**
     * 获取toc应收明细分页数据
     * @param dto
     */
    @RequestMapping("/listReceiveCostItem.asyn")
    @ResponseBody
    private ViewResponseBean listReceiveCostItem(TocSettlementReceiveBillCostItemDTO dto) {
        try {
            // 响应结果集
            toCReceiveBillService.listReceiveCostItemByPage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(dto);
    }

    /**
     * 获取店铺
     * @param merchantShopRelMongo
     */
    @RequestMapping("/getShopInfo.asyn")
    @ResponseBody
    private ViewResponseBean getShopInfo(MerchantShopRelMongo merchantShopRelMongo) {
        List<MerchantShopRelMongo> mongoList = new ArrayList<>();
        List<Map<String, Object>> resultMap = new ArrayList<>();
        try {
            Map<String, Object> params = new HashMap<>();

            params.put("shopTypeCode", merchantShopRelMongo.getShopTypeCode());

            params.put("status", DERP_SYS.MERCHANTSHOPREL_STATUS_1);
            if (StringUtils.isNotBlank(merchantShopRelMongo.getStorePlatformCode())) {
                params.put("storePlatformCode", merchantShopRelMongo.getStorePlatformCode());
            }
            if (StringUtils.isNotBlank(merchantShopRelMongo.getShopCode())) {
                params.put("shopCode", merchantShopRelMongo.getShopCode());
            }
            mongoList = merchantShopRelMongoDao.findAll(params);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(mongoList);
    }

    /**
     * 导出 失败原因
     * */
    @RequestMapping("/downLoad.asyn")
    @ResponseBody
    private ViewResponseBean downLoad(Long id, HttpServletResponse response)throws Exception{
        try{
            //查询该任务
            if(id==null){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            TocSettlementReceiveBillDTO tocSettlementReceiveBillDTO = toCReceiveBillService.searchDTOById(id);
            if(StringUtils.isEmpty(tocSettlementReceiveBillDTO.getErrorMsgPath())){
                return null;
            }
            File file = new File(tocSettlementReceiveBillDTO.getErrorMsgPath());

            //转换中文否则可能会产生乱码
            String downloadFilename = URLEncoder.encode(tocSettlementReceiveBillDTO.getErrorMsgPath().substring(tocSettlementReceiveBillDTO.getErrorMsgPath().lastIndexOf("/")+1), "UTF-8");
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

        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success();
    }

    /**
     * 删除
     *
     * @param ids
     */
    @RequestMapping("/delToCReceiveBill.asyn")
    @ResponseBody
    public ViewResponseBean delToCReceiveBill(String ids) {
        try {
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = toCReceiveBillService.delToCReceiveBill(list);
            if (!b) {
                return ResponseFactory.error(StateCodeEnum.ERROR_301);
            }
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success();
    }

    /**
     * 访问增加补扣款页面
     * @throws SQLException
     */
    @RequestMapping("/toFreeADDPage.html")
    public String toFreeADDPage(Long id, Model model) throws SQLException {
        TocSettlementReceiveBillDTO tocSettlementReceiveBillDTO = toCReceiveBillService.searchDTOById(id);
        //费用明细
        List<TocSettlementReceiveBillCostItemModel> receiveBillCostItemDTOS = toCReceiveBillCostItemService.listCostByHandAdd(id);
        List<SelectBean> brandSelectBeans = toCReceiveBillService.getBrandSelectBeans();
        model.addAttribute("bill", tocSettlementReceiveBillDTO);
        model.addAttribute("orderItems", receiveBillCostItemDTOS);
        model.addAttribute("brandSelectBeans", brandSelectBeans);
        return "derp/bill/toc-receive-cost-bill-add";
    }

    /**
     * 保存补扣款信息
     */
    @RequestMapping("/saveToCReceiveBillCost.asyn")
    @ResponseBody
    public ViewResponseBean saveToCReceiveBillCost(String json) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            // 实例化JSON对象
            JSONObject jsonData = JSONObject.fromObject(json);
            Map classMap = new HashMap();
            classMap.put("costItemList", Map.class);
            TocSettlementReceiveBillDTO dto = (TocSettlementReceiveBillDTO) JSONObject.toBean(jsonData, TocSettlementReceiveBillDTO.class, classMap);
            retMap = toCReceiveBillCostItemService.saveReceiveBillCost(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(retMap);
    }

    /**
     * 提交
     * @param
     */
    @RequestMapping("/submitReceiveBill.asyn")
    @ResponseBody
    private ViewResponseBean submitReceiveBill(Long id) {
        Map<String, String> result = new HashMap<>();
        try {
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            // 响应结果集
            User user = ShiroUtils.getUser();
            result = toCReceiveBillService.confirmReceiveBill(user,id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(result);
    }

    /**
     * 访问审核页面
     *
     * @throws SQLException
     */
    @RequestMapping("/toAuditPage.html")
    public String toAuditPage(Long id, Model model) throws SQLException {
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
        model.addAttribute("month", month);
        model.addAttribute("bill", tocSettlementReceiveBillDTO);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalRmbPrice", totalRmbPrice);
        model.addAttribute("totalPriceLabel", NumberToCN.number2CNMontrayUnit(totalPrice));
        model.addAttribute("deductionMap", deductionMap);
        model.addAttribute("receiveMap", receiveMap);
        model.addAttribute("receiveBillAuditItems", receiveBillAuditItemModels);
        model.addAttribute("verifyItems", verifyItemModels);
        model.addAttribute("alreadyPrice", alreadyPrice);
        return "derp/bill/toc-receive-bill-audit";
    }

    /**
     * 审核
     */
    @RequestMapping("/auditItem.asyn")
    @ResponseBody
    public ViewResponseBean auditItem(TocSettlementReceiveBillAuditItemModel model, String settlementType, String billInDate) {
        Map<String, String> result = new HashMap<>();
        try {
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils().addObject(model.getBillId())
                    .addObject(model.getAuditResult())
                    .addObject(model.getAuditRemark()).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            User user=ShiroUtils.getUser();
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
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(result);
    }

    /**
     * 获取提交NC信息
     * @param ids
     */
    @RequestMapping("/listReceiveBillsToNC.asyn")
    @ResponseBody
    private ViewResponseBean listReceiveBillsToNC(String ids) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(ids).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            // 响应结果集
            List<ToCReceiveBillToNCDTO> receiveBillToNCDTOs = toCReceiveBillService.listReceiveBillsToNCById(ids);
            return ResponseFactory.success(receiveBillToNCDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
    }

    /**
     * 同步NC
     * @param ids
     */
    @RequestMapping("/synNC.asyn")
    @ResponseBody
    private ViewResponseBean synNC(String ids) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(ids).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            User user=ShiroUtils.getUser();
            // 响应结果集
            Map<String, String> result = toCReceiveBillService.synNC(user,ids);
            return ResponseFactory.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
    }

    /**
     * 导出
     * */
    @RequestMapping("/exportBill.asyn")
    private void exportBill(HttpServletResponse response, HttpServletRequest request, Long id) throws Exception{
        try {
            User user = ShiroUtils.getUser();
            TocSettlementReceiveBillDTO billDTO = new TocSettlementReceiveBillDTO();
            billDTO.setId(id);
            //表头信息
            List<Map<String,Object>> billExportList = toCReceiveBillService.listForExport(billDTO, user);

            //表头信息
            String mainSheetName = "平台结算单列表";
            String[] mainColumns = {"公司","平台结算单号","事业部","运营类型","客户名称","平台名称","店铺名称",
                    "应收金额 (RMB)","账单月份","结算日期","账单状态","创建人","创建时间","NC单号","NC状态","凭证名称","凭证状态"};
            String[] mainKeys = {"merchantName","billCode","buName", "shopTypeName","customerName","storePlatformName","shopName",
                    "receivablePrice","month","settlementDate","billStatus", "creater","createTime", "ncCode", "ncStatus","voucherName","voucherStatus"};

            //表体信息
            List<Map<String,Object>> itemList = toCReceiveBillService.listForExportItem(id, user);

            String itemSheetName = "应收详情";
            String[] itemColumns = {"平台结算单","外部订单号","商品货号","商品名称","销售数量","结算费项", "母品牌", "平台结算单","结算金额（结算原币）","结算汇率","结算金额（RMB）"};
            String[] itemKeys = {"billCode","externalCode","goodsNo","goodsName","num","projectName", "brandName", "oriCurrency","originalAmount",
                    "settlementRate", "rmbAmount"};
            //生成表格
            ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, mainColumns, mainKeys, billExportList);
            ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, itemList);

            List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
            sheets.add(mainSheet) ;
            sheets.add(itemSheet) ;

            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
            //导出文件
            String fileName = "平台结算单导出";
            FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交作废申请
     */
    @RequestMapping("/saveInvalidBill.asyn")
    @ResponseBody
    private ViewResponseBean saveInvalidBill(String id, String invalidRemark) {
        Map<String, String> result = new HashMap<>();
        try {
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils()
                    .addObject(id).addObject(invalidRemark).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            User user=ShiroUtils.getUser();
            // 响应结果集
            result = toCReceiveBillService.saveInvalidBill(user,id, invalidRemark);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(result);
    }

    /**
     * 校验toc结算单是否满足开票条件
     * @param ids
     */
    @RequestMapping("/validateInfo.asyn")
    @ResponseBody
    public ViewResponseBean validateInfo(String ids) {
        try {
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            Map<String, String> resultMap = toCReceiveBillService.validate(list);
            return ResponseFactory.success(resultMap);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
    }

    /**
     * 开票
     */
    @RequestMapping("/toInvoicePage.html")
    public String toInvoicePage(String ids, String tempId, Model model) throws Exception {
        FileTempDTO fileTempModel = fileTempService.searchById(Long.valueOf(tempId));
        Map<String, Object> resultMap = toCReceiveBillService.getInvoiceInfo(ids);
        MerchantInfoMongo merchantInfo = (MerchantInfoMongo) resultMap.get("merchantInfo");
        String currency = (String) resultMap.get("currency");
        String billDate = (String) resultMap.get("billDate");
        BigDecimal totalAmount = (BigDecimal) resultMap.get("totalAllAmount");
        List<Map<String, Object>> items = (List<Map<String, Object>>) resultMap.get("items");

        model.addAttribute("merchantInfo", merchantInfo);
        model.addAttribute("ids", ids);
        model.addAttribute("tempId", tempId);
        model.addAttribute("fileTempCode", fileTempModel.getCode());
        model.addAttribute("currency", currency);
        model.addAttribute("billDate", billDate);
        model.addAttribute("totalAllAmount", totalAmount);
        model.addAttribute("itemLists", items);
        return fileTempModel.getToUrl();
    }

    /**
     * 提交保存发票
     * @throws SQLException
     */
    @RequestMapping("/saveSubmitInvoice.asyn")
    @ResponseBody
    public ViewResponseBean saveSubmitInvoice(@RequestBody Map<String, Object> map) {
        try {
            if (map == null) {
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }

            User user = ShiroUtils.getUser();
            // 响应结果集
            toCReceiveBillService.modifyJSONContract(map, user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success();
    }

    /**
     * 导出PDF
     * @param ids
     */
    @RequestMapping("/exportPDF.asyn")
    @ResponseBody
    public ViewResponseBean exportPDF(String ids, HttpServletResponse response) {
        try {
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
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
        } catch (NullPointerException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
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

    @RequestMapping("/getVerifyItems.asyn")
    @ResponseBody
    public ViewResponseBean getVerifyItems(Long id) {
        try {
            BigDecimal totalRmbPrice = toCReceiveBillService.statisticsRmbAmount(id);

            //核算记录
            List<TocSettlementReceiveBillVerifyItemModel> verifyItemModels = toCReceiveBillService.listVerifyByBillId(id);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("totalRmbAmount", totalRmbPrice);
            resultMap.put("verifyItemModels", verifyItemModels);
            return ResponseFactory.success(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
    }

    /**
     * 保存核销信息
     */
    @RequestMapping("/saveReceiveBillVerify.asyn")
    @ResponseBody
    public ViewResponseBean saveReceiveBillVerify(TocSettlementReceiveBillVerifyItemDTO dto) {
        Map<String, String> retMap = new HashMap<String, String>();
        try {
            User user = ShiroUtils.getUser();
            retMap = toCReceiveBillService.saveVerifyItem(dto, user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(retMap);
    }


    /**
     * 平台结算单汇总导出
     * */
    @RequestMapping("/exportSummaryBill.asyn")
    private void exportSummaryBill(HttpServletResponse response, HttpServletRequest request, TocSettlementReceiveBillDTO dto) throws Exception{
        try {
            User user = ShiroUtils.getUser();
            //表头信息
            List<Map<String,Object>> billExportList = toCReceiveBillService.listForExport(dto, user);

            //表头信息
            String mainSheetName = "平台结算单列表";
            String[] mainColumns = {"公司","平台结算单号","事业部","运营类型","客户名称","平台名称","店铺名称",
                    "应收金额(RMB)","账单月份","结算日期","账单状态","创建人","创建时间","NC单号","NC状态","凭证名称","凭证状态"};
            String[] mainKeys = {"merchantName","billCode","buName", "shopTypeName","customerName","storePlatformName","shopName",
                    "receivablePrice","month","settlementDate","billStatus", "creater","createTime", "ncCode", "ncStatus","voucherName","voucherStatus"};

            //表体信息
            List<Map<String,Object>> itemList = toCReceiveBillService.listForSummaryExport(dto, user);

            String itemSheetName = "应收详情";
            String[] itemColumns = {"平台结算单","结算费项", "平台费项名称", "母品牌", "补扣款类型","平台结算原币", "结算金额（结算原币）","结算金额（RMB）"};
            String[] itemKeys = {"billCode", "projectName", "PlatformSettlementName", "parentBrandName", "billType", "oriCurrency", "receivePrice", "receiveRmbPrice"};
            //生成表格
            ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, mainColumns, mainKeys, billExportList);
            ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, itemColumns, itemKeys, itemList);

            List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
            sheets.add(mainSheet) ;
            sheets.add(itemSheet) ;

            SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
            //导出文件
            String fileName = "平台结算单汇总导出";
            FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
