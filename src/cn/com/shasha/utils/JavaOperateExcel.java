package cn.com.shasha.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.record.formula.functions.Cell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.wgx.util.json.DateUtil;
import com.wgx.util.json.JSONException;
import com.wgx.util.json.JSONObject;

import cn.com.shasha.services.*;
import cn.com.shasha.sys.*;

/**
 * java��ȡExcel�ļ�
 * 
 * @author shobos
 * 
 */
public class JavaOperateExcel {

	private static String  headArray[] = new String[] { "��˰��ʶ���", "��˰������", "����˰�����", "ר��Ա����","����ָ��", "����ʱ��","��������"};
	private static int  headArray_len = headArray.length;
			//"����ָ��", "��������","����Ӧ�Դ�ʩ","˰����ش���","ר��Ա����"
	/**
	 * ����poi��ȡExcel�ļ�
	 * 
	 * @param filePath
	 *            excel�ļ�·��
	 * @throws java.io.IOException
	 */
	public static JSONObject readExcel(String filePath, int lrpeople, String lrdept,HttpSession s)
			throws IOException {
		Object o = s.getAttribute("Session_User");
		WLUser user = (WLUser)o;
		Timestamp ts = new Timestamp(new java.util.Date().getTime());
		JSONObject jres = new JSONObject();
		String errorStr = "";
		int r = 0;
		try {
			FileInputStream fis = new FileInputStream(filePath); // ����excel�ļ�·�������ļ���
			POIFSFileSystem fs = new POIFSFileSystem(fis); // ����poi��ȡexcel�ļ���
			HSSFWorkbook wb = new HSSFWorkbook(fs); // ��ȡexcel������
			HSSFSheet sheet = wb.getSheetAt(0); // ��ȡexcel��sheet��0��ʾ��ȡ��һ����1��ʾ�ڶ���.....
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			// ��ȡexcel�ĵ�һ����Ϣ,���б�ͷ���鿴�Ƿ�Ϊ��ȷģ���ļ�
			HSSFRow headrow = sheet.getRow(0);
//System.out.println("headrow.getLastCellNum()---" + headrow.getLastCellNum());
			if (headrow != null) {
				if (headrow.getLastCellNum() == 6
						|| sheet.getPhysicalNumberOfRows() > 1) {
					for (int i = 0; i < headrow.getLastCellNum(); i++) {
						HSSFCell cell = headrow.getCell((short) i);
						if (cell == null) {
							errorStr = "��Excel��ʽ����!1";
						} else {
							if (cell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
								errorStr = "��Excel��ʽ����!2";
								break;
							}
						}
					}
					if (errorStr.equals("")) {
						// �����ͷ�Ƿ���ȷ,���������ƥ��
						for (int i = 0; i < headrow.getLastCellNum()&&i<headArray_len; i++) {
							HSSFCell cell = headrow.getCell((short) i);
							if (!(cell.getStringCellValue().trim())
									.equals(headArray[i])) {
//System.out.println(cell.getStringCellValue().trim() + " " + headArray[i]);
								errorStr = "��Excel��ʽ����!3";
								break;
							}
						}
					}
				} else {
					errorStr = "��Excel��ʽ����!4";
				}
			} else {
				errorStr = "��Excel�ǿ��ĵ�!";
			}
			// ����ͷ��ʽ��ȷ,������еĵ���
//System.out.println("sheet.getPhysicalNumberOfRows()--"
//					+ sheet.getPhysicalNumberOfRows());
			if (errorStr.equals("")) {

				// ��ȡsheet���ܹ��ж���������sheet.getPhysicalNumberOfRows()
				for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
					HSSFRow row = sheet.getRow(i); // ȡ��sheet�е�ĳһ������
					String lineError = "";

//					if (row != null) {
//						// ��ȡ�������ܹ��ж���������row.getLastCellNum()
//						for (int j = 0; j < 6; j++) {
//							HSSFCell cell = row.getCell((short) j); // ��ȡ�����е�һ����Ԫ�����
//							// �жϵ�Ԫ���������ͣ�����ط�ֵ��ע�⣺��ȡĳһ���е����ݵ�ʱ����Ҫ�ж��������ͣ�����ᱨ��
//							// java.lang.NumberFormatException: You cannot get a
//							// string
//							// value from a numeric cell�ȵȴ���
//							String value = "";
//							if (cell != null) {
//								// ��ΪҪ�����ڵ���,���ж��Ƿ�Ϊ����,������Ϊstring,�д���˳���ǰ��
//								if (j == 5) {
//									if (cell.getCellType() == 0) {
//										if (!HSSFDateUtil
//												.isCellDateFormatted(cell)) {
//											lineError = i + 1 + ",";
//System.out.println("fuck1" + lineError);
//											break;
//										}
//									} else {
//										System.out.println(cell.getCellType());
//										lineError = i + 1 + ",";
//										System.out.println("fuck21" + lineError);
//										break;
//									}
//								} else {
//									if (cell.getCellType() != 1) {
//										lineError = i + 1 + ",";
//										System.out.println("fuck3" + lineError);
//										break;
//									} else {
//										System.out.println("fuck54" + lineError);
//										/*
//										 * if(j==1) {
//										 * if(!Character.isDigit(cell.
//										 * getStringCellValue().charAt(0))) {
//										 * lineError = i+1 + ","; break; } }
//										 */
//									}
//								}
//							}
//						}

						// �޴���д�����ݿ�
						if (lineError.equals("")) {
							String nsrsbh, nsrmc, swjg_mc, zgy_mc, fxzb,limit_time, fxms, fxydcs=null, fxzb_dm=null, swjg_dm=null,zx_swjg_dm=null;
							boolean zgy_bool = false;
							int index = 0,zgy_dm=-1,zxr_dm=-1;
							nsrsbh = row.getCell(index) != null ? row
									.getCell(index).getStringCellValue().trim()
									: "";
							index++;
							nsrmc = row.getCell(index) != null ? row
									.getCell(index).getStringCellValue().trim()
									: "";
							index++;
							swjg_mc = row.getCell(index) != null ? row
									.getCell(index).getStringCellValue().trim()
									: "";
							index++;
							zgy_mc = row.getCell(index) != null ? row
									.getCell(index).getStringCellValue().trim()
									: "";
							index++;
							fxzb = row.getCell(index) != null ? row
									.getCell(index).getStringCellValue().trim()
									: "";
							index++;
							limit_time = row.getCell(index) != null ? row
									.getCell(index).getStringCellValue().trim()
									: "";
							index++;
							fxms = row.getCell(index) != null ? row
									.getCell(index).getStringCellValue().trim()
									: "";
							Date limit = DateUtil.getDate(limit_time, "yyyy-M-d");
							swjg_dm = TaskManData.GetSwjgDmByName(swjg_mc);
							zgy_dm = WLUserData.GetUserByNameAndSwjgDM(zgy_mc, swjg_dm);
							JSONObject fxzb_json = TaskManData.GetFxzbJSONObject("fxzb",fxzb);
							if(null != fxzb_json){
								fxydcs = fxzb_json.getString("fxydjy");
								fxzb_dm = fxzb_json.getString("sid");
								zgy_bool = fxzb_json.getBoolean("zgy");
								if(zgy_bool){
									zxr_dm = zgy_dm;
									zx_swjg_dm = swjg_dm;
								}
							}
							TasksTemp ps = new TasksTemp();
							ps.setNsrsbh(nsrsbh);
							ps.setNsrmc(nsrmc);
							ps.setSwjg_mc(swjg_mc);
							ps.setZgy_mc(zgy_mc);
							ps.setFxzb(fxzb);
							ps.setFxydcs(fxydcs);
							ps.setFxzb_dm(fxzb_dm);
							ps.setZgy_dm(zgy_dm+"");
							ps.setSwjg_dm(swjg_dm);
							ps.setZxr_dm(zxr_dm);
							ps.setZx_swjg_dm(zx_swjg_dm);
							ps.setFxms(fxms);
							ps.setLimit_time(new Timestamp(limit.getTime()));
							ps.setImp_userid(user.getUserid());
							ps.setImp_date(ts);
							r += ps.insertSelfTask();
//System.out.println("insert:" + i);
						}
						errorStr += lineError;
					}
				}else{
//System.out.println("errorStr---" + errorStr);
			}
		} catch (Exception e) {
			errorStr = "����ʱ�����쳣,�����ļ���������µ���!";
			e.printStackTrace();
		}
		// System.out.println("errorStr---"+errorStr);
		try {
			jres.put("r", r);
			jres.put("res", errorStr);
		} catch (JSONException e) {
		}
		return jres;
	}

	public static java.util.Date toDate(String s) {
		java.util.Date rtn = null;
		if (s != null && !"".equals(s.trim())) {
			SimpleDateFormat dtformat = new SimpleDateFormat("yyyy-MM-dd",
					Locale.getDefault());
			try {
				rtn = dtformat.parse(s);
			} catch (Exception e) {
				rtn = null;
				e.printStackTrace();
			}
		}
		return rtn;
	}

}