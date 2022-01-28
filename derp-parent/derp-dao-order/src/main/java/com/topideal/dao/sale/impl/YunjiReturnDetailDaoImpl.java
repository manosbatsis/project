package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.YunjiReturnDetailDao;
import com.topideal.entity.vo.sale.YunjiReturnDetailModel;
import com.topideal.mapper.sale.YunjiReturnDetailMapper;

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
	/**
	 * 查询所有云集状态为未使用的所有云集退货爬虫明细
	 */
	@Override
	public List<Map<String, Object>> getYunjiReturnDetail(Map<String, Object> map) {
		return mapper.getYunjiReturnDetail(map);
	}
	/**
	 * 修改云集退货为已使用
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int modifyYunJiReturnDetail(YunjiReturnDetailModel model) throws SQLException {
		return mapper.modifyYunJiReturnDetail(model);
		
	}
	/**
	 * 修改云集账单退货详情错误信息
	 */
	@Override
	public int updateYunjiReturnDetail(YunjiReturnDetailModel model) throws SQLException {
		return mapper.updateYunjiReturnDetail(model);
	}
	
	/**
	 * 根据结算单号，sku汇总云集退货明细
	 */
	@Override
	public List<YunjiReturnDetailModel> getPlatformStatementSumData(Map<String, Object> itemQueryMap) {
		return mapper.getPlatformStatementSumData(itemQueryMap);
	}
	/**
	 * 修改云集退货为未使用
	 */
	@Override
	public int updateNotUsed(YunjiReturnDetailModel model) throws SQLException {
		return mapper.updateNotUsed(model);
	}
}