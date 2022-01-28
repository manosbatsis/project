package com.topideal.mapper.automatic;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.AutomaticCheckTaskDTO;
import com.topideal.entity.dto.BusinessFinanceAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.BusinessFinanceAutomaticVerificationModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface BusinessFinanceAutomaticVerificationMapper extends BaseMapper<BusinessFinanceAutomaticVerificationModel> {

    PageDataModel<BusinessFinanceAutomaticVerificationDTO> listAutomaticVeriByPage(BusinessFinanceAutomaticVerificationDTO dto) throws SQLException;

    void deleteByMap(Map<String, Object> params);

    /**
     * 置空当前刷新报表
     */
    void updateAutomaticVeri(BusinessFinanceAutomaticVerificationDTO dto) throws SQLException;

}
