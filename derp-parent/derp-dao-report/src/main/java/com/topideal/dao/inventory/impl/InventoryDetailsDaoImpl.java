package com.topideal.dao.inventory.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.inventory.InventoryDetailsDao;
import com.topideal.entity.vo.inventory.InventoryDetailsModel;
import com.topideal.mapper.inventory.InventoryDetailsMapper;

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
     * 统计事业部商家、仓库、条码在本月的入库
     * **/
	public List<Map<String, Object>> getBuBarcodeInStorageSum(Map<String, Object> map){
		return mapper.getBuBarcodeInStorageSum(map);
	}

	/**
     * 统计事业部商家、仓库、条码在本月的出库
     * **/
	public List<Map<String, Object>> getBuBarcodeOutStorageSum(Map<String, Object> map){
		return mapper.getBuBarcodeOutStorageSum(map);
	}

	/**
     * 统计事业部商家、仓库、条码在本月的损益入库
     * **/
	public List<Map<String, Object>> getBuBarcodeProfitLossInStorageSum(Map<String, Object> map){
		return mapper.getBuBarcodeProfitLossInStorageSum(map);
	}

	/**
     * 统计事业部商家、仓库、条码在本月的损益出库
     * **/
	public List<Map<String, Object>> getBuBarcodeProfitLossOutStorageSum(Map<String, Object> map){
		return mapper.getBuBarcodeProfitLossOutStorageSum(map);	
	}
	/**
     * 统计商家、仓库、条码在本月的出入库明细统计数量
     * **/
	public Integer getBarcodeOutInStorageDetailCount(Map<String, Object> map){
		return mapper.getBarcodeOutInStorageDetailCount(map);
	}


	/**
     * 统计事业部商家、仓库、在本月的出入库明细
     * **/
	public List<Map<String, Object>> getBuOutInStorageDetail(Map<String, Object> map){
		return mapper.getBuOutInStorageDetail(map);
	}


	/**
	 * 统计商家、仓库、在本月的损益明细
	 * **/
	public List<InventoryDetailsModel> getBuProfitLossOutInStorageDetail(Map<String, Object> map){
		return mapper.getBuProfitLossOutInStorageDetail(map);
	}

	/**
     * 统计事业部商家、仓库、条码在本月的好品损益入库
     **/
	@Override
	public List<Map<String, Object>> getBuBarcodeProfitlossGoodInSum(Map<String, Object> map) {
		return mapper.getBuBarcodeProfitlossGoodInSum(map);
	}

	/**
     * 统计事业部商家、仓库、条码在本月的好品损益出库
     **/
	@Override
	public List<Map<String, Object>> getBuBarcodeProfitlossGoodOutSum(Map<String, Object> map) {
		return mapper.getBuBarcodeProfitlossGoodOutSum(map);
	}

	/**
	 * 统计事业部商家、仓库、条码在本月的坏品损益入库
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getBuBarcodeProfitlossDamagedInSum(Map<String, Object> map) {
		return mapper.getBuBarcodeProfitlossDamagedInSum(map);
	}

	/**
	 * 统计事业部商家、仓库、条码在本月的坏品损益出库
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getBuBarcodeProfitlossDamagedOutSum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.getBuBarcodeProfitlossDamagedOutSum(map);
	}

	// 来货残次
	@Override
	public List<InventoryDetailsModel> getInbadDetails(Map<String, Object> map) {
		return mapper.getInbadDetails(map);
	} 
	// 出库残次
	@Override
	public List<InventoryDetailsModel> getOutbadDetails(Map<String, Object> map) {
		return mapper.getOutbadDetails(map);
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
