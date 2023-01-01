package com.coolegrams.coolegrams.telegrambot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.coolegrams.coolegrams.SearchTasks;
import com.coolegrams.coolegrams.dto.CoupangItemDto;
import com.coolegrams.coolegrams.dto.TelegramUserInfoDto;
import com.coolegrams.coolegrams.service.TelegramUserInfoService;
import com.coolegrams.coolegrams.serviceImpl.CallCoupangApiServiceImpl;
import com.coolegrams.coolegrams.serviceImpl.ItemRefreshServiceImpl;
import com.coolegrams.coolegrams.serviceImpl.ItemSelectApiServiceImpl;
import com.coolegrams.coolegrams.serviceImpl.TelegramUserInfoServiceImpl;
import com.fasterxml.jackson.databind.ser.std.ArraySerializerBase;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Telegrambot extends TelegramLongPollingBot{
	@Autowired
	private SearchTasks searchTasks;
	@Autowired
	private ItemRefreshServiceImpl itemRefreshService;
	@Autowired
	private ItemSelectApiServiceImpl itemSelectApiService;
	@Autowired
	private TelegramUserInfoServiceImpl userInfoService;
	@Autowired
	private CallCoupangApiServiceImpl callCoupangApiserviceCall;
	//회원 별 진행 단계 저장
	private HashMap<Long, HashMap<String,Object>> userState= new HashMap<Long, HashMap<String,Object>>();
	//가격 설정 시 다음 메세지와 연결고리를 만들 기 위한 hashmap
	private HashMap<String,String> itemPriceMaker = new HashMap<>();
	
	private DecimalFormat formatter= new DecimalFormat("###,###");
	
/*	
	@PostConstruct
	public void telegrambotInit() {
		try {
			TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
			api.registerBot(telegramBot);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
*/	
	@Override
	public void onUpdateReceived(Update update) {
		String getTodayLink = itemSelectApiService.getTodayLink();
        Message message = update.getMessage();
        System.out.println(message.toString());
        log.debug(message.toString(),"DEBUG");
        if (update.hasMessage() && update.getMessage().hasText()) {
        	SendMessage sendMessage = new SendMessage();
        	Long tempChatId = update.getMessage().getChatId();
            sendMessage.setChatId(tempChatId.toString());
            //처음 들어왔을 때
            if( update.getMessage().getText().equals("/start") ){
            	if(userState.containsKey(tempChatId)) {
            		userState.remove(tempChatId);
            		HashMap<String, Object> tempState = new HashMap<>();
                    tempState.put("depth", 0);
                    tempState.put("item", "");
                    userState.put(tempChatId, tempState);
            	} else {
            		HashMap<String, Object> tempState = new HashMap<>();
                    tempState.put("depth", 0);
                    tempState.put("item", "");
                    userState.put(tempChatId, tempState);
            	}
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                commonMenu(replyKeyboardMarkup);
                replyKeyboardMarkup.setResizeKeyboard(true);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText("🔔솔트셀렉 알람 서비스 입니다\n쿠팡은 Dynamic Pricing 통해\n상품 가격을 조정합니다.\n구매하고 싶은 쿠팡 상품의 가격을 설정하세요✍️\n⚡목표가 이하⚡\n⚡최저가에⚡\n도달하면 솔트셀렉 봇이 알려드려요!\n\n⬇️키보드 아래 메뉴를 이용해 주세요");
                try {
                	System.out.println("전송");
                    sendMessage.setChatId(update.getMessage().getChatId());
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }  
            }
            /* 메뉴 선택 시작 */
            //1. 상품 등록 선택
            else if(update.getMessage().getText().equals("상품 알람 등록📲") && 0==(int)userState.get(tempChatId).get("depth")) {
            	if(userInfoService.userAlarmCount(String.valueOf(tempChatId))<10) {
            
            	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                List<KeyboardRow> keyboardRowList = new ArrayList<>();
                KeyboardRow row;

                row=new KeyboardRow();
                row.add("/메인으로 돌아가기🏠");
                keyboardRowList.add(row);
                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setKeyboard(keyboardRowList);
            	sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText("🔔알람을 받고싶은🔔\n상품명 or 키워드 or 상품 번호 or 링크를\n\n⬇️아래 키보드 혹은 키패드를⌨️\n이용해 입력해 주세요~");
                try {
                    sendMessage.setChatId(update.getMessage().getChatId());
                    execute(sendMessage);
                	userState.get(tempChatId).remove("depth");
                	userState.get(tempChatId).put("depth", 1);
                	userState.get(tempChatId).put("item", "itemSearch");
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                } // 경우의 수가 두개 생김. depth==1 이면서, text가 메인으로 돌아가기 / depth ==1 이면서, "메인으로 돌아가기가 아닌 것"
            	} else {
            		SendMessage sd = new SendMessage();
            		sd.setChatId(tempChatId);
            		sd.setText("알람은 최대 10개까지 등록 가능합니다.\n기존 알람은 알람 리스트에서 삭제할 수 있습니다.\n메인메뉴로 돌아갑니다.");
                    
            		try {
						execute(sd);
						SendMessage sd2 = new SendMessage();
	            		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
	                    commonMenu(replyKeyboardMarkup);
	                    replyKeyboardMarkup.setResizeKeyboard(true);
	                    sd2.setReplyMarkup(replyKeyboardMarkup);
	                    sd2.setChatId(tempChatId);
	                    sd2.setText("🔔솔트셀렉 알람 서비스 입니다\n쿠팡은 Dynamic Pricing 통해\n상품 가격을 조정합니다.\n구매하고 싶은 쿠팡 상품의 가격을 설정하세요✍️\n⚡목표가 이하⚡\n⚡최저가에⚡\n도달하면 솔트셀렉 봇이 알려드려요!\n\n⬇️키보드 아래 메뉴를 이용해 주세요");
	                    try {
							execute(sd2);
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
            	}
            }
            // 상품 검색에서 메인으로 가기 선택
            else if(update.getMessage().getText().equals("/메인으로 돌아가기🏠") && 1==(int)userState.get(tempChatId).get("depth") && "itemSearch".equals(userState.get(tempChatId).get("item"))){
            	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                commonMenu(replyKeyboardMarkup);
                replyKeyboardMarkup.setResizeKeyboard(true);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText("🔔솔트셀렉 알람 서비스 입니다\n쿠팡은 Dynamic Pricing 통해\n상품 가격을 조정합니다.\n구매하고 싶은 쿠팡 상품의 가격을 설정하세요✍️\n⚡목표가 이하⚡\n⚡최저가에⚡\n도달하면 솔트셀렉 봇이 알려드려요!\n\n⬇️키보드 아래 메뉴를 이용해 주세요");
                try {
                    sendMessage.setChatId(update.getMessage().getChatId());
                    execute(sendMessage);
                    HashMap<String, Object> tempState = new HashMap<>();
                    userState.remove(tempChatId);
                    tempState.put("depth", 0);
                    tempState.put("item", "");
                    userState.put(tempChatId, tempState);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }  
            }
            // 상품 검색에서 상품 or 키워드를 검색
            else if(!update.getMessage().getText().equals("/메인으로 돌아가기🏠") && 1==(int)userState.get(tempChatId).get("depth") && "itemSearch".equals(userState.get(tempChatId).get("item"))) {
            	String inputData = update.getMessage().getText();
            	SendMessage secondMessage = new SendMessage();
            	secondMessage.setText("\"" + inputData +"\""+ " 검색하겠습니다! 잠시만 기다려주세요...");
            	secondMessage.setChatId(update.getMessage().getChatId());
                try {
					execute(secondMessage);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
                /* 크롤링 검색 */
                CoupangItemDto[] ItemDto;
                boolean linkOrKeyword; // 링크검색인지, 키워드검색인지 판단
                if(inputData.contains("https://www.coupang.com/vp/products/")||inputData.contains("https://coupang.com/vp/products/")) {
                	String[] tempfirstUrl= inputData.split("products/");
                	String[] tempSecondUrl = tempfirstUrl[1].split("\\?");
                	inputData=tempSecondUrl[0];
                }
                	ItemDto = searchTasks.searchProcess(inputData);
                	linkOrKeyword=false;
                
                if(ItemDto.length == 0) {
                	SendMessage thirdMessage = new SendMessage();
                	if(linkOrKeyword)
                		thirdMessage.setText("링크가 형식에 맞지 않습니다! \n메인메뉴로 돌아갑니다~");
                	else {
                		thirdMessage.setText("검색어가 형식에 맞지 않습니다! \n메인메뉴로 돌아갑니다~");
                	}
                	thirdMessage.setChatId(update.getMessage().getChatId());
                	
                	/**/
                	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    commonMenu(replyKeyboardMarkup);
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    commonMenu(replyKeyboardMarkup);
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    thirdMessage.setReplyMarkup(replyKeyboardMarkup);
                 	userState.get(tempChatId).remove("depth");
                	userState.get(tempChatId).remove("item");
                	userState.get(tempChatId).put("depth", 0);
                	userState.get(tempChatId).put("item", "");
                	try {
						execute(thirdMessage);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
               		
                } else {
                	
                	SendMessage thirdMessage = new SendMessage();
                	thirdMessage.setText("⬇️검색 결과입니다!⬇️\n");
                	thirdMessage.setChatId(update.getMessage().getChatId());
                    try {
    					execute(thirdMessage);
    				} catch (TelegramApiException e) {
    					e.printStackTrace();
    				}
                    SendMessage forthMessage = new SendMessage();
                	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                    List<KeyboardRow> keyboardRowList = new ArrayList<>();
                    KeyboardRow row;
                    row=new KeyboardRow();
                    
                    row.add("/메인으로 돌아가기🏠");
                    keyboardRowList.add(row);
                    for(int i =0 ; i< ItemDto.length ; i++) {
                    	row=new KeyboardRow();
                    	String temp = ItemDto[i].getProductPrice().replaceAll(",", "");
                        row.add(ItemDto[i].getProductName()+"[💰"+formatter.format(Integer.parseInt(temp))+"원"+"]");
                        keyboardRowList.add(row);
                    }
                    replyKeyboardMarkup.setResizeKeyboard(true);
                    replyKeyboardMarkup.setKeyboard(keyboardRowList);
                    forthMessage.setReplyMarkup(replyKeyboardMarkup);
                    forthMessage.setChatId(update.getMessage().getChatId());
                    forthMessage.setText("알람 받기를 원하는 제품의 버튼을 클릭해 주세요!");
                    try {
                    	userState.get(tempChatId).remove("depth");
                    	userState.get(tempChatId).remove("item");
                    	userState.get(tempChatId).put("depth", 2);
                    	userState.get(tempChatId).put("item", "itemSearch");
    					execute(forthMessage);
    				} catch (TelegramApiException e) {
    					e.printStackTrace();
    				}
                }
            }
            else if(!update.getMessage().getText().equals("/메인으로 돌아가기🏠")&& 2==(int)userState.get(tempChatId).get("depth") && "itemSearch".equals(userState.get(tempChatId).get("item"))) {
            	String selectedItem = update.getMessage().getText();
            	SendMessage secondMessage = new SendMessage();
            	secondMessage.setChatId(update.getMessage().getChatId());
            	secondMessage.setText("키보드 혹은 키 패드를 사용하여 가격을\n'숫자로만' 입력해 주세요!\n\nex) 19000\n⬆️(원 단위)");
            	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                List<KeyboardRow> keyboardRowList = new ArrayList<>();
                KeyboardRow row;
                row=new KeyboardRow();
                row.add("/메인으로 돌아가기🏠");
                keyboardRowList.add(row);
                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setKeyboard(keyboardRowList);
                secondMessage.setReplyMarkup(replyKeyboardMarkup);
                
            	userState.get(tempChatId).remove("depth");
            	userState.get(tempChatId).remove("item");
            	userState.get(tempChatId).put("depth", 3);
            	userState.get(tempChatId).put("item", "itemSearch");
            	itemPriceMaker.put(String.valueOf(tempChatId)+"3"+"itemSearch", selectedItem);
            	try {
					execute(secondMessage);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
            	
            }
            else if(update.getMessage().getText().equals("/메인으로 돌아가기🏠") && 2==(int)userState.get(tempChatId).get("depth") && "itemSearch".equals(userState.get(tempChatId).get("item"))){
            	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                commonMenu(replyKeyboardMarkup);
                replyKeyboardMarkup.setResizeKeyboard(true);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText("🔔솔트셀렉 알람 서비스 입니다\n쿠팡은 Dynamic Pricing 통해\n상품 가격을 조정합니다.\n구매하고 싶은 쿠팡 상품의 가격을 설정하세요✍️\n⚡목표가 이하⚡\n⚡최저가에⚡\n도달하면 솔트셀렉 봇이 알려드려요!\n\n⬇️키보드 아래 메뉴를 이용해 주세요");
                try {
                	HashMap<String, Object> tempState = new HashMap<>();
                    userState.remove(tempChatId);
                    tempState.put("depth", 0);
                    tempState.put("item", "");
                    userState.put(tempChatId, tempState);
                    sendMessage.setChatId(update.getMessage().getChatId());
                    execute(sendMessage);
                    

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }  
            }
            else if(update.getMessage().getText().equals("/메인으로 돌아가기🏠") && 2==(int)userState.get(tempChatId).get("depth") && "itemSearch".equals(userState.get(tempChatId).get("item"))){
            	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                commonMenu(replyKeyboardMarkup);
                replyKeyboardMarkup.setResizeKeyboard(true);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText("🔔솔트셀렉 알람 서비스 입니다\n쿠팡은 Dynamic Pricing 통해\n상품 가격을 조정합니다.\n구매하고 싶은 쿠팡 상품의 가격을 설정하세요✍️\n⚡목표가 이하⚡\n⚡최저가에⚡\n도달하면 솔트셀렉 봇이 알려드려요!\n\n⬇️키보드 아래 메뉴를 이용해 주세요");
                try {
                    sendMessage.setChatId(update.getMessage().getChatId());
                    HashMap<String, Object> tempState = new HashMap<>();
                    userState.remove(tempChatId);
                    tempState.put("depth", 0);
                    tempState.put("item", "");
                    userState.put(tempChatId, tempState);
                    execute(sendMessage);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }  
            }


            else if(!update.getMessage().getText().equals("/메인으로 돌아가기🏠")&& 3==(int)userState.get(tempChatId).get("depth") && "itemSearch".equals(userState.get(tempChatId).get("item"))) {
            		SendMessage secondMessage = new SendMessage();
            		String userId = String.valueOf(update.getMessage().getChatId());
                 	String selectedItem;
                 	String tempMessage = update.getMessage().getText();
                 	String validInputpattern ="^[0-9]*$";
                 	String[] selectedItemSplit;
                 	tempMessage=tempMessage.replaceAll(" ","");
                 	tempMessage=tempMessage.replaceAll("원","");
                 	String getStringcurrentPrice="";
                 	String tempSelectedItem="";
                 	String[] tempSelectedItemSplit;
                 	if(itemPriceMaker.containsKey(userId+"3"+"itemSearch")) {
                 		tempSelectedItem = itemPriceMaker.get(userId+"3"+"itemSearch");
                 		tempSelectedItemSplit= tempSelectedItem.split("\\[💰");
                 		getStringcurrentPrice = tempSelectedItemSplit[1].replaceAll("\\]", "").replaceAll(",","").replaceAll("원","");
                 		System.out.println(getStringcurrentPrice+"@@@@@@@@@@@@@@@@@@@@@");
                 	}

                 	if(!Pattern.matches("^[0-9]*$",tempMessage)|| tempMessage.length()>11 ||Integer.parseInt(getStringcurrentPrice) <= Integer.parseInt(tempMessage)) {
                 		if(Pattern.matches("^[0-9]*$",tempMessage) && Integer.parseInt(tempMessage)<0) {
	                 		secondMessage.setChatId(tempChatId);
	                     	secondMessage.setText("잘못 된 입력입니다.\n메인 메뉴로 돌아갑니다.");
                 		}
                 		else if(!Pattern.matches("^[0-9]*$",tempMessage)) {
                 			secondMessage.setChatId(tempChatId);
	                     	secondMessage.setText("잘못 된 입력입니다.\n메인 메뉴로 돌아갑니다.");
                 		}
                 		else {
                 			if(Integer.parseInt(getStringcurrentPrice) <= Integer.parseInt(tempMessage)) {
                 				secondMessage.setChatId(tempChatId);
    	                     	secondMessage.setText("목표가는 현재가 보다 낮게 설정해야 합니다.\n메인 메뉴로 돌아갑니다.");
                 			}
                 			else {
                 				secondMessage.setChatId(tempChatId);
                         		secondMessage.setText("잘못 된 입력입니다.\n메인 메뉴로 돌아갑니다.");
                 			}
                 		}	
                 		
	                 	try {
                 			HashMap<String, Object> tempState = new HashMap<>();
                 			userState.remove(tempChatId);
                 			tempState.put("depth", 0);
                 			tempState.put("item", "");
                 			userState.put(tempChatId, tempState);
								execute(secondMessage);
							} catch (TelegramApiException e) {
								e.printStackTrace();
							}
                 		
                 		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                 		commonMenu(replyKeyboardMarkup);
                 		SendMessage backMessage = new SendMessage();
                 		backMessage.setReplyMarkup(replyKeyboardMarkup);
                        backMessage.setText("🔔솔트셀렉 알람 서비스 입니다\n쿠팡은 Dynamic Pricing 통해\n상품 가격을 조정합니다.\n구매하고 싶은 쿠팡 상품의 가격을 설정하세요✍️\n⚡목표가 이하⚡\n⚡최저가에⚡\n도달하면 솔트셀렉 봇이 알려드려요!\n\n⬇️키보드 아래 메뉴를 이용해 주세요");
                 		try {
                 			backMessage.setChatId(update.getMessage().getChatId());
                 			HashMap<String, Object> tempState = new HashMap<>();
                 			userState.remove(tempChatId);
                 			tempState.put("depth", 0);
                 			tempState.put("item", "");
                 			userState.put(tempChatId, tempState);
                 			execute(backMessage);
                 			return ;
	                    } catch (TelegramApiException e) {
	                        e.printStackTrace();
	                    }  

                 	}
                 	String getcurrentPrice = "";
                 	if(itemPriceMaker.containsKey(userId+"3"+"itemSearch")) {
                 		selectedItem = itemPriceMaker.get(userId+"3"+"itemSearch");
                 		selectedItemSplit= selectedItem.split("\\[💰");
                 		getcurrentPrice = "[💰"+selectedItemSplit[1];
                 	}
                 	else {
                 		secondMessage.setChatId(tempChatId);
                 		secondMessage.setText("잘못 된 입력입니다. 메인 메뉴로 돌아갑니다.");
                 		
                 		userState.get(tempChatId).remove("depth");
                    	userState.get(tempChatId).remove("item");
                    	userState.get(tempChatId).put("depth", 0);
                    	userState.get(tempChatId).put("item", "");
                    	
                    	try {
							execute(secondMessage);
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}
                    	SendMessage mainBackMessage = new SendMessage();
                    	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                        commonMenu(replyKeyboardMarkup);
                        replyKeyboardMarkup.setResizeKeyboard(true);
                        mainBackMessage.setReplyMarkup(replyKeyboardMarkup);
                        mainBackMessage.setText("🔔솔트셀렉 알람 서비스 입니다\n쿠팡은 Dynamic Pricing 통해\n상품 가격을 조정합니다.\n구매하고 싶은 쿠팡 상품의 가격을 설정하세요✍️\n⚡목표가 이하⚡\n⚡최저가에⚡\n도달하면 솔트셀렉 봇이 알려드려요!\n\n⬇️키보드 아래 메뉴를 이용해 주세요");
                        mainBackMessage.setChatId(update.getMessage().getChatId());
                        HashMap<String, Object> tempState = new HashMap<>();
                        userState.remove(tempChatId);
                        tempState.put("depth", 0);
                        tempState.put("item", "");
                        userState.put(tempChatId, tempState);
                        try {
							execute(mainBackMessage);
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}
                    	
                    	return;
                 	}
                 	String productId = itemRefreshService.getProductIdbyProductName(selectedItemSplit[0]);
                 	int currentPrice = itemRefreshService.getProductIdbyCurrentPrice(productId);
                 	String deepLink = callCoupangApiserviceCall.callCoupangItemInfoAPI(productId);
                 	
                 	userInfoService.addUserInfo(new TelegramUserInfoDto(userId,productId,selectedItemSplit[0],currentPrice,Long.parseLong(tempMessage),deepLink));            	
                 	
                 	secondMessage.setChatId(tempChatId);
                 	secondMessage.setText("'"+selectedItemSplit[0]+"'\n"+"현재가 : "+getcurrentPrice+"\n목표가 : [💰"+formatter.format(Long.parseLong(tempMessage))+"원]\n 등록 완료!\n목표가 도달 시 알람 드릴게용~\n메인 메뉴로 돌아갑니다!");
                 	try {                    
						execute(secondMessage);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
             		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
             		commonMenu(replyKeyboardMarkup);
             		SendMessage backMessage = new SendMessage();
             		backMessage.setReplyMarkup(replyKeyboardMarkup);
                    backMessage.setText("🔔솔트셀렉 알람 서비스 입니다\n쿠팡은 Dynamic Pricing 통해\n상품 가격을 조정합니다.\n구매하고 싶은 쿠팡 상품의 가격을 설정하세요✍️\n⚡목표가 이하⚡\n⚡최저가에⚡\n도달하면 솔트셀렉 봇이 알려드려요!\n\n⬇️키보드 아래 메뉴를 이용해 주세요");
             		try {
             			backMessage.setChatId(update.getMessage().getChatId());
             			HashMap<String, Object> tempState = new HashMap<>();
             			userState.remove(tempChatId);
             			tempState.put("depth", 0);
             			tempState.put("item", "");
             			userState.put(tempChatId, tempState);
             			execute(backMessage);	
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                } 

            }
            else if(update.getMessage().getText().equals("/메인으로 돌아가기🏠") && 3==(int)userState.get(tempChatId).get("depth") && "itemSearch".equals(userState.get(tempChatId).get("item"))){
            	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                commonMenu(replyKeyboardMarkup);
                replyKeyboardMarkup.setResizeKeyboard(true);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText("🔔솔트셀렉 알람 서비스 입니다\n쿠팡은 Dynamic Pricing 통해\n상품 가격을 조정합니다.\n구매하고 싶은 쿠팡 상품의 가격을 설정하세요✍️\n⚡목표가 이하⚡\n⚡최저가에⚡\n도달하면 솔트셀렉 봇이 알려드려요!\n\n⬇️키보드 아래 메뉴를 이용해 주세요");
                try {
                    sendMessage.setChatId(update.getMessage().getChatId());
                    HashMap<String, Object> tempState = new HashMap<>();
                    userState.remove(tempChatId);
                    tempState.put("depth", 0);
                    tempState.put("item", "");
                    userState.put(tempChatId, tempState);
                    execute(sendMessage);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }  
            }
            /**
             * 
             */
            
            else if(update.getMessage().getText().equals("등록 상품 목록📝") && 0==(int)userState.get(tempChatId).get("depth")) {
            	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            	List<KeyboardRow> keyboardRowList = new ArrayList<>();
                
                KeyboardRow row;
                System.out.println(tempChatId);
                TelegramUserInfoDto[] listDto =  userInfoService.getSubscribeList(String.valueOf(tempChatId));
                row = new KeyboardRow();
                row.add("/메인으로 돌아가기🏠");
                row.add("/알람 삭제🔕");
                keyboardRowList.add(row);
                for(int i = 0 ; i < listDto.length ; i++ ) {
                	row=new KeyboardRow();
                	row.add(listDto[i].getSubscribeProductName()+"[💰 목표가 : "+listDto[i].getGoalPrice()+"원]");
                	keyboardRowList.add(row);
                }

                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setKeyboard(keyboardRowList);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText("알람 목록을 조회합니다.");
                try {
                    HashMap<String, Object> tempState = new HashMap<>();
                	userState.remove(tempChatId);
                    tempState.put("depth", 1);
                    tempState.put("item", "itemList");
                    userState.put(tempChatId, tempState);
         
					execute(sendMessage);
				} catch (TelegramApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            else if(update.getMessage().getText().equals("/알람 삭제🔕") && 1==(int)userState.get(tempChatId).get("depth") && "itemList".equals(userState.get(tempChatId).get("item"))) {
            	SendMessage deleteMessage = new SendMessage();
            	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            	List<KeyboardRow> keyboardRowList = new ArrayList<>();
                
                KeyboardRow row;
                TelegramUserInfoDto[] listDto =  userInfoService.getSubscribeList(String.valueOf(tempChatId));
                row = new KeyboardRow();
                row.add("/메인으로 돌아가기🏠");
                keyboardRowList.add(row);
                for(int i = 0 ; i < listDto.length ; i++ ) {
                	row=new KeyboardRow();
                	row.add(listDto[i].getSubscribeProductName()+" ❌");
                	keyboardRowList.add(row);
                }

                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setKeyboard(keyboardRowList);
                deleteMessage.setText("버튼을 누르면, 해당 상품이 목록에서 삭제 됩니다!");
                deleteMessage.setReplyMarkup(replyKeyboardMarkup);
                deleteMessage.setChatId(tempChatId);
                try {
                    HashMap<String, Object> tempState = new HashMap<>();
                	userState.remove(tempChatId);
                    tempState.put("depth", 2);
                    tempState.put("item", "itemList");
                    userState.put(tempChatId, tempState);
                	execute(deleteMessage);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
            }
            else if(update.getMessage().getText().equals("/메인으로 돌아가기🏠") && 1==(int)userState.get(tempChatId).get("depth") && "itemList".equals(userState.get(tempChatId).get("item"))) {
            	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                commonMenu(replyKeyboardMarkup);
                replyKeyboardMarkup.setResizeKeyboard(true);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText("🔔솔트셀렉 알람 서비스 입니다\n쿠팡은 Dynamic Pricing 통해\n상품 가격을 조정합니다.\n구매하고 싶은 쿠팡 상품의 가격을 설정하세요✍️\n⚡목표가 이하⚡\n⚡최저가에⚡\n도달하면 솔트셀렉 봇이 알려드려요!\n\n⬇️키보드 아래 메뉴를 이용해 주세요");
                try {
                    sendMessage.setChatId(update.getMessage().getChatId());
                    HashMap<String, Object> tempState = new HashMap<>();
                    userState.remove(tempChatId);
                    tempState.put("depth", 0);
                    tempState.put("item", "");
                    userState.put(tempChatId, tempState);
                    execute(sendMessage);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }  

            }
            else if(2==(int)userState.get(tempChatId).get("depth") && "itemList".equals(userState.get(tempChatId).get("item"))) {
            	String tempText = update.getMessage().getText();
            	if("❌".equals(String.valueOf(tempText.charAt(tempText.length()-1))) ) {
                	SendMessage deleteCompleteMessage = new SendMessage();
            		tempText = tempText.substring(0, tempText.length()-2);
            		System.out.println(tempText);
                	itemSelectApiService.changeUseYn(tempText);            		
                	
                	deleteCompleteMessage.setText("삭제 완료! 메인 메뉴로 돌아갑니다~");
                	deleteCompleteMessage.setChatId(tempChatId);
                	try {
						execute(deleteCompleteMessage);
	            		SendMessage mainBackMessage = new SendMessage();
	                	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
	                    commonMenu(replyKeyboardMarkup);
	                    replyKeyboardMarkup.setResizeKeyboard(true);
	                    mainBackMessage.setReplyMarkup(replyKeyboardMarkup);
	                    mainBackMessage.setText("🔔솔트셀렉 알람 서비스 입니다\n쿠팡은 Dynamic Pricing 통해\n상품 가격을 조정합니다.\n구매하고 싶은 쿠팡 상품의 가격을 설정하세요✍️\n⚡목표가 이하⚡\n⚡최저가에⚡\n도달하면 솔트셀렉 봇이 알려드려요!\n\n⬇️키보드 아래 메뉴를 이용해 주세요");
	                    mainBackMessage.setChatId(update.getMessage().getChatId());
	                    HashMap<String, Object> tempState = new HashMap<>();
	                    userState.remove(tempChatId);
	                    tempState.put("depth", 0);
	                    tempState.put("item", "");
	                    userState.put(tempChatId, tempState);
	                    try {
							execute(mainBackMessage);
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
            	}
            	else if("/메인으로 돌아가기🏠".equals(tempText)) {
            		SendMessage deleteFailMessage = new SendMessage();
            		deleteFailMessage.setText("메인 메뉴로 돌아갑니다!");
            		deleteFailMessage.setChatId(tempChatId);
            		try {
						execute(deleteFailMessage);
	            		SendMessage mainBackMessage = new SendMessage();
	                	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
	                    commonMenu(replyKeyboardMarkup);
	                    replyKeyboardMarkup.setResizeKeyboard(true);
	                    mainBackMessage.setReplyMarkup(replyKeyboardMarkup);
	                    mainBackMessage.setText("🔔솔트셀렉 알람 서비스 입니다\n쿠팡은 Dynamic Pricing 통해\n상품 가격을 조정합니다.\n구매하고 싶은 쿠팡 상품의 가격을 설정하세요✍️\n⚡목표가 이하⚡\n⚡최저가에⚡\n도달하면 솔트셀렉 봇이 알려드려요!\n\n⬇️키보드 아래 메뉴를 이용해 주세요");
	                    mainBackMessage.setChatId(update.getMessage().getChatId());
	                    HashMap<String, Object> tempState = new HashMap<>();
	                    userState.remove(tempChatId);
	                    tempState.put("depth", 0);
	                    tempState.put("item", "");
	                    userState.put(tempChatId, tempState);
	                    try {
							execute(mainBackMessage);
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
            	}
            	else {
            		SendMessage deleteFailMessage = new SendMessage();
            		deleteFailMessage.setText("잘못된 입력입니다... 메인 메뉴로 돌아갑니다.");
            		deleteFailMessage.setChatId(tempChatId);
            		try {
						execute(deleteFailMessage);
	            		SendMessage mainBackMessage = new SendMessage();
	                	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
	                    commonMenu(replyKeyboardMarkup);
	                    replyKeyboardMarkup.setResizeKeyboard(true);
	                    mainBackMessage.setReplyMarkup(replyKeyboardMarkup);
	                    mainBackMessage.setText("🔔솔트셀렉 알람 서비스 입니다\n쿠팡은 Dynamic Pricing 통해\n상품 가격을 조정합니다.\n구매하고 싶은 쿠팡 상품의 가격을 설정하세요✍️\n⚡목표가 이하⚡\n⚡최저가에⚡\n도달하면 솔트셀렉 봇이 알려드려요!\n\n⬇️키보드 아래 메뉴를 이용해 주세요");
	                    mainBackMessage.setChatId(update.getMessage().getChatId());
	                    HashMap<String, Object> tempState = new HashMap<>();
	                    userState.remove(tempChatId);
	                    tempState.put("depth", 0);
	                    tempState.put("item", "");
	                    userState.put(tempChatId, tempState);
	                    try {
							execute(mainBackMessage);
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}

            		
            		
            	}
            }
            else if(update.getMessage().getText().equals("ExitbyEk"))
            	System.exit(0);
            /**
             * 
             * 
             */
            else {
            	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                commonMenu(replyKeyboardMarkup);
                replyKeyboardMarkup.setResizeKeyboard(true);
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                sendMessage.setText("🔔솔트셀렉 알람 서비스 입니다\n쿠팡은 Dynamic Pricing 통해\n상품 가격을 조정합니다.\n구매하고 싶은 쿠팡 상품의 가격을 설정하세요✍️\n⚡목표가 이하⚡\n⚡최저가에⚡\n도달하면 솔트셀렉 봇이 알려드려요!\n\n⬇️키보드 아래 메뉴를 이용해 주세요");
                try {
                    sendMessage.setChatId(update.getMessage().getChatId());
                    HashMap<String, Object> tempState = new HashMap<>();
                    userState.remove(tempChatId);
                    tempState.put("depth", 0);
                    tempState.put("item", "");
                    userState.put(tempChatId, tempState);
                    execute(sendMessage);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }  
            	
            }
        }
}
        
	

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return "coulegram_bot";
	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return "5813468957:AAFRZYeIWDUZL0drmkCEtT0yeihsGtJwNVI";
	}
	
	public void commonMenu(ReplyKeyboardMarkup replyKeyboardMarkup) {  
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        
        KeyboardRow row;

        row=new KeyboardRow();
        row.add("상품 알람 등록📲");
        keyboardRowList.add(row);

        row=new KeyboardRow();
        row.add("등록 상품 목록📝");
        keyboardRowList.add(row);

        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
	}
	

	public void sendAlarmMessage(String chatId, int goalPrice, int currentPrice, String coupangUrl,String productName, int inputTimePrice) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatId);
		sendMessage.setText("🔔 등록하신 상품의 가격이 목표가에 도달했습니다.\n\n🎁 [상품명]: "+productName+"\n\n📈 가격 변동\n목표가 : ["+formatter.format(goalPrice)+ "원] \n"+"등록가 : ["+formatter.format(inputTimePrice)+"원]\n"+"현재가 : ["+formatter.format(currentPrice)+"원]("+(inputTimePrice-currentPrice)+"원 ⬇️)"+"\n\n❗ 리워드 링크 안내\n쿠팡 파트너스 활동을 통해 일정액의\n수수료를 제공받을 수 있습니다.\n발생한 수익은 시스템 운영비용에 사용되니\n많은 이용 부탁드립니다.\n\n"+"➡[구매 링크 바로가기]\n"+coupangUrl);
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
