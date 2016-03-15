// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 

package cn.com.info21.system;

import cn.com.info21.jdom.Document;
import cn.com.info21.jdom.input.SAXBuilder;
import java.io.InputStream;

class InitPropLoader {

	InitPropLoader() {
	}

	public String getInfo21Home() {
		String info21Home = null;
		InputStream in = null;
		try {
			in = getClass().getResourceAsStream("/info21_init.xml");
			if (in != null) {
				Document doc = (new SAXBuilder()).build(in);
				info21Home = doc.getRootElement().getText();
			}
		} catch (Exception e) {
			
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (Exception exception1) {
			}
		}
		if (info21Home != null)
			for (info21Home = info21Home.trim();
				info21Home.endsWith("/") || info21Home.endsWith("\\");
				info21Home = info21Home.substring(0, info21Home.length() - 1));
		if ("".equals(info21Home))
			info21Home = null;
		return info21Home;
	}
}
