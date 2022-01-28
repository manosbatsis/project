package com.topideal.dao.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.IndexOrderStatisticsDao;
import com.topideal.entity.vo.IndexOrderStatisticsModel;
import com.topideal.mapper.IndexOrderStatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class IndexOrderStatisticsDaoImpl implements IndexOrderStatisticsDao {

    @Autowired
    private IndexOrderStatisticsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<IndexOrderStatisticsModel> list(IndexOrderStatisticsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(IndexOrderStatisticsModel model) throws SQLException {
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
    public int modify(IndexOrderStatisticsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public IndexOrderStatisticsModel  searchByPage(IndexOrderStatisticsModel  model) throws SQLException{
        PageDataModel<IndexOrderStatisticsModel> pageModel=mapper.listByPage(model);
        return (IndexOrderStatisticsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public IndexOrderStatisticsModel  searchById(Long id)throws SQLException {
        IndexOrderStatisticsModel  model=new IndexOrderStatisticsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public IndexOrderStatisticsModel searchByModel(IndexOrderStatisticsModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public void batchDelByDate(String month) throws SQLException {
        mapper.batchDelByDate(month);
    }

    @Override
    public List<IndexOrderStatisticsModel> getListByPage(IndexOrderStatisticsModel indexOrderStatisticsModel) throws SQLException {
        return mapper.getTop10(indexOrderStatisticsModel);
    }
}