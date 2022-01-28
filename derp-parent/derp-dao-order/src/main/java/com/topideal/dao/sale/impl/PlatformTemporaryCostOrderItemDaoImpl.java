package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.PlatformTemporaryCostOrderItemDao;
import com.topideal.entity.dto.common.PlatformTemporaryCostOrderItemDTO;
import com.topideal.entity.vo.sale.PlatformTemporaryCostOrderItemModel;
import com.topideal.mapper.sale.PlatformTemporaryCostOrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PlatformTemporaryCostOrderItemDaoImpl implements PlatformTemporaryCostOrderItemDao {

    @Autowired
    private PlatformTemporaryCostOrderItemMapper mapper;

    /**
     * 列表查询
     * @param model
     */
    @Override
    public List<PlatformTemporaryCostOrderItemModel> list(PlatformTemporaryCostOrderItemModel model) throws SQLException {
        return mapper.list(model);
    }
    /**
     * 新增
     * @param model
     */
    @Override
    public Long save(PlatformTemporaryCostOrderItemModel model) throws SQLException {
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
    public int modify(PlatformTemporaryCostOrderItemModel  model) throws SQLException {
        return mapper.update(model);
    }

    /**
     * 分页查询
     * @param model
     */
    @Override
    public PlatformTemporaryCostOrderItemModel  searchByPage(PlatformTemporaryCostOrderItemModel  model) throws SQLException{
        PageDataModel<PlatformTemporaryCostOrderItemModel> pageModel=mapper.listByPage(model);
        return (PlatformTemporaryCostOrderItemModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PlatformTemporaryCostOrderItemModel  searchById(Long id)throws SQLException {
        PlatformTemporaryCostOrderItemModel  model=new PlatformTemporaryCostOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }

    /**
     * 根据商家实体类查询商品
     * @param model
     * */
    @Override
    public PlatformTemporaryCostOrderItemModel searchByModel(PlatformTemporaryCostOrderItemModel model) throws SQLException {
        return mapper.get(model);
    }

    @Override
    public Integer batchSave(List<PlatformTemporaryCostOrderItemModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public Integer batchUpdateIds(List<String> orderCodes, String orderType) {
        return mapper.batchUpdateIds(orderCodes, orderType);
    }

    @Override
    public List<PlatformTemporaryCostOrderItemModel> listItemByPlatformIds(List<Long> platformIds) {
        return mapper.listItemByPlatformIds(platformIds);
    }

    @Override
    public List<PlatformTemporaryCostOrderItemDTO> sumAmountByOrderIds(List<Long> platformIds) {
        return mapper.sumAmountByOrderIds(platformIds);
    }

    @Override
    public void deleteCostOrderItemById(List<Long> idList) {
        mapper.deleteCostOrderItemById(idList);
    }

    @Override
    public List<PlatformTemporaryCostOrderItemDTO> listPlatformTemporaryCostItemDTO(PlatformTemporaryCostOrderItemDTO dto) {
        return mapper.listPlatformTemporaryCostItemDTO(dto);
    }
    
    /**
     * 根据orderCode单号删除 
     */
	@Override
	public int deleteByOrderCode(List<String> orderCodeList) throws SQLException {
		return mapper.deleteByOrderCode(orderCodeList);
	}
}