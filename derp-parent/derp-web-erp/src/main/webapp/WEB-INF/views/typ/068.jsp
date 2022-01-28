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
                <!--  title   start  -->
                <div class="col-12">
                    <div class="card-box">
                        <div class="col-12 row">
                            <div>
                                <ol class="breadcrumb">
                                    <li><i class="block"></i> 当前位置：</li>
                                    <li class="breadcrumb-item"><a href="#">进销存汇总表</a></li>
                                </ol>
                            </div>
                        </div>
                        <!--  title   end  -->
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">仓库 :</span>
                                    <input class="input_msg" type="text">
                                    <span class="msg_title">年月 :</span>
                                    <input class="input_msg" type="email">
                                    <span class="msg_title">条码 :</span>
                                    <input class="input_msg" type="email">
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="$mytable.fn.reload();">查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">商品名称 :</span>
                                    <input class="input_msg" type="text">
                                </div>
                            </div>
                        </div>
                        <div class="row col-md-12 mt20">
                            <div class="button-list">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm">编辑</button>
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="import-buttons">删除</button>
                            </div>
                        </div>
                        <div class="row col-12 mt20">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                                   width="100%">
                                <thead>
                                <tr>
                                    <th style="vertical-align: middle;" rowspan="2">货名</th>
                                    <th style="vertical-align: middle;" rowspan="2">条码</th>
                                    <th style="vertical-align: middle;" rowspan="2">8位码</th>
                                    <th style="vertical-align: middle;" rowspan="2">单价</br>HKD</th>
                                    <th style="white-space:nowrap;" colspan="8">数量</th>
                                    <th style="white-space:nowrap;">金额</th>
                                </tr>
                                <tr>
                                    <th>本月期</br>初库存</th>
                                    <th>本月入库</th>
                                    <th>本月出库</th>
                                    <th>损益</th>
                                    <th>本月期</br>末库存</th>
                                    <th>仓库现存量</th>
                                    <th>销售未确认</th>
                                    <th>采购未上架</th>
                                    <th>本月期末</br>库存金额</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>欧乐B阶段性儿童牙刷（阶段三赛车总动员特别版）</td>
                                    <td>3014260279400</td>
                                    <td>75072002</td>
                                    <td>11</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>=E4+F4-G4-H4</td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td>3120</td>
                                </tr>
                                <tr>
                                    <td>欧乐B阶段性儿童牙刷（阶段三赛车总动员特别版）</td>
                                    <td>3014260279400</td>
                                    <td>75072002</td>
                                    <td>11</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>=E4+F4-G4-H4</td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td>3120</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!--======================Modal框===========================  -->
                    <!-- end row -->

                </div>
                <!-- container -->
            </div>
        </form>

    </div> <!-- content -->
</div>
