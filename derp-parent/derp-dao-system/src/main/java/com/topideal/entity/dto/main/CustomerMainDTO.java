package com.topideal.entity.dto.main;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;

public class CustomerMainDTO extends PageModel implements Serializable{


	@ApiModelProperty(value = "id")
    private Long id;
	@ApiModelProperty(value = "客户ID")
    private String ccode;
	@ApiModelProperty(value = "客户名称")
    private String cname;
	@ApiModelProperty(value = "客商类型编码")
    private String ccodetypes;
    @ApiModelProperty(value = "客商类型")
    private String ccodetypeLabel;
	@ApiModelProperty(value = "客户简称")
    private String cshortname;
	@ApiModelProperty(value = "客户等级编码")
    private String ccodegrade;
	@ApiModelProperty(value = "英文名称")
    private String cnameen;
	@ApiModelProperty(value = "英文简称")
    private String cshortnameen;
	@ApiModelProperty(value = "注册地")
    private String registeredaddr;
	@ApiModelProperty(value = "组织机构代码")
    private String ccodethk;
	@ApiModelProperty(value = "企业性质")
    private String cmprop;
	@ApiModelProperty(value = "经营范围")
    private String businessscope;
	@ApiModelProperty(value = "省市区代码")
    private String citycode;
	@ApiModelProperty(value = "企业地址")
    private String addr;
	@ApiModelProperty(value = "英文地址")
    private String addren;
	@ApiModelProperty(value = "营业执照号")
    private String busilicenseno;
	@ApiModelProperty(value = "公司电话")
    private String otelno;
	@ApiModelProperty(value = "联系人")
    private String linkman;
	@ApiModelProperty(value = "联系人电话")
    private String linktel;
	@ApiModelProperty(value = "开户银行")
    private String depositbank;
	@ApiModelProperty(value = "人民币账号")
    private String cnyaccount;
	@ApiModelProperty(value = "美元账号")
    private String usdaccount;
	@ApiModelProperty(value = "法人代表")
    private String lgrepresentative;
	@ApiModelProperty(value = "法人国籍")
    private String lgrncode;
	@ApiModelProperty(value = "法人代表身份证")
    private String lgrid;
	@ApiModelProperty(value = "法人代表电话")
    private String lgrtel;
	@ApiModelProperty(value = "传真")
    private String fax;
	@ApiModelProperty(value = "Email")
    private String email;
	@ApiModelProperty(value = "备注")
    private String remark;
	@ApiModelProperty(value = "是否供应商 1-是 0-否")
    private String isSupplier;
	@ApiModelProperty(value = "isSupplierLabel")
    private String isSupplierLabel;
    @ApiModelProperty(value = "是否客户 1-是 0-否")
    private String isCustomer;
    @ApiModelProperty(value = "isCustomerLabel")
    private String isCustomerLabel;
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
    @ApiModelProperty(value = "证件类型: 枚举备注: Y 营业执照 Z 注册登记证 G 个人身份证 H 护照 J 机构代码 S 社会团体法人登记证书")
    private String idType;
    @ApiModelProperty(value = "主数据客户状态 枚举备注: 1：有效/0：锁定，默认有效")
    private String mainStatus;
    private String mainStatusLabel;
    @ApiModelProperty(value = "注册时间")
    private Timestamp registeredDate;
    @ApiModelProperty(value = "数据来源")
    private String mainSource;
    @ApiModelProperty(value = "租户编码")
    private String tenantCode;
    @ApiModelProperty(value = "客户类型(客户角色) 电商企业,仓储企业,物流公司,电商平台,支付企业,监管场所经营人,报关企业,委托单位,账册主体,资金方,客户,供应商;可多选考虑用置位方式")
    private String mainType;
    @ApiModelProperty(value = "版本号")
    private String version;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*ccode get 方法 */
    public String getCcode(){
    return ccode;
    }
    /*ccode set 方法 */
    public void setCcode(String  ccode){
    this.ccode=ccode;
    }
    /*cname get 方法 */
    public String getCname(){
    return cname;
    }
    /*cname set 方法 */
    public void setCname(String  cname){
    this.cname=cname;
    }
    /*ccodetypes get 方法 */
    public String getCcodetypes(){
    return ccodetypes;
    }
    /*ccodetypes set 方法 */
    public void setCcodetypes(String  ccodetypes){
    this.ccodetypes=ccodetypes;
    }

    public String getCcodetypeLabel() {
        return ccodetypeLabel;
    }

    public void setCcodetypeLabel(String ccodetypeLabel) {
        this.ccodetypeLabel = ccodetypeLabel;
        if(ccodetypes != null) {
            this.ccodetypeLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerMainTypeList, ccodetypes);
        }
        if(StringUtils.isBlank(this.ccodetypeLabel)) {
            this.ccodetypeLabel = "非客商类型";
        }
    }

    /*cshortname get 方法 */
    public String getCshortname(){
    return cshortname;
    }
    /*cshortname set 方法 */
    public void setCshortname(String  cshortname){
    this.cshortname=cshortname;
    }
    /*ccodegrade get 方法 */
    public String getCcodegrade(){
    return ccodegrade;
    }
    /*ccodegrade set 方法 */
    public void setCcodegrade(String  ccodegrade){
    this.ccodegrade=ccodegrade;
    }
    /*cnameen get 方法 */
    public String getCnameen(){
    return cnameen;
    }
    /*cnameen set 方法 */
    public void setCnameen(String  cnameen){
    this.cnameen=cnameen;
    }
    /*cshortnameen get 方法 */
    public String getCshortnameen(){
    return cshortnameen;
    }
    /*cshortnameen set 方法 */
    public void setCshortnameen(String  cshortnameen){
    this.cshortnameen=cshortnameen;
    }
    /*registeredaddr get 方法 */
    public String getRegisteredaddr(){
    return registeredaddr;
    }
    /*registeredaddr set 方法 */
    public void setRegisteredaddr(String  registeredaddr){
    this.registeredaddr=registeredaddr;
    }
    /*ccodethk get 方法 */
    public String getCcodethk(){
    return ccodethk;
    }
    /*ccodethk set 方法 */
    public void setCcodethk(String  ccodethk){
    this.ccodethk=ccodethk;
    }
    /*cmprop get 方法 */
    public String getCmprop(){
    return cmprop;
    }
    /*cmprop set 方法 */
    public void setCmprop(String  cmprop){
    this.cmprop=cmprop;
    }
    /*businessscope get 方法 */
    public String getBusinessscope(){
    return businessscope;
    }
    /*businessscope set 方法 */
    public void setBusinessscope(String  businessscope){
    this.businessscope=businessscope;
    }
    /*citycode get 方法 */
    public String getCitycode(){
    return citycode;
    }
    /*citycode set 方法 */
    public void setCitycode(String  citycode){
    this.citycode=citycode;
    }
    /*addr get 方法 */
    public String getAddr(){
    return addr;
    }
    /*addr set 方法 */
    public void setAddr(String  addr){
    this.addr=addr;
    }
    /*addren get 方法 */
    public String getAddren(){
    return addren;
    }
    /*addren set 方法 */
    public void setAddren(String  addren){
    this.addren=addren;
    }
    /*busilicenseno get 方法 */
    public String getBusilicenseno(){
    return busilicenseno;
    }
    /*busilicenseno set 方法 */
    public void setBusilicenseno(String  busilicenseno){
    this.busilicenseno=busilicenseno;
    }
    /*otelno get 方法 */
    public String getOtelno(){
    return otelno;
    }
    /*otelno set 方法 */
    public void setOtelno(String  otelno){
    this.otelno=otelno;
    }
    /*linkman get 方法 */
    public String getLinkman(){
    return linkman;
    }
    /*linkman set 方法 */
    public void setLinkman(String  linkman){
    this.linkman=linkman;
    }
    /*linktel get 方法 */
    public String getLinktel(){
    return linktel;
    }
    /*linktel set 方法 */
    public void setLinktel(String  linktel){
    this.linktel=linktel;
    }
    /*depositbank get 方法 */
    public String getDepositbank(){
    return depositbank;
    }
    /*depositbank set 方法 */
    public void setDepositbank(String  depositbank){
    this.depositbank=depositbank;
    }
    /*cnyaccount get 方法 */
    public String getCnyaccount(){
    return cnyaccount;
    }
    /*cnyaccount set 方法 */
    public void setCnyaccount(String  cnyaccount){
    this.cnyaccount=cnyaccount;
    }
    /*usdaccount get 方法 */
    public String getUsdaccount(){
    return usdaccount;
    }
    /*usdaccount set 方法 */
    public void setUsdaccount(String  usdaccount){
    this.usdaccount=usdaccount;
    }
    /*lgrepresentative get 方法 */
    public String getLgrepresentative(){
    return lgrepresentative;
    }
    /*lgrepresentative set 方法 */
    public void setLgrepresentative(String  lgrepresentative){
    this.lgrepresentative=lgrepresentative;
    }
    /*lgrncode get 方法 */
    public String getLgrncode(){
    return lgrncode;
    }
    /*lgrncode set 方法 */
    public void setLgrncode(String  lgrncode){
    this.lgrncode=lgrncode;
    }
    /*lgrid get 方法 */
    public String getLgrid(){
    return lgrid;
    }
    /*lgrid set 方法 */
    public void setLgrid(String  lgrid){
    this.lgrid=lgrid;
    }
    /*lgrtel get 方法 */
    public String getLgrtel(){
    return lgrtel;
    }
    /*lgrtel set 方法 */
    public void setLgrtel(String  lgrtel){
    this.lgrtel=lgrtel;
    }
    /*fax get 方法 */
    public String getFax(){
    return fax;
    }
    /*fax set 方法 */
    public void setFax(String  fax){
    this.fax=fax;
    }
    /*email get 方法 */
    public String getEmail(){
    return email;
    }
    /*email set 方法 */
    public void setEmail(String  email){
    this.email=email;
    }
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
    }
    /*isSupplier get 方法 */
    public String getIsSupplier(){
    return isSupplier;
    }
    /*isSupplier set 方法 */
    public void setIsSupplier(String  isSupplier){
    this.isSupplier=isSupplier;
    this.isSupplierLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerMainIsList, isSupplier) ;
    }
    /*isCustomer get 方法 */
    public String getIsCustomer(){
    return isCustomer;
    }
    /*isCustomer set 方法 */
    public void setIsCustomer(String  isCustomer){
    this.isCustomer=isCustomer;
    this.isCustomerLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerMainIsList, isCustomer) ;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
	public String getIsSupplierLabel() {
		return isSupplierLabel;
	}
	public void setIsSupplierLabel(String isSupplierLabel) {
		this.isSupplierLabel = isSupplierLabel;
	}
	public String getIsCustomerLabel() {
		return isCustomerLabel;
	}
	public void setIsCustomerLabel(String isCustomerLabel) {
		this.isCustomerLabel = isCustomerLabel;
	}

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getMainStatus() {
        return mainStatus;
    }

    public void setMainStatus(String mainStatus) {
        this.mainStatus = mainStatus;
        this.mainStatusLabel = DERP_SYS.getLabelByKey(DERP_SYS.customerMainStatusList, mainStatus) ;
    }

    public Timestamp getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Timestamp registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getMainSource() {
        return mainSource;
    }

    public void setMainSource(String mainSource) {
        this.mainSource = mainSource;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getMainType() {
        return mainType;
    }

    public void setMainType(String mainType) {
        this.mainType = mainType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
	public String getMainStatusLabel() {
		return mainStatusLabel;
	}
	public void setMainStatusLabel(String mainStatusLabel) {
		this.mainStatusLabel = mainStatusLabel;
	}
    
}
