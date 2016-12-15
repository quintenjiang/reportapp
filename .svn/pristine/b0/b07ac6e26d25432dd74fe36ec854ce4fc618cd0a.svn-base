package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public abstract class MaxFileProcessor<T> {

	private static Logger logger = Logger.getLogger(MaxFileProcessor.class);

	private static class ColumnDefinition {

		String id;
		int i0;
		int i1;

		@Override
		public String toString() {
			return id + "[" + i0 + "," + (i1 - i0) + "]";
		}

	}

	private static Map<String,ColumnDefinition> parseHeader( String line1, String line2 ) {
		Map<String,ColumnDefinition> res = new HashMap<String,ColumnDefinition>();

		for( int i0 = 0;; ) {

			while( i0 < line2.length() && !"-".equals( StringUtils.substring( line2, i0, i0 + 1 ) ) )
				++i0;
			if( i0 >= line2.length() ) break;

			int i1 = i0 + 1;
			while( i1 < line2.length() && "-".equals( StringUtils.substring( line2, i1, i1 + 1 ) ) )
				++i1;

			ColumnDefinition c = new ColumnDefinition();
			c.i0 = i0;
			c.i1 = i1;
			c.id = StringUtils.trimToEmpty( StringUtils.substring( line1, i0, i1 ) );
			res.put( c.id, c );

			i0 = i1 + 1;

		}
		return res;

	}

	private Map<String,ColumnDefinition> map;
	private String line;

	protected Set<String> getKeySet() {
		return map.keySet();
	}
	
	protected String getById( String id ) {
		ColumnDefinition c = map.get( id );
		if( c == null ) return null;
		return StringUtils.trimToEmpty( StringUtils.substring( line, c.i0, c.i1 ) );
	}
	
	protected String getByIdFailIfBlank( String id, String errMsg ) {
		String s = StringUtils.trimToNull(getById(id));
		if( s == null ) throw new RuntimeException(errMsg);
		return s;
	}
	
	protected String getByIdFailIfBlank( String id ) {
		return getByIdFailIfBlank(id,"Missing "+id);
	}
	
	protected void logError( String s ) {
		logger.error( s + " for line: "+line );
	}
	
	abstract void addElement( Map<String,T> map );

	public Map<String,T> readAll( String fname ) throws Exception {

		Map<String,T> res = new HashMap<>();
		String header = null;
		try( BufferedReader br = new BufferedReader( new FileReader( fname ) ) ) {
			for( line = br.readLine(); line != null; line = br.readLine() ) {
				
				if( StringUtils.contains( line, "70D03 0131921" ) ) {
					System.out.print( "***" );
				}
				
				if( StringUtils.isBlank( line ) ) continue;
				if( map == null ) {
					if( header == null ) {
						header = line;
						continue;
					}
					map = parseHeader( header, line );
					continue;
				}
				try {
					addElement( res );
				}
				catch( Throwable t ) {
					logError( t.getMessage() );
				}
			}
		}
		return res;
	}


}