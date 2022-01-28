package com.topideal.dao.inventory.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.inventory.BuInventoryDao;
import com.topideal.entity.dto.ManageDepartmentInventoryDTO;
import com.topideal.entity.vo.inventory.BuInventoryModel;
import com.topideal.mapper.inventory.BuInventoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuInventoryDaoImpl implements BuInventoryDao {

    @Autowired
    private BuInventoryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuInventoryModel> list(BuInventoryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuInventoryModel model) throws SQLException {
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
    public int modify(BuInventoryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuInventoryModel  searchByPage(BuInventoryModel  model) throws SQLException{
        PageDataModel<BuInventoryModel> pageModel=mapper.listByPage(model);
        return (BuInventoryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuInventoryModel  searchById(Long id)throws SQLException {
        BuInventoryModel  model=new BuInventoryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuInventoryModel searchByModel(BuInventoryModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 获取事业部库存
	 */
	@Override
	public List<Map<String, Object>> getBuDepotEndNumForMap(Map<String, Object> paramMap) {
		return mapper.getBuDepotEndNumForMap(paramMap);
	}
	
	/**
	 * 删除 该商家 仓库 事业部 月份 的事业部库存
	 */
	@Override
	public int delBuInventory(Map<String, Object> map)  {
		return mapper.delBuInventory(map);
	}

	
	
	/**
	 * 获取的上月 按商家 仓库事业库存分组的统计量
	 */
	@Override
	public List<Map<String, Object>> getBarcodeMonthBuInventory(Map<String, Object> map) throws SQLException {		
		return mapper.getBarcodeMonthBuInventory(map);
	}
	/**
	 * 查询 按商家 仓库事业库存 之前月份有没有数据
	 */
	@Override
	public int getBeforeMonthBarcodeCount(Map<String, Object> map) throws SQLException {	
		return mapper.getBeforeMonthBarcodeCount(map);
	}
	@Override
	public List<Map<String, Object>> getMonthlyAutoVeriListGroupByBarcode(Map<String, Object> params) {
		return mapper.getMonthlyAutoVeriListGroupByBarcode(params);
	}

	@Override
	public List<BuInventoryModel> getLatestInventory(BuInventoryModel buInventoryModel) throws SQLException {
		return mapper.getLatestInventory(buInventoryModel);
	}
	@Override
	public List<BuInventoryModel> getNegativeNumList(Map<String, Object> queryMap) {
		return mapper.getNegativeNumList(queryMap);
	}

	@Override
	public List<BuInventoryModel> getBuInventoryByCommbarcode(String month, List<Long> buIds) throws SQLException {
		return mapper.getBuInventoryByCommbarcode(month, buIds);
	}

	@Override
	public List<ManageDepartmentInventoryDTO> getDepartmentInventoryStatistic(Map<String, Object> paramMap) {
		return mapper.getDepartmentInventoryStatistic(paramMap);
	}
}