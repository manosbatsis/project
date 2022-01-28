<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box table-responsive">
                    <!--  title   start  -->
                    <div class="col-12">
                        <div>
                            <div class="col-12 row">
                                <div>
                                    <ol class="breadcrumb">
                                        <li><i class="block"></i> 当前位置：</li>
                                        <li class="breadcrumb-item"><a href="#">爬虫配置</a></li>
                                        <li class="breadcrumb-item "><a
                                                href="javascript:$load.a('/groupMerchandiseContrast/toPage.html');">组合商品对照表</a>
                                        </li>
                                        <li class="breadcrumb-item "><a href="javascript:dh_details(${detail.id });">组合商品详情</a>
                                        </li>
                                    </ol>
                                </div>
                            </div>
                            <!--  title   end  -->
                            <!--  title   基本信息   start -->

                            <div class="title_text">基本信息</div>
                            <form id="add-form">
                                <div class="info_box">
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>店铺名称：</span>
                                            <span>${detail.shopName }</span>
                                        </div>
                                        <div class="info_text">
                                            <span>店铺编码：</span>
                                            <span>${detail.shopCode }</span>
                                        </div>
                                        <div class="info_text">
                                            <span>公司：</span>
                                            <span>${detail.merchantName }</span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>商品ID：</span>
                                            <span>${detail.skuId }</span>
                                        </div>
                                        <div class="info_text">
                                            <span>商品名称：</span>
                                            <span>${detail.groupGoodsName }</span>
                                        </div>
                                        <div class="info_text">
                                            <span>状态：</span>
                                            <span>${detail.statusLabel }</span>
                                        </div>
                                    </div>
                                    <div class="info_item">
                                        <div class="info_text">
                                            <span>备注：</span>
                                            <span>${detail.remark }</span>
                                        </div>
                                        <div class="info_text">
                                            <span>创建时间：</span>
                                            <span><fmt:formatDate value="${detail.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                                        </div>
                                        <div class="info_text">
                                            <span>修改时间：</span>
                                            <span><fmt:formatDate value="${detail.modifyDate }" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                                        </div>
                                    </div>

                                </div>
                                <!--   商品信息  start -->
                                <div class="col-12 ml10" style="padding-top: 20px;">
                                    <div>
                                        <ul class="nav nav-tabs">
                                            <li class="nav-item">
                                                <a href="javascript:void(0);" onclick="changeTable('0');" data-toggle="tab"
                                                   class="nav-link active">组合品信息</a>
                                            </li>
                                            <li class="nav-item">
                                                <a href="javascript:void(0);" onclick="changeTable('1');" data-toggle="tab"
                                                   class="nav-link">变更记录</a>
                                            </li>
                                        </ul>
                                        <div>
                                            <div class="tab-pane fade show active" id="itemList">
                                                <div id="goodsInfoTab">
                                                    <table id="table-list" class="table table-striped" cellspacing="0" width="100%">
                                                        <thead>
                                                        <tr>
                                                            <th>商品货号</th>
                                                            <th>商品名称</th>
                                                            <th>条形码</th>
                                                            <th>商品品牌</th>
                                                            <th>数量</th>
                                                            <th>销售标准单价</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <c:forEach items="${detail.detailList}" var="item">
                                                            <tr>
                                                                <td>${item.goodsNo }</td>
                                                                <td>${item.goodsName }</td>
                                                                <td>${item.barcode }</td>
                                                                <td>${item.brand }</td>
                                                                <td>${item.num }</td>
                                                                <td>${item.price }</td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                                <div id="historyInfoTab" style="display: none;">
                                                    <table class="table table-striped" cellspacing="0" width="100%">
                                                        <thead>
                                                        <tr>
                                                            <th style="white-space:nowrap;">商品货号</th>
                                                            <th style="white-space:nowrap;">商品名称</th>
                                                            <th style="white-space:nowrap;">条形码</th>
                                                            <th style="white-space:nowrap;">数量</th>
                                                            <th style="white-space:nowrap;">销售标准单价</th>
                                                            <th style="white-space:nowrap;">修改人</th>
                                                            <th style="white-space:nowrap;">修改时间</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <c:forEach items="${detail.historyList}" var="item">
                                                            <tr>
                                                                <td>${item.goodsNo}</td>
                                                                <td>${item.goodsName}</td>
                                                                <td>${item.barcode}</td>
                                                                <td>${item.num}</td>
                                                                <td>${item.price}</td>
                                                                <td>${item.editor}</td>
                                                                <td><fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                            <div class="tab-pane fade" id="batchDefault">
                                            </div>
                                        </div>
                                    </div>
                                </div>


                            <!--   商品信息  end -->
                            <div class="form-row">
                                <div class="col-3">
                                    <div class="form-row m-t-50">
                                        <div class="row">
                                            <div class="col-12">
                                                <button type="button" class="btn btn-find waves-effect waves-light btn_default"
                                                        id="cancel-buttons">返 回
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- end row -->
                <!-- 弹窗开始 -->
                <!-- 弹窗结束 -->
            </div>
        </div>
        <!-- container -->
    </div>

</div>
<!-- content -->
<script type="text/javascript">
    $(function () {
        //重新加载select2
        $(".goods_select2").select2({
            language: "zh-CN",
            placeholder: '请选择',
            allowClear: true
        });

        function dh_details(id) {
            //$load.a(pageUrl+"30013&id="+id);
            $module.load.pageOrder("act=30013&id=" + id);
        } 

        //返回
        $("#cancel-buttons").click(function () {
            $module.load.pageOrder("act=11003");
        });
    });

    function changeTable(type) {
        if (type == '0') {
            $("#goodsInfoTab").show();
            $("#historyInfoTab").hide();
        }else  if (type == '1') {
            $("#historyInfoTab").show();
            $("#goodsInfoTab").hide();
        }
    }
</script>
