package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.ReceivePaymentSubjectDao;
import com.topideal.entity.vo.bill.ReceivePaymentSubjectModel;
import com.topideal.mapper.bill.ReceivePaymentSubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ReceivePaymentSubjectDaoImpl implements ReceivePaymentSubjectDao {

    @Autowired
    private ReceivePaymentSubjectMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReceivePaymentSubjectModel> list(ReceivePaymentSubjectModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReceivePaymentSubjectModel model) throws SQLException {
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
    public int modify(ReceivePaymentSubjectModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReceivePaymentSubjectModel  searchByPage(ReceivePaymentSubjectModel  model) throws SQLException{
        PageDataModel<ReceivePaymentSubjectModel> pageModel=mapper.queryByPage(model);
        return (ReceivePaymentSubjectModel ) pageModel.getModel();
    }

    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReceivePaymentSubjectModel  searchById(Long id)throws SQLException {
        ReceivePaymentSubjectModel  model=new ReceivePaymentSubjectModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReceivePaymentSubjectModel searchByModel(ReceivePaymentSubjectModel model) throws SQLException {
		return mapper.get(model);
	}

    /**
     * 根据NC收支费项名称查询用户
     * @param name
     * @return
     */
    @Override
    public ReceivePaymentSubjectModel searchByName(String name) {
        return mapper.searchByName(name);
    }

    /**
     * 根据NC收支费项编码查询用户
     * @param code
     * @return
     */
    @Override
    public ReceivePaymentSubjectModel searchByCode(String code) {
        return mapper.searchByCode(code);
    }
}