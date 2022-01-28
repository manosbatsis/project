<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/WEB-INF/views/common.jsp" />

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
                                <li class="breadcrumb-item"><a href="#">库存管理</a></li>
                                <li class="breadcrumb-item "><a href="javascript:list();">事业部仓库库存</a></li>
                            </ol>
                        </div>
                    </div>
                    <!--  title   end  -->
                    <form id="form1" >
                        <div class="form_box">
                            <div class="">
                                <div class="form_item h35">
                                    <span class="msg_title">查询月份:</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg form_datetime date-input" id ="month" name="month">
                                    <span class="msg_title">仓库 :</span>
                                    <select name="depotId" class="input_msg" id="depotId">
                                        <option value="">请选择</option>

                                    </select>
                                    <span class="msg_title">事业部:</span>
                                    <select name="buId" class="input_msg" id="buId">
                                        <option value="">请选择</option>
                                    </select>
                                     <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='$mytable.fn.reload();' >查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                                <div class="form_item h35">
                                <span class="msg_title">品牌:</span>
									<select class="input_msg" name="brandId" id="brandId">
                                     	<option value="">请选择</option>
                                    </select>
                                    <span class="msg_title">商品货号 :</span>
                                    <input type="text" required="" parsley-type="text"  id="goodsNo" class="input_msg" name="goodsNo">
									 <span class="msg_title">条码:</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg" id="barcode" name="barcode">                                   
                                </div>
                                <div class="form_item h35">
								<span class="msg_title">商品名称 :</span>
                                    <input type="text" required="" parsley-type="text"  id="goodsName" class="input_msg" name="goodsName">								
								</div>

                            </div>
                            <!-- <a href="javascript:" class="unfold">展开功能</a>  -->
                        </div>
                    </form>
                    <div class="row col-12 mt20">
                        <div class="button-list">
                            <shiro:hasPermission name="buInventory_exportBuInventory">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="exportBuInventory" >导出</button>                                
                            </shiro:hasPermission>
                            <shiro:hasPermission name="buInventory_flashBuInventory">
                                <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="flashBuInventory" >刷新</button>                                
                            </shiro:hasPermission>
							<label id="labelTime" style="color: red;margin-left: 20px;"></label>
                        </div>
                    </div>
                    <!--  ================================表格 ===============================================   -->
                    <div class="table-responsive">
                        <table id="datatable-buttons" class="table table-striped twoRows" cellspacing="0" width="100%">
                            <thead>
                            <tr>
                                <th>
                                    <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
                                        <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
                                        <span></span>
                                    </label>
                                </th>
                                <!--                             <th>序号</th> -->                               
                                <th>公司</th>
								<th>事业部</th>
                                <th>仓库名称</th>
                                <th>品牌</th>
                                <th>商品货号</th>
                                <th>商品名称</th>
                                <th>商品条形码</th>
								<th>库存数量</th>
                                <th>好品数量</th>
                                <th>坏品数量</th>                                
                                <th>理货单位</th>
                                <th>归属月份</th>
                                <th>刷新日期</th> 
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
            <!-- container -->
        </div>
    </div>
</div> <!-- content -->
<script type="text/javascript">
    $(document).ready(function() {
        var buId = '${buId}';
    	$module.businessUnit.getSelectBeanByMerchantDepotRel.call($("select[name='buId']")[0],buId, {});
    	$module.brand.loadSelectData.call($("select[name='brandId']")[0]);
    	 $(".date-input").datetimepicker({
             format: 'yyyy-mm',
             autoclose: true,
             todayBtn: true,
             startView: 'year',
             minView:'year',
             maxView:'decade',
             language:  'zh-CN',
         });
    	
    	//重置按钮
        $("button[type='reset']").click(function(){
            $(".goods_select2").each(function(){
                $(this).val("");
            });
            //重新加载select2
            $(".goods_select2").select2({
                language:"zh-CN",
                placeholder: '请选择',
                allowClear:true
            });
        });
        var depotId= '${depotId}';
        //加载仓库下拉
        $module.depot.loadSelectData.call($("select[name='depotId']")[0],"");
       $("#depotId").val(depotId);
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url: serverAddr+'/buInventory/listBuInventory.asyn?r='+Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" value='+data.id+' name="iCheck" class="iCheck">';
                }
            },
                {data:'merchantName'},{data:'buName'},{data:'depotName'},{data:'brandName'},{data:'goodsNo'},{data:'goodsName'},{data:'barcode'},
                {data:'surplusNum'},{data:'okNum'},{data:'wornNum'},
                {data:'unitLabel'},{data:'month'},{data:'createDate'},
                { //操作
                    orderable: false,
                    width: "130px",
                    data: null,
                    render: function (data, type, row, meta) {
                    	var id=row.id;
                    	/* var edit = '<a href="javascript:develop('+row.id+')">展开功能</a>';  */
                    	var edit = ''; 
                        return edit;
                    }
                },  
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
        	$("#labelTime").text("刷新日期："+aData.createDate);
            if(aData.goodsName != null && aData.goodsName.length>25){
                $('td:eq(6)', nRow).html(aData.goodsName.substring(0, 25)+"...");
                $('td:eq(6)', nRow).attr("title",aData.goodsName);
            }
            $('td:eq(14)', nRow).html("<p class='nowrap'><a href='javascript:'></a></p>"
            		+"<p class='nowrap'><a href='javascript:void(0);'>展开</a></p><p><input type='hidden' value='"+aData.id+"'/></p>"); 
        }
        //生成列表信息
        $('#datatable-buttons').mydatatables();

        $("#datatable-buttons_wrapper").removeClass('container-fluid');
    } );
    
    
    // 点击展开功能显示某个电商订单的商品
    $('#datatable-buttons tbody').on( 'click', 'tr td:nth-child(15) p:nth-child(2)', function () {
    	debugger;
    	var thisObj=$(this);
           if($(this).text() == '展开'){
              $(this).text('收起');
              $(this).css({"color":"#007bff","cursor":"pointer"});
              var id=$(this).next().children().val();
              var editItem="<tr id='goodsList'><td></td><td colspan='8'><table  width='100%'> <tr >"+
              					 "<th class='nowrap'  width='10%'>批次号</th>"+
              					 "<th class='nowrap'  width='10%'>生产日期</th>"+
              					 "<th class='nowrap' width='10%'>失效日期</th>"+
              					 "<th class='nowrap' width='20%'>效期区间</th>"+
              					 "<th class='nowrap' width='5%'>库存类型</th>"+
              					 "<th class='nowrap' width='10%'>批次库存数量</th>"+
              					"<th class='nowrap' width='15%'>首次上架日期</th>"+
              					 "</tr>";   
              $custom.request.ajax(serverAddr+"/buInventory/getBuInventoryItem.asyn", {"id" : id}, function(data){
            	  debugger;
      			 for (var j= 0; j < data.data.length; j++) {
      			  	var result = data.data[j];
      				editItem+="<tr>"+
      				"<td class='nowrap'>"+(result.batchNo==null?'':result.batchNo)+"</td>"+
      				"<td class='nowrap'>"+(result.productionDate==null?'':result.productionDate)+"</td>"+
      				"<td class='nowrap'>"+(result.overdueDate==null?'':result.overdueDate)+"</td>"+
      				"<td class='nowrap'>"+(result.effectiveIntervalLabel==null?'':result.effectiveIntervalLabel)+"</td>"+
      				"<td class='nowrap'>"+(result.typeLabel==null?'':result.typeLabel)+"</td>"+
      				"<td class='nowrap'>"+(result.num==null?'':result.num)+"</td>"+
      				"<td class='nowrap'>"+(result.firstShelfDate==null?'':result.firstShelfDate)+"</td>"+
      				"</tr>";
      				
      			}
      			"</table></td></tr>";; 
      		  	thisObj.parent("td").parent("tr").after(editItem);
         		}); 
          }else if($(this).text() == '收起'){
              $(this).text('展开');
              $(this).css({"color":"#007bff","cursor":"pointer"});
      		  thisObj.parent("td").parent("tr").next().html("");
          }
    	});
    
    //详情
    function develop(id){
		debugger;
		$(this).after('<tr><td>12131414</td></tr>');
    	$(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    }

    function list(){
        $module.load.pageInventory("bls=6019");
    }

    function searchData(){
        $mytable.fn.reload();
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
    /* $(".unfold").click(function () {
        $(".form_con").toggleClass('hauto');
        if($(this).text() == '展开功能'){
            $(this).text('收起功能');
        }else{
            $(this).text('展开功能');
        }
    });  */

     //导出
    $("#exportBuInventory").on("click",function(){
    	var month=  $("#month").val();
    	if (!month) {
    		$custom.alert.error("请选择月份！");
    		return false;
		}
        var depotId=  $("#depotId").val();
        var buId=$("#buId").val();
        var brandId=$("#brandId").val();
        var barcode=$("#barcode").val();
        var goodsNo=$("#goodsNo").val();
        var goodsName=$("#goodsName").val();
        
        $custom.alert.warning("确定导出筛选的数据？",function(){
           var url=serverAddr+"/buInventory/exportBuInventory.asyn?depotId="+depotId+"&month="+month+"&buId="+buId+"&brandId="+brandId
        		   +"&barcode="+barcode+"&goodsNo="+goodsNo+"&goodsName="+goodsName;
           $downLoad.downLoadByUrl(url);
        });
    });
     
    //刷新
    $("#flashBuInventory").on("click",function(){
        var month=  $("#month").val();
        if (!month) {
        	$custom.alert.error("请选择月份！");
    		return false;
		}
        var depotId=  $("#depotId").val();
        var buId=$("#buId").val();
        var massage="确定刷新筛选的数据?";
        if(depotId==""||depotId==buId){
        	if(depotId==""){
        		massage="仓库未选择,确定刷新所有仓的数据?"
        	}
        	if(buId==""){
        		massage="事业部未选择,确定刷新所有仓的数据?"
        	}       	
        	if(depotId=="" && buId==""){
        		massage="仓库和事业部都没有选择,确定刷新所有仓的数据?"
        	}
        	
        	
       	}
        $custom.alert.warning(massage,function(){           
           $custom.request.ajax(serverAddr+"/buInventory/flashBuInventory.asyn", {"month":month,"depotId":depotId,"buId":buId}, function(result){
        	   if(result.state == '200'){
					//1成功 0失败
					if (result.data.status=='1') {
						$custom.alert.success(result.data.massage);
						setTimeout(function () {						
						}, 1000);
					}else{
						$("#save-buttons").attr("disabled", false);
						$custom.alert.error	(result.data.massage);
					}
					
				}else{
					$("#save-buttons").attr("disabled", false);
					$custom.alert.error("刷新失败！");
				}
			});
           
           

        });
    });
     
    $(function () {
        $("#datatable-buttons_wrapper").removeClass('container-fluid');
    })
    
</script>
