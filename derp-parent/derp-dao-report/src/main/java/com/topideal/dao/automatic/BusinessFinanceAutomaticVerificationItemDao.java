package com.topideal.dao.automatic;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.BusinessFinanceAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.BusinessFinanceAutomaticVerificationItemModel;

import java.util.List;
import java.util.Map;


public interface BusinessFinanceAutomaticVerificationItemDao extends BaseDao<BusinessFinanceAutomaticVerificationItemModel> {


    /**
     * 根据条件删除业财自核对表未对平商品信息数据
     */
    void deleteByMap(Map<String, Object> params);

    /**
     * 根据条件查询导出
     */
    List<BusinessFinanceAutomaticVerificationItemModel> listForExport(BusinessFinanceAutomaticVerificationItemModel model);






}