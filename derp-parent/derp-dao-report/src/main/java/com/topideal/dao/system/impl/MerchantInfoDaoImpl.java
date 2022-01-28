package com.topideal.dao.system.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.mapper.system.MerchantInfoMapper;

/**
 * 商家信息impl
 * @author zhanghx
 */
@Repository
public class MerchantInfoDaoImpl implements MerchantInfoDao {

    @Autowired
    private MerchantInfoMapper mapper;

	/**
	 * 新增
	 */
    @Override
    public Long save(MerchantInfoModel model) throws SQLException {
        int num=mapper.insert(model);
        return model.getId();
    }
    
	/**
     * 删除
     */
    @Override
    public int delete(List ids) throws SQLException {
	    return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     */
    @Override
    public int modify(MerchantInfoModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
    	int num = mapper.update(model);
        return num;
    }
    
	/**
     * 分页查询
     */
    @Override
    public MerchantInfoModel  searchByPage(MerchantInfoModel  model) throws SQLException{
        PageDataModel<MerchantInfoModel> pageModel=mapper.listByPage(model);
        return (MerchantInfoModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     */
    @Override
    public MerchantInfoModel  searchById(Long id)throws SQLException {
        MerchantInfoModel  model=new MerchantInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
    /**
     * 根据商家实体类查询商品
     * */
	@Override
	public MerchantInfoModel searchByModel(MerchantInfoModel model) throws SQLException{
		return mapper.get(model);
	}

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MerchantInfoModel> list(MerchantInfoModel model) throws SQLException {
		return mapper.list(model);
	}

	@Override
	public List<SelectBean> getSelectBean(MerchantInfoModel model) throws SQLException {
		return mapper.getSelectBean(model);
	}

    @Override
    public List<MerchantInfoModel> listDstp(MerchantInfoModel model){
        return mapper.listDstp(model);
    }
}
