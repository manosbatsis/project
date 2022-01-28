package com.topideal.order.service.common.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.SdSaleConfigDao;
import com.topideal.dao.common.SdSaleConfigItemDao;
import com.topideal.dao.common.SdSaleTypeDao;
import com.topideal.entity.dto.common.SdSaleConfigDTO;
import com.topideal.entity.vo.common.SdSaleConfigItemModel;
import com.topideal.entity.vo.common.SdSaleConfigModel;
import com.topideal.entity.vo.common.SdSaleTypeModel;
import com.topideal.mongo.dao.CommbarcodeMongoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.CommbarcodeMongo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.common.SdSaleConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SdSaleConfigServiceImpl implements SdSaleConfigService{

	@Autowired
	private SdSaleConfigDao sdSaleConfigDao;
	@Autowired
	private SdSaleConfigItemDao sdSaleConfigItemDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao ;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private CommonBusinessService commonBusinessService ;
	@Autowired
	private SdSaleTypeDao sdSaleTypeDao;
	@Autowired
	private CommbarcodeMongoDao commbarcodeMongoDao;
    
	/**
	 * 查询列表信息 分页
	 */
	@Override
	public SdSaleConfigDTO listDTOByPage(SdSaleConfigDTO dto,User user) throws Exception {
		if(!DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType()) && dto.getMerchantId().longValue() != user.getMerchantId().longValue()) {//非管理员
			dto.setList(new ArrayList<>());
            dto.setTotal(0);
            return dto;
		}
		if (dto.getBuId() == null) {
            List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
            
            if (buList.isEmpty()) {
                dto.setList(new ArrayList<>());
                dto.setTotal(0);
                return dto;
            }
            dto.setBuList(buList);
        }
		return sdSaleConfigDao.listDTOByPage(dto);
	}

	/**
	 * 保存或审核
	 */
	@Override
	public void saveOrAudit(SdSaleConfigDTO dto,User user,String operate) throws Exception {
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
		if(dto.getEffectiveDate().getTime() > dto.getExpirationDate().getTime()){
			throw new RuntimeException("生效时间不能大于失效时间");
		}
		if( dto.getItemList() == null ||  dto.getItemList().size() < 1){
			throw new RuntimeException("单比例、多比例配置不能都为空");
		}
		
		//新增时，公司+事业部+客户+生效时间+失效时间为维度，允许生效时间存在重叠；
		if(dto.getId() == null) {
			SdSaleConfigModel queryModel = new SdSaleConfigModel();
			queryModel.setMerchantId(dto.getMerchantId());
			queryModel.setBuId(dto.getBuId());
			queryModel.setCustomerId(dto.getCustomerId());
			queryModel.setEffectiveDate(dto.getEffectiveDate());
			queryModel.setExpirationDate(dto.getExpirationDate());
			queryModel = sdSaleConfigDao.searchByModel(queryModel);
			if(queryModel != null) {
				throw new RuntimeException("公司+事业部+客户+生效时间+失效时间 已存在相同配置");
			}			
		}
		
		List<String> isExistItem = new ArrayList<String>();//检查是否有重复SD类型+标准条码
		for(SdSaleConfigItemModel itemModel : dto.getItemList()) {
			if(itemModel.getSdTypeId() == null){
				throw new RuntimeException("SD类型不能为空");
			}
			SdSaleTypeModel sdSaleTypeModel = sdSaleTypeDao.searchById(itemModel.getSdTypeId());
			if(DERP_ORDER.SDTYPECONFIG_TYPE_2.equals(sdSaleTypeModel.getType())){//多比例
				String key = itemModel.getSdTypeId() + itemModel.getCommbarcode();
				if(isExistItem.contains(key)){
					throw new RuntimeException("多比例：SD类型"+itemModel.getSdTypeName()+"+标准条码"+itemModel.getCommbarcode()+" 仅能存在一条配置记录");
				}
				isExistItem.add(key);
			}else if (DERP_ORDER.SDTYPECONFIG_TYPE_1.equals(sdSaleTypeModel.getType())){//单比例
				String key = itemModel.getSdTypeId().toString();
				if(isExistItem.contains(key)){
					throw new RuntimeException("单比例：SD类型 "+itemModel.getSdTypeName()+" 仅能存在一条配置记录");
				}
				isExistItem.add(key);
			}
			if(itemModel.getProportion() == null){
				throw new RuntimeException("SD类型："+itemModel.getSdTypeName()+" 比例不能为空");
			}
			itemModel.setSdTypeName(sdSaleTypeModel.getSdType());
			itemModel.setSdSimpleName(sdSaleTypeModel.getSdTypeName());
			itemModel.setSdType(sdSaleTypeModel.getType());
			itemModel.setProjectId(sdSaleTypeModel.getProjectId());
			itemModel.setProjectName(sdSaleTypeModel.getProjectName());

		}
		SdSaleConfigModel model = new SdSaleConfigModel();
		BeanUtils.copyProperties(dto, model);
		String operateAction = "";
		if("2".equals(operate)){				
			model.setStatus(DERP_ORDER.SDPURCHASE_STATUS_1);//已审核
			model.setAuditer(user.getId());
			model.setAuditName(user.getName());
			model.setAuditDate(TimeUtils.getNow());
			operateAction = "审核";
		}else {
			model.setStatus(DERP_ORDER.SDPURCHASE_STATUS_0);//待审核
			operateAction = "保存";
		}
		Long num = null;
		if(model.getId() == null) {//新增
			model.setCreater(user.getId());
			model.setCreateName(user.getName());
			model.setCreateDate(TimeUtils.getNow());
			num = sdSaleConfigDao.save(model);	
		}else {//编辑
			model.setModifier(user.getId());
			model.setModifiyName(user.getName());
			model.setModifyDate(TimeUtils.getNow());
			sdSaleConfigDao.modify(model);	
			num = model.getId();
			
			//删除sd配置明细
			SdSaleConfigItemModel queryItemModel = new SdSaleConfigItemModel();
			queryItemModel.setSaleConfigId(num);
			List<SdSaleConfigItemModel> existList = sdSaleConfigItemDao.list(queryItemModel);
			List<Long> ids = existList.stream().map(SdSaleConfigItemModel::getId).collect(Collectors.toList());
			sdSaleConfigItemDao.delete(ids);
		}				
		
		for(SdSaleConfigItemModel item : dto.getItemList()){
			item.setCreateDate(TimeUtils.getNow());
			item.setSaleConfigId(num);
			sdSaleConfigItemDao.save(item);
		}
		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_15, num.toString(), operateAction, null, null);
		
	}
	/**
	 * 根据id获取信息
	 */
	@Override
	public SdSaleConfigDTO searchDetail(Long id) throws Exception {
		SdSaleConfigModel model =  sdSaleConfigDao.searchById(id);
		SdSaleConfigDTO dto = new SdSaleConfigDTO();
		BeanUtils.copyProperties(model, dto);
		return dto;
	}

	/**
	 * 根据配置id获取关联的sd类型和品牌
	 */
	@Override
	public List<SdSaleConfigItemModel> searchItemDetail(SdSaleConfigItemModel model) throws Exception {
		return sdSaleConfigItemDao.list(model);
	}
	
	/**
	 * 删除
	 */
	@Override
	public void delSdSaleConfig(String ids) throws Exception {
		List<Long> idList= StrUtils.parseIds(ids);
		for(Long id : idList) {
			SdSaleConfigModel model = sdSaleConfigDao.searchById(id);
			if(DERP_ORDER.SDPURCHASE_STATUS_1.equals(model.getStatus())) {
				throw new RuntimeException("只能删除“待审核”的配置");
			}
			SdSaleConfigItemModel queryItemModel = new SdSaleConfigItemModel();
			queryItemModel.setSaleConfigId(id);
			List<SdSaleConfigItemModel> existList = sdSaleConfigItemDao.list(queryItemModel);
			List<Long> itemIds = existList.stream().map(SdSaleConfigItemModel::getId).collect(Collectors.toList());
			sdSaleConfigItemDao.delete(itemIds);
		}
		sdSaleConfigDao.delete(idList);
		
	}

	/**
	 * 多比例商品导入
	 */
	@Override
	public Map<String,Object> importGoods(List<List<Map<String, String>>> data) throws Exception {
		List<Map<String, String>> msgList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> configItemList = new ArrayList<Map<String, String>>();
		List<String> checkGoodsList = new ArrayList<>(); // 校验 sd类型+标准条码 是否重复
		Integer success = 0;
		Integer pass = 0;
		Integer failure = 0;
		List<Map<String, String>> objects = data.get(0);
		BigDecimal price = null;
		for (int j = 0; j < objects.size(); j++) {
			Map<String, String> msg = new HashMap<String, String>();
			Map<String, String> map = objects.get(j);
			String sdType = map.get("SD类型");
			if(StringUtils.isBlank(sdType)){
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "SD类型不能为空！");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			SdSaleTypeModel sdSaleTypeModel = new SdSaleTypeModel();
			sdSaleTypeModel.setSdType(sdType);
			sdSaleTypeModel.setType(DERP_ORDER.SDTYPECONFIG_TYPE_2);
			sdSaleTypeModel = sdSaleTypeDao.searchByModel(sdSaleTypeModel);
			if(sdSaleTypeModel == null){
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "多比例SD类型:"+sdType+"不存在！");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			String commbarcode = map.get("标准条码");
			if(StringUtils.isBlank(commbarcode)){
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "标准条码不能为空！");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			Map<String,Object> param = new HashMap<>();
			param.put("commbarcode",commbarcode);
			CommbarcodeMongo CommbarcodeMongo = commbarcodeMongoDao.findOne(param);
			if(CommbarcodeMongo == null){
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "标准条码:"+commbarcode+"不存在！");
				msgList.add(msg);
				failure += 1;
				continue;
			}

			String proportion = map.get("比例");
			if(StringUtils.isBlank(proportion)){
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "比例不能为空！");
				msgList.add(msg);
				failure += 1;
				continue;
			}else{
				int indexOf = proportion.indexOf(".");
				// 如果是小数
				if (indexOf != -1) {
					int count = proportion.length() - 1 - indexOf;
					if (count > 5) {
						msg.put("row", String.valueOf(j + 1));
						msg.put("msg", "比例不能超过5位小数");
						msgList.add(msg);
						failure += 1;
						continue;
					}
				}
			}

			String key = sdType + commbarcode;
			if(checkGoodsList.contains(key)){
				msg.put("row", String.valueOf(j + 1));
				msg.put("msg", "SD类型："+sdType+"，标准条码："+commbarcode+" 存在重复配置");
				msgList.add(msg);
				failure += 1;
				continue;
			}
			checkGoodsList.add(key);
		}

		if (failure == 0) {
			for (int j = 0; j < objects.size(); j++) {
				Map<String, String> map = objects.get(j);
				String sdType = map.get("SD类型");
				String commbarcode = map.get("标准条码");
				String proportion = map.get("比例");

				SdSaleTypeModel sdSaleTypeModel = new SdSaleTypeModel();
				sdSaleTypeModel.setSdType(sdType);
				sdSaleTypeModel = sdSaleTypeDao.searchByModel(sdSaleTypeModel);

				Map<String,Object> param = new HashMap<>();
				param.put("commbarcode",commbarcode);
				CommbarcodeMongo CommbarcodeMongo = commbarcodeMongoDao.findOne(param);

				Map<String, String> itemMap = new HashMap<String, String>();
				itemMap.put("sdTypeId",sdSaleTypeModel.getId().toString());
				itemMap.put("sdTypeName",sdSaleTypeModel.getSdType());
				itemMap.put("sdSimpleName",sdSaleTypeModel.getSdTypeName());
				itemMap.put("sdType",DERP_ORDER.SDTYPECONFIG_TYPE_2);
				itemMap.put("projectId",sdSaleTypeModel.getProjectId().toString());
				itemMap.put("projectName",sdSaleTypeModel.getProjectName());
				itemMap.put("commbarcode",commbarcode);
				itemMap.put("brandParent",CommbarcodeMongo.getCommBrandParentName());
				itemMap.put("goodsName",CommbarcodeMongo.getCommGoodsName());
				itemMap.put("proportion",proportion);

				configItemList.add(itemMap);
				success++;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msgList", msgList);
		map.put("configItemList", configItemList);
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		return map;
	}

	//反审
	@Override
	public void reverseAudit(Long id , User user) throws Exception {
		SdSaleConfigModel model = sdSaleConfigDao.searchById(id);
		if(DERP_ORDER.SDPURCHASE_STATUS_0.equals(model.getStatus())){
			throw new RuntimeException("反审失败，只有“待审核”可进行反审操作");
		}
		model.setStatus(DERP_ORDER.SDPURCHASE_STATUS_0);
		model.setModifier(user.getId());
		model.setModifiyName(user.getName());
		model.setModifyDate(TimeUtils.getNow());
		sdSaleConfigDao.modify(model);

		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_15, id.toString(), "反审", null, null);
	}

	public static void main(String[] args) {
		double str = 0.54321;
		System.out.println(Double.valueOf(str));
		System.out.println(new BigDecimal(str) +"   ---  "+new BigDecimal(str).doubleValue());
	}
}
