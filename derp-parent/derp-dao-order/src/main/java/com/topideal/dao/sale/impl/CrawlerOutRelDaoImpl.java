package com.topideal.dao.sale.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.CrawlerOutRelDao;
import com.topideal.entity.vo.sale.CrawlerOutRelModel;
import com.topideal.mapper.sale.CrawlerOutRelMapper;

import java.sql.SQLException;
import java.util.List;

/**
 * 爬虫账单核销表 daoImpl
 * @author lchenxing
 */
@Repository
public class CrawlerOutRelDaoImpl implements CrawlerOutRelDao {

    @Autowired
    private CrawlerOutRelMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CrawlerOutRelModel> list(CrawlerOutRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CrawlerOutRelModel model) throws SQLException {
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
    public int modify(CrawlerOutRelModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CrawlerOutRelModel  searchByPage(CrawlerOutRelModel  model) throws SQLException{
        PageDataModel<CrawlerOutRelModel> pageModel=mapper.listByPage(model);
        return (CrawlerOutRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CrawlerOutRelModel  searchById(Long id)throws SQLException {
        CrawlerOutRelModel  model=new CrawlerOutRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CrawlerOutRelModel searchByModel(CrawlerOutRelModel model) throws SQLException {
		return mapper.get(model);
	}
}
