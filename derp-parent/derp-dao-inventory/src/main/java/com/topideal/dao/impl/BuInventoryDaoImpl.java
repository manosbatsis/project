package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.BuInventoryDao;
import com.topideal.entity.dto.BuInventoryDTO;
import com.topideal.entity.vo.BuInventoryModel;
import com.topideal.mapper.BuInventoryMapper;
import com.topideal.mongo.dao.BuInventoryMongoDao;

import net.sf.json.JSONObject;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuInventoryDaoImpl implements BuInventoryDao {

    @Autowired
    private BuInventoryMapper mapper;
    @Autowired
	private BuInventoryMongoDao mongo;  //事业部库存
	
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
        	//存储到MONGODB
			String json= JSONObject.fromObject(model).toString();
			JSONObject jsonObject=JSONObject.fromObject(json);
			jsonObject.put("buInventoryId",model.getId());
			jsonObject.remove("id");
			mongo.insertJson(jsonObject.toString());
        	
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
	 * 获取的上月 按商家 仓库事业库存分组的统计量
	 */
	@Override
	public List<Map<String, Object>> getMonthBuInventory(Map<String, Object> map) throws SQLException {		
		return mapper.getMonthBuInventory(map);
	}
	
	/**
	 * 查询 按商家 仓库事业库存 之前月份有没有数据
	 */
	@Override
	public int getBeforeMonthBuInventory(Map<String, Object> map) throws SQLException {	
		return mapper.getBeforeMonthBuInventory(map);
	}
	
	/**
	 * 获取事业部库存分页
	 */
	@Override
	public BuInventoryDTO getListBuInventoryByPage(BuInventoryDTO model) throws SQLException {
		PageDataModel<BuInventoryDTO> pageModel = mapper.getListBuInventoryByPage(model);
		return (BuInventoryDTO) pageModel.getModel();
	}
	
	/**
	 * 导出 事业部库存
	 */
	@Override
	public List<Map<String, Object>>  exportBuInventory(BuInventoryDTO model) throws SQLException {
		return mapper.exportBuInventory(model);
	}
	
	
	/**
	 * 获取事业部库分组数据
	 */
	@Override
	public List<Map<String, Object>> getBuInventoryGruop(Map<String, Object> map) throws SQLException {		
		return mapper.getBuInventoryGruop(map);
	}
	/**
	 * 删除 该商家 仓库 事业部 月份 的事业部库存
	 */
	@Override
	public int delBuInventory(Map<String, Object> map) throws SQLException {
		return mapper.delBuInventory(map);
	}
	
	
	@Override
	public List<Map<String, Object>> countByMonthAndMerchant(BuInventoryDTO model) throws SQLException {
		return mapper.countByMonthAndMerchant(model);
	}

	@Override
	public List<Map<String, Object>> countBuInventoryByBuId(BuInventoryDTO model) throws SQLException {
		return mapper.countBuInventoryByBuId(model);
	}

	@Override
	public List<Map<String, Object>> countBuInventoryByDepotId(BuInventoryDTO model) throws SQLException {
		return mapper.countBuInventoryByDepotId(model);
	}
	@Override
	public List<Map<String, Object>> exportBuInventoryItem(BuInventoryDTO model) throws Exception {
		return mapper.exportBuInventoryItem(model);
	}
	

}