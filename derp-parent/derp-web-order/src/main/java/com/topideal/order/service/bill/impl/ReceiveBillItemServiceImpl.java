package com.topideal.order.service.bill.impl;

import com.topideal.dao.bill.ReceiveBillItemDao;
import com.topideal.entity.dto.bill.ReceiveBillItemDTO;
import com.topideal.entity.vo.bill.ReceiveBillItemModel;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.order.service.bill.ReceiveBillItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 应收账单service实现类
 * @Author: Chen Yiluan
 * @Date: 2020/07/27 15:01
 **/
@Service
public class ReceiveBillItemServiceImpl implements ReceiveBillItemService {

    @Autowired
    private ReceiveBillItemDao receiveBillItemDao;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;

    @Override
    public List<ReceiveBillItemDTO> itemListGroupByBillId(List<Long> billIds) throws SQLException {
        List<ReceiveBillItemDTO> itemModels =  receiveBillItemDao.itemListGroupByBillId(billIds);
        for (ReceiveBillItemDTO receiveBillItemDto : itemModels) {
            Map<String, Object> merchandiseParam = new HashMap<>();
            if (receiveBillItemDto.getGoodsId() != null) {
                merchandiseParam.put("merchandiseId", receiveBillItemDto.getGoodsId());
                MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(merchandiseParam);
                receiveBillItemDto.setGoodsName(merchandiseInfoMogo.getName());
                receiveBillItemDto.setGoodsNo(merchandiseInfoMogo.getGoodsNo());
                receiveBillItemDto.setCommbarcode(merchandiseInfoMogo.getCommbarcode());
            }
        }
        return itemModels;
    }
}
