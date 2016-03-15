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
 * java读取Excel文件
 * 
 * @author shobos
 * 
 */
public class JavaOperateExcel {

	private static String  headArray[] = new String[] { "纳税人识别号", "纳税人名称", "主管税务机关", "专管员姓名","风险指标", "任务时限","风险描述"};
	private static int  headArray_len = headArray.length;
			//"风险指标", "风险描述","风险应对措施","税务机关代码","专管员代码"
	/**
	 * 利用poi读取Excel文件
	 * 
	 * @param filePath
	 *            excel文件路径
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
			FileInputStream fis = new FileInputStream(filePath); // 根据excel文件路径创建文件流
			POIFSFileSystem fs = new POIFSFileSystem(fis); // 利用poi读取excel文件流
			HSSFWorkbook wb = new HSSFWorkbook(fs); // 读取excel工作簿
			HSSFSheet sheet = wb.getSheetAt(0); // 读取excel的sheet，0表示读取第一个、1表示第二个.....
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			// 先取excel的第一行信息,进行表头检查查看是否为正确模板文件
			HSSFRow headrow = sheet.getRow(0);
//System.out.println("headrow.getLastCellNum()---" + headrow.getLastCellNum());
			if (headrow != null) {
				if (headrow.getLastCellNum() == 6
						|| sheet.getPhysicalNumberOfRows() > 1) {
					for (int i = 0; i < headrow.getLastCellNum(); i++) {
						HSSFCell cell = headrow.getCell((short) i);
						if (cell == null) {
							errorStr = "该Excel格式不对!1";
						} else {
							if (cell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
								errorStr = "该Excel格式不对!2";
								break;
							}
						}
					}
					if (errorStr.equals("")) {
						// 检验表头是否正确,与数组进行匹配
						for (int i = 0; i < headrow.getLastCellNum()&&i<headArray_len; i++) {
							HSSFCell cell = headrow.getCell((short) i);
							if (!(cell.getStringCellValue().trim())
									.equals(headArray[i])) {
//System.out.println(cell.getStringCellValue().trim() + " " + headArray[i]);
								errorStr = "该Excel格式不对!3";
								break;
							}
						}
					}
				} else {
					errorStr = "该Excel格式不对!4";
				}
			} else {
				errorStr = "该Excel是空文档!";
			}
			// 若表头格式正确,则进行行的导入
//System.out.println("sheet.getPhysicalNumberOfRows()--"
//					+ sheet.getPhysicalNumberOfRows());
			if (errorStr.equals("")) {

				// 获取sheet中总共有多少行数据sheet.getPhysicalNumberOfRows()
				for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
					HSSFRow row = sheet.getRow(i); // 取出sheet中的某一行数据
					String lineError = "";

//					if (row != null) {
//						// 获取该行中总共有多少列数据row.getLastCellNum()
//						for (int j = 0; j < 6; j++) {
//							HSSFCell cell = row.getCell((short) j); // 获取该行中的一个单元格对象
//							// 判断单元格数据类型，这个地方值得注意：当取某一行中的数据的时候，需要判断数据类型，否则会报错
//							// java.lang.NumberFormatException: You cannot get a
//							// string
//							// value from a numeric cell等等错误
//							String value = "";
//							if (cell != null) {
//								// 若为要输日期的列,则判断是否为日期,其余列为string,有错就退出当前行
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

						// 无错，就写入数据库
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
			errorStr = "导入时发生异常,请检查文件无误后重新导入!";
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