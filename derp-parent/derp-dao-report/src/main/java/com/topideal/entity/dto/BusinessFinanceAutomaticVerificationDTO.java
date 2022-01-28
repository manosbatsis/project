package com.topideal.entity.dto;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class BusinessFinanceAutomaticVerificationDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "公司id")
    private Long merchantId;

    @ApiModelProperty(value = "公司名称")
    private String merchantName;

    @ApiModelProperty(value = "归属月份")
    private String month;

    @ApiModelProperty(value = "校验结果: 1-已对平 0-未对平")
    private String status;
    @ApiModelProperty(value = "校验结果中文")
    private String statusLabel;

    @ApiModelProperty(value = "事业部业务进销存")
    private Integer buInventorySummaryEndNum;

    @ApiModelProperty(value = "事业部财务进销存")
    private Integer buFinanceSummaryEndNum;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "事业部id")
    private Long buId;

    @ApiModelProperty(value = "事业部名称")
    private String buName;

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
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
        this.status=status;
        this.statusLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.businessFinanceAutomaticVerification_statusList, status);
    }
    /*buInventorySummaryEndNum get 方法 */
    public Integer getBuInventorySummaryEndNum(){
    return buInventorySummaryEndNum;
    }
    /*buInventorySummaryEndNum set 方法 */
    public void setBuInventorySummaryEndNum(Integer  buInventorySummaryEndNum){
    this.buInventorySummaryEndNum=buInventorySummaryEndNum;
    }
    /*buFinanceSummaryEndNum get 方法 */
    public Integer getBuFinanceSummaryEndNum(){
    return buFinanceSummaryEndNum;
    }
    /*buFinanceSummaryEndNum set 方法 */
    public void setBuFinanceSummaryEndNum(Integer  buFinanceSummaryEndNum){
    this.buFinanceSummaryEndNum=buFinanceSummaryEndNum;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }
}
