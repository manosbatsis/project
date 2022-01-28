package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.*;
import com.topideal.entity.vo.sale.AgreementCurrencyConfigModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface AgreementCurrencyConfigDao extends BaseDao<AgreementCurrencyConfigModel>{

    /**
     * 根据条件查询（分页）
     * @return
     */
    AgreementCurrencyConfigDTO queryDTOListByPage(AgreementCurrencyConfigDTO dto)throws SQLException;

    int getTotal(AgreementCurrencyConfigDTO dto) throws SQLException;
    /**
     * 根据条件查询
     * @return
     */
    List<AgreementCurrencyConfigDTO> queryDTOList(AgreementCurrencyConfigDTO dto)throws SQLException;

    /**
     * 导出查询表头表体
     * @param dto
     * @return
     * @throws SQLException
     */
    List<AgreementCurrencyConfigExportDTO> getDetailsByExport(AgreementCurrencyConfigDTO dto) throws SQLException;

    /**
     * 根据条件获取信息
     * @param map
     * @return
     */
    Map<String,Object> getConfigByMap(Map<String, Object> map);
    /**
     * 逻辑删除
     * @param ids
     * @return
     */
    int delConfig(List ids)throws SQLException;

}
