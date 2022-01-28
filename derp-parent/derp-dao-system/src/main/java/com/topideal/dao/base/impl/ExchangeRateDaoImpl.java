package com.topideal.dao.base.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.ExchangeRateDao;
import com.topideal.entity.dto.base.ExchangeRateDTO;
import com.topideal.entity.vo.base.ExchangeRateModel;
import com.topideal.mapper.base.ExchangeRateMapper;
import com.topideal.mongo.dao.ExchangeRateMongoDao;
import com.topideal.tools.BaseUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 汇率管理表 daoImpl
 * @author lian_
 */
@Repository
public class ExchangeRateDaoImpl implements ExchangeRateDao {

    @Autowired
    private ExchangeRateMapper mapper;
    @Autowired
    private ExchangeRateMongoDao exchangeRateMongoDao;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ExchangeRateModel> list(ExchangeRateModel model) throws SQLException {
		return mapper.listNoDel(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ExchangeRateModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            //存储到MONGODB
            String json= JSONObject.fromObject(model).toString();
            JSONObject jsonObject=JSONObject.fromObject(json);
            jsonObject.put("rateId",model.getId());
            jsonObject.put("effectiveDate", TimeUtils.formatDay(model.getEffectiveDate()));
            jsonObject.remove("id");
            exchangeRateMongoDao.insertJson(jsonObject.toString());
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
            params.put("rateId",(Long)ids.get(i));
            exchangeRateMongoDao.remove(params);
        }
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(ExchangeRateModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        int num = mapper.update(model);
        if(num > 0){
            if (DERP.DEL_CODE_006.equals(model.getStatus())) {
                Map<String, Object> params = new HashMap<String,Object>();
                params.put("rateId",model.getId());
                exchangeRateMongoDao.remove(params);
            } else {
                ExchangeRateModel rateModel = new ExchangeRateModel();
                rateModel.setId(model.getId());
                rateModel = mapper.get(rateModel);
                //修改mongodb
                Map<String, Object> params = new HashMap<String,Object>();
                params.put("rateId",model.getId());

                String json= JSONObject.fromObject(rateModel).toString();
                JSONObject jsonObject=JSONObject.fromObject(json);
                jsonObject.put("rateId",model.getId());
                jsonObject.remove("id");

                if (model.getEffectiveDate() != null) {
                    jsonObject.put("effectiveDate", TimeUtils.formatDay(model.getEffectiveDate()));
                }

                exchangeRateMongoDao.update(params,jsonObject);
            }
        }
        return num;
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ExchangeRateModel  searchByPage(ExchangeRateModel  model) throws SQLException{
        PageDataModel<ExchangeRateModel> pageModel=mapper.listByPage(model);
        return (ExchangeRateModel) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ExchangeRateModel  searchById(Long id)throws SQLException {
        ExchangeRateModel  model=new ExchangeRateModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ExchangeRateModel searchByModel(ExchangeRateModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public ExchangeRateDTO searchDTOById(Long id) {
		return mapper.searchDTOById(id);
	}
	@Override
	public ExchangeRateDTO getListByPage(ExchangeRateDTO dto) throws SQLException {
		PageDataModel<ExchangeRateDTO> pageModel=mapper.getListByPage(dto);
        return (ExchangeRateDTO) pageModel.getModel();
	}

    @Override
    public void deleteByEffectiveDate(String effectiveDate) throws SQLException {
        Map<BaseUtils.Operator,Map<String,Object>> params = new HashMap<>();
        Map<String,Object> dateMap = new HashMap<>();
        dateMap.put("effectiveDate", effectiveDate);
        params.put(BaseUtils.Operator.eq, dateMap);
        Map<String,Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("dataSource", "SGCJ");
        params.put(BaseUtils.Operator.ne, dataSourceMap);
        exchangeRateMongoDao.removeByOperator(params);
        mapper.deleteByEffectiveDate(effectiveDate);
    }

    @Override
    public ExchangeRateModel getLatestRate(ExchangeRateModel model) throws SQLException {
        return mapper.getLatestRate(model);
    }

}