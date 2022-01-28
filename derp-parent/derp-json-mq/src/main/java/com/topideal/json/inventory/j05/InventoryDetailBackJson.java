package com.topideal.json.inventory.j05;

import java.io.Serializable;
import java.util.List;

/**
 * 商品收发明细回推json
 **/
public class InventoryDetailBackJson implements Serializable {

    private String code;

    private String merchantName;

    private List<InventoryDetailJson> detailJsons;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<InventoryDetailJson> getDetailJsons() {
        return detailJsons;
    }

    public void setDetailJsons(List<InventoryDetailJson> detailJsons) {
        this.detailJsons = detailJsons;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
