
package com.coolegrams.coolegrams.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import com.coolegrams.coolegrams.dto.CoupangFirstResponseDTO;
import com.coolegrams.coolegrams.dto.CoupangItemDto;
import com.coolegrams.coolegrams.mapper.CallCoupangApiMapper;
import com.coolegrams.coolegrams.service.CallCoupangApiService;
import com.coupang.openapi.sdk.Hmac;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CallCoupangApiServiceImpl implements CallCoupangApiService {
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	CallCoupangApiMapper callCoupangApiMapper;
	private static final String HOST = "api-gateway.coupang.com";
	private static final int PORT = 443;
	private static final String SCHEMA = "https";
	// replace with your own accessKey
	private static final String ACCESS_KEY = "42e6ff5f-4075-4638-a1c0-2925fa51376a";
	// replace with your own secretKey
	private static final String SECRET_KEY = "444df798827224b541eba9452b18496042c4215c";

	// v2/providers/affiliate_open_api/apis/openapi/v1/
	@Override
	public String callCoupangItemInfoAPI(String id) {
		String returnValue = "";
		// params
		
		String method = "POST";
		// replace with your own vendorId
		// String path =
		// "/v2/providers/affiliate_open_api/apis/openapi/v1/products/bestcategories/1002";//{sellerProductId}
		String path = "/v2/providers/affiliate_open_api/apis/openapi/v1/deeplink";
		String strjson = "{\"coupangUrls\": [\"https://www.coupang.com/vp/products/" + id + "\"]}";
		System.out.println(strjson);
		CloseableHttpClient client = null;
		try {
			// create client
			client = HttpClients.createDefault();
			// build uri
			URIBuilder uriBuilder = new URIBuilder().setPath(path);

			/********************************************************/
			// authorize, demonstrate how to generate hmac signature here
			String authorization = Hmac.generate(method, uriBuilder.build().toString(), SECRET_KEY, ACCESS_KEY);
			// print out the hmac key
			System.out.println(authorization);
			/********************************************************/

			uriBuilder.setScheme(SCHEMA).setHost(HOST).setPort(PORT);
			HttpPost requestPost = new HttpPost(uriBuilder.build().toString());

			StringEntity params = new StringEntity(strjson, "UTF-8");

			/********************************************************/
			// set header, demonstarte how to use hmac signature here
			requestPost.addHeader("Authorization", authorization);
			/********************************************************/
			requestPost.addHeader("content-type", "application/json");
			requestPost.setEntity(params);
			CloseableHttpResponse response = null;
			JSONParser jsonParser = new JSONParser();
			try {
				// execute post request
				response = client.execute(requestPost);
				// print result
				System.out.println("status code:" + response.getStatusLine().getStatusCode());
				System.out.println("status message:" + response.getStatusLine().getReasonPhrase());

				HttpEntity entity = response.getEntity();
				String temp = EntityUtils.toString(entity);
				JSONObject resultFirstJson = (JSONObject) jsonParser.parse(temp);
				if (!resultFirstJson.toString().contains("400")) {
					System.out.println(resultFirstJson.get("data"));
					System.out.println(resultFirstJson.get("data"));
					JSONArray tempa = (JSONArray) resultFirstJson.get("data");
					JSONObject a = (JSONObject) tempa.get(0);
					returnValue = a.get("shortenUrl").toString();
					System.out.println("if실행");
				}
				else {
				 System.out.println("else실행");
				 returnValue ="https://www.coupang.com/np/search?component=&q="+id+"&channel=user";
				}

				// JSONObject a = objectMapper.readValue(resultFirstJson.get("data").toString(),
				// JSONObject.class);
				// System.out.println(a);
				// JSONObject resultSecondJson = (JSONObject)
				// jsonParser.parse(resultFirstJson.get("data").toString());

				// System.out.println(resultSecondJson.get("shortenUrl").toString()+"@@@@@@@@@@");
				/*
				 * int jsonSize = jsonArray.size(); CoupangItemDto[] coupangItemArrayElement =
				 * new CoupangItemDto[jsonSize]; for(int i = 0 ; i<jsonSize; i++) { JSONObject
				 * tempItem = (JSONObject) jsonArray.get(i); System.out.println(tempItem);
				 * System.out.println((String)tempItem.get("productImage"));
				 * coupangItemArrayElement[i]=new CoupangItemDto(
				 * String.valueOf(tempItem.get("productImage")),
				 * String.valueOf(tempItem.get("productId")),
				 * String.valueOf(tempItem.get("rank")),
				 * String.valueOf(tempItem.get("productUrl")),
				 * String.valueOf(tempItem.get("keyword")),
				 * String.valueOf(tempItem.get("categoryName")),
				 * String.valueOf(tempItem.get("productName")),
				 * String.valueOf(tempItem.get("productPrice")),
				 * String.valueOf(tempItem.get("isRocket")) ); }
				 */

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (response != null) {
					try {
						response.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return returnValue;
	}
}
