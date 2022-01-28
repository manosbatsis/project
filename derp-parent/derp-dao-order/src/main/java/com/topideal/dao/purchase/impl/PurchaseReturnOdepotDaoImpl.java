package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.PurchaseReturnOdepotDao;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotExportDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotModel;
import com.topideal.mapper.purchase.PurchaseReturnOdepotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PurchaseReturnOdepotDaoImpl implements PurchaseReturnOdepotDao {

    @Autowired
    private PurchaseReturnOdepotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseReturnOdepotModel> list(PurchaseReturnOdepotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseReturnOdepotModel model) throws SQLException {
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
    public int modify(PurchaseReturnOdepotModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseReturnOdepotModel  searchByPage(PurchaseReturnOdepotModel  model) throws SQLException{
        PageDataModel<PurchaseReturnOdepotModel> pageModel=mapper.listByPage(model);
        return (PurchaseReturnOdepotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseReturnOdepotModel  searchById(Long id)throws SQLException {
        PurchaseReturnOdepotModel  model=new PurchaseReturnOdepotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseReturnOdepotModel searchByModel(PurchaseReturnOdepotModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public PurchaseReturnOdepotDTO getListByPage(PurchaseReturnOdepotDTO dto) {
		PageDataModel<PurchaseReturnOdepotDTO> pageModel=mapper.getListByPage(dto);
        return (PurchaseReturnOdepotDTO ) pageModel.getModel();
	}
	@Override
	public PurchaseReturnOdepotDTO getDTOById(Long id) {
		return mapper.getDTOById(id);
	}
	@Override
	public List<PurchaseReturnOdepotExportDTO> getDetailsByExport(PurchaseReturnOdepotDTO dto) {
		return mapper.getDetailsByExport(dto);
	}

}