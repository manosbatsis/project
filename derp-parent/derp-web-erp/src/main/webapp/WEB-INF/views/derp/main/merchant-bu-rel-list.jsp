<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!-- Start content  -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- end row -->
        <div class="row">
            <div class="col-12">
                <div class="card-box table-responsive">
                    <div class="col-12 row">
                        <div>
                            <ol class="breadcrumb">
                                <li><i class="block"></i> 当前位置：</li>
                                <li class="breadcrumb-item"><a href="#">公司档案</a></li>
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/merchantBuRel/toPage.html');">公司事业部</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">公司编码 :</span>
                                    <input class="input_msg" type="text" name="merchantCode" >
                                    <span class="msg_title">公司简称 :</span>
                                    <input class="input_msg" type="text" name="merchantName" >
                                    <span class="msg_title">事业部名称 :</span>
                                    <select name="buId" class="input_msg" id="buId">
                                        <option value="">请选择</option>
                                        <c:forEach items="${businessUnitList }" var="businessUnit">
                                            <option value="${businessUnit.id }">${businessUnit.name }</option>
                                        </c:forEach>
                                	</select>
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light reset btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <shiro:hasPermission name="merchant_bu_rel_add">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="add-buttons" onclick="$m10002.init();">新增</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="merchant_bu_rel_export">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="export-buttons" onclick="">导出</button>
                        </shiro:hasPermission>
                    </div>
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                            	<th class="tc">公司编码</th>
                                <th class="tc">公司简称</th>
                                <th class="tc">事业部编码</th>
                                <th class="tc">事业部</th>
                                <th class="tc">状态</th>
                                <th class="tc">采购价格管理</th>
                                <th class="tc">销售价格管理</th>
                                <th class="tc">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
                <jsp:include page="/WEB-INF/views/modals/10002.jsp" />
                <jsp:include page="/WEB-INF/views/modals/10003.jsp" />
            </div>
        </div>

        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->



<script type="text/javascript">

    $(document).ready(function() {

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/merchantBuRel/listMerchantBuRel.asyn?r='+Math.random(),
            columns:[
                {data:'merchantCode'},{data:'merchantName'},{data:'buCode'},{data:'buName'},{data:'statusLabel'},
                {data:'purchasePriceManageLabel'},
                {data:'salePriceManageLabel'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {
                        var edit = "";
                    	/* if(row.status == '0'){
                            edit='<shiro:hasPermission name="merchant_bu_rel_status"><a href="javascript:editStaus('+row.id+',\'1\')">启用</a></shiro:hasPermission>  ' ;
                    	}else{
                            edit='<shiro:hasPermission name="merchant_bu_rel_status"><a href="javascript:editStaus('+row.id+',\'0\')">禁用</a></shiro:hasPermission>  ' ;
                    	} */
                    	return edit+'<shiro:hasPermission name="merchant_bu_rel_edit"><a href="javascript:editPurchasePriceManage('+row.id+')">编辑</a></shiro:hasPermission>';
                    }
                },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

    });

    function searchData(){
        $mytable.fn.reload();
    }

    //编辑
    function editPurchasePriceManage(id){
        $custom.request.ajax("/merchantBuRel/toDetailsPage.asyn",{"id":id},function(data1){
            if(data1.state==200){
                $m10003.init(data1.data);
            }else{
                if(!!data1.expMessage){
                    $custom.alert.error(data1.expMessage);
                }else{
                    $custom.alert.error(data1.message);
                }
            }
        });
    }
    //删除
/*     function editStaus(id,status){
    	
    	var str = "" ;
    	if(status == '0'){
    		str = "禁用" ;
    	}else if(status == '1'){
    		str = "启用" ;
    	}
    	
    	$custom.alert.warning("是否" + str , function(){
    		var url = "/merchantBuRel/changeStaus.asyn" ;
        	
        	$custom.request.ajax(url,{"id":id,"status":status},function(data) {
                if (data.state == 200) {
                	$custom.alert.success(data.message);
                    //重新加载table表格
                	$mytable.fn.reload();
                } else {
                	
                	if(data.state == 313){
                		$custom.alert.error("该事业部已被引用，无法删除");
                		
                		return ;
                	}
                	
                    $custom.alert.error(data.expMessage);
                }
            });
    	}) ;
    	
    } */
    
  //导出
    $("#export-buttons").on("click",function(){
    	//根据筛选条件导出
    	var jsonData=null;
    	var jsonArray=$("#form1").serializeArray();
        $.each(jsonArray, function(i, field){
            if(field.value!=null&&field.value!=''&&field.value!=undefined){
                jsonData=$json.setJson(jsonData,field.name,field.value);
            }
        });
        var form = JSON.parse(jsonData);
        
      //导出数据
    	var url = "/merchantBuRel/exportMerchantBuRel.asyn";
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
    });

</script>