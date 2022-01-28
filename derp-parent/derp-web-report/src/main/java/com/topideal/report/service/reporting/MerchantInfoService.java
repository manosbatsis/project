package com.topideal.report.service.reporting;
import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.entity.vo.system.MerchantInfoModel;

/**
 * 公司管理service实现类
 */
public interface MerchantInfoService {

	
	/**
	 * 公司列表
	 * 
	 * @param model
	 * @return
	 */
	List<MerchantInfoModel> listMerchantInfo(MerchantInfoModel model) throws SQLException;
	
	
	/**
	 * 查询商家信息表下拉列表
	 * */
	List<SelectBean> getSelectBean(MerchantInfoModel model) throws SQLException;

	/**
	 * 公司对应的仓库信息（仓库类型为保税仓、海外仓和中转仓）
	 */
	List<DepotInfoModel> depotListByMerchant(Long merchantId) throws SQLException;
}
