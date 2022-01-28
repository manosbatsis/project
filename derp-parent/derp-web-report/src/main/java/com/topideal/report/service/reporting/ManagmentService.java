package com.topideal.report.service.reporting;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.ManageDepartmentInventoryCleanDTO;
import com.topideal.entity.dto.ManageDepartmentInventoryDTO;
import com.topideal.entity.dto.ManageDepartmentSaleAchieveDTO;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.report.webapi.form.ManagmentReportForm;

import java.util.List;
import java.util.Map;

public interface ManagmentService {

    /**
     * 部门销售金额统计
     * @param form
     * @return
     */
    List getDepartmentSalesAmountStatistic(ManagmentReportForm form, User user);

    List<ManageDepartmentSaleAchieveDTO> getDepartmentSalesAchieveStatistic(ManagmentReportForm form, User user);

    /**
     * 部门库存清空天数统计 （经营报表）
     * @param form
     * @return
     */
    List<ManageDepartmentInventoryCleanDTO> getDepartmentInventoryCleanStatistic(ManagmentReportForm form, User user);

    /**
     * 获取公司列表(经营报表)
     * @return
     */
    List<MerchantInfoModel> getMerchantList(User user) throws Exception;

    List<BusinessUnitModel> getBuList(User user, List<Long> departmentIds);

    List<SelectBean> getDepartmentSelectList(ManagmentReportForm form, User user);

    List<SelectBean> getCustomerSelectList(User user);

    List<SelectBean> getDepotSelectList(User user);

    /**
     * 部门库存统计（经营管理报表）
     * @param form
     * @return
     */
    List<ManageDepartmentInventoryDTO> getDepartmentInventoryStatistic(ManagmentReportForm form, User user);

    /**
     * 部门库存统计导出 （经营管理报表）
     * @param form
     * @param user
     * @return
     */
    Map<String, Object> exportDepartmentInventoryStatistic(ManagmentReportForm form, User user);

    /**
     * 导出部门库存清空天数统计 （经营管理报表）
     * @param form
     * @param user
     * @return
     */
    Map<String, Object> exportDepartmentInventoryCleanStatistic(ManagmentReportForm form, User user);
}
