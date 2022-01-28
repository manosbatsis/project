package com.topideal.dao.common.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.FileTempBakDao;
import com.topideal.dao.common.FileTempDao;
import com.topideal.entity.dto.common.FileTempDTO;
import com.topideal.entity.vo.common.FileTempBakModel;
import com.topideal.entity.vo.common.FileTempModel;
import com.topideal.mapper.common.FileTempBakMapper;
import com.topideal.mapper.common.FileTempMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class FileTempDaoImpl implements FileTempDao {

    @Autowired
    private FileTempMapper mapper;
    
    @Autowired
    private FileTempBakMapper bakMapper ;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<FileTempModel> list(FileTempModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(FileTempModel model) throws SQLException {
        int num=mapper.insert(model);
        if(num==1){
        	
        	FileTempBakModel bakModel = new FileTempBakModel() ;
        	
        	BeanUtils.copyProperties(model, bakModel);
        	
        	bakModel.setFileTempId(model.getId());
        	bakModel.setId(null);
        	bakModel.setCreateDate(TimeUtils.getNow());
        	bakModel.setModifyDate(null);
        	
        	bakMapper.insert(bakModel) ;
        	
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
    public int modify(FileTempModel  model) throws SQLException {
    	
    	Long fileId = model.getId();
    	
    	FileTempModel tempModel = searchById(fileId);
    	FileTempBakModel tempSaveModel = new FileTempBakModel() ;
    	
    	BeanUtils.copyProperties(tempModel, tempSaveModel);
    	
    	tempSaveModel.setId(null);
    	tempSaveModel.setFileTempId(fileId);
    	tempSaveModel.setCreateDate(TimeUtils.getNow());
    	tempSaveModel.setModifyDate(null);
    	
    	bakMapper.insert(tempSaveModel) ;
    	
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public FileTempModel  searchByPage(FileTempModel  model) throws SQLException{
        PageDataModel<FileTempModel> pageModel=mapper.listByPage(model);
        return (FileTempModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public FileTempModel  searchById(Long id)throws SQLException {
        FileTempModel  model=new FileTempModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public FileTempModel searchByModel(FileTempModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public List<FileTempDTO> getPageList(FileTempDTO dto) {
		List<FileTempDTO> pageList = mapper.getPageList(dto);
		
		return pageList ;
	}

    @Override
    public int delByFileId(Long fileId) throws SQLException {
        return mapper.delByFileId(fileId);
    }

    @Override
    public List<FileTempDTO> listFileTempInfo(FileTempDTO dto) throws SQLException {
        return mapper.listFileTempInfo(dto);
    }
	@Override
	public Integer coutPageList(FileTempDTO dto) {
		return mapper.coutPageList(dto);
	}


}