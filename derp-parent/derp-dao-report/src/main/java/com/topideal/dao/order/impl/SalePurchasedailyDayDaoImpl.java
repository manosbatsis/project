package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.SalePurchasedailyDayDao;
import com.topideal.entity.vo.reporting.SalePurchasedailyDayModel;
import com.topideal.mapper.reporting.SalePurchasedailyDayMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SalePurchasedailyDayDaoImpl implements SalePurchasedailyDayDao {

    @Autowired
    private SalePurchasedailyDayMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SalePurchasedailyDayModel> list(SalePurchasedailyDayModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SalePurchasedailyDayModel model) throws SQLException {
    	model.setCreateDate(TimeUtils.getNow());
    	model.setModifyDate(TimeUtils.getNow());
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
    public int modify(SalePurchasedailyDayModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public SalePurchasedailyDayModel  searchByPage(SalePurchasedailyDayModel  model) throws SQLException{
        PageDataModel<SalePurchasedailyDayModel> pageModel=mapper.listByPage(model);
        return (SalePurchasedailyDayModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SalePurchasedailyDayModel  searchById(Long id)throws SQLException {
        SalePurchasedailyDayModel  model=new SalePurchasedailyDayModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SalePurchasedailyDayModel searchByModel(SalePurchasedailyDayModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 * 根据条件统计当天的购销采销日报数据
	 * @param startDate 开始时间(必传)
	 * @param endDate   结束时间(必传)
	 * @param merchantId 商家id
	 * @return
	 */
	@Override
	public List<SalePurchasedailyDayModel> countSalePurchasedailyDayByParam(String startDate, String endDate,
			Long merchantId) {
		return mapper.countSalePurchasedailyDayByParam(startDate,endDate,merchantId);
	}
	/**
	 * 分页查询
	 * @param model
	 * @return
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	@Override
	public SalePurchasedailyDayModel listSalePurchaseDailyByPage(SalePurchasedailyDayModel model) throws SQLException {
		PageDataModel<SalePurchasedailyDayModel> pageModel=mapper.listSalePurchaseDailyByPage(model);
		return (SalePurchasedailyDayModel ) pageModel.getModel();
	}
	/**
	 * 导出
	 * @param model
	 * @return
	 */
	@Override
	public List<SalePurchasedailyDayModel> exportSalePurchaseDailyDetails(SalePurchasedailyDayModel model) {
		return mapper.exportSalePurchaseDailyDetails(model);
	}
	/**
	 * 获取导出的总条数
	 * @param model
	 * @return
	 */
	@Override
	public Long getExportTotalCount(SalePurchasedailyDayModel model) {
		return mapper.getExportTotalCount(model);
	}
	/**
	 * 根据条件获取最大的报表日期
	 * @param merchantId
	 * @return
	 */
	@Override
	public SalePurchasedailyDayModel getMaxReportDate(Long merchantId) {
		List<SalePurchasedailyDayModel> list = mapper.getMaxReportDate(merchantId);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

}