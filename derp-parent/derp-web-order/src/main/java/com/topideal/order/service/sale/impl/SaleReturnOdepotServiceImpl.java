package com.topideal.order.service.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.*;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.sale.SaleReturnDeclareOrderDTO;
import com.topideal.entity.dto.sale.SaleReturnOdepotDTO;
import com.topideal.entity.dto.sale.SaleReturnOdepotItemDTO;
import com.topideal.entity.vo.sale.SaleReturnOdepotItemModel;
import com.topideal.entity.vo.sale.SaleReturnOdepotModel;
import com.topideal.entity.vo.sale.SaleReturnOrderItemModel;
import com.topideal.entity.vo.sale.SaleReturnOrderModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.sale.SaleReturnOdepotService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 销售退货出库单service实现类
 */
@Service
public class SaleReturnOdepotServiceImpl implements SaleReturnOdepotService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(SaleReturnOdepotServiceImpl.class);
	// 销售退货出库单表头
	@Autowired
	private SaleReturnOdepotDao saleReturnOdepotDao;
	// 销售退货表头
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;
	// 销售退货出库单表体
	@Autowired
	private SaleReturnOdepotItemDao saleReturnOdepotItemDao;
	//财务经销存关账mongdb
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	//商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// mongoDB仓库
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private SaleReturnOrderItemDao saleReturnItemDao;
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private CommonBusinessService commonBusinessService ;
	@Autowired
	private SaleReturnDeclareOrderDao saleReturnDeclareOrderDao ;
	/**
	 * 列表（分页）
	 * @param model
	 * @return
	 */
	@Override
	public SaleReturnOdepotDTO listSaleReturnOdepotByPage(SaleReturnOdepotDTO dto, User user)
			throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return saleReturnOdepotDao.searchDTOByPage(dto);
	}

	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	@Override
	public SaleReturnOdepotDTO searchDetail(Long id) throws SQLException {
		SaleReturnOdepotDTO dto = new SaleReturnOdepotDTO();
		dto.setId(id);
		return saleReturnOdepotDao.searchDTOById(id);
	}


	/**
	 * 根据表头ID获取表体(包括商品信息)
	 * @param id
	 * @return
	 */
	@Override
	public List<SaleReturnOdepotItemDTO> listItemByOrderId(Long id) throws SQLException {
		SaleReturnOdepotItemDTO dto = new SaleReturnOdepotItemDTO();
		dto.setSreturnOdepotId(id);
		return saleReturnOdepotItemDao.listSaleReturnOdepotItemDTO(dto);
	}

	@Override
	public List<SaleReturnOdepotDTO> listSaleReturnOdepot(SaleReturnOdepotDTO dto, User user) throws SQLException{
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buIds.isEmpty()) {
				return new ArrayList<SaleReturnOdepotDTO>();
			}
			dto.setBuList(buIds);
		}
		return saleReturnOdepotDao.listSaleReturnOdepotDTO(dto);
	}

	@Override
	public boolean saveOdepotReport(String json, User user) throws Exception {
		String returnCode=null;	// 销售退货出库单号
		// 解析json
		JSONObject jsonObj = JSONObject.fromObject(json);
		Long orderId = Long.valueOf(jsonObj.getString("orderId")); // 销售退货订单ID
		String returnOutDate = (String) jsonObj.get("returnOutDate");	// 出库日期
		// 获取销售退货订单信息
		SaleReturnOrderModel model = saleReturnOrderDao.searchById(orderId);
		if(model==null){
			throw new RuntimeException("该销售退订单不存在，销售退订单ID："+orderId);
		}
		if(model.getBuId()==null){
			throw new RuntimeException("该销售退订单没有事业部信息");
		}
		// 销售退货订单没有退出仓库信息，不能进行出库打托
		if(model.getOutDepotId()==null){
			throw new RuntimeException("该销售退货订单没有退出仓库信息，不能进行出库打托");
		}
		// 获取退出仓库信息
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", model.getOutDepotId());// 根据仓库id
		DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
		if(mongo==null){
			throw new RuntimeException("退出仓库ID："+model.getOutDepotId()+"没找到仓库信息");
		}
		/**
		 * 1.点击确认时，需再次校验该销售退订单是否已有存在出库中或已出库的退出库单，若有保存失败，
		 * 弹窗提示“该销售退订单存在出库中/已出库的出库单，不允许重复出库”，且页面返回到销售退订单列表页；
		 * 若无则进入下一个校验规则；
		 */
		SaleReturnOdepotModel saleReturnOdepot = new SaleReturnOdepotModel();
		saleReturnOdepot.setOrderId(orderId);
		List<SaleReturnOdepotModel> list = saleReturnOdepotDao.list(saleReturnOdepot);
		if(list.size()>0){
			throw new RuntimeException("该销售退订单存在销售退出库单，不允许重复出库");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sDate = sdf.format(new Date());
		Date nowDate = sdf.parse(sDate);
		Date returnOutD = sdf.parse(returnOutDate);
		// 出库日期必须不能大于当前日期
		if (returnOutD.compareTo(nowDate) == 1) {
			throw new RuntimeException("出库日期不能大于当前日期");
		}

		// 获取该商家下最后的关账月份
		// 获取最大的关账日/月结日期
		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(model.getMerchantId());
		closeAccountsMongo.setDepotId(model.getOutDepotId());
		closeAccountsMongo.setBuId(model.getBuId());
		String maxdate = "";
		if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
		}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
		}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
			maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
		}
		String maxCloseAccountsMonth = "";
		if (StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		if (StringUtils.isNotBlank(maxCloseAccountsMonth) && StringUtils.isNotBlank(returnOutDate)) {
			// 出库日期必须大于关账日期
			if (Timestamp.valueOf(returnOutDate + " 00:00:00").getTime() < Timestamp.valueOf(maxCloseAccountsMonth)
					.getTime()) {
				throw new RuntimeException("出库日期必须大于关账日期/月结日期");
			}
		}
		// 新增销售退货出库表
		SaleReturnOdepotModel sReturnOdepotModel = new SaleReturnOdepotModel();
		sReturnOdepotModel.setOrderId(orderId);//销售退货ID
		sReturnOdepotModel.setMerchantId(user.getMerchantId());//商家ID
		sReturnOdepotModel.setMerchantName(user.getMerchantName());//商家名称
		sReturnOdepotModel.setContractNo(model.getContractNo());//合同号
		sReturnOdepotModel.setInDepotId(model.getInDepotId());//退入仓库id
		sReturnOdepotModel.setInDepotName(model.getInDepotName());//退入仓库名称
		sReturnOdepotModel.setOutDepotId(model.getOutDepotId());//退出仓库id
		sReturnOdepotModel.setOutDepotName(model.getOutDepotName());//退出仓库名称
		sReturnOdepotModel.setReturnOutDate(TimeUtils.parseDay(returnOutDate));//退货出库时间
		sReturnOdepotModel.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_027);//"011","待入仓",012","已入仓""027","出库中"
		sReturnOdepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSTC));//销售退货出库单号
		sReturnOdepotModel.setOrderCode(model.getCode());//销售退货编号
		sReturnOdepotModel.setMerchantReturnNo(model.getMerchantReturnNo());//企业退运单号
		sReturnOdepotModel.setInspectNo(model.getInspectNo());//申报地国检编码
		sReturnOdepotModel.setCustomsNo(model.getCustomsNo());//申报地海关编码
		// model和serveTypes这两个字段先不设值
		//sReturnOdepotModel.setModel(null);//业务场景 账册内货权转移/账册内货权转移调仓
		//sReturnOdepotModel.setServeTypes(null);//服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务
		sReturnOdepotModel.setCustomerId(model.getCustomerId());//客户id
		sReturnOdepotModel.setCustomerName(model.getCustomerName());//客户名称
		sReturnOdepotModel.setRemark(model.getRemark());//备注
		sReturnOdepotModel.setLbxNo(model.getLbxNo());
		sReturnOdepotModel.setOutExternalCode(model.getCode());// 销售退出外部单号
		sReturnOdepotModel.setBuId(model.getBuId());// 事业部
		sReturnOdepotModel.setBuName(model.getBuName());
		if("1".equals(model.getIsGenerateDeclare())) {
			SaleReturnDeclareOrderDTO saleReturnDeclareOrderDTO = new SaleReturnDeclareOrderDTO();
			saleReturnDeclareOrderDTO.setSaleReturnOrderCodes(model.getCode());
			List<SaleReturnDeclareOrderDTO> declareList = saleReturnDeclareOrderDao.listReturnDeclareOrder(saleReturnDeclareOrderDTO);
			if(declareList != null && declareList.size() > 0) {
				sReturnOdepotModel.setReturnDeclareOrderId(declareList.get(0).getId());
				sReturnOdepotModel.setReturnDeclareOrderCode(declareList.get(0).getCode());
			}
		}

		Long id = saleReturnOdepotDao.save(sReturnOdepotModel);
		returnCode = sReturnOdepotModel.getCode();	// 销售退货出库单号
		// 解析表体数据
		JSONArray itemArr = JSONArray.fromObject(jsonObj.get("itemList"));
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
		List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		for (int i = 0; i < itemArr.size(); i++) {
			JSONObject job = itemArr.getJSONObject(i);
			Long outGoodsId = Long.valueOf(job.getString("outGoodsId"));
			String outGoodsCode = (String) job.get("outGoodsCode");
			String outGoodsNo = (String) job.get("outGoodsNo");
			String outGoodsName = (String) job.get("outGoodsName");
			String barcode = (String) job.get("barcode");
			Integer totalNum = Integer.valueOf(job.getString("totalNum")); // 退货总量
			String returnNumStr = (String) job.get("returnNum");// 出库好品数量
			String returnBatchNo = (String) job.get("returnBatchNo");	// 退货批次
			String poNo = (String) job.get("poNo");// PO单号
			String productionDate = (String) job.get("productionDate");	// 生产日期
			String overdueDate = (String) job.get("overdueDate");	// 失效日期

			Integer returnNum = 0;
			if (StringUtils.isNotBlank(returnNumStr)) {
				returnNum = Integer.valueOf(returnNumStr);
			}
			String badGoodsNumStr = (String) job.get("badGoodsNum");// 出库坏品数量
			Integer badGoodsNum = 0;
			if (StringUtils.isNotBlank(badGoodsNumStr)) {
				badGoodsNum = Integer.valueOf(badGoodsNumStr);
			}
			if(returnNum==0 && badGoodsNum==0){
				continue;
			}
    		if(outGoodsId==null){
    			throw new RuntimeException("该销售退货订单没有退出商品信息，不能进行出库打托");
    		}
			// 获取退出商品信息
		 	Map<String, Object> params = new HashMap<String,Object>();
    		params.put("merchandiseId", outGoodsId);
    		MerchandiseInfoMogo outGoods = merchandiseInfoMogoDao.findOne(params);
    		if(outGoods==null){
    			throw new RuntimeException("退出商品ID："+outGoodsId+"没找到商品信息");
    		}
    		if(totalNum < returnNum+badGoodsNum) {
    			throw new RuntimeException("退出商品ID："+outGoodsId+"，好品量+坏品量不能大于计划退出量");
    		}

			SaleReturnOdepotItemModel saleReturnOdepotItemModel = new SaleReturnOdepotItemModel();
			saleReturnOdepotItemModel.setOutGoodsId(outGoodsId);
			saleReturnOdepotItemModel.setOutGoodsName(outGoods.getName());
			saleReturnOdepotItemModel.setOutGoodsCode(outGoods.getGoodsCode());
			saleReturnOdepotItemModel.setOutGoodsNo(outGoods.getGoodsNo());
			saleReturnOdepotItemModel.setReturnNum(returnNum);	// 正常品数量
			saleReturnOdepotItemModel.setReturnBatchNo(returnBatchNo);//批次号
			saleReturnOdepotItemModel.setWornNum(badGoodsNum);	// 坏品数量
			saleReturnOdepotItemModel.setCreateDate(TimeUtils.getNow());
			saleReturnOdepotItemModel.setCreater(user.getId());
			saleReturnOdepotItemModel.setBarcode(outGoods.getBarcode());
			saleReturnOdepotItemModel.setExpireNum(0);	//过期数量
			saleReturnOdepotItemModel.setPoNo(poNo);
			saleReturnOdepotItemModel.setCommbarcode(outGoods.getCommbarcode());
			saleReturnOdepotItemModel.setSreturnOdepotId(id); // 销售退货出库ID
			if(StringUtils.isNotBlank(productionDate)) {
				saleReturnOdepotItemModel.setProductionDate(TimeUtils.strToSqlDate(productionDate));	//生产日期
			}
			if(StringUtils.isNotBlank(overdueDate)) {
				saleReturnOdepotItemModel.setOverdueDate(TimeUtils.strToSqlDate(overdueDate));//失效日期
			}

			Long flag=saleReturnOdepotItemDao.save(saleReturnOdepotItemModel);

			String inventoryUnit = "";//理货单位
			if (DERP.ORDER_TALLYINGUNIT_00.equals(model.getTallyingUnit())) {
				inventoryUnit = DERP.INVENTORY_UNIT_0;
			} else if (DERP.ORDER_TALLYINGUNIT_01.equals(model.getTallyingUnit())) {
				inventoryUnit = DERP.INVENTORY_UNIT_1;
			} else if (DERP.ORDER_TALLYINGUNIT_02.equals(model.getTallyingUnit())) {
				inventoryUnit = DERP.INVENTORY_UNIT_2;
			}

			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			//扣减销售出库库存量
			String now1 = sdf2.format(new Date());
			invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
			invetAddOrSubtractRootJson.setMerchantId(sReturnOdepotModel.getMerchantId().toString());
			invetAddOrSubtractRootJson.setMerchantName(sReturnOdepotModel.getMerchantName());
			invetAddOrSubtractRootJson.setTopidealCode(model.getTopidealCode());
			invetAddOrSubtractRootJson.setDepotId(sReturnOdepotModel.getOutDepotId().toString());
			invetAddOrSubtractRootJson.setDepotName(sReturnOdepotModel.getOutDepotName());
			invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
			invetAddOrSubtractRootJson.setDepotType(mongo.getType());
			invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
			invetAddOrSubtractRootJson.setOrderNo(sReturnOdepotModel.getCode());	// 销售退出库单号
			invetAddOrSubtractRootJson.setBusinessNo(model.getCode());	// 销售退订单号
			invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_004);
			invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_006);
			invetAddOrSubtractRootJson.setBuId(String.valueOf(model.getBuId()));	// 事业部
			invetAddOrSubtractRootJson.setBuName(model.getBuName());
			invetAddOrSubtractRootJson.setSourceDate(TimeUtils.formatFullTime());
			invetAddOrSubtractRootJson.setDivergenceDate(TimeUtils.format(returnOutD, "yyyy-MM-dd HH:mm:ss"));	// 出库日期
			// 获取当前年月
			invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(returnOutD));

			// 好品
			if (returnNum!=null && returnNum>0) {
				InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

				invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(saleReturnOdepotItemModel.getOutGoodsId()));
				invetAddOrSubtractGoodsListJson.setGoodsNo(saleReturnOdepotItemModel.getOutGoodsNo());
				invetAddOrSubtractGoodsListJson.setCommbarcode(saleReturnOdepotItemModel.getCommbarcode());
				invetAddOrSubtractGoodsListJson.setBarcode(saleReturnOdepotItemModel.getBarcode());
				invetAddOrSubtractGoodsListJson.setGoodsName(saleReturnOdepotItemModel.getOutGoodsName());

				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
					invetAddOrSubtractGoodsListJson.setUnit(inventoryUnit);
				}
				invetAddOrSubtractGoodsListJson.setType("0");//商品分类 （0 好品 1坏品 ）
				invetAddOrSubtractGoodsListJson.setNum(returnNum);
				invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
				invetAddOrSubtractGoodsListJson.setBatchNo(returnBatchNo);
				invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期（0是 1否）---默认不过期
				invetAddOrSubtractGoodsListJson.setStockLocationTypeId(model.getStockLocationTypeId() == null ? "" : model.getStockLocationTypeId().toString());
				invetAddOrSubtractGoodsListJson.setStockLocationTypeName(model.getStockLocationTypeName());
				invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
			}

			// 坏品
			if (badGoodsNum!=null && badGoodsNum>0) {
				InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

				invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(saleReturnOdepotItemModel.getOutGoodsId()));
				invetAddOrSubtractGoodsListJson.setGoodsNo(saleReturnOdepotItemModel.getOutGoodsNo());
				invetAddOrSubtractGoodsListJson.setCommbarcode(saleReturnOdepotItemModel.getCommbarcode());
				invetAddOrSubtractGoodsListJson.setBarcode(saleReturnOdepotItemModel.getBarcode());
				invetAddOrSubtractGoodsListJson.setGoodsName(saleReturnOdepotItemModel.getOutGoodsName());

				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
					invetAddOrSubtractGoodsListJson.setUnit(inventoryUnit);
				}
				invetAddOrSubtractGoodsListJson.setType("1");//商品分类 （0 好品 1坏品 ）
				invetAddOrSubtractGoodsListJson.setNum(badGoodsNum);	//坏品数量
				invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
				invetAddOrSubtractGoodsListJson.setBatchNo(returnBatchNo);
				invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期（0是 1否）---默认不过期
				invetAddOrSubtractGoodsListJson.setStockLocationTypeId(model.getStockLocationTypeId() == null ? "" : model.getStockLocationTypeId().toString());
				invetAddOrSubtractGoodsListJson.setStockLocationTypeName(model.getStockLocationTypeName());
				invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
			}
		}
		invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
		//库存回推数据
		Map<String, Object> customParam2=new HashMap<String, Object>();
		invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.SALERETURN_OUTDEPOT_REPORT__PUSH_BACK.getTags());//回调标签
		invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.SALERETURN_OUTDEPOT_REPORT__PUSH_BACK.getTopic());//回调主题
		customParam2.put("code", returnCode);// 销售退货出库单号
		invetAddOrSubtractRootJson.setCustomParam(customParam2);////自定义回调参数
		// 减库存
		rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_16, model.getCode(), "打托出库", null, null);
		return true;
	}

	@SuppressWarnings("unused")
	@Override
	public Map<String, Object> importSaleReturnOdepot(User user, List<List<Map<String, String>>> data) throws Exception {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		Map<String , Integer> checkGoodsNum = new HashMap<String, Integer>();//记录销售退编号+商品货号维度
		Map<String , SaleReturnOdepotModel> returnOdepotMap = new HashMap<String, SaleReturnOdepotModel>();
		Map<String , List<SaleReturnOdepotItemModel>> returnOdepotItemMap = new HashMap<String, List<SaleReturnOdepotItemModel>>();
		int success = 0;
		int failure = 0;

		String deliverDate = "";
		DepotInfoMongo outDepot = null;
		DepotInfoMongo inDepot = null;
		List<Map<String, String>> objects = data.get(0);
		for (int j = 0; j < objects.size(); j++) {
			boolean isFlag = true;
			Map<String, String> msg = new HashMap<String, String>();
			Map<String, String> map = objects.get(j);
			String saleReturnCode = map.get("销售退订单编号");
			if(checkIsNullOrNot(j, saleReturnCode, "销售退订单编号不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			SaleReturnOrderModel returnOrderModel = new SaleReturnOrderModel();
			returnOrderModel.setCode(saleReturnCode);
			returnOrderModel =	saleReturnOrderDao.searchByModel(returnOrderModel);
			if(returnOrderModel == null) {
				setErrorMsg(j, "销售退订单编号不存在", resultList);
				failure += 1;
				continue ;
			}
			if(DERP_ORDER.SALERETURNORDER_STATUS_006.equals(returnOrderModel.getStatus())) {
				setErrorMsg(j, "销售退订单编号不存在", resultList);
				failure += 1;
				continue ;
			}
			if(DERP_ORDER.SALERETURNORDER_STATUS_001.equals(returnOrderModel.getStatus())) {
				setErrorMsg(j, "销售退订单:"+returnOrderModel.getCode()+"为“待审核”，无法导入出库单", resultList);
				failure += 1;
				continue ;
			}
			if(!returnOrderModel.getReturnType().equals(DERP_ORDER.SALERETURNORDER_RETURNTYPE_3)) {
				setErrorMsg(j, "只允许导入销售退类型为购销退货的出库单", resultList);
				failure += 1;
				continue ;
			}
			Map<String, Object> outDepotInfoParams = new HashMap<String, Object>();
			outDepotInfoParams.put("code", returnOrderModel.getOutDepotCode());
			outDepot = depotInfoMongoDao.findOne(outDepotInfoParams);
			if (outDepot == null) {
				setErrorMsg(j, "仓库不存在", resultList);
				failure += 1;
				continue;
			}

			outDepotInfoParams.clear();
			outDepotInfoParams.put("depotId", returnOrderModel.getOutDepotId());
			outDepotInfoParams.put("merchantId", user.getMerchantId());
			DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(outDepotInfoParams);
			if (depotMerchantRel == null) {
				setErrorMsg(j, "公司仓库关联不存在", resultList);
				failure += 1;
				continue;
			}
			Map<String, Object> inDepotInfoParams = new HashMap<String, Object>();
			inDepotInfoParams.put("depotId", returnOrderModel.getOutDepotId());
			inDepotInfoParams.put("merchantId", user.getMerchantId());
			DepotMerchantRelMongo inDepotMerchantRel = depotMerchantRelMongoDao.findOne(inDepotInfoParams);

			//入库仓下推指令为是，出库仓是否已入定出
			if(DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(inDepotMerchantRel.getIsInOutInstruction())  && DERP_SYS.DEPOTMERCHANTREL_ISINDEPENDOUT_1.equals(depotMerchantRel.getIsOutDependIn())) {
				setErrorMsg(j, "入库仓进出库指令为“是”，不能导入出库仓是“已入定出”的单据", resultList);
				failure += 1;
				continue;
			}

			String poNo = map.get("PO号");
			if(checkIsNullOrNot(j, poNo, "PO号不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			poNo = poNo.trim();

			String returnOutDate = map.get("出库时间");

			String outGoodsNo = map.get("退出商品货号");
			if(checkIsNullOrNot(j, outGoodsNo, "退出商品货号不能为空", resultList)) {
				failure += 1;
				continue ;
			}
			MerchandiseInfoMogo merchandise =null;
			if(StringUtils.isNotBlank(outGoodsNo)){
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("goodsNo", outGoodsNo) ;
				params.put("merchantId", user.getMerchantId());
				merchandise = merchandiseInfoMogoDao.findOne(params);
				if(merchandise == null) {
					setErrorMsg(j, "商品信息不存在", resultList);
					failure += 1;
					continue ;
				}
			}
			SaleReturnOrderItemModel itemModel = new SaleReturnOrderItemModel();
			itemModel.setOrderId(returnOrderModel.getId());
			itemModel.setOutGoodsNo(outGoodsNo);
			itemModel.setPoNo(poNo);
			itemModel = saleReturnItemDao.searchByModel(itemModel);
			if(itemModel == null) {
				setErrorMsg(j, "商品货号："+outGoodsNo+"，PO号："+poNo+" 在销售退订单编号:"+saleReturnCode+" 中不存在", resultList);
				failure += 1;
				continue ;
			}
			SaleReturnOdepotModel outModel = new SaleReturnOdepotModel();
			outModel.setOrderCode(saleReturnCode);
			outModel = saleReturnOdepotDao.searchByModel(outModel);
			if(outModel != null) {
				setErrorMsg(j, "销售退订单编号:"+saleReturnCode+" 已存在销售退出库单", resultList);
				failure += 1;
				continue ;
			}

			String returnNum = map.get("退出好品数量");
			String wornNum = map.get("退出坏品数量");
			if(StringUtils.isBlank(returnNum) && StringUtils.isBlank(wornNum)){
				setErrorMsg(j, "退出好品数量或退出坏品数量必须有一个不为空", resultList);
				failure += 1;
				continue ;
			}else if("0".equals(returnNum) && "0".equals(wornNum)){
				setErrorMsg(j, "退出好品数量或退出坏品数量必须有一个不为0", resultList);
				failure += 1;
				continue ;
			}

			Integer goodNum = 0;
			Integer badNum = 0;
			if(itemModel != null) {
				goodNum = itemModel.getReturnNum() == null ?0:itemModel.getReturnNum();
				badNum = itemModel.getBadGoodsNum() == null ?0:itemModel.getBadGoodsNum();

			}
			Integer returnDepotNum = goodNum + badNum;// 计划退出商品数量
			Integer numInt = StringUtils.isBlank(returnNum)?0:Integer.valueOf(returnNum);
			Integer wornNumInt = StringUtils.isBlank(wornNum)?0:Integer.valueOf(wornNum);
			Integer totalGoodsNum = numInt+wornNumInt;	// 导入进来的好品+坏品数量

			Integer outDepotNum = 0;//已导入的退出商品量
			if(checkGoodsNum.containsKey(saleReturnCode+outGoodsNo+poNo)) {
				outDepotNum = checkGoodsNum.get(saleReturnCode+outGoodsNo+poNo);
			}else {
				checkGoodsNum.put(saleReturnCode+outGoodsNo+poNo, totalGoodsNum);
			}

			Integer totalNum = returnDepotNum - outDepotNum;	// 减去后的数量
			if(totalGoodsNum > totalNum){
				setErrorMsg(j, "销售退订单编号:"+saleReturnCode+"退出商品货号:"+outGoodsNo+"，PO号："+poNo+"剩余出库量为:"+totalNum, resultList);
				failure += 1;
				continue ;
			}

			String returnBatchNo = map.get("批次号");
			String returnProductionDate = map.get("生产日期");
			String returnOverdueDate = map.get("失效日期");
			if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(outDepot.getBatchValidation())) {
				if(checkIsNullOrNot(j, returnBatchNo, "批次效期强校验的仓库，批次号不能为空", resultList)) {
					failure += 1;
					continue;
				}
				returnBatchNo = returnBatchNo.trim() ;

				if(checkIsNullOrNot(j, returnProductionDate, "批次效期强校验的仓库，生产日期不能为空", resultList)) {
					failure += 1;
					continue;
				}
				returnProductionDate = returnProductionDate.trim() ;
				if(!isDate(returnProductionDate)) {
					setErrorMsg(j, "生产日期格式有误", resultList);
					failure += 1;
					continue;
				}

				if(checkIsNullOrNot(j, returnOverdueDate, "批次效期强校验的仓库，失效日期不能为空", resultList)) {
					failure += 1;
					continue;
				}
				returnOverdueDate = returnOverdueDate.trim() ;
				if(!isDate(returnOverdueDate)) {
					setErrorMsg(j, "失效日期格式有误", resultList);
					failure += 1;
					continue;
				}
			}
			// 获取最大的关账日/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(user.getMerchantId());
			closeAccountsMongo.setDepotId(outDepot.getDepotId());
			closeAccountsMongo.setBuId(returnOrderModel.getBuId());
			String maxdate = "";
			if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
			}
			String maxCloseAccountsMonth="";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
				maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
			}
			String returnOutDateTime = returnOutDate ;
			if(returnOutDate.length() <= 10) {
				returnOutDateTime =returnOutDate + " 00:00:00";
			}
			// 关账日期和发货日期比较
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 出库日期
				if (Timestamp.valueOf(returnOutDateTime).getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					setErrorMsg(j,"出库日期必须大于关账日期/月结日期", resultList);
					failure += 1;
					continue;
				}
			}

			SaleReturnOdepotModel sReturnOdepotModel = new SaleReturnOdepotModel();
			sReturnOdepotModel.setMerchantId(user.getMerchantId());//商家ID
			sReturnOdepotModel.setMerchantName(user.getMerchantName());//商家名称
			sReturnOdepotModel.setContractNo(returnOrderModel.getContractNo());//合同号
			sReturnOdepotModel.setInDepotId(returnOrderModel.getInDepotId());//退入仓库id
			sReturnOdepotModel.setInDepotName(returnOrderModel.getInDepotName());//退入仓库名称
			sReturnOdepotModel.setOutDepotId(returnOrderModel.getOutDepotId());//退出仓库id
			sReturnOdepotModel.setOutDepotName(returnOrderModel.getOutDepotName());//退出仓库名称
			sReturnOdepotModel.setReturnOutDate(TimeUtils.parseDay(returnOutDate));//退货出库时间
			sReturnOdepotModel.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_027);//"011","待入仓",012","已入仓""027","出库中"
			sReturnOdepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSTC));//销售退货出库单号
			sReturnOdepotModel.setOrderCode(returnOrderModel.getCode());//销售退货编号
			sReturnOdepotModel.setMerchantReturnNo(returnOrderModel.getMerchantReturnNo());//企业退运单号
			sReturnOdepotModel.setInspectNo(returnOrderModel.getInspectNo());//申报地国检编码
			sReturnOdepotModel.setCustomsNo(returnOrderModel.getCustomsNo());//申报地海关编码
			sReturnOdepotModel.setCustomerId(returnOrderModel.getCustomerId());//客户id
			sReturnOdepotModel.setCustomerName(returnOrderModel.getCustomerName());//客户名称
			sReturnOdepotModel.setRemark(returnOrderModel.getRemark());//备注
			sReturnOdepotModel.setLbxNo(returnOrderModel.getLbxNo());
			sReturnOdepotModel.setOutExternalCode(returnOrderModel.getCode());// 销售退出外部单号
			sReturnOdepotModel.setBuId(returnOrderModel.getBuId());// 事业部
			sReturnOdepotModel.setBuName(returnOrderModel.getBuName());
			sReturnOdepotModel.setDataSource("1");//手工导入
			sReturnOdepotModel.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_015);//待出仓
			sReturnOdepotModel.setOrderId(returnOrderModel.getId());
			if("1".equals(returnOrderModel.getIsGenerateDeclare())) {
				SaleReturnDeclareOrderDTO saleReturnDeclareOrderDTO = new SaleReturnDeclareOrderDTO();
				saleReturnDeclareOrderDTO.setSaleReturnOrderCodes(returnOrderModel.getCode());
				List<SaleReturnDeclareOrderDTO> declareList = saleReturnDeclareOrderDao.listReturnDeclareOrder(saleReturnDeclareOrderDTO);
				if(declareList != null && declareList.size() > 0) {
					sReturnOdepotModel.setReturnDeclareOrderId(declareList.get(0).getId());
					sReturnOdepotModel.setReturnDeclareOrderCode(declareList.get(0).getCode());
				}
			}

			SaleReturnOdepotModel existModel = returnOdepotMap.get(saleReturnCode);
			if(existModel != null) {
				if(existModel.getReturnOutDate().getTime() != sReturnOdepotModel.getReturnOutDate().getTime()) {
					setErrorMsg(j,"相同销售退订单号"+saleReturnCode+"的出库日期要一致", resultList);
					failure += 1;
					continue;
				}
			}
			returnOdepotMap.put(saleReturnCode, sReturnOdepotModel);

			SaleReturnOdepotItemModel saleReturnOdepotItemModel = new SaleReturnOdepotItemModel();
			saleReturnOdepotItemModel.setOutGoodsId(merchandise.getMerchandiseId());
			saleReturnOdepotItemModel.setOutGoodsName(merchandise.getName());
			saleReturnOdepotItemModel.setOutGoodsCode(merchandise.getGoodsCode());
			saleReturnOdepotItemModel.setOutGoodsNo(outGoodsNo);
			saleReturnOdepotItemModel.setReturnNum(numInt);	// 正常品数量
			saleReturnOdepotItemModel.setReturnBatchNo(returnBatchNo);//批次号
			saleReturnOdepotItemModel.setWornNum(wornNumInt);	// 坏品数量
			saleReturnOdepotItemModel.setCreateDate(TimeUtils.getNow());
			saleReturnOdepotItemModel.setCreater(user.getId());
			saleReturnOdepotItemModel.setBarcode(merchandise.getBarcode());
			saleReturnOdepotItemModel.setExpireNum(0);	//过期数量
			saleReturnOdepotItemModel.setCommbarcode(merchandise.getCommbarcode());
			saleReturnOdepotItemModel.setPoNo(poNo);
			if(StringUtils.isNotBlank(returnProductionDate)) {
				saleReturnOdepotItemModel.setProductionDate(TimeUtils.strToSqlDate(returnProductionDate));	//生产日期
			}
			if(StringUtils.isNotBlank(returnOverdueDate)) {
				saleReturnOdepotItemModel.setOverdueDate(TimeUtils.strToSqlDate(returnOverdueDate));//失效日期
			}

			List<SaleReturnOdepotItemModel> itemList = returnOdepotItemMap.get(saleReturnCode);
			if(itemList == null) {
				itemList = new ArrayList<SaleReturnOdepotItemModel>() ;
			}
			itemList.add(saleReturnOdepotItemModel) ;
			returnOdepotItemMap.put(saleReturnCode, itemList) ;

		}

		if(failure == 0) {
			for (String saleReturnCode : returnOdepotMap.keySet()) {
				SaleReturnOdepotModel model = returnOdepotMap.get(saleReturnCode);
				Long id =saleReturnOdepotDao.save(model);
				if(id != null) {
					 //合并相同调入商品货号+批次+效期
	                Map<String, SaleReturnOdepotItemModel> map = new HashMap<>();
	                List<SaleReturnOdepotItemModel> saleReturnOdepotItemModels = returnOdepotItemMap.get(saleReturnCode);

	                for (SaleReturnOdepotItemModel itemModel : saleReturnOdepotItemModels) {
	                    String key = itemModel.getPoNo() + "_" + itemModel.getOutGoodsNo() + "_" + itemModel.getReturnBatchNo() + "_" +
	                            itemModel.getProductionDate() + "_" + itemModel.getOverdueDate();

	                    SaleReturnOdepotItemModel existItemModel = map.get(key);
	                    if (existItemModel != null) {
	                        Integer goodsNum = itemModel.getReturnNum() == null ? 0 : itemModel.getReturnNum();
	                        Integer wornNum = itemModel.getWornNum() == null ? 0 : itemModel.getWornNum();
	                        Integer existGoodsNum = existItemModel.getReturnNum() == null ? 0 : existItemModel.getReturnNum();
	                        Integer existWornNum = existItemModel.getWornNum() == null ? 0 : existItemModel.getWornNum();
	                        itemModel.setReturnNum(goodsNum + existGoodsNum);
	                        itemModel.setWornNum(wornNum + existWornNum);
	                    }
	                    map.put(key, itemModel);
	                }

	                for (String key : map.keySet()) {
	                	SaleReturnOdepotItemModel itemModel = map.get(key);
	                	itemModel.setSreturnOdepotId(id);
	                	saleReturnOdepotItemDao.save(itemModel);
					}
					success += 1;
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
	}

	/**
	 * 判断输入字段是否为空
	 * @param index
	 * @param content
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean checkIsNullOrNot(int index , String content , String msg ,List<ImportErrorMessage> resultList ) {

		if(StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);

			return true ;

		}else {
			return false ;
		}

	}

	/**
	 * 错误时，设置错误内容
	 * @param index
	 * @param msg
	 * @param resultList
	 */
	private void setErrorMsg(int index , String msg ,List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
	}
	/**
	 * 判断是否yyyy-MM-dd
	 * @param date
	 * @return
	 */
	private boolean isDate(String date) {
		Pattern p = Pattern.compile("\\d{4}\\-\\d{1,2}\\-\\d{1,2}");
		return p.matcher(date).matches();
	}

	@Override
	public Map<String, Object> auditSaleReturnOdepot(List list, User user) throws Exception {
		int num = 0;
		//失败原因
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		Map<String, Integer> auditedNumMap = new HashMap<>();
		for(int j = 0; j < list.size() ;j++){
			boolean flag = true;
			SaleReturnOdepotModel saleReturnOdepotModel = saleReturnOdepotDao.searchById(Long.parseLong(list.get(j).toString()));
			// 1.单据状态必须为待出仓
			if(!DERP_ORDER.SALERETURNODEPOT_STATUS_015.equals(saleReturnOdepotModel.getStatus()) || !"1".equals(saleReturnOdepotModel.getDataSource())){
				setErrorMsg(j,"审核失败，必须是手工导入且状态为待出仓的单据\n", resultList);
				flag = false;
				continue;
			}
			SaleReturnOrderModel returnOrderModel = new SaleReturnOrderModel();
			returnOrderModel.setCode(saleReturnOdepotModel.getOrderCode());
			returnOrderModel =	saleReturnOrderDao.searchByModel(returnOrderModel);
			if(returnOrderModel == null) {
				setErrorMsg(j,"审核失败，销售退订单:"+saleReturnOdepotModel.getOrderCode()+"不存在\n", resultList);
				flag = false;
				continue;
			}
			if(!returnOrderModel.getReturnType().equals(DERP_ORDER.SALERETURNORDER_RETURNTYPE_3)) {
				setErrorMsg(j,"审核失败，只允许销售退类型为购销退货的单据进行审核\n", resultList);
				flag = false;
				continue;
			}

			//mongo查询参数集合
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", saleReturnOdepotModel.getOutDepotId());
			DepotInfoMongo depot = depotInfoMongoDao.findOne(params);
			if (depot == null) {
				setErrorMsg(j,"审核失败，仓库ID为:"+saleReturnOdepotModel.getOutDepotId()+",未查到该出仓仓库信息\n", resultList);
				flag = false;
				continue;
			}
			// 仓库公司关联表
			Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
			depotMerchantRelParam.put("merchantId", user.getMerchantId());
			depotMerchantRelParam.put("depotId", saleReturnOdepotModel.getOutDepotId());
			DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
			if(depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)){
				setErrorMsg(j,"审核失败，仓库ID为:"+saleReturnOdepotModel.getOutDepotId()+",未查到该公司下出仓仓库信息\n", resultList);
				flag = false;
				continue;
			}

			// 获取最大的关账日/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(saleReturnOdepotModel.getMerchantId());
			closeAccountsMongo.setDepotId(saleReturnOdepotModel.getOutDepotId());
			closeAccountsMongo.setBuId(saleReturnOdepotModel.getBuId());
			String maxdate = "";
			if(closeAccountsMongo.getDepotId() == null && closeAccountsMongo.getBuId() != null) {//查询关账日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG1);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() == null){//查询月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG2);
			}else if(closeAccountsMongo.getDepotId() != null && closeAccountsMongo.getBuId() != null){//获取最大的关账日/月结日期
				maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo,DERP.CLOSEACCOUNTFLAG3);
			}
			String maxCloseAccountsMonth="";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate+"-01 00:00:00"));
				maxCloseAccountsMonth=nextMonth+"-01 00:00:00";
			}

			// 关账日期和发货日期比较
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 入库日期
				if (saleReturnOdepotModel.getReturnOutDate().getTime()<Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					setErrorMsg(j,"审核失败，归属日期(发货日期)必须大于关账日期/月结日期库\n", resultList);
					flag = false;
					continue;
				}
			}

			// 销售出库商品数量是否小于等于销售订单商品可核销量
			// 获取销售出库的商品信息
			SaleReturnOdepotItemModel itemOutModel = new SaleReturnOdepotItemModel();
			itemOutModel.setSreturnOdepotId(saleReturnOdepotModel.getId());
			List<SaleReturnOdepotItemModel> itemList = saleReturnOdepotItemDao.list(itemOutModel);
			for(SaleReturnOdepotItemModel item:itemList) {
				SaleReturnOrderItemModel itemModel = new SaleReturnOrderItemModel();
				itemModel.setOrderId(returnOrderModel.getId());
				itemModel.setOutGoodsNo(item.getOutGoodsNo());
				itemModel = saleReturnItemDao.searchByModel(itemModel);
				if(itemModel == null) {
					setErrorMsg(j,"商品货号："+item.getOutGoodsNo()+"在销售退订单编号:"+returnOrderModel.getCode()+" 中不存在", resultList);
					flag = false;
					continue;
				}
				Integer goodNum = itemModel.getReturnNum() == null ? 0:itemModel.getReturnNum();
				Integer badNum = itemModel.getBadGoodsNum() == null ? 0:itemModel.getBadGoodsNum();
				Integer totalNum = goodNum + badNum;//计划退出数量

				String key = saleReturnOdepotModel.getCode() + item.getOutGoodsNo();
				Integer auditedNum = item.getReturnNum()+item.getWornNum();	// 好品+坏品
				if (auditedNumMap.containsKey(key)) {
					auditedNum += auditedNumMap.get(key);
				}
				if (totalNum < auditedNum) {
					setErrorMsg(j,"审核失败，销售退出库单号：" + saleReturnOdepotModel.getCode() + ",商品货号:" + item.getOutGoodsNo() +  "可核销量不足\n", resultList);
					flag = false;
					break;
				}
				auditedNumMap.put(key, auditedNum);
			}
			if (flag) {
				//修改出库单状态
				saleReturnOdepotModel.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_027);
				saleReturnOdepotModel.setModifyDate(TimeUtils.getNow());
				saleReturnOdepotDao.modify(saleReturnOdepotModel);

				InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
				List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();

				//扣减销售出库库存量
				invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
				invetAddOrSubtractRootJson.setMerchantId(saleReturnOdepotModel.getMerchantId().toString());
				invetAddOrSubtractRootJson.setMerchantName(saleReturnOdepotModel.getMerchantName());
				invetAddOrSubtractRootJson.setTopidealCode(user.getTopidealCode());
				invetAddOrSubtractRootJson.setDepotId(saleReturnOdepotModel.getOutDepotId().toString());
				invetAddOrSubtractRootJson.setDepotName(saleReturnOdepotModel.getOutDepotName());
				invetAddOrSubtractRootJson.setDepotCode(depot.getCode());
				invetAddOrSubtractRootJson.setDepotType(depot.getType());
				invetAddOrSubtractRootJson.setIsTopBooks(depot.getIsTopBooks());
				invetAddOrSubtractRootJson.setOrderNo(saleReturnOdepotModel.getCode());	// 销售退出库单号
				invetAddOrSubtractRootJson.setBusinessNo(saleReturnOdepotModel.getOrderCode());	// 销售退订单号
				invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_006);
				invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_006);
				invetAddOrSubtractRootJson.setBuId(String.valueOf(saleReturnOdepotModel.getBuId()));	// 事业部
				invetAddOrSubtractRootJson.setBuName(saleReturnOdepotModel.getBuName());
				invetAddOrSubtractRootJson.setSourceDate(TimeUtils.formatFullTime());
				invetAddOrSubtractRootJson.setDivergenceDate(TimeUtils.format(saleReturnOdepotModel.getReturnOutDate(), "yyyy-MM-dd HH:mm:ss"));	// 出库日期
				// 获取当前年月
				invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(saleReturnOdepotModel.getReturnOutDate()));
				for(SaleReturnOdepotItemModel item:itemList) {
					// 好品
					if (item.getReturnNum() !=null && item.getReturnNum() > 0) {
						InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

						invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getOutGoodsId()));
						invetAddOrSubtractGoodsListJson.setGoodsNo(item.getOutGoodsNo());
						invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());
						invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
						invetAddOrSubtractGoodsListJson.setGoodsName(item.getOutGoodsName());


						invetAddOrSubtractGoodsListJson.setType("0");//商品分类 （0 好品 1坏品 ）
						invetAddOrSubtractGoodsListJson.setNum(item.getReturnNum());
						invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
						invetAddOrSubtractGoodsListJson.setBatchNo(item.getReturnBatchNo());
						if(item.getProductionDate() != null) {
							invetAddOrSubtractGoodsListJson.setProductionDate(TimeUtils.format(item.getProductionDate(), "yyyy-MM-dd"));
						}
						if(item.getOverdueDate() != null) {
							invetAddOrSubtractGoodsListJson.setOverdueDate(TimeUtils.format(item.getOverdueDate(), "yyyy-MM-dd"));
						}
						invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期（0是 1否）---默认不过期
						if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
							// 托盘
							if (returnOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)) {
								invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_0);// 0 托盘 1箱 2 件
							} else if (returnOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)) {
								invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_1);// 0 托盘 1箱 2 件
							} else if (returnOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)) {
								invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
							}
						}
						invetAddOrSubtractGoodsListJson.setStockLocationTypeId(returnOrderModel.getStockLocationTypeId() == null ? "" : returnOrderModel.getStockLocationTypeId().toString());
						invetAddOrSubtractGoodsListJson.setStockLocationTypeName(returnOrderModel.getStockLocationTypeName());
						invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
					}

					// 坏品
					if (item.getWornNum() != null && item.getWornNum() > 0) {
						InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

						invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getOutGoodsId()));
						invetAddOrSubtractGoodsListJson.setGoodsNo(item.getOutGoodsNo());
						invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());
						invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
						invetAddOrSubtractGoodsListJson.setGoodsName(item.getOutGoodsName());


						invetAddOrSubtractGoodsListJson.setType("1");//商品分类 （0 好品 1坏品 ）
						invetAddOrSubtractGoodsListJson.setNum(item.getWornNum());	//坏品数量
						invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
						invetAddOrSubtractGoodsListJson.setBatchNo(item.getReturnBatchNo());
						if(item.getProductionDate() != null) {
							invetAddOrSubtractGoodsListJson.setProductionDate(TimeUtils.format(item.getProductionDate(), "yyyy-MM-dd"));
						}
						if(item.getOverdueDate() != null) {
							invetAddOrSubtractGoodsListJson.setOverdueDate(TimeUtils.format(item.getOverdueDate(), "yyyy-MM-dd"));
						}
						invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期（0是 1否）---默认不过期
						if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
							// 托盘
							if (returnOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)) {
								invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_0);// 0 托盘 1箱 2 件
							} else if (returnOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)) {
								invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_1);// 0 托盘 1箱 2 件
							} else if (returnOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)) {
								invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
							}
						}
						invetAddOrSubtractGoodsListJson.setStockLocationTypeId(returnOrderModel.getStockLocationTypeId() == null ? "" : returnOrderModel.getStockLocationTypeId().toString());
						invetAddOrSubtractGoodsListJson.setStockLocationTypeName(returnOrderModel.getStockLocationTypeName());
						invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
					}
				}
				//库存回推数据
				Map<String, Object> customParam2=new HashMap<String, Object>();
				invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.SALERETURN_OUTDEPOT_REPORT__PUSH_BACK.getTags());//回调标签
				invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.SALERETURN_OUTDEPOT_REPORT__PUSH_BACK.getTopic());//回调主题
				customParam2.put("code", saleReturnOdepotModel.getCode());// 销售退货出库单号
				invetAddOrSubtractRootJson.setCustomParam(customParam2);//自定义回调参数
				try {
					invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
					// 减库存
					rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

				} catch (Exception e) {
					LOGGER.error("----------------------销售退出库单[" + saleReturnOdepotModel.getCode() + "]扣减库存失败----------------------");
				}

			}
			num++;
		}

        Map<String,Object> map = new HashMap<String,Object>();
		map.put("success",num);
		map.put("failure",list.size()-num);
		map.put("failureMsg", resultList);
		return map;
	}

	@Override
	public void delSaleReturnOdepot(String ids) throws Exception {
		List<Long> idList = StrUtils.parseIds(ids);
		for(Long id : idList) {
			SaleReturnOdepotModel model = saleReturnOdepotDao.searchById(id);
			if(!("1".equals(model.getDataSource()) && DERP_ORDER.SALERETURNODEPOT_STATUS_015.equals(model.getStatus()))) {
				throw new RuntimeException("只能删除手工导入且单据状态为“待出仓”的单据");
			}
			model.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_006);
			saleReturnOdepotDao.modify(model);
		}
	}
}
