package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.SaleShelfDao;
import com.topideal.entity.vo.order.SaleShelfModel;
import com.topideal.mapper.order.SaleShelfMapper;

/**
 * 销售上架信息表 daoImpl
 * @author lian_
 *
 */
@Repository
public class SaleShelfDaoImpl implements SaleShelfDao {

    @Autowired
    private SaleShelfMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleShelfModel> list(SaleShelfModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleShelfModel model) throws SQLException {
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
    @SuppressWarnings("rawtypes")
	@Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(SaleShelfModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleShelfModel  searchByPage(SaleShelfModel  model) throws SQLException{
        PageDataModel<SaleShelfModel> pageModel=mapper.listByPage(model);
        return (SaleShelfModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleShelfModel  searchById(Long id)throws SQLException {
        SaleShelfModel  model=new SaleShelfModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleShelfModel searchByModel(SaleShelfModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 根据po查出对应的上架总量信息
	 * @param poNo
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getShelfAccountByPo(Map<String, Object> queryMap) {
		return mapper.getShelfAccountByPo(queryMap);
	}
	
	/**
	 * 根据po、标准条码、sku,获取销售上架明细
	 */
	@Override
	public List<Map<String, Object>> getVipShelfDetails(Map<String, Object> queryMap) {
		return mapper.getVipShelfDetails(queryMap);
	}
	@Override
	public Map<String, Object> getShelfAccountByPoAndCommbarcode(Map<String, Object> queryMap) {
		return mapper.getShelfAccountByPoAndCommbarcode(queryMap);
	}
	@Override
	public List<Map<String, Object>> getNewShelfAccountByPo(Map<String, Object> shelfAccountQueryMap) {
		return mapper.getNewShelfAccountByPo(shelfAccountQueryMap);
	}
	@Override
	public Map<String, Object> getNewShelfAccountByPoAndCommbarcode(Map<String, Object> queryMap) {
		return mapper.getNewShelfAccountByPoAndCommbarcode(queryMap);
	}
	@Override
	public List<Map<String, Object>> getNewVipShelfDetails(Map<String, Object> queryMap) {
		return mapper.getNewVipShelfDetails(queryMap);
	}
}