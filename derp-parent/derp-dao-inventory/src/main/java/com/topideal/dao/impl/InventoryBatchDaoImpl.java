package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryBatchDao;
import com.topideal.entity.dto.InventoryBatchDTO;
import com.topideal.entity.vo.InventoryBatchModel;
import com.topideal.mapper.InventoryBatchMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * 
 * @author lchenxing
 */  
@Repository
public class InventoryBatchDaoImpl implements InventoryBatchDao {

	@Autowired
	private InventoryBatchMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<InventoryBatchModel> list(InventoryBatchModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param InventoryBatchVo
	 */
	@Override
	public Long save(InventoryBatchModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());//获取当前系统时间
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
	public int modify(InventoryBatchModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());//获取当前系统时间
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param InventoryBatchVo
	 */
	@Override
	public InventoryBatchModel searchByPage(InventoryBatchModel model) throws SQLException {
		PageDataModel<InventoryBatchModel> pageModel = mapper.listByPage(model);
		return (InventoryBatchModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public InventoryBatchModel searchById(Long id) throws SQLException {
		InventoryBatchModel model = new InventoryBatchModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public InventoryBatchModel searchByModel(InventoryBatchModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 统计批次库存效期预警
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@Override
	public InventoryBatchDTO selectInventoryWarningByPage(InventoryBatchDTO model) throws Exception {
		// TODO Auto-generated method stub
		PageDataModel<InventoryBatchDTO> pageModel = mapper.selectInventoryWarningByPage(model);
		return (InventoryBatchDTO) pageModel.getModel();
	}

	/**
	 * 更加失效日期 ASC排序 查询先失效的库存量
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<InventoryBatchModel> listOrbyOverdueDate(InventoryBatchModel model) throws Exception {
		// TODO Auto-generated method stub
		return mapper.listOrbyOverdueDate(model);
	}

	/**
	 * 更加主键id进行单个删除
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public int delInventoryBatch(Long id) throws Exception {
		// TODO Auto-generated method stub
		return mapper.del(id);
	}

	/**
	 * 商品库存明细
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@Override
	public InventoryBatchDTO listProductInventoryDetailsByPage(InventoryBatchDTO model) throws Exception {
		// TODO Auto-generated method stub
		PageDataModel<InventoryBatchDTO> pageModel = mapper.listProductInventoryDetailsByPage(model);
		return (InventoryBatchDTO) pageModel.getModel();
	}

	/**
	 * 查询所有的商家和对应的仓库
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<InventoryBatchModel> getAllMerchantOrDepot() throws Exception {
		// TODO Auto-generated method stub
		return mapper.getAllMerchantOrDepot();
	}

	/**
	 * 导出批次库存明细
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> exportInventoryBatchMap(InventoryBatchDTO model) throws Exception {
		// TODO Auto-generated method stub
		return mapper.exportInventoryBatchMap(model);
	}

	/**
	 * 导出效期预警
	 * 
	 * @param list
	 * @return
	 */
	@Override
	public List<Map<String, Object>> exportInventoryWarningMap(InventoryBatchDTO model) throws Exception {
		// TODO Auto-generated method stub
		return mapper.exportInventoryWarningMap(model);
	}

	/**
	 * 导出商品库存明细sheet0
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<InventoryBatchDTO> exportProductInventoryDetails(InventoryBatchDTO model) throws Exception {
		return mapper.exportProductInventoryDetails(model);
	}
	/**
	 * 导出商品库存明细 sheet1
	 */
	@Override
	public List<InventoryBatchDTO> getInventoryBatchExport(InventoryBatchDTO model) throws Exception {
		return mapper.getInventoryBatchExport(model);
	}

	/**
	 * 扣减库存数量
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateLowerGoodsNum(Map<String,Object> params) throws SQLException {

		return mapper.updateLowerGoodsNum(params);
	}

	/**
	 * 增加库存数量
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateAddGoodsNum(Map<String,Object> params) throws SQLException {

		return mapper.updateAddGoodsNum(params);
	}

	
	/**
	    * 统计批次库存明细 好品和坏品 已过期的数量
	    * @param merchantId
	    * @return
	    * @throws Exception
	    */
	@Override
	public InventoryBatchModel countInventoryAmount(InventoryBatchModel model) throws Exception {
		// TODO Auto-generated method stub
		return mapper.countInventoryAmount(model);
	}

	  /**
	    * 扣减库存接口 查询单个批次库存明细 
	    * @param model
	    * @return
	    * @throws Exception
	    */
	@Override
	public InventoryBatchModel searchByInventoryBatchModel(InventoryBatchModel model) throws SQLException {
		return mapper.searchByInventoryBatchModel(model);
	}

	@Override
	public List<InventoryBatchModel> searchInventoryBatchModelByGoodsList(InventoryBatchModel model)
			throws SQLException {
		// TODO Auto-generated method stub
		return mapper.searchInventoryBatchModelByGoodsList(model);
	}

	@Override
	public InventoryBatchModel getProductInventoryDetailsByOne(InventoryBatchModel model) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getProductInventoryDetailsByOne(model);
	}

	@Override
	public InventoryBatchModel getInventoryBatchModelAllSurplusNum(InventoryBatchModel model) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getInventoryBatchModelAllSurplusNum(model);
	}

	@Override
	public List<InventoryBatchModel> getInventoryBatchModelByZero(InventoryBatchModel model) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getInventoryBatchModelByZero(model);
	}

	@Override
	public InventoryBatchDTO getInventoryBatchByPage(InventoryBatchDTO model) throws SQLException {
		// TODO Auto-generated method stub
		PageDataModel<InventoryBatchDTO> pageModel = mapper.getInventoryBatchByPage(model);
		return (InventoryBatchDTO) pageModel.getModel();
	}

	@Override
	public List<InventoryBatchModel> getInventoryBatchModelByGoodsBatchList(InventoryBatchModel model)
			throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getInventoryBatchModelByGoodsBatchList(model);
	}

	@Override
	public InventoryBatchModel getInventoryBatchModelMapByOne(InventoryBatchModel model) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getInventoryBatchModelMapByOne(model);
	}

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	@Override
	public Integer checkGoodsIsUse(Long id) {
		return mapper.checkGoodsIsUse(id);
	}
	/**
	 * 校验仓库是否已经使用
	 */
	@Override
	public Integer checkDepotIsUse(Long depotId) {		
		return mapper.checkDepotIsUse(depotId);
	}
	/**
     * 加库存接口 查询单个批次库存明细 
     * @param model
     * @return
     * @throws Exception
     */
	@Override
	public InventoryBatchModel searchInventoryBatchForAdd(InventoryBatchModel inBatchModel) throws SQLException {
		return mapper.searchInventoryBatchForAdd(inBatchModel);
	}
	/**
     * 根据商家、仓库、理货单位获取库存可用量
     * @param merchantId
     * @param depotId
     * @param goodsId
     * @param unit
     * @return
     */
	@Override
	public List<InventoryBatchModel> getAvailableNum(Long merchantId, Long depotId, Long goodsId, String unit) {
		return mapper.getAvailableNum(merchantId,depotId,goodsId,unit);
	}
	/**
	 * 根据商家,仓库,商品,理货单位,批次,好坏品,过期品 查询库存余量 
	 * @param merchantId
	 * @param depotId
	 * @param goodsId
	 * @param unit
	 * @param type
	 * @param isExpire
	 * @param batchNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> getBadOrExpiredSurplusNum(Long merchantId, Long depotId, Long goodsId, String unit,
			String type, String isExpire, String batchNo) throws Exception {
		// TODO Auto-generated method stub
		return mapper.getBadOrExpiredSurplusNum(merchantId,depotId,goodsId,unit,type,isExpire,batchNo);
	}

	/**
	 * 根据仓库ID 查询仓库库存余量不为0的所有商品是否都有批次效期
	 * @param depotId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List <InventoryBatchModel> getBatchValidation(Long depotId) throws Exception {
		return mapper.getBatchValidation(depotId);
	}

	/**
	 * 获取批次库存已经过期 但是 过期状态还是未过期 的数据
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<InventoryBatchModel> getIsexpireInventoryBatchlist() throws Exception {
		// TODO Auto-generated method stub
		return mapper.getIsexpireInventoryBatchlist();
	}

	/**
	 * 获取最小的创建日期(首次上架日期)
	 */
	@Override
	public InventoryBatchModel getMinDate(InventoryBatchModel model) throws Exception {		
		return mapper.getMinDate(model);
	}


	
	

}
