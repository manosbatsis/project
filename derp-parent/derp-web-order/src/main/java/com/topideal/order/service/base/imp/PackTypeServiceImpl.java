package com.topideal.order.service.base.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.mongo.dao.PackTypeMongoDao;
import com.topideal.mongo.entity.PackTypeMogo;
import com.topideal.order.service.base.PackTypeService;

/**
 * 包装方式
 * @author zhanghx
 */
@Service
public class PackTypeServiceImpl implements PackTypeService {

	@Autowired
	private PackTypeMongoDao packTypeDao;
	
	@Override
	public List<SelectBean> getSelectBean() {
		List<SelectBean> result = new ArrayList<SelectBean>();
		Map<String, Object> params = new HashMap<String, Object>();
		List<PackTypeMogo> list = packTypeDao.findAll(params);
		for (PackTypeMogo mogo : list) {
			SelectBean bean = new SelectBean();
			bean.setValue(mogo.getName());
			bean.setLabel(mogo.getName());
			result.add(bean);
		}
		return result;
	}

}
