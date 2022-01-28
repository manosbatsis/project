package com.topideal.order.service.bill.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.dao.bill.TocTemporaryReceiveBillCostItemDao;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillCostItemDTO;
import com.topideal.order.service.bill.ToCTempCostDiffOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/1 15:27
 * @Description: 费用差异调整单
 */
@Service
public class ToCTempCostDiffOrderServiceImpl implements ToCTempCostDiffOrderService {

    @Autowired
    private TocTemporaryReceiveBillCostItemDao tocTemporaryReceiveBillCostItemDao;

    @Override
    public TocTemporaryReceiveBillCostItemDTO listCostDiffOrder(User user, TocTemporaryReceiveBillCostItemDTO dto) throws Exception {
        dto.setOrderType(DERP_ORDER.TOCTEMPITEMBILL_ORDERTYPE_2);
        TocTemporaryReceiveBillCostItemDTO listByPage = tocTemporaryReceiveBillCostItemDao.getCostDiffListByPage(dto);
        List<TocTemporaryReceiveBillCostItemDTO> list = listByPage.getList();
        list.stream().forEach(eneity -> {
            if(StringUtils.isNotBlank(eneity.getOrderType())) {
                eneity.setOrderTypeLabel(DERP_ORDER.getLabelByKey(DERP_ORDER.tocTempItemCostBill_orderTypeList, eneity.getOrderType()));
            }
        });
        return listByPage;
    }
}
