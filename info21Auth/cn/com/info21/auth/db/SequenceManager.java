// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SequenceManager.java

package cn.com.info21.auth.db;

import cn.com.info21.database.*;
import java.sql.*;

// Referenced classes of package com.jivesoftware.base.database:
//            ConnectionManager

public class SequenceManager {

	public static long nextID(int type) {
		switch (type) {
			case 100 : // 'd'
				return managers[0].nextUniqueID();

			case 102 : // 'f'
				return managers[1].nextUniqueID();

			case 104 : // 'h'
				return managers[2].nextUniqueID();

			case 105 : // 'i'
				return managers[3].nextUniqueID();

			case 0 : // '\0'
				return managers[4].nextUniqueID();

			case 1 : // '\001'
				return managers[5].nextUniqueID();

			case 2 : // '\002'
				return managers[6].nextUniqueID();

			case 14 : // '\016'
				return managers[7].nextUniqueID();

			case 3 : // '\003'
				return managers[8].nextUniqueID();

			case 4 : // '\004'
				return managers[9].nextUniqueID();

			case 13 : // '\r'
				return managers[10].nextUniqueID();
		}
		throw new IllegalArgumentException("Invalid type");
	}

	public SequenceManager(int type, int blockSize) {
		this.type = type;
		this.blockSize = blockSize;
		currentID = 0L;
		maxID = 0L;
	}

	public synchronized long nextUniqueID() {
		if (currentID >= maxID)
			getNextBlock(5);
		long id = currentID;
		currentID++;
		return id;
	}

	private void getNextBlock(int count) {
		if (count == 0) {
			return;
		}
		Connection con = null;
		PreparedStatement pstmt = null;
		boolean abortTransaction = false;
		boolean success = false;
		try {
			con = DbConnectionManager.getTransactionConnection();
			pstmt = con.prepareStatement(LOAD_ID);
			pstmt.setInt(1, type);
			ResultSet rs = pstmt.executeQuery();
			long currentID = 1;
			if (!rs.next()) {
				pstmt = con.prepareStatement(INSERT_ID);
				pstmt.setLong(1, currentID);
				pstmt.setInt(2, type);
				pstmt.executeUpdate();
			}
			currentID = rs.getLong(1);
			pstmt.close();
			long newID = currentID + (long) blockSize;
			pstmt = con.prepareStatement(UPDATE_ID);
			pstmt.setLong(1, newID);
			pstmt.setInt(2, type);
			pstmt.setLong(3, currentID);
			success = pstmt.executeUpdate() == 1;
			if (success) {
				this.currentID = currentID;
				maxID = newID;
			}
		} catch (SQLException e) {
			abortTransaction = true;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			DbConnectionManager.closeTransactionConnection(
				con,
				abortTransaction);
		}
		if (!success) {
			try {
				Thread.currentThread();
				Thread.sleep(75L);
			} catch (InterruptedException interruptedexception) {
			}
			getNextBlock(count - 1);
		}
	}

	private static final String LOAD_ID =
		"SELECT id FROM sys_sequenceId WHERE idType=?";
	private static final String UPDATE_ID =
		"UPDATE sys_sequenceId SET id=? WHERE idType=? AND id=?";
	private static final String INSERT_ID =
		"INSERT INTO sys_sequenceId(id, idType) VALUES(?, ?)";
	private static SequenceManager managers[];
	private int type;
	private long currentID;
	private long maxID;
	private int blockSize;

	static {
		managers = new SequenceManager[11];
		managers[0] = new SequenceManager(100, 1);
		managers[1] = new SequenceManager(102, 10);
		managers[2] = new SequenceManager(104, 10);
		managers[3] = new SequenceManager(105, 15);
		managers[4] = new SequenceManager(0, 1);
		managers[5] = new SequenceManager(1, 10);
		managers[6] = new SequenceManager(2, 15);
		managers[7] = new SequenceManager(14, 1);
		managers[8] = new SequenceManager(3, 1);
		managers[9] = new SequenceManager(4, 1);
		managers[10] = new SequenceManager(13, 3);
	}
}
