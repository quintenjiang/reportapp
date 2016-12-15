package test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.ArrayUtils;

public class CryptoHelper {
	
	// Triple DES, triple-length key
	private static final String alg = "DESede/ECB/NoPadding";
	private static final String alg1 = "DESede/CBC/NoPadding";
	
	public static byte[] hexStringToByteArray( String s ) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}	
	
	public static String byteArrayToHexString( byte[] b, int iFrom, int iTo ) {
		final String x = "0123456789ABCDEF";
		StringBuilder sb = new StringBuilder((iTo-iFrom)*2);
	    for( int i=iFrom; i < iTo; ++i ) {
	    	sb.append(x.charAt((b[i]>>4)&0xf));
	    	sb.append(x.charAt(b[i]&0xf));
	    }
	    return sb.toString();
	}	
	
	public static byte[] encrypt( byte[] key, byte[] message ) throws Exception {
		Cipher cipher = Cipher.getInstance(alg);
		SecretKey skey = new SecretKeySpec(keyToTripleKey(key),"DESede");
		cipher.init(Cipher.ENCRYPT_MODE, skey);
		return cipher.doFinal(message);
    }

    public static byte[] decrypt( byte[] key, byte[] message) throws Exception {
    	Cipher decipher = Cipher.getInstance(alg);
		SecretKey skey = new SecretKeySpec(keyToTripleKey(key),"DESede");
    	decipher.init(Cipher.DECRYPT_MODE, skey);
    	return decipher.doFinal(message);
	}
    
    public static byte[] decryptCbc( byte[] iv, byte[] key, byte[] message) throws Exception {
    	Cipher decipher = Cipher.getInstance(alg1);
		SecretKey skey = new SecretKeySpec(keyToTripleKey(key),"DESede");
    	decipher.init(Cipher.DECRYPT_MODE, skey,new IvParameterSpec(iv));
    	return decipher.doFinal(message);
	}
    
    
    public static String getKvv( byte[] key ) throws Exception {
    	byte[] v = encrypt(keyToTripleKey(key), new byte[]{0,0,0,0,0,0,0,0});
		return byteArrayToHexString(v,0,2);
    }
    public static String getKvv6( byte[] key ) throws Exception {
    	byte[] v = encrypt(keyToTripleKey(key), new byte[]{0,0,0,0,0,0,0,0});
		return byteArrayToHexString(v,0,3);
    }
    
    public static byte[] keyToTripleKey( byte[] key0 ) throws Exception {
    	byte[] key;
    	switch( key0.length ) {
	    	case 8:
	    		key = new byte[24];
	    		System.arraycopy(key0, 0, key, 0, 8);
	    		System.arraycopy(key0, 0, key, 8, 8);
	    		System.arraycopy(key0, 0, key, 16, 8);
	    		return key;
	    	case 16:
	    		key = new byte[24];
	    		System.arraycopy(key0, 0, key, 0, 16);
	    		System.arraycopy(key0, 0, key, 16, 8);
	    		return key;
	    	case 24:
	    		return key0;
    	}
    	throw new Exception("Invalid key len="+key0.length+". Expected 8, 16 or 24.");
    }

    public static byte[] keyPortion( byte[] key0, int portion ) throws Exception {
    	byte[] key;
    	switch( key0.length ) {
	    	case 8:
	    		return key0;
	    	case 16:
	    		key = new byte[8];
	    		if( portion == 0 || portion == 2 ) {
	    			System.arraycopy(key0, 0, key, 0, 8);
	    		}
	    		else {
	    			System.arraycopy(key0, 8, key, 0, 8);
	    		}
	    		return key;
	    	case 24:
	    		key = new byte[8];
	    		if( portion == 0 ) {
	    			System.arraycopy(key0, 0, key, 0, 8);
	    		}
	    		else if( portion == 1 ) {
	    			System.arraycopy(key0, 8, key, 0, 8);
	    		}
	    		else {
	    			System.arraycopy(key0, 16, key, 0, 8);
	    		}
	    		return key;
    	}
    	throw new Exception("Invalid key len="+key0.length+". Expected 8, 16 or 24.");
    }

	public static byte[] calcKeyVariant( byte[] key, byte[] variantMsg ) throws Exception {
		byte[] b1 = keyToTripleKey(key);
		if( variantMsg == null ) return b1;
		byte[] b2 = keyToTripleKey(variantMsg);
		byte[] b = new byte[b1.length];
		for( int i=0; i<b.length; ++i ) b[i] = (byte) (b1[i]^b2[i]);
		return b;
	}
	
	public static byte[] calcMac( byte[] key, byte[] data ) throws Exception {
	
		Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
		SecretKey skey = new SecretKeySpec(keyToTripleKey(key),"DESede");
		IvParameterSpec iv = new IvParameterSpec(new byte[]{0,0,0,0,0,0,0,0});
		cipher.init(Cipher.ENCRYPT_MODE,skey,iv);
		byte[] v;
		if( data.length % 8 == 0 ) {
			v = cipher.doFinal(data);
		}
		else {
			cipher.update(data);
			v = cipher.doFinal(new byte[]{0,0,0,0,0,0,0,0},0,8-data.length%8);
		}
		return ArrayUtils.subarray(v,v.length-8,v.length-4);
	}

	public static String byteArrayToHexString( byte[] b ) {
		return byteArrayToHexString(b, 0, b.length);
	}
	
	
	public static byte[] createClearPinblock( String pin, String pan ) {
		byte[] t = new byte[] {(byte) pin.length(),0,0,0,0,0,0,0};
		for( int i=0; i<14; ++i ) {
			int hex = 0xf;
			if( i < pin.length() ) hex = pin.codePointAt(i) & 0xf;
			if( i%2 == 0 ) {
				t[i/2+1] |= (hex<<4);
			}
			else {
				t[i/2+1] |= hex;
			}
		}

		byte[] p = new byte[] {0,0,0,0,0,0,0,0};
		for( int i=0; i<pan.length() && i<12; ++i ) {
			int hex = pan.codePointAt(i) & 0xf;
			if( i%2 == 0 ) {
				p[i/2+2] |= (hex<<4);
			}
			else {
				p[i/2+2] |= hex;
			}
		}

		for( int i=0; i<8; ++i ) t[i] ^= p[i];
		return t;
	}
	
	public static boolean validateKek( String kek ) {
		final String x = "0123456789ABCDEF";
		
		for( int i=0; i < kek.length(); ++i ) {
			char c = kek.charAt(i);
			if(x.indexOf(c, 0) < 0) return false;
		}
	    return true;
	}	
	
	public static String track2ToHex( String clearTrack2 ) {
		StringBuilder sb = new StringBuilder();
		for( char c : clearTrack2.toCharArray() ) {
			if( c >= '0' && c <='9' ) {
				sb.append(c);
				continue;
			}
			if( c == '=' || c == 'D' ) {
				sb.append('D');
				continue;
			}
			if( c == 'F' || c == '?' ) {
				continue;
			}
			if( c == 'E' || c == 'M' ) {
				sb.append('E');
				continue;
			}
			throw new RuntimeException("T2 has invalid char="+c);
		}
		
		if( sb.length() > 0 && sb.charAt(sb.length()-1) != 'F' ) sb.append('F');
		if( sb.length() % 2 != 0 ) sb.append('F');
		return sb.toString();
	}

	public static String hexTotrack2( String hex ) {
		StringBuilder sb = new StringBuilder();
		for( char c : hex.toCharArray() ) {
			if( c >= '0' && c <='9' ) {
				sb.append(c);
				continue;
			}
			if( c == 'D' ) {
				sb.append('=');
				continue;
			}
			if( c == 'F' ) {
				break;
			}
			if( c == 'E' ) {
				sb.append('M');
				continue;
			}
		}
		return sb.toString();
	}

	
	

}
