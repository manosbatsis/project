package com.topideal.service.api.purchase;

import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;

public interface YwmsPurchaseWarehouseService {

	InvetAddOrSubtractRootJson saveStatus(String json, String keys, String topics, String tags) throws Exception;

	void pushInventory(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson);

}
