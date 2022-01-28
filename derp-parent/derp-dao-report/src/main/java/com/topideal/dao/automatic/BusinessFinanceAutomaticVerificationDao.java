package com.topideal.dao.automatic;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.BusinessFinanceAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.BusinessFinanceAutomaticVerificationModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface BusinessFinanceAutomaticVerificationDao extends BaseDao<BusinessFinanceAutomaticVerificationModel> {

    BusinessFinanceAutomaticVerificationDTO listAutomaticVeriByPage(BusinessFinanceAutomaticVerificationDTO dto) throws SQLException;

    /**
     * 根据条件删除业财自核对表数据
     */
    void deleteByMap(Map<String, Object> params);

    /**
     * 置空当前刷新报表
     */
    void updateAutomaticVeri(BusinessFinanceAutomaticVerificationDTO dto) throws SQLException;





}
