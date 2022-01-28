package com.topideal.entity.dto.common;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
@ApiModel
public class TradeLinkConfigDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "链路名称")
    private String linkName;

    @ApiModelProperty(value = "起点公司ID")
    private Long startPointMerchantId;

    @ApiModelProperty(value = "起点公司名称")
    private String startPointMerchantName;

    @ApiModelProperty(value = "起点事业部id")
    private Long startPointBuId;

    @ApiModelProperty(value = "起点事业部名称")
    private String startPointBuName;

    @ApiModelProperty(value = "起点销售加价比例")
    private BigDecimal startPointPremiumRate;

    @ApiModelProperty(value = "客户1id")
    private Long oneCustomerId;

    @ApiModelProperty(value = "客户1名称")
    private String oneCustomerName;

    @ApiModelProperty(value = "客户1事业部id")
    private Long oneBuId;

    @ApiModelProperty(value = "客户1事业部名称")
    private String oneBuName;

    @ApiModelProperty(value = "客户1销售加价比例")
    private BigDecimal onePremiumRate;

    @ApiModelProperty(value = "客户2id")
    private Long twoCustomerId;

    @ApiModelProperty(value = "客户2名称")
    private String twoCustomerName;

    @ApiModelProperty(value = "客户2事业部id")
    private Long twoBuId;

    @ApiModelProperty(value = "客户2事业部名称")
    private String twoBuName;

    @ApiModelProperty(value = "客户2销售加价比例")
    private BigDecimal twoPremiumRate;

    @ApiModelProperty(value = "客户3id")
    private Long threeCustomerId;

    @ApiModelProperty(value = "客户3名称")
    private String threeCustomerName;

    @ApiModelProperty(value = "客户3事业部id")
    private Long threeBuId;

    @ApiModelProperty(value = "客户3事业部名称")
    private String threeBuName;

    @ApiModelProperty(value = "客户3销售加价比例")
    private BigDecimal threePremiumRate;

    @ApiModelProperty(value = "客户4id")
    private Long fourCustomerId;

    @ApiModelProperty(value = "客户4名称")
    private String fourCustomerName;

    @ApiModelProperty(value = "客户4事业部id")
    private Long fourBuId;

    @ApiModelProperty(value = "客户4事业部名称")
    private String fourBuName;

    @ApiModelProperty(value = "创建人")
    private Long creater;

    @ApiModelProperty(value = "创建人用户名")
    private String createName;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "修改人名称")
    private Long modifier;

    @ApiModelProperty(value = "修改人用户名")
    private String modifierName;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "客户1类型(1内部,2外部)")
    private String oneType;

    @ApiModelProperty(value = "客户2类型(1内部,2外部)")
    private String twoType;

    @ApiModelProperty(value = "客户3类型(1内部,2外部)")
    private String threeType;

    @ApiModelProperty(value = "客户4类型(1内部,2外部)")
    private String fourType;

    @ApiModelProperty(value = "客户1(1客户id,2公司id)")
    private String oneIdvaluetype;

    @ApiModelProperty(value = "客户2(1客户id,2公司id)")
    private String twoIdvaluetype;

    @ApiModelProperty(value = "客户3(1客户id,2公司id)")
    private String threeIdvaluetype;

    @ApiModelProperty(value = "客户4(1客户id,2公司id)")
    private String fourIdvaluetype;

    @ApiModelProperty(value = "起点供应商id")
    private Long startSupplierId;

    @ApiModelProperty(value = "起点供应商名")
    private String startSupplierName;


    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*linkName get 方法 */
    public String getLinkName(){
    return linkName;
    }
    /*linkName set 方法 */
    public void setLinkName(String  linkName){
    this.linkName=linkName;
    }
    /*startPointMerchantId get 方法 */
    public Long getStartPointMerchantId(){
    return startPointMerchantId;
    }
    /*startPointMerchantId set 方法 */
    public void setStartPointMerchantId(Long  startPointMerchantId){
    this.startPointMerchantId=startPointMerchantId;
    }
    /*startPointMerchantName get 方法 */
    public String getStartPointMerchantName(){
    return startPointMerchantName;
    }
    /*startPointMerchantName set 方法 */
    public void setStartPointMerchantName(String  startPointMerchantName){
    this.startPointMerchantName=startPointMerchantName;
    }
    /*startPointBuId get 方法 */
    public Long getStartPointBuId(){
    return startPointBuId;
    }
    /*startPointBuId set 方法 */
    public void setStartPointBuId(Long  startPointBuId){
    this.startPointBuId=startPointBuId;
    }
    /*startPointBuName get 方法 */
    public String getStartPointBuName(){
    return startPointBuName;
    }
    /*startPointBuName set 方法 */
    public void setStartPointBuName(String  startPointBuName){
    this.startPointBuName=startPointBuName;
    }
    /*startPointPremiumRate get 方法 */
    public BigDecimal getStartPointPremiumRate(){
    return startPointPremiumRate;
    }
    /*startPointPremiumRate set 方法 */
    public void setStartPointPremiumRate(BigDecimal  startPointPremiumRate){
    this.startPointPremiumRate=startPointPremiumRate;
    }
    /*oneCustomerId get 方法 */
    public Long getOneCustomerId(){
    return oneCustomerId;
    }
    /*oneCustomerId set 方法 */
    public void setOneCustomerId(Long  oneCustomerId){
    this.oneCustomerId=oneCustomerId;
    }
    /*oneCustomerName get 方法 */
    public String getOneCustomerName(){
    return oneCustomerName;
    }
    /*oneCustomerName set 方法 */
    public void setOneCustomerName(String  oneCustomerName){
    this.oneCustomerName=oneCustomerName;
    }
    /*oneBuId get 方法 */
    public Long getOneBuId(){
    return oneBuId;
    }
    /*oneBuId set 方法 */
    public void setOneBuId(Long  oneBuId){
    this.oneBuId=oneBuId;
    }
    /*oneBuName get 方法 */
    public String getOneBuName(){
    return oneBuName;
    }
    /*oneBuName set 方法 */
    public void setOneBuName(String  oneBuName){
    this.oneBuName=oneBuName;
    }
    /*onePremiumRate get 方法 */
    public BigDecimal getOnePremiumRate(){
    return onePremiumRate;
    }
    /*onePremiumRate set 方法 */
    public void setOnePremiumRate(BigDecimal  onePremiumRate){
    this.onePremiumRate=onePremiumRate;
    }
    /*twoCustomerId get 方法 */
    public Long getTwoCustomerId(){
    return twoCustomerId;
    }
    /*twoCustomerId set 方法 */
    public void setTwoCustomerId(Long  twoCustomerId){
    this.twoCustomerId=twoCustomerId;
    }
    /*twoCustomerName get 方法 */
    public String getTwoCustomerName(){
    return twoCustomerName;
    }
    /*twoCustomerName set 方法 */
    public void setTwoCustomerName(String  twoCustomerName){
    this.twoCustomerName=twoCustomerName;
    }
    /*twoBuId get 方法 */
    public Long getTwoBuId(){
    return twoBuId;
    }
    /*twoBuId set 方法 */
    public void setTwoBuId(Long  twoBuId){
    this.twoBuId=twoBuId;
    }
    /*twoBuName get 方法 */
    public String getTwoBuName(){
    return twoBuName;
    }
    /*twoBuName set 方法 */
    public void setTwoBuName(String  twoBuName){
    this.twoBuName=twoBuName;
    }
    /*twoPremiumRate get 方法 */
    public BigDecimal getTwoPremiumRate(){
    return twoPremiumRate;
    }
    /*twoPremiumRate set 方法 */
    public void setTwoPremiumRate(BigDecimal  twoPremiumRate){
    this.twoPremiumRate=twoPremiumRate;
    }
    /*threeCustomerId get 方法 */
    public Long getThreeCustomerId(){
    return threeCustomerId;
    }
    /*threeCustomerId set 方法 */
    public void setThreeCustomerId(Long  threeCustomerId){
    this.threeCustomerId=threeCustomerId;
    }
    /*threeCustomerName get 方法 */
    public String getThreeCustomerName(){
    return threeCustomerName;
    }
    /*threeCustomerName set 方法 */
    public void setThreeCustomerName(String  threeCustomerName){
    this.threeCustomerName=threeCustomerName;
    }
    /*threeBuId get 方法 */
    public Long getThreeBuId(){
    return threeBuId;
    }
    /*threeBuId set 方法 */
    public void setThreeBuId(Long  threeBuId){
    this.threeBuId=threeBuId;
    }
    /*threeBuName get 方法 */
    public String getThreeBuName(){
    return threeBuName;
    }
    /*threeBuName set 方法 */
    public void setThreeBuName(String  threeBuName){
    this.threeBuName=threeBuName;
    }
    /*threePremiumRate get 方法 */
    public BigDecimal getThreePremiumRate(){
    return threePremiumRate;
    }
    /*threePremiumRate set 方法 */
    public void setThreePremiumRate(BigDecimal  threePremiumRate){
    this.threePremiumRate=threePremiumRate;
    }
    /*fourCustomerId get 方法 */
    public Long getFourCustomerId(){
    return fourCustomerId;
    }
    /*fourCustomerId set 方法 */
    public void setFourCustomerId(Long  fourCustomerId){
    this.fourCustomerId=fourCustomerId;
    }
    /*fourCustomerName get 方法 */
    public String getFourCustomerName(){
    return fourCustomerName;
    }
    /*fourCustomerName set 方法 */
    public void setFourCustomerName(String  fourCustomerName){
    this.fourCustomerName=fourCustomerName;
    }
    /*fourBuId get 方法 */
    public Long getFourBuId(){
    return fourBuId;
    }
    /*fourBuId set 方法 */
    public void setFourBuId(Long  fourBuId){
    this.fourBuId=fourBuId;
    }
    /*fourBuName get 方法 */
    public String getFourBuName(){
    return fourBuName;
    }
    /*fourBuName set 方法 */
    public void setFourBuName(String  fourBuName){
    this.fourBuName=fourBuName;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }
    /*modifierName get 方法 */
    public String getModifierName(){
    return modifierName;
    }
    /*modifierName set 方法 */
    public void setModifierName(String  modifierName){
    this.modifierName=modifierName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }

    public String getTwoType() {
        return twoType;
    }

    public void setTwoType(String twoType) {
        this.twoType = twoType;
    }

    public String getThreeType() {
        return threeType;
    }

    public void setThreeType(String threeType) {
        this.threeType = threeType;
    }

    public String getFourType() {
        return fourType;
    }

    public void setFourType(String fourType) {
        this.fourType = fourType;
    }

    public String getTwoIdvaluetype() {
        return twoIdvaluetype;
    }

    public void setTwoIdvaluetype(String twoIdvaluetype) {
        this.twoIdvaluetype = twoIdvaluetype;
    }

    public String getThreeIdvaluetype() {
        return threeIdvaluetype;
    }

    public void setThreeIdvaluetype(String threeIdvaluetype) {
        this.threeIdvaluetype = threeIdvaluetype;
    }

    public String getFourIdvaluetype() {
        return fourIdvaluetype;
    }

    public void setFourIdvaluetype(String fourIdvaluetype) {
        this.fourIdvaluetype = fourIdvaluetype;
    }

    public String getOneType() {
        return oneType;
    }

    public void setOneType(String oneType) {
        this.oneType = oneType;
    }

    public String getOneIdvaluetype() {
        return oneIdvaluetype;
    }

    public void setOneIdvaluetype(String oneIdvaluetype) {
        this.oneIdvaluetype = oneIdvaluetype;
    }

    public Long getStartSupplierId() {
        return startSupplierId;
    }

    public void setStartSupplierId(Long startSupplierId) {
        this.startSupplierId = startSupplierId;
    }

    public String getStartSupplierName() {
        return startSupplierName;
    }

    public void setStartSupplierName(String startSupplierName) {
        this.startSupplierName = startSupplierName;
    }
}
