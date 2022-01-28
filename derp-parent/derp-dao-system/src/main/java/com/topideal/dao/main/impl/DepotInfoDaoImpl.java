package com.topideal.dao.main.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.DepotInfoDao;
import com.topideal.entity.dto.main.DepotInfoDTO;
import com.topideal.entity.dto.main.DepotMerchantRelDTO;
import com.topideal.entity.dto.main.MerchantDepotBuRelDTO;
import com.topideal.entity.vo.main.DepotInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.mapper.main.DepotInfoMapper;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仓库信息表  Impl
 * @author lchenxing
 */
@Repository
public class DepotInfoDaoImpl implements DepotInfoDao {

    @Autowired
    private DepotInfoMapper mapper; //仓库信息表
	@Autowired
	private DepotInfoMongoDao mongo;  //仓库信息 mongo

	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(DepotInfoModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
			//存储到MONGODB
			String json= JSONObject.fromObject(model).toString();
			JSONObject jsonObject=JSONObject.fromObject(json);
			jsonObject.put("depotId",model.getId());
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
			params.put("depotId",(Long)ids.get(i));
			mongo.remove(params);
		}
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(DepotInfoModel model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());//获取当前系统时间    		
    	int num = mapper.update(model);
    	if(num > 0){
    		DepotInfoModel depotInfo = new DepotInfoModel();
    		depotInfo.setId(model.getId());
    		depotInfo = mapper.get(depotInfo);

			//修改mongodb 仓库信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("depotId",model.getId());

			JSONObject jsonObject=JSONObject.fromObject(depotInfo);
			jsonObject.put("depotId",model.getId());
			jsonObject.remove("id");

			mongo.update(params,jsonObject);
    	}
        return num;
    }
    
    @Override
    public int updateNULL(DepotInfoModel model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());//获取当前系统时间   	
		// 把仓库传值为空的设置为空(指定字段)
		int num = mapper.updateNULL(model);    	
        return num;
    }

	@Override
	public List<DepotInfoModel> listByLike(DepotInfoModel depotInfoModel) {
		return mapper.listByLike(depotInfoModel);
	}

	/**
     * 分页查询
     * @param model
     */
    @Override
    public DepotInfoModel  searchByPage(DepotInfoModel  model) throws SQLException{
        PageDataModel<DepotInfoModel> pageModel=mapper.listByPage(model);
        return (DepotInfoModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public DepotInfoModel  searchById(Long id)throws SQLException {
        DepotInfoModel  model=new DepotInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }

    /**
     * 通过实体查询实体类信息
     * @param model
     * @return
     */
	@Override
	public DepotInfoModel searchByModel(DepotInfoModel model)
			throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<DepotInfoModel> list(DepotInfoModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 查询商品下拉列表
	 * */
	@Override
	public List<SelectBean> getSelectBean(DepotInfoModel depotInfoModel) throws SQLException {
		return mapper.getSelectBean(depotInfoModel);
	}

	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public DepotInfoDTO getListByPage(DepotInfoDTO model) throws SQLException {
		PageDataModel<DepotInfoDTO> pageModel=mapper.getListByPage(model);
        return (DepotInfoDTO ) pageModel.getModel();
	}

	/**
	 * 查询代理商家下拉列表
	 */
	@Override
	public List<MerchantInfoModel> getSelectPoxy() throws SQLException {
		return mapper.getSelectPoxy();
	}

	/**
	 * 查询仓库下拉列表，是否同关区
	 * */
	@Override
	public List<SelectBean> getSelectBeanByArea(DepotInfoDTO dto) throws SQLException {
		return mapper.getSelectBeanByArea(dto);
	}

	@Override
	public List<SelectBean> getSelectBeanForAdmin(DepotInfoModel depotInfoModel) throws SQLException {
		return mapper.getSelectBeanForAdmin(depotInfoModel);
	}
	/**
	 * 根据页面传入商家id获取此商家下仓库的下拉框
	 */
	@Override
	public List<SelectBean> getSelectBeanByMerchantRel(DepotMerchantRelDTO dto) throws SQLException {
		return mapper.getSelectBeanByMerchantRel(dto);
	}

	@Override
	public DepotInfoDTO searchDTOById(Long id) {
		return mapper.searchDTOById(id);
	}
	/**
	 * 获取商家下仓库的下拉框
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<DepotInfoDTO> getSelectBeanByDTO(DepotInfoDTO dto) throws SQLException {		
		return mapper.getSelectBeanByDTO(dto);
	}

	@Override
	public List<SelectBean> getSelectBeanByMerchantBuRel(MerchantDepotBuRelDTO dto) throws SQLException {
		return mapper.getSelectBeanByMerchantBuRel(dto);
	}


}
