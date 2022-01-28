package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TobTemporaryReceiveBillCostItemMonthlyDao;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillCostItemMonthlyDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillItemMonthlyDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillCostItemMonthlyModel;
import com.topideal.mapper.bill.TobTemporaryReceiveBillCostItemMonthlyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class TobTemporaryReceiveBillCostItemMonthlyDaoImpl implements TobTemporaryReceiveBillCostItemMonthlyDao {

    @Autowired
    private TobTemporaryReceiveBillCostItemMonthlyMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TobTemporaryReceiveBillCostItemMonthlyModel> list(TobTemporaryReceiveBillCostItemMonthlyModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TobTemporaryReceiveBillCostItemMonthlyModel model) throws SQLException {
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
    public int modify(TobTemporaryReceiveBillCostItemMonthlyModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TobTemporaryReceiveBillCostItemMonthlyModel  searchByPage(TobTemporaryReceiveBillCostItemMonthlyModel  model) throws SQLException{
        PageDataModel<TobTemporaryReceiveBillCostItemMonthlyModel> pageModel=mapper.listByPage(model);
        return (TobTemporaryReceiveBillCostItemMonthlyModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TobTemporaryReceiveBillCostItemMonthlyModel  searchById(Long id)throws SQLException {
        TobTemporaryReceiveBillCostItemMonthlyModel  model=new TobTemporaryReceiveBillCostItemMonthlyModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TobTemporaryReceiveBillCostItemMonthlyModel searchByModel(TobTemporaryReceiveBillCostItemMonthlyModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public int deleteByModel(TobTemporaryReceiveBillCostItemMonthlyModel itemMonthlyModel) throws SQLException {
        return mapper.deleteByModel(itemMonthlyModel);
    }

    @Override
    public Integer batchSave(List<TobTemporaryReceiveBillCostItemMonthlyModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public TobTemporaryReceiveBillCostItemMonthlyDTO listToBTempCostMonthlyByPage(TobTemporaryReceiveBillCostItemMonthlyDTO dto) throws SQLException {
        PageDataModel<TobTemporaryReceiveBillCostItemMonthlyDTO> pageModel = mapper.listToBTempCostMonthlyByPage(dto);
        return (TobTemporaryReceiveBillCostItemMonthlyDTO ) pageModel.getModel();
    }

    @Override
    public List<TobTemporaryReceiveBillCostItemMonthlyDTO> listByDto(TobTemporaryReceiveBillCostItemMonthlyDTO dto) throws SQLException {
        return mapper.listByDto(dto);
    }

    @Override
    public List<TobTemporaryReceiveBillCostItemMonthlyDTO> getMonthlyVerify(TobTemporaryReceiveBillCostItemMonthlyDTO dto) throws SQLException {
        return mapper.getMonthlyVerify(dto);
    }
}