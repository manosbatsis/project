package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.TariffRateConfigDao;
import com.topideal.entity.dto.base.CustomsAreaConfigDTO;
import com.topideal.entity.dto.base.TariffRateConfigDTO;
import com.topideal.entity.vo.main.DepartmentInfoModel;
import com.topideal.entity.vo.main.TariffRateConfigModel;
import com.topideal.mapper.main.TariffRateConfigMapper;
import com.topideal.mongo.dao.TariffRateConfigMongoDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class TariffRateConfigDaoImpl implements TariffRateConfigDao {

    @Autowired
    private TariffRateConfigMapper mapper;
    @Autowired
    private TariffRateConfigMongoDao tariffRateConfigMongoDao;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TariffRateConfigModel> list(TariffRateConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TariffRateConfigModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            //mongo添加数据
            JSONObject json = JSONObject.fromObject(model);
            json.remove("id") ;
            json.put("tariffRateConfigId", model.getId());
            tariffRateConfigMongoDao.insertJson(json.toString());

            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param ids
     */
    @Override
    public int delete(List ids) throws SQLException {	// 先从mongoDB删除

        for (int i = 0; i < ids.size(); i++) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("tariffRateConfigId", (Long) ids.get(i));
            tariffRateConfigMongoDao.remove(params);
        }
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(TariffRateConfigModel  model) throws SQLException {

        //修改Mongo的数据
        TariffRateConfigModel tariffRateConfigModel=new TariffRateConfigModel();
        tariffRateConfigModel.setId(model.getId());;
        tariffRateConfigModel= mapper.get(tariffRateConfigModel);

        Map<String, Object> params = new HashMap<String,Object>();
        params.put("tariffRateConfigId", model.getId());

        JSONObject jsonObject = JSONObject.fromObject(tariffRateConfigModel);
        jsonObject.put("tariffRateConfigId",model.getId());
        jsonObject.remove("id");

        tariffRateConfigMongoDao.update(params, jsonObject);
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TariffRateConfigModel  searchByPage(TariffRateConfigModel  model) throws SQLException{
        PageDataModel<TariffRateConfigModel> pageModel=mapper.listByPage(model);
        return (TariffRateConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TariffRateConfigModel  searchById(Long id)throws SQLException {
        TariffRateConfigModel  model=new TariffRateConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TariffRateConfigModel searchByModel(TariffRateConfigModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public TariffRateConfigDTO getListByPage(TariffRateConfigDTO model) {
        PageDataModel<TariffRateConfigDTO> pageModel = mapper.getListByPage(model);
        return (TariffRateConfigDTO) pageModel.getModel();
    }
}