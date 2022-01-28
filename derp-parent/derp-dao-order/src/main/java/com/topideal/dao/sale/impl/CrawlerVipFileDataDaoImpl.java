package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.CrawlerVipFileDataDao;
import com.topideal.entity.vo.sale.CrawlerVipFileDataModel;
import com.topideal.mapper.sale.CrawlerVipFileDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class CrawlerVipFileDataDaoImpl implements CrawlerVipFileDataDao {

    @Autowired
    private CrawlerVipFileDataMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<CrawlerVipFileDataModel> list(CrawlerVipFileDataModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(CrawlerVipFileDataModel model) throws SQLException {
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
    public int modify(CrawlerVipFileDataModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public CrawlerVipFileDataModel  searchByPage(CrawlerVipFileDataModel  model) throws SQLException{
        PageDataModel<CrawlerVipFileDataModel> pageModel=mapper.listByPage(model);
        return (CrawlerVipFileDataModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public CrawlerVipFileDataModel  searchById(Long id)throws SQLException {
        CrawlerVipFileDataModel  model=new CrawlerVipFileDataModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public CrawlerVipFileDataModel searchByModel(CrawlerVipFileDataModel model) throws SQLException {
		return mapper.get(model);
	}

}