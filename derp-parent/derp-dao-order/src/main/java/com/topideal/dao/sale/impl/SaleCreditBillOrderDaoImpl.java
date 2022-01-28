package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.SaleCreditBillOrderDao;
import com.topideal.entity.dto.sale.OccupationCapitalStatisticsDTO;
import com.topideal.entity.vo.sale.SaleCreditBillOrderModel;
import com.topideal.mapper.sale.SaleCreditBillOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SaleCreditBillOrderDaoImpl implements SaleCreditBillOrderDao {

    @Autowired
    private SaleCreditBillOrderMapper mapper;

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SaleCreditBillOrderModel> list(SaleCreditBillOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SaleCreditBillOrderModel model) throws SQLException {
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
    public int modify(SaleCreditBillOrderModel  model) throws SQLException {
        return mapper.update(model);
    }

	/**
     * 分页查询
     * @param model
     */
    @Override
    public SaleCreditBillOrderModel  searchByPage(SaleCreditBillOrderModel  model) throws SQLException{
        PageDataModel<SaleCreditBillOrderModel> pageModel=mapper.listByPage(model);
        return (SaleCreditBillOrderModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SaleCreditBillOrderModel  searchById(Long id)throws SQLException {
        SaleCreditBillOrderModel  model=new SaleCreditBillOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }

      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SaleCreditBillOrderModel searchByModel(SaleCreditBillOrderModel model) throws SQLException {
		return mapper.get(model);
	}

	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

    @Override
    public void batchUpdate(List<SaleCreditBillOrderModel> saleCreditBillOrderModels) {
        mapper.batchUpdate(saleCreditBillOrderModels);
    }

    @Override
    public OccupationCapitalStatisticsDTO listOccupationCapitalDTOByPage(OccupationCapitalStatisticsDTO dto) {
        PageDataModel<OccupationCapitalStatisticsDTO> pageModel = mapper.listOccupationCapitalDTOByPage(dto);
	    return (OccupationCapitalStatisticsDTO) pageModel.getModel();
    }

    @Override
    public Integer getOccupationCapitalCount(OccupationCapitalStatisticsDTO dto) {
        return mapper.getOccupationCapitalCount(dto);
    }

    @Override
    public List<OccupationCapitalStatisticsDTO> listOccupationCapitalDTO(OccupationCapitalStatisticsDTO dto) {
        return mapper.listOccupationCapitalDTO(dto);
    }
}
