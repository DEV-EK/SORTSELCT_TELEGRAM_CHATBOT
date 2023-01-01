package com.coolegrams.coolegrams.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshItemInfoDto {
	private String productName;
	private String productId;
	private String coupangLink;
	private int currentPrice;
}
