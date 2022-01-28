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
                                <li class="breadcrumb-item "><a href="javascript:$load.a('/supplierMerchandise/toPage.html');">供应商商品列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="">
                                <div class="form_item h35">
                                    <span class="msg_title">商品编码 :</span>
                                    <input class="input_msg" type="text" name="goodsCode" >
                                    <span class="msg_title">品牌编码 :</span>
                                    <input class="input_msg" type="text" name="brandCode" >
                                    <span class="msg_title">条形码 :</span>
                                    <input class="input_msg" type="text" name="barcode" >
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light reset btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">	                                
	                                <span class="msg_title">事业部 :</span>
	                                <select class="input_msg "  name="buId" id=buId>
	                                </select>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <shiro:hasPermission name="supplierMerchandise_export">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="export" ">导出</button>
                        </shiro:hasPermission>
                    </div>
                    <div class="row mt20">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
	                            <th>
	                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
	                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
	                                    <span></span>
	                                </label>
	                            </th>
                                <th>商品编码</th>
                                <th>商品名称</th>
                                <th>品牌名称</th>
                                <th>品牌编码</th>
                                <th>条形码</th>
                                <th>产品类型</th>
                                <th>建议零售价</th>
                                <th>经销商货号</th>
                                <th>事业部</th>
                                <th>数据来源</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->



<script type="text/javascript">
//加载事业部
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null,null,null);
    $(document).ready(function() {

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/supplierMerchandise/listSupplierMerchandise.asyn?r='+Math.random(),
            columns:[
				{ //复选框单元格
				    className: "td-checkbox",
				    orderable: false,
				    width: "30px",
				    data: null,
				    render: function (data, type, row, meta) {
				        return '<input type="checkbox" class="iCheck">';
				    }
				},     
                {data:'goodsCode'},{data:'name'},{data:'brandName'},{data:'brandCode'},
                {data:'barcode'},{data:'goodsType'},{data:'salePrice'},
                {data:'supplierGoodsNo'},{data:'buName'},{data:'sourceLabel'},
                
                
                
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

	//导出
    $("#export").on("click",function(){
    	//根据筛选条件导出
    	var jsonData=null;
    	var jsonArray=$("#form1").serializeArray();
        $.each(jsonArray, function(i, field){
            if(field.value!=null&&field.value!=''&&field.value!=undefined){
                jsonData=$json.setJson(jsonData,field.name,field.value);
            }
        });
        debugger;
        var form = JSON.parse(jsonData);
   		//导出数据
       	var url = "/supplierMerchandise/export.asyn";
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