package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.BuStockLocationTypeConfigDao;
import com.topideal.entity.dto.main.BuStockLocationTypeConfigDTO;
import com.topideal.entity.vo.main.BuStockLocationTypeConfigModel;
import com.topideal.mapper.main.BuStockLocationTypeConfigMapper;
import com.topideal.mongo.dao.BuStockLocationTypeConfigMongoDao;
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
public class BuStockLocationTypeConfigDaoImpl implements BuStockLocationTypeConfigDao {

    @Autowired
    private BuStockLocationTypeConfigMapper mapper;
    @Autowired
    private BuStockLocationTypeConfigMongoDao mongo ;

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuStockLocationTypeConfigModel> list(BuStockLocationTypeConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuStockLocationTypeConfigModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            JSONObject json = JSONObject.fromObject(model);
            json.remove("id") ;
            json.put("buStockLocationTypeId", model.getId());
            mongo.insertJson(json.toString());
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
        //从mongoDB删除
        for (int i = 0; i < ids.size(); i++) {
            Map<String, Object> params = new HashMap<String,Object>();
            params.put("buStockLocationTypeId",(Long)ids.get(i));
            mongo.remove(params);
        }
        return mapper.batchDel(ids);
    }

	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(BuStockLocationTypeConfigModel  model) throws SQLException {
        int num =mapper.update(model);
        if(num > 0) {
            BuStockLocationTypeConfigModel buStockLocationTypeConfigModel = new BuStockLocationTypeConfigModel();
            buStockLocationTypeConfigModel.setId(model.getId());
            buStockLocationTypeConfigModel = mapper.get(buStockLocationTypeConfigModel);

            Map<String, Object> params = new HashMap<String,Object>();
            params.put("buStockLocationTypeId", model.getId());

            JSONObject jsonObject = JSONObject.fromObject(buStockLocationTypeConfigModel);
            jsonObject.put("buStockLocationTypeId",model.getId());
            jsonObject.remove("id");

            mongo.update(params, jsonObject);
        }

        return num;
    }

	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuStockLocationTypeConfigModel  searchByPage(BuStockLocationTypeConfigModel  model) throws SQLException{
        PageDataModel<BuStockLocationTypeConfigModel> pageModel=mapper.listByPage(model);
        return (BuStockLocationTypeConfigModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuStockLocationTypeConfigModel  searchById(Long id)throws SQLException {
        BuStockLocationTypeConfigModel  model=new BuStockLocationTypeConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }

      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuStockLocationTypeConfigModel searchByModel(BuStockLocationTypeConfigModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public BuStockLocationTypeConfigDTO listDTOByPage(BuStockLocationTypeConfigDTO dto) {
        PageDataModel<BuStockLocationTypeConfigDTO> pageModel=mapper.listDTOByPage(dto);
        return (BuStockLocationTypeConfigDTO ) pageModel.getModel();
    }
}
