package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.SaleDeclareOrderDTO;
import com.topideal.entity.vo.sale.SaleDeclareOrderModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SaleDeclareOrderMapper extends BaseMapper<SaleDeclareOrderModel> {
	/**
     * 查询所有数据 分页
     * @return
     */
    PageDataModel<SaleDeclareOrderDTO> listDTOByPage(SaleDeclareOrderDTO dto)throws SQLException;
    /**
     * 查询所有数据
     * @return
     */
    List<SaleDeclareOrderDTO> listSaleDeclareOrder(SaleDeclareOrderDTO dto)throws SQLException;
    /**
     * 根据id查询数据
     * @return
     */
    SaleDeclareOrderDTO searchDetail(SaleDeclareOrderDTO dto)throws SQLException;
    /**
	 * 统计类型数量
	 * @param dto
	 * @return
	 */
	List<Map<String, Object>> getDeclareTypeNum(SaleDeclareOrderDTO dto);
	/**
     * 修改
     * @param model
     */
    int modifyWithNull(SaleDeclareOrderModel  model) throws SQLException ;
	
}
