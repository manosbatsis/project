package com.topideal.dao.automatic.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.automatic.YunjiAutomaticVerificationDao;
import com.topideal.entity.dto.YunjiAutomaticVerificationDTO;
import com.topideal.entity.vo.automatic.YunjiAutomaticVerificationModel;
import com.topideal.mapper.automatic.YunjiAutomaticVerificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class YunjiAutomaticVerificationDaoImpl implements YunjiAutomaticVerificationDao {

    @Autowired
    private YunjiAutomaticVerificationMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<YunjiAutomaticVerificationModel> list(YunjiAutomaticVerificationModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(YunjiAutomaticVerificationModel model) throws SQLException {
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
    @SuppressWarnings("rawtypes")
	@Override
    public int delete(List ids) throws SQLException {
        return mapper.batchDel(ids);
    }
    
	/**
     * 修改
     * @param model
     */
    @Override
    public int modify(YunjiAutomaticVerificationModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public YunjiAutomaticVerificationModel  searchByPage(YunjiAutomaticVerificationModel  model) throws SQLException{
        PageDataModel<YunjiAutomaticVerificationModel> pageModel=mapper.listByPage(model);
        return (YunjiAutomaticVerificationModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public YunjiAutomaticVerificationModel  searchById(Long id)throws SQLException {
        YunjiAutomaticVerificationModel  model=new YunjiAutomaticVerificationModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public YunjiAutomaticVerificationModel searchByModel(YunjiAutomaticVerificationModel model) throws SQLException {
		return mapper.get(model);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public YunjiAutomaticVerificationDTO listByPage(YunjiAutomaticVerificationDTO dto) {
		List<YunjiAutomaticVerificationDTO> list = mapper.getPageList(dto);
		Integer total = mapper.getTotal(dto) ;
		
		YunjiAutomaticVerificationDTO returnDTO = new YunjiAutomaticVerificationDTO() ;
		returnDTO.setList(list);
		returnDTO.setTotal(total);
		
        return returnDTO;
	}
	@Override
	public int getExportCount(YunjiAutomaticVerificationDTO dto) {
		return mapper.getExportCount(dto) ;
	}
	@Override
	public List<YunjiAutomaticVerificationDTO> getExportList(YunjiAutomaticVerificationDTO dto) {
		return mapper.getExportList(dto);
	}
	@Override
	public List<YunjiAutomaticVerificationModel> getDifferentList() {
		return mapper.getDifferentList();
	}

}