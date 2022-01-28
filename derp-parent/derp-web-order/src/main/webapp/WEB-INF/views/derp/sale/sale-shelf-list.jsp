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
                                <li class="breadcrumb-item"><a href="#">上架列表</a></li>
                            </ol>
                        </div>
                    </div>

                    <ul class="nav nav-tabs">
                        <shiro:hasPermission name="shelf_list">
                            <li class="nav-item">
                                <a href="#" data-toggle="tab" class="nav-link active" onclick="$module.load.pageOrder('act=40041');">上架单</a>
                            </li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="shelf_idepotList">
                            <li class="nav-item">
                                <a href="#" data-toggle="tab" class="nav-link" onclick="$module.load.pageOrder('act=40042');">上架入库单</a>
                            </li>
                        </shiro:hasPermission>
                    </ul>

                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">

                                    <span class="msg_title">po号 :</span>
                                    <input class="input_msg" name="poNo"/>

                                    <span class="msg_title">客户 :</span>
                                    <input class="input_msg" name="customerName"/>

                                    <span class="msg_title">上架日期 :</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="shelfStartDate" id="shelfStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="shelfEndDate" id="shelfEndDate">
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">

                                    <span class="msg_title">出仓仓库 :</span>
                                    <select name="outDepotId" class="input_msg">
                                    </select>

                                    <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId">
                                    </select>

                                    <span class="msg_title">单据状态 :</span>
                                    <select  class="input_msg" name="state">
                                    </select>

                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">销售订单号 :</span>
                                    <input class="input_msg" name="saleOrderCode"/>
                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="saleShelfIdepot_export">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportOrder">导出</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="row mt10 table-responsive">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>上架单号</th>
                                <th>PO号</th>
                                <th>销售订单号</th>
                                <th>客户</th>
                                <th>事业部</th>
                                <th>上架总量</th>
                                <th>上架人</th>
                                <th>上架日期</th>
                                <th>创建日期</th>
                                <th>单据状态</th>
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
                <!-- end row -->
            </div>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
$module.constant.getConstantListByNameURL.call($('select[name="state"]')[0],"shelf_statusList",null);
//加载事业部
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null,null);
function showHideCode(obj){
    $(obj).prev().toggle();
		//被隐藏了
	if($(obj).prev().is(":hidden")){
		$(obj).prev().prev().show();
		}else{
			$(obj).prev().prev().hide();
		} 
   }

 
    $(document).ready(function() {
    	$('[type="reset"]').click();
    	$module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0], "", {"type":"a,c,d"});
    	// $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0], "", {"type":"b"});

        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/shelf/listShelf.asyn?r='+Math.random(),
            columns:[
                {data:'code'},{data:'poNo'},{data:'saleOrderCode'},{data:'customerName'},{data:'buName'},{data:'totalShelfNum'},{data:'operator'},{data:'shelfDate'},{data:'createDate'},{data:'stateLabel'},
                { //操作
                    orderable: false,
                    width:70,
                    data: null,
                    render: function (data, type, row, meta) {
                    	return '<shiro:hasPermission name="saleShelfIdepot_detail"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission>';
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

    //详情
    function details(id){
        $module.load.pageOrder("act=40102&id=" + id);
    }
  	
	// 时间插件
    laydate.render({
        elem: '#shelfStartDate', //指定元素
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss',
        ready: function () {
            $('.laydate-btns-time').remove();
        }
    });
    laydate.render({
        elem: '#shelfEndDate', //指定元素
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
    	//根据筛选条件导出
    	var jsonData=null;
    	var jsonArray=$("#form1").serializeArray();
        $.each(jsonArray, function(i, field){
            if(field.value!=null&&field.value!=''&&field.value!=undefined){
                jsonData=$json.setJson(jsonData,field.name,field.value);
            }
        });
        var form = JSON.parse(jsonData);

        $custom.request.ajax(serverAddr+"/shelf/getOrderCount.asyn", form, function(data){//判断导出的数量是否超出1W
        	if(data.state==200){
        		var total = data.data.total;//总数
        		if(total>10000){
        			$custom.alert.error("导出的数量过大，请填写筛选条件再导出");
        			return;
        		}
        		//导出数据
            	var url = serverAddr+"/shelf/exportShelf.asyn";

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

    function dh_list(){
        $module.load.pageOrder("act=4010");
    }

</script>
