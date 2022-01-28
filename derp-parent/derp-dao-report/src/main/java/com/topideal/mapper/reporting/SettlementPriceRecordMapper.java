package com.topideal.mapper.reporting;

import java.math.BigDecimal;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.SettlementPriceRecordDTO;
import com.topideal.entity.vo.reporting.SettlementPriceRecordModel;
import com.topideal.mapper.BaseMapper;

/**
 * 结算价格记录表 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface SettlementPriceRecordMapper extends BaseMapper<SettlementPriceRecordModel> {

  /**查询商家、货号生效日期内的结算价格
   */
  public SettlementPriceRecordModel getBarcodePrice(Map<String, Object> map);
  /**查询事业部商家、货号生效日期内的结算价格
   */
  public SettlementPriceRecordModel getBuBarcodePrice(Map<String, Object> map);

  public SettlementPriceRecordModel getLatestItem(SettlementPriceRecordModel spRecordModel);

  /**
   *  获取历史最近生效价格
   * @param temp
   * @return
   */
  public BigDecimal getHistoryPrice(SettlementPriceRecordModel temp);
  
  
  public PageDataModel<SettlementPriceRecordDTO> getListByPage(SettlementPriceRecordDTO dto);
  


}
