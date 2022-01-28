package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.MerchandiseContrastDao;
import com.topideal.entity.dto.sale.MerchandiseContrastDTO;
import com.topideal.entity.vo.sale.MerchandiseContrastModel;
import com.topideal.mapper.sale.MerchandiseContrastMapper;

/**
 * 爬虫商品对照表 daoImpl
 * @author lian_
 *
 */
@Repository
public class MerchandiseContrastDaoImpl implements MerchandiseContrastDao {

    @Autowired
    private MerchandiseContrastMapper mapper;
	
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MerchandiseContrastModel model) throws SQLException {
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
    public int modify(MerchandiseContrastModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MerchandiseContrastModel  searchByPage(MerchandiseContrastModel  model) throws SQLException{
        PageDataModel<MerchandiseContrastModel> pageModel=mapper.listByPage(model);
        return (MerchandiseContrastModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MerchandiseContrastModel  searchById(Long id)throws SQLException {
        MerchandiseContrastModel  model=new MerchandiseContrastModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MerchandiseContrastModel searchByModel(MerchandiseContrastModel model) throws SQLException {
		return mapper.get(model);
	}

	@Override
	public List<MerchandiseContrastModel> list(MerchandiseContrastModel model) throws SQLException {
	
		return mapper.list(model);
	}

	/**
     * 分页查询
     * @param model
     */
    @Override
    public MerchandiseContrastDTO searchDTOByPage(MerchandiseContrastDTO  dto) throws SQLException{
        PageDataModel<MerchandiseContrastDTO> pageModel=mapper.searchDTOByPage(dto);
        return (MerchandiseContrastDTO ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MerchandiseContrastDTO  searchDTOById(Long id)throws SQLException {
    	MerchandiseContrastDTO  dto=new MerchandiseContrastDTO ();
    	dto.setId(id);
        return mapper.getDTODetails(dto);
    }

    @Override
    public MerchandiseContrastModel isExistModel(MerchandiseContrastModel model) throws SQLException {
        return mapper.isExistModel(model);
    }

    @Override
    public List<Map<String, Object>> listForExport(MerchandiseContrastDTO dto) throws SQLException {
        return mapper.listForExport(dto);
    }
    /**查询sku对应的经销货号*/
    @Override
    public List<MerchandiseContrastDTO> listContrastAndItem(MerchandiseContrastModel model){
        return mapper.listContrastAndItem(model);
    }


}
