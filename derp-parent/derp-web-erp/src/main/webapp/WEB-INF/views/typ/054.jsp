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
                                    <li class="breadcrumb-item"><a href="#">盘点指定列表</a></li>
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
                                                    <div class="fr">盘点单号<span>：</span></div>
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
                                                    <div class="fr">仓库<span>：</span></div>
                                                </label>
                                                <div class="col-7 ml10">
                                                    <select class="form-control">
                                                        <option></option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-3">
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
                                                <div class="fr">数据来源<span>：</span></div>
                                            </label>
                                            <div class="col-7 ml10">
                                                <select class="form-control">
                                                    <option>手工新增</option>
                                                    <option>批量导入</option>
                                                    <option>OP盘点</option>
                                                    <option>跨境宝</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-3">
                                        <div class="row">
                                            <label class="col-5 col-form-label">
                                                <div class="fr">业务场景<span>：</span></div>
                                            </label>
                                            <div class="col-7 ml10">
                                                <select class="form-control">
                                                    <option>客服盘点申请</option>
                                                    <option>仓库自行盘点</option>
                                                    <option>前端盘点申请</option>
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
                                                    <option>待确认</option>
                                                    <option>已确认</option>
                                                    <option>已作废</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                        <div class="col-12 mt20">
                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm">新增</button>
                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm">确认</button>
                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm">驳回</button>
                            <button type="button" class="btn btn-find waves-effect waves-light btn-sm">导入</button>
                        </div>
                        <div class="row col-12 mt10">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                                   width="100%">
                                <thead>
                                <tr>
                                    <th style="white-space:nowrap;" class="tc">序号</th>
                                    <th style="white-space:nowrap;" class="tc">盘点单号</th>
                                    <th style="white-space:nowrap;" class="tc">公司</th>
                                    <th style="white-space:nowrap;" class="tc">仓库</th>
                                    <th style="white-space:nowrap;" class="tc">服务类型</th>
                                    <th style="white-space:nowrap;" class="tc">业务场景</th>
                                    <th style="white-space:nowrap;" class="tc">单据来源</th>
                                    <th style="white-space:nowrap;" class="tc">单据日期</th>
                                    <th style="white-space:nowrap;" class="tc">单据状态</th>
                                    <th style="white-space:nowrap;" class="tc">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td><input type="checkbox">0</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>0</td>
                                    <td>手工新增</td>
                                    <td>0</td>
                                    <td>待确认</td>
                                    <td><a href="javascript:">详情</a></td>
                                </tr>
                                <tr>
                                    <td><input type="checkbox">1</td>
                                    <td>1</td>
                                    <td>1</td>
                                    <td>1</td>
                                    <td>1</td>
                                    <td>1</td>
                                    <td>手工新增</td>
                                    <td>1</td>
                                    <td>待确认</td>
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