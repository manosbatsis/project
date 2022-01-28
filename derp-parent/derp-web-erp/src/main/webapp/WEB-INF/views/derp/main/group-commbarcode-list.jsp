<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE html>
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
								<li class="breadcrumb-item"><a href="#">品牌管理</a></li>
								<li class="breadcrumb-item"><a href="#">组码管理</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<ul class="nav nav-tabs">
						<shiro:hasPermission name="commbarcode_list">
							<li class="nav-item">
								<a href="#" data-toggle="tab" class="nav-link " onclick="$load.a('/list/menu.asyn?act=2001');">标准条码管理</a>
							</li>
						</shiro:hasPermission>
						<shiro:hasPermission name="groupCommbarcode_list">
							<li class="nav-item">
								<a href="#poList" data-toggle="tab" class="nav-link active" onclick="$load.a('/list/menu.asyn?act=2002');">组码管理</a>
							</li>
						</shiro:hasPermission>
	           		</ul>
	           		<div class="tab-content">
					<form id="form1">
						<div class="form_box">
							<div class="form_con">
								<div class="form_item h35">
                                    <span class="msg_title">标准条码 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" id="commbarcode" name="commbarcode" >
                                    <span class="msg_title">组码 :</span>
                                    <input type="text" parsley-type="text" class="input_msg" id="groupCommbarcode" name="groupCommbarcode" >
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
								</div>
							</div>
						</div>
					</form>
					<div class="row col-md-12 mt20">
	                     <div class="button-list">
							<shiro:hasPermission name="groupCommbarcode_add">
	                        	<input type="button" class="btn btn-find waves-effect waves-light btn-sm" value="新增" onclick="$m12001.init('0');"/>
							</shiro:hasPermission>
	                      </div>
	                </div>
                    
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive" >
                        <table id="datatable-buttons" class="table table-striped dataTable no-footer" cellspacing="0"  width="100%" >
                            <thead>
								<tr>
									<th>组码</th>
									<th>标准条码</th>
									<th>组品名</th>
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
				<!--======================Modal框===========================  -->
                <jsp:include page="/WEB-INF/views/modals/12001.jsp" />
                <!-- end row -->
                
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

	$(document).ready(function() {
		//初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/groupCommbarcode/listGroupCommbarcodes.asyn?r='+Math.random(),
            columns:[
            /* 每一列的数据 */
                {data:'groupCommbarcode'},{data:'commbarcode'},{data:'groupName'},
                { //操作
                	orderable: false,
                	'sWidth': "80px",
                	data: null,
                	render: function (data, type, row, meta) {
                	    return '<shiro:hasPermission name="groupCommbarcode_edit"><a href="javascript:$m12001.init(\'1\',\''+row.id+'\')">编辑</a></shiro:hasPermission> <shiro:hasPermission name="groupCommbarcode_delete"><a href="javascript:$m12001.del(\''+row.id+'\')">删除</a></shiro:hasPermission>';
                	}
                }
            ],
            formid:'#form1'
        };
        
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        };
        
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        
        function searchData() {
        	
			$mytable.fn.reload();
		}
        
	})

</script>