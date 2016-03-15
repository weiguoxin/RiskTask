package com.wgx.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener  implements HttpSessionListener  {

	private static int currentNum = 0;
	
	public void sessionCreated(HttpSessionEvent se) {
		currentNum ++;
		System.out.println("sessionCreated");
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		if(currentNum >0){
			currentNum --;
		}
		System.out.println("sessionDestroyed");
	}
	
	public static int getCurrentNum(){
		return currentNum;
	}

}
