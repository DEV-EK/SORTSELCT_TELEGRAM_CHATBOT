package com.coolegrams.coolegrams.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.coolegrams.coolegrams.dto.CoupangItemDto;

@Mapper
public interface CallCoupangApiMapper {
	void insertCoupangItemInfo(CoupangItemDto[] items);
}
