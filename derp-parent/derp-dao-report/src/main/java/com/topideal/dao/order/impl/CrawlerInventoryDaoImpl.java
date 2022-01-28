package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.CrawlerInventoryDao;
import com.topideal.entity.vo.order.CrawlerInventoryModel;
import com.topideal.mapper.order.CrawlerInventoryMapper;

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
	
	/* 
	 * 根据商家id，货号，时间查询
	 */
	@Override
	public List<CrawlerInventoryModel> listInventory(CrawlerInventoryModel model) throws SQLException {
		return mapper.listInventory(model);
	}
	
	/**
	 * 根据货号集合、商家id、日期 统计该日的库存量
	 */
	@Override
	public CrawlerInventoryModel countInventoryNum(CrawlerInventoryModel model) throws SQLException {
		return mapper.countInventoryNum(model);
	}
	/**
	 * 根据商家id 货号 和日期查询爬虫库存
	 */
	@Override
	public List<CrawlerInventoryModel> getListByModel(CrawlerInventoryModel model) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getListByModel(model);
	}

	/**
	 * 统计全年的爬虫商品 根据商家货号进行分组
	 */
	@Override
	public List<Map<String, Object>> byYearTimeCrawlerInventory(String startTimeyear,String endTimeyear) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.byYearTimeCrawlerInventory(startTimeyear,endTimeyear);
	}
	/**
	 *  获取当日云集的库存数据(保税仓)
	 */
	@Override
	public List<Map<String, Object>> getDayCrawlerInventory(CrawlerInventoryModel model) throws SQLException {
		return mapper.getDayCrawlerInventory(model);
	}
	/**
	 * 获取当日云集的库存数据(退货仓)
	 */
	@Override
	public List<Map<String, Object>> getDayReturnbinInventory(CrawlerInventoryModel model) throws SQLException {
		return mapper.getDayReturnbinInventory(model);
	}
	
	
	
}
