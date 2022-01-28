package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class GroupMerchandiseContrastModel extends PageModel implements Serializable{

    /**
    * 主键
    */
    private Long id;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 店铺编码
    */
    private String shopCode;
    /**
    * 店铺名称
    */
    private String shopName;
    /**
    * 组合品ID
    */
    private String skuId;
    /**
    * 组合品名称
    */
    private String groupGoodsName;

    private Long shopId;

    //状态：0-已停用 1-已启用
    private String status;

   private List<GroupMerchandiseContrastDetailModel> detailList;


    /**
     * 创建时间
     */
    private Timestamp createDate;
    /**
     * 修改时间
     */
    private Timestamp modifyDate;

    //备注
    private String remark;

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

    public List<GroupMerchandiseContrastDetailModel> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<GroupMerchandiseContrastDetailModel> detailList) {
        this.detailList = detailList;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
