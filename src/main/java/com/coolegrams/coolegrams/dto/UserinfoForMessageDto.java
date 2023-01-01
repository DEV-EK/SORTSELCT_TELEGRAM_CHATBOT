package com.coolegrams.coolegrams.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//SELECT CHAT_ID, GOAL_PRICE from user_info_hist WHERE SUBSCRIBE_ID=#{vo.getProductID} AND USE_YN ='Y';


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserinfoForMessageDto {
	private int userSeq;
	private String chatId;
	private String productName;
	private String productId;
	private int goalPrice;
	private String coupangUrl;
}
