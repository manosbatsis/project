<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
      <div class="col-12 row">
         <ol class="breadcrumb">
           <li><i class="dripicons-location"></i> 当前位置：</li>
           <li class="breadcrumb-item"><a href="#">报文详情</a></li>
         </ol>
      </div>
      <div class="row col-12">
         <div class="button-list">
            <button type="button" class="btn btn-add waves-effect waves-light btn-sm">返回</button>
         </div>
      </div>    
      <div class="row mt20">
      	<div class="card-box col-6 table-responsive">
      			 <table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>调出商品编号</th>
                            <th>调出商品货号</th>
                            <th>调出商品名称</th>
                            <th>调拨出库数量</th>
                            <th>调出批次</th>
                            <th>生产日期</th>
                            <th>失效日期</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
      		</div>
      	<div class="col-6 card-box table-responsive">
      			<div>
      				接口名称：速运标准订单接收接口

请求方：ESDBJ

响应方：ESDIM

请求地址：http://openapi.esdex.com/order.do

请求报文：{"cp_code":"1000000429","declare_scheme_sid":"ESD21","dno":"AUS001","esdno":"981817010531","is_trace_source":2,"itemList":[{"item":"","HScode":"0402290000","item_name":"Aptamil婴儿奶粉3段900g","item_category":"01010700","spec":"900g/罐","qty1":"千克","item_net_weight":900,"item_weight":1059,"unit":"千克","assem_country":"澳大利亚","price_declare":95.88,"brand":"Aptamil","item_quantity":1}],"mail_no":"EK292666115HK","order_create_time":"2018-06-12 10:13:29","platform":"0000034","premium_fee":0,"receiver":{"country":"中国","address":"广东省广州市白云区石井镇石槎路小坪时代广场3楼328室","province":"香港","city":"香港","phone":"","identity_no_back":"/file/user/968889_sOppositePath/031b7b6953ad48639ff4a99012db4fe3.jpg","name":"谭燕红","mobile":"18520478420","county":"中国","identity_no":"","identity_no_front":"/file/user/968889_sPositivePath/ca1731e47d1a4f1dae21d85a5ccd72e3.jpg","zip_code":"510000"},"remark":"","sender":{"country":"美国","address":"纽约","province":"纽约","city":"纽约","phone":"18708380927","name":"0927","mobile":"0927","county":"美国","zip_code":""},"serciveList":[{"service_type":0}],"storehouse_id":"NYPXSCE","totai_taxes_pay":95.88,"totai_taxes_reference":"","total_freight":0,"trade_no":"171346177689962888"}

进程信息：

回执报文：{"code":10000,"sub_msg":"执行成功","body":{},"status":1}

状态码：1

状态信息：

状态外码：

关联单号：981817010531

业务单号：171346177689962888

扩展字段1：EK292666115HK

是否关闭：否

重推次数：0

是否打开重推：是
      			</div>
      		</div>
      </div>
        <!-- end row -->
    </div>

</div> <!-- content -->
