package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TobTemporaryReceiveBillRebateItemDao;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillCostItemMonthlyDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillRebateItemDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillRebateItemModel;
import com.topideal.mapper.bill.TobTemporaryReceiveBillRebateItemMapper;
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
public class TobTemporaryReceiveBillRebateItemDaoImpl implements TobTemporaryReceiveBillRebateItemDao {

    @Autowired
    private TobTemporaryReceiveBillRebateItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TobTemporaryReceiveBillRebateItemModel> list(TobTemporaryReceiveBillRebateItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TobTemporaryReceiveBillRebateItemModel model) throws SQLException {
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
    public int modify(TobTemporaryReceiveBillRebateItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TobTemporaryReceiveBillRebateItemModel  searchByPage(TobTemporaryReceiveBillRebateItemModel  model) throws SQLException{
        PageDataModel<TobTemporaryReceiveBillRebateItemModel> pageModel=mapper.listByPage(model);
        return (TobTemporaryReceiveBillRebateItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TobTemporaryReceiveBillRebateItemModel  searchById(Long id)throws SQLException {
        TobTemporaryReceiveBillRebateItemModel  model=new TobTemporaryReceiveBillRebateItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TobTemporaryReceiveBillRebateItemModel searchByModel(TobTemporaryReceiveBillRebateItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public Integer batchSave(List<TobTemporaryReceiveBillRebateItemModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public int delItemsByBillIds(List<Long> billIds) throws SQLException {
        return mapper.delItemsByBillIds(billIds);
    }

    @Override
    public BigDecimal listRebateItemPriceByIds(TobTemporaryReceiveBillCostItemMonthlyDTO costItemMonthlyDTO) throws SQLException {
        return mapper.listRebateItemPriceByIds(costItemMonthlyDTO);
    }

    @Override
    public TobTemporaryReceiveBillRebateItemDTO listToBTempRebateItemByPage(TobTemporaryReceiveBillRebateItemDTO dto) throws Exception {
        PageDataModel<TobTemporaryReceiveBillRebateItemDTO> pageModel = mapper.listToBTempRebateItemByPage(dto);
        return (TobTemporaryReceiveBillRebateItemDTO ) pageModel.getModel();
    }

    @Override
    public Integer getRebateTempDetailsCount(TobTemporaryReceiveBillDTO dto) throws Exception {
        return mapper.getRebateTempDetailsCount(dto);
    }

    @Override
    public List<TobTemporaryReceiveBillRebateItemDTO> getVerifyRebateItems(TobTemporaryReceiveBillDTO dto) throws SQLException {
        return mapper.getVerifyRebateItems(dto);
    }

    @Override
    public List<TobTemporaryReceiveBillRebateItemModel> listByBillIds(List<Long> billIds) throws Exception {
        return mapper.listByBillIds(billIds);
    }

    @Override
    public List<TobTemporaryReceiveBillRebateItemModel> listNonVerifyByCloseAccount(Long merchantId, Long buId, String month) throws Exception {
        return mapper.listNonVerifyByCloseAccount(merchantId, buId, month);
    }

    @Override
    public List<TobTemporaryReceiveBillRebateItemModel> listAllVerifyByCloseAccount(Long merchantId, Long buId, String month) throws Exception {
        return mapper.listAllVerifyByCloseAccount(merchantId, buId, month);
    }

    @Override
    public List<TobTemporaryReceiveBillRebateItemModel> getWriteOffNonVerifyItems(Long merchantId, List<String> sdCodes, String isWriteOff) throws Exception {
        return mapper.getWriteOffNonVerifyItems(merchantId, sdCodes, isWriteOff);
    }

    @Override
    public List<String> getVerifySdCodes(Long merchantId) throws Exception {
        return mapper.getVerifySdCodes(merchantId);
    }


}