package com.nrttech.atalla;

import java.security.InvalidParameterException;

import org.apache.commons.lang3.StringUtils;

public class AkbKeyRsa {
	
	public String getHeader() {
		return akb.substring(0,8);
	}

	public String getKey() {
		return akb.substring(8,akb.length()-16);
	}

	public String getMac() {
		return StringUtils.right( akb, 16 );
	}

	private String akb;
	public String getAkb() {
		return akb;
	}
	
	// 1PUNN0I0 DFAD3A01ED3543918BAC43C93FC179468B679C7E28FEA4BB B6AFE7C5E0A18120 D2B9
	static public AkbKeyRsa fromDbFormat( String akb ) {
		AkbKeyRsa k = new AkbKeyRsa();
		k.akb = akb;
		return k;
	}
	
	public void appendKeyInAtallaFormat( StringBuilder sb ) {
		sb.append(getHeader());
		sb.append(',');
		sb.append(getKey());
		sb.append(',');
		sb.append(getMac());
	}
	
	static public AkbKeyRsa fromAtallaResponse( String akbData ) {
		String[] a = StringUtils.splitPreserveAllTokens(akbData,',');
		if( a == null || a.length != 3 ) throw new InvalidParameterException("invalid key parts: akbData="+akbData);
		if( StringUtils.length(a[0]) != 8 ) throw new InvalidParameterException("header len not 8: akbData="+akbData);
		if( StringUtils.length(a[2]) != 16 ) throw new InvalidParameterException("mac len not 16: akbData="+akbData);
		return fromDbFormat(a[0]+a[1]+a[2]);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		appendKeyInAtallaFormat(sb);
		return sb.toString();
	}
	
	
}
