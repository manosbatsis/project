package com.topideal.dao.base;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.base.BrandSuperiorDTO;
import com.topideal.entity.vo.base.BrandSuperiorModel;

import java.sql.SQLException;
import java.util.List;


public interface BrandSuperiorDao extends BaseDao<BrandSuperiorModel> {

    /**
     * 分页
     * @param model
     * @return
     * @throws SQLException
     */
    BrandSuperiorDTO getListByPage(BrandSuperiorDTO dto) throws SQLException;

    /**
     * 获取下拉
     * @return
     * @throws SQLException
     */
    List<SelectBean> getSelectBean() throws SQLException;

    /**根据商品的标准条码查询母品牌*/
    BrandSuperiorModel getByCommbarcode(String commbarcode) throws SQLException;
}
