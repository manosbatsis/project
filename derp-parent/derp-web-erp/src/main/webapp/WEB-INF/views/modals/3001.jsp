<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Custom Modals -->
<style>
    .header-notice-modal {
        background-color: #fff;
        color: #333;
        font-family: fantasy;
        border-top-left-radius: .3rem;
        border-top-right-radius: .3rem;
        display: flex;
        border-bottom: solid 1px #aaa;
        height: 30px;
    }
    .header-notice-title {
        padding-left: 15px !important;
        padding-top: 5px !important;
        padding-right: 795px !important;
        font-weight: bolder !important;
        font-size: 14px !important;
    }
    .header-notice-button {
        background-color: #fff !important;
        border: none !important;
    }
    .header-notice-close {
    }
    .header-notice-close::after {
        content: "\2716";
    }
    .noticeBody {
        height: 400px !important;
        overflow-y: scroll !important;
        overflow-X: scroll !important;
        padding: inherit !important;
        margin-top: 5px !important;
        width: 98.5% !important;
    }
    .noticeBody::-webkit-scrollbar {
        /*高宽分别对应横竖滚动条的尺寸*/
        width: 8px;
        height: 8px;
    }
    .noticeBody::-webkit-scrollbar-thumb {
        border-radius: 10px;
        -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
        background: rgba(0,0,0,0.2);
     }
     .noticeBody::-webkit-scrollbar-track {
        -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
        border-radius: 0;
        background: rgba(0,0,0,0.1);

     }
    .noticeHead{
        text-align: center;
        border-bottom: solid 1px #DCDCDC;
    }
    .noticeType{
        position: absolute !important;
        margin-left: 30% !important;
    }
    .publishDate{
        margin-left: 20% !important;
    }
    .noticeFooter{
        height: 20px;
        font-size: 14px;
    }
    .noticeFooter a{
        width: 200px;
        white-space:nowrap;
        text-overflow:ellipsis;
        overflow:hidden;
    }

</style>
<div>
    <!-- Signup modal content -->
    <div id="noticeModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;margin-left: -25%;">
        <div class="modal-dialog" >
            <div class="modal-content" style="width: 900px;">
                <div class="header-notice-modal" >
                    <span class="header-notice-title" >系统公告</span>
                    <button class="header-notice-button" ><a class="header-notice-close" href="javascript:$m3001.cancel();"></a></button>
                </div>
                <div class="modal-body">
                     <div class="noticeHead">
                        <div class="noticeTitle"></div>
                        <div style="padding: 0px 10px 5px 10px;font-size: 14px;">
	                        <div class="noticeType"></div>
	                        <div class="publishDate"></div>
                        </div>
                     </div>
                     <div class="noticeBody"></div>
                </div>
                <div class="modal-footer noticeFooter" style="display:none;">
                    <a href="#" class="before" style="position: absolute; left: 15%;"></a>
                    <a href="#" class="after" style="position: absolute; right: 18%;"></a>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script type="text/javascript">
    var $m3001={
        init:function(id , type){
        	$('.noticeTitle').empty() ;
        	$('.noticeBody').empty() ;
        	$('.noticeType').empty() ;
        	$('.publishDate').empty() ;
        	$('.before').empty() ;
        	$('.after').empty() ;
        	
        	
        	$custom.request.ajax($m3001.params.url,{"id":id},function(data) {
                if (data.state == 200) {
                    $('.noticeTitle').append("<h4>"+data.data.title+"</h4>");
                    $('.noticeType').append("公告类型:"+data.data.typeLabel);
                    
                    var publishDate = "" ;
                    if(data.data.publishDate){
                    	publishDate = data.data.publishDate;
                    }
                    
                    $('.publishDate').append("发布时间:"+publishDate);
                    $('.noticeBody').append(data.data.contentBody);
                } else {
                    $custom.alert.error(data.message);
                }
            });
        	
        	/**
        	* 获取上一条，下一条
        	*/
        	if("homePage" == type){
        		$(".noticeFooter").show() ;
        		
        		var url = "/notice/getAroundNotice.asyn" ;
        		
        		$custom.request.ajax(url,{"id":id},function(data) {
        			if(data.state == 200){
        				var before = data.data.after;
        				var after = data.data.before;
        				
        				if(before){
        					$(".before").html("上一条：" + before.title) ;
        					$(".before").attr("title", before.title) ;
        					
        					$(".before").unbind();
        					$(".before").click(function(){
        						showNotice(before.id, this) ;
        						getNotice(1) ;
        					});
        				}else{
        					$(".before").html("上一条：没有了") ;
        				}
        				
        				if(after){
                            $(".after").html("下一条：" + after.title) ;
                            $(".after").attr("title", after.title) ;
                            
                            $(".after").unbind();
                            $(".after").click(function(){
                            	showNotice(after.id, this) ;
                            	getNotice(1) ;
                            });
                        }else{
                        	$(".after").html("下一条：没有了") ;
                        }
        			}
        		});
        	}
        	
            this.show();
        },
        cancel:function() {
            $m3001.hide();
        },
        show:function(){
            $($m3001.params.modalId).modal({keyboard: false});
        },
        hide:function(){
            $($m3001.params.modalId).modal("hide");
            $(".noticeFooter").hide() ;
            
            $('.noticeTitle').empty() ;
            $('.noticeBody').empty() ;
            $('.noticeType').empty() ;
            $('.publishDate').empty() ;
        },
        params:{
            url:'/notice/getDetail.asyn',
            modalId:'#noticeModal',
        }
    }


</script>

