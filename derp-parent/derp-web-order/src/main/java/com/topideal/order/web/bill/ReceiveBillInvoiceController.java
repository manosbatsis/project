    package com.topideal.order.web.bill;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.*;
import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.dto.bill.ReceiveBillInvoiceDTO;
import com.topideal.entity.dto.bill.ReceiveBillToNCDTO;
import com.topideal.order.service.bill.ReceiveBillInvoiceService;
import com.topideal.order.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description: 应收发票库
 * @Author: Chen Yiluan
 * @Date: 2020/09/03 15:49
 **/
@Controller
@RequestMapping("/receiveBillInvoice")
public class ReceiveBillInvoiceController {

    @Autowired
    private ReceiveBillInvoiceService receiveBillInvoiceService;

    /**
     * 访问列表页面
     *
     * @param model
     */
    @RequestMapping("/toPage.html")
    public String toPage(Model model) throws Exception {
        return "derp/bill/receive-bill-invoice-list";
    }

    /**
     * 获取分页数据
     *
     * @param dto
     */
    @RequestMapping("/listReceiveBillInvoice.asyn")
    @ResponseBody
    private ViewResponseBean listReceiveBillInvoice(ReceiveBillInvoiceDTO dto) {
        try {
            User user = ShiroUtils.getUser();
            // 设置商家id
            dto.setMerchantId(user.getMerchantId());
            // 响应结果集
            ReceiveBillInvoiceDTO receiveBillInvoiceDTO = receiveBillInvoiceService.listReceiveBillInvoiceByPage(dto, user);
            return ResponseFactory.success(receiveBillInvoiceDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }

    }

    /**
     * 根据查询条件导出
     */
    @RequestMapping("/exportInvoice.asyn")
    public void exportInvoice(ReceiveBillInvoiceDTO dto, HttpServletResponse response, HttpServletRequest request) {
        User user= ShiroUtils.getUser();
        dto.setMerchantId(user.getMerchantId());

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

    /**
     * 获取关联应收单信息
     * @param id
     */
    @RequestMapping("/listReceiveBills.asyn")
    @ResponseBody
    private ViewResponseBean listReceiveBills(String id) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            // 响应结果集
            List<ReceiveBillDTO> receiveBillDTOS = receiveBillInvoiceService.listBillByInvoiceId(id);
            return ResponseFactory.success(receiveBillDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
    }

    @ResponseBody
    @RequestMapping("/preview/{fileName}")
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


    /**
     * 访问附件上传页面
     * @param model
     */
    @RequestMapping("/toInvoiceUploadPage.html")
    public String toInvoiceUploadPage(Long id, Model model) throws Exception {
        ReceiveBillInvoiceDTO dto = new ReceiveBillInvoiceDTO();
        dto.setId(id);
        ReceiveBillInvoiceDTO invoiceDTO = receiveBillInvoiceService.searchByDTO(dto);
        model.addAttribute("invoice", invoiceDTO);
        return "derp/bill/receive-bill-invoice-upload";
    }


    @ResponseBody
    @RequestMapping("/previewByUrl.asyn")
    public String previewByUrl(HttpServletResponse response, String fileUrl, String fileName) throws Exception {
        InputStream input = null;
        OutputStream out = null;
        String downloadFilename = null;
        try {

            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            input = conn.getInputStream();
            downloadFilename = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + downloadFilename);
            out = response.getOutputStream();
            byte[] b = new byte[input.available()];
            if (out != null) {
                if (input != null) {
                    while ((input.read(b)) != -1) {
                        out.write(b);
                    }
                } else {
                    System.out.println("InputStream为空。。。");
                }
            } else {
                System.out.println("OutputStream为空。。。");
            }
            out.flush();
            input.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return downloadFilename;
    }

    /**
     * 完成发票签章
     * @param dto
     */
    @RequestMapping("/modifyInvoice.asyn")
    @ResponseBody
    private ViewResponseBean modifyInvoice(ReceiveBillInvoiceDTO dto) {
        Map<String, String> result = new HashMap<>();
        try {
            // 必填项空值校验
            boolean isNull = new EmptyCheckUtils().addObject(dto.getId()).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            User user = ShiroUtils.getUser();
            // 响应结果集
            result = receiveBillInvoiceService.modifyStatus(dto.getId(), user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
        return ResponseFactory.success(result);
    }

    /**
     * 获取关联应收单提交NC信息
     * @param id
     */
    @RequestMapping("/listReceiveBillsToNC.asyn")
    @ResponseBody
    private ViewResponseBean listReceiveBillsToNC(String id, String dataSource) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            // 响应结果集
            List<ReceiveBillToNCDTO> receiveBillToNCDTOs = receiveBillInvoiceService.listReceiveBillsToNCById(id, dataSource);
            return ResponseFactory.success(receiveBillToNCDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
    }

    /**
     * 同步NC
     * @param id
     */
    @RequestMapping("/synNC.asyn")
    @ResponseBody
    private ViewResponseBean synNC(String id, String dataSource) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            User user = ShiroUtils.getUser();
            // 响应结果集
            Map<String, String> result = receiveBillInvoiceService.synNC(id, dataSource, user);
            return ResponseFactory.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
    }

    /**
     * 根据查询条件导出应收对账
     */
    @RequestMapping("/exportBillInvoice.asyn")
    public void exportBillInvoice(ReceiveBillInvoiceDTO dto, HttpServletResponse response, HttpServletRequest request) throws Exception {
        User user= ShiroUtils.getUser();
        dto.setMerchantId(user.getMerchantId());

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

    /**
     * 导出应收单NC提交信息
     * @param ids
     */
    @RequestMapping("/download.asyn")
    @ResponseBody
    public ViewResponseBean exportNCReceiveBill(String ids, HttpServletResponse response) {
        try {
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if (!isRight) {
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
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
            return null;
        } catch (NullPointerException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
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
    @RequestMapping("/validateSynNC.asyn")
    @ResponseBody
    public ViewResponseBean validateSynNC(String id) {
        try {
            boolean isNull = new EmptyCheckUtils().addObject(id).empty();
            if (isNull) {
                // 输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            Map<String, String> resultMap = receiveBillInvoiceService.validateSynNC(id);
            return ResponseFactory.success(resultMap);
        } catch (SQLException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
        } catch (NullPointerException e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
        }
    }

    /**
     * 访问发票替换页面
     * @param model
     */
    @RequestMapping("/toReplace.html")
    public String toReplace(Model model, String id) throws Exception {
        model.addAttribute("invoiceId", id);
        return "derp/bill/receive-bill-invoice-replace";
    }

    /**
     * 替换发票文件
     * */
    @RequestMapping("/replaceInvoice.asyn")
    @ResponseBody
    public ViewResponseBean replaceInvoice(String id, @RequestParam(value = "file", required = false) MultipartFile file) throws Exception{
        User user = ShiroUtils.getUser();
        Map resultMap = new HashMap();//返回的结果集
        try{
            if(file==null || StringUtils.isBlank(id)){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            resultMap = receiveBillInvoiceService.replaceInvoiceFile(id, file, user);
        }catch(Exception e){
            e.printStackTrace();
            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
        }
        return ResponseFactory.success(resultMap);
    }
}
