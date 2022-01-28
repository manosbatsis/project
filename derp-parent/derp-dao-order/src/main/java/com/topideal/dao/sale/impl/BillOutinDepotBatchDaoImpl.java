package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.BillOutinDepotBatchDao;
import com.topideal.entity.vo.sale.BillOutinDepotBatchModel;
import com.topideal.mapper.sale.BillOutinDepotBatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BillOutinDepotBatchDaoImpl implements BillOutinDepotBatchDao {

    @Autowired
    private BillOutinDepotBatchMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BillOutinDepotBatchModel> list(BillOutinDepotBatchModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BillOutinDepotBatchModel model) throws SQLException {
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
    public int modify(BillOutinDepotBatchModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BillOutinDepotBatchModel  searchByPage(BillOutinDepotBatchModel  model) throws SQLException{
        PageDataModel<BillOutinDepotBatchModel> pageModel=mapper.listByPage(model);
        return (BillOutinDepotBatchModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BillOutinDepotBatchModel  searchById(Long id)throws SQLException {
        BillOutinDepotBatchModel  model=new BillOutinDepotBatchModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BillOutinDepotBatchModel searchByModel(BillOutinDepotBatchModel model) throws SQLException {
		return mapper.get(model);
	}

}