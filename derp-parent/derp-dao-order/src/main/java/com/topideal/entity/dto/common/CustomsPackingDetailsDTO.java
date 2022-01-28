package com.topideal.entity.dto.common;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

public class CustomsPackingDetailsDTO {

    @ApiModelProperty("商品货号")
    private String goodsNo;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("条形码")
    private String barcode;

    @ApiModelProperty("托盘号")
    private String torrNo;

    @ApiModelProperty("箱数")
    private Integer boxNum;

    @ApiModelProperty("件数")
    private Integer piecesNum;

    @ApiModelProperty("柜号")
    private String cabinetNo;

    @ApiModelProperty("毛重")
    private Double grossWeight;

    @ApiModelProperty("净重")
    private Double netWeight;

    @ApiModelProperty("总毛重")
    private Double totalGrossWeight;

    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*torrNo get 方法 */
    public String getTorrNo(){
    return torrNo;
    }
    /*torrNo set 方法 */
    public void setTorrNo(String  torrNo){
    this.torrNo=torrNo;
    }
    /*boxNum get 方法 */
    public Integer getBoxNum(){
    return boxNum;
    }
    /*boxNum set 方法 */
    public void setBoxNum(Integer  boxNum){
    this.boxNum=boxNum;
    }
    /*piecesNum get 方法 */
    public Integer getPiecesNum(){
    return piecesNum;
    }
    /*piecesNum set 方法 */
    public void setPiecesNum(Integer  piecesNum){
    this.piecesNum=piecesNum;
    }
    /*cabinetNo get 方法 */
    public String getCabinetNo(){
    return cabinetNo;
    }
    /*cabinetNo set 方法 */
    public void setCabinetNo(String  cabinetNo){
    this.cabinetNo=cabinetNo;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getTotalGrossWeight() {
        return totalGrossWeight;
    }

    public void setTotalGrossWeight(Double totalGrossWeight) {
        this.totalGrossWeight = totalGrossWeight;
    }
}
