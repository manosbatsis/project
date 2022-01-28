package com.topideal.service.timer;

import java.sql.SQLException;

public interface ReceiveBillNcVoucherBackfillService {

	void saveReceiveBillNcVoucherBackfill(String json, String keys, String topics, String tags) throws SQLException;

}
