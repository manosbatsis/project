package com.topideal.dao.system;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.system.BrandParentModel;


public interface BrandParentDao extends BaseDao<BrandParentModel> {


    BrandParentModel getBrandParentByGoodsId(Long goodsId);








}
