package com.topideal.api.sapience.sapience010;

/**
 * 融资申请附件下载接口请求json
 **/
public class FinancingAttachmentDownloadRequest {

    //方法名
    private String method;
    //来源系统
    private String sourceCode;
    //文件下载唯一标识
    private String fileKey;
    //文件名称
    private String fileName;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
