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
                                    <li class="breadcrumb-item"><a href="#">类型调整列表</a></li>
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
                                                    <div class="fr">商    品<span>：</span></div>
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
                                                    <div class="fr">仓库名称<span>：</span></div>
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
                                                    <div class="fr">公司<span>：</span></div>
                                                </label>
                                                <div class="col-7 ml10">
                                                    <input type="text" required="" parsley-type="text" class="form-control"
                                                           name="goodsNo">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group col-1">
                                            <div class="row">
                                                <button type="button" class="btn ml15 btn-find waves-effect waves-light"
                                                        onclick='$mytable.fn.reload();'>查询
                                                </button>
                                            </div>
                                        </div>
                                        <div class="col-1">
                                            <div class="row">
                                                <button type="reset" class="btn btn-light ml15 waves-effect waves-light">重置
                                                </button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                <div class="form-row">
                                    <div class="col-3">
                                        <div class="row">
                                            <label class="col-5 col-form-label ">
                                                <div class="fr">业务类别<span>：</span></div>
                                            </label>
                                            <div class="col-7 ml10">
                                                <select class="form-control">
                                                    <option>好坏品互转</option>
                                                    <option>货号变更</option>
                                                    <option>效期调整</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3">
                                        <div class="row">
                                            <label class="col-5 col-form-label">
                                                <div class="fr">单据状态<span>：</span></div>
                                            </label>
                                            <div class="col-7 ml10">
                                                <select class="form-control">
                                                    <option>已调整</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>

                        <div class="row col-12 mt-5">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                                   width="100%">
                                <thead>
                                <tr>
                                    <th style="white-space:nowrap;" class="tc">序号</th>
                                    <th style="white-space:nowrap;" class="tc">公司</th>
                                    <th style="white-space:nowrap;" class="tc">类型调整单号</th>
                                    <th style="white-space:nowrap;" class="tc">业务类别</th>
                                    <th style="white-space:nowrap;" class="tc">来源单据号</th>
                                    <th style="white-space:nowrap;" class="tc">仓库名称</th>
                                    <th style="white-space:nowrap;" class="tc">调整时间</th>
                                    <th style="white-space:nowrap;" class="tc">单据状态</th>
                                    <th style="white-space:nowrap;" class="tc">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td><input type="checkbox">1</td>
                                    <td>健太</td>
                                    <td>LXTZ20180709001</td>
                                    <td>好坏品互转</td>
                                    <td>KCPD20170913112754</td>
                                    <td>南沙综合仓1</td>
                                    <td></td>
                                    <td>已调整</td>
                                    <td><a href="javascript:">详情</a></td>
                                </tr>
                                <tr>
                                    <td><input type="checkbox">2</td>
                                    <td>健太</td>
                                    <td>LXTZ20180709001</td>
                                    <td>好坏品互转</td>
                                    <td>KCPD20170913112754</td>
                                    <td>南沙综合仓1</td>
                                    <td></td>
                                    <td>已调整</td>
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
<script>
    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
    })
</script>