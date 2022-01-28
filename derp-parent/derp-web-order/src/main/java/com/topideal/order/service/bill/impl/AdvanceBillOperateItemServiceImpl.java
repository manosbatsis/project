package com.topideal.order.service.bill.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.dao.bill.AdvanceBillOperateItemDao;
import com.topideal.entity.dto.bill.AdvanceBillOperateItemDTO;
import com.topideal.order.service.bill.AdvanceBillOperateItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdvanceBillOperateItemServiceImpl implements AdvanceBillOperateItemService {

    @Autowired
    private AdvanceBillOperateItemDao advanceBillOperateItemDao;

    @Override
    public List<AdvanceBillOperateItemDTO> listAdvanceBillOperateItem(Long advanceId) {
        List<AdvanceBillOperateItemDTO> list=new ArrayList<>();
        if(advanceId==null){
            return list;
        }
        list=advanceBillOperateItemDao.getAdvanceById(advanceId);
        for(AdvanceBillOperateItemDTO operate:list){
            String operateType= DERP_ORDER.getLabelByKey(DERP_ORDER.advanceBill_typeList, operate.getOperateType());
            String operateResult=DERP_ORDER.getLabelByKey(DERP_ORDER.advanceBill_resultList, operate.getOperateResult());
            if(DERP_ORDER.ADVANCEBILL_TYPE_1.equals(operate.getOperateType())||DERP_ORDER.ADVANCEBILL_TYPE_3.equals(operate.getOperateType())){
                operate.setContent(operateType+":"+operateResult);
            }else{
                operate.setContent(operateType);
            }
        }
        return list;
    }
}
