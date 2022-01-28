package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.ReceiveCloseAccountsDTO;
import com.topideal.entity.vo.bill.ReceiveCloseAccountsModel;

import java.util.List;
import java.util.Map;


public interface ReceiveCloseAccountsDao extends BaseDao<ReceiveCloseAccountsModel> {


    /**
     * 获取最近的记录
     * @param model
     * @return
     */
    ReceiveCloseAccountsModel getLatestByModel(ReceiveCloseAccountsModel model);

    ReceiveCloseAccountsDTO listDTOByPage(ReceiveCloseAccountsDTO dto);

    /**
     * 获取除自己外最近的记录
     * @param param
     * @return
     */
    ReceiveCloseAccountsDTO getLatestExcludeIdByMap(Map<String, Object> param);

    /**
     * 根据Map更新状态
     * @param param
     */
    int updateStatusByMap(Map<String, Object> param);

    /**
     * 批量新增
     * @param modelList
     */
    void batchSave(List<ReceiveCloseAccountsModel> modelList);
}
