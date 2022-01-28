package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.SkuPriceWarnDao;
import com.topideal.entity.dto.SkuPriceWarnDTO;
import com.topideal.entity.vo.reporting.SkuPriceWarnModel;
import com.topideal.mapper.reporting.SkuPriceWarnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class SkuPriceWarnDaoImpl implements SkuPriceWarnDao {

    @Autowired
    private SkuPriceWarnMapper mapper;

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<SkuPriceWarnModel> list(SkuPriceWarnModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(SkuPriceWarnModel model) throws SQLException {
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
    public int modify(SkuPriceWarnModel  model) throws SQLException {
        return mapper.update(model);
    }

	/**
     * 分页查询
     * @param model
     */
    @Override
    public SkuPriceWarnModel  searchByPage(SkuPriceWarnModel  model) throws SQLException{
        PageDataModel<SkuPriceWarnModel> pageModel=mapper.listByPage(model);
        return (SkuPriceWarnModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public SkuPriceWarnModel  searchById(Long id)throws SQLException {
        SkuPriceWarnModel  model=new SkuPriceWarnModel ();
        model.setId(id);
        return mapper.get(model);
    }

      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public SkuPriceWarnModel searchByModel(SkuPriceWarnModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public List<SkuPriceWarnDTO> listToDeal(SkuPriceWarnDTO dto) throws SQLException {
        return mapper.listToDeal(dto);
    }

    @Override
    public Long countForDeal(SkuPriceWarnDTO dto) throws SQLException {
        return mapper.countForDeal(dto);
    }
}
