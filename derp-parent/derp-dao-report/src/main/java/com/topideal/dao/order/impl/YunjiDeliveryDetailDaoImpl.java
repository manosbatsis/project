package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.YunjiDeliveryDetailDao;
import com.topideal.entity.vo.order.YunjiDeliveryDetailModel;
import com.topideal.mapper.order.YunjiDeliveryDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class YunjiDeliveryDetailDaoImpl implements YunjiDeliveryDetailDao {

    @Autowired
    private YunjiDeliveryDetailMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<YunjiDeliveryDetailModel> list(YunjiDeliveryDetailModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(YunjiDeliveryDetailModel model) throws SQLException {
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
    public int modify(YunjiDeliveryDetailModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public YunjiDeliveryDetailModel  searchByPage(YunjiDeliveryDetailModel  model) throws SQLException{
        PageDataModel<YunjiDeliveryDetailModel> pageModel=mapper.listByPage(model);
        return (YunjiDeliveryDetailModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public YunjiDeliveryDetailModel  searchById(Long id)throws SQLException {
        YunjiDeliveryDetailModel  model=new YunjiDeliveryDetailModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public YunjiDeliveryDetailModel searchByModel(YunjiDeliveryDetailModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 根据结算ID，云集SKU统计状态未为校验的结算明细
	 */
	@Override
	public List<Map<String, Object>> getYjVeriDetail(Map<String, Object> queryMap) {
		return mapper.getYjVeriDetail(queryMap);
	}
	@Override
	public int changeVeriStatus(Map<String, Object> queryMap) {
		return mapper.changeVeriStatus(queryMap);
	}
	@Override
	public Map<String, Object> getDeliveryAccount(Map<String, Object> queryPlatformMap) {
		return mapper.getDeliveryAccount(queryPlatformMap);
	}

}