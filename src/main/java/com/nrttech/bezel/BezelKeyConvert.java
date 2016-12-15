package com.nrttech.bezel;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nrttech.atalla.AkbKey;
import com.nrttech.atalla.AtallaCommandProcessor;
import com.nrttech.services.IdpServices;
	
public class BezelKeyConvert {
	
	private static Logger logger = Logger.getLogger(BezelKeyConvert.class);
	
	public void bezelDoInject(
		String serialNumber,
		String bezelKeyMasterKey,
		String checkDigits,
		String comment
	) {

		String serialNumberBytes = BezelUtils.normalizeSerialNumber( serialNumber );
		String checkDigitsBytes = BezelUtils.normalizeCheckDigits( checkDigits );
		
		if( serialNumberBytes == null ) throw new BezelException( BezelResult.BAD_PARAM, "serial number" );
		
//	    List<AkbMasterKey> list = idpServices.queryBezelKeyBySerialNumber( serialNumberBytes );
//	    if( list.size() > 1 ) {
//	    	throw new BezelException( BezelResult.BAD_SETUP, "found "+list.size()+" keys (max 1 expected)" );
//	    }
//	    if( list.size() == 1 ) {
//	    	String checkDigitsCurrent = AkbKey.fromDbFormat( list.get( 0 ).getKeycurr() ).getKvv4();
//	    	if( StringUtils.equals( checkDigitsBytes, checkDigitsCurrent ) ) throw new BezelException( BezelResult.ALREADY_INJECTED, "check digits match" );
//	    	throw new BezelException( BezelResult.ALREADY_INJECTED, "!!! check digits different" );
//	    }

		AkbKey ktk = AkbKey.fromDbFormat( transportKey );
		AkbKey converted;
		try {
			converted = atallaCommandProcessor.importExternalKey( ktk, 0, bezelKeyMasterKey );
		}
		catch( Throwable t ) {
			System.out.println("cannot import key for bezel serial number:" +  serialNumber);
			logger.error( "cannot import key for bezel serial number:" +  serialNumber );
			throw new BezelException( BezelResult.BAD_PARAM, "cannot import key: " + t );
		}
		
		if( !StringUtils.startsWith( checkDigitsBytes, converted.getKvv4() ) ) {
			//throw new BezelException( BezelResult.BAD_CHECK_DIGITS, "expected="+checkDigitsBytes+" actual="+converted.getKvv4() );
			//System.err.println("FAIL:" +serialNumber + "  bezelKeyMasterKey:" + bezelKeyMasterKey + "   checkDigits:" + checkDigits );
			System.out.println("cannot import key for bezel serial number:" +  serialNumber);
			logger.error( "cannot import key for bezel serial number:" +  serialNumber );
		} else {
			System.out.println("import key for bezel serial number:" +  serialNumber);
			logger.error( "import key for bezel serial number:" +  serialNumber );
		}
	    idpServices.updateAkbKey(serialNumber,comment,converted.getAkb76());
	}

	@Autowired
	AtallaCommandProcessor atallaCommandProcessor;
	
	@Autowired
	String transportKey;
	
	@Autowired
	IdpServices idpServices;
}
