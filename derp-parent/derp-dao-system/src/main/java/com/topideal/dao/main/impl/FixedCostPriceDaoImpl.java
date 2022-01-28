package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.FixedCostPriceDao;
import com.topideal.entity.dto.main.FixedCostPriceDTO;
import com.topideal.entity.vo.main.FixedCostPriceModel;
import com.topideal.mapper.main.FixedCostPriceMapper;
import com.topideal.mongo.dao.FixedCostPriceMongoDao;
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
public class FixedCostPriceDaoImpl implements FixedCostPriceDao {

    @Autowired
    private FixedCostPriceMapper mapper;
    @Autowired
    private FixedCostPriceMongoDao mongo ;

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<FixedCostPriceModel> list(FixedCostPriceModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(FixedCostPriceModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            JSONObject json = JSONObject.fromObject(model);
            json.remove("id") ;
            json.put("fixedCostPriceId", model.getId());
            if(model.getAuditDate() != null){
                json.put("auditDate", TimeUtils.format(model.getAuditDate(), "yyyy-MM-dd HH:mm:ss"));
            }
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
            params.put("fixedCostPriceId",(Long)ids.get(i));
            mongo.remove(params);
        }
        return mapper.batchDel(ids);
    }

	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(FixedCostPriceModel  model) throws SQLException {
        int num =mapper.update(model);
        if(num > 0) {
            FixedCostPriceModel fixedCostPriceModel = new FixedCostPriceModel();
            fixedCostPriceModel.setId(model.getId());
            fixedCostPriceModel = mapper.get(fixedCostPriceModel);

            Map<String, Object> params = new HashMap<String,Object>();
            params.put("fixedCostPriceId", model.getId());

            JSONObject jsonObject = JSONObject.fromObject(fixedCostPriceModel);
            jsonObject.put("fixedCostPriceId",model.getId());
            jsonObject.remove("id");
            if(model.getAuditDate() != null){
                jsonObject.put("auditDate", TimeUtils.format(fixedCostPriceModel.getAuditDate(), "yyyy-MM-dd HH:mm:ss"));
            }

            mongo.update(params, jsonObject);
        }

        return num;
    }

	/**
     * 分页查询
     * @param model
     */
    @Override
    public FixedCostPriceModel  searchByPage(FixedCostPriceModel  model) throws SQLException{
        PageDataModel<FixedCostPriceModel> pageModel=mapper.listByPage(model);
        return (FixedCostPriceModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public FixedCostPriceModel  searchById(Long id)throws SQLException {
        FixedCostPriceModel  model=new FixedCostPriceModel ();
        model.setId(id);
        return mapper.get(model);
    }

      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public FixedCostPriceModel searchByModel(FixedCostPriceModel model) throws SQLException {
		return mapper.get(model);
	}


    @Override
    public List<FixedCostPriceDTO> listByDTO(FixedCostPriceDTO dto) {
        return mapper.listByDTO(dto);
    }

    @Override
    public int updateByDTO(FixedCostPriceDTO dto) {
        return mapper.updateByDTO(dto);
    }

    @Override
    public int countByDTO(FixedCostPriceDTO dto) {
        return mapper.countByDTO(dto);
    }

    @Override
    public List<FixedCostPriceDTO> listForExport(FixedCostPriceDTO dto) {
        return mapper.listForExport(dto);
    }

    @Override
    public int batchSave(List<FixedCostPriceModel> list) {
        int num = mapper.batchSave(list);
        if(num==list.size()){
            for (FixedCostPriceModel model : list) {
                JSONObject json = JSONObject.fromObject(model);
                json.remove("id") ;
                json.put("fixedCostPriceId", model.getId());
                mongo.insertJson(json.toString());
            }
        }
        return num;
    }

    @Override
    public List<FixedCostPriceModel> getLatestModel(FixedCostPriceDTO dto) {
        return mapper.getLatestModel(dto);
    }

    @Override
    public FixedCostPriceDTO listDTOByPage(FixedCostPriceDTO dto) {
        PageDataModel<FixedCostPriceDTO> pageModel=mapper.listDTOByPage(dto);
        return (FixedCostPriceDTO ) pageModel.getModel();
    }
}
