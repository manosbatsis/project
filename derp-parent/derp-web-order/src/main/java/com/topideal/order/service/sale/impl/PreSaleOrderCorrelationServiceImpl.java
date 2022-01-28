package com.topideal.order.service.sale.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.auth.User;
import com.topideal.dao.sale.PreSaleOrderCorrelationDao;
import com.topideal.entity.dto.sale.PreSaleOrderCorrelationDTO;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.order.service.sale.PreSaleOrderCorrelationService;

/**
 * @Description: 预售勾稽明细service实现
 * @Date: 2020/06/02 15:57
 **/
@Service
public class PreSaleOrderCorrelationServiceImpl implements PreSaleOrderCorrelationService {

    @Autowired
    private PreSaleOrderCorrelationDao preSaleOrderCorrelationDao;
    // 用户事业部
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;

    @Override
    public PreSaleOrderCorrelationDTO listPreSaleOrderCorrelationByPage(PreSaleOrderCorrelationDTO dto,User user) throws SQLException {
        if(dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if(buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }
        PreSaleOrderCorrelationDTO preSaleOrderCorrelationDTO = preSaleOrderCorrelationDao.listPreSaleOrderCorrelationByPage(dto);
        int total = preSaleOrderCorrelationDao.getTotal(dto);
        preSaleOrderCorrelationDTO.setTotal(total);
        return preSaleOrderCorrelationDTO;
    }

    @Override
    public PreSaleOrderCorrelationDTO detailPreSaleOrderCorrelation(PreSaleOrderCorrelationDTO dto) throws SQLException {
        return preSaleOrderCorrelationDao.detail(dto);
    }

    @Override
    public List<PreSaleOrderCorrelationDTO> detailByCodeAndGoodsNo(PreSaleOrderCorrelationDTO dto) throws SQLException {
        return preSaleOrderCorrelationDao.detailByCodeAndGoodsNo(dto);
    }

    @Override
    public List<Map<String, Object>> detailForExport(PreSaleOrderCorrelationDTO dto,User user) throws SQLException {
        if(dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            if(buIds.isEmpty()) {
                return new ArrayList<>();
            }
            dto.setBuList(buIds);
        }
        return preSaleOrderCorrelationDao.detailForExport(dto);
    }

    @Override
    public List<Map<String, Object>> listForExport(PreSaleOrderCorrelationDTO dto,User user) throws SQLException {
        if(dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            if(buIds.isEmpty()) {
                return new ArrayList<>();
            }
            dto.setBuList(buIds);
        }
        List<Map<String, Object>> listForExport = preSaleOrderCorrelationDao.listForExport(dto);
        for (Map<String, Object> map : listForExport) {
            BigDecimal preNum = (BigDecimal) map.get("pre_num");
            BigDecimal saleNum = (BigDecimal) map.get("sale_num");
            BigDecimal outNum = (BigDecimal) map.get("out_num");
            Integer preNumInt = (preNum == null) ? 0 : preNum.intValue();
            Integer saleNumInt = (saleNum == null) ? 0 : saleNum.intValue();
            Integer outNumInt = (outNum == null) ? 0 : outNum.intValue();
            map.put("pre_num", preNumInt);
            map.put("sale_num", saleNumInt);
            map.put("out_num", outNumInt);
            map.put("pre_sale_num", preNumInt - saleNumInt);
            map.put("surplus_num", preNumInt - outNumInt);
        }
        return listForExport;
    }
}
