package com.topideal.mapper.bill;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.TocTemporaryCostBillDTO;
import com.topideal.entity.vo.bill.TocTemporaryCostBillModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface TocTemporaryCostBillMapper extends BaseMapper<TocTemporaryCostBillModel> {

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    PageDataModel<TocTemporaryCostBillDTO> listTocTempCostReceiveBillByPage(TocTemporaryCostBillDTO dto) throws SQLException;


    List<TocTemporaryCostBillDTO> listForExport(TocTemporaryCostBillDTO dto);

    Integer deleteByModel(TocTemporaryCostBillModel model) throws SQLException;

    TocTemporaryCostBillDTO getDTO(TocTemporaryCostBillDTO dto);

    /**
     * 据商家+客户+事业部+币种 汇总暂估费用金额
     * @param queryMap
     * @return
     * @throws SQLException
     */
    List<TocTemporaryCostBillDTO> getTocTemporaryCostList(Map<String, Object> queryMap) throws SQLException;

    /**
     * 获取ID List
     * @param alreadyModel
     * @return
     */
    List<Long> searchIdListByModel(TocTemporaryCostBillModel alreadyModel);
}
