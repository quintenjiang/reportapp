package com.nrttech.bezel;

import org.apache.commons.lang3.StringUtils;

	
public enum BezelResult {
	OK,
	BAD_PARAM, BAD_SIGNATURE, SERVER_ERROR, BAD_SETUP, BAD_FLOW, BAD_CHECK_DIGITS, ALREADY_INJECTED,
	;
	
	String toResponseString( String details ) {
		StringBuilder sb = new StringBuilder();
		switch(this) {
			case OK: sb.append( "00 Success" ); break;
			case BAD_PARAM: sb.append( "01 Bad parameter" ); break;
			case BAD_SIGNATURE: sb.append( "02 Bad signature" ); break;
			case SERVER_ERROR: sb.append( "03 Server Error" ); break;
			case BAD_SETUP: sb.append( "04 Server setup problem" ); break;
			case BAD_FLOW: sb.append( "05 Wrong command flow" ); break;
			case BAD_CHECK_DIGITS: sb.append( "06 Check digits did not match" ); break;
			case ALREADY_INJECTED: sb.append( "07 Key already injected" ); break;
		}
		if( StringUtils.isNotBlank( details ) ) {
			sb.append( " (" );
			sb.append( details );
			sb.append( ")" );
		}
		return sb.toString();
	}
	
} 
