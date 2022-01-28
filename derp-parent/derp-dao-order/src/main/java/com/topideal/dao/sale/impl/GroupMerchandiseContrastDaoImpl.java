package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.GroupMerchandiseContrastDao;
import com.topideal.entity.dto.sale.GroupMerchandiseContrastDTO;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastModel;
import com.topideal.mapper.sale.GroupMerchandiseContrastMapper;


/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class GroupMerchandiseContrastDaoImpl implements GroupMerchandiseContrastDao {

    @Autowired
    private GroupMerchandiseContrastMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<GroupMerchandiseContrastModel> list(GroupMerchandiseContrastModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(GroupMerchandiseContrastModel model) throws SQLException {
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
    public int modify(GroupMerchandiseContrastModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public GroupMerchandiseContrastModel  searchByPage(GroupMerchandiseContrastModel  model) throws SQLException{
        PageDataModel<GroupMerchandiseContrastModel> pageModel=mapper.listByPage(model);
        return (GroupMerchandiseContrastModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public GroupMerchandiseContrastModel  searchById(Long id)throws SQLException {
        GroupMerchandiseContrastModel  model=new GroupMerchandiseContrastModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public GroupMerchandiseContrastModel searchByModel(GroupMerchandiseContrastModel model) throws SQLException {
		return mapper.get(model);
	}


    @Override
    public GroupMerchandiseContrastModel getDetails(GroupMerchandiseContrastModel model) throws SQLException {
        return mapper.getDetails(model);
    }

    @Override
    public GroupMerchandiseContrastModel isExist(GroupMerchandiseContrastModel model) throws SQLException {
        return mapper.isExist(model);
    }

    @Override
    public List<GroupMerchandiseContrastDTO> getExportList(GroupMerchandiseContrastDTO dto) throws SQLException {
        return mapper.getExportList(dto);
    }
    @Override
    public int isExistSkuId(String skuId) {
        return mapper.isExistSkuId(skuId);
    }
	@Override
	public GroupMerchandiseContrastDTO getDTODetails(GroupMerchandiseContrastDTO dto) throws SQLException {
		return mapper.getDTODetails(dto);
	}
	/**
     * 分页查询
     * @param model
     */
    @Override
    public GroupMerchandiseContrastDTO  searchDTOByPage(GroupMerchandiseContrastDTO  dto) throws SQLException{
        PageDataModel<GroupMerchandiseContrastDTO> pageModel=mapper.listDTOByPage(dto);
        return (GroupMerchandiseContrastDTO ) pageModel.getModel();
    }


}