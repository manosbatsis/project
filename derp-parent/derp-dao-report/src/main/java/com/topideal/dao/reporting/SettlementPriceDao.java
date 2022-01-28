package com.topideal.dao.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.SettlementPriceDTO;
import com.topideal.entity.dto.SettlementPriceExamineDTO;
import com.topideal.entity.dto.SettlementPriceWarnconfigDTO;
import com.topideal.entity.vo.reporting.SettlementPriceModel;

/**
 * 结算价格 dao
 * @author lian_
 */
public interface SettlementPriceDao extends BaseDao<SettlementPriceModel> {
	/**
	 * 根据商家+商品条码查询存在的记录(剔除修改记录中的id)
	 * @param merchantId
	 * @param barcode
	 * @param ids
	 * @return
	 */
	List<SettlementPriceModel> getListNotInIds(Long merchantId, String barcode, String ids);

	
	public SettlementPriceModel getPriceOne(Map<String, Object> paramMap);


	/**
     * 根据条件查询
     * @return
     */
	List<SettlementPriceDTO> queryList(SettlementPriceDTO dto);


	/**
	 * 根据IDS获取
	 * @param list
	 * @return
	 */
	List<SettlementPriceModel> searchByIds(List list);


	/**
	 * 查询分页信息和总数
	 * @param model
	 * @return
	 */
	List<SettlementPriceExamineDTO> listExamineList(SettlementPriceExamineDTO model);


	Integer countExamineList(SettlementPriceExamineDTO model);


	SettlementPriceDTO listPriceDTO(SettlementPriceDTO dto);


	List<SettlementPriceDTO> getListByEffectiveDate(Map<String, Object> map);





}
