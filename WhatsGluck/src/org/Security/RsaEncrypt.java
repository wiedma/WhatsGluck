package org.Security;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.math.BigInteger;
import java.util.Random;


public class RsaEncrypt {
	
	private static final int keySize = 64;
	
	public static KeyPair getNewKeyPair(){
		BigInteger p, q, N, phi, e;
		Random random = new Random();
		p = BigInteger.probablePrime(keySize, random);
		do{ q = BigInteger.probablePrime(keySize, random);}while(p.equals(q));
		
		N = p.multiply(q);
		phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
			
		do{
			e = nextRandomBigInteger(phi);
		}while(teilerFremd(phi, e));
		
		return null;
	}
	
	 public static boolean teilerFremd(BigInteger zahl1, BigInteger zahl2) {
		   while (!zahl2.equals(BigInteger.ZERO)) {
		     if (zahl1.max(zahl2) != zahl2) {
		       zahl1 = zahl1.subtract(zahl2);
		     } else {
		       zahl2 = zahl2.subtract(zahl1);
		     }
		   }
		   return zahl1.equals(BigInteger.ONE);
		 }
	 
	 public static BigInteger nextRandomBigInteger(BigInteger n) {
		    Random rand = new Random();
		    BigInteger result = new BigInteger(n.bitLength(), rand);
		    while( result.compareTo(n) < 0 ) {
		        result = new BigInteger(n.bitLength(), rand);
		    }
		    return result;
		}
	
}
