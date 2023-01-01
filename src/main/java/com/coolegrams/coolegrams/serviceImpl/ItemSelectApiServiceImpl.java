package com.coolegrams.coolegrams.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.coolegrams.coolegrams.dto.CoupangItemDto;
import com.coolegrams.coolegrams.dto.RefreshItemInfoDto;
import com.coolegrams.coolegrams.mapper.ItemSelectApiMapper;
import com.coolegrams.coolegrams.service.ItemSelectApiService;

@ComponentScan
@Service
public class ItemSelectApiServiceImpl implements ItemSelectApiService{
	@Autowired
	private ItemSelectApiMapper itemSelectApiMapper;
	
	/**
	 * 검색한 키워드에 대한 결과값을 디비에서 가져오는 메소드
	 */
	@Override
	public CoupangItemDto[] getSearchResult(String keyword) {
		return itemSelectApiMapper.getSearchResult(keyword);
	}
	
	/**
	 * 검색한 키워드에 대한 결과값이 있는지 조회하는 메소드. 값이 없으면 이어서 api를 호출해야함
	 */
	@Override
	public int getSearchResultCount(String keyword) {
		return itemSelectApiMapper.getSearchResultCount(keyword);
	}
	
	/**
	 * productName으로 productId를 조회하는 메소드
	 */
	@Override
	public String getProductIdbyProductName(String productName) {
		return itemSelectApiMapper.getProductIdbyProductName(productName);
	}
	
	/**
	 * scheduled 돌며 가격 변동을 갱신할 목록을 가져오는 메소드
	 */
	@Override
	public String[] getScheduledList() {
		return itemSelectApiMapper.getScheduledList();
	}

	/**
	 *  알람 삭제 선택시, 해당 제품명으로 로우를 찾아 USE_YN을 'N'으로 바꾸는 메소드 
	 */
	@Override
	public void changeUseYn(String tempText) {
		itemSelectApiMapper.changeUseYn(tempText);
	}
	
	@Override
	public void insertCrawlingResponse(RefreshItemInfoDto refreshItemInfoDto) {
		itemSelectApiMapper.insertCrawlingResponse(refreshItemInfoDto);
		
	}
	
	public String getTodayLink() {
		return itemSelectApiMapper.getTodayLink();
	}

}
