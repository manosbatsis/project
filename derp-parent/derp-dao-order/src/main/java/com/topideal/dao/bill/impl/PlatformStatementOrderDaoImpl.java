package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.PlatformStatementOrderDao;
import com.topideal.entity.dto.bill.PlatformStatementOrderDTO;
import com.topideal.entity.dto.bill.PlatformStatementOrderExportDTO;
import com.topideal.entity.vo.bill.PlatformStatementOrderModel;
import com.topideal.mapper.bill.PlatformStatementOrderMapper;
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
public class PlatformStatementOrderDaoImpl implements PlatformStatementOrderDao {

    @Autowired
    private PlatformStatementOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PlatformStatementOrderModel> list(PlatformStatementOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PlatformStatementOrderModel model) throws SQLException {
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
    public int modify(PlatformStatementOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PlatformStatementOrderModel  searchByPage(PlatformStatementOrderModel  model) throws SQLException{
        PageDataModel<PlatformStatementOrderModel> pageModel=mapper.listByPage(model);
        return (PlatformStatementOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PlatformStatementOrderModel  searchById(Long id)throws SQLException {
        PlatformStatementOrderModel  model=new PlatformStatementOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PlatformStatementOrderModel searchByModel(PlatformStatementOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public PlatformStatementOrderDTO getListByPage(PlatformStatementOrderDTO dto) {
		PageDataModel<PlatformStatementOrderDTO> pageModel=mapper.getListByPage(dto);
        return (PlatformStatementOrderDTO ) pageModel.getModel();
	}
	@Override
	public PlatformStatementOrderDTO searchDTOById(Long id) {
		PlatformStatementOrderDTO  model=new PlatformStatementOrderDTO ();
        model.setId(id);
        return mapper.getDTO(model);
	}
	@Override
	public List<PlatformStatementOrderExportDTO> getExportOrders(PlatformStatementOrderExportDTO dto) {
		return mapper.getExportOrders(dto);
	}


    /**
     * 根据ids 查询平台结算单信息
     * @param ids
     * @return
     */
    @Override
    public List<PlatformStatementOrderDTO> listByIds(List<Long> ids) throws Exception {
        return mapper.listByIds(ids);
    }

    @Override
    public void batchUpdate(List<Long> ids, String billCode, String isCreateReceive) throws SQLException {
        mapper.batchUpdate(ids, billCode, isCreateReceive);
    }

    @Override
    public Integer countExportNum(PlatformStatementOrderExportDTO dto) {
        return mapper.countExportNum(dto);
    }

}