package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.main.MerchantDepotBuRelModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface MerchantDepotBuRelMapper extends BaseMapper<MerchantDepotBuRelModel> {

    /**
     * 根据关联仓库获取公司事业部下拉框
     * @param model
     * @return
     * @throws SQLException
     */
    List<SelectBean> getSelectBean(MerchantDepotBuRelModel model) throws SQLException;

	String getSelectInfoByMerchantId(MerchantDepotBuRelModel model);

	int delByModel(MerchantDepotBuRelModel delModel);

	String getBuNameByMerchantDepot(MerchantDepotBuRelModel model);

}
