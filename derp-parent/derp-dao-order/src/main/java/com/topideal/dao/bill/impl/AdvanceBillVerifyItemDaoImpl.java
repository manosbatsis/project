package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.AdvanceBillVerifyItemDao;
import com.topideal.entity.dto.bill.AdvanceBillVerifyItemDTO;
import com.topideal.entity.vo.bill.AdvanceBillVerifyItemModel;
import com.topideal.mapper.bill.AdvanceBillVerifyItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class AdvanceBillVerifyItemDaoImpl implements AdvanceBillVerifyItemDao {

    @Autowired
    private AdvanceBillVerifyItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<AdvanceBillVerifyItemModel> list(AdvanceBillVerifyItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(AdvanceBillVerifyItemModel model) throws SQLException {
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
    public int modify(AdvanceBillVerifyItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public AdvanceBillVerifyItemModel  searchByPage(AdvanceBillVerifyItemModel  model) throws SQLException{
        PageDataModel<AdvanceBillVerifyItemModel> pageModel=mapper.listByPage(model);
        return (AdvanceBillVerifyItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public AdvanceBillVerifyItemModel  searchById(Long id)throws SQLException {
        AdvanceBillVerifyItemModel  model=new AdvanceBillVerifyItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public AdvanceBillVerifyItemModel searchByModel(AdvanceBillVerifyItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<AdvanceBillVerifyItemDTO> getAdvanceById(long id) {
        return mapper.getAdvanceById(id);
    }

    @Override
    public int delItems(Long advanceId) throws SQLException {
        return mapper.delItems(advanceId);
    }

    @Override
    public List<AdvanceBillVerifyItemModel> getAdvancesByIds(List<Long> advanceIds) {
        return mapper.getAdvancesByIds(advanceIds);
    }

    @Override
    public BigDecimal getTotalVerifyPrice(Long advanceId) throws SQLException {
        return mapper.getTotalVerifyPrice(advanceId);
    }

    @Override
    public List<AdvanceBillVerifyItemModel> getLatestVerifyModelByAdvanceIds(List<Long> advanceIds) {
        return mapper.getLatestVerifyModelByAdvanceIds(advanceIds);
    }
}