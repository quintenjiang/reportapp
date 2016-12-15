package com.nrttech.atalla;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;


public class AtallaException extends Exception {
	
	private static final long serialVersionUID = -8336479783992488196L;

	public AtallaException( String comment, String response ) {
		super(makeMessage(comment,response));
	}
	
	private static String makeMessage( String comment, String response ) {
		if( StringUtils.isEmpty(response) ) return comment;
		String[] a = StringUtils.splitPreserveAllTokens(response,'#');
		if( a == null || a.length < 3 || !StringUtils.equals(a[0],"<00") || a[1].length()<6 ) {
			return comment + ": unknown response: "+response;
		}
		
		String errType = a[1].substring(0,2);
		String errField = a[1].substring(2,4);
		String swRevision = a[1].substring(4,6);
		int detailedErr = NumberUtils.toInt(a[2],-1);
		if( detailedErr == -1 ) {
			return comment + ": response: "+response+" ErrType("+errType+": "+getErrorTypeText(errType)+") ErrField("+errField+") SwRev("+swRevision+")";
		}
		return comment + ": response: "+response+" ErrType("+errType+": "+getErrorTypeText(errType)+") ErrField("+errField+") SwRev("+swRevision+") DetailedErr("+detailedErr+": "+getDetailedErrorText(detailedErr)+")";
	}

	private static String getErrorTypeText( String errType ) {
		int err = NumberUtils.toInt(errType,-1);
		switch( err ) {
			case  0: return "Response to test message";
			case  1: return "Length out of range";
			case  2: return "Invalid character";
			case  3: return "Value out of range";
			case  4: return "Invalid number of parameters";
			case  5: return "Parity error";
			case  6: return "Key usage error";
			case  7: return "Key usage error";
			case  8: return "Execution error";
			case  9: return "Expecting single-length key";
			case 10: return "Key Length error";
			case 20: return "Serial number set, cannot modify it";
			case 21: return "NSP not in a Security Association, or Serial number not set";
			case 22: return "Non-existent command or option";
			case 23: return "Invalid command or option";
			case 24: return "Incorrect challenge";
			case 25: return "Incorrect Acknowledgement";
			case 26: return "Duplicate command or option";
			case 27: return "No challenge to verify, a command 109 has been received without a prior command 108";
			case 28: return "The configuration string in command 108 is too long";
			case 29: return "Unable to allocate memory for the configuration string";
			case 41: return "ASRM timed out waiting for the response from the NSP";
			case 73: return "Header mismatch";
			case 92: return "Autokey error";
			case 93: return "Factory keys already generated";
			case 94: return "No factory keys generated";
			default: return "Unknown Error";	
		}
	}
	
	private static String getDetailedErrorText( int err ) {
		switch( err ) {
			case 1: return "Invalid command string length";
			case 2: return "Invalid command length";
			case 3: return "Invalid parameter length";
			case 4: return "Passcode length not matched with user data";
			case 5: return "Non empty field - conflicts with other fields";
			case 101: return "Invalid command string format";
			case 102: return "Invalid character";
			case 200: return "Value out of range";
			case 201: return "Invalid command";
			case 202: return "Invalid parameter value";
			case 209: return "Invalid key length specified";
			case 211: return "Invalid ANSI-formatted message authentication code";
			case 212: return "Invalid MAC";
			case 215: return "Invalid checksum on string";
			case 216: return "Value in field is not same as other field";
			case 217: return "Count value not greater than zero";
			case 218: return "Command count table is full";
			case 220: return "No free key slot for RSA key";
			case 230: return "Command does not exist";
			case 301: return "Too many fields";
			case 302: return "Too few response fields";
			case 303: return "Too few fields";
			case 304: return "Initialization vector is missing";
			case 305: return "Wrong combination of keys";
			case 306: return "Invalid number of parameters";
			case 307: return "PAN does not match validation data";
			case 501: return "Table entry in use";
			case 502: return "Table full";
			case 509: return "Key did not have odd parity";
			case 510: return "Specified variant cannot be used";
			case 511: return "KD1 or KD2 check digits do not match expected check digits";
			case 512: return "Single length key not allowed";
			case 513: return "Command 14-5, weak keyy";
			case 514: return "Command 14-5, keys have different length";
			case 516: return "Session key check digits do not match expected check digits";
			case 517: return "Key check digits do not match expected check digits";
			case 518: return "MAC does not match";
			case 519: return "Cannot load key into key table";
			case 520: return "MFK already loaded";
			case 521: return "Not in factory state";
			case 601: return "Non-existent module key entry";
			case 602: return "Non-existent MFK";
			case 603: return "Non-existent KEK";
			case 604: return "Non-existent Pending MFK";
			case 605: return "Incorrect entry of double-length key location";
			case 607: return "Security violation";
			case 612: return "MFK name in command does not match the MFK name";
			case 613: return "Pending MFK name in command does not match the pending MFK name";
			case 623: return "Key location empty";
			case 702: return "Internal error";
			case 708: return "Internal error";
			case 713: return "Internal error";
			case 714: return "BSAFE error";
			case 715: return "DUKPT error";
			case 716: return "Random number generator error";
			case 801: return "Failed hardware function";
			case 802: return "Failed ACE function (general)";
			case 805: return "Failed ACE function (Response returned smaller than minimum)";
			case 806: return "Failed ACE function (Response length invalid)";
			case 807: return "Failed ACE function (Response ID incorrect)";
			case 808: return "Failed ACE function (Response ID invalid)";
			case 809: return "Failed ACE function (Command had NULL error)";
			case 810: return "Failed ACE function (Command had NULL first item)";
			case 811: return "Failed ACE function (Response had NULL item)";
			case 812: return "Failed ACE function (Response had NULL first item)";
			case 813: return "Failed ACE function (Command ID was modified)";
			case 814: return "Failed ACE function (Command has invalid item)";
			case 815: return "Failed ACE function (Command has invalid first item)";
			case 816: return "Failed ACE function (Response has invalid item)";
			case 817: return "Failed ACE function (Response has invalid first item)";
			case 818: return "Failed ACE function (Command contains too many fields)";
			case 819: return "Failed ACE function (Response contains too many fields)";
			case 820: return "Failed ACE function (Command buffer format is invalid)";
			case 821: return "Failed ACE function (Response buffer format is invalid)";
			case 901: return "Expecting a single-length key and received a double length key";
			case 902: return "Expecting a double-length key and received a single length key";
			case 903: return "The double-length key is a replicated single-length key";
			case 2000: return "The serial number is already set, it cannot be modified";
			case 2100: return "The serial number is not loaded";
			case 2101: return "NSP is not in a security association";
			case 2200: return "Non-existent command item in the configuration string";
			case 2300: return "Invalid command item format";
			case 2301: return "Command 105 has not been received";
			case 2303: return "The command is a counted command";
			case 2400: return "The input HASH in command 109 does not match the stored hash";
			case 2500: return "The acknowledgment text is incorrect or missing";
			case 2600: return "Conflicting duplication of a configuration parameter";
			case 2700: return "Command 109 was received without a prior command 108";
			case 7300: return "The variant of the key in table incorrect";
			case 7301: return "The variant for a decimalization table is wrong";
			case 9201: return "RSA keys already exists";
			case 9202: return "Global key data is corrupted";
			case 9203: return "Cannot allocate memory with mymalloc";
			case 9205: return "Failed signature verification";
			case 9208: return "Failed certificate verification";
			case 9209: return "Cannot sign the certificate or bad signature";
			case 9210: return "CBC encryption/decryption failed";
			case 9211: return "No communication key";
			case 9212: return "No session key";
			case 9213: return "MAC computation or verification failed";
			case 9214: return "Invalid buffer data length";
			case 9215: return "Invalid data length inside the header";
			case 9219: return "RSA key generation failed";
			case 9220: return "Certificates do not match";
			case 9240: return "Error writing flash memory";
			case 9241: return "Error writing eerom memory";
			default: return "Unknown Error";	
		}
	}
}
