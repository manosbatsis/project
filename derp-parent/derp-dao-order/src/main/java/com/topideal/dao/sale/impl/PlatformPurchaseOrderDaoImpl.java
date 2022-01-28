package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.PlatformPurchaseOrderDao;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderDTO;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderExportDTO;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderModel;
import com.topideal.mapper.sale.PlatformPurchaseOrderMapper;
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
public class PlatformPurchaseOrderDaoImpl implements PlatformPurchaseOrderDao {

    @Autowired
    private PlatformPurchaseOrderMapper mapper;

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PlatformPurchaseOrderModel> list(PlatformPurchaseOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PlatformPurchaseOrderModel model) throws SQLException {
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
    public int modify(PlatformPurchaseOrderModel  model) throws SQLException {
        return mapper.update(model);
    }

	/**
     * 分页查询
     * @param model
     */
    @Override
    public PlatformPurchaseOrderModel  searchByPage(PlatformPurchaseOrderModel  model) throws SQLException{
        PageDataModel<PlatformPurchaseOrderModel> pageModel=mapper.listByPage(model);
        return (PlatformPurchaseOrderModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PlatformPurchaseOrderModel  searchById(Long id)throws SQLException {
        PlatformPurchaseOrderModel  model=new PlatformPurchaseOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }

      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PlatformPurchaseOrderModel searchByModel(PlatformPurchaseOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public PlatformPurchaseOrderDTO getListByPage(PlatformPurchaseOrderDTO dto) {
		PageDataModel<PlatformPurchaseOrderDTO> pageModel=mapper.getListByPage(dto);
        return (PlatformPurchaseOrderDTO ) pageModel.getModel();
	}
	@Override
	public int modifyWherSubmit(PlatformPurchaseOrderModel updateOrder) {
		return mapper.updateWhenSubmit(updateOrder);
	}
	@Override
	public PlatformPurchaseOrderDTO searchDTOById(Long id) {
		PlatformPurchaseOrderDTO dto = mapper.searchDTOById(id) ;

		return dto ;
	}
	@Override
	public List<PlatformPurchaseOrderExportDTO> getExportList(PlatformPurchaseOrderExportDTO dto) {
		return mapper.getExportList(dto);
	}

	@Override
	public List<PlatformPurchaseOrderDTO> listPlatformPurchaseOrderByCodeAndPoNo(PlatformPurchaseOrderDTO dto) {
		return mapper.listPlatformPurchaseOrderByCodeAndPoNo(dto);
	}


	/**
	 * 分页查询近两个月的平台采购单(平台采购单未上架信息发送消息)
	 */
	@Override
	public List<PlatformPurchaseOrderModel> getPurchaseOrderMonth(Map<String, Object> map)throws SQLException {
		return mapper.getPurchaseOrderMonth(map);
	}

	@Override
	public List<PlatformPurchaseOrderDTO> listDTO(PlatformPurchaseOrderDTO dto) {
		return mapper.listDTO(dto);
	}


}
