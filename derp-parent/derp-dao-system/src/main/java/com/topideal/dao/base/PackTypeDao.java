package com.topideal.dao.base;


import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.base.PackTypeModel;

import java.util.List;

/**
 * 包装方式 dao
 * @author lian_
 */
public interface PackTypeDao extends BaseDao<PackTypeModel> {

    /**
     * 模糊查询
     * @param packTypeModel
     * @return
     */
    List<PackTypeModel> listByLike(PackTypeModel packTypeModel);
}

