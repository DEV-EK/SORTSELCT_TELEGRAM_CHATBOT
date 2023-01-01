package com.coolegrams.coolegrams.service;

import com.coolegrams.coolegrams.dto.RefreshItemInfoDto;

public interface ItemRefreshService {
	int getExistsYn(String keyword);
	int getCurrentPrice(String keyword);
	int insertItemInfo(RefreshItemInfoDto dto);
	String getProductIdbyProductName(String string);
}
