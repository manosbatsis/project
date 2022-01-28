package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.TocTempCostBillItemMonthlyDTO;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillCostItemMonthlyModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface TocTemporaryReceiveBillCostItemMonthlyMapper extends BaseMapper<TocTemporaryReceiveBillCostItemMonthlyModel> {


    PageDataModel<TocTempCostBillItemMonthlyDTO> listDTOByPage(TocTempCostBillItemMonthlyDTO dto);

    void deleteByModel(TocTemporaryReceiveBillCostItemMonthlyModel model);

    void batchSaveByBillItemList(List<TocTemporaryReceiveBillCostItemMonthlyModel> itemList);

    void deleteByDTO(TocTempCostBillItemMonthlyDTO dto);

    int countByDTO(TocTempCostBillItemMonthlyDTO dto);

    List<Map<String, Object>> listSumForExport(TocTempCostBillItemMonthlyDTO dto);

    List<Map<String, Object>> listForMapItem(TocTempCostBillItemMonthlyDTO dto);

    /**
     * 按照“公司+事业部+客户+销售币种+月份”维度汇总 未核销金额
     * @param dto
     * @return
     */
    List<TocTempCostBillItemMonthlyDTO> getMonthlyNonVerify(TocTempCostBillItemMonthlyDTO dto);
}
