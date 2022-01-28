package com.topideal.service.base.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.base.PackTypeDao;
import com.topideal.entity.vo.base.PackTypeModel;
import com.topideal.mongo.dao.PackTypeMongoDao;
import com.topideal.mongo.entity.PackTypeMogo;
import com.topideal.service.base.PackTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包装方式
 * @author zhanghx
 */
@Service
public class PackTypeServiceImpl implements PackTypeService {


	@Autowired
	private PackTypeDao packTypeDao;
	@Override
	public List<SelectBean> getSelectBean() throws SQLException {
		List<SelectBean> result = new ArrayList<SelectBean>();
		PackTypeModel model=new PackTypeModel();
		List<PackTypeModel> list = packTypeDao.list(model);
		
		for (PackTypeModel pack : list) {
			SelectBean bean = new SelectBean();
			bean.setValue(pack.getCode());
			bean.setLabel(pack.getName());
			result.add(bean);
		}
		return result;
	}

	@Override
	public PackTypeModel serchByModel(PackTypeModel model) throws SQLException {		
		return packTypeDao.searchByModel(model);
	}

}
