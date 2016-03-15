package com.url.ajax.json;
import com.url.ajax.json.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String griddata = "{\"selecteddta\":[{\"operating\":\"Advertising\",\"budget\":23.4,\"actual\":10.3},{\"operating\":\"Bad debts\",\"budget\":34.6,\"actual\":21.3}]}";
		JSONObject jsonObj = null;  
		JSONObject jsonObject = null;  
		try {  
		 jsonObj = new JSONObject(griddata);  
			JSONArray array=jsonObj.getJSONArray("selecteddta");
			for(int i=0;i<array.length();i++)
			{	
			System.out.println("array:"+array.get(i));
			//JSONObject obj=jsonObj.getJSONObject("singer");
			//System.out.println("obj:"+obj.get("firstName"));
			 jsonObject = (JSONObject)array.get(i);  
			 System.out.println(jsonObject);  
			 System.out.println("1---"+jsonObject.getString("operating"));
			 System.out.println("2---"+jsonObject.getDouble("budget"));
			 System.out.println("3---"+jsonObject.getDouble("actual"));
			} 
			 Pattern p = Pattern.compile("¡¶[\u4e00-\u9fa5]+¡·"); 
			 Matcher m = p.matcher("¡¶·µÀÏ»¹Í¯¡·"); 
			 System.out.println(m.find());
		} catch (JSONException e) {  
		  e.printStackTrace(System.err);  
		 } 
		
	}

}
