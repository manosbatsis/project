package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SaleDeclareOrderDao;
import com.topideal.entity.dto.sale.SaleDeclareOrderDTO;
import com.topideal.entity.vo.sale.SaleDeclareOrderModel;
import com.topideal.mapper.sale.SaleDeclareOrderMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SaleDeclareOrderDaoImpl implements SaleDeclareOrderDao {

    @Autowired
    private SaleDeclareOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleDeclareOrderModel> list(SaleDeclareOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleDeclareOrderModel model) throws SQLException {
        model.setCreateDate(TimeUtils.getNow());
        model.setModifyDate(TimeUtils.getNow());
        int num=mapper.insert(model);
        if(num==1){
            return model.getId();
        }
        return null;
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(SaleDeclareOrderModel  model) throws SQLException {
        model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleDeclareOrderModel  searchByPage(SaleDeclareOrderModel  model) throws SQLException{
        PageDataModel<SaleDeclareOrderModel> pageModel=mapper.listByPage(model);
        return (SaleDeclareOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleDeclareOrderModel  searchById(Long id)throws SQLException {
        SaleDeclareOrderModel  model=new SaleDeclareOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleDeclareOrderModel searchByModel(SaleDeclareOrderModel model) throws SQLException {
		return mapper.get(model);
	}
	
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}
	@Override
	public SaleDeclareOrderDTO listDTOByPage(SaleDeclareOrderDTO dto) throws SQLException {
		PageDataModel<SaleDeclareOrderDTO> list = mapper.listDTOByPage(dto);
		return (SaleDeclareOrderDTO)list.getModel();
	}
	@Override
	public List<SaleDeclareOrderDTO> listSaleDeclareOrder(SaleDeclareOrderDTO dto) throws SQLException {
		return mapper.listSaleDeclareOrder(dto);
	}
	@Override
	public SaleDeclareOrderDTO searchDetail(SaleDeclareOrderDTO dto) throws SQLException {
		return mapper.searchDetail(dto);
	}
	@Override
	public List<Map<String, Object>> getDeclareTypeNum(SaleDeclareOrderDTO dto) {
		return mapper.getDeclareTypeNum(dto);
	}
	/**
     * 修改
     * @param model
     */
    @Override
    public int modifyWithNull(SaleDeclareOrderModel  model) throws SQLException {
        model.setModifyDate(TimeUtils.getNow());
        return mapper.modifyWithNull(model);
    }
}