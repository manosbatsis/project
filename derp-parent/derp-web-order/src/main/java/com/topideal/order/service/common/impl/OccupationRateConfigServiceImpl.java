package com.topideal.order.service.common.impl;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.OccupationRateConfigDao;
import com.topideal.entity.dto.common.OccupationRateConfigDTO;
import com.topideal.entity.vo.common.OccupationRateConfigModel;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.order.service.common.OccupationRateConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OccupationRateConfigServiceImpl implements OccupationRateConfigService {
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;
    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao;
    @Autowired
    private OccupationRateConfigDao occupationRateConfigDao;

    @Override
    public OccupationRateConfigDTO listDTOByPage(OccupationRateConfigDTO dto,User user) throws Exception {
        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }
        return occupationRateConfigDao.listDTOByPage(dto);
    }

    @Override
    public void saveOccuptionRateCongfig(OccupationRateConfigDTO dto, User user) throws Exception {
        // 获取该事业部信息
        Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
        merchantBuRelParam.put("merchantId", user.getMerchantId());
        merchantBuRelParam.put("buId", dto.getBuId());
        merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1); // 启用
        MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
        if (merchantBuRelMongo == null || "".equals(merchantBuRelMongo)) {
            throw new RuntimeException("事业部ID为：" + dto.getBuId() + ",未查到该公司下事业部信息");
        }
        // 校验事业部与当前账号所绑定的事业部是否匹配
        boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), merchantBuRelMongo.getBuId());
        if (!userRelateBu) {
           throw new RuntimeException("事业部编码为：" + merchantBuRelMongo.getBuCode() + ",用户id：" + user.getId() + ",无权限操作该事业部");
        }
        if(dto.getExpirationDate().getTime() < dto.getEffectiveDate().getTime()){
            throw new RuntimeException("失效时间要大于生效时间");
        }

        //校验是否存在时间段有重叠的资金占用费率配置记录
        OccupationRateConfigModel existModel = new OccupationRateConfigModel();
        existModel.setBuId(dto.getBuId());
        existModel.setMerchantId(dto.getMerchantId());
        List<OccupationRateConfigModel> existList = occupationRateConfigDao.list(existModel);
        for(OccupationRateConfigModel queryModel : existList){
            //t1和t2两个时间段，只有t1完全在t2前面（t1失效时间小于t2生效时间），或者t1完全在t2后面（t1生效时间大于t2失效时间），才确保两个时间段没有重叠，其他情况均为有重叠
            //编辑时，剔除当前配置
            if(((dto.getId() != null && !dto.getId().equals(queryModel.getId())) || dto.getId() == null)
                    && !(dto.getExpirationDate().getTime() < queryModel.getEffectiveDate().getTime() ||
                    dto.getEffectiveDate().getTime() > queryModel.getExpirationDate().getTime())){
                throw new RuntimeException("系统存在时间段重叠的资金占用费率配置数据");

            }
        }
        OccupationRateConfigModel model = new OccupationRateConfigModel();
        BeanUtils.copyProperties(dto, model);

        model.setBuName(merchantBuRelMongo.getBuName());
        model.setMerchantId(user.getMerchantId());
        model.setMerchantName(user.getMerchantName());
        if(dto.getId() == null){
            model.setCreater(user.getId());
            model.setCreateName(user.getName());
            model.setCreateDate(TimeUtils.getNow());
            Long id = occupationRateConfigDao.save(model);
        }else{
            model.setModifier(user.getId());
            model.setModifyName(user.getName());
            model.setModifyDate(TimeUtils.getNow());
            occupationRateConfigDao.modify(model);
        }

    }

    @Override
    public void delOccuptionRateCongfig(String ids) throws Exception {
        List<Long> idList = StrUtils.parseIds(ids);
        for(Long id : idList){
            OccupationRateConfigModel model = occupationRateConfigDao.searchById(id);
            if(model == null){
                throw new RuntimeException("配置不存在");
            }
        }
        occupationRateConfigDao.delete(idList);
    }
}
