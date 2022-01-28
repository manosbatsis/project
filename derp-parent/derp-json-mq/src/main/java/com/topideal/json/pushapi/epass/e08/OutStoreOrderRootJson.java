package com.topideal.json.pushapi.epass.e08;

import java.util.List;

/**
 * 出库订单
 **/
public class OutStoreOrderRootJson {

    private String order_id;//企业订单编号
    private String busi_scene;//业务场景类型 10：销售出库 20：调拨出库 30：退运出库 02:区内跨账册调出
    private String contract_no;//报关合同号
    private String created_time;//订单创建时间 yyyy-mm-dd HH:mi:ss
    private String planed_time;//计划出库时间 yyyy-mm-dd HH:mi:ss
    private String ebc_code;//电商企业编码
    private String warehouse_code;//仓库编码
    private String customs_code;//关区编码
    private List<OutStoreOrderItemJson> good_list;//商品信息标签

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getBusi_scene() {
        return busi_scene;
    }

    public void setBusi_scene(String busi_scene) {
        this.busi_scene = busi_scene;
    }

    public String getContract_no() {
        return contract_no;
    }

    public void setContract_no(String contract_no) {
        this.contract_no = contract_no;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getPlaned_time() {
        return planed_time;
    }

    public void setPlaned_time(String planed_time) {
        this.planed_time = planed_time;
    }

    public String getEbc_code() {
        return ebc_code;
    }

    public void setEbc_code(String ebc_code) {
        this.ebc_code = ebc_code;
    }

    public String getWarehouse_code() {
        return warehouse_code;
    }

    public void setWarehouse_code(String warehouse_code) {
        this.warehouse_code = warehouse_code;
    }

    public String getCustoms_code() {
        return customs_code;
    }

    public void setCustoms_code(String customs_code) {
        this.customs_code = customs_code;
    }

    public List<OutStoreOrderItemJson> getGood_list() {
        return good_list;
    }

    public void setGood_list(List<OutStoreOrderItemJson> good_list) {
        this.good_list = good_list;
    }
}
