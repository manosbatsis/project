package com.topideal.dao.base;


import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.base.ExchangeRateDTO;
import com.topideal.entity.vo.base.ExchangeRateModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 汇率管理表 dao
 * @author lian_
 */
public interface ExchangeRateDao extends BaseDao<ExchangeRateModel> {
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ExchangeRateModel> list(ExchangeRateModel model) throws SQLException ;
	
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ExchangeRateModel model) throws SQLException ;
    
	/**
     * 删除
     * @param ids
     */
    @Override
    public int delete(List ids) throws SQLException ;
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(ExchangeRateModel model) throws SQLException ;

	/**
     * 分页查询
     * @param model
     */
    @Override
    public ExchangeRateModel  searchByPage(ExchangeRateModel model) throws SQLException;
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ExchangeRateModel  searchById(Long id)throws SQLException ;
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ExchangeRateModel searchByModel(ExchangeRateModel model) throws SQLException ;

	public ExchangeRateDTO searchDTOById(Long id);

	public ExchangeRateDTO getListByPage(ExchangeRateDTO dto) throws SQLException;

	//根据汇率日期删除汇率
	public void deleteByEffectiveDate(String effectiveDate) throws SQLException;

	/**
	 * 根据本位币+原币查询最新的汇率
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	ExchangeRateModel getLatestRate(ExchangeRateModel model) throws SQLException;
}
