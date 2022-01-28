package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.MerchandiseExternalWarehouseDTO;
import com.topideal.entity.vo.main.MerchandiseExternalWarehouseModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@MyBatisRepository
public interface MerchandiseExternalWarehouseMapper extends BaseMapper<MerchandiseExternalWarehouseModel> {

    /**
     * 分页查询数据
     * @param dto
     * @return
     */
    public PageDataModel<MerchandiseExternalWarehouseDTO> getListByPage(MerchandiseExternalWarehouseDTO dto);

    /**
     * 根据id查看详情
     * @param id
     * @return
     */
    public MerchandiseExternalWarehouseDTO getDetailById(@Param("id") Long id);


    /**
     * 导出查询
     * @param dto
     * @return
     */
    List<MerchandiseExternalWarehouseDTO> exportMerchandiseExternalWarehouseMap(MerchandiseExternalWarehouseDTO dto);
}
