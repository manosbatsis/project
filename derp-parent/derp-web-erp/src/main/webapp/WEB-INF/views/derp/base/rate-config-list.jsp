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
                                    <li class="breadcrumb-item"><a href="javascript:$load.a('/rateConfig/toPage.html');">汇率管理配置</a></li>
                                </ol>
                           </div>
                        </div>
                        <!-- 筛选条件开始 -->
                        <form id="form1">
                        <div class="form_box">
                            <div class="form_con">
                                <div class="form_item h35">
                                    <span class="msg_title">原币种：</span>
                                    	<select class="input_msg"  name="origCurrencyCode" id="origCurrencyCode">
			                              </select>
                                    <span>本币种</span>
                                    	<select class="input_msg"  name="tgtCurrencyCode" id="tgtCurrencyCode"  >
			                            </select>
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
                                <shiro:hasPermission name="rateConfig_toAddPage">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="add-buttons">新增</button>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="rateConfig_export">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" id="export-buttons">导出</button>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="rateConfig_del">
                                    <button type="button" class="btn btn-find waves-effect waves-light btn-sm" onclick="$custom.request.delChecked('/rateConfig/delRateConfig.asyn');">删除</button>
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
    $module.constant.getConstantListByNameURL.call($('select[name="dataSource"]')[0],"exchangeRateConfig_dataSourceList",null);
    $(document).ready(function() {
        //初始化
        $mytable.fn.init();
        //配置表格参数
        $mytable.params={
            url : '/rateConfig/listRateConfig.asyn?r=' + Math.random(),
            columns:[{ //复选框单元格
                className: "td-checkbox",
                orderable: false,
                width: "30px",
                data: null,
                render: function (data, type, row, meta) {
                    return '<input type="checkbox" class="iCheck">';
                }
            },
            {data : 'origCurrencyName'},{data : 'tgtCurrencyName'},{data: 'dataSourceLabel'},{data : 'createDate'},{data : 'createName'},
            ],
            formid:'#form1'
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
        $load.a("/rateConfig/toAddPage.html");
    });


    //导出
    $("#export-buttons").click(function () {
        var param = $("#form1").serialize();
        $custom.alert.warning("确定导出？",function(){
            //导出
            var url = "/rateConfig/export.asyn?"+param;
            $downLoad.downLoadByUrl(url);
        });
    });


</script>
