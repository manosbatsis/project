package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.CommbarcodeDao;
import com.topideal.entity.dto.main.CommbarcodeDTO;
import com.topideal.entity.vo.main.CommbarcodeModel;
import com.topideal.mapper.main.CommbarcodeMapper;
import com.topideal.mongo.dao.CommbarcodeMongoDao;

import net.sf.json.JSONObject;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class CommbarcodeDaoImpl implements CommbarcodeDao {

    @Autowired
    private CommbarcodeMapper mapper;
    
    @Autowired
    private CommbarcodeMongoDao mongo ;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CommbarcodeModel> list(CommbarcodeModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CommbarcodeModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
        	JSONObject json = JSONObject.fromObject(model);
			json.remove("id") ;
			
			json.put("commbarcodeId", model.getId());//品牌id
			
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
			params.put("commbarcodeId",(Long)ids.get(i));
			mongo.remove(params);
		}
    	
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(CommbarcodeModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
    	
    	int num = mapper.update(model);

		if(num > 0){

			CommbarcodeModel commbarcodeModel= new CommbarcodeModel();
			commbarcodeModel.setId(model.getId());
			commbarcodeModel = mapper.get(commbarcodeModel);

			//修改mongodb信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("commbarcodeId",model.getId());

			JSONObject jsonObject=JSONObject.fromObject(commbarcodeModel);
			jsonObject.put("commbarcodeId",model.getId());
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
    public CommbarcodeModel  searchByPage(CommbarcodeModel  model) throws SQLException{
        PageDataModel<CommbarcodeModel> pageModel=mapper.listByPage(model);
        return (CommbarcodeModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CommbarcodeModel  searchById(Long id)throws SQLException {
        CommbarcodeModel  model=new CommbarcodeModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CommbarcodeModel searchByModel(CommbarcodeModel model) throws SQLException {
		return mapper.get(model);
	}
	
	@Override
	public List<CommbarcodeDTO> listCommbarcodes(CommbarcodeDTO model) {
		return mapper.listCommbarcodes(model);
	}
	
	@Override
	public Integer listCommbarcodesCount(CommbarcodeDTO model) {
		return mapper.listCommbarcodesCount(model);
	}
	@Override
	public List<CommbarcodeDTO> getExportList(List<String> ids) {
		return mapper.getExportList(ids);
	}
	@Override
	public List<CommbarcodeModel> getVagueList(String commbarcode) {
		return mapper.getVagueList(commbarcode);
	}
	@Override
	public CommbarcodeDTO searchByDTO(CommbarcodeDTO dto) {
		return mapper.searchByDTO(dto);
	}

	@Override
	public void batchUpdateCommbarcode(List<CommbarcodeModel> commbarcodeModels) throws SQLException {

		mapper.batchUpdateCommbarcode(commbarcodeModels);

		for (CommbarcodeModel tempModel : commbarcodeModels) {

			CommbarcodeModel commbarcodeModel= new CommbarcodeModel();
			commbarcodeModel.setId(tempModel.getId());
			commbarcodeModel = mapper.get(commbarcodeModel);

			//修改mongodb信息
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("commbarcodeId",tempModel.getId());

			JSONObject jsonObject=JSONObject.fromObject(commbarcodeModel);
			jsonObject.put("commbarcodeId",tempModel.getId());
			jsonObject.remove("id");

			mongo.update(params, jsonObject);
		}

	}
	
	@Override
	public List<CommbarcodeDTO> getExportCommbarcodes(List<String> ids){
		return mapper.getExportCommbarcodes(ids);
	}

}