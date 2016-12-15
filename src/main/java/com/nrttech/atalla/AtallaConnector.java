package com.nrttech.atalla;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

public class AtallaConnector {
	
	private static Logger logger = Logger.getLogger(AtallaConnector.class);
	
    int port;
    String address;
    
    int toConnectMsec = 10000;
    int toMessageMsec = 1000;
    int toReconnectMsec = 1000;
    
    public AtallaConnector( String address, int port ) {
    	this.address = address;
    	this.port = port;
    }
    
    public AtallaConnector( String address ) {
    	this.address = StringUtils.trim( StringUtils.substringBefore( address, ":" ) );
    	this.port = NumberUtils.toInt( StringUtils.substringAfter( address, ":" ) );
    }
    
	private void connectChannel( SocketChannel channel ) throws Exception {
		
		Selector selector = null;
		try {
			selector = Selector.open();
			long tStart = System.currentTimeMillis();
			channel.register(selector,SelectionKey.OP_CONNECT);
			channel.connect(new InetSocketAddress(address,port));
			
			for(;;) {
				selector.select(toConnectMsec);
				if( System.currentTimeMillis()-tStart >= toConnectMsec ) {
					// Connect timeout
					throw new Exception("Connect timeout");
				}
				
				for( Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); it.remove() ) {
					SelectionKey key = it.next();
					if( key.isConnectable() ) {
						// Connection is done
						if( !channel.finishConnect() ) throw new Exception("Connect failed");
						return;
					}
				}
			}
		}
		finally {
			if( selector != null ) {
				selector.close();
			}
		}
	}

	
	/**
	 * @param channel
	 * @return response
	 * @throws Exception 
	 */
	public String atallaCommand( SocketChannel channel, String request ) throws Exception {
		
		Selector selectorRead = null;
		Selector selectorWrite = null;
		try {

		    ByteBuffer outMessage = ByteBuffer.wrap(request.getBytes());
	        ByteBuffer inMessage = ByteBuffer.allocate(1024);
		    
			selectorRead = Selector.open();
			selectorWrite = Selector.open();
			channel.register(selectorRead,SelectionKey.OP_READ);
			channel.register(selectorWrite,SelectionKey.OP_WRITE);
			
			for( long tckEnd = System.currentTimeMillis()+10000; System.currentTimeMillis()<tckEnd; ) {
				
				// See if we need to send anything
				if( outMessage.remaining() != 0 ) {
					selectorWrite.select(1000);
					for( Iterator<SelectionKey> it = selectorWrite.selectedKeys().iterator(); it.hasNext(); it.remove() ) {
						SelectionKey key = it.next();
						if( !key.isWritable() ) continue;
						channel.write(outMessage);
					}
					continue;
				}
				
				// Everything was written - try reading
				selectorRead.select(1000);
				for( Iterator<SelectionKey> it = selectorRead.selectedKeys().iterator(); it.hasNext(); it.remove() ) {
				
					SelectionKey key = it.next();
					if( !key.isReadable() ) continue;
					
					if( inMessage.remaining() == 0 ) {
						throw new Exception("Received message is too long");
					}
					
					// Receive message body here
					int len = channel.read(inMessage);
					if( len < 0 ) {
						throw new Exception("Comm error");
					}
					
					for( int j=inMessage.position()-len; j<inMessage.position(); ++j ) {
						if( inMessage.get(j) == '>' ) {
							// Message end
							for( int i=0; i<j; ++i ) {
								if( inMessage.get(i) == '<' ) {
									// Message start
									return new String(inMessage.array(),i,j-i+1);
								}
							}
							throw new Exception("Message end w/o start");
						}
					}
					
					// Wait more
					continue;
				}
			}
			throw new Exception("Message TO");
		}
		finally {
			if( selectorRead != null ) {
				try {
					selectorRead.close();
				}
				catch (Throwable e) {
					logger.error("Selector close failed",e);
				}
			}
			if( selectorWrite != null ) {
				try {
					selectorWrite.close();
				}
				catch (Throwable e) {
					logger.error("Selector close failed",e);
				}
			}
		}
	}
	
	private SocketChannel connect() throws Exception {
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		connectChannel(channel);
		return channel;
	}
		
	private void disconnect( SocketChannel channel ) {
		if( channel == null ) return;	
		try {
			channel.shutdownOutput();
			channel.shutdownInput();
		}
		catch( Throwable t ) {
			logger.error("channel shutdown failed", t);
		}
		finally {
			try {
				channel.close();
			}
			catch( Throwable t ) {
				logger.error("channel close failed", t);
			}
		}
	}
	
	private SocketChannel channel = null;
	
	public String command( String rq ) throws Exception {
		if( channel == null ) channel = connect();
		try {
			return atallaCommand(channel,rq);
		}
		finally {
			disconnect(channel);
			channel = null;
		}
	}
	
}
