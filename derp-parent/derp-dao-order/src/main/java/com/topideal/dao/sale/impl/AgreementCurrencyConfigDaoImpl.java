package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.sale.AgreementCurrencyConfigDTO;
import com.topideal.entity.dto.sale.AgreementCurrencyConfigExportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.AgreementCurrencyConfigDao;
import com.topideal.entity.vo.sale.AgreementCurrencyConfigModel;
import com.topideal.mapper.sale.AgreementCurrencyConfigMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class AgreementCurrencyConfigDaoImpl implements AgreementCurrencyConfigDao {

    @Autowired
    private AgreementCurrencyConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AgreementCurrencyConfigModel> list(AgreementCurrencyConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AgreementCurrencyConfigModel model) throws SQLException {
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
    public int modify(AgreementCurrencyConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AgreementCurrencyConfigModel  searchByPage(AgreementCurrencyConfigModel  model) throws SQLException{
        PageDataModel<AgreementCurrencyConfigModel> pageModel=mapper.listByPage(model);
        return (AgreementCurrencyConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AgreementCurrencyConfigModel  searchById(Long id)throws SQLException {
        AgreementCurrencyConfigModel  model=new AgreementCurrencyConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AgreementCurrencyConfigModel searchByModel(AgreementCurrencyConfigModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public AgreementCurrencyConfigDTO queryDTOListByPage(AgreementCurrencyConfigDTO dto) throws SQLException {
        PageDataModel<AgreementCurrencyConfigDTO> pageModel = mapper.queryDTOListByPage(dto);
        return (AgreementCurrencyConfigDTO) pageModel.getModel();
    }

    @Override
    public int getTotal(AgreementCurrencyConfigDTO dto) throws SQLException {
        return mapper.getTotal(dto);
    }

    @Override
    public List<AgreementCurrencyConfigDTO> queryDTOList(AgreementCurrencyConfigDTO dto) throws SQLException {
        return mapper.queryDTOList(dto);
    }

    @Override
    public List<AgreementCurrencyConfigExportDTO> getDetailsByExport(AgreementCurrencyConfigDTO dto) throws SQLException {
        return mapper.getDetailsByExport(dto);
    }

    @Override
    public Map<String, Object> getConfigByMap(Map<String, Object> map) {
        return mapper.getConfigByMap(map);
    }

    @Override
    public int delConfig(List ids) throws SQLException {
        return mapper.delConfig(ids);
    }
}