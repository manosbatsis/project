package com.topideal.dao.bill.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.AdvanceBillItemDao;
import com.topideal.entity.dto.bill.AdvanceBillItemDTO;
import com.topideal.entity.vo.bill.AdvanceBillItemModel;
import com.topideal.mapper.bill.AdvanceBillItemMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class AdvanceBillItemDaoImpl implements AdvanceBillItemDao {

    @Autowired
    private AdvanceBillItemMapper mapper;
	
    @Override
    public List<Map<String, Object>>getAdvanceBillItemList(List<Long> ids) {
        return mapper.getAdvanceBillItemList(ids);
    }
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AdvanceBillItemModel> list(AdvanceBillItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AdvanceBillItemModel model) throws SQLException {
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
    public int modify(AdvanceBillItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AdvanceBillItemModel  searchByPage(AdvanceBillItemModel  model) throws SQLException{
        PageDataModel<AdvanceBillItemModel> pageModel=mapper.listByPage(model);
        return (AdvanceBillItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AdvanceBillItemModel  searchById(Long id)throws SQLException {
        AdvanceBillItemModel  model=new AdvanceBillItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AdvanceBillItemModel searchByModel(AdvanceBillItemModel model) throws SQLException {
		return mapper.get(model);
	}


    @Override
    public List<AdvanceBillItemDTO> getAdvanceById(Long advanceId) {
        return mapper.getAdvanceById(advanceId);
    }

    @Override
    public int delItems(Long advanceId) throws SQLException {
        return mapper.delItems(advanceId);
    }

    @Override
    public List<AdvanceBillItemDTO> synNcItemByIds(List<Long> advanceId) {
        return mapper.synNcItemByIds(advanceId);
    }

    @Override
    public List<Map<String, Object>> listItemPrice(List<Long> ids) throws SQLException {
        return mapper.listItemPrice(ids);
    }

    @Override
    public List<Map<String, Object>> listItemPriceByOrderCodes(List<Long> ids, List<String> orderCodes) throws SQLException {
        return mapper.listItemPriceByOrderCodes(ids, orderCodes);
    }

    @Override
    public List<AdvanceBillItemDTO> listWithoutVerify(List<String> orderCodes) {
        return mapper.listWithoutVerify(orderCodes);
    }

    @Override
    public List<AdvanceBillItemModel> listByIds(List<Long> ids) {
        return mapper.listByIds(ids);
    }


}