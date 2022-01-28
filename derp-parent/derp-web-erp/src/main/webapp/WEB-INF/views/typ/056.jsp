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
                                    <li class="breadcrumb-item"><a href="#">盘点结果详情</a></li>
                                </ol>
                            </div>
                        </div>
                        <!--  title   end  -->
                        <div class="form-row">
                            <form id="form1">
                                <div class="col-4">
                                    <div class="row">
                                        <label class="col-5 col-form-label ">
                                            <div class="fr">盘点单号<span>：</span></div>
                                        </label>
                                        <div class="col-7 ml10">
                                            xxxxxxxx
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="form-row">
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label ">
                                        <div class="fr">单据状态<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        待确认
                                    </div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr">服务类型<span>：</span></div>
                                    </label>
                                    个性盘点
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr">创建时间<span>：</span></div>
                                    </label>
                                    xxxxxxx
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label ">
                                        <div class="fr">盘点仓库<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        南沙综合仓
                                    </div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr">业务场景<span>：</span></div>
                                    </label>
                                    前端个性盘点
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr">确认时间<span>：</span></div>
                                    </label>
                                    xxxxxxx
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label ">
                                        <div class="fr">企业盘点单号<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        xxxx
                                    </div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr">确认状态<span>：</span></div>
                                    </label>
                                    xxxxxxx
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr">确认人<span>：</span></div>
                                    </label>
                                    xxxxxxx
                                </div>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label ">
                                        <div class="fr">备注<span>：</span></div>
                                    </label>
                                    <div class="col-7 ml10">
                                        xxxx
                                    </div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr">驳回原因<span>：</span></div>
                                    </label>
                                   xxxxxxx
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row">
                                    <label class="col-5 col-form-label">
                                        <div class="fr">接收时间<span>：</span></div>
                                    </label>
                                    xxxxxxx
                                </div>
                            </div>
                        </div>
                        <div class="page-title col-12 borderb mt20">
                            <div class="line">商品信息</div>
                        </div>
                        <div class="row col-12 mt10">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                                   width="100%">
                                <thead>
                                <tr>
                                    <th style="white-space:nowrap;" class="tc">序号</th>
                                    <th style="white-space:nowrap;" class="tc">商品编号</th>
                                    <th style="white-space:nowrap;" class="tc">商品名称</th>
                                    <th style="white-space:nowrap;" class="tc">商品条形码</th>
                                    <th style="white-space:nowrap;" class="tc">盘盈数量</th>
                                    <th style="white-space:nowrap;" class="tc">盘亏数量</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>1</td>
                                    <td>32089854</td>
                                    <td>城野医生亲研收缩毛孔收敛水控油收敛保湿化妆水日本护肤爽肤水</td>
                                    <td>A898432324</td>
                                    <td>5</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>2</td>
                                    <td>48009091</td>
                                    <td>ONLY2018夏新加菲猫字母印花T恤女|118201589</td>
                                    <td>A890932222</td>
                                    <td></td>
                                    <td>5</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!-- end row -->
                </div>
                <!-- container -->
            </div>
        </form>
    </div> <!-- content -->
</div>