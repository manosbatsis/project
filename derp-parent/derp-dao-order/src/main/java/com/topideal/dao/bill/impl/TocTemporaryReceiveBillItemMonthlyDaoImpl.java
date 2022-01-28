package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TocTemporaryReceiveBillItemMonthlyDao;
import com.topideal.entity.dto.bill.TocTempReceiveBillItemMonthlyDTO;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillItemMonthlyModel;
import com.topideal.mapper.bill.TocTemporaryReceiveBillItemMonthlyMapper;
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
public class TocTemporaryReceiveBillItemMonthlyDaoImpl implements TocTemporaryReceiveBillItemMonthlyDao {

    @Autowired
    private TocTemporaryReceiveBillItemMonthlyMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TocTemporaryReceiveBillItemMonthlyModel> list(TocTemporaryReceiveBillItemMonthlyModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TocTemporaryReceiveBillItemMonthlyModel model) throws SQLException {
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
    public int modify(TocTemporaryReceiveBillItemMonthlyModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TocTemporaryReceiveBillItemMonthlyModel  searchByPage(TocTemporaryReceiveBillItemMonthlyModel  model) throws SQLException{
        PageDataModel<TocTemporaryReceiveBillItemMonthlyModel> pageModel=mapper.listByPage(model);
        return (TocTemporaryReceiveBillItemMonthlyModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TocTemporaryReceiveBillItemMonthlyModel  searchById(Long id)throws SQLException {
        TocTemporaryReceiveBillItemMonthlyModel  model=new TocTemporaryReceiveBillItemMonthlyModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TocTemporaryReceiveBillItemMonthlyModel searchByModel(TocTemporaryReceiveBillItemMonthlyModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public TocTempReceiveBillItemMonthlyDTO listDTOByPage(TocTempReceiveBillItemMonthlyDTO dto) {
        PageDataModel<TocTempReceiveBillItemMonthlyDTO> pageModel = mapper.listDTOByPage(dto);
        return (TocTempReceiveBillItemMonthlyDTO ) pageModel.getModel();
    }

    @Override
    public void deleteByModel(TocTemporaryReceiveBillItemMonthlyModel model) {
        mapper.deleteByModel(model);
    }

    @Override
    public void batchSaveByBillItemList(List<TocTemporaryReceiveBillItemMonthlyModel> itemList) {
        mapper.batchSaveByBillItemList(itemList);
    }

    @Override
    public void deleteByDTO(TocTempReceiveBillItemMonthlyDTO dto) {
        mapper.deleteByDTO(dto);
    }

    @Override
    public int countByDTO(TocTempReceiveBillItemMonthlyDTO dto) {
        return mapper.countByDTO(dto);
    }

    @Override
    public List<Map<String, Object>> listForMapItem(TocTempReceiveBillItemMonthlyDTO dto) {
        return mapper.listForMapItem(dto);
    }

    @Override
    public List<Map<String, Object>> listSumForExport(TocTempReceiveBillItemMonthlyDTO dto) {
        return mapper.listSumForExport(dto);
    }

    @Override
    public List<TocTempReceiveBillItemMonthlyDTO> getMonthlyNonVerify(TocTempReceiveBillItemMonthlyDTO dto) {
        return mapper.getMonthlyNonVerify(dto);
    }
}