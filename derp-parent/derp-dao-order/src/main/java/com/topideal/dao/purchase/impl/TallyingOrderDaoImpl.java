package com.topideal.dao.purchase.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.purchase.TallyingOrderDTO;
import com.topideal.entity.dto.sale.PendingConfirmTallyingOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.TallyingOrderDao;
import com.topideal.entity.vo.purchase.TallyingOrderModel;
import com.topideal.mapper.purchase.TallyingOrderMapper;

/**
 * 理货单 impl.
 * 
 * @author lchenxing
 */
@Repository
public class TallyingOrderDaoImpl implements TallyingOrderDao {

	@Autowired
	private TallyingOrderMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<TallyingOrderModel> list(TallyingOrderModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param TallyingOrderDTO
	 */
	@Override
	public Long save(TallyingOrderModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 * 
	 * @param List
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * 
	 * @param List
	 */
	@Override
	public int modify(TallyingOrderModel model) throws SQLException {
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param TallyingOrderDTO
	 */
	@Override
	public TallyingOrderModel searchByPage(TallyingOrderModel model) throws SQLException {
		PageDataModel<TallyingOrderModel> pageModel = mapper.listByPage(model);
		return (TallyingOrderModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public TallyingOrderModel searchById(Long id) throws SQLException {
		TallyingOrderModel model = new TallyingOrderModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public TallyingOrderModel searchByModel(TallyingOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 根据id修改理货单状态（批量）
	 * @param ids  多个id以 “,”隔开
	 * @param state  状态   2：确认  3：驳回
	 * @return
	 */
	@Override
	public int modifyOrderState(String ids, String state) {
		return mapper.modifyOrderState(ids,state);
	}

	/**
     * 查询出表体,理货单对应的商品和批次信息
     * @return model
     */
	@Override
	public TallyingOrderDTO getDetails(TallyingOrderDTO dto) {
		return mapper.getDetails(dto);
	}

	/**
	 * 采购模块-理货单分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public TallyingOrderDTO getListByPage(TallyingOrderDTO dto) throws SQLException {
		PageDataModel<TallyingOrderDTO> pageModel = mapper.getListByPage(dto);
		return (TallyingOrderDTO) pageModel.getModel();
	}

	@Override
	public List<PendingConfirmTallyingOrderVo> getPendingConfirmOrders(Map<String, Object> map) throws SQLException {
		return mapper.getPendingConfirmOrders(map);
	}

	@Override
	public Integer countPendingConfirmOrders(Map<String, Object> map) throws SQLException {
		return mapper.countPendingConfirmOrders(map);
	}

	@Override
	public List<TallyingOrderDTO> getListDetails(TallyingOrderDTO dto) {
		return mapper.getListDetails(dto);
	}

	@Override
	public TallyingOrderDTO getDTODetails(TallyingOrderDTO dto) {
		return mapper.getDTODetails(dto);
	}

	@Override
	public TallyingOrderDTO getDTOListByPage(TallyingOrderDTO dto) throws SQLException {
		PageDataModel<TallyingOrderDTO> pageModel = mapper.listDTOByPage(dto);
		return (TallyingOrderDTO) pageModel.getModel();
	}

}
