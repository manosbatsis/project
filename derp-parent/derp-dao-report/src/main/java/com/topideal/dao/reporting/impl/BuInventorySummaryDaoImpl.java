package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuInventorySummaryDao;
import com.topideal.entity.dto.BuInventorySummaryDTO;
import com.topideal.entity.vo.reporting.BuInventorySummaryModel;
import com.topideal.mapper.reporting.BuInventorySummaryMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuInventorySummaryDaoImpl implements BuInventorySummaryDao {

    @Autowired
    private BuInventorySummaryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuInventorySummaryModel> list(BuInventorySummaryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuInventorySummaryModel model) throws SQLException {
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
    public int modify(BuInventorySummaryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuInventorySummaryModel  searchByPage(BuInventorySummaryModel  model) throws SQLException{
        PageDataModel<BuInventorySummaryModel> pageModel=mapper.listByPage(model);
        return (BuInventorySummaryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuInventorySummaryModel  searchById(Long id)throws SQLException {
        BuInventorySummaryModel  model=new BuInventorySummaryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuInventorySummaryModel searchByModel(BuInventorySummaryModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 清空事业部商家、仓库、本月的报表数据
	 */
	@Override
	public int delBuDepotMonthReport(Map<String, Object> map) {
		return mapper.delBuDepotMonthReport(map);
	}
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public BuInventorySummaryDTO getListByPage(BuInventorySummaryDTO model) throws SQLException {
		PageDataModel<BuInventorySummaryDTO> pageModel = mapper.getListByPage(model);
		return (BuInventorySummaryDTO) pageModel.getModel();
	}
	
	
	public List<Map<String, Object>> listBuDepotMonth(Map<String, Object>map) {
		List<Map<String, Object>> summaryList = mapper.listBuDepotMonth(map);
		if(summaryList!=null&&summaryList.size()>0){
    		for(Map<String,Object> value:summaryList){
    			String unit =(String)value.get("unit");
    			if(StringUtils.isEmpty(unit)){
    				value.put("unit", "");
				}else if(unit.equals("0")){
					value.put("unit","托盘");
				}else if(unit.equals("1")){
					value.put("unit","箱");
				}else if(unit.equals("2")){
					value.put("unit","件");
				}
    			Integer outDamagedNum = (Integer)value.get("out_damaged_num");
    			if(outDamagedNum.intValue()>0) value.put("out_damaged_num", "-"+outDamagedNum.intValue());
    		}
    	}
		return summaryList;
	}

	@Override
	public List<String> commbarcodeListByMonth(Map<String, Object> params) {
		return mapper.commbarcodeListByMonth(params);
	}

	@Override
	public List<Map<String, Object>> listCommbarcodeMonth(Map<String, Object> params) {
		return mapper.listCommbarcodeMonth(params);
	}
	
	@Override
	public List<Map<String, Object>> getMonthlyAutoVeriListGroupByBarcode(Map<String, Object> params) {
		return mapper.getMonthlyAutoVeriListGroupByBarcode(params);
	}
	
	@Override
	public List<Map<String, Object>> getInWarehouseSumAccount(Map<String, Object> queryMap) {
		return mapper.getInWarehouseSumAccount(queryMap);
	}
	/**
	 * 查询上月是否有 事业部业务经销存
	 * @param model
	 * @return
	 */
	@Override
	public int getLastMonthSummaryCount(BuInventorySummaryModel model) {		
		return mapper.getLastMonthSummaryCount(model);
	}
	/**
	 * 查询之前月是否有业务经销存
	 * @param model
	 * @return
	 */
	@Override
	public int getBeforLastMonthSummaryCount(BuInventorySummaryModel model) {
		return mapper.getBeforLastMonthSummaryCount(model);
	}
	
	/**
	 * 在库天数库存统计导出
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getInWarehouseExport(Map<String, Object> map) {
		return mapper.getInWarehouseExport(map);
	}
}