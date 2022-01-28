package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.PojzTempDao;
import com.topideal.entity.vo.sale.PojzTempModel;
import com.topideal.mapper.sale.PojzTempMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PojzTempDaoImpl implements PojzTempDao {

    @Autowired
    private PojzTempMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PojzTempModel> list(PojzTempModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PojzTempModel model) throws SQLException {
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
    public int modify(PojzTempModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PojzTempModel  searchByPage(PojzTempModel  model) throws SQLException{
        PageDataModel<PojzTempModel> pageModel=mapper.listByPage(model);
        return (PojzTempModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PojzTempModel  searchById(Long id)throws SQLException {
        PojzTempModel  model=new PojzTempModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PojzTempModel searchByModel(PojzTempModel model) throws SQLException {
		return mapper.get(model);
	}
	/**唯品4.0-获取商家+仓库+po+货号临时结转余量
	 * */
	public Integer getPojzNum(Map<String, Object> map) {
		return mapper.getPojzNum(map);
	}
}