package org.Security;
import java.math.BigInteger;
import java.util.Random;


public class RsaEncrypt {
	
	private static final int keySize = 256;
	
	public static BigInteger[] getNewKeyPair(){
		BigInteger p, q, N, phi, e, d;
		Random random = new Random();
		p = BigInteger.probablePrime(keySize, random);
		do{ q = BigInteger.probablePrime(keySize, random);}while(p.equals(q));
		
		N = p.multiply(q);
		phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
			
		do{
			e = nextRandomBigInteger(phi);
		}while(!phi.gcd(e).equals(BigInteger.ONE));
		
		d = e.modInverse(phi);
		return new BigInteger[] {N, d, e};
	}
	
	
	public static String nachrichtVerschlüsseln(BigInteger publicKey, BigInteger modul, String nachricht) {
		BigInteger nachrichtInt = new BigInteger(nachricht.getBytes());
		BigInteger nachrichtVerschlüsselt = nachrichtInt.modPow(publicKey, modul);
		return nachrichtVerschlüsselt.toString();
	}
	
	public static String nachrichtEntschlüsseln(BigInteger privateKey, BigInteger modul, String nachricht) {
		BigInteger nachrichtVerschlüsselt = new BigInteger(nachricht);
		BigInteger nachrichtEntschlüsselt = nachrichtVerschlüsselt.modPow(privateKey, modul);
		byte[] nachrichtBytes = nachrichtEntschlüsselt.toByteArray();
		String klartext = new String(nachrichtBytes);
		return klartext;
	}
	
	 
	 public static BigInteger nextRandomBigInteger(BigInteger n) {
		    Random rand = new Random();
		    BigInteger result = new BigInteger(n.bitLength(), rand);
		    while( result.compareTo(n) > 0 ) {
		        result = new BigInteger(n.bitLength(), rand);
		    }
		    return result;
		}
	
}
