<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- Custom Modals -->
<div>
    <!-- Signup modal content -->
    <div id="invoice-modal" class="modal fade bs-example-modal-lg" >
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
            	<div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                     <h5 class="modal-title" id="myLargeModalLabel">组合品详情</h5>
                </div>
                <div class="modal-body">
                      <div class="form-row mt20">
                   			<table id="datatable-buttons2" class="table table-bordered" cellspacing="0" width="100%">
			                        <thead>
			                      <tr>
			                            <th>商品条码</th>
			                            <th>商品货号</th>
			                            <th>商品名称</th>
			                            <th>商品编码</th>
			                            <th>单品数量</th>
			                            <th>商品价格</th>
			                        </tr>
			                        </thead> 
			                        <thead id="groupListDiv">
			                         
			                        </thead>
			                       <!-- <tr id="groupListDiv"></tr> -->
			                    </table>
                      </div>
                      <div class="form-row mt20">
                      	<div class="form-inline m0auto">
                          		<div class=form-group>
                          			<button type="button" class="btn btn-info waves-effect waves-light fr" id="cancel-buttons" onclick="returnBtn()">返回</button>
                          		</div>
                          	</div>
                      </div>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>
<script type="text/javascript">
		
		function returnBtn(){
				$("#invoice-modal").modal("hide");
		}
		  
</script>