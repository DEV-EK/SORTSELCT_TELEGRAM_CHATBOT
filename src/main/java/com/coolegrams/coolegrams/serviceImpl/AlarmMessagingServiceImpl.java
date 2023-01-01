package com.coolegrams.coolegrams.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coolegrams.coolegrams.dto.MessageforRefreshDto;
import com.coolegrams.coolegrams.dto.UserinfoForMessageDto;
import com.coolegrams.coolegrams.mapper.AlarmMessagingMapper;
import com.coolegrams.coolegrams.service.AlarmMessagingService;

@Service
public class AlarmMessagingServiceImpl implements AlarmMessagingService{
	@Autowired
	AlarmMessagingMapper alarmMessagingMapper;
	
	@Override
	public MessageforRefreshDto[] getBatchTarget() {
		return alarmMessagingMapper.getBatchTarget();
	}

	@Override
	public UserinfoForMessageDto[] getSubscribeUser() {
		return alarmMessagingMapper.getSubscribeUser();
	}

	public void updateIFYN(int dtoSeq) {
		alarmMessagingMapper.updateIFYN(dtoSeq);
	}

	public int getBatchTargetPrice(String productId, String pName) {
		return alarmMessagingMapper.getBatchTargetPrice(productId, pName);
	}

	@Override
	public UserinfoForMessageDto[] getSubscribeUser(String productId) {
		return null;
	}

	public int getBatchPossible(String productId) {
		return alarmMessagingMapper.getBatchPossible(productId);
	}

	public int getInputTimePrice(int userSeq) {
		return alarmMessagingMapper.getInputTimePrice(userSeq);
	}
	
}
