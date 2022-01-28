<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<style>
    .date-input {
        font-size: 13px;
    }
</style>
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
								<li class="breadcrumb-item"><a href="#">账单管理</a></li>
								<li class="breadcrumb-item"><a href="#">NC收支费项及科目关系</a></li>
							</ol>
						</div>
					</div>
					<!--  title   end  -->
					<form id="form1" autocomplete="off">
						<div class="form_box">
							<div class="form_con">
								<div class="form_item h35">
									<span class="msg_title">NC收支费项 :</span>
									<input class="input_msg" name="name" />
									<span class="msg_title">科目编码 :</span>
									<input class="input_msg" name="subCode" />
									<span class="msg_title">科目名称 :</span>
									<input class="input_msg" name="subName" />
									<div class="fr">
										<button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();'>查询</button>
										<button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
									</div>
								</div>
							</div>
						</div>
					</form>
					<div class="row col-12 mt-5">
						<shiro:hasPermission name="receivePaymentSubject_add">
							<button type="button" class="btn btn-find waves-effect waves-light btn-sm btn_default" id="save-buttons" onclick='$m15002.init();'>新增</button>
						</shiro:hasPermission>
					</div>
					<!--  ================================表格 ===============================================   -->
					<div class="mt10 table-responsive">
						<table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>序号</th>
									<th>收支费项编码</th>
									<th>NC收支费项</th>
									<th>科目编码</th>
									<th>科目名称</th>
									<th>创建时间</th>
									<th>创建人</th>
									<th>状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<!--  ================================表格  end ===============================================   -->
				</div>
				<!-- end row -->
			</div>
			<!-- container -->
			<!--======================Modal框===========================  -->
			<jsp:include page="/WEB-INF/views/modals/15002.jsp" />
			<jsp:include page="/WEB-INF/views/modals/15003.jsp" />
		</div>
		</div>
	</div>
	<!-- content -->
<script type="text/javascript">

    $(document).ready(function() {
			//初始化
			$mytable.fn.init();
			//配置表格参数
			$mytable.params = {
				url : serverAddr+'/receivePaymentSubject/listReceivePaymentSubject.asyn?r=' + Math.random(),
				columns : [
                    {  //序号
                        data : null,
                        bSortable : false,
                        className : "",
                        render : function(data, type, row, meta) {
                            // 显示行号
                            var startIndex = meta.settings._iDisplayStart;
                            return startIndex + meta.row + 1;
                        }
                    },
						{data : 'code'},{data : 'name'},{data : 'subCode'},{data : 'subName'},
						{data : 'createDate'},{data : 'createrName'}, {data : 'statusLabel'},
                    { //操作
                        orderable: false,
                        width:120,
                        data: null,
                        render: function (data, type, row, meta) {
                            var caoZuoHtml;
                            if (row.status == '1') {
                                caoZuoHtml = '<shiro:hasPermission name="receivePaymentSubject_toEditStatus"><a   href="javascript:;"   onclick="enableNcPay('+row.id+', 0)" class="red">停用</a>&nbsp;&nbsp;</shiro:hasPermission>';
                            } else {
                                caoZuoHtml = '<shiro:hasPermission name="receivePaymentSubject_toEditStatus"><a   href="javascript:;"   onclick="enableNcPay('+row.id+', 1)" class="red">启用</a>&nbsp;&nbsp;</shiro:hasPermission>';
                            }
                            caoZuoHtml+= '<shiro:hasPermission name="receivePaymentSubject_toEditPage"><a href="javascript:edit('+row.id+');">编辑</a>&nbsp;&nbsp;</shiro:hasPermission>';

                            return caoZuoHtml;
                        }
                    },
				],
				formid : '#form1'
			};
			//每一行都进行 回调
	        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
	        };
			//生成列表信息
			$('#datatable-buttons').mydatatables();
		});
		function searchData() {
			$mytable.fn.reload();
		}

    //启用/停用
    function enableNcPay(id,type){
        var typeLabel = "停用";
        if (type == '1') {
            typeLabel = "启用";
        }
        $custom.alert.warning("是否确认"+typeLabel+"？",function(){
            $custom.request.ajax(serverAddr+"/receivePaymentSubject/enableNcPay.asyn", {"id":id, "type": type}, function(result){
                if(result.state == '200'){
                    $custom.alert.success(typeLabel+"成功！");
                    setTimeout(function () {
                        // $load.a(serverAddr+'/receivePaymentSubject/toPage.html');
                        $mytable.fn.reload();
                    }, 1000);
                }else{
                    $custom.alert.error(result.message);
                }
            });
        });
    }

    //编辑
    function edit(id){
		$m15003.init(id);
    }
	</script>