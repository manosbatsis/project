<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
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
                                <li class="breadcrumb-item"><a href="#">模版管理</a></li>
                                <li class="breadcrumb-item "><a href="#">清关单证配置</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  过滤条件查询  start  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div >
                                <div class="form_item h35">
                                 	<!-- <span class="msg_title">模板编码 ：</span>
                                    <input type="text" class="input_msg" name="fileTempCode"> -->
                                    <span class="msg_title">模版名称 :</span>
                                    <input type="text" class="input_msg" name="fileTempName">
                                    <span class="msg_title">进出仓配置 ：</span> 
                                    <select id="status" name="depotConfig" class="input_msg"></select>  
                                    <span class="msg_title">适用仓库 :</span>
                                    <input name="depotIds" id="depotIds" type="hidden">
                                    <select id="depotSelect" class="selectpicker show-tick form-control"  multiple data-live-search="true"></select>                                                                      
                                    <div class="fr">
                                        <button type="button" class="btn btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light reset btn_default">重置</button>
                                    </div>
                                </div>
	                            <div class="form_item h35">
	                             		<span class="msg_title">是否同关区 ：</span> 
	                                    <select id="isSameArea" name="isSameArea" class="input_msg"></select>
			                            <span class="msg_title">状态 :</span>
	                                    <select id="status" name="status" class="input_msg"></select>
	                                    
	                            </div>
	                            
                            </div>
                        </div>
                    </form>
                    <!-- 过滤条件查询  end  -->

                    <div class="row col-md-12 mt20">
                        <shiro:hasPermission name="customsFileConfig_add">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="add-buttons" onclick="$m15004.init();">新建配置</button>
                        </shiro:hasPermission>
                         <shiro:hasPermission name="customsFileConfig_delete">
                            <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="delete-buttons" onclick="del();">删除</button>
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
                                <th class="tc">模版编码</th>
                                <th class="tc">模版名称</th>
                                <th class="tc">进出仓配置</th>
                                <th class="tc">是否同关区</th>
                                <th class="tc">适用仓库</th>
                                <th class="tc">创建时间</th>
                                <th class="tc">状态</th>
                                <th class="tc">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="/WEB-INF/views/modals/15004.jsp" />
        <!-- End row -->
        <!-- end row -->
    </div> <!-- container -->
</div> <!-- content -->



<script type="text/javascript">
	//	状态
    $module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"temp_statusList",null);
	//	进出仓配置
	$module.constant.getConstantListByNameURL.call($('select[name="depotConfig"]')[0],"customsFileConfig_DepotConfigList",null);
	//	是否同关区
	$module.constant.getConstantListByNameURL.call($('select[name="isSameArea"]')[0],"isSameAreaList",null);
    //仓库集合
	getSearchDepotSelect() ;

    $(document).ready(function() {

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr + '/customsFileConfig/listCustomsFileConfig.asyn?r='+Math.random(),
            columns:[
            	{ //复选框单元格
					className : "td-checkbox",
					orderable : false,
					data : null,
					render : function(data, type, row, meta) {
						return '<input type="checkbox" class="iCheck">';
					}
				},
                {data:'fileTempCode'},
                {data:'fileTempName'},
                {data:'depotConfigLabel'},
                {data:'isSameAreaLabel'},
                {data:'depotNames'},
                {data:'createDate'},
                {data:'statusLabel'},
                { //操作
                    orderable: false,
                    width: "100px",
                    data: null,
                    render: function (data, type, row, meta) {                    	
                    	var html = "" ;
                    	html += '<shiro:hasPermission name="customsFileConfig_edit"><a href="javascript:$m15004.init('+row.id+',\''+ row.status + '\',\''+row.depotConfig+'\',\''+row.fileTempId+'\',\''+row.fileTempCode+'\',\''+row.fileTempName+'\',\''+row.isSameArea+'\')">适用配置&nbsp;&nbsp;</a></shiro:hasPermission>' ;
                    	
                    	return html ;
                    	
                    }
                },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        	// 返回日期格式化校验
			var createDate=new Date(aData.createDate);
			var year = createDate.getFullYear();
			var month = createDate.getMonth()+ 1, month = month < 10 ? '0' + month : month;
			var day = createDate.getDate(), day = day < 10 ? '0' + day : day;
			$('td:eq(6)', nRow).html(year + '-' + month + '-' + day);
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();

        $('#depotSelect').selectpicker('val',['']);
    });

    function searchData(){
        var depotIds = [] ;
        $($("#depotSelect").find("option:selected").selectpicker('val')).each(function(i , m){
            var val = $(m).val() ;
            if (val != '') {
            	depotIds.push(val);
            }
        });
        $("#depotIds").val(depotIds.join(","));
        $mytable.fn.reload();
    }
    
    /**
     *	下拉搜索
     */
    $("#depotSelect").prev().find(".bs-searchbox").find("input").keyup(
        function(){
            var name = $("#form1 .open input").val() ;
            getSearchDepotSelect(name) ;
        }
    );

    /**
     * 获取适用仓库下拉
     */
    function getSearchDepotSelect(name){
        $custom.request.ajax($module.params.getAllDepotURL,{"name":name},function(data) {
            if (data.state == 200) {
                var html = "" ;
                var depotList = data.data ;
                $(depotList).each(function(index , depot){
                    html += '<option value="'+depot.value+'">' +depot.label+'</option>' ;
                }) ;
                $("#depotSelect").append(html) ;
                $('#depotSelect').selectpicker({width: '136px'});
                $('#depotSelect').selectpicker({noneSelectedText : '请选择'});
                $('#depotSelect').selectpicker('refresh');

                $("#depotSelect").prev().prev().css({"height": "30px","margin-bottom": "5px","border": "1px solid #dadada","background": "white","font-size": "12px"}) ;
                $("#depotSelect").prev().css({"z-index":"99"}) ;
                $("#depotSelect").prev().find(".dropdown-menu.inner").css({"height":"220px"}) ;

            } else {
                $custom.alert.error(data.message);
            }
        });
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
    //删除
    function del(){
    	$custom.request.delChecked(serverAddr+'/customsFileConfig/delCustomsFileConfig.asyn');
    }
</script>