package com.topideal.order.webapi.bill;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.*;
import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.dto.bill.ReceiveBillInvoiceDTO;
import com.topideal.entity.dto.bill.ReceiveBillToNCDTO;
import com.topideal.order.service.bill.ReceiveBillInvoiceService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.bill.form.ReceiveBillInvoiceForm;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Api(tags = "应收发票库")
@RestController
@RequestMapping("/webapi/order/receiveBillInvoice")
public class APIReceiveBillInvoiceController {

    @Autowired
    private ReceiveBillInvoiceService receiveBillInvoiceService;


    @ApiOperation(value = "获取分页数据")
    @PostMapping(value = "/listReceiveBillInvoice.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    private ResponseBean<ReceiveBillInvoiceDTO> listReceiveBillInvoice(ReceiveBillInvoiceForm form) {
        try {
            User user = ShiroUtils.getUserByToken(form.getToken());
            ReceiveBillInvoiceDTO dto = new ReceiveBillInvoiceDTO();
            dto.setMerchantId(user.getMerchantId());
            BeanUtils.copyProperties(form, dto);
            // 响应结果集
            ReceiveBillInvoiceDTO receiveBillInvoiceDTO = receiveBillInvoiceService.listReceiveBillInvoiceByPage(dto, user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, receiveBillInvoiceDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }

    @ApiOperation(value = "获取关联应收账单")
    @PostMapping(value = "/listReceiveBills.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "应收发票id", required = true)})
    private ResponseBean<ReceiveBillDTO> listReceiveBills(String token, String id) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }

            List<ReceiveBillDTO> receiveBillDTOS = receiveBillInvoiceService.listBillByInvoiceId(id);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, receiveBillDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "导出应收对账",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value="/exportBillInvoice.asyn")
    public void exportBillInvoice(ReceiveBillInvoiceForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
        User user= ShiroUtils.getUserByToken(form.getToken());
        ReceiveBillInvoiceDTO dto = new ReceiveBillInvoiceDTO();
        dto.setMerchantId(user.getMerchantId());
        BeanUtils.copyProperties(form, dto);
        //表头信息
        List<Map<String,Object>> receiveBillInvoiceDTOS = receiveBillInvoiceService.listForBillExport(dto);

        //表头信息
        String sheetName = "应收对账明细";
        String[] columns = {"公司","应收单号","发票编号","客户",	"账单年月","发票日期","结算币种","事业部","结算类型","销售模式","平台",
                "渠道","品牌","NC收支费项","NC科目","应收金额","操作人"};
        String[] keys = {"merchantName","billCode","invoiceNo","customer","month","invoiceDate","currency","buName","settlementType","saleModel","ncPlatformName",
                "ncChannelName","brandName","paymentSubjectName","subjectName","price","synchronizer"};

        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, receiveBillInvoiceDTOS);
        //导出文件
        String fileName = "应收对账明细";
        FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
    }

    @ApiOperation(value = "获取发票详情")
    @PostMapping(value = "/getInvoiceDetail.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "发票id", required = true)})
    private ResponseBean<ReceiveBillInvoiceDTO> getInvoiceDetail(@RequestParam(value = "token", required = true) String token, Long id) {
        try {
            ReceiveBillInvoiceDTO dto = new ReceiveBillInvoiceDTO();
            dto.setId(id);
            ReceiveBillInvoiceDTO invoiceDTO = receiveBillInvoiceService.searchByDTO(dto);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, invoiceDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }

    }


    @ApiOperation(value = "完成发票签章")
    @PostMapping(value = "/modifyInvoice.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "发票id", required = true)})
    private ResponseBean modifyInvoice(@RequestParam(value = "token", required = true) String token, Long id) {
        try {
            User user = ShiroUtils.getUserByToken(token);
            // 响应结果集
            Map<String, String> result = receiveBillInvoiceService.modifyStatus(id, user);
            String code = result.get("code");
            String message = result.get("message");
            String status = result.get("status");

            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015.getCode(), message);
            }

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, status);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "获取关联应收单提交NC信息")
    @PostMapping(value = "/listReceiveBillsToNC.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "发票id", required = true),
            @ApiImplicitParam(name = "dataSource", value = "数据来源", required = true),
    })
    private ResponseBean listReceiveBillsToNC(@RequestParam(value = "token", required = true) String token, String id, String dataSource) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            // 响应结果集
            List<ReceiveBillToNCDTO> receiveBillToNCDTOs = receiveBillInvoiceService.listReceiveBillsToNCById(id, dataSource);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, receiveBillToNCDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "同步NC")
    @PostMapping(value = "/synNC.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "发票id", required = true),
            @ApiImplicitParam(name = "dataSource", value = "数据来源", required = true),
    })
    private ResponseBean synNC(@RequestParam(value = "token", required = true) String token, String id, String dataSource) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user = ShiroUtils.getUserByToken(token);
            // 响应结果集
            Map<String, String> result = receiveBillInvoiceService.synNC(id, dataSource, user);
            String code = result.get("code");
            String message = result.get("message");

            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015.getCode(), message);
            }

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    @ApiOperation(value = "导出应收单NC提交信息",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value="/download.asyn")
    public ResponseBean exportNCReceiveBill(String ids, HttpServletResponse response) {
        try {
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            List list = StrUtils.parseIds(ids);
            String pdfFile = receiveBillInvoiceService.exportNcBillPDF(list);
            File filePath = new File(pdfFile);
            //转换中文否则可能会产生乱码
            String downloadFilename = URLEncoder.encode("应收账单打印模板.zip", "UTF-8");
            // 指明response的返回对象是文件流
            response.setContentType("application/octet-stream");
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            File[] pdfFiles = filePath.listFiles();
            if(pdfFiles != null && pdfFiles.length > 0){
                for(File file : pdfFiles){
                    //获取模板,压缩到zip文件中
                    InputStream in = new FileInputStream(file);
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    byte[] buffer = new byte[1024];
                    int r = 0;
                    while ((r = in.read(buffer)) != -1) {
                        zos.write(buffer, 0, r);
                    }
                    in.close();
                }
                deleteDir(pdfFile);
            }
            zos.flush();
            zos.close();
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

    private void deleteDir(String dirPath) {
        File file = new File(dirPath);
        if(file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if(files == null) {
                file.delete();
            } else  {
                for (int i = 0; i < files.length; i++) {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }

    /**
     * 校验发票是否有至少一个已同步的应收账单，若有则不予操作同步NC
     * @param id
     */
    @ApiOperation(value = "校验发票")
    @PostMapping(value = "/validateSynNC.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "发票id", required = true)
    })
    public ResponseBean validateSynNC(@RequestParam(value = "token", required = true) String token, String id) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {
                // 输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            Map<String, String> resultMap = receiveBillInvoiceService.validateSynNC(id);
            String code = resultMap.get("code");

            boolean flag = true;

            if ("01".equals(code)) {
                flag = false;
            }

            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, flag);
        } catch (Exception e) {
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }



    @ApiOperation(value = "替换发票文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "id", value = "发票id", required = true)
    })
    @PostMapping(value = "/replaceInvoice.asyn", headers = "content-type=multipart/form-data")
    public ResponseBean replaceInvoice(String token, String id,
                                       @ApiParam(value = "替换文件", required = true)
                                       @RequestParam(value = "file", required = true) MultipartFile file) throws Exception{

        try{
            if(file==null || StringUtils.isBlank(id)){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
            }
            User user = ShiroUtils.getUserByToken(token);
            Map<String, Object> resultMap = receiveBillInvoiceService.replaceInvoiceFile(id, file, user);
            String code = (String) resultMap.get("code");
            String message = (String) resultMap.get("message");

            if ("01".equals(code)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015.getCode(), message);
            }
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

        }catch(Exception e){
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }


    @ApiOperation(value = "发票预览")
    @GetMapping("/preview/{fileName}")
    public String preview(HttpServletResponse response, @PathVariable @NotNull String fileName) throws Exception {
        InputStream input = null;
        OutputStream out = null;
        String downloadFilename = null;
        try {
            ReceiveBillInvoiceDTO dto = new ReceiveBillInvoiceDTO();
            dto.setInvoiceNo(fileName);
            ReceiveBillInvoiceDTO invoiceDTO = receiveBillInvoiceService.searchByDTO(dto);
            if (StringUtils.isBlank(invoiceDTO.getInvoicePath())) {
                return null;
            }
            String filePath = invoiceDTO.getInvoicePath();
            byte[] bytes = DownloadUtil.loadFileByteFromURL(filePath);
            input = new ByteArrayInputStream(bytes);
            downloadFilename = URLEncoder.encode(fileName+"账单发票.pdf", "UTF-8");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "filename=" + downloadFilename);
            out = response.getOutputStream();
            IOUtils.copy(input, out);
            out.flush();
            input.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                input.close();
            }
            if (out != null) {
                out.close();
            }
        }
        return downloadFilename;
    }

    @ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping(value="/exportInvoice.asyn")
    public void exportInvoice(ReceiveBillInvoiceForm form, HttpServletResponse response, HttpServletRequest request) throws IOException {
        User user= ShiroUtils.getUserByToken(form.getToken());
        ReceiveBillInvoiceDTO dto = new ReceiveBillInvoiceDTO();
        dto.setMerchantId(user.getMerchantId());
        BeanUtils.copyProperties(form, dto);
        //表头信息
        List<Map<String,Object>> receiveBillInvoiceDTOS = receiveBillInvoiceService.listForExport(dto, user);

        //表头信息
        String sheetName = "应收发票信息";
        String[] columns = {"发票编号","开票公司","开票人","开票日期","客户","发票状态","币种","金额"};
        String[] keys = {"invoice_no","merchant_name","creater","invoice_date","customer_name","statusLabel","currencyLabel","invoice_amount"};

        //生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, receiveBillInvoiceDTOS);
        //导出文件
        String fileName = "应收发票库"+ TimeUtils.formatDay();
        FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
    }
}
