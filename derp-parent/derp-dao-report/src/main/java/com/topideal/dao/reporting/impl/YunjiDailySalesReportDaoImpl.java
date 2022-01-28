package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.YunjiDailySalesReportDao;
import com.topideal.entity.vo.reporting.YunjiDailySalesReportModel;
import com.topideal.mapper.reporting.YunjiDailySalesReportMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class YunjiDailySalesReportDaoImpl implements YunjiDailySalesReportDao {

    @Autowired
    private YunjiDailySalesReportMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<YunjiDailySalesReportModel> list(YunjiDailySalesReportModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(YunjiDailySalesReportModel model) throws SQLException {
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
    public int modify(YunjiDailySalesReportModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public YunjiDailySalesReportModel  searchByPage(YunjiDailySalesReportModel  model) throws SQLException{
        PageDataModel<YunjiDailySalesReportModel> pageModel=mapper.listByPage(model);
        return (YunjiDailySalesReportModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public YunjiDailySalesReportModel  searchById(Long id)throws SQLException {
        YunjiDailySalesReportModel  model=new YunjiDailySalesReportModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public YunjiDailySalesReportModel searchByModel(YunjiDailySalesReportModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 删除云集采销日报
	 */
	@Override
	public void deleteYunjiDailySalesReport(YunjiDailySalesReportModel model) throws SQLException {
		mapper.deleteYunjiDailySalesReport(model);
		
	}
	/**
	 * 云集采销日报分页
	 */
	@Override
	public YunjiDailySalesReportModel getListByPage(YunjiDailySalesReportModel model) throws SQLException {
		List<YunjiDailySalesReportModel> listPage = mapper.getListPage(model);
		int count = mapper.getDailySalesReportCount(model);
		model.setList(listPage);
		model.setTotal(count);
		return model;
	}
	/**
	 * 云集采销日报导出
	 */
	@Override
	public List<YunjiDailySalesReportModel> listDailySalesReportExport(YunjiDailySalesReportModel model)throws SQLException {
		return mapper.listDailySalesReportExport(model);
	}
	/**
	 * 导出云集日报的 数量
	 */
	@Override
	public int getDailySalesReportCount(YunjiDailySalesReportModel model) throws SQLException {
		return mapper.getDailySalesReportCount(model);
	}

}