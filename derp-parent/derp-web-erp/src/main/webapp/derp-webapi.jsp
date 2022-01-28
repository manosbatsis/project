<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />

</head>


<body>

<jsp:include page="/WEB-INF/views/common.jsp"/>

<h2 cid="n0" mdtype="heading" class="md-end-block md-heading" style="box-sizing: border-box; break-after: avoid-page; break-inside: avoid; font-size: 1.75em; margin-top: 1rem; margin-bottom: 1rem; position: relative; line-height: 1.225; cursor: text; padding-bottom: 0.3em; border-bottom: 1px solid rgb(238, 238, 238); white-space: pre-wrap; color: rgb(51, 51, 51); font-family: &quot;Open Sans&quot;, &quot;Clear Sans&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif;">
    <span md-inline="plain" class="md-plain" style="box-sizing: border-box;">经分销ERP-WEB接口文档</span>
</h2>
<p></p>
<div style="text-align:center">
    <figure class="md-table-fig md-focus" cid="n9" mdtype="table" style="box-sizing: border-box; margin: 1.2em 0px; overflow-x: auto; max-width: calc(100% + 16px); padding: 0px; cursor: default; color: rgb(51, 51, 51); font-family: &quot;Open Sans&quot;, &quot;Clear Sans&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif; white-space: normal;">
        <table class="md-table" width="800">
            <thead style="box-sizing: border-box; background-color: rgb(248, 248, 248);">
            <tr class="md-end-block firstRow" cid="n10" mdtype="table_row" style="box-sizing: border-box; break-inside: avoid; break-after: auto; border-top: 1px solid rgb(223, 226, 229); margin: 0px; padding: 0px;">
                <th style="box-sizing: border-box; padding: 6px 13px; border-bottom: 0px; border-top-color: rgb(223, 226, 229); border-right-color: rgb(223, 226, 229); border-left-color: rgb(223, 226, 229); margin: 0px; text-align: center;">
                    <span class="td-span" cid="n11" mdtype="table_cell" style="box-sizing: border-box; display: inline-block; min-width: 1ch; width: 772px; min-height: 10px;"><span md-inline="plain" class="md-plain" style="box-sizing: border-box;">模块</span></span>
                </th>
            </tr>
            </thead>
            <tbody style="box-sizing: border-box;">
            <tr class="md-end-block" cid="n17" mdtype="table_row" style="box-sizing: border-box; break-inside: avoid; break-after: auto; border-top: 1px solid rgb(223, 226, 229); margin: 0px; padding: 0px;">
                <td style="box-sizing: border-box; padding: 6px 13px; border-color: rgb(223, 226, 229); margin: 0px; min-width: 32px; text-align: center;">
                    <span class="td-span" cid="n18" mdtype="table_cell" style="box-sizing: border-box; display: inline-block; min-width: 1ch; width: 772px; min-height: 10px;"><span md-inline="plain" class="md-plain md-expand" style="box-sizing: border-box;"><a href="/doc.html">主服务-system</a></span></span>
                </td>
            </tr>
            <tr class="md-end-block" cid="n180" mdtype="table_row" style="box-sizing: border-box; break-inside: avoid; break-after: auto; border-top: 1px solid rgb(223, 226, 229); margin: 0px; padding: 0px; background-color: rgb(248, 248, 248);">
                <td style="box-sizing: border-box; padding: 6px 13px; border-color: rgb(223, 226, 229); margin: 0px; min-width: 32px; text-align: center;">
                    <span class="td-span" cid="n181" mdtype="table_cell" style="box-sizing: border-box; display: inline-block; min-width: 1ch; width: 772px; min-height: 10px;"><span md-inline="plain" class="md-plain" style="box-sizing: border-box;"><a href="${storageWebhost}/doc.html">仓储-storage</a>&nbsp;&nbsp;</span></span>
                </td>
            </tr>
            <tr class="md-end-block" cid="n182" mdtype="table_row" style="box-sizing: border-box; break-inside: avoid; break-after: auto; border-top: 1px solid rgb(223, 226, 229); margin: 0px; padding: 0px;">
                <td style="box-sizing: border-box; padding: 6px 13px; border-color: rgb(223, 226, 229); margin: 0px; min-width: 32px; text-align: center;">
                    <span class="td-span" cid="n183" mdtype="table_cell" style="box-sizing: border-box; display: inline-block; min-width: 1ch; width: 772px; min-height: 10px;"><span md-inline="plain" class="md-plain" style="box-sizing: border-box;"><a href="${inventoryWebhost}/doc.html">库存-inventory</span></span>
                </td>
            </tr>
            <tr class="md-end-block" cid="n184" mdtype="table_row" style="box-sizing: border-box; break-inside: avoid; break-after: auto; border-top: 1px solid rgb(223, 226, 229); margin: 0px; padding: 0px; background-color: rgb(248, 248, 248);">
                <td style="box-sizing: border-box; padding: 6px 13px; border-color: rgb(223, 226, 229); margin: 0px; min-width: 32px; text-align: center;">
                    <span class="td-span" cid="n185" mdtype="table_cell" style="box-sizing: border-box; display: inline-block; min-width: 1ch; width: 772px; min-height: 10px;"><span md-inline="plain" class="md-plain" style="box-sizing: border-box;"><a href="${reportWebhost}/doc.html">报表-report&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span>
                </td>
            </tr>
            <tr class="md-end-block" cid="n186" mdtype="table_row" style="box-sizing: border-box; break-inside: avoid; break-after: auto; border-top: 1px solid rgb(223, 226, 229); margin: 0px; padding: 0px;">
                <td style="box-sizing: border-box; padding: 6px 13px; border-color: rgb(223, 226, 229); margin: 0px; min-width: 32px; text-align: center; word-break: break-all;">
                    <span class="td-span" cid="n187" mdtype="table_cell" style="box-sizing: border-box; display: inline-block; min-width: 1ch; width: 772px; min-height: 10px;"><span md-inline="plain" class="md-plain" style="box-sizing: border-box;"><a href="${orderWebhost}/doc.html">业务-order&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span>
                </td>
            </tr>
            </tbody>
        </table>
    </figure>
</div>

</body>
</html>