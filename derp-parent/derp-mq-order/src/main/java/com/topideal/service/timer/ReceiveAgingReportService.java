package com.topideal.service.timer;

public interface ReceiveAgingReportService {

    void saveReceiveAgingReport(String json, String keys, String topics, String tags) throws Exception;
}
