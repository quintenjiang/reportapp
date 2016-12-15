package com.nrttech.atalla;

import org.apache.commons.lang3.StringUtils;




public class AtallaCommandProcessor {
	
	private AtallaConnector connector;
	
	public AtallaCommandProcessor( AtallaConnector connector ) {
		this.connector = connector;
	}
	
	public AkbKey[] generateKey( String header, AkbKey kek ) throws Exception {
		
		//<10#1MDNE0I0##D#>
		StringBuilder sb = new StringBuilder();
		sb.append("<10#");
		sb.append(header);
		sb.append("#");
		kek.appendKeyInAtallaFormat(sb);
		sb.append("#D#>");
		
		String response = connector.command(sb.toString());
		
		String[] a = StringUtils.splitPreserveAllTokens(response,'#');
		
		//<20#1MDNE0I0,64D0EC5A9A31EF278D0C617B718CB0F4B0CC391A7CDC6C50,9B12E17FE24F181C##AA7EBE#>		
		if( a.length >= 4 && StringUtils.equals(a[0],"<20") && StringUtils.equals(a[a.length-1],">") ) {
			return new AkbKey[] {
					AkbKey.fromAtallaResponse(a[1],a[3]),
					AkbKey.fromAtallaResponse(a[2],a[3]),
			};
		}
		
		throw new Exception(response);
	}
	
	public AkbKey[] generateKeySingleDes( String header, AkbKey kek ) throws Exception {
		
		//<10#1MDNE0I0##S#>
		StringBuilder sb = new StringBuilder();
		sb.append("<10#");
		sb.append(header);
		sb.append("#");
		kek.appendKeyInAtallaFormat(sb);
		sb.append("#S#>");
		
		String response = connector.command(sb.toString());
		
		String[] a = StringUtils.splitPreserveAllTokens(response,'#');
		
		//<20#1MDNE0I0,64D0EC5A9A31EF278D0C617B718CB0F4B0CC391A7CDC6C50,9B12E17FE24F181C##AA7EBE#>		
		if( a.length >= 4 && StringUtils.equals(a[0],"<20") && StringUtils.equals(a[a.length-1],">") ) {
			return new AkbKey[] {
					AkbKey.fromAtallaResponse(a[1],a[3]),
					AkbKey.fromAtallaResponse(a[2],a[3]),
			};
		}
		
		throw new Exception(response);
	}
	
	public  AkbKey importExternalKey( AkbKey kek, int variant, String hexKey ) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append("<11B#");
		sb.append(variant);
		sb.append("#");
		sb.append(hexKey);
		sb.append("#");
		kek.appendKeyInAtallaFormat(sb);
		sb.append("#>");

		//<21B#1MDNN000,DF8FDBD737F42167F4958BE468B2F60E78CEA915237E249C,9844E3D30F39FA36#E93075#>
		String rs = connector.command(sb.toString());
		String[] a = StringUtils.splitPreserveAllTokens(rs, '#');
		
		if( a == null ) throw new AtallaException("no response",null);
		if( a.length < 4 || !StringUtils.equals(a[0],"<21B") ) throw new AtallaException(sb.toString(),rs);
		
		return AkbKey.fromAtallaResponse(a[1],a[2]);
	}
	
	public String generateRandom( ) throws Exception {
		String rs = connector.command("<93#H#32#>");
		String[] a = StringUtils.splitPreserveAllTokens(rs, '#');
		if( a == null ) throw new AtallaException("no response",null);
		
		return a[1];
		
	}
	
	public  AkbKey generateMasterKey1( ) throws Exception {

		String rs = connector.command("<10#1KDEE000##D#>");
		String[] a = StringUtils.splitPreserveAllTokens(rs, '#');
		
		if( a == null ) throw new AtallaException("no response",null);
		if( a.length < 5 || !StringUtils.equals(a[0],"<20") ) throw new AtallaException("<10#1KDEE000##D#>",rs);
		return AkbKey.fromAtallaResponse(a[1], a[3]);
	}
	
	public  String exportExternalKey(  AkbKey masterKey,  AkbKey workingKey ) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<1A#0#");
		masterKey.appendKeyInAtallaFormat(sb);
		sb.append("#");
		workingKey.appendKeyInAtallaFormat(sb);
		sb.append("#>");

		//<2A#653E5C6FEA9CD922235B8C0E321CA9CF#7919C2#>
		String rs = connector.command(sb.toString());
		String[] a = StringUtils.splitPreserveAllTokens(rs, '#');
		
		if( a == null ) throw new AtallaException("no response",null);
		if( a.length < 4 || !StringUtils.equals(a[0],"<2A") ) throw new AtallaException(sb.toString(),rs);
		if( !StringUtils.startsWith(a[2],workingKey.getKvv4()) ) throw new AtallaException("working key KVV mismacth",null);
		
		return a[1];
	}
	
	public boolean validateSignature( String dataHex, String signature, AkbKeyRsa kek ) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		sb.append("<125#0#3#1#");
		sb.append(dataHex.length()/2);
		sb.append("#");
		sb.append(dataHex);
		sb.append("##");
		sb.append(signature);
		sb.append("#");
		kek.appendKeyInAtallaFormat(sb);
		sb.append("#>");
		
		String response = connector.command(sb.toString());
		String[] a = StringUtils.splitPreserveAllTokens(response,'#');
		
		//<225#0#Y#MFK1#>
		if( a.length >= 4 && StringUtils.equals(a[0],"<225") && StringUtils.equals(a[a.length-1],">") ) {
			return StringUtils.equalsIgnoreCase( a[2], "Y" );
		}
		
		throw new Exception(response);
	}

	public AkbKeyRsa importDevicePublicKey( String publicExponentHex, String modulusHex, String signedDataHex, String signatureHex, AkbKeyRsa akbCaPu ) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<123#K#3#1#");
		sb.append(publicExponentHex);
		sb.append("#");
		sb.append(modulusHex);
		sb.append("#");
		sb.append(signedDataHex);
		sb.append("#");
		sb.append(signatureHex);
		sb.append("#");
		akbCaPu.appendKeyInAtallaFormat(sb);
		sb.append("#>");
		
		String response = connector.command(sb.toString());
		String[] a = StringUtils.splitPreserveAllTokens(response,'#');
		
		if( a.length >= 3 && StringUtils.equals(a[0],"<223") && StringUtils.equals(a[a.length-1],">") ) {
			return AkbKeyRsa.fromAtallaResponse( a[1] );
		}
		throw new Exception(response);
	}

	public String[] generateMasterKeyCryptogram( AkbKeyRsa akbRsaBezelPu ) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<12F#D#0##");
		akbRsaBezelPu.appendKeyInAtallaFormat(sb);
		sb.append("#>");
		
		String response = connector.command(sb.toString());
		String[] a = StringUtils.splitPreserveAllTokens(response,'#');
		
		if( a.length >= 6 && StringUtils.equals(a[0],"<22F") && StringUtils.equals(a[a.length-1],">") ) {
			return new String[] {
				AkbKey.fromAtallaResponse( a[2], a[3] ).getAkb76(),
				a[4],a[3]
			};
		}
		throw new Exception(response);
	}

	public String signMasterKeyCryptogram( String dataHex, AkbKeyRsa akbKimsPr ) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<124#0#3#0#");
		sb.append(dataHex.length()/2);
		sb.append("#");
		sb.append(dataHex);
		sb.append("##");
		akbKimsPr.appendKeyInAtallaFormat(sb);
		sb.append("#>");
		
		String response = connector.command(sb.toString());
		String[] a = StringUtils.splitPreserveAllTokens(response,'#');
		
		if( a.length >= 4 && StringUtils.equals(a[0],"<224") && StringUtils.equals(a[a.length-1],">") ) {
			return a[2];
		}
		throw new Exception(response);
	}	
	

}
