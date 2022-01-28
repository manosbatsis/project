package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TobTemporaryReceiveBillItemDao;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillItemDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillItemMonthlyDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillItemModel;
import com.topideal.mapper.bill.TobTemporaryReceiveBillItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class TobTemporaryReceiveBillItemDaoImpl implements TobTemporaryReceiveBillItemDao {

    @Autowired
    private TobTemporaryReceiveBillItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TobTemporaryReceiveBillItemModel> list(TobTemporaryReceiveBillItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TobTemporaryReceiveBillItemModel model) throws SQLException {
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
    public int modify(TobTemporaryReceiveBillItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TobTemporaryReceiveBillItemModel  searchByPage(TobTemporaryReceiveBillItemModel  model) throws SQLException{
        PageDataModel<TobTemporaryReceiveBillItemModel> pageModel=mapper.listByPage(model);
        return (TobTemporaryReceiveBillItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TobTemporaryReceiveBillItemModel  searchById(Long id)throws SQLException {
        TobTemporaryReceiveBillItemModel  model=new TobTemporaryReceiveBillItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TobTemporaryReceiveBillItemModel searchByModel(TobTemporaryReceiveBillItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public Integer batchSave(List<TobTemporaryReceiveBillItemModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public int delItemsByBillIds(List<Long> billIds) throws SQLException {
        return mapper.delItemsByBillIds(billIds);
    }

    @Override
    public BigDecimal listItemPriceByIds(TobTemporaryReceiveBillItemMonthlyDTO itemMonthlyDTO) throws SQLException {
        return mapper.listItemPriceByIds(itemMonthlyDTO);
    }


    @Override
    public TobTemporaryReceiveBillItemDTO listToBTempItemByPage(TobTemporaryReceiveBillItemDTO dto) throws SQLException {
        PageDataModel<TobTemporaryReceiveBillItemDTO> pageModel = mapper.listToBTempItemByPage(dto);
        return (TobTemporaryReceiveBillItemDTO ) pageModel.getModel();
    }

    @Override
    public Integer getTempDetailsCount(TobTemporaryReceiveBillDTO dto) throws Exception {
        return mapper.getTempDetailsCount(dto);
    }

    @Override
    public List<TobTemporaryReceiveBillItemDTO> listToBeVerifyItems(TobTemporaryReceiveBillDTO dto) throws Exception {
        return mapper.listToBeVerifyItems(dto);
    }

    @Override
    public List<TobTemporaryReceiveBillItemModel> listByBillIds(List<Long> billIds) throws Exception {
        return mapper.listByBillIds(billIds);
    }

    @Override
    public List<TobTemporaryReceiveBillItemModel> listNonVerifyByCloseAccount(Long merchantId, Long buId, String month) throws Exception {
        return mapper.listNonVerifyByCloseAccount(merchantId, buId, month);
    }

    @Override
    public List<TobTemporaryReceiveBillItemModel> listAllVerifyByCloseAccount(Long merchantId, Long buId, String month) throws Exception {
        return mapper.listAllVerifyByCloseAccount(merchantId, buId, month);
    }
}