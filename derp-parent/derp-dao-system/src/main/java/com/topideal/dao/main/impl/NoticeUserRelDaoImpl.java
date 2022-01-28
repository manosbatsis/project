package com.topideal.dao.main.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.main.NoticeUserRelDao;
import com.topideal.entity.vo.main.NoticeUserRelModel;
import com.topideal.mapper.main.NoticeUserRelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class NoticeUserRelDaoImpl implements NoticeUserRelDao {

    @Autowired
    private NoticeUserRelMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<NoticeUserRelModel> list(NoticeUserRelModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(NoticeUserRelModel model) throws SQLException {
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
    public int modify(NoticeUserRelModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public NoticeUserRelModel  searchByPage(NoticeUserRelModel  model) throws SQLException{
        PageDataModel<NoticeUserRelModel> pageModel=mapper.listByPage(model);
        return (NoticeUserRelModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public NoticeUserRelModel  searchById(Long id)throws SQLException {
        NoticeUserRelModel  model=new NoticeUserRelModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public NoticeUserRelModel searchByModel(NoticeUserRelModel model) throws SQLException {
		return mapper.get(model);
	}

}