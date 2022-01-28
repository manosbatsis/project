package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.PlatformPurchaseOrderItemDao;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderItemDTO;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderItemModel;
import com.topideal.mapper.sale.PlatformPurchaseOrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PlatformPurchaseOrderItemDaoImpl implements PlatformPurchaseOrderItemDao {

    @Autowired
    private PlatformPurchaseOrderItemMapper mapper;

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PlatformPurchaseOrderItemModel> list(PlatformPurchaseOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PlatformPurchaseOrderItemModel model) throws SQLException {
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
    public int modify(PlatformPurchaseOrderItemModel  model) throws SQLException {
        return mapper.update(model);
    }

	/**
     * 分页查询
     * @param model
     */
    @Override
    public PlatformPurchaseOrderItemModel  searchByPage(PlatformPurchaseOrderItemModel  model) throws SQLException{
        PageDataModel<PlatformPurchaseOrderItemModel> pageModel=mapper.listByPage(model);
        return (PlatformPurchaseOrderItemModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PlatformPurchaseOrderItemModel  searchById(Long id)throws SQLException {
        PlatformPurchaseOrderItemModel  model=new PlatformPurchaseOrderItemModel ();
        model.setId(id);
        return mapper.get(model);
    }

      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PlatformPurchaseOrderItemModel searchByModel(PlatformPurchaseOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<PlatformPurchaseOrderItemDTO> listDTO(PlatformPurchaseOrderItemDTO dto) {
        return mapper.listDTO(dto);
    }
}
