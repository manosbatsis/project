package com.topideal.mapper.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.MerchantBuRelDTO;
import com.topideal.entity.vo.main.MerchantBuRelModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface MerchantBuRelMapper extends BaseMapper<MerchantBuRelModel> {

	List<SelectBean> getSelectBean(MerchantBuRelModel merchantBuRelModel);

	PageDataModel<MerchantBuRelDTO> getListByPage(MerchantBuRelDTO dto);

	List<MerchantBuRelDTO> getExportList(MerchantBuRelDTO dto);

	/**
	 * 获取商家事业部关系表数据
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	MerchantBuRelDTO getMerchantBuRel(MerchantBuRelDTO dto) throws SQLException;

}
