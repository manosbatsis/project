package com.topideal.order.service.sale.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.AgreementCurrencyConfigDao;
import com.topideal.dao.sale.AgreementCurrencyConfigItemDao;
import com.topideal.dao.sale.PreSaleOrderCorrelationDao;
import com.topideal.dao.sale.PreSaleOrderDao;
import com.topideal.dao.sale.PreSaleOrderItemDao;
import com.topideal.entity.dto.sale.AgreementCurrencyConfigDTO;
import com.topideal.entity.dto.sale.AgreementCurrencyConfigExportDTO;
import com.topideal.entity.dto.sale.AgreementCurrencyConfigItemDTO;
import com.topideal.entity.vo.sale.AgreementCurrencyConfigItemModel;
import com.topideal.entity.vo.sale.AgreementCurrencyConfigModel;
import com.topideal.mongo.dao.BrandMongoDao;
import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.dao.MerchantDepotBuRelMongoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.BrandMongo;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.order.service.sale.AgreementCurrencyConfigService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 协议单价service实现类
 */
@Service
public class AgreementCurrencyConfigServiceImpl implements AgreementCurrencyConfigService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory
			.getLogger(AgreementCurrencyConfigServiceImpl.class);
	// 协议单价表头
	@Autowired
	private AgreementCurrencyConfigDao agreementCurrencyConfigDao;
	// 协议单价表头
	@Autowired
	private AgreementCurrencyConfigItemDao agreementCurrencyConfigItemDao;
	// 销售订单表头
	@Autowired
	private PreSaleOrderDao preSaleOrderDao;
	// 销售订单表体
	@Autowired
	private PreSaleOrderItemDao preSaleOrderItemDao;
	// 公司事业部信息
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	// 公司仓库事业部关联表
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	//商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private BrandMongoDao brandMongoDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public AgreementCurrencyConfigDTO listAgreementCurrencyByPage(AgreementCurrencyConfigDTO dto,User user) throws SQLException {
		List<Long> outBuList = new ArrayList<>();
		List<Long> inBuList = new ArrayList<>();
		int isFlag=0;// 2：移出移入事业部都为空，列表分页没按查询条件时
		if(dto.getOutBuId() == null) {//移出事业部
			isFlag=1;
			outBuList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(outBuList!=null && outBuList.size()>0) {
				dto.setOutBuList(outBuList);
			}
		}
		if(dto.getInBuId() == null) {//移入事业部
			isFlag=isFlag+1;
			inBuList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(inBuList!=null && inBuList.size()>0) {
				dto.setInBuList(inBuList);
			}
		}
		if(isFlag==2){
			if(outBuList.isEmpty() && inBuList.isEmpty()){
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
		}
		AgreementCurrencyConfigDTO agreementCurrencyConfigDTO = agreementCurrencyConfigDao.queryDTOListByPage(dto);
		int total = agreementCurrencyConfigDao.getTotal(dto);
		agreementCurrencyConfigDTO.setTotal(total);
		return agreementCurrencyConfigDTO;
	}

	@Override
	public List<AgreementCurrencyConfigExportDTO> getDetailsByExport(AgreementCurrencyConfigDTO dto,User user) throws SQLException {
		List<Long> outBuList = new ArrayList<>();
		List<Long> inBuList = new ArrayList<>();
		int isFlag=0;// 2：移出移入事业部都为空，列表分页没按查询条件时
		if(dto.getOutBuId() == null) {//移出事业部
			isFlag=1;
			outBuList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(outBuList!=null && outBuList.size()>0) {
				dto.setOutBuList(outBuList);
			}
		}
		if(dto.getInBuId() == null) {//移入事业部
			isFlag=isFlag+1;
			inBuList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(inBuList!=null && inBuList.size()>0) {
				dto.setInBuList(inBuList);
			}
		}
		if(isFlag==2){
			if(outBuList.isEmpty() && inBuList.isEmpty()){
				return new ArrayList<AgreementCurrencyConfigExportDTO>();
			}
		}
		List<AgreementCurrencyConfigExportDTO> list = agreementCurrencyConfigDao.getDetailsByExport(dto);
		return list;
	}

	@Override
	public List<AgreementCurrencyConfigDTO> listAgreementCurrencyConfig(AgreementCurrencyConfigDTO dto,User user) throws SQLException {
		List<Long> outBuList = new ArrayList<>();
		List<Long> inBuList = new ArrayList<>();
		int isFlag=0;// 2：移出移入事业部都为空，列表分页没按查询条件时
		if(dto.getOutBuId() == null) {//移出事业部
			isFlag=1;
			outBuList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(outBuList!=null && outBuList.size()>0) {
				dto.setOutBuList(outBuList);
			}
		}
		if(dto.getInBuId() == null) {//移入事业部
			isFlag=isFlag+1;
			inBuList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(inBuList!=null && inBuList.size()>0) {
				dto.setInBuList(inBuList);
			}
		}
		if(isFlag==2){
			if(outBuList.isEmpty() && inBuList.isEmpty()){
				return new ArrayList<AgreementCurrencyConfigDTO>();
			}
		}
		return agreementCurrencyConfigDao.queryDTOList(dto);
	}
	/**
	 * 新增
	 * @param json
	 * @param userId
	 * @param userName
	 * @param topidealCode
	 * @return
	 * @throws Exception
	 */
	@Override
	public String saveAgreementCurrencyConfig(String json, Long userId, String userName, String topidealCode) throws Exception {
		//解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		Long merchantId = Long.valueOf(jsonObj.getString("merchantId"));
		String merchantName = (String) jsonObj.get("merchantName");
		Long inBuId = Long.valueOf(jsonObj.getString("inBuId"));
		String inBuName = (String) jsonObj.get("inBuName");
		Long outBuId = Long.valueOf(jsonObj.getString("outBuId"));
		String outBuName = (String) jsonObj.get("outBuName");
		String currency = (String) jsonObj.get("currency");		// 协议币种

		// 校验事业部与当前账号所绑定的事业部是否匹配
		boolean userRelateInBu = userBuRelMongoDao.isUserRelateBu(userId, inBuId);
		if(!userRelateInBu){
			throw new RuntimeException("移入事业部为："+inBuId+",用户id："+userId+",无权限操作该事业部");
		}
		boolean userRelateOutBu = userBuRelMongoDao.isUserRelateBu(userId, outBuId);
		if(!userRelateOutBu){
			throw new RuntimeException("移出事业部为："+outBuId+",用户id："+userId+",无权限操作该事业部");
		}
		if(inBuId==outBuId){
			throw new RuntimeException("移入/移出事业部不能选择一样！");
		}
		//解析表体数据
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		BigDecimal totalAmount = BigDecimal.ZERO;
		List<AgreementCurrencyConfigItemModel> itemList=new ArrayList<AgreementCurrencyConfigItemModel>();
		for(int i=0;i<itemArr.size();i++){
			JSONObject job = itemArr.getJSONObject(i);
			if( job.isNullObject() || job.isEmpty()){
				continue;
			}
			Long goodsId = Long.valueOf(job.getString("goodsId"));
			String goodsCode = (String) job.get("goodsCode");
			String goodsNo = (String) job.get("goodsNo");
			String goodsName = (String) job.get("goodsName");
			String brandName = (String) job.get("brandName");
			String price = (String) job.get("price");
			//获取商品信息
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("merchandiseId", goodsId);
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(paramMap);
			if(merchandise==null){	// 商品不存在保存失败
				throw new RuntimeException("新增失败,没有查询到商品信息");
			}
			// 校验以“移入事业部+移出事业部+商品货号”为维度是否已存在配置
			Map<String,Object> findMap = new HashMap<>();
			findMap.put("merchantId",merchantId);
			findMap.put("inBuId",inBuId);
			findMap.put("outBuId",outBuId);
			findMap.put("goodsId",goodsId);
			Map<String, Object> configMap = agreementCurrencyConfigDao.getConfigByMap(findMap);
			if(configMap!=null){
				throw new RuntimeException("商品:"+goodsNo+"在该移入移出事业部已配置协议单价");
			}
			//保存表头表体
			AgreementCurrencyConfigModel saveModel = new AgreementCurrencyConfigModel();
			saveModel.setStatus(DERP_ORDER.AGREEMENTCURRENCYCONFIG_STATUS_032);//已生效
			saveModel.setInBuId(inBuId);
			saveModel.setInBuName(inBuName);
			saveModel.setOutBuId(outBuId);
			saveModel.setOutBuName(outBuName);
			saveModel.setCurrency(currency);
			saveModel.setMerchantId(merchantId);
			saveModel.setMerchantName(merchantName);
			saveModel.setCreater(userId);
			saveModel.setCreateName(userName);
			saveModel.setCreateDate(TimeUtils.getNow());

			Long agreementId = agreementCurrencyConfigDao.save(saveModel);
			if(agreementId == null){
				return "新增失败";
			}
			AgreementCurrencyConfigItemModel saveItemModel = new AgreementCurrencyConfigItemModel();
			saveItemModel.setAgreementId(agreementId);
			saveItemModel.setGoodsId(goodsId);
			saveItemModel.setGoodsNo(goodsNo);
			saveItemModel.setPrice(new BigDecimal(price));
			saveItemModel.setGoodsName(goodsName);
			saveItemModel.setGoodsCode(goodsCode);
			saveItemModel.setBrandName(brandName);
			saveItemModel.setCreateDate(TimeUtils.getNow());

			agreementCurrencyConfigItemDao.save(saveItemModel);
		}
		return "新增成功";
	}

	/**
	 * 删除
	 * @param ids
	 * @return
	 * @throws RuntimeException
	 */
	@Override
	public boolean delAgreementCurrencyConfig(List ids) throws SQLException, RuntimeException {
		int num = agreementCurrencyConfigDao.delConfig(ids);
		if(num >= 1){
            return true;
        }
        return false;
	}
	/**
	 * 导入预售单
	 */
	//@Override
	public Map saveImportAgreementCurrencyConfig(Map<Integer, List<List<Object>>> data, Long userId, String name, Long merchantId,
							   String merchantName, String topidealCode,String relMerchantIds) throws SQLException {
		int success = 0;
		int pass = 0;
		int failure = 0;
		List<Map<String,String>> msgList = new ArrayList<Map<String,String>>();
		Set<AgreementCurrencyConfigDTO> dtoSet = new HashSet<>();
		// 检查某个移入事业部、移出事业部、商品货号是否重复
		Set<String> checkSet = new HashSet<>();
		//出仓仓库信息
		DepotInfoMongo outDepotInfo =null;
		for (int i = 0; i < 1; i++) {//表头
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				try{
					Map<String,String> msg = new HashMap<String,String>();
					Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
					String inBuCode = map.get("移入事业部自编码");
					if(inBuCode == null || "".equals(inBuCode)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "移入事业部自编码不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					// 获取该移入事业部信息
					Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
					merchantBuRelParam.put("merchantId", merchantId);
					merchantBuRelParam.put("buCode", inBuCode);
					merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);	// 启用
					MerchantBuRelMongo inBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
					if(inBuRelMongo == null || "".equals(inBuRelMongo)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "移入事业部编码为："+inBuCode+",未查到该公司下事业部信息");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					// 校验事业部与当前账号所绑定的事业部是否匹配
					boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(userId, inBuRelMongo.getBuId());
					if(!userRelateBu){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "移入事业部编码为："+inBuCode+",用户id："+userId+",无权限操作该事业部");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					String outBuCode = map.get("移出事业部自编码");
					if(outBuCode == null || "".equals(outBuCode)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "移出事业部自编码不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					// 获取该移出事业部信息
					merchantBuRelParam.clear();
					merchantBuRelParam.put("merchantId", merchantId);
					merchantBuRelParam.put("buCode", outBuCode);
					merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);	// 启用
					MerchantBuRelMongo outBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
					if(outBuRelMongo == null || "".equals(outBuRelMongo)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "移出事业部编码为："+outBuCode+",未查到该公司下事业部信息");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					// 校验事业部与当前账号所绑定的事业部是否匹配
					boolean userRelateOutBu = userBuRelMongoDao.isUserRelateBu(userId, outBuRelMongo.getBuId());
					if(!userRelateOutBu){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "移出事业部编码为："+outBuCode+",用户id："+userId+",无权限操作该事业部");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					String goodsNo = map.get("商品货号");
					if(goodsNo == null || "".equals(goodsNo)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "商品货号不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					//获取商品信息
					Map<String, Object> params1 = new HashMap<String,Object>();
					params1.put("goodsNo", goodsNo);
					params1.put("merchantId", merchantId);
					MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(params1);
					if(goods == null){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "商品货号:"+goodsNo+"，该商品不存在");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					String price = map.get("协议单价");
					if(price == null || "".equals(price)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "协议单价不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					String currency = map.get("协议币种");
					if(currency == null || "".equals(currency)){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "协议币种不能为空");
						msgList.add(msg);
						failure+=1;
						continue;
					}else{
						String currencyLabel = DERP_ORDER.getLabelByKey(DERP.currencyCodeList, currency);
						if(StringUtils.isBlank(currencyLabel)){
							msg.put("row", String.valueOf(j+1));
							msg.put("msg", "协议币种输入有误");
							msgList.add(msg);
							failure+=1;
							continue;
						}
					}
					// 判断是否有相同移入事业部、移出事业部、商品货号仅能存在一条数据
					String isKey=inBuCode+outBuCode+goodsNo;
					if(!checkSet.contains(isKey)){
						checkSet.add(isKey);
					}else{
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "移入事业部:"+inBuCode+"移出事业部:"+outBuCode+"商品货号:"+goodsNo+"有多条数据,请合并后导入");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					// 校验以“移入事业部+移出事业部+商品货号”为维度查询配置表是否已存在配置
					Map<String,Object> findMap = new HashMap<>();
					findMap.put("merchantId",merchantId);
					findMap.put("inBuId",inBuRelMongo.getBuId());
					findMap.put("outBuId",outBuRelMongo.getBuId());
					findMap.put("goodsId",goods.getMerchandiseId());
					Map<String, Object> configMap = agreementCurrencyConfigDao.getConfigByMap(findMap);
					if(configMap!=null){
						msg.put("row", String.valueOf(j+1));
						msg.put("msg", "移入事业部:"+inBuCode+"移出事业部:"+outBuCode+"商品货号:"+goodsNo+"在该移入移出事业部已配置协议单价");
						msgList.add(msg);
						failure+=1;
						continue;
					}
					//注入数据
					AgreementCurrencyConfigDTO saveDto = new AgreementCurrencyConfigDTO();
					saveDto.setStatus(DERP_ORDER.AGREEMENTCURRENCYCONFIG_STATUS_032);//已生效
					saveDto.setInBuId(inBuRelMongo.getBuId());
					saveDto.setInBuName(inBuRelMongo.getBuName());
					saveDto.setOutBuId(outBuRelMongo.getBuId());
					saveDto.setOutBuName(outBuRelMongo.getBuName());
					saveDto.setCurrency(currency);
					saveDto.setMerchantId(merchantId);
					saveDto.setMerchantName(merchantName);
					saveDto.setCreater(userId);
					saveDto.setCreateName(name);
					saveDto.setCreateDate(TimeUtils.getNow());

					AgreementCurrencyConfigItemDTO saveItemDto = new AgreementCurrencyConfigItemDTO();
					saveItemDto.setGoodsId(goods.getMerchandiseId());
					saveItemDto.setGoodsNo(goodsNo);
					saveItemDto.setPrice(new BigDecimal(price));
					saveItemDto.setGoodsName(goods.getName());
					saveItemDto.setGoodsCode(goods.getGoodsCode());
					saveItemDto.setCreateDate(TimeUtils.getNow());
					// 查询品牌信息					
					Map<String, Object> params3 = new HashMap<String,Object>();
					params3.put("brandId", goods.getBrandId());
					BrandMongo brandMongo = brandMongoDao.findOne(params3);
					if(brandMongo != null){
						saveItemDto.setBrandName(brandMongo.getName());
					}
					List<AgreementCurrencyConfigItemDTO> itemDTOList = new ArrayList<>();
					itemDTOList.add(saveItemDto);
					saveDto.setItemList(itemDTOList);
					dtoSet.add(saveDto);

				}catch (Exception e) {
					e.printStackTrace();
					failure+=1;
					continue;
				}
			}
		}
		// 没有失败信息，保存数据
		if(failure==0){
			for (AgreementCurrencyConfigDTO agreementCurrencyConfigDTO : dtoSet) {
				Map<String,String> msg = new HashMap<String,String>();
				AgreementCurrencyConfigModel saveModel = new AgreementCurrencyConfigModel();
				BeanUtils.copyProperties(agreementCurrencyConfigDTO, saveModel);
				Long agreementId = agreementCurrencyConfigDao.save(saveModel);

				AgreementCurrencyConfigItemModel saveItemModel = new AgreementCurrencyConfigItemModel();
				List<AgreementCurrencyConfigItemDTO> itemList = agreementCurrencyConfigDTO.getItemList();
				if(itemList == null || itemList.size() == 0){
					msg.put("row", "");
					msg.put("msg", "商品信息为空");
					msgList.add(msg);
					failure+=1;
					continue;
				}
				BeanUtils.copyProperties(itemList.get(0),saveItemModel);
				saveItemModel.setAgreementId(agreementId);
				agreementCurrencyConfigItemDao.save(saveItemModel);
				success++;
			}
		}
		Map map =new HashMap();
		map.put("success",success);
		map.put("pass",pass);
		map.put("failure",failure);
		map.put("msgList",msgList);
		return map;
	}

	/**
	 * 把两个数组组成一个map
	 * @param keys   键数组
	 * @param values 值数组
	 * @return 键值对应的map
	 */
	private Map<String, String> toMap(Object[] keys, Object[] values) {
		if (keys != null && values != null && keys.length == values.length) {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			for (int i = 0; i < keys.length; i++) {
				map.put((String) keys[i], values[i].toString());
			}
			return map;
		}
		return null;
	}
}
