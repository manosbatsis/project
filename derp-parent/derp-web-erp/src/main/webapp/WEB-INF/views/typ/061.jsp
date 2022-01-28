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
                                    <li class="breadcrumb-item"><a href="#">爬虫数据列表</a></li>
                                </ol>
                            </div>
                        </div>
                        <!--  title   end  -->
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form-row">
                                    <form id="form1">
                                        <div class="col-3">
                                            <div class="row">
                                                <label class="col-5 col-form-label ">
                                                    <div class="fr">使用平台<span>：</span></div>
                                                </label>
                                                <div class="col-7 ml10">
                                                    <input type="text" required="" parsley-type="text" class="form-control"
                                                           name="goodsNo">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-3">
                                            <div class="row">
                                                <label class="col-5 col-form-label">
                                                    <div class="fr">用户名<span>：</span></div>
                                                </label>
                                                <div class="col-7 ml10">
                                                    <input type="text" required="" parsley-type="text" class="form-control"
                                                           name="goodsNo">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-3 ml15">
                                            <div class="row">
                                                <label class="col-5 col-form-label">
                                                    <div class="fr">创建时间<span>：</span></div>
                                                </label>
                                                <div class="col-7 ml10">
                                                    <input type="text" required="" parsley-type="text" class="form-control"
                                                           name="goodsNo">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group col-2">
                                            <div class="row">
                                                <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();'>查询</button>
                                                <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                            </div>
                                        </div>
                                    </form>
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
                                    <th style="white-space:nowrap;">序号</th>
                                    <th style="white-space:nowrap;">使用平台</th>
                                    <th style="white-space:nowrap;">用户名</th>
                                    <th style="white-space:nowrap;">关联公司</th>
                                    <th style="white-space:nowrap;">关联客户</th>
                                    <th style="white-space:nowrap;">爬取账单时点</th>
                                    <th style="white-space:nowrap;">创建时间</th>
                                    <th style="white-space:nowrap;">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td><input type="checkbox">1</td>
                                    <td>健太</td>
                                    <td>xxxx</td>
                                    <td>xxxxx</td>
                                    <td>xxxx</td>
                                    <td>十日账单</td>
                                    <td>2018-05-15 16:07:05</td>
                                    <td><a href="javascript:">详情</a></td>
                                </tr>
                                <tr>
                                    <td><input type="checkbox">2</td>
                                    <td>健太</td>
                                    <td>xxxx</td>
                                    <td>xxxx</td>
                                    <td>xxxx</td>
                                    <td>每日账单</td>
                                    <td>2018-05-15 16:07:05</td>
                                    <td><a href="javascript:">详情</a></td>
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
