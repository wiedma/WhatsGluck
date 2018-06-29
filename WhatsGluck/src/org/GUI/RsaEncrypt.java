package org.GUI;

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
			
//		do(){
//			
//		}while();
		
		return null;
	}
	
	 public static int ggT(int zahl1, int zahl2) {
		   while (zahl2 != 0) {
		     if (zahl1 > zahl2) {
		       zahl1 = zahl1 - zahl2;
		     } else {
		       zahl2 = zahl2 - zahl1;
		     }
		   }
		   return zahl1;
		 }
	
}
