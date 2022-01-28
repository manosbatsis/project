package com.topideal.service.timer;

import java.sql.SQLException;

public interface PurchaseAmountBackfillService {

	void savePurchaseAmountBackfill(String json, String keys, String topics, String tags) throws SQLException;

	void savePurchaseInvoiceInfo(String json, String keys, String topics, String tags) throws SQLException;

}
