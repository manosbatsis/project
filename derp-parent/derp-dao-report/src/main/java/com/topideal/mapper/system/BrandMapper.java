package com.topideal.mapper.system;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.system.BrandModel;
import com.topideal.mapper.BaseMapper;

@MyBatisRepository
public interface BrandMapper extends BaseMapper<BrandModel> {

	public List<SelectBean> getSelectBean();
	
	public List<BrandModel> getListBrandInfo();
}
