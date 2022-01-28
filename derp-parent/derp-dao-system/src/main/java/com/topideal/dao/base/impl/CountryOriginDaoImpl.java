package com.topideal.dao.base.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.base.CountryOriginDao;
import com.topideal.entity.vo.base.CountryOriginModel;
import com.topideal.mapper.base.CountryOriginMapper;
import com.topideal.mongo.dao.CountryOriginMongoDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 原产国  impl
 * @author lchenxing
 */
@Repository
public class CountryOriginDaoImpl implements CountryOriginDao {

    @Autowired
    private CountryOriginMapper mapper;     //原产国
    @Autowired
    private CountryOriginMongoDao mongo ;

	/**
	 * 新增
	 * @param CountryOriginModel
	 */
    @Override
    public Long save(CountryOriginModel model) throws SQLException {
        int num=mapper.insert(model);

        if(num==1){

            JSONObject json = JSONObject.fromObject(model);
            json.remove("id") ;
            json.put("countryOriginId", model.getId());//母品牌id
            mongo.insertJson(json.toString());

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

        for (int i = 0; i < ids.size(); i++) {
            Map<String, Object> params = new HashMap<String,Object>();
            params.put("countryOriginId",(Long)ids.get(i));
            mongo.remove(params);
        }

        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param List
     */
    @Override
    public int modify(CountryOriginModel  model) throws SQLException {

        int num = mapper.update(model);

        if(num > 0) {
            CountryOriginModel countryOriginModel = new CountryOriginModel();
            countryOriginModel.setId(model.getId());
            countryOriginModel = mapper.get(countryOriginModel);

            Map<String, Object> params = new HashMap<String,Object>();
            params.put("countryOriginId", model.getId());

            JSONObject jsonObject = JSONObject.fromObject(countryOriginModel);
            jsonObject.put("countryOriginId",model.getId());
            jsonObject.remove("id");

            mongo.update(params, jsonObject);
        }

        return num;
    }
    
	/**
     * 分页查询
     * @param CountryOriginModel
     */
    @Override
    public CountryOriginModel  searchByPage(CountryOriginModel  model) throws SQLException{
        PageDataModel<CountryOriginModel> pageModel=mapper.listByPage(model);
        return (CountryOriginModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public CountryOriginModel  searchById(Long id) throws SQLException{
        CountryOriginModel  model=new CountryOriginModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
    /**
     * 根据原产国实体类查询商品
     * @param CountryOriginModel
     *
     * */
	@Override
	public CountryOriginModel searchByModel(CountryOriginModel model) throws SQLException{
		return mapper.get(model);
	}

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CountryOriginModel> list(CountryOriginModel model) throws SQLException {
		return mapper.list(model);
	}
	
	/**
	 * 原产国下拉列表
	 */
	@Override
	public List<SelectBean> getSelectBean() throws SQLException {
		return mapper.getSelectBean();
	}

    @Override
    public List<CountryOriginModel> listByLike(CountryOriginModel countryOriginModel) {
        return mapper.listByLike(countryOriginModel);
    }
}
