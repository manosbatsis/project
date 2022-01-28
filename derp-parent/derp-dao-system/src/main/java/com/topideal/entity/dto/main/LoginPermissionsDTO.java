package com.topideal.entity.dto.main;

import com.topideal.common.system.bean.TreeBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class LoginPermissionsDTO {
    @ApiModelProperty(value = "系统名称",position = 0)
    private String systemName;
    @ApiModelProperty(value = "公司名称",position = 1)
    private String merchantName;
    @ApiModelProperty(value = "卓志编码",position = 2)
    private String topidealCode;
    @ApiModelProperty(value = "公司Id",position = 3)
    private Long merchantId;
    @ApiModelProperty(value = "用户Id",position = 4)
    private Long userId;
    //用户类型  1 平台用户  2 商家（超管理）  3 商家用户
    @ApiModelProperty(value = "用户类型 1-平台用户  2-商家（超管理）  3-商家用户",position = 5)
    private String userType;
    @ApiModelProperty(value = "用户名称",position = 6)
    private String username;
    @ApiModelProperty(value = "用户姓名",position = 7)
    private String name;
    @ApiModelProperty(value = "绑定公司",position = 8)
    private List<MerchantMinDTO> merchantList;
    @ApiModelProperty(value = "菜单",position = 9)
    private  List<TreeBean> treeMenuList;
    @ApiModelProperty(value = "按钮权限符",position = 10)
    private List<String> btnList;


    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<MerchantMinDTO> getMerchantList() {
        return merchantList;
    }

    public void setMerchantList(List<MerchantMinDTO> merchantList) {
        this.merchantList = merchantList;
    }

    public List<TreeBean> getTreeMenuList() {
        return treeMenuList;
    }

    public void setTreeMenuList(List<TreeBean> treeMenuList) {
        this.treeMenuList = treeMenuList;
    }

    public List<String> getBtnList() {
        return btnList;
    }

    public void setBtnList(List<String> btnList) {
        this.btnList = btnList;
    }

    public String getTopidealCode() {
        return topidealCode;
    }

    public void setTopidealCode(String topidealCode) {
        this.topidealCode = topidealCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
