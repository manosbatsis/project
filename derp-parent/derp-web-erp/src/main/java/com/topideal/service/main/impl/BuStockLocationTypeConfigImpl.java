package com.topideal.service.main.impl;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.OperateSysLogDao;
import com.topideal.dao.main.BuStockLocationTypeConfigDao;
import com.topideal.dao.main.MerchantBuRelDao;
import com.topideal.entity.dto.main.BuStockLocationTypeConfigDTO;
import com.topideal.entity.dto.main.MerchantBuRelDTO;
import com.topideal.entity.vo.base.OperateSysLogModel;
import com.topideal.entity.vo.main.BuStockLocationTypeConfigModel;
import com.topideal.service.base.OperateSysLogService;
import com.topideal.service.main.BuStockLocationTypeConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/13 17:00
 * @Description: 事业部库位类型
 */
@Service
public class BuStockLocationTypeConfigImpl implements BuStockLocationTypeConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuStockLocationTypeConfigImpl.class);

    @Autowired
    private BuStockLocationTypeConfigDao dao;

    @Autowired
    private OperateSysLogService operateSysLogService;
    @Autowired
    private OperateSysLogDao operateSysLogDao;
    @Autowired
    private MerchantBuRelDao merchantBuRelDao;

    @Override
    public BuStockLocationTypeConfigDTO listByPage(User user, BuStockLocationTypeConfigDTO dto) {
        if(dto == null) {return null;}
        return dao.listDTOByPage(dto);
    }

    @Override
    public void addStockLocationType(User user, BuStockLocationTypeConfigDTO dto) throws Exception{
        if(dto == null) {return;}
        BuStockLocationTypeConfigModel searchModel = new BuStockLocationTypeConfigModel();
        searchModel.setMerchantId(dto.getMerchantId());
        searchModel.setBuId(dto.getBuId());
        searchModel.setName(dto.getName());
        searchModel = dao.searchByModel(searchModel);
        if(searchModel != null && searchModel.getId() != null) {
            throw new DerpException("该类型已存在");
        }

        BuStockLocationTypeConfigModel model = new BuStockLocationTypeConfigModel();
        BeanUtils.copyProperties(dto, model);

        //获取事业部信息
        MerchantBuRelDTO merchantBuRelDTO = new MerchantBuRelDTO();
        merchantBuRelDTO.setMerchantId(model.getMerchantId());
        merchantBuRelDTO.setBuId(model.getBuId());
        MerchantBuRelDTO merchantBuRel = merchantBuRelDao.getMerchantBuRel(merchantBuRelDTO);
        if(merchantBuRel == null) {
            throw new DerpException("事业部在该主体下未查到关联数据");
        }
        model.setStatus(DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
        model.setBuName(merchantBuRel.getBuName());
        model.setCreateDate(TimeUtils.getNow());
        Long id = dao.save(model);
        if(id == null) {
            throw new DerpException("新增库位类型失败");
        }

        // 记录操作日志
        OperateSysLogModel saveModel = new OperateSysLogModel() ;
        saveModel.setModule(DERP_SYS.OREARTE_SYS_LOG_7);
        saveModel.setCreateDate(TimeUtils.getNow());
        saveModel.setOperateDate(TimeUtils.getNow());
        saveModel.setOperateId(user.getId());
        saveModel.setOperater(user.getName());
        saveModel.setOperateRemark("");
        saveModel.setOperateResult("");
        saveModel.setOperateAction("新增");
        saveModel.setRelId(id);
        operateSysLogDao.save(saveModel);
    }

    @Override
    public void modifyStockLocationType(User user, BuStockLocationTypeConfigModel model) throws Exception{
        if(model == null || model.getId() == null) {
            throw new DerpException("数据异常");
        }
        model.setModifyDate(TimeUtils.getNow());
        int updateNum = dao.modify(model);
        if(updateNum <= 0) {
            throw new DerpException("更新失败");
        }

        // 记录操作日志
        OperateSysLogModel saveModel = new OperateSysLogModel() ;
        saveModel.setModule(DERP_SYS.OREARTE_SYS_LOG_7);
        saveModel.setCreateDate(TimeUtils.getNow());
        saveModel.setOperateDate(TimeUtils.getNow());
        saveModel.setOperateId(user.getId());
        saveModel.setOperater(user.getName());
        saveModel.setOperateRemark("");
        saveModel.setOperateResult("");
        saveModel.setOperateAction(DERP_SYS.getLabelByKey(DERP_SYS.bu_stock_location_type_config_statusList, model.getStatus()));
        saveModel.setRelId(model.getId());
        operateSysLogDao.save(saveModel);
    }

    @Override
    public List<SelectBean> getSelectBeanByBu(BuStockLocationTypeConfigDTO dto) throws Exception{
        List<SelectBean> result = new ArrayList<SelectBean>();
        BuStockLocationTypeConfigModel model = new BuStockLocationTypeConfigModel();
        BeanUtils.copyProperties(dto, model);
        model.setStatus(DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
        List<BuStockLocationTypeConfigModel> list = dao.list(model);
        if(list != null) {
            list.stream().forEach(entity -> {
                SelectBean bean = new SelectBean();
                bean.setValue(entity.getId().toString());
                bean.setLabel(entity.getName());
                result.add(bean);
            });
        }
        return result;
    }
}
