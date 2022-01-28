package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TocTemporaryReceiveBillCostItemMonthlyDao;
import com.topideal.entity.dto.bill.TocTempCostBillItemMonthlyDTO;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillCostItemMonthlyModel;
import com.topideal.mapper.bill.TocTemporaryReceiveBillCostItemMonthlyMapper;
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
public class TocTemporaryReceiveBillCostItemMonthlyDaoImpl implements TocTemporaryReceiveBillCostItemMonthlyDao {

    @Autowired
    private TocTemporaryReceiveBillCostItemMonthlyMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TocTemporaryReceiveBillCostItemMonthlyModel> list(TocTemporaryReceiveBillCostItemMonthlyModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TocTemporaryReceiveBillCostItemMonthlyModel model) throws SQLException {
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
    public int modify(TocTemporaryReceiveBillCostItemMonthlyModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TocTemporaryReceiveBillCostItemMonthlyModel  searchByPage(TocTemporaryReceiveBillCostItemMonthlyModel  model) throws SQLException{
        PageDataModel<TocTemporaryReceiveBillCostItemMonthlyModel> pageModel=mapper.listByPage(model);
        return (TocTemporaryReceiveBillCostItemMonthlyModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TocTemporaryReceiveBillCostItemMonthlyModel  searchById(Long id)throws SQLException {
        TocTemporaryReceiveBillCostItemMonthlyModel  model=new TocTemporaryReceiveBillCostItemMonthlyModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TocTemporaryReceiveBillCostItemMonthlyModel searchByModel(TocTemporaryReceiveBillCostItemMonthlyModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public TocTempCostBillItemMonthlyDTO listDTOByPage(TocTempCostBillItemMonthlyDTO dto) {
        PageDataModel<TocTempCostBillItemMonthlyDTO> pageModel = mapper.listDTOByPage(dto);
        return (TocTempCostBillItemMonthlyDTO ) pageModel.getModel();

    }

    @Override
    public void deleteByModel(TocTemporaryReceiveBillCostItemMonthlyModel model) {
        mapper.deleteByModel(model);
    }

    @Override
    public void batchSaveByBillItemList(List<TocTemporaryReceiveBillCostItemMonthlyModel> itemList) {
        mapper.batchSaveByBillItemList(itemList);
    }

    @Override
    public void deleteByDTO(TocTempCostBillItemMonthlyDTO dto) {
        mapper.deleteByDTO(dto);
    }

    @Override
    public int countByDTO(TocTempCostBillItemMonthlyDTO dto) {
        return mapper.countByDTO(dto);
    }

    @Override
    public List<Map<String, Object>> listSumForExport(TocTempCostBillItemMonthlyDTO dto) {
        return mapper.listSumForExport(dto);
    }

    @Override
    public List<Map<String, Object>> listForMapItem(TocTempCostBillItemMonthlyDTO dto) {
        return mapper.listForMapItem(dto);
    }

    @Override
    public List<TocTempCostBillItemMonthlyDTO> getMonthlyNonVerify(TocTempCostBillItemMonthlyDTO dto) {
        return mapper.getMonthlyNonVerify(dto);
    }
}