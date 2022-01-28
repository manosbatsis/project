package com.topideal.service.main.impl;

import com.topideal.common.system.auth.User;
import com.topideal.dao.main.CostPriceDifferenceDao;
import com.topideal.entity.dto.main.CostPriceDifferenceDTO;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.service.main.CostPriceDifferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.CipherSpi;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
public class CostPriceDifferenceServiceImpl implements CostPriceDifferenceService {

    @Autowired
    private CostPriceDifferenceDao costPriceDifferenceDao;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;

    @Override
    public CostPriceDifferenceDTO listCostPriceDifferenceDTO(CostPriceDifferenceDTO dto, User user) throws SQLException {

        if (dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buIds.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buIds);
        }

        return costPriceDifferenceDao.queryDTOListByPage(dto);
    }

    @Override
    public List<CostPriceDifferenceDTO> getExportMainList(CostPriceDifferenceDTO dto, User user) throws SQLException {
        if (dto.getBuId() == null) {
            List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buIds.isEmpty()) {
                return new ArrayList<>();
            }
            dto.setBuList(buIds);
        }

        List<CostPriceDifferenceDTO> costPriceDifferenceDTOS = costPriceDifferenceDao.listByDTO(dto);

        for (CostPriceDifferenceDTO costPriceDifferenceDTO : costPriceDifferenceDTOS) {
            BigDecimal agio = costPriceDifferenceDTO.getPurchasePrice().subtract(costPriceDifferenceDTO.getFixedCost());
            costPriceDifferenceDTO.setAgio(String.valueOf(agio));
            costPriceDifferenceDTO.setFixedCostStr(String.valueOf(costPriceDifferenceDTO.getFixedCost()));
            costPriceDifferenceDTO.setPurchasePriceStr(String.valueOf(costPriceDifferenceDTO.getPurchasePrice()));
        }
        return costPriceDifferenceDTOS;
    }
}
