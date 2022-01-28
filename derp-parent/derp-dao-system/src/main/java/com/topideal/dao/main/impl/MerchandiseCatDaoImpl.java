package com.topideal.dao.main.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.MerchandiseCatDao;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.mapper.main.MerchandiseCatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 商品分类表  impl
 * @author lchenxing
 */
@Repository
public class MerchandiseCatDaoImpl implements MerchandiseCatDao {

    @Autowired
    private MerchandiseCatMapper mapper;

	/**
	 * 新增
	 * @param MerchandiseCatModel
	 */
    @Override
    public Long save(MerchandiseCatModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param List
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param List
     */
    @Override
    public int modify(MerchandiseCatModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param MerchandiseCatModel
     */
    @Override
    public MerchandiseCatModel  searchByPage(MerchandiseCatModel  model) throws SQLException{
        PageDataModel<MerchandiseCatModel> pageModel=mapper.listByPage(model);
        return (MerchandiseCatModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public MerchandiseCatModel  searchById(Long id) throws SQLException{
        MerchandiseCatModel  model=new MerchandiseCatModel ();
        model.setId(id);
        return mapper.get(model);
    }

    /**
	 * 根据实体类查询实体
	 * @param MerchandiseCatModel
	 * @return
	 */
	@Override
	public MerchandiseCatModel searchByModel(MerchandiseCatModel model)
			throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MerchandiseCatModel> list(MerchandiseCatModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 查询下拉列表
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SelectBean> getSelectBean(Map<String, Object> map) throws SQLException {
		return mapper.getSelectBean(map);
	}
	/**
	 * 根据传参获取商品分类表数据
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SelectBean> getSelectBeanByModel(MerchandiseCatModel model) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getSelectBeanByModel(model);
	}

	@Override
	public List<MerchandiseCatModel> listByLike(MerchandiseCatModel catModel) {
		return mapper.listByLike(catModel);
	}

}
