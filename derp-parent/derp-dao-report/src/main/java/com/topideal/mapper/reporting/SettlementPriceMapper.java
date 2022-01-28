package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.SettlementPriceDTO;
import com.topideal.entity.dto.SettlementPriceExamineDTO;
import com.topideal.entity.dto.SettlementPriceWarnconfigDTO;
import com.topideal.entity.vo.reporting.SettlementPriceModel;

/**
 * 结算价格 mapper
 * @author lian_
 */
@MyBatisRepository
public interface SettlementPriceMapper extends BaseMapper<SettlementPriceModel> {
	/**
	 * 获取列表数据（分页）
	 * @param model
	 * @return
	 */
	PageDataModel<SettlementPriceModel> getListByPage(SettlementPriceModel model)throws SQLException ;
	/**
	 * 根据商家+商品条码查询存在的记录(剔除修改记录中的id)
	 * @param merchantId
	 * @param barcode
	 * @param ids
	 * @return
	 */
	List<SettlementPriceModel> getListNotInIds(@Param("merchantId") Long merchantId, @Param("barcode") String barcode, @Param("ids") String ids);

	public SettlementPriceModel getPriceOne(Map<String, Object> paramMap);
	
	/**
	 * 根据条件查询
	*/
	List<SettlementPriceDTO> queryList(SettlementPriceDTO dto);
	
	/**
	 *  根据ids查询
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<SettlementPriceModel> searchByIds(@Param("ids") List list);
	
	/**
	 * 获取分页和数量
	 * @param model
	 * @return
	 */
	List<SettlementPriceExamineDTO> listExamineList(SettlementPriceExamineDTO model);
	Integer countExamineList(SettlementPriceExamineDTO model);
	
	/**
	 * 分页
	 * @param dto
	 * @return
	 */
	PageDataModel<SettlementPriceDTO> getListByPage(SettlementPriceDTO dto);
	
	List<SettlementPriceDTO> getListByEffectiveDate(Map<String, Object> map);

}
