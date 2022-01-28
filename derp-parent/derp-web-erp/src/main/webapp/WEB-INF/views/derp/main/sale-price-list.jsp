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
                                <li class="breadcrumb-item"><a href="#">客户档案</a></li>
                                <li class="breadcrumb-item"><a href="javascript:$load.a('/customerSale/toPage.html');">销售价格管理</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1">
                        <div class="form_box">
                            <div>
                                <div class="form_item h35">
                                    <span class="msg_title">客户 :</span>
                                    <select class="input_msg" name="customerIds" id="customerIds">
		                                <option value="">请选择</option>
		                            </select>
                                    <input type="hidden" name="customerIdsStr"/>
                                    <span class="msg_title">创建人 :</span>
                                    <input type="text" class="input_msg" name="createName" id="createName">
                                    <span class="msg_title">标准形码 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="commbarcodes"name="commbarcodes">
                                  <%--  <span class="msg_title">状态 :</span>
                                    	<select name="status" class="input_msg"></select>--%>
                                    <span class="msg_title">事业部 :</span>
                                        <select name="buId" class="input_msg" id=buId></select>
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <input type="hidden"  id="formStatus" name="status" value="">
                            </div>
                            <!-- <a href="javascript:" class="unfold">展开功能</a> -->
                        </div> 
                    </form>
                    <div class="row col-12 mt20" style="margin-bottom: 20px">
                        <div class="button-list">
                            <%-- <shiro:hasPermission name="customerSale_toAddPage">
                        	    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                            </shiro:hasPermission> --%>
                            <shiro:hasPermission name="customerSale_delPriceSale">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="deleteId()">删除</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="customerSale_toImportPage">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="import-buttons">批量导入</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="customerSale_submit">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="salePrice-buttons">提交</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="customerSale_audit">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="audit-buttons">审核</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="customerSale_export">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-buttons">导出</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="mt10">
                        <div style="margin-left: 20px" class="row">
                            <ul class="nav nav-tabs">
                                <li class="nav-item">
                                    <a href="#beSubmited" data-toggle="tab" class="nav-link active" onclick="loadSubumitTable('002')">待提交（<span id="beSubmitedCount"></span>）</a>
                                </li>
                                <li class="nav-item">
                                    <a href="#beSubmited" data-toggle="tab" class="nav-link" onclick="loadSubumitTable('001');">待审核（<span id="beAuditCount"></span>）</a>
                                </li>
                                <li class="nav-item">
                                    <a href="#beSubmited" data-toggle="tab" class="nav-link" onclick="loadSubumitTable('003');">已审核（<span id="beAreadyAuditCount"></span>）</a>
                                </li>
                                <li class="nav-item">
                                    <a href="#beSubmited" data-toggle="tab" class="nav-link" onclick="loadSubumitTable('004');">已驳回（<span id="beRejectedCount"></span>）</a>
                                </li>
                                <li class="nav-item">
                                    <a href="#beSubmited" data-toggle="tab" class="nav-link" onclick="loadSubumitTable('');">全部（<span id="beAllCount"></span>）</a>
                                </li>
                            </ul>
                        </div>
                        <div class="tab-content mt10" width="100%">
                            <div class="tab-pane fade show active table-responsive"
                                 id="beSubmited">
                                <div>
                                    <table id="salePriceList"  class="table table-striped" cellspacing="0" width=2300>
                                        <thead>
                                        <tr>
                                            <th>
                                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                                    <input type="checkbox" name="keeperUserGroup-checkable" id="keeperUserGroup-checkable"
                                                           class="group-checkable"/>
                                                    <span></span>
                                                </label>
                                            </th>
                                             <th>所属公司</th>
                                             <th>事业部</th>
                                             <th>客户名称</th>
                                             <th>标准条码</th>
                                             <th>商品名称</th>
                                             <th>品牌</th>
                                             <th>规格型号</th>
                                             <th>销售价</th>
                                             <th>状态</th>
                                             <th>报价生效时间</th>
                                             <th>报价失效时间</th>
                                             <th>创建人</th>
                                             <th>审核人</th>
                                             <th>操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    <%--<table id="datatable-buttons" class="table table-striped" cellspacing="0" width=2300>
                        <thead>
                        <tr>
                            <th>
                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                    <span></span>
                                </label>
                            </th>
                            <th>所属公司</th>                            
                            <th>客户编号</th>
                            <th>客户名称</th>
                            <th>标准条码</th>
                            <th>商品名称</th>
                            <th>品牌</th>
                            <th>规格型号</th>
                            <th>币种</th>
                            <th>销售价</th>
                            <th>状态</th>
                            <th>报价生效时间</th>
                            <th>报价失效时间</th>
                            <th>创建人</th>
                            <th>创建时间</th>
                            <th>审核人</th>
                            <th>审核时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>--%>
                        <input type="hidden" value="" name="statusw" id="status">
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                <jsp:include page="/WEB-INF/views/modals/17001.jsp" />
                <jsp:include page="/WEB-INF/views/modals/17002.jsp" />
                <!-- end row -->
            </div>
            <!-- container -->
        </div>
    </div>
    </div> <!-- content -->
    <script type="text/javascript">
     //加载事业部
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null,null);
//     $module.brand.loadSelectData.call($("select[name='brandId']")[0]);
//     $module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"customer_sale_price_statusList",null);

     function deleteId(){
         //获取选中的id信息
         var ids=$mytable.fn.getCheckedRow();
         if(ids==null||ids==''||ids==undefined){
             $custom.alert.warningText("至少选择一条记录！");
             return ;
         }
         $custom.alert.warning("确定删除选中对象？",function(){
             $custom.request.ajax('/customerSale/delPriceSale.asyn',{"ids":ids},function(data){
                 if(data.state==200){
                     $custom.alert.success("删除成功");
                     //删除成功，重新加载页面
                     setTimeout("$load.a('/customerSale/toPage.html');", 500) ;
                 }else{
                     if(data.expMessage != null){
                         $custom.alert.error(data.expMessage);
                     }else{
                         $custom.alert.error(data.message);
                     }
                 }
             });
         });
     }
        $(document).ready(function () {
            showCustomer();
            //初始化数据
            loadSubumitTable('002');
            $("#formStatus").val("002");
            //获取数量
            getCountStatus();

            //重置按钮
            $("button[type='reset']").click(function () {
                $(".selectpicker").selectpicker('val', ['noneSelectedText']);
                $(".selectpicker").selectpicker('refresh');
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

            /**
             * 全选
             */
            $("input[name='keeperUserGroup-checkable']").on("change", function () {
                console.log('ddddddddddd');
                if ($(this).is(':checked')) {
                    $(":checkbox", '#salePriceList').prop("checked", $(this).prop("checked"));
                    $('#salePriceList tbody tr').addClass('success');
                } else {
                    $(":checkbox", '#salePriceList').prop("checked", false);
                    $('#salePriceList tbody tr').removeClass('success');
                }
            });
            $('#salePriceList').on("change", ":checkbox", function () {
                $(this).parents('tr').toggleClass('success');
            });
            $('#tableId').on("change", function () {
                console.log(2345)
            });
            //

            //初始化
            // $mytable.fn.init();
            //配置表格参数
            /* $mytable.params = {
                 url: '/customerSale/listSalePrice.asyn?r=' + Math.random(),
                 columns: [{ //复选框单元格
                     className: "td-checkbox",
                     orderable: false,
                     data: null,
                     render: function (data, type, row, meta) {
                         return '<input type="checkbox" name="item-checkbox" class="iCheck" value="'+data.id+'">';
                     }
                 },
                 {data: 'merchantName'},  {data: 'customerCode'}, {data: 'customerName'}, {data: 'commbarcode'},
                 {data: 'goodsName'},{data: 'brandName'},{data: 'spec'},
                 {data: 'currencyLabel'}, {data: 'salePrice'},
                 {data: 'statusLabel'},{data: 'effectiveDate'},{data: 'expiryDate'},{data: 'createName'},
                 {data: 'createDate'},{data: 'auditName'},{data: 'auditDate'},
                 ],
                 formid: '#form1'
             };*/
            //每一行都进行 回调
            /*   $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
                  if(aData.goodsName != null && aData.goodsName != undefined && aData.goodsName.length > 20){
                      $('td:eq(3)', nRow).html("<span title='" + aData.goodsName + "'>" + aData.goodsName.substring(0, 20) + "...</span>");
                  }
              }; */
            //生成列表信息
            //$('#datatable-buttons').mydatatables();
            /*   });*/


            //批量导入
            $("#import-buttons").click(function () {
                $load.a("/customerSale/toImportPage.html");
            });

            //提交
            $("#salePrice-buttons").click(function () {
                var checkeds = $.find("input[name='item-checkbox']:checked");

                var idsArray = new Array();
                if (checkeds.length == 0) {
                    $custom.alert.warningText("请选择需要提交的数据！");
                    return;
                }
                $custom.alert.warning("确定提交选中对象？", function () {
                    $(checkeds).each(function (i, m) {
                        idsArray.push($(m).val());
                    });
                    var idsStr = idsArray.join(",");
                    var check = true;
                    var ids = $mytable.fn.getCheckedRow();
                    var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
                    var index = 0;
                    for (var i = 0; i < nTrs.length; i++) {
                        if ($(nTrs[i]).find("input[type=checkbox]").prop('checked')) {
                            var row = $mytable.tableObj.fnGetData(nTrs[i]);
                            if (row.status != "002") {
                                $custom.alert.error("勾选的记录状态必须未待提交");
                                return;
                            }
                            if (index == 0) {
                                merchantId = row.merchantId;
                                customerCode = row.customerCode;
                                buId = row.buId;
                            } else {
                                if (merchantId != row.merchantId) {
                                    check = false;
                                    $custom.alert.error("勾选的记录必须为相同的公司！");
                                    return;
                                }
                                if (customerCode != row.customerCode) {
                                    check = false;
                                    $custom.alert.error("勾选的记录必须为相同的客户！");
                                    return;
                                }
                                if (buId != row.buId) {
                                    check = false;
                                    $custom.alert.error("勾选的记录必须为相同的事业部！");
                                    return;
                                }
                            }
                            index++;
                        }
                    }
                    if (check && !ids) {
                        $custom.alert.error("请至少选择一单");
                        return;
                    }
                    if (check) {
                        $custom.request.ajax("/customerSale/submitSMPrice.asyn", {"ids": idsStr}, function (data) {
                            console.log(data);
                            if (data.data.code == "00") {
                                $custom.alert.success("提交成功");
                                //重新加载页面
                                loadSubumitTable('002');
                                getCountStatus();
                            } else {
                                if (!!data.expMessage) {
                                    $custom.alert.error(data.expMessage);
                                } else {
                                    $custom.alert.error(data.data.message);
                                }
                            }
                        });
                    }
                });
            });

            //批量审核
            $("#audit-buttons").click(function () {
                var checkeds = $.find("input[name='item-checkbox']:checked");
                var idsArray = new Array();
                if (checkeds.length == 0) {
                    $custom.alert.warningText("请选择需要审核的数据！");
                    return;
                }
                $(checkeds).each(function (i, m) {
                    idsArray.push($(m).val());
                });
                var idsStr = idsArray.join(",");
                var check = true;
                var ids = $mytable.fn.getCheckedRow();
                var nTrs = $mytable.tableObj.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr
                var index = 0;
                for (var i = 0; i < nTrs.length; i++) {
                    if ($(nTrs[i]).find("input[type=checkbox]").prop('checked')) {
                        var row = $mytable.tableObj.fnGetData(nTrs[i]);
                        if (row.status != "001") {
                            $custom.alert.error("勾选的记录状态必须未待审核");
                            return;
                        }
                        if (index == 0) {
                            merchantId = row.merchantId;
                            customerCode = row.customerCode;
                            buId = row.buId;
                        } else {
                            if (merchantId != row.merchantId) {
                                check = false;
                                $custom.alert.error("勾选的记录必须为相同的公司！");
                                return;
                            }
                            if (customerCode != row.customerCode) {
                                check = false;
                                $custom.alert.error("勾选的记录必须为相同的客户！");
                                return;
                            }
                            if (buId != row.buId) {
                                check = false;
                                $custom.alert.error("勾选的记录必须为相同的事业部！");
                                return;
                            }
                        }
                        index++;
                    }
                }
                if (check && !ids) {
                    $custom.alert.error("请至少选择一单");
                    return;
                }
                if (check) {
                    $m17001.init(idsStr);
                }
            });

            //导出
            $("#export-buttons").on("click", function () {
                var param = $("#form1").serialize();
                $custom.alert.warning("确定导出？", function () {
                    //var url = serverAddr+"/customerSale/export.asyn?"+param;
                    //$downLoad.downLoadByUrl(url);
                    location.href = "/customerSale/export.asyn?" + param;
                });
            });


            // 点击展开功能
            $(".unfold").click(function () {
                $(".form_con").toggleClass('hauto');
                if ($(this).text() == '展开功能') {
                    $(this).text('收起功能');
                } else {
                    $(this).text('展开功能');
                }
            });

        });
     /*获取数据*/
     function loadSubumitTable(status) {
       /*  var thead = '<tr>'
              <th><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">' +
             '<input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" id="tableId"/><span></span></label></th>'
              <th>所属公司</th>'
              <th>事业部</th>'
              <th>客户名称</th>'
              <th>标准条码</th>'
              <th>商品名称</th>'
              <th>品牌</th>'
              <th>规格型号</th>'
              <th>销售价</th>'
              <th>状态</th>'
              <th>报价生效时间</th>'
              <th>报价失效时间</th>'
              <th>创建人</th>'
              <th>审核人</th>'
              <th>操作</th>'
            </tr>';
        $('#salePriceList').find("thead").html(thead);*/
       $('#keeperUserGroup-checkable').prop("checked",false);
         $("#formStatus").val(status);
         loadSalePriceDetail(status);
     }

     //设置表格数据
     function loadSalePriceDetail(status){
         $('#status').val(status);//状态值
         var customerIds=$('input[name="customerIdsStr"]').val();//客户ids
         var commbarcodes=$('#commbarcodes').val();//商品条码
         var buId=$('#buId').val();//事业部
         var createName=$('#createName').val();//创建人

         dttable = $('#salePriceList').dataTable();
         dttable.fnClearTable(false); //清空一下table
         dttable.fnDestroy(); //还原初始化了的datatable
         var $table=  $('#salePriceList');
         var _table = $table.dataTable({
             //自定义
             language: $mytable.lang,
             //是否允许用户改变表格每页显示的记录数
             // lengthChange: false,
             //启用服务器端分页
             serverSide: true,
             //禁用原生搜索
             searching: false,
             //允许多次初始化同一表格
             retrieve: true,
             //禁用排序
             "ordering": false, // 禁止排序
             //显示数字的翻页样式
             sPaginationType: "full_numbers",
             "dom": '<"top"rt><"bottom"lip><"clear">',
             //显示字段数据
             columns:[{ //复选框单元格
                     className: "td-checkbox",
                     orderable: false,
                     data: null,
                     render: function (data, type, row, meta) {
                         return '<input type="checkbox" name="item-checkbox" class="iCheck" value="'+data.id+'">';
                     }
                 },
                 {data: 'merchantName'},  {data: 'buName'},
                 {
                     orderable: false,
                     data: null,
                     width: '120px',
                     render: function (data, type, row, meta) {
                         return row.customerCode + '<br>' + row.customerName;
                     }
                 },
                 {data: 'commbarcode'}, {data: 'goodsName'},{data: 'brandName'},{data: 'spec'},
                 {
                     orderable: false,
                     data: null,
                     width: '120px',
                     render: function (data, type, row, meta) {
                         return row.currencyLabel + '&nbsp;' + row.salePrice;
                     }
                 },
                 {data: 'statusLabel'},
                 {
                     orderable: false,
                     data: null,
                     width: '110px',
                     render: function (data, type, row, meta) {
                         return formatDate(row.effectiveDate,'yyyy-MM-dd');
                     }
                 },
                 {
                     orderable: false,
                     data: null,
                     width: '110px',
                     render: function (data, type, row, meta) {
                         return formatDate(row.expiryDate,'yyyy-MM-dd');
                     }
                 },
                 {
                     orderable: false,
                     data: null,
                     width: '130px',
                     render: function (data, type, row, meta) {
                         var createName = '';
                         var createDate = '';
                         if (row.createName) {
                             createName = row.createName;
                         }

                         if (row.createDate) {
                             createDate = row.createDate;
                         }
                         return createName + '<br>' + createDate;
                     }
                 },
                 {
                     orderable: false,
                     data: null,
                     width: '130px',
                     render: function (data, type, row, meta) {
                         var auditName = '';
                         var auditDate = '';
                         if (row.auditName) {
                             auditName = row.auditName;
                         }

                         if (row.auditDate) {
                             auditDate = row.auditDate;
                         }
                         return auditName+ '<br>' + auditDate;
                     }
                 },
                 { //操作
                     orderable: false,
                     width: "130px",
                     data: null,
                     render: function (data, type, row, meta) {
                         var edit = "";
                         if(row.status =='002' || row.status =='004'){
                             edit = '<shiro:hasPermission name="customerSale_edit"><a href="javascript:void(0);" onclick="edit('+row.id+','+row.status+')">编辑</a></shiro:hasPermission>'
                         }
                         return edit;
                     }
                 },
             ],
             formid: '#form1',
             //异步获取数据
             ajax:function(data, callback, settings){
                 $custom.request.ajax('/customerSale/listSalePrice.asyn?r=' + Math.random(), {"customerIds":customerIds,"commbarcodes":commbarcodes,"buId":buId,"status":status,"createName":createName,"begin":data.start,"pageSize":data.length},
                     function(result) {
                         //异常判断与处理
                         if (result.errorCode) {
                             $custom.alert.error("查询失败");
                             return;
                         }
                         //封装返回数据
                         var returnData = {};
                         returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                         returnData.recordsTotal = result.data.total;//总记录数
                         returnData.recordsFiltered = result.data.total;//后台不实现过滤功能，每次查询均视作全部结果
                         returnData.data = result.data.list;
                         //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                         //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                         callback(returnData);
                     });
             },
         });
         //生成列表信息
         $('#salePriceList').mydatatables();

         var customerIdsArr = customerIds.split(",");
         $('select[name="customerIds"]').selectpicker('val', customerIdsArr);
     }

     //获取状态数量
     function getCountStatus(){
         var buId=$('#buId').val();//事业部
         var status=$('#status').val();//状态值
         var customerIds=$('input[name="customerIdsStr"]').val();//客户ids
         var commbarcodes=$('#commbarcodes').val();//商品条码
         var createName=$('#createName').val();//创建人

         $custom.request.ajax("/customerSale/listSalePriceCount.asyn",  {"customerIds":customerIds,"commbarcodes":commbarcodes,"buId":buId,"status":status,"createName":createName},function(data){
             if(data.state==200){
                 console.log(data);
                 $("#beAllCount").text(data.data.beAll);
                 $("#beAuditCount").text(data.data.beAudit);
                 $("#beAreadyAuditCount").text(data.data.beAreadyAudit);
                 $("#beRejectedCount").text(data.data.beRejected);
                 $("#beSubmitedCount").text(data.data.beSubmited);
             }
         });
     }

     //搜索框
     function searchData() {
         //$mytable.fn.reload();
         var customerIds = $('select[name="customerIds"]').val();
         $('input[name="customerIdsStr"]').val(customerIds);
         var status=$("#status").val();
         loadSubumitTable(status);
         getCountStatus();
     }

     //编辑
     function edit(id,status){
         $m17002.init(id,status);
     }

     function formatDate(date, fmt) {
         if (!date) {
             return '';
         }
         date = date;
         fmt=fmt || 'yyyy-MM-dd hh:mm:ss';
         if(Object.prototype.toString.call(date).slice(8,-1)!=='Date'){
             date=new Date(date)
         }
         if (/(y+)/.test(fmt)) {
             fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
         }
         let o = {
             'M+': date.getMonth() + 1,
             'd+': date.getDate(),
             'h+': date.getHours(),
             'm+': date.getMinutes(),
             's+': date.getSeconds()
         }
         for (let k in o) {
             if (new RegExp("(" + k + ")").test(fmt))
                 fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
         }
         return fmt
     }
     function showCustomer(){
         var selectObj=$('select[name="customerIds"]');
         var json={"type": ""}
         var jsonData=$module.constant.ajax($module.params.getCustomerByMerchantURL,json);

         selectObj.empty();

         width = "136px" ;
         $(selectObj).removeClass("input_msg") ;

         $(selectObj).wrap('<div style="width:'+ width +'; display: inline-block;"></div>') ;

         $(selectObj).addClass("selectpicker").addClass("show-tick") ;
         $(selectObj).attr("data-live-search", "true") ;
         $(selectObj).attr("multiple","multiple") ;

         jsonData.forEach(function(o,i){
             selectObj.append("<option value='"+ o.value+"'>"+o.label+"</option>");
         });

         $(selectObj).selectpicker({width: width});
         $(selectObj).selectpicker({noneSelectedText : '请选择'});
         $(selectObj).selectpicker('refresh');

         $(".selectpicker").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white"}) ;
         $(".selectpicker").prev().css({"z-index":"99"});
         $(".selectpicker").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;

     }

    </script>