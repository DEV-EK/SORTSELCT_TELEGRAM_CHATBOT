package com.coolegrams.coolegrams.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.coolegrams.coolegrams.dto.MessageforRefreshDto;
import com.coolegrams.coolegrams.dto.UserinfoForMessageDto;

@Mapper
public interface AlarmMessagingMapper {

	public MessageforRefreshDto[] getBatchTarget();

	public UserinfoForMessageDto[] getSubscribeUser();

	public void updateIFYN(int dtoSeq);

	public int getBatchTargetPrice(String productId, String productName);

	public int getBatchPossible(String productId);

	public int getInputTimePrice(int userSeq);

}
