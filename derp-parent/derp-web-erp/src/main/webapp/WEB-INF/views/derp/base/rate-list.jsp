<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

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
                                    <li class="breadcrumb-item"><a href="javascript:$load.a('/rate/toPage.html');">汇率管理</a></li>
                                </ol>
                           </div>
                        </div>
                        <!-- 筛选条件开始 -->
                        <form id="form1">
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">兑换币种：</span>
                                    	<select class="input_msg"  name="origCurrencyCode" id="origCurrencyCode">
			                              </select>
                                    <span>兑</span>
                                    	<select class="input_msg"  name="tgtCurrencyCode" id="tgtCurrencyCode"  >
			                            </select>
                                    <span class="msg_title">汇率日期 :</span>
                                    <input type="text" required="" parsley-type="text" class="input_msg form_datetime date-input" name="effectiveDateStr" id="effectiveDateStr">
<!--                                     <input type="text" required="" parsley-type="text" class="input_msg" name="effectiveDate"> -->
                                    <span class="msg_title">数据来源 :</span>
                                    <select class="input_msg"  name="dataSource" id="dataSource"></select>
                                    <div class="fr">
                                        <button type="button" class="btn ml15 btn-find waves-effect waves-light btn_default" onclick='searchData();'>查询</button>
                                        <button type="reset" class="btn btn-light waves-effect waves-light btn_default">重置</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <!-- 筛选条件结束 -->
                        <div class="row col-md-12 mt20">
                            <div class="button-list">
                                <shiro:hasPermission name="rate_toAddPage">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="rate_export">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-buttons">导出</button>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="rate_delRate">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="$custom.request.delChecked('/rate/delRate.asyn');">删除</button>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="rate_get">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="getRate()">获取汇率</button>
                                    <input type="hidden" class="input_msg form_datetime date-input" id="selectDate"/>
                                    <input type="checkbox" style="display: none" id="isPeriod">
                                </shiro:hasPermission>
                            </div>
                        </div>
                        <div class="mt20">
                            <table id="datatable-buttons" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                <tr>
		                            <th>
		                                <label class="mt-checkbox mt-checkbox-single mt-checkbox-outline">
		                                    <input type="checkbox" name="keeperUserGroup-checkable" class="group-checkable" />
		                                    <span></span>
		                                </label>
		                            </th>
                                    <th style="white-space:nowrap;">原币</th>
                                    <th style="white-space:nowrap;">本币</th>
                                    <th style="white-space:nowrap;">即期汇率<a title="1外币兑换本位币" href="#" class="dripicons-information" style="color: #00a0e9;padding-left: 5px;"></a></th>
                                    <th style="white-space:nowrap;">平均汇率</th>
                                    <th style="white-space:nowrap;">汇率日期</th>
                                    <th style="white-space:nowrap;">数据来源</th>
                                    <th style="white-space:nowrap;">创建时间</th>
                                    <th style="white-space:nowrap;">创建人</th>
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
    $module.constant.getConstantListByNameURL.call($('select[name="origCurrencyCode"]')[0],"currencyCodeList",null);
    $module.constant.getConstantListByNameURL.call($('select[name="tgtCurrencyCode"]')[0],"currencyCodeList",null);
    $module.constant.getConstantListByNameURL.call($('select[name="dataSource"]')[0],"exchangeRate_dataSourceList",null);
    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url : '/rate/listRate.asyn?r=' + Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            {data : 'origCurrencyName'},{data : 'tgtCurrencyName'},{data : 'rate'},{data : 'avgRate'},{data : 'effectiveDate'},{data: 'dataSourceLabel'},{data : 'createDate'},{data : 'createName'},
            ],
            formid:'#form1'
        };

        //每一行都进行 回调
        $mytable.rewriter.fnRowCallback=function(nRow, aData, iDisplayIndex){
            if(aData.effectiveDate != null){
                var date = new Date(aData.effectiveDate);
                $('td:eq(5)', nRow).html(date.getFullYear() + '-' + (date.getMonth()+1) + '-' + date.getDate());
            }
            if(aData.dataSource != 'SGCJ'){
                $('td:eq(0)', nRow).html('<input type="checkbox" disabled="true">');
            }
        };

        //生成列表信息
        $('#datatable-buttons').mydatatables();

        //日期控件初始化
        $(".date-input").datetimepicker({
            language:  'zh-CN',
            format: "yyyy-mm-dd",
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2
        });

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

    });

    function searchData(){
        $mytable.fn.reload();
    }

    //新增
    $("#add-buttons").click(function(){
        $load.a("/rate/toAddPage.html");
    });

    $(function () {
        $("#datatable-buttons_wrapper").removeClass('container-fluid');
    });

    //导出
    $("#export-buttons").click(function () {
        var param = $("#form1").serialize();
        $custom.alert.warning("确定导出？",function(){
            //导出
            var url = "/rate/exportRate.asyn?"+param;
            $downLoad.downLoadByUrl(url);
        });
    });

    //获取汇率
    function getRate() {
        var selectDate = $("#selectDate").val();
        var isPeriod = $("#isPeriod").prop("checked");
        var checkUrl = '/rate/getCurrencyRates.asyn';
        $custom.request.ajax(checkUrl,{"selectDate":selectDate, "isPeriod":isPeriod},function(result){
            if(result.state==200){
                $custom.alert.success("正在获取汇率信息，请稍后刷新列表");
//                $load.a("/rate/toPage.html");
            }else{
                $custom.alert.error(result.data.message);
            }
        });
    }
</script>
