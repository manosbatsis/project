package com.topideal.mapper.base;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.base.ExchangeRateDTO;
import com.topideal.entity.vo.base.ExchangeRateModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

/**
 * 汇率管理表 mapper
 * @author lian_
 */
@MyBatisRepository
public interface ExchangeRateMapper extends BaseMapper<ExchangeRateModel> {

	/**
     * 查询所有数据
     * @return
     */
    PageDataModel<ExchangeRateDTO> getListByPage(ExchangeRateDTO dto)throws SQLException;

    List<ExchangeRateModel> listNoDel(ExchangeRateModel model)throws SQLException;

	ExchangeRateDTO searchDTOById(Long id);

    //根据汇率日期删除汇率
    void deleteByEffectiveDate(@Param("effectiveDate") String effectiveDate) throws SQLException;

    /**
     * 根据本位币+原币查询最新的汇率
     * @param model
     * @return
     * @throws SQLException
     */
    ExchangeRateModel getLatestRate(ExchangeRateModel model) throws SQLException;
}
