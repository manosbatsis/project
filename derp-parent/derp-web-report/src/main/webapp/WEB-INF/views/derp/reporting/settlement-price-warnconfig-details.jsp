<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="modify-form" action="/email/toDetailsPage.html">
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
                                            <li class="breadcrumb-item"><a href="#">邮件管理</a></li>
                                            <li class="breadcrumb-item"><a
                                                    href="javascript:$load.a('/settlementPriceWarnconfig/toPage.html');">单价预警配置列表</a></li>
                                            <li class="breadcrumb-item"><a
                                                    href="javascript:$load.a('/settlementPriceWarnconfig/toDetailsPage.html?id=${detail.id}');">单价预警配置详情</a>
                                            </li>
                                        </ol>
                                    </div>
                                </div>
                                <div class="title_text">单价预警配置</div>
                                <div class="info_box">
                                
                                <div class="info_item">
                                        <input type="hidden" parsley-type="email" class="form-control" name="id"
                                               value="${detail.id}">
                                        <div class="info_text">
                                            <span>公司：</span>
                                            <span>
                                                ${detail.merchantName }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>事业部：</span>
                                            <span>${detail.buName}</span>
                                        </div>
                                        <div class="info_text">
                                            <span>波动范围：</span>
                                            <span>${detail.waveRange}<span style="font-weight: bold;font-size:16px">%</span></span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                       <div class="info_text">
                                            <span>邮件主题：</span>
                                            <span>
                                                  ${detail.emailTitle}  
                                            </span>
                                        </div>
                                         <div class="info_text">
                                            <span>创建时间：</span>
                                            <span>
                                                ${detail.createDate}
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>修改时间：</span>
                                            <span>
                                                ${detail.modifyDate}
                                            </span>
                                        </div>
                                    </div>    
                                     <div class="info_item">
                                        <div class="info_text">
                                            <span>收件人邮箱：</span>
                                            <span>
                                                ${detail.receiverEmail }
                                            </span>
                                        </div>
                                         <div class="info_text"></div>
                                          <div class="info_text"></div>
                                    </div>    
                                </div>
                                <div class="form-row m-t-50">
                                    <div class="col-12">
                                        <div class="row">
                                            <div class="col-5">
                                                <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="cancel-buttons">返回</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                            </div>
                        </div>
                    </div>
                    <!-- end row -->
        </form>
    </div>
    <!-- container -->
</div>

<!-- content -->
<script type="text/javascript">
    $(function () {
        //返回按钮
        $("#cancel-buttons").click(function () {
            $module.load.pageReport('act=15001');
        });

    });
</script>
