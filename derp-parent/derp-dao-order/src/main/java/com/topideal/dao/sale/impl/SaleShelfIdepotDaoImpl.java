package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.SaleShelfIdepotDao;
import com.topideal.entity.dto.sale.SaleShelfIdepotDTO;
import com.topideal.entity.vo.sale.SaleShelfIdepotModel;
import com.topideal.mapper.sale.SaleShelfIdepotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SaleShelfIdepotDaoImpl implements SaleShelfIdepotDao {

    @Autowired
    private SaleShelfIdepotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleShelfIdepotModel> list(SaleShelfIdepotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleShelfIdepotModel model) throws SQLException {
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
    public int modify(SaleShelfIdepotModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleShelfIdepotModel  searchByPage(SaleShelfIdepotModel  model) throws SQLException{
        PageDataModel<SaleShelfIdepotModel> pageModel=mapper.listByPage(model);
        return (SaleShelfIdepotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleShelfIdepotModel  searchById(Long id)throws SQLException {
        SaleShelfIdepotModel  model=new SaleShelfIdepotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleShelfIdepotModel searchByModel(SaleShelfIdepotModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public SaleShelfIdepotDTO getListByPage(SaleShelfIdepotDTO dto) {
		PageDataModel<SaleShelfIdepotDTO> pageModel=mapper.getListByPage(dto);
        return (SaleShelfIdepotDTO ) pageModel.getModel();
	}
	@Override
	public SaleShelfIdepotDTO searchDTOById(Long id) {
		return mapper.searchDTOById(id);
	}
	@Override
	public Integer getOrderCount(SaleShelfIdepotDTO dto) {
		return mapper.getOrderCount(dto);
	}
	@Override
	public List<Map<String, Object>> getExportList(SaleShelfIdepotDTO dto) {
		return mapper.getExportList(dto);
	}
	@Override
	public List<SaleShelfIdepotDTO> listDTO(SaleShelfIdepotDTO dto) {
		return mapper.listDTO(dto);
	}

}