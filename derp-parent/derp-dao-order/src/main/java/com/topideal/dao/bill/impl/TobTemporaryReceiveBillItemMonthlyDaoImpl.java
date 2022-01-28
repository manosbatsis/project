package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.TobTemporaryReceiveBillItemMonthlyDao;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillItemDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillItemMonthlyDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillItemMonthlyModel;
import com.topideal.mapper.bill.TobTemporaryReceiveBillItemMonthlyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class TobTemporaryReceiveBillItemMonthlyDaoImpl implements TobTemporaryReceiveBillItemMonthlyDao {

    @Autowired
    private TobTemporaryReceiveBillItemMonthlyMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TobTemporaryReceiveBillItemMonthlyModel> list(TobTemporaryReceiveBillItemMonthlyModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TobTemporaryReceiveBillItemMonthlyModel model) throws SQLException {
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
    public int modify(TobTemporaryReceiveBillItemMonthlyModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TobTemporaryReceiveBillItemMonthlyModel  searchByPage(TobTemporaryReceiveBillItemMonthlyModel  model) throws SQLException{
        PageDataModel<TobTemporaryReceiveBillItemMonthlyModel> pageModel=mapper.listByPage(model);
        return (TobTemporaryReceiveBillItemMonthlyModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TobTemporaryReceiveBillItemMonthlyModel  searchById(Long id)throws SQLException {
        TobTemporaryReceiveBillItemMonthlyModel  model=new TobTemporaryReceiveBillItemMonthlyModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TobTemporaryReceiveBillItemMonthlyModel searchByModel(TobTemporaryReceiveBillItemMonthlyModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public int deleteByModel(TobTemporaryReceiveBillItemMonthlyModel itemMonthlyModel) throws SQLException {
        return mapper.deleteByModel(itemMonthlyModel);
    }

    @Override
    public Integer batchSave(List<TobTemporaryReceiveBillItemMonthlyModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public TobTemporaryReceiveBillItemMonthlyDTO listToBTempRevenueMonthlyByPage(TobTemporaryReceiveBillItemMonthlyDTO dto) throws SQLException {
        PageDataModel<TobTemporaryReceiveBillItemMonthlyDTO> pageModel = mapper.listToBTempRevenueMonthlyByPage(dto);
        return (TobTemporaryReceiveBillItemMonthlyDTO ) pageModel.getModel();
    }

    @Override
    public List<TobTemporaryReceiveBillItemMonthlyDTO> listByDto(TobTemporaryReceiveBillItemMonthlyDTO dto) throws SQLException {
        return mapper.listByDto(dto);
    }

    @Override
    public List<TobTemporaryReceiveBillItemMonthlyDTO> getMonthlyVerify(TobTemporaryReceiveBillItemMonthlyDTO dto) throws SQLException {
        return mapper.getMonthlyVerify(dto);
    }


}