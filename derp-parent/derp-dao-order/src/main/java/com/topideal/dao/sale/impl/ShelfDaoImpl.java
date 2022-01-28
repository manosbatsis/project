package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.ShelfDao;
import com.topideal.entity.dto.sale.ShelfDTO;
import com.topideal.entity.vo.sale.ShelfModel;
import com.topideal.mapper.sale.ShelfMapper;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class ShelfDaoImpl implements ShelfDao {

    @Autowired
    private ShelfMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<ShelfModel> list(ShelfModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(ShelfModel model) throws SQLException {
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
    public int modify(ShelfModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public ShelfModel  searchByPage(ShelfModel  model) throws SQLException{
        PageDataModel<ShelfModel> pageModel=mapper.listByPage(model);
        return (ShelfModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public ShelfModel  searchById(Long id)throws SQLException {
        ShelfModel  model=new ShelfModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public ShelfModel searchByModel(ShelfModel model) throws SQLException {
		return mapper.get(model);
	}

    /**
     * 根据ID获取详情
     * @param id
     */
    @Override
    public ShelfDTO searchShelfModelById(Long id) {
	    return mapper.searchShelfModelById(id);
    }

    @Override
    public Integer getOrderCount(ShelfDTO shelfDTO) {
        return mapper.getOrderCount(shelfDTO);
    }

    @Override
    public List<Map<String, Object>> getExportList(ShelfDTO shelfDTO) {
        return mapper.getExportList(shelfDTO);
    }

    @Override
    public ShelfDTO searchByPage1(ShelfDTO shelfDTO) {
        PageDataModel<ShelfDTO> pageModel=mapper.listByPage1(shelfDTO);
        return (ShelfDTO) pageModel.getModel();
    }

    @Override
    public List<Map<String, Object>> getShelfItemByCodes(List<String> codeList) throws SQLException {
        return mapper.getShelfItemByCodes(codeList);
    }

    @Override
    public List<Map<String, Object>> getShelfItemInfo(String code) throws SQLException {
        return mapper.getShelfItemInfo(code);
    }
	@Override
	public List<ShelfDTO> listDTO(ShelfDTO dto) throws SQLException {		
		return mapper.listDTO(dto);
	}

    @Override
    public List<ShelfDTO> listToBTempDTO(ShelfDTO dto) {
        return mapper.listToBTempDTO(dto);
    }

    @Override
    public Integer batchUpdateStatus(String isGenerate, List<String> shelfCodes) {
        return mapper.batchUpdateStatus(isGenerate, shelfCodes);
    }

    @Override
    public List<ShelfModel> getReceiveShelfList(Long merchantId, List<Long> customerIds, String month) {
        return mapper.getReceiveShelfList(merchantId, customerIds, month);
    }
}