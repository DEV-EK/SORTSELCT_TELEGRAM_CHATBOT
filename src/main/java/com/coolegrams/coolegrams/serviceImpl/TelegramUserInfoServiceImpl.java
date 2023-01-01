package com.coolegrams.coolegrams.serviceImpl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coolegrams.coolegrams.dto.TelegramUserInfoDto;
import com.coolegrams.coolegrams.mapper.TelegramUserInfoMapper;
import com.coolegrams.coolegrams.service.TelegramUserInfoService;

@Service
public class TelegramUserInfoServiceImpl implements TelegramUserInfoService{
	@Autowired
	private TelegramUserInfoMapper userInfoMapper;
	
	@Override
	public void addUserInfo(TelegramUserInfoDto dto) {
		userInfoMapper.addUserInfo(dto);
	}

	@Override
	public int userAlarmCount(String chatId) {
		return userInfoMapper.userAlarmCount(chatId);
	}

	@Override
	public TelegramUserInfoDto[] getSubscribeList(String chatId) {
		System.out.println(Arrays.toString(userInfoMapper.getSubscribeList(chatId))+"@@@@@@@@@@@@");
		return userInfoMapper.getSubscribeList(chatId);
	}
	
}
