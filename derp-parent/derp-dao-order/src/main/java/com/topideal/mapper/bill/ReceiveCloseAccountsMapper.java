package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.ReceiveCloseAccountsDTO;
import com.topideal.entity.vo.bill.ReceiveCloseAccountsModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface ReceiveCloseAccountsMapper extends BaseMapper<ReceiveCloseAccountsModel> {

    ReceiveCloseAccountsModel getLatestByModel(ReceiveCloseAccountsModel model);

    PageDataModel<ReceiveCloseAccountsDTO> listDTOByPage(ReceiveCloseAccountsDTO dto);

    ReceiveCloseAccountsDTO getLatestExcludeIdByMap(Map<String, Object> param);

    int updateStatusByMap(Map<String, Object> param);

    void batchSave(List<ReceiveCloseAccountsModel> modelList);
}
