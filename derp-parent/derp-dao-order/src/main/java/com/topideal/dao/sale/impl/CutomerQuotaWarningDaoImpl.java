package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.CutomerQuotaWarningDao;
import com.topideal.entity.dto.sale.CutomerQuotaWarningDTO;
import com.topideal.entity.vo.sale.CutomerQuotaWarningModel;
import com.topideal.mapper.sale.CutomerQuotaWarningMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class CutomerQuotaWarningDaoImpl implements CutomerQuotaWarningDao {

    @Autowired
    private CutomerQuotaWarningMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CutomerQuotaWarningModel> list(CutomerQuotaWarningModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CutomerQuotaWarningModel model) throws SQLException {
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
    public int modify(CutomerQuotaWarningModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CutomerQuotaWarningModel  searchByPage(CutomerQuotaWarningModel  model) throws SQLException{
        PageDataModel<CutomerQuotaWarningModel> pageModel=mapper.listByPage(model);
        return (CutomerQuotaWarningModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CutomerQuotaWarningModel  searchById(Long id)throws SQLException {
        CutomerQuotaWarningModel  model=new CutomerQuotaWarningModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CutomerQuotaWarningModel searchByModel(CutomerQuotaWarningModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public CutomerQuotaWarningDTO listDTOByPage(CutomerQuotaWarningDTO dto) throws Exception {
        PageDataModel<CutomerQuotaWarningDTO> pageModel=mapper.listDTOByPage(dto);
        return (CutomerQuotaWarningDTO ) pageModel.getModel();
    }

    @Override
    public List<CutomerQuotaWarningDTO> listDTO(CutomerQuotaWarningDTO dto) throws Exception {
        return mapper.listDTO(dto);
    }
}