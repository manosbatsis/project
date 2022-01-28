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
                                            <li class="breadcrumb-item"><a href="#">邮件提醒</a></li>
                                            <li class="breadcrumb-item"><a
                                                    href="javascript:$load.a('/email/toPage.html');">邮件提醒列表</a></li>
                                            <li class="breadcrumb-item"><a
                                                    href="javascript:$load.a('/email/toDetailsPage.html?id=${detail.id }');">邮件提醒详情</a>
                                            </li>
                                        </ol>
                                    </div>
                                </div>
                                <div class="title_text">邮件提醒信息</div>
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
                                            <span>供应商：</span>
                                            <span>${detail.customerName}</span>
                                        </div>
                                        <div class="info_text">
                                            <span>提醒类型：</span>
                                            <span>${detail.reminderTypeLabel}</span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>基准时间：</span>
                                            <span>
                                                 {detail.baseTimeTypeLabel}  
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>付款账期：</span>
                                            <span>
                                                 ${detail.accountPeriodDay }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>账期单位：</span>
                                            <span>
                                                ${detail.accountUnitTypeLabel }
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>提前天数提醒：</span>
                                            <span>
                                                ${detail.advanceReminderDay }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>提醒单位：</span>
                                            <span>
                                                ${detail.reminderUnitTypeLabel }
                                            </span>
                                        </div>
                                        <div class="info_text">
                                            <span>状态：</span>
                                            <span>
                                                ${detail.statusLabel }
                                            </span>
                                        </div>
                                    </div>
                                    <div class="info_item">
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
                                        <div class="info_text">                                           
                                        </div>
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
            $load.a("/email/toPage.html");
        });

    });
</script>
