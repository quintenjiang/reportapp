package test;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.nrttech.bezel.BezelKeyConvert;

public class TeMain {

	public static void main(String[] args) {
		try( FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext("*-context.xml") ) {
			final BezelKeyConvert keyConvert = applicationContext.getBean(BezelKeyConvert.class);
			String sn = "48240000752";
			String crypto = "1CB96B214287A65CBA1F96224E91DED0";
			String checkDigits = "564B";
			try {
				keyConvert.bezelDoInject( sn, crypto, checkDigits, "" );
			}
			catch( Throwable t ) {
				//logger.info( "Failed ID: " + super.getById( "ID" ) + " : " + t );
				return;
			}
			
			
		}
    	catch( Throwable t ) {
    		//logger.info( "Failed", t );
    	}

	}

}
