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
		//ͳ�ƻ���������
		Count_SWJG_mapKey.put("swjg_mc", "˰�����");
		Count_SWJG_mapKey.put("a", "��������");
		Count_SWJG_mapKey.put("b", "δ�������");
		Count_SWJG_mapKey.put("c", "���������");
		Count_SWJG_mapKey.put("d", "��ɰٷֱ�");
		//ͳ��ִ����������
		Count_Ren_mapKey.put("username", "ִ��������");
		Count_Ren_mapKey.put("a", "��������");
		Count_Ren_mapKey.put("b", "δ�������");
		Count_Ren_mapKey.put("c", "���������");
		Count_Ren_mapKey.put("d", "��ɱ���");
//		"  id, nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb, fxms, begin_time, end_time, limit_time, " +
//		"fxydcs, status, swjg_dm, zgy_dm, task_man,imp_date,imp_userid,zxr_dm,rwhk,zxr_mc,[checked]," +
//		"zx_swjg_dm,zx_swjg_mc,fxzb_dm,change_zxr"
		//������ϸ
		Tasks_Column_mapKey.put("nsrsbh", "��˰��ʶ���");
		Tasks_Column_mapKey.put("nsrmc", "��˰������");
		Tasks_Column_mapKey.put("swjg_mc", "����˰���������");
		Tasks_Column_mapKey.put("zgy_mc", "ר��Ա");
		Tasks_Column_mapKey.put("fxzb", "����ָ��");
		Tasks_Column_mapKey.put("fxms", "��������");
		Tasks_Column_mapKey.put("fxydcs", "����Ӧ�Խ���");
		Tasks_Column_mapKey.put("zx_swjg_mc", "ִ��˰���������");
		Tasks_Column_mapKey.put("zxr_mc", "ִ����");
		Tasks_Column_mapKey.put("begin_time", "�����·�ʱ��");
		Tasks_Column_mapKey.put("limit_time", "����ִ��ʱ��");
		Tasks_Column_mapKey.put("end_time", "�������ʱ��");
		Tasks_Column_mapKey.put("rwhk", "�������");
		Tasks_Column_mapKey.put("status", "״̬");
		Tasks_Column_mapKey.put("checked", "���");
//		Tasks_Column_mapKey.put("swjg_dm", "˰����ش���");
//		Tasks_Column_mapKey.put("zgy_dm", "ר��Ա����");
//		Tasks_Column_mapKey.put("task_man", "");
//		Tasks_Column_mapKey.put("imp_date", "����ʱ��");
//		Tasks_Column_mapKey.put("imp_userid", "����ID");
//		Tasks_Column_mapKey.put("zxr_dm", "ִ���˴���");
//		Tasks_Column_mapKey.put("zx_swjg_dm", "ִ��˰����ش���");
//		Tasks_Column_mapKey.put("fxzb_dm", "����ָ�����");
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
System.out.println( "����sheet = " +(sheetLen) );
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
							v = "δ�·�";
						}else if("0".equals(v)){
							v = "ִ����";
						}else if("1".equals(v)){
							v = "��ִ��";
						}
					}else if(e.getKey().toString().equals("checked")){
						if("0".equals(v)){
							v = "δ���";
						}else if("1".equals(v)){
							v = "�����";
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
