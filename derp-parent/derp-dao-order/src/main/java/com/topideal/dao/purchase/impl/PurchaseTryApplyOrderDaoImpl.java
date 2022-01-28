package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.PurchaseTryApplyOrderDao;
import com.topideal.entity.dto.purchase.PurchaseFrameContractDTO;
import com.topideal.entity.dto.purchase.PurchaseTryApplyOrderDTO;
import com.topideal.entity.vo.purchase.PurchaseTryApplyOrderModel;
import com.topideal.mapper.purchase.PurchaseTryApplyOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PurchaseTryApplyOrderDaoImpl implements PurchaseTryApplyOrderDao {

    @Autowired
    private PurchaseTryApplyOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseTryApplyOrderModel> list(PurchaseTryApplyOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseTryApplyOrderModel model) throws SQLException {
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
    public int modify(PurchaseTryApplyOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseTryApplyOrderModel  searchByPage(PurchaseTryApplyOrderModel  model) throws SQLException{
        PageDataModel<PurchaseTryApplyOrderModel> pageModel=mapper.listByPage(model);
        return (PurchaseTryApplyOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseTryApplyOrderModel  searchById(Long id)throws SQLException {
        PurchaseTryApplyOrderModel  model=new PurchaseTryApplyOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseTryApplyOrderModel searchByModel(PurchaseTryApplyOrderModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public int batchSave(List<PurchaseTryApplyOrderModel> addList) {
        return mapper.batchSave(addList);
    }

    @Override
    public PurchaseTryApplyOrderDTO listDTOByPage(PurchaseTryApplyOrderDTO dto) {
        PageDataModel<PurchaseTryApplyOrderDTO> pageModel=mapper.listDTOByPage(dto);
        return (PurchaseTryApplyOrderDTO) pageModel.getModel();
    }

    @Override
    public PurchaseTryApplyOrderDTO getDetail(PurchaseTryApplyOrderDTO dto) {
        PurchaseTryApplyOrderDTO responseDTO = mapper.getDetail(dto);
        return responseDTO;
    }

    @Override
    public List<PurchaseTryApplyOrderDTO> listOaBillCodeSelect(PurchaseTryApplyOrderDTO dto) {
        return mapper.listOaBillCodeSelect(dto);
    }

    @Override
    public int countByDTO(PurchaseTryApplyOrderDTO dto) {
        return mapper.countByDTO(dto);
    }

    @Override
    public List<PurchaseFrameContractDTO> listForExport(PurchaseTryApplyOrderDTO dto) {
        return mapper.listForExport(dto);
    }

    @Override
    public PurchaseTryApplyOrderDTO getLatestDTO(PurchaseTryApplyOrderDTO dto) {
        return mapper.getLatestDTO(dto);
    }
}