package com.topideal.inventory.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryFreeUnfreeOrderDao;
import com.topideal.dao.InventoryFreezeDetailsDao;
import com.topideal.dao.MonthlyAccountDao;
import com.topideal.entity.dto.MonthlyAccountDTO;
import com.topideal.entity.vo.MonthlyAccountItemModel;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.inventory.service.MonthlyAccountService;
import com.topideal.json.storage.j01.AdjustmentInventoryGoodsListJson;
import com.topideal.json.storage.j01.AdjustmentInventoryRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;

import net.sf.json.JSONObject;

/**
 * 库存管理-月库存转结-service实现类
 */
@Service
public class MonthlyAccountServiceImpl implements MonthlyAccountService {

	//月库存转结dao
    @Autowired
    private MonthlyAccountDao monthlyAccountDao;
	@Autowired
	private InventoryFreezeDetailsDao inventoryFreezeDetailsDao;	
	@Autowired
	private InventoryFreeUnfreeOrderDao  inventoryFreeUnfreeOrderDao;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
    @Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;//财务经销存关账mongdb
	
	/**
	 * 月库存转结列表（分页）
	 * @param model 
	 * @return
	 */
    @Override
    public MonthlyAccountModel listMonthlyAccount(MonthlyAccountModel model)throws SQLException {
        return monthlyAccountDao.searchByPage(model);
    }

    /**
	 * 月库存转结列表（分页）DTO
	 * @param model 
	 * @return
	 */
    @Override
    public MonthlyAccountDTO listDTOMonthlyAccount(MonthlyAccountDTO model)throws Exception {
        return monthlyAccountDao.searchDTOByPage(model);
    }
    /**
	 * 根据主键id 查询月库存转结详情
	 * @param model 
	 * @return
	 */
	@Override
	public MonthlyAccountModel searchById(Long id) throws SQLException {
		// TODO Auto-generated method stub
		return monthlyAccountDao.searchById(id);
	}
	
	/**
	 * 根据主键id 查询月库存转结详情
	 * @param model 
	 * @return
	 */
	@Override
	public MonthlyAccountDTO searchDTOById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return monthlyAccountDao.searchDTOById(id);
	}

	/**
	 * 更新月结账单状态和月结时间
	 */
	@Override
	public boolean updateMonthlyAccountState(MonthlyAccountModel monthlyAccountModel,Long userId,String name) throws Exception {
		// TODO Auto-generated method stub
		monthlyAccountModel.setState("2");
		monthlyAccountModel.setSettlementDate(TimeUtils.getNow());
		monthlyAccountModel.setCreater(userId);
		monthlyAccountModel.setPlanName(name);
		int num=monthlyAccountDao.modify(monthlyAccountModel);
		if(num>0){
			return true;
		}else{
			return false;
		}
	
	}
	
	
	/**
	 *  用于库存新增扣减接口 根据归属月份查询未结转的账单
	 * @param monthlyAccountModel
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<MonthlyAccountModel> getMonthlyAccountListByMonth(MonthlyAccountModel monthlyAccountModel)
			throws Exception {
		// TODO Auto-generated method stub
		return monthlyAccountDao.getMonthlyAccountListByMonth(monthlyAccountModel);
	}
	
	
	/**
	 *  生成库存调整单
	 * @param monthlyAccountModel
	 * @param monthlyAccountItemModelList
	 * @return
	 * @throws Exception
	 */
	@Override
	public String producedAdjustmentInventory(Long userId,String name,MonthlyAccountModel monthlyAccountModel,
			List<Map<String,Object>> mapList) throws Exception {
		boolean isNull = true;
		JSONObject object=null;
		try {
			List<AdjustmentInventoryGoodsListJson> adjustmentInventoryItemVoList=new ArrayList<AdjustmentInventoryGoodsListJson>();
			AdjustmentInventoryRootJson  adInventoryVo=new  AdjustmentInventoryRootJson();
			adInventoryVo.setMerchantId(String.valueOf(monthlyAccountModel.getMerchantId()));// 商家ID
			adInventoryVo.setMerchantName(monthlyAccountModel.getMerchantName());// 商家名称
			adInventoryVo.setDepotId(String.valueOf(monthlyAccountModel.getDepotId()));// 仓库ID
			adInventoryVo.setDepotName(monthlyAccountModel.getDepotName());// 仓库名称
			adInventoryVo.setTopidealCode(monthlyAccountModel.getTopidealCode());
			adInventoryVo.setModel("2");// 业务类别 月结损益
			adInventoryVo.setSourceCode(monthlyAccountModel.getOrderNo());// 来源单据号
			adInventoryVo.setAdjustmentTime(TimeUtils.formatFullTime(new Date()));// 调整时间
			adInventoryVo.setSourceTime( TimeUtils.format(monthlyAccountModel.getEndDate(), "yyyy-MM-dd HH:mm:ss"));
			adInventoryVo.setMonths(monthlyAccountModel.getSettlementMonth());// 月份
			if(mapList != null && mapList.size() > 0){
				for (Map<String,Object> map : mapList) {
					String type = map.get("type").toString();//0：调减  1：调增
					Integer num = Integer.valueOf(map.get("num").toString());//需要调增/调减的数量
					MonthlyAccountItemModel monAccItemModel = (MonthlyAccountItemModel) map.get("model");
					AdjustmentInventoryGoodsListJson adInventoryItemVo=new AdjustmentInventoryGoodsListJson();
					adInventoryItemVo.setGoodsId(String.valueOf(monAccItemModel.getGoodsId()));// 商品ID
					adInventoryItemVo.setGoodsName(monAccItemModel.getGoodsName());// 商品名称
					adInventoryItemVo.setGoodsNo(monAccItemModel.getGoodsNo());// 商品货号
					adInventoryItemVo.setGoodsCode(monAccItemModel.getGoodsCode());// 商品编码
					adInventoryItemVo.setBatchNo(monAccItemModel.getBatchNo());// 原始批次号
					adInventoryItemVo.setBarcode(monAccItemModel.getBarcode());// 商品条形码
					adInventoryItemVo.setStockType(monAccItemModel.getType());// 商品分类
					if(StringUtils.isNotBlank(monAccItemModel.getUnit())){
						adInventoryItemVo.setUnit(monAccItemModel.getUnit());//库存单位
					}
					if(monAccItemModel.getProductionDate()!=null){
						adInventoryItemVo.setProductionDate(monAccItemModel.getProductionDate().toString());;// 生产日期
					}
					if(monAccItemModel.getOverdueDate()!=null){
						adInventoryItemVo.setOverdueDate(monAccItemModel.getOverdueDate().toString());// 失效日期
					}
					adInventoryItemVo.setType(type);
					adInventoryItemVo.setAdjustTotal(num);// 数量
					adjustmentInventoryItemVoList.add(adInventoryItemVo);
				}
			}
			adInventoryVo.setGoodsList(adjustmentInventoryItemVoList);
			//将对象转成json 格式
			object=new JSONObject().fromObject(adInventoryVo);
			isNull = new EmptyCheckUtils().addObject(object).empty();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(isNull){
			return "";
		}else{
			return object.toString(); 
		}
	}
	
	@Override
	public List<Map<String, Object>> exportMonthlyAccountMap(MonthlyAccountModel monthlyAccountModel) throws Exception {
		return monthlyAccountDao.exportMonthlyAccountMap(monthlyAccountModel);
	}

	@Override
	public List<MonthlyAccountModel> listByMonthlyAccount(MonthlyAccountModel monthlyAccountModel) throws SQLException {
		return monthlyAccountDao.listOrderBySettlementMonth(monthlyAccountModel);
	}

	/**
	 * 新增月结表头
	 */
	@Override
	public Map<String, Object> saveMonthlyAccount(User user, Long depotId, String month) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("code", "00");
		map.put("message", "添加成功");		
				
		Map<String, Object> depotInfoParam=new HashMap<String, Object>();
		depotInfoParam.put("depotId", depotId);
		DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoParam);
		if (depotInfoMongo==null) {
			map.put("code", "01");
			map.put("message", "没有查到对应的仓库信息");
			return map;
		}
		MonthlyAccountModel monthlyAccountQuery= new MonthlyAccountModel();
		monthlyAccountQuery.setMerchantId(user.getMerchantId());
		monthlyAccountQuery.setDepotId(depotId);
		monthlyAccountQuery.setSettlementMonth(month);
		monthlyAccountQuery = monthlyAccountDao.searchByModel(monthlyAccountQuery);
		if (monthlyAccountQuery!=null) {
			map.put("code", "01");
			map.put("message", "当前月份,当前仓库月结数据已经存在");
			return map;
		}
		// 新增月结
		MonthlyAccountModel monActModel = new MonthlyAccountModel();
		monActModel.setMerchantId(user.getMerchantId());
		monActModel.setMerchantName(user.getMerchantName());
		monActModel.setDepotId(depotInfoMongo.getDepotId());
		monActModel.setDepotName(depotInfoMongo.getName());
		monActModel.setSettlementMonth(month);
		Timestamp firstDate = Timestamp.valueOf(month+"-01 00:00:00");
		monActModel.setFirstDate(firstDate);
		monActModel.setOrderNo(SmurfsUtils.getID("YJSY"));
		monActModel.setTopidealCode(user.getTopidealCode());
		Timestamp endDate = TimeUtils.getMonthEndDate(firstDate);
		monActModel.setEndDate(endDate);
		monActModel.setState(DERP_INVENTORY.MONTHLYACCOUNT_STATE_1);// 未结转
		monActModel.setCreateDate(TimeUtils.getNow());
		//以后都是按库存量结转 没有按现存量结转了
		monActModel.setDepotType("2");

		//保存月结表头数据
		Long id = monthlyAccountDao.save(monActModel);
		return map;
	}

	@Override
	public int countByMonthlyAccount(MonthlyAccountModel monthlyAccountModel) throws SQLException {
		return monthlyAccountDao.countByMonthlyAccount(monthlyAccountModel);
	}

	// 修改成未月结状态
	@Override
	public Map<String, Object> updateNotSettlement(Long id) throws SQLException {
	     Map<String,Object> retrunMap=new HashMap<String,Object>();
	     retrunMap.put("status", "00");
	     retrunMap.put("errorMessage", "修改成功");
	     if (id==null) {
	    	 retrunMap.put("errorMessage", "月结账单id为空");
    		 retrunMap.put("status", "01");
    		 return retrunMap;
		 }
		MonthlyAccountModel monthlyAccountModel = monthlyAccountDao.searchById(id);
		if (monthlyAccountModel==null) {
			retrunMap.put("errorMessage", "月结账单信息为空");
   		 	retrunMap.put("status", "01");
   		 	return retrunMap;
		}
		MonthlyAccountModel monthlyAccountQuery=new MonthlyAccountModel();
		monthlyAccountQuery.setMerchantId(monthlyAccountModel.getMerchantId());
		monthlyAccountQuery.setDepotId(monthlyAccountModel.getDepotId());
		monthlyAccountQuery.setSettlementMonth(monthlyAccountModel.getSettlementMonth());
		int num = monthlyAccountDao.getAfterMonthlyAccountCount(monthlyAccountQuery);
		if (num>0) {
			retrunMap.put("errorMessage", "存在大于本月的月结转账单数据");
   		 	retrunMap.put("status", "01");
   		 	return retrunMap;
		}
		
		MonthlyAccountModel monthly=new MonthlyAccountModel();
		monthly.setState("1");
		monthly.setId(id);
		monthly.setModifyDate(TimeUtils.getNow());
		
		int modify = monthlyAccountDao.modify(monthly);	
		
		Map<String, Object> params=new HashMap<>();
		params.put("merchantId", monthlyAccountModel.getMerchantId());
		params.put("depotId", monthlyAccountModel.getDepotId());
		params.put("source", DERP.CLOSE_ACCOUNTS_SOURCE_2);
		FinanceCloseAccountsMongo closeAccounts = financeCloseAccountsMongoDao.findOne(params); 
		String accountstMonth=monthlyAccountModel.getSettlementMonth();
		//获取当前月份前一个月的 月份
		String lastMonth = TimeUtils.getLastMonth(Timestamp.valueOf(accountstMonth+"-01 00:00:00"));
		
		if (closeAccounts!=null) {
			//删除之前数据
			financeCloseAccountsMongoDao.remove(params);
		}		
		// 向已经财务月结和关账mongdb中 插入已经月结的数据  
		FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
		closeAccountsMongo.setMerchantId(monthlyAccountModel.getMerchantId());
		closeAccountsMongo.setMerchantName(monthlyAccountModel.getMerchantName());
		closeAccountsMongo.setDepotId(monthlyAccountModel.getDepotId());
		closeAccountsMongo.setDepotName(monthlyAccountModel.getDepotName());				
		closeAccountsMongo.setMonth(lastMonth);			
		closeAccountsMongo.setStatus(monthlyAccountModel.getState());
		String createDateStr = TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd HH:mm:ss");
		closeAccountsMongo.setCreateDateStr(createDateStr);
		closeAccountsMongo.setSource(DERP.CLOSE_ACCOUNTS_SOURCE_2);//1.财务经销存关账 2.已经月结				
		JSONObject jsonObject = JSONObject.fromObject(closeAccountsMongo);
		//新增
		financeCloseAccountsMongoDao.insertJson(jsonObject.toString());
		return retrunMap;
	}
}
