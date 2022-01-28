package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.PlatformStatementItemDao;
import com.topideal.entity.dto.bill.PlatformStatementItemDTO;
import com.topideal.entity.vo.bill.PlatformStatementItemModel;
import com.topideal.mapper.bill.PlatformStatementItemMapper;
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
public class PlatformStatementItemDaoImpl implements PlatformStatementItemDao {

    @Autowired
    private PlatformStatementItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PlatformStatementItemModel> list(PlatformStatementItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PlatformStatementItemModel model) throws SQLException {
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
    public int modify(PlatformStatementItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PlatformStatementItemModel  searchByPage(PlatformStatementItemModel  model) throws SQLException{
        PageDataModel<PlatformStatementItemModel> pageModel=mapper.listByPage(model);
        return (PlatformStatementItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PlatformStatementItemModel  searchById(Long id)throws SQLException {
        PlatformStatementItemModel  model=new PlatformStatementItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PlatformStatementItemModel searchByModel(PlatformStatementItemModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 根据订单ID删除
	 * @param platformStatementIds
	 * @return
	 */
	@Override
	public Integer deleteByOrderId(Long platformStatementIds) {
		return mapper.deleteByOrderId(platformStatementIds);
	}
	@Override
	public PlatformStatementItemDTO getListByPage(PlatformStatementItemDTO dto) {
		PageDataModel<PlatformStatementItemDTO> pageModel=mapper.getListByPage(dto);
        return (PlatformStatementItemDTO ) pageModel.getModel();
	}

    /**
     * 根据选择开票的平台结算单id根据商品和品牌维度统计开票数据
     * @param ids
     * @return
     */
    @Override
    public List<Map<String, Object>> listInvoiceItem(List<Long> ids, String source) throws SQLException {
        return mapper.listInvoiceItem(ids, source);
    }

    /**
     * 根据选择开票的平台结算单id根据商品和类型统计开票数据
     * @param ids
     * @return
     */
    @Override
    public List<Map<String, Object>> listInvoiceItemByType(List<Long> ids) throws SQLException {
        return mapper.listInvoiceItemByType(ids);
    }

    @Override
    public Map<String, Object> statisticsByPlatformIdList(Long billId) throws SQLException {
        return mapper.statisticsByPlatformIdList(billId);
    }
}