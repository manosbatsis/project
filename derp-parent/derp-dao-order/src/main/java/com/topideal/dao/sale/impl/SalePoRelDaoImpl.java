package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SalePoRelDao;
import com.topideal.entity.vo.sale.SalePoRelModel;
import com.topideal.mapper.sale.SalePoRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SalePoRelDaoImpl implements SalePoRelDao {

    @Autowired
    private SalePoRelMapper mapper;

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SalePoRelModel> list(SalePoRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SalePoRelModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }

	/**
     * 删除
     * @param ids
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }

	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(SalePoRelModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }

	/**
     * 分页查询
     * @param model
     */
    @Override
    public SalePoRelModel  searchByPage(SalePoRelModel  model) throws SQLException{
        PageDataModel<SalePoRelModel> pageModel=mapper.listByPage(model);
        return (SalePoRelModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SalePoRelModel  searchById(Long id)throws SQLException {
        SalePoRelModel  model=new SalePoRelModel ();
        model.setId(id);
        return mapper.get(model);
    }

      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SalePoRelModel searchByModel(SalePoRelModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public int getCountByOrderId(Long orderId) {
        return mapper.getCountByOrderId(orderId);
    }
    @Override
    public List<SalePoRelModel> getAllByOrderId(Long orderId,Long merchantId) {
        return mapper.getAllByOrderId(orderId,merchantId);
    }

    @Override
    public int delbyPoNoAndOrderId(SalePoRelModel model) {
        return mapper.delbyPoNoAndOrderId(model);
    }
	@Override
	public List<SalePoRelModel> getAllByNoDelete(Map<String,Object> paramMap) {
		return mapper.getAllByNoDelete(paramMap);
	}
}
