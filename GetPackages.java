/*
 * GetPackages.java
 *
 * Copyright (c) 2013 Yoshi 
 * This software is distributed under the MIT License.(MIT-LICENSE.txt)
 *
 */

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.model.Market;

public class GetPackages {

        public static void main(String args[]) {

		if(args.length != 2) {
			System.out.println("Usage :java -jar this_jar_name confpath apk-id");
			System.exit(1);
		}
		String confpath = args[0];
		String apkid = args[1];
		GetAppConfig conf = new GetAppConfig(confpath);
		String userid = conf.getUserID();
		String passwd = conf.getPassword();
		String deviceid = conf.getDeviceID();
		String savedir = conf.getPackageDir();

		// check input data
		if(userid.equals("")) {
			System.out.println("Error: Failed to get UserID.");
			System.exit(2);
		} else if (passwd.equals("")) {
			System.out.println("Error: Failed to get Password.");
			System.exit(3);
		} else if (deviceid.equals("")) {
			System.out.println("Error: Failed to get DeviceID.");
			System.exit(4);
		}

		// get the specified package
		getpkg(apkid, userid, passwd, deviceid, savedir);
	}

	private static void getpkg (
		String assetId, // asset id(e.g. v2:com.hoge.tool:1:23)
		String userid,  // user id(email address)
		String passwd,  // password
		String deviceid,// device id
		String savedir  // directory for storing packages
	) {
		try {
		MarketSession session = new MarketSession(Boolean.valueOf(true));
	        System.out.println("Login...");
		session.getContext().setAndroidId(deviceid);
		Locale locale = new Locale("ja", "JP");
		session.setLocale(locale);
		session.setOperator("NTT DOCOMO", "44010");
		session.getContext().setDeviceAndSdkVersion("passion:8");

	        session.login(userid, passwd, deviceid);

		// Issue a GetAssetRequest for the specifed assetId (application identifier in the market)
		Market.GetAssetResponse.InstallAsset ia = session.queryGetAssetRequest(assetId).getInstallAsset(0);
		System.out.println("URI: "+ia.getBlobUrl());
		String cookieName = ia.getDownloadAuthCookieName();
		String cookieValue = ia.getDownloadAuthCookieValue();
	        System.out.println("cookieName: "+cookieName+ ", cookieValue: "+cookieValue);

		//Retrieve the blob containg the url for the required assetId
		URL url = new URL(ia.getBlobUrl());
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("User-Agent", "Android-Market/2 (sapphire PLAT-RC33); gzip");
		conn.setRequestProperty("Cookie", cookieName + "=" + cookieValue);

      		if (conn.getResponseCode() == 302) {
      		  String location = conn.getHeaderField("Location");
      		  url = new URL(location);
      		  conn = (HttpURLConnection)url.openConnection();
      		  conn.setRequestMethod("GET");
      		  conn.setRequestProperty("User-Agent", "Android-Market/2 (sapphire PLAT-RC33); gzip");
      		  conn.setRequestProperty("Cookie", cookieName + "=" + cookieValue);
      		}

		//Save the stream onto filesystem
		InputStream inputstream =  (InputStream) conn.getInputStream();
		String fileToSave = assetId + ".apk";
		System.out.println("Downloading " + fileToSave);
		BufferedOutputStream stream = 
			new BufferedOutputStream(new FileOutputStream(savedir + fileToSave));
		byte buf[] = new byte[1024];
		int k = 0;
		for(long l = 0L; (k = inputstream.read(buf)) != -1; l += k )
			stream.write(buf, 0, k);
		inputstream.close();
		stream.close();  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
