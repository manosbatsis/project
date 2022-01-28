package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.YunjiReturnDetailDao;
import com.topideal.entity.vo.order.YunjiReturnDetailModel;
import com.topideal.mapper.order.YunjiReturnDetailMapper;
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
public class YunjiReturnDetailDaoImpl implements YunjiReturnDetailDao {

    @Autowired
    private YunjiReturnDetailMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<YunjiReturnDetailModel> list(YunjiReturnDetailModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(YunjiReturnDetailModel model) throws SQLException {
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
    public int modify(YunjiReturnDetailModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public YunjiReturnDetailModel  searchByPage(YunjiReturnDetailModel  model) throws SQLException{
        PageDataModel<YunjiReturnDetailModel> pageModel=mapper.listByPage(model);
        return (YunjiReturnDetailModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public YunjiReturnDetailModel  searchById(Long id)throws SQLException {
        YunjiReturnDetailModel  model=new YunjiReturnDetailModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public YunjiReturnDetailModel searchByModel(YunjiReturnDetailModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public List<Map<String, Object>> getYjVeriDetail(Map<String, Object> queryMap) {
		return mapper.getYjVeriDetail(queryMap);
	}
	@Override
	public int changeVeriStatus(Map<String, Object> queryMap) {
		return mapper.changeVeriStatus(queryMap);
	}
	@Override
	public Map<String, Object> getReturnAccount(Map<String, Object> queryPlatformMap) {
		return mapper.getReturnAccount(queryPlatformMap);
	}

}