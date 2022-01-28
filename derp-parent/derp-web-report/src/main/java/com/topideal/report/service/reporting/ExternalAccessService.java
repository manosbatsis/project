package com.topideal.report.service.reporting;

import com.topideal.entity.dto.BrandSaleDTO;
import com.topideal.entity.dto.SkuPriceWarnDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 首页 提供给外部访问 service
 **/
public interface ExternalAccessService {


    /**
     * @Description 统计指定业务模式下的销售出库单的所有客户的销售数量
     */
    public List<BrandSaleDTO> countSaleOutOrderByMerchantIdAndSaleType(Map<String, Object> params) throws SQLException;

    /**
     * @Description 统计指定业务模式下的销售出库单的所有客户销售top 10 的品牌
     */
    public List<BrandSaleDTO> getSaleOutOrderTop10ByBrand(Map<String, Object> params) throws SQLException;

    /**
     * @Description 统计指定业务模式下的电商订单的所有店铺/品牌的Top10销售数量
     */
    public List<BrandSaleDTO> countOrderByMerchantIdAndShopType(Map<String, Object> params) throws SQLException;

    /**
     * 首页——统计对应商家主体下所有代销客户的销售总量
     */
    List<Map<String, Object>> countByCustomer(Map<String, Object> params) throws SQLException;

    /**
     * 首页——统计所有客户代销模式下销售top 10 的品牌
     */
    List<Map<String, Object>> getBillOutDepotTop10ByBrand(Map<String, Object> params) throws SQLException;

    /**
     * 首页——待办事项SKU预警：每月1号累计当月邮件预警的SKU
     */
    List<SkuPriceWarnDTO> getSkuPriceWarns(Map<String, Object> params) throws SQLException;

    /**
     * 首页——待办事项SKU预警：每月1号累计当月邮件预警的SKU数量
     */
    Long countSkuPriceWarns(Map<String, Object> params) throws SQLException;
}
