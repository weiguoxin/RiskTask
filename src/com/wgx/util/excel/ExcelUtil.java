package com.wgx.util.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.wgx.util.json.JSONArray;
import com.wgx.util.json.JSONException;
import com.wgx.util.json.JSONObject;


import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelUtil {

	public static Map<String, String> Count_SWJG_mapKey = new LinkedHashMap<String, String>();
	public static Map<String, String> Count_Ren_mapKey = new LinkedHashMap<String, String>();
	public static Map<String, String> Tasks_Column_mapKey = new LinkedHashMap<String, String>();
	public static final String PATH = ExcelUtil.class.getClassLoader()
			.getResource("").getPath()
			+ "../../temp//";

	static {
		//统计机关任务数
		Count_SWJG_mapKey.put("swjg_mc", "税务机关");
		Count_SWJG_mapKey.put("a", "所有任务");
		Count_SWJG_mapKey.put("b", "未完成任务");
		Count_SWJG_mapKey.put("c", "已完成任务");
		Count_SWJG_mapKey.put("d", "完成百分比");
		//统计执行人任务数
		Count_Ren_mapKey.put("username", "执行人姓名");
		Count_Ren_mapKey.put("a", "所有任务");
		Count_Ren_mapKey.put("b", "未完成任务");
		Count_Ren_mapKey.put("c", "已完成任务");
		Count_Ren_mapKey.put("d", "完成比例");
//		"  id, nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb, fxms, begin_time, end_time, limit_time, " +
//		"fxydcs, status, swjg_dm, zgy_dm, task_man,imp_date,imp_userid,zxr_dm,rwhk,zxr_mc,[checked]," +
//		"zx_swjg_dm,zx_swjg_mc,fxzb_dm,change_zxr"
		//任务详细
		Tasks_Column_mapKey.put("nsrsbh", "纳税人识别号");
		Tasks_Column_mapKey.put("nsrmc", "纳税人名称");
		Tasks_Column_mapKey.put("swjg_mc", "主管税务机关名称");
		Tasks_Column_mapKey.put("zgy_mc", "专管员");
		Tasks_Column_mapKey.put("fxzb", "风险指标");
		Tasks_Column_mapKey.put("fxms", "风险描述");
		Tasks_Column_mapKey.put("fxydcs", "风险应对建议");
		Tasks_Column_mapKey.put("zx_swjg_mc", "执行税务机关名称");
		Tasks_Column_mapKey.put("zxr_mc", "执行人");
		Tasks_Column_mapKey.put("begin_time", "任务下发时间");
		Tasks_Column_mapKey.put("limit_time", "任务执行时限");
		Tasks_Column_mapKey.put("end_time", "任务完成时间");
		Tasks_Column_mapKey.put("rwhk", "任务回馈");
		Tasks_Column_mapKey.put("status", "状态");
		Tasks_Column_mapKey.put("checked", "审核");
//		Tasks_Column_mapKey.put("swjg_dm", "税务机关代码");
//		Tasks_Column_mapKey.put("zgy_dm", "专管员代码");
//		Tasks_Column_mapKey.put("task_man", "");
//		Tasks_Column_mapKey.put("imp_date", "导入时间");
//		Tasks_Column_mapKey.put("imp_userid", "导入ID");
//		Tasks_Column_mapKey.put("zxr_dm", "执行人代码");
//		Tasks_Column_mapKey.put("zx_swjg_dm", "执行税务机关代码");
//		Tasks_Column_mapKey.put("fxzb_dm", "风险指标代码");
		File f = new File(PATH);
		if (!f.isDirectory()) {
			f.mkdirs();
		}
	}

	public static File Excel(JSONArray list,  Map<String, String> mapKey) {
		String filename = new Date().getTime() + ".xls";
// System.out.println(PATH + filename);
		File f = new File(PATH + filename);
		OutputStream os = null;
		try {
			f.createNewFile();
			os = new FileOutputStream(f);
			WritableWorkbook wwb = Workbook.createWorkbook(os);
			int maxLen = 65535;
			int sheetLen = 0;
			int columnLen = mapKey.size();
			int len = list.length();
			WritableSheet ws = null;
			for (int i = 0,n = maxLen; i < len; i++,n++) {
				if (n == maxLen) {
					n = 0;
					if (++sheetLen > 255) {
						break;
					}
					ws = wwb.createSheet("sheet" + sheetLen, sheetLen);
System.out.println( "创建sheet = " +(sheetLen) );
					Iterator it = mapKey.entrySet().iterator();
					int j = 0;
					while (it.hasNext()) {
						Map.Entry e = (Map.Entry) it.next();
						Label l = new Label(j, n,
								e.getValue().toString());
						ws.setColumnView(j, 25);
						ws.addCell(l);
						j++;
					}
					ws.setColumnView(0, 25);
				}
				Iterator it = mapKey.entrySet().iterator();
				int j = 0;
				while (it.hasNext()) {
					Map.Entry e = (Map.Entry) it.next();
					JSONObject jo = list.getJSONObject(i);
					String v = jo.getString(e.getKey().toString());
					if(e.getKey().toString().equals("status")){
						if("-1".equals(v)){
							v = "未下发";
						}else if("0".equals(v)){
							v = "执行中";
						}else if("1".equals(v)){
							v = "已执行";
						}
					}else if(e.getKey().toString().equals("checked")){
						if("0".equals(v)){
							v = "未审核";
						}else if("1".equals(v)){
							v = "已审核";
						}
					}
					Label l = new Label(j, n + 1, v);
					ws.addCell(l);
					j++;
				}
				if (i % 1000 == 0) {
					os.flush();
				}
			}
			if (sheetLen == 0)
				wwb.createSheet("sheet" + sheetLen, sheetLen);
			wwb.write();
			wwb.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return f;
	}
}
