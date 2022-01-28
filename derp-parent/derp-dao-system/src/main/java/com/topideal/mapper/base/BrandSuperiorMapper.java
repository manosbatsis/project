package com.topideal.mapper.base;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.base.BrandSuperiorDTO;
import com.topideal.entity.vo.base.BrandSuperiorModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface BrandSuperiorMapper extends BaseMapper<BrandSuperiorModel> {

    /**
     * 分页
     * @param model
     * @return
     * @throws SQLException
     */
    PageDataModel<BrandSuperiorDTO> getListByPage(BrandSuperiorDTO dto);

    /**
     * 获取下拉
     * @return
     * @throws SQLException
     */
    List<SelectBean> getSelectBean() throws SQLException;

    /**根据商品的标准条码查询母品牌*/
    BrandSuperiorModel getByCommbarcode(@Param("commbarcode") String commbarcode) throws SQLException;
}
