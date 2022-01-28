package com.topideal.entity.dto.main;

import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 不同单据切仓商品修改实体
 */
public class UpdateDepotOrderDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "行号")
    private Integer index;

    @ApiModelProperty(value = "商品信息")
    MerchandiseInfoModel merchandiseInfoModel;

    @ApiModelProperty(value = "商品信息集合")
    List<MerchandiseInfoModel> merchandiseList;


    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public MerchandiseInfoModel getMerchandiseInfoModel() {
        return merchandiseInfoModel;
    }

    public void setMerchandiseInfoModel(MerchandiseInfoModel merchandiseInfoModel) {
        this.merchandiseInfoModel = merchandiseInfoModel;
    }

    public List<MerchandiseInfoModel> getMerchandiseList() {
        return merchandiseList;
    }

    public void setMerchandiseList(List<MerchandiseInfoModel> merchandiseList) {
        this.merchandiseList = merchandiseList;
    }
}
