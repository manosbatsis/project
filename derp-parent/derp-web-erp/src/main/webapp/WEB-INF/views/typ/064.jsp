<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>
<link href='<c:url value="/resources/assets/css/common.css" />' rel="stylesheet" type="text/css"/>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="add-form" action="/merchandise/saveMerchandise.asyn">
            <div class="row">
                <div class="col-12">
                    <div class="card-box">
                        <!--  title   start  -->
                        <div class="col-12">
                            <div>
                                <div class="col-12 row">
                                    <div>
                                        <ol class="breadcrumb">
                                            <li><i class="block"></i> 当前位置：</li>
                                            <li class="breadcrumb-item"><a href="#">预申报单</a></li>
                                            <li class="breadcrumb-item"><a href="#">编辑清关资料</a></li>
                                        </ol>
                                    </div>
                                </div>
                                <!--  title   end  -->
                                <ul class="nav nav-tabs">
                                    <li class="nav-item">
                                        <a href="#contract" data-toggle="tab" class="nav-link active">合同</a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="#invoice" data-toggle="tab" class="nav-link">发票</a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="#packingList" data-toggle="tab" class="nav-link">装箱单</a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="#packingDetail" data-toggle="tab" class="nav-link">装箱明细</a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="#rawMaterial" data-toggle="tab" class="nav-link">原料成分占比</a>
                                    </li>
                                </ul>
                                <div class="tab-content">
                                    <div class="tab-pane fade show active" id="contract">
                                        <p class="customs_clearance_titile">合同</p>
                                        <div class="datum_head_right">
                                            <span>合同编号：</span>
                                            <input type="text">
                                        </div>
                                        <div class="datum_head_right">
                                            <span>签订日期：</span>
                                            <input type="text">
                                        </div>
                                        <div class="datum_head_right">
                                            <span>签订地点：</span>
                                            <input type="text">
                                        </div>
                                        <div class="datum_head_left">
                                            <span>甲方：</span>
                                            <input type="text">
                                        </div>
                                        <div class="datum_head_left">
                                            <span>甲方地址：</span>
                                            <input type="text">
                                        </div>
                                        <div class="datum_head_left">
                                            <span>乙方：</span>
                                            <input type="text">
                                        </div>
                                        <p>基于甲方与【乙方】签订的编号为 【合同编号】的《委托服务协议》（下称“《委托服务协议》”），甲方现委托乙方办理如下商品在中国南沙保税港区的申报通关、仓储服务：</p>
                                        <p class="datum_title">一、商品资料明细：</p>
                                        <div class="batch_import">
                                            <table>
                                                <thead>
                                                <tr>
                                                    <td>商品名称</td>
                                                    <td>货号</td>
                                                    <td>数量</td>
                                                    <td>单价(RMB)</td>
                                                    <td>总价(RMB)</td>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>1</td>
                                                    <td>2</td>
                                                    <td>3</td>
                                                    <td>4</td>
                                                    <td>5</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="datum_explain">
                                            <p>商品原产地：</p>
                                            <p>特殊操作要求：</p>
                                        </div>
                                        <p class="datum_title">二、其他</p>
                                        <div class="other_con">
                                            <p>1.本协议一式贰份，甲、乙双方各持一份，具有同等法律效力。</p>
                                            <p>2.本协议经甲、乙双方签章后生效，通过传真件或扫描件方式签订同样具备法律效力。</p>
                                            <p>3.本协议作为《委托服务协议》不可分割的组成部分，未尽之处，适用《委托服务协议》的约定。</p>
                                        </div>
                                        <div class="main_text">
                                            （以下无正文）
                                        </div>
                                        <div class="form-row m-t-50">
                                            <div class="col-12">
                                                <div class="row">
                                                    <div class="col-5">
                                                        <span class="fr">甲方（签章）</span>
                                                    </div>
                                                    <div class="col-2"></div>
                                                    <div class="col-5">
                                                        <span>乙方（签章）</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="invoice">
                                        <p class="customs_clearance_titile">发票</p>
                                        <div class="datum_head_right">
                                            <span>DATE时间：</span>
                                            <input type="text">
                                        </div>
                                        <div class="datum_head_right">
                                            <span>INVOICE NO发票号：</span>
                                            <input type="text">
                                        </div>
                                        <div class="datum_head_right">
                                            <span>CONTRACT NO合同号：</span>
                                            <input type="text">
                                        </div>
                                        <div class="datum_head_left">
                                            <span>Consignee：</span>
                                            <input type="text">
                                        </div>
                                        <div class="datum_head_left">
                                            <span>收货人：</span>
                                        </div>
                                        <div class="datum_head_left">
                                            <span>收货人地址：</span>
                                            <input type="text">
                                        </div>
                                        <div class="batch_import table_invoice ">
                                            <table>
                                                <thead>
                                                <tr>
                                                    <td>
                                                        <p>COMMODITY.</p>
                                                        <p>商品名称</p>
                                                    </td>
                                                    <td>
                                                        <p>Article number</p>
                                                        <p>货号</p>
                                                    </td>
                                                    <td>
                                                        <p>QUANTITY( PCS )</p>
                                                        <p>数量</p>
                                                    </td>
                                                    <td>
                                                        <p>UNITY PRICE</p>
                                                        <p>单价(RMB)</p>
                                                    </td>
                                                    <td>
                                                        <p>AMOUNT</p>
                                                        <p>总价（RMB）</p>
                                                    </td>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>1</td>
                                                    <td>2</td>
                                                    <td>3</td>
                                                    <td>4</td>
                                                    <td>5</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="invoice_info">
                                            <p><span>1.Shipment before：</span><input type="text"></p>
                                            <p>装船时间：</p>
                                            <p><span>2.Payment：</span><input type="text"></p>
                                            <p>付款方式：</p>
                                            <p><span>3.Packing：</span><input type="text"></p>
                                            <p>包装：</p>
                                            <p><span>4.Port of Loading：</span><input type="text"></p>
                                            <p>装货港：</p>
                                            <p><span>5.Port of Discharge：</span><input type="text"></p>
                                            <p>卸货港：</p>
                                            <p><span>6.Payment RULES：</span><input type="text"></p>
                                            <p>付款条约：</p>
                                            <p><span>7.Country of Origin：</span><input type="text"></p>
                                            <p>原产国：</p>
                                            <p><span>8.Shipper：</span><input type="text"></p>
                                            <p>境外发货人：</p>
                                            <p><span>9.Marks and Numbers：</span><input type="text"></p>
                                            <p>唛头：</p>
                                        </div>
                                        <p class="invoice_remark">注：本发票通过扫描件方式出具同样具有法律效力。</p>
                                    </div>
                                    <div class="tab-pane fade" id="packingList">
                                        <p class="customs_clearance_titile">装箱单</p>
                                        <div class="datum_head_right">
                                            <span>DATE时间：</span>
                                            <input type="text">
                                        </div>
                                        <div class="datum_head_right">
                                            <span>INVOICE NO发票号：</span>
                                            <span class="datum_head_right_con"></span>
                                        </div>
                                        <div class="datum_head_right">
                                            <span>CONTRACT NO合同号：</span>
                                            <span class="datum_head_right_con"></span>
                                        </div>
                                        <div class="datum_head_left">
                                            <span>Consignee：</span>
                                        </div>
                                        <div class="datum_head_left">
                                            <span>收货人：</span>
                                        </div>
                                        <div class="datum_head_left">
                                            <span>收货人地址：</span>
                                        </div>
                                        <div class="batch_import table_invoice ">
                                            <table>
                                                <thead>
                                                <tr>
                                                    <td>
                                                        <p>COMMODITY.</p>
                                                        <p>商品名称</p>
                                                    </td>
                                                    <td>
                                                        <p>Specifications</p>
                                                        <p>规格</p>
                                                    </td>
                                                    <td>
                                                        <p>Article number</p>
                                                        <p>货号</p>
                                                    </td>
                                                    <td>
                                                        <p>QUANTITY( PCS )</p>
                                                        <p>数量</p>
                                                    </td>
                                                    <td>
                                                        <p>NET WEIGHT（KGS）</p>
                                                        <p>净重</p>
                                                    </td>
                                                    <td>
                                                        <p>GROSS WEIGHT（KGS）</p>
                                                        <p>毛重</p>
                                                    </td>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>1</td>
                                                    <td>2</td>
                                                    <td>3</td>
                                                    <td>4</td>
                                                    <td>5</td>
                                                    <td>6</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="invoice_info">
                                            <p><span>1.Shipment before：</span><input type="text"></p>
                                            <p>装船时间：</p>
                                            <p><span>2.Payment：</span><input type="text"></p>
                                            <p>付款方式：</p>
                                            <p><span>3.Packing：</span><input type="text"></p>
                                            <p>包装：</p>
                                            <p><span>4.Port of Loading：</span><input type="text"></p>
                                            <p>装货港：</p>
                                            <p><span>5.Port of Discharge：</span><input type="text"></p>
                                            <p>卸货港：</p>
                                            <p><span>6.Payment RULES：</span><input type="text"></p>
                                            <p>付款条约：</p>
                                            <p><span>7.Country of Origin：</span><input type="text"></p>
                                            <p>原产国：</p>
                                            <p><span>8.Shipper：</span><input type="text"></p>
                                            <p>境外发货人：</p>
                                            <p><span>9.Marks and Numbers：</span><input type="text"></p>
                                            <p>唛头：</p>
                                        </div>
                                        <p class="invoice_remark">注：本发票通过扫描件方式出具同样具有法律效力。</p>
                                    </div>
                                    <div class="tab-pane fade" id="packingDetail">
                                        <p class="customs_clearance_titile">装箱明细</p>
                                        <div class="datum_head_left">
                                            <span>Consignee：</span>
                                        </div>
                                        <div class="datum_head_left">
                                            <span>收货人：</span>
                                        </div>
                                        <div class="datum_head_left">
                                            <span>收货人地址：</span>
                                        </div>
                                        <div class="batch_import table_invoice ">
                                            <table>
                                                <thead>
                                                <tr>
                                                    <td>
                                                        <p>The pallet no.</p>
                                                        <p>托盘号</p>
                                                    </td>
                                                    <td>
                                                        <p>COMMODITY.</p>
                                                        <p>商品名称</p>
                                                    </td>
                                                    <td>
                                                        <p>Article number</p>
                                                        <p>货号</p>
                                                    </td>
                                                    <td>
                                                        条形码
                                                    </td>
                                                    <td>
                                                        <p>QUANTITY ( PCS )</p>
                                                        <p>数量</p>
                                                    </td>
                                                    <td>
                                                        <p>Cartons</p>
                                                        <p>箱数</p>
                                                    </td>
                                                    <td>
                                                        <p>NET WEIGHT（KGS）</p>
                                                        <p>净重</p>
                                                    </td>
                                                    <td>
                                                        <p>GROSS WEIGHT（KGS）</p>
                                                        <p>毛重</p>
                                                    </td>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td><input type="text"></td>
                                                    <td>2</td>
                                                    <td>3</td>
                                                    <td>4</td>
                                                    <td>5</td>
                                                    <td><input type="text"></td>
                                                    <td>7</td>
                                                    <td>8</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="rawMaterial">
                                        <p class="customs_clearance_titile">原料成分占比</p>
                                        <div class="batch_import table_invoice ">
                                            <table>
                                                <thead>
                                                <tr>
                                                    <td>商品名称</td>
                                                    <td>成分占比</td>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>1</td>
                                                    <td>2</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                        <p class="invoice_remark">注：本成分占比通过扫描件方式出具同样具有法律效力。</p>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                    <!--======================Modal框===========================  -->
                    <!-- end row -->
                </div>
            </div>
        </form>
    </div>
    <!-- container -->
</div>

</div> <!-- content -->

