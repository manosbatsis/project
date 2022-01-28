package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SaleReturnIdepotDao;
import com.topideal.entity.dto.sale.SaleReturnIdepotDTO;
import com.topideal.entity.vo.sale.SaleReturnIdepotModel;
import com.topideal.mapper.sale.SaleReturnIdepotMapper;

/**
 * 销售退货入库
 * @author lchenxing
 */
@Repository
public class SaleReturnIdepotDaoImpl implements SaleReturnIdepotDao {

    @Autowired
    private SaleReturnIdepotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleReturnIdepotModel> list(SaleReturnIdepotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param SaleReturnIdepotModel
	 */
    @Override
    public Long save(SaleReturnIdepotModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
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
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param List
     */
    @Override
    public int modify(SaleReturnIdepotModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param SaleReturnIdepotModel
     */
    @Override
    public SaleReturnIdepotModel  searchByPage(SaleReturnIdepotModel  model) throws SQLException{
        PageDataModel<SaleReturnIdepotModel> pageModel=mapper.listByPage(model);
        return (SaleReturnIdepotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public SaleReturnIdepotModel  searchById(Long id)throws SQLException {
        SaleReturnIdepotModel  model=new SaleReturnIdepotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
            /**
     	* 根据商家实体类查询商品
     	* @param MerchandiseInfoModel
     	* */
	@Override
	public SaleReturnIdepotModel searchByModel(SaleReturnIdepotModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public SaleReturnIdepotDTO searchDTOByPage(SaleReturnIdepotDTO dto) throws SQLException {
		 PageDataModel<SaleReturnIdepotDTO> pageModel=mapper.listDTOByPage(dto);
	     return (SaleReturnIdepotDTO ) pageModel.getModel();
	}
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public SaleReturnIdepotDTO  searchDTOById(Long id)throws SQLException {
    	SaleReturnIdepotDTO  dto=new SaleReturnIdepotDTO ();
    	dto.setId(id);
        return mapper.getDTOById(dto);
    }
	@Override
	public List<SaleReturnIdepotDTO> listSaleReturnIdepotDTO(SaleReturnIdepotDTO dto) throws SQLException {
		return mapper.listSaleReturnIdepot(dto);
	}
}
