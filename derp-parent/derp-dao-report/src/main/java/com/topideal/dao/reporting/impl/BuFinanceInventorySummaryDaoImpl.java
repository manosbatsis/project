package com.topideal.dao.reporting.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.reporting.BuFinanceInventorySummaryDao;
import com.topideal.entity.dto.BuFinanceInventorySummaryDTO;
import com.topideal.entity.vo.reporting.BuFinanceInventorySummaryModel;
import com.topideal.mapper.reporting.BuFinanceInventorySummaryMapper;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinanceInventorySummaryDaoImpl implements BuFinanceInventorySummaryDao {

    @Autowired
    private BuFinanceInventorySummaryMapper mapper;
    @Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;//财务经销存关账mongdb
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceInventorySummaryModel> list(BuFinanceInventorySummaryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 查询商家、仓库、月份报表的状态
	 * */
	public String getSummaryStatus(Map<String, Object> map){
		return mapper.getSummaryStatus(map);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceInventorySummaryModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param ids
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(BuFinanceInventorySummaryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceInventorySummaryModel  searchByPage(BuFinanceInventorySummaryModel  model) throws SQLException{
        PageDataModel<BuFinanceInventorySummaryModel> pageModel=mapper.listByPage(model);
        return (BuFinanceInventorySummaryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceInventorySummaryModel  searchById(Long id)throws SQLException {
        BuFinanceInventorySummaryModel  model=new BuFinanceInventorySummaryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceInventorySummaryModel searchByModel(BuFinanceInventorySummaryModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 清空事业部商家、仓库、本月的报表数据
	 */
	@Override
	public int delBuDepotMonthReport(Map<String, Object> map) throws SQLException {
		return mapper.delBuDepotMonthReport(map);
	}
	/**
	 * 查询 事业部 商家、仓库、上月货号的期末结存数量、调整标准成本单价、调整期末结存金额
	 * */
	public Map<String, Object> getBuGoodsNoSummary(Map<String, Object> map){
		return mapper.getBuGoodsNoSummary(map);
	}
	
	/**
	 * 分页获取事业部 财务降序存
	 */
	@Override
	public BuFinanceInventorySummaryDTO getListByPage(BuFinanceInventorySummaryDTO model) {
		PageDataModel<BuFinanceInventorySummaryDTO> pageModel = mapper.getListByPage(model);
		return (BuFinanceInventorySummaryDTO) pageModel.getModel();
	}
	/**
	 * 事业部财务经销存导出
	 */
	@Override
	public List<Map<String, Object>> getBuListForMap(Map<String, Object> paramMap) {
		return mapper.getBuListForMap(paramMap);
	}
	/**
	 * 事业部 导出-查询本期所有仓库
	 */
	@Override
	public List<Map<String, Object>> getBuDepotListForMap(Map<String, Object> paramMap) {
		return mapper.getBuDepotListForMap(paramMap);
	}
	

	
	/**
	 * 事业部 导出-查询本期所有仓库商品库存
	 */
	@Override
	public List<Map<String, Object>> getBuDepotEndNumForMap(Map<String, Object> paramMap) {
		return mapper.getBuDepotEndNumForMap(paramMap);
	}
	/**
	 * 事业部 导出-查询本期所有仓库组码库存
	 */
	@Override
	public List<Map<String, Object>> getBuDepotEndNumForMapA(Map<String, Object> paramMap) {
		return mapper.getBuDepotEndNumForMapA(paramMap);
	}
	
	/**
	 * 根据组码分组查询财务经销存总表  事业部财务进销存标准条码汇总
	 */
	@Override
	public List<Map<String, Object>> getBuListForGroupCommbarcodeMap(Map<String, Object> paramMap) {
		return mapper.getBuListForGroupCommbarcodeMap(paramMap);
	}

	@Override
	public List<Map<String, Object>> listCommbarcodeMonth(Map<String, Object> params) {
		return mapper.listCommbarcodeMonth(params);
	}

	/**
	 * 关帐页面分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	public BuFinanceInventorySummaryDTO getListDescByPage(BuFinanceInventorySummaryDTO model) throws SQLException {
		PageDataModel<BuFinanceInventorySummaryDTO> pageModel = mapper.getListDescByPage(model);
		return (BuFinanceInventorySummaryDTO) pageModel.getModel();
	}
	
	@Override
	public int closeReport(BuFinanceInventorySummaryModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		int num = mapper.updateClose(model);
		if (num>0) {
			//移除该商家改月份下的 状态关账的数据
			Map<String, Object> params= new HashMap<>();
			params.put("merchantId",  model.getMerchantId());
			params.put("buId",  model.getBuId());
			params.put("source", DERP.CLOSE_ACCOUNTS_SOURCE_1);
			FinanceCloseAccountsMongo closeAccounts = financeCloseAccountsMongoDao.findOne(params); 
			String accountstMonth=model.getMonth();//关账月份
			if (closeAccounts!=null) {
				String month = closeAccounts.getMonth();
				if (Timestamp.valueOf(month+"-01 00:00:00").getTime()>Timestamp.valueOf(accountstMonth+"-01 00:00:00").getTime()) {
					accountstMonth=month;
				}
				//删除之前数据
				financeCloseAccountsMongoDao.remove(params);
			}
			
			// 新增财务经销存关账
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setBuId(model.getBuId());
			closeAccountsMongo.setBuName(model.getBuName());
			closeAccountsMongo.setMerchantId(model.getMerchantId());
			closeAccountsMongo.setMerchantName(model.getMerchantName());
			closeAccountsMongo.setMonth(accountstMonth);			
			closeAccountsMongo.setStatus(model.getStatus());
			String createDateStr = TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd HH:mm:ss");
			closeAccountsMongo.setCreateDateStr(createDateStr);
			closeAccountsMongo.setSource(DERP.CLOSE_ACCOUNTS_SOURCE_1);//1.财务经销存关账 2.已经月结				
			JSONObject jsonObject = JSONObject.fromObject(closeAccountsMongo);
			financeCloseAccountsMongoDao.insertJson(jsonObject.toString());
		}
			
        return num;
	}

	@Override
	public int updateNotClose(BuFinanceInventorySummaryModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		int num = mapper.updateNotClose(model);
		if (num>0) {
			//移除该商家改月份下的 状态关账的数据
			Map<String, Object> params= new HashMap<>();
			params.put("merchantId",  model.getMerchantId());
			params.put("buId",  model.getBuId());
			params.put("source", DERP.CLOSE_ACCOUNTS_SOURCE_1);
			FinanceCloseAccountsMongo closeAccounts = financeCloseAccountsMongoDao.findOne(params); 
			String accountstMonth=model.getMonth();//关账月份
			if (closeAccounts!=null) {
				String month = closeAccounts.getMonth();
				if (Timestamp.valueOf(month+"-01 00:00:00").getTime()>Timestamp.valueOf(accountstMonth+"-01 00:00:00").getTime()) {
					accountstMonth=month;
				}
				//删除之前数据
				financeCloseAccountsMongoDao.remove(params);
			}
			String lastMonth = TimeUtils.getLastMonth(Timestamp.valueOf(accountstMonth+"-01 00:00:00"));
			// 获取上月财务经销存关账
			FinanceCloseAccountsMongo closeAccountsMongo=new FinanceCloseAccountsMongo();
			closeAccountsMongo.setBuId(model.getBuId());
			closeAccountsMongo.setBuName(model.getBuName());
			closeAccountsMongo.setMerchantId(model.getMerchantId());
			closeAccountsMongo.setMerchantName(model.getMerchantName());
			closeAccountsMongo.setMonth(lastMonth);			
			closeAccountsMongo.setStatus(DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_030);
			String createDateStr = TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd HH:mm:ss");
			closeAccountsMongo.setCreateDateStr(createDateStr);
			closeAccountsMongo.setSource(DERP.CLOSE_ACCOUNTS_SOURCE_1);//1.财务经销存关账 2.已经月结				
			JSONObject jsonObject = JSONObject.fromObject(closeAccountsMongo);
			financeCloseAccountsMongoDao.insertJson(jsonObject.toString());
		}
			
        return num;
	}
	
	
	

	@Override
	public Integer countBeforeMonthList(Map<String,Object> paramMap) throws SQLException {
		return mapper.countBeforeMonthList(paramMap);
	}
	
	@Override
	public Integer countAftrerMonthList(Map<String,Object> paramMap) throws SQLException {
		return mapper.countAftrerMonthList(paramMap);
	}
	/**
	 * 获取事业部财务经销存和标准条码
	 */
	@Override
	public List<BuFinanceInventorySummaryModel> getSummaryList(BuFinanceInventorySummaryModel model) {	
		return mapper.getSummaryList(model);
	}
	
	/**
	 * 累计汇总表分页
	 */
	@Override
	public BuFinanceInventorySummaryDTO getListAddByPage(BuFinanceInventorySummaryDTO model) throws SQLException {
		List<BuFinanceInventorySummaryDTO>addList=mapper.getListAdd(model);
		int total = mapper.getListAddCount(model);
		BuFinanceInventorySummaryDTO  maxModel=mapper.getMaxCreatDate(model);
		if (maxModel!=null) {
			model.setCreateDate(maxModel.getCreateDate());
		}
		model.setList(addList);
		model.setTotal(total);
        return model;
	        
	        
	}
	/**
	 * 累计汇总导出
	 */
	@Override
	public List<BuFinanceInventorySummaryDTO> getListAddExport(BuFinanceInventorySummaryDTO model) throws SQLException {
		return mapper.getListAddExport(model);
	}
	@Override
	public List<Map<String, Object>> getFgInventByGroupCommbar(Map<String, Object> map) {
		return mapper.getFgInventByGroupCommbar(map);
	}
	
	/**
	 * 统计汇总事业部财务经销存总表
	 */
	@Override
	public List<Map<String, Object>> getMaxCloseAccountMerchantBu()throws SQLException {
		return mapper.getMaxCloseAccountMerchantBu();
	}

	@Override
	public List<Map<String, Object>> getAmountYear(BuFinanceInventorySummaryDTO dto, String isParentBrand,List<Long> brandIds) {
		return mapper.getAmountYear(dto,isParentBrand,brandIds);
	}

	@Override
	public List<Map<String, Object>> getBrandPurchaseAmountYear(BuFinanceInventorySummaryDTO dto, String isParentBrand,List<Long> brandIds) {
		return mapper.getBrandPurchaseAmountYear(dto,isParentBrand,brandIds);
	}
	@Override
	public List<Map<String, Object>> getBrandList(BuFinanceInventorySummaryDTO dto, String isParentBrand) {
		return mapper.getBrandList(dto,isParentBrand);
	}
	@Override
	public List<BuFinanceInventorySummaryDTO> getYearFinanceInventoryAnalysisExportList(BuFinanceInventorySummaryDTO dto, String isParentBrand,List<Long> brandIds) {
		return mapper.getYearFinanceInventoryAnalysisExportList(dto,isParentBrand,brandIds);
	}
	/**
	 * 查询商家、仓库、事业部、上月货号的期末结存数量、调整标准成本单价、调整期末结存金额
	 * */
	@Override
	public Map<String, Object> getGoodsNoSummary(Map<String, Object> map){
		return mapper.getGoodsNoSummary(map);
	}

}