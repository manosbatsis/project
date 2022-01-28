package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.GroupMerchandiseContrastDetailHistoryDao;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastDetailHistoryModel;
import com.topideal.mapper.sale.GroupMerchandiseContrastDetailHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class GroupMerchandiseContrastDetailHistoryDaoImpl implements GroupMerchandiseContrastDetailHistoryDao {

    @Autowired
    private GroupMerchandiseContrastDetailHistoryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<GroupMerchandiseContrastDetailHistoryModel> list(GroupMerchandiseContrastDetailHistoryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(GroupMerchandiseContrastDetailHistoryModel model) throws SQLException {
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
    public int modify(GroupMerchandiseContrastDetailHistoryModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public GroupMerchandiseContrastDetailHistoryModel  searchByPage(GroupMerchandiseContrastDetailHistoryModel  model) throws SQLException{
        PageDataModel<GroupMerchandiseContrastDetailHistoryModel> pageModel=mapper.listByPage(model);
        return (GroupMerchandiseContrastDetailHistoryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public GroupMerchandiseContrastDetailHistoryModel  searchById(Long id)throws SQLException {
        GroupMerchandiseContrastDetailHistoryModel  model=new GroupMerchandiseContrastDetailHistoryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public GroupMerchandiseContrastDetailHistoryModel searchByModel(GroupMerchandiseContrastDetailHistoryModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public int batchDelByGroupId(Long groupId) {
        return mapper.batchDelByGroupId(groupId);
    }
}