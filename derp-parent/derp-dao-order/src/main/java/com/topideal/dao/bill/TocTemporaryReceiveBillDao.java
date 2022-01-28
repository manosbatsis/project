package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillDTO;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillModel;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface TocTemporaryReceiveBillDao extends BaseDao<TocTemporaryReceiveBillModel> {


    Integer batchSave(List<TocTemporaryReceiveBillModel> list) throws SQLException;

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    TocTemporaryReceiveBillDTO listTocTempReceiveBillByPage(TocTemporaryReceiveBillDTO dto) throws SQLException;

    Integer deleteByModel(TocTemporaryReceiveBillModel model) throws SQLException;

    TocTemporaryReceiveBillDTO searchDTOById(Long id);

    List<TocTemporaryReceiveBillDTO> listForExport(TocTemporaryReceiveBillDTO dto);

    int updateWithNull(TocTemporaryReceiveBillModel model) throws SQLException;

    /**
     * （应收账龄报告）
     * @param queryMap
     * @return
     * @throws SQLException
     */
    Map<String, BigDecimal> getByToCReceive(Map<String, Object> queryMap) throws SQLException;


    /**
     * 根据商家+事业部+客户+币种以及月份的维度获取未核销的金额
     * @param queryMap
     * @return
     */
    List<Map<String,Object>> getItemSearchList(Map<String,Object> queryMap);

    /**
     * 应收账龄报告（POP、一件代发）
     * @param queryMap
     * @return
     */
    List<TocTemporaryReceiveBillDTO> listBySearchQuery(Map<String,Object> queryMap);

    List<Long> searchIdListByModel(TocTemporaryReceiveBillModel alreadyModel);
}
