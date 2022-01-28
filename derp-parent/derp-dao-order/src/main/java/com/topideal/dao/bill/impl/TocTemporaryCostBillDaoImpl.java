package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TocTemporaryCostBillDao;
import com.topideal.entity.dto.bill.TocTemporaryCostBillDTO;
import com.topideal.entity.dto.sale.PlatformTemporaryCostOrderDTO;
import com.topideal.entity.vo.bill.TocTemporaryCostBillModel;
import com.topideal.mapper.bill.TocTemporaryCostBillMapper;
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
public class TocTemporaryCostBillDaoImpl implements TocTemporaryCostBillDao {

    @Autowired
    private TocTemporaryCostBillMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TocTemporaryCostBillModel> list(TocTemporaryCostBillModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TocTemporaryCostBillModel model) throws SQLException {
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
    public int modify(TocTemporaryCostBillModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TocTemporaryCostBillModel  searchByPage(TocTemporaryCostBillModel  model) throws SQLException{
        PageDataModel<TocTemporaryCostBillModel> pageModel=mapper.listByPage(model);
        return (TocTemporaryCostBillModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TocTemporaryCostBillModel  searchById(Long id)throws SQLException {
        TocTemporaryCostBillModel  model=new TocTemporaryCostBillModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TocTemporaryCostBillModel searchByModel(TocTemporaryCostBillModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public TocTemporaryCostBillDTO listTocTempCostReceiveBillByPage(TocTemporaryCostBillDTO dto) throws SQLException {
        PageDataModel<TocTemporaryCostBillDTO> pageModel = mapper.listTocTempCostReceiveBillByPage(dto);
        return (TocTemporaryCostBillDTO ) pageModel.getModel();
    }

    @Override
    public List<TocTemporaryCostBillDTO> listForExport(TocTemporaryCostBillDTO dto) {
        return mapper.listForExport(dto);
    }

    @Override
    public Integer deleteByModel(TocTemporaryCostBillModel model) throws SQLException {
        return mapper.deleteByModel(model);
    }

    @Override
    public TocTemporaryCostBillDTO searchDTOById(Long id) {
        TocTemporaryCostBillDTO dto = new TocTemporaryCostBillDTO();
        dto.setId(id);
        return mapper.getDTO(dto);
    }

    @Override
    public List<TocTemporaryCostBillDTO> getTocTemporaryCostList(Map<String, Object> queryMap) throws SQLException {
        return  mapper.getTocTemporaryCostList(queryMap);
    }

    @Override
    public List<Long> searchIdListByModel(TocTemporaryCostBillModel alreadyModel) {
        return  mapper.searchIdListByModel(alreadyModel);
    }
}