<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <div class="row">
            <!--  title   start  -->
            <div class="col-12">
                <div class="card-box">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">供应商档案</a></li>
                                <li class="breadcrumb-item"><a
                                        href="javascript:$load.a('/supplierInquiryPool/toPage.html');">供应商询价池列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1">
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">供应商选择 :</span>
                                    <select name="customerId" id="customerId" class="input_msg">
                                        <option value="">请选择</option>
                                        <c:forEach items="${supplierBean }" var="supplier">
                                            <option value="${supplier.value }">${supplier.label }</option>
                                        </c:forEach>
                                    </select>
                                    <span class="msg_title">供应商品牌 :</span>
                                    <select name="brandId" id="brandId" class="input_msg">
                                        <option value="">请选择</option>
                                        <c:forEach items="${brandBean }" var="brand">
                                            <option value="${brand.value }">${brand.label }</option>
                                        </c:forEach>
                                    </select>
                                    <span class="msg_title">商品名称 :</span>
                                    <input type="text" class="input_msg" name="goodsName" id="goodsName">
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">商品条码 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="goodsNo"name="goodsNo">
                                    <span class="msg_title">商品分类 :</span>
                                    <select name="merchandiseCatId" id="merchandiseCatId" class="input_msg">
                                        <option value="">请选择</option>
                                        <c:forEach items="${productTypeBean }" var="productType">
                                            <option value="${productType.value }">${productType.label }</option>
                                        </c:forEach>
                                    </select>
                                    <span class="msg_title">原产国 :</span>
                                    <select name="countryId" id="countryId" class="input_msg">
                                        <option value="">请选择</option>
                                        <c:forEach items="${countryBean }" var="country">
                                            <option value="${country.value }">${country.label }</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="supplierInquiryPool_delSIPool">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        onclick="$custom.request.delChecked('/supplierInquiryPool/delSIPool.asyn');">删除
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="supplierInquiryPool_toImportPage">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        id="import-buttons">批量导入
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="supplierInquiryPool_exportSupplier">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="exportLogs();">批量导出</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                               width="100%">
                            <thead>
                            <tr>
                                <th style="white-space:nowrap;" class="tc">序号</th>
                                <th style="white-space:nowrap;" class="tc">品类</th>
                                <th style="white-space:nowrap;" class="tc">商品名称</th>
                                <th style="white-space:nowrap;" class="tc">品牌</th>
                                <th style="white-space:nowrap;" class="tc">供应商</th>
                                <th style="white-space:nowrap;" class="tc">起订量</th>
                                <th style="white-space:nowrap;" class="tc">计量单位</th>
                                <th style="white-space:nowrap;" class="tc">供货价</th>
                                <th style="white-space:nowrap;" class="tc">原产国</th>
                                <th style="white-space:nowrap;" class="tc">规格型号</th>
                                <th style="white-space:nowrap;" class="tc">录入日期</th>
                                <th style="white-space:nowrap;" class="tc">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                <!--======================Modal框===========================  -->
                <%--         <jsp:include page="/WEB-INF/views/modals/1011.jsp" /> --%>
                <!-- end row -->
            </div>
            <!-- container -->
        </div>
    </div>
    </div> <!-- content -->
    <script type="text/javascript">
        $(document).ready(function () {

            //重置按钮
            $("button[type='reset']").click(function () {
                $(".goods_select2").each(function () {
                    $(this).val("");
                });
                //重新加载select2
                $(".goods_select2").select2({
                    language: "zh-CN",
                    placeholder: '请选择',
                    allowClear: true
                });
            });

            //重新加载select2
            $(".goods_select2").select2({
                language: "zh-CN",
                placeholder: '请选择',
                allowClear: true
            });

            //初始化
            $mytable.fn.init();
            //配置表格参数
            $mytable.params = {
                url: '/supplierInquiryPool/listSIPool.asyn?r=' + Math.random(),
                columns: [{ //复选框单元格
                    className: "td-checkbox",
                    orderable: false,
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<input type="checkbox" class="iCheck">';
                    }
                },
                    {data: 'merchandiseCatName'}, {data: 'goodsName'}, {data: 'brandName'}, {data: 'customerName'}, {data: 'minimum'}, {data: 'unit'}, {data: 'supplyPrice'}, {data: 'countryName'}, {data: 'spec'}, {data: 'createDate'},
                    { //操作
                        orderable: false,
                        data: null,
                        width:'70px',
                        render: function (data, type, row, meta) {
                            return '<shiro:hasPermission name="supplierInquiryPool_toEditPage"><a href="javascript:edit(' + row.id + ')">编辑</a></shiro:hasPermission> '
                                + '<shiro:hasPermission name="supplierInquiryPool_toDetailsPage"><a href="javascript:details(' + row.id + ')">详情</a></shiro:hasPermission>';
                        }
                    },
                ],
                formid: '#form1'
            };
            //每一行都进行 回调
            $mytable.rewriter.fnRowCallback = function (nRow, aData, iDisplayIndex) {
                $('td:eq(7)', nRow).html("￥" + aData.supplyPrice);
            };
            //生成列表信息
            $('#datatable-buttons').mydatatables();

        });

        function searchData() {
            $mytable.fn.reload();
        }

        //详情
        function details(id) {
        	
        	$module.revertList.serializeListForm() ;
        	$module.revertList.isMainList = true ; 
        	
            $load.a("/supplierInquiryPool/toDetailsPage.html?id=" + id);
        }

        //编辑
        function edit(id) {
        	
        	$module.revertList.serializeListForm() ;
        	$module.revertList.isMainList = true ; 
        	
            $load.a("/supplierInquiryPool/toEditPage.html?id=" + id);
        }

        //批量导入
        $("#import-buttons").click(function () {
            $load.a("/supplierInquiryPool/toImportPage.html");
        });
        /**
         * 全选
         */
        $("input[name='keeperUserGroup-checkable']").on("change", function(){
            if($(this).is(':checked')){
                $(":checkbox", '#datatable-buttons').prop("checked",$(this).prop("checked"));
                $('#datatable-buttons tbody tr').addClass('success');
            }else{
                $(":checkbox", '#datatable-buttons').prop("checked",false);
                $('#datatable-buttons tbody tr').removeClass('success');
            }
        })
        $('#datatable-buttons').on("change", ":checkbox", function() {
            $(this).parents('tr').toggleClass('success');
        })
        // 点击展开功能
        $(".unfold").click(function () {
            $(".form_con").toggleClass('hauto');
            if($(this).text() == '展开功能'){
                $(this).text('收起功能');
            }else{
                $(this).text('展开功能');
            }
        });
        
        //导出
        function exportLogs(){
        	var customerId = $("#customerId").val();
        	var brandId = $("#brandId").val();
        	var goodsName = $("#goodsName").val();
        	var goodsNo = $("#goodsNo").val();
        	var merchandiseCatId = $("#merchandiseCatId").val();
        	var countryId = $("#countryId").val();
       		$custom.alert.warning("是否确认导出？",function(){
       		    location.href="/supplierInquiryPool/exportSupplier.asyn?customerId="+customerId+"&brandId="+brandId+"&goodsName="+goodsName+"&goodsNo="+goodsNo+"&merchandiseCatId="+merchandiseCatId+"&countryId="+countryId;
       		});
        }	
    </script>
