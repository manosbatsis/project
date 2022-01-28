package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.ApiGoodsConfigDao;
import com.topideal.entity.vo.reporting.ApiGoodsConfigModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.mapper.reporting.ApiGoodsConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ApiGoodsConfigDaoImpl implements ApiGoodsConfigDao {

    @Autowired
    private ApiGoodsConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ApiGoodsConfigModel> list(ApiGoodsConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ApiGoodsConfigModel model) throws SQLException {
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
    public int modify(ApiGoodsConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ApiGoodsConfigModel  searchByPage(ApiGoodsConfigModel  model) throws SQLException{
        PageDataModel<ApiGoodsConfigModel> pageModel=mapper.listByPage(model);
        return (ApiGoodsConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ApiGoodsConfigModel  searchById(Long id)throws SQLException {
        ApiGoodsConfigModel  model=new ApiGoodsConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ApiGoodsConfigModel searchByModel(ApiGoodsConfigModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<MerchandiseInfoModel> getConfigMerchandiseList(ApiGoodsConfigModel apiGoodsConfigModel) throws SQLException {
        return mapper.getConfigMerchandiseList(apiGoodsConfigModel);
    }
}