package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.ReceiveAgingReportDao;
import com.topideal.entity.dto.bill.ReceiveAgingReportDTO;
import com.topideal.entity.vo.bill.ReceiveAgingReportModel;
import com.topideal.mapper.bill.ReceiveAgingReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ReceiveAgingReportDaoImpl implements ReceiveAgingReportDao {

    @Autowired
    private ReceiveAgingReportMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReceiveAgingReportModel> list(ReceiveAgingReportModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReceiveAgingReportModel model) throws SQLException {
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
    public int modify(ReceiveAgingReportModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReceiveAgingReportModel  searchByPage(ReceiveAgingReportModel  model) throws SQLException{
        PageDataModel<ReceiveAgingReportModel> pageModel=mapper.listByPage(model);
        return (ReceiveAgingReportModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReceiveAgingReportModel  searchById(Long id)throws SQLException {
        ReceiveAgingReportModel  model=new ReceiveAgingReportModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReceiveAgingReportModel searchByModel(ReceiveAgingReportModel model) throws SQLException {
		return mapper.get(model);
	}
    
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

    @Override
    public ReceiveAgingReportDTO listReceiveBillByPage(ReceiveAgingReportDTO dto) {
        PageDataModel<ReceiveAgingReportDTO> pageModel = mapper.getDTOListByPage(dto);
        return (ReceiveAgingReportDTO) pageModel.getModel();
    }

    @Override
    public List<ReceiveAgingReportDTO> listForExport(ReceiveAgingReportDTO dto) {
        return mapper.listForExport(dto);
    }

    @Override
    public void deleteByReceiveAging(Map<String,Object> searchQueryMap) {
         mapper.deleteByReceiveAging(searchQueryMap);
    }

    @Override
    public Map<String, Object> getMaxRefrshDate( Long merchantId) {
        return mapper.getMaxRefrshDate(merchantId);
    }

    @Override
    public List<ReceiveAgingReportDTO> listBySearchQuery(Map<String, Object> queryMap) {
        return mapper.listBySearchQuery(queryMap);
    }
}