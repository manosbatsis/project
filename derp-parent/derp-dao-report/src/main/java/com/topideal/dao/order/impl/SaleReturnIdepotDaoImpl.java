package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.SaleReturnIdepotDao;
import com.topideal.entity.vo.order.SaleReturnIdepotModel;
import com.topideal.mapper.order.SaleReturnIdepotMapper;

/**
 * 销售退货入库 daoImpl
 * @author lian_
 */
@Repository
public class SaleReturnIdepotDaoImpl implements SaleReturnIdepotDao {

    @Autowired
    private SaleReturnIdepotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleReturnIdepotModel> list(SaleReturnIdepotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleReturnIdepotModel model) throws SQLException {
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
    public int modify(SaleReturnIdepotModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleReturnIdepotModel  searchByPage(SaleReturnIdepotModel  model) throws SQLException{
        PageDataModel<SaleReturnIdepotModel> pageModel=mapper.listByPage(model);
        return (SaleReturnIdepotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleReturnIdepotModel  searchById(Long id)throws SQLException {
        SaleReturnIdepotModel  model=new SaleReturnIdepotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleReturnIdepotModel searchByModel(SaleReturnIdepotModel model) throws SQLException {
		return mapper.get(model);
	}
	
	
	/**
	 * 报表api 根据 商家开始时间结束时间分页查询销售退货入库
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getListPage(Long merchantId, String startTime,String endTime,Integer startIndex,Integer pageSize) throws SQLException {
		
		return mapper.getListPage(merchantId, startTime,endTime,startIndex,pageSize);
	}
	
	/**
	 * 报表api 根据 商家开始时间结束时间查询销售 退货入库总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Integer getListCount(Long merchantId, String startTime, String endTime) {
		// TODO Auto-generated method stub
		return mapper.getListCount(merchantId,startTime,endTime);
	}

}