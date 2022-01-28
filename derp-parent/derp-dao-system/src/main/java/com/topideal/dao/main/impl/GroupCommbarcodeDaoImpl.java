package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.GroupCommbarcodeDao;
import com.topideal.entity.dto.main.GroupCommbarcodeDTO;
import com.topideal.entity.vo.main.GroupCommbarcodeModel;
import com.topideal.mapper.main.GroupCommbarcodeMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class GroupCommbarcodeDaoImpl implements GroupCommbarcodeDao {

    @Autowired
    private GroupCommbarcodeMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<GroupCommbarcodeModel> list(GroupCommbarcodeModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(GroupCommbarcodeModel model) throws SQLException {
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
    public int modify(GroupCommbarcodeModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public GroupCommbarcodeModel  searchByPage(GroupCommbarcodeModel  model) throws SQLException{
        PageDataModel<GroupCommbarcodeModel> pageModel=mapper.listByPage(model);
        return (GroupCommbarcodeModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public GroupCommbarcodeModel  searchById(Long id)throws SQLException {
        GroupCommbarcodeModel  model=new GroupCommbarcodeModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public GroupCommbarcodeModel searchByModel(GroupCommbarcodeModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 获取列表
	 */
	@Override
	public List<GroupCommbarcodeDTO> listgroupCommbarcodes(GroupCommbarcodeDTO model) {
		return mapper.listgroupCommbarcodes(model);
	}
	/**
	 * 获取列表分页信息
	 */
	@Override
	public List<GroupCommbarcodeDTO> listgroupCommbarcodesPage(GroupCommbarcodeDTO model) {
		return mapper.listgroupCommbarcodesPage(model);
	}

}