<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />
<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
          <div class="row">
            <div class="col-12">
                <div class="card-box">
                        <div class="col-12 row">
                            <div>
                                <ol class="breadcrumb">
                                    <li><i class="block"></i> 当前位置：</li>
                                    <li class="breadcrumb-item"><a href="javascript:dh_list()">欧莱雅管理/欧莱雅采购订单</a></li>
                                </ol>
                           </div>
                        </div>
                        <!-- 筛选条件开始 -->
                        <form id="form1">
                        <div class="form_box">
                            <div>
                               
                                <div class="form_item h35">

                                   <span class="msg_title">订单编号 :</span>
                                   <input type="text" required="" parsley-type="text" class="input_msg" id="vordercode" name="vordercode">
                                   <span class="msg_title">订单日期 :</span>
                                    <input type="text"  name="dorderdateStartDate" id="dorderdateStartDate" class="laydateStart">
                                    -
                                    <input type="text"   name="dorderdateEndDate" id="dorderdateEndDate" class="laydateEnd"> 
                                  
                                  
                                  
                                  <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                
                                <div class="form_item h35">
                                    <span class="msg_title">事业部 :</span>
                                    <select class="input_msg" name="buId" id="buId"></select>
                                    <span class="msg_title">CSR确认单号:</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="vdef7" name="vdef7">
                                    

                                </div>

                                <!--  <a href="javascript:;" class="unfold">展开功能</a> -->
                            </div>
                        </div>
                    </form>
                    <!-- 筛选条件结束 -->
                        <div class="row col-md-12 mt20">
                            <div class="button-list">
                                <shiro:hasPermission name="orealPurchaseOrder_export">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export">导出</button>
                                </shiro:hasPermission>
                            </div>
                        </div>
                        <div class="mt20">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                <tr>
		                            
                                    <th style="white-space:nowrap;">序号</th>
                                    <th style="white-space:nowrap;">单号</th>
                                    <th style="white-space:nowrap;">CSR号</th>
                                    <th style="white-space:nowrap;">订单日期</th>
                                    <th style="white-space:nowrap;">品牌</th>
                                    <th style="white-space:nowrap;">供应商</th>
                                    <th style="white-space:nowrap;">类型</th>
                                    <th style="white-space:nowrap;">收货地址</th>
                                    <th style="white-space:nowrap;">公司</th>
                                    <th style="white-space:nowrap;">事业部</th>
                                    <th style="white-space:nowrap;">数据来源</th>
                                    <th style="white-space:nowrap;">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!--======================Modal框===========================  -->
                    <!-- end row -->
			</div>
		</div>
	</div>
</div>
                <!-- container -->
<script type="text/javascript">
$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],null,null,null,null);

//导出
$("#export").on("click",function(){
    var param = $("#form1").serialize();
    $custom.alert.warning("确定导出？",function(){
        var url = serverAddr+"/orealPurchaseOrder/export.asyn?"+param;
        $downLoad.downLoadByUrl(url);
    });
});

$(".laydateStart").each(function(){
	   laydate.render({
        elem: this, //指定元素
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss',
        ready: function () {
            $('.laydate-btns-time').remove();
        }
    });
});
$(".laydateEnd").each(function(){
	 laydate.render({
      elem: this, //指定元素
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
});

//加载状态
$(document).ready(function() {
    //初始化
    $mytable.fn.init();
    //配置表格参数
    $mytable.params={
        url : serverAddr+'/orealPurchaseOrder/listByPage.asyn?r=' + Math.random(),
        columns:[{ //复选框单元格
            className: "td-checkbox",
            orderable: false,
            width: "30px",
            data: null,
            render: function (data, type, row, meta) {
                 return '<input type="checkbox" class="iCheck">';
            }
        },
        {data : 'vordercode'},{data : 'vdef7'},{data : 'dorderdate'},{data : 'vdef1'},{data : 'custname'},
        {data : 'vdef13'},{data : 'adress'},{data : 'merchantName'},{data : 'buName'},{data : 'sourceLabel'},
            { //操作
                orderable: false,
                width: "130px",
                data: null,
                render: function (data, type, row, meta) {   
                	var edit = "";
                	edit='<shiro:hasPermission name="orealPurchaseOrder_details"><a href="javascript:details('+row.id+')">详情</a></shiro:hasPermission> &nbsp';
                	
                    return edit;
                }
            },
        ],
        formid:'#form1'
    }
    //每一行都进行 回调
    $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
    };
        
    //生成列表信息
    $('#datatable-buttons').mydatatables();
   
});

function searchData(){
    $mytable.fn.reload();
}

function details(id) {
    $module.load.pageOrder("act=22002&id=" + id);
} 







// 点击展开功能
$(".unfold").click(function () {
    $(".form_con").toggleClass('hauto');
    if($(this).text() == '展开功能'){
        $(this).text('收起功能');
    }else{
        $(this).text('展开功能');
    }
})

</script>
