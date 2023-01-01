package com.coolegrams.coolegrams.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.coolegrams.coolegrams.dto.TelegramUserInfoDto;

@Mapper
public interface TelegramUserInfoMapper {
	public void addUserInfo(TelegramUserInfoDto dto);
	public int userAlarmCount(String chatId);
	public TelegramUserInfoDto[] getSubscribeList(String chatId);
}
