package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.YunjiDeliveryDetailDao;
import com.topideal.entity.vo.sale.YunjiDeliveryDetailModel;
import com.topideal.mapper.sale.YunjiDeliveryDetailMapper;

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
	 * 查询所有云集状态为未使用的所有云集发货爬虫明细
	 */
	@Override
	public List<Map<String, Object>> getYunjiDeliveryDetail(Map<String, Object> map) {
		return mapper.getYunjiDeliveryDetail(map);
	}
	
	/**
	 * 获取所有未使用的账单号
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getYunjiDeliveryDetailBySettleId(Map<String, Object> map) {
		return mapper.getYunjiDeliveryDetailBySettleId(map);
	}
	
	/**
	 * 修改云集发货为已使用
	 */
	@Override
	public int modifyYunjiDeliveryDetail(YunjiDeliveryDetailModel model) throws SQLException {
		return mapper.modifyYunjiDeliveryDetail(model);
	}
	
	/**
	 * 修改云集账单发货详情错误信息
	 */
	@Override
	public int updateYunjiDeliveryDetail(YunjiDeliveryDetailModel model) throws SQLException {
		return mapper.updateYunjiDeliveryDetail(model);
	}
	
	/**
	 * 根据结算单号，sku汇总云集发货明细
	 * @return 
	 */
	@Override
	public List<YunjiDeliveryDetailModel> getPlatformStatementSumData(Map<String, Object> itemQueryMap) {
		return mapper.getPlatformStatementSumData(itemQueryMap) ;
	}
	/**
	 * 修改为未使用
	 */
	@Override
	public int updateNotUsed(YunjiDeliveryDetailModel model) throws SQLException {
		return mapper.updateNotUsed(model);
	}
}