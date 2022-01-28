package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.BillMonthlySnapshotDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillDTO;
import com.topideal.entity.vo.bill.BillMonthlySnapshotModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface BillMonthlySnapshotMapper extends BaseMapper<BillMonthlySnapshotModel> {


    /**
     * 根据条件删除月结快照
     * @param billMonthlySnapshotModel
     * @return
     * @throws SQLException
     */
    int deleteByModel(BillMonthlySnapshotModel billMonthlySnapshotModel) throws SQLException;

    /**
     * 批量插入月结快照
     * */
    Integer batchSave(List<BillMonthlySnapshotModel> list) throws SQLException;

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    PageDataModel<BillMonthlySnapshotDTO> listBillMonthlySnapshotByPage(BillMonthlySnapshotDTO dto) throws SQLException;

    /**
     * 按照“公司+事业部+客户+销售币种+月结月份”维度汇总未核金额；
     * @param dto
     * @return
     * @throws SQLException
     */
    List<BillMonthlySnapshotDTO> getMonthlyNonVerify(BillMonthlySnapshotDTO dto) throws SQLException;

    List<BillMonthlySnapshotDTO> listByDTO(BillMonthlySnapshotDTO dto) throws SQLException;

}
