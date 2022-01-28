package com.topideal.order.service.common.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.PlatformCostConfigDao;
import com.topideal.dao.common.PlatformCostConfigItemDao;
import com.topideal.dao.platformdata.PlatformCategoryConfigDao;
import com.topideal.entity.dto.common.PlatformCostConfigDTO;
import com.topideal.entity.dto.common.PlatformCostConfigItemDTO;
import com.topideal.entity.vo.common.PlatformCostConfigItemModel;
import com.topideal.entity.vo.common.PlatformCostConfigModel;
import com.topideal.entity.vo.platformdata.PlatformCategoryConfigModel;
import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.dao.BrandSuperiorMongoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.mongo.entity.BrandSuperiorMongo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.common.PlatformCostConfigService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlatformCostConfigServiceImpl  implements PlatformCostConfigService {

    /* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformCostConfigServiceImpl.class);

    @Autowired
    private PlatformCostConfigDao platformCostConfigDao;
    @Autowired
    private PlatformCostConfigItemDao platformCostConfigItemDao;
    @Autowired
    private UserBuRelMongoDao userBuRelMongoDao;
    @Autowired
    private MerchantBuRelMongoDao merchantBuRelMongoDao;
    @Autowired
    private PlatformCategoryConfigDao platformCategoryConfigDao;
    @Autowired
    private BrandSuperiorMongoDao brandSuperiorMongoDao;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao;
    @Autowired
    private RMQProducer rocketMQProducer;
    @Autowired
    private CommonBusinessService commonBusinessService ;

    @Override
    public PlatformCostConfigDTO listDTOByPage(PlatformCostConfigDTO dto, User user) throws Exception {
        if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }
        return platformCostConfigDao.listDTOByPage(dto);
    }

    @Override
    public Map saveOrAudit(PlatformCostConfigDTO dto, User user, String operate) throws Exception {
        Map<String,Object> resultMap=new HashMap<>();

        String msg="";

        // 获取该事业部信息
        Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
        merchantBuRelParam.put("merchantId", dto.getMerchantId());
        merchantBuRelParam.put("buId", dto.getBuId());
        merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1); // 启用
        MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
        if (merchantBuRelMongo == null || "".equals(merchantBuRelMongo)) {
            throw new RuntimeException("事业部ID为：" + dto.getBuId() + ",未查到该公司下事业部信息");
        }
        // 用户事业部
        boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), dto.getBuId());
        if (!userRelateBu) {
            throw new RuntimeException("事业部编码为：" + merchantBuRelMongo.getBuCode() + ",用户id：" + user.getId() + ",无权限操作该事业部");
        }

        //判断表体是否为空
        if(dto.getItemList() == null || dto.getItemList().size() < 1) {
            resultMap.put("code","01");
            resultMap.put("message","关联的配置信息不能为空！");
            return resultMap;
        }


        //校验公司+事业部+客户+平台名称+生效时间+失效时间,已存在相同配置
        PlatformCostConfigDTO queryModel=new PlatformCostConfigDTO();
        queryModel.setMerchantId(dto.getMerchantId());
        queryModel.setBuId(dto.getBuId());
        queryModel.setCustomerId(dto.getCustomerId());
        queryModel.setStorePlatformCode(dto.getStorePlatformCode());
        queryModel.setStorePlatformName(dto.getStorePlatformName());
        queryModel.setEffectiveDate(dto.getEffectiveDate());
        queryModel.setExpirationDate(dto.getExpirationDate());
        queryModel=platformCostConfigDao.searchByCheck(queryModel);
        if(dto.getId()!=null){
            if(queryModel!=null){
                if(dto.getId()!=queryModel.getId()){
                    resultMap.put("code","01");
                    resultMap.put("message","公司+事业部+客户+平台名称+生效时间+失效时间,已存在相同配置");
                    return resultMap;
                }
            }
        }else {
            if(queryModel!=null){
                resultMap.put("code","01");
                resultMap.put("message","公司+事业部+客户+平台名称+生效时间+失效时间,已存在相同配置");
                return resultMap;
            }
        }

        //存储表体对象
        List<PlatformCostConfigItemModel> itemList = new ArrayList<PlatformCostConfigItemModel>();
        Map<String,Object> map=new HashMap<>();
        for(PlatformCostConfigItemDTO item:dto.getItemList()){
            //校验相同 “公司+事业部+客户+平台+平台费项” 仅能存在一个比例，不允许重复配置；
            String str=dto.getMerchantId()+"_"+dto.getBuId()+"_"+dto.getStorePlatformCode()
                   +"_"+dto.getCustomerId()+"_"+item.getPlatformSettlementId();
            if(map.containsKey(str)){
                resultMap.put("code","01");
                resultMap.put("message","公司+事业部+客户+平台+平台费项,仅能存在一个比例");
                return resultMap;
            }
            map.put(str,item);

            PlatformCostConfigItemModel model=new PlatformCostConfigItemModel();
            BeanUtils.copyProperties(item,model);
            itemList.add(model);
        }

        PlatformCostConfigModel model = new PlatformCostConfigModel();
        BeanUtils.copyProperties(dto, model);
        if("2".equals(operate)){
            model.setStatus(DERP_ORDER.SDPURCHASE_STATUS_1);//已审核
            model.setAuditer(user.getId());
            model.setAuditName(user.getName());
            model.setAuditDate(TimeUtils.getNow());
            msg="审核";
        }else {
            model.setStatus(DERP_ORDER.SDPURCHASE_STATUS_0);//未审核
            msg="编辑";
        }
        model.setStorePlatformName(DERP.getLabelByKey(DERP.storePlatformList, dto.getStorePlatformCode()));

        Long id= null;
        if(model.getId() == null) {//新增
            model.setCreater(user.getId());
            model.setCreateName(user.getName());
            model.setCreateDate(TimeUtils.getNow());
            id = platformCostConfigDao.save(model);
            msg="新增";
        }else {//审核
            model.setModifier(user.getId());
            model.setModifiyName(user.getName());
            model.setModifyDate(TimeUtils.getNow());
            platformCostConfigDao.modify(model);
            id = model.getId();

            //删除已有的sd类型和品牌 关联
            PlatformCostConfigItemModel queryItemModel = new PlatformCostConfigItemModel();
            queryItemModel.setPlatformConfigId(id);
            List<PlatformCostConfigItemModel> existList = platformCostConfigItemDao.list(queryItemModel);
            List<Long> ids = existList.stream().map(PlatformCostConfigItemModel::getId).collect(Collectors.toList());
            platformCostConfigItemDao.delete(ids);
        }
        //新增表体
        for(PlatformCostConfigItemModel item : itemList){
            item.setCreater(user.getId());
            item.setCreateName(user.getName());
            item.setCreateDate(TimeUtils.getNow());
            item.setPlatformConfigId(id);
            platformCostConfigItemDao.save(item);
        }

        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_14, String.valueOf(id), msg, null, null);

        resultMap.put("code","00");
        resultMap.put("message","保存成功！");
        return resultMap;
    }

    @Override
    public PlatformCostConfigDTO searchDetail(Long id) throws Exception {
        PlatformCostConfigDTO dto=new PlatformCostConfigDTO();
        PlatformCostConfigModel config=platformCostConfigDao.searchById(id);
        BeanUtils.copyProperties(config,dto);
        return dto;
    }


    @Override
    public List<PlatformCostConfigItemDTO> searchItemDetail(Long configId) throws Exception {
        return platformCostConfigItemDao.getConfigById(configId);
    }

    @Override
    public Map delSdSaleConfig(String ids) throws Exception {
        Map result = new HashMap();
        List<Long> idList= StrUtils.parseIds(ids);
        for(Long id : idList) {
            PlatformCostConfigModel model = platformCostConfigDao.searchById(id);
            if(DERP_ORDER.SDPURCHASE_STATUS_1.equals(model.getStatus())) {
                result.put("code","01");
                result.put("message","只能删除“未审核”的数据");
                return result;
            }
            PlatformCostConfigItemModel queryItemModel = new PlatformCostConfigItemModel();
            queryItemModel.setPlatformConfigId(id);
            List<PlatformCostConfigItemModel> existList = platformCostConfigItemDao.list(queryItemModel);
            List<Long> itemIds = existList.stream().map(PlatformCostConfigItemModel::getId).collect(Collectors.toList());
            platformCostConfigItemDao.delete(itemIds);
        }
        platformCostConfigDao.delete(idList);
        result.put("code","00");
        result.put("message","删除成功！");
        return result;
    }

    @Override
    public Map counterTrial(Long id, User user) throws SQLException {
        Map result = new HashMap();

        //判断平台费用单不存在
        PlatformCostConfigModel platformCostConfigModel=platformCostConfigDao.searchById(id);
        if(platformCostConfigModel==null){
            result.put("code","01");
            result.put("message","平台费用单不存在！");
            return result;
        }
        platformCostConfigModel.setStatus(DERP_ORDER.SDPURCHASE_STATUS_0);//未审核
        platformCostConfigModel.setModifier(user.getId());
        platformCostConfigModel.setModifiyName(user.getName());
        platformCostConfigModel.setModifyDate(TimeUtils.getNow());
        platformCostConfigDao.modify(platformCostConfigModel);

        /*记录操作日志*/
        commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_14, String.valueOf(id), "反审", null, null);

        result.put("code","00");
        result.put("message","反审成功！");
        return result;
    }


}
