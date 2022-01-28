<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<style>
    .select2-container{border: 1px solid #dadada;}
</style>
<div>
    <!-- Signup modal content -->
    <div id="signup-modal" class="modal fade bs-example-modal-lg" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            	<div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">选择品牌</h5>
                </div>
                <div class="modal-body">
                	<form id="brand-form">
                		<div class="form-row">
                          <div class="col-6">
                              <div class="row">
                                  <label  class="col-5 col-form-label " style="white-space:nowrap;"><div class="fr">品牌名称<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <input type="text" class="form-control" name="name" id="name">
                                      <input type="hidden" class="form-control" id="unNeedIds" name="unNeedIds">
                                  </div>
                              </div>
                          </div>
                          <div class="col-6">
                              <div class="row">
                                  <label class="col-5 col-form-label"><div class="fr">匹配状态<span>：</span></div></label>
                                  <div class="col-7 ml10">
                                      <select class="form-control" name="isMatching" id="isMatching">
                                      	 <option value="">全部</option>
                                     	 <option value="0" selected>未匹配</option>
                                     	 <option value="1">已匹配</option>
                                      </select>
                                  </div>
                              </div>
                          </div> 
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='searchData();'>查询</button>
                          			<button type="reset" class="btn btn-light waves-effect waves-light ml15" >重置</button>
                          		</div>
                          	</div>
                      </div>
                      </form>
                      <div class="form-row mt20">
                   			<table id="datatable-buttons" class="table table-bordered" cellspacing="0" width="100%">
			                        <thead>
			                        <tr>
			                            <th>
			                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
			                                    <!-- <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
			                                    <span>全选</span> -->
			                                </label>
			                            </th>
			                            <th>品牌名称</th>
			                            <th>是否匹配品牌</th>
			                            <th>匹配时间</th>
			                        </tr>
			                        </thead>
			                    </table>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" onclick='$m1111.save();'>确定</button>
                          			<button type="button" class="btn btn-light waves-effect waves-light ml15"  onclick='$m1111.hide();'>取消</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">
	//重置按钮
	$("button[type='reset']").click(function(){
		$("#brand-form").find(".goods_select2").each(function(){
	    	$(this).val("");
	    });
		$("#brand-form").find(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		});
	});
	
	//储存选择过的商品id
	var unNeedIds = [];
	
	var $m1111={
	    init:function(){
			//初始化重选按钮
			$(".group-checkable").prop("checked",false);
			//重置表单
			$($m1111.params.formId)[0].reset();
			var unIds = $("#unNeedIds").val();
			if(unIds==null || unIds==""){
				unNeedIds = [];
			}else{
			    unNeedIds = unIds.split(",");
			}
			var unNeedIds_temp = "";
			if(unNeedIds!=[]){
			    for (var i = 0; i < unNeedIds.length; i++) {
			        if(unNeedIds_temp == ""){
			        	unNeedIds_temp = unNeedIds[i];
					}else{
						unNeedIds_temp = unNeedIds_temp + "," + unNeedIds[i];
					}
				}
			}
			$("#unNeedIds").val(unNeedIds_temp);
			this.show();
	    },
	    list:function(){
	        //加载表格
	        $("#datatable-buttons tr:gt(0)").remove();
	        searchData();
	    },
	    save:function(){
	    	var ids=$mytable.fn.getCheckedRow();
	    	if(ids!=""){
	        	$custom.request.ajax(
	        		$m1111.params.getListByIdsUrl,{"ids":ids},function(data){
	                	if(data.state==200){
	                        var $tr = "";
	                        $.each(data.data,function(index,event){
	                        	$tr+="<tr><td><input type='checkbox' name='brand-checkbox'><input type='hidden' name='brandId' value='"+event.id+"'></td>"+
	                            "<td>"+(event.name==null?'':event.name)+"</td>"+
	                            "</tr>";
	                        	unNeedIds.push(event.id);
	                        });
	                        $("#unNeedIds").val(unNeedIds);
	                        $("#table-list").append($tr); 
	                    }else{
	                        $custom.alert.error(data.message);
	                    }
	            	}
	        	);
	    	}
	    	$($m1111.params.modalId).modal("hide");
	    },
	    show:function(){
	        this.list();
	        $($m1111.params.modalId).modal("show");
	    },
	    hide:function(){
	        $($m1111.params.modalId).modal("hide");
	    },
	    params:{
	        getListUrl:'/brand/getList.asyn?r='+Math.random(),
	        getListByIdsUrl:'/brand/getListByIds.asyn?r='+Math.random(),
	        formId:'#brand-form',
	        modalId:'#signup-modal',
	    }
	}
	
	//配置表格参数
	$mytable.params={
	    url:$m1111.params.getListUrl,
	    columns:[{ //复选框单元格
	        className: "td-checkbox",
	        orderable: false,
	        width: "30px",
	        data: null,
	        render: function (data, type, row, meta) {
	            return '<input type="checkbox" class="iCheck">';
	        }
	    },
	    {data:'name'},{data:'isMatching'},{data:'operationDate'}
	    ],
	    formid:$m1111.params.formId
	}
	$mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
		if (aData.isMatching == '1'){
			$('td:eq(0)', nRow).html('');
            $('td:eq(2)', nRow).html('已匹配');
        } else if (aData.isMatching == '0'){
            $('td:eq(2)', nRow).html('未匹配');
        } 
	};
	//生成列表信息
	$('#datatable-buttons').mydatatables();
	
	/**
	 * 全选
	 */
	$('#datatable-buttons').on("change", ":checkbox", function() {
	    // 列表复选框
	    if ($(this).is("[name='keeperUserGroup-checkable']")) {
	        // 全选
	        $(":checkbox", '#datatable-buttons').prop("checked",$(this).prop("checked"));
	    }else{
	        // 一般复选
	        var checkbox = $("tbody :checkbox", '#datatable-buttons');
	        $(":checkbox[name='cb-check-all']", '#datatable-buttons').prop('checked', checkbox.length == checkbox.filter(':checked').length);
	    }
	});
	
	function searchData(){
	    $mytable.fn.reload();
	}
	
	//渲染 下拉框
	function selLoad(data,id){
		$("#"+id).empty();
	    var ops="<option value=''>请选择</option>";
	    $.each(data,function(index,event){
	    	if(event.value!=null){
	        	ops+="<option value='"+event.value+"'>"+event.label+"</option>";
	        }
	    });
	    $("#"+id).append(ops);
	}
</script> --%>