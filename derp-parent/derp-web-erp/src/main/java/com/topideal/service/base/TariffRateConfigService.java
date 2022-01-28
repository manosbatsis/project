package com.topideal.service.base;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.base.CustomsAreaConfigDTO;
import com.topideal.entity.dto.base.TariffRateConfigDTO;
import com.topideal.entity.vo.main.TariffRateConfigModel;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface TariffRateConfigService {

    /**
     * 查询税率列表
     * @param model
     * @return
     * @throws SQLException
     */
    TariffRateConfigDTO getListByPage(TariffRateConfigDTO model) throws SQLException;

    /**
     * 新增税率配置
     * @param model
     * @return
     */
    Map<String,Object> saveTariffRate(TariffRateConfigModel model) throws SQLException;


    /**
     * 批量删除
     * @param ids
     * @return
     */
    Map<String,Object>  delReptile(String ids) throws SQLException;

    /**
     * 获取下拉选择框
     * @return
     * @throws SQLException 
     */
	List<SelectBean> getSelectBean() throws SQLException;
    /**
     * 获取税率配置表
     * @param model
     * @return
     * @throws SQLException
     */
    List<TariffRateConfigModel>getTariffRateConfigList(TariffRateConfigModel model)throws SQLException;

}
