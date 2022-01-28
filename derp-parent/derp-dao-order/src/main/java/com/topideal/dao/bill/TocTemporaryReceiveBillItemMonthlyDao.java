package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.TocTempReceiveBillItemMonthlyDTO;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillItemMonthlyModel;

import java.util.List;
import java.util.Map;


public interface TocTemporaryReceiveBillItemMonthlyDao extends BaseDao<TocTemporaryReceiveBillItemMonthlyModel> {


    TocTempReceiveBillItemMonthlyDTO listDTOByPage(TocTempReceiveBillItemMonthlyDTO dto);

    /**
     * 删除
     * @param model
     */
    void deleteByModel(TocTemporaryReceiveBillItemMonthlyModel model);

    /**
     * 根据toc暂估收入List生成toc暂估月结数据
     * @param itemListPage
     */
    void batchSaveByBillItemList(List<TocTemporaryReceiveBillItemMonthlyModel> itemListPage);

    void deleteByDTO(TocTempReceiveBillItemMonthlyDTO dto);

    int countByDTO(TocTempReceiveBillItemMonthlyDTO dto);

    /**
     * 2C月结暂估收入明细
     * @param dto
     * @return
     */
    List<Map<String, Object>> listForMapItem(TocTempReceiveBillItemMonthlyDTO dto);

    /**
     * 2C月结暂估收入汇总
     * @param dto
     * @return
     */
    List<Map<String, Object>> listSumForExport(TocTempReceiveBillItemMonthlyDTO dto);

    /**
     * 按照“公司+事业部+客户+销售币种+月份”维度汇总 未核销金额
     * @param dto
     * @return
     */
    List<TocTempReceiveBillItemMonthlyDTO> getMonthlyNonVerify(TocTempReceiveBillItemMonthlyDTO dto);
}
