package com.topideal.report.service.reporting.impl;

import com.topideal.common.system.auth.User;
import com.topideal.dao.reporting.BuLocationSummaryDao;
import com.topideal.entity.dto.BuLocationSummaryDTO;
import com.topideal.report.service.reporting.BuLocationSummaryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/18 16:19
 * @Description:
 */
@Service
public class BuLocationSummaryServiceImpl implements BuLocationSummaryService {

    private static final Logger LOG = Logger.getLogger(BuLocationSummaryServiceImpl.class);

    @Autowired
    private BuLocationSummaryDao dao;

    @Override
    public BuLocationSummaryDTO listByPage(User user, BuLocationSummaryDTO dto) {
        if(dto == null) {
            return null;
        }
        return dao.listDTOByPage(dto);
    }
}
