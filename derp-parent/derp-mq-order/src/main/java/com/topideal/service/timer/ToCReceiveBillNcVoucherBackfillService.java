package com.topideal.service.timer;

import java.sql.SQLException;

public interface ToCReceiveBillNcVoucherBackfillService {

	void saveReceiveBillNcVoucherBackfill(String json, String keys, String topics, String tags) throws SQLException;

}
