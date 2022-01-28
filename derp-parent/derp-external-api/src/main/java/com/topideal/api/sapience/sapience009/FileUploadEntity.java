package com.topideal.api.sapience.sapience009;

/**
 * @Description: 附件
 * @Author: Chen Yiluan
 * @Date: 2021/01/26 10:37
 **/
public class FileUploadEntity {

    //附件下载地址
    private String fileUrl;
    //附件名称
    private String fileName;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
