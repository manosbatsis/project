package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryRealTimeSnapshotDao;
import com.topideal.entity.dto.InventoryRealTimeSnapshotDTO;
import com.topideal.entity.vo.InventoryRealTimeSnapshotModel;
import com.topideal.mapper.InventoryRealTimeSnapshotMapper;

/**
 * 实时库存快照表 daoImpl
 * @author lchenxing
 */
@Repository
public class InventoryRealTimeSnapshotDaoImpl implements InventoryRealTimeSnapshotDao {

    @Autowired
    private InventoryRealTimeSnapshotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<InventoryRealTimeSnapshotModel> list(InventoryRealTimeSnapshotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(InventoryRealTimeSnapshotModel model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
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
    public int modify(InventoryRealTimeSnapshotModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public InventoryRealTimeSnapshotModel  searchByPage(InventoryRealTimeSnapshotModel  model) throws SQLException{
        PageDataModel<InventoryRealTimeSnapshotModel> pageModel=mapper.listByPage(model);
        return (InventoryRealTimeSnapshotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public InventoryRealTimeSnapshotModel  searchById(Long id)throws SQLException {
        InventoryRealTimeSnapshotModel  model=new InventoryRealTimeSnapshotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
   /**
 	* 根据商家实体类查询商品
 	* @param model
 	*/
	@Override
	public InventoryRealTimeSnapshotModel searchByModel(InventoryRealTimeSnapshotModel model) throws SQLException {
		return mapper.get(model);
	}
	/**按商家、仓库、批次、效期...统计商品批次快照仓库现存量
	 * */
	public Integer getQtyNumBatch(Map<String, Object> paramMap){
		return mapper.getQtyNumBatch(paramMap);
	}
	/**
	 * 根据条件获取实时库存快照-支持分页获取
	 * @param merchantId
	 * @param depotId
	 * @param monthlyAccountDate yyyy-MM-dd
	 * @return
	 */
	@Override
	public List<InventoryRealTimeSnapshotModel> getListByParams(Long merchantId, Long depotId,
			String monthlyAccountDate,Integer begin,Integer pageSize) {
		return mapper.getListByParams(merchantId,depotId,monthlyAccountDate,begin,pageSize);
	}
	/**
     * 分页查询
     */
	public InventoryRealTimeSnapshotDTO getListByPage(InventoryRealTimeSnapshotDTO model){
		PageDataModel<InventoryRealTimeSnapshotDTO> pageModel = mapper.getListByPage(model);
		return (InventoryRealTimeSnapshotDTO) pageModel.getModel();
	}
	/**
	 * 根据条件获取实时库存的总条数
	 * @param merchantId
	 * @param depotId
	 * @param monthlyAccountDate yyyy-MM-dd
	 * @return
	 */
	@Override
	public Integer getListTotalNum(Long merchantId, Long depotId, String monthlyAccountDate) {
		return mapper.getListTotalNum(merchantId,depotId,monthlyAccountDate);
	}
	
	@Override
	public void delData() {
		mapper.delData();
	}
	
	/**
	 *  分页查询实时库存快照--页面列表
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public InventoryRealTimeSnapshotDTO getInventoryRealTimeSnapshotListByPage(InventoryRealTimeSnapshotDTO model)
			throws SQLException {
		 PageDataModel<InventoryRealTimeSnapshotDTO> pageModel = mapper.getInventoryRealTimeSnapshotListByPage(model);		 
		return  (InventoryRealTimeSnapshotDTO) pageModel.getModel();
	}
	/**
	 * 导出实时库存快照表
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> exportinventoryRealTimeSnapshotMap(InventoryRealTimeSnapshotModel model)
			throws Exception {
		// TODO Auto-generated method stub
		return mapper.exportinventoryRealTimeSnapshotMap(model);
	}
}
