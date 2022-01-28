package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryDetailsDao;
import com.topideal.entity.dto.InventoryDetailsDTO;
import com.topideal.entity.vo.InventoryDetailsModel;
import com.topideal.mapper.InventoryDetailsMapper;

/**
 * 库存收发明细 impl
 * 
 * @author lchenxing
 */
@Repository
public class InventoryDetailsDaoImpl implements InventoryDetailsDao {

	@Autowired
	private InventoryDetailsMapper mapper; // 库存收发明细

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<InventoryDetailsModel> list(InventoryDetailsModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param InventoryDetailsModel
	 */
	@Override
	public Long save(InventoryDetailsModel model) throws SQLException {
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
	public int modify(InventoryDetailsModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param InventoryDetailsModel
	 */
	@Override
	public InventoryDetailsModel searchByPage(InventoryDetailsModel model) throws SQLException {
		PageDataModel<InventoryDetailsModel> pageModel = mapper.listByPage(model);
		return (InventoryDetailsModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public InventoryDetailsModel searchById(Long id) throws SQLException {
		InventoryDetailsModel model = new InventoryDetailsModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public InventoryDetailsModel searchByModel(InventoryDetailsModel model) throws SQLException {
		return mapper.get(model);
	}

	
	  /**
     * 导出商品收发明细
     * @param id
     * @return
     * @throws Exception
     */
	@Override
	public List<Map<String, Object>> exportInventoryDetailsMap(InventoryDetailsDTO model ) throws Exception {
		// TODO Auto-generated method stub
		return mapper.exportInventoryDetailsMap(model);
	}

	/**
	 *  分页查询商品收发明细
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public InventoryDetailsDTO getlistByPage(InventoryDetailsDTO model) throws SQLException {
		// TODO Auto-generated method stub
		PageDataModel<InventoryDetailsDTO>  pageModel=	mapper.getlistByPage(model);
		return (InventoryDetailsDTO)pageModel.getModel();
	}

	/**
	 * 根据货号、订单号统计收发明细
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> countDetailsByOrderNo(InventoryDetailsModel model) throws SQLException {
		return mapper.countDetailsByOrderNo(model);
	}
	/**
	 * 根据条件获取收发明细
	 * @param merchantId  商家id
	 * @param depotId  仓库id
	 * @param divergenceMonth  归属月份
	 * @param operateType  操作类型
	 * @return
	 */
	@Override
	public List<InventoryDetailsModel> getDetailsByParams(Long merchantId, Long depotId, String divergenceMonth,
			String operateType) {
		return mapper.getDetailsByParams(merchantId, depotId, divergenceMonth, operateType);
	}
	
	/**
	 *  获取上个月的 所有加减明细商家和仓库
	 */
	@Override
	public List<InventoryDetailsModel> getAllDetailMerchantOrDepotByTime(String lastMonth)throws Exception {		
		return mapper.getAllDetailMerchantOrDepotByTime(lastMonth);
	}
	
	/**
	 * 获取本月 商家 仓库 事业部 的 加的明细 
	 */
	@Override
	public List<Map<String, Object>> getMonthBuAddOrSubInventory(Map<String, Object> map) throws SQLException {
		return mapper.getMonthBuAddOrSubInventory(map);
	}
	/**
	 * 迁移数据到历史表
	 * */
	@Override
	public int synsMoveToHistory(Map<String,Object> map){
		return mapper.synsMoveToHistory(map);
	}
	/**
	 * 删除已迁移到历史表的数据
	 * */
	@Override
	public int delMoveToHistory(Map<String,Object> map){
		return mapper.delMoveToHistory(map);
	}
}
