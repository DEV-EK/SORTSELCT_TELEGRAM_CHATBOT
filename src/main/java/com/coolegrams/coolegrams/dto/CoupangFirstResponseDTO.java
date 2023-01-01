package com.coolegrams.coolegrams.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoupangFirstResponseDTO {
	private String rCode;
	private String rMessage;
	private String data;
}
