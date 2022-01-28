package com.topideal.dao.base.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.base.ExchangeRateConfigDao;
import com.topideal.entity.dto.base.ExchangeRateConfigDTO;
import com.topideal.entity.vo.base.ExchangeRateConfigModel;
import com.topideal.mapper.base.ExchangeRateConfigMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ExchangeRateConfigDaoImpl implements ExchangeRateConfigDao {

    @Autowired
    private ExchangeRateConfigMapper mapper;
	

	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ExchangeRateConfigModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
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
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(ExchangeRateConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ExchangeRateConfigModel  searchByPage(ExchangeRateConfigModel  model) throws SQLException{
        PageDataModel<ExchangeRateConfigModel> pageModel=mapper.listByPage(model);
        return (ExchangeRateConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ExchangeRateConfigModel  searchById(Long id)throws SQLException {
        ExchangeRateConfigModel  model=new ExchangeRateConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ExchangeRateConfigModel searchByModel(ExchangeRateConfigModel model) throws SQLException {
		return mapper.get(model);
	}

	@Override
	public List<ExchangeRateConfigModel> list(ExchangeRateConfigModel model) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.list(model);
	}

	@Override
	public ExchangeRateConfigDTO getListByPage(ExchangeRateConfigDTO dto) throws SQLException {
		PageDataModel<ExchangeRateConfigDTO> pageModel = mapper.getListByPage(dto);
		return (ExchangeRateConfigDTO) pageModel.getModel();
	}

	//导出
	@Override
	public List<Map<String, Object>> listForExport(ExchangeRateConfigModel dto) throws Exception {
		return mapper.listForExport(dto);
	}
	


}