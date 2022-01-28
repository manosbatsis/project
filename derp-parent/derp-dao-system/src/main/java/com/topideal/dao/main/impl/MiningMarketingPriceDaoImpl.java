package com.topideal.dao.main.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.MiningMarketingPriceDao;
import com.topideal.entity.dto.main.MiningMarketingPriceDTO;
import com.topideal.entity.vo.main.MiningMarketingPriceModel;
import com.topideal.mapper.main.MiningMarketingPriceMapper;

/**
 * 采销报价
 * @author lchenxing
 */
@Repository
public class MiningMarketingPriceDaoImpl implements MiningMarketingPriceDao {

    @Autowired
    private MiningMarketingPriceMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<MiningMarketingPriceModel> list(MiningMarketingPriceModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(MiningMarketingPriceModel model) throws SQLException {
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
    public int modify(MiningMarketingPriceModel  model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public MiningMarketingPriceModel  searchByPage(MiningMarketingPriceModel  model) throws SQLException{
        PageDataModel<MiningMarketingPriceModel> pageModel=mapper.listByPage(model);
        return (MiningMarketingPriceModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public MiningMarketingPriceModel  searchById(Long id)throws SQLException {
        MiningMarketingPriceModel  model=new MiningMarketingPriceModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
       /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public MiningMarketingPriceModel searchByModel(MiningMarketingPriceModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public MiningMarketingPriceDTO getListByPage(MiningMarketingPriceDTO dto) {
		PageDataModel<MiningMarketingPriceDTO> pageModel=mapper.getListByPage(dto);
        return (MiningMarketingPriceDTO ) pageModel.getModel();
	}
    
}
