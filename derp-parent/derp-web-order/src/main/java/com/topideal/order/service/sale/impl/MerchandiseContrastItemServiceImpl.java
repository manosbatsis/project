package com.topideal.order.service.sale.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.MerchandiseContrastDao;
import com.topideal.dao.sale.MerchandiseContrastItemDao;
import com.topideal.entity.dto.sale.MerchandiseContrastDTO;
import com.topideal.entity.dto.sale.MerchandiseContrastItemDTO;
import com.topideal.entity.vo.sale.MerchandiseContrastItemModel;
import com.topideal.entity.vo.sale.MerchandiseContrastModel;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.order.service.sale.MerchandiseContrastItemService;
import com.topideal.order.service.sale.MerchandiseContrastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 爬虫商品对照表体
 * @author tsh_
 */
@Service
public class MerchandiseContrastItemServiceImpl implements MerchandiseContrastItemService {

    @Autowired
    private MerchandiseContrastItemDao merchandiseContrastItemDao;


    @Override
    public List<MerchandiseContrastItemModel> searchDetail(MerchandiseContrastItemModel contrastItemModel)  throws SQLException {
        return merchandiseContrastItemDao.list(contrastItemModel);
    }

    @Override
    public List<MerchandiseContrastItemDTO> getContrastItemByContrastId(MerchandiseContrastItemModel contrastItemModel) {
        return merchandiseContrastItemDao.getContrastItemByContrastId(contrastItemModel);
    }
}
