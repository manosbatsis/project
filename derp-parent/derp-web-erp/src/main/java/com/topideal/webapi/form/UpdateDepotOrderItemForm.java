package com.topideal.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 不同单据切仓商品修改表体請求参数
 */
public class UpdateDepotOrderItemForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "条形码")
    private String barcode;

    @ApiModelProperty(value = "行号")
    private Integer index;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
    
    
   
}
