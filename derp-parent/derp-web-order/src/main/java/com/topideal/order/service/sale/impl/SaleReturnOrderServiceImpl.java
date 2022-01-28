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
import com.topideal.dao.transfer.TransferOrderDao;
import com.topideal.entity.dto.sale.SaleOrderDTO;
import com.topideal.entity.dto.sale.SaleReturnDeclareOrderDTO;
import com.topideal.entity.dto.sale.SaleReturnOrderDTO;
import com.topideal.entity.dto.sale.SaleReturnOrderItemDTO;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.sale.SaleReturnOrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 销售退货订单service实现类
 */
@Service
public class SaleReturnOrderServiceImpl implements SaleReturnOrderService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(SaleReturnOrderServiceImpl.class);
	// 销售退货订单表头
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;
	// 销售退货订单表体
	@Autowired
	private SaleReturnOrderItemDao saleReturnOrderItemDao;
	// 销售订单表头
	@Autowired
	private SaleOrderDao saleOrderDao;
	// 销售订单表体
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	//供应商
	@Autowired
	private CustomerInfoMongoDao customerInfoMongoDao;
	@Autowired
	private CustomerMerchantRelMongoDao customerMerchantRelMongoDao;
	//商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	//仓库公司关联表 mongo
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	//lbx来源
	@Autowired
	private LbxNoMongoDao lbxNoMongoDao;
	@Autowired
	private ApiSecretConfigMongoDao apiSecretConfigMongoDao;
	//销售出库清单
	@Autowired
	SaleOutDepotDao saleOutDepotDao;

	@Autowired
	SaleReturnIdepotDao saleReturnIdepotDao;
	@Autowired
	SaleReturnIdepotItemDao saleReturnIdepotItemDao;

	// 销售上架信息
	@Autowired
	SaleShelfDao saleShelfDao;

	// 库存调整明细
	@Autowired
	InventoryAdjustmentDetailDao inventoryAdjustmentDetailDao;

	// 销售退货出库表体
	@Autowired
	SaleReturnOdepotItemDao saleReturnOdepotItemDao;

	// 销售退货出库
	@Autowired
	SaleReturnOdepotDao saleReturnOdepotDao;

	@Autowired
	SalePoRelDao salePoRelDao;

	@Autowired
	MerchandiseContrastDao merchandiseContrastDao;

	@Autowired
	CrawlerBillDao crawlerBillDao;

	@Autowired
	private TransferOrderDao transferOrderDao;
	// 上架入库单
	@Autowired
	private SaleShelfIdepotItemDao shelfIdepotItemDao;
	//po历史结转临时表
	@Autowired
	private PojzTempDao pojzTempDao;
	//账单出入库单表
	@Autowired
	private BillOutinDepotItemDao billOutinDepotItemDao;
	// 公司事业部信息
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	// 公司仓库事业部关联表
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private CommonBusinessService commonBusinessService ;
	@Autowired
	private BrandSuperiorMongoDao brandSuperiorMongoDao;
	@Autowired
	private UnitMongoDao unitMongoDao;
	@Autowired
	private MerchandiseCustomsRelMongoDao merchandiseCustomsRelMongoDao;
	@Autowired
	private BrandMongoDao brandMongoDao;
	@Autowired
	private SaleReturnDeclareOrderDao saleReturnDeclareOrderDao;
	@Autowired
	private BuStockLocationTypeConfigMongoDao buStockLocationTypeConfigMongoDao;

	/**
	 * 列表（分页）
	 * @param model
	 * @return
	 */
	@Override
	public SaleReturnOrderDTO listSaleReturnOrderByPage(SaleReturnOrderDTO dto, User user)
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
		SaleReturnOrderDTO saleReturnOrder = saleReturnOrderDao.searchDTOByPage(dto);
		List<SaleReturnOrderDTO> dtos = saleReturnOrder.getList();
		//出库仓库不校验出仓仓库是否为下推接口指令，仅校验出仓仓库为非批次效期强校验，且不以入定出，才可有打托出库按钮操作权限
		for (SaleReturnOrderDTO saleReturnOrderDTO : dtos) {
			Long outDepotId = saleReturnOrderDTO.getOutDepotId();
			Long inDepotId = saleReturnOrderDTO.getInDepotId();

			if(null!=outDepotId){
				// 出库仓不为空
				if (outDepotId!=null) {
					Map<String, Object> outdepotParam = new HashMap<>();
					outdepotParam.put("depotId", outDepotId);
					DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(outdepotParam);
					if (depotInfoMongo!=null) {//'批次效期强校验：1-是 0-否',

						saleReturnOrderDTO.setOutBatchValidation(depotInfoMongo.getBatchValidation());
					}
					Map<String, Object> depotrelParam = new HashMap<>();
					depotrelParam.put("merchantId", saleReturnOrderDTO.getMerchantId());
					depotrelParam.put("depotId", outDepotId);
					DepotMerchantRelMongo outDepot = depotMerchantRelMongoDao.findOne(depotrelParam);
					if (outDepot != null) {
						// 出仓仓库进出接口指令 1-是 0 - 否
						//saleReturnOrderDTO.setOutDepotIsInOutInstruction(outDepot.getIsInOutInstruction());
						saleReturnOrderDTO.setOutDepotIsInDependOut(outDepot.getIsInDependOut());	// 以入定出
					}
				}

			}

			// 入库仓不为空
			if (inDepotId!=null) {
				Map<String, Object> indepotParam = new HashMap<>();
				indepotParam.put("depotId", inDepotId);
				DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(indepotParam);
				if (depotInfoMongo!=null) {//'批次效期强校验：1-是 0-否',
					saleReturnOrderDTO.setInBatchValidation(depotInfoMongo.getBatchValidation());
				}
				Map<String, Object> depotrelParam = new HashMap<>();
				depotrelParam.put("merchantId", saleReturnOrderDTO.getMerchantId());
				depotrelParam.put("depotId", inDepotId);
				DepotMerchantRelMongo inDepot = depotMerchantRelMongoDao.findOne(depotrelParam);
				if (inDepot != null) {
					// 出仓仓库进出接口指令 1-是 0 - 否
					//saleReturnOrderDTO.setOutDepotIsInOutInstruction(outDepot.getIsInOutInstruction());
					saleReturnOrderDTO.setInDepotisOutDependIn(inDepot.getIsOutDependIn());	// 已出定入
					saleReturnOrderDTO.setInBatchValidation(depotInfoMongo.getBatchValidation());// 是否批次效期强校验
				}
			}
		}
		return saleReturnOrder;
	}

	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	@Override
	public SaleReturnOrderDTO searchDetail(Long id) throws SQLException {
		SaleReturnOrderDTO dto = new SaleReturnOrderDTO();
		dto.setId(id);
		return saleReturnOrderDao.searchDTOById(id);
	}


	/**
	 * 根据表头ID获取表体(包括商品信息)
	 * @param id
	 * @return
	 */
	@Override
	public List<SaleReturnOrderItemDTO> listItemByOrderId(Long id) throws SQLException {
		SaleReturnOrderItemDTO saleReturnOrderItem = new SaleReturnOrderItemDTO();
		saleReturnOrderItem.setOrderId(id);
		return saleReturnOrderItemDao.listSaleReturnOrderItemDTO(saleReturnOrderItem);
	}

	/**
	 * 删除销售退货订单
	 * @param ids
	 * @return
	 * @throws RuntimeException
	 */
	@Override
	public boolean delSaleReturnOrder(List ids) throws Exception {
		//判断是否有已审核的订单
		boolean flag = false;
		Long num1 = null;
		//判断是否有已审核的订单
		for(Object id:ids){
			//获取销售退货订单信息
			SaleReturnOrderModel saleReturnOrder = saleReturnOrderDao.searchById(Long.parseLong(id.toString()));
			//单据状态不是待审核
			if(saleReturnOrder.getStatus() != null && !saleReturnOrder.getStatus().equals(DERP_ORDER.SALERETURNORDER_STATUS_001)){
				throw new RuntimeException("删除失败，存在已审核的订单");
			}
			 num1 = saleReturnOrderItemDao.delBesidesIds(null,saleReturnOrder.getId()); // 删除表头
		}
		int num2 = saleReturnOrderDao.delete(ids);
		if(num1== null || num2<=0){
            throw new RuntimeException("删除失败");
        }

        return  true;
	}

	/**
	 * 审核
	 * @param list
	 * @param user
	 * @return
	 * @throws SQLException
	 * @throws RuntimeException
	 */
/*	@Override
	public Map<String,Object> auditSaleReturnOrder(List list, Long userId, String name, Long merchantId,String topidealCode) throws SQLException, RuntimeException {
		int num = 0;
		String msg = "";
		for(Object id:list){
			//获取销售退货订单信息
			SaleReturnOrderModel saleReturnOrder = saleReturnOrderDao.searchById(Long.parseLong(id.toString()));
			//单据状态不是待审核
			if(!StatusEnum.DSH.getKey().equals(saleReturnOrder.getStatus())){
				throw new RuntimeException("审核失败，单据状态不为：待审核");
			}
			//如果是消费者退货
			if("1".equals(saleReturnOrder.getReturnType())){
				SaleReturnIdepotModel saleReturnIdepotModel=new SaleReturnIdepotModel();
				saleReturnIdepotModel.setOrderId(saleReturnOrder.getId());
				saleReturnIdepotModel.setMerchantId(saleReturnOrder.getMerchantId());
				saleReturnIdepotModel.setMerchantName(saleReturnOrder.getMerchantName());
				saleReturnIdepotModel.setInDepotId(saleReturnOrder.getInDepotId());
				saleReturnIdepotModel.setInDepotName(saleReturnOrder.getInDepotName());
				saleReturnIdepotModel.setOutDepotId(saleReturnOrder.getOutDepotId());
				saleReturnIdepotModel.setOutDepotName(saleReturnOrder.getOutDepotName());
				saleReturnIdepotModel.setReturnInDate(saleReturnOrder.getAuditDate());
				saleReturnIdepotModel.setStatus("028");
				saleReturnIdepotModel.setCreateDate(TimeUtils.getNow());
				saleReturnIdepotModel.setCreater(saleReturnOrder.getCreater());
				saleReturnIdepotModel.setCode(CodeGeneratorUtil.getNo("XSTR"));
				saleReturnIdepotModel.setOrderCode(saleReturnOrder.getCode());
				saleReturnIdepotModel.setCustomerId(saleReturnOrder.getCustomerId());
				saleReturnIdepotModel.setCustomerName(saleReturnOrder.getCustomerName());
				saleReturnIdepotModel.setRemark(saleReturnOrder.getRemark());
				saleReturnIdepotModel.setModifyDate(TimeUtils.getNow());
				saleReturnIdepotModel.setReturnInNum(saleReturnOrder.getTotalReturnNum());
				saleReturnIdepotModel.setReturnInDate(saleReturnOrder.getCreateDate());

				saleReturnIdepotDao.save(saleReturnIdepotModel);


				List<SaleReturnOrderItemModel> saleReturnOrderItem = saleReturnOrderItemDao.searchByOrderId(saleReturnOrder.getId());
				SaleReturnIdepotItemModel saleReturnIdepotItemModel=null;
				List<SaleReturnIdepotItemModel> outItemlist = new ArrayList<SaleReturnIdepotItemModel>();
				for (int i = 0; i < saleReturnOrderItem.size(); i++) {
					 saleReturnIdepotItemModel=new SaleReturnIdepotItemModel();
					SaleReturnOrderItemModel som = saleReturnOrderItem.get(i);
					saleReturnIdepotItemModel.setSreturnIdepotId(saleReturnIdepotModel.getId());
					saleReturnIdepotItemModel.setInGoodsId(som.getInGoodsId());
					saleReturnIdepotItemModel.setInGoodsCode(som.getInGoodsCode());
					saleReturnIdepotItemModel.setInGoodsName(som.getInGoodsName());
					saleReturnIdepotItemModel.setInGoodsNo(som.getInGoodsNo());
					saleReturnIdepotItemModel.setReturnNum(som.getReturnNum());
					saleReturnIdepotItemModel.setCreateDate(TimeUtils.getNow());
					saleReturnIdepotItemModel.setId(saleReturnIdepotItemModel.getId());
					saleReturnIdepotItemModel.setBarcode(som.getBarcode());


					Long a=SaleReturnIdepotItemDao.save(saleReturnIdepotItemModel);

					outItemlist.add(saleReturnIdepotItemModel);
				}


				Map<String, Object> depotInfo_params = new HashMap<String, Object>();
				depotInfo_params.put("depotId", saleReturnOrder.getInDepotId());// 根据仓库id

				//修改单据状态
				saleReturnOrder.setStatus(StatusEnum.YSH.getKey());
				saleReturnOrder.setAuditDate(TimeUtils.getNow());
				saleReturnOrder.setAuditor(userId);
				saleReturnOrder.setAuditName(name);
				saleReturnOrder.setModifyDate(TimeUtils.getNow());
				num += saleReturnOrderDao.modify(saleReturnOrder);

				DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				//扣减销售出库库存量
				Date date = new Date();
				String now1 = sdf.format(date);
				InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
				invetAddOrSubtractRootJson.setMerchantId(saleReturnOrder.getMerchantId().toString());
				invetAddOrSubtractRootJson.setMerchantName(saleReturnOrder.getMerchantName());
				invetAddOrSubtractRootJson.setTopidealCode(saleReturnOrder.getTopidealCode());

				invetAddOrSubtractRootJson.setDepotId(mongo.getDepotId().toString());
				invetAddOrSubtractRootJson.setDepotName(mongo.getName());
				invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
				invetAddOrSubtractRootJson.setDepotType(mongo.getType());
				invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
				invetAddOrSubtractRootJson.setOrderNo(saleReturnIdepotModel.getCode());
				invetAddOrSubtractRootJson.setBusinessNo(saleReturnOrder.getCode());
				invetAddOrSubtractRootJson.setSource(SourceStatusEnum.XSTHDD.getKey());
				invetAddOrSubtractRootJson.setSourceType(InventoryStatusEnum.XSTHRK.getKey());
				invetAddOrSubtractRootJson.setSourceDate(now1);
				invetAddOrSubtractRootJson.setDivergenceDate(now1);	//出入时间


				// 获取当前年月
				invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(date));	//出入时间
				List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
				SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

				for (SaleReturnIdepotItemModel item : outItemlist) {
					InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
					invetAddOrSubtractGoodsListJson.setGoodsId(item.getInGoodsId().toString());
					invetAddOrSubtractGoodsListJson.setGoodsName(item.getInGoodsName());
					invetAddOrSubtractGoodsListJson.setGoodsNo(item.getInGoodsNo());
					invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
					invetAddOrSubtractGoodsListJson.setType("0");
					invetAddOrSubtractGoodsListJson.setNum(item.getReturnNum());
					invetAddOrSubtractGoodsListJson.setOperateType("1");// 字符串 0 调减 1调增
					invetAddOrSubtractGoodsListJson.setBatchNo(item.getReturnBatchNo());
					invetAddOrSubtractGoodsListJson.setIsExpire("1");
					invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
					if (item.getProductionDate()!=null) {
						Timestamp productionDateTimestamp  = item.getProductionDate();
						invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(productionDateTimestamp));
					}
					if (item.getOverdueDate()!=null) {
						Timestamp overdueDateTimestamp = item.getOverdueDate();
						invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(overdueDateTimestamp));
						Timestamp currentDate = new Timestamp(date.getTime());
						if(currentDate.before(item.getOverdueDate())){
							//currentDate 时间比 item.getOverdueDate() 时间早(未过期)
							invetAddOrSubtractGoodsListJson.setIsExpire("1");
						}else{
							invetAddOrSubtractGoodsListJson.setIsExpire("0");
						}
					}
					invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
				}
				invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);

				//库存回推数据
				Map<String, Object> customParam=new HashMap<String, Object>();
				invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.SALE_ORDER_STOCK_IN_PUSH_BACK.getTags());//回调标签
				invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.SALE_ORDER_STOCK_IN_PUSH_BACK.getTopic());//回调主题
				customParam.put("code", saleReturnOrder.getCode());//销售退货订单号
				invetAddOrSubtractRootJson.setCustomParam(customParam);////自定义回调参数
				try {
					rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else if("2".equals(saleReturnOrder.getReturnType())){		//如果是代销退货
				//获取销售退货表体（商品信息）
				SaleReturnOrderItemModel saleReturnOrderItem = new SaleReturnOrderItemModel();
				saleReturnOrderItem.setOrderId(saleReturnOrder.getId());	// 销售退货订单ID
				// 根据销售退货订单ID到销售退货表体里查询该销售退货订单ID的商品有哪些
				List<SaleReturnOrderItemModel> itemList = saleReturnOrderItemDao.list(saleReturnOrderItem);
				//获取出仓仓库信息
				Map<String, Object> params1 = new HashMap<String, Object>();
				params1.put("depotId", saleReturnOrder.getOutDepotId());
				DepotInfoMongo outDepot = depotInfoMongoDao.findOne(params1);
				if (outDepot==null) {
					throw new RuntimeException("没有查询到出库仓");
				}

				//获取入仓仓库信息
				Map<String, Object> params2 = new HashMap<String, Object>();
				params2.put("depotId", saleReturnOrder.getInDepotId());
				DepotInfoMongo inDepot = depotInfoMongoDao.findOne(params2);
				if (inDepot==null) {
					throw new RuntimeException("没有查询到入库仓");
				}

				 * 根据仓库类型，执行不同的方法
				 * 1.卓志保税仓且为代销仓（云集代销仓）退回 卓志保税仓（非菜鸟）
				 *   非卓志保税仓且为代销仓（唯品代销仓等）退回 卓志保税仓（非菜鸟）
				 *   同关区----调用调拨指令
				 * 2.卓志保税仓且为代销仓（云集代销仓）退回 卓志保税仓（非菜鸟）
				 *   非卓志保税仓且为代销仓（唯品代销仓等）退回 卓志保税仓（非菜鸟）
				 *   跨关区----调用采购入库指令
				 * 3.其他无操作

				// 如果出库仓或者入库仓是菜鸟仓查询mongdb 中lbx号是否存在如果存在就不能审核
				if (inDepot.getName().contains("菜鸟")||outDepot.getName().contains("菜鸟")) {
					if (StringUtils.isBlank(saleReturnOrder.getLbxNo())) {
						throw new RuntimeException("出库仓或者入库仓为菜鸟仓的时,lbxNo号不能为空");
					}
					Map<String, Object>params3 = new HashMap<String, Object>();
					params3.put("lbxNo", saleReturnOrder.getLbxNo());
					List<LbxNoMongo> params3List = lbxNoMongoDao.findAll(params3);
					if (params3List.size()>0) {
						throw new RuntimeException("出库仓或者入库仓为菜鸟仓时,mongdb已经存在lbxNo单号:"+saleReturnOrder.getLbxNo());
					}
				}
				// TopBooks:是否卓志仓(1-是,0-否)
				if(("a".equals(outDepot.getType()) && "1".equals(outDepot.getIsTopBooks())
						&& "a".equals(inDepot.getType()) && !inDepot.getName().contains("菜鸟"))
				|| ("b".equals(outDepot.getType()) && "1".equals(outDepot.getIsTopBooks())
						&& "a".equals(inDepot.getType()) && !inDepot.getName().contains("菜鸟"))){
					if(outDepot.getInspectNo().equals(inDepot.getInspectNo())
							&& outDepot.getCustomsNo().equals(inDepot.getCustomsNo())){//同关区
					if (StringUtils.isBlank(saleReturnOrder.getIsSameArea())) {
						throw new RuntimeException("是否同关区不能为空");
					}
					if("1".equals(saleReturnOrder.getIsSameArea())){	// 同关区  // 调用调拨指令推送

						//查询调入客户
						TransferOrderRootJson rootJson = new TransferOrderRootJson();
						// 出入库都是是保税仓 是否同关区必填   我们的同关区 数值和跨境宝是相反的
						if ("a".equals(outDepot.getType())&&"a".equals(inDepot.getType())) {
							String isSameArea = saleReturnOrder.getIsSameArea();//是否同关区（0-否，1-是）
					    	if ("0".equals(isSameArea)) {
					    		rootJson.setDistrict_type("1");		//  district_type:是否同关区( 0 :是, 1：否 )
							}
					    	if ("1".equals(isSameArea)) {
					    		rootJson.setDistrict_type("0");
							}
						}


						rootJson.setDatasource("DISTRIBUTED");//数据来源 经分销：DISTRIBUTED（经分销此字段必填）
					    rootJson.setOrder_id(saleReturnOrder.getCode());//销售单号
					    rootJson.setTo_electric_code(topidealCode);//调入电商企业编号
					    rootJson.setOdate(TimeUtils.formatFullTime());//时间yyyy-mm-dd HH:mi:ss
					    rootJson.setCustoms_code(outDepot.getCustomsNo());//海关编码
					    rootJson.setCiqb_code(outDepot.getInspectNo());//国检编码
					    rootJson.setBusi_scene(saleReturnOrder.getModel());//业务场景
					    rootJson.setCartons("1");		//箱数   调出仓库为海外仓，调入仓库为综合仓必填
				    	if(outDepot.getType().matches("a|b")&&inDepot.getType().matches("a|b")){
				    		rootJson.setServe_types(saleReturnOrder.getServeTypes());//服务类型
				    	}
				    	rootJson.setTrust_code(topidealCode);//委托单位 卓志编码
				    	rootJson.setFrom_store_code(outDepot.getCode());//出仓仓库
				    	if(!inDepot.getType().matches("b|e")){
				    		rootJson.setTo_store_code(inDepot.getCode());//调入仓库
				    	}
				    	rootJson.setContract_no(saleReturnOrder.getContractNo());//合同号
				    	rootJson.setInvoice_no(saleReturnOrder.getInvoiceNo());//发票号
				    	rootJson.setPort_loading(saleReturnOrder.getPortLoading());//装货港
				    	rootJson.setPack_type(saleReturnOrder.getPackType());//包装方式
				    	rootJson.setPay_rules(saleReturnOrder.getPayRules());//付款条约
				    	rootJson.setRecadd(saleReturnOrder.getDeliveryAddr());//收货地址
				    	rootJson.setMark(saleReturnOrder.getMark());//唛头
				    	rootJson.setOverseas_shipper(saleReturnOrder.getShipper());//境外发货人
				    	if(saleReturnOrder.getBoxNum() != null){
				    		rootJson.setCartons(saleReturnOrder.getBoxNum().toString());//箱数
				    	}
				    	if(saleReturnOrder.getBillWeight() != null){
				    		rootJson.setBill_weight(saleReturnOrder.getBillWeight().toString());//提单毛重
				    	}
				    	rootJson.setPort_dest_no("44011501");//卸货港编码	44011501：南沙新港口岸 测试暂时写死
				    	if(outDepot.getType().matches("a")&&inDepot.getType().matches("a") && !outDepot.getCustomsNo().equals(inDepot.getCustomsNo())
				    			&& !outDepot.getInspectNo().equals(inDepot.getInspectNo())){
				    		rootJson.setRe_service_type("10");
				    	}
				    	if(outDepot.getType().matches("a")&&inDepot.getType().matches("b|c")){
				    		rootJson.setRe_service_type("10");
				    	}
				    	Integer total_num=0;//件数合计数量
				    	List<TransferGoodsItemJson> goodList= new ArrayList<TransferGoodsItemJson>();//调拨指令商品列表
				    	// 遍历销售退货订单表体(退货的商品)
				    	for(int i=0;i<itemList.size();i++){
				    		SaleReturnOrderItemModel orderItem=itemList.get(i);
				    		TransferGoodsItemJson itemTemp =new TransferGoodsItemJson();
				        	itemTemp.setIndex(i+1);//序号
				        	itemTemp.setFrom_good_Id(orderItem.getOutGoodsNo());//销出货号
				        	itemTemp.setTo_good_Id(orderItem.getInGoodsNo());	// 调入货号
				        	//保税仓调海外仓
							if(outDepot.getType().matches("a|b")&&inDepot.getType().matches("b|c|e")){
								itemTemp.setStock_type("0");//库存类型 0:好品;1:坏品;
							}
				        	Integer num1 = orderItem.getReturnNum();	// 好品数量
				        	itemTemp.setBrand(orderItem.getBrandName());//品牌类型
				        	if(orderItem.getPrice() != null){
				        		itemTemp.setPrice(orderItem.getPrice().toString());//单价
				        	}
				        	if(orderItem.getGrossWeight() != null){
				        		itemTemp.setWeight(orderItem.getGrossWeight().toString());//毛重
				        	}
				        	if(orderItem.getGrossWeight() != null){
				        		itemTemp.setWeight(orderItem.getGrossWeight().toString());//毛重
				        	}
				        	if(orderItem.getNetWeight() != null){
				        		itemTemp.setNet_weight(orderItem.getNetWeight().toString());//净重
				        	}
				        	itemTemp.setCont_no(orderItem.getBoxNo());//箱号
				        	itemTemp.setBargainno(orderItem.getContractNo());//合同号
				        	total_num+=num1;
				        	itemTemp.setAmount(num1);	// 调出数量
				        	goodList.add(itemTemp);
				    	}
				    	rootJson.setTotal_num(total_num);	//件数合计数量  调出仓库为海外仓时选填
				    	rootJson.setGood_list(goodList);

				    	String signTopidealCode = topidealCode;
						//判断仓库使用当前公司秘钥,还是关联公司的秘钥  IsMerchant: 1公司key 2关联公司key
						if(outDepot.getIsMerchant()!=null&&outDepot.getIsMerchant().equals("2")){
							Map<String, Object> aMap = new HashMap<String, Object>();
							aMap.put("merchantId", outDepot.getMerchantId());
							ApiSecretConfigMongo apiSecret = apiSecretConfigMongoDao.findOne(aMap);
							if(apiSecret==null){
								msg+=saleReturnOrder.getCode()+":调出仓库关联公司秘钥不存在\n";
								continue;
							}
							signTopidealCode = apiSecret.getTopidealCode();
						}
						try {
							JSONObject jsonObject = JSONObject.fromObject(rootJson);
							jsonObject.put("method",EpassAPIMethodEnum.EPASS_E04_METHOD.getMethod());	// 推送调拨指令接口
							jsonObject.put("topideal_code", signTopidealCode);
							// 推跨境宝MQ
							rocketMQProducer.send(jsonObject.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(),MQPushApiEnum.PUSH_EPASS.getTags());
						} catch (Exception e) {
							e.printStackTrace();
							msg+="单号："+saleReturnOrder.getCode()+",推送异常\n";
							continue;
						}
					}else{//跨关区
						//调用采购入库指令推送
						PurchaseOrderRootJson rootJson = new PurchaseOrderRootJson();
						rootJson.setDatasource("DISTRIBUTED");//数据来源  经分销：DISTRIBUTED（经分销此字段必填）
						rootJson.setEnt_inbound_id(saleReturnOrder.getCode()); // 企业入仓编号
						rootJson.setContract_no(saleReturnOrder.getContractNo());
						rootJson.setInvoice_no(saleReturnOrder.getInvoiceNo());
						rootJson.setRecadd(saleReturnOrder.getDeliveryAddr());	// 收货地址
						rootJson.setPort_loading(saleReturnOrder.getPortLoading());	// 装货港
						rootJson.setPack_type(saleReturnOrder.getPackType());
						if(saleReturnOrder.getBoxNum() != null){
							rootJson.setCartons(saleReturnOrder.getBoxNum().toString());
						}
						rootJson.setPay_rules(saleReturnOrder.getPayRules());
						if(saleReturnOrder.getBillWeight() != null){
							rootJson.setBill_weight(saleReturnOrder.getBillWeight().toString());
						}
						rootJson.setTransportbl_no(saleReturnOrder.getLadingBill());
						rootJson.setPort_dest_no(saleReturnOrder.getPortDestNo());
						rootJson.setStatus("1");
						rootJson.setWarehouse_id(saleReturnOrder.getInDepotCode());
						rootJson.setEmail_add(saleReturnOrder.getEmail());
						rootJson.setNotes(saleReturnOrder.getRemark());
						rootJson.setMark(saleReturnOrder.getMark());
						rootJson.setOverseas_shipper(saleReturnOrder.getShipper());
						rootJson.setBl_no(saleReturnOrder.getBlNo());
				    	List<PurchaseGoodsListJson> goodList= new ArrayList<PurchaseGoodsListJson>();//采购入库指令商品列表
				    	for(int i=0;i<itemList.size();i++){
				    		SaleReturnOrderItemModel orderItem=itemList.get(i);
				    		PurchaseGoodsListJson goods = new PurchaseGoodsListJson();
				    		goods.setIndex(String.valueOf(i+1));//序号
				    		if(orderItem.getReturnNum() != null){
				    			// 商品总数量：好品+坏品
				    			goods.setAmount(orderItem.getReturnNum().toString()+orderItem.getBadGoodsNum().toString());
				    		}
				    		if(orderItem.getPrice() != null){
				    			goods.setPrice(orderItem.getPrice().toString());
				    		}
				    		goods.setCont_no(orderItem.getBoxNo());
				    		goods.setBargainno(orderItem.getContractNo());
				    		goods.setGoods_id(orderItem.getInGoodsNo());
				    		goods.setGoods_name(orderItem.getInGoodsName());
				    		if(orderItem.getGrossWeight() != null){
				    			goods.setWeight(orderItem.getGrossWeight().toString());
				    		}
				    		if(orderItem.getNetWeight() != null){
				    			goods.setNet_weight(orderItem.getNetWeight().toString());
				    		}
				    		goods.setBrand(orderItem.getBrandName());
				    		goodList.add(goods);
				    	}
				    	rootJson.setGoods_list(goodList);
						String signTopidealCode = topidealCode;
						//判断仓库使用当前公司秘钥,还是关联公司的秘钥 1公司key 2关联公司key
						if(outDepot.getIsMerchant()!=null&&outDepot.getIsMerchant().equals("2")){
							Map<String, Object> aMap = new HashMap<String, Object>();
							aMap.put("merchantId", outDepot.getMerchantId());
							ApiSecretConfigMongo apiSecret = apiSecretConfigMongoDao.findOne(aMap);
							if(apiSecret==null){
								msg+=saleReturnOrder.getCode()+":调出仓库关联公司秘钥不存在\n";
								continue;
							}
							signTopidealCode = apiSecret.getTopidealCode();
						}
						JSONObject jsonObject = JSONObject.fromObject(rootJson);
						jsonObject.put("method",EpassAPIMethodEnum.EPASS_E01_METHOD.getMethod());
						jsonObject.put("topideal_code", signTopidealCode);
						try {
							rocketMQProducer.send(jsonObject.toString(), MQPushApiEnum.PUSH_EPASS.getTopic(),MQPushApiEnum.PUSH_EPASS.getTags());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				//修改单据状态
				saleReturnOrder.setStatus(StatusEnum.YSH.getKey());
				saleReturnOrder.setAuditDate(TimeUtils.getNow());
				saleReturnOrder.setAuditor(userId);
				saleReturnOrder.setAuditName(name);
				num += saleReturnOrderDao.modify(saleReturnOrder);

				// 如果出库仓或者入库仓是菜鸟仓则向mongdb中插入lbx号
				if (inDepot.getName().contains("菜鸟")||outDepot.getName().contains("菜鸟")) {
					LbxNoMongo lbxNoMongo = new LbxNoMongo();
					lbxNoMongo.setLbxNo(saleReturnOrder.getLbxNo());
					lbxNoMongo.setType("XSTO");
					lbxNoMongoDao.insert(lbxNoMongo);
				}
			}
			}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success",num);
		map.put("failure",list.size()-num);
		map.put("msg",msg);
        return map;
	}
*/


	/**
	 * 审核
	 * @param list
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String,Object> auditSaleReturnOrder(List list, User user) throws Exception {
		int num = 0;
		String msg = "";
		ArrayList<String>  poList=new ArrayList<>();

		for(Object id:list){
			//获取销售退货订单信息
			SaleReturnOrderModel saleReturnOrder = saleReturnOrderDao.searchById(Long.parseLong(id.toString()));
			//单据状态不是待审核
			if(!DERP_ORDER.SALERETURNORDER_STATUS_001.equals(saleReturnOrder.getStatus())){
				throw new RuntimeException("审核失败，单据状态不为：待审核");
			}
			if(null == saleReturnOrder.getBuId()){
				throw new RuntimeException("审核失败，该销售退货订单的事业部信息为空");
			}
			//如果是消费者退货
			if(DERP_ORDER.SALERETURNORDER_RETURNTYPE_1.equals(saleReturnOrder.getReturnType())){
				SaleReturnIdepotModel saleReturnIdepotModel=new SaleReturnIdepotModel();
				saleReturnIdepotModel.setOrderId(saleReturnOrder.getId());
				saleReturnIdepotModel.setMerchantId(saleReturnOrder.getMerchantId());
				saleReturnIdepotModel.setMerchantName(saleReturnOrder.getMerchantName());
				saleReturnIdepotModel.setInDepotId(saleReturnOrder.getInDepotId());
				saleReturnIdepotModel.setInDepotName(saleReturnOrder.getInDepotName());
				saleReturnIdepotModel.setOutDepotId(saleReturnOrder.getOutDepotId());
				saleReturnIdepotModel.setOutDepotName(saleReturnOrder.getOutDepotName());
				saleReturnIdepotModel.setReturnInDate(saleReturnOrder.getAuditDate());
				saleReturnIdepotModel.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_028);
				saleReturnIdepotModel.setCreateDate(TimeUtils.getNow());
				saleReturnIdepotModel.setCreater(saleReturnOrder.getCreater());
				saleReturnIdepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSTR));
				saleReturnIdepotModel.setOrderCode(saleReturnOrder.getCode());
				saleReturnIdepotModel.setCustomerId(saleReturnOrder.getCustomerId());
				saleReturnIdepotModel.setCustomerName(saleReturnOrder.getCustomerName());
				saleReturnIdepotModel.setRemark(saleReturnOrder.getRemark());
				saleReturnIdepotModel.setModifyDate(TimeUtils.getNow());
				saleReturnIdepotModel.setReturnInNum(saleReturnOrder.getTotalReturnNum());
				saleReturnIdepotModel.setReturnInDate(saleReturnOrder.getCreateDate());

				saleReturnIdepotDao.save(saleReturnIdepotModel);


				List<SaleReturnOrderItemModel> saleReturnOrderItem = saleReturnOrderItemDao.searchByOrderId(saleReturnOrder.getId());
				SaleReturnIdepotItemModel saleReturnIdepotItemModel=null;
				List<SaleReturnIdepotItemModel> outItemlist = new ArrayList<SaleReturnIdepotItemModel>();
				for (int i = 0; i < saleReturnOrderItem.size(); i++) {
					saleReturnIdepotItemModel = new SaleReturnIdepotItemModel();
					SaleReturnOrderItemModel som = saleReturnOrderItem.get(i);
					saleReturnIdepotItemModel.setSreturnIdepotId(saleReturnIdepotModel.getId());
					saleReturnIdepotItemModel.setInGoodsId(som.getInGoodsId());
					saleReturnIdepotItemModel.setInGoodsCode(som.getInGoodsCode());
					saleReturnIdepotItemModel.setInGoodsName(som.getInGoodsName());
					saleReturnIdepotItemModel.setInGoodsNo(som.getInGoodsNo());
					saleReturnIdepotItemModel.setReturnNum(som.getReturnNum());
					saleReturnIdepotItemModel.setWornNum(som.getBadGoodsNum());// 坏品数量
					saleReturnIdepotItemModel.setCreateDate(TimeUtils.getNow());
					saleReturnIdepotItemModel.setId(saleReturnIdepotItemModel.getId());
					saleReturnIdepotItemModel.setBarcode(som.getBarcode());

					Long a=saleReturnIdepotItemDao.save(saleReturnIdepotItemModel);

					outItemlist.add(saleReturnIdepotItemModel);
				}


				Map<String, Object> depotInfo_params = new HashMap<String, Object>();
				depotInfo_params.put("depotId", saleReturnOrder.getInDepotId());// 根据仓库id

				//修改单据状态
				saleReturnOrder.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_003);
				saleReturnOrder.setAuditDate(TimeUtils.getNow());
				saleReturnOrder.setAuditor(user.getId());
				saleReturnOrder.setAuditName(user.getName());
				saleReturnOrder.setModifyDate(TimeUtils.getNow());
				num += saleReturnOrderDao.modify(saleReturnOrder);

				DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				//扣减销售出库库存量
				Date date = new Date();
				String now1 = sdf.format(date);
				InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
				invetAddOrSubtractRootJson.setMerchantId(saleReturnOrder.getMerchantId().toString());
				invetAddOrSubtractRootJson.setMerchantName(saleReturnOrder.getMerchantName());
				invetAddOrSubtractRootJson.setTopidealCode(saleReturnOrder.getTopidealCode());

				invetAddOrSubtractRootJson.setDepotId(mongo.getDepotId().toString());
				invetAddOrSubtractRootJson.setDepotName(mongo.getName());
				invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
				invetAddOrSubtractRootJson.setDepotType(mongo.getType());
				invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
				invetAddOrSubtractRootJson.setOrderNo(saleReturnIdepotModel.getCode());
				invetAddOrSubtractRootJson.setBusinessNo(saleReturnOrder.getCode());
				//invetAddOrSubtractRootJson.setSource(SourceStatusEnum.XSTHDD.getKey());
				invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_004);
				//invetAddOrSubtractRootJson.setSourceType(InventoryStatusEnum.XSTHRK.getKey());
				invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCE_005);
				invetAddOrSubtractRootJson.setSourceDate(now1);
				invetAddOrSubtractRootJson.setDivergenceDate(now1);	//出入时间
				invetAddOrSubtractRootJson.setBuId(String.valueOf(saleReturnOrder.getBuId()));// 事业部
				invetAddOrSubtractRootJson.setBuName(saleReturnOrder.getBuName());

				// 获取当前年月
				invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(date));	//出入时间
				List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
				SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

				for (SaleReturnIdepotItemModel item : outItemlist) {
					//  好品
					if(item.getReturnNum() != null && item.getReturnNum() > 0) {
						InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

						invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getInGoodsId()));
						invetAddOrSubtractGoodsListJson.setGoodsNo(item.getInGoodsNo());
						invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());
						invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
						invetAddOrSubtractGoodsListJson.setGoodsName(item.getInGoodsName());


						invetAddOrSubtractGoodsListJson.setType("0");	//商品分类 （0 好品 1坏品 ）
						invetAddOrSubtractGoodsListJson.setNum(item.getReturnNum());
						invetAddOrSubtractGoodsListJson.setOperateType("1");// 字符串 0 调减 1调增
						invetAddOrSubtractGoodsListJson.setBatchNo(item.getReturnBatchNo());
						invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期  （0 是 1 否）
						if (item.getProductionDate()!=null) {
							Date productionDateTimestamp  = item.getProductionDate();
							invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(productionDateTimestamp));
						}
						if (item.getOverdueDate()!=null) {
							Date overdueDateTimestamp = item.getOverdueDate();
							invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(overdueDateTimestamp));
							Timestamp currentDate = new Timestamp(date.getTime());
							if(currentDate.before(item.getOverdueDate())){
								//currentDate 时间比 item.getOverdueDate() 时间早(未过期)
								invetAddOrSubtractGoodsListJson.setIsExpire("1");
							}else{
								invetAddOrSubtractGoodsListJson.setIsExpire("0");
							}
						}
						invetAddOrSubtractGoodsListJson.setStockLocationTypeId(saleReturnOrder.getStockLocationTypeId() == null ? "" : saleReturnOrder.getStockLocationTypeId().toString());
						invetAddOrSubtractGoodsListJson.setStockLocationTypeName(saleReturnOrder.getStockLocationTypeName());
						invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
					}
					// 坏品
					if(item.getWornNum() != null && item.getWornNum() > 0){
						InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();

						invetAddOrSubtractGoodsListJson.setGoodsId(String.valueOf(item.getInGoodsId()));
						invetAddOrSubtractGoodsListJson.setGoodsNo(item.getInGoodsNo());
						invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());
						invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
						invetAddOrSubtractGoodsListJson.setGoodsName(item.getInGoodsName());


						invetAddOrSubtractGoodsListJson.setType("1");	//商品分类 （0 好品 1坏品 ）
						invetAddOrSubtractGoodsListJson.setNum(item.getWornNum());
						invetAddOrSubtractGoodsListJson.setOperateType("1");// 字符串 0 调减 1调增
						invetAddOrSubtractGoodsListJson.setBatchNo(item.getReturnBatchNo());
						invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期  （0 是 1 否）
						if (item.getProductionDate()!=null) {
							Date productionDateTimestamp  = item.getProductionDate();
							invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(productionDateTimestamp));
						}
						if (item.getOverdueDate()!=null) {
							Date overdueDateTimestamp = item.getOverdueDate();
							invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(overdueDateTimestamp));
							Timestamp currentDate = new Timestamp(date.getTime());
							if(currentDate.before(item.getOverdueDate())){
								//currentDate 时间比 item.getOverdueDate() 时间早(未过期)
								invetAddOrSubtractGoodsListJson.setIsExpire("1");
							}else{
								invetAddOrSubtractGoodsListJson.setIsExpire("0");
							}
						}
						invetAddOrSubtractGoodsListJson.setStockLocationTypeId(saleReturnOrder.getStockLocationTypeId() == null ? "" : saleReturnOrder.getStockLocationTypeId().toString());
						invetAddOrSubtractGoodsListJson.setStockLocationTypeName(saleReturnOrder.getStockLocationTypeName());
						invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
					}
					invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
				}

				//库存回推数据
				Map<String, Object> customParam=new HashMap<String, Object>();
				invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.SALE_ORDER_STOCK_IN_PUSH_BACK.getTags());//回调标签
				invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.SALE_ORDER_STOCK_IN_PUSH_BACK.getTopic());//回调主题
				customParam.put("code", saleReturnOrder.getCode());//销售退货订单号
				invetAddOrSubtractRootJson.setCustomParam(customParam);////自定义回调参数
				try {
					rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}else if(DERP_ORDER.SALERETURNORDER_RETURNTYPE_2.equals(saleReturnOrder.getReturnType())||
					DERP_ORDER.SALERETURNORDER_RETURNTYPE_3.equals(saleReturnOrder.getReturnType())){		//如果是代销退货/购销退货

				//获取销售退货表体（商品信息）
				SaleReturnOrderItemModel saleReturnOrderItemModel = new SaleReturnOrderItemModel();
				saleReturnOrderItemModel.setOrderId(saleReturnOrder.getId());	// 销售退货订单ID
				// 根据销售退预申报订单ID到销售退货表体里查询该销售退货订单ID的商品有哪些
				List<SaleReturnOrderItemModel> itemList = saleReturnOrderItemDao.list(saleReturnOrderItemModel);

				Map<String, Object> outDepotParam = new HashMap<String, Object>();
				outDepotParam.put("depotId", saleReturnOrder.getOutDepotId());
				DepotInfoMongo outDepot = depotInfoMongoDao.findOne(outDepotParam);

				Map<String, Object> inDepotParam = new HashMap<String, Object>();
				inDepotParam.put("depotId", saleReturnOrder.getInDepotId());
				DepotInfoMongo inDepot = depotInfoMongoDao.findOne(inDepotParam);
				if (inDepot==null) {
					throw new RuntimeException("没有查询到入库仓");
				}

				// 遍历销售退货订单表体(退货的商品)
				for(int i=0; i < itemList.size(); i++){
					/**1.不管是退货类型为购销还是代销，只要出仓仓库是唯品代销仓的商品，
					 *  2.校验PO单号+商品货号是否有存在对应上架记录:
					 * 		2.1满足则执行下一个判断条件，
					 * 		2.2不满足提示错误“PO单号+退出商品货号“找不到对应PO+商品货号上架记录”
					 *  3.校验po号、 货号的可核销量（唯品核销表中的未结算量=可核销量=上架入库总量+临时结转量-账单出库量+账单入库量-调拨出量+调拨入量-销售退货量）
					 *  4.并且增加到关联表里去，不足则导入失败**/

					// 1.如果出仓仓库是唯品代销仓
					if(outDepot != null && outDepot.getDepotCode().equals("VIP001")){
						/**2.校验PO单号+商品货号是否有存在对应上架记录；满足则执行下一个判断条件，
						 * 不满足提示错误“PO单号+退出商品货号“找不到对应PO+商品货号上架记录”*/
						// 2.根据PO单号+商品货号+订单类型查询是否有上架记录(先查临时结转表，若没有再查上架入库表)
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("merchantId",user.getMerchantId());
						paramMap.put("depotId",outDepot.getDepotId());
						paramMap.put("poNo",itemList.get(i).getPoNo());
						paramMap.put("goodsNoList",Arrays.asList(itemList.get(i).getOutGoodsNo()));
						//2.1 临时结转量
						Integer shelfNum = pojzTempDao.getPojzNum(paramMap);
						if (shelfNum == null) {
							//2.2 上架入库量
							shelfNum = shelfIdepotItemDao.getshelfInNum(paramMap);
						}
						// 2.3不满足提示错误“PO单号+退出商品货号“找不到对应PO+商品货号上架记录
						if(shelfNum==null){
							throw new RuntimeException("退出仓库为“唯品代销仓”，找不到"+"PO号为:"+itemList.get(i).getPoNo()+"-"+"商品货号"+itemList.get(i).getOutGoodsNo()+"上架记录");
						}else{		// 2.1 满足则执行下一个判断条件
							// 3.校验PO号、 货号的可核销量（唯品核销表中的未结算量=可核销量=上架入库总量+临时结转量-账单出库量+账单入库量-调拨出量+调拨入量-销售退货量）
							Integer unsettledAccount = this.getVerifiNum(user.getMerchantId(), outDepot.getDepotId(),itemList.get(i).getPoNo(), Arrays.asList(itemList.get(i).getOutGoodsNo()));

							Integer outNum = itemList.get(i).getReturnNum();	// 退出量
							// 上架商品可核销量<退出量(不足)
							if(unsettledAccount<outNum){
								throw new RuntimeException("退出仓库为“唯品代销仓”时，PO号:"+itemList.get(i).getPoNo()+"-"+"商品货号:"+itemList.get(i).getOutGoodsNo()+"的退货数量应小于或等于唯品PO未核销量:"+unsettledAccount);
							}
						}
					}
				}
				// 如果出库仓或者入库仓是菜鸟仓查询mongdb 中lbx号是否存在如果存在就不能审核
				if(StringUtils.isNotBlank(saleReturnOrder.getLbxNo())){
					if (inDepot.getName().contains("菜鸟")||(outDepot!=null&&outDepot.getName().contains("菜鸟"))) {
						Map<String, Object>params3 = new HashMap<String, Object>();
						params3.put("lbxNo", saleReturnOrder.getLbxNo());
						List<LbxNoMongo> params3List = lbxNoMongoDao.findAll(params3);
						if (params3List.size()>0) {
							throw new RuntimeException("出库仓或者入库仓为菜鸟仓时,mongdb已经存在lbxNo单号:"+saleReturnOrder.getLbxNo());
						}
					}
				}

				//修改单据状态
				saleReturnOrder.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_003);
				saleReturnOrder.setAuditDate(TimeUtils.getNow());
				saleReturnOrder.setAuditor(user.getId());
				saleReturnOrder.setAuditName(user.getName());
				num += saleReturnOrderDao.modify(saleReturnOrder);

			}
			Map<String, Object> outDepotParam = new HashMap<String, Object>();
			outDepotParam.put("depotId", saleReturnOrder.getOutDepotId());
			DepotInfoMongo outDepot = depotInfoMongoDao.findOne(outDepotParam);

			Map<String, Object> inDepotParam = new HashMap<String, Object>();
			inDepotParam.put("depotId", saleReturnOrder.getInDepotId());
			DepotInfoMongo inDepot = depotInfoMongoDao.findOne(inDepotParam);
			// 如果出库仓或者入库仓是菜鸟仓则向mongdb中插入lbx号
			if(StringUtils.isNotBlank(saleReturnOrder.getLbxNo())){
				if (inDepot.getName().contains("菜鸟")|| (null!=outDepot&&outDepot.getName().contains("菜鸟"))) {
					LbxNoMongo lbxNoMongo = new LbxNoMongo();
					lbxNoMongo.setLbxNo(saleReturnOrder.getLbxNo());
					lbxNoMongo.setType("XSTO");
					lbxNoMongoDao.insert(lbxNoMongo);
				}
			}

//			if(poList.size()>0){
//				for (int i = 0; i < poList.size(); i++) {
//					SalePoRelModel saleOrderByPoModel=new SalePoRelModel();
//					saleOrderByPoModel.setOrderCode(saleReturnOrder.getCode());	// 销售退货单号
//					saleOrderByPoModel.setOrderId(saleReturnOrder.getId());	// 销售退货单ID
//					saleOrderByPoModel.setState("1");	// 类型:0:销售订单1:销售退订单
//					saleOrderByPoModel.setCreateDate(TimeUtils.getNow());	// 创建时间
//					saleOrderByPoModel.setPoNo(poList.get(i));
//					saleOrderByPoModel.setMerchantId(user.getMerchantId());// 公司ID
//					saleOrderByPoModel.setMerchantName(saleReturnOrder.getMerchantName()); // 公司名称
//					salePoRelDao.save(saleOrderByPoModel);	// 保存到销售单_po关联表
//				}
//			}
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_16, saleReturnOrder.getCode(), "审核", null, null);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success",num);
		map.put("failure",list.size()-num);
		map.put("msg",msg);
        return map;
	}


	/**
	 * 导入销售退货订单(代销退货)
	 * @param json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map importSaleReturnOrder(Map<Integer, List<List<Object>>> data, User user ,String relMerchantIds)throws SQLException{
		int success = 0;
		int pass = 0;
		int failure = 0;
		List<Map<String,String>> msgList = new ArrayList<Map<String,String>>();
		//表头数据集合  ps:序号是表头与表体关联的标识    序号与表头是1对1，表头与表体是1对多
		Map<String,SaleReturnOrderModel> modelMap = new HashMap<String,SaleReturnOrderModel>();
		//表体数据集合
		Map<String,List<SaleReturnOrderItemModel>> itemMap= new HashMap<String,List<SaleReturnOrderItemModel>>();
		//入库出库信息
		Map<String,DepotInfoMongo> inDepotMap = new HashMap<String,DepotInfoMongo>();
		//出库仓库信息
		Map<String,DepotInfoMongo> outDepotMap = new HashMap<String,DepotInfoMongo>();
		//用于统计销售退货订单的总商品数量
		Map<String,Integer> totalMap= new HashMap<String,Integer>();
	    // 检查某个销售订单编号+该仓库公司关联的信息
	    Map<String,Map<String , DepotMerchantRelMongo>> checkGoodsNoByDepotMap = new HashMap<String,Map<String , DepotMerchantRelMongo>>();
	    Map<String , DepotMerchantRelMongo>  depotMerchantRelMap =new HashMap<>();
	    // 保存某个销售订单编号+是否同关区
	    Map<String , String> isSameAreaByCode = new HashMap<>();
		// 退出仓库自编码
		String outDepotCode=null;
		Integer returnNum2 = null;	// 好品数量
		Integer badGoodsNum2 = null;	// 坏品数量
		String poNo=null;

		for (int i = 0; i < 1; i++) {//表头
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				try{
					Map<String,String> message = new HashMap<String,String>();
					Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
					//-------------公共必填字段的校验------------------------
					String index = map.get("自编销售退货订单号").trim();
					if(index == null || "".equals(index)){
						message.put("row", String.valueOf(j+1));
						message.put("message", "基本信息的自编销售退货订单号不能为空");
						msgList.add(message);
						failure+=1;
						continue;
					}

					String code = map.get("退货客户ID").trim();
					if(code == null || "".equals(code)){
						message.put("row", String.valueOf(j+1));
						message.put("message", "退货客户ID不能为空");
						msgList.add(message);
						failure+=1;
						continue;
					}
					outDepotCode = map.get("退出仓库自编码").trim();
					if(outDepotCode == null || "".equals(outDepotCode)){
						message.put("row", String.valueOf(j+1));
						message.put("message", "退出仓库自编码不能为空");
						msgList.add(message);
						failure+=1;
						continue;
					}
					//根据仓库编码、代销标识获取仓库信息
					Map<String, Object> params2 = new HashMap<String, Object>();
					params2.put("depotCode", outDepotCode);
					params2.put("isTopBooks", "1");
					DepotInfoMongo outDepotInfo = depotInfoMongoDao.findOne(params2);
					if(null == outDepotInfo){
						//根据仓库编码获取仓库信息
						Map<String, Object> params3 = new HashMap<String, Object>();
						params3.put("depotCode", outDepotCode);
						outDepotInfo = depotInfoMongoDao.findOne(params3);
						if(null == outDepotInfo){//获取不到，则该编码没有对应的仓库信息
							message.put("row", String.valueOf(j+1));
							message.put("message", "出仓仓库不存在");
							msgList.add(message);
							failure+=1;
							continue;
						}
						//根据仓库编码获取到仓库信息，则该仓库非代销仓
						message.put("row", String.valueOf(j+1));
						message.put("message", "该仓库不允许退货");
						msgList.add(message);
						failure+=1;
						continue;
					}

					//  若退出仓库是保税仓或海外仓，就报错提示该仓库不允许退货
					if(DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepotInfo.getType()) || (DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepotInfo.getType()))){
						message.put("row", String.valueOf(j+1));
						message.put("message", "该仓库不允许退货");
						msgList.add(message);
						failure+=1;
						continue;
					}
					// 仓库公司关联表
					Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
					depotMerchantRelParam.put("merchantId", user.getMerchantId());
					depotMerchantRelParam.put("depotId", outDepotInfo.getDepotId());
					DepotMerchantRelMongo outDepotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);

					if(outDepotMerchantRelMongo == null || "".equals(outDepotMerchantRelMongo)){
						message.put("row", String.valueOf(j+1));
						message.put("message", "退出仓库ID为："+outDepotInfo.getDepotId()+",未查到该公司下调出仓库信息");
						msgList.add(message);
						failure+=1;
						continue;
					}
					String inDepotCode = map.get("退入仓库自编码").trim();
					if(inDepotCode == null || "".equals(inDepotCode)){
						message.put("row", String.valueOf(j+1));
						message.put("message", "退入仓库自编码不能为空");
						msgList.add(message);
						failure+=1;
						continue;
					}
					//获取入仓仓库信息
					Map<String, Object> params1 = new HashMap<String, Object>();
					params1.put("depotCode", inDepotCode);
					DepotInfoMongo inDepotInfo = depotInfoMongoDao.findOne(params1);
					if(null == inDepotInfo){
						message.put("row", String.valueOf(j+1));
						message.put("message", "入仓仓库不存在");
						msgList.add(message);
						failure+=1;
						continue;
					}
					inDepotMap.put(index, inDepotInfo);
					outDepotMap.put(index, outDepotInfo);

					depotMerchantRelParam.put("depotId", inDepotInfo.getDepotId());
					DepotMerchantRelMongo inDepotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);

					if(inDepotMerchantRelMongo == null || "".equals(inDepotMerchantRelMongo)){
						message.put("row", String.valueOf(j+1));
						message.put("message", "退入仓库ID为："+inDepotInfo.getDepotId()+",未查到该公司下退入仓库信息");
						msgList.add(message);
						failure+=1;
						continue;
					}
					String key = index;
					depotMerchantRelMap.put("outDepotMerchantInfo", outDepotMerchantRelMongo);
					depotMerchantRelMap.put("inDepotMerchantInfo", inDepotMerchantRelMongo);
					checkGoodsNoByDepotMap.put(key, depotMerchantRelMap);
					// 同关区必填
					String isSameArea = map.get("是否同关区").trim();
					if(StringUtils.isBlank(isSameArea)){
						message.put("row", String.valueOf(j+1));
						message.put("message", "出库仓或者入库仓为保税仓,是否同关区不能为空");
						msgList.add(message);
						failure+=1;
						continue;
					}
					//isSameArea是否同关区（0-否，1-是）
					if ("是".equals(isSameArea)) {
						isSameArea="1";
					}else if ("否".equals(isSameArea)) {
						isSameArea="0";
					}else {
						message.put("row", String.valueOf(j+1));
						message.put("message", "是否同关区取值只能为,是或者否");
						msgList.add(message);
						failure+=1;
						continue;
					}
					if(!isSameAreaByCode.containsKey(key)){
						isSameAreaByCode.put(key, isSameArea);
					}

					String contractNo = map.get("报关合同号").trim();
					contractNo = contractNo.trim();
					if(contractNo == null || "".equals(contractNo)){
						message.put("row", String.valueOf(j+1));
						message.put("message", "报关合同号不能为空");
						msgList.add(message);
						failure+=1;
						continue;
					}
					// 查询某公司某仓库的相关信息
					Map<String, Object> inDepotMerchantRelParam = new HashMap<String, Object>();
					inDepotMerchantRelParam.put("merchantId", user.getMerchantId());
					inDepotMerchantRelParam.put("depotId", inDepotInfo.getDepotId());
					DepotMerchantRelMongo depotMerchantRelInfo = depotMerchantRelMongoDao.findOne(inDepotMerchantRelParam);
					if(depotMerchantRelInfo == null || "".equals(depotMerchantRelInfo)){
						message.put("row", String.valueOf(j+1));
						message.put("message", "仓库ID为："+outDepotInfo.getDepotId()+",未查到该公司下调出仓库信息");
						msgList.add(message);
						failure+=1;
						continue;
					}
					//-------------公共必填字段的校验end------------------------
					/**
					 * 现调整为：
					 * 购销退货、代销退货类型中，
					 * 且退入仓为保税仓（并在对应公司下的“进出接口指令”标识为“是”的保税仓库）时必填，其他情况下非必填
					 */
						String invoiceNo = map.get("发票号");
						String deliveryAddr = map.get("收货地址");
						String packType = map.get("包装方式");
						String boxNum = map.get("箱数");
						String ladingBill = map.get("头程提单号");
						String mark = map.get("唛头");
						String shipper = map.get("境外发货人");
						String portLoading = map.get("装货港");
						String payRules = map.get("付款条约");
						String billWeight = map.get("提单毛重KG");
						String portDestNo = map.get("卸货港");

					if(DERP_SYS.DEPOTINFO_TYPE_A.equals(inDepotInfo.getType()) &&
					    DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelInfo.getIsInOutInstruction())){

						if(invoiceNo == null || "".equals(invoiceNo)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "发票号不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						invoiceNo = invoiceNo.trim();
						if(deliveryAddr == null || "".equals(deliveryAddr)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "收货地址不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						deliveryAddr = deliveryAddr.trim();
						if(packType == null || "".equals(packType)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "包装方式不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						packType = packType.trim();
						Pattern pattern1 = Pattern.compile("[0-9]*");
				        Matcher isNum1 = pattern1.matcher(boxNum);
						if(boxNum == null || "".equals(boxNum)|| !isNum1.matches()){
							message.put("row", String.valueOf(j+1));
							message.put("message", "箱数不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						boxNum = boxNum.trim();
						if(ladingBill == null || "".equals(ladingBill)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "头程提单号不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						ladingBill = ladingBill.trim();
						if(mark == null || "".equals(mark)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "唛头不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						mark = mark.trim();
						if(shipper == null || "".equals(shipper)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "境外发货人不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						shipper = shipper.trim();
						if(portLoading == null || "".equals(portLoading)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "装货港不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						portLoading = portLoading.trim();
						if(payRules == null || "".equals(payRules)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "付款条约不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						payRules = payRules.trim();
						if(billWeight == null || "".equals(billWeight)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "提单毛重KG不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						billWeight = billWeight.trim();
						if(portDestNo == null || "".equals(portDestNo)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "卸货港不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						portDestNo = portDestNo.trim();
					}

					String lbxNo = map.get("LBX单号");
					if(StringUtils.isNotBlank(lbxNo)){
						if(DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepotInfo.getType()) && DERP_SYS.DEPOTINFO_TYPE_A.equals(inDepotInfo.getType()) && inDepotInfo.getName().contains("菜鸟")){
							// 根据lbx 号查询销售退货订单 非删除状态的销售退货订单
							SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();
							saleReturnOrderModel.setLbxNo(lbxNo);
							List<SaleReturnOrderModel> saleReturnOrderList = saleReturnOrderDao.searchNotDelStatusSaleReturnOrder(saleReturnOrderModel);
							if (saleReturnOrderList.size()>0) {
								message.put("row", String.valueOf(j+1));
								message.put("message", "销售退货订单LBX单号已经存在");
								msgList.add(message);
								failure+=1;
								continue;
							}
						}
					}

					//非必填字段
					String remark= map.get("退货原因");
					remark = remark.trim();
					//注入数据
					SaleReturnOrderModel saleReturnOrder = new SaleReturnOrderModel();
					saleReturnOrder.setIsSameArea(isSameArea);
//					saleReturnOrder.setCode(CodeGeneratorUtil.getNo("XSTO"));
					saleReturnOrder.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSTO));
					saleReturnOrder.setContractNo(contractNo);
					saleReturnOrder.setCreateDate(TimeUtils.getNow());
					saleReturnOrder.setCreateName(user.getName());
					saleReturnOrder.setMerchantId(user.getMerchantId());
					saleReturnOrder.setMerchantName(user.getMerchantName());
					saleReturnOrder.setTopidealCode(user.getTopidealCode());
					saleReturnOrder.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_001);
					saleReturnOrder.setInvoiceNo(invoiceNo);
					saleReturnOrder.setDeliveryAddr(deliveryAddr);
					saleReturnOrder.setPackType(packType);
					if(boxNum != null && StringUtils.isNotBlank(boxNum)){
						saleReturnOrder.setBoxNum(Integer.parseInt(boxNum));
					}
					saleReturnOrder.setLadingBill(ladingBill);
					saleReturnOrder.setMark(mark);
					saleReturnOrder.setShipper(shipper);
					saleReturnOrder.setInDepotId(inDepotInfo.getDepotId());
					saleReturnOrder.setInDepotName(inDepotInfo.getName());
					saleReturnOrder.setInDepotCode(inDepotInfo.getCode());
					saleReturnOrder.setLbxNo(lbxNo);
					saleReturnOrder.setOutDepotId(outDepotInfo.getDepotId());
					saleReturnOrder.setOutDepotName(outDepotInfo.getName());
					saleReturnOrder.setCustomsNo(outDepotInfo.getCustomsNo());
					saleReturnOrder.setInspectNo(outDepotInfo.getInspectNo());
					saleReturnOrder.setOutDepotCode(outDepotInfo.getCode());
					saleReturnOrder.setRemark(remark);
					saleReturnOrder.setReturnType(DERP_ORDER.SALERETURNORDER_RETURNTYPE_2);	// 代销退货
					saleReturnOrder.setPortLoading(portLoading);
					saleReturnOrder.setPayRules(payRules);
					if (StringUtils.isNotBlank(billWeight)) {
						saleReturnOrder.setBillWeight(Double.parseDouble(billWeight));// 提单毛重
					}
					saleReturnOrder.setPortDestNo(portDestNo);
					//获取客户信息
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("code", code);
					params.put("merchantId", user.getMerchantId());
					CustomerMerchantRelMongo customerInfo = customerMerchantRelMongoDao.findOne(params);
					if(customerInfo == null){
						message.put("row", String.valueOf(j+1));
						message.put("message", "客户信息不存在");
						msgList.add(message);
						failure+=1;
						continue;
					}
					saleReturnOrder.setCustomerId(customerInfo.getCustomerId());
					saleReturnOrder.setCustomerName(customerInfo.getName());
					modelMap.put(index, saleReturnOrder);		// 销售退订单表头数据集合
				}catch (Exception e) {
					e.printStackTrace();
					failure+=1;
					continue;
				}
			}
		}
		for (int i = 1; i < 2; i++) {//表体
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				try{
					Map<String,String> message = new HashMap<String,String>();
					Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
					//-------------公共必填字段的校验---------------------------
					String index = map.get("自编销售退货订单号").trim();
					if(index == null || "".equals(index)){
						message.put("row", String.valueOf(j+1));
						message.put("message", "商品明细页的自编销售退货订单号不能为空");
						msgList.add(message);
						failure+=1;
						continue;
					}
					String outGoodsNo = map.get("退出商品货号");
					outGoodsNo = outGoodsNo.trim();
					if(outGoodsNo == null || "".equals(outGoodsNo)){
						message.put("row", String.valueOf(j+1));
						message.put("message", "退出商品货号不能为空");
						msgList.add(message);
						failure+=1;
						continue;
					}
					String inGoodsNo = map.get("退入商品货号");
					if(inGoodsNo == null || "".equals(inGoodsNo)){
						message.put("row", String.valueOf(j+1));
						message.put("message", "退入商品货号不能为空");
						msgList.add(message);
						failure+=1;
						continue;
					}
					inGoodsNo = inGoodsNo.trim();

					String returnNum = map.get("好品数量");
					returnNum = returnNum.trim();
					String badGoodsNum = map.get("坏品数量");
					badGoodsNum = badGoodsNum.trim();

					if((StringUtils.isEmpty(returnNum) &&StringUtils.isEmpty(badGoodsNum))){
						message.put("row", String.valueOf(j+1));
						message.put("message", "好品数量和坏品数量必须填写一个");
						msgList.add(message);
						failure+=1;
						continue;
					}else{
						returnNum2 = StringUtils.isEmpty(returnNum)?0:Integer.valueOf(returnNum);
						badGoodsNum2 = StringUtils.isEmpty(badGoodsNum)?0:Integer.valueOf(badGoodsNum);
						if(returnNum2<0 || badGoodsNum2<0){
							message.put("row", String.valueOf(j+1));
							message.put("message", "好品数量和坏品数量不能为负数");
							msgList.add(message);
							failure+=1;
							continue;
						}else if(( returnNum2 ==0) && (badGoodsNum2==0 )){
								message.put("row", String.valueOf(j+1));
								message.put("message", "好品数量和坏品数量不能都为0");
								msgList.add(message);
								failure+=1;
								continue;
						}
					}

					//-------------公共必填字段的校验end------------------------
					if(!outDepotMap.containsKey(index) || outDepotMap.get(index) == null){
						message.put("row", String.valueOf(j+1));
						message.put("message", "没有表头数据或表头数据保存异常");
						msgList.add(message);
						failure+=1;
						continue;
					}
					if(!inDepotMap.containsKey(index) || inDepotMap.get(index) == null){
						message.put("row", String.valueOf(j+1));
						message.put("message", "没有表头数据或表头数据保存异常");
						msgList.add(message);
						failure+=1;
						continue;
					}
					DepotInfoMongo outDepotInfo = outDepotMap.get(index);
					DepotInfoMongo inDepotInfo = inDepotMap.get(index);
					String price = map.get("退货商品单价");
					price = price.trim();
					// 跨关区退 若退入仓库是保税仓，退货商品单价不能为空；
					String key=index;
					if(isSameAreaByCode.containsKey(key)){
						String isSameAreaStr = isSameAreaByCode.get(index);
						if(DERP.ISSAMEAREA_0.equals(isSameAreaStr) && DERP_SYS.DEPOTINFO_TYPE_A.equals(inDepotInfo.getType())){
							if(price == null || "".equals(price)){
								message.put("row", String.valueOf(j+1));
								message.put("message", "退货商品单价不能为空");
								msgList.add(message);
								failure+=1;
								continue;
							}
						}
				}

					//注入数据
				SaleReturnOrderItemModel saleReturnOrderItem = new SaleReturnOrderItemModel();
				saleReturnOrderItem.setCreateDate(TimeUtils.getNow());
				saleReturnOrderItem.setCreater(user.getId());
//					saleReturnOrderItem.setSeq(i);

				String regex = "^[0-9]{10}$";
				String poDate = map.get("PO单时间").trim();
				if(poDate == null || "".equals(poDate)){
					 if(outDepotCode.equals("VIP001")){
							message.put("row", String.valueOf(j+1));
							message.put("message", "退出仓库为“唯品代销仓”时，PO单时间不能为空");
							msgList.add(message);
							failure+=1;
							continue;
					}
				}

				poNo = map.get("PO单号").trim();
				if (outDepotCode.equals("VIP001")) {
					if (poNo == null || "".equals(poNo)) {
						message.put("row", String.valueOf(j + 1));
						message.put("message", "退出仓库为“唯品代销仓”时，PO号不能为空");
						msgList.add(message);
						failure += 1;
						continue;
					} else {
						if (!poNo.matches(regex)) {
							message.put("row", String.valueOf(j + 1));
							message.put("message", "PO单号只能为10位数字");
							msgList.add(message);
							failure += 1;
							continue;
						}
					}
				}
				if(StringUtils.isNotBlank(poDate)){
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date date1 = null;
					try
					{
					  date1= formatter.parse(poDate);
					} catch (ParseException e)
					{
						message.put("row", String.valueOf(j+1));
						message.put("message", "PO单时间格式不正确");
						msgList.add(message);
						failure+=1;
						continue;
					}
				}

			 	Timestamp standardPoDate = TimeUtils.parseDay(poDate);
				if(StringUtils.isNotBlank(poNo)){
					List<SaleOrderModel> saleModel = saleOrderDao.searchByPo(poNo);
					if(saleModel==null){
						message.put("row", String.valueOf(j+1));
						message.put("message", "该PO单号不存在销售单");
						msgList.add(message);
						failure+=1;
						continue;
					}
				}
				if (StringUtils.isNotBlank(poDate)) {
					try {
						saleReturnOrderItem.setPoDate(standardPoDate);// PO时间
					} catch (Exception e) {
					}
				}
				//获取商品信息
				Map<String, Object> params1 = new HashMap<String,Object>();
				params1.put("goodsNo", outGoodsNo);
				params1.put("merchantId", user.getMerchantId());
				MerchandiseInfoMogo outGoods = merchandiseInfoMogoDao.findOne(params1);

				if(outGoods == null){
					message.put("row", String.valueOf(j+1));
					message.put("message", "退出商品不存在");
					msgList.add(message);
					failure+=1;
					continue;
				}
				/**
				 *（1）当出库仓库在该公司下的“选品限制”为“仅备案商品”时，可选的商品为该公司下仅为备案商品
				 *（2）当出库仓库在该公司下的“选品限制”为“仅外仓统一码”时，可选的商品为该公司下仅为标识为外仓统一码的商品货号；
				 *（3）当出库仓库在该公司下的“选品限制”为“无限制”时，可选的商品为该公司下所有的商品货号信息；
				 */
				Map<String, DepotMerchantRelMongo> map2 = checkGoodsNoByDepotMap.get(key);
				if(checkGoodsNoByDepotMap.containsKey(key)){
					// 退出仓库公司关联信息
					DepotMerchantRelMongo outDepotMerchantInfo = map2.get("outDepotMerchantInfo");
					// （1）当出库仓库在该公司下的“选品限制”为“仅备案商品”时，可选的商品为该公司下仅为备案商品
					if(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(outDepotMerchantInfo.getProductRestriction())){
						if(!DERP_SYS.MERCHANDISEINFO_ISRECORD_1.equals(outGoods.getIsRecord())){
							message.put("row", String.valueOf(j+1));
							message.put("message", "找不到该商品货号"+outGoods.getGoodsNo()+"，当出库仓库在该公司下的“选品限制”为“仅备案商品”时，可选的商品必须是该公司下的备案商品");
							msgList.add(message);
							failure+=1;
							continue;
						}
					}else if(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(outDepotMerchantInfo.getProductRestriction())){
						// （2）当出库仓库在该公司下的“选品限制”为“仅外仓统一码”时，可选的商品为该公司下仅为标识为外仓统一码的商品货号；
						if(!DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0.equals(outGoods.getOutDepotFlag())){
							message.put("row", String.valueOf(j+1));
							message.put("message", "找不到该商品货号"+outGoods.getGoodsNo()+"，当出库仓库在该公司下的“选品限制”为“仅外仓统一码”时，可选的商品为该公司下仅为标识为外仓统一码的商品货号");
							msgList.add(message);
							failure+=1;
							continue;
						}
					}
				}
				boolean flag = false; // 是否根据退出商品取退入商品
				MerchandiseInfoMogo inGoods = null;	// 退入商品
				// 退入仓库公司关联信息
				DepotMerchantRelMongo inDepotMerchantInfo = map2.get("inDepotMerchantInfo");
				// 检验退入商品
				if (!StringUtils.isEmpty(inGoodsNo)) {
					Map<String, Object> inParamMap = new HashMap<String, Object>();
					inParamMap.put("goodsNo", inGoodsNo);
					// 状态(1使用中,0已禁用)
					inParamMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
					inParamMap.put("merchantId", user.getMerchantId());
					inGoods = merchandiseInfoMogoDao.findOne(inParamMap);
					if(inGoods==null || !inGoods.getCommbarcode().equals(outGoods.getCommbarcode())){
						flag = true;
					} else {
						if (inDepotMerchantInfo != null && inDepotMerchantInfo.getProductRestriction().equals(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1)) {
							if (!DERP_SYS.MERCHANDISEINFO_ISRECORD_1.equals(inGoods.getIsRecord())) {
								flag = true;
							}
						} else if (inDepotMerchantInfo != null && inDepotMerchantInfo.getProductRestriction().equals(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2)) {
							if (!DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(inGoods.getOutDepotFlag())) {
								flag = true;
							}
						}
					}
				}
					if (flag || StringUtils.isEmpty(inGoodsNo)) {
						//当入库仓库在本公司下的“选品限制”为“仅备案商品”时，可选的商品为本公司下仅为备案商品
						if (inDepotMerchantInfo != null && inDepotMerchantInfo.getProductRestriction().equals(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1)) {
							Map<String, Object> mparamMap = new HashMap<String, Object>();
							mparamMap.put("commbarcode", outGoods.getCommbarcode());
							// 状态(1使用中,0已禁用)
							mparamMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
							mparamMap.put("isRecord", DERP_SYS.MERCHANDISEINFO_ISRECORD_1);
							mparamMap.put("merchantId", user.getId());
							List<MerchandiseInfoMogo> inMerchandiseList = merchandiseInfoMogoDao.findAll(mparamMap);
							if (inMerchandiseList != null && inMerchandiseList.size() > 0) {
								inGoods = inMerchandiseList.get(0);
							}
						}
						//当入库仓库在本公司下的“选品限制”为“仅外仓统一码”时，根据退出的商品货号查询本公司标准条码下标识为外仓统一码的货号默认回填
						else if (inDepotMerchantInfo != null && inDepotMerchantInfo.getProductRestriction().equals(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2)) {
							Map<String, Object> mparamMap = new HashMap<String, Object>();
							mparamMap.put("commbarcode", outGoods.getCommbarcode());
							// 状态(1使用中,0已禁用)
							mparamMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
							mparamMap.put("outDepotFlag", DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
							mparamMap.put("merchantId", user.getMerchantId());
							List<MerchandiseInfoMogo> inMerchandiseList = merchandiseInfoMogoDao.findAll(mparamMap);
							if (inMerchandiseList != null && inMerchandiseList.size() > 0) {
								inGoods = inMerchandiseList.get(0);
							}
						} else { //当入库仓库在本公司下的“选品限制”为“无限制”时，可选的商品为本公司下所有的商品货号信息
							inGoods = outGoods;
						}
					}

					if(inGoods == null){
						message.put("row", String.valueOf(j+1));
						message.put("message", "根据退出商品找不到对应退入商品");
						msgList.add(message);
						failure+=1;
						continue;
					}

					saleReturnOrderItem.setGrossWeight(inGoods.getGrossWeight());
					saleReturnOrderItem.setNetWeight(inGoods.getNetWeight());

					saleReturnOrderItem.setPoNo(poNo);		// PO单号
					saleReturnOrderItem.setPoDate(standardPoDate);	// PO时间
					saleReturnOrderItem.setOutGoodsCode(outGoods.getCode());
					saleReturnOrderItem.setOutGoodsId(outGoods.getMerchandiseId());
					saleReturnOrderItem.setOutGoodsName(outGoods.getName());
					saleReturnOrderItem.setOutGoodsNo(outGoods.getGoodsNo());
					saleReturnOrderItem.setPrice(StringUtils.isBlank(price) ? BigDecimal.ZERO: new BigDecimal(price));
					Integer totalNum = returnNum2+badGoodsNum2;
					BigDecimal amount = saleReturnOrderItem.getPrice().multiply(new BigDecimal(totalNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
					saleReturnOrderItem.setAmount(amount);
					saleReturnOrderItem.setTaxAmount(amount);//销售总金额（含税）
					saleReturnOrderItem.setTaxRate(0.0);//税率
					saleReturnOrderItem.setTax(BigDecimal.ZERO);//税额
					saleReturnOrderItem.setTaxPrice(StringUtils.isBlank(price) ? BigDecimal.ZERO: new BigDecimal(price));//销售单价（含税）

					// 退入商品货号由退出商品货号系统规则逻辑带出
					saleReturnOrderItem.setInGoodsCode(inGoods.getCode());
					saleReturnOrderItem.setInGoodsId(inGoods.getMerchandiseId());
					saleReturnOrderItem.setInGoodsName(inGoods.getName());
					saleReturnOrderItem.setInGoodsNo(inGoods.getGoodsNo());
					saleReturnOrderItem.setBarcode(inGoods.getBarcode());
					saleReturnOrderItem.setReturnNum(returnNum2);	// 好品数量
					saleReturnOrderItem.setBadGoodsNum(badGoodsNum2);	// 坏品数量


					if(!modelMap.containsKey(index) || modelMap.get(index) == null){
						message.put("row", String.valueOf(j+1));
						message.put("message", "没有表头数据或表头数据保存异常");
						msgList.add(message);
						failure+=1;
						continue;
					}
					//获取退货订单的信息
					SaleReturnOrderModel saleReturnOrder = modelMap.get(index);


					if(map.get("退货批次").trim() != null && !"".equals(map.get("退货批次").trim())){
						saleReturnOrderItem.setReturnBatchNo(map.get("退货批次"));
					}
					//记录表体信息
					List<SaleReturnOrderItemModel> itemModelList = new ArrayList<SaleReturnOrderItemModel>();
					if(itemMap.containsKey(index)){
						itemModelList = itemMap.get(index);
					}
					itemModelList.add(saleReturnOrderItem);
					itemMap.put(index, itemModelList);
					//记录总商品数量
					Integer totalNum1=0;
					if(totalMap.containsKey(index)){
						totalNum1 = totalMap.get(index);
					}
					totalNum1 += returnNum2+ badGoodsNum2;
					totalMap.put(index, totalNum1);

				} catch(Exception e){
					e.printStackTrace();
					failure+=1;
					continue;
				}
			}
		}
		//保存数据
		for(Entry<String, SaleReturnOrderModel> entry : modelMap.entrySet()){
			Map<String,String> message = new HashMap<String,String>();
			String index = entry.getKey();
			SaleReturnOrderModel saleReturnOrder = entry.getValue();
			if(saleReturnOrder.getOutDepotCode().equals(saleReturnOrder.getInDepotCode())){
				saleReturnOrder.setModel("40");
				saleReturnOrder.setServeTypes("G0");
			}else{
				saleReturnOrder.setModel("50");
				saleReturnOrder.setServeTypes("E0");
			}
			//保存表头
			Integer totalNum = totalMap.get(index);
			if(totalNum == null || totalNum == 0){
				message.put("row", "");
				message.put("message", "自编销售退货订单号："+index+"的商品信息为空");
				msgList.add(message);
				failure+=1;
				continue;
			}
			//保存表体
			List<SaleReturnOrderItemModel> itemList = itemMap.get(index);
			if(itemList == null || itemList.size() == 0){
				message.put("row", "");
				message.put("message", "自编销售退货订单号："+index+"的商品信息为空");
				msgList.add(message);
				failure+=1;
				continue;
			}
			if(failure==0){
				saleReturnOrder.setTotalReturnNum(totalNum);		// 计划退货数量
				saleReturnOrderDao.save(saleReturnOrder);			// 将表头存进去
				// 遍历表体
				for (int i = 0; i < itemList.size(); i++) {
					SaleReturnOrderItemModel item = itemList.get(i);
					item.setOrderId(saleReturnOrder.getId());	// 销售退货订单ID
					saleReturnOrderItemDao.save(item);			// 将表体保存进去
					success++;
				}
				commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_16, saleReturnOrder.getCode(), "手工导入", null, null);
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
	 * 导入销售退货订单(消费者退货)
	 * @param json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map importSaleReturnOrder2(Map<Integer, List<List<Object>>> data, User user,String relMerchantIds)throws SQLException{
			int success = 0;
			int pass = 0;
			int failure = 0;
			List<Map<String,String>> msgList = new ArrayList<Map<String,String>>();
			//表头数据集合  ps:序号是表头与表体关联的标识    序号与表头是1对1，表头与表体是1对多
			Map<String,SaleReturnOrderModel> modelMap = new HashMap<String,SaleReturnOrderModel>();
			//表体数据集合
			Map<String,List<SaleReturnOrderItemModel>> itemMap= new HashMap<String,List<SaleReturnOrderItemModel>>();
			//销售退货数据集合
			Map<String,List<SaleReturnOrderRelModel>> relMap= new HashMap<String,List<SaleReturnOrderRelModel>>();
			//销售订单预退货数量集合
			Map<Long,Integer> reNumMap= new HashMap<Long,Integer>();
			//入库出库信息
			Map<String,DepotInfoMongo> inDepotMap = new HashMap<String,DepotInfoMongo>();
			//出库仓库信息
			Map<String,DepotInfoMongo> outDepotMap = new HashMap<String,DepotInfoMongo>();
			//用于统计销售退货订单的总商品数量
			Map<String,Integer> totalMap= new HashMap<String,Integer>();
		    // 检查某个销售订单编号+该仓库公司关联的信息
		    Map<String , DepotMerchantRelMongo> checkGoodsNoByDepotMap = new HashMap<>();
			Map<String, Double> billWeightMap = new HashMap<>(); //存单据对应的提单毛重

			for (int i = 0; i < 1; i++) {//表头
				List<List<Object>> objects = data.get(i);
				for (int j = 1; j < objects.size(); j++) {
					try{
						Map<String,String> message = new HashMap<String,String>();
						Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
						//-------------公共必填字段的校验------------------------

						/**
						 * 退出仓库自编码、退入仓库自编码、退货客户ID、退入商品货号、退货数量为必填项，
						 * 退货商品单价和原有模板中其他字段为非必填；
						 */
						String no = map.get("序号").trim();	// 获取序号
						if(StringUtils.isBlank(no)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "表头序号不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}


						String outDepotCode = map.get("退出仓库自编码").trim();
						if(outDepotCode == null || "".equals(outDepotCode)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "退出仓库自编码不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						//根据仓库编码、代销标识获取仓库信息
						Map<String, Object> params2 = new HashMap<String, Object>();
						params2.put("depotCode", outDepotCode);
						//params2.put("isTopBooks", "1");
						DepotInfoMongo outDepotInfo = depotInfoMongoDao.findOne(params2);
						if(null == outDepotInfo){
							//根据仓库编码获取仓库信息
							Map<String, Object> params3 = new HashMap<String, Object>();
							params3.put("depotCode", outDepotCode);
							outDepotInfo = depotInfoMongoDao.findOne(params3);
							if(null == outDepotInfo){//获取不到，则该编码没有对应的仓库信息
								message.put("row", String.valueOf(j+1));
								message.put("message", "出仓仓库不存在");
								msgList.add(message);
								failure+=1;
								continue;
							}
							//根据仓库编码获取到仓库信息，则该仓库非代销仓
							message.put("row", String.valueOf(j+1));
							message.put("message", "该仓库不允许退货");
							msgList.add(message);
							failure+=1;
							continue;
						}
						// 仓库公司关联表
						Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
						depotMerchantRelParam.put("merchantId", user.getMerchantId());
						depotMerchantRelParam.put("depotId", outDepotInfo.getDepotId());
						DepotMerchantRelMongo depotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
						if(depotMerchantRelMongo == null || "".equals(depotMerchantRelMongo)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "仓库ID为："+outDepotInfo.getDepotId()+",未查到该公司下调出仓库信息");
							msgList.add(message);
							failure+=1;
							continue;
						}
						String key=no;
						if(!checkGoodsNoByDepotMap.containsKey(key)){
							checkGoodsNoByDepotMap.put(key, depotMerchantRelMongo);
						}

						String inDepotCode = map.get("退入仓库自编码").trim();
						if(inDepotCode == null || "".equals(inDepotCode)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "退入仓库自编码不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}

						//获取入仓仓库信息
						Map<String, Object> params1 = new HashMap<String, Object>();
						params1.put("depotCode", inDepotCode);
						DepotInfoMongo inDepotInfo = depotInfoMongoDao.findOne(params1);
						if(null == inDepotInfo){
							message.put("row", String.valueOf(j+1));
							message.put("message", "入仓仓库不存在");
							msgList.add(message);
							failure+=1;
							continue;
						}
						if(!inDepotCode.equals("XS001")){
							message.put("row", String.valueOf(j+1));
							message.put("message", "退入仓仓库必须为销毁区");
							msgList.add(message);
							failure+=1;
							continue;
						}
						inDepotMap.put(no, inDepotInfo);
						outDepotMap.put(no, outDepotInfo);
						String code = map.get("退货客户ID").trim();
						if(code == null || "".equals(code)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "退货客户ID不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						String  buCode= map.get("事业部");
						if(buCode == null || "".equals(buCode)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "事业部不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						// 获取该事业部信息
						Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
						merchantBuRelParam.put("merchantId", user.getMerchantId());
						merchantBuRelParam.put("buCode", buCode);
						merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);	// 启用
						MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
						if(merchantBuRelMongo == null || "".equals(merchantBuRelMongo)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "事业部编码为："+outDepotInfo.getDepotId()+",未查到该公司下事业部信息");
							msgList.add(message);
							failure+=1;
							continue;
						}
						// 校验公司- 出仓仓库-事业部的关联表
						if(outDepotInfo.getDepotId()!=null){
							Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
							merchantDepotBuRelParam.put("merchantId", user.getMerchantId());
							merchantDepotBuRelParam.put("depotId", outDepotInfo.getDepotId());	// 出仓仓库
							merchantDepotBuRelParam.put("buId", merchantBuRelMongo.getBuId());
							MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
							if(outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)){
								message.put("row", String.valueOf(j+1));
								message.put("message", "事业部编码为："+outDepotInfo.getDepotId()+",出仓仓库："+outDepotCode+",未查到该公司仓库事业部关联信息");
								msgList.add(message);
								failure+=1;
								continue;
							}
						}
						// 校验公司- 入仓仓库-事业部的关联表
						if(inDepotInfo.getDepotId()!=null){
							Map<String, Object> merchantDepotBuRelParam = new HashMap<String, Object>();
							merchantDepotBuRelParam.put("merchantId", user.getMerchantId());
							merchantDepotBuRelParam.put("depotId", inDepotInfo.getDepotId());	// 退入仓库
							merchantDepotBuRelParam.put("buId", merchantBuRelMongo.getBuId());
							MerchantDepotBuRelMongo outMerchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(merchantDepotBuRelParam);
							if(outMerchantDepotBuRelMongo == null || "".equals(outMerchantDepotBuRelMongo)){
								message.put("row", String.valueOf(j+1));
								message.put("message", "事业部编码为："+outDepotInfo.getDepotId()+",退入仓库："+inDepotCode+",未查到该公司仓库事业部关联信息");
								msgList.add(message);
								failure+=1;
								continue;
							}
						}
						// 校验事业部与当前账号所绑定的事业部是否匹配
						boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), merchantBuRelMongo.getBuId());
						if(!userRelateBu){
							message.put("row", String.valueOf(j+1));
							message.put("message", "事业部编码为："+merchantBuRelMongo.getBuCode()+",用户id："+user.getId()+",无权限操作该事业部");
							msgList.add(message);
							failure+=1;
							continue;
						}
						/**库位类型*/
						String stockLocationName = map.get("库位类型");
						//通过“公司+事业部”查询公司事业部是否启用了库位管理，若启用则该字段必填
						if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0.equals(merchantBuRelMongo.getStockLocationManage()) && StringUtils.isBlank(stockLocationName)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"已开启库位管理，库位类型不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}else if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_1.equals(merchantBuRelMongo.getStockLocationManage()) && StringUtils.isNotBlank(stockLocationName)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"未开启库位管理，库位类型不允许填写");
							msgList.add(message);
							failure+=1;
							continue;
						}
						BuStockLocationTypeConfigMongo stockLocationMongo = null;
						if(StringUtils.isNotBlank(stockLocationName)){
							Map<String,Object> stockLocationMap = new HashMap<>();
							stockLocationMap.put("merchantId", user.getMerchantId());
							stockLocationMap.put("buId", merchantBuRelMongo.getBuId());
							stockLocationMap.put("name", stockLocationName);
							stockLocationMap.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
							stockLocationMongo = buStockLocationTypeConfigMongoDao.findOne(stockLocationMap);

							if(stockLocationMongo == null){
								message.put("row", String.valueOf(j+1));
								message.put("message", "当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"库位类型："+stockLocationName+"，不存在");
								msgList.add(message);
								failure+=1;
								continue;
							}
						}

						//-------------公共必填字段的校验end------------------------

//						String xstoCode=CodeGeneratorUtil.getNo("XSTO");
						String xstoCode=SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSTO);
						//注入数据
						SaleReturnOrderModel saleReturnOrder = new SaleReturnOrderModel();
						saleReturnOrder.setCode(xstoCode);
						saleReturnOrder.setCreateDate(TimeUtils.getNow());
						saleReturnOrder.setCreateName(user.getName());
						saleReturnOrder.setMerchantId(user.getMerchantId());
						saleReturnOrder.setMerchantName(user.getMerchantName());
						saleReturnOrder.setTopidealCode(user.getTopidealCode());
						saleReturnOrder.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_001);
						saleReturnOrder.setInDepotId(inDepotInfo.getDepotId());
						saleReturnOrder.setInDepotName(inDepotInfo.getName());
						saleReturnOrder.setInDepotCode(inDepotInfo.getCode());
						saleReturnOrder.setOutDepotId(outDepotInfo.getDepotId());
						saleReturnOrder.setOutDepotName(outDepotInfo.getName());
						saleReturnOrder.setCustomsNo(outDepotInfo.getCustomsNo());
						saleReturnOrder.setInspectNo(outDepotInfo.getInspectNo());
						saleReturnOrder.setOutDepotCode(outDepotInfo.getCode());
						saleReturnOrder.setReturnType("1");	// 消费者退货
						//获取客户信息
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("code", code);
						params.put("merchantId", user.getMerchantId());
						CustomerMerchantRelMongo customerInfo = customerMerchantRelMongoDao.findOne(params);
						if(customerInfo == null){
							message.put("row", String.valueOf(j+1));
							message.put("message", "客户信息不存在");
							msgList.add(message);
							failure+=1;
							continue;
						}
						saleReturnOrder.setCustomerId(customerInfo.getCustomerId());
						saleReturnOrder.setCustomerName(customerInfo.getName());
						saleReturnOrder.setBuId(merchantBuRelMongo.getBuId());	// 事业部ID
						saleReturnOrder.setBuName(merchantBuRelMongo.getBuName());
						if(stockLocationMongo != null){
							saleReturnOrder.setStockLocationTypeId(stockLocationMongo.getBuStockLocationTypeId());
							saleReturnOrder.setStockLocationTypeName(stockLocationMongo.getName());
						}
						modelMap.put(no, saleReturnOrder);
					}catch (Exception e) {
						e.printStackTrace();
						failure+=1;
						continue;
					}
				}
			}
			for (int i = 1; i < 2; i++) {//表体
				List<List<Object>> objects = data.get(i);
				for (int j = 1; j < objects.size(); j++) {
					try{
						Map<String,String> message = new HashMap<String,String>();
						Map<String, String> map = this.toMap(data.get(i).get(0).toArray(),objects.get(j).toArray());
						//-------------公共必填字段的校验---------------------------
						String no=map.get("序号").trim();
						if(StringUtils.isBlank(no)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "商品信息序号不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}

						String inGoodsNo = map.get("退入商品货号").trim();
						if(inGoodsNo == null || "".equals(inGoodsNo)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "退入商品货号不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						String returnNum = map.get("退货数量").trim();
						if(returnNum == null || "".equals(returnNum)){
							message.put("row", String.valueOf(j+1));
							message.put("message", "退货数量不能为空");
							msgList.add(message);
							failure+=1;
							continue;
						}
						//-------------公共必填字段的校验end------------------------
						if(!outDepotMap.containsKey(no) || outDepotMap.get(no) == null){
							message.put("row", String.valueOf(j+1));
							message.put("message", "没有表头数据或表头数据保存异常");
							msgList.add(message);
							failure+=1;
							continue;
						}
						if(!inDepotMap.containsKey(no) || inDepotMap.get(no) == null){
							message.put("row", String.valueOf(j+1));
							message.put("message", "没有表头数据或表头数据保存异常");
							msgList.add(message);
							failure+=1;
							continue;
						}
						DepotInfoMongo outDepotInfo = outDepotMap.get(no);
						DepotInfoMongo inDepotInfo = inDepotMap.get(no);
						String price = map.get("退货商品单价").trim();

						//注入数据
						SaleReturnOrderItemModel saleReturnOrderItem = new SaleReturnOrderItemModel();
						saleReturnOrderItem.setCreateDate(TimeUtils.getNow());
						saleReturnOrderItem.setCreater(user.getId());
						if(price != null && StringUtils.isNotBlank(price)){
							saleReturnOrderItem.setPrice(new BigDecimal(price));
						}
						//获取商品信息
						Map<String, Object> params = new HashMap<String,Object>();
						params.put("goodsNo", inGoodsNo);
						params.put("merchantId", user.getMerchantId());
						MerchandiseInfoMogo inGoods = merchandiseInfoMogoDao.findOne(params);

						if(inGoods == null){
							message.put("row", String.valueOf(j+1));
							message.put("message", "退入商品不存在");
							msgList.add(message);
							failure+=1;
							continue;
						}
						/**
						 *（1）当出库仓库在该公司下的“选品限制”为“仅备案商品”时，可选的商品为该公司下仅为备案商品
						 *（2）当出库仓库在该公司下的“选品限制”为“仅外仓统一码”时，可选的商品为该公司下仅为标识为外仓统一码的商品货号；
						 *（3）当出库仓库在该公司下的“选品限制”为“无限制”时，可选的商品为该公司下所有的商品货号信息；
						 */
						String key=no;
						if(checkGoodsNoByDepotMap.containsKey(key)){
							DepotMerchantRelMongo depotMerchantRelMongo = checkGoodsNoByDepotMap.get(no);
							// （1）当出库仓库在该公司下的“选品限制”为“仅备案商品”时，可选的商品为该公司下仅为备案商品
							if(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(depotMerchantRelMongo.getProductRestriction())){
								if(!DERP_SYS.MERCHANDISEINFO_ISRECORD_1.equals(inGoods.getIsRecord())){
									message.put("row", String.valueOf(j+1));
									message.put("message", "找不到该商品货号"+inGoods.getGoodsNo()+"，当出库仓库在该公司下的“选品限制”为“仅备案商品”时，可选的商品必须是该公司下的备案商品");
									msgList.add(message);
									failure+=1;
									continue;
								}
							}else if(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(depotMerchantRelMongo.getProductRestriction())){
								// （2）当出库仓库在该公司下的“选品限制”为“仅外仓统一码”时，可选的商品为该公司下仅为标识为外仓统一码的商品货号；
								if(!DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0.equals(inGoods.getOutDepotFlag())){
									message.put("row", String.valueOf(j+1));
									message.put("message", "找不到该商品货号"+inGoods.getGoodsNo()+"，当出库仓库在该公司下的“选品限制”为“仅外仓统一码”时，可选的商品为该公司下仅为标识为外仓统一码的商品货号");
									msgList.add(message);
									failure+=1;
									continue;
								}
							}
						}
						saleReturnOrderItem.setReturnNum(Integer.valueOf(returnNum));	// 退货好品数量
						saleReturnOrderItem.setBadGoodsNum(0);	// 退货坏品数量(目前先把它默认设为0)
						saleReturnOrderItem.setInGoodsCode(inGoods.getCode());
						saleReturnOrderItem.setInGoodsId(inGoods.getMerchandiseId());
						saleReturnOrderItem.setInGoodsName(inGoods.getName());
						saleReturnOrderItem.setInGoodsNo(inGoods.getGoodsNo());
						saleReturnOrderItem.setBarcode(inGoods.getBarcode());

						// 序号
						saleReturnOrderItem.setSeq(j);

						double grossWeight = 0.0;//毛重
						double netWeight = 0.0;//净重

						grossWeight=inGoods.getGrossWeight()==null?0.0:inGoods.getGrossWeight();
						netWeight=inGoods.getNetWeight()==null?0.0:inGoods.getNetWeight();
						Double billWeight = grossWeight*Integer.valueOf(returnNum); //商品毛重
						saleReturnOrderItem.setGrossWeight(billWeight);
						saleReturnOrderItem.setNetWeight(netWeight*Integer.valueOf(returnNum));

						//记录表体信息
						List<SaleReturnOrderItemModel> itemModelList = new ArrayList<SaleReturnOrderItemModel>();
						if(itemMap.containsKey(no)){
							itemModelList = itemMap.get(no);
							billWeight += billWeightMap.get(no);
							billWeightMap.put(no, billWeight);
						}else{
							billWeightMap.put(no, billWeight);
						}

						itemModelList.add(saleReturnOrderItem);
						itemMap.put(no, itemModelList);


						//记录总商品数量
						Integer totalNum=0;
						if(totalMap.containsKey(no)){
							totalNum = totalMap.get(no);
						}
						totalNum += Integer.valueOf(returnNum);
						totalMap.put(no, totalNum);

					} catch(Exception e){
						e.printStackTrace();
						failure+=1;
						continue;
					}
				}
			}
			//保存数据
			for(Entry<String, SaleReturnOrderModel> entry : modelMap.entrySet()){
				Map<String,String> message = new HashMap<String,String>();
				String index = entry.getKey();
				SaleReturnOrderModel saleReturnOrder = entry.getValue();
				//保存表头
				Integer totalNum = totalMap.get(index);
				if(totalNum == null || totalNum == 0){
					message.put("row", "");
					message.put("message", "销售退货订单号："+saleReturnOrder.getCode()+"的商品信息为空");
					msgList.add(message);
					failure+=1;
					continue;
				}
				//保存表体
				List<SaleReturnOrderItemModel> itemList = itemMap.get(index);
				if(itemList == null || itemList.size() == 0){
					message.put("row", "");
					message.put("message", "自编销售退货订单号："+index+"的商品信息为空");
					msgList.add(message);
					failure+=1;
					continue;
				}
				if(failure==0){
					Double billWeight = billWeightMap.get(index);
					saleReturnOrder.setBillWeight(billWeight);
					saleReturnOrder.setTotalReturnNum(totalNum);
					saleReturnOrderDao.save(saleReturnOrder);	// 保存表头
					for(SaleReturnOrderItemModel item:itemList){
						item.setOrderId(saleReturnOrder.getId());
						// 保存表体
						saleReturnOrderItemDao.save(item);
						success++;
					}
					commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_16, saleReturnOrder.getCode(), "手工导入", null, null);
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
	 * 获取选中订单的所有商品和对应数量（相同商品数量叠加）
	 */
	@Override
	public Map<String,Integer> getOrderGoodsInfo(List<Long> ids) throws Exception {
		Map<String,Integer> map = new HashMap<String,Integer>();
		for (Long id : ids) {
			// 根据id获取销售退货订单信息
			SaleReturnOrderModel saleReturnOrder = saleReturnOrderDao.searchById(id);

			//单据状态不是待审核
			if(!DERP_ORDER.SALERETURNORDER_STATUS_001.equals(saleReturnOrder.getStatus())){
				throw new RuntimeException("审核失败，单据状态不为：待审核");
			}
			//获取仓库信息
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotId", saleReturnOrder.getOutDepotId());
			DepotInfoMongo outDepot = depotInfoMongoDao.findOne(params);
			//获取销售退表体（商品信息）
			SaleReturnOrderItemModel saleReturnOrderItem = new SaleReturnOrderItemModel();
			saleReturnOrderItem.setOrderId(saleReturnOrder.getId());
			List<SaleReturnOrderItemModel> itemList = saleReturnOrderItemDao.list(saleReturnOrderItem);

//			if("1".equals(saleReturnOrder.getReturnType())){	// 消费者退货
//				// 遍历销售退表体（商品信息）
//				for(SaleReturnOrderItemModel item:itemList){
//					Integer num = 0;
//					if(map.containsKey(item.getOutGoodsId()+"-"+saleReturnOrder.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getOutGoodsNo())){
//						num = map.get(item.getOutGoodsId()+"-"+saleReturnOrder.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getOutGoodsNo());
//					}
//					num+=item.getReturnNum();	// 好品数量
//					map.put(item.getOutGoodsId()+"-"+saleReturnOrder.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getOutGoodsNo(), num);
//				}
//			}else{
					// 代销者退货
					// 是否同关区（0-否，1-是）
					if("1".equals(saleReturnOrder.getIsSameArea())){	// 同关区
						// 遍历销售退表体（商品信息）
						for(SaleReturnOrderItemModel item:itemList){
							Integer num = 0;
							if(map.containsKey(item.getOutGoodsId()+"-"+saleReturnOrder.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getOutGoodsNo()+"-"+saleReturnOrder.getIsSameArea()+"-"+saleReturnOrder.getReturnType())){
								num = map.get(item.getOutGoodsId()+"-"+saleReturnOrder.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getOutGoodsNo()+"-"+saleReturnOrder.getIsSameArea()+"-"+saleReturnOrder.getReturnType());
							}
							// 由于历史数据都为空,所以先判断为空的设为0
							if (item.getBadGoodsNum()==null) {
								item.setBadGoodsNum(0);
							}
							num+=item.getReturnNum()+item.getBadGoodsNum();		// 合计值:好品数量+坏品数量
							map.put(item.getOutGoodsId()+"-"+saleReturnOrder.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getOutGoodsNo()+"-"+saleReturnOrder.getIsSameArea()+"-"+saleReturnOrder.getReturnType(), num);
						}
					}else{	// 跨关区

						Map<String,Integer> map2 = new HashMap<String,Integer>();
						// 遍历销售退表体（商品信息）
						for(SaleReturnOrderItemModel item:itemList){
							Integer num1 = 0;	// 存好品数量
							if(map2.containsKey(item.getOutGoodsId()+"-"+saleReturnOrder.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getOutGoodsNo()+"1")){
								num1 = map2.get(item.getOutGoodsId()+"-"+saleReturnOrder.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getOutGoodsNo()+"1");
							}
							num1+=item.getReturnNum();	// 好品数量
							map2.put(item.getOutGoodsId()+"-"+saleReturnOrder.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getOutGoodsNo()+"1", num1);

							Integer num2 = 0;	// 存坏品数量
							if(map2.containsKey(item.getOutGoodsId()+"-"+saleReturnOrder.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getOutGoodsNo()+"2")){
								num2 = map2.get(item.getOutGoodsId()+"-"+saleReturnOrder.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getOutGoodsNo()+"2");
							}
							// 由于历史数据都为空,所以先判断为空的设为0
							if (item.getBadGoodsNum()==null) {
								item.setBadGoodsNum(0);
							}
							num2+=item.getBadGoodsNum();	// 坏品数量
							map2.put(item.getOutGoodsId()+"-"+saleReturnOrder.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getOutGoodsNo()+"2", num2);

							map.put(item.getOutGoodsId()+"-"+saleReturnOrder.getOutDepotId()+"-"+outDepot.getCode()+"-"+item.getOutGoodsNo()+"-"+saleReturnOrder.getIsSameArea()+"-"+saleReturnOrder.getReturnType()+"-"+num2, num1);
						}
					}
		//	}
		}
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

	@Override
	public SaleReturnOrderDTO listSaleOrderAndOutDepotOrder(SaleReturnOrderDTO dto, Long merchantId) throws SQLException {
		SaleOrderDTO saleOrderDto = saleOrderDao.searchDTOById(dto.getSaleOrderId());

		dto.setReturnType(DERP_ORDER.SALERETURNORDER_RETURNTYPE_3);// 购销退货
		dto.setCustomerId(saleOrderDto.getCustomerId());	// 客户
		dto.setCustomerName(saleOrderDto.getCustomerName());
		dto.setOutDepotId(saleOrderDto.getOutDepotId());	// 退出仓库(出仓仓库)
		dto.setOutDepotName(saleOrderDto.getOutDepotName());
		dto.setInDepotId(saleOrderDto.getInDepotId());	// 退入仓库(入仓仓库)
		dto.setInDepotName(saleOrderDto.getInDepotName());
		dto.setSaleOrderRelCode(saleOrderDto.getCode());	// 关联的销售订单号
		dto.setBuId(saleOrderDto.getBuId());
		dto.setBuName(saleOrderDto.getBuName());
		dto.setSaleOrderId(saleOrderDto.getId());
		dto.setBusinessModel(saleOrderDto.getBusinessModel());
		dto.setCurrency(saleOrderDto.getCurrency());

		//注入表体数据
		for(SaleReturnOrderItemDTO  returnItemDTO : dto.getItemList()){
			//查询货品
			Double grossWeight = 0.0;//毛重
			Double netWeight = 0.0;//净重
			//获取商品信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("barcode", returnItemDTO.getBarcode());
			params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);	// 状态(1使用中,0已禁用)
			params.put("merchantId", merchantId);
			List<MerchandiseInfoMogo> inGoodsList = merchandiseInfoMogoDao.findMerchandiseByDepotId(params,saleOrderDto.getOutDepotId());
			if(inGoodsList != null && inGoodsList.size() > 0){
				MerchandiseInfoMogo inGoods = inGoodsList.get(0);
				returnItemDTO.setInGoodsCode(inGoods.getCode());
				grossWeight = inGoods.getGrossWeight() == null ? 0.0 :inGoods.getGrossWeight();
				netWeight = inGoods.getNetWeight()== null ? 0.0 :inGoods.getNetWeight();

				returnItemDTO.setGrossWeight(grossWeight);
				returnItemDTO.setNetWeight(netWeight);
				returnItemDTO.setInGoodsId(inGoods.getMerchandiseId());
				returnItemDTO.setInGoodsCode(inGoods.getGoodsCode());
				returnItemDTO.setInGoodsNo(inGoods.getGoodsNo());
				returnItemDTO.setInGoodsName(inGoods.getName());
			}else{
				returnItemDTO.setInGoodsName(returnItemDTO.getInGoodsName());
			}

			// 退出仓(若选了多条销售订单，随机取一个销售订单的入仓仓库)为空，则默认退出商品为退入商品
			if(saleOrderDto.getInDepotId() == null){
				returnItemDTO.setOutGoodsId(returnItemDTO.getInGoodsId());
				returnItemDTO.setOutGoodsCode(returnItemDTO.getInGoodsCode());
				returnItemDTO.setOutGoodsName(returnItemDTO.getInGoodsName());
				returnItemDTO.setOutGoodsNo(returnItemDTO.getInGoodsNo());
			}else{
				// 退出仓库不为空，则根据退入商品标准条码+选品限制查询退出商品
				// 查询入库仓库公司关联信息
				Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
				depotMerchantRelParam.put("merchantId", merchantId);
				depotMerchantRelParam.put("depotId",saleOrderDto.getInDepotId() );	// 退入仓库
				DepotMerchantRelMongo inDepotMerchantRelMongo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
				if(inDepotMerchantRelMongo == null || "".equals(inDepotMerchantRelMongo)){
					LOGGER.info("入库仓库ID："+saleOrderDto.getInDepotId()+",未查到该公司下入库仓库信息");
					throw new RuntimeException("入库仓库ID："+saleOrderDto.getInDepotId()+",未查到该公司下入库仓库信息");
				}
				/**
				 * 根据上架单的条形码+退出仓库 查询关联的商品货号，
				 */
				Map<String, Object> paramMap = new HashMap<String, Object>();
				List<MerchandiseInfoMogo> inMerchandiseList = new ArrayList<>();
				paramMap.put("barcode", returnItemDTO.getBarcode());
				paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);	// 状态(1使用中,0已禁用)
				paramMap.put("merchantId", merchantId);
				List<MerchandiseInfoMogo> merchandiseInfoMogoList = merchandiseInfoMogoDao.findMerchandiseByDepotId(paramMap,saleOrderDto.getInDepotId());
				if(merchandiseInfoMogoList == null || merchandiseInfoMogoList.size() < 1 ){
					LOGGER.info("根据退入商品条形码:"+returnItemDTO.getBarcode()+",入库仓库:"+saleOrderDto.getInDepotName()+"未找到关联商品");
					throw new RuntimeException("根据退入商品条形码:"+returnItemDTO.getBarcode()+",入库仓库:"+saleOrderDto.getInDepotName()+"未找到关联商品");
				}
				MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoList.get(0);
				returnItemDTO.setOutGoodsId(merchandiseInfoMogo.getMerchandiseId());
				returnItemDTO.setOutGoodsCode(merchandiseInfoMogo.getGoodsCode());
				returnItemDTO.setOutGoodsName(merchandiseInfoMogo.getName());
				returnItemDTO.setOutGoodsNo(merchandiseInfoMogo.getGoodsNo());
			}
		}
		return dto;
	}
	/**
	 * 新增编辑销售退
	 */
	@Override
	public void saveSaleReturnOrder(SaleReturnOrderDTO dto, User user) throws Exception {
		if(dto.getBuId() == null){
			throw new RuntimeException("保存失败，请选择事业部");
		}
		if(StringUtils.isBlank(dto.getReturnType())){
			throw new RuntimeException("保存失败，退货类型不能为空");
		}
		if(dto.getCustomerId() == null){
			throw new RuntimeException("保存失败，客户不能为空");
		}
		if(dto.getInDepotId() == null){
			throw new RuntimeException("保存失败，退入仓库不能为空");
		}
		if(DERP_ORDER.SALERETURNORDER_RETURNTYPE_3.equals(dto.getReturnType()) && StringUtils.isBlank(dto.getReturnMode())){
			throw new RuntimeException("保存失败，购销退货时，退货方式不能为空");
		}
		if(StringUtils.isBlank(dto.getBusinessModel())){
			throw new RuntimeException("保存失败，销售类型不能为空");
		}
		if(StringUtils.isBlank(dto.getCurrency())){
			throw new RuntimeException("保存失败，币种不能为空");
		}
		DepotInfoMongo outDepotInfo = null;
		if(dto.getOutDepotId() != null){
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("depotId", dto.getOutDepotId());
			outDepotInfo = depotInfoMongoDao.findOne(params1);
		}

		// 获取该事业部信息
		Map<String, Object> merchantBuRelParam = new HashMap<String, Object>();
		merchantBuRelParam.put("merchantId", dto.getMerchantId());
		merchantBuRelParam.put("buId", dto.getBuId());
		merchantBuRelParam.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1);	// 启用
		MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(merchantBuRelParam);
		if(merchantBuRelMongo == null || "".equals(merchantBuRelMongo)){
			throw new RuntimeException("事业部ID为："+dto.getBuId()+",未查到该公司下事业部信息");
		}
		// 校验事业部与当前账号所绑定的事业部是否匹配
		boolean userRelateBu = userBuRelMongoDao.isUserRelateBu(user.getId(), merchantBuRelMongo.getBuId());
		if(!userRelateBu){
			throw new RuntimeException("事业部编码为："+merchantBuRelMongo.getBuCode()+",用户id："+user.getId()+",无权限操作该事业部");
		}

		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("depotId", dto.getInDepotId());
		DepotInfoMongo inDepotInfo = depotInfoMongoDao.findOne(params1);
		if(inDepotInfo == null){
			throw new RuntimeException("保存失败，退入仓库不存在");
		}

		//-----------------------菜鸟仓参数校验---------------------------------
		if(StringUtils.isNotBlank(dto.getLbxNo())){
			if(inDepotInfo.getName().contains("菜鸟")){
				if(StringUtils.isBlank(dto.getLbxNo())){
					throw new RuntimeException("保存失败，退入仓库为菜鸟仓，lbx单号不能为空");
				}
				Map<String, Object> lbx_params = new HashMap<String, Object>();
				lbx_params.put("lbxNo", dto.getLbxNo());
				lbx_params.put("type", "XSTO");	// XSTO-销售退货
				// 删除lbx号
				lbxNoMongoDao.remove(lbx_params);

				LbxNoMongo lbxNoMongo = new LbxNoMongo();
				lbxNoMongo.setLbxNo(dto.getLbxNo());
				lbxNoMongo.setType("XSTO");
				// 往mongodb插入lbx号
				lbxNoMongoDao.insert(lbxNoMongo);
			}
			//新增
			if(dto.getId() == null){
				// 根据lbx号查询销售退货订单 非删除状态的销售退货订单
				SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();
				saleReturnOrderModel.setLbxNo(dto.getLbxNo());
				List<SaleReturnOrderModel> saleReturnOrderList = saleReturnOrderDao.searchNotDelStatusSaleReturnOrder(saleReturnOrderModel);
				if (saleReturnOrderList.size()>0) {
					throw new RuntimeException("保存失败，销售退货订单LBX单号已经存在");
				}
			}
		}
		if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0.equals(merchantBuRelMongo.getStockLocationManage()) && dto.getStockLocationTypeId() == null){
			throw new RuntimeException("当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"已开启库位管理，库位类型不能为空");
		}else if(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_1.equals(merchantBuRelMongo.getStockLocationManage()) && dto.getStockLocationTypeId() != null){
			throw new RuntimeException("当前公司主体下，事业部编码：" + merchantBuRelMongo.getBuCode()+"未开启库位管理，库位类型不允许填写");
		}
		if(dto.getStockLocationTypeId() != null){
			Map<String,Object> stockLocationMap = new HashMap<>();
			stockLocationMap.put("buStockLocationTypeId", dto.getStockLocationTypeId());
			stockLocationMap.put("status", DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1);
			BuStockLocationTypeConfigMongo stockLocationMongo = buStockLocationTypeConfigMongoDao.findOne(stockLocationMap);
			if(stockLocationMongo == null){
				throw new RuntimeException("库位类型输入有误") ;
			}
			if(stockLocationMongo != null){
				dto.setStockLocationTypeName(stockLocationMongo.getName());
			}
		}

		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("customerid", dto.getCustomerId());
		CustomerInfoMogo customer = customerInfoMongoDao.findOne(params2);
		//注入数据
		SaleReturnOrderModel saleReturnOrder = new SaleReturnOrderModel();
		BeanUtils.copyProperties(dto, saleReturnOrder);
		saleReturnOrder.setInDepotName(inDepotInfo.getName());
		saleReturnOrder.setInDepotCode(inDepotInfo.getCode());
		saleReturnOrder.setBuName(merchantBuRelMongo.getBuName());
		if(outDepotInfo != null){
			saleReturnOrder.setOutDepotId(outDepotInfo.getDepotId());
			saleReturnOrder.setOutDepotName(outDepotInfo.getName());
			saleReturnOrder.setOutDepotCode(outDepotInfo.getCode());
		}
		saleReturnOrder.setCustomerName(customer == null ? "" : customer.getName());
		//购销退货
		if(StringUtils.isNotBlank(dto.getSaleOrderRelCode())){
			SaleOrderModel saleOrderModel = new SaleOrderModel();
			saleOrderModel.setCode(dto.getSaleOrderRelCode());
			saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);
			saleReturnOrder.setSaleOrderId(saleOrderModel.getId());
		}
		saleReturnOrder.setTopidealCode(user.getTopidealCode());
		saleReturnOrder.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_001);

		if(dto.getReturnType().equals(DERP_ORDER.SALERETURNORDER_RETURNTYPE_2)||dto.getReturnType().equals(DERP_ORDER.SALERETURNORDER_RETURNTYPE_3)){
			if(StringUtils.isNotBlank(saleReturnOrder.getOutDepotCode())&&saleReturnOrder.getOutDepotCode().equals(saleReturnOrder.getInDepotCode())){
				saleReturnOrder.setModel("40");
				saleReturnOrder.setServeTypes("G0");
			}else{
				saleReturnOrder.setModel("50");
				saleReturnOrder.setServeTypes("E0");
			}
		}
		//解析表体数据
		if( dto.getItemList() == null ||  dto.getItemList().size() < 1){
			throw new RuntimeException("保存失败，销售退商品信息不能为空");
		}
		Integer totalNum = 0;
		Set<String> poList = new HashSet<String>();
		List<SaleReturnOrderItemModel> itemList=new ArrayList<SaleReturnOrderItemModel>();
		for(SaleReturnOrderItemDTO itemDTO : dto.getItemList()){
			// 当类型为代销退货/购销退货两种类型中退出仓为“	唯品会备查库”时则需要PO单号必填；其余情况非必填
			if(DERP_ORDER.SALERETURNORDER_RETURNTYPE_2.equals(dto.getReturnType()) || DERP_ORDER.SALERETURNORDER_RETURNTYPE_3.equals(dto.getReturnType())){
				if(null!=outDepotInfo && "VIP001".equals(outDepotInfo.getDepotCode()) && StringUtils.isBlank(itemDTO.getPoNo())){
					throw new RuntimeException("保存失败，退出仓为“唯品会备查库”时PO单号不能为空");
				}
			}
			//获取退入商品信息
			if(itemDTO.getInGoodsId() == null){
				throw new RuntimeException("请选择退入商品" );
			}
			Map<String, Object> inParamMap = new HashMap<String, Object>();
			inParamMap.put("merchandiseId", itemDTO.getInGoodsId());
			MerchandiseInfoMogo inMerchandise = merchandiseInfoMogoDao.findOne(inParamMap);
			if(inMerchandise==null){	// 商品不存在保存失败
				throw new RuntimeException("商品ID为：" + itemDTO.getInGoodsId() + "，该商品不存在" );
			}
			//获取退出商品信息
			MerchandiseInfoMogo outMerchandise = null;
			if(itemDTO.getOutGoodsId() != null) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("merchandiseId", itemDTO.getOutGoodsId());
				outMerchandise = merchandiseInfoMogoDao.findOne(paramMap);
				if(outMerchandise==null){	// 商品不存在保存失败
					throw new RuntimeException("商品ID为：" + itemDTO.getOutGoodsId() + "，该商品不存在") ;
				}
			}else {
				 throw new RuntimeException("请选择退出商品") ;
			}

			if(itemDTO.getReturnNum() == null){
				throw new RuntimeException("保存失败，退出数量不能为空");
			}

			/**
			 * 当类型为购销退货时:
			 * 1.校验所选的退入商品货号必须存在已勾选的销售订单中
			 * 2.对应商品货号的退货商品数量不得大于已勾选销售订单中该商品货号的总销售数量，点击保存时做强校验
			 */
			if(dto.getReturnType().equals(DERP_ORDER.SALERETURNORDER_RETURNTYPE_3)){
				if(StringUtils.isBlank(itemDTO.getPoNo())) {
					throw new RuntimeException("保存失败，所选的退入商品货号:"+inMerchandise.getGoodsNo()+" PO号不能为空");
				}

//				SaleOrderItemModel saleItemModel = new SaleOrderItemModel();
//				saleItemModel.setOrderId(saleReturnOrder.getSaleOrderId());
//				saleItemModel.setGoodsId(itemDTO.getInGoodsId());
//				List<SaleOrderItemModel> saleItemList = saleOrderItemDao.list(saleItemModel);
//				if(saleItemList != null && saleItemList.size() > 0) {
//					if(saleItemList.get(0).getPrice().compareTo(itemDTO.getPrice()) < 0) {
//						throw new RuntimeException("保存失败，所选的退入商品货号:"+inMerchandise.getGoodsNo()+" 单价不得大于对应商品货号的销售单价");
//					}
//				}

				SaleShelfModel saleShelfModel = new  SaleShelfModel();
                saleShelfModel.setOrderId(saleReturnOrder.getSaleOrderId());
                saleShelfModel.setBarcode(itemDTO.getBarcode());
				saleShelfModel.setPoNo(itemDTO.getPoNo());
                List<SaleShelfModel> saleShelfList = saleShelfDao.list(saleShelfModel);
				if(saleShelfList != null) {
					Integer shelfNum = saleShelfList.stream().map(SaleShelfModel::getShelfNum).reduce(0, Integer::sum);
					Integer lackNum = saleShelfList.stream().map(SaleShelfModel::getLackNum).reduce(0, Integer::sum);
					Integer damageNum = saleShelfList.stream().map(SaleShelfModel::getDamagedNum).reduce(0, Integer::sum);
					if(itemDTO.getReturnNum() > (shelfNum + lackNum + damageNum)){
						throw new RuntimeException("保存失败，对应商品条形码:"+inMerchandise.getBarcode()+"的退货商品数量不得大于已勾选销售订单中该条形码的合计上架数量");
					}
				}else{
					throw new RuntimeException("保存失败，所选的商品条形码:"+inMerchandise.getBarcode()+"不存在已勾选的销售订单中");
				}
			}

			//注入数据
			SaleReturnOrderItemModel saleReturnOrderItem = new SaleReturnOrderItemModel();
			BeanUtils.copyProperties(itemDTO, saleReturnOrderItem);
			saleReturnOrderItem.setBadGoodsNum(0);

			saleReturnOrderItem.setPoDate(TimeUtils.parseDay(itemDTO.getPoDateStr()));
			totalNum += itemDTO.getReturnNum();
			if(StringUtils.isNotBlank(itemDTO.getPoNo())){
				poList.add(itemDTO.getPoNo());
			}

			saleReturnOrderItem.setInGoodsNo(inMerchandise.getGoodsNo());
			saleReturnOrderItem.setInGoodsName(inMerchandise.getName());
			saleReturnOrderItem.setInGoodsCode(inMerchandise.getGoodsCode());
			saleReturnOrderItem.setBarcode(inMerchandise.getBarcode());
			saleReturnOrderItem.setOutGoodsNo(outMerchandise.getGoodsNo());
			saleReturnOrderItem.setOutGoodsName(outMerchandise.getName());
			saleReturnOrderItem.setOutGoodsCode(outMerchandise.getGoodsCode());
			itemList.add(saleReturnOrderItem);
		}
		//将商品明细表的PO单号汇总到表头PO字段，表头字段不可编辑
		saleReturnOrder.setPoNo(StringUtils.join(poList ,"&"));
		saleReturnOrder.setTotalReturnNum(totalNum);	// 计划退货数量
		//保存
		Long id = null;
		if(dto.getId() == null){
			saleReturnOrder.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSTO));
			saleReturnOrder.setCreateDate(TimeUtils.getNow());
			saleReturnOrder.setCreateName(user.getName());
			saleReturnOrder.setCreater(user.getId());
			id = saleReturnOrderDao.save(saleReturnOrder);
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_16, saleReturnOrder.getCode(), "新增", null, null);
		}else{
			SaleReturnOrderModel querySaleReturnOrderModel =  saleReturnOrderDao.searchById(dto.getId());

			saleReturnOrder.setCode(querySaleReturnOrderModel.getCode());
			saleReturnOrder.setCreateDate(querySaleReturnOrderModel.getCreateDate());
			saleReturnOrder.setCreateName(querySaleReturnOrderModel.getCreateName());
			saleReturnOrder.setCreater(querySaleReturnOrderModel.getCreater());
			saleReturnOrder.setModifyDate(TimeUtils.getNow());
			saleReturnOrder.setIsGenerateDeclare("0");
			saleReturnOrderDao.modifyWithNull(saleReturnOrder);

			//先删除原表体信息
			SaleReturnOrderItemModel returnItemModel = new SaleReturnOrderItemModel();
			returnItemModel.setOrderId(dto.getId());
			List<SaleReturnOrderItemModel> delItemList = saleReturnOrderItemDao.list(returnItemModel);
			List<Long> delIemIds = delItemList.stream().map(SaleReturnOrderItemModel::getId).collect(Collectors.toList());
			saleReturnOrderItemDao.delete(delIemIds);
			id = dto.getId();

			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_16, querySaleReturnOrderModel.getCode(), "编辑", null, null);
		}
		for(SaleReturnOrderItemModel itemModel : itemList){
			itemModel.setOrderId(id);
			saleReturnOrderItemDao.save(itemModel);
		}

	}

	@Override
	public void isSameParameter(String ids) throws SQLException, RuntimeException {
		SaleOrderModel saleOrderModel = new SaleOrderModel();
		Long outDepotId=null;	// 仓库
		Long customerId=null;	// 客户
		String businessModel=null;	// 销售类型
		List<Long> idList = StrUtils.parseIds(ids);
		for (Long id : idList) {
			// 根据id获取信息
			saleOrderModel = new SaleOrderModel();
			saleOrderModel.setId(id);
			saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);
			if(businessModel == null){
				businessModel = saleOrderModel.getBusinessModel();
			}else if(!businessModel.equals(saleOrderModel.getBusinessModel())){
				throw new RuntimeException("选单失败，只能选相同销售类型的单据");
			}
			if(outDepotId == null){
				outDepotId = saleOrderModel.getOutDepotId();
			}else if(!outDepotId.equals(saleOrderModel.getOutDepotId())){
				throw new RuntimeException("选单失败，只能选相同的出仓仓库");
			}
			if(customerId == null){
				customerId = saleOrderModel.getCustomerId();
			}else if(!customerId.equals(saleOrderModel.getCustomerId())){
				throw new RuntimeException("选单失败，只能选相同的客户");
			}
		}
	}

	/**
	 * 可核销量=上架入库量+临时结转量+账单入库量+调拨入量-账单出库量-调拨出量-销售退货量
	 */
	private Integer getVerifiNum(Long merchantId, Long depotId, String poNo,List<String> goodsNoList){

		/**公共查询条件*/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantId",merchantId);
		paramMap.put("depotId",depotId);
		paramMap.put("poNo",poNo);
		paramMap.put("goodsNoList",goodsNoList);

		//1.上架入库量
		Integer shelfInNum = shelfIdepotItemDao.getshelfInNum(paramMap);
		if (shelfInNum == null) shelfInNum = 0;

		//2.临时结转量
		Integer jztemNum = pojzTempDao.getPojzNum(paramMap);
		if (jztemNum == null) jztemNum = 0;

		//3.账单入库量
		paramMap.put("operateType",DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//库存调整类型  0 调减 1调增
		Integer billInNum = billOutinDepotItemDao.getBillOutInDepotNum(paramMap);
		if(billInNum ==null) billInNum = 0;

		//4.账单出库量
		paramMap.put("operateType",DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//库存调整类型  0 调减 1调增
		Integer billOutNum = billOutinDepotItemDao.getBillOutInDepotNum(paramMap);
		if(billOutNum ==null) billOutNum = 0;
		//唯品暂存区
		Map<String,Object> depotParam = new HashMap<>();
		depotParam.put("depotCode", "WPTH001");
		DepotInfoMongo wzcDepot = depotInfoMongoDao.findOne(depotParam);
		//唯品备查库
		depotParam.put("depotCode", "VIP001");
		DepotInfoMongo vipDepot = depotInfoMongoDao.findOne(depotParam);

		//5.调拨入量=调拨单调出仓库为“唯品暂存区”，且调入仓库为“唯品备查库” 已入库的入库数量
		paramMap.put("outDepotId", wzcDepot.getDepotId());
		paramMap.put("inDepotId", vipDepot.getDepotId());
		Integer transferInNum = transferOrderDao.getTransferInNumByMap(paramMap);
		if(transferInNum==null) transferInNum=0;

		//6.调拨出量=调拨单调出仓库为“唯品备查库”，调入仓库为“唯品暂存区” 已出库的数量
		paramMap.put("outDepotId", vipDepot.getDepotId());
		paramMap.put("inDepotId", wzcDepot.getDepotId());
		Integer transferOutNum = transferOrderDao.getTransferOutNumByMap(paramMap);
		if(transferOutNum==null) transferOutNum=0;

		//7.销售退货数量
		Integer saleRetrunNum = saleReturnOrderDao.getReturnCount(paramMap); // 退货量(好品+坏品)
		if (saleRetrunNum == null) saleRetrunNum = 0;

		//可核销量=上架入库量+临时结转量+账单入库量+调拨入量-账单出库量-调拨出量-销售退货量
		Integer verifiNum = shelfInNum+jztemNum+billInNum+transferInNum-billOutNum-transferOutNum-saleRetrunNum;
		return verifiNum;
	}

	@Override
	public Map<String, String> isOutdepotReport(Long id) throws Exception {
		Map<String, String> result = new HashMap<>();
		SaleReturnOrderModel sroModel = new SaleReturnOrderModel();
		sroModel.setId(id);
		SaleReturnOrderModel saleReturnOrderModel = saleReturnOrderDao.searchByModel(sroModel);
		if(DERP_ORDER.SALERETURNORDER_RETURNTYPE_1.equals(saleReturnOrderModel.getReturnType())){
			result.put("code", "01");
			result.put("message", "该销售退订单类型为消费者退货，不用出库打托");
			return result;
		}
		// 当单据状态为“已审核”时或“已入库”时，有出库打托
		if(DERP_ORDER.SALERETURNORDER_STATUS_003.equals(saleReturnOrderModel.getStatus()) ||
				DERP_ORDER.SALERETURNORDER_STATUS_012.equals(saleReturnOrderModel.getStatus())){
			SaleReturnOdepotModel saleReturnOdepot = new SaleReturnOdepotModel();
			saleReturnOdepot.setOrderId(id);
			List<SaleReturnOdepotModel> list = saleReturnOdepotDao.list(saleReturnOdepot);
			// 校验当单据状态为“已审核”或“已入库”时且有销售退出库数据时，不可操作出库打托；
			if(list.size()>0){
				result.put("code", "01");
				result.put("message", "该销售退订单存在出库中/已出库的出库单，不允许重复出库");
				return result;
			}
		}else{
			result.put("code", "01");
			result.put("message", "该销售退订单单据状态不为“已审核”或“已入库”，不允许出库打托");
			return result;
		}
		result.put("code", "00");
		return result;
	}

	/**
	 * 拿到该商品原始的毛重净重
	 * @param itemList
	 * @return
	 */
	@Override
	public List<SaleReturnOrderItemDTO> getOrignGrossNetWeightItem(List<SaleReturnOrderItemDTO> itemList) {
		List<SaleReturnOrderItemDTO> saleReturnOrderItemList = new ArrayList<>();
		for (int i = 0; i < itemList.size(); i++) {
			SaleReturnOrderItemDTO saleReturnOrderItemDTO = itemList.get(i);

			//获取商品信息
			Map<String, Object> params1 = new HashMap<String,Object>();
			params1.put("merchandiseId", saleReturnOrderItemDTO.getInGoodsId());
			MerchandiseInfoMogo inGoods = merchandiseInfoMogoDao.findOne(params1);
			saleReturnOrderItemList.add(saleReturnOrderItemDTO);
		}
		return saleReturnOrderItemList;
	}



	/**
	 * 获取表体详情
	 */
	@Override
	public List<SaleReturnOrderItemDTO> getSaleReturnOrderItem(SaleReturnOrderItemDTO itemDto) throws Exception {
		return saleReturnOrderItemDao.getSaleReturnOrderItem(itemDto);
	}

	@Override
	public Map<String, String> validInDepotDate(Long id, String returnInDate) throws Exception {
		Map<String, String> result = new HashMap<>();
		SaleReturnOrderModel model = saleReturnOrderDao.searchById(id);


		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String today = sdf.format(new Date());
		if (today.compareTo(returnInDate) < 0) {
			result.put("code", "01");
			result.put("message", "入库日期必须小于或等于当前日期");
			return result;
		}
		// 获取最大的关账日/月结日期
		FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(model.getMerchantId());
		closeAccountsMongo.setDepotId(model.getInDepotId());
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
		if (org.apache.commons.lang3.StringUtils.isNotBlank(maxdate)) {
			// 获取该月份下月时间
			String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
			maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(maxCloseAccountsMonth)) {
			// 入库日期必须大于关账日期
//			String closeDate = sdf.format(maxCloseAccountsMonth);
			if (returnInDate.compareTo(maxCloseAccountsMonth) < 0) {
				result.put("code", "01");
				result.put("message", "入库日期必须大于关账日期/月结日期");
				return result;
			}
		}
		result.put("code", "00");
		return result;
	}
	@Override
	public Map<String, String> isExistSaleReturnIdepot(SaleReturnOrderDTO dto) throws SQLException {
		Map<String, String> result = new HashMap<>();
		SaleReturnIdepotModel saleReturnIdepot = new SaleReturnIdepotModel();
		saleReturnIdepot.setOrderId(dto.getId());
		saleReturnIdepot = saleReturnIdepotDao.searchByModel(saleReturnIdepot);
		if (saleReturnIdepot != null && !saleReturnIdepot.getStatus().equals(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_006)) {
			result.put("code", "01");
			result.put("message", "该销售退货订单已经入库,不允许重复入库");
			return result;
		}
		result.put("code", "00");
		return result;
	}

	@Override
	public Map<String, String> saveSaleReturnIdepot(JSONObject jsonData, User user) throws Exception {
    	Map<String, String> result = new HashMap<>();
    	String returnInDate = (String) jsonData.get("returnInDate");
    	Long orderId = jsonData.getLong("orderId");
    	JSONArray jsonArray = jsonData.getJSONArray("goodsList");

    	//查询销售退货订单
		SaleReturnOrderModel orderModel = saleReturnOrderDao.searchById(orderId);
		if (orderModel == null) {
			result.put("code", "01");
			result.put("message", "该销售退货订单不存在！");
			return result;
		}

		//校验该销售退货订单是否存在调拨出库单，若不存在则不能上架入库
		SaleReturnOdepotModel saleReturnOut=new SaleReturnOdepotModel();
		saleReturnOut.setOrderId(orderId);
		saleReturnOut = saleReturnOdepotDao.searchByModel(saleReturnOut);


		//校验该销售退货订单是否已有存在调拨入库单
		SaleReturnIdepotModel saleReturnIn=new SaleReturnIdepotModel();
		saleReturnIn.setOrderId(orderId);
		saleReturnIn = saleReturnIdepotDao.searchByModel(saleReturnIn);
		if (saleReturnIn!=null) {
			result.put("code", "01");
			result.put("message", "该销售退货已存在入库单，不允许重复入库!");
			return result;
		}

		//根据仓库id到mongoDB中查询调入仓库信息
		Map<String, Object> inDepotInfoMap = new HashMap<>();
		inDepotInfoMap.put("depotId", orderModel.getInDepotId());//调入仓库id
		DepotInfoMongo inDepotInfoMongo = depotInfoMongoDao.findOne(inDepotInfoMap);

		//事业部
		Map<String, Object> buMap = new HashMap<>();
		buMap.put("merchantId",user.getMerchantId());
		buMap.put("buId", orderModel.getBuId());
		MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(buMap);
		if (merchantBuRelMongo == null) {
			throw new RuntimeException("该销售退货订单下的公司事业部不存在!");
		}
		int returnInNumTotal=0;
		List<SaleReturnIdepotItemModel> newItemDtos = new ArrayList<>();
		for (Object object : jsonArray) {
    		JSONObject itemJson=(JSONObject) object;
    		Long inGoodsId = itemJson.getLong("inGoodsId");
    		Long outGoodsId = itemJson.getLong("outGoodsId");
    		String tallyingUnit = (String) jsonData.get("tallyingUnit");
    		Integer returnNum = itemJson.getInt("returnNum");//依次扣减出库数量后剩余好品数量
    		Integer badGoodsNum = itemJson.getInt("badGoodsNum");//依次扣减出库数量后剩余坏品数量
			String returnBatchNo = (String) itemJson.get("returnBatchNo");	// 退货批次
			String productionDate = (String) itemJson.get("productionDate");	// 生产日期
			String overdueDate = (String) itemJson.get("overdueDate");	// 失效日期
			String poNo = (String) itemJson.get("poNo");	// po号

    		returnInNumTotal=returnInNumTotal+returnNum+badGoodsNum;
    		Map<String, Object> params=new  HashMap<>();
    		params.put("merchandiseId", inGoodsId);
    		MerchandiseInfoMogo inMerchandiseMongo = merchandiseInfoMogoDao.findOne(params);
    		if (inMerchandiseMongo==null) {
    			throw new RuntimeException("该销售退货订单入库商品不存在,入库商品id:"+inGoodsId);
			}
    		List<SaleReturnOdepotItemModel> outDepotItemModels=new ArrayList<>();
    		if (saleReturnOut!=null) {
        		outDepotItemModels = saleReturnOdepotItemDao.getSaleReturnOdepotItemList(saleReturnOut.getId(), outGoodsId);
			}

    		// 销售退货出库 除了已入定出会有过期品量,其他没有过期品的量  (已入定出不会走上架入库流程)
    		for (int i = 0; i < outDepotItemModels.size(); i++) {
    			Integer outWornNum = outDepotItemModels.get(i).getWornNum();//调出坏品数量
				Integer outGoodNum = outDepotItemModels.get(i).getReturnNum();//调出好品数量
				Integer outNum = outWornNum + outGoodNum;
				String rBatchNo = StringUtils.isBlank(returnBatchNo) ? outDepotItemModels.get(i).getReturnBatchNo() : returnBatchNo;
				Date rProDate = StringUtils.isBlank(productionDate) ? outDepotItemModels.get(i).getProductionDate() : TimeUtils.strToSqlDate(productionDate);
				Date rOverDueDate = StringUtils.isBlank(overdueDate) ? outDepotItemModels.get(i).getOverdueDate() : TimeUtils.strToSqlDate(overdueDate);
				if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(inDepotInfoMongo.getBatchValidation()) || DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(inDepotInfoMongo.getBatchValidation())){
					if(StringUtils.isBlank(rBatchNo)) {
						throw new RuntimeException("批次效期强校验的仓库，批次号不能为空！");
					}
					if(rProDate == null) {
						throw new RuntimeException("批次效期强校验的仓库，生产日期不能为空！");
					}
					if(rOverDueDate == null) {
						throw new RuntimeException("批次效期强校验的仓库，失效日期不能为空！");
					}
				}
				//1.如果调出商品的好品数量+坏品数量 >= 调入商品数量则直接取先失效的批次、效期
				// 2.如果是扣减批次的最后一个批次，则全部入库到该批次下
				if (outNum >= returnNum+badGoodsNum || i == outDepotItemModels.size()-1) {
					SaleReturnIdepotItemModel newItem = new SaleReturnIdepotItemModel();
					newItem.setInGoodsId(inGoodsId);
					newItem.setInGoodsCode(inMerchandiseMongo.getGoodsCode());
					newItem.setInGoodsName(inMerchandiseMongo.getName());
					newItem.setInGoodsNo(inMerchandiseMongo.getGoodsNo());
					newItem.setBarcode(inMerchandiseMongo.getBarcode());
					newItem.setCommbarcode(inMerchandiseMongo.getCommbarcode());
					newItem.setWornNum(badGoodsNum);
					newItem.setReturnNum(returnNum);
					newItem.setReturnBatchNo(rBatchNo);
					newItem.setProductionDate(rProDate);
					newItem.setOverdueDate(rOverDueDate);
					newItem.setPoNo(poNo);
					newItemDtos.add(newItem);
					break;
				} else {
					//如果调出商品的好品数量+坏品数量 < 调入商品数量则依次扣减先失效的批次、效期
					if (outNum >= badGoodsNum && outNum-badGoodsNum < returnNum) {
						SaleReturnIdepotItemModel newItem = new SaleReturnIdepotItemModel();
						newItem.setInGoodsId(inGoodsId);
						newItem.setInGoodsCode(inMerchandiseMongo.getGoodsCode());
						newItem.setInGoodsName(inMerchandiseMongo.getName());
						newItem.setInGoodsNo(inMerchandiseMongo.getGoodsNo());
						newItem.setBarcode(inMerchandiseMongo.getBarcode());
						newItem.setCommbarcode(inMerchandiseMongo.getCommbarcode());
						newItem.setWornNum(badGoodsNum);
						newItem.setReturnNum(outNum-badGoodsNum);
						newItem.setReturnBatchNo(rBatchNo);
						newItem.setProductionDate(rProDate);
						newItem.setOverdueDate(rOverDueDate);
						newItem.setPoNo(poNo);
						newItemDtos.add(newItem);
						returnNum = returnNum - (outNum-badGoodsNum);
						badGoodsNum = 0;
					} else if (outNum < badGoodsNum) { //如果调出商品的好品数量+坏品数量 < 调入商品坏品数量则依次扣减先失效的批次、效期
						SaleReturnIdepotItemModel newItem = new SaleReturnIdepotItemModel();
						newItem.setInGoodsId(inGoodsId);
						newItem.setInGoodsCode(inMerchandiseMongo.getGoodsCode());
						newItem.setInGoodsName(inMerchandiseMongo.getName());
						newItem.setInGoodsNo(inMerchandiseMongo.getGoodsNo());
						newItem.setBarcode(inMerchandiseMongo.getBarcode());
						newItem.setCommbarcode(inMerchandiseMongo.getCommbarcode());
						newItem.setWornNum(outNum);
						newItem.setReturnNum(0);
						newItem.setReturnBatchNo(rBatchNo);
						newItem.setProductionDate(rProDate);
						newItem.setOverdueDate(rOverDueDate);
						newItem.setPoNo(poNo);
						newItemDtos.add(newItem);
						badGoodsNum -= outNum;
					}
				}


			}

    		//如果对应调出商品未出库
			if (outDepotItemModels == null || outDepotItemModels.size() == 0) {
				if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(inDepotInfoMongo.getBatchValidation()) || DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(inDepotInfoMongo.getBatchValidation())){
					if(StringUtils.isBlank(returnBatchNo)) {//
						throw new RuntimeException("批次效期强校验的仓库，批次号不能为空！");
					}
					if(productionDate == null) {
						throw new RuntimeException("批次效期强校验的仓库，生产日期不能为空！");
					}
					if(overdueDate == null) {
						throw new RuntimeException("批次效期强校验的仓库，失效日期不能为空！");
					}
				}
				SaleReturnIdepotItemModel newItem = new SaleReturnIdepotItemModel();
				newItem.setInGoodsId(inGoodsId);
				newItem.setInGoodsCode(inMerchandiseMongo.getGoodsCode());
				newItem.setInGoodsName(inMerchandiseMongo.getName());
				newItem.setInGoodsNo(inMerchandiseMongo.getGoodsNo());
				newItem.setBarcode(inMerchandiseMongo.getBarcode());
				newItem.setCommbarcode(inMerchandiseMongo.getCommbarcode());
				newItem.setWornNum(badGoodsNum);
				newItem.setReturnNum(returnNum);
				newItem.setReturnBatchNo(returnBatchNo);
				newItem.setProductionDate(TimeUtils.strToSqlDate(productionDate));
				newItem.setOverdueDate(TimeUtils.strToSqlDate(overdueDate));
				newItem.setPoNo(poNo);
				newItemDtos.add(newItem);
			}

		}


		SaleReturnIdepotModel inDepotModel = new SaleReturnIdepotModel();
		inDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSTR));
		inDepotModel.setOrderId(orderId);
		inDepotModel.setMerchantId(orderModel.getMerchantId());
		inDepotModel.setMerchantName(orderModel.getMerchantName());
		inDepotModel.setContractNo(orderModel.getContractNo());
		inDepotModel.setInDepotId(orderModel.getInDepotId());
		inDepotModel.setInDepotName(orderModel.getInDepotName());
		inDepotModel.setOutDepotId(orderModel.getOutDepotId());
		inDepotModel.setOutDepotName(orderModel.getOutDepotName());
		inDepotModel.setReturnInDate(TimeUtils.parseDay(returnInDate));
		inDepotModel.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_028);
		inDepotModel.setCreater(user.getId());
		inDepotModel.setOrderCode(orderModel.getCode());
		//inDepotModel.setMerchantReturnNo();
		inDepotModel.setInspectNo(inDepotInfoMongo.getInspectNo());
		inDepotModel.setCustomsNo(inDepotInfoMongo.getCustomsNo());
		inDepotModel.setModel(orderModel.getModel());
		inDepotModel.setServeTypes(orderModel.getServeTypes());
		inDepotModel.setCustomerId(orderModel.getCustomerId());
		inDepotModel.setCustomerName(orderModel.getCustomerName());
		//inDepotModel.setRemark(remark);
		inDepotModel.setLbxNo(orderModel.getLbxNo());
		inDepotModel.setReturnInNum(returnInNumTotal);
		//inDepotModel.setInExternalCode(inExternalCode);
		//inDepotModel.setYunjiAccountNo(yunjiAccountNo);
		inDepotModel.setBuName(orderModel.getBuName());
		inDepotModel.setBuId(orderModel.getBuId());
		if("1".equals(orderModel.getIsGenerateDeclare())) {
			SaleReturnDeclareOrderDTO saleReturnDeclareOrderDTO = new SaleReturnDeclareOrderDTO();
			saleReturnDeclareOrderDTO.setSaleReturnOrderCodes(orderModel.getCode());
			List<SaleReturnDeclareOrderDTO> declareList = saleReturnDeclareOrderDao.listReturnDeclareOrder(saleReturnDeclareOrderDTO);
			if(declareList != null && declareList.size() > 0) {
				inDepotModel.setReturnDeclareOrderId(declareList.get(0).getId());
				inDepotModel.setReturnDeclareOrderCode(declareList.get(0).getCode());
			}
		}
		saleReturnIdepotDao.save(inDepotModel);


		String inventoryUnit = "";//理货单位
		if (DERP.ORDER_TALLYINGUNIT_00.equals(orderModel.getTallyingUnit())) {
			inventoryUnit = DERP.INVENTORY_UNIT_0;
		} else if (DERP.ORDER_TALLYINGUNIT_01.equals(orderModel.getTallyingUnit())) {
			inventoryUnit = DERP.INVENTORY_UNIT_1;
		} else if (DERP.ORDER_TALLYINGUNIT_02.equals(orderModel.getTallyingUnit())) {
			inventoryUnit = DERP.INVENTORY_UNIT_2;
		}
		List<InvetAddOrSubtractGoodsListJson> subGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();// 加库存商品
		//销售退货入库单表体
		for (SaleReturnIdepotItemModel item : newItemDtos) {
			item.setSreturnIdepotId(inDepotModel.getId());

			saleReturnIdepotItemDao.save(item);
			Map<String, Object> mParam = new HashMap<>();
			mParam.put("merchandiseId", item.getInGoodsId());
			MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(mParam);


			if (item.getReturnNum() != null && item.getReturnNum().intValue() > 0){
				// 拼装加库存商品
				InvetAddOrSubtractGoodsListJson aSGood = new InvetAddOrSubtractGoodsListJson();

				aSGood.setGoodsId(String.valueOf(item.getInGoodsId()));
				aSGood.setGoodsName(item.getInGoodsName());
				aSGood.setGoodsNo(item.getInGoodsNo());
				aSGood.setBarcode(item.getBarcode());
				aSGood.setProductionDate(TimeUtils.formatDay(item.getProductionDate()));
				aSGood.setOverdueDate(TimeUtils.formatDay(item.getOverdueDate()));
				aSGood.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);// 商品分类 （0 好品 1坏品） 字符串
				aSGood.setNum(item.getReturnNum());
				aSGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 操作类型（调增\调减） 字符串 0 调减 1调增
				String unit = orderModel.getTallyingUnit();
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepotInfoMongo.getType())) {
					aSGood.setUnit(inventoryUnit);
				}
				aSGood.setUnit(unit);
				aSGood.setIsExpire(DERP.ISEXPIRE_1);
				aSGood.setBatchNo(item.getReturnBatchNo());
				aSGood.setProductionDate(TimeUtils.formatDay(item.getProductionDate()));
				aSGood.setOverdueDate(TimeUtils.formatDay(item.getOverdueDate()));
				aSGood.setStockLocationTypeId(orderModel.getStockLocationTypeId() == null ? "" : orderModel.getStockLocationTypeId().toString());
				aSGood.setStockLocationTypeName(orderModel.getStockLocationTypeName());
				subGoodsList.add(aSGood);
			}

			if (item.getWornNum() != null && item.getWornNum().intValue() > 0){
				// 拼装加库存商品
				InvetAddOrSubtractGoodsListJson aSGood = new InvetAddOrSubtractGoodsListJson();

				aSGood.setGoodsId(String.valueOf(item.getInGoodsId()));
				aSGood.setGoodsName(item.getInGoodsName());
				aSGood.setGoodsNo(item.getInGoodsNo());
				aSGood.setBarcode(item.getBarcode());
				aSGood.setProductionDate(TimeUtils.formatDay(item.getProductionDate()));
				aSGood.setOverdueDate(TimeUtils.formatDay(item.getOverdueDate()));
				aSGood.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);// 商品分类 （0 好品 1坏品） 字符串
				aSGood.setNum(item.getWornNum());
				aSGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 操作类型（调增\调减） 字符串 0 调减 1调增
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepotInfoMongo.getType())) {
					aSGood.setUnit(inventoryUnit);
				}
				aSGood.setIsExpire(DERP.ISEXPIRE_1);
				aSGood.setBatchNo(item.getReturnBatchNo());
				aSGood.setProductionDate(TimeUtils.formatDay(item.getProductionDate()));
				aSGood.setOverdueDate(TimeUtils.formatDay(item.getOverdueDate()));
				aSGood.setStockLocationTypeId(orderModel.getStockLocationTypeId() == null ? "" : orderModel.getStockLocationTypeId().toString());
				aSGood.setStockLocationTypeName(orderModel.getStockLocationTypeName());
				subGoodsList.add(aSGood);
			}
		}
		// 拼装加库存接口参数
		InvetAddOrSubtractRootJson subInventoryRoot = new InvetAddOrSubtractRootJson();
		subInventoryRoot.setMerchantId(String.valueOf(orderModel.getMerchantId()));
		subInventoryRoot.setMerchantName(orderModel.getMerchantName());
		subInventoryRoot.setTopidealCode(orderModel.getTopidealCode());
		subInventoryRoot.setDepotType(inDepotInfoMongo.getType());
		subInventoryRoot.setIsTopBooks(inDepotInfoMongo.getIsTopBooks());
		subInventoryRoot.setDepotId(String.valueOf(inDepotInfoMongo.getDepotId()));
		subInventoryRoot.setDepotCode(inDepotInfoMongo.getCode());
		subInventoryRoot.setDepotName(inDepotInfoMongo.getName());
		subInventoryRoot.setOrderNo(inDepotModel.getCode());
		subInventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_004); //DBDD("004","销售退货订单"),
		subInventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_005); //DBRK("005","退货入库"),
		subInventoryRoot.setSourceDate(TimeUtils.formatFullTime());
		subInventoryRoot.setOwnMonth(TimeUtils.formatMonth(inDepotModel.getReturnInDate()));
		subInventoryRoot.setDivergenceDate(TimeUtils.format(inDepotModel.getReturnInDate(), "yyyy-MM-dd HH:mm:ss"));
		subInventoryRoot.setBusinessNo(orderModel.getCode());
		subInventoryRoot.setBuId(String.valueOf(orderModel.getBuId()));
		subInventoryRoot.setBuName(orderModel.getBuName());
		subInventoryRoot.setGoodsList(subGoodsList);
		//回调参数
		subInventoryRoot.setBackTopic(MQPushBackOrderEnum.SALER_ETURN_IDEPOT_IN_PUSH_BACK.getTopic());
		subInventoryRoot.setBackTags(MQPushBackOrderEnum.SALER_ETURN_IDEPOT_IN_PUSH_BACK.getTags());
		Map<String, Object> customParam=new HashMap<>();
		customParam.put("code", orderModel.getCode());
		subInventoryRoot.setCustomParam(customParam);
		// 加库存
		JSONObject subjson = JSONObject.fromObject(subInventoryRoot);
		rocketMQProducer.send(subjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
				MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

		commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_16, orderModel.getCode(), "上架入库", null, null);
		result.put("code", "00");
		result.put("message", "保存成功!");
		return result;
	}

	/**
	 * 查询导出数据
	 */
	@Override
	public List<SaleReturnOrderDTO> getListDTO(SaleReturnOrderDTO dto, User user) throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return null;
			}
			dto.setBuList(buList);
		}
		List<SaleReturnOrderDTO> list = saleReturnOrderDao.listDTO(dto);
		return list;
	}

	@Override
	public Boolean checkCreateDeclare(String ids) throws Exception {
		List<Long> saleIds = StrUtils.parseIds(ids);
		List<String> checkOrder = new ArrayList<String>();
		for(Long saleId :saleIds) {
			SaleReturnOrderModel model = saleReturnOrderDao.searchById(saleId);
			//销售订单状态必须为【已审核】或【部分出库】
			if(!DERP_ORDER.SALERETURNORDER_STATUS_003.equals(model.getStatus()) &&!DERP_ORDER.SALERETURNORDER_STATUS_016.equals(model.getStatus())) {
				throw new RuntimeException("销售退订单状态必须为“已审核”或“已出仓”");
			}
			// 查询某公司某仓库的相关信息
			Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
			depotMerchantRelParam.put("merchantId", model.getMerchantId());
			depotMerchantRelParam.put("depotId", model.getInDepotId());
			DepotMerchantRelMongo depotMerchantRelInfo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);

		 	//销售退入仓仓库是 在对应公司下的“进出接口指令”标识为“是”的，才能转预申报
			if(depotMerchantRelInfo!=null && DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_0.equals(depotMerchantRelInfo.getIsInOutInstruction())) {
				throw new RuntimeException("销售退订单："+model.getCode()+" 入仓仓库："+model.getInDepotName()+" 在对应公司下的“进出接口指令”标识为“否”，无法转销售退预申报");
			}

			String outDepotIdStr = model.getOutDepotId() == null ? "": model.getOutDepotId().toString();//出库仓可能为空
			String key = model.getMerchantId()+model.getBuId()+outDepotIdStr+ model.getCurrency()+ model.getInDepotId()+ model.getBusinessModel();
			if(checkOrder.size() < 1){
				checkOrder.add(key);
			}else if (!checkOrder.contains(key)) {
				throw new RuntimeException("请选择相同公司+事业部+退出仓库+退入仓库+销售类型+销售币种的销售退单据");
			}
		}
		return true;
	}

	@Override
	public List<SaleReturnOrderItemDTO> getSalePopupListByPage(String saleOrderCode , String unNeedGoodsIdsAndPoNo, String goodsName,String goodsNo,String barcode, String poNo ,Long inDepotId) throws Exception{

		SaleOrderModel querySaleModel = new SaleOrderModel();
		querySaleModel.setCode(saleOrderCode);
		querySaleModel = saleOrderDao.searchByModel(querySaleModel);

		String unNeedIds = "";
		if(StringUtils.isNotBlank(unNeedGoodsIdsAndPoNo)){
			Set<Long> unNeedIdList = new HashSet<Long>();
			for(String unNeed : unNeedGoodsIdsAndPoNo.split(",")) {
				String unNeedPoNo = unNeed.split("__")[0];
				String unNeedBarcode = unNeed.split("__")[1];

				SaleShelfModel queryShelfItemModel = new SaleShelfModel();
				queryShelfItemModel.setOrderId(querySaleModel.getId());
				queryShelfItemModel.setPoNo(unNeedPoNo);
				queryShelfItemModel.setBarcode(unNeedBarcode);
				List<SaleShelfModel> shelfItemList = saleShelfDao.list(queryShelfItemModel);

				if(shelfItemList != null && shelfItemList.size() > 0) {
					List<Long> list = shelfItemList.stream().map(SaleShelfModel::getId).collect(Collectors.toList());
					unNeedIdList.addAll(list);
				}
			}
			unNeedIds = StringUtils.join(unNeedIdList,",");
		}

		List<SaleReturnOrderItemDTO> itemList = new ArrayList<SaleReturnOrderItemDTO>();

		SaleReturnOrderItemDTO queryReturnItemDTO = new SaleReturnOrderItemDTO();
		queryReturnItemDTO.setSaleOrderId(querySaleModel.getId().toString());
		queryReturnItemDTO.setUnNeedIds(unNeedIds);
		queryReturnItemDTO.setInGoodsName(goodsName);
		queryReturnItemDTO.setInGoodsNo(goodsNo);
		queryReturnItemDTO.setBarcode(barcode);
		queryReturnItemDTO.setPoNo(poNo);
		queryReturnItemDTO = saleReturnOrderItemDao.getSalePopupListByPage(queryReturnItemDTO);
		List<SaleReturnOrderItemDTO> queryReturnItemList = queryReturnItemDTO.getList();
		for(SaleReturnOrderItemDTO saleItem : queryReturnItemList) {
			SaleReturnOrderItemDTO saleReturnOrderItemDTO = new SaleReturnOrderItemDTO();
			saleReturnOrderItemDTO.setSaleOrderId(querySaleModel.getId().toString());
			saleReturnOrderItemDTO.setSaleOrderCode(querySaleModel.getCode());
			saleReturnOrderItemDTO.setPoNo(saleItem.getPoNo());
			saleReturnOrderItemDTO.setMerchantId(querySaleModel.getMerchantId());
			saleReturnOrderItemDTO.setMerchantName(querySaleModel.getMerchantName());
			saleReturnOrderItemDTO.setReturnNum(saleItem.getReturnNum());
			saleReturnOrderItemDTO.setAmount(saleItem.getAmount());
			saleReturnOrderItemDTO.setTaxAmount(saleItem.getTaxAmount());
			saleReturnOrderItemDTO.setCommbarcode(saleItem.getCommbarcode());
			saleReturnOrderItemDTO.setBarcode(saleItem.getBarcode());

			BigDecimal price = BigDecimal.ZERO;
			BigDecimal taxPrice = BigDecimal.ZERO;
			if(saleItem.getReturnNum() != null && saleItem.getReturnNum().intValue() > 0){
				price = saleItem.getAmount().divide(new BigDecimal(saleItem.getReturnNum()), 8 , BigDecimal.ROUND_HALF_UP);
				taxPrice = saleItem.getTaxAmount().divide(new BigDecimal(saleItem.getReturnNum()), 8 , BigDecimal.ROUND_HALF_UP);
			}
			Double taxRate = 0.0;
			BigDecimal tax = BigDecimal.ZERO;
			if(saleItem.getAmount() != null && saleItem.getAmount().compareTo(BigDecimal.ZERO) > 0){
				tax = saleItem.getTaxAmount().subtract(saleItem.getAmount());
				taxRate = saleItem.getTaxAmount().divide(saleItem.getAmount(),2,BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			saleReturnOrderItemDTO.setPrice(price);
			saleReturnOrderItemDTO.setTax(tax);
			saleReturnOrderItemDTO.setTaxRate(taxRate);
			saleReturnOrderItemDTO.setTaxPrice(taxPrice);

			//查询货品
			Double grossWeight = 0.0;//毛重
			Double netWeight = 0.0;//净重
			//获取商品信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("barcode", saleItem.getBarcode());
			params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);	// 状态(1使用中,0已禁用)
			params.put("merchantId", querySaleModel.getMerchantId());
			List<MerchandiseInfoMogo> inGoodsList = merchandiseInfoMogoDao.findMerchandiseByDepotId(params, inDepotId);
			if(inGoodsList != null && inGoodsList.size() > 0){
				MerchandiseInfoMogo inGoods = inGoodsList.get(0);
				saleReturnOrderItemDTO.setInGoodsCode(inGoods.getCode());
				grossWeight = inGoods.getGrossWeight() == null ? 0.0 :inGoods.getGrossWeight();
				netWeight = inGoods.getNetWeight()== null ? 0.0 :inGoods.getNetWeight();

				saleReturnOrderItemDTO.setGrossWeight(grossWeight);
				saleReturnOrderItemDTO.setNetWeight(netWeight);
				saleReturnOrderItemDTO.setInGoodsId(inGoods.getMerchandiseId());
				saleReturnOrderItemDTO.setInGoodsCode(inGoods.getGoodsCode());
				saleReturnOrderItemDTO.setInGoodsNo(inGoods.getGoodsNo());
				saleReturnOrderItemDTO.setInGoodsName(inGoods.getName());

				if(inGoods.getBrandId() != null ) {
					Map<String, Object> brandParams = new HashMap<String, Object>();
					brandParams.put("brandId", inGoods.getBrandId());
					BrandMongo brandMongo = brandMongoDao.findOne(brandParams);
					if(brandMongo != null) {
						saleReturnOrderItemDTO.setBrandName(brandMongo.getName());
					}
				}
				if (inGoods.getUnit() != null) {
					// 根据单位id获取单位信息
					Map<String, Object> unitParams = new HashMap<String, Object>();
					unitParams.put("unitId", inGoods.getUnit());
					UnitMongo unit = unitMongoDao.findOne(unitParams);
					if (unit != null) {
						saleReturnOrderItemDTO.setUnitName(unit.getName());// 单位
					}
				}
				//平台关区
				Map<String, Object> merchandiseCustomsRelParams = new HashMap<String, Object>();
				merchandiseCustomsRelParams.put("goodsId", saleItem.getInGoodsId());
				List<MerchandiseCustomsRelMongo> merchandiseCustomsRelList = merchandiseCustomsRelMongoDao.findAll(merchandiseCustomsRelParams);
				if(merchandiseCustomsRelList != null && merchandiseCustomsRelList.size() > 0) {
					List<String>  customsRelNames = merchandiseCustomsRelList.stream().map(MerchandiseCustomsRelMongo::getCustomsAreaName).collect(Collectors.toList());
					saleReturnOrderItemDTO.setCustomsArea(StringUtils.join(customsRelNames,","));
				}
			}else{
				saleReturnOrderItemDTO.setInGoodsName(saleItem.getInGoodsName());
			}

			// 退出仓(若选了多条销售订单，随机取一个销售订单的入仓仓库)为空，则默认退出商品为退入商品
			if(querySaleModel.getInDepotId() == null){
				saleReturnOrderItemDTO.setOutGoodsId(saleItem.getInGoodsId());
				saleReturnOrderItemDTO.setOutGoodsCode(saleItem.getInGoodsCode());
				saleReturnOrderItemDTO.setOutGoodsName(saleItem.getInGoodsName());
				saleReturnOrderItemDTO.setOutGoodsNo(saleItem.getInGoodsNo());
			}else{
				/**
				 * 根据上架单的条形码+退出仓库 查询关联的商品货号，
				 */
				Map<String, Object> paramMap = new HashMap<String, Object>();
				List<MerchandiseInfoMogo> inMerchandiseList = new ArrayList<>();
				paramMap.put("barcode", saleReturnOrderItemDTO.getBarcode());
				paramMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);	// 状态(1使用中,0已禁用)
				paramMap.put("merchantId", querySaleModel.getMerchantId());
				List<MerchandiseInfoMogo> merchandiseInfoMogoList = merchandiseInfoMogoDao.findMerchandiseByDepotId(paramMap,querySaleModel.getInDepotId());
				if(merchandiseInfoMogoList != null && merchandiseInfoMogoList.size() > 0 ){
//					LOGGER.info("根据退入商品条形码:"+saleReturnOrderItemDTO.getBarcode()+",入库仓库:"+querySaleModel.getInDepotName()+"未找到关联商品");
//					throw new RuntimeException("根据退入商品条形码:"+saleReturnOrderItemDTO.getBarcode()+",入库仓库:"+querySaleModel.getInDepotName()+"未找到关联商品");
					MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoList.get(0);
					saleReturnOrderItemDTO.setOutGoodsId(merchandiseInfoMogo.getMerchandiseId());
					saleReturnOrderItemDTO.setOutGoodsCode(merchandiseInfoMogo.getGoodsCode());
					saleReturnOrderItemDTO.setOutGoodsName(merchandiseInfoMogo.getName());
					saleReturnOrderItemDTO.setOutGoodsNo(merchandiseInfoMogo.getGoodsNo());
				}

			}

			itemList.add(saleReturnOrderItemDTO);
		}
		return itemList;
	}
}
