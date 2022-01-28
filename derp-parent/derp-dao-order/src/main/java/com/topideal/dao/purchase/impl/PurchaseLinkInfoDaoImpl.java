package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.PurchaseLinkInfoDao;
import com.topideal.entity.dto.purchase.PurchaseLinkInfoDTO;
import com.topideal.entity.vo.purchase.PurchaseLinkInfoModel;
import com.topideal.mapper.purchase.PurchaseLinkInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PurchaseLinkInfoDaoImpl implements PurchaseLinkInfoDao {

    @Autowired
    private PurchaseLinkInfoMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PurchaseLinkInfoModel> list(PurchaseLinkInfoModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PurchaseLinkInfoModel model) throws SQLException {
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
    public int modify(PurchaseLinkInfoModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PurchaseLinkInfoModel  searchByPage(PurchaseLinkInfoModel  model) throws SQLException{
        PageDataModel<PurchaseLinkInfoModel> pageModel=mapper.listByPage(model);
        return (PurchaseLinkInfoModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PurchaseLinkInfoModel  searchById(Long id)throws SQLException {
        PurchaseLinkInfoModel  model=new PurchaseLinkInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PurchaseLinkInfoModel searchByModel(PurchaseLinkInfoModel model) throws SQLException {
		return mapper.get(model);
	}
	@Override
	public PurchaseLinkInfoDTO getPurchaseLinkDtoByPurchaseId(Long id) {
		return mapper.getPurchaseLinkDtoByPurchaseId(id);
	}
	@Override
	public PurchaseLinkInfoDTO getPurchaseLinkDtoById(Long id) {
		return mapper.getPurchaseLinkDtoById(id);
	}
}