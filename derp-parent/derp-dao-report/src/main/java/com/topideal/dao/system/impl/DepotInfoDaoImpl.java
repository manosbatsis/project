package com.topideal.dao.system.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.system.DepotInfoDao;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.mapper.system.DepotInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 仓库信息表 Impl
 * @author zhanghx
 */
@Repository
public class DepotInfoDaoImpl implements DepotInfoDao {

	@Autowired
	private DepotInfoMapper mapper; // 仓库信息表

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(DepotInfoModel model) throws SQLException {
		int num = mapper.insert(model);
		return model.getId();
	}

	/**
	 * 删除
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
	public int modify(DepotInfoModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());// 获取当前系统时间
		int num = mapper.update(model);
		return num;
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public DepotInfoModel searchByPage(DepotInfoModel model) throws SQLException {
		PageDataModel<DepotInfoModel> pageModel = mapper.listByPage(model);
		return (DepotInfoModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public DepotInfoModel searchById(Long id) throws SQLException {
		DepotInfoModel model = new DepotInfoModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 通过实体查询实体类信息
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public DepotInfoModel searchByModel(DepotInfoModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<DepotInfoModel> list(DepotInfoModel model) throws SQLException {
		return mapper.list(model);
	}
    
	/**
	 * 查询商家的所有仓储
	 * **/
	public List<DepotInfoModel> listMerchantDepot(Map<String, Object> map) {
		
		return mapper.listMerchantDepot(map);
	}
	
	/**
	 * 自动校验数据源获取
	 * @param queryMap
	 * @return
	 */
	@Override
	public List<DepotInfoModel> getDepotByDataSource(Map<String, Object> queryMap) {
		return mapper.getDepotByDataSource(queryMap);
	}

	/**
	 * 获取存货跌价统计仓库
	 * @param depotMap
	 * @return
	 */
	@Override
	public List<DepotInfoModel> listFallingPriceDepot(Map<String, Object> depotMap) {
		return mapper.listFallingPriceDepot(depotMap);
	}

	@Override
	public List<DepotInfoModel> depotListByMerchant(Long merchantId) throws SQLException {
		return mapper.depotListByMerchant(merchantId);
	}

	@Override
	public List<DepotInfoModel> listByMap(Map<String, Object> map) {
		return mapper.listByMap(map);
	}
}
