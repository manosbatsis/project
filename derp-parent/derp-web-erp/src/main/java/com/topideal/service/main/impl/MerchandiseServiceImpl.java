package com.topideal.service.main.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.finance.FinanceUtils;
import com.topideal.api.finance.f02.SyncGoodsPushRequest;
import com.topideal.api.pms.PMSUtils;
import com.topideal.api.pms.p_001.PMSGoodQueryRequest;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.api.sync.sy01.SyncGoodsRequest;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.EpassAPIMethodEnum;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.BrandDao;
import com.topideal.dao.base.BrandSuperiorDao;
import com.topideal.dao.base.CountryOriginDao;
import com.topideal.dao.base.OperateSysLogDao;
import com.topideal.dao.base.PackTypeDao;
import com.topideal.dao.base.UnitInfoDao;
import com.topideal.dao.main.CommbarcodeDao;
import com.topideal.dao.main.CommbarcodeItemDao;
import com.topideal.dao.main.CustomsAreaConfigDao;
import com.topideal.dao.main.DepotInfoDao;
import com.topideal.dao.main.DepotMerchantRelDao;
import com.topideal.dao.main.MerchandiseCatDao;
import com.topideal.dao.main.MerchandiseCustomsRelDao;
import com.topideal.dao.main.MerchandiseDepotRelDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchantBrandRelDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.dao.main.MerchantRelDao;
import com.topideal.entity.dto.main.DepotInfoDTO;
import com.topideal.entity.dto.main.ImportErrorMessage;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;
import com.topideal.entity.dto.main.UpdateDepotOrderDTO;
import com.topideal.entity.vo.base.BrandModel;
import com.topideal.entity.vo.base.BrandSuperiorModel;
import com.topideal.entity.vo.base.CountryOriginModel;
import com.topideal.entity.vo.base.OperateSysLogModel;
import com.topideal.entity.vo.base.PackTypeModel;
import com.topideal.entity.vo.base.UnitInfoModel;
import com.topideal.entity.vo.main.CommbarcodeItemModel;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.entity.vo.main.CustomsAreaConfigModel;
import com.topideal.entity.vo.main.DepotInfoModel;
import com.topideal.entity.vo.main.DepotMerchantRelModel;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.entity.vo.main.MerchandiseCustomsRelModel;
import com.topideal.entity.vo.main.MerchandiseDepotRelModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantBrandRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantRelModel;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.service.main.MerchandiseService;
import com.topideal.webapi.form.UpdateDepotOrderForm;
import com.topideal.webapi.form.UpdateDepotOrderItemForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 商品管理service实现类
 */
@Service
public class MerchandiseServiceImpl implements MerchandiseService {
	
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(MerchandiseCatServiceImpl.class);

	// 商品信息dao
	@Autowired
	private MerchandiseInfoDao merchandiseDao;
	// 原厂国dao
	@Autowired
	private CountryOriginDao countryOriginDao;
	// 品牌dao
	@Autowired
	private BrandDao brandDao;
	// 商家dao
	@Autowired
	private MerchantInfoDao merchantInfoDao;
	// 商品分类dao
	@Autowired
	private MerchandiseCatDao merchandiseCatDao;
	// 仓库dao
	@Autowired
	private DepotInfoDao depotInfoDao;
	// 单位dao
	@Autowired
	private UnitInfoDao unitInfoDao;

	// 子商家
	@Autowired
	private MerchantRelDao merchantRelDao;
	@Autowired
	private PackTypeDao packTypeDao;


	//商家品牌关联表
	@Autowired
	private MerchantBrandRelDao merchantBrandRelDao;
    @Autowired
	private RMQProducer rocketMQProducer; // MQ

    // 商家
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;
    @Autowired
    private CommbarcodeDao commbarcodeDao ;
    @Autowired
    private CommbarcodeItemDao commbarcodeItemDao ;
    @Autowired
	private DepotMerchantRelDao depotMerchantRelDao;
    @Autowired
	private BrandSuperiorDao brandSuperiorDao;
    @Autowired
	private MerchandiseCustomsRelDao merchandiseCustomsRelDao;
    @Autowired
	private CustomsAreaConfigDao customsAreaConfigDao;

	@Autowired
	private MerchandiseDepotRelDao merchandiseDepotRelDao;// 商品仓库关系表
    @Autowired
    private OperateSysLogDao operateSysLogDao;
    
    
	@Override
	public Map importMerchandiseDepotRel(Map<Integer, List<List<Object>>> data, User user) throws Exception {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		// 通过商家id获取商家信息
		MerchantInfoModel merchant = merchantInfoDao.searchById(user.getMerchantId());
        if (merchant == null) {
            throw new RuntimeException("商家不存在");
        }
		int success = 0;
		// 校验列表导入货号是否重复
		Map<String, String>goodsNoCheckMap=new HashMap<>();
		// 封装新增的 商品仓库关系表
		List<MerchandiseDepotRelModel> addList=new ArrayList<>();
		// 要删除的 商品车仓库 关系表
		List<Long> delList=new ArrayList<>();
		//日志list
		List<OperateSysLogModel>logList=new ArrayList<OperateSysLogModel>();

		// 数据
		for (int i = 0; i < 1; i++) {
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(i).get(0).toArray(), objects.get(j).toArray());
					String goodsNo = map.get("商品货号");
					String depotCodes = map.get("关联仓库");	//多个英文逗号隔开				
					String type = map.get("类型");// 01 新增 02 替换					
					if (StringUtils.isBlank(goodsNo)) {
						getImportErrorMessage(resultList,j,"商品货号不能为空");
						continue;
					}
					goodsNo=goodsNo.trim();
					if (goodsNoCheckMap.containsKey(goodsNo)) {
						getImportErrorMessage(resultList,j,"商品货号:"+goodsNo+"不能重复");
						continue;
					}
					goodsNoCheckMap.put(goodsNo, goodsNo);
					MerchandiseInfoModel merchandise=new MerchandiseInfoModel();
					merchandise.setMerchantId(user.getMerchantId());
					merchandise.setGoodsNo(goodsNo);
					merchandise.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);
					merchandise = merchandiseDao.searchByModel(merchandise);
					if (merchandise==null) {
						getImportErrorMessage(resultList,j,"根据商品货号:"+goodsNo+"没有查找到启用的商品");
						continue;
					}
					if (StringUtils.isBlank(type)) {
						getImportErrorMessage(resultList,j,"类型不能为空");
						continue;
					}
					if (!("01".equals(type)||"02".equals(type))) {
						getImportErrorMessage(resultList,j,"类型只能为“ 01 新增”或者 “02替换”");
						continue;
					}
										
					if (StringUtils.isBlank(depotCodes)&&"01".equals(type)) {
						getImportErrorMessage(resultList,j,"类型是01新增的关联仓库不能为空");
						continue;
					}		
					//替换 要先删除原来的  删除的商品仓库关系的商品封装
					if ("02".equals(type)) {
						delList.add(merchandise.getId());
					}
					// 替换和新增的数据封装
					if (StringUtils.isNotBlank(depotCodes)) {
						depotCodes=depotCodes.trim();
						List<String> depotCodeList = Arrays.asList(depotCodes.split(","));
						String depotCodeError="";
						for (String depotCode : depotCodeList) {
							if (StringUtils.isBlank(depotCode)) continue;
							DepotInfoDTO depotInfo=new DepotInfoDTO();
							depotInfo.setDepotCode(depotCode);
							depotInfo.setMerchantId(user.getMerchantId());	
							//查询商家仓库关系数据
							 List<DepotInfoDTO> depotInfoList = depotInfoDao.getSelectBeanByDTO(depotInfo);
							if (depotInfoList.size()==0) {
								depotCodeError=depotCodeError+","+depotCode;
								continue;
							}
							depotInfo = depotInfoList.get(0);							
							MerchandiseDepotRelModel merchandiseDepotRel=new MerchandiseDepotRelModel();
							merchandiseDepotRel.setDepotId(depotInfo.getId());
							merchandiseDepotRel.setDepotName(depotInfo.getName());
							merchandiseDepotRel.setMerchantId(user.getMerchantId());
							merchandiseDepotRel.setGoodsId(merchandise.getId());
							addList.add(merchandiseDepotRel);
						}
						if (StringUtils.isNotBlank(depotCodeError)) {
							getImportErrorMessage(resultList,j,"根据仓库自编码:"+depotCodeError+"没有查找到"+user.getMerchantName()+"下启用的仓库");
							continue;
						}
					}// 封装实体接收
					//封装日志生成
					OperateSysLogModel saveModel = new OperateSysLogModel() ;
				    saveModel.setModule(DERP_SYS.OREARTE_SYS_LOG_4);
				    saveModel.setCreateDate(TimeUtils.getNow());
				    saveModel.setOperateDate(TimeUtils.getNow());
				    saveModel.setOperateId(user.getId());
				    saveModel.setOperater(user.getName());
				    saveModel.setOperateRemark("导入商品仓库关系");
				    saveModel.setOperateResult("导入成功");
				    saveModel.setOperateAction("导入");
				    saveModel.setRelId(merchandise.getId());
				    logList.add(saveModel);

				}//excle 数据循环
			}//sheet 循环

		// 没有失败才进行新增商品
		if (resultList.size()==0) {			
			// 删除原来的的 商品仓库关系表
			if (delList.size()>0)merchandiseDepotRelDao.deleteByGoodsId(delList);
			// 循环新增商家仓库关系表数据
			for (MerchandiseDepotRelModel saveModel: addList) {
				MerchandiseDepotRelModel depotRelModel=new MerchandiseDepotRelModel();
				depotRelModel.setDepotId(saveModel.getDepotId());
				depotRelModel.setGoodsId(saveModel.getGoodsId());
				List<MerchandiseDepotRelModel> depotRelList = merchandiseDepotRelDao.list(depotRelModel);
				if (depotRelList.size()==0) {
					merchandiseDepotRelDao.save(saveModel);
					success+=1;
				}
			}			
			// 循环新增日志
			for (OperateSysLogModel logModel : logList) {
				operateSysLogDao.save(logModel) ;
			}
		}
	
		Map map = new HashMap();
		map.put("success", success);
		map.put("pass", 0);
		map.put("failure", resultList.size());
		map.put("message", resultList);
		return map;

	}

    /**
     * 商品关联仓库保存
     */
    @Override
	public boolean saveMerchandiseDepotRel(User user,Long goodsId, String depotIds) throws SQLException {
    	MerchandiseInfoModel merchandiseInfo = merchandiseDao.searchById(goodsId);
    	if (merchandiseInfo==null)throw new  RuntimeException("根据商品id没有找到商品");
    	//新增商品仓库关系表数据
    	saveMerchandiseDepotRelCom(depotIds, goodsId, merchandiseInfo.getMerchantId());	
    	OperateSysLogModel saveModel = new OperateSysLogModel() ;
	    saveModel.setModule(DERP_SYS.OREARTE_SYS_LOG_4);
	    saveModel.setCreateDate(TimeUtils.getNow());
	    saveModel.setOperateDate(TimeUtils.getNow());
	    saveModel.setOperateId(user.getId());
	    saveModel.setOperater(user.getName());
	    saveModel.setOperateRemark("商品关联仓库保存");
	    saveModel.setOperateResult("成功");
	    saveModel.setOperateAction("商品关联仓库保存");
	    saveModel.setRelId(goodsId);
	    operateSysLogDao.save(saveModel) ;
		return true;
	}

	/**
	 * 根据id获取商品详情
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public MerchandiseInfoDTO searchDetail(Long id) throws SQLException {
		return merchandiseDao.searchDTOById(id);
	}

	/**
	 * 商品列表（分页）
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public MerchandiseInfoDTO listMerchandise(MerchandiseInfoDTO dto) throws SQLException {
		List ids = new ArrayList();
		// 查询当前商家的所有代理商家
		MerchantRelModel merchantRel = new MerchantRelModel();
		merchantRel.setMerchantId(dto.getMerchantId());
		List<MerchantRelModel> list = merchantRelDao.list(merchantRel);
		if (list != null && list.size() > 0) {
			for (MerchantRelModel rel : list) {
				ids.add(rel.getProxyMerchantId());
			}
		}
		ids.add(dto.getMerchantId());
		dto.setMerchantIds(ids);
		return merchandiseDao.getListByPage(dto);
	}

	/**
	 * 根据商品id删除商品(支持批量)
	 * 
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean delMerchandise(List ids) throws Exception {
		/*// 先去删除货期配置表
		for(Object id:ids){
			//获取商品信息
			MerchandiseInfoModel merchandiseModel = new MerchandiseInfoModel();
			merchandiseModel.setId(Long.parseLong(id.toString()));
			MerchandiseInfoModel merchandiseInfo = merchandiseDao.searchByModel(merchandiseModel);
			if(merchandiseInfo==null){
				return false;
			}
			  // 通过商家id获取商家信息
			MerchantInfoModel merchantInfoModel =new MerchantInfoModel();
			merchantInfoModel.setId(merchandiseInfo.getMerchantId());
			MerchantInfoModel merchant = merchantInfoDao.searchByModel(merchantInfoModel);
	        if (merchant == null) {
	        	return false;
	        }
			if(merchandiseInfo != null){
				// 若是嘉宝的新增商品发消息给ordermq,从宝洁商品货期配置表里删除该商品的货期配置
				if("0000138".equals(merchant.getTopidealCode())){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("merchantId", merchandiseInfo.getMerchantId());	// 商家ID
					jsonObj.put("merchantName", merchandiseInfo.getMerchantName());	// 商家名称
					jsonObj.put("merchantCode", merchant.getCode());	// 商家编码
					jsonObj.put("goodsId", merchandiseInfo.getId());			// 商品ID
					jsonObj.put("goodsNo", merchandiseInfo.getGoodsNo());			// 商品货号
					jsonObj.put("goodsName", merchandiseInfo.getName());			// 商品名称
					jsonObj.put("goodsCode", merchandiseInfo.getGoodsCode());			// 商品编码
					jsonObj.put("commbarcode", merchandiseInfo.getCommbarcode());		// 标准条码
					jsonObj.put("oldCommbarcode", null);	
					jsonObj.put("operateType", "0");		// 操作类型 0:删除 1:增加/修改
					
					try{//orderMQ
						SendResult result = rocketMQProducer.send(jsonObj.toString(), MQOrderEnum.BJ_GOODS_TIME_CONFIG.getTopic(),MQOrderEnum.BJ_GOODS_TIME_CONFIG.getTags());
						LOGGER.info("宝洁商品货期配置MsgId:"+result.getMsgId());
					}catch(Exception e){
						e.printStackTrace();
						LOGGER.error("宝洁商品货期配置异常："+e.getMessage());
					}
				}	
			}
		}*/
		// 删除商品表
		int num = merchandiseDao.delete(ids);
		if (num >= 1) {
			return true;
		}
		return false;
	}

	/**
	 * 新增商品
	 * 
	 * @param model
	 * @return
	 */
	@Override

	public boolean saveMerchandise(User user,MerchandiseInfoModel model,String customsAreaId) throws Exception {
		// 根据商家+商品货号维度查询商品是否可以新增
		MerchandiseInfoModel merchandise = new MerchandiseInfoModel();
		merchandise.setMerchantId(model.getMerchantId());
		String goodsNo = model.getGoodsNo();
		goodsNo = goodsNo.trim();
		merchandise.setGoodsNo(goodsNo);
		List<MerchandiseInfoModel> list = merchandiseDao.list(merchandise);
		if (list != null && list.size() > 0) {
			throw new RuntimeException("货号已存在，新增失败");
		}		
		// 获取单位
		Long unit = model.getUnit();
		UnitInfoModel unitInfoModel =null;
		if (unit!=null)unitInfoModel = unitInfoDao.searchById(unit);	
		if (unitInfoModel==null)unitInfoModel=new UnitInfoModel();
		// 获取包装方式
		Long countyId = model.getCountyId();
		CountryOriginModel countryModel= null;
		if (countyId!=null)countryModel = countryOriginDao.searchById(countyId);
		if (countryModel==null)countryModel=new CountryOriginModel();
		// 包装方式
		String packType = model.getPackType();
		
		//封装推送金融
		JSONObject jsonData= syncGoodsPushModel(model,unitInfoModel,countryModel,packType);		
		Object notes = jsonData.get("notes");
		if (notes==null) throw new RuntimeException("调通用商品库链接失败");
		String commonBarcode=null;
		if ("成功".equals(notes)) {
			commonBarcode = jsonData.getString("commonBarcode");	
		}else {
			commonBarcode=model.getBarcode();
		}
		if (StringUtils.isBlank(commonBarcode)) throw new RuntimeException("未获取到标准条码");
		// 获取二级分类
		MerchandiseCatModel merchandiseCat2 = new MerchandiseCatModel(); 
		merchandiseCat2.setId(model.getProductTypeId());
		merchandiseCat2.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);//查询二级分类
    	merchandiseCat2 = merchandiseCatDao.searchByModel(merchandiseCat2);
    	if (merchandiseCat2==null) throw new RuntimeException("二级分类不存在");
    	
    	MerchandiseCatModel merchandiseCat1= new MerchandiseCatModel();
    	merchandiseCat1.setId(merchandiseCat2.getParentId());// 二级分类的父级id
		merchandiseCat1 = merchandiseCatDao.searchByModel(merchandiseCat1);
		if (merchandiseCat1==null) throw new RuntimeException("一级分类不存在");
		
		BrandModel brandModel=new BrandModel();
		brandModel.setId(model.getBrandId());
		brandModel = brandDao.searchByModel(brandModel);  
		if (brandModel==null) throw new RuntimeException("品牌不存在");
		
		
		/*MerchandiseCatModel merchandiseCat2 = null ;
		if (jsonData.get("secondCategory")!=null&&StringUtils.isNotBlank(jsonData.getString("secondCategory"))) {
			merchandiseCat2= new MerchandiseCatModel();
	    	merchandiseCat2.setName(jsonData.getString("secondCategory"));
	    	merchandiseCat2 = merchandiseCatDao.searchByModel(merchandiseCat2);
		}
		
		// 通过二级分类找一级分类
		MerchandiseCatModel merchandiseCat1= new MerchandiseCatModel();
    	if (merchandiseCat2!=null&&merchandiseCat2.getParentId()!=null) {   		
    		merchandiseCat1.setId(merchandiseCat2.getParentId());// 二级分类的父级id
    		merchandiseCat1 = merchandiseCatDao.searchByModel(merchandiseCat1);
		}
    	if (merchandiseCat2==null) merchandiseCat2=new MerchandiseCatModel();
    	if (merchandiseCat1==null) merchandiseCat1=new MerchandiseCatModel();
		// 品牌处理		
		BrandModel brandModel=null;
		if (jsonData.get("standardBrand")!=null&&StringUtils.isNotBlank(jsonData.getString("standardBrand"))) {
			String brandName = jsonData.getString("standardBrand");// 品牌	
			brandModel=new  BrandModel();
			brandModel.setName(brandName);
			brandModel = brandDao.searchByModel(brandModel);  
		}
		String englishBrandName=null;
    	if (jsonData.get("englishName")!=null&&StringUtils.isNotBlank(jsonData.getString("englishName"))){
    		englishBrandName=jsonData.getString("englishName");
		}
    	String chinaBrandName=null;
		if (jsonData.get("chineseName")!=null&&StringUtils.isNotBlank(jsonData.getString("chineseName"))){
			chinaBrandName=jsonData.getString("chineseName");
		}
		if (brandModel==null)brandModel=new BrandModel();
		// 新增或修改品牌
		if (jsonData.get("standardBrand")!=null&&StringUtils.isNotBlank(jsonData.getString("standardBrand"))) {
			brandModel.setName(jsonData.getString("standardBrand"));
			brandModel.setEnglishBrandName(englishBrandName);
			brandModel.setChinaBrandName(chinaBrandName); 
			// 新增品牌名称
	    	if (brandModel.getId()==null) {		
	        	brandDao.save(brandModel);
			}else {
				brandModel.setModifyDate(TimeUtils.getNow());
				brandDao.modify(brandModel);
			}
		}**/
		// 商品条码
		String barcode = model.getBarcode();
		
		if (brandModel.getId()!=null) {
			// 查询品牌是否存在
			MerchantBrandRelModel merchantBrandRel = new MerchantBrandRelModel();
			merchantBrandRel.setBrandId(brandModel.getId());
			merchantBrandRel.setMerchantId(model.getMerchantId());
			merchantBrandRel = merchantBrandRelDao.searchByModel(merchantBrandRel);
			if (merchantBrandRel == null || merchantBrandRel.getId() == null) {// 不存在则新增
				merchantBrandRel = new MerchantBrandRelModel();
				merchantBrandRel.setBrandId(model.getBrandId());
				merchantBrandRel.setMerchantId(model.getMerchantId());
				merchantBrandRelDao.save(merchantBrandRel);
			}
		}
		
		
		String code = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ERP);
		// 生成自编码
		model.setCode(code);
		model.setGoodsCode(code);
		model.setProductTypeId(merchandiseCat2.getId());// 二级产品分类id
		model.setProductTypeName(merchandiseCat2.getName());// 二级产品分类名称
		model.setProductTypeId0(merchandiseCat1.getId());// 一级产品分类id
		model.setProductTypeName0(merchandiseCat1.getName());// 一级产品分类名称
		model.setSource(DERP_SYS.MERCHANDISEINFO_SOURCE_0);//数据来源默认0
		model.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);//状态默认你1
		model.setIsRecord(DERP_SYS.MERCHANDISEINFO_ISRECORD_0);
		model.setIsGroup(DERP_SYS.MERCHANDISEINFO_ISGROUP_0);
		model.setCommbarcode(commonBarcode);// 标准条码
		//判断同个商家、同个标准条码下是否存在“外仓统一码”为是的商品，若存在，则新增商品的“外仓统一码”标识为否
		if (isExistOutDepotMerchandise(model.getMerchantId(), model.getBarcode())) {
			model.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_1);
		} else {
			model.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
		}
		//-------------------------------- 获取pms商品开始-----------------------
		List<String>productCodes=new ArrayList<>();
		List<String>cCodes=new ArrayList<>();
		cCodes.add(user.getTopidealCode());
		productCodes.add(model.getGoodsNo());
		// 获取pms 商品信息
		Map<String, Map<String, Object>> pmsGoodsMap=new  HashMap<String, Map<String,Object>>();
		pmsGoodsMap = getPmsGoods(productCodes, cCodes);			
		// 获取pms商品 毛重 净重
		Map<String, Object> map = pmsGoodsMap.get(model.getGoodsNo());
		if (map!=null) {
			Double net = (Double) map.get("net");
			Double kgs = (Double) map.get("kgs");
			Long countryId = (Long) map.get("countryId");
			model.setNetWeight(net);
			model.setGrossWeight(kgs);
			model.setCountyId(countryId);
		}
		Long id = merchandiseDao.save(model);
		//新增商家仓库关系表公共方法
		String depotIds = model.getDepotIds();
		saveMerchandiseDepotRelCom(depotIds,id,user.getMerchantId());
		

		List<MerchandiseCustomsRelModel> relList=new ArrayList<>();
		// 封装 商品关区关系表数据
		if (StringUtils.isNotBlank(customsAreaId)) {
			List<String> customsAreaIdList = Arrays.asList(customsAreaId.split(","));
			for (String customsAreaIdStr : customsAreaIdList) {
				if (StringUtils.isBlank(customsAreaIdStr))continue;
				CustomsAreaConfigModel customsAreaConfig = customsAreaConfigDao.searchById(Long.valueOf(customsAreaIdStr));
				if (customsAreaConfig==null)customsAreaConfig=new CustomsAreaConfigModel();
				MerchandiseCustomsRelModel relModel=new MerchandiseCustomsRelModel();
				relModel.setMerchantId(model.getMerchantId());
				relModel.setMerchantName(model.getMerchantName());
				relModel.setGoodsId(model.getId());
				relModel.setGoodsNo(model.getGoodsNo());
				relModel.setCustomsAreaId(customsAreaConfig.getId());
				relModel.setCustomsAreaCode(customsAreaConfig.getCode());
				relModel.setCustomsAreaName(customsAreaConfig.getName());
				relModel.setCreateDate(TimeUtils.getNow());
				relList.add(relModel);
			}			
		}
		// 新增商品关区关系表数据
		for (MerchandiseCustomsRelModel relModel : relList) {
			merchandiseCustomsRelDao.save(relModel);
		}
		
		// 现在标准条码数据
		saveCommBarbarcode(model, brandModel, merchandiseCat2);	
        // 封装推送主数据实体
		JSONObject requestJson = sendGoodsToSyncRequest(user,model,merchandiseCat2,merchandiseCat1,brandModel);
		// 推送主数据
		sendGoodsToSync(requestJson);
		//推送复制商品
		JSONObject json=new JSONObject();
		json.put("merchantId", model.getMerchantId());
		json.put("goodsId", id);
		json.put("tag", "2");//"2" 页面/其他触发单个货号 "1" 定时器 
		sendCopyMerchandiseMQ(json);
		// 日志
		OperateSysLogModel saveModel = new OperateSysLogModel() ;
	    saveModel.setModule(DERP_SYS.OREARTE_SYS_LOG_4);
	    saveModel.setCreateDate(TimeUtils.getNow());
	    saveModel.setOperateDate(TimeUtils.getNow());
	    saveModel.setOperateId(user.getId());
	    saveModel.setOperater(user.getName());
	    saveModel.setOperateRemark("");
	    saveModel.setOperateResult("");
	    saveModel.setOperateAction("新增");
	    saveModel.setRelId(id);
	    operateSysLogDao.save(saveModel) ;
		return true;
	}
	/**
	 * 新增商家仓库关系表公共方法
	 * @param depotIds
	 * @param goodsId
	 * @param merchantId
	 * @throws SQLException 
	 */
	private void saveMerchandiseDepotRelCom(String depotIds, Long goodsId, Long merchantId) throws SQLException {
		if (goodsId==null)return;
		//删除商品仓库关系数据
		List<Long> delGoodsIdList =new ArrayList<>();
		delGoodsIdList.add(goodsId);
		merchandiseDepotRelDao.deleteByGoodsId(delGoodsIdList);
		if (StringUtils.isNotBlank(depotIds)) {
			List<Long> depotIdsList = StrUtils.parseIds(depotIds);
			for (Long depotId : depotIdsList) {
	    		if (depotId==null) continue;
	    		MerchandiseDepotRelModel saveModel=new MerchandiseDepotRelModel();   		
	    		DepotInfoModel depotInfo = depotInfoDao.searchById(depotId);   		
	        	saveModel.setMerchantId(merchantId);
	        	saveModel.setGoodsId(goodsId);
	        	saveModel.setCreateDate(TimeUtils.getNow());
	    		saveModel.setDepotId(depotId);
	    		saveModel.setDepotName(depotInfo.getName());
	    		merchandiseDepotRelDao.save(saveModel);
			}
		}		
	}

	/**
	 * 推送复制商品到卓普信/宝丰/嘉宝
	 * @param json
	 * @throws Exception 
	 */
	private void sendCopyMerchandiseMQ(JSONObject json) throws Exception {
		// TODO Auto-generated method stub		
		SendResult send = rocketMQProducer.send(json.toString(), MQErpEnum.COPY_MERCHANDISE.getTopic(), MQErpEnum.COPY_MERCHANDISE.getTags());		
		if (send==null||!send.getSendStatus().name().equals("SEND_OK")) {
			throw new RuntimeException("推送商品复制数据失败");
		}	
	}

	/**
	 * 推送商品同步接口（通用库）
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	private JSONObject syncGoodsPushModel(MerchandiseInfoModel model,UnitInfoModel unitInfoModel,CountryOriginModel countryModel,String packType) throws Exception {
		
		// 去掉特殊字符(否则金融会报错)
		String goodsName = model.getName();
		goodsName=replcesString(goodsName);
		String spec = model.getSpec();
		spec=replcesString(spec);
		String englishGoodsName = model.getEnglishGoodsName();
		englishGoodsName=replcesString(englishGoodsName);
		String manufacturer = model.getManufacturer();		
		manufacturer=replcesString(manufacturer);
		String manufacturerAddr = model.getManufacturerAddr();
		manufacturerAddr=replcesString(manufacturerAddr);
		//替换特殊字符结束 
		
		String barcode = model.getBarcode();
		SyncGoodsPushRequest goodsRequest= new SyncGoodsPushRequest();
		goodsRequest.setGoodsName(goodsName);
		//goodsRequest.setGoodsSpec(spec);
		goodsRequest.setGoodsPacktype(packType);
		BigDecimal filingPrice = model.getFilingPrice();
		if (filingPrice!=null) {
			goodsRequest.setRecordPrice(Double.valueOf(filingPrice.toString()));
		}
		goodsRequest.setGoodsBarcode(barcode);
		goodsRequest.setGoodsEnName(englishGoodsName);
		//goodsRequest.setGoodsDese(model.getDescribe());
		goodsRequest.setGoodsQualityDays(model.getShelfLifeDays());
		goodsRequest.setProductComp(manufacturer);
		goodsRequest.setProductCompAddr(manufacturerAddr);
		goodsRequest.setQtpUnit(unitInfoModel.getName());
		goodsRequest.setKgs(model.getGrossWeight());
		goodsRequest.setNet(model.getNetWeight());
		goodsRequest.setCustassemCountry(countryModel.getName());
		goodsRequest.setMessageType("0");
		goodsRequest.setContractsUnit(unitInfoModel.getName());
		
		JSONObject jsonData =null;
		JSONObject jSONObject=null;
		try {
			jSONObject = JSONObject.fromObject(goodsRequest);
			// 调用
			String result = FinanceUtils.syncGoodsPush(jSONObject);
			jsonData = JSONObject.fromObject(result);
			if (jsonData==null||"".equals(jsonData))throw new RuntimeException("推送商品同步接口失败");
			String status = jsonData.getString("status");
			if (!"1".equals(status))throw new RuntimeException("推送商品同步接口失败");
			if (jsonData.get("commonBarcode")==null||StringUtils.isBlank(jsonData.getString("commonBarcode"))) {
				throw new RuntimeException("金融返回的标准条码为空");
			}			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("推送商品同步接口失败jsonData:"+jsonData);
		}
		
		return jsonData;
	}

	
	   /**
     * 替换特殊字符
     * @return
     */
    public  String replcesString(String str) {
		if (StringUtils.isEmpty(str)) return null;
		str = str.replace("%", ""); 		
		str= str.replace("+", ""); 	
		return str;

	}

	/**
	 * 推送主数据
	 * @throws Exception
	 */
	private void sendGoodsToSync(JSONObject requestJson) throws Exception {
		// 推送
		SendResult send = rocketMQProducer.send(requestJson.toString(), MQPushApiEnum.PUSH_MAINDATA.getTopic(), MQPushApiEnum.PUSH_MAINDATA.getTags());		
		if (send==null||!send.getSendStatus().name().equals("SEND_OK")) {
			throw new RuntimeException("推送主数据失败");
		}		
		
	}

	/**
	 * 同步商品到主数据 封装Jsong
	 * @param user
	 * @param model
	 * @throws Exception 
	 */
	private JSONObject sendGoodsToSyncRequest(User user, MerchandiseInfoModel model,
			MerchandiseCatModel merchandiseCat2,MerchandiseCatModel merchandiseCat1,BrandModel brandModel) throws Exception {
		// 原产国查询
		Long countyId = model.getCountyId();
		CountryOriginModel countryOriginModel=new CountryOriginModel();
		if (countyId!=null) {
			countryOriginModel = countryOriginDao.searchById(countyId);
		}		
		if (countryOriginModel==null) countryOriginModel=new CountryOriginModel();
		//计量单位查询
		Long unitId = model.getUnit();
		UnitInfoModel unitInfo=new UnitInfoModel(); 
		if (unitId!=null) {
			unitInfo = unitInfoDao.searchById(unitId);
		}
		if (unitInfo==null)unitInfo=new UnitInfoModel(); 
		SyncGoodsRequest request=new SyncGoodsRequest();
		request.setSyncType(1);//同步类型 枚举备注: 1:新增；2:更新
		request.setSubscriberCode("JFX");
		request.setTenantCode("zhuozhi");
		request.setProductName(model.getName());
		request.setClass1(merchandiseCat1.getSysCode());
		request.setClass2(merchandiseCat2.getSysCode());
		request.setIsUpdateProduct(0);
		request.setGoodsCode(model.getGoodsNo());
		request.setGoodsBusiCode(model.getGoodsCode());
		request.setGoodsName(model.getName());
		request.setMerchantCode(user.getTopidealCode());
		request.setGoodsEnName(model.getEnglishGoodsName());
		request.setProductCode(model.getCommbarcode());
		request.setBarCode(model.getBarcode());
		request.setUnit(unitInfo.getCode());
		request.setBrand(brandModel.getName());
		request.setChinaBrand(brandModel.getChinaBrandName());
		request.setEnglishBrand(brandModel.getEnglishBrandName());
		request.setCiqOriginCountry(countryOriginModel.getCode());
		if (model.getLength()!=null) {
			request.setLength(model.getLength()*10);	
		}
		if (model.getWidth()!=null) {
			request.setWidth(model.getWidth()*10);
		}
		if (model.getHeight()!=null) {
			request.setHeight(model.getHeight()*10);	
		}
		if (model.getVolume()!=null) {
			request.setVolume(model.getVolume()*1000);
		}
		if (model.getGrossWeight()!=null) {
			request.setGrossWeight(model.getGrossWeight()*1000);
		}
		if (model.getNetWeight()!=null) {
			request.setNetWeight(model.getNetWeight()*1000);
		}
		request.setSpec(model.getSpec());
		if (model.getFilingPrice()!=null) {
			request.setPrice(new BigDecimal("100").multiply(model.getFilingPrice()));
		}		
		request.setQualityDays(model.getShelfLifeDays());						
		
		JSONObject jsonObject = JSONObject.fromObject(request);
		jsonObject.put("order_id", model.getGoodsCode()) ;
		jsonObject.put("method", EpassAPIMethodEnum.EPASS_E07_METHOD.getMethod()) ;
		return jsonObject;
	}

	/**
	 * 修改商品
	 * 
	 * @param model
	 * @return
	 */
	@Override

	public boolean modifyMerchandise(User user,MerchandiseInfoModel model,String customsAreaId) throws Exception {
			
		// 根据商品分类ID获取商品分类信息
		MerchandiseCatModel twoCatModel = merchandiseCatDao.searchById(model.getProductTypeId());
		model.setProductTypeName(twoCatModel.getName());
		// 根据一级分类id查询一级分类
		MerchandiseCatModel oneCatModel = merchandiseCatDao.searchById(model.getProductTypeId0());
		model.setProductTypeName0(oneCatModel.getName());
		
		List<String>productCodes=new ArrayList<>();
		List<String>cCodes=new ArrayList<>();
		cCodes.add(user.getTopidealCode());
		productCodes.add(model.getGoodsNo());

		//修改商品
		int num = merchandiseDao.modify(model);
		//新增商家仓库关系表数据公共方法
		String depotIds = model.getDepotIds();
		saveMerchandiseDepotRelCom(depotIds, model.getId(),model.getMerchantId());
		// 之前的数据 获取商品信息其他字段为空 现在从新查询获取
		model = merchandiseDao.searchById(model.getId());
		
		MerchandiseCustomsRelModel relQuery=new MerchandiseCustomsRelModel();
		relQuery.setGoodsId(model.getId());
		List<MerchandiseCustomsRelModel> relQueryList = merchandiseCustomsRelDao.list(relQuery);
		List<Long>delIds=new ArrayList<>();
		for (MerchandiseCustomsRelModel relModel : relQueryList) {
			delIds.add(relModel.getId());
		}
		if (delIds.size()>0) {
			merchandiseCustomsRelDao.delete(delIds);
		}
		
		List<MerchandiseCustomsRelModel> relList=new ArrayList<>();
		// 封装 商品关区关系表数据
		if (StringUtils.isNotBlank(customsAreaId)) {
			List<String> customsAreaIdList = Arrays.asList(customsAreaId.split(","));
			for (String customsAreaIdStr : customsAreaIdList) {
				if (StringUtils.isBlank(customsAreaIdStr))continue;
				CustomsAreaConfigModel customsAreaConfig = customsAreaConfigDao.searchById(Long.valueOf(customsAreaIdStr));
				if (customsAreaConfig==null)customsAreaConfig=new CustomsAreaConfigModel();
				MerchandiseCustomsRelModel relModel=new MerchandiseCustomsRelModel();
				relModel.setMerchantId(model.getMerchantId());
				relModel.setMerchantName(model.getMerchantName());
				relModel.setGoodsId(model.getId());
				relModel.setGoodsNo(model.getGoodsNo());
				relModel.setCustomsAreaId(customsAreaConfig.getId());
				relModel.setCustomsAreaCode(customsAreaConfig.getCode());
				relModel.setCustomsAreaName(customsAreaConfig.getName());
				relModel.setCreateDate(TimeUtils.getNow());
				relList.add(relModel);
			}			
		}
		// 新增商品关区关系表数据
		for (MerchandiseCustomsRelModel relModel : relList) {
			merchandiseCustomsRelDao.save(relModel);
		}
		
		BrandModel brandModel = null ;
		
		if(model.getBrandId() != null) {
			brandModel = brandDao.searchById(model.getBrandId());
		}
		
		
		
		saveCommBarbarcode(model, brandModel, twoCatModel);
		// 日志
		OperateSysLogModel saveModel = new OperateSysLogModel() ;
	    saveModel.setModule(DERP_SYS.OREARTE_SYS_LOG_4);
	    saveModel.setCreateDate(TimeUtils.getNow());
	    saveModel.setOperateDate(TimeUtils.getNow());
	    saveModel.setOperateId(model.getModifier());
	    saveModel.setOperater(model.getUpdateName());
	    saveModel.setOperateRemark("");
	    saveModel.setOperateResult("");
	    saveModel.setOperateAction("编辑");
	    saveModel.setRelId(model.getId());
	    operateSysLogDao.save(saveModel) ;
		return true;
	}

	/**
	 * 导入
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override

	public Map importMerchandise(Map<Integer, List<List<Object>>> data,User user) throws Exception {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		// 通过商家id获取商家信息
		MerchantInfoModel merchant = merchantInfoDao.searchById(user.getMerchantId());
        if (merchant == null) {
            throw new RuntimeException("商家不存在");
        }
		int success = 0;
		// 校验列表导入货号是否重复
		Map<String, String>goodsNoCheckMap=new HashMap<>();
		List<JSONObject> reuqestJSONList=new ArrayList<>();
		// 商家货号所对应的商品
		Map<String, MerchandiseInfoModel>merchandiseInfoMap=new HashMap<>();
		// 商家货号所对应的分类
		Map<String, MerchandiseCatModel>merchandiseCat2Map=new HashMap<>();	
		// 商家货号所对应的品牌
		Map<String, BrandModel>brandModelMap=new HashMap<>();
		// 关区
		Map<String, String>customsAreaMap=new HashMap<>();
		Map<String, List<MerchandiseDepotRelModel>>merchandiseDepotRelMap=new HashMap<String,  List<MerchandiseDepotRelModel>>();
		List<String>productCodes=new ArrayList<>();
		List<String>cCodes=new ArrayList<>();
		cCodes.add(merchant.getTopidealCode());
		// 数据
		for (int i = 0; i < 1; i++) {
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(i).get(0).toArray(), objects.get(j).toArray());
				try {
					String goodsNo = map.get("商品货号");
					String barcode = map.get("商品条形码");
					String name = map.get("商品名称");
					String customsArea=map.get("备案平台关区");
					String hsCode=map.get("HS编码");
					String spec=map.get("规格型号");
					String unitCode = map.get("计量单位");
					String countyCode=map.get("原产国");
					String component=map.get("商品成分");
					String factoryNo=map.get("企业商品编码");
					String declareFactor=map.get("申报要素");
					String packType=map.get("包装方式");
					String manufacturer=map.get("生产企业名称");
					String manufacturerAddr=map.get("生产厂家地址");
					String remark=map.get("备注");
					String describe=map.get("商品描述");					
					String englishGoodsName=map.get("商品英文名称");
					String grossWeightStr=map.get("商品毛重");					
					String netWeightStr=map.get("商品净重");
					String shelfLifeDaysStr=map.get("保质天数");
					String filingPriceStr=map.get("备案单价");	
					String lengthStr=map.get("长");
					String widthStr=map.get("宽");
					String heightStr=map.get("高");
					String volumeStr=map.get("体积");
					String boxToUnitStr=map.get("箱规");
					String torrToUnitStr=map.get("托规");
					String brandName=map.get("品牌");				
					String secondCategory=map.get("二级分类");
					String depotCodes=map.get("关联仓库");	

					if (StringUtils.isNotBlank(customsArea)) {
						List<String> customsAreaList = Arrays.asList(customsArea.split(","));
						String falg="";
						for (String customsName : customsAreaList) {
							if (StringUtils.isBlank(customsName))continue;
							CustomsAreaConfigModel customsAreaConfig=new CustomsAreaConfigModel();
							customsAreaConfig.setName(customsName);
							customsAreaConfig = customsAreaConfigDao.searchByModel(customsAreaConfig);
							if (customsAreaConfig==null){
								falg=falg+customsName+"没有查到关区,";
							}		
						}
						if (StringUtils.isNotBlank(falg)) {
							getImportErrorMessage(resultList,j,falg);
							continue;
						}
					}
					// 必填字段的校验					
					goodsNo = goodsNo.trim();										
					if (StringUtils.isBlank(goodsNo)) {
						getImportErrorMessage(resultList,j,"商品货号不能为空");
						continue;
					}
					// 存放商家和货号key
					String merchantGoodsKey=user.getMerchantId()+","+goodsNo;
					if (goodsNoCheckMap.containsKey(goodsNo)) {
						getImportErrorMessage(resultList,j,"列表中商品货号不能重复,货号:"+goodsNo);
						continue;
					}
					// 存储商家+货号
					goodsNoCheckMap.put(merchantGoodsKey, merchantGoodsKey);		
					productCodes.add(goodsNo);
					// 封装商品仓库关系表数据
					List<MerchandiseDepotRelModel>merchandiseDepotRelList=new ArrayList<>();
					if (StringUtils.isNotBlank(depotCodes)) {
						depotCodes=depotCodes.trim();
						List<String> depotCodeList = Arrays.asList(depotCodes.split(","));
						String depotCodeError="";
						for (String depotCode : depotCodeList) {
							if (StringUtils.isBlank(depotCode)) continue;
							DepotInfoDTO depotInfo=new DepotInfoDTO();
							depotInfo.setDepotCode(depotCode);
							depotInfo.setMerchantId(user.getMerchantId());	
							//查询商家仓库关系数据
							 List<DepotInfoDTO> depotInfoList = depotInfoDao.getSelectBeanByDTO(depotInfo);
							if (depotInfoList.size()==0) {
								depotCodeError=depotCodeError+","+depotCode;
								continue;
							}
							depotInfo = depotInfoList.get(0);	
							
							MerchandiseDepotRelModel merchandiseDepotRel=new MerchandiseDepotRelModel();
							merchandiseDepotRel.setDepotId(depotInfo.getId());
							merchandiseDepotRel.setDepotName(depotInfo.getName());
							merchandiseDepotRel.setMerchantId(user.getMerchantId());
							merchandiseDepotRelList.add(merchandiseDepotRel);// 封装商品仓库关系实体
						}
						if (StringUtils.isNotBlank(depotCodeError)) {
							getImportErrorMessage(resultList,j,"根据仓库自编码:"+depotCodeError+"没有查找到启用的仓库");
							continue;
						}
						merchandiseDepotRelMap.put(merchantGoodsKey, merchandiseDepotRelList);
					}
					
					
					
					if (StringUtils.isBlank(name)) {
						getImportErrorMessage(resultList,j,"商品名称不能为空");
						continue;
					}
					if (StringUtils.isBlank(unitCode)) {
						getImportErrorMessage(resultList,j,"计量单位不能为空");
						continue;
					}
					if (StringUtils.isBlank(spec)) {
						getImportErrorMessage(resultList,j,"规格型号不能为空");
						continue;
					}
					if(StringUtils.isBlank(brandName)){
						getImportErrorMessage(resultList,j,"品牌不能为空");
						continue;
					}
					if(StringUtils.isBlank(secondCategory)){
						getImportErrorMessage(resultList,j,"二级分类不能为空");
						continue;
					}
					brandName=brandName.trim();
					secondCategory=secondCategory.trim();
					UnitInfoModel unitInfo = new UnitInfoModel();
					unitInfo.setCode(unitCode);
					unitInfo = unitInfoDao.searchByModel(unitInfo);
					if (unitInfo == null) {
						getImportErrorMessage(resultList,j,"找不到该计量单位");
						continue;
					}
					if (StringUtils.isBlank(countyCode)) {
						getImportErrorMessage(resultList,j,"原产国不能为空");
						continue;
					}
					CountryOriginModel countryOrigin = new CountryOriginModel();
					countryOrigin.setCode(countyCode);
					countryOrigin = countryOriginDao.searchByModel(countryOrigin);
					if (countryOrigin == null) {
						getImportErrorMessage(resultList,j,"找不到该原产国");
						continue;
					}					
					
					// 是否为数字校验开始----------------------
					if(StringUtils.isNotBlank(lengthStr)&&!Character.isDigit(lengthStr.charAt(i))){
						getImportErrorMessage(resultList,j,"商品长度应为数字");
						continue;
					}
					if(StringUtils.isNotBlank(widthStr)&&!Character.isDigit(widthStr.charAt(i))){
						getImportErrorMessage(resultList,j,"商品宽度应为数字");
						continue;
					}
					if(StringUtils.isNotBlank(heightStr)&&!Character.isDigit(heightStr.charAt(i))){
						getImportErrorMessage(resultList,j,"商品高度应为数字");
						continue;
					}
					
					if(StringUtils.isNotBlank(volumeStr)&&!Character.isDigit(volumeStr.charAt(i))){
						getImportErrorMessage(resultList,j,"商品体积应为数字");
						continue;
					}
					if(StringUtils.isNotBlank(grossWeightStr)&&!Character.isDigit(grossWeightStr.charAt(i))){
						getImportErrorMessage(resultList,j,"商品毛重应为数字");
						continue;
					}			
					if(StringUtils.isNotBlank(netWeightStr)&&!Character.isDigit(netWeightStr.charAt(i))){
						getImportErrorMessage(resultList,j,"商品净重应为数字");
						continue;
					}
					if(StringUtils.isNotBlank(filingPriceStr)&&!Character.isDigit(filingPriceStr.charAt(i))){
						getImportErrorMessage(resultList,j,"备案单价应为数字");
						continue;
					}
					if(StringUtils.isNotBlank(shelfLifeDaysStr)&&!Character.isDigit(shelfLifeDaysStr.charAt(i))){
						getImportErrorMessage(resultList,j,"保质天数应为数字");
						continue;
					}
					if(StringUtils.isNotBlank(boxToUnitStr)&&!Character.isDigit(boxToUnitStr.charAt(i))){
						getImportErrorMessage(resultList,j,"箱规应为数字");
						continue;
					}
					if(StringUtils.isNotBlank(torrToUnitStr)&&!Character.isDigit(torrToUnitStr.charAt(i))){
						getImportErrorMessage(resultList,j,"托规应为数字");
						continue;
					}
					
					
					
					Double grossWeight=null;
					if (StringUtils.isNotBlank(grossWeightStr))grossWeight=Double.valueOf(grossWeightStr);	
					Double netWeight=null;
					if (StringUtils.isNotBlank(netWeightStr))netWeight=Double.valueOf(netWeightStr);
					Integer shelfLifeDays=null;
					if (StringUtils.isNotBlank(shelfLifeDaysStr))shelfLifeDays=Integer.valueOf(shelfLifeDaysStr);
					BigDecimal filingPrice=null;
					if (StringUtils.isNotBlank(filingPriceStr))filingPrice=new BigDecimal(filingPriceStr);				
					Double length=null;
					if (StringUtils.isNotBlank(lengthStr))length=Double.valueOf(lengthStr);
					Double width=null;
					if (StringUtils.isNotBlank(widthStr))width=Double.valueOf(widthStr);
					Double height=null;
					if (StringUtils.isNotBlank(heightStr))height=Double.valueOf(heightStr);
					Double volume=null;
					if (StringUtils.isNotBlank(volumeStr))volume=Double.valueOf(volumeStr);
					
					Integer boxToUnit=null;
					if (StringUtils.isNotBlank(boxToUnitStr))boxToUnit=Integer.valueOf(boxToUnitStr);
					Integer torrToUnit=null;
					if (StringUtils.isNotBlank(torrToUnitStr))torrToUnit=Integer.valueOf(torrToUnitStr);

					// 是否为数字校验结束----------------------

					
					/**
					 * 	推送通用商品库条码格式
					 *  a、纯数字长度=13或14位
					 *  b、纯数字长度=12位
					 *	c、长度为15位，前面14位为纯数字后面一位不是数字
					 *	d、长度为14位，前面13位为纯数字后面一位不是数字
					 *	e、长度为13位，前面12位为纯数字后面一位不是数字
					 *	f、纯数字长度=11位；
					 *	g、纯数字长度=8位且以9开头
					 */

					if (StringUtils.isBlank(barcode)) {
						getImportErrorMessage(resultList,j,"商品条形码不能为空");
						continue;
					}
					// 判断是否在数据库存在，是：跳过;否：新增
					MerchandiseInfoModel goodsRequest = new MerchandiseInfoModel();
					goodsRequest.setBarcode(barcode);
					goodsRequest.setName(name);
					goodsRequest.setSpec(spec);										
					goodsRequest.setFilingPrice(filingPrice);
					goodsRequest.setBarcode(barcode);
					goodsRequest.setEnglishGoodsName(englishGoodsName);
					goodsRequest.setDescribe(describe);
					goodsRequest.setShelfLifeDays(shelfLifeDays);					
					goodsRequest.setManufacturer(manufacturer);
					goodsRequest.setManufacturerAddr(manufacturerAddr);
					goodsRequest.setGrossWeight(grossWeight);
					goodsRequest.setNetWeight(netWeight);									
					PackTypeModel packTypeModel=new PackTypeModel();
					if (StringUtils.isNotBlank(packType)) {
						// 查询包装方式						
						packTypeModel.setCode(map.get("包装方式"));
						packTypeModel = packTypeDao.searchByModel(packTypeModel);
						if (packTypeModel == null) {							
							getImportErrorMessage(resultList,j,"找不到该包装方式");
							continue;
						}
					}
					if (packTypeModel==null) packTypeModel=new PackTypeModel();
					String packTypeName = packTypeModel.getName();
					JSONObject jsonData=null;
					try {
						jsonData = syncGoodsPushModel(goodsRequest, unitInfo, countryOrigin, packTypeName);
					} catch (Exception e) {
						getImportErrorMessage(resultList,j,"商品货号:"+goodsNo+"推送商品同步接口（通用库）失败,返回结果"+e.getMessage());
						continue;
					}
					
					
					Object notes = jsonData.get("notes");
					if (notes==null) {
						getImportErrorMessage(resultList,j,"调通用商品库链接失败");
						continue;
					}					
					String commonBarcode=null;
					if ("成功".equals(notes)) {
						commonBarcode = jsonData.getString("commonBarcode");	
					}else {
						commonBarcode=barcode;
					}
					if (StringUtils.isBlank(commonBarcode)) {
						getImportErrorMessage(resultList,j,"未获取到标准条码");
						continue;
					}

					// 获取二级分类
					MerchandiseCatModel merchandiseCat2 = new MerchandiseCatModel(); 
					merchandiseCat2.setSysCode(secondCategory);
					merchandiseCat2.setLevel(DERP_SYS.MERCHANDISECAT_LEVEL_1);//查询二级分类
			    	merchandiseCat2 = merchandiseCatDao.searchByModel(merchandiseCat2);
					if (merchandiseCat2==null) {
						getImportErrorMessage(resultList,j,secondCategory+":没有查询到二级分类");
						continue;
					}					

			    	//查询一级分类
			    	MerchandiseCatModel merchandiseCat1= new MerchandiseCatModel();
			    	if (merchandiseCat2.getParentId()!=null) {
			    		merchandiseCat1.setId(merchandiseCat2.getParentId());// 二级分类的父级id
						merchandiseCat1 = merchandiseCatDao.searchByModel(merchandiseCat1);
					}			    	
					if (merchandiseCat1==null) merchandiseCat1=new MerchandiseCatModel();
					

					// 品牌处理																	
					BrandModel brandModel=new  BrandModel();
					// 新增或修改品牌
					if (StringUtils.isNotBlank(brandName)) {						
						brandModel.setName(brandName);
						//保存新增和修改的品牌
						brandModelMap.put(merchantGoodsKey, brandModel);
					}
					// 判断是否在数据库存在，是：跳过;否：新增
					MerchandiseInfoModel model = new MerchandiseInfoModel();
					model.setGoodsNo(goodsNo);// 商品货号
					model.setMerchantId(user.getMerchantId());// 商家
					model = merchandiseDao.searchByModel(model);
					if (model != null && model.getId() != null) {
						getImportErrorMessage(resultList,j,"商品已存在");
						continue;
					}					
					// 保存
					MerchandiseInfoModel merchandiseInfo = new MerchandiseInfoModel();					
					String code = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ERP);
					merchandiseInfo.setPackType(packTypeModel.getCode());
					// 生成自编码
					merchandiseInfo.setCode(code);
					merchandiseInfo.setGoodsCode(code);
					merchandiseInfo.setName(name);
					merchandiseInfo.setEnglishGoodsName(englishGoodsName);
					merchandiseInfo.setBarcode(barcode);
					merchandiseInfo.setGoodsNo(goodsNo);
					merchandiseInfo.setCreateDate(TimeUtils.getNow());
					merchandiseInfo.setCreater(user.getId());
					merchandiseInfo.setMerchantId(user.getMerchantId());// 商家
					merchandiseInfo.setMerchantName(user.getMerchantName());
					merchandiseInfo.setCreateName(user.getName());
					merchandiseInfo.setIsGroup(DERP_SYS.MERCHANDISEINFO_ISGROUP_0);// 默认不是组合
					merchandiseInfo.setIsRecord(DERP_SYS.MERCHANDISEINFO_ISRECORD_0);// 默认不是备案
					merchandiseInfo.setUnitName(unitInfo.getName());
					merchandiseInfo.setCommbarcode(commonBarcode); 	// 标准条码
					merchandiseInfo.setFactoryNo(factoryNo);
					merchandiseInfo.setFilingPrice(filingPrice);
					merchandiseInfo.setProductTypeId(merchandiseCat2.getId());// 二级类目
					merchandiseInfo.setProductTypeName(merchandiseCat2.getName());// 二级类目
					merchandiseInfo.setProductTypeId0(merchandiseCat1.getId());// 一级类目
					merchandiseInfo.setProductTypeName0(merchandiseCat1.getName());// 一级类目

					merchandiseInfo.setSpec(spec);
					merchandiseInfo.setHsCode(hsCode);					
					if (countryOrigin!=null)merchandiseInfo.setCountyId(countryOrigin.getId());// 原厂国id
					if (unitInfo!=null)merchandiseInfo.setUnit(unitInfo.getId());// 标准单位
					merchandiseInfo.setGrossWeight(grossWeight);
					merchandiseInfo.setNetWeight(netWeight);
					merchandiseInfo.setShelfLifeDays(shelfLifeDays);				
					merchandiseInfo.setLength(length);
					merchandiseInfo.setWidth(width);
					merchandiseInfo.setHeight(height);				
					merchandiseInfo.setVolume(volume);
					merchandiseInfo.setManufacturer(manufacturer);
					merchandiseInfo.setManufacturerAddr(manufacturerAddr);
					merchandiseInfo.setDescribe(describe);					
					merchandiseInfo.setRemark(remark);
					merchandiseInfo.setComponent(component);
					merchandiseInfo.setBoxToUnit(boxToUnit);
					merchandiseInfo.setTorrToUnit(torrToUnit);
					merchandiseInfo.setDeclareFactor(declareFactor);

					merchandiseInfoMap.put(merchantGoodsKey, merchandiseInfo);	
					customsAreaMap.put(merchantGoodsKey, customsArea);
					JSONObject requestJson = sendGoodsToSyncRequest(user,merchandiseInfo,merchandiseCat2,merchandiseCat1,brandModel);
					reuqestJSONList.add(requestJson);
					
				} catch (SQLException e) {
					e.printStackTrace();		
					getImportErrorMessage(resultList,j,"(商品导入失败)数据异常");
				}
			}
		}	
		// 获取pms 商品信息
		Map<String, Map<String, Object>> pmsGoodsMap=new  HashMap<String, Map<String,Object>>();
		if (resultList.size()==0&&goodsNoCheckMap.size()>0) {
			 pmsGoodsMap = getPmsGoods(productCodes, cCodes);
		}
		
		List<JSONObject>jsonList=new ArrayList<>();
		// 没有失败才进行新增商品
		if (resultList.size()==0&&goodsNoCheckMap.size()>0) {
			Set<String> keySet = goodsNoCheckMap.keySet();			
			for (String merchantGoodsKey : keySet) {
				BrandModel brandModel = brandModelMap.get(merchantGoodsKey);
				BrandModel brandQuery=null;
				if (brandModel!=null) {
					brandQuery=new  BrandModel();
					brandQuery.setName(brandModel.getName());
					brandQuery = brandDao.searchByModel(brandQuery);
					if (brandQuery==null){
						brandDao.save(brandModel);
					}else {
						brandModel.setId(brandQuery.getId());
						brandModel.setModifyDate(TimeUtils.getNow());
						brandDao.modify(brandModel);
					}
					
				}
				// 新增商品附表
				if (brandModel!=null&&brandModel.getId()!=null) {
					// 查询品牌是否存在
					MerchantBrandRelModel merchantBrandRel = new MerchantBrandRelModel();
					merchantBrandRel.setBrandId(brandModel.getId());
					merchantBrandRel.setMerchantId(user.getMerchantId());
					merchantBrandRel = merchantBrandRelDao.searchByModel(merchantBrandRel);
					if (merchantBrandRel == null || merchantBrandRel.getId() == null) {// 不存在则新增
						merchantBrandRel = new MerchantBrandRelModel();
						merchantBrandRel.setBrandId(brandModel.getId());
						merchantBrandRel.setMerchantId(user.getMerchantId());
						merchantBrandRelDao.save(merchantBrandRel);
					}
				}
				if (brandModel==null)brandModel=new BrandModel();
				
				// 新增商品
				MerchandiseInfoModel merchandiseInfo = merchandiseInfoMap.get(merchantGoodsKey);
				
				if (isExistOutDepotMerchandise(user.getMerchantId(), merchandiseInfo.getBarcode())) {
					merchandiseInfo.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_1);
				} else {
					merchandiseInfo.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
				}
				merchandiseInfo.setBrandId(brandModel.getId());
				// 获取pms商品 毛重 净重
				Map<String, Object> map = pmsGoodsMap.get(merchandiseInfo.getGoodsNo());
				if (map!=null) {
					Double net = (Double) map.get("net");
					Double kgs = (Double) map.get("kgs");
					Long countryId = (Long) map.get("countryId");
					merchandiseInfo.setNetWeight(net);
					merchandiseInfo.setGrossWeight(kgs);
					merchandiseInfo.setCountyId(countryId);
				}

				//新增商品
			    Long id = merchandiseDao.save(merchandiseInfo);
			    // 新增商品仓库关系数据
			    
			    List<MerchandiseDepotRelModel> merchandiseDepotList = merchandiseDepotRelMap.get(merchantGoodsKey);	
			    // 新增商品仓库关系表数据
			    if (merchandiseDepotList!=null&&merchandiseDepotList.size()>0) {
			    	for (MerchandiseDepotRelModel model : merchandiseDepotList) {
			    		model.setGoodsId(id);
			    		merchandiseDepotRelDao.save(model);
					}
				}			    			    
				OperateSysLogModel saveModel = new OperateSysLogModel() ;
			    saveModel.setModule(DERP_SYS.OREARTE_SYS_LOG_4);
			    saveModel.setCreateDate(TimeUtils.getNow());
			    saveModel.setOperateDate(TimeUtils.getNow());
			    saveModel.setOperateId(user.getId());
			    saveModel.setOperater(user.getName());
			    saveModel.setOperateRemark("导入备注");
			    saveModel.setOperateResult("导入成功");
			    saveModel.setOperateAction("导入");
			    saveModel.setRelId(id);
			    operateSysLogDao.save(saveModel) ;
			    
			    // 存储推送商品复制
			    JSONObject json=new JSONObject();
				json.put("merchantId", merchandiseInfo.getMerchantId());
				json.put("goodsId", id);
				json.put("tag", "2");//"2" 页面/其他触发单个货号 "1" 定时器 
			    jsonList.add(json);

			    success=success+1;
			    if (id!=null) {
			    	MerchandiseCatModel merchandiseCat2 = merchandiseCat2Map.get(merchantGoodsKey);
			    	if (merchandiseCat2==null) merchandiseCat2= new MerchandiseCatModel();
			    	saveCommBarbarcode(merchandiseInfo, brandModel, merchandiseCat2);
				}
			    String customsArea = customsAreaMap.get(merchantGoodsKey);
			    if (StringUtils.isNotBlank(customsArea)) {
					List<String> customsAreaList = Arrays.asList(customsArea.split(","));
					for (String customsName : customsAreaList) {
						if (StringUtils.isBlank(customsName))continue;
						CustomsAreaConfigModel customsAreaConfig=new CustomsAreaConfigModel();
						customsAreaConfig.setName(customsName);
						customsAreaConfig = customsAreaConfigDao.searchByModel(customsAreaConfig);
						if (customsAreaConfig==null)continue;
						
						MerchandiseCustomsRelModel customsRel=new MerchandiseCustomsRelModel();
						customsRel.setMerchantId(merchandiseInfo.getMerchantId());
						customsRel.setMerchantName(merchandiseInfo.getMerchantName());
						customsRel.setGoodsId(merchandiseInfo.getId());
						customsRel.setGoodsNo(merchandiseInfo.getGoodsNo());
						customsRel.setCustomsAreaId(customsAreaConfig.getId());
						customsRel.setCustomsAreaCode(customsAreaConfig.getCode());
						customsRel.setCustomsAreaName(customsAreaConfig.getName());
						merchandiseCustomsRelDao.save(customsRel);
					}
				}
			    
			}			
		}
		//推送 主数据
		if (resultList.size()==0&&goodsNoCheckMap.size()>0) {			
			for (JSONObject jsonObject : reuqestJSONList) {
				sendGoodsToSync(jsonObject);
			}
		}
		//  推送复制商品到卓普信/宝丰/嘉宝		
		for (JSONObject json : jsonList) {
			sendCopyMerchandiseMQ(json);
		}
		
		
		
		Map map = new HashMap();
		map.put("success", success);
		map.put("pass", 0);
		map.put("failure", resultList.size());
		map.put("message", resultList);
		return map;
	}

	/**
	 * 调用PMS的商品查询接口 获取毛重 净重
	 * @return
	 * @throws SQLException 
	 */
	private Map<String, Map<String, Object>> getPmsGoods(List<String> productCodes,List<String> cCodes) throws SQLException {
		PMSGoodQueryRequest pmsRequest=new PMSGoodQueryRequest();
		pmsRequest.setProductCodes(productCodes);// 商品货号
		pmsRequest.setcCodes(cCodes);// 商家卓志编码
		pmsRequest.setQueryType("0");//枚举: 0,1枚举备注: 0:商品信息,1:+海关账册商品信息
		pmsRequest.setSource("JFX");
		pmsRequest.setRowsPerPage(300);// 每页3白条
		pmsRequest.setPageNo(0);
		pmsRequest.setCuiCode("zhuozhi");		
		JSONObject fromObject = JSONObject.fromObject(pmsRequest);
		String goodsStr=null;
		JSONObject reslutJson=null;
		Integer totalPageRows=0;
		try {
			goodsStr = PMSUtils.getGoods(fromObject);
			reslutJson = JSONObject.fromObject(goodsStr);
			totalPageRows =Integer.valueOf(reslutJson.getString("totalPageRows"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 商品封装
		Map<String, Map<String, Object>> pmsGoodsMap=new HashMap<>();
		for (int i = 0; i <totalPageRows; i++) {
			pmsRequest.setPageNo(i);
			
			goodsStr=null;
			reslutJson=null;
			JSONArray jsonArray=null;
			try {
				goodsStr = PMSUtils.getGoods(fromObject);
				if (StringUtils.isBlank(goodsStr)) continue;
				reslutJson = JSONObject.fromObject(goodsStr);		
				if (goodsStr!=null) jsonArray= reslutJson.getJSONArray("goodsInfo");
			} catch (Exception e) {
				e.printStackTrace();
			}
						
			if (jsonArray!=null) {
				for (Object object : jsonArray) {
					JSONObject jSONObject=(JSONObject) object;
					if (jSONObject.get("productCode")==null||StringUtils.isBlank(jSONObject.getString("productCode")))continue;
					String productCode = jSONObject.getString("productCode");//货号
					productCode=productCode.trim();
					if (pmsGoodsMap.containsKey(productCode))continue;
					Map<String, Object>map=new  HashMap<>();
					Double net=0.0;
					if (jSONObject.get("net")!=null&&StringUtils.isNotBlank(jSONObject.getString("net"))) {
						net=Double.valueOf(jSONObject.getString("net"));
					}
					Double kgs =0.0;
					if (jSONObject.get("kgs")!=null&&StringUtils.isNotBlank(jSONObject.getString("kgs"))) {
						kgs=Double.valueOf(jSONObject.getString("kgs"));
					}		

					CountryOriginModel countryOrigin=new CountryOriginModel();
					if (jSONObject.get("countryName")!=null&&StringUtils.isNotBlank(jSONObject.getString("countryName"))) {
						countryOrigin.setName(jSONObject.getString("countryName"));
						countryOrigin = countryOriginDao.searchByModel(countryOrigin);
					}
					if (countryOrigin==null)countryOrigin=new CountryOriginModel();

					map.put("net", net);
					map.put("kgs", kgs);
					map.put("countryId", countryOrigin.getId());
					pmsGoodsMap.put(productCode, map);
				}

			}
		}

		
		return pmsGoodsMap;
	}
	
	/**
	 * 获取异常信息
	 * @param resultList
	 * @param j
	 */
	private void getImportErrorMessage(List<ImportErrorMessage> resultList, int j, String errorMessage) {
		ImportErrorMessage message = new ImportErrorMessage();
		message.setRows(j + 1);
		message.setMessage(errorMessage);
		resultList.add(message);
	}


	/**
	 * 导入临时
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map importMerchandiseLinshi(Map<Integer, List<List<Object>>> data,User user) throws Exception {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		// 通过商家id获取商家信息
		MerchantInfoModel merchant = merchantInfoDao.searchById(user.getMerchantId());
        if (merchant == null) {
            throw new RuntimeException("商家不存在");
        }
		int success = 0;
		// 校验列表导入货号是否重复
		Map<String, String>goodsNoCheckMap=new HashMap<>();
		// 商家货号所对应的商品
		Map<String, MerchandiseInfoModel>merchandiseInfoMap=new HashMap<>();
		// 商家货号所对应的分类
		Map<String, MerchandiseCatModel>merchandiseCat2Map=new HashMap<>();	
		// 商家货号所对应的品牌
		Map<String, BrandModel>brandModelMap=new HashMap<>();
		// 关区
		Map<String, String>customsAreaMap=new HashMap<>();
		Map<String, List<MerchandiseDepotRelModel>>merchandiseDepotRelMap=new HashMap<String,  List<MerchandiseDepotRelModel>>();
		List<String>productCodes=new ArrayList<>();
		List<String>cCodes=new ArrayList<>();
		cCodes.add(merchant.getTopidealCode());
		// 数据
		for (int i = 0; i < 1; i++) {
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				Map<String, String> map = this.toMap(data.get(i).get(0).toArray(), objects.get(j).toArray());
				try {
					String goodsNo = map.get("商品货号");
					String barcode = map.get("商品条形码");
					String name = map.get("商品名称");
					String customsArea=map.get("备案平台关区");
					String hsCode=map.get("HS编码");
					String spec=map.get("规格型号");
					String unitCode = map.get("计量单位");
					String countyCode=map.get("原产国");
					String component=map.get("商品成分");
					String factoryNo=map.get("企业商品编码");
					String declareFactor=map.get("申报要素");
					String packType=map.get("包装方式");
					String manufacturer=map.get("生产企业名称");
					String manufacturerAddr=map.get("生产厂家地址");
					String remark=map.get("备注");
					String describe=map.get("商品描述");					
					String englishGoodsName=map.get("商品英文名称");
					String grossWeightStr=map.get("商品毛重");					
					String netWeightStr=map.get("商品净重");
					String shelfLifeDaysStr=map.get("保质天数");
					String filingPriceStr=map.get("备案单价");	
					String lengthStr=map.get("长");
					String widthStr=map.get("宽");
					String heightStr=map.get("高");
					String volumeStr=map.get("体积");
					String boxToUnitStr=map.get("箱规");
					String torrToUnitStr=map.get("托规");
					String commonBarcode=map.get("标准条码");
					String brandName=map.get("品牌");				
					String secondCategory=map.get("二级分类");
					String depotCodes=map.get("关联仓库");
					
					if (StringUtils.isNotBlank(customsArea)) {
						List<String> customsAreaList = Arrays.asList(customsArea.split(","));
						String falg="";
						for (String customsName : customsAreaList) {
							if (StringUtils.isBlank(customsName))continue;
							CustomsAreaConfigModel customsAreaConfig=new CustomsAreaConfigModel();
							customsAreaConfig.setName(customsName);
							customsAreaConfig = customsAreaConfigDao.searchByModel(customsAreaConfig);
							if (customsAreaConfig==null){
								falg=falg+customsName+"没有查到关区,";
							}		
						}
						if (StringUtils.isNotBlank(falg)) {
							getImportErrorMessage(resultList,j,falg);
							continue;
						}
					}
					
					if (StringUtils.isBlank(commonBarcode)) {
						getImportErrorMessage(resultList,j,"标准条码不能为空");
						continue;
					}					

					// 必填字段的校验					
					goodsNo = goodsNo.trim();										
					if (StringUtils.isBlank(goodsNo)) {
						getImportErrorMessage(resultList,j,"商品货号不能为空");
						continue;
					}
					// 存放商家和货号key
					String merchantGoodsKey=user.getMerchantId()+","+goodsNo;
					if (goodsNoCheckMap.containsKey(goodsNo)) {
						getImportErrorMessage(resultList,j,"列表中商品货号不能重复,货号:"+goodsNo);
						continue;
					}
					// 存储商家+货号
					goodsNoCheckMap.put(merchantGoodsKey, merchantGoodsKey);					
					if (StringUtils.isBlank(name)) {
						getImportErrorMessage(resultList,j,"商品名称不能为空");
						continue;
					}
					productCodes.add(goodsNo);
					
					// 封装商品仓库关系表数据
					List<MerchandiseDepotRelModel>merchandiseDepotRelList=new ArrayList<>();
					if (StringUtils.isNotBlank(depotCodes)) {
						depotCodes=depotCodes.trim();
						List<String> depotCodeList = Arrays.asList(depotCodes.split(","));
						String depotCodeError="";
						for (String depotCode : depotCodeList) {
							if (StringUtils.isBlank(depotCode)) continue;
							DepotInfoDTO depotInfo=new DepotInfoDTO();
							depotInfo.setDepotCode(depotCode);
							depotInfo.setMerchantId(user.getMerchantId());	
							//查询商家仓库关系数据
							 List<DepotInfoDTO> depotInfoList = depotInfoDao.getSelectBeanByDTO(depotInfo);
							if (depotInfoList.size()==0) {
								depotCodeError=depotCodeError+","+depotCode;
								continue;
							}
							depotInfo = depotInfoList.get(0);	
							
							MerchandiseDepotRelModel merchandiseDepotRel=new MerchandiseDepotRelModel();
							merchandiseDepotRel.setDepotId(depotInfo.getId());
							merchandiseDepotRel.setDepotName(depotInfo.getName());
							merchandiseDepotRel.setMerchantId(user.getMerchantId());
							merchandiseDepotRelList.add(merchandiseDepotRel);// 封装商品仓库关系实体
						}
						if (StringUtils.isNotBlank(depotCodeError)) {
							getImportErrorMessage(resultList,j,"根据仓库自编码:"+depotCodeError+"没有查找到启用的仓库");
							continue;
						}
						merchandiseDepotRelMap.put(merchantGoodsKey, merchandiseDepotRelList);
					}
					
					
					if (StringUtils.isBlank(unitCode)) {
						getImportErrorMessage(resultList,j,"计量单位不能为空");
						continue;
					}
					if (StringUtils.isBlank(spec)) {
						getImportErrorMessage(resultList,j,"规格型号不能为空");
						continue;
					}
					if(StringUtils.isBlank(brandName)){
						getImportErrorMessage(resultList,j,"品牌不能为空");
						continue;
					}
					if(StringUtils.isBlank(secondCategory)){
						getImportErrorMessage(resultList,j,"二级分类不能为空");
						continue;
					}
					brandName=brandName.trim();
					secondCategory=secondCategory.trim();
					
					UnitInfoModel unitInfo = new UnitInfoModel();
					unitInfo.setCode(unitCode);
					unitInfo = unitInfoDao.searchByModel(unitInfo);
					if (unitInfo == null) {
						getImportErrorMessage(resultList,j,"找不到该计量单位");
						continue;
					}
					if (StringUtils.isBlank(countyCode)) {
						getImportErrorMessage(resultList,j,"原产国不能为空");
						continue;
					}
					CountryOriginModel countryOrigin = new CountryOriginModel();
					countryOrigin.setCode(countyCode);
					countryOrigin = countryOriginDao.searchByModel(countryOrigin);
					if (countryOrigin == null) {
						getImportErrorMessage(resultList,j,"找不到该原产国");
						continue;
					}					

					// 是否为数字校验开始----------------------
					if(StringUtils.isNotBlank(lengthStr)&&!Character.isDigit(lengthStr.charAt(i))){
						getImportErrorMessage(resultList,j,"商品长度应为数字");
						continue;
					}
					if(StringUtils.isNotBlank(widthStr)&&!Character.isDigit(widthStr.charAt(i))){
						getImportErrorMessage(resultList,j,"商品宽度应为数字");
						continue;
					}
					if(StringUtils.isNotBlank(heightStr)&&!Character.isDigit(heightStr.charAt(i))){
						getImportErrorMessage(resultList,j,"商品高度应为数字");
						continue;
					}
					
					if(StringUtils.isNotBlank(volumeStr)&&!Character.isDigit(volumeStr.charAt(i))){
						getImportErrorMessage(resultList,j,"商品体积应为数字");
						continue;
					}
					if(StringUtils.isNotBlank(grossWeightStr)&&!Character.isDigit(grossWeightStr.charAt(i))){
						getImportErrorMessage(resultList,j,"商品毛重应为数字");
						continue;
					}			
					if(StringUtils.isNotBlank(netWeightStr)&&!Character.isDigit(netWeightStr.charAt(i))){
						getImportErrorMessage(resultList,j,"商品净重应为数字");
						continue;
					}
					if(StringUtils.isNotBlank(filingPriceStr)&&!Character.isDigit(filingPriceStr.charAt(i))){
						getImportErrorMessage(resultList,j,"备案单价应为数字");
						continue;
					}
					if(StringUtils.isNotBlank(shelfLifeDaysStr)&&!Character.isDigit(shelfLifeDaysStr.charAt(i))){
						getImportErrorMessage(resultList,j,"保质天数应为数字");
						continue;
					}
					if(StringUtils.isNotBlank(boxToUnitStr)&&!Character.isDigit(boxToUnitStr.charAt(i))){
						getImportErrorMessage(resultList,j,"箱规应为数字");
						continue;
					}
					if(StringUtils.isNotBlank(torrToUnitStr)&&!Character.isDigit(torrToUnitStr.charAt(i))){
						getImportErrorMessage(resultList,j,"托规应为数字");
						continue;
					}
					
					
					Double grossWeight=null;
					if (StringUtils.isNotBlank(grossWeightStr))grossWeight=Double.valueOf(grossWeightStr);	
					Double netWeight=null;
					if (StringUtils.isNotBlank(netWeightStr))netWeight=Double.valueOf(netWeightStr);
					Integer shelfLifeDays=null;
					if (StringUtils.isNotBlank(shelfLifeDaysStr))shelfLifeDays=Integer.valueOf(shelfLifeDaysStr);
					BigDecimal filingPrice=null;
					if (StringUtils.isNotBlank(filingPriceStr))filingPrice=new BigDecimal(filingPriceStr);				
					Double length=null;
					if (StringUtils.isNotBlank(lengthStr))length=Double.valueOf(lengthStr);
					Double width=null;
					if (StringUtils.isNotBlank(widthStr))width=Double.valueOf(widthStr);
					Double height=null;
					if (StringUtils.isNotBlank(heightStr))height=Double.valueOf(heightStr);
					Double volume=null;
					if (StringUtils.isNotBlank(volumeStr))volume=Double.valueOf(volumeStr);
					
					Integer boxToUnit=null;
					if (StringUtils.isNotBlank(boxToUnitStr))boxToUnit=Integer.valueOf(boxToUnitStr);
					Integer torrToUnit=null;
					if (StringUtils.isNotBlank(torrToUnitStr))torrToUnit=Integer.valueOf(torrToUnitStr);

					// 是否为数字校验结束----------------------

					
					/**
					 * 	推送通用商品库条码格式
					 *  a、纯数字长度=13或14位
					 *  b、纯数字长度=12位
					 *	c、长度为15位，前面14位为纯数字后面一位不是数字
					 *	d、长度为14位，前面13位为纯数字后面一位不是数字
					 *	e、长度为13位，前面12位为纯数字后面一位不是数字
					 *	f、纯数字长度=11位；
					 *	g、纯数字长度=8位且以9开头
					 */

					if (StringUtils.isBlank(barcode)) {
						getImportErrorMessage(resultList,j,"商品条形码不能为空");
						continue;
					}
					PackTypeModel packTypeModel=new PackTypeModel();
					if (StringUtils.isNotBlank(packType)) {
						// 查询包装方式						
						packTypeModel.setCode(map.get("包装方式"));
						packTypeModel = packTypeDao.searchByModel(packTypeModel);
						if (packTypeModel == null) {							
							getImportErrorMessage(resultList,j,"找不到该包装方式");
							continue;
						}
					}
										
					BrandModel brandModel=new  BrandModel();
					if (StringUtils.isNotBlank(brandName)) {
						brandModel.setName(brandName);
						//保存新增和修改的品牌
						brandModelMap.put(merchantGoodsKey, brandModel);
					}					

					// 获取二级分类
					MerchandiseCatModel merchandiseCat2 = new MerchandiseCatModel();					
			    	merchandiseCat2.setSysCode(secondCategory);
			    	merchandiseCat2 = merchandiseCatDao.searchByModel(merchandiseCat2);
					if (merchandiseCat2==null) {
						getImportErrorMessage(resultList,j,secondCategory+":没有查询到二级分类");
						continue;
					}					
					// 通过二级分类找一级分类
					MerchandiseCatModel merchandiseCat1= new MerchandiseCatModel();
			    	if (merchandiseCat2!=null&&merchandiseCat2.getParentId()!=null) {   		
			    		merchandiseCat1.setId(merchandiseCat2.getParentId());// 二级分类的父级id
			    		merchandiseCat1 = merchandiseCatDao.searchByModel(merchandiseCat1);
					}
			    	if (merchandiseCat2==null) merchandiseCat2=new MerchandiseCatModel();
			    	if (merchandiseCat1==null) merchandiseCat1=new MerchandiseCatModel();
			    	//存放二级分类
			    	merchandiseCat2Map.put(merchantGoodsKey, merchandiseCat2);
														
					// 判断是否在数据库存在，是：跳过;否：新增
					MerchandiseInfoModel model = new MerchandiseInfoModel();
					model.setGoodsNo(goodsNo);// 商品货号
					model.setMerchantId(user.getMerchantId());// 商家
					model = merchandiseDao.searchByModel(model);
					if (model != null && model.getId() != null) {
						getImportErrorMessage(resultList,j,"商品已存在");
						continue;
					}					
					// 保存
					MerchandiseInfoModel merchandiseInfo = new MerchandiseInfoModel();					
					String code = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_ERP);
					merchandiseInfo.setPackType(packTypeModel.getCode());
					// 生成自编码
					merchandiseInfo.setCode(code);
					merchandiseInfo.setGoodsCode(code);
					merchandiseInfo.setName(name);
					merchandiseInfo.setEnglishGoodsName(englishGoodsName);
					merchandiseInfo.setBarcode(barcode);
					merchandiseInfo.setGoodsNo(goodsNo);
					merchandiseInfo.setCreateDate(TimeUtils.getNow());
					merchandiseInfo.setCreater(user.getId());
					merchandiseInfo.setMerchantId(user.getMerchantId());// 商家
					merchandiseInfo.setMerchantName(user.getMerchantName());
					merchandiseInfo.setCreateName(user.getName());
					merchandiseInfo.setIsGroup(DERP_SYS.MERCHANDISEINFO_ISGROUP_0);// 默认不是组合
					merchandiseInfo.setIsRecord(DERP_SYS.MERCHANDISEINFO_ISRECORD_0);// 默认不是备案
					merchandiseInfo.setUnitName(unitInfo.getName());
					merchandiseInfo.setCommbarcode(commonBarcode); 	// 标准条码
					merchandiseInfo.setFactoryNo(factoryNo);
					merchandiseInfo.setFilingPrice(filingPrice);
					merchandiseInfo.setProductTypeId(merchandiseCat2.getId());// 二级类目
					merchandiseInfo.setProductTypeName(merchandiseCat2.getName());// 二级类目
					merchandiseInfo.setProductTypeId0(merchandiseCat1.getId());// 一级类目
					merchandiseInfo.setProductTypeName0(merchandiseCat1.getName());// 一级类目

					merchandiseInfo.setSpec(spec);
					merchandiseInfo.setHsCode(hsCode);					
					if (countryOrigin!=null)merchandiseInfo.setCountyId(countryOrigin.getId());// 原厂国id
					if (unitInfo!=null)merchandiseInfo.setUnit(unitInfo.getId());// 标准单位
					merchandiseInfo.setGrossWeight(grossWeight);
					merchandiseInfo.setNetWeight(netWeight);
					merchandiseInfo.setShelfLifeDays(shelfLifeDays);				
					merchandiseInfo.setLength(length);
					merchandiseInfo.setWidth(width);
					merchandiseInfo.setHeight(height);				
					merchandiseInfo.setVolume(volume);
					merchandiseInfo.setManufacturer(manufacturer);
					merchandiseInfo.setManufacturerAddr(manufacturerAddr);
					merchandiseInfo.setDescribe(describe);					
					merchandiseInfo.setRemark(remark);
					merchandiseInfo.setComponent(component);
					merchandiseInfo.setBoxToUnit(boxToUnit);
					merchandiseInfo.setTorrToUnit(torrToUnit);
					merchandiseInfo.setDeclareFactor(declareFactor);
					merchandiseInfoMap.put(merchantGoodsKey, merchandiseInfo);	
					customsAreaMap.put(merchantGoodsKey, customsArea);

				} catch (SQLException e) {
					e.printStackTrace();		
					getImportErrorMessage(resultList,j,"(商品导入失败)数据异常");
				}
			}
		}	
		
		// 获取pms 商品信息
		Map<String, Map<String, Object>> pmsGoodsMap=new  HashMap<String, Map<String,Object>>();
		if (resultList.size()==0&&goodsNoCheckMap.size()>0) {
			 pmsGoodsMap = getPmsGoods(productCodes, cCodes);
		}
		List<JSONObject>jsonList=new ArrayList<>();
		// 没有失败才进行新增商品
		if (resultList.size()==0&&goodsNoCheckMap.size()>0) {
			Set<String> keySet = goodsNoCheckMap.keySet();			
			for (String merchantGoodsKey : keySet) {
				BrandModel brandModel = brandModelMap.get(merchantGoodsKey);
				BrandModel brandQuery=null;
				if (brandModel!=null) {
					brandQuery=new  BrandModel();
					brandQuery.setName(brandModel.getName());
					brandQuery = brandDao.searchByModel(brandQuery);
					if (brandQuery==null){
						brandDao.save(brandModel);
					}else {
						brandModel.setId(brandQuery.getId());
						brandModel.setModifyDate(TimeUtils.getNow());
						brandDao.modify(brandModel);
					}
					
				}
				// 新增商品附表
				if (brandModel!=null&&brandModel.getId()!=null) {
					// 查询品牌是否存在
					MerchantBrandRelModel merchantBrandRel = new MerchantBrandRelModel();
					merchantBrandRel.setBrandId(brandModel.getId());
					merchantBrandRel.setMerchantId(user.getMerchantId());
					merchantBrandRel = merchantBrandRelDao.searchByModel(merchantBrandRel);
					if (merchantBrandRel == null || merchantBrandRel.getId() == null) {// 不存在则新增
						merchantBrandRel = new MerchantBrandRelModel();
						merchantBrandRel.setBrandId(brandModel.getId());
						merchantBrandRel.setMerchantId(user.getMerchantId());
						merchantBrandRelDao.save(merchantBrandRel);
					}
				}
				if (brandModel==null)brandModel=new BrandModel();
				
				// 新增商品
				MerchandiseInfoModel merchandiseInfo = merchandiseInfoMap.get(merchantGoodsKey);
				
				if (isExistOutDepotMerchandise(user.getMerchantId(), merchandiseInfo.getBarcode())) {
					merchandiseInfo.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_1);
				} else {
					merchandiseInfo.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
				}
				merchandiseInfo.setBrandId(brandModel.getId());
				// 获取pms商品 毛重 净重
				Map<String, Object> map = pmsGoodsMap.get(merchandiseInfo.getGoodsNo());
				if (map!=null) {
					Double net = (Double) map.get("net");
					Double kgs = (Double) map.get("kgs");
					Long countryId = (Long) map.get("countryId");
					merchandiseInfo.setNetWeight(net);
					merchandiseInfo.setGrossWeight(kgs);
					merchandiseInfo.setCountyId(countryId);
				}

				
			    Long id = merchandiseDao.save(merchandiseInfo);
			    List<MerchandiseDepotRelModel> merchandiseDepotList = merchandiseDepotRelMap.get(merchantGoodsKey);	
			    // 新增商品仓库关系表数据
			    if (merchandiseDepotList!=null&&merchandiseDepotList.size()>0) {
			    	for (MerchandiseDepotRelModel model : merchandiseDepotList) {
			    		model.setGoodsId(id);
			    		merchandiseDepotRelDao.save(model);
					}
				}

			    //操作日志
				OperateSysLogModel saveModel = new OperateSysLogModel() ;
			    saveModel.setModule(DERP_SYS.OREARTE_SYS_LOG_4);
			    saveModel.setCreateDate(TimeUtils.getNow());
			    saveModel.setOperateDate(TimeUtils.getNow());
			    saveModel.setOperateId(user.getId());
			    saveModel.setOperater(user.getName());
			    saveModel.setOperateRemark("(导入备注)");
			    saveModel.setOperateResult("(导入成功)");
			    saveModel.setOperateAction("(导入)");
			    saveModel.setRelId(id);
			    operateSysLogDao.save(saveModel) ;
			    
			    JSONObject json=new JSONObject();
				json.put("merchantId", merchandiseInfo.getMerchantId());
				json.put("goodsId", id);
			    jsonList.add(json);
				
			    success=success+1;
			    if (id!=null) {
			    	MerchandiseCatModel merchandiseCat2 = merchandiseCat2Map.get(merchantGoodsKey);
			    	if (merchandiseCat2==null) merchandiseCat2= new MerchandiseCatModel();
			    	saveCommBarbarcode(merchandiseInfo, brandModel, merchandiseCat2);
				}
			    String customsArea = customsAreaMap.get(merchantGoodsKey);
			    if (StringUtils.isNotBlank(customsArea)) {
					List<String> customsAreaList = Arrays.asList(customsArea.split(","));
					for (String customsName : customsAreaList) {
						if (StringUtils.isBlank(customsName))continue;
						CustomsAreaConfigModel customsAreaConfig=new CustomsAreaConfigModel();
						customsAreaConfig.setName(customsName);
						customsAreaConfig = customsAreaConfigDao.searchByModel(customsAreaConfig);
						if (customsAreaConfig==null)continue;
						
						MerchandiseCustomsRelModel customsRel=new MerchandiseCustomsRelModel();
						customsRel.setMerchantId(merchandiseInfo.getMerchantId());
						customsRel.setMerchantName(merchandiseInfo.getMerchantName());
						customsRel.setGoodsId(merchandiseInfo.getId());
						customsRel.setGoodsNo(merchandiseInfo.getGoodsNo());
						customsRel.setCustomsAreaId(customsAreaConfig.getId());
						customsRel.setCustomsAreaCode(customsAreaConfig.getCode());
						customsRel.setCustomsAreaName(customsAreaConfig.getName());
						merchandiseCustomsRelDao.save(customsRel);
					}
				}
			    
			}			
		}
		//  推送复制商品到卓普信/宝丰/嘉宝		
		for (JSONObject json : jsonList) {
		sendCopyMerchandiseMQ(json);
		}
		
		Map map = new HashMap();
		map.put("success", success);
		map.put("pass", 0);
		map.put("failure", resultList.size());
		map.put("message", resultList);
		return map;
	}
	
	
	
	
	
	
	
	/**
	 * 把两个数组组成一个map
	 * 
	 * @param keys
	 *            键数组
	 * @param values
	 *            值数组
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

	/**
	 * 条件查询商品下拉列表
	 * 
	 * @param merchantId
	 */
	@Override
	public List<SelectBean> getSelectBean(Long merchantId, Long depotId) throws SQLException {
		DepotInfoModel depotInfo = depotInfoDao.searchById(depotId);
		if (!DERP_SYS.DEPOTINFO_TYPE_A.equals(depotInfo.getType())) {
			depotId = null;
		}
		return merchandiseDao.getSelectBean(merchantId, depotId);
	}

	/**
	 * 采购订单获取商品列表
	 * 
	 * @param model
	 *            商品信息 类型 1采购 2调拨
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public MerchandiseInfoModel getList(MerchandiseInfoModel model) throws SQLException {
		DepotInfoModel depotInfo = depotInfoDao.searchById(model.getDepotId());
		if (!DERP_SYS.DEPOTINFO_TYPE_A.equals(depotInfo.getType())) {
			model.setDepotId(null);
		}
		List ids = new ArrayList();
		// 卓志保税仓且是代销仓时，取代理商家商品
		if (DERP_SYS.DEPOTINFO_TYPE_A.equals(depotInfo.getType()) 
				&& DERP_SYS.DEPOTINFO_ISTOPBOOKS_1.equals(depotInfo.getIsTopBooks())) {
			ids.add(depotInfo.getMerchantId());
		}
		// 采购：只带出当前登录商家的商品
		// 调拨：带出当前登录商家、以及所有代理商家商品
		else {
			// 查询当前商家的所有代理商家
			MerchantRelModel merchantRel = new MerchantRelModel();
			merchantRel.setMerchantId(model.getMerchantId());
			List<MerchantRelModel> list = merchantRelDao.list(merchantRel);
			if (list != null && list.size() > 0) {
				for (MerchantRelModel rel : list) {
					ids.add(rel.getProxyMerchantId());
				}
			}
			model.setMerchantIds(ids);
			ids.add(model.getMerchantId());
		}
		model.setMerchantIds(ids);
		return merchandiseDao.getListToPurchaseByPage(model);
	}

	/**
	 * 根据仓库类型获取商品列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MerchandiseInfoModel getListForDepotType(MerchandiseInfoModel model) throws SQLException {
		// 类型为卓志保税仓时只查已备案商品
		DepotInfoModel depotInfo = depotInfoDao.searchById(model.getDepotId());
		if (DERP_SYS.DEPOTINFO_TYPE_A.equals(depotInfo.getType())) {
			model.setIsRecord(DERP_SYS.MERCHANDISEINFO_ISRECORD_1);
		}
		model.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);// 使用中
		/**
		 * 1、卓志保税仓且是代销仓时，取代理商家商品 2、卓志保税仓非代销仓时，取本商家商品 3、非卓志保税仓时，取商家的所有商品（含代理商家商品）
		 * 是否是代销仓(1-是,0-否)
		 */
		List ids = new ArrayList();
		// 1、卓志保税仓且是代销仓时，取代理商家商品
		if (DERP_SYS.DEPOTINFO_TYPE_A.equals(depotInfo.getType()) 
				&& depotInfo.getIsTopBooks().equals(DERP_SYS.DEPOTINFO_ISTOPBOOKS_1)) {
			// 查询当前商家的所有代理商家
			MerchantRelModel merchantRel = new MerchantRelModel();
			merchantRel.setMerchantId(model.getMerchantId());
			List<MerchantRelModel> list = merchantRelDao.list(merchantRel);
			if (list != null && list.size() > 0) {
				for (MerchantRelModel rel : list) {
					ids.add(rel.getProxyMerchantId());
				}
			}
		}
		// 2、卓志保税仓非代销仓时，取本商家商品
		else if (DERP_SYS.DEPOTINFO_TYPE_A.equals(depotInfo.getType()) 
				&& depotInfo.getIsTopBooks().equals(DERP_SYS.DEPOTINFO_ISTOPBOOKS_0)) {
			ids.add(model.getMerchantId());
		}
		// 3、非卓志保税仓时，取商家的所有商品（含代理商家商品）
		else if (!DERP_SYS.DEPOTINFO_TYPE_A.equals(depotInfo.getType())) {
			ids.add(model.getMerchantId());
			// 查询当前商家的所有代理商家
			MerchantRelModel merchantRel = new MerchantRelModel();
			merchantRel.setMerchantId(model.getMerchantId());
			List<MerchantRelModel> list = merchantRelDao.list(merchantRel);
			if (list != null && list.size() > 0) {
				for (MerchantRelModel rel : list) {
					ids.add(rel.getProxyMerchantId());
				}
			}
		}

		model.setMerchantIds(ids);
		return merchandiseDao.getListforMerchantIdByPage(model);
	}

	/**
	 * 根据商品id获取商品列表
	 */
	@Override
	public List<MerchandiseInfoDTO> getListByIds(List ids) throws SQLException {
		List<MerchandiseInfoModel> list = merchandiseDao.getListByIds(ids);
		List<MerchandiseInfoDTO> merchandiseInfoDTOS = new ArrayList<>();
		for (MerchandiseInfoModel model : list) {
			MerchandiseInfoDTO dto = new MerchandiseInfoDTO();
			BeanUtils.copyProperties(model, dto);
			if (StringUtils.isNotBlank(model.getCommbarcode())) {
				//根据该商品查询母品牌的申报单价系数
				BrandSuperiorModel brandSuperiorModel = brandSuperiorDao.getByCommbarcode(model.getCommbarcode());
				if (brandSuperiorModel != null) {
					Double priceDeclareRatio = brandSuperiorModel.getPriceDeclareRatio();
					dto.setPriceDeclareRatio(priceDeclareRatio);
					
					BigDecimal filingPrice = model.getFilingPrice();
					
					if(filingPrice != null 
							&& priceDeclareRatio != null) {
						dto.setSupplyMinPrice(filingPrice.multiply(new BigDecimal(priceDeclareRatio)).setScale(8, BigDecimal.ROUND_HALF_UP));
					}else if(filingPrice != null) {
						dto.setSupplyMinPrice(filingPrice.setScale(8, BigDecimal.ROUND_HALF_UP));
					}else {
						dto.setSupplyMinPrice(new BigDecimal(0)) ;
					}
				}
			}
			merchandiseInfoDTOS.add(dto);
		}
		return merchandiseInfoDTOS;
	}

	/**
	 * 销售订单获取商品列表
	 * 
	 * @param model
	 *            商品信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MerchandiseInfoModel getListByMerchantId(MerchandiseInfoModel model, Long outDepotId) throws SQLException {
		List ids = new ArrayList();
		if (outDepotId != null) {
			DepotInfoModel depotInfo = depotInfoDao.searchById(outDepotId);
			if (DERP_SYS.DEPOTINFO_TYPE_A.equals(depotInfo.getType())) {
				model.setIsRecord(DERP_SYS.MERCHANDISEINFO_ISRECORD_1);
			}
			if (!DERP_SYS.DEPOTINFO_TYPE_A.equals(depotInfo.getType()) 
					|| !DERP_SYS.DEPOTINFO_ISTOPBOOKS_1.equals(depotInfo.getIsTopBooks())) {
				ids.add(model.getMerchantId());
			}
		} else {
			ids.add(model.getMerchantId());
		}
		// 查询当前商家的所有代理商家
		MerchantRelModel merchantRel = new MerchantRelModel();
		merchantRel.setMerchantId(model.getMerchantId());
		List<MerchantRelModel> list = merchantRelDao.list(merchantRel);
		if (list != null && list.size() > 0) {
			for (MerchantRelModel rel : list) {
				ids.add(rel.getProxyMerchantId());
			}
		}
		model.setMerchantIds(ids);
		return merchandiseDao.getListforMerchantIdByPage(model);
	}

	/**
	 * 根据商家id获取备案商品列表
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public MerchandiseInfoModel getListByMerchant(MerchandiseInfoModel model) throws SQLException {
		List ids = new ArrayList();
		ids.add(model.getMerchantId());
		// 查询当前商家的所有代理商家
		MerchantRelModel merchantRel = new MerchantRelModel();
		merchantRel.setMerchantId(model.getMerchantId());
		List<MerchantRelModel> list = merchantRelDao.list(merchantRel);
		if (list != null && list.size() > 0) {
			for (MerchantRelModel rel : list) {
				ids.add(rel.getProxyMerchantId());
			}
		}
		model.setMerchantIds(ids);
		return merchandiseDao.getListForAddByPage(model);
	}

	/**
	 * 主数据修改商品
	 * 
	 * @param model
	 * @param groupIds
	 * @param groupNums
	 * @param groupPrices
	 * @return
	 * @throws SQLException
	 */
	@Override

	public boolean modifyMerchandiseByMain(MerchandiseInfoModel model, String groupIds, String groupNums,
			String groupPrices) throws Exception {
		
		/*MerchandiseCatModel merchandiseCatModel = new MerchandiseCatModel();*/
		// 根据一级分类id获取一级分类
		/*MerchandiseCatModel oneMerchandiseCat = merchandiseCatDao.searchById(model.getProductTypeId0());
		// 根据二级分类id 获取二级分类
		MerchandiseCatModel twoMerchandiseCat = merchandiseCatDao.searchById(model.getProductTypeId());
		merchandiseCatModel.setId(model.getProductTypeId());
		model.setGoodsClassifyName(merchandiseCatModel.getName());
		// 如果是组合，更改是否备案，要校验单品是否和组合一致
		if ("1".equals(model.getIsGroup())) {
			List<MerchandiseGroupRelModel> list = merchandiseGroupRelDao.getListByGroupId(model.getId());
			int flag = 0;
			for (MerchandiseGroupRelModel relModel : list) {
				if (!relModel.getIsRecord().equals(model.getIsRecord())) {
					flag = 1;
					break;
				}
			}
			if (flag == 1) {
				throw new RuntimeException("组合品和单品的是否备案必须保持一致");
			}
		}

		MerchandiseInfoModel merchandiseInfoModel = merchandiseDao.searchById(model.getId());		
		//MerchandiseCatModel merchandiseCatModel = new MerchandiseCatModel();
		ProductInfoModel productInfoModel= new ProductInfoModel();
		productInfoModel.setId(merchandiseInfoModel.getProductId());
		productInfoModel.setProductTypeId0(oneMerchandiseCat.getId());
		productInfoModel.setProductTypeName0(oneMerchandiseCat.getName());
		productInfoModel.setProductTypeId(twoMerchandiseCat.getId());
		productInfoModel.setProductTypeName(twoMerchandiseCat.getName());
	
    	int product_num = productInfoDao.modify(productInfoModel);
		int num = merchandiseDao.modify(model);
		if (num < 1) {
			return false;
		}
		 // 通过商家id获取商家信息
        Map<String, Object> merchantParams = new HashMap<String, Object>();
        merchantParams.put("merchantId", merchandiseInfoModel.getMerchantId());
        MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchantParams);
        if (merchant == null) {
            throw new RuntimeException("商家不存在");
        }
		// 若是嘉宝的新增商品发消息给ordermq
		if("0000138".equals(merchant.getTopidealCode())){
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("merchantId", merchant.getMerchantId());	// 商家ID
			jsonObj.put("merchantName", merchant.getName());	// 商家名称
			jsonObj.put("merchantCode", merchant.getCode());	// 商家编码
			jsonObj.put("goodsId", model.getId());			// 商品ID
			jsonObj.put("goodsNo", model.getGoodsNo());			// 商品货号
			jsonObj.put("goodsName", model.getName());			// 商品名称
			jsonObj.put("goodsCode", model.getGoodsCode());			// 商品编码
			jsonObj.put("commbarcode", model.getCommbarcode());		// 修改以后的标准条码
			jsonObj.put("oldCommbarcode", merchandiseInfoModel.getCommbarcode());		// 修改以前的标准条码
			jsonObj.put("operateType", "1");		// 操作类型 0:删除 1:增加/修改
			try{//orderMQ
				SendResult result = rocketMQProducer.send(jsonObj.toString(), MQOrderEnum.BJ_GOODS_TIME_CONFIG.getTopic(),MQOrderEnum.BJ_GOODS_TIME_CONFIG.getTags());
				LOGGER.info("宝洁商品货期配置MsgId:"+result.getMsgId());
			}catch(Exception e){
				e.printStackTrace();
				LOGGER.error("宝洁商品货期配置异常："+e.getMessage());
			}
		}

		productInfoModel = productInfoDao.searchById(merchandiseInfoModel.getProductId()) ;
		BrandModel brandModel = null ;
		
		if(productInfoModel.getBrandId() != null) {
			brandModel = brandDao.searchById(productInfoModel.getBrandId()) ;
		}
		
		saveCommBarbarcode(merchandiseInfoModel, brandModel, twoMerchandiseCat);
		
		// 重新生成组合
		if (groupIds != null && !"".equals(groupIds)) {
			// 根据组合id删除原有关系
			merchandiseGroupRelDao.delByGroupId(model.getId());
			// 删除mongo中组合id原有关系
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("groupGoodsId", model.getId());
			merchandiseGroupRelMongoDao.remove(params);
			String[] group_ids = groupIds.split(",");
			String[] group_nums = groupNums.split(",");
			String[] group_prices = groupPrices.split(",");
			for (int i = 0; i < group_ids.length; i++) {
				MerchandiseGroupRelModel relModel = new MerchandiseGroupRelModel();
				relModel.setGroupGoodsId(model.getId());// 组合id
				relModel.setGoodsId(Long.parseLong(group_ids[i]));// 商品id
				relModel.setCreater(model.getCreater());// 创建人
				relModel.setNum(Integer.parseInt(group_nums[i]));// 数量
				BigDecimal price = new BigDecimal(group_prices[i]);
				relModel.setPrice(price);// 价格
				merchandiseGroupRelDao.save(relModel);
			}
		}*/
		return true;
	}

	/**
	 * 公共的商品弹窗
	 * @param model
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public MerchandiseInfoModel getPopupList(MerchandiseInfoModel model) throws SQLException {
		return merchandiseDao.getPopupListByPage(model);
	}
	
	public List<MerchandiseInfoModel> getMerchandiseList(MerchandiseInfoModel model) throws SQLException{
		return merchandiseDao.list(model);
	}
	/**
	 * 上传图片
	 * @param fileName   文件名称
	 * @param fileBytes  文件字节数组
	 * @param fileSize   文件大小
	 * @param ext        文件后缀
	 * @param userId     用户id
	 * @param userName   用户名称
	 * @param id         商品Id
	 * @param type       用于标识上传图片的字段 1:商品图片
	 * @return
	 * @throws SQLException
	 */
	@Override

	public String uploadFile(String fileName, byte[] fileBytes, Long fileSize, String ext, Long userId, String userName,
			Long id, String type) throws SQLException {
		JSONObject jsonObject = new JSONObject();//推送内容
		jsonObject.put("fileTypeCode",SmurfsAPICodeEnum.UPLOAD_00003.getCode());
		jsonObject.put("fileName",fileName);
		jsonObject.put("fileBytes", Base64.encodeBase64String(fileBytes));
		jsonObject.put("fileExt",ext);
		jsonObject.put("fileSize",String.valueOf(fileSize));
		//调用外部接口上传图片
        String resultMsg=SmurfsUtils.send(jsonObject,SmurfsAPIEnum.SMURFS_UPLOAD_FILE);
        System.out.println(resultMsg);
        if(resultMsg == null || "".equals(resultMsg)){
        	throw new RuntimeException("蓝精灵上传图片失败");
        }
        JSONObject jsonObj = JSONObject.fromObject(resultMsg);
        if(jsonObj.getInt("code")!= 200){
        	throw new RuntimeException("蓝精灵上传图片失败");
        }
         //获取商品信息
        MerchandiseInfoModel model = new MerchandiseInfoModel();
        model.setId(id);
        model = merchandiseDao.searchById(id);
        if (model==null)return null;
        String url = jsonObj.getString("url");
       // String url="http://label.exp.esdex.com/group1/M00/05/F4/Cg0i_VzAHimAQqk0AAFlF8FmS80084.jpg";
        MerchandiseInfoModel updateModel=new MerchandiseInfoModel();
        updateModel.setId(id);
        updateModel.setModifyDate(TimeUtils.getNow());//图片修改日期
        updateModel.setImageId(userId);//图片修改人id
        updateModel.setImageName(userName);//图片修改人名称
		if ("1".equals(type)) {
			updateModel.setImageUrl1(url);	
		}else if ("2".equals(type)) {
			updateModel.setImageUrl2(url);
		}else if ("3".equals(type)) {
			updateModel.setImageUrl3(url);
		}else if ("4".equals(type)) {
			updateModel.setImageUrl4(url);
		}else if ("5".equals(type)) {
			updateModel.setImageUrl5(url);
		}		
		merchandiseDao.modify(updateModel);
        return url;
	}
	//通过id获取图片
	@Override
	public MerchandiseInfoModel searchMerchandise(Long id) throws SQLException{
		MerchandiseInfoModel model = new MerchandiseInfoModel();
		model.setId(id);
		List<MerchandiseInfoModel> merchandiseList = merchandiseDao.list(model); 
        if(merchandiseList != null && merchandiseList.size()>0){
        	model = merchandiseList.get(0);
        }else{
        	model = new MerchandiseInfoModel();
        }
		return model;
	}

	/**
	 * 客户销售价格弹框列表
	 */
	@Override
	public MerchandiseInfoModel getSaleListByPage(MerchandiseInfoModel model) throws SQLException {
		List ids = new ArrayList();
		ids.add(model.getMerchantId());
		// 查询当前商家的所有代理商家
		MerchantRelModel merchantRel = new MerchantRelModel();
		merchantRel.setMerchantId(model.getMerchantId());
		List<MerchantRelModel> list = merchantRelDao.list(merchantRel);
		if (list != null && list.size() > 0) {
			for (MerchantRelModel rel : list) {
				ids.add(rel.getProxyMerchantId());
			}
		}
		model.setMerchantIds(ids);
		return merchandiseDao.getSaleListByPage(model);
	}

	/**
	 * 根据商品id获取客户销售价格列表
	 */
	@Override
	public List<MerchandiseInfoModel> getSaleListByIds(List ids) throws SQLException {
		List<MerchandiseInfoModel> list = merchandiseDao.getSaleListByIds(ids);
		return list;
	}
	/**
	 * 结算价格-选择商品
	 * 获取商家下所有商品（条码相同只取一条记录）
	 * @param model
	 * @return
	 */
	@Override
	public MerchandiseInfoModel getListForSettlmentByPage(MerchandiseInfoModel model) throws SQLException {
		List<Long> ids = new ArrayList<Long>();
		// 查询当前商家的所有代理商家
		MerchantRelModel merchantRel = new MerchantRelModel();
		merchantRel.setMerchantId(model.getMerchantId());
		List<MerchantRelModel> list = merchantRelDao.list(merchantRel);
		if (list != null && list.size() > 0) {
			for (MerchantRelModel rel : list) {
				ids.add(rel.getProxyMerchantId());
			}
		}
		ids.add(model.getMerchantId());
		model.setMerchantIds(ids);
		return merchandiseDao.getListForSettlmentByPage(model);
	}

	/**
	 * 禁用/启用
	 */
	@Override

	public boolean auditMerchandies(MerchandiseInfoModel model,User user) throws SQLException {
		int num = merchandiseDao.modify(model);
		
		OperateSysLogModel saveModel = new OperateSysLogModel() ;
	    saveModel.setModule(DERP_SYS.OREARTE_SYS_LOG_4);
	    saveModel.setCreateDate(TimeUtils.getNow());
	    saveModel.setOperateDate(TimeUtils.getNow());
	    saveModel.setOperateId(user.getId());
	    saveModel.setOperater(user.getName());
	    String operateRemark=null;
	    String operateResult=null;
	    String operateAction=null;
	    if (DERP_SYS.MERCHANDISEINFO_STATUS_1.equals(model.getStatus())) {
	    	operateRemark="";
	    	operateResult="";
	    	operateAction="启用";
		}else {
			operateRemark="";
	    	operateResult="";
	    	operateAction="禁用";
		}
	    saveModel.setOperateRemark(operateRemark);
	    saveModel.setOperateResult(operateResult);
	    saveModel.setOperateAction(operateAction);
	    saveModel.setRelId(model.getId());
	    operateSysLogDao.save(saveModel) ;
		if (num >= 1) {
			return true;
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> getListByIdsAndOutInDepot(List<String> ids, String type, Long depotId, Long merchantId) throws SQLException {
		List<Map<String, Object>> merchandiseList = new ArrayList<>();
		//查询相关商品信息
		List<MerchandiseInfoModel> merchandiseInfoModels = merchandiseDao.getListByIds(ids);

		//查询仓库在该商家的选品限制
		DepotMerchantRelModel depotMerchantRelModel = new DepotMerchantRelModel();
		depotMerchantRelModel.setMerchantId(merchantId);
		depotMerchantRelModel.setDepotId(depotId);
		DepotMerchantRelModel relModel = depotMerchantRelDao.searchByModel(depotMerchantRelModel);

		List<Map<String, MerchandiseInfoModel>> merchandiseMapList = new ArrayList<>();
		for (MerchandiseInfoModel merchandise : merchandiseInfoModels) {
			Map<String, MerchandiseInfoModel> merchandiseInfoModelMap = new HashMap<>();
			//原商品信息
			merchandiseInfoModelMap.put("preGoods", merchandise);
			MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel();
			//选品限制为“仅备案商品”
			if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(relModel.getProductRestriction())) {
				merchandiseInfoModel.setCommbarcode(merchandise.getCommbarcode());
				merchandiseInfoModel.setIsRecord(DERP_SYS.MERCHANDISEINFO_ISRECORD_1);
				merchandiseInfoModel.setMerchantId(merchantId);
				List<MerchandiseInfoModel> isRecordList = merchandiseDao.getListByModel(merchandiseInfoModel);

				if (isRecordList != null && isRecordList.size() > 0) {
					List<String> goodsNoList = new ArrayList<>();
					for (MerchandiseInfoModel model : isRecordList) {
						goodsNoList.add(model.getGoodsNo());
					}
					if (goodsNoList.contains(merchandise.getGoodsNo())) {
						merchandiseInfoModelMap.put("otherGoods", merchandise);
					} else {
						merchandiseInfoModelMap.put("otherGoods", isRecordList.get(0));
					}
				} else {
					merchandiseInfoModelMap.put("otherGoods", null);
				}
			} else if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(relModel.getProductRestriction())) {//选品限制为“仅外仓统一码”
				merchandiseInfoModel.setCommbarcode(merchandise.getCommbarcode());
				merchandiseInfoModel.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
				merchandiseInfoModel.setMerchantId(merchantId);
				List<MerchandiseInfoModel> outDepotFlagList = merchandiseDao.getListByModel(merchandiseInfoModel);
				if (outDepotFlagList != null && outDepotFlagList.size() > 0) {
					merchandiseInfoModelMap.put("otherGoods", outDepotFlagList.get(0));
				} else {
					merchandiseInfoModelMap.put("otherGoods", null);
				}
			} else { //选品限制为“无限制”
				merchandiseInfoModelMap.put("otherGoods", merchandise);
			}
			merchandiseMapList.add(merchandiseInfoModelMap);
		}

		//3: 销售退 4: 调拨
		if ("3".equals(type)) {
			//退出商品货号需根据退入商品货号所在标准条码+该商家下的且符合退出仓库选品限制的商品货号（随机取符合条件即可，需包含退入商品货号）；
			for (Map<String, MerchandiseInfoModel> merchandiseInfoModelMap : merchandiseMapList) {
				MerchandiseInfoModel preMerchandise = merchandiseInfoModelMap.get("preGoods");
				MerchandiseInfoModel otherMerchandise = merchandiseInfoModelMap.get("otherGoods");

				Map<String, Object> map = new HashMap<>();
				map.put("inGoodsId", preMerchandise.getId());
				map.put("inGoodsCode", preMerchandise.getGoodsCode());
				map.put("inGoodsNo", preMerchandise.getGoodsNo());
				map.put("inGoodsName", preMerchandise.getName());
				map.put("inCommbarcode", preMerchandise.getCommbarcode());
				map.put("brandName", preMerchandise.getBrandName());
				map.put("grossWeight", preMerchandise.getGrossWeight());
				map.put("netWeight", preMerchandise.getNetWeight());
				map.put("barcode", preMerchandise.getBarcode());
				map.put("inFilingPrice", preMerchandise.getFilingPrice());
				if (otherMerchandise != null) {
					map.put("outGoodsId", otherMerchandise.getId());
					map.put("outGoodsNo", otherMerchandise.getGoodsNo());
					map.put("outGoodsName", otherMerchandise.getName());
					map.put("outGoodsCode", otherMerchandise.getGoodsCode());
					map.put("outCommbarcode", otherMerchandise.getCommbarcode());
				} else {
					map.put("outGoodsId", "");
					map.put("outGoodsNo", "");
					map.put("outGoodsName", "");
					map.put("outCommbarcode", "");
					map.put("outGoodsCode", "");
				}

				if (StringUtils.isNotBlank(preMerchandise.getCommbarcode())) {
					//根据该商品查询母品牌的申报单价系数
					BrandSuperiorModel brandSuperiorModel = brandSuperiorDao.getByCommbarcode(preMerchandise.getCommbarcode());
					if (brandSuperiorModel != null) {
						Double priceDeclareRatio = brandSuperiorModel.getPriceDeclareRatio();
						map.put("priceDeclareRatio", priceDeclareRatio);
					}
				}
				merchandiseList.add(map);
			}
		} else if ("4".equals(type)) {
			//调入商品货号需根据调出商品货号所在标准条码+该商家下的且符合调入仓库选品限制的商品货号（随机取符合条件即可，需包含调出商品货号）；
			for (Map<String, MerchandiseInfoModel> merchandiseInfoModelMap : merchandiseMapList) {
				MerchandiseInfoModel preMerchandise = merchandiseInfoModelMap.get("preGoods");
				MerchandiseInfoModel otherMerchandise = merchandiseInfoModelMap.get("otherGoods");

				Map<String, Object> map = new HashMap<>();
				map.put("outGoodsId", preMerchandise.getId());
				map.put("outGoodsNo", preMerchandise.getGoodsNo());
				map.put("outGoodsName", preMerchandise.getName());
				map.put("outGoodsCode", preMerchandise.getGoodsCode());
				map.put("outCommbarcode", preMerchandise.getCommbarcode());
				map.put("outFilingPrice", preMerchandise.getFilingPrice());
				map.put("outBarcode",preMerchandise.getBarcode());
				if (otherMerchandise != null) {
					map.put("inGoodsId", otherMerchandise.getId());
					map.put("inGoodsCode", otherMerchandise.getGoodsCode());
					map.put("inGoodsNo", otherMerchandise.getGoodsNo());
					map.put("inGoodsName", otherMerchandise.getName());
					map.put("inCommbarcode", otherMerchandise.getCommbarcode());
					map.put("brandName", otherMerchandise.getBrandName());
					map.put("grossWeight", otherMerchandise.getGrossWeight());
					map.put("netWeight", otherMerchandise.getNetWeight());
				} else {
					map.put("inGoodsId", "");
					map.put("inGoodsCode", "");
					map.put("inGoodsNo", "");
					map.put("inGoodsName", "");
					map.put("inCommbarcode", "");
					map.put("brandName", "");
					map.put("grossWeight", "");
					map.put("netWeight", "");
				}

				if (StringUtils.isNotBlank(preMerchandise.getCommbarcode())) {
					//根据该商品查询母品牌的申报单价系数
					BrandSuperiorModel brandSuperiorModel = brandSuperiorDao.getByCommbarcode(preMerchandise.getCommbarcode());
					if (brandSuperiorModel != null) {
						Double priceDeclareRatio = brandSuperiorModel.getPriceDeclareRatio();
						map.put("priceDeclareRatio", priceDeclareRatio);
					}
				}
				merchandiseList.add(map);
			}
		}

		return merchandiseList;
	}

	@Override
	public MerchandiseInfoDTO getOrderPopupList(MerchandiseInfoDTO dto) throws SQLException {
		MerchandiseInfoDTO merchandiseInfoDTO = merchandiseDao.getPopupListDTOByPage(dto);
		List<MerchandiseInfoDTO> merchandiseInfoDTOS = merchandiseInfoDTO.getList();

		if (dto.getMerchantId() != null) {
			for (MerchandiseInfoDTO infoDTO : merchandiseInfoDTOS) {
				MerchandiseCustomsRelModel merchandiseCustomsRelModel = new MerchandiseCustomsRelModel();
				merchandiseCustomsRelModel.setGoodsId(infoDTO.getId());

				List<MerchandiseCustomsRelModel> merchandiseCustomsRelModels = merchandiseCustomsRelDao.list(merchandiseCustomsRelModel);
				List<String> customsAreaNames = new ArrayList<>();
				for (MerchandiseCustomsRelModel relModel : merchandiseCustomsRelModels) {
					customsAreaNames.add(relModel.getCustomsAreaName());
				}

				if (!customsAreaNames.isEmpty()) {
					infoDTO.setCustomsAreaNames(StringUtils.join(customsAreaNames.toArray(), ","));
				}
			}
		}

		merchandiseInfoDTO.setList(merchandiseInfoDTOS);
		return merchandiseInfoDTO;
	}

	@Override
	public MerchandiseInfoDTO getOrderPopupDtoList(MerchandiseInfoDTO dto, String productRestriction,User user) throws Exception {
		//判断仓库是否为空，若为空，则查看是否有选品限制，根据选品限制查找商品列表
		if (dto.getDepotId() == null) {
			if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(productRestriction)) {
				dto.setIsRecord(DERP_SYS.MERCHANDISEINFO_ISRECORD_1);
			}

			if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(productRestriction)) {
				dto.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
			}
		} else {
			/**
			 * (1)当出库仓库在该商家下的“选品限制”为“仅备案商品”时，可选的商品为该商家下仅为备案商品；
			 * (2)当出库仓库在该商家下的“选品限制”为“仅外仓统一码”时，可选的商品为该商家下仅为标识为外仓统一码的商品货号；
			 * (3)当出库仓库在该商家下的“选品限制”为“无限制”时，可选的商品为该商家下所有的商品货号信息；
			 */
			DepotMerchantRelModel depotMerchantRelModel = new DepotMerchantRelModel();
			depotMerchantRelModel.setMerchantId(dto.getMerchantId());
			depotMerchantRelModel.setDepotId(dto.getDepotId());
			DepotMerchantRelModel relModel = depotMerchantRelDao.searchByModel(depotMerchantRelModel);
			if (relModel == null) {
				throw new RuntimeException("没有找到该仓库商家关联信息！");
			}

			if (dto.getId() != null) {
				MerchandiseInfoModel exist = merchandiseDao.searchById(dto.getId());
				if (exist == null) {
					throw new RuntimeException("没有找到该商品信息！");
				}
				dto.setCommbarcode(exist.getCommbarcode());
			}

			if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(relModel.getProductRestriction())) {
				dto.setIsRecord(DERP_SYS.MERCHANDISEINFO_ISRECORD_1);
			}

			if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(relModel.getProductRestriction())) {
				dto.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
			}
		}
		List ids = new ArrayList();
		ids.add(dto.getMerchantId());
		dto.setMerchantIds(ids);
		MerchandiseInfoDTO merchandiseInfoDTO = merchandiseDao.getPopupListDTOByPage(dto);
		List<MerchandiseInfoDTO> merchandiseInfoDTOS = merchandiseInfoDTO.getList();

		if (dto.getMerchantId() != null) {
			for (MerchandiseInfoDTO infoDTO : merchandiseInfoDTOS) {
				MerchandiseCustomsRelModel merchandiseCustomsRelModel = new MerchandiseCustomsRelModel();
				merchandiseCustomsRelModel.setGoodsId(infoDTO.getId());

				List<MerchandiseCustomsRelModel> merchandiseCustomsRelModels = merchandiseCustomsRelDao.list(merchandiseCustomsRelModel);
				List<String> customsAreaNames = new ArrayList<>();
				for (MerchandiseCustomsRelModel relModel : merchandiseCustomsRelModels) {
					customsAreaNames.add(relModel.getCustomsAreaName());
				}

				if (!customsAreaNames.isEmpty()) {
					infoDTO.setCustomsAreaNames(StringUtils.join(customsAreaNames.toArray(), ","));
				}
			}
		}

		merchandiseInfoDTO.setList(merchandiseInfoDTOS);
		return merchandiseInfoDTO;

	}

	@Override
	public List<UpdateDepotOrderDTO> getOrderPopupDtoList(UpdateDepotOrderForm form, User user) throws Exception {
		List<UpdateDepotOrderItemForm> itemList = form.getItemList();
		List<String> barcodeList = new ArrayList<>();

		for (UpdateDepotOrderItemForm itemForm : itemList) {
			barcodeList.add(itemForm.getBarcode());
		}

		//根据仓库+条码查询商品信息
		List<MerchandiseInfoModel> merchandiseInfoModels = merchandiseDao.getByDepotAndBarcode(form.getDepotId(), barcodeList, user.getMerchantId());

		Map<String, List<MerchandiseInfoModel>> barcodeMerchandiseMap = new HashMap<>();
		for (MerchandiseInfoModel merchandiseInfoModel : merchandiseInfoModels) {
			List<MerchandiseInfoModel> merchandiseInfoModelList = new ArrayList<>();

			if (barcodeMerchandiseMap.containsKey(merchandiseInfoModel.getBarcode())) {
				merchandiseInfoModelList.addAll(barcodeMerchandiseMap.get(merchandiseInfoModel.getBarcode()));
			}

			merchandiseInfoModelList.add(merchandiseInfoModel);
			barcodeMerchandiseMap.put(merchandiseInfoModel.getBarcode(), merchandiseInfoModelList);
		}

		//已选择的商品id集合
		List<Long> goodsIdsList = new ArrayList<>();
		List<UpdateDepotOrderDTO> returnDTOs = new ArrayList<>();
		for (UpdateDepotOrderItemForm itemForm : itemList) {
			UpdateDepotOrderDTO updateDepotOrderDTO = new UpdateDepotOrderDTO();
			updateDepotOrderDTO.setIndex(itemForm.getIndex());

			List<MerchandiseInfoModel> merchandiseInfoModelList = barcodeMerchandiseMap.get(itemForm.getBarcode());
			updateDepotOrderDTO.setMerchandiseInfoModel(new MerchandiseInfoModel());
			if (merchandiseInfoModelList != null) {
				for (MerchandiseInfoModel merchandise : merchandiseInfoModelList) {
					//如果这个商品已经选择，则重新取一个
					if (!goodsIdsList.contains(merchandise.getId())) {
						updateDepotOrderDTO.setMerchandiseInfoModel(merchandise);
						if (!form.getOrderType().matches("1|2")) {
							goodsIdsList.add(merchandise.getId());
						}
						break;
					}
				}
			}
			returnDTOs.add(updateDepotOrderDTO);
		}

		return returnDTOs;
	}

	@Override
	public List<Map<String, Object>> getListByIdsAndDepot(List<String> ids, String type, Long depotId, Long merchantId, String unNeedIds, User user) throws SQLException {

		List<Long> goodsIdsList = new ArrayList<>();
		if (StringUtils.isNotBlank(unNeedIds)) {
			goodsIdsList.addAll(StrUtils.parseIds(unNeedIds));
		}

		List<Map<String, Object>> merchandiseList = new ArrayList<>();
		//查询相关商品信息
		List<MerchandiseInfoModel> merchandiseInfoModels = merchandiseDao.getListByIds(ids);

		List<String> barcodeList = new ArrayList<>();

		for (MerchandiseInfoModel merchandiseInfoModel : merchandiseInfoModels) {
			if (!barcodeList.contains(merchandiseInfoModel.getBarcode())) {
				barcodeList.add(merchandiseInfoModel.getBarcode());
			}

		}

		//根据仓库+条码查询商品信息
		List<MerchandiseInfoModel> otherMerchandiseInfoModels = merchandiseDao.getByDepotAndBarcode(depotId, barcodeList, user.getMerchantId());

		Map<String, List<MerchandiseInfoModel>> barcodeMerMap = new HashMap<>();
		for (MerchandiseInfoModel merchandiseInfoModel : otherMerchandiseInfoModels) {
			List<MerchandiseInfoModel> merchandiseInfoModelList = new ArrayList<>();

			if (barcodeMerMap.containsKey(merchandiseInfoModel.getBarcode())) {
				merchandiseInfoModelList.addAll(barcodeMerMap.get(merchandiseInfoModel.getBarcode()));
			}

			merchandiseInfoModelList.add(merchandiseInfoModel);
			barcodeMerMap.put(merchandiseInfoModel.getBarcode(), merchandiseInfoModelList);
		}

		List<Map<String, MerchandiseInfoModel>> merchandiseMapList = new ArrayList<>();
		for (MerchandiseInfoModel merchandise : merchandiseInfoModels) {
			Map<String, MerchandiseInfoModel> merchandiseInfoModelMap = new HashMap<>();
			//原商品信息
			merchandiseInfoModelMap.put("preGoods", merchandise);
			merchandiseInfoModelMap.put("otherGoods", null);
			List<MerchandiseInfoModel> merchandiseInfoModelList = barcodeMerMap.get(merchandise.getBarcode());

			if (merchandiseInfoModelList != null) {
				for (MerchandiseInfoModel newMerchandise : merchandiseInfoModelList) {
					//如果这个商品已经选择，则重新取一个
					if (!goodsIdsList.contains(newMerchandise.getId())) {
						merchandiseInfoModelMap.put("otherGoods", newMerchandise);
						goodsIdsList.add(newMerchandise.getId());
						break;
					}
				}
			}

			merchandiseMapList.add(merchandiseInfoModelMap);
		}

		//3: 销售退 4: 调拨
		if ("3".equals(type)) {
			//退出商品货号需根据退入商品货号所在标准条码+该商家下的且符合退出仓库选品限制的商品货号（随机取符合条件即可，需包含退入商品货号）；
			for (Map<String, MerchandiseInfoModel> merchandiseInfoModelMap : merchandiseMapList) {
				MerchandiseInfoModel preMerchandise = merchandiseInfoModelMap.get("preGoods");
				MerchandiseInfoModel otherMerchandise = merchandiseInfoModelMap.get("otherGoods");

				Map<String, Object> map = new HashMap<>();
				map.put("inGoodsId", preMerchandise.getId());
				map.put("inGoodsCode", preMerchandise.getGoodsCode());
				map.put("inGoodsNo", preMerchandise.getGoodsNo());
				map.put("inGoodsName", preMerchandise.getName());
				map.put("inCommbarcode", preMerchandise.getCommbarcode());
				map.put("brandName", preMerchandise.getBrandName());
				map.put("grossWeight", preMerchandise.getGrossWeight());
				map.put("netWeight", preMerchandise.getNetWeight());
				map.put("barcode", preMerchandise.getBarcode());
				map.put("inFilingPrice", preMerchandise.getFilingPrice());
				if (otherMerchandise != null) {
					map.put("outGoodsId", otherMerchandise.getId());
					map.put("outGoodsNo", otherMerchandise.getGoodsNo());
					map.put("outGoodsName", otherMerchandise.getName());
					map.put("outGoodsCode", otherMerchandise.getGoodsCode());
					map.put("outCommbarcode", otherMerchandise.getCommbarcode());
				} else {
					map.put("outGoodsId", "");
					map.put("outGoodsNo", "");
					map.put("outGoodsName", "");
					map.put("outCommbarcode", "");
					map.put("outGoodsCode", "");
				}

				if (StringUtils.isNotBlank(preMerchandise.getCommbarcode())) {
					//根据该商品查询母品牌的申报单价系数
					BrandSuperiorModel brandSuperiorModel = brandSuperiorDao.getByCommbarcode(preMerchandise.getCommbarcode());
					if (brandSuperiorModel != null) {
						Double priceDeclareRatio = brandSuperiorModel.getPriceDeclareRatio();
						map.put("priceDeclareRatio", priceDeclareRatio);
					}
				}
				merchandiseList.add(map);
			}
		} else if ("4".equals(type)) {
			//调入商品货号需根据调出商品货号所在标准条码+该商家下的且符合调入仓库选品限制的商品货号（随机取符合条件即可，需包含调出商品货号）；
			for (Map<String, MerchandiseInfoModel> merchandiseInfoModelMap : merchandiseMapList) {
				MerchandiseInfoModel preMerchandise = merchandiseInfoModelMap.get("preGoods");
				MerchandiseInfoModel otherMerchandise = merchandiseInfoModelMap.get("otherGoods");

				Map<String, Object> map = new HashMap<>();
				map.put("outGoodsId", preMerchandise.getId());
				map.put("outGoodsNo", preMerchandise.getGoodsNo());
				map.put("outGoodsName", preMerchandise.getName());
				map.put("outGoodsCode", preMerchandise.getGoodsCode());
				map.put("outCommbarcode", preMerchandise.getCommbarcode());
				map.put("outFilingPrice", preMerchandise.getFilingPrice());
				map.put("outBarcode",preMerchandise.getBarcode());
				map.put("grossWeight", preMerchandise.getGrossWeight());
				map.put("netWeight", preMerchandise.getNetWeight());
				if (otherMerchandise != null) {
					map.put("inGoodsId", otherMerchandise.getId());
					map.put("inGoodsCode", otherMerchandise.getGoodsCode());
					map.put("inGoodsNo", otherMerchandise.getGoodsNo());
					map.put("inGoodsName", otherMerchandise.getName());
					map.put("inCommbarcode", otherMerchandise.getCommbarcode());
					map.put("brandName", otherMerchandise.getBrandName());
				} else {
					map.put("inGoodsId", "");
					map.put("inGoodsCode", "");
					map.put("inGoodsNo", "");
					map.put("inGoodsName", "");
					map.put("inCommbarcode", "");
					map.put("brandName", "");
				}

				if (StringUtils.isNotBlank(preMerchandise.getCommbarcode())) {
					//根据该商品查询母品牌的申报单价系数
					BrandSuperiorModel brandSuperiorModel = brandSuperiorDao.getByCommbarcode(preMerchandise.getCommbarcode());
					if (brandSuperiorModel != null) {
						Double priceDeclareRatio = brandSuperiorModel.getPriceDeclareRatio();
						map.put("priceDeclareRatio", priceDeclareRatio);
					}
				}
				merchandiseList.add(map);
			}
		}

		return merchandiseList;
	}

	@Override
	public List<UpdateDepotOrderDTO> getOrderPopupList(UpdateDepotOrderForm form, User user) throws Exception {
		List<UpdateDepotOrderItemForm> itemList = form.getItemList();
		List<String> barcodeList = new ArrayList<>();

		for (UpdateDepotOrderItemForm itemForm : itemList) {
			barcodeList.add(itemForm.getBarcode());
		}

		//根据仓库+条码查询商品信息
		List<MerchandiseInfoModel> merchandiseInfoModels = merchandiseDao.getByDepotAndBarcode(form.getDepotId(), barcodeList, user.getMerchantId());

		Map<String, List<MerchandiseInfoModel>> barcodeMerchandiseMap = new HashMap<>();
		for (MerchandiseInfoModel merchandiseInfoModel : merchandiseInfoModels) {
			List<MerchandiseInfoModel> merchandiseInfoModelList = new ArrayList<>();

			if (barcodeMerchandiseMap.containsKey(merchandiseInfoModel.getBarcode())) {
				merchandiseInfoModelList.addAll(barcodeMerchandiseMap.get(merchandiseInfoModel.getBarcode()));
			}

			merchandiseInfoModelList.add(merchandiseInfoModel);
			barcodeMerchandiseMap.put(merchandiseInfoModel.getBarcode(), merchandiseInfoModelList);
		}

		List<UpdateDepotOrderDTO> returnDTOs = new ArrayList<>();
		for (UpdateDepotOrderItemForm itemForm : itemList) {
			List<MerchandiseInfoModel> merchandiseInfoModelList = barcodeMerchandiseMap.get(itemForm.getBarcode());

			if (merchandiseInfoModelList == null) {
				merchandiseInfoModelList = new ArrayList<>();

			}

			UpdateDepotOrderDTO updateDepotOrderDTO = new UpdateDepotOrderDTO();
			updateDepotOrderDTO.setIndex(itemForm.getIndex());
			updateDepotOrderDTO.setMerchandiseList(merchandiseInfoModelList);
			returnDTOs.add(updateDepotOrderDTO);
		}

		return returnDTOs;
	}


	/**
     * 根据数据生成标准条码记录
     * @param merchandiseInfoModel
     * @param brandModel
     * @param merchandiseCatModel
     * @throws SQLException
     */
    private void saveCommBarbarcode(MerchandiseInfoModel merchandiseInfoModel , BrandModel brandModel ,MerchandiseCatModel merchandiseCatModel) throws SQLException {
    	
    	if(merchandiseCatModel == null) {
    		merchandiseCatModel = new MerchandiseCatModel() ;
    	}
    	
    	if(brandModel == null) {
    		brandModel = new BrandModel() ;
    	}
    	
    	//判断标准条码是否存在，若不存在，在标准条码管理新增一条未维护数据
    	CommbarcodeModel commbarcodeModel = new CommbarcodeModel() ;
    	commbarcodeModel.setCommbarcode(merchandiseInfoModel.getCommbarcode());
    	commbarcodeModel = commbarcodeDao.searchByModel(commbarcodeModel) ;
    	
    	Long commbarcodeId = null ;
    	
    	//不存在新增一条记录
    	if(commbarcodeModel == null ) {
    		commbarcodeModel = new CommbarcodeModel() ;
    		commbarcodeModel.setCommbarcode(merchandiseInfoModel.getCommbarcode());
    		commbarcodeModel.setCommGoodsName(merchandiseInfoModel.getName());
    		commbarcodeModel.setCommTypeId(merchandiseCatModel.getId());
    		commbarcodeModel.setCommTypeName(merchandiseCatModel.getName());
    		commbarcodeModel.setMaintainStatus(DERP_SYS.COMMBARCODE_MAINTAINSTATUS_0);
    		commbarcodeModel.setCreateDate(TimeUtils.getNow());
    		commbarcodeModel.setModifyDate(TimeUtils.getNow());
    		
    		commbarcodeId = commbarcodeDao.save(commbarcodeModel);
    		//新增表体
    		if(commbarcodeId != null) {
    			CommbarcodeItemModel commbarcodeItemModel = new CommbarcodeItemModel() ;
    			commbarcodeItemModel.setCommbarcodeId(commbarcodeId);
    			commbarcodeItemModel.setGoodsName(merchandiseInfoModel.getName());
    			commbarcodeItemModel.setGoodsCode(merchandiseInfoModel.getGoodsCode());
    			commbarcodeItemModel.setGoodsNo(merchandiseInfoModel.getGoodsNo());
    			commbarcodeItemModel.setGoodsId(merchandiseInfoModel.getId());
    			commbarcodeItemModel.setBarcode(merchandiseInfoModel.getBarcode());
    			commbarcodeItemModel.setBrandId(brandModel.getId());
    			commbarcodeItemModel.setBrandName(brandModel.getName());
    			commbarcodeItemModel.setTypeId(merchandiseCatModel.getId());
    			commbarcodeItemModel.setTypeName(merchandiseCatModel.getName());
    			commbarcodeItemModel.setCreateDate(TimeUtils.getNow());
    			commbarcodeItemModel.setModifyDate(TimeUtils.getNow());
    			commbarcodeItemModel.setOutDepotFlag(merchandiseInfoModel.getOutDepotFlag());

    			commbarcodeItemDao.save(commbarcodeItemModel) ;
    		}
    	}
    	//存在修改表体内容
    	else {
    		commbarcodeId = commbarcodeModel.getId() ;
    		//根据标准条码ID、商品货号查表体是否存在
    		CommbarcodeItemModel commbarcodeItemModel = new CommbarcodeItemModel() ;
    		commbarcodeItemModel.setCommbarcodeId(commbarcodeId);
    		commbarcodeItemModel.setGoodsId(merchandiseInfoModel.getId());
    		
    		commbarcodeItemModel = commbarcodeItemDao.searchByModel(commbarcodeItemModel) ;
    		
    		if(commbarcodeItemModel == null) {
    			commbarcodeItemModel = new CommbarcodeItemModel() ;
    			commbarcodeItemModel.setCommbarcodeId(commbarcodeId);
    			commbarcodeItemModel.setGoodsName(merchandiseInfoModel.getName());
    			commbarcodeItemModel.setGoodsCode(merchandiseInfoModel.getGoodsCode());
    			commbarcodeItemModel.setGoodsNo(merchandiseInfoModel.getGoodsNo());
    			commbarcodeItemModel.setGoodsId(merchandiseInfoModel.getId());
    			commbarcodeItemModel.setBarcode(merchandiseInfoModel.getBarcode());
    			commbarcodeItemModel.setBrandId(brandModel.getId());
    			commbarcodeItemModel.setBrandName(brandModel.getName());
    			commbarcodeItemModel.setTypeId(merchandiseCatModel.getId());
    			commbarcodeItemModel.setTypeName(merchandiseCatModel.getName());
    			commbarcodeItemModel.setCreateDate(TimeUtils.getNow());
    			commbarcodeItemModel.setModifyDate(TimeUtils.getNow());
				commbarcodeItemModel.setOutDepotFlag(merchandiseInfoModel.getOutDepotFlag());

    			commbarcodeItemDao.save(commbarcodeItemModel) ;
    		}else {
    			commbarcodeItemModel.setCommbarcodeId(commbarcodeId);
    			commbarcodeItemModel.setGoodsName(merchandiseInfoModel.getName());
    			commbarcodeItemModel.setGoodsCode(merchandiseInfoModel.getGoodsCode());
    			commbarcodeItemModel.setGoodsNo(merchandiseInfoModel.getGoodsNo());
    			commbarcodeItemModel.setGoodsId(merchandiseInfoModel.getId());
    			commbarcodeItemModel.setBarcode(merchandiseInfoModel.getBarcode());
    			commbarcodeItemModel.setBrandId(brandModel.getId());
    			commbarcodeItemModel.setBrandName(brandModel.getName());
    			commbarcodeItemModel.setTypeId(merchandiseCatModel.getId());
    			commbarcodeItemModel.setTypeName(merchandiseCatModel.getName());
    			commbarcodeItemModel.setModifyDate(TimeUtils.getNow());
				commbarcodeItemModel.setOutDepotFlag(merchandiseInfoModel.getOutDepotFlag());

    			commbarcodeItemDao.modify(commbarcodeItemModel) ;
    		}
    	}
    }

	/**
	 * @Description 判断同个商家、同个标准条码下是否存在“外仓统一码”为"是"的商品
	 * @param merchantId
	 * @param barcode
	 * @return
	 */
	private boolean isExistOutDepotMerchandise(Long merchantId, String barcode) throws SQLException {
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
		//判断商家是否是代理商家，若是则直接返回false, 打标“外仓统一码”为"否"
		if (merchantInfoModel.getIsProxy().equals(DERP_SYS.MERCHANTINFO_ISPROXY_1)) {
			return false;
		}
		MerchandiseInfoModel isExistOutDepotMerchandise = new MerchandiseInfoModel();
		isExistOutDepotMerchandise.setMerchantId(merchantId);
		isExistOutDepotMerchandise.setBarcode(barcode);
		isExistOutDepotMerchandise.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);
		isExistOutDepotMerchandise.setOutDepotFlag(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
		List<MerchandiseInfoModel> list = merchandiseDao.list(isExistOutDepotMerchandise);
		if (list == null||list.size()==0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<MerchandiseInfoDTO> exportList(MerchandiseInfoDTO dto) throws SQLException {
		List ids = new ArrayList();
		// 查询当前商家的所有代理商家
		MerchantRelModel merchantRel = new MerchantRelModel();
		merchantRel.setMerchantId(dto.getMerchantId());
		List<MerchantRelModel> list = merchantRelDao.list(merchantRel);
		if (list != null && list.size() > 0) {
			for (MerchantRelModel rel : list) {
				ids.add(rel.getProxyMerchantId());
			}
		}
		ids.add(dto.getMerchantId());
		dto.setMerchantIds(ids);
		dto.setPageSize(10000);
		dto.setBegin(0);
		List<MerchandiseInfoDTO> exportList=new ArrayList<>();
		while(true){
			List<MerchandiseInfoDTO>  exportListTemp = merchandiseDao.exportList(dto);
			dto.setBegin(dto.getBegin()+dto.getPageSize());
			if (exportListTemp==null||exportListTemp.size()==0)break;
			exportList.addAll(exportListTemp);
		}
		
		return exportList;
	}

	@Override
	public Map<String, Object> uploadImportImage(Map<String, MerchandiseInfoModel> barcodeMap, Long merchantId)throws SQLException {
		int success = 0;
		int pass = 0;
		int failure = 0;
		List<ImportErrorMessage> errorList = new ArrayList<ImportErrorMessage>();
		
		Set<String> keySet = barcodeMap.keySet();
		if (merchantId==null)return null;
		for (String barcode : keySet) {
			if (StringUtils.isBlank(barcode)){
				failure+=1;
				continue;
			}
			MerchandiseInfoModel update = barcodeMap.get(barcode);
			MerchandiseInfoModel query=new MerchandiseInfoModel();
			query.setMerchantId(merchantId);
			query.setBarcode(barcode);
			List<MerchandiseInfoModel> queryList = merchandiseDao.list(query);
			for (MerchandiseInfoModel merchandiseInfoModel : queryList) {
				update.setId(merchandiseInfoModel.getId());
				merchandiseDao.modify(update);
			}
			success+=1;
		}	
		Map map = new HashMap();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message", errorList);
		return map;
	}



	
}
