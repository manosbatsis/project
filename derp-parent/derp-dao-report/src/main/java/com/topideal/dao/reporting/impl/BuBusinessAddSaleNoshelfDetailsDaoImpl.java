package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuBusinessAddSaleNoshelfDetailsDao;
import com.topideal.entity.vo.reporting.BuBusinessAddSaleNoshelfDetailsModel;
import com.topideal.mapper.reporting.BuBusinessAddSaleNoshelfDetailsMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuBusinessAddSaleNoshelfDetailsDaoImpl implements BuBusinessAddSaleNoshelfDetailsDao {

    @Autowired
    private BuBusinessAddSaleNoshelfDetailsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuBusinessAddSaleNoshelfDetailsModel> list(BuBusinessAddSaleNoshelfDetailsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuBusinessAddSaleNoshelfDetailsModel model) throws SQLException {
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
    public int modify(BuBusinessAddSaleNoshelfDetailsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuBusinessAddSaleNoshelfDetailsModel  searchByPage(BuBusinessAddSaleNoshelfDetailsModel  model) throws SQLException{
        PageDataModel<BuBusinessAddSaleNoshelfDetailsModel> pageModel=mapper.listByPage(model);
        return (BuBusinessAddSaleNoshelfDetailsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuBusinessAddSaleNoshelfDetailsModel  searchById(Long id)throws SQLException {
        BuBusinessAddSaleNoshelfDetailsModel  model=new BuBusinessAddSaleNoshelfDetailsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuBusinessAddSaleNoshelfDetailsModel searchByModel(BuBusinessAddSaleNoshelfDetailsModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 清除商家 仓库 月份 删除 (事业部业务经销存)累计销售在途明细表
	 */
	@Override
	public int delBuBusinessAddSaleNoshelfDetails(Map<String, Object> map) throws SQLException {
		return mapper.delBuBusinessAddSaleNoshelfDetails(map);
	}
	
	/**
	 * 查询商家、仓库、月份(事业部业务经销存)累计销售在途明细表 (导出)
	 */
	@Override
	public List<Map<String, Object>> listBuAddSaleNoshelfDetailsMap(Map<String, Object> map) {
		return mapper.listBuAddSaleNoshelfDetailsMap(map);
	}
	
	/**
	 * 销售未上架导出
	 */
	@Override
	public List<Map<String, Object>> listBuSaleNoshelfDetailsMap(Map<String, Object> map) {
		return mapper.listBuSaleNoshelfDetailsMap(map);
	}

}