package com.coolegrams.coolegrams.service;

import com.coolegrams.coolegrams.dto.TelegramUserInfoDto;

public interface TelegramUserInfoService {
	public void addUserInfo(TelegramUserInfoDto dto);
	public int userAlarmCount(String chatId);
	public TelegramUserInfoDto[] getSubscribeList(String chatId);
}
