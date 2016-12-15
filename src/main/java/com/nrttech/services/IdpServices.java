package com.nrttech.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.nrttech.atalla.AkbKey;
import com.nrttech.bezel.BezelException;
import com.nrttech.bezel.BezelResult;
import com.nrttech.data.AkbMasterKey;
import com.nrttech.data.DataInterface;

@Transactional
public class IdpServices  {

	private static Logger logger = Logger.getLogger(IdpServices.class);

	@Autowired
	DataInterface dataInterface;
	
//	public List<AkbMasterKey> queryBezelKeyBySerialNumber( String serialNumber ) {
//		logger.debug("queryBezelKeyBySerialNumber");
//		return dataInterface.queryAkbMasterKeyById( "T.MEI."+serialNumber+".M" );
//	}
	
	public void updateAkbKey( String serialNumber, String comment, String akb76 ) {
		logger.debug("updateAkbKey");
//		List<AkbMasterKey> list = queryBezelKeyBySerialNumber( serialNumber );
//		if( list != null && list.size() > 0 ) throw new BezelException( BezelResult.ALREADY_INJECTED, "check digits "+AkbKey.fromDbFormat(list.get( 0 ).getKeycurr()).getKvv4() );
		
//		AkbMasterKey k = new AkbMasterKey();
//		k.setKeycurr( akb76 );
//		k.setId( "T.MEI."+serialNumber+".M" );
//		k.setVariant( 0 );
//		k.setUpdepoch( System.currentTimeMillis() );
//		k.setComment( StringUtils.left(comment,32) );
		//dataInterface.insertAkbMasterKeyById( k );
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sn", serialNumber);
		map.put("key", akb76);
		
		dataInterface.insertAkbMasterKeyById( map );
	}
}
