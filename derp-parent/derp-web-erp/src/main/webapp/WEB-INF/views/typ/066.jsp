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
                                            <li class="breadcrumb-item"><a href="#">品牌列表</a></li>
                                            <li class="breadcrumb-item"><a href="#">品牌匹配</a></li>
                                        </ol>
                                    </div>
                                </div>
                                <!--  title   end  -->
                                <div class="title_text mb-2">选择品牌</div>
                                <div class="mb-2"><button class="btn btn-info btn_default">选择</button></div>
                                <div class="brand_matching">
                                    <div class="brand_matching_table">
                                        <table class="table table-striped" cellspacing="0" width="100%">
                                            <thead>
                                            <tr>
                                                <th>品牌ID</th>
                                                <th>品牌名称</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td>10009890</td>
                                                <td>Gerbe嘉宝</td>
                                                <td><a href="javascript:">删除</a></td>
                                            </tr>
                                            <tr>
                                                <td>10009890</td>
                                                <td>Gerbe嘉宝</td>
                                                <td><a href="javascript:">删除</a></td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <span class="matching"> > </span>
                                    <div class="form-group">
                                        <label for="">匹配品牌名称：</label>
                                        <input type="text" class="form-control">
                                    </div>
                                </div>



                                <div class="form-row m-t-50">
                                    <div class="col-12">
                                        <div class="row">
                                            <div class="col-6">
                                                <button type="button"
                                                        class="btn btn-info waves-effect waves-light fr btn_default"
                                                        id="save-buttons">确认匹配
                                                </button>
                                            </div>
                                            <div class="col-6">
                                                <button type="button"
                                                        class="btn btn-light waves-effect waves-light btn_default"
                                                        id="cancel-buttons">取 消
                                                </button>
                                            </div>
                                        </div>
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

