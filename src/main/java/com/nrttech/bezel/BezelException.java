package com.nrttech.bezel;


public class BezelException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BezelException( BezelResult bezelResult, String msg ) {
		super(bezelResult.toResponseString( msg ));
	}
	
}
