package com.topideal.dao.transfer.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.transfer.TransferOrderDao;
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.entity.dto.transfer.TransferOrderVo;
import com.topideal.entity.dto.transfer.TransferOutInBean;
import com.topideal.entity.vo.transfer.TransferOrderModel;
import com.topideal.mapper.transfer.TransferOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 调拨订单 impl
 * @author lchenxing
 */
@Repository
public class TransferOrderDaoImpl implements TransferOrderDao {

    @Autowired
    private TransferOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TransferOrderModel> list(TransferOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param TransferOrderModel
	 */
    @Override
    public Long save(TransferOrderModel model) throws SQLException {
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
    public int modify(TransferOrderModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());//获取当前系统时间
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param TransferOrderModel
     */
    @Override
    public TransferOrderModel  searchByPage(TransferOrderModel  model) throws SQLException{
        PageDataModel<TransferOrderModel> pageModel=mapper.getListByPage(model);
        return (TransferOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public TransferOrderModel  searchById(Long id)throws SQLException {
        TransferOrderModel  model=new TransferOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
            /**
     	* 根据商家实体类查询商品
     	* @param MerchandiseInfoModel
     	* */
	@Override
	public TransferOrderModel searchByModel(TransferOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 模糊查询
	 */
	@Override
	public TransferOrderDTO getById(Long id) throws SQLException {
		return mapper.getById(id);
	}
	
    /**
     * 条件过滤查询，会查询出表体、商品
     * @param transferOrder
     * @return
     */
	@Override
	public TransferOrderModel getDetails(TransferOrderModel model) {
		return mapper.getDetails(model);
	}
	/**
	 * 将调拨实体类映射成Vo实体类
	 * @param id
	 * @param merchantId
	 * @return
	 */
	@Override
	public TransferOrderVo searchVoById(Long id, Long merchantId) {
		TransferOrderModel model = new TransferOrderModel();
		model.setId(id);
		model.setMerchantId(merchantId);
		return mapper.getVo(model);
	}
	/**
	 * 调入调出流水列表（分页）
	 * 
	 * @param model
	 * @return
	 */
	public TransferOutInBean listTransferOutInPage(TransferOutInBean model){
		 
		PageDataModel<TransferOutInBean> pageModel=mapper.listTransferOutInByPage(model);
	        return (TransferOutInBean ) pageModel.getModel();
//	        return mapper.listTransferOutInByPage(model);
	}
	public TransferOrderModel getByMap(Map<String, Object> map){
		return mapper.getByMap(map);
	}
	 /**
     * 根据调拨单id统计调出商品数量
     * */
    public List<Map<String, Object>> getItemSumByIds(List<Long> Ids){
    	return mapper.getItemSumByIds(Ids);
    }

	@Override
	public TransferOrderDTO listDtoByPage(TransferOrderDTO dto) throws SQLException {
		PageDataModel<TransferOrderDTO> pageModel=mapper.getDtoListByPage(dto);
		return (TransferOrderDTO ) pageModel.getModel();
	}

	@Override
	public Integer getTransferInNumByMap(Map<String, Object> map) {
		return mapper.getTransferInNumByMap(map);
	}

	@Override
	public Integer getTransferOutNumByMap(Map<String, Object> map) {
		return mapper.getTransferOutNumByMap(map);
	}

	@Override
	public Long countForExport(TransferOrderDTO dto) {
		return mapper.countForExport(dto);
	}

	@Override
	public List<Map<String, Object>> listForExport(TransferOrderDTO dto) {
		return mapper.listForExport(dto);
	}

	@Override
	public List<Map<String, Object>> listForExportItem(TransferOrderDTO dto) {
		return mapper.listForExportItem(dto);
	}

	@Override
	public List<TransferOrderDTO> listDTObyMap(Map<String, Object> param) {
		return mapper.listDTObyMap(param);
	}

	@Override
	public int modifyWithNull(TransferOrderModel model) throws SQLException {
		return mapper.modifyWithNull(model);
	}
}
