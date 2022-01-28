package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.GroupMerchandiseContrastDetailDao;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastDetailModel;
import com.topideal.mapper.sale.GroupMerchandiseContrastDetailMapper;


/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class GroupMerchandiseContrastDetailDaoImpl implements GroupMerchandiseContrastDetailDao {

    @Autowired
    private GroupMerchandiseContrastDetailMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<GroupMerchandiseContrastDetailModel> list(GroupMerchandiseContrastDetailModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(GroupMerchandiseContrastDetailModel model) throws SQLException {
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
    public int modify(GroupMerchandiseContrastDetailModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public GroupMerchandiseContrastDetailModel  searchByPage(GroupMerchandiseContrastDetailModel  model) throws SQLException{
        PageDataModel<GroupMerchandiseContrastDetailModel> pageModel=mapper.listByPage(model);
        return (GroupMerchandiseContrastDetailModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public GroupMerchandiseContrastDetailModel  searchById(Long id)throws SQLException {
        GroupMerchandiseContrastDetailModel  model=new GroupMerchandiseContrastDetailModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public GroupMerchandiseContrastDetailModel searchByModel(GroupMerchandiseContrastDetailModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public int batchDelByGroupId(Long groupId) {
        return mapper.batchDelByGroupId(groupId);
    }
}