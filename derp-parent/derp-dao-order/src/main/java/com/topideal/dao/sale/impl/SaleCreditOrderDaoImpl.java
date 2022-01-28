package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.SaleCreditOrderDao;
import com.topideal.entity.dto.sale.SaleCreditOrderDTO;
import com.topideal.entity.vo.sale.SaleCreditOrderModel;
import com.topideal.mapper.sale.SaleCreditOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SaleCreditOrderDaoImpl implements SaleCreditOrderDao {

    @Autowired
    private SaleCreditOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleCreditOrderModel> list(SaleCreditOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleCreditOrderModel model) throws SQLException {
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
    public int modify(SaleCreditOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleCreditOrderModel  searchByPage(SaleCreditOrderModel  model) throws SQLException{
        PageDataModel<SaleCreditOrderModel> pageModel=mapper.listByPage(model);
        return (SaleCreditOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleCreditOrderModel  searchById(Long id)throws SQLException {
        SaleCreditOrderModel  model=new SaleCreditOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleCreditOrderModel searchByModel(SaleCreditOrderModel model) throws SQLException {
		return mapper.get(model);
	}
    
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}
	@Override
	public SaleCreditOrderDTO listDTOByPage(SaleCreditOrderDTO dto) throws SQLException {
		PageDataModel<SaleCreditOrderDTO> list = mapper.listDTOByPage(dto);
		return (SaleCreditOrderDTO) list.getModel();
	}
	@Override
	public List<Map<String, Object>> getCreditTypeNum(SaleCreditOrderDTO dto) throws SQLException {
		return mapper.getCreditTypeNum(dto);
	}
	@Override
	public SaleCreditOrderDTO searchDetail(SaleCreditOrderDTO dto) throws SQLException {
		return mapper.searchDetail(dto);
	}

	@Override
	public void batchUpdate(List<SaleCreditOrderModel> saleCreditModels) {
		mapper.batchUpdate(saleCreditModels);
	}

	@Override
	public List<SaleCreditOrderModel> listByIds(List<Long> ids) {
		return mapper.listByIds(ids);
	}
}