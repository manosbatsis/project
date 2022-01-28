package com.topideal.service.timer;

import java.sql.SQLException;

public interface AdvanceBillNcBackfillService {

    public void saveReceiveBillNcBackfill(String json, String keys, String topics, String tags) throws SQLException;

}
