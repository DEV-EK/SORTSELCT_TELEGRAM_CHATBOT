package com.coolegrams.coolegrams.service;

import com.coolegrams.coolegrams.dto.MessageforRefreshDto;
import com.coolegrams.coolegrams.dto.UserinfoForMessageDto;

public interface AlarmMessagingService {
	MessageforRefreshDto[] getBatchTarget();

	UserinfoForMessageDto[] getSubscribeUser(String productId);
	
	void updateIFYN(int dtoSeq);

	UserinfoForMessageDto[] getSubscribeUser();
}