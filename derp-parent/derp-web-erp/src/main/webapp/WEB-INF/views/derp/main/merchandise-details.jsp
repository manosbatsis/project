<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>

    label[class^=col-],div[class^=col-]{padding: 2px 0;}

	.edit_span{
		text-align: right;
		line-height: 30px;
	}

	.box_input{
		width: 38px;
	    height: 30px;
	    border: 1px solid #dadada;
	    text-indent: 5px;
	    margin: 0 10px 0 10px;
	}

</style>

<!-- Start content -->
<div class="content">
    <div class="container-fluid mt80">
        <!-- Start row -->
        <div class="row">
            <div class="card-box margin table-responsive">
            	<div class="col-12 row">
                    <div>
                       <ol class="breadcrumb">
                       <li><i class="block"></i> 当前位置：</li>
                        <li class="breadcrumb-item"><a href="#">商品档案</a></li>
                        <li class="breadcrumb-item "><a href="javascript:$load.a('/merchandise/toPage.html');">商品列表</a></li>
                        <li class="breadcrumb-item "><a href="javascript:$load.a('/merchandise/toDetailsPage.html?id=${detail.id}');">商品详情</a></li>
                    </ol>
                    </div>
            </div>
                <div class="title_text">基本信息</div>
                <div class="info_box">
                    <div class="info_item">
                        <div class="info_text">
                            <span>所属公司：</span>
                            <span>
                                ${detail.merchantName }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>商品编码：</span>
                            <span>
                                ${detail.goodsCode }
                            </span>
                        </div>
                        
                        <div class="info_text" >
                            <span>外仓统一码：</span>
                            <span>
                                ${detail.outDepotFlagLabel }
                            </span>
                        </div>
                    </div>
                    
                    <div class="info_item">
                        <div class="info_text">
                            <span>商品货号：</span>
                            <span>
                                ${detail.goodsNo }
                            </span>
                        </div>                       
						<div class="info_text">
                            <span>商品条形码：</span>
                            <span>
                                ${detail.barcode }
                            </span>
                        </div>
                        <div class="info_text" >
                            <span>商品名称：</span>
                            <span>
                                ${detail.name }
                            </span>
                        </div>
                    </div>
                    
                    <div class="info_item">
                        <div class="info_text">
                            <span>数据来源：</span>
                            <span>
                                ${detail.sourceLabel }
                            </span>
                        </div>                       
						<div class="info_text">
                            <span>备案平台关区：</span>
                            <span>
                               ${customsAreaIdStr}
                            </span>
                        </div>
                        <div class="info_text" >
                            <span>HS编码：</span>
                            <span>
                                ${detail.hsCode }
                            </span>
                        </div>
                    </div>
                    
                    <div class="info_item">
                        <div class="info_text">
                            <span>企业商品编码：</span>
                            <span>
                                ${detail.factoryNo }
                            </span>
                        </div>                       
						<div class="info_text">
                            <span>商品品牌：</span>
                            <span>
                               ${brandName }
                            </span>
                        </div>
                        <div class="info_text" >
                            <span>一级品类：</span>
                            <span>
                                ${detail.productTypeName0 }
                            </span>
                        </div>
                    </div>
                    
                    <div class="info_item">
                        <div class="info_text">
                            <span>商品英文名称：</span>
                            <span>
                                ${detail.englishGoodsName }
                            </span>
                        </div>                       
						<div class="info_text">
                            <span>标准条码：</span>
                            <span>
                               ${detail.commbarcode }
                            </span>
                        </div>
                        <div class="info_text" >
                            <span>二级品类：</span>
                            <span>
                                ${detail.productTypeName }
                            </span>
                        </div>
                    </div>
                    
                    <div class="info_item">
                        <div class="info_text">
                            <span>标准品牌：</span>
                            <span>
                                ${brandName }
                            </span>
                        </div>                       
						<div class="info_text">
                            <span>创建人：</span>
                            <span>
                               ${detail.creater }
                            </span>
                        </div>
                        <div class="info_text" >
                            <span>创建时间：</span>
                            <span>
                                ${createDate }
                            </span>
                        </div>
                    </div>
                    
                   <div class="title_text">基本信息</div> 
                   <div class="info_item">
                        <div class="info_text">
                            <span>毛重：</span>
                            <span>
                                ${detail.netWeight }&nbsp;kg
                            </span>
                        </div>                       
						<div class="info_text">
                            <span>净重：</span>
                            <span>
                               ${detail.netWeight }&nbsp;kg
                            </span>
                        </div>
                        <div class="info_text" >
                            <span>规格型号：</span>
                            <span>
                                ${detail.spec }
                            </span>
                        </div>
                    </div> 
                    <div class="info_item">
                        <div class="info_text">
                            <span>保质天数：</span>
                            <span>
                                ${detail.shelfLifeDays }&nbsp;天
                            </span>
                        </div>                       
						<div class="info_text">
                            <span>申报单价：</span>
                            <span>
                               ${detail.filingPrice }
                            </span>
                        </div>
                        <div class="info_text" >
                            <span>计量单位：</span>
                            <span>
                                ${unitName }
                            </span>
                        </div>
                    </div> 
                    <div class="info_item">
                        <div class="info_text">
                            <span>原产国：</span>
                            <span>
                                ${countyName }
                            </span>
                        </div>                       
						<div class="info_text">
                            <span>原产地区：</span>
                            <span>
                               ${detail.countyArea }
                            </span>
                        </div>
                        <div class="info_text" >
                            <span>申报要素：</span>
                            <span>
                                ${detail.declareFactor }
                            </span>
                        </div>
                    </div> 
                     <div class="info_item">
                        <div class="info_text">
                            <span>商品成分：</span>
                            <span>
                                ${detail.component }
                            </span>
                        </div>                       
						<div class="info_text">
                            <span>生产企业名称：</span>
                            <span>
                               ${detail.manufacturer }
                            </span>
                        </div>
                        <div class="info_text">
                            <span>生产企业地址：</span>
                            <span>
                                ${detail.manufacturerAddr }
                            </span>
                        </div>
                    </div> 
                    
                    <div class="info_item">
                        <div class="info_text" >
                            <span>是否备案：</span>
                            <span>
                                ${detail.isRecordLabel }
                            </span>
                        </div>
                        <div class="info_text">

                        </div>
                        <div class="info_text">

                        </div>
                    </div>
                    
                <div class="card-box margin table-responsive">
              	<div class="title_text">箱规设置</div>
				<div class="form-row mt20 ml15">
				    <div class="edit_item">
				        <label class="edit_label" style="width: 15%">箱转换：</label>
				        <span class="edit_span">1箱 转换为</span>&nbsp;
				        <span class="edit_span">${detail.boxToUnit }</span>&nbsp;
				        <span class="edit_span">件</span>
				    </div>
				    <div class="edit_item">
				        <label class="edit_label" style="width: 15%">托转换：</label>
				        <span class="edit_span">1托 转换为</span>&nbsp;
				        <span class="edit_span">${detail.torrToUnit }</span>&nbsp;
				        <span class="edit_span">件</span>
				    </div>
				</div>
              </div>
                    
              <div class="title_text">商品描述</div>      
              <div class="info_item">
                    <div class="info_text">
                        <span>长：</span>
                        <span>
                            ${detail.length }
                        </span>
                    </div>                       
					<div class="info_text">
                        <span>宽：</span>
                        <span>
                           ${detail.width }
                        </span>
                    </div>
                    <div class="info_text">
                        <span>高：</span>
                        <span>
                            ${detail.height }
                        </span>
                    </div>
               </div>       
               <div class="info_item">
                    <div class="info_text">
                        <span>体积：</span>
                        <span>
                            ${detail.volume }
                        </span>
                    </div>                       
					<div class="info_text">
                        <span>包装方式：</span>
                        <span>
                           ${detail.packType }
                        </span>
                    </div>
                    <div class="info_text">
                        <span>商品描述：</span>
                        <span>
                            ${detail.describe }
                        </span>
                    </div>
               </div>     
               <div class="info_item">
                    <div class="info_text">
                        <span>最后修改人：</span>
                        <span>
                            ${detail.modifier }
                        </span>
                    </div>                       
					<div class="info_text">
                        <span>最后修改时间：</span>
                        <span>
                           ${modifyDate }
                        </span>
                    </div>
                    <div class="info_text">

                    </div>
               </div>       
                    
              </div>
            



		 <table class="table table-striped" cellspacing="0" width="100%">
		 <div class="title_text">商品信息</div>
                   <div class="row col-12 mt20">
                       <table class="table table-bordered" cellspacing="0" width="100%">
                           <thead>
                           <tr>
                               <th>图片1</th>
                               <th>图片2</th>
                               <th>图片3</th>
                               <th>图片4</th>
                               <th>图片5</th>
                           </tr>
                           </thead>
                           <tbody>
                           
                           <tr>
                            <td style="height:12px; padding-top: 0px; width:300px">
                       		<img style="border:0;margin-top:0px;" src="${detail.imageUrl1 }" alt="image" id="file1img" width="100" height="100" onclick="showimage('${detail.imageUrl1 }')"/>                       								                         							                         					                        
                   			</td>
                            <td style="height:12px; padding-top: 0px; width:300px">
                       		<img style="border:0;margin-top:0px;" src="${detail.imageUrl2 }" alt="image" id="file2img" width="100" height="100" onclick="showimage('${detail.imageUrl2 }')"/>                      							                         							                         					                        
                   			</td>
                            <td style="height:12px; padding-top: 0px; width:300px">
                       		<img style="border:0;margin-top:0px;" src="${detail.imageUrl3 }" alt="image" id="file3img" width="100" height="100" onclick="showimage('${detail.imageUrl3 }')"/>                       						                         							                         					                        
                   			</td>
                            <td style="height:12px; padding-top: 0px; width:300px">
                       		<img style="border:0;margin-top:0px;" src="${detail.imageUrl4 }" alt="image" id="file4img" width="100" height="100" onclick="showimage('${detail.imageUrl4 }')"/>                       						                         							                         					                        
                   			</td>
                   			<td style="height:12px; padding-top: 0px; width:300px">
                       		<img style="border:0;margin-top:0px;" src="${detail.imageUrl5 }" alt="image" id="file5img" width="100" height="100" onclick="showimage('${detail.imageUrl5 }')"/>					                         							                         					                        
                   			</td>
                           </tr>

                          
                           </tbody>
                       </table>
                   </div>
                                       
                                       

         </table>   
                                                 
       </div>                                        


            <div class="card-box margin table-responsive">
            	 <div class="form-row">
                    <div class="col-3">
                        <div class="form-row m-t-50">
                              <div class="row">
                                  <div class="col-5">
                                      <button type="button" class="btn btn-find waves-effect waves-light btn_default" id="cancel-buttons">返 回</button>
                                  </div>
                              </div>
                      </div>
                    </div>
          </div> 
            	
            </div>
            <!-- end row -->
        </div>
        <!-- container -->
    </div>
    
    
        <div id="ShowImage_Form" class="modal hide">         
         <div class="modal-header">
             <button data-dismiss="modal" class="close" type="button"></button>
         </div>
           <div class="modal-body">
            <div id="img_show"  >
            </div>
        </div>
    </div>

</div> <!-- content -->
<script type="text/javascript">

		$(function(){
			//返回按钮
			$("#cancel-buttons").click(function(){
				$load.a("/merchandise/toPage.html");

			});
		});
		
		//显示大图    
		function showimage(source){
			window.open(source);
		      /* $("#ShowImage_Form").find("#img_show").html("<center><image src='"+source+"' class='carousel-inner img-responsive img-rounded'/></center>");
		      $("#ShowImage_Form").modal(); */
		}
		//关闭大图
		$("#ShowImage_Form").click(function(){//再次点击淡出消失弹出层
			 $("#ShowImage_Form").modal('hide');  //手动关闭
		});
</script>
