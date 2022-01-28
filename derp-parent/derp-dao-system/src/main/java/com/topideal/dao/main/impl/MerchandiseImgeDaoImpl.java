package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.MerchandiseImgeDao;
import com.topideal.entity.vo.main.MerchandiseImgeModel;
import com.topideal.mapper.main.MerchandiseImgeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class MerchandiseImgeDaoImpl implements MerchandiseImgeDao {

    @Autowired
    private MerchandiseImgeMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MerchandiseImgeModel> list(MerchandiseImgeModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MerchandiseImgeModel model) throws SQLException {
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
    public int modify(MerchandiseImgeModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MerchandiseImgeModel  searchByPage(MerchandiseImgeModel  model) throws SQLException{
        PageDataModel<MerchandiseImgeModel> pageModel=mapper.listByPage(model);
        return (MerchandiseImgeModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MerchandiseImgeModel  searchById(Long id)throws SQLException {
        MerchandiseImgeModel  model=new MerchandiseImgeModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MerchandiseImgeModel searchByModel(MerchandiseImgeModel model) throws SQLException {
		return mapper.get(model);
	}

}