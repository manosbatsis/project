package com.topideal.service.base.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.BrandParentDao;
import com.topideal.dao.base.BrandSuperiorDao;
import com.topideal.entity.dto.base.BrandSuperiorDTO;
import com.topideal.entity.vo.base.BrandParentModel;
import com.topideal.entity.vo.base.BrandSuperiorModel;
import com.topideal.service.base.BrandSuperiorService;

/**
 * 品牌父类
 * @author zhanghx
 */
@Service
public class BrandSuperiorServiceImpl implements BrandSuperiorService {
	@Autowired
	public BrandSuperiorDao dao;
	@Autowired
	private BrandParentDao parentdao;

	@Override
	public BrandSuperiorDTO listByPage(BrandSuperiorDTO dto) throws SQLException {
		return dao.getListByPage(dto);
	}

	@Override
	public boolean save(BrandSuperiorModel model,User user) throws SQLException, RuntimeException {
		boolean flag = true ;
		//申报系数不能为空
		if(model.getPriceDeclareRatio()==null){
			throw new RuntimeException("申报系数不能为空");
		}
		
		//母品牌名称不能重复，校验唯一性
		BrandSuperiorModel supmodel = new BrandSuperiorModel();
		supmodel.setName(model.getName());
		supmodel = dao.searchByModel(supmodel);
		if(supmodel != null){
			throw new RuntimeException("母品牌名称已存在");
		}
		model.setCreater(user.getId());
		model.setCreaterName(user.getName());
		model.setCreateDate(TimeUtils.getNow());
		Long num = dao.save(model);
		if(num == null){
			flag = false;
		}
		return flag;
	}

	@Override
	public BrandSuperiorModel findById(Long id) throws SQLException {
		return dao.searchById(id);
	}

	@Override
	public boolean modify(BrandSuperiorModel model, User user) throws SQLException, RuntimeException {
		//申报系数不能为空
		if(model.getPriceDeclareRatio()==null){
			throw new RuntimeException("申报系数不能为空");
		}		
		model.setModifier(user.getId());
		model.setModifierName(user.getName());
		model.setModifyDate(TimeUtils.getNow());
		dao.modify(model);

		return true;
	}

	@Override
	public boolean delete(List<Long> ids) throws SQLException, RuntimeException {
		if(ids != null && ids.size() > 0){
			for(Long id :ids){
				BrandSuperiorModel m = dao.searchById(id);
				BrandParentModel parentModel =  new  BrandParentModel();
				parentModel.setSuperiorParentBrandId(id);
				List<BrandParentModel> list = parentdao.list(parentModel);
				if(list != null && list.size() > 0){
					throw new RuntimeException("母品牌 "+m.getName()+" 已存在关联的标准品牌，不予删除");
				}
			}
			dao.delete(ids);
		}

		return true;
	}

	@Override
	public List<SelectBean> getSelectBean() throws SQLException {
		return dao.getSelectBean();
	}
}
