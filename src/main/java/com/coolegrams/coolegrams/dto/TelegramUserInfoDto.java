package com.coolegrams.coolegrams.dto;

import org.springframework.context.annotation.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUserInfoDto {
	private String chatId;
	private String subscribeProductId;
	private String subscribeProductName;
	private long inputTimePrice;
	private long goalPrice;
	private String coupangUrl;
}
