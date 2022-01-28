<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />

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
                                <li class="breadcrumb-item"><a href="#">标准成本单价管理</a></li>
                                <li class="breadcrumb-item"><a href="#">标准成本单价列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <ul class="nav nav-tabs">
                        <li class="nav-item">
                            <a href="#" data-toggle="tab" class="nav-link active" onclick="$module.load.pageReport('act=13001');">单价管理</a>
                        </li>
                        <li class="nav-item">
                            <a href="#poList" data-toggle="tab" class="nav-link" onclick="$module.load.pageReport('act=13006');">待审核单价</a>
                        </li>
                    </ul>
                    <div class="tab-content">
                    <!--  title   end  -->
                    <form id="form1">
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                     <span class="msg_title">条形码 :</span>
                                    	<input type="text" required="" parsley-type="text" class="input_msg" id="barcode"name="barcode">
                                    <span class="msg_title">商品名称 :</span>
                                    	<input type="text" required="" parsley-type="text" class="input_msg" id="goodsName"name="goodsName">
                                    <span class="msg_title">品牌 :</span>
                                    <select name="brandId" id="brandId" class="input_msg">
                                        <option value="">请选择</option>
                                        <c:forEach items="${brandBean }" var="brand">
                                            <option value="${brand.value }">${brand.label }</option>
                                        </c:forEach>
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">是否组合 :</span>
                                    <select name="isGroup" id="isGroup" class="input_msg">
                                    </select>
                                    <span class="msg_title">状态 :</span>
                                    <select name="status" id="status" class="input_msg">
                                    </select>
                                    <span class="msg_title">事业部 :</span>
                                    <select name="buId" id="buId" class="input_msg">
                                    </select>
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="settlementPrice_add">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                            </shiro:hasPermission>

                            <shiro:hasPermission name="settlementPrice_import">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="import-buttons">批量导入</button>
                            </shiro:hasPermission>

                            <shiro:hasPermission name="settlementPrice_exportSettlementPrice">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="exportOrder">批量导出</button>
                            </shiro:hasPermission>
                            
                            <shiro:hasPermission name="settlementPrice_toExamine">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="toExamine">提交审核</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10">
                    <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>
                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                    <span></span>
                                </label>
                            </th>
                            <th>所属公司</th>
                            <th>事业部</th>
                            <th>商品名称</th>
                            <th>条形码</th>
                            <th>品牌</th>
                            <th>规格型号</th>
                            <th>结算币种</th>
                            <th>标准成本单价</th>
                            <th>生效年月</th>
                            <th>是否组合</th>
                            <th>修改时间</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                    </div>
                    </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                <!-- end row -->
            </div>
            <!-- container -->
        </div>
    </div>
    </div> <!-- content -->
<script type="text/javascript">
    var buId = '${buId}';
    var barcode = '${barcode}';
    $("#barcode").val(barcode);
	$module.constant.getConstantListByNameURL.call($('select[name="isGroup"]')[0],"settlementPrice_isGroupList",null);
	$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"settlementPrice_statusList",null);
	//事业部
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],buId, null,null);
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
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params = {
            url: serverAddr+'/settlementPrice/listPrice.asyn?r=' + Math.random(),
            columns: [{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            {data: 'merchantName'}, {data: 'buName'},{data: 'goodsName'}, {data: 'barcode'}, {data: 'brandName'},{data: 'goodsSpec'},{data: 'currencyLabel'}, 
            {data: 'price'},{data: 'effectiveDate'}, {data: 'isGroupLabel'},{data: 'modifyDate'},
            {data: 'statusLabel'},
            { //操作
                orderable: false,
                data: null,
                width:'70px',
                render: function (data, type, row, meta) {
                	
                	var html = '' ;
                	
                	if(row.status != '001') {
	                	html += '<shiro:hasPermission name="settlementPrice_edit"><a href="javascript:edit(' + row.id + ')">编辑</a></shiro:hasPermission>  '
                	}
                    
                	html += '<shiro:hasPermission name="settlementPrice_details"><a href="javascript:details(' + row.id + ')">详情</a></shiro:hasPermission>' ;
                	
                	return html;
                }
            },
            ],
            formid: '#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            if(aData.goodsName != null && aData.goodsName != undefined && aData.goodsName.length > 20){
                $('td:eq(3)', nRow).html("<span title='" + aData.goodsName + "'>" + aData.goodsName.substring(0, 20) + "...</span>");               
            }
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
    });

    function searchData() {
        $mytable.fn.reload();
    }
	 //新增
	 $("#add-buttons").click(function(){
		$module.load.pageReport("act=13002");
	 });
    //详情
    function details(id) {
    	$module.load.pageReport("act=13004&id=" + id);
    }

    //编辑
    function edit(id) {
    	$module.load.pageReport("act=13003&id=" + id);
    }

    //批量导入
    $("#import-buttons").click(function () {
    	$module.load.pageReport("act=13005");
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
    $("#exportOrder").on("click",function(){
    	//根据筛选条件导出
    	var jsonData=null;
    	var jsonArray=$("#form1").serializeArray();
        $.each(jsonArray, function(i, field){
            if(field.value!=null&&field.value!=''&&field.value!=undefined){
                jsonData=$json.setJson(jsonData,field.name,field.value);
            }
        });
        var form = JSON.parse(jsonData);
       
        $custom.request.ajax(serverAddr+"/settlementPrice/getSettlementPriceCount.asyn", form, function(data){//判断导出的数量是否超出1W
        	if(data.state==200){
        		var total = data.data.total;//总数
        		if(total>10000){
        			$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
        			return;
        		}
        		//导出数据
            	var url = serverAddr+"/settlementPrice/exportSettlementPrice.asyn";
        		var i =0;
            	if(!!form){
           		    for(var key in form){
           		    	if(i==0){
               	    		url +="?";
               	    	}else{
               	    		url +="&";
               	    	}
           		    	url +=key+"="+form[key];
           		    	i++;
           		    }
            	}
            	$downLoad.downLoadByUrl(url);
        	}else{
            	if(!!data.expMessage){
            		$custom.alert.error(data.expMessage);

            	}else{
            		$custom.alert.error(data.message);

            	}
            }
        });
    });
  
  //提交审核
  $("#toExamine").on("click" , function(){
	  var ids=$mytable.fn.getCheckedRow();
	  
	  if(ids==null||ids==''||ids==undefined){
          $custom.alert.warningText("至少选择一条记录！");
          return ;
      }
	  
	  $custom.request.ajax(serverAddr+"/settlementPrice/toExamine.asyn", {"ids" : ids} , function(data){//判断导出的数量是否超出1W
		  if(data.state==200){
			  $custom.alert.success("操作成功") ;
			  $module.load.pageReport("act=13001");
          }else{
              if(!!data.expMessage){
                  $custom.alert.error(data.expMessage);

              }else{
                  $custom.alert.error(data.message);

              }
          }
	  });
  });

	
</script>