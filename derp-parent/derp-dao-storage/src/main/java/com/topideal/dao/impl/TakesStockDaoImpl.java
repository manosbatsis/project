package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.TakesStockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.TakesStockDao;
import com.topideal.entity.vo.TakesStockModel;
import com.topideal.mapper.TakesStockMapper;

/**盘点指令表
 * @author lchenxing
 */
@Repository
public class TakesStockDaoImpl implements TakesStockDao {

    @Autowired
    private TakesStockMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TakesStockModel> list(TakesStockModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TakesStockModel model) throws SQLException {
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
    public int modify(TakesStockModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TakesStockModel  searchByPage(TakesStockModel  model) throws SQLException{
        PageDataModel<TakesStockModel> pageModel=mapper.listByPage(model);
        return (TakesStockModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TakesStockModel  searchById(Long id)throws SQLException {
        TakesStockModel  model=new TakesStockModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TakesStockModel searchByModel(TakesStockModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public TakesStockDTO searchDTOByPage(TakesStockDTO dto) throws SQLException {
        PageDataModel<TakesStockDTO> pageModel = mapper.listDTOByPage(dto);
        return (TakesStockDTO) pageModel.getModel();
    }

    @Override
    public TakesStockDTO getDtoDetail(Long id) throws SQLException {
        return mapper.getDetail(id);
    }
}
