package com.coolegrams.coolegrams.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoupangItemDto {
	private String productImage; //이미지 링크
	private String productId; // 프로덕트 id
	private String rank; // 순위
	private String coupangLink; // 제품 세부url
	private String keyword; // 검색 키워드
	private String categoryName; //카테고리명
	private String productName; //상품명
	private String productPrice; //상품가격
	private String isRocket; //로켓배송여부
}
