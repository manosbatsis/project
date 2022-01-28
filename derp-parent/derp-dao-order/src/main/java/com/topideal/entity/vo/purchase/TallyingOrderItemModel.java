package com.topideal.entity.vo.purchase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;


/**
 * 理货单商品详情
 * @author lianchenxing
 */
public class TallyingOrderItemModel extends PageModel implements Serializable {

	// 多货数量
	private Integer multiNum;
	// 缺少数量
	private Integer lackNum;
	// 商品id
	private Long goodsId;
	// 正常数量
	private Integer normalNum;
	// 创建人
	private Long creater;
	// id
	private Long id;
	// 采购单数
	private Integer purchaseNum;
	// 理货单id
	private Long tallyingOrderId;
	// 理货数量
	private Integer tallyingNum;
	// 创建时间
	private Timestamp createDate;
	// 商品条形码
	private String barcode;
    //创建人名称
    private String createName;
    // 商品货号
 	private String goodsNo;
 	// 商品名称
 	private String goodsName;
 	 //长
    private Double length;
    //体积
    private Double volume;
    //毛重
    private Double grossWeight;
    //净重
    private Double netWeight;
    //宽
    private Double width;
    //高
    private Double height;
    //理货单位(00-托盘，01-箱，02-件，03-KG)
    private String tallyingUnit;

	// 理货单商品批次详情表
	private List<TallyingItemBatchModel> itemBatchModel;
	
	
	
	  public String getTallyingUnit() {
		return tallyingUnit;
	}
	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}
	/*height get 方法 */
    public Double getHeight(){
        return height;
    }
    /*height set 方法 */
    public void setHeight(Double  height){
        this.height=height;
    }
	  /*width get 方法 */
    public Double getWidth(){
        return width;
    }
    /*width set 方法 */
    public void setWidth(Double  width){
        this.width=width;
    }
	/*length get 方法 */
    public Double getLength(){
        return length;
    }
    /*length set 方法 */
    public void setLength(Double  length){
        this.length=length;
    }
    /*volume get 方法 */
    public Double getVolume(){
        return volume;
    }
    /*volume set 方法 */
    public void setVolume(Double  volume){
        this.volume=volume;
    }
    /*grossWeight get 方法 */
    public Double getGrossWeight(){
        return grossWeight;
    }
    /*grossWeight set 方法 */
    public void setGrossWeight(Double  grossWeight){
        this.grossWeight=grossWeight;
    }
    /*netWeight get 方法 */
    public Double getNetWeight(){
        return netWeight;
    }
    /*netWeight set 方法 */
    public void setNetWeight(Double  netWeight){
        this.netWeight=netWeight;
    }
	   /*createName get 方法 */
    public String getCreateName(){
        return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
        this.createName=createName;
    }
	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
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

	/* multiNum get 方法 */
	public Integer getMultiNum() {
		return multiNum;
	}

	/* multiNum set 方法 */
	public void setMultiNum(Integer multiNum) {
		this.multiNum = multiNum;
	}

	/* lackNum get 方法 */
	public Integer getLackNum() {
		return lackNum;
	}

	/* lackNum set 方法 */
	public void setLackNum(Integer lackNum) {
		this.lackNum = lackNum;
	}

	/* goodsId get 方法 */
	public Long getGoodsId() {
		return goodsId;
	}

	/* goodsId set 方法 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	/* normalNum get 方法 */
	public Integer getNormalNum() {
		return normalNum;
	}

	/* normalNum set 方法 */
	public void setNormalNum(Integer normalNum) {
		this.normalNum = normalNum;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* purchaseNum get 方法 */
	public Integer getPurchaseNum() {
		return purchaseNum;
	}

	/* purchaseNum set 方法 */
	public void setPurchaseNum(Integer purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

	/* tallyingOrderId get 方法 */
	public Long getTallyingOrderId() {
		return tallyingOrderId;
	}

	/* tallyingOrderId set 方法 */
	public void setTallyingOrderId(Long tallyingOrderId) {
		this.tallyingOrderId = tallyingOrderId;
	}

	/* tallyingNum get 方法 */
	public Integer getTallyingNum() {
		return tallyingNum;
	}

	/* tallyingNum set 方法 */
	public void setTallyingNum(Integer tallyingNum) {
		this.tallyingNum = tallyingNum;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public List<TallyingItemBatchModel> getItemBatchModel() {
		return itemBatchModel;
	}

	public void setItemBatchModel(List<TallyingItemBatchModel> itemBatchModel) {
		this.itemBatchModel = itemBatchModel;
	}
}
