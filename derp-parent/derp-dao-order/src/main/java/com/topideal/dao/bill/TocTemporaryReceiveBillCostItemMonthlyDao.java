package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.TocTempCostBillItemMonthlyDTO;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillCostItemMonthlyModel;

import java.util.List;
import java.util.Map;


public interface TocTemporaryReceiveBillCostItemMonthlyDao extends BaseDao<TocTemporaryReceiveBillCostItemMonthlyModel> {


    TocTempCostBillItemMonthlyDTO listDTOByPage(TocTempCostBillItemMonthlyDTO dto);

    void deleteByModel(TocTemporaryReceiveBillCostItemMonthlyModel model);

    void batchSaveByBillItemList(List<TocTemporaryReceiveBillCostItemMonthlyModel> itemListPage);

    void deleteByDTO(TocTempCostBillItemMonthlyDTO dto);

    int countByDTO(TocTempCostBillItemMonthlyDTO dto);

    /**
     * 汇总数据
     * @param dto
     * @return
     */
    List<Map<String, Object>> listSumForExport(TocTempCostBillItemMonthlyDTO dto);

    /**
     * 明细数据
     * @param dto
     * @return
     */
    List<Map<String, Object>> listForMapItem(TocTempCostBillItemMonthlyDTO dto);

    /**
     * 按照“公司+事业部+客户+销售币种+月份”维度汇总 未核销金额
     * @param dto
     * @return
     */
    List<TocTempCostBillItemMonthlyDTO> getMonthlyNonVerify(TocTempCostBillItemMonthlyDTO dto);
}
