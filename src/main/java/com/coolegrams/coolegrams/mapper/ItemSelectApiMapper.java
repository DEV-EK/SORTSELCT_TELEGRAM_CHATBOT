package com.coolegrams.coolegrams.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.coolegrams.coolegrams.dto.CoupangItemDto;
import com.coolegrams.coolegrams.dto.RefreshItemInfoDto;

@Mapper
public interface ItemSelectApiMapper {
	CoupangItemDto[] getSearchResult(@Param("keyword") String keyword);
	int getSearchResultCount(@Param("keyword")String keyword);
	String getProductIdbyProductName(@Param("productName")String productName);
	String[] getScheduledList();
	void changeUseYn(String productName);
	void insertCrawlingResponse(RefreshItemInfoDto refreshItemInfoDto);
	String getTodayLink();
}