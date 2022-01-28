package com.topideal.dao.bill.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.bill.BillMonthlySnapshotDao;
import com.topideal.entity.dto.bill.BillMonthlySnapshotDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillDTO;
import com.topideal.entity.vo.bill.BillMonthlySnapshotModel;
import com.topideal.mapper.bill.BillMonthlySnapshotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class BillMonthlySnapshotDaoImpl implements BillMonthlySnapshotDao {

    @Autowired
    private BillMonthlySnapshotMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<BillMonthlySnapshotModel> list(BillMonthlySnapshotModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(BillMonthlySnapshotModel model) throws SQLException {
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
    public int modify(BillMonthlySnapshotModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public BillMonthlySnapshotModel  searchByPage(BillMonthlySnapshotModel  model) throws SQLException{
        PageDataModel<BillMonthlySnapshotModel> pageModel=mapper.listByPage(model);
        return (BillMonthlySnapshotModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public BillMonthlySnapshotModel  searchById(Long id)throws SQLException {
        BillMonthlySnapshotModel  model=new BillMonthlySnapshotModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public BillMonthlySnapshotModel searchByModel(BillMonthlySnapshotModel model) throws SQLException {
		return mapper.get(model);
	}

    @Override
    public int deleteByModel(BillMonthlySnapshotModel billMonthlySnapshotModel) throws SQLException {
        return mapper.deleteByModel(billMonthlySnapshotModel);
    }

    @Override
    public Integer batchSave(List<BillMonthlySnapshotModel> list) throws SQLException {
        return mapper.batchSave(list);
    }

    @Override
    public BillMonthlySnapshotDTO listBillMonthlySnapshotByPage(BillMonthlySnapshotDTO dto) throws SQLException {
        PageDataModel<BillMonthlySnapshotDTO> pageModel = mapper.listBillMonthlySnapshotByPage(dto);
        return (BillMonthlySnapshotDTO ) pageModel.getModel();
    }

    @Override
    public List<BillMonthlySnapshotDTO> getMonthlyNonVerify(BillMonthlySnapshotDTO dto) throws SQLException {
        return mapper.getMonthlyNonVerify(dto);
    }

    @Override
    public List<BillMonthlySnapshotDTO> listByDTO(BillMonthlySnapshotDTO dto) throws SQLException {
        return mapper.listByDTO(dto);
    }
}