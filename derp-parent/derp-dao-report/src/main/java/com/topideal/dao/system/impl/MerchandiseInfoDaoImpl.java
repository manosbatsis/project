package com.topideal.dao.system.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.mapper.system.MerchandiseInfoMapper;

/**
 * 商品信息impl
 * 
 * @author zhanghx
 */
@Repository
public class MerchandiseInfoDaoImpl implements MerchandiseInfoDao {

	@Autowired
	private MerchandiseInfoMapper mapper; // 商品信息

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(MerchandiseInfoModel model) throws SQLException {
		int num = mapper.insert(model);
		return model.getId();
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * 
	 * @param model
	 */
	@Override
	public int modify(MerchandiseInfoModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public MerchandiseInfoModel searchByPage(MerchandiseInfoModel model) throws SQLException {
		PageDataModel<MerchandiseInfoModel> pageModel = mapper.listByPage(model);
		return (MerchandiseInfoModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public MerchandiseInfoModel searchById(Long id) throws SQLException {
		MerchandiseInfoModel model = new MerchandiseInfoModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商品实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public MerchandiseInfoModel searchByModel(MerchandiseInfoModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 列表查询 分页
	 * 
	 * @param model
	 */
	@Override
	public List<MerchandiseInfoModel> list(MerchandiseInfoModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 获取商家的所有商品
	 */
	public List<Map<String, Object>> getMerchantAllBarcode(Map<String, Object> paramMap) {
		return mapper.getMerchantAllBarcode(paramMap);
	}
	/**
	 * 获取商家的所有商品
	 */
	public List<MerchandiseInfoModel> getListByMerchantIds(Map<String, Object> paramMap){
		return mapper.getListByMerchantIds(paramMap);
	}


	/**
	 * 查询商家所有货品条码、货号
	 * 
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getMerchantAllInfo(Map<String, Object> paramMap) throws SQLException {
		return mapper.getMerchantAllInfo(paramMap);
	}
	
	/**
	 * 结算价格-导入（条码相同只取一条记录）
	 */
	@Override
	public MerchandiseInfoModel getListForSettlment(Long merchantId,String barcode) throws SQLException {
		return mapper.getListForSettlment(merchantId,barcode);
	}
	/**
	 * 查询商家所有商品
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getAllMerchandiseByMerchantId(Map<String, Object> paramMap) throws SQLException{
		return mapper.getAllMerchandiseByMerchantId(paramMap);
	}
	

	@Override
	public Map<String, Object> getGoodsByMerchantIdWeighted(Map<String, Object> paramMap) throws SQLException {
		return mapper.getGoodsByMerchantIdWeighted(paramMap);
	}
	

	/*@Override
	public List<MerchandiseInfoModel> getAllGroupMerchandiseByGroupId(Long goodsId) throws SQLException {
		return mapper.getAllGroupMerchandiseByGroupId(goodsId);
	}*/

	@Override
	public Map<String, Object> getMerchandiseInfo(Long goodsId) {
		return mapper.getMerchandiseInfo(goodsId);
	}

	/**
	 * 查询昨天新增和修改的条码
	 * */
	@Override
	public List<String> getBarcodeListDSTP(Map<String,Object> map){
		return mapper.getBarcodeListDSTP(map);
	}
	/**根据条码查询商品
	 * */
	@Override
	public List<Map<String,Object>> getDetailList(Map<String,Object> map){
		return mapper.getDetailList(map);
	}
}
