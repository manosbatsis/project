<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!-- Start content -->
<jsp:include page="/WEB-INF/views/common.jsp" />
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
                                <li class="breadcrumb-item"><a href="#">调拨管理</a></li>
                                <li class="breadcrumb-item"><a href="javascript:$load.a('/transferOut/toPage.html');">调拨出库列表</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1">
                        <div class="form_box">
                            <div class="form_con">
                               
                                <div class="form_item h35">
                                    <span class="msg_title">调拨出单号 :</span>
                                    <input class="input_msg" type="text" name="code" required="" >
                                    <span class="msg_title">调入仓库 :</span>
                                    <select name="inDepotId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">调出仓库 :</span>
                                    <select name="outDepotId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">调拨单号:</span>
                                    <input type="text" class="input_msg" name="transferOrderCode">
                                    <span class="msg_title">合同号 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" name="contractNo">
                                    <span class="msg_title">单据状态 :</span>
                                    <select name="status" class="input_msg">
                                    </select>
                                </div>
                                <div class="form_item h35">
                                    <span class="msg_title">事业部 :</span>
                                    <select name="buId" class="input_msg">
                                        <option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">调出时间:</span>
                                    <input type="text" class="input_msg form_datetime date-input" name="transferStartDate" id="transferStartDate">
                                    -
                                    <input type="text" class="input_msg form_datetime date-input" name="transferEndDate" id="transferEndDate">

                                </div>
                            </div>
                            <a href="javascript:" class="unfold">展开功能</a>
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="transferOut_exportOutDepot">
                                <input type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportTransOutDepot" value="导出"/>
                            </shiro:hasPermission>

                        </div>
                    </div>
                   
                    <!--  ================================表格 ===============================================   -->
                    <div class="table-responsive">
                        <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th  class="tc">序号</th>
                                <th  class="tc">调拨出单号</th>
                                <th  class="tc">合同号</th>
                                <th  class="tc">调入仓库</th>
                                <th  class="tc">调出仓库</th>
                                <th  class="tc">事业部</th>
                                <th  class="tc">调拨单号</th>
                                <th  class="tc">单据状态</th>
                                <th  class="tc">调出时间</th>
                                <th  class="tc">操作</th>
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
            <!-- container -->
        </div>
    </div>
</div> <!-- content -->
<script type="text/javascript">
    $module.constant.getConstantListByNameURL.call($('select[name="status"]')[0],"transferOutDepot_statusList",null);

    //加载仓库
    $module.depot.getSelectBeanByMerchantRel.call($("select[name='outDepotId']")[0],null, {});
    $module.depot.getSelectBeanByMerchantRel.call($("select[name='inDepotId']")[0],null, {});
    //加载事业部
    $module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null, null,null);
    $(document).ready(function() {
        laydate.render({
            elem: '#transferStartDate', //指定元素
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            ready: function () {
                $('.laydate-btns-time').remove();
            },
        });

        laydate.render({
            elem: '#transferEndDate', //指定元素
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

        //重新加载select2
        $(".goods_select2").select2({
            language:"zh-CN",
            placeholder: '请选择',
            allowClear:true
        });
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:serverAddr+'/transferOut/transferOutDepot.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
                {data:'code'},{data:'contractNo'},{data:'inDepotName'},{data:'outDepotName'},{data:'buName'},{data:'transferOrderCode'},{data:'statusLabel'},{data:'transferDate'},
                { //操作
                    orderable: false,
                    width: "80px",
                    data: null,
                    render: function (data, type, row, meta) {
                        return '<shiro:hasPermission name="transferOut_detail"><a href="javascript:detail('+row.id+')">详情</a></shiro:hasPermission>';
                    }
                },
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            /*if(aData.status == '015'){
                $('td:eq(6)', nRow).html('待出仓');
            }else if(aData.status == '016'){
                $('td:eq(6)', nRow).html('已出仓');
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            }else if(aData.status == '027'){
                $('td:eq(6)', nRow).html('出库中');
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            }*/

        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        //时间初始化
        $('[type="reset"]').click();
    } );



    function searchData(){
        $mytable.fn.reload();
    }

    //详情
    function detail(id){
        //$load.a(pageUrl+"80031&id="+id);
        $module.load.pageOrder("act=80031&id="+id);
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
    // 点击展开功能
    $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });
    //重置
	$("button[type='reset']").click(function(){
		$(".goods_select2").each(function(){
			$(this).val("");
		});
		//重新加载
		 $(".goods_select2").select2({
			language:"zh-CN",
			placeholder: '请选择',
			allowClear:true
		}); 
	});
    $("#exportTransOutDepot").on("click",function(){
         $custom.alert.warning("确定导出？",function(){
        	//检查导出数量
        	 var countUrl = serverAddr+"/transferOut/exportOutDepotCount.asyn"; 
        	 var param = $("#form1").serializeArray();
        	 $custom.request.ajax(countUrl,param,function(result){
        		 if(result.data.code!='00'){
        			  $custom.alert.warningText(result.data.message);
        		 }else{
        			  //导出
        	          var url = serverAddr+"/transferOut/exportOutDepot.asyn?"+$("#form1").serialize();
                     $downLoad.downLoadByUrl(url) ;
        		 }
        	 });
         }); 
        
   });
</script>
