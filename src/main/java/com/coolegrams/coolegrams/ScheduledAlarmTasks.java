package com.coolegrams.coolegrams;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.coolegrams.coolegrams.dto.MessageforRefreshDto;
import com.coolegrams.coolegrams.dto.UserinfoForMessageDto;
import com.coolegrams.coolegrams.serviceImpl.AlarmMessagingServiceImpl;
import com.coolegrams.coolegrams.telegrambot.Telegrambot;

@Component
public class ScheduledAlarmTasks {
	@Autowired
	AlarmMessagingServiceImpl alarmMessagingService;
	@Autowired
	Telegrambot telebot;
	@Scheduled(cron="* */2 * * * *")
	public void alarmCallProcess() {
		//아직 목표가 못 찍은 유저 리스트
		UserinfoForMessageDto[] batchList = alarmMessagingService.getSubscribeUser();
		for(int i = 0 ; i < batchList.length ; i++) {
			//productid로 현재가격 가져온 뒤
			int currentPrice=0;
			String pId =batchList[i].getProductId();
			String pName = batchList[i].getProductName();
			int inputTimePrice = alarmMessagingService.getInputTimePrice(batchList[i].getUserSeq());
			if(alarmMessagingService.getBatchPossible(pId)!=0)
				currentPrice = alarmMessagingService.getBatchTargetPrice(pId, pName);
			if(currentPrice - batchList[i].getGoalPrice()<=0 ) {
				telebot.sendAlarmMessage(batchList[i].getChatId(), batchList[i].getGoalPrice(), currentPrice, batchList[i].getCoupangUrl(),pName,inputTimePrice);
				//메시지 송신 한 로우 플래그 값 업데이트
				alarmMessagingService.updateIFYN(batchList[i].getUserSeq());
			}
		}
	}
}
