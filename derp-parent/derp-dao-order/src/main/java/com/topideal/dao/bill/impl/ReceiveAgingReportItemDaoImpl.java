package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.ReceiveAgingReportItemDao;
import com.topideal.entity.dto.bill.ReceiveAgingReportItemDTO;
import com.topideal.entity.vo.bill.ReceiveAgingReportItemModel;
import com.topideal.mapper.bill.ReceiveAgingReportItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ReceiveAgingReportItemDaoImpl implements ReceiveAgingReportItemDao {

    @Autowired
    private ReceiveAgingReportItemMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReceiveAgingReportItemModel> list(ReceiveAgingReportItemModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReceiveAgingReportItemModel model) throws SQLException {
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
    public int modify(ReceiveAgingReportItemModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReceiveAgingReportItemModel  searchByPage(ReceiveAgingReportItemModel  model) throws SQLException{
        PageDataModel<ReceiveAgingReportItemModel> pageModel=mapper.listByPage(model);
        return (ReceiveAgingReportItemModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReceiveAgingReportItemModel  searchById(Long id)throws SQLException {
        ReceiveAgingReportItemModel  model=new ReceiveAgingReportItemModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReceiveAgingReportItemModel searchByModel(ReceiveAgingReportItemModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<ReceiveAgingReportItemDTO> getAgingReportId(Long id) {
        return mapper.getAgingReportId(id) ;
    }

    @Override
    public void deleteReceiveAgingReportItem(List<Long> ids) {
        mapper.deleteReceiveAgingReportItem(ids);
    }

    @Override
    public int countTempBillNum(ReceiveAgingReportItemDTO model) {
        return mapper.countTempBillNum(model);
    }

    @Override
    public List<ReceiveAgingReportItemDTO> listForExportTempItemPage(ReceiveAgingReportItemDTO dto) throws SQLException {
        return mapper.listForExportTempItemPage(dto);
    }
}