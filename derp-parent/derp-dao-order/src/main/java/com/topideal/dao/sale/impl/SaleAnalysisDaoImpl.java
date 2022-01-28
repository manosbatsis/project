package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.SaleAnalysisDao;
import com.topideal.entity.dto.sale.SaleAnalysisDTO;
import com.topideal.entity.vo.sale.SaleAnalysisModel;
import com.topideal.mapper.sale.SaleAnalysisMapper;

/**
 * 销售出库分析表 impl
 * @author lchenxing
 */
@Repository
public class SaleAnalysisDaoImpl implements SaleAnalysisDao {

    @Autowired
    private SaleAnalysisMapper mapper;  //销售出库分析表
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleAnalysisModel> list(SaleAnalysisModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param SaleAnalysisModel
	 */
    @Override
    public Long save(SaleAnalysisModel model) throws SQLException {
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
    public int modify(SaleAnalysisModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param SaleAnalysisModel
     */
    @Override
    public SaleAnalysisModel  searchByPage(SaleAnalysisModel  model) throws SQLException{
        PageDataModel<SaleAnalysisModel> pageModel=mapper.listByPage(model);
        return (SaleAnalysisModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public SaleAnalysisModel  searchById(Long id)throws SQLException {
        SaleAnalysisModel  model=new SaleAnalysisModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
	    /**
	* 根据商家实体类查询商品
	* @param MerchandiseInfoModel
	* */
	@Override
	public SaleAnalysisModel searchByModel(SaleAnalysisModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 根据条件查询（分页）
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public SaleAnalysisDTO queryDTOListByPage(SaleAnalysisDTO dto) throws SQLException {
		PageDataModel<SaleAnalysisDTO> pageModel=mapper.queryDTOListByPage(dto);
        return (SaleAnalysisDTO ) pageModel.getModel();
	}
}
