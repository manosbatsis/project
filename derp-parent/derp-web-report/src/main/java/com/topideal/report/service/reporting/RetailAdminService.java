package com.topideal.report.service.reporting;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.BuFinanceInventorySummaryDTO;
import com.topideal.entity.dto.SaleDataDTO;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.entity.vo.system.MerchantInfoModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface RetailAdminService {
    //获取事业部信息
    List<BusinessUnitModel> getBuList(User user) throws SQLException;
    //获取仓库信息
    List<Map<String,Object>> getDepotList(String month) throws Exception;
    //获取品牌信息
    List<Map<String,Object>> getBrandList(String month) throws Exception;

    List<Map<String, Object>> getTargetAndAchieve(Long departmentId, String month, User user) throws Exception;

    List<SaleDataDTO> getBrandSaleTop(SaleDataDTO dto, User user) throws SQLException;

    List<SaleDataDTO> getCusSaleTop(SaleDataDTO dto, User user) throws SQLException;

    List<Map<String, Object>> getInWarehouseData(Long buId, String month, User user) throws SQLException;

    List<Map<String, Object>> getInventoryAnalysisData(Long buId, String month, String type, User user) throws SQLException;

    //年度进销存分析
    Map<String, Object> getYearFinanceInventoryAnalysis(Long buId, String month, String isParentBrand, String brandIds, User user) throws Exception;

    //品牌在库天数
    List<Map<String, Object>> getBrandInWarehouse(Long buId, String month, User user, String inWarehouseRange, String isParentBrand) throws Exception;

    //仓库滞销预警、仓库效期预警 获取公司
    List<MerchantInfoModel> getMerchantList() throws Exception;

    //仓库滞销预警
    Map<String, Object> getDepotUnsellableWarning(String merchantIds, String month, String depotIds, User user) throws Exception;

    //仓库效期预警
    Map<String, Object> getDepotExpireWarning(String merchantIds, String month, String brandParentId, User user) throws Exception;
    //获取仓库滞销预警各品牌金额明细
    List<Map<String, Object>> getDepotUnsellableDetail(String merchantIds, String month, Long brandParentId, String inWarehouseInterval, String depotIds, User user) throws Exception;

    //事业部销售达成导出
    List<Map<String, Object>> getTargetAndAchieveExportList(Long departmentId, String month, User user) throws Exception;;

    //年度进销存分析导出
    List<BuFinanceInventorySummaryDTO> getYearFinanceInventoryAnalysisExportList(Long buId, String month, String isParentBrand, String brandIds, User user);

    //客户销量导出
    List<Map<String, Object>> getCustomersExportList(SaleDataDTO dto, User user);

    //品牌销量导出
    List<Map<String, Object>> getBrandExportList(SaleDataDTO dto, User user);

    //库存量分析导出
    List<Map<String, Object>> getInventoryAnalysisExportList(Long buId, String month, String type, User user);

    //商品在库天数导出
    List<Map<String, Object>> getInWarehouseDaysExportList(Long buId, String month, User user) throws Exception;
    //仓库滞销预警导出
    List<Map<String, Object>> getDepotUnsellableWarningExportList(String merchantIds, String month, User user, String depotIds) throws Exception;
    //仓库效期预警导出
    List<Map<String, Object>> getDepotExpireWarningExportList(String merchantIds, String month, User user, String brandParentIds) throws Exception;

    /**
     * 获取部门id(数据洞察, 经营管理)
     * @param user
     * @return
     * @throws Exception
     */
    List<Long> getDepartmentIds(User user) throws Exception;
}
