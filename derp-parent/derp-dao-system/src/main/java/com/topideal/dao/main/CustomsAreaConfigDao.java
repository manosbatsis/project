package com.topideal.dao.main;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.base.CustomsAreaConfigDTO;
import com.topideal.entity.vo.main.CustomsAreaConfigModel;

import java.util.List;


public interface CustomsAreaConfigDao extends BaseDao<CustomsAreaConfigModel> {

    /**
     * 分页查询列表
     * @param model
     * @return
     */
    public CustomsAreaConfigDTO listByPage(CustomsAreaConfigDTO model);

    /**
     * 获取关区下拉列表
     * @return
     * @throws Exception
     */
    List<SelectBean> getCustomsSelectBean() throws Exception;

    /**
     * 模糊查询
     * @param customsAreaConfigModel
     * @return
     */
    List<CustomsAreaConfigModel> listByLike(CustomsAreaConfigModel customsAreaConfigModel);
}
