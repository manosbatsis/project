package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TocTemporaryReceiveBillDao;
import com.topideal.entity.dto.bill.*;
import com.topideal.entity.vo.bill.TocTemporaryReceiveBillModel;
import com.topideal.mapper.bill.TocTemporaryReceiveBillMapper;
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
public class TocTemporaryReceiveBillDaoImpl implements TocTemporaryReceiveBillDao {

    @Autowired
    private TocTemporaryReceiveBillMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TocTemporaryReceiveBillModel> list(TocTemporaryReceiveBillModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TocTemporaryReceiveBillModel model) throws SQLException {
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
    public int modify(TocTemporaryReceiveBillModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TocTemporaryReceiveBillModel  searchByPage(TocTemporaryReceiveBillModel  model) throws SQLException{
        PageDataModel<TocTemporaryReceiveBillModel> pageModel=mapper.listByPage(model);
        return (TocTemporaryReceiveBillModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TocTemporaryReceiveBillModel  searchById(Long id)throws SQLException {
        TocTemporaryReceiveBillModel  model=new TocTemporaryReceiveBillModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TocTemporaryReceiveBillModel searchByModel(TocTemporaryReceiveBillModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public Integer batchSave(List<TocTemporaryReceiveBillModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public TocTemporaryReceiveBillDTO listTocTempReceiveBillByPage(TocTemporaryReceiveBillDTO dto) throws SQLException {
        PageDataModel<TocTemporaryReceiveBillDTO> pageModel = mapper.listTocTempReceiveBillByPage(dto);
        return (TocTemporaryReceiveBillDTO ) pageModel.getModel();
    }

    @Override
    public Integer deleteByModel(TocTemporaryReceiveBillModel model) throws SQLException {
        return mapper.deleteByModel(model);
    }

    @Override
    public TocTemporaryReceiveBillDTO searchDTOById(Long id) {
        TocTemporaryReceiveBillDTO model=new TocTemporaryReceiveBillDTO ();
        model.setId(id);
        return mapper.getDTO(model);
    }

    @Override
    public List<TocTemporaryReceiveBillDTO> listForExport(TocTemporaryReceiveBillDTO dto) {
        return mapper.listForExport(dto);
    }

    @Override
    public int updateWithNull(TocTemporaryReceiveBillModel model) throws SQLException {
        return mapper.updateWithNull(model);
    }

    @Override
    public Map<String, BigDecimal> getByToCReceive(Map<String, Object> queryMap) throws SQLException {
        return mapper.getByToCReceive(queryMap);
    }

    @Override
    public List<Map<String, Object>> getItemSearchList(Map<String, Object> queryMap) {
        return mapper.getItemSearchList(queryMap);
    }

    @Override
    public List<TocTemporaryReceiveBillDTO> listBySearchQuery(Map<String, Object> queryMap) {
        return mapper.listBySearchQuery(queryMap);
    }

    @Override
    public List<Long> searchIdListByModel(TocTemporaryReceiveBillModel alreadyModel) {
        return mapper.searchIdListByModel(alreadyModel);
    }


}