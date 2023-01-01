package com.coolegrams.coolegrams.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.coolegrams.coolegrams.dto.RefreshItemInfoDto;

@Mapper
public interface ItemRefreshMapper {
	int getCurrentPrice(String keyword);
	int getExistsYn(String keyword);
	int insertItemInfo(RefreshItemInfoDto dto);
	String getProductIdbyProductName(String string);
	int getProductIdbyCurrentPrice(String string);
}
