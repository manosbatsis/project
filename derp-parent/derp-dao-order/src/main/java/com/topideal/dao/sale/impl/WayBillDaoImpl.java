package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.WayBillDao;
import com.topideal.entity.vo.sale.WayBillModel;
import com.topideal.mapper.sale.WayBillMapper;

/**
 *运单表 impl
 * @author lchenxing
 */
@Repository
public class WayBillDaoImpl implements WayBillDao {

    @Autowired
    private WayBillMapper mapper;  //运单表
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<WayBillModel> list(WayBillModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param WayBillModel
	 */
    @Override
    public Long save(WayBillModel model) throws SQLException {
    	model.setCreateDate(TimeUtils.getNow());
    	model.setModifyDate(TimeUtils.getNow());
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 删除
     * @param List
     */
    @Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param List
     */
    @Override
    public int modify(WayBillModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param WayBillModel
     */
    @Override
    public WayBillModel  searchByPage(WayBillModel  model) throws SQLException{
        PageDataModel<WayBillModel> pageModel=mapper.listByPage(model);
        return (WayBillModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param Long
     */
    @Override
    public WayBillModel  searchById(Long id)throws SQLException {
        WayBillModel  model=new WayBillModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
            /**
     	* 根据商家实体类查询商品
     	* @param MerchandiseInfoModel
     	* */
	@Override
	public WayBillModel searchByModel(WayBillModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 根据运单号查询运单表
	 * @param listWayBillNo
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<WayBillModel> selectByWayBillNo(List listWayBillNo,Long merchantId) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.selectByWayBillNo(listWayBillNo,merchantId);
	}
	/**
	 * 迁移数据到历史表
	 * */
	@Override
	public int synsMoveToHistory(Map<String,Object> map){
		return mapper.synsMoveToHistory(map);
	}
	/**
	 * 删除已迁移到历史表的数据
	 * */
	@Override
	public int delMoveToHistory(Map<String,Object> map){
		return mapper.delMoveToHistory(map);
	}
}
