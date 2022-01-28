package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.GroupCommbarcodeItemDao;
import com.topideal.entity.vo.main.GroupCommbarcodeItemModel;
import com.topideal.mapper.main.GroupCommbarcodeItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class GroupCommbarcodeItemDaoImpl implements GroupCommbarcodeItemDao {

    @Autowired
    private GroupCommbarcodeItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<GroupCommbarcodeItemModel> list(GroupCommbarcodeItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(GroupCommbarcodeItemModel model) throws SQLException {
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
    public int modify(GroupCommbarcodeItemModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public GroupCommbarcodeItemModel  searchByPage(GroupCommbarcodeItemModel  model) throws SQLException{
        PageDataModel<GroupCommbarcodeItemModel> pageModel=mapper.listByPage(model);
        return (GroupCommbarcodeItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public GroupCommbarcodeItemModel  searchById(Long id)throws SQLException {
        GroupCommbarcodeItemModel  model=new GroupCommbarcodeItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public GroupCommbarcodeItemModel searchByModel(GroupCommbarcodeItemModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 根据标准条码ID删除
	 */
	@Override
	public int deleteByCommbarcodeId(List<Long> idList) {
		return mapper.deleteByCommbarcodeId(idList);
	}

}