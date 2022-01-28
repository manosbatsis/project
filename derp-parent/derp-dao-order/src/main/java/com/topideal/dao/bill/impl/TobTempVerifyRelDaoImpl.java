package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TobTempVerifyRelDao;
import com.topideal.entity.vo.bill.TobTempVerifyRelModel;
import com.topideal.mapper.bill.TobTempVerifyRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class TobTempVerifyRelDaoImpl implements TobTempVerifyRelDao {

    @Autowired
    private TobTempVerifyRelMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TobTempVerifyRelModel> list(TobTempVerifyRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TobTempVerifyRelModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(TobTempVerifyRelModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TobTempVerifyRelModel  searchByPage(TobTempVerifyRelModel  model) throws SQLException{
        PageDataModel<TobTempVerifyRelModel> pageModel=mapper.listByPage(model);
        return (TobTempVerifyRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TobTempVerifyRelModel  searchById(Long id)throws SQLException {
        TobTempVerifyRelModel  model=new TobTempVerifyRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TobTempVerifyRelModel searchByModel(TobTempVerifyRelModel model) throws SQLException {
		return mapper.get(model);
	}
    
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

    @Override
    public Integer batchSave(List<TobTempVerifyRelModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public Integer batchDelByReceiveId(Long receiveBillId) throws SQLException {
        return mapper.batchDelByReceiveId(receiveBillId);
    }

    @Override
    public List<Map<String, Object>> getRelReceiveBill(List<Long> tobIds) throws SQLException {
        return mapper.getRelReceiveBill(tobIds);
    }

    @Override
    public List<Map<String, Object>> getRelReceiveItemBill(List<Long> tobItemIds, String type) throws SQLException {
        return mapper.getRelReceiveItemBill(tobItemIds, type);
    }
    @Override
    public Map<String, BigDecimal> getByTobItemId(Map<String, Object> queryMap) throws SQLException {
        return mapper.getByTobItemAmount(queryMap);
    }

    @Override
    public List<TobTempVerifyRelModel> getRelBeforeMonth(List<Long> tobItemIds, String month, String type) throws SQLException {
        return mapper.getRelBeforeMonth(tobItemIds, month, type);
    }
}