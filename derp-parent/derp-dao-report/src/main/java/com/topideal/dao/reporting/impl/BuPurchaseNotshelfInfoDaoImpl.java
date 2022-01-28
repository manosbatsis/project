package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.topideal.common.constant.DERP;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuPurchaseNotshelfInfoDao;
import com.topideal.entity.vo.reporting.BuPurchaseNotshelfInfoModel;
import com.topideal.mapper.reporting.BuPurchaseNotshelfInfoMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuPurchaseNotshelfInfoDaoImpl implements BuPurchaseNotshelfInfoDao {

    @Autowired
    private BuPurchaseNotshelfInfoMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuPurchaseNotshelfInfoModel> list(BuPurchaseNotshelfInfoModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuPurchaseNotshelfInfoModel model) throws SQLException {
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
    public int modify(BuPurchaseNotshelfInfoModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuPurchaseNotshelfInfoModel  searchByPage(BuPurchaseNotshelfInfoModel  model) throws SQLException{
        PageDataModel<BuPurchaseNotshelfInfoModel> pageModel=mapper.listByPage(model);
        return (BuPurchaseNotshelfInfoModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuPurchaseNotshelfInfoModel  searchById(Long id)throws SQLException {
        BuPurchaseNotshelfInfoModel  model=new BuPurchaseNotshelfInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuPurchaseNotshelfInfoModel searchByModel(BuPurchaseNotshelfInfoModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public int delBuDepotMonthReport(Map<String, Object> map) {
		return mapper.delBuDepotMonthReport(map);
	}
	
	/**
	 * 批量插入
	 */
	@Override
	public int insertBuBatch(List<BuPurchaseNotshelfInfoModel> list) {
		return mapper.insertBuBatch(list);
	}
	
	/**
	 * 采购未上架导出
	 */
	@Override
	public List<Map<String, Object>> listBuForMap(Map<String, Object> map) {
		List<Map<String, Object>> list = mapper.listBuForMap(map);
 		Map<String, String> unitMap = new HashMap<String, String>();
 		unitMap.put(DERP.INVENTORY_UNIT_0, "托盘");
 		unitMap.put(DERP.INVENTORY_UNIT_1, "箱");
 		unitMap.put(DERP.INVENTORY_UNIT_2, "件");
 		Map<String, String> statusMap = new HashMap<String, String>();
 		statusMap.put("001", "待审核");
 		statusMap.put("003", "已审核");
 		statusMap.put("007", "已完结");
 		statusMap.put("028", "入库中"); 
 		statusMap.put("005", "部分入库");
 		 if(list!=null&&list.size()>0){
 			  for(Map<String, Object> value:list){
 				  String unit = (String)value.get("unit");
 				  if(StringUtils.isEmpty(unit)){
 					  value.put("unit","");
 				  }else{
 					 value.put("unit",unitMap.get(unit));
 				  }
 				  //状态
 				  String status = (String)value.get("status");
 				  value.put("status",statusMap.get(status));
 			  }
 		  }
 		return list;

	}

	@Override
	public List<Map<String, Object>> getPurchaseAddForMap(Map<String, Object> map) {
		List<Map<String, Object>> list = mapper.getPurchaseAddForMap(map);
 		Map<String, String> unitMap = new HashMap<String, String>();
 		unitMap.put(DERP.INVENTORY_UNIT_0, "托盘");
 		unitMap.put(DERP.INVENTORY_UNIT_1, "箱");
 		unitMap.put(DERP.INVENTORY_UNIT_2, "件");
 		Map<String, String> statusMap = new HashMap<String, String>();
 		statusMap.put("001", "待审核");
 		statusMap.put("003", "已审核");
 		statusMap.put("007", "已完结");
 		statusMap.put("028", "入库中"); 
 		statusMap.put("005", "部分入库");
 		 if(list!=null&&list.size()>0){
 			  for(Map<String, Object> value:list){
 				  String unit = (String)value.get("unit");
 				  if(StringUtils.isEmpty(unit)){
 					  value.put("unit","");
 				  }else{
 					 value.put("unit",unitMap.get(unit));
 				  }
 				  //状态
 				  String status = (String)value.get("status");
 				  value.put("status",statusMap.get(status));
 			  }
 		  }
 		return list;

	}
	
	
	
}