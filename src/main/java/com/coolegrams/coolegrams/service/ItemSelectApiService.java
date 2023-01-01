package com.coolegrams.coolegrams.service;

import com.coolegrams.coolegrams.dto.CoupangItemDto;
import com.coolegrams.coolegrams.dto.RefreshItemInfoDto;

public interface ItemSelectApiService {
	public CoupangItemDto[] getSearchResult(String keyword); 
	public int getSearchResultCount(String keyword);
	public String getProductIdbyProductName(String productName);
	public String[] getScheduledList();
	public void changeUseYn(String tempText);
	void insertCrawlingResponse(RefreshItemInfoDto refreshItemInfoDto);
}
