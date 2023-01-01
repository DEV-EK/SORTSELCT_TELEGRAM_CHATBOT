package com.coolegrams.coolegrams.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coolegrams.coolegrams.dto.RefreshItemInfoDto;
import com.coolegrams.coolegrams.mapper.ItemRefreshMapper;
import com.coolegrams.coolegrams.service.ItemRefreshService;

@Service
public class ItemRefreshServiceImpl implements ItemRefreshService{
	@Autowired
	ItemRefreshMapper itemRefreshMapper;
	@Override
	public int getCurrentPrice(String keyword) {
		return itemRefreshMapper.getCurrentPrice(keyword);
	}
	@Override
	public int getExistsYn(String keyword) {
		return itemRefreshMapper.getExistsYn(keyword);
	}
	@Override
	public int insertItemInfo(RefreshItemInfoDto dto) {
		return itemRefreshMapper.insertItemInfo(dto);
	}
	public String getProductIdbyProductName(String string) {
		return itemRefreshMapper.getProductIdbyProductName(string);
	}
	public int getProductIdbyCurrentPrice(String string) {
		return itemRefreshMapper.getProductIdbyCurrentPrice(string);
	}

}
