package com.topideal.service.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.base.BrandDao;
import com.topideal.dao.main.CommbarcodeDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.service.UpdateCommbarcodeBrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 维护标准条码中的标准品牌信息——定时器
 * @author
 */
@Service
public class UpdateCommbarcodeBrandServiceImpl implements UpdateCommbarcodeBrandService {
    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateCommbarcodeBrandServiceImpl.class);

    @Autowired
    private CommbarcodeDao commbarcodeDao;
    @Autowired
    private MerchandiseInfoDao merchandiseInfoDao;
    @Autowired
    private BrandDao brandDao;

    @Override
    @SystemServiceLog(point= DERP_LOG_POINT.POINT_13201700001,model=DERP_LOG_POINT.POINT_13201700001_Label)
    public void UpdateCommbarcodeBrand(String json, String keys, String topics, String tags) throws SQLException {

        Map<Long, BrandModel> brandModelMap = new HashMap<>();
        //查找标准条码管理中状态为未维护的记录
        CommbarcodeModel commbarcodeModel = new CommbarcodeModel();
        commbarcodeModel.setMaintainStatus(DERP_SYS.COMMBARCODE_MAINTAINSTATUS_0);
        List<CommbarcodeModel> commbarcodeModelList = commbarcodeDao.list(commbarcodeModel);
        List<CommbarcodeModel> updateCommbarcodeList = new ArrayList<>();
        //获取该标准条码关联的商品的品牌，若有多个品牌则全部获取
        for (CommbarcodeModel commbarcode : commbarcodeModelList) {
            List<Long> brandIdList = new ArrayList<>();
            MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel();
            merchandiseInfoModel.setCommbarcode(commbarcode.getCommbarcode());
            merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);
            List<MerchandiseInfoModel> merchandiseInfoModelList = merchandiseInfoDao.list(merchandiseInfoModel);
            for (MerchandiseInfoModel merchandiseInfo : merchandiseInfoModelList) {
                if (merchandiseInfo.getBrandId() != null) {
                    brandIdList.add(merchandiseInfo.getBrandId());
                }
            }

            //在品牌列表中查询对应的标准品牌
            for (Long brandId : brandIdList) {

                BrandModel brandModel = brandModelMap.get(brandId);
                if (brandModel == null) {
                    brandModel = brandDao.searchById(brandId);
                }

                if (brandModel != null) {
                    brandModelMap.put(brandId, brandModel);
                }

                if (brandModel.getParentId() != null) {
                    CommbarcodeModel updateCommbarcode = new CommbarcodeModel();
                    updateCommbarcode.setId(commbarcode.getId());
                    updateCommbarcode.setCommBrandParentId(brandModel.getParentId());
                    updateCommbarcode.setCommBrandParentName(brandModel.getParentName());
                    updateCommbarcode.setMaintainStatus(DERP_SYS.COMMBARCODE_MAINTAINSTATUS_1);
                    updateCommbarcodeList.add(updateCommbarcode);
                    break;
                }
            }
        }

        //批量更新：将获取到的标准品牌更新到标准条码里，更新维护状态为已维护
        int pageSize = 1000;
        for (int i = 0; i < updateCommbarcodeList.size(); ) {
            int pageSub = (i+pageSize) < updateCommbarcodeList.size() ? (i+pageSize) : updateCommbarcodeList.size();
            commbarcodeDao.batchUpdateCommbarcode(updateCommbarcodeList.subList(i, pageSub));
            i = pageSub;
        }
    }
}
