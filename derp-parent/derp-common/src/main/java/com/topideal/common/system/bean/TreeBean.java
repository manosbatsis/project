package com.topideal.common.system.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * 用于树形结构
 */
@ApiModel(value = "菜单")
public class TreeBean implements Serializable{

    @ApiModelProperty(value = "菜单id 必填",position = 0)
    private Long id;
    @ApiModelProperty(value = "上级菜单id 必填",position = 1)
    private Long pId;
    @ApiModelProperty(value = "菜单名称 必填",position = 2)
    private String name;

    @ApiModelProperty(hidden=true)
    private boolean open;
    //是否选中
    @ApiModelProperty(hidden=true)
    private boolean checked;
    //访问地址
    @ApiModelProperty(hidden=true)
    private String url;
    @ApiModelProperty(value = "地址 必填",position = 3)
    private String serverAddr;
    @ApiModelProperty(value = "菜单排序 必填",position = 4)
    private Integer seq;
    //图标
    @ApiModelProperty(value = "图标",position = 5)
    private String icon;
    //1-主服务-main 2-业务-order 3-仓储-storage 4-库存-inventory 5-报表-report
    @ApiModelProperty(hidden=true)
    private String module;

    @ApiModelProperty(value ="下级菜单",position = 6)
    private List<TreeBean> children;

    @ApiModelProperty(value ="授权码")
    private String permission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public List<TreeBean> getChildren() {
        return children;
    }

    public void setChildren(List<TreeBean> children) {
        this.children = children;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }
    public String getModule() {
        return module;
    }
    public void setModule(String module) {
        this.module = module;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
