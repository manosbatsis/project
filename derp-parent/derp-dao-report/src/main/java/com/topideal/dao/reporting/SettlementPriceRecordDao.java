package com.topideal.dao.reporting;

import java.math.BigDecimal;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.SettlementPriceRecordDTO;
import com.topideal.entity.vo.reporting.SettlementPriceRecordModel;

/**
 * 结算价格记录表 dao
 * @author lian_
 *
 */
public interface SettlementPriceRecordDao extends BaseDao<SettlementPriceRecordModel> {


	/**查询商家、货号生效日期内的结算价格
	 */
	public SettlementPriceRecordModel getBarcodePrice(Map<String, Object> map);
	/**查询 事业部商家、货号生效日期内的结算价格
	 */
	public SettlementPriceRecordModel getBuBarcodePrice(Map<String, Object> map);

	/**
	 * 获取最新的item
	 * @param spRecordModel
	 * @return
	 */
	public SettlementPriceRecordModel getLatestItem(SettlementPriceRecordModel spRecordModel);

	/**
	 *  获取最近生效价格
	 * @param temp
	 * @return
	 */
	public BigDecimal getHistoryPrice(SettlementPriceRecordModel temp);
	
	
	public SettlementPriceRecordDTO listRecordPriceDTO(SettlementPriceRecordDTO dto);

}
