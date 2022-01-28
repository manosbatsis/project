package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuFinanceAddTransferNoshelfDetailsDao;
import com.topideal.entity.vo.reporting.BuFinanceAddTransferNoshelfDetailsModel;
import com.topideal.mapper.reporting.BuFinanceAddTransferNoshelfDetailsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuFinanceAddTransferNoshelfDetailsDaoImpl implements BuFinanceAddTransferNoshelfDetailsDao {

    @Autowired
    private BuFinanceAddTransferNoshelfDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuFinanceAddTransferNoshelfDetailsModel> list(BuFinanceAddTransferNoshelfDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuFinanceAddTransferNoshelfDetailsModel model) throws SQLException {
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
    public int modify(BuFinanceAddTransferNoshelfDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuFinanceAddTransferNoshelfDetailsModel  searchByPage(BuFinanceAddTransferNoshelfDetailsModel  model) throws SQLException{
        PageDataModel<BuFinanceAddTransferNoshelfDetailsModel> pageModel=mapper.listByPage(model);
        return (BuFinanceAddTransferNoshelfDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuFinanceAddTransferNoshelfDetailsModel  searchById(Long id)throws SQLException {
        BuFinanceAddTransferNoshelfDetailsModel  model=new BuFinanceAddTransferNoshelfDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuFinanceAddTransferNoshelfDetailsModel searchByModel(BuFinanceAddTransferNoshelfDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 清除商家 仓库 月份 (事业部财务经销存)累计调拨明细
	 */
	@Override
	public int delBuFinanceAddTransferNoshelfDetail(Map<String, Object> map) throws SQLException {
		return mapper.delBuFinanceAddTransferNoshelfDetail(map);
	}
	@Override
	public List<BuFinanceAddTransferNoshelfDetailsModel> getAddTransferNoshelfExport(Map<String, Object> map) throws SQLException {
		return mapper.getAddTransferNoshelfExport(map);
	}

}