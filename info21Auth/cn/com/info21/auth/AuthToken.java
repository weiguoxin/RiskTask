// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AuthToken.java

package cn.com.info21.auth;

public interface AuthToken {
	public abstract long getUserID();
	public abstract String getUserName();
	public abstract boolean isAnonymous();
	public abstract boolean isSuperAdmin();
}
