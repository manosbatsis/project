//package com.topideal.report.web.reporting;
//
//import com.topideal.common.constant.DERP_REPORT;
//import com.topideal.common.constant.DERP_SYS;
//import com.topideal.common.system.auth.User;
//import com.topideal.common.system.web.ResponseFactory;
//import com.topideal.common.system.web.StateCodeEnum;
//import com.topideal.common.system.web.ViewResponseBean;
//import com.topideal.common.tools.TimeUtils;
//import com.topideal.entity.dto.BrandSaleDTO;
//import com.topideal.entity.dto.InventoryWarningCountDto;
//import com.topideal.entity.vo.reporting.SkuPriceWarnModel;
//import com.topideal.report.service.reporting.ExternalAccessService;
//import com.topideal.report.service.reporting.InventoryFallingPriceReservesService;
//import com.topideal.report.shiro.ShiroUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.sql.SQLException;
//import java.util.*;
//
///**
// * @Description: 首页 提供给外部访问的类
// **/
//@Controller
//@RequestMapping("/external")
//public class ExternalAccessController {
//
//    @Autowired
//    private InventoryFallingPriceReservesService inventoryFallingPriceReservesService;
//
//    @Autowired
//    private ExternalAccessService externalAccessService;
//
//
//    /**
//     * 根据商家、仓库统计当天效期预警
//     * @Param
//     * @return
//     */
//    @RequestMapping("/countInventoryWarning.asyn")
//    @ResponseBody
//    private ViewResponseBean countInventoryWarning(Long depotId) {
//        List<InventoryWarningCountDto> list = new ArrayList<InventoryWarningCountDto>();
//        Long merchantId = null;
//        try {
//            // 响应结果集
//            Map<String, Object> paramMap = new HashMap<>();
//            User user = ShiroUtils.getUser();
//            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
//                merchantId = user.getMerchantId();
//                paramMap.put("merchantId", merchantId);
//            }
//            paramMap.put("depotId", depotId);
//            paramMap.put("reportDate", TimeUtils.formatDay(new Date()));
//            list = inventoryFallingPriceReservesService.countInventoryWarning(paramMap);
//            for (InventoryWarningCountDto vo : list) {
//                String effectiveIntervalLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.fallingPrice_effectiveIntervalLabelList, vo.getEffectiveInterval());
//                vo.setEffectiveIntervalLabel(effectiveIntervalLabel);
//            }
//        } catch (SQLException e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//        } catch (Exception e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//        }
//        return ResponseFactory.success(list);
//    }
//
//    /**
//     * 根据商家、单据业务模式统计销售出库单的销售数量
//     * @Param
//     * @return
//     */
//    @RequestMapping("/countSaleOutOrderSaleNum.asyn")
//    @ResponseBody
//    private ViewResponseBean countSaleOutOrderSaleNum(String type, String deliverDate) {
//        List<BrandSaleDTO> list = new ArrayList<BrandSaleDTO>();
//        Long merchantId = null;
//        try {
//            // 响应结果集
//            Map<String, Object> paramMap = new HashMap<>();
//            User user = ShiroUtils.getUser();
//            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
//                merchantId = user.getMerchantId();
//                paramMap.put("merchantId", merchantId);
//            }
//            List<String> typeList = Arrays.asList(type.split(","));
//            list = externalAccessService.countSaleOutOrderByMerchantIdAndSaleType(merchantId, typeList, deliverDate);
//        } catch (SQLException e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//        } catch (Exception e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//        }
//        return ResponseFactory.success(list);
//    }
//
//    /**
//     * 根据商家、单据业务模式统计销售出库单的销售top 10 的品牌
//     * @Param
//     * @return
//     */
//    @RequestMapping("/getTop10SaleOutOrderBrand.asyn")
//    @ResponseBody
//    private ViewResponseBean getTop10SaleOutOrderBrand(String type, String deliverDate) {
//        List<BrandSaleDTO> list = new ArrayList<BrandSaleDTO>();
//        Long merchantId = null;
//        try {
//            // 响应结果集
//            Map<String, Object> paramMap = new HashMap<>();
//            User user = ShiroUtils.getUser();
//            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
//                merchantId = user.getMerchantId();
//                paramMap.put("merchantId", merchantId);
//            }
//            List<String> typeList = Arrays.asList(type.split(","));
//            list = externalAccessService.getSaleOutOrderTop10ByBrand(merchantId, typeList, deliverDate);
//        } catch (SQLException e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//        } catch (Exception e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//        }
//        return ResponseFactory.success(list);
//    }
//
//    /**
//     * 根据商家、单据业务模式统计销售出库单的销售数量
//     * @Param
//     * @return
//     */
//    @RequestMapping("/countOrderSaleNum.asyn")
//    @ResponseBody
//    private ViewResponseBean countOrderSaleNum(String type, String deliverDate) {
//        List<BrandSaleDTO> list = new ArrayList<BrandSaleDTO>();
//        Long merchantId = null;
//        try {
//            // 响应结果集
//            Map<String, Object> paramMap = new HashMap<>();
//            User user = ShiroUtils.getUser();
//            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
//                merchantId = user.getMerchantId();
//            }
//            list = externalAccessService.countOrderByMerchantIdAndShopType(merchantId, type, deliverDate, DERP_REPORT.INDEXORDERSTATISTICS_STATISTICALDIMENSION_1);
//        } catch (SQLException e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//        } catch (Exception e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//        }
//        return ResponseFactory.success(list);
//    }
//
//    /**
//     * 根据商家、单据业务模式统计销售出库单的电商订单Top10品牌
//     * @Param
//     * @return
//     */
//    @RequestMapping("/getTop10OrderBrand.asyn")
//    @ResponseBody
//    private ViewResponseBean getTop10OrderBrand(String type, String deliverDate) {
//        List<BrandSaleDTO> list = new ArrayList<BrandSaleDTO>();
//        Long merchantId = null;
//        try {
//            // 响应结果集
//            Map<String, Object> paramMap = new HashMap<>();
//            User user = ShiroUtils.getUser();
//            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
//                merchantId = user.getMerchantId();
//            }
//            list = externalAccessService.countOrderByMerchantIdAndShopType(merchantId, type, deliverDate, DERP_REPORT.INDEXORDERSTATISTICS_STATISTICALDIMENSION_2);
//        } catch (SQLException e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//        } catch (Exception e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//        }
//        return ResponseFactory.success(list);
//    }
//
//    /**
//     * 统计对应商家主体下所有代销客户的销售总量
//     * @Param
//     * @return
//     */
//    @RequestMapping("/countBillOutDepotOrderNum.asyn")
//    @ResponseBody
//    private ViewResponseBean countBillOutDepotOrderNum(String deliverDate) {
//        User user = ShiroUtils.getUser();
//        Map<String, Object> param = new HashMap<>();
//        if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
//            param.put("merchantId", user.getMerchantId());
//        }
//        param.put("deliverDate", deliverDate);
//        List<Map<String, Object>> mapList = new ArrayList<>();
//        try {
//            mapList = externalAccessService.countByCustomer(param);
//        } catch (SQLException e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//        } catch (Exception e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//        }
//
//        return ResponseFactory.success(mapList);
//
//    }
//
//    /**
//     * 统计所有客户代销模式下销售top 10 的品牌
//     * @Param
//     * @return
//     */
//    @RequestMapping("/getBillOutDepotTop10ByBrand.asyn")
//    @ResponseBody
//    private ViewResponseBean getBillOutDepotTop10ByBrand(String deliverDate) {
//        User user = ShiroUtils.getUser();
//        Map<String, Object> param = new HashMap<>();
//        if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
//            param.put("merchantId", user.getMerchantId());
//        }
//        param.put("deliverDate", deliverDate);
//        List<Map<String, Object>> mapList = new ArrayList<>();
//        try {
//            mapList = externalAccessService.getBillOutDepotTop10ByBrand(param);
//        } catch (SQLException e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//        } catch (Exception e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//        }
//
//        return ResponseFactory.success(mapList);
//
//    }
//
//    /**
//     * SKU预警列表
//     * @Param
//     * @return
//     */
//    @RequestMapping("/getSkuPriceWarns.asyn")
//    @ResponseBody
//    private ViewResponseBean getSkuPriceWarns() {
//        List<SkuPriceWarnModel> result = new ArrayList<>();
//        try {
//            result = externalAccessService.getSkuPriceWarns();
//        } catch (Exception e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//        }
//
//        return ResponseFactory.success(result);
//
//    }
//
//    /**
//     * 统计SKU预警
//     * @Param
//     * @return
//     */
//    @RequestMapping("/countSkuPriceWarns.asyn")
//    @ResponseBody
//    private ViewResponseBean countSkuPriceWarns() {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            Long count = externalAccessService.countSkuPriceWarns();
//            result.put("count", count);
//        } catch (Exception e) {
//            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//        }
//
//        return ResponseFactory.success(result);
//
//    }
//
//}
