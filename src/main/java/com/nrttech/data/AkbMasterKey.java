package com.nrttech.data;

public class AkbMasterKey {
	private String id;
	private int variant;
	private String keycurr;
	private long updepoch;
	private String comment;
	
	public String getId() {
		return id;
	}
	
	public void setId( String id ) {
		this.id = id;
	}
	
	public int getVariant() {
		return variant;
	}
	
	public void setVariant( int variant ) {
		this.variant = variant;
	}
	
	public long getUpdepoch() {
		return updepoch;
	}
	
	public void setUpdepoch( long updepoch ) {
		this.updepoch = updepoch;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment( String comment ) {
		this.comment = comment;
	}

	public String getKeycurr() {
		return keycurr;
	}

	public void setKeycurr( String keycurr ) {
		this.keycurr = keycurr;
	}
}
