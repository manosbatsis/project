package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.TocTempReceiveBillItemMonthlyDTO;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillItemMonthlyModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface TocTemporaryReceiveBillItemMonthlyMapper extends BaseMapper<TocTemporaryReceiveBillItemMonthlyModel> {


    PageDataModel<TocTempReceiveBillItemMonthlyDTO> listDTOByPage(TocTempReceiveBillItemMonthlyDTO dto);

    void deleteByModel(TocTemporaryReceiveBillItemMonthlyModel model);

    void batchSaveByBillItemList(List<TocTemporaryReceiveBillItemMonthlyModel> itemList);

    void deleteByDTO(TocTempReceiveBillItemMonthlyDTO dto);

    int countByDTO(TocTempReceiveBillItemMonthlyDTO dto);

    List<Map<String, Object>> listForMapItem(TocTempReceiveBillItemMonthlyDTO dto);

    List<Map<String, Object>> listSumForExport(TocTempReceiveBillItemMonthlyDTO dto);

    /**
     * 按照“公司+事业部+客户+销售币种+月份”维度汇总 未核销金额
     * @param dto
     * @return
     */
    List<TocTempReceiveBillItemMonthlyDTO> getMonthlyNonVerify(TocTempReceiveBillItemMonthlyDTO dto);
}
