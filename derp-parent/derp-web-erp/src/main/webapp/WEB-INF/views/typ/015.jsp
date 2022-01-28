<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- App favicon -->
<link rel="shortcut icon" href='<c:url value="/resources/assets/images/favicon.ico"/>'>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
          <form id="add-form" action="/merchandise/saveMerchandise.asyn">
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
                        <li class="breadcrumb-item"><a href="#">采购订单详情</a></li>
                    </ol>
                    </div>
            </div>
                    <!--  title   end  -->
                 <div class="ml10 page-title col-12 borderb">
                    <div class="ml10">采购订单明细</div>
                </div>
                <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">采购订单编号 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>GOODSNO201804085</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">PO号<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>4713575086398A</div>
                            </div>
                        </div>
                    </div>
                </div>  
                 <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">入库期限 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>GOODSNO201804085</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">供应商<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>4713575086398A</div>
                            </div>
                        </div>
                    </div>
                </div> 
                 <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">仓库名称 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>GOODSNO201804085</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">业务场景<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>4713575086398A</div>
                            </div>
                        </div>
                    </div>
                </div>   
               <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">创建人 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>GOODSNO201804085</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">创建时间<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>4713575086398A</div>
                            </div>
                        </div>
                    </div>
                </div> 
                 <div class="form-row mt20 ml15">
                    <div class="col-5">
                        <div class="row">
                            <label class="col-4 col-form-label " style="white-space:nowrap;"><span class="fr">审核人人 <span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>GOODSNO201804085</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-5">
                        <div class="row">
                            <label  class="col-5 col-form-label" style="white-space:nowrap;"><span class="fr">审核时间<span>：</span></span></label>
                            <div class="col-7 ml10 line">
                                <div>4713575086398A</div>
                            </div>
                        </div>
                    </div>
                </div>  
                <div class="ml10 page-title col-12 borderb mt20">
                    <div class="ml10">采购计划商品明细</div>
                </div>  
                 <!--  ================================表格 ===============================================   -->
                    <div class="row col-12 mt10">
                    <table id="datatable-buttons" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th style="white-space:nowrap;" class="tc">序号</th>
                            <th style="white-space:nowrap;" class="tc">子PO编号</th>
                            <th style="white-space:nowrap;" class="tc">商品编码</th>
                            <th style="white-space:nowrap;" class="tc">商品货号</th>
                            <th style="white-space:nowrap;" class="tc">商品名称</th>
                            <th style="white-space:nowrap;" class="tc">计量单位</th>
                            <th style="white-space:nowrap;" class="tc">采购数量</th>
                            <th style="white-space:nowrap;" class="tc">采购单价</th>
                            <th style="white-space:nowrap;" class="tc">采购总金额</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                    <!--  ================================表格  end ===============================================   -->               
             </div>
                  <!-- end row -->
          </form>
        </div>
        <!-- container -->
    </div>

</div> <!-- content -->
<script type="text/javascript">
	$(function(){
	
		
	});
	
	//table
   $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url:'/customer/listCustomer.asyn?r='+Math.random(),
            columns:[
                     /* { //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            }, */
            /* {  //序号
                data : null,  
                bSortable : false,  
                className : "text-right",  
                width : "30px",  
                render : function(data, type, row, meta) {  
                    // 显示行号  
                    var startIndex = meta.settings._iDisplayStart;  
                    return startIndex + meta.row + 1;  
                }  
            }, */
            {data:'code'},{data:'name'},{data:'orgCode'},{data:'cusType'},{data:'legalTel'},{data:'orgCode'},{data:'cusType'},{data:'legalTel'},{data:'legalTel'},
           /*  { //操作
                orderable: false,
                width: "130px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<a href="javascript:edit('+row.id+')">编辑</a>';
                }
            }, */
            ],
            formid:'#form1'
        };
        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            if (aData.cusType == '1'){
                $('td:eq(4)', nRow).html('客户');
            }else if (aData.cusType == '2'){
                $('td:eq(4)', nRow).html('供应商');
            }
        };
        //生成列表信息
        $('#datatable-buttons').mydatatables();
        });
        

	 
	 
	 
	 
</script>
