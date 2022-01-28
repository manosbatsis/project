package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.TocTemporaryCostBillDTO;
import com.topideal.entity.vo.bill.TocTemporaryCostBillModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface TocTemporaryCostBillDao extends BaseDao<TocTemporaryCostBillModel> {


    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    TocTemporaryCostBillDTO listTocTempCostReceiveBillByPage(TocTemporaryCostBillDTO dto) throws SQLException;



    List<TocTemporaryCostBillDTO> listForExport(TocTemporaryCostBillDTO dto);

    Integer deleteByModel(TocTemporaryCostBillModel model) throws SQLException;

    TocTemporaryCostBillDTO searchDTOById(Long id);


    /**
     * 根据商家+客户+事业部+币种 汇总暂估费用金额
     * @param queryMap
     * @return
     * @throws SQLException
     */
    List<TocTemporaryCostBillDTO> getTocTemporaryCostList(Map<String, Object> queryMap) throws SQLException;

    /**
     * 获取主键ID List
     * @param alreadyModel
     * @return
     */
    List<Long> searchIdListByModel(TocTemporaryCostBillModel alreadyModel);
}
