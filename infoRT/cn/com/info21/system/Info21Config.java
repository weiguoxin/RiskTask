// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Info21Config.java

package cn.com.info21.system;

import cn.com.info21.util.XMLProperties;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Info21Config {

	public Info21Config() {
	}

	public static String getInfo21Home() {
		if (info21Home == null)
			loadProperties();
		return info21Home;
	}

	public static String getProperty(String name) {
		if (properties == null)
			loadProperties();
		if (properties == null)
			return null;
		else
			return properties.getProperty(name);
	}

	public static void setProperty(String name, String value) {
		if (properties == null)
			loadProperties();
		properties.setProperty(name, value);
	}

	public static void deleteProperty(String name) {
		if (properties == null)
			loadProperties();
		properties.deleteProperty(name);
	}

	private static synchronized void loadProperties() {
		if (failedLoading)
			return;
		if (properties == null) {
			if (info21Home == null)
				info21Home = System.getProperty("info21Home");
			if (info21Home == null)
				info21Home = (new InitPropLoader()).getInfo21Home();
			if (info21Home == null) {
				failedLoading = true;
				return;
			}
			try {
				properties =
					new XMLProperties(
						info21Home + File.separator + INFO21_CONFIG_FILENAME);
			} catch (IOException ioexception) {
			}
		}
	}
	public static String info21Home = null;
	private static final String INFO21_CONFIG_FILENAME = "info21_config.xml";
	public static boolean failedLoading = false;
	private static XMLProperties properties = null;
	private static Locale locale = null;
}
