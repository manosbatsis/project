package com.topideal.service.maindata;

import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;

import net.sf.json.JSONObject;

/**
 * 产品商品信息下发接口
 * Created by 杨创 on 2020/08/04.
 */
public interface SyncGoodsSendService {

    /**
     * 产品商品信息下发接口
     * @param json
     * @return
     * @throws Exception
     */
    public Long goodsSend(JSONObject jsonData,MerchantInfoModel merchantInfoModel,MerchandiseInfoModel merchandiseInfoQuery,Long sourceGoodsId)throws Exception;
    
    /**
     * 新增/或修改商家商品
     * @return
     * @throws Exception
     */
    public boolean synsMerchantGoods(String json)throws Exception;

    /**
     * 判断是否是经分销的商家
     * @param model
     * @return
     * @throws Exception
     */
    public MerchantInfoModel isExitMerchant(MerchantInfoModel model)throws Exception;;


}
