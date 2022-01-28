package com.topideal.dao.impl;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.InventorySummaryDao;
import com.topideal.entity.dto.InventorySummaryDTO;
import com.topideal.entity.vo.InventorySummaryModel;
import com.topideal.mapper.InventorySummaryMapper;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;

/**
 * 商品收发汇总 impl
 * 
 * @author lchenxing
 */
@Repository
public class InventorySummaryDaoImpl implements InventorySummaryDao {

	@Autowired
	private InventorySummaryMapper mapper; // 商品收发汇总
	//商品信息
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<InventorySummaryModel> list(InventorySummaryModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param InventorySummaryModel
	 */
	@Override
	public Long save(InventorySummaryModel model) throws SQLException {
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
	public int modify(InventorySummaryModel model) throws SQLException {
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param InventorySummaryModel
	 */
	@Override
	public InventorySummaryModel searchByPage(InventorySummaryModel model) throws SQLException {
		PageDataModel<InventorySummaryModel> pageModel = mapper.listByPage(model);
		return (InventorySummaryModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public InventorySummaryModel searchById(Long id) throws SQLException {
		InventorySummaryModel model = new InventorySummaryModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public InventorySummaryModel searchByModel(InventorySummaryModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 商品收發匯總
	 */
	@Override
	public InventorySummaryDTO listInventorySummaryByPage(InventorySummaryDTO inventorySummaryModel)
			throws Exception {
		// TODO Auto-generated method stub
		
		List<InventorySummaryDTO> listPage = mapper.listInventorySummaryPage(inventorySummaryModel);
		int count = mapper.listInventorySummaryCount(inventorySummaryModel);
		inventorySummaryModel.setList(listPage);
		inventorySummaryModel.setTotal(count);
		return inventorySummaryModel;
	}

	/**
	 *  导出商品收发汇总统计
	 * @param inventorySummaryModel
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<InventorySummaryDTO> exportInventorySummary(InventorySummaryModel inventorySummaryModel)
			throws Exception {
		// TODO Auto-generated method stub
		return mapper.exportInventorySummary(inventorySummaryModel);
	}
	/**
	 * 根据商家、商品货号、仓库、当前月获取本月期初库存(月结)
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	@Override
	public List<InventorySummaryModel> getOpeningInventoryNum(Long merchantId, Long depotId, String goodsNo,
			String currentMonth,String unit) {
		return mapper.getOpeningInventoryNum(merchantId,depotId,goodsNo,currentMonth,unit);
	}
	/**
	 * 根据商家、商品货号、仓库、当前月获取本月期初库存(期初)
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	@Override
	public List<InventorySummaryModel> getInitInventoryNum(Long merchantId, Long depotId, String goodsNo,String unit) {
		return mapper.getInitInventoryNum(merchantId,depotId,goodsNo,unit);
	}
	/**
	 * 获取本月累计入库数量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	@Override
	public List<InventorySummaryModel> getInDepotTotal(Long merchantId, Long depotId, String goodsNo,
			String currentMonth,String unit) {
		return mapper.getInDepotTotal(merchantId,depotId,goodsNo,currentMonth,unit);
	}
	/**
	 * 获取本月累计出库数量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	@Override
	public List<InventorySummaryModel> getOutDepotTotal(Long merchantId, Long depotId, String goodsNo,
			String currentMonth,String unit) {
		return mapper.getOutDepotTotal(merchantId,depotId,goodsNo,currentMonth,unit);
	}
	/**
	 * 获取电商在途量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	@Override
	public List<InventorySummaryModel> getEOnwayNum(Long merchantId, Long depotId, String goodsNo,
			String currentMonth,String unit) {
		return mapper.getEOnwayNum(merchantId,depotId,goodsNo,currentMonth,unit);
	}
	/**
	 * 获取调拨出库在途量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	@Override
	public List<InventorySummaryModel> getTransferOutNum(Long merchantId, Long depotId, String goodsNo,
			String currentMonth,String unit) {
		return mapper.getTransferOutNum(merchantId,depotId,goodsNo,currentMonth,unit);
	}
	/**
	 * 获取销售在途量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	@Override
	public List<InventorySummaryModel> getSaleOnwayNum(Long merchantId, Long depotId, String goodsNo,
			String currentMonth,String unit) {
		return mapper.getSaleOnwayNum(merchantId,depotId,goodsNo,currentMonth,unit);
	}
	/**
	 * 120天的均销量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getAverageNum(Long merchantId, Long depotId, String goodsNo,String unit) {
		return mapper.getAverageNum(merchantId,depotId,goodsNo,unit);
	}

}
