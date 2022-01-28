package com.topideal.dao.reporting.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.BuSaleNotshelfInfoDao;
import com.topideal.entity.vo.reporting.BuSaleNotshelfInfoModel;
import com.topideal.mapper.reporting.BuSaleNotshelfInfoMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BuSaleNotshelfInfoDaoImpl implements BuSaleNotshelfInfoDao {

    @Autowired
    private BuSaleNotshelfInfoMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BuSaleNotshelfInfoModel> list(BuSaleNotshelfInfoModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BuSaleNotshelfInfoModel model) throws SQLException {
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
    public int modify(BuSaleNotshelfInfoModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BuSaleNotshelfInfoModel  searchByPage(BuSaleNotshelfInfoModel  model) throws SQLException{
        PageDataModel<BuSaleNotshelfInfoModel> pageModel=mapper.listByPage(model);
        return (BuSaleNotshelfInfoModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BuSaleNotshelfInfoModel  searchById(Long id)throws SQLException {
        BuSaleNotshelfInfoModel  model=new BuSaleNotshelfInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BuSaleNotshelfInfoModel searchByModel(BuSaleNotshelfInfoModel model) throws SQLException {
		return mapper.get(model);
	}
	
	/**
	 *  清空事业部商家、仓库、本月的报表数据 
	 */
	@Override
	public int delBuDepotMonthReport(Map<String, Object> map) {
		return mapper.delBuDepotMonthReport(map);
	}
	/**
	 * 批量插入
	 */
	@Override
	public int insertBuBatch(List<BuSaleNotshelfInfoModel> list) {
		return mapper.insertBuBatch(list);
	}
	
	/**
	 * 销售未确认导出
	 */
	@Override
	public List<Map<String, Object>> listBuForMap(Map<String, Object> map) {
		  List<Map<String, Object>> list = mapper.listBuForMap(map);
		  if(list!=null&&list.size()>0){
			  for(Map<String, Object> value:list){
				  String unit = (String)value.get("unit");
				  if(StringUtils.isEmpty(unit)){
					  value.put("unit","");
				  }else if(unit.equals("0")){
					  value.put("unit","托盘");
				  }else if(unit.equals("1")){
					  value.put("unit","箱");
				  }else if(unit.equals("2")){
					  value.put("unit","件");
				  }

				  String businessModel = (String) value.get("business_model");
				  if (DERP_REPORT.SALENOTSHELFINFO_BUSINESSMODEL_1.equals(businessModel)) {
				  	value.put("business_model", "购销");
				  } else if (DERP_REPORT.SALENOTSHELFINFO_BUSINESSMODEL_2.equals(businessModel)){
				  	value.put("business_model", "代销");
				  }else if (DERP_REPORT.SALENOTSHELFINFO_BUSINESSMODEL_3.equals(businessModel)){
					value.put("business_model", "购销整批结算");
				  }else if (DERP_REPORT.SALENOTSHELFINFO_BUSINESSMODEL_4.equals(businessModel)){
					value.put("business_model", "购销实销实结");
				  }
			  }
		  }
		return list;

	}

}