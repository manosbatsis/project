package com.topideal.dao.common.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.QuotaPeriodConfigDao;
import com.topideal.entity.dto.common.QuotaPeriodConfigDTO;
import com.topideal.entity.vo.common.QuotaPeriodConfigModel;
import com.topideal.mapper.common.QuotaPeriodConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class QuotaPeriodConfigDaoImpl implements QuotaPeriodConfigDao {

    @Autowired
    private QuotaPeriodConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<QuotaPeriodConfigModel> list(QuotaPeriodConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(QuotaPeriodConfigModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(QuotaPeriodConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public QuotaPeriodConfigModel  searchByPage(QuotaPeriodConfigModel  model) throws SQLException{
        PageDataModel<QuotaPeriodConfigModel> pageModel=mapper.listByPage(model);
        return (QuotaPeriodConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public QuotaPeriodConfigModel  searchById(Long id)throws SQLException {
        QuotaPeriodConfigModel  model=new QuotaPeriodConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public QuotaPeriodConfigModel searchByModel(QuotaPeriodConfigModel model) throws SQLException {
		return mapper.get(model);
	}
    
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}
	@Override
	public QuotaPeriodConfigDTO getListByPage(QuotaPeriodConfigDTO dto) {
		PageDataModel<QuotaPeriodConfigDTO> pageModel=mapper.getListByPage(dto);
        return (QuotaPeriodConfigDTO ) pageModel.getModel();
	}
}