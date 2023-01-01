package com.coolegrams.coolegrams.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageforRefreshDto {
	private int dtoSeq;
	private String productName;
	private String productId;
	private int currentPrice;
}
