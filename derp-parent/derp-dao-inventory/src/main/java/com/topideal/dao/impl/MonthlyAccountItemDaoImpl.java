package com.topideal.dao.impl;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.MonthlyAccountItemDao;
import com.topideal.entity.dto.MonthlyAccountItemDTO;
import com.topideal.entity.vo.MonthlyAccountItemModel;
import com.topideal.mapper.MonthlyAccountItemMapper;

/**
 * 月结账单商品明细 impl
 * 
 * @author lchenxing
 */
@Repository
public class MonthlyAccountItemDaoImpl implements MonthlyAccountItemDao {

	@Autowired
	private MonthlyAccountItemMapper mapper; // 月结账单商品明细

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<MonthlyAccountItemModel> list(MonthlyAccountItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param MonthlyAccountItemModel
	 */
	@Override
	public Long save(MonthlyAccountItemModel model) throws SQLException {
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
	public int modify(MonthlyAccountItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param MonthlyAccountItemModel
	 */
	@Override
	public MonthlyAccountItemModel searchByPage(MonthlyAccountItemModel model) throws SQLException {
		PageDataModel<MonthlyAccountItemModel> pageModel = mapper.listByPage(model);
		return (MonthlyAccountItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public MonthlyAccountItemModel searchById(Long id) throws SQLException {
		MonthlyAccountItemModel model = new MonthlyAccountItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public MonthlyAccountItemModel searchByModel(MonthlyAccountItemModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public MonthlyAccountItemModel getOne(MonthlyAccountItemModel monthlyAccountItemModel) {
		return mapper.getOne(monthlyAccountItemModel);
	}
	

	/**
     * 根据库存扣减接口的商家仓库商品信息查询对应的月结详情信息
     * @param monthlyAccountItemModel
     * @return
     * @throws Exception
     */
	@Override
	public MonthlyAccountItemModel getMonthlyAccountItemListByMonth(MonthlyAccountItemModel monthlyAccountItemModel)
			throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getMonthlyAccountItemListByMonth(monthlyAccountItemModel);
	}

	

	/**
	 * 按时间排序
	 * @param monthlyAccountItemModel
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<MonthlyAccountItemModel> listOrbyOverdueDate(MonthlyAccountItemModel monthlyAccountItemModel)
			throws SQLException {
		// TODO Auto-generated method stub
		return mapper.listOrbyOverdueDate(monthlyAccountItemModel);
	}

	
	 /**
     * 更加主键id进行单个删除
     * @param id
     * @return
     * @throws Exception
     */
	@Override
	public int delMonthlyAccountItem(Long id) throws Exception {
		// TODO Auto-generated method stub
		return mapper.del(id);
	}
	
	/**
	 * 扣减商品库存量
	 * @param monthlyAccountItemModel
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int updateLowerMonthlyGoodsNum(MonthlyAccountItemModel monthlyAccountItemModel) throws SQLException{
     	return	mapper.updateLowerMonthlyGoodsNum(monthlyAccountItemModel);
	}
	
	/**
	 * 增加商品库存量
	 * @param monthlyAccountItemModel
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int updateAddMonthlyGoodsNum(MonthlyAccountItemModel monthlyAccountItemModel) throws SQLException{
		return mapper.updateAddMonthlyGoodsNum(monthlyAccountItemModel);
	}

	/**
	 * 更新实时库存量和商品条形码
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int updateAvailableNumOrBarcode(Map<String, Object> map) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.updateAvailableNumOrBarcode(map);
	}

	@Override
	public List<MonthlyAccountItemModel> getMonthlyAccountItemByCreateDate(
			Map<String,Object> map) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getMonthlyAccountItemByCreateDate(map);
	}

	@Override
	public MonthlyAccountItemModel selectMonthlyAccountItemModelMapByOne(MonthlyAccountItemModel monthlyAccountItemModel) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.selectMonthlyAccountItemModelMapByOne(monthlyAccountItemModel);
	}

	@Override
	public int updateAddMonthlySurplusNum(MonthlyAccountItemModel monthlyAccountItemModel) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.updateAddMonthlySurplusNum(monthlyAccountItemModel);
	}

	@Override
	public List<MonthlyAccountItemDTO> getMonthlyAccountItemModelMapList(
			MonthlyAccountItemDTO monthlyAccountItemModel) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getMonthlyAccountItemModelMapList(monthlyAccountItemModel);
	}

	@Override
	public int updateMonthlyAccountItemModelSettlementNum(MonthlyAccountItemModel monthlyAccountItemModel)
			throws SQLException {
		// TODO Auto-generated method stub
		return mapper.updateMonthlyAccountItemModelSettlementNum(monthlyAccountItemModel);
	}
	/**
     * 获取批次有差异的记录
     * @param model
     * @return
     */
	@Override
	public List<Map<String, Object>> getDifferenceList(MonthlyAccountItemModel model) {
		return mapper.getDifferenceList(model);
	}
	/**
     * 根据条件获取需要调增的记录
     * @param merchantId
     * @param depotId
     * @param goodsId
     * @param batchNo
     * @return
     */
	@Override
	public List<MonthlyAccountItemModel> getAddItemByParam(Long merchantId, Long depotId, Long goodsId, String batchNo, String unit,String type, String bv) {
		return mapper.getAddItemByParam(merchantId,depotId,goodsId, batchNo, unit,type,bv);
	}

	@Override
	public List<MonthlyAccountItemModel> getSubItemByParam(Long merchantId, Long depotId, Long goodsId,
			String batchNo, String unit,String type, String bv) {
		return mapper.getSubItemByParam(merchantId,depotId,goodsId, batchNo, unit,type,bv);
	}
	/**
     * 根据表头ID获取表体数据（结余数量大于0）
     * @param monthlyAccountId
     * @return
     */
	@Override
	public List<MonthlyAccountItemModel> getItemListById(Long monthlyAccountId) {
		return mapper.getItemListById(monthlyAccountId);
	}
	/**
     * 按商品分组统计月结详情数据
     * @param monthlyAccountItemModel
     * @return
     * @throws SQLException
     */
	@Override
	public List<MonthlyAccountItemDTO> getItemListGroupGoods(MonthlyAccountItemDTO model) throws SQLException {
		return mapper.getItemListGroupGoods(model);
	}
	/**
     * 按商品统计有差异的记录
     * @param model
     * @return
     */
	@Override
	public List<Map<String, Object>> getDifferenceGroupGoods(MonthlyAccountItemModel model) {
		return mapper.getDifferenceGroupGoods(model);
	}
	/**
     * 根据条件获取月结表体数据
     * @param merchantId 商家id
     * @param depotId 仓库id
     * @param goodsId 商品id
     * @param batchNo 批次
     * @param productionDate 生产日期
     * @param overdueDate 失效日期
     * @param unit 理货单位
     * @param stockType 库存类型（好、坏品）
     * @param monthlyAccountId 月结表头id
     * @return
     */
	@Override
	public MonthlyAccountItemModel getItemByInventoryRealTimeSnapshot(Long merchantId, Long depotId, Long goodsId, String batchNo,
			Date productionDate, Date overdueDate, String unit, String stockType, Long monthlyAccountId) {
		return mapper.getItemByInventoryRealTimeSnapshot(merchantId,depotId,goodsId,batchNo,productionDate,overdueDate,unit,stockType,monthlyAccountId);
	}
	/**
     * 根据月结id删除明细
     * @param monthlyAccountId
     */
	@Override
	public int delItemByMonthlyAccountId(Long monthlyAccountId) {
		return mapper.delItemByMonthlyAccountId(monthlyAccountId);
	}
	/**
     * 导出月结明细-强校验
     * @param model
     * @return
     */
	@Override
	public List<Map<String, Object>> exportMonthlyAccountItemModelMapList(MonthlyAccountItemModel monthlyAccountItemModel) {
		return mapper.exportMonthlyAccountItemModelMapList(monthlyAccountItemModel);
	}
	/**
     * 导出月结明细-弱校验
     * @param model
     * @return
     */
	@Override
	public List<Map<String, Object>> exportItemListGroupGoods(MonthlyAccountItemModel monthlyAccountItemModel) {
		return mapper.exportItemListGroupGoods(monthlyAccountItemModel);
	}

	/**
	 * 校验月结现存量和 库存余量是否相等
	 */
	@Override
	public List<MonthlyAccountItemModel> checkMonthlySurplusNum(MonthlyAccountItemModel model) throws SQLException {
		return mapper.checkMonthlySurplusNum(model);
	}

	@Override
	public List<MonthlyAccountItemModel> getMonthlyAccountItem(MonthlyAccountItemModel model) {
		return mapper.getMonthlyAccountItem(model);
	}
	

}
