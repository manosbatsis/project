package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.dto.main.MerchantInfoDTO;
import com.topideal.entity.vo.main.CustomerMerchantRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.mapper.main.MerchantInfoMapper;
import com.topideal.mongo.dao.MerchantInfoMongoDao;

import net.sf.json.JSONObject;

/**
 * 商家信息impl
 * @author lchenxing
 */
@Repository
public class MerchantInfoDaoImpl implements MerchantInfoDao {

    @Autowired
    private MerchantInfoMapper mapper;
    @Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao; //商家信息 mongo

	/**
	 * 新增
	 * @param MerchantInfoDTO
	 */
    @Override
    public Long save(MerchantInfoModel model) throws SQLException {
    	if(!"".equals(model.getArticulationPercent())&&model.getArticulationPercent()!=null){//把页面勾稽数据变为百分比
    		Double articulation_percent=model.getArticulationPercent()/100.00;
        	model.setArticulationPercent(articulation_percent);
    	}
        int num=mapper.insert(model);
        if(num==1){
        	//存储到MONGODB
			JSONObject jsonObject=JSONObject.fromObject(model);
			jsonObject.put("merchantId",model.getId());
			jsonObject.remove("id");

			merchantInfoMongoDao.insertJson(jsonObject.toString());
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
		//先从mongoDB删除
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("merchantId",(Long)ids.get(i));
			merchantInfoMongoDao.remove(params);
		}
	    return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param List
     */
    @Override
    public int modify(MerchantInfoModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
    	if(!"".equals(model.getArticulationPercent())&&model.getArticulationPercent()!=null){//把页面勾稽数据变为百分比
    		Double articulation_percent=model.getArticulationPercent()/100.00;
        	model.setArticulationPercent(articulation_percent);
    	}	
    	int num = mapper.update(model);
    	if(num > 0){
    		MerchantInfoModel merchant = new MerchantInfoModel();
    		merchant.setId(model.getId());
    		merchant = mapper.get(merchant);
	    	//修改mongodb 商家信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("merchantId",model.getId());

			JSONObject jsonObject=JSONObject.fromObject(merchant);
			jsonObject.put("merchantId",model.getId());
			jsonObject.remove("id");

			merchantInfoMongoDao.update(params,jsonObject);
    	}
        return num;
    }
    
    /**
     * 修改非必填字段
     */
    @Override
    public int updateNULL(MerchantInfoModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
    	// 修改数据为空
    	int num = mapper.updateNULL(model);   	   	
        return num;
    }
    
	/**
     * 分页查询
     * @param MerchantInfoDTO
     */
    @Override
    public MerchantInfoModel  searchByPage(MerchantInfoModel  model) throws SQLException{
        PageDataModel<MerchantInfoModel> pageModel=mapper.listByPage(model);
        return (MerchantInfoModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public MerchantInfoModel searchById(Long id)throws SQLException {
        MerchantInfoModel  model=new MerchantInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
    /**
     * 根据商家实体类查询商品
     * @param MerchandiseInfoModel
     * */
	@Override
	public MerchantInfoModel searchByModel(MerchantInfoModel model) throws SQLException{
		return mapper.get(model);
	}

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MerchantInfoModel> list(MerchantInfoModel model) throws SQLException {
		return mapper.list(model);
	}
	
	/**
	 * 商家信息表下拉列表
	 */
	@Override
	public List<SelectBean> getSelectBean(MerchantInfoModel model) throws SQLException {
		return mapper.getSelectBean(model);
	}

	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public MerchantInfoDTO getListByPage(MerchantInfoDTO dto) throws SQLException {
		PageDataModel<MerchantInfoDTO> pageModel=mapper.getListByPage(dto);
        return (MerchantInfoDTO ) pageModel.getModel();
	}
	/**
	 * 根据条件查询下拉列表
	 * 
	 * @param id
	 */
	@Override
	public List<SelectBean> getSelectBeanById(MerchantInfoModel model) throws SQLException {
		return mapper.getSelectBeanById(model);
	}

	@Override
	public MerchantInfoDTO searchDTOById(Long id) {
		return mapper.searchDTOById(id);
	}

    @Override
    public List<MerchantInfoDTO> getMerchantRelInfoAndMerchantInfo(Map<String, Object> map) {
		return mapper.getMerchantRelInfoAndMerchantInfo(map);
    }

	

}
