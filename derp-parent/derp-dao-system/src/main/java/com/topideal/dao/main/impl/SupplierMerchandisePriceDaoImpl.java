package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.SupplierMerchandisePriceDao;
import com.topideal.entity.dto.main.SupplierMerchandisePriceDTO;
import com.topideal.entity.vo.main.SupplierMerchandisePriceModel;
import com.topideal.mapper.main.SupplierMerchandisePriceMapper;
import com.topideal.mongo.dao.SupplierMerchandisePriceMongoDao;

import net.sf.json.JSONObject;

/**
 * 供应商商品价格表 impl
 * @author lchenxing
 */
@Repository
public class SupplierMerchandisePriceDaoImpl implements SupplierMerchandisePriceDao {

    @Autowired
    private SupplierMerchandisePriceMapper mapper;  //供应商商品价格表 mapper
    @Autowired
    private SupplierMerchandisePriceMongoDao supplierMerchandisePriceMongoDao;// MongoDB
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SupplierMerchandisePriceModel> list(SupplierMerchandisePriceModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param SupplierMerchandisePriceDTO
	 */
    @Override
    public Long save(SupplierMerchandisePriceModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            //存储到MONGODB
            String json= JSONObject.fromObject(model).toString();
            JSONObject jsonObject=JSONObject.fromObject(json);
            jsonObject.put("supplierMerchandisePriceId",model.getId());
            jsonObject.remove("id");
			jsonObject.remove("expiryDate");
			jsonObject.remove("effectiveDate");
			jsonObject.remove("auditDate");
			jsonObject.put("expiryDate", TimeUtils.format(model.getExpiryDate(), "yyyy-MM-dd"));
			jsonObject.put("effectiveDate", TimeUtils.format(model.getEffectiveDate(), "yyyy-MM-dd"));
			if(model.getAuditDate() != null) {				
				jsonObject.put("auditDate", TimeUtils.format(model.getAuditDate(), "yyyy-MM-dd HH:mm:ss"));
			}
            supplierMerchandisePriceMongoDao.insertJson(jsonObject.toString());
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
            params.put("supplierMerchandisePriceId",(Long)ids.get(i));
            supplierMerchandisePriceMongoDao.remove(params);
        }
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param List
     */
    @Override
    public int modify(SupplierMerchandisePriceModel  model) throws SQLException {
    	//得到当前系统的时间
    	model.setModifyDate(TimeUtils.getNow());
        int update = mapper.update(model);
        if(update>0){
            SupplierMerchandisePriceModel supplierMerchandisePriceModel = new SupplierMerchandisePriceModel();
            supplierMerchandisePriceModel.setId(model.getId());
            supplierMerchandisePriceModel = mapper.get(supplierMerchandisePriceModel);
            //修改mongodb 货品信息
            Map<String, Object> params = new HashMap<String,Object>();
            params.put("supplierMerchandisePriceId",supplierMerchandisePriceModel.getId());
            String json= JSONObject.fromObject(supplierMerchandisePriceModel).toString();
            JSONObject jsonObject=JSONObject.fromObject(json);
            jsonObject.put("supplierMerchandisePriceId",supplierMerchandisePriceModel.getId());
            jsonObject.remove("id");
        	jsonObject.remove("expiryDate");
			jsonObject.remove("effectiveDate");
			jsonObject.remove("auditDate");
			jsonObject.put("expiryDate", TimeUtils.format(supplierMerchandisePriceModel.getExpiryDate(), "yyyy-MM-dd"));
			jsonObject.put("effectiveDate", TimeUtils.format(supplierMerchandisePriceModel.getEffectiveDate(), "yyyy-MM-dd"));
			if(model.getAuditDate() != null) {				
				jsonObject.put("auditDate", TimeUtils.format(model.getAuditDate(), "yyyy-MM-dd HH:mm:ss"));
			}
            supplierMerchandisePriceMongoDao.update(params,jsonObject);
        }
        return update;
    }
    
	/**
     * 分页查询
     * @param SupplierMerchandisePriceDTO
     */
    @Override
    public SupplierMerchandisePriceModel  searchByPage(SupplierMerchandisePriceModel  model) throws SQLException{
        PageDataModel<SupplierMerchandisePriceModel> pageModel=mapper.listByPage(model);
        return (SupplierMerchandisePriceModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public SupplierMerchandisePriceModel  searchById(Long id)throws SQLException {
        SupplierMerchandisePriceModel  model=new SupplierMerchandisePriceModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
            /**
     	* 根据商家实体类查询商品
     	* @param MerchandiseInfoModel
     	* */
	@Override
	public SupplierMerchandisePriceModel searchByModel(SupplierMerchandisePriceModel model) throws SQLException {
		return mapper.get(model);
	}

    /**
     * 列表查询(分页)
     * @param model
     * @return
     * @throws SQLException
     */
	@Override
	public SupplierMerchandisePriceDTO getListByPage(SupplierMerchandisePriceDTO model)throws SQLException {
		PageDataModel<SupplierMerchandisePriceDTO> pageModel=mapper.getDTOListByPage(model);
        return (SupplierMerchandisePriceDTO ) pageModel.getModel();
	}

    @Override
    public List<SupplierMerchandisePriceDTO> queryDTOList(SupplierMerchandisePriceDTO dto) throws SQLException {
        return mapper.queryDTOList(dto);
    }

    /**
     *  详情
     * @param id
     * @return
     * @throws SQLException
     */
	@Override
	public SupplierMerchandisePriceDTO getDetails(Long id) throws SQLException {
		return mapper.getDetails(id);
	}
	@Override
	public SupplierMerchandisePriceModel getSMPriceByPurchaseOrder(SupplierMerchandisePriceModel model) {
		return mapper.getSMPriceByPurchaseOrder(model);
	}

    @Override
    public List<Map<String, Object>> statisticsStateNum(SupplierMerchandisePriceDTO dto) {
        return mapper.statisticsStateNum(dto);
    }

    @Override
    public List<SupplierMerchandisePriceModel> listByIds(List<Long> ids) {
        return mapper.listByIds(ids);
    }

    @Override
    public SupplierMerchandisePriceDTO searchDTOById(Long id) {
        return mapper.searchDTOById(id);
    }

    @Override
    public List<SupplierMerchandisePriceModel> listGroupByStockType(SupplierMerchandisePriceModel model) {
        return mapper.listGroupByStockType(model);
    }


}
