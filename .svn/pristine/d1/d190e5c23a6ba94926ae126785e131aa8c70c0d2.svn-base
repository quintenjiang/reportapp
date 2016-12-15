package com.nrttech.bezel;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
public class BezelUtils {
	
	static Logger logger = Logger.getLogger( BezelUtils.class );
	
	public static String normalizeSerialNumber( String s ) {
		try {
			return StringUtils.trimToEmpty( s ).toUpperCase();
		}
		catch( Throwable t ) {
			logger.error( "", t );
		}
		return null;
	}

	public static String normalizeCheckDigits( String s ) {
		try {
			String sTrimmed = StringUtils.trimToEmpty( s ).toUpperCase();
			if( StringUtils.containsOnly( sTrimmed, "0123456789ABCDEF" ) ) return sTrimmed;
			throw new Exception("check digits are not hex");
		}
		catch( Throwable t ) {
			logger.error( "", t );
		}
		return null;
	}
	
} 
