package com.topideal.report.service.automatic;

import com.topideal.entity.dto.BusinessFinanceAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.BusinessFinanceAutomaticVerificationItemModel;
import org.apache.ibatis.jdbc.SQL;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 业财自核对service
 * @Author: Chen Yiluan
 * @Date: 2020-05-19 14:59
 **/
public interface BusinessFinanceAutoVeriService {

    BusinessFinanceAutomaticVerificationDTO listAutomaticVeriByPage(BusinessFinanceAutomaticVerificationDTO dto) throws SQLException;

    /**
     * 根据条件查询导出
     */
    List<BusinessFinanceAutomaticVerificationItemModel> listForExport(BusinessFinanceAutomaticVerificationDTO dto);

    void updateAutomaticVeri(BusinessFinanceAutomaticVerificationDTO dto) throws SQLException;

}
