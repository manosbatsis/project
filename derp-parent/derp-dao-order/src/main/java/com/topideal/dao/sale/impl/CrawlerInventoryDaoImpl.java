package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.CrawlerInventoryDao;
import com.topideal.entity.vo.sale.CrawlerInventoryModel;
import com.topideal.mapper.sale.CrawlerInventoryMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class CrawlerInventoryDaoImpl implements CrawlerInventoryDao {

    @Autowired
    private CrawlerInventoryMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CrawlerInventoryModel> list(CrawlerInventoryModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CrawlerInventoryModel model) throws SQLException {
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
    public int modify(CrawlerInventoryModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CrawlerInventoryModel  searchByPage(CrawlerInventoryModel  model) throws SQLException{
        PageDataModel<CrawlerInventoryModel> pageModel=mapper.listByPage(model);
        return (CrawlerInventoryModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CrawlerInventoryModel  searchById(Long id)throws SQLException {
        CrawlerInventoryModel  model=new CrawlerInventoryModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CrawlerInventoryModel searchByModel(CrawlerInventoryModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 根据条件获取云集库存数据，多条相同的sku获取最后一条记录
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<CrawlerInventoryModel> getCrawlerInventoryByModel(CrawlerInventoryModel model) throws SQLException {
		return mapper.getCrawlerInventoryByModel(model);
	}
}
