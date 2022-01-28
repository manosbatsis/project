package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.PurchaseReturnItemDao;
import com.topideal.entity.dto.purchase.PurchaseReturnItemDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnItemModel;
import com.topideal.mapper.purchase.PurchaseReturnItemMapper;
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
public class PurchaseReturnItemDaoImpl implements PurchaseReturnItemDao {

    @Autowired
    private PurchaseReturnItemMapper mapper;

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseReturnItemModel> list(PurchaseReturnItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseReturnItemModel model) throws SQLException {
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
    public int modify(PurchaseReturnItemModel  model) throws SQLException {
        return mapper.update(model);
    }

	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseReturnItemModel  searchByPage(PurchaseReturnItemModel  model) throws SQLException{
        PageDataModel<PurchaseReturnItemModel> pageModel=mapper.listByPage(model);
        return (PurchaseReturnItemModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseReturnItemModel  searchById(Long id)throws SQLException {
        PurchaseReturnItemModel  model=new PurchaseReturnItemModel ();
        model.setId(id);
        return mapper.get(model);
    }

      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseReturnItemModel searchByModel(PurchaseReturnItemModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public int delByMap(Map<String, Object> delMap) {
		return mapper.delByMap(delMap);
	}

    @Override
    public PurchaseReturnItemDTO getPurchaseItemPopupByPage(PurchaseReturnItemDTO dto) {
        PageDataModel<PurchaseReturnItemModel> pageModel=mapper.getPurchaseItemPopupByPage(dto);
	    return (PurchaseReturnItemDTO) pageModel.getModel();
    }

    @Override
    public Integer getTotalRetrurnNum(Map<String, Object> paramMap) {
        return mapper.getTotalRetrurnNum(paramMap);
    }


}
