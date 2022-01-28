<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <div class="row">
           <div class="col-12">
              <div class="card-box table-responsive" >
                          <!--  title   start  -->
            <div class="col-12">
                <div class="card-box table-responsive">
                <div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="dripicons-location"></i> 当前位置：</li>
                       <li class="breadcrumb-item"><a href="#">枚举值管理</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/enum/toPage.html');">枚举类型列表</a></li>
                       <li class="breadcrumb-item"><a href="javascript:$load.a('/enum/toDetailsPage.html?id=${detail.id }');">枚举类型详情</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                    <div class="title_text">枚举类型明细</div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">枚举名称<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>
                                	<input type="hidden" value="${detail.id }" id="id">
                                	${detail.enumName }
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">备注<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.remark }</div>
                            </div>
                        </div>
                    </div>
                </div>  
                 <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">创建时间<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>${detail.createDate }</div>
                            </div>
                        </div>
                    </div>
                </div> 
                <div class="ml10 page-title col-12 borderb mt20">
                	<div class="row">
                         <div class="col-10">
                             <div class="ml10 page-title col-12 borderb mt20">
                               <div class="ml10">枚举值明细</div>
                             </div>
                        </div>
                         <div class="col-1 mt10">
                          <button type="button" class="btn btn-add waves-effect waves-light" onclick="$m6013.init();">新增</button>
                        </div>
                         <div class="col-1 mt10">
                          <button type="button" class="btn btn-add waves-effect waves-light" id="delete-list-button">删 除</button>
                        </div> 
                        </div>
                </div>  
                 <!--  ================================表格 ===============================================   -->
                    <div class="row col-12 mt10">
                    <table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                        	<th style="width:50px;text-align:center"><input id="checkbox11" type="checkbox" name="all"></th>
                            <th style="width:150px;text-align:center">键</th>
                            <th style="width:150px;text-align:center">名称</th>
                        </tr>
                        <c:forEach items="${list}" var="item">
                        	<tr>
                        		<td style="width:50px;text-align:center"><input type="checkbox" name="itemId" value="${item.id }"></td>
	                            <td style="width:150px;text-align:center">${item.key }</td>
	                            <td style="width:150px;text-align:center">${item.value }</td>
	                        </tr>
                        </c:forEach>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                <div class="form-row">
                    <div class="col-3">
                        <div class="form-row m-t-50">
                              <div class="row">
                                  <div class="col-5">
                                      <button type="button" class="btn btn-light waves-effect waves-light" id="cancel-buttons">返 回</button>
                                  </div>
                              </div>
                      </div>
                    </div>
                </div>
                    <!--  ================================表格  end ===============================================   -->               
             </div>
                  <!-- end row -->
                  <jsp:include page="/WEB-INF/views/modals/6013.jsp" />
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
$(function(){
	//返回按钮
	$("#cancel-buttons").click(function(){
		$load.a("/enum/toPage.html");
	});
	
	$("input[name='all']").click(function(){
		//判断当前点击的复选框处于什么状态$(this).is(":checked") 返回的是布尔类型
		if($(this).is(":checked")){
			$("input[name='item-checkbox']").prop("checked",true);
		}else{
			$("input[name='item-checkbox']").prop("checked",false);
		}
	});
	
	//删除选中行
 	$("#delete-list-button").click(function() {
 		$custom.alert.warning("确定删除选中对象？",function(){
	 		var itemId = [];
	        $("input[name='itemId']:checked").each(function() { // 遍历选中的checkbox
	        	itemId.push($(this).val());
	        });
	        var ids = "";
	        for (var i = 0; i < itemId.length; i++) {
	        	if(ids == ""){
	        		ids = itemId[i];
				}else{
					ids = ids + "," + itemId[i];
				}
			}
	        var form = {"ids":ids};
	        $custom.request.ajax('/enum/delEnumItem.asyn?r='+Math.random(),form,function(data){
            	if(data.state==200){
	                $custom.alert.success("删除成功");
	                var id = $("#id").val();
	                setTimeout(function () {
	                    $load.a("/enum/toDetailsPage.html?id="+id);
					}, 1000);
            	}else{
                    $custom.alert.error("删除失败");
                }
            });
 		});
 	});
});
</script>
