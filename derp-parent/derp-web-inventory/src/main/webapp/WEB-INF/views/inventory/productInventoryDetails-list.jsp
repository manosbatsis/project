<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
                       <li class="breadcrumb-item"><a href="#">库存管理</a></li>
                       <li class="breadcrumb-item "><a href="javascript:list();">商品库存明细</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div >
                         
                                <div class="form_item h35">
                                    <span class="msg_title">公司 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="merchantName">
                                    <input class="input_msg" type="hidden" name="depotIds" id="depotIds">
                                   
                                    <span class="msg_title">仓库 :</span>
                                    <div style="min-width:136px;max-width: 635px;display: inline-block;">
		                                <select name="depotId" class="input_msg goods_select2" id="depotId">
		                                    <option value="">请选择</option>
		                                </select>
	                                </div>	
                                    <span class="msg_title">商品货号 :</span>
                                    <input type="text" required="" parsley-type="text" value="${goodsNo}" id="goodsNo" class="input_msg" name="goodsNo">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData()' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">商品条码 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="barcode">
                                    <span class="msg_title">品牌 :</span>
                                    <select name="brandId" id="brandId"  class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                </div>
                            </div>

                        </div>
                   </form> 
                      <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="productInventoryDetails_exportPInventoryDetails">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="productInventoryDetails">导出</button>
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
<!--                             <th>序号</th> -->
                            <th>公司</th>
                            <th>仓库名称</th>
                            <th>商品货号</th>
                            <th>商品名称</th>
                            <th>商品条码</th>
                            <th>品牌名称</th>
                            <th>库存数量</th>
                            <th>冻结数量</th>
                            <th>坏品数量</th>
                            <th>过期数量</th>
                            <th>可用量</th>
                            <th>单位</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                    <!--  ================================表格  end ===============================================   -->
                </div>
                    <!--======================Modal框===========================  -->
        <jsp:include page="/WEB-INF/views/modals/1011.jsp" />
                  <!-- end row -->
        </div>
        <!-- container -->
    </div>
    </div>
</div> <!-- content -->
<script type="text/javascript">
$("button[type='reset']").click(function(){
    $(".goods_select2").each(function(){
       $(this).val("");
   }); 
    $("#depotId").select2({
		 language:"zh-CN",
		 placeholder: '请选择',
		 multiple: true,
		 closeOnSelect: true
	});
    
});

$(document).ready(function() {
	  $("#depotId").select2({
			 language:"zh-CN",
			 placeholder: '',
			 multiple: true,
			 closeOnSelect: true
		});
	$module.brand.loadSelectData.call($("select[name='brandId']")[0],"");
	
	//加载仓库下拉
	$module.depot.loadSelectData.call($("select[name='depotId']")[0],"");
    
	
    //初始化
    $mytable.fn.init();
    //配置表格参数
    $mytable.params={
   		url: serverAddr+'/productInventoryDetails/listProductInventoryDetails.asyn?r='+Math.random(),    
        columns:[{ //复选框单元格
            className: "td-checkbox",
            orderable: false,
            width: "30px",
            data: null,
            render: function (data, type, row, meta) {
                return '<input type="checkbox"  depotId='+data.depotId+' goodsId='+data.goodsId+'  name="iCheck" class="iCheck">';
            }
        },
//         {  //序号
//             data : null,  
//             bSortable : false,  
//             className : "text-right",  
//             width : "30px",  
//             render : function(data, type, row, meta) {  
//                 // 显示行号  
//                 var startIndex = meta.settings._iDisplayStart;  
//                 return startIndex + meta.row + 1;  
//                     }  
//         },
        {data:'merchantName'},{data:'depotName'},{data:'goodsNo'},{data:'goodsName'},
        {data:'barcode'},{data:'brandName'},
        {data:'surplusNum'},{data:'freezeNum'},{data:'badNum'},{data:'okayNum'},
        {data:'availableNum'},{data:'unitLabel'},{ //操作
            orderable: false,
            width: "50px",
            data: null,
            render: function (data, type, row, meta) {
                return '<shiro:hasPermission name="productInventoryDetails_detail"><a href="javascript:details(\''+row.goodsNo+'\','+row.depotId+')">详情</a></shiro:hasPermission>';
            }
        },
        ],
        formid:'#form1'
    };
  	//每一行都进行 回调
   /*  $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
    	if (aData.unit == '0'){
            $('td:eq(12)', nRow).html('托盘');
        }else if (aData.unit == '1'){
            $('td:eq(12)', nRow).html('箱');
        }else if (aData.unit == '2'){
            $('td:eq(12)', nRow).html('件');
        }else {
            $('td:eq(12)', nRow).html(aData.unit);
        }
    } */
    //生成列表信息
    $('#datatable-buttons').mydatatables();

} );


function list(){
	$module.load.pageOrder("bls=6012");
}



function searchData(){
	 var depotIds = $('select[name="depotId"]').val();
	 $("#depotIds").val(depotIds);
     $mytable.fn.reload();
}
 //进入批次库存明细详情
 function details(goodsNo,depotId){
		$module.load.pageInventory("bls=6010&goodsNo="+goodsNo+"&depotId="+depotId);
 }
 
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
 
 //导出
 $("#productInventoryDetails").on("click",function(){
     //获取选中的id信息
   var depotIds = $('select[name="depotId"]').val();
   var goodsNo=  $("#goodsNo").val();
     $custom.alert.warning("确定导出筛选的数据？",function(){
         var url=serverAddr+"/productInventoryDetails/exportProductInventoryDetails.asyn?depotIds="+depotIds+"&goodsNo="+goodsNo;
         //window.open(url);
         $downLoad.downLoadByUrl(url);
     });
 });
//点击展开功能
 $(".unfold").click(function () {
     $(".form_con").toggleClass('hauto');
     if($(this).text() == '展开功能'){
         $(this).text('收起功能');
     }else{
         $(this).text('展开功能');
     }
 });
</script>
