package com.nrttech.atalla;

import java.security.InvalidParameterException;

import org.apache.commons.lang3.StringUtils;

public class AkbKey {
	
	public String getHeader() {
		return akb76.substring(0,8);
	}

	public String getKey() {
		return akb76.substring(8,8+48);
	}

	public String getMac() {
		return akb76.substring(8+48,8+48+16);
	}

	public String getKvv4() {
		return akb76.substring(8+48+16,8+48+16+4);
	}

	private String akb76;
	public String getAkb76() {
		return akb76;
	}
	
	// 1PUNN0I0 DFAD3A01ED3543918BAC43C93FC179468B679C7E28FEA4BB B6AFE7C5E0A18120 D2B9
	static public AkbKey fromDbFormat( String akb76 ) {
		if( StringUtils.length(akb76) != 76 ) throw new InvalidParameterException("akb76="+akb76);
		AkbKey k = new AkbKey();
		k.akb76 = akb76;
		return k;
	}
	
	public void appendKeyInAtallaFormat( StringBuilder sb ) {
		sb.append(getHeader());
		sb.append(',');
		sb.append(getKey());
		sb.append(',');
		sb.append(getMac());
	}
	
	static public AkbKey fromAtallaResponse( String akbData, String kvv ) {
		String[] a = StringUtils.splitPreserveAllTokens(akbData,',');
		if( a == null || a.length != 3 ) throw new InvalidParameterException("invalid key parts: akbData="+akbData);
		if( StringUtils.length(a[0]) != 8 ) throw new InvalidParameterException("header len not 8: akbData="+akbData);
		if( StringUtils.length(a[1]) != 48 ) throw new InvalidParameterException("key len not 48: akbData="+akbData);
		if( StringUtils.length(a[2]) != 16 ) throw new InvalidParameterException("mac len not 16: akbData="+akbData);
		
		if( StringUtils.length(kvv) < 4 ) throw new InvalidParameterException("too shrt kvv="+kvv);
		return fromDbFormat(a[0]+a[1]+a[2]+kvv.substring(0,4));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(100);
		appendKeyInAtallaFormat(sb);
		sb.append('[');
		sb.append(getKvv4());
		sb.append(']');
		return sb.toString();
	}
	
	
}
