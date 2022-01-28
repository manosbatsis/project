package com.topideal.report.service.automatic.impl;

import com.topideal.dao.automatic.BusinessFinanceAutomaticVerificationDao;
import com.topideal.dao.automatic.BusinessFinanceAutomaticVerificationItemDao;
import com.topideal.entity.dto.BusinessFinanceAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.BusinessFinanceAutomaticVerificationItemModel;
import com.topideal.report.service.automatic.BusinessFinanceAutoVeriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * @Description: 业财自核对 service 实现
 * @Author: Chen Yiluan
 * @Date: 2020/05/19 15:01
 **/
@Service
public class BusinessFinanceAutoVeriServiceImpl implements BusinessFinanceAutoVeriService {

    @Autowired
    private BusinessFinanceAutomaticVerificationDao businessFinanceAutomaticVerificationDao;
    @Autowired
    private BusinessFinanceAutomaticVerificationItemDao businessFinanceAutomaticVerificationItemDao;

    @Override
    public BusinessFinanceAutomaticVerificationDTO listAutomaticVeriByPage(BusinessFinanceAutomaticVerificationDTO dto) throws SQLException {
        BusinessFinanceAutomaticVerificationDTO resultDto =  businessFinanceAutomaticVerificationDao.listAutomaticVeriByPage(dto);
        /*if (resultDto != null) {
            List<BusinessFinanceAutomaticVerificationDTO> dtoList = resultDto.getList();
            for (BusinessFinanceAutomaticVerificationDTO verificationDTO : dtoList) {
                BusinessFinanceAutomaticVerificationModel model = new BusinessFinanceAutomaticVerificationModel();
                model.setMonth(dto.getMonth());
                model.setMerchantId(dto.getMerchantId());
                model.setStatus(DERP_REPORT.BUSINESSFINANCEAUTOMATICVERIFICATION_STATUS_0);
                List<BusinessFinanceAutomaticVerificationModel> list = businessFinanceAutomaticVerificationDao.list(model);
                if (list == null || list.size() == 0) {
                    verificationDTO.setStatus(DERP_REPORT.BUSINESSFINANCEAUTOMATICVERIFICATION_STATUS_1);
                } else {
                    verificationDTO.setStatus(DERP_REPORT.BUSINESSFINANCEAUTOMATICVERIFICATION_STATUS_0);
                }
            }
        }*/

        return resultDto;
    }

    @Override
    public List<BusinessFinanceAutomaticVerificationItemModel> listForExport(BusinessFinanceAutomaticVerificationDTO dto) {
        BusinessFinanceAutomaticVerificationItemModel itemModel = new BusinessFinanceAutomaticVerificationItemModel();
        itemModel.setMerchantId(dto.getMerchantId());
        itemModel.setMonth(dto.getMonth());
        return businessFinanceAutomaticVerificationItemDao.listForExport(itemModel);
    }

    @Override
    public void updateAutomaticVeri(BusinessFinanceAutomaticVerificationDTO dto) throws SQLException {
        businessFinanceAutomaticVerificationDao.updateAutomaticVeri(dto);
    }

    /**
     * 比较数字是否相等
     */
    private boolean isSameNum(List<Integer> nums) {
        for (int i = 1; i < nums.size(); i++) {
            if (nums.get(i).intValue() != nums.get(0).intValue()) {
                return false;
            }
        }
        return true;
    }
}
