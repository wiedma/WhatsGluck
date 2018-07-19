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
	
	
	public static String nachrichtVerschl�sseln(BigInteger publicKey, BigInteger modul, String nachricht) {
		BigInteger nachrichtInt = new BigInteger(nachricht.getBytes());
		BigInteger nachrichtVerschl�sselt = nachrichtInt.modPow(publicKey, modul);
		return nachrichtVerschl�sselt.toString();
	}
	
	public static String nachrichtEntschl�sseln(BigInteger privateKey, BigInteger modul, String nachricht) {
		BigInteger nachrichtVerschl�sselt = new BigInteger(nachricht);
		BigInteger nachrichtEntschl�sselt = nachrichtVerschl�sselt.modPow(privateKey, modul);
		byte[] nachrichtBytes = nachrichtEntschl�sselt.toByteArray();
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
