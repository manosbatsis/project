package com.topideal.dao.main.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.MerchantBuRelDao;
import com.topideal.entity.dto.main.MerchantBuRelDTO;
import com.topideal.entity.vo.main.DepotInfoModel;
import com.topideal.entity.vo.main.MerchantBuRelModel;
import com.topideal.mapper.main.MerchantBuRelMapper;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;

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
public class MerchantBuRelDaoImpl implements MerchantBuRelDao {

    @Autowired
    private MerchantBuRelMapper mapper;
    @Autowired
    private MerchantBuRelMongoDao mongo;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MerchantBuRelModel> list(MerchantBuRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MerchantBuRelModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
			//存储到MONGODB
			String json= JSONObject.fromObject(model).toString();
			JSONObject jsonObject=JSONObject.fromObject(json);
			jsonObject.put("merchantBuRelId",model.getId());
			jsonObject.remove("id");
			mongo.insertJson(jsonObject.toString());
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

		// 先从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("merchantBuRelId", (Long) ids.get(i));
			mongo.remove(params);
		}

        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(MerchantBuRelModel  model) throws SQLException {
    	int num = mapper.update(model) ;
    	if(num > 0){
    		MerchantBuRelModel relModel = new MerchantBuRelModel();
    		relModel.setId(model.getId());
    		relModel = mapper.get(relModel);

			//修改mongodb 仓库信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("merchantBuRelId", relModel.getId());

			JSONObject jsonObject=JSONObject.fromObject(relModel);
			jsonObject.put("merchantBuRelId",relModel.getId());
			jsonObject.remove("id");

			mongo.update(params,jsonObject);
    	}
    	
        return num;
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MerchantBuRelModel  searchByPage(MerchantBuRelModel  model) throws SQLException{
        PageDataModel<MerchantBuRelModel> pageModel=mapper.listByPage(model);
        return (MerchantBuRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MerchantBuRelModel  searchById(Long id)throws SQLException {
        MerchantBuRelModel  model=new MerchantBuRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MerchantBuRelModel searchByModel(MerchantBuRelModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public List<SelectBean> getSelectBean(MerchantBuRelModel merchantBuRelModel) {
		return mapper.getSelectBean(merchantBuRelModel);
	}
	@Override
	public MerchantBuRelDTO getListByPage(MerchantBuRelDTO dto) {
		PageDataModel<MerchantBuRelDTO> pageModel=mapper.getListByPage(dto);
        return (MerchantBuRelDTO ) pageModel.getModel();
	}
	@Override
	public List<MerchantBuRelDTO> getExportList(MerchantBuRelDTO dto) {
		return mapper.getExportList(dto);
	}
	
	@Override
	public MerchantBuRelDTO getMerchantBuRel(MerchantBuRelDTO dto) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getMerchantBuRel(dto);
	}

}