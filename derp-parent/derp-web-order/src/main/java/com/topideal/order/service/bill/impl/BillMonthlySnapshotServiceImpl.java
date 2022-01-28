package com.topideal.order.service.bill.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.dao.bill.BillMonthlySnapshotDao;
import com.topideal.entity.dto.bill.BillMonthlySnapshotDTO;
import com.topideal.entity.vo.bill.BillMonthlySnapshotModel;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.order.service.bill.BillMonthlySnapshotService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BillMonthlySnapshotServiceImpl implements BillMonthlySnapshotService  {

    @Autowired
    private BillMonthlySnapshotDao billMonthlySnapshotDao;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;

    @Override
    public BillMonthlySnapshotDTO listBillMonthlySnapshotByPage(BillMonthlySnapshotDTO dto, User user) throws Exception {
        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }
        return billMonthlySnapshotDao.listBillMonthlySnapshotByPage(dto);
    }

    @Override
    public List<Map<String, Object>> listForExport(BillMonthlySnapshotDTO dto, User user) throws Exception {
        List<Map<String, Object>> mapList = new ArrayList<>();
        if(dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            //关联ID为空时，返回空列表
            if(buIds.isEmpty()) {
               return mapList;
            }

            dto.setBuList(buIds);
        }

        List<BillMonthlySnapshotDTO> billMonthlySnapshotDTOS = billMonthlySnapshotDao.listByDTO(dto);

        for (BillMonthlySnapshotDTO billMonthlySnapshotDTO : billMonthlySnapshotDTOS) {
            Map<String, Object> map = new HashMap<>();
            map.put("receiveCode", billMonthlySnapshotDTO.getReceiveCode());
            map.put("billType", DERP_ORDER.getLabelByKey(DERP_ORDER.billMonthlySnapshot_billTypeList, billMonthlySnapshotDTO.getBillType()));
            map.put("buName", billMonthlySnapshotDTO.getBuName());
            map.put("invoiceNo", billMonthlySnapshotDTO.getInvoiceNo());
            map.put("creditMonth", billMonthlySnapshotDTO.getCreditMonth());
            map.put("customerName", billMonthlySnapshotDTO.getCustomerName());
            map.put("storePlatformName", DERP.getLabelByKey(DERP.storePlatformList, billMonthlySnapshotDTO.getStorePlatformCode()));
            map.put("receivableAmount", billMonthlySnapshotDTO.getReceivableAmount());
            map.put("nonverifyAmount", billMonthlySnapshotDTO.getNonverifyAmount());
            map.put("currency", DERP.getLabelByKey(DERP.currencyCodeList, billMonthlySnapshotDTO.getCurrency()));
            map.put("invoiceDate", billMonthlySnapshotDTO.getInvoiceDate());
            mapList.add(map);
        }
        return mapList;
    }
}
