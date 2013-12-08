/*
 * GetAppConfig.java
 *
 * Copyright (c) 2013 Yoshi 
 * This software is distributed under the MIT License.(MIT-LICENSE.txt)
 *
 */

import java.io.*;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonFactory;
 
public class GetAppConfig {
	private String userid = "";
	private String passwd = "";
	private String deviceid = "";
	private String pkgdir = "";
	private String jsonfile = "getapp.conf";

	GetAppConfig() { getConf(); }

	GetAppConfig(String conf) { this.jsonfile = conf; getConf(); }

	private void setUserID (String u) { this.userid = u; }
	private void setPassword (String p) { this.passwd = p; }
	private void setDeviceID (String i) { this.deviceid = i; }
	private void setPackageDir (String d) { this.pkgdir = d; }

	public String getUserID () { return this.userid; }
	public String getPassword () { return this.passwd; }
	public String getDeviceID () { return this.deviceid; }
	public String getPackageDir () { return this.pkgdir; }

	private void getConf () {
		String line;
		String json="";
		try {
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(new FileInputStream(this.jsonfile), "UTF-8"));
		while((line = reader.readLine()) != null){ json+=line; }
		reader.close();
		} catch (Exception e) { 
		System.out.println("Error: readconf(): " + e.getMessage());
		return;
		}

		JsonFactory factory = new JsonFactory();
		try {
		JsonParser parser = factory.createParser(json);
		while (parser.nextToken() != JsonToken.END_OBJECT) {
			String name = parser.getCurrentName();
			if(name == null) continue;
			parser.nextToken();
			if(name.equals("userid")) {
			    this.setUserID(parser.getText());
			}else if(name.equals("passwd")) {
			    this.setPassword(parser.getText());
			}else if(name.equals("deviceid")) {
			    this.setDeviceID(parser.getText());
			}else if(name.equals("pkgdir")) {
			    this.setPackageDir(parser.getText());
			} else {
			    parser.skipChildren();
			}
		} // while
		} catch (Exception e) {
			System.out.println("Error: parseconf(): " + e.getMessage());
		}
	}
}
