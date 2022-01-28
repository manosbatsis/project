package com.topideal.dao.common.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.TradeLinkConfigDao;
import com.topideal.entity.dto.common.TradeLinkConfigDTO;
import com.topideal.entity.vo.common.TradeLinkConfigModel;
import com.topideal.mapper.common.TradeLinkConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class TradeLinkConfigDaoImpl implements TradeLinkConfigDao {

    @Autowired
    private TradeLinkConfigMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TradeLinkConfigModel> list(TradeLinkConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(TradeLinkConfigModel model) throws SQLException {
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
    public int modify(TradeLinkConfigModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public TradeLinkConfigModel  searchByPage(TradeLinkConfigModel  model) throws SQLException{
        PageDataModel<TradeLinkConfigModel> pageModel=mapper.listByPage(model);
        return (TradeLinkConfigModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public TradeLinkConfigModel  searchById(Long id)throws SQLException {
        TradeLinkConfigModel  model=new TradeLinkConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public TradeLinkConfigModel searchByModel(TradeLinkConfigModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public TradeLinkConfigDTO getTradeLinkConfigListByPage(TradeLinkConfigDTO dto) throws SQLException {
        PageDataModel<TradeLinkConfigDTO> pageModel=mapper.getTradeLinkConfigListByPage(dto);
        return (TradeLinkConfigDTO ) pageModel.getModel();
    }

    @Override
    public List<TradeLinkConfigDTO> isExistsSameTradeLink(TradeLinkConfigDTO dto) {
        return mapper.isExistsSameTradeLink(dto);
    }
}