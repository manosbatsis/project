package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.GroupMerchandiseContrastDTO;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;



@MyBatisRepository
public interface GroupMerchandiseContrastMapper extends BaseMapper<GroupMerchandiseContrastModel> {

    GroupMerchandiseContrastModel getDetails(GroupMerchandiseContrastModel model) ;

    GroupMerchandiseContrastModel isExist(GroupMerchandiseContrastModel model) ;

    List<GroupMerchandiseContrastDTO> getExportList(GroupMerchandiseContrastDTO dto) ;

    int isExistSkuId(@Param("skuId") String skuId);
    
    GroupMerchandiseContrastDTO getDTODetails(GroupMerchandiseContrastDTO dto) ;
    
    PageDataModel<GroupMerchandiseContrastDTO>  listDTOByPage(GroupMerchandiseContrastDTO  dto);

}
