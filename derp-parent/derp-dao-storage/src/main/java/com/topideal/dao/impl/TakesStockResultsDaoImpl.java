package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.TakesStockResultsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.TakesStockResultsDao;
import com.topideal.entity.vo.TakesStockResultsModel;
import com.topideal.mapper.TakesStockResultsMapper;

/**盘点结果表
 * @author lchenxing
 */
@Repository
public class TakesStockResultsDaoImpl implements TakesStockResultsDao {

    @Autowired
    private TakesStockResultsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TakesStockResultsModel> list(TakesStockResultsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TakesStockResultsModel model) throws SQLException {
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
    public int modify(TakesStockResultsModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TakesStockResultsModel  searchByPage(TakesStockResultsModel  model) throws SQLException{
        PageDataModel<TakesStockResultsModel> pageModel=mapper.getListByPage(model);
        return (TakesStockResultsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TakesStockResultsModel  searchById(Long id)throws SQLException {
        TakesStockResultsModel  model=new TakesStockResultsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TakesStockResultsModel searchByModel(TakesStockResultsModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public TakesStockResultsDTO searchDTOByPage(TakesStockResultsDTO dto) throws SQLException {
        PageDataModel<TakesStockResultsDTO> pageModel = mapper.getDTOListByPage(dto);
        return (TakesStockResultsDTO) pageModel.getModel();
    }

    @Override
    public TakesStockResultsDTO getDetail(Long id) throws SQLException {
        return mapper.getDetail(id);
    }

    @Override
    public List<Map<String, Object>> getItemByResultIds(List<Long> resultIds) {
        return mapper.getItemByResultIds(resultIds);
    }

    @Override
    public List<TakesStockResultsModel> getListByIds(List<Long> ids) throws SQLException {
        return mapper.getListByIds(ids);
    }

    @Override
    public List<Map<String, Object>> listForExport(TakesStockResultsDTO dto) {
        return mapper.listForExport(dto);
    }

    @Override
    public List<Map<String, Object>> listForExportItem(TakesStockResultsDTO dto) {
        return mapper.listForExportItem(dto);
    }
}
