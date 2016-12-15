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

public final class CamelConsoleMain {
	
	private static Logger logger = Logger.getLogger(CamelConsoleMain.class);

    private CamelConsoleMain() {}

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
		 
	}
	
    public static void main(String[] args) throws Exception {
    	
    	Log4jConfigurer.initLogging("classpath:conf/log4j.xml");
    	
    	logger.debug("Initializing Spring context.");
    	try( FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext("classpath:conf/*-context.xml") ) {
    		
	    	logger.debug("Spring context initialized.");
	    	final BezelKeyConvert keyConvert = applicationContext.getBean(BezelKeyConvert.class);
	    	
	    	for( String arg: args ) {
	    		
	    		File f = new File(arg);
	    		if( !f.exists() ) throw new BezelException( BezelResult.BAD_PARAM, "File not found: "+arg );
	    		if( !f.isFile() ) throw new BezelException( BezelResult.BAD_PARAM, "Not a file: "+arg );
	    		
	    		logger.info( "Processing file: " + arg );
	    		
	    		String thisLine = null;
	    		BufferedReader br = null;
	    		try{
	    	          br = new BufferedReader(new FileReader(arg));
	    	         while ((thisLine = br.readLine()) != null) {
	    	            processingKeyDataMasterKeyOnly(keyConvert, thisLine);
	    	         }       
	    	      } catch(Exception e){
	    	         e.printStackTrace();
	    	      } finally {
	    	    	  br.close();
	    	      }
	    	}

    	}
    	catch( Throwable t ) {
    		logger.info( "Failed", t );
    	}

		return;
    }
    
}
