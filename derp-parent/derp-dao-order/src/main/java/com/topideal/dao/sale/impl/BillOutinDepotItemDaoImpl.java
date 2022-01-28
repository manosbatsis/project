package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.BillOutinDepotItemDao;
import com.topideal.entity.vo.sale.BillOutinDepotItemModel;
import com.topideal.mapper.sale.BillOutinDepotItemMapper;
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
public class BillOutinDepotItemDaoImpl implements BillOutinDepotItemDao {

    @Autowired
    private BillOutinDepotItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BillOutinDepotItemModel> list(BillOutinDepotItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BillOutinDepotItemModel model) throws SQLException {
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
    public int modify(BillOutinDepotItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BillOutinDepotItemModel  searchByPage(BillOutinDepotItemModel  model) throws SQLException{
        PageDataModel<BillOutinDepotItemModel> pageModel=mapper.listByPage(model);
        return (BillOutinDepotItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BillOutinDepotItemModel  searchById(Long id)throws SQLException {
        BillOutinDepotItemModel  model=new BillOutinDepotItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BillOutinDepotItemModel searchByModel(BillOutinDepotItemModel model) throws SQLException {
		return mapper.get(model);
	}
	/**唯品4.0-获取商家+仓库+po+货号账单出入库量 */
	public Integer getBillOutInDepotNum(Map<String, Object> map) {
		return mapper.getBillOutInDepotNum(map);
	}
	
	/**唯品4.0-根据单号汇总表体相同商品的数量*/
    public List<Map<String, Object>> getCodeGoodsNum(String code){
    	return mapper.getCodeGoodsNum(code);
    }

    @Override
    public List<Map<String, Object>> getTotalByBillCode(String billCode, String currency, Long merchantId, Long buId) {
        return mapper.getTotalByBillCode(billCode, currency, merchantId, buId);
    }
    
    /**统计出库占用数量*/
	@Override
	public Integer getOutOccupyNum(Map<String, Object> queryMap) {
		return mapper.getOutOccupyNum(queryMap);
	}
	@Override
	public Integer getInOccupyNum(Map<String, Object> queryMap) {
		return mapper.getInOccupyNum(queryMap);
	}

    @Override
    public List<Map<String, Object>> getCodeGoodsBacthNum(String code) {
        return mapper.getCodeGoodsBatchNum(code);
    }

    @Override
    public List<BillOutinDepotItemModel> getDetailsByReceive(Map<String, Object> map) throws SQLException {
        return mapper.getDetailsByReceive(map);
    }

}