package com.topideal.report.webapi.reporting;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.BrandSaleDTO;
import com.topideal.entity.dto.InventoryWarningCountDto;
import com.topideal.entity.dto.SkuPriceWarnDTO;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.report.service.reporting.ExternalAccessService;
import com.topideal.report.service.reporting.InventoryFallingPriceReservesService;
import com.topideal.report.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Description: 首页 提供给外部访问的类
 **/
@RestController
@RequestMapping("/webapi/report/external")
@Api(tags = "首页 提供给外部访问的类")
public class APIExternalAccessController {

    @Autowired
    private InventoryFallingPriceReservesService inventoryFallingPriceReservesService;

    @Autowired
    private ExternalAccessService externalAccessService;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao ;

    @ApiOperation(value = "根据商家、仓库统计当天效期预警")
    @PostMapping(value = "/countInventoryWarning.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "depotId", value = "仓库id", required = true)})
    private ResponseBean countInventoryWarning(@RequestParam(value = "token", required = true) String token, Long depotId) {

        try {
            // 响应结果集
            List<InventoryWarningCountDto> list = new ArrayList<InventoryWarningCountDto>();
            Map<String, Object> paramMap = new HashMap<>();
            User user = ShiroUtils.getUserByToken(token);
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                paramMap.put("merchantId", user.getMerchantId());

                List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
                if(!buList.isEmpty()) {
                    paramMap.put("buList",buList);
                }
            }
            paramMap.put("depotId", depotId);
            paramMap.put("reportDate", TimeUtils.formatDay(new Date()));
            list = inventoryFallingPriceReservesService.countInventoryWarning(paramMap);
            for (InventoryWarningCountDto vo : list) {
                String effectiveIntervalLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.fallingPrice_effectiveIntervalLabelList, vo.getEffectiveInterval());
                vo.setEffectiveIntervalLabel(effectiveIntervalLabel);
            }

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    /**
     * 根据商家、单据业务模式统计销售出库单的销售数量
     * @Param
     * @return
     */
    @ApiOperation(value = "根据商家、单据业务模式统计销售出库单的销售数量")
    @PostMapping(value = "/countSaleOutOrderSaleNum.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "type", value = "单据业务模式", required = true),
            @ApiImplicitParam(name = "deliverDate", value = "出库时间", required = true)
    })
    private ResponseBean countSaleOutOrderSaleNum(String token, String type, String deliverDate) {

        try {
            List<BrandSaleDTO> list = new ArrayList<BrandSaleDTO>();
            Map<String, Object> paramMap = new HashMap<>();
            Long merchantId = null;
            User user = ShiroUtils.getUserByToken(token);
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                merchantId = user.getMerchantId();
                paramMap.put("merchantId", merchantId);

                List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
                if(!buList.isEmpty()) {
                    paramMap.put("buList",buList);
                }
            }
            List<String> typeList = Arrays.asList(type.split(","));
            paramMap.put("saleTypes",typeList);
            paramMap.put("deliverDate",deliverDate);
            list = externalAccessService.countSaleOutOrderByMerchantIdAndSaleType(paramMap);

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    /**
     * 根据商家、单据业务模式统计销售出库单的销售top 10 的品牌
     * @Param
     * @return
     */
    @ApiOperation(value = "根据商家、单据业务模式统计销售出库单的销售top 10 的品牌")
    @PostMapping(value = "/getTop10SaleOutOrderBrand.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "type", value = "单据业务模式", required = true),
            @ApiImplicitParam(name = "deliverDate", value = "出库时间", required = true)
    })
    private ResponseBean getTop10SaleOutOrderBrand(String token, String type, String deliverDate) {

        try {
            List<BrandSaleDTO> list = new ArrayList<BrandSaleDTO>();
            Map<String, Object> paramMap = new HashMap<>();
            User user = ShiroUtils.getUserByToken(token);
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
               paramMap.put("merchantId", user.getMerchantId());

                List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
                if(!buList.isEmpty()) {
                    paramMap.put("buList",buList);
                }
            }
            List<String> typeList = Arrays.asList(type.split(","));
            paramMap.put("saleTypes",typeList);
            paramMap.put("deliverDate",deliverDate);
            list = externalAccessService.getSaleOutOrderTop10ByBrand(paramMap);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    /**
     * 根据商家、单据业务模式统计销售出库单的销售数量
     * @Param
     * @return
     */
    @ApiOperation(value = "根据商家、单据业务模式统计销售出库单的销售数量")
    @PostMapping(value = "/countOrderSaleNum.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "type", value = "单据业务模式", required = true),
            @ApiImplicitParam(name = "deliverDate", value = "出库时间", required = true)
    })
    private ResponseBean countOrderSaleNum(String token, String type, String deliverDate) {

        try {

            List<BrandSaleDTO> list = new ArrayList<BrandSaleDTO>();
            Map<String, Object> paramMap = new HashMap<>();
            User user = ShiroUtils.getUserByToken(token);
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                paramMap.put("merchantId", user.getMerchantId());

                List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
                if(!buList.isEmpty()) {
                    paramMap.put("buList",buList);
                }
            }
            paramMap.put("type", type);
            paramMap.put("deliverDate",deliverDate);
            paramMap.put("statisticalDimension",DERP_REPORT.INDEXORDERSTATISTICS_STATISTICALDIMENSION_1);
            list = externalAccessService.countOrderByMerchantIdAndShopType(paramMap);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    /**
     * 根据商家、单据业务模式统计销售出库单的电商订单Top10品牌
     * @Param
     * @return
     */
    @ApiOperation(value = "根据商家、单据业务模式统计销售出库单的电商订单Top10品牌")
    @PostMapping(value = "/getTop10OrderBrand.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "type", value = "单据业务模式", required = true),
            @ApiImplicitParam(name = "deliverDate", value = "出库时间", required = true)
    })
    private ResponseBean getTop10OrderBrand(String token, String type, String deliverDate) {

        try {
            List<BrandSaleDTO> list = new ArrayList<BrandSaleDTO>();
            Map<String, Object> paramMap = new HashMap<>();
            User user = ShiroUtils.getUserByToken(token);
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                paramMap.put("merchantId", user.getMerchantId());

                List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
                if(!buList.isEmpty()) {
                    paramMap.put("buList",buList);
                }
            }
            paramMap.put("statisticalDimension",DERP_REPORT.INDEXORDERSTATISTICS_STATISTICALDIMENSION_2);
            paramMap.put("type", type);
            paramMap.put("deliverDate",deliverDate);
            list = externalAccessService.countOrderByMerchantIdAndShopType(paramMap);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    /**
     * 统计对应商家主体下所有代销客户的销售总量
     * @Param
     * @return
     */
    @ApiOperation(value = "统计对应商家主体下所有代销客户的销售总量")
    @PostMapping(value = "/countBillOutDepotOrderNum.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "deliverDate", value = "出库时间", required = true)
    })
    private ResponseBean countBillOutDepotOrderNum(String token, String deliverDate) {

        try {
            User user = ShiroUtils.getUserByToken(token);
            Map<String, Object> paramMap = new HashMap<>();
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                paramMap.put("merchantId", user.getMerchantId());

                List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
                if(!buList.isEmpty()) {
                    paramMap.put("buList",buList);
                }
            }
            paramMap.put("deliverDate", deliverDate);
            List<Map<String, Object>> mapList = externalAccessService.countByCustomer(paramMap);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, mapList);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }

    /**
     * 统计所有客户代销模式下销售top 10 的品牌
     * @Param
     * @return
     */
    @ApiOperation(value = "统计所有客户代销模式下销售top 10 的品牌")
    @PostMapping(value = "/getBillOutDepotTop10ByBrand.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "deliverDate", value = "出库时间", required = true)
    })
    private ResponseBean getBillOutDepotTop10ByBrand(String token, String deliverDate) {

        try {
            User user = ShiroUtils.getUserByToken(token);
            Map<String, Object> param = new HashMap<>();
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                param.put("merchantId", user.getMerchantId());

                List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
                if(!buList.isEmpty()) {
                    param.put("buList",buList);
                }
            }
            param.put("deliverDate", deliverDate);
            List<Map<String, Object>> mapList = externalAccessService.getBillOutDepotTop10ByBrand(param);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, mapList);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }

    /**
     * SKU预警列表
     * @Param
     * @return
     */
    @ApiOperation(value = "SKU预警列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value = "/getSkuPriceWarns.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean getSkuPriceWarns(String token) {
        try {
            User user = ShiroUtils.getUserByToken(token);
            Map<String, Object> param = new HashMap<>();
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                param.put("merchantId", user.getMerchantId());

                List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
                if(!buList.isEmpty()) {
                    param.put("buList",buList);
                }
            }
            List<SkuPriceWarnDTO> result = externalAccessService.getSkuPriceWarns(param);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, result);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }

    /**
     * 统计SKU预警
     * @Param
     * @return
     */
    @ApiOperation(value = "统计SKU预警")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @PostMapping(value = "/countSkuPriceWarns.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean countSkuPriceWarns(String token) {
        try {
            User user = ShiroUtils.getUserByToken(token);
            Map<String, Object> param = new HashMap<>();
            if (!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())) {
                param.put("merchantId", user.getMerchantId());

                List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
                if(!buList.isEmpty()) {
                    param.put("buList",buList);
                }
            }
            Long count = externalAccessService.countSkuPriceWarns(param);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, count);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }


    }

}
