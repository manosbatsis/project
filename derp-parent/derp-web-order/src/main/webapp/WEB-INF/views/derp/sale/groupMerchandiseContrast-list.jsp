<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- end row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">爬虫配置</a></li>
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/groupMerchandiseContrast/toPage.html');">商品对照表</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1">
                        <div class="form_box">
                            <div >
                                <div class="form_item h35">
                                    <span class="msg_title">店铺 :</span>
                                    <select id="shopCode" name="shopCode" class="selectpicker show-tick form-control" data-live-search="true"
                                            data-first-option="false" title='可按编码/名称查询'>
                                    </select>
                                    <span class="msg_title">商品ID :</span>
                                    <input class="input_msg" type="text" name="skuId" required="" id="skuId">
                                    <span class="msg_title">公司 :</span>
                                    <select name="merchantId" class="input_msg" id="merchantId">
                                        <option value="">请选择</option>
                                        <c:forEach items="${merchantSelectBean }" var="merchant">
                                            <option value="${merchant.value }">${merchant.label }</option>
                                        </c:forEach>
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default"
                                                onclick='searchData();'>查询
                                        </button>
                                        <button type="button" class="btn btn-light waves-effect waves-light btn_default" onclick="formReset();">
                                            重置
                                        </button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">商品名称 :</span>
                                    <input class="input_msg" type="text" name="groupGoodsName" required="" style="width:190px;" id="groupGoodsName">
                                    <span class="msg_title">状态 :</span>
                                    <select class="input_msg" name="status" id="status">
                                    </select>
                                    <span class="msg_title">更新时间 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="updateStartDate" id="updateStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="updateEndDate" id="updateEndDate">

                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="groupMerchandiseContrast_add">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">
                                    新增
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="groupMerchandiseContrast_import">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        id="import-buttons">导入
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="groupMerchandiseContrast_exportInfo">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        onclick="exportInfo()">导出
                                </button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="groupMerchandiseContrast_delete">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm"
                                        onclick="$custom.request.delChecked(serverAddr + '/groupMerchandiseContrast/deleteGroupMerchandise.asyn');">删除
                                </button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <div class="mt10 table-responsive" style="width: 100%">
                        <table id="datatable-buttons" class="table table-striped twoRows" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable"
                                               class="group-checkable"/>
                                        <span></span>
                                    </label>
                                </th>
                                <th>店铺编码</th>
                                <th>店铺名称</th>
                                <th>公司</th>
                                <th>商品ID</th>
                                <th>商品名称</th>
                                <th>状态</th>
                                <th style="min-width: 100px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

		<div class="popupbg" style="display: none">
		    <div class="popup_box">
		        <img src="/resources/assets/images/uploading.gif" alt="">
		        <p>正在校验是否被使用中...</p>
		    </div>
		</div>
        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div>
<!-- content -->
      

<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"groupMerchandiseContrast_statusList",null);
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
            url: serverAddr+'/groupMerchandiseContrast/listGroupMerchandiseContrast.asyn?r=' + Math.random(),
            columns: [{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            {data: 'shopCode'},{data: 'shopName'},{data:'merchantName'},{data:'skuId'},{data:'groupGoodsName'},{data:'statusLabel'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<shiro:hasPermission name="groupMerchandiseContrast_edit"><a href="javascript:edit(' + row.id + ')">编辑</a></shiro:hasPermission>' +' '
                            + '  <shiro:hasPermission name="groupMerchandiseContrast_detail"><a href="javascript:details(' + row.id + ')">详情</a></shiro:hasPermission>';
                    }
                },
            ],
            formid: '#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback = function (nRow, aData, iDisplayIndex) {

        };
        
        getShopCodeSelect() ;
        
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        

    });

    function searchData() {
        $mytable.fn.reload();
    }

    //新增
    $("#add-buttons").click(function () {
        $module.load.pageOrder("act=110032");
    });

    //详情
    function details(id) {
        $module.load.pageOrder("act=110031&id="+id);
    }

    //编辑
    function edit(id) {

        $module.load.pageOrder("act=110033&id="+id);
    }

    //导入
    $("#import-buttons").click(function () {
        $module.load.pageOrder("act=110034");
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
    });
    
    $(".date-input").datetimepicker({
        language:  'zh-CN',
        format: "yyyy-mm-dd",
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2
    });
    
    $(function () {
        $("#datatable-buttons_wrapper").removeClass('container-fluid');
    });

    //导出
    function exportInfo(){
        var shopCode = $("#shopCode").val();
        var merchantId = $("#merchantId").val();
        var skuId = $("#skuId").val();
        var status = $("#status").val();
        var groupGoodsName = $("#groupGoodsName").val();
        var updateStartDate = $("#updateStartDate").val();
        var updateEndDate = $("#updateEndDate").val();
        $custom.alert.warning("是否确认导出？",function(){
//            window.open(serverAddr+"/groupMerchandiseContrast/exportInfo.asyn?shopCode="+shopCode+"&merchantId="+merchantId);

            //导出
            var url = serverAddr+"/groupMerchandiseContrast/exportInfo.asyn?shopCode="+shopCode+"&merchantId="+merchantId+"&status="+status
                +"&skuId="+skuId+"&groupGoodsName="+groupGoodsName+"&updateStartDate="+updateStartDate+"&updateEndDate="+updateEndDate;
            $downLoad.downLoadByUrl(url);
        });
    }

    /**
     *   下拉搜索
     */
     $("#shopCode").prev().find(".bs-searchbox").find("input").keyup(
         function(){
             var shopName = $("#form1 .open input").val() ;
             getShopCodeSelect(shopName) ;
         }
     );


    $('#shopCode').on('hidden.bs.select', function (e) {
        var shopSelectText  = $('#shopCode').find('option:selected')[0].textContent;
        var shopCode = $("#shopCode").val();
        var dataSource = $('#shopCode').find("option:selected").attr("source")
        if (shopCode && dataSource) {
            $(".filter-option").html(shopSelectText.substring(shopCode.length+dataSource.length+4));
        }
    });

    /**
     * 获取标准品牌下拉
     */
     function getShopCodeSelect(shopName){
         var url = serverAddr + "/groupMerchandiseContrast/getShop.asyn" ;
         $custom.request.ajax(url,{"shopName":shopName},function(data) {
             if (data.state == 200) {
                 var html = "" ;
                 
                 var shopList = data.data ;
                 
                 if(!shopName){
                     html += '<option value="">请选择</option>' ;
                 }
                 
                 $(shopList).each(function(index , shop){
                     html += '<option value="'+shop.shopCode+'" source="' + shop.dataSourceName + '">'  + shop.dataSourceName +'&nbsp;&nbsp;' + shop.shopCode + '&nbsp;&nbsp;' +shop.shopName + '</option>' ;
                 
                 }) ;
                 
                 $("#shopCode").html(html) ;
                 $('#shopCode').selectpicker({width: '190px'});
                 $('#shopCode').selectpicker('refresh');
                 
                 $("#shopCode").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white","font-size": "12px"}) ;
                 $("#shopCode").prev().css({"z-index":"99"}) ;
                 $("#shopCode").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;
                 
             } else {
                 $custom.alert.error(data.message);
             }
         });
     }
     
     function formReset(){
    	 getShopCodeSelect() ;
         
         $("#form1")[0].reset() ;
         
         searchData() ;
     }
        
</script>