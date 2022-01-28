package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.PurchaseFrameContractDao;
import com.topideal.entity.dto.purchase.PurchaseFrameContractDTO;
import com.topideal.entity.vo.purchase.PurchaseFrameContractModel;
import com.topideal.mapper.purchase.PurchaseFrameContractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PurchaseFrameContractDaoImpl implements PurchaseFrameContractDao {

    @Autowired
    private PurchaseFrameContractMapper mapper;
	
    @Override
	public List<PurchaseFrameContractDTO> listFrameContracSelect(PurchaseFrameContractDTO dto) throws SQLException {
		return mapper.listFrameContracSelect(dto);
	}
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseFrameContractModel> list(PurchaseFrameContractModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseFrameContractModel model) throws SQLException {
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
    public int modify(PurchaseFrameContractModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseFrameContractModel  searchByPage(PurchaseFrameContractModel  model) throws SQLException{
        PageDataModel<PurchaseFrameContractModel> pageModel=mapper.listByPage(model);
        return (PurchaseFrameContractModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseFrameContractModel  searchById(Long id)throws SQLException {
        PurchaseFrameContractModel  model=new PurchaseFrameContractModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseFrameContractModel searchByModel(PurchaseFrameContractModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public int batchSave(List<PurchaseFrameContractModel> addList) {
        return mapper.batchSave(addList);
    }

    @Override
    public PurchaseFrameContractDTO listDTOByPage(PurchaseFrameContractDTO dto) {
        PageDataModel<PurchaseFrameContractDTO> pageModel = mapper.listDTOByPage(dto);
        return (PurchaseFrameContractDTO ) pageModel.getModel();
    }

    @Override
    public PurchaseFrameContractDTO getDetail(PurchaseFrameContractDTO dto) {
        return mapper.getDetail(dto);
    }

    @Override
    public int countByDTO(PurchaseFrameContractDTO dto) {
        return mapper.countByDTO(dto);
    }

    @Override
    public List<PurchaseFrameContractDTO> listForExport(PurchaseFrameContractDTO dto) {
        return mapper.listForExport(dto);
    }

    @Override
    public PurchaseFrameContractDTO getLatestDTO(PurchaseFrameContractDTO dto) {
        return mapper.getLatestDTO(dto);
    }

}