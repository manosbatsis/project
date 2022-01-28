<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                                <li class="breadcrumb-item"><a href="#">事业部移库单列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">移库仓库 :</span>
                                    <select name="depotId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">移入事业部 :</span>
                                    <select  class="input_msg" name="inBuId">
                                    </select>
                                    <span class="msg_title">移出事业部 :</span>
                                    <select  class="input_msg" name="outBuId">
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">移库单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="code">
                                    <span class="msg_title">来源业务单号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="businessNo">
                                    <span class="msg_title">移库状态 :</span>
                                    <select  class="input_msg" name="status"></select>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">创建时间 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="createStartDate" id="createStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="createEndDate" id="createEndDate">

                                    <span class="msg_title">移库日期 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="moveStartDate" id="moveStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="moveEndDate" id="moveEndDate">
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="buMoveInventory_import">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="importOrder">导入</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="buMoveInventory_export">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportOrder">导出</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="buMoveInventory_audit">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="auditOrder">审核</button>
                            </shiro:hasPermission>
							<shiro:hasPermission name="buMoveInventory_del">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="delOrder">删除</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>
                                </th>
                                <th>移库单号</th>
                                <th>来源业务单号</th>
                                <th>仓库</th>
                                <th>移入事业部</th>
                                <th>移出事业部</th>
                                <th>创建时间</th>
                                <th>移库日期</th>
                                <th>单据来源</th>
                                <th>创建人</th>
                                <th>移库状态</th>
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
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
//加载仓库的下拉数据
$module.depot.loadSelectData.call($('select[name="depotId"]')[0]);
$module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"buMoveInventory_statusList",null);
//加载事业部
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='inBuId']")[0],null,null, null,null);
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='outBuId']")[0],null,null, null,null);

    $(document).ready(function() {
    	$('[type="reset"]').click();
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/buMoveInventory/listBuMoveInventory.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data:'code'},{data:'businessNo'},{data:'depotName'},{data:'inBuName'},{data:'outBuName'},{data:'createDate'},{data:'moveDate'},{data:'orderSourceLabel'},{data:'createName'},{data:'statusLabel'},
                { //操作
                    orderable: false,
                    width:70,
                    data: null,
                    render: function (data, type, row, meta) {
                    	var details='<shiro:hasPermission name="buMoveInventory_detail"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
                        return details;
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
    } );

    function searchData(){
        $mytable.fn.reload();
    }
    //导入
   	$("#importOrder").click(function(){
   		$module.load.pageOrder("act=13003");
   	});
   	//删除
   	$("#delOrder").click(function(){
   	 var ids=$mytable.fn.getCheckedRow();
     if(ids==null||ids==''||ids==undefined){
         $custom.alert.warningText("至少选择一条记录！");
         return ;
     }
   		$custom.alert.warning("对勾选的移库单确认删除？",function(){
	   		$custom.request.ajax(serverAddr+"/buMoveInventory/delBuMoveInventory.asyn", {"ids" : ids}, function(data){
	   			if(data.state==200){
	   				$custom.alert.success("删除成功");
	   				//重新加载页面
                    $mytable.fn.reload();
	   			}else{
	   				if(!!data.expMessage){
	            		$custom.alert.error(data.expMessage);
	            	}else{
	            		$custom.alert.error(data.message);
	            	}
	   			}
	   		});
   		});
   	});
	//审核
	$("#auditOrder").on("click",function(){
		var url = serverAddr+"/buMoveInventory/auditBuMoveInventory.asyn";
		//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids==null||ids==''||ids==undefined){
			$custom.alert.warningText("至少选择一条记录！");
			return ;
		}
		$custom.alert.warning("对勾选的移库单确认审核？",function(){
            //执行审核
            $custom.request.ajax(url,{"ids":ids},function(data){
                if(data.state==200){
                    $custom.alert.success("审核成功");
                    //重新加载页面
                    $mytable.fn.reload();
                }else{
                    if(!!data.expMessage){
                        $custom.alert.error(data.expMessage);
                    }else{
                        $custom.alert.error(data.message);
                    }
                }
            });
		});
	});
    //详情
    function details(id){
        $module.load.pageOrder("act=13002&id=" + id);
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
  	
		// 时间插件
	    laydate.render({
	        elem: '#deliverStartDate', //指定元素
	        type: 'datetime',
	        format: 'yyyy-MM-dd HH:mm:ss',
	        ready: function () {
	            $('.laydate-btns-time').remove();
	        }
	    });
	    laydate.render({
	        elem: '#deliverEndDate', //指定元素
	        type: 'datetime',
	        format: 'yyyy-MM-dd HH:mm:ss',
	        ready: function () {
	            $('.laydate-btns-time').remove();
	        },
	        done: function(value){
	            this.dateTime.hours = 23;
	            this.dateTime.minutes = 59;
	            this.dateTime.seconds = 59;
	        }
	    });
    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
    
    //导出
    $("#exportOrder").on("click",function(){
    	var jsonData=null;
    	//获取选中的id信息
		var ids=$mytable.fn.getCheckedRow();
		if(ids){
			jsonData=$json.setJson(jsonData,"ids",ids);
		}else{
			//根据筛选条件导出
	    	var jsonArray=$("#form1").serializeArray();
	        $.each(jsonArray, function(i, field){
	            if(field.value!=null&&field.value!=''&&field.value!=undefined){
	                jsonData=$json.setJson(jsonData,field.name,field.value);
	            }
	        });
		}
        var form = JSON.parse(jsonData);
        
        $custom.request.ajax(serverAddr+"/buMoveInventory/getOrderCount.asyn", form, function(data){//判断导出的数量是否超出1W
        	if(data.state==200){
        		var total = data.data.total;//总数
        		if(total>10000){
        			$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
        			return;
        		}
        		//导出数据
            	var url = serverAddr+"/buMoveInventory/exportBuMoveInventory.asyn";
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
        	}else{
            	if(!!data.expMessage){
            		$custom.alert.error(data.expMessage);

            	}else{
            		$custom.alert.error(data.message);

            	}
            }
        });
    });
laydate.render({
    elem: '#createStartDate', //指定元素
    type: 'datetime',
    format: 'yyyy-MM-dd HH:mm:ss',
    ready: function () {
        $('.laydate-btns-time').remove();
    }
});
laydate.render({
    elem: '#createEndDate', //指定元素
    type: 'datetime',
    format: 'yyyy-MM-dd HH:mm:ss',
    ready: function () {
        $('.laydate-btns-time').remove();
    },
    done: function(value){
        this.dateTime.hours = 23;
        this.dateTime.minutes = 59;
        this.dateTime.seconds = 59;
    }
});
laydate.render({
    elem: '#moveStartDate', //指定元素
    type: 'datetime',
    format: 'yyyy-MM-dd HH:mm:ss',
    ready: function () {
        $('.laydate-btns-time').remove();
    }
});
laydate.render({
    elem: '#moveEndDate', //指定元素
    type: 'datetime',
    format: 'yyyy-MM-dd HH:mm:ss',
    ready: function () {
        $('.laydate-btns-time').remove();
    },
    done: function(value){
        this.dateTime.hours = 23;
        this.dateTime.minutes = 59;
        this.dateTime.seconds = 59;
    }
});
</script>
