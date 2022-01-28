package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.SupplierInquiryPoolDao;
import com.topideal.entity.dto.main.SupplierInquiryPoolDTO;
import com.topideal.entity.vo.main.SupplierInquiryPoolModel;
import com.topideal.mapper.main.SupplierInquiryPoolMapper;

/**
 *供应商询价池 impl
 * @author lchenxing
 */
@Repository
public class SupplierInquiryPoolDaoImpl implements SupplierInquiryPoolDao {

    @Autowired
    private SupplierInquiryPoolMapper mapper;  //供应商询价池
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SupplierInquiryPoolModel> list(SupplierInquiryPoolModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param SupplierInquiryPoolDTO
	 */
    @Override
    public Long save(SupplierInquiryPoolModel model) throws SQLException {
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
    public int modify(SupplierInquiryPoolModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param SupplierInquiryPoolDTO
     */
    @Override
    public SupplierInquiryPoolModel  searchByPage(SupplierInquiryPoolModel  model) throws SQLException{
        PageDataModel<SupplierInquiryPoolModel> pageModel=mapper.listByPage(model);
        return (SupplierInquiryPoolModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public SupplierInquiryPoolModel  searchById(Long id)throws SQLException {
        SupplierInquiryPoolModel  model=new SupplierInquiryPoolModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
            /**
     	* 根据商家实体类查询商品
     	* @param MerchandiseInfoModel
     	* */
	@Override
	public SupplierInquiryPoolModel searchByModel(SupplierInquiryPoolModel model) throws SQLException {
		return mapper.get(model);
	}
	
	
	@Override
	public SupplierInquiryPoolDTO getlistByPage(SupplierInquiryPoolDTO dto) throws SQLException {
		PageDataModel<SupplierInquiryPoolDTO> pageModel = mapper.getlistByPage(dto);
		return (SupplierInquiryPoolDTO) pageModel.getModel();
	}
	
	/**
	 * 详情
	 */
	@Override
	public SupplierInquiryPoolDTO getDetails(Long id) throws SQLException {
		return mapper.getDetails(id);
	}
	
	/**
	 *  导出供应商询价池
	 */
	@Override
	public List<SupplierInquiryPoolModel> exportList(SupplierInquiryPoolModel model) throws SQLException {
		return mapper.exportSupplierInquiry(model);
	}
    
}
