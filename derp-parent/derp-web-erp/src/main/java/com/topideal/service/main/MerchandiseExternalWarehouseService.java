package com.topideal.service.main;

import com.topideal.common.enums.ActionTypeEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.entity.dto.main.MerchandiseExternalWarehouseDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 外仓备案商品
 */
public interface MerchandiseExternalWarehouseService {

    /**
     * 分页查询数据
     * @param dto
     * @return
     * @throws SQLException
     */
    public MerchandiseExternalWarehouseDTO getListByPage(MerchandiseExternalWarehouseDTO dto, User user) throws SQLException;

    /**
     * 删除
     * @param user
     */
    public Map deleteById(User user, List<Long> ids) throws SQLException;


    /**
     * 导入平台备案商品
     * @param data
     * @param user
     * @return
     * @throws Exception
     */
    Map importMerchandiseWarehouse(Map<Integer, List<List<Object>>> data, User user) throws Exception;


    /**
     * 根据id查看详情
     * @param id
     * @return
     */
    public MerchandiseExternalWarehouseDTO getDetailById(Long id);


    /**
     * 导出
     * @param model
     * @return
     */
    List<MerchandiseExternalWarehouseDTO> exportMerchandiseExternalWarehouse(MerchandiseExternalWarehouseDTO model,User user) throws SQLException;


    ResponseBean saveOrUpdateByDTO(MerchandiseExternalWarehouseDTO dto, ActionTypeEnum update) throws Exception;
}
