package com.topideal.order.web.bill;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DerpBasic;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.dto.common.FileTempDTO;
import com.topideal.entity.vo.bill.ReceiveBillOperateModel;
import com.topideal.entity.vo.bill.ReceiveBillVerifyItemModel;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.mongo.dao.BrandSuperiorMongoDao;
import com.topideal.mongo.entity.BrandSuperiorMongo;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.order.service.bill.*;
import com.topideal.order.service.filetemp.FileTempService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.tools.DownloadExcelUtil;
import com.topideal.order.webapi.bill.form.ReceiveBillAuditForm;
import com.topideal.order.webapi.bill.form.ReceiveBillSubmitForm;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 应收账单
 * @Author: Chen Yiluan
 * @Date: 2020/07/27 14:53
 **/
@Controller
@RequestMapping("/receiveBill")
public class ReceiveBillController {

    private static final Logger LOGGER= LoggerFactory.getLogger(ReceiveBillController.class);

    @Autowired
    private ReceiveBillService receiveBillService;
    @Autowired
    private ReceiveBillCostItemService receiveBillCostItemService;
    @Autowired
    private ReceiveBillVerifyItemService receiveBillVerifyItemService;
    @Autowired
    private ReceiveBillOperateService receiveBillOperateService;
    @Autowired
    private FileTempService fileTempService;
    @Autowired
    private BrandSuperiorMongoDao brandSuperiorMongoDao;
    @Autowired
    private AdvanceBillService advanceBillService;

    /**
     * 访问列表页面
     *
     * @param model
     */
    @RequestMapping("/toPage.html")
    public String toPage(Model model) throws Exception {
        return "derp/bill/receive-bill-list";
    }

    /**
     * 获取分页数据
     *
     * @param dto
     */
    @RequestMapping("/listReceiveBill.asyn")
    @ResponseBody
    private ViewResponseBean listReceiveBill(ReceiveBillDTO dto) {
        try {
            User user = ShiroUtils.getUser();
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            receiveBillService.listReceiveBillByPage(dto, user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(dto);
    }

    /**
     * 获取分页数据
     * @param dto
     */
    @RequestMapping("/listAddOrder.asyn")
    @ResponseBody
    private ViewResponseBean listAddOrder(ReceiveBillDTO dto) {
        try {
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils()
                    .addObject(dto.getOrderType())
                    .empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            User user = ShiroUtils.getUser();
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            receiveBillService.listAddOrderByPage(dto, user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(dto);
    }

    /**
     * 判断单据是否已生成应收账单
     * @param json
     */
    @RequestMapping("/isExistReceiveBill.asyn")
    @ResponseBody
    private ViewResponseBean isExistReceiveBill(String json) {
        Map<String, String> retMap = new HashMap<String, String>();
        try {
            // 实例化JSON对象
            JSONObject jsonData = JSONObject.fromObject(json);
            Map classMap = new HashMap();
            classMap.put("orderList", Map.class);
            ReceiveBillDTO dto = (ReceiveBillDTO) JSONObject.toBean(jsonData, ReceiveBillDTO.class, classMap);
            retMap = receiveBillService.isExistOrder(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(retMap);
    }

    /**
     * 保存应收账单并跳转到编辑页面
     * @param json
     */
    @RequestMapping("/saveReceiveBill.asyn")
    @ResponseBody
    private ViewResponseBean saveReceiveBill(String json) {
        Map<String, String> result = new HashMap<>();
        try {
            User user = ShiroUtils.getUser();
            // 实例化JSON对象
            JSONObject jsonData = JSONObject.fromObject(json);
            Map classMap = new HashMap();
            classMap.put("orderList", Map.class);
            ReceiveBillDTO dto = (ReceiveBillDTO) JSONObject.toBean(jsonData, ReceiveBillDTO.class, classMap);
            // 响应结果集
            result = receiveBillService.saveReceiveBill(dto, user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(result);
    }

    /**
     * 访问详情页面
     *
     * @throws SQLException
     */
    @RequestMapping("/toDetailsPage.html")
    public String toDetailsPage(Long id, String pageSource, Model model) throws SQLException {

        String month = null;
        ReceiveBillDTO receiveBillModel = receiveBillService.searchDTOById(id);
        if (receiveBillModel.getBillDate() != null) {
            month = TimeUtils.formatMonth(receiveBillModel.getBillDate()).replace("-", "年");
        }
        BigDecimal totalPrice = new BigDecimal("0");
        BigDecimal totalNum = new BigDecimal("0");
        BigDecimal itemTotalPrice = new BigDecimal("0");
        BigDecimal itemTotalNum = new BigDecimal("0");
        BigDecimal costTotalPrice = new BigDecimal("0");
        BigDecimal costTotalNum = new BigDecimal("0");
        BigDecimal totalTaxAmount = new BigDecimal("0");
        BigDecimal totalTax = new BigDecimal("0");
        //应收结算
        List<Map<String, Object>> receiveMap = receiveBillService.receiveTotalById(id);
        for (Map<String, Object> map : receiveMap) {
            BigDecimal price = (BigDecimal) map.get("totalPrice");
            BigDecimal taxAmount = (BigDecimal) map.get("totalTaxAmount") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalTaxAmount");;
            BigDecimal tax = (BigDecimal) map.get("totalTax")== null ? BigDecimal.ZERO : (BigDecimal) map.get("totalTax");
            BigDecimal num = map.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalNum");
            if (price != null) {
                totalPrice = totalPrice.add(price);
                totalTaxAmount = totalTaxAmount.add(taxAmount);
                totalTax = totalTax.add(tax);
                itemTotalPrice = itemTotalPrice.add(price);
            }
            totalNum = totalNum.add(num);
            itemTotalNum = itemTotalNum.add(num);
        }
        //抵扣费用
        List<ReceiveBillCostItemDTO> deductionMap = receiveBillService.deductionTotalById(id);
        for (ReceiveBillCostItemDTO itemModel : deductionMap) {
            BigDecimal price = itemModel.getPrice();
            if (price != null) {
                totalPrice = totalPrice.add(price);
                costTotalPrice = costTotalPrice.add(price);
            }

            if (itemModel.getTax() != null) {
                totalTax = totalTax.add(itemModel.getTax());
            }

            if (itemModel.getTaxAmount() != null) {
                totalTaxAmount = totalTaxAmount.add(itemModel.getTaxAmount());
            }

            totalNum = totalNum.add(new BigDecimal(itemModel.getNum()));
            costTotalNum = costTotalNum.add(new BigDecimal(itemModel.getNum()));
        }

        //审核记录
        List<ReceiveBillOperateModel> receiveBillOperateModels = receiveBillOperateService.listByBillId(id);
        //核算记录
        List<ReceiveBillVerifyItemModel> verifyItemModels = receiveBillVerifyItemService.listByBillId(id);
        BigDecimal alreadyPrice = new BigDecimal("0");
        for (ReceiveBillVerifyItemModel itemModel : verifyItemModels) {
            BigDecimal price = itemModel.getPrice();
            alreadyPrice = alreadyPrice.add(price);
        }

        List<ReceiveBillItemDTO> itemDTOS = receiveBillService.listReceiveItem(receiveBillModel);
        List<ReceiveBillCostItemDTO> costItemDTOS = receiveBillService.listReceiveCostItem(receiveBillModel);

        model.addAttribute("month", month);
        model.addAttribute("bill", receiveBillModel);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalTaxAmount", totalTaxAmount);
        model.addAttribute("totalTax", totalTax);
        model.addAttribute("totalPriceLabel", NumberToCN.number2CNMontrayUnit(totalPrice));
        model.addAttribute("deductionMap", deductionMap);
        model.addAttribute("receiveMap", receiveMap);
        model.addAttribute("itemDTOS", itemDTOS);
        model.addAttribute("costItemDTOS", costItemDTOS);
        model.addAttribute("itemTotalPrice", itemTotalPrice);
        model.addAttribute("itemTotalNum", itemTotalNum);
        model.addAttribute("costTotalPrice", costTotalPrice);
        model.addAttribute("costTotalNum", costTotalNum);
        model.addAttribute("receiveBillOperateModels", receiveBillOperateModels);
        model.addAttribute("verifyItems", verifyItemModels);
        model.addAttribute("pageSource", pageSource);
        model.addAttribute("alreadyPrice", alreadyPrice);
        return "derp/bill/receive-bill-details";
    }

    /**
     * 提交
     *
     * @param
     */
    @RequestMapping("/submitReceiveBill.asyn")
    @ResponseBody
    private ViewResponseBean submitReceiveBill(String json) {
        Map<String, String> result = new HashMap<>();
        try {
            User user = ShiroUtils.getUser();
            JSONObject jsonData=JSONObject.fromObject(json);
            Map classMap = new HashMap();
            classMap.put("itemList",ReceiveBillItemDTO.class);
            ReceiveBillSubmitForm form = (ReceiveBillSubmitForm) JSONObject.toBean(jsonData, ReceiveBillSubmitForm.class,classMap);

            // 响应结果集
            result = receiveBillService.confirmReceiveBill(form, user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(result);
    }

    /**
     * 编辑页面保存
     * @param
     */
    @RequestMapping("/saveReceiveBillFromEdit.asyn")
    @ResponseBody
    private ViewResponseBean saveReceiveBillFromEdit(String json) {
        Map<String, String> result = new HashMap<>();
        try {
            User user = ShiroUtils.getUser();
            JSONObject jsonData=JSONObject.fromObject(json);
            Map classMap = new HashMap();
            classMap.put("itemList", ReceiveBillItemDTO.class);
            ReceiveBillSubmitForm form = (ReceiveBillSubmitForm) JSONObject.toBean(jsonData, ReceiveBillSubmitForm.class,classMap);

            // 响应结果集
            result = receiveBillService.saveReceiveBillFromEdit(form, user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(result);
    }

    /**
     * 访问详情页面
     *
     * @throws SQLException
     */
    @RequestMapping("/toFreeADDPage.html")
    public String toFreeADDPage(Long id, Model model) throws SQLException {
        ReceiveBillDTO receiveBillModel = receiveBillService.searchDTOById(id);
        //费用明细
        List<ReceiveBillCostItemDTO> receiveBillCostItemDTOS = receiveBillCostItemService.itemListGroupByBillId(id, true);
        List<SelectBean> brandSelectBeans = new ArrayList<>();
        List<BrandSuperiorMongo> brandList = brandSuperiorMongoDao.findAll(new HashMap<>());
        if (brandList != null && brandList.size() > 0) {
            for (BrandSuperiorMongo brandSuperiorMongo : brandList) {
                SelectBean selectBean = new SelectBean();
                selectBean.setValue(String.valueOf(brandSuperiorMongo.getBrandSuperiorId()));
                selectBean.setLabel(brandSuperiorMongo.getName());
                brandSelectBeans.add(selectBean);
            }
        }
        ArrayList<DerpBasic> storePlatformList = DERP.storePlatformList;
        List<SelectBean> storePlatformSeleteBeans = new ArrayList<>();
        for (DerpBasic basic : storePlatformList) {
            SelectBean selectBean = new SelectBean();
            selectBean.setValue(String.valueOf(basic.getKey()));
            selectBean.setLabel(basic.getValue());
            storePlatformSeleteBeans.add(selectBean);
        }
        model.addAttribute("storePlatformList", storePlatformSeleteBeans);
        model.addAttribute("bill", receiveBillModel);
        model.addAttribute("orderItems", receiveBillCostItemDTOS);
        model.addAttribute("brandSelectBeans", brandSelectBeans);
        return "derp/bill/receive-cost-bill-add";
    }

    /**
     * 保存补扣款信息
     */
    @RequestMapping("/saveReceiveBillCost.asyn")
    @ResponseBody
    public ViewResponseBean saveReceiveBillCost(String json) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            // 实例化JSON对象
            JSONObject jsonData = JSONObject.fromObject(json);
            Map classMap = new HashMap();
            classMap.put("costItemList", Map.class);
            ReceiveBillDTO dto = (ReceiveBillDTO) JSONObject.toBean(jsonData, ReceiveBillDTO.class, classMap);
            retMap = receiveBillCostItemService.saveReceiveBillCost(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(retMap);
    }

    @RequestMapping("/getVerifyItems.asyn")
    @ResponseBody
    public ViewResponseBean getVerifyItems(Long id) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            //核算记录
            List<ReceiveBillVerifyItemModel> verifyItemModels = receiveBillVerifyItemService.listByBillId(id);
            return ResponseFactory.success(verifyItemModels);
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
    public ViewResponseBean saveReceiveBillVerify(ReceiveBillVerifyItemDTO dto) {
        Map<String, String> retMap = new HashMap<String, String>();
        try {
            User user = ShiroUtils.getUser();
            retMap = receiveBillVerifyItemService.saveVerifyItem(dto, user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(retMap);
    }

    /**
     * 审核
     */
    @RequestMapping("/auditItem.asyn")
    @ResponseBody
    public ViewResponseBean auditItem(String json) {
        Map<String, String> result = new HashMap<>();
        try {
            User user = ShiroUtils.getUser();
            JSONObject jsonData=JSONObject.fromObject(json);
            Map classMap = new HashMap();
            classMap.put("itemDTOS",ReceiveBillItemDTO.class);
            classMap.put("costItemDTOS",ReceiveBillCostItemDTO.class);
            ReceiveBillAuditForm form = (ReceiveBillAuditForm) JSONObject.toBean(jsonData, ReceiveBillAuditForm.class,classMap);

            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils().addObject(form.getBillId())
                    .addObject(form.getAuditResult())
                    .addObject(form.getAuditRemark()).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            // 响应结果集
            result = receiveBillService.auditReceiveBill(form, user);
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
        ReceiveBillDTO receiveBillModel = receiveBillService.searchDTOById(id);
        if (receiveBillModel.getBillDate() != null) {
            month = TimeUtils.formatMonth(receiveBillModel.getBillDate()).replace("-", "年");
        }
        BigDecimal totalPrice = new BigDecimal("0");
        BigDecimal totalNum = new BigDecimal("0");
        //明细汇总
        BigDecimal itemTotalPrice = new BigDecimal("0");
        BigDecimal itemTotalNum = new BigDecimal("0");
        //费用明细汇总
        BigDecimal costTotalPrice = new BigDecimal("0");
        BigDecimal costTotalNum = new BigDecimal("0");

        BigDecimal totalTaxAmount = new BigDecimal("0");
        BigDecimal totalTax = new BigDecimal("0");
        //应收结算
        List<Map<String, Object>> receiveMap = receiveBillService.receiveTotalById(id);
        for (Map<String, Object> map : receiveMap) {
            BigDecimal price = (BigDecimal) map.get("totalPrice");
            BigDecimal taxAmount = (BigDecimal) map.get("totalTaxAmount") == null ? BigDecimal.ZERO :(BigDecimal) map.get("totalTaxAmount") ;
            BigDecimal tax = (BigDecimal) map.get("totalTax") == null ? BigDecimal.ZERO :(BigDecimal) map.get("totalTax");
            BigDecimal num = map.get("totalNum") == null ? BigDecimal.ZERO : (BigDecimal) map.get("totalNum");
            if (price != null) {
                totalPrice = totalPrice.add(price);
                totalTaxAmount = totalTaxAmount.add(taxAmount);
                totalTax = totalTax.add(tax);
                itemTotalPrice = itemTotalPrice.add(price);
            }
            totalNum = totalNum.add(num);
            itemTotalNum = itemTotalNum.add(num);
        }
        //抵扣费用
        List<ReceiveBillCostItemDTO> deductionMap = receiveBillService.deductionTotalById(id);
        for (ReceiveBillCostItemDTO itemModel : deductionMap) {
            BigDecimal price = itemModel.getPrice();
            Integer num = itemModel.getNum();
            if (price != null) {
                totalPrice = totalPrice.add(price);
                costTotalPrice = costTotalPrice.add(price);
            }

            if (itemModel.getTax() != null) {
                totalTax = totalTax.add(itemModel.getTax());
            }

            if (itemModel.getTaxAmount() != null) {
                totalTaxAmount = totalTaxAmount.add(itemModel.getTaxAmount());
            }
            totalNum = totalNum.add(new BigDecimal(num));
            costTotalNum = costTotalNum.add(new BigDecimal(num));
        }

        //审核记录
        List<ReceiveBillOperateModel> receiveBillOperateModels = receiveBillOperateService.listByBillId(id);

        //核算记录
        List<ReceiveBillVerifyItemModel> verifyItemModels = receiveBillVerifyItemService.listByBillId(id);
        BigDecimal alreadyPrice = new BigDecimal("0");
        for (ReceiveBillVerifyItemModel itemModel : verifyItemModels) {
            BigDecimal price = itemModel.getPrice();
            alreadyPrice = alreadyPrice.add(price);
        }

        List<ReceiveBillItemDTO> itemDTOS = receiveBillService.listReceiveItem(receiveBillModel);
        List<ReceiveBillCostItemDTO> costItemDTOS = receiveBillService.listReceiveCostItem(receiveBillModel);

        model.addAttribute("month", month);
        model.addAttribute("bill", receiveBillModel);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalTaxAmount", totalTaxAmount);
        model.addAttribute("totalTax", totalTax);
        model.addAttribute("totalPriceLabel", NumberToCN.number2CNMontrayUnit(totalPrice));
        model.addAttribute("deductionMap", deductionMap);
        model.addAttribute("receiveMap", receiveMap);
        model.addAttribute("itemDTOS", itemDTOS);
        model.addAttribute("costItemDTOS", costItemDTOS);
        model.addAttribute("itemTotalPrice", itemTotalPrice);
        model.addAttribute("itemTotalNum", itemTotalNum);
        model.addAttribute("costTotalPrice", costTotalPrice);
        model.addAttribute("costTotalNum", costTotalNum);
        model.addAttribute("receiveBillOperateModels", receiveBillOperateModels);
        model.addAttribute("verifyItems", verifyItemModels);
        model.addAttribute("alreadyPrice", alreadyPrice);
        return "derp/bill/receive-bill-audit";
    }

    /**
     * 刷新
     */
    @RequestMapping("/refreshReceiveBill.asyn")
    @ResponseBody
    public ViewResponseBean refreshReceiveBill(String id) {
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
            result = receiveBillService.refreshReceiveBill(Long.valueOf(id), user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(result);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @RequestMapping("/delReceiveBill.asyn")
    @ResponseBody
    public ViewResponseBean delReceiveBill(String ids) {
        try {
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = receiveBillService.delReceiveBill(list);
            if (!b) {
                return ResponseFactory.error(StateCodeEnum.ERROR_301);
            }
        } catch (SQLException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
        } catch (NullPointerException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success();
    }

    /**
     * 校验应收单是否满足开票条件
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
            Map<String, String> resultMap = receiveBillService.validate(list);
            return ResponseFactory.success(resultMap);
        } catch (SQLException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
        } catch (NullPointerException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
    }


    /**
     * 开票
     */
    @RequestMapping("/toInvoicePage.html")
    public String toInvoicePage(String ids, String tempId, String pageSource, Model model) throws Exception {
        FileTempDTO fileTempModel = fileTempService.searchById(Long.valueOf(tempId));
        User user = ShiroUtils.getUser();
        List list = StrUtils.parseIds(ids);
        List<ReceiveBillDTO> receiveBillDTOS = receiveBillService.listByIds(list);
        List<String> codes = new ArrayList<>();
        List<String> relCodes = new ArrayList<>();
        List<String> poNos = new ArrayList<>();
        for (ReceiveBillDTO dto : receiveBillDTOS) {
            codes.add(dto.getCode());
            relCodes.add(dto.getRelCode());
            poNos.add(dto.getPoNo());
        }
        Timestamp invoiceDate = receiveBillOperateService.getMaxAuditDate(list);
        CustomerInfoMogo customerInfo = receiveBillService.getCustomer(receiveBillDTOS.get(0).getCustomerId());
        MerchantInfoMongo merchantInfo = receiveBillService.getMerchantInfo(user.getMerchantId());
        if (fileTempModel !=null && StringUtils.isNotBlank(fileTempModel.getToUrl())
                && fileTempModel.getToUrl().contains("receive-bill-weiping-invoice-details")) {
            List<Map<String, Object>> items = receiveBillService.listWPItemInfos(list, user.getMerchantId());
            model.addAttribute("itemLists", items);
        } else if (fileTempModel !=null && StringUtils.isNotBlank(fileTempModel.getToUrl())
                && fileTempModel.getToUrl().contains("jingdong-invoice-details")) {
            List<Map<String, Object>> items = receiveBillService.listJDItemInfos(list, user.getMerchantId(), user);
            model.addAttribute("itemLists", items);
        } else if (fileTempModel !=null && StringUtils.isNotBlank(fileTempModel.getToUrl())
                && fileTempModel.getToUrl().contains("kaola-invoice-details")) {
            List<Map<String, Object>> items = receiveBillService.listKLItemInfos(list, user.getMerchantId());
            model.addAttribute("itemLists", items);
            model.addAttribute("invoiceDate", TimeUtils.getNow());
        } else if (fileTempModel !=null && StringUtils.isNotBlank(fileTempModel.getToUrl())
                && fileTempModel.getToUrl().contains("yizi-invoice-details")) {
            List<Map<String, Object>> items = receiveBillService.listYZItemInfos(list, user.getMerchantId(), user);
            model.addAttribute("itemLists", items);
        } else {
            List<Map<String, Object>> items = receiveBillService.listItemInfos(list, user.getMerchantId(), user);
            model.addAttribute("itemLists", items);
            if (items != null && items.size() > 1) {
                Map<String, Object> map = items.get(items.size() - 1);
                model.addAttribute("totalAllAmount", map.get("totalAllAmount"));
                model.addAttribute("totalAllNum", map.get("totalAllNum"));
            }
        }
        model.addAttribute("currency", receiveBillDTOS.get(0).getCurrency());
        model.addAttribute("currencyLabel", receiveBillDTOS.get(0).getCurrencyLabel());
        model.addAttribute("codes", StringUtils.join(codes.toArray(), ","));
        model.addAttribute("poNos", StringUtils.join(poNos.toArray(), "&"));
        model.addAttribute("merchantInfo", merchantInfo);
        model.addAttribute("customerInfo", customerInfo);
        model.addAttribute("customerId", receiveBillDTOS.get(0).getCustomerId());
        model.addAttribute("pageSource", pageSource);
        model.addAttribute("ids", ids);
        model.addAttribute("tempId", tempId);
        model.addAttribute("fileTempCode", fileTempModel.getCode());
        model.addAttribute("invoiceDate", invoiceDate);
        model.addAttribute("invoiceSource", "1");
        return fileTempModel.getToUrl();
    }

    /**
     * 提交发票
     *
     * @throws SQLException
     */
    @RequestMapping("/modifyJsonContract.asyn")
    @ResponseBody
    public ViewResponseBean modifyJsonContract(@RequestBody Map<String, Object> map) {
        try {
            if (map == null) {
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }
            // 响应结果集
            receiveBillService.modifyJSONContract(map, (String) map.get("invoiceSource"));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success();
    }

    @RequestMapping("/exportTempDateFile.asyn")
    public void exportTempDateFile(HttpServletResponse response, HttpServletRequest request,
                                   String id) {
        BufferedInputStream bis = null;
        OutputStream out = null;
        FileInputStream inputStream = null;
        try {
            ReceiveBillDTO receiveBillDTO = receiveBillService.searchDTOById(Long.valueOf(id));
            if (StringUtils.isBlank(receiveBillDTO.getInvoicePath())) {
                return;
            }
            File file = new File(receiveBillDTO.getInvoicePath());
            //转换中文否则可能会产生乱码
            String downloadFilename = URLEncoder.encode(receiveBillDTO.getInvoicePath().substring(receiveBillDTO.getInvoicePath().lastIndexOf("/")), "UTF-8");
            FileExportUtil.setHeader(response, request, downloadFilename);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + downloadFilename);
            bis = new BufferedInputStream(new FileInputStream(file));
            byte[] b = new byte[bis.available() + 1000];
            int i = 0;
            out = response.getOutputStream();   //直接下载导出
            while ((i = bis.read(b)) != -1) {
                out.write(b, 0, i);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ResponseBody
    @RequestMapping("/preview.asyn")
    public String preview(HttpServletResponse response, String id) throws Exception {
        InputStream input = null;
        OutputStream out = null;
        String downloadFilename = null;
        try {
            ReceiveBillDTO receiveBillDTO = receiveBillService.searchDTOById(Long.valueOf(id));
            if (StringUtils.isBlank(receiveBillDTO.getInvoicePath())) {
                return null;
            }
            String filePath = receiveBillDTO.getInvoicePath();
            File file = new File(filePath);

            input = new FileInputStream(file);
            downloadFilename = URLEncoder.encode(receiveBillDTO.getInvoicePath().substring(receiveBillDTO.getInvoicePath().lastIndexOf("/")+1), "UTF-8");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + downloadFilename);
            out = response.getOutputStream();
            byte[] b = new byte[input.available()];
            if (out != null) {
                if (input != null) {
                    while ((input.read(b)) != -1) {
                        out.write(b);
                    }
                } else {
                    System.out.println("InputStream为空。。。");
                }
            } else {
                System.out.println("OutputStream为空。。。");
            }
            out.flush();
            input.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return downloadFilename;
    }

    /**
     * 查询销售订单关联的po单号
     * @param orderCode 销售单号
     */
    @RequestMapping("/listPoInfo.asyn")
    @ResponseBody
    public ViewResponseBean listPoInfo(String orderCode) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(orderCode).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List<Map<String, Object>> saleShelfModels = receiveBillService.listSaleShelfInfo(orderCode);
            return ResponseFactory.success(saleShelfModels);
        } catch (SQLException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
        } catch (NullPointerException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
    }

    /**
     * 判断作废单据是否关联其他应收账单开票
     * @param ids
     */
    @RequestMapping("/isExistRelInvoice.asyn")
    @ResponseBody
    private ViewResponseBean isExistRelInvoice(String ids) {
        Map<String, String> result = new HashMap<>();
        try {
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils().addObject(ids).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List<String> idList = StrUtils.parseIds(ids);
            if (idList.size() > 1) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }
            // 响应结果集
            result = receiveBillService.isExistRelInvoice(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(result);
    }

    /**
     * 提交作废申请
     */
    @RequestMapping("/saveInvalidBill.asyn")
    @ResponseBody
    private ViewResponseBean saveInvalidBill(String ids, String invalidRemark) {
        Map<String, String> result = new HashMap<>();
        try {
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils()
                    .addObject(ids).addObject(invalidRemark).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            User user = ShiroUtils.getUser();
            // 响应结果集
            result = receiveBillService.saveInvalidBill(ids, invalidRemark, user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(result);
    }

    /**
     * 校验开票应收单关联业务单号的所有单据状态是否是“待核销/部分核销/已核销",且开票状态是”未开票“
     * @param ids
     */
    @RequestMapping("/validateRelCodeInfo.asyn")
    @ResponseBody
    public ViewResponseBean validateRelCodeInfo(String ids) {
        try {
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            Map<String, String> resultMap = receiveBillService.validateRelCodeInfo(list);
            return ResponseFactory.success(resultMap);
        } catch (SQLException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
        } catch (NullPointerException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
    }

    /**
     * 根据应收账单号查询应收账单是否生成
     * @param billCode
     */
    @RequestMapping("/isBillSave.asyn")
    @ResponseBody
    public ViewResponseBean isBillSave(String billCode) {
        try {
            //校验id是否正确
            boolean isNull = new EmptyCheckUtils().addObject(billCode).empty();
            if (isNull) {
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            Map<String, String> resultMap = receiveBillService.isBillSave(billCode);
            return ResponseFactory.success(resultMap);
        } catch (SQLException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
        } catch (NullPointerException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
    }

    /**
     * 根据查询条件导出调拨订单
     */
    @RequestMapping("/exportReceiveBills.asyn")
    public void exportReceiveBills(ReceiveBillDTO dto, HttpServletResponse response, HttpServletRequest request) throws SQLException {
        User user= ShiroUtils.getUser();
        dto.setMerchantId(user.getMerchantId());

        //表头信息
        Map<Long, ReceiveBillDTO> receiveBillDTOMap = receiveBillService.listForExport(dto, user);
        List<Map<String,Object>> receiveBillList = new LinkedList<>();
        List<Long> billIds = new ArrayList<>();
        Map<Long, String> idCodeMap = new HashMap<>();
        Map<Long, String> customerNameMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for (Long billId : receiveBillDTOMap.keySet()) {
            billIds.add(billId);
            ReceiveBillDTO receiveBillDTO = receiveBillDTOMap.get(billId);
            idCodeMap.put(billId, receiveBillDTO.getCode());
            customerNameMap.put(billId,receiveBillDTO.getCustomerName());
            BigDecimal receivedPrice = receiveBillDTO.getReceivedPrice() == null ? new BigDecimal("0") : receiveBillDTO.getReceivedPrice();
            Map<String, Object> receiveBillMap = new HashMap<>();
            receiveBillMap.put("code", receiveBillDTO.getCode());
            receiveBillMap.put("relCode", receiveBillDTO.getRelCode());
            receiveBillMap.put("customerName", receiveBillDTO.getCustomerName());
            receiveBillMap.put("buName", receiveBillDTO.getBuName());
            receiveBillMap.put("poNo", receiveBillDTO.getPoNo());
            receiveBillMap.put("invoiceNo", receiveBillDTO.getInvoiceNo());
            receiveBillMap.put("invoiceDate", receiveBillDTO.getInvoiceDate());
            receiveBillMap.put("receivablePrice", receiveBillDTO.getReceivablePrice());
            receiveBillMap.put("currencyLabel", receiveBillDTO.getCurrencyLabel());
            receiveBillMap.put("billMonth", receiveBillDTO.getBillDate() != null ? sdf.format(receiveBillDTO.getBillDate()) : null);
            receiveBillMap.put("receivedPrice", receiveBillDTO.getReceivedPrice());
            receiveBillMap.put("uncollectedPrice", receiveBillDTO.getReceivablePrice().subtract(receivedPrice));
            receiveBillMap.put("billStatus", receiveBillDTO.getBillStatusLabel());
            receiveBillMap.put("invoiceStatus", receiveBillDTO.getInvoiceStatusLabel());
            receiveBillMap.put("ncStatus", receiveBillDTO.getNcStatusLabel());
            receiveBillMap.put("ncCode", receiveBillDTO.getNcCode());
            receiveBillMap.put("voucherCode", receiveBillDTO.getVoucherCode());
            receiveBillMap.put("voucherStatus", receiveBillDTO.getVoucherStatusLabel());
            receiveBillMap.put("period", receiveBillDTO.getPeriod());
            receiveBillMap.put("creater", receiveBillDTO.getCreater());
            receiveBillMap.put("createDate", receiveBillDTO.getCreateDate());
            receiveBillMap.put("sortType", DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_sortTypeList, receiveBillDTO.getSortType()));
            receiveBillList.add(receiveBillMap);
        }
        //表头信息
        String[] columns = {"应收账单号","应收分类","关联业务单号","客户","事业部","PO号","发票号码","发票日期","应收金额","币种",
                "账单月份","已收金额", "未收金额","账单状态","开票状态","NC应收状态","NC单据","凭证编码","凭证状态","会计期间","创建人", "创建时间"};
        String[] keys = {"code","sortType","relCode","customerName","buName","poNo","invoiceNo","invoiceDate","receivablePrice","currencyLabel",
                "billMonth","receivedPrice", "uncollectedPrice", "billStatus","invoiceStatus","ncStatus","ncCode","voucherCode","voucherStatus",
                "period", "creater","createDate"};

        //应收明细
        List<Map<String,Object>> receiveBillItemList = new ArrayList<>();
        List<Map<String,Object>> receiveBillCostItemList = new ArrayList<>();
        if (!billIds.isEmpty()) {
            //应收明细
            receiveBillItemList = receiveBillService.listForItemExport(billIds, idCodeMap,customerNameMap);

            //费项明细
            receiveBillCostItemList = receiveBillService.listForCostItemExport(billIds, idCodeMap,customerNameMap);

        }

        String[] itemColumns = {"应收账单号","客户名称","项目","PO号","平台SKU编码","标准条码","商品货号","商品名称","数量","结算金额",
                "系统业务单号","母品牌","发票描述","核销平台", "NC收支费项","科目名称"};
        String[] itemKeys = {"billCode","customerName","projectName","poNo","platformSku","commbarcode","goodsNo","goodsName","num",
                "price", "code", "parentBrandName","invoiceDescription","verifyPlatformCode","paymentSubjectName", "subjectName"};


        String[] costColumns = {"应收账单号","客户名称","费用类型","PO号","标准条码","商品货号","商品名称", "类型","数量","费用金额",
                "母品牌","NC收支费项","备注","发票描述","核销平台",  };
        String[] costKeys = {"billCode","customerName","projectName","poNo","commbarcode","goodsNo","goodsName","billType", "num",
                "price", "parentBrandName","paymentSubjectName", "remark","invoiceDescription","verifyPlatformCode"};

        //生成表格
        ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet("应收账单信息", columns, keys, receiveBillList);
        ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet("应收明细", itemColumns, itemKeys, receiveBillItemList);
        ExportExcelSheet costSheet = ExcelUtilXlsx.createSheet("费项明细", costColumns, costKeys, receiveBillCostItemList);

        List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
        sheets.add(mainSheet) ;
        sheets.add(itemSheet) ;
        sheets.add(costSheet) ;

        SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
        //导出文件
        String fileName = "应收账单"+ TimeUtils.formatDay();
        FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
    }

    /**
     * 刷新
     */
    @RequestMapping("/updateVoucher.asyn")
    @ResponseBody
    public ViewResponseBean updateVoucher(String ids) {
        Map<String, String> retMap = new HashMap<String, String>();
        try{
            //校验id是否正确
            boolean isNull = new EmptyCheckUtils().addObject(ids).empty();
            if (isNull) {
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List list = StrUtils.parseIds(ids);
            retMap = receiveBillService.updateVoucher(list);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(retMap);
    }

    /**
     * 创建应收
     * @param model
     */
    @RequestMapping("/toAddPage.html")
    public String toAddPage(Model model) throws Exception {
        List<SelectBean> brandSelectBeans = new ArrayList<>();
        List<BrandSuperiorMongo> brandList = brandSuperiorMongoDao.findAll(new HashMap<>());
        if (brandList != null && brandList.size() > 0) {
            for (BrandSuperiorMongo brandSuperiorMongo : brandList) {
                SelectBean selectBean = new SelectBean();
                selectBean.setValue(String.valueOf(brandSuperiorMongo.getBrandSuperiorId()));
                selectBean.setLabel(brandSuperiorMongo.getName());
                brandSelectBeans.add(selectBean);
            }
        }
        model.addAttribute("brandSelectBeans", brandSelectBeans);
        ArrayList<DerpBasic> storePlatformList = DERP.storePlatformList;
        List<SelectBean> storePlatformSeleteBeans = new ArrayList<>();
        for (DerpBasic basic : storePlatformList) {
            SelectBean selectBean = new SelectBean();
            selectBean.setValue(String.valueOf(basic.getKey()));
            selectBean.setLabel(basic.getValue());
            storePlatformSeleteBeans.add(selectBean);
        }
        model.addAttribute("storePlatformList", storePlatformSeleteBeans);
        return "derp/bill/receive-bill-add";
    }

    /**
     * 保存应收账单
     */
    @RequestMapping("/saveAddReceiveBill.asyn")
    @ResponseBody
    public ViewResponseBean saveAddReceiveBill(String json) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        try {
            // 实例化JSON对象
            User user = ShiroUtils.getUser();
            JSONObject jsonData = JSONObject.fromObject(json);
            Map classMap = new HashMap();
            classMap.put("costItemList", Map.class);
            ReceiveBillDTO dto = (ReceiveBillDTO) JSONObject.toBean(jsonData, ReceiveBillDTO.class, classMap);
            retMap = receiveBillService.saveApiAddReceiveBill(dto, user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(retMap);
    }

    /**
     * 获取勾稽预收单分页数据
     * @param dto
     */
    @RequestMapping("/listBeVerifyAdvanceBill.asyn")
    @ResponseBody
    private ViewResponseBean listBeVerifyAdvanceBill(ReceiveBillVerifyAdvanceDTO dto) {
        try {
            User user = ShiroUtils.getUser();
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            advanceBillService.listVerifyAdvanceByPage(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(dto);
    }

    /**
     * 保存预收核销信息
     */
    @RequestMapping("/saveAdvanceVerify.asyn")
    @ResponseBody
    public ViewResponseBean saveAdvanceVerify(String ids) {
        Map<String, String> retMap = new HashMap<String, String>();
        try {
            retMap = receiveBillVerifyItemService.saveAdvanceVerifyItem(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(retMap);
    }

    /**
     * 应收明细导入
     * @param file
     * @param billId
     * @return
     */
    @RequestMapping("/importReceiveItems.asyn")
    @ResponseBody
    public ViewResponseBean importReceiveItems(@RequestParam(value = "file", required = false) MultipartFile file, Long billId) {
        Map resultMap = new HashMap();//返回的结果集
        try{
            if(file==null){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
            if(data == null){//数据为空
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }
            User user= ShiroUtils.getUser();
            resultMap = receiveBillService.importReceiveItems(data, billId, user);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(resultMap);
    }

    /**
     * 应收明细导出
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("/exportReceiveItems.asyn")
    public void exportReceiveItems(HttpServletResponse response, HttpServletRequest request, String json) throws Exception{
        /** 标题  */
        /*String[] COLUMNS= {"系统业务单号","费项编码","结算费项","PO号","平台SKU编码","商品货号","税率","数量","结算金额（不含税）"};
        String[] KEYS= {"code","projectCode","projectName","poNo","platformSku","goodsNo","taxRate","num","price"};
        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , itemList);
        //导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);*/
        OutputStream out = null;
        try{
            String fileName = "应收明细导出";
            List<ReceiveBillItemDTO> itemList=null;
            if(json != null || StringUtils.isNotBlank(json)){			//输入信息不完整
                itemList = receiveBillService.exportListItem(json);
            }
            Workbook workbook = null ;

            ReceiveBillExportDTO dto=new ReceiveBillExportDTO();
            dto.setItemList(itemList);
            workbook = DownloadExcelUtil.exportTemplateExcel("RECEIVEITEM", dto);

            FileExportUtil.setHeader(response, request, fileName +".xlsx");
            out = response.getOutputStream();
            workbook.write(out);
        }catch(Exception e) {
            System.err.println("文件导出异常:" + e);
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 费用明细导入
     * @param file
     * @param billId
     * @return
     */
    @RequestMapping("/importReceiveCostItems.asyn")
    @ResponseBody
    public ViewResponseBean importReceiveCostItems(@RequestParam(value = "file", required = false) MultipartFile file, Long billId) {
        Map resultMap = new HashMap();//返回的结果集
        try{
            if(file==null){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
            if(data == null){//数据为空
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }
            User user= ShiroUtils.getUser();
            resultMap = receiveBillService.importReceiveCostItems(data, billId, user);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(resultMap);
    }

    /**
     * 费用明细导出
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("/exportReceiveCostItems.asyn")
    public void exportReceiveCostItems(HttpServletResponse response, HttpServletRequest request, String json) throws Exception{
      /*  *//** 标题  *//*

        String[] COLUMNS= {"费项编码","类型","PO号","商品货号","母品牌","数量","费用金额（不含税）","税率","发票描述","备注"};
        String[] KEYS= {"projectCode","billType","poNo","goodsNo","parentBrandCode","num","price","taxRate","invoiceDescription","remark"};

        String sheetName = "费用明细导出";
        List<ReceiveBillCostItemDTO> itemList=null;
        if(json != null || StringUtils.isNotBlank(json)){			//输入信息不完整
            itemList = receiveBillService.exportListCostItem(json);
        }
        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , itemList);
        //导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);*/

        OutputStream out = null;
        try{
            String fileName = "费用明细导出";
            List<ReceiveBillCostItemDTO> itemList=null;
            if(json != null || StringUtils.isNotBlank(json)){			//输入信息不完整
                itemList = receiveBillService.exportListCostItem(json);
            }
            Workbook workbook = null ;

            ReceiveBillExportDTO dto=new ReceiveBillExportDTO();
            dto.setCostItemList(itemList);
            workbook = DownloadExcelUtil.exportTemplateExcel("RECEIVECOST", dto);

            FileExportUtil.setHeader(response, request, fileName +".xlsx");
            out = response.getOutputStream();
            workbook.write(out);
        }catch(Exception e) {
            System.err.println("文件导出异常:" + e);
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建应收页面明细导入
     * @param file
     * @param customerId
     * @return
     */
    @RequestMapping("/importAddReceiveItems.asyn")
    @ResponseBody
    public ViewResponseBean importAddReceiveItems(@RequestParam(value = "file", required = false) MultipartFile file, Long customerId) {
        Map resultMap = new HashMap();//返回的结果集
        try{
            if(file==null){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
            if(data == null){//数据为空
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }
            User user= ShiroUtils.getUser();
            resultMap = receiveBillService.importAddReceiveItems(data, customerId, user);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(resultMap);
    }

}
