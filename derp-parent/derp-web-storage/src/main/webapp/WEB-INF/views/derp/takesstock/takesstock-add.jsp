<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Start content -->
<jsp:include page="/WEB-INF/views/common.jsp" />
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <form id="add-form" action="/merchandise/saveMerchandise.asyn">
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
                                            <li class="breadcrumb-item"><a href="#">新增/盘点指令</a></li>
                                        </ol>
                                    </div>
                                </div>
                                
                                <div class="edit_box mt20">
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red">*</i> 盘点仓库：</label>
                                        <select name="depotId" class="edit_input" id="depotId">
                                            <option value="">全部</option>
                                        </select>
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label">委托单位：</label>
                                        <input type="text"  class="edit_input" value="${user.merchantName }" disabled="disabled">
                                    </div>
                                    <div class="edit_item"></div>
                                </div>
                                <div class="edit_box">
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red">*</i> 业务场景：</label>
                                        <select class="edit_input" id="model" name="model">
                                        </select>
                                    </div>
                                    <div class="edit_item">
                                        <label class="edit_label"><i class="red">*</i> 服务类型：</label>
                                        <select class="edit_input" id="serverType" name="serverType">
                                        </select>
                                    </div>

                                    <div class="edit_item"></div>
                                </div>
                                <div class="edit_box">
                                    <div class="edit_item" style="padding-left: 25px;">
                                        <label class="edit_label" style="flex: 0.4">备注：</label>
                                        <textarea class="edit_input" id="remark" maxlength="100"></textarea>
                                    </div>
                                    <div class="edit_item"></div>
                                </div>

                                <!--  title   基本信息  start -->
                                <!--   商品信息  start -->
                                <div class="title_text">商品信息</div>
                                <div class="mt10">
                                    <table id="datatable-buttons" class="table table-striped" cellspacing="0"
                                           width="100%">
                                        <thead>
                                        <tr>
                                            <th style="white-space:nowrap;">操作</th>
                                            <th style="white-space:nowrap;">商品编号</th>
                                            <th style="white-space:nowrap;">商品货号</th>
                                            <th style="white-space:nowrap;">商品名称</th>
                                            <th style="white-space:nowrap;">条码</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                             <tr >
					                        		 <td><i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i></td>
					                        		 <td><button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="$erpPopup.orderGoods.init('5',this,'depotId');">选择商品</button></td>
						                             <td></td>
						                             <td></td>
						                             <td></td>
					                        	</tr>
                                        </tbody>
                                    </table>
                                </div>
                                <!--   商品信息  end -->
                                <div class="form-row">
                                    <div class="col-12">
                                        <div class="row">
                                            <div class="col-6">
                                                <button type="button" class="btn btn-info waves-effect waves-light fr btn_default" onclick="saveTakes();" id="save-buttons">保 存</button>
                                            </div>
                                            <div class="col-6">
                                                <button type="button" class="btn btn-light waves-effect waves-light btn_default" onclick="javascript:$module.load.pageStorage('act=9001');">关 闭</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 新增弹窗部分   start -->
                    </div>
                    <!-- end row -->
        </form>
          <!-- 弹窗开始 -->
          <jsp:include page="/WEB-INF/views/modals/8011_V2.jsp" />
    </div>
    <!-- container -->
</div>
</div> <!-- content -->
<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="serverType"]')[0],"takesStock_serverTypeList",null);
    $module.constant.getConstantListByNameURL.call($('select[name="model"]')[0],"takesStock_modelList",null);
    $(document).ready(function() {
        //加载仓库
        $module.depot.getSelectBeanByMerchantRel.call($("select[name='depotId']")[0],null, {"isInOutInstruction": "1"});

    });


	//添加行
	function addtr(){
	    $("#datatable-buttons").append('<tr>'+
	                                    '<td><i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i></td>'+  
	                                    '<td><button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick="$erpPopup.orderGoods.init(\'5\',this,\'depotId\');">选择商品</button></td>'+
	                                    '<td></td>'+
	                                    '<td></td>'+
										'<td></td>'+ 
	                                    '</tr>'); 
	}

	//删除行
	function deltr(obj){
	    var rows = $(obj).parent("td").parent("tr").parent("tbody").find("tr").length ;
	    if(rows>1){
	           $(obj).parent("td").parent("tr").remove();   
	    }else{
	           $(obj).parent("td").parent("tr").replaceWith('<tr>'+
	                                    '<td ><i class="fi-plus" onclick="addtr();"></i>&nbsp;&nbsp;&nbsp;<i class="fi-minus" onclick="deltr(this);"></i></td>'+  
	                                    '<td><button type="button" class="btn btn-find waves-effect waves-light" onclick="$erpPopup.orderGoods.init(\'5\',this,\'depotId\');">选择商品</button></td>'+
	                                    '<td></td>'+
	                                    '<td></td>'+
										'<td></td>'+ 
	                                    '</tr>'); 
	    }
	}
		
	//保存按钮
	function saveTakes(){
		
		    var depotId=$("#depotId").val();//调入仓库
			var model=$("#model").val();//业务场景
			var serverType=$("#serverType").val();//服务类型
			var remark=$("#remark").val();//备注

			//检查必填参数
			if(depotId==null||depotId==""||depotId==undefined){
			     $custom.alert.error("请选择仓库！");
			     return false;
			}
			if(model==null||model==""||model==undefined){
			     $custom.alert.error("请选择业务场景！");
			     return false;
			}
			if(serverType==null||serverType==""||serverType==undefined){
			     $custom.alert.error("请选择服务类型！");
			     return false;
			}

        var json = {};
			json.depotId=depotId;
			json.model=model;
			json.serverType=serverType;
			json.remark=remark;

			//商品		 	 	 	
			var goodsList = [];
			var check = true;
			$("#datatable-buttons tbody tr").each(function (index, item) {
			
	                var goodsId = $(this).find("td").eq(1).find("input").val();//商品id
	                var goodsNo = $(this).find("td").eq(2).find("input").val();//商品货号
	                var barcode = $(this).find("td").eq(4).find("input").val();//条形码
					if(goodsId==null||goodsId==""||goodsId==undefined){
					     $custom.alert.error("请选择商品！");
					     check = false;
					     return ;
					}
					var goods={};
					goods.goodsId = goodsId;
					goods.goodsNo = goodsNo;
	                goods.barcode = barcode;
                    goodsList.push(goods);
             }); 
            if(check == false) return false;
			json.goodsList = goodsList;
			var jsonStr = JSON.stringify(json);
			var url = serverAddr+"/takesstock/saveTakesStock.asyn?token="+token;
		    $.ajax({
				type : 'POST',
				url : url,
				data:jsonStr,
				contentType:'application/json',
				success:function(result) {
					if(result.state == '200'&&result.data.code == '00'){
						$custom.alert.success("保存成功！");
						setTimeout(function () {
							$module.load.pageStorage('act=9001');
						}, 1000);
					}else{
					    var message = result.data.message;
					    if(message!=null&&message!=''&&message!=undefined){
					        $custom.alert.error(message);
					    }else{
					         $custom.alert.error("保存失败！");
					    }
					}
						
				}
		    });
		}
			
    //弹框取消按钮
    $("#cancel-button").click(function(){
        $(".fade").removeClass("show");
    });

    $("#depotId").change(function() {
        $("#datatable-buttons  tbody").empty();
        addtr();
    });

    /**
     * 全选
     */
    $('#datatable-buttons').on("change", ":checkbox", function () {
        // 列表复选框
        if ($(this).is("[name='keeperUserGroup-checkable']")) {
            // 全选
            $(":checkbox", '#datatable-buttons').prop("checked", $(this).prop("checked"));
        } else {
            // 一般复选
            var checkbox = $("tbody :checkbox", '#datatable-buttons');
            $(":checkbox[name='cb-check-all']", '#datatable-buttons').prop('checked', checkbox.length == checkbox.filter(':checked').length);
        }
    });
</script>
