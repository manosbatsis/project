package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.PlatformTemporaryCostOrderDao;
import com.topideal.entity.dto.sale.PlatformTemporaryCostOrderDTO;
import com.topideal.entity.vo.sale.PlatformTemporaryCostOrderModel;
import com.topideal.mapper.sale.PlatformTemporaryCostOrderMapper;
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
public class PlatformTemporaryCostOrderDaoImpl implements PlatformTemporaryCostOrderDao {

    @Autowired
    private PlatformTemporaryCostOrderMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PlatformTemporaryCostOrderModel> list(PlatformTemporaryCostOrderModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PlatformTemporaryCostOrderModel model) throws SQLException {
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
    public int modify(PlatformTemporaryCostOrderModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PlatformTemporaryCostOrderModel  searchByPage(PlatformTemporaryCostOrderModel  model) throws SQLException{
        PageDataModel<PlatformTemporaryCostOrderModel> pageModel=mapper.listByPage(model);
        return (PlatformTemporaryCostOrderModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PlatformTemporaryCostOrderModel  searchById(Long id)throws SQLException {
        PlatformTemporaryCostOrderModel  model=new PlatformTemporaryCostOrderModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PlatformTemporaryCostOrderModel searchByModel(PlatformTemporaryCostOrderModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public Integer batchSave(List<PlatformTemporaryCostOrderModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public PlatformTemporaryCostOrderDTO listDTOByPage(PlatformTemporaryCostOrderDTO dto) {
        PageDataModel<PlatformTemporaryCostOrderDTO> pageModel = mapper.listDTOByPage(dto);
        return (PlatformTemporaryCostOrderDTO) pageModel.getModel();
    }

    @Override
    public PlatformTemporaryCostOrderDTO searchByDTOId(Long id) {
        return mapper.searchByDTOId(id);
    }

    @Override
    public List<Map<String, Object>> listForMapItem(PlatformTemporaryCostOrderDTO dto) {
        return mapper.listForMapItem(dto);
    }

    @Override
    public Integer getPlatFormTemporaryCount(PlatformTemporaryCostOrderDTO dto) {
        return mapper.getPlatFormTemporaryCount(dto);
    }

    @Override
    public List<PlatformTemporaryCostOrderModel> listByExternalCodes(List<String> externalCodes, Long merchantId) throws SQLException {
        return mapper.listByExternalCodes(externalCodes, merchantId);
    }

    @Override
    public List<Map<String, Object>> countOrderByDTO(PlatformTemporaryCostOrderDTO dto) throws SQLException {
        return mapper.countOrderByDTO(dto);
    }

    @Override
    public List<PlatformTemporaryCostOrderDTO> listTempOrderDTO(PlatformTemporaryCostOrderDTO dto) throws SQLException {
        return mapper.listTempOrderDTO(dto);
    }

    @Override
    public List<PlatformTemporaryCostOrderDTO> listDistinctOrderByDTO(PlatformTemporaryCostOrderDTO costOrderDTO) {
        return mapper.listDistinctOrderByDTO(costOrderDTO);
    }

    @Override
    public Long statisticsDistinctByDTO(PlatformTemporaryCostOrderDTO costOrderDTO) {
        return mapper.statisticsDistinctByDTO(costOrderDTO);
    }
    /**
     * 根据orderCode单号删除 
     */
	@Override
	public int deleteByOrderCode(List<String> orderCodeList) throws SQLException {
		return mapper.deleteByOrderCode(orderCodeList);
	}
}