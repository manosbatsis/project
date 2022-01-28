package com.topideal.service.maindata;

import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;

import net.sf.json.JSONObject;

/**
 * 主数据商品同步   业务
 * Created by weixiaolei on 2018/5/23.
 */
public interface SynsGoodsInfoService {

    /**
     * 同步主数据商品信息
     * @param json
     * @return
     * @throws Exception
     */
    public Long goodsInfo(JSONObject jsonData,MerchantInfoModel merchantInfoModel,MerchandiseInfoModel merchandiseInfoQuery,Long sourceGoodsId)throws Exception;


    /**
     * 新增/或修改商家商品
     * @param json
     * @return
     */
    public boolean synsMerchantGoods(String json)throws Exception;


}
