package com.topideal.dao.common.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.common.OccupationRateConfigDao;
import com.topideal.entity.dto.common.OccupationRateConfigDTO;
import com.topideal.entity.vo.common.OccupationRateConfigModel;
import com.topideal.mapper.common.OccupationRateConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class OccupationRateConfigDaoImpl implements OccupationRateConfigDao {

    @Autowired
    private OccupationRateConfigMapper mapper;

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<OccupationRateConfigModel> list(OccupationRateConfigModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(OccupationRateConfigModel model) throws SQLException {
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
    public int modify(OccupationRateConfigModel  model) throws SQLException {
        return mapper.update(model);
    }

	/**
     * 分页查询
     * @param model
     */
    @Override
    public OccupationRateConfigModel  searchByPage(OccupationRateConfigModel  model) throws SQLException{
        PageDataModel<OccupationRateConfigModel> pageModel=mapper.listByPage(model);
        return (OccupationRateConfigModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public OccupationRateConfigModel  searchById(Long id)throws SQLException {
        OccupationRateConfigModel  model=new OccupationRateConfigModel ();
        model.setId(id);
        return mapper.get(model);
    }

      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public OccupationRateConfigModel searchByModel(OccupationRateConfigModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public OccupationRateConfigDTO listDTOByPage(OccupationRateConfigDTO dto) {
        PageDataModel<OccupationRateConfigDTO> pageModel = mapper.listDTOByPage(dto);
        return (OccupationRateConfigDTO ) pageModel.getModel();
    }

    @Override
    public OccupationRateConfigDTO searchDTO(OccupationRateConfigDTO dto) {
        return mapper.searchDTO(dto);
    }
}
