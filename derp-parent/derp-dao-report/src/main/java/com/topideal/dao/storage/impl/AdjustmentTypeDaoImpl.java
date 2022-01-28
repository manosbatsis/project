package com.topideal.dao.storage.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.storage.AdjustmentTypeDao;
import com.topideal.entity.vo.storage.AdjustmentTypeModel;
import com.topideal.mapper.storage.AdjustmentTypeMapper;

/**
 * 类型调整 daoImpl
 * @author lian_
 */
@Repository
public class AdjustmentTypeDaoImpl implements AdjustmentTypeDao {

    @Autowired
    private AdjustmentTypeMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AdjustmentTypeModel> list(AdjustmentTypeModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AdjustmentTypeModel model) throws SQLException {
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
    public int modify(AdjustmentTypeModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AdjustmentTypeModel  searchByPage(AdjustmentTypeModel  model) throws SQLException{
        PageDataModel<AdjustmentTypeModel> pageModel=mapper.listByPage(model);
        return (AdjustmentTypeModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AdjustmentTypeModel  searchById(Long id)throws SQLException {
        AdjustmentTypeModel  model=new AdjustmentTypeModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AdjustmentTypeModel searchByModel(AdjustmentTypeModel model) throws SQLException {
		return mapper.get(model);
	}
	
	
	/**
	 * 报表api 根据 商家开始时间结束时间分页查询类型调整单
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getListPage(Long merchantId, String startTime,String endTime,Integer startIndex,Integer pageSize,String type) throws SQLException {
		
		return mapper.getListPage(merchantId, startTime,endTime,startIndex,pageSize,type);
	}
	
	/**
	 * 报表api 根据 商家开始时间结束时间查询类型调整单总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Integer getListCount(Long merchantId, String startTime, String endTime,String type) {
		// TODO Auto-generated method stub
		return mapper.getListCount(merchantId,startTime,endTime,type);
	}
	/**
	 * 根据创建修改时间查询出库单
	 * */
	public List<Map<String, Object>> getOutDepotOrderByTime(Map<String, Object> paramMap){
		return mapper.getOutDepotOrderByTime(paramMap);
	}
	/**
	 * 根据创建修改时间查询出库单统计数量
	 * */
	public Integer getOutDepotOrderByTimeCount(Map<String, Object> paramMap){
		return mapper.getOutDepotOrderByTimeCount(paramMap);
	}
	/**
	 * 根据出库单号查询出库单商品-货号变更、效期调整
	 * */
	public List<Map<String, Object>> getOutDepotItemByCodes(List<String> codes){
		return mapper.getOutDepotItemByCodes(codes);
	}
	/**
	 * 根据出库单查询商品批次-货号变更、效期调整
	 * */
	public List<Map<String, Object>> getItemBatchByCode(Map<String, Object> paramMap){
		return mapper.getItemBatchByCode(paramMap);
	}
	/**
	 * 根据出库单号查询出库单商品-好坏品互转出、大货理货出
	 * */
	public List<Map<String, Object>> getOutDepotItemByCodes14(List<String> codes){
		return mapper.getOutDepotItemByCodes14(codes);
	}
	/**
	 * 根据出库单查询商品批次-好坏品互转出、大货理货出
	 * */
	public List<Map<String, Object>> getItemBatchByCode14(Map<String, Object> paramMap){
		return mapper.getItemBatchByCode14(paramMap);
	}

}



