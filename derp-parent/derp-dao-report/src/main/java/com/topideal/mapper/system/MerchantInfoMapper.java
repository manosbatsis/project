package com.topideal.mapper.system;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.mapper.BaseMapper;

/**
 * 商家信息 mapper
 * @author zhanghx
 */
@MyBatisRepository
public interface MerchantInfoMapper extends BaseMapper<MerchantInfoModel> {
	
	/**
	 * 查询商家信息表下拉列表
	 * */
	List<SelectBean> getSelectBean(MerchantInfoModel model) throws SQLException;


	List<MerchantInfoModel> listDstp(MerchantInfoModel model);
}

