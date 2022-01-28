package com.topideal.dao.reporting.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.reporting.VipDifferenceAnalysisDao;
import com.topideal.entity.dto.VipDifferenceAnalysisDTO;
import com.topideal.entity.vo.reporting.VipDifferenceAnalysisModel;
import com.topideal.mapper.reporting.VipDifferenceAnalysisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class VipDifferenceAnalysisDaoImpl implements VipDifferenceAnalysisDao {

    @Autowired
    private VipDifferenceAnalysisMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<VipDifferenceAnalysisModel> list(VipDifferenceAnalysisModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(VipDifferenceAnalysisModel model) throws SQLException {
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
    public int modify(VipDifferenceAnalysisModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public VipDifferenceAnalysisModel  searchByPage(VipDifferenceAnalysisModel  model) throws SQLException{
        PageDataModel<VipDifferenceAnalysisModel> pageModel=mapper.listByPage(model);
        return (VipDifferenceAnalysisModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public VipDifferenceAnalysisModel  searchById(Long id)throws SQLException {
        VipDifferenceAnalysisModel  model=new VipDifferenceAnalysisModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public VipDifferenceAnalysisModel searchByModel(VipDifferenceAnalysisModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public int delByMap(Map<String, Object> delMap) {
		return mapper.delByMap(delMap);
	}
	@Override
	public List<VipDifferenceAnalysisDTO> getDifferenceExportList(VipDifferenceAnalysisDTO differenceDTO) {
		return mapper.getDifferenceExportList(differenceDTO);
	}

}