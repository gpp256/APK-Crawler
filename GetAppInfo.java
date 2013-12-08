/*
 * GetAppInfo.java
 *
 * Copyright (c) 2013 Yoshi 
 * This software is distributed under the MIT License.(MIT-LICENSE.txt)
 *
 */

import java.io.*;
import java.util.Locale;

import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.MarketSession.Callback;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.ResponseContext;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;

public class GetAppInfo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length != 4) {
			System.out.println("Usage :\n" +
				"java -jar this_jar_name confpath query_str startIndex numbers");
			System.exit(1);
		}
		String confpath = args[0];
		GetAppConfig conf = new GetAppConfig(confpath);
		String email = conf.getUserID();
		String password = conf.getPassword();
		String query = args[1];
		final int startIndex = Integer.parseInt(args[2]);
		final int numbers = Integer.parseInt(args[3]);
		String androidid = conf.getDeviceID();

		if(email.equals("")) {
			System.out.println("Error: Failed to get UserID.");
			System.exit(2);
		} else if (password.equals("")) {
			System.out.println("Error: Failed to get Password.");
			System.exit(3);
		} else if (androidid.equals("")) {
			System.out.println("Error: Failed to get DeviceID.");
			System.exit(4);
		}

		System.out.println("query: " + query);
		MarketSession session = new MarketSession();
		session.getContext().setAndroidId(androidid);

		Locale locale = new Locale("ja","JP");
		session.setLocale(locale);
		session.setOperator("NTT DOCOMO", "44010");
		session.getContext().setDeviceAndSdkVersion("passion:8");

		try {
			session.login(email, password, androidid);
		} catch (Exception e) {
			System.out.println("Error: failed to login.: " + e.getMessage());
			System.exit(1);
		}
		
		AppsRequest appsRequest = AppsRequest.newBuilder()
			.setQuery(query)
			.setStartIndex(startIndex)
			.setEntriesCount(numbers)
			.setWithExtendedInfo(true)
			.build();

		Callback<AppsResponse> callback = new Callback<AppsResponse> () {
			@Override
			public void onResult(ResponseContext context, AppsResponse response) {
				int totalcnt,cnt;
				JsonFactory factory = new JsonFactory();
				try {
			        JsonGenerator generator = 
					factory.createGenerator(new FileWriter(new File("pkginfo.json")));
				generator.writeStartObject();
				//generator.setRootValueSeparator(new SerializedString("\n"));

				if(response != null) {
					totalcnt = response.getEntriesCount();
					cnt = response.getAppCount();
					System.out.println("startIndex = " + startIndex);
					System.out.println("entriesCount = " + numbers);
					System.out.println("totalcount = " + totalcnt);
					System.out.println("count = " + cnt);
					generator.writeNumberField("startIndex", startIndex);
					generator.writeNumberField("entriesCount", numbers);
					generator.writeNumberField("total", totalcnt);
					generator.writeNumberField("count", cnt);
					generator.writeRaw("\n");
				} else {
					cnt = -1; 
				}
				generator.writeFieldName("dataset");
				generator.writeStartArray();

				if (cnt > 0) {
					for(int i = 0; ((i < cnt) && (i < numbers)); i++) {
						generator.writeStartObject();
						generator.writeNumberField("num", i+startIndex);
						System.out.println("------------------------------------------------------------------------------------");
						int counter = i + startIndex;
						System.out.println(counter + ":");
						System.out.println("------------------------------------------------------------------------------------");
						generator.writeStringField("title", response.getApp(i).getTitle());
						generator.writeStringField("appType", ""+response.getApp(i).getAppType());
						generator.writeStringField("category", response.getApp(i).getExtendedInfo().getCategory());
						generator.writeStringField("rating", response.getApp(i).getRating());
						generator.writeNumberField("ratingCount", response.getApp(i).getRatingsCount());
						generator.writeStringField("countText", response.getApp(i).getExtendedInfo().getDownloadsCountText());
						generator.writeStringField("creatorId", response.getApp(i).getCreatorId());
						generator.writeStringField("id", response.getApp(i).getId());
						generator.writeStringField("packageName", response.getApp(i).getPackageName());
						generator.writeStringField("version", response.getApp(i).getVersion());
						generator.writeNumberField("versionCode", response.getApp(i).getVersionCode());
						generator.writeStringField("price", response.getApp(i).getPrice());
						generator.writeNumberField("priceMicros", response.getApp(i).getPriceMicros());
						generator.writeStringField("priceCurrency", response.getApp(i).getPriceCurrency());
						generator.writeStringField("contactWebsite", response.getApp(i).getExtendedInfo().getContactWebsite());
						generator.writeNumberField("screenshotsCount", response.getApp(i).getExtendedInfo().getScreenshotsCount());
						generator.writeNumberField("installSize", response.getApp(i).getExtendedInfo().getInstallSize());
						generator.writeStringField("permissionIdList", ""+response.getApp(i).getExtendedInfo().getPermissionIdList());
						generator.writeStringField("promotoText", response.getApp(i).getExtendedInfo().getPromoText());
						generator.writeStringField("description", response.getApp(i).getExtendedInfo().getDescription());
						
						System.out.println("title: " + response.getApp(i).getTitle());
						System.out.println("appType: " + response.getApp(i).getAppType());
						System.out.println("category: " + response.getApp(i).getExtendedInfo().getCategory());
						System.out.println("rating: " + response.getApp(i).getRating());
						System.out.println("ratingsCount: " + response.getApp(i).getRatingsCount());
						System.out.println("count: " + response.getApp(i).getExtendedInfo().getDownloadsCount());
						System.out.println("countText: " + response.getApp(i).getExtendedInfo().getDownloadsCountText());
						System.out.println("creator: " + response.getApp(i).getCreator());
						System.out.println("creatorId: " + response.getApp(i).getCreatorId());
						System.out.println("id: " + response.getApp(i).getId());
						System.out.println("packageName: " + response.getApp(i).getPackageName());
						System.out.println("version: " + response.getApp(i).getVersion());
						//System.out.println("contactEmail: " + response.getApp(i).getExtendedInfo().getContactEmail());
						//System.out.println("contactPhone: " + response.getApp(i).getExtendedInfo().getContactPhone());
						System.out.println("installSize: " + response.getApp(i).getExtendedInfo().getInstallSize());
						generator.writeEndObject();
						generator.writeRaw("\n");
					}
				} else if (cnt == 0) {
					System.out.println("no hit");
				} else {
					System.out.println("Bad Reqeust");
				}
				
				generator.writeEndArray();
				generator.writeEndObject();
				generator.close();

				} catch (Exception e) {
					System.out.println("Error: pkginfo(): " + e.getMessage());
				}

			} // onResult()
		};
		session.append(appsRequest, callback);
		session.flush();
	}
}
