package com.topideal.dao.sale;


import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.GroupMerchandiseContrastDTO;
import com.topideal.entity.vo.sale.GroupMerchandiseContrastModel;

import java.sql.SQLException;
import java.util.List;


public interface GroupMerchandiseContrastDao extends BaseDao<GroupMerchandiseContrastModel> {


    GroupMerchandiseContrastModel getDetails(GroupMerchandiseContrastModel model ) throws SQLException;

    GroupMerchandiseContrastModel isExist(GroupMerchandiseContrastModel model) throws SQLException;

    List<GroupMerchandiseContrastDTO> getExportList(GroupMerchandiseContrastDTO dto) throws SQLException;

    int isExistSkuId(String skuId);
    
    GroupMerchandiseContrastDTO getDTODetails(GroupMerchandiseContrastDTO dto) throws SQLException;

    GroupMerchandiseContrastDTO  searchDTOByPage(GroupMerchandiseContrastDTO  dto) throws SQLException;

}
