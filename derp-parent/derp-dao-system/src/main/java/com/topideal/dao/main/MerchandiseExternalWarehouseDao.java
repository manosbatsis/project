package com.topideal.dao.main;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.MerchandiseExternalWarehouseDTO;
import com.topideal.entity.dto.main.MerchandiseInfoDTO;
import com.topideal.entity.vo.main.MerchandiseExternalWarehouseModel;

import java.sql.SQLException;
import java.util.List;


public interface MerchandiseExternalWarehouseDao extends BaseDao<MerchandiseExternalWarehouseModel>{


    /**
     * 分页查询数据
     * @param dto
     * @return
     */
    public MerchandiseExternalWarehouseDTO getListByPage(MerchandiseExternalWarehouseDTO dto);


    /**
     * 根据id查看详情
     * @param id
     * @return
     */
   public  MerchandiseExternalWarehouseDTO getDetailById(Long id);


    List<MerchandiseExternalWarehouseDTO> exportList(MerchandiseExternalWarehouseDTO dto) throws SQLException;

}
