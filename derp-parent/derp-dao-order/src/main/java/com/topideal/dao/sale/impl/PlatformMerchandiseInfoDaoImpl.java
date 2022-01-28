package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.sale.PlatformMerchandiseInfoDao;
import com.topideal.entity.dto.sale.PlatformMerchandiseInfoDTO;
import com.topideal.entity.vo.sale.PlatformMerchandiseInfoModel;
import com.topideal.mapper.sale.PlatformMerchandiseInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by weixiaolei on 2018/4/10.
 * @author lchenxing
 */
@Repository
public class PlatformMerchandiseInfoDaoImpl implements PlatformMerchandiseInfoDao {

    @Autowired
    private PlatformMerchandiseInfoMapper mapper;
	
	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<PlatformMerchandiseInfoModel> list(PlatformMerchandiseInfoModel model) throws SQLException {
		return mapper.list(model);
	}
	/**
	 * 新增
	 * @param model
	 */
    @Override
    public Long save(PlatformMerchandiseInfoModel model) throws SQLException {
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
    public int modify(PlatformMerchandiseInfoModel  model) throws SQLException {
        return mapper.update(model);
    }
    
	/**
     * 分页查询
     * @param model
     */
    @Override
    public PlatformMerchandiseInfoModel  searchByPage(PlatformMerchandiseInfoModel  model) throws SQLException{
        PageDataModel<PlatformMerchandiseInfoModel> pageModel=mapper.listByPage(model);
        return (PlatformMerchandiseInfoModel ) pageModel.getModel();
    }
    
    /**
     * 通过id查询实体类信息
     * @param id
     */
    @Override
    public PlatformMerchandiseInfoModel  searchById(Long id)throws SQLException {
        PlatformMerchandiseInfoModel  model=new PlatformMerchandiseInfoModel ();
        model.setId(id);
        return mapper.get(model);
    }
    
      /**
     	* 根据商家实体类查询商品
     	* @param model
     	* */
	@Override
	public PlatformMerchandiseInfoModel searchByModel(PlatformMerchandiseInfoModel model) throws SQLException {
		return mapper.get(model);
	}
    /**
     * 分页查询
     */
    @Override
    public PlatformMerchandiseInfoDTO searchDTOByPage(PlatformMerchandiseInfoDTO dto) throws Exception{
        PageDataModel<PlatformMerchandiseInfoDTO> pageModel=mapper.searchDTOByPage(dto);
        return (PlatformMerchandiseInfoDTO ) pageModel.getModel();
    }
    /**批量插入
     * */
    @Override
    public Integer insertBatch(List<PlatformMerchandiseInfoModel> list){
        return mapper.insertBatch(list);
    }

    public List<PlatformMerchandiseInfoDTO> listByDto(PlatformMerchandiseInfoDTO dto){
        return mapper.listByDto(dto);
    }
}