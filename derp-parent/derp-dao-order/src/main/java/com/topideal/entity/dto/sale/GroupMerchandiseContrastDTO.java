package com.topideal.entity.dto.sale;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class GroupMerchandiseContrastDTO extends PageModel implements Serializable{

   
	@ApiModelProperty(value = "主键ID")
    private Long id;
   
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;
    
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
   
	@ApiModelProperty(value = "店铺编码")
    private String shopCode;
  
	@ApiModelProperty(value = "店铺名称")
    private String shopName;
   
	@ApiModelProperty(value = "组合品ID")
    private String skuId;
   
	@ApiModelProperty(value = "组合品名称")
    private String groupGoodsName;
	
	@ApiModelProperty(value = "店铺id")
    private Long shopId;
	
	@ApiModelProperty(value = "组合商品对照商品明细")
	
    private List<GroupMerchandiseContrastDetailDTO> detailList;
	
	@ApiModelProperty(value = "组合商品对照更改记录表")
    private List<GroupMerchandiseContrastDetailHistoryDto> historyList;

    //扩展字段
	@ApiModelProperty(value = "数量")
    private Integer num;
	
	@ApiModelProperty(value = "商品名称")
    private String goodsName;
	
	@ApiModelProperty(value = "条形码")
    private String barcode;
	
	@ApiModelProperty(value = "商品货号")
    private String goodsNo;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "备注")
    private String remark;
    
	@ApiModelProperty(value = "更新开始时间", hidden=true)
    private String updateStartDate ;
 
	@ApiModelProperty(value = "更新结束时间", hidden=true)
    private String updateEndDate ;
	
	@ApiModelProperty(value = "状态 0-已停用 1-已启用")
    private String status;
	
	@ApiModelProperty(value = "状态（中文）")
    private String statusLabel;
	
	@ApiModelProperty(value = "id集合，多个用逗号隔开")
	private String ids;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
    return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
    this.merchantName=merchantName;
    }
    /*shopCode get 方法 */
    public String getShopCode(){
    return shopCode;
    }
    /*shopCode set 方法 */
    public void setShopCode(String  shopCode){
    this.shopCode=shopCode;
    }
    /*shopName get 方法 */
    public String getShopName(){
    return shopName;
    }
    /*shopName set 方法 */
    public void setShopName(String  shopName){
    this.shopName=shopName;
    }
    /*skuId get 方法 */
    public String getSkuId(){
    return skuId;
    }
    /*skuId set 方法 */
    public void setSkuId(String  skuId){
    this.skuId=skuId;
    }
    /*groupGoodsName get 方法 */
    public String getGroupGoodsName(){
    return groupGoodsName;
    }
    /*groupGoodsName set 方法 */
    public void setGroupGoodsName(String  groupGoodsName){
    this.groupGoodsName=groupGoodsName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public List<GroupMerchandiseContrastDetailDTO> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<GroupMerchandiseContrastDetailDTO> detailList) {
        this.detailList = detailList;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
	public String getUpdateStartDate() {
		return updateStartDate;
	}
	public void setUpdateStartDate(String updateStartDate) {
		this.updateStartDate = updateStartDate;
	}
	public String getUpdateEndDate() {
		return updateEndDate;
	}
	public void setUpdateEndDate(String updateEndDate) {
		this.updateEndDate = updateEndDate;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.groupMerchandiseContrast_statusList, status);
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public List<GroupMerchandiseContrastDetailHistoryDto> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<GroupMerchandiseContrastDetailHistoryDto> historyList) {
        this.historyList = historyList;
    }
    public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
}
