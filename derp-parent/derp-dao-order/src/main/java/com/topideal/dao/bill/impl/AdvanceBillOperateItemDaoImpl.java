package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.AdvanceBillOperateItemDao;
import com.topideal.entity.dto.bill.AdvanceBillOperateItemDTO;
import com.topideal.entity.vo.bill.AdvanceBillOperateItemModel;
import com.topideal.mapper.bill.AdvanceBillOperateItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class AdvanceBillOperateItemDaoImpl implements AdvanceBillOperateItemDao {

    @Autowired
    private AdvanceBillOperateItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AdvanceBillOperateItemModel> list(AdvanceBillOperateItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AdvanceBillOperateItemModel model) throws SQLException {
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
    public int modify(AdvanceBillOperateItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AdvanceBillOperateItemModel  searchByPage(AdvanceBillOperateItemModel  model) throws SQLException{
        PageDataModel<AdvanceBillOperateItemModel> pageModel=mapper.listByPage(model);
        return (AdvanceBillOperateItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AdvanceBillOperateItemModel  searchById(Long id)throws SQLException {
        AdvanceBillOperateItemModel  model=new AdvanceBillOperateItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AdvanceBillOperateItemModel searchByModel(AdvanceBillOperateItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<AdvanceBillOperateItemDTO> getAdvanceById(Long advanceId) {
        return mapper.getAdvanceById(advanceId);
    }

    @Override
    public int delItems(Long advanceId) throws SQLException {
        return mapper.delItems(advanceId);
    }

    @Override
    public  List<Map<String, Object>> getMaxAuditDate(List<Long> advanceIds) {
        return mapper.getMaxAuditDate(advanceIds);
    }

    @Override
    public List<AdvanceBillOperateItemModel> getLatestAuditModelByAdvanceIds(List<Long> advanceIds) {
        return mapper.getLatestAuditModelByAdvanceIds(advanceIds);
    }
}