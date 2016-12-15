package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.Log4jConfigurer;

import com.nrttech.bezel.BezelException;
import com.nrttech.bezel.BezelKeyConvert;
import com.nrttech.bezel.BezelResult;

public final class KeyMain {
	
	private static Logger logger = Logger.getLogger(KeyMain.class);

    private KeyMain() {}

	public static String convertCreated( String created, String createId ) {
		long ts = NumberUtils.toLong( created )*1000;
		if( ts == 0 ) return null;
		if( StringUtils.isBlank( createId ) ) return null;
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis( ts );
		return
			String.format( "%04d%02d%02d %02d%02d%02d",
			 c.get( Calendar.YEAR ),c.get( Calendar.MONTH )+1,c.get( Calendar.DAY_OF_MONTH ),
			 c.get( Calendar.HOUR_OF_DAY ),c.get( Calendar.MINUTE ),c.get( Calendar.SECOND )
			) +
			" "+createId;
	}
	
	private static void  processingKeyDataMasterKeyOnly(final BezelKeyConvert keyConvert, String data) throws Exception {
		//logger.info("processingKeyDataMasterKeyOnly: " + data);

		 String[] a = StringUtils.splitPreserveAllTokens(data, ',');
		 if(a == null || a.length < 3) {
			 logger.info("Incorrect data: " + data);
			 return;
		 }
		 String serialNumberBytes = StringUtils.trimToNull(a[0]);
		 if(serialNumberBytes == null) {
			 logger.info("Incorrect data: " + data);
			 return;
			 
		 }
		 String crypto = StringUtils.trimToNull(a[1]);
		 if(crypto == null || crypto.length() != 36) {
			 logger.info("Incorrect data: " + data);
			 return;
			 
		 }
		 String checkDigits = StringUtils.right(crypto, 4);
		 crypto = StringUtils.left(crypto, 32);
		
		 
			try {
				keyConvert.bezelDoInject( serialNumberBytes, crypto, checkDigits, "" );
			}
			catch( Throwable t ) {
				//logger.info( "Failed ID: " + super.getById( "ID" ) + " : " + t );
				return;
			}
		 
		 
		 //System.out.println("serialNumberBytes:" +serialNumberBytes + " crypto:" + crypto + "  checkDigits:" + checkDigits);
		
		
	}
    public static void main(String[] args) throws Exception {
    	
    	Log4jConfigurer.initLogging("log4j.xml");
    	
    	logger.debug("Initializing Spring context.");
    	try( FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext("*-context.xml") ) {
    		
	    	logger.debug("Spring context initialized.");
	    	final BezelKeyConvert keyConvert = applicationContext.getBean(BezelKeyConvert.class);
	    	
	    	int countProcessed = 0;
	    	String arg="tmp_20161209_ex.2.txt";
//	    	for( String arg: args ) {
	    		
	    		File f = new File(arg);
	    		if( !f.exists() ) throw new BezelException( BezelResult.BAD_PARAM, "File not found: "+arg );
	    		if( !f.isFile() ) throw new BezelException( BezelResult.BAD_PARAM, "Not a file: "+arg );
	    		String thisLine = null;
	    		try{
	    	         // open input stream test.txt for reading purpose.
	    	         BufferedReader br = new BufferedReader(new FileReader(arg));
	    	         while ((thisLine = br.readLine()) != null) {
	    	            //System.out.println(thisLine);
	    	            processingKeyDataMasterKeyOnly(keyConvert, thisLine);
	    	         }       
	    	      }catch(Exception e){
	    	         e.printStackTrace();
	    	      }
	    		
	    		
/*	    		logger.info( "Processing file: " + arg );
	    		
	    		Map<String,String> result = new MaxFileProcessor<String>() {

					@Override
					void addElement( Map<String,String> map ) {
						
						String serialNumberBytes = StringUtils.upperCase( StringUtils.substringBetween( super.getById( "ID" ), "x'", "'" ) );
						if( StringUtils.isBlank(serialNumberBytes) ) {
							logger.info( "Ignored empty ID: " + super.getById( "ID" ) );
							return;
						}
						
						if( !StringUtils.containsOnly( serialNumberBytes, "0123456789ABCDEF" ) ) {
							logger.info( "Ignored non-hex ID: " + super.getById( "ID" ) );
						}
						
						String sn = new String(CryptoHelper.hexStringToByteArray( serialNumberBytes ));
						String cryptoAndCheckDigits = StringUtils.upperCase(super.getById( "CRYPT4" ));
						
						String crypto = StringUtils.substringBetween( cryptoAndCheckDigits, "CRYPT=\"", "\"" );
						String checkDigits = StringUtils.left(StringUtils.substringBetween( cryptoAndCheckDigits, "CHK=\"", "\"" ),4);
						
						if( (StringUtils.length( crypto ) != 16 &&  StringUtils.length( crypto ) != 32) || !StringUtils.containsOnly( crypto, "0123456789ABCDEF" ) ) {
							logger.info( "Ignored ID (bad or empty Crypt): " + super.getById( "ID" ) );
							return;
						}
						
						if( StringUtils.length( checkDigits ) != 4 || !StringUtils.containsOnly( checkDigits, "0123456789ABCDEF" ) ) {
							logger.info( "Ignored ID (bad or empty CheckDigits): " + super.getById( "ID" ) );
							return;
						}
						
						String c = convertCreated(getById("CREATED"),getById("CREATEID"));
						String m = convertCreated(getById("MODIFIED"),getById("MODIFYID"));
						
						String info = "";
						if( c != null ) info += "CREATED "+c;
						if( m != null ) {
							if( StringUtils.isNotEmpty( info ) ) info += "/";
							info += "MODIFIED "+m;
						}
						
						map.put( sn, crypto );
						try {
							keyConvert.bezelDoInject( sn, crypto, checkDigits, info );
						}
						catch( Throwable t ) {
							logger.info( "Failed ID: " + super.getById( "ID" ) + " : " + t );
							return;
						}
						logger.info( "OK: sn=" + sn );
					}
				}.readAll( f.toString() );
	    		++countProcessed;
//	    	}
	    	
	    	if( countProcessed == 0 ) {
		    	logger.info("No files were processed. Add a file as a parameter");
	    	}*/
    	}
    	catch( Throwable t ) {
    		logger.info( "Failed", t );
    	}

		return;
    }
    
}
