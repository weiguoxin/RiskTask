package cn.com.shasha.utils;
import java.sql.*;
import java.text.SimpleDateFormat;
import cn.com.info21.database.DbConnectionManager;
public class DataCenter {
	/**
	 *@tblName varchar(255), -- ����
	 *@strGetFields varchar(1000) = '*', -- ��Ҫ���ص���
	 *@fldName varchar(255)='', -- ������ֶ���(�ɰ�����TABLE.FLDNAME��ʽ��
	 *@PageSize int = 10, -- ҳ�ߴ�
	 *@PageIndex int = 1, -- ҳ��
	 *@doCount bit = 0, -- ���ؼ�¼����, �� 0 ֵ�򷵻�
	 *@OrderType bit = 0, -- ������������, �� 0 ֵ����
	 *@strWhere varchar(1500) = '' -- ��ѯ���� (ע��: ��Ҫ�� where)
	 */
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public String ResultSetToJsonForShow(String tableName, String strGetFields,
			String fldName, int PageSize, int PageIndex,
			Boolean OrderType,String strWhere) {
		ResultSet oRs = null;
		String sRecordCount = "";
		String sJson = "";
		Connection con = null;
		CallableStatement proc = null;
		Boolean success = true;
		try {
			if (PageIndex == 0) {
				PageIndex = 1;
			}			
			con = DbConnectionManager.getConnection();
			proc = con.prepareCall(
					"{ call pagination(?,?,?,?,?,?,?,?)}");
			proc.setString(1, tableName);
			proc.setString(2, strGetFields);
			proc.setString(3, fldName);
			proc.setInt(4, PageSize);
			proc.setInt(5, PageIndex);
			proc.setBoolean(6, OrderType);
			proc.setString(7, strWhere);
			proc.registerOutParameter(8, java.sql.Types.INTEGER);
			oRs = proc.executeQuery();
			sJson = getJsonArray(oRs);
			sRecordCount = proc.getString(8);
			proc.clearParameters();
			if(sJson.equalsIgnoreCase("")){
				sJson="[{id:-1, nsrsbh:'', nsrmc:'', swjg_mc:'', zgy_mc:'', fxzb:'', fxms:'', begin_time:'', end_time:'', limit_time:'', " +
				"fxydcs:'', status:'', swjg_dm:'', zgy_dm:'', task_man:'',imp_date:'',imp_userid:'',zxr_dm:'',rwhk:'',zxr_mc:'',checked:''," +
				"zx_swjg_dm:'',zx_swjg_mc:'',fxzb_dm:'',change_zxr:''}]";
//System.out.println("sjon is null");
				//success=false;
			}
			sJson = "{totalCount:" + sRecordCount + ",'success':" + success + ",'topics':" + sJson + "}";
//System.out.println(sJson);
		} catch (SQLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(con, null, proc, oRs);
		}
		return sJson;
	}

	/*
	 * @�������ڼ��ر����ݵ����� @��ʽ��
	 */

	public String ResultSetToJsonForLoad(String tableName, String strGetFields,
			String fldName, int PageSize, int PageIndex,
			Boolean doCount, Boolean OrderType,String strWhere) {
		ResultSet oRs = null;
		String sJson = "";
		Connection con = null;
		CallableStatement proc = null;
		try {
			con = DbConnectionManager.getConnection();
			proc = con.prepareCall(
			"{ call pagination(?,?,?,?,?,?,?,?)}");
			proc.setString(1, tableName);
			proc.setString(2, strGetFields);
			proc.setString(3, fldName);
			proc.setInt(4, PageSize);
			proc.setInt(5, PageIndex);
			proc.setBoolean(6, OrderType);
			proc.setString(7, strWhere);
			proc.registerOutParameter(8, java.sql.Types.INTEGER);
			oRs = proc.executeQuery();
			sJson = ResultSetToJsonTabName(oRs, tableName);

			// System.out.println(sJson);
		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally {			
			DbConnectionManager.close(con, null, proc, oRs);
		}
		return sJson;
	}

	/*
	 * �Ѽ���ת��ΪJSON���ݷ��� @Prams
	 */
	public String ResultSetToJson(ResultSet rs) {
		String sJson = "";
		try {
			while (rs.next()) {
				sJson += "{";
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					if (rs.getMetaData().getColumnType(i) == java.sql.Types.INTEGER) {
						sJson += rs.getMetaData().getColumnName(i)
								.toUpperCase()
								+ ":" + (rs.getString(i)==null?"":rs.getString(i)) + ",";
					} else {
						sJson += rs.getMetaData().getColumnName(i)
								.toUpperCase()
								+ ":'" + (rs.getString(i)==null?"":rs.getString(i)) + "',";
					}

				}
				sJson = sJson.substring(0, sJson.length() - 1);
				sJson += "},";
			}
			if (sJson.length() > 0) {
				sJson = sJson.substring(0, sJson.length() - 1);
			}
			sJson = "{success:true,data:" + sJson + "}";
		} catch (SQLException e) {
			sJson = "ִ��SQLʧ�ܣ�";
			System.out.println("*** Error��DataCenter.ResultSetToJson *** ");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sJson;
	}

	/*
	 * @�������ڳ�ʼ���������� @tab:��λ����ǰ׺
	 * @��ʽ��{success:true,data:{YJBZ_SJID:'1',YJBZ_YJXMBH:'00001'}}
	 */
	public String ResultSetToJsonTabName(ResultSet rs, String tab) {
		String sJson = "";

		sJson = ResultSetToBaseJsonWithName(rs, tab);
		sJson = "{success:true,data:" + sJson + "}";

		return sJson;
	}

	public String LoadJson(ResultSet rs, String tab) {
		String sJson = "";

		try {
			while (rs.next()) {
				sJson += "{";
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					if (rs.getMetaData().getColumnType(i) == java.sql.Types.INTEGER) {
						sJson += tab
								+ rs.getMetaData().getColumnName(i)
										.toUpperCase() + ":" + (rs.getString(i)==null?"":rs.getString(i))
								+ ",";
					} else {
						sJson += tab
								+ rs.getMetaData().getColumnName(i)
										.toUpperCase() + ":'" + (rs.getString(i)==null?"":rs.getString(i))
								+ "',";
					}

				}
				sJson = sJson.substring(0, sJson.length() - 1);
				sJson += "},";
			}
			if (sJson.length() > 0) {
				sJson = sJson.substring(0, sJson.length() - 1);
			}
			sJson = "{success:true,data:" + sJson + "}";
		} catch (SQLException e) {
			sJson = "ִ��SQLʧ�ܣ�";
			System.out.println("*** Error��DataCenter.ResultSetToJson *** ");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sJson;
	}

	/*
	 * �Ѽ���ת��ΪJSON���ݷ��� ֻ�������ݲ���success data @
	 * ��ʽ��{Count:3,'List':[{'YJXMBH':'00001','XMMC':'����ҩƷ1']}
	 */
	public String getJsonArray(ResultSet rs) {
		StringBuffer sJson =  new StringBuffer();
		StringBuffer s = ResultSetToBaseJson(rs);
		if (s.length() > 0) {
			sJson.append("[");
			sJson.append(s);
			sJson.append("]");
		}

		return sJson.toString();
	}

	public StringBuffer ResultSetToBaseJson(ResultSet rs) {
		StringBuffer sJson = new StringBuffer();
		try {
			while (rs.next()) {
				sJson.append("{");
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					int dataType=rs.getMetaData().getColumnType(i);
					if (dataType == java.sql.Types.INTEGER
						|| dataType == java.sql.Types.NUMERIC) {
						sJson.append(rs.getMetaData().getColumnName(i)
								.toLowerCase());
						sJson.append(":");
						String str = rs.getString(i)==null?"0":rs.getString(i).trim();
						sJson.append(str);
						sJson.append(",");
					} else if (dataType == java.sql.Types.DATE
							|| dataType == java.sql.Types.TIMESTAMP) {
						if(rs.getTimestamp(i)!=null){
							sJson.append(rs.getMetaData().getColumnName(i)
							.toLowerCase()
							+ ":'" + dateFormat.format(rs.getTimestamp(i)) + "',");						
						}else{
							sJson.append(rs.getMetaData().getColumnName(i)
							.toLowerCase()
							+ ":'',");					
						}

					} else {
						sJson.append(rs.getMetaData().getColumnName(i)
								.toLowerCase());
						sJson.append(":'");
						String str = rs.getString(i)==null?"":rs.getString(i).trim();
						str = str.replace("\"", "��").replace("'", "��").replace("\n", "").replace("\r", "");
						sJson.append(str + "',");
					}
				}
				//sJson = sJson.substring(0, sJson.length() - 1);
				sJson.deleteCharAt(sJson.length()-1);
				sJson.append("},");
			}
			if (sJson.length() > 0) {
				//sJson = sJson.substring(0, sJson.length() - 1);
				sJson.deleteCharAt(sJson.length()-1);
			}
		} catch (SQLException e) {
			sJson = new StringBuffer("ִ��SQLʧ�ܣ�");
			System.out.println("*** Error��DataCenter.ResultSetToJson *** ");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sJson;
	}

	public String ResultSetToBaseJsonWithName(ResultSet rs, String TabName) {
		String sJson = "";
		try {
			while (rs.next()) {
				sJson += "{";
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					if (rs.getMetaData().getColumnType(i) == java.sql.Types.INTEGER) {
						sJson += TabName
								+ "_"
								+ rs.getMetaData().getColumnName(i)
										.toUpperCase() + ":" + (rs.getString(i)==null?"":rs.getString(i))
								+ ",";
					} else if (rs.getMetaData().getColumnType(i) == java.sql.Types.DATE
							|| rs.getMetaData().getColumnType(i) == java.sql.Types.TIMESTAMP) {
						if(rs.getTimestamp(i)!=null){
							sJson += TabName
							+ "_"
							+ rs.getMetaData().getColumnName(i)
							.toUpperCase() + ":'" +  dateFormat.format(rs.getTimestamp(i))
							+ "',";							
						}else
						{
							sJson += TabName
							+ "_"
							+ rs.getMetaData().getColumnName(i)
									.toUpperCase() + ":'" +  dateFormat.format(rs.getTimestamp(i))
							+ "',";							
						}	

					} else {
						sJson += TabName
								+ "_"
								+ rs.getMetaData().getColumnName(i)
										.toUpperCase() + ":'" + (rs.getString(i)==null?"":rs.getString(i))
								+ "',";
					}
				}
				sJson = sJson.substring(0, sJson.length() - 1);
				sJson += "},";
			}
			if (sJson.length() > 0) {
				sJson = sJson.substring(0, sJson.length() - 1);
			}
		} catch (SQLException e) {
			sJson = "ִ��SQLʧ�ܣ�";
			System.out.println("*** Error��DataCenter.ResultSetToJson *** ");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sJson;
	}
	/*
	 * ���ɿ�������combox��json����
	 * prefixName��������json��ǰ׺������total��
	 */
	public String ResultSetToJsonForCombox(String tableName, String fieldsName,
			String strWhere, int topnum, String prefixName) {
		String sJson = "";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	    StringBuffer strSql = new StringBuffer();
		try {
			con = DbConnectionManager.getConnection();
			if (topnum == 0) {
				strSql.append("select ");
			} else {
				strSql.append("select top ");
				strSql.append(topnum);
			}		
			if (fieldsName == null || fieldsName.equals("")) {
				strSql.append("* from ");
				strSql.append(tableName);
			} else {
				strSql.append(fieldsName);
				strSql.append(" from ");
				strSql.append(tableName);
			}			
			if (strWhere != null && !strWhere.equals("")) {
				strSql.append(" where ");
				strSql.append(strWhere);
			}
//System.out.println("strSQL---"+strSql);
			pstmt = con.prepareStatement(strSql.toString());
			rs = pstmt.executeQuery();			
			sJson = getJsonArray(rs);
			if(sJson.equalsIgnoreCase("")){
				sJson="{}";
			}
			sJson = "{success:true,"+prefixName+":" + sJson + "}";
			// System.out.println(sJson);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(con, null, pstmt, rs);
		}
		return sJson;
	}
	/**
	 *@taskmonth char(2), -- Ҫ��ѯ���·�,��Ϊ0,��Ĭ��Ϊ��ǰ��
	 * taskwatch������
	 */
	public String ResultSetToJsonForTask(String taskmonth) {
		ResultSet oRs = null;
		String sJson = "";
		Connection con = null;
		CallableStatement proc = null;
		Boolean success = true;
		try {		
			con = DbConnectionManager.getConnection();
			proc = con.prepareCall(
					"{ call taskwatch(?)}");
			proc.setString(1, taskmonth);
			oRs = proc.executeQuery();
			sJson = getJsonArray(oRs);
			proc.clearParameters();
			if(sJson.equalsIgnoreCase("")){
				sJson="{}";
				System.out.println("sjon is null");
				success=false;
			}
			sJson = "{'success':" + success + ",'topics':" + sJson + "}";
			// System.out.println(sJson);
		} catch (SQLException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		} finally {
			DbConnectionManager.close(con, null, proc, oRs);
		}
		return sJson;
	}
	
}
