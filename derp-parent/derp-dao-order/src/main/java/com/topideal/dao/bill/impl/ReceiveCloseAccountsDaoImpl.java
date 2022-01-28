package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.ReceiveCloseAccountsDao;
import com.topideal.entity.dto.bill.ReceiveCloseAccountsDTO;
import com.topideal.entity.vo.bill.ReceiveCloseAccountsModel;
import com.topideal.mapper.bill.ReceiveCloseAccountsMapper;
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
public class ReceiveCloseAccountsDaoImpl implements ReceiveCloseAccountsDao {

    @Autowired
    private ReceiveCloseAccountsMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ReceiveCloseAccountsModel> list(ReceiveCloseAccountsModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ReceiveCloseAccountsModel model) throws SQLException {
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
    public int modify(ReceiveCloseAccountsModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ReceiveCloseAccountsModel  searchByPage(ReceiveCloseAccountsModel  model) throws SQLException{
        PageDataModel<ReceiveCloseAccountsModel> pageModel=mapper.listByPage(model);
        return (ReceiveCloseAccountsModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ReceiveCloseAccountsModel  searchById(Long id)throws SQLException {
        ReceiveCloseAccountsModel  model=new ReceiveCloseAccountsModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ReceiveCloseAccountsModel searchByModel(ReceiveCloseAccountsModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public ReceiveCloseAccountsModel getLatestByModel(ReceiveCloseAccountsModel model) {
        return mapper.getLatestByModel(model);
    }

    @Override
    public ReceiveCloseAccountsDTO listDTOByPage(ReceiveCloseAccountsDTO dto) {
        PageDataModel<ReceiveCloseAccountsDTO> pageModel=mapper.listDTOByPage(dto);
        return (ReceiveCloseAccountsDTO ) pageModel.getModel();
    }

    @Override
    public ReceiveCloseAccountsDTO getLatestExcludeIdByMap(Map<String, Object> param) {
        return mapper.getLatestExcludeIdByMap(param);
    }

    @Override
    public int updateStatusByMap(Map<String, Object> param) {
        return mapper.updateStatusByMap(param);
    }

    @Override
    public void batchSave(List<ReceiveCloseAccountsModel> modelList) {
        mapper.batchSave(modelList);
    }
}