package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuInventorySummaryItemDao;
import com.topideal.entity.dto.BuInventorySummaryItemDTO;
import com.topideal.entity.vo.reporting.BuInventorySummaryItemModel;
import com.topideal.mapper.reporting.BuInventorySummaryItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuInventorySummaryItemDaoImpl implements BuInventorySummaryItemDao {

    @Autowired
    private BuInventorySummaryItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuInventorySummaryItemModel> list(BuInventorySummaryItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuInventorySummaryItemModel model) throws SQLException {
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
    public int modify(BuInventorySummaryItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuInventorySummaryItemModel  searchByPage(BuInventorySummaryItemModel  model) throws SQLException{
        PageDataModel<BuInventorySummaryItemModel> pageModel=mapper.listByPage(model);
        return (BuInventorySummaryItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuInventorySummaryItemModel  searchById(Long id)throws SQLException {
        BuInventorySummaryItemModel  model=new BuInventorySummaryItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuInventorySummaryItemModel searchByModel(BuInventorySummaryItemModel model) throws SQLException {
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
	 * 批量新增 事业部 业务经销存表体
	 */
	@Override
	public int insertBuBatch(List<BuInventorySummaryItemModel> list) {
		return mapper.insertBuBatch(list);
	}
	@Override
	public void updateBuItemNoBySale(BuInventorySummaryItemModel model) {
		mapper.updateBuItemNoBySale(model);
		
	}
	@Override
	public void updateBuItemNoBySaleReturn(BuInventorySummaryItemModel model) {
		mapper.updateBuItemNoBySaleReturn(model);
		
	}
	@Override
	public void updateBuItemNoByTransferOutDepot(BuInventorySummaryItemModel model) {
		mapper.updateBuItemNoByTransferOutDepot(model);
		
	}
	@Override
	public void updateBuItemNoByTransferInDepot(BuInventorySummaryItemModel model) {
		mapper.updateBuItemNoByTransferInDepot(model);
		
	}
	@Override
	public void updateBuItemNoByOrder(BuInventorySummaryItemModel model) {
		mapper.updateBuItemNoByOrder(model);
		
	}
	@Override
	public void updateBuItemNoBySaleShelfIdepot(BuInventorySummaryItemModel model) {
		mapper.updateBuItemNoBySaleShelfIdepot(model);
		
	}

	/**
	 * 查询入库单ids
	 * @param model
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getBuWarehouseIds(BuInventorySummaryItemModel model) throws SQLException {
		return mapper.getBuWarehouseIds(model);
	}
	
	/**
	 * 获取事业部采购的po号，合同号，供应商名称
	 */
	@Override
	public List<Map<String, Object>> getBuPurchaseInfos(Long id) throws SQLException {
		return mapper.getBuPurchaseInfos(id);
	}
	//根据id修改po号，合同号，供应商名称
	@Override
	public void updateBuItemNoById(Map<String, Object> map) throws SQLException {
		mapper.updateBuItemNoById(map);	
	}
	// 修改母品牌
	@Override
	public void updateSuperiorParentBrand(BuInventorySummaryItemModel model) throws SQLException {
		mapper.updateSuperiorParentBrand(model);	
	}
	
	
	@Override
	public List<Map<String, Object>> listBuOutListForMap(Map<String, Object> map) {
		List<Map<String, Object>> tempList = mapper.listBuOutListForMap(map);
		if(tempList!=null&&tempList.size()>0){
			for(Map<String,Object> value:tempList){
				String unit = (String)value.get("unit");
				if(StringUtils.isEmpty(unit)){
					value.put("unit", "");
				}else if(unit.equals("0")){
					value.put("unit", "托盘");
				}else if(unit.equals("1")){
					value.put("unit", "箱");
				}else if(unit.equals("2")){
					value.put("unit", "件");
				}
			}
		}
		return tempList;
	}
	@Override
	public List<Map<String, Object>> listBuInListForMap(Map<String, Object> map) {
		List<Map<String, Object>> tempList = mapper.listBuInListForMap(map);
		if(tempList!=null&&tempList.size()>0){
			for(Map<String,Object> value:tempList){
				String unit = (String)value.get("unit");
				if(StringUtils.isEmpty(unit)){
					value.put("unit", "");
				}else if(unit.equals("0")){
					value.put("unit","托盘");
				}else if(unit.equals("1")){
					value.put("unit","箱");
				}else if(unit.equals("2")){
					value.put("unit","件");
				}
			}
		}
		return tempList;
	}
	@Override
	public List<Map<String, Object>> listBuProfitLossListForMap(Map<String, Object> map) {
		List<Map<String, Object>> list = mapper.listBuProfitLossListForMap(map);
		Map<String, String> operateTypeMap = new HashMap<String, String>();
		operateTypeMap.put(DERP_INVENTORY.INVENTORY_OPERATETYPE_0, "调减");
		operateTypeMap.put(DERP_INVENTORY.INVENTORY_OPERATETYPE_1, "调增");

		Map<String, String> isWornMap = new HashMap<String, String>();
		isWornMap.put(DERP_INVENTORY.INITINVENTORY_TYPE_0, "好品");
		isWornMap.put(DERP_INVENTORY.INITINVENTORY_TYPE_1, "坏品");

		if (list != null && list.size() > 0) {
			for (Map<String, Object> value : list) {
				//调整类型
				String operateType = (String)value.get("operate_type");
				value.put("operate_type",operateTypeMap.get(operateType));

				//是否坏品
				String isWorn = (String) value.get("is_worn");
				value.put("is_worn", isWornMap.get(isWorn));
			}
		}
		return list;
	}
	/**
	 * 统计入库明细数量
	 * @param map
	 * @return
	 */
	@Override
	public Integer listBuInListCount(Map<String, Object> map) {
		return mapper.listBuInListCount(map);
	}
	/**
	 * 统计出库明细数量
	 * @param map
	 * @return
	 */
	@Override
	public Integer listBuOutListCount(Map<String, Object> map) {
		return mapper.listBuOutListCount(map);
	}
	
	@Override
	public List<BuInventorySummaryItemDTO> getBuList(BuInventorySummaryItemDTO model) {
		return mapper.getBuList(model);
	}
	@Override
	public List<BuInventorySummaryItemModel> getWarehouseExportlist(BuInventorySummaryItemModel tempItem) {
		return mapper.getWarehouseExportlist(tempItem);
	}
	@Override
	public int insertBuOutInStorageDetail(Map<String, Object> outInMap) {		
		return mapper.insertBuOutInStorageDetail(outInMap);
	}
	
	
}