package com.topideal.order.service.sale.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.auth.User;
import com.topideal.dao.sale.CutomerQuotaWarningDao;
import com.topideal.dao.sale.CutomerQuotaWarningItemDao;
import com.topideal.entity.dto.sale.CutomerQuotaWarningDTO;
import com.topideal.entity.dto.sale.CutomerQuotaWarningItemDTO;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.UserBuRelMongo;
import com.topideal.order.service.sale.CutomerQuotaWarningService;

@Service
public class CutomerQuotaWarningServiceImpl implements CutomerQuotaWarningService {
    @Autowired
    private CutomerQuotaWarningDao cutomerQuotaWarningDao;
    @Autowired
    private CutomerQuotaWarningItemDao cutomerQuotaWarningItemDao;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao ;

    /**
     * 获取分页列表
     * @param dto
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public CutomerQuotaWarningDTO listDTOByPage(CutomerQuotaWarningDTO dto, User user) throws Exception {
        if (dto.getBuId() == null) {
            List<Long> buList = new ArrayList<Long>();
            Map<String, Object> queryMap = new HashMap<>() ;
            queryMap.put("userId", user.getId()) ;
            List<UserBuRelMongo> userBuRelMongoList = userBuRelMongoDao.findAll(queryMap);
            if(userBuRelMongoList != null && userBuRelMongoList.size() > 0) {
                for(UserBuRelMongo mongo : userBuRelMongoList) {
                    buList.add(mongo.getBuId());
                }
            }

            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }
        return cutomerQuotaWarningDao.listDTOByPage(dto);
    }

    /**
     * 获取表体列表分页
     * @param dto
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public CutomerQuotaWarningItemDTO getItemListByPage(CutomerQuotaWarningItemDTO dto,User user) throws Exception {

        return cutomerQuotaWarningItemDao.listDTOByPage(dto);
    }

    /**
     * 获取列表 不分页
     * @param dto
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public List<CutomerQuotaWarningDTO> listCutomerQuotaWarning(CutomerQuotaWarningDTO dto,User user) throws Exception {
        if (dto.getBuId() == null) {
            List<Long> buList = new ArrayList<Long>();
            Map<String, Object> queryMap = new HashMap<>() ;
            queryMap.put("userId", user.getId()) ;
            List<UserBuRelMongo> userBuRelMongoList = userBuRelMongoDao.findAll(queryMap);
            if(userBuRelMongoList != null && userBuRelMongoList.size() > 0) {
                for(UserBuRelMongo mongo : userBuRelMongoList) {
                    buList.add(mongo.getBuId());
                }
            }
            if (buList.isEmpty()) {
                return new ArrayList<CutomerQuotaWarningDTO>();
            }
            dto.setBuList(buList);
        }
        return cutomerQuotaWarningDao.listDTO(dto);
    }
}
