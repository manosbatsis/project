package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.MerchantShopRelDao;
import com.topideal.entity.dto.main.MerchantShopRelDTO;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.MerchantShopRelModel;
import com.topideal.mapper.main.MerchantShopRelMapper;
import com.topideal.mongo.dao.MerchantShopRelMongoDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *商家店铺关联表 daoImpl
 * @author lchenxing
 */
@Repository
public class MerchantShopRelDaoImpl implements MerchantShopRelDao {

    @Autowired
    private MerchantShopRelMapper mapper;
    @Autowired
    private MerchantShopRelMongoDao mongo;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MerchantShopRelModel> list(MerchantShopRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MerchantShopRelModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
        	//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("shopId",model.getId());
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
    	//先从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("shopId",(Long)ids.get(i));
			mongo.remove(params);
		}
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(MerchantShopRelModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        int num= mapper.update(model);
        if(num > 0){
        	MerchantShopRelModel shopModel= new MerchantShopRelModel();
    		shopModel.setId(model.getId());
    		shopModel = mapper.get(shopModel);
			//修改mongodb信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("shopId",model.getId());
			
			JSONObject jsonObject=JSONObject.fromObject(shopModel);
			jsonObject.put("shopId",model.getId());
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
    public MerchantShopRelModel  searchByPage(MerchantShopRelModel  model) throws SQLException{
        PageDataModel<MerchantShopRelModel> pageModel=mapper.listByPage(model);
        return (MerchantShopRelModel ) pageModel.getModel();
    }          
    
    /**
     * 通过id查询实体类信息
     * @param id           
     */
    @Override
    public MerchantShopRelModel  searchById(Long id)throws SQLException {
        MerchantShopRelModel  model=new MerchantShopRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MerchantShopRelModel searchByModel(MerchantShopRelModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 查询商家下拉列表
	 */
	@Override
	public List<MerchantInfoModel> getSelectMerchant() throws SQLException {
		return mapper.getSelectMerchant();
	}
	/**
	 * 列表查询
	 */
	@Override
	public MerchantShopRelDTO getListByPage(MerchantShopRelDTO dto) throws SQLException {
		PageDataModel<MerchantShopRelDTO> pageModel = mapper.getListByPage(dto);
		return (MerchantShopRelDTO) pageModel.getModel();
	}
	/**
	 * 详情
	 */
	@Override
	public MerchantShopRelDTO getDetails(Long id) throws SQLException {
		return mapper.getDetails(id);
	}

	@Override
	public List<MerchantShopRelModel> getSelectMerchantShopRel(MerchantShopRelModel model) throws SQLException {
		return mapper.getSelectMerchantShopRel(model);
	}
	/**检查店铺编码是否已存在*/
	public List<MerchantShopRelModel> getcheckShopCode(MerchantShopRelModel model){
		return mapper.getcheckShopCode(model);
	}

	@Override
	public List<MerchantShopRelDTO> getExportList(MerchantShopRelDTO dto) {
		return mapper.getExportList(dto);
	}

	@Override
	public int updateWithNull(MerchantShopRelModel model) {
		return mapper.updateWithNull(model);
	}
}