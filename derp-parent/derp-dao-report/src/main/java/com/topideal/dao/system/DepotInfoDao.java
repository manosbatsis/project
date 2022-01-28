package com.topideal.dao.system;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.system.DepotInfoModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 仓库信息表  Dao
 * @author zhanghx
 */
public interface DepotInfoDao extends BaseDao<DepotInfoModel> {
    
	/**
	 * 查询商家的所有仓储
	 * **/
	public List<DepotInfoModel> listMerchantDepot(Map<String, Object> map);
	
	/**
	 * 自动校验数据源获取
	 * @param queryMap
	 * @return
	 */
	public List<DepotInfoModel> getDepotByDataSource(Map<String, Object> queryMap);

	/**
	 * 获取存货跌价统计仓库
	 * @param depotMap
	 * @return
	 */
	public List<DepotInfoModel> listFallingPriceDepot(Map<String, Object> depotMap);

	/**
	 * 公司对应的仓库信息（仓库类型为保税仓、海外仓和中转仓）
	 */
	List<DepotInfoModel> depotListByMerchant(Long merchantId) throws SQLException;

    List<DepotInfoModel> listByMap(Map<String, Object> map);
}

