package org.Security;
import java.math.BigInteger;
import java.util.Random;


public class RsaEncrypt {
	
	private static final int keySize = 64;
	
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
	
//	 private static boolean teilerFremd(BigInteger zahl1, BigInteger zahl2) {
//		   while (!zahl2.equals(BigInteger.ZERO)) {
//		     if (zahl1.max(zahl2) != zahl2) {
//		       zahl1 = zahl1.subtract(zahl2);
//		     } else {
//		       zahl2 = zahl2.subtract(zahl1);
//		     }
//		   }
//		   return zahl1.equals(BigInteger.ONE);
//		 }
	 
	 public static BigInteger nextRandomBigInteger(BigInteger n) {
		    Random rand = new Random();
		    BigInteger result = new BigInteger(n.bitLength(), rand);
		    while( result.compareTo(n) > 0 ) {
		        result = new BigInteger(n.bitLength(), rand);
		    }
		    return result;
		}
	 
//	 public static BigInteger[] extendedEuklid(BigInteger a, BigInteger b) {
//		 if(a.compareTo(b) < 0) {
//			 BigInteger c = a;
//			 a = b;
//			 b = c;
//		 }
//		 
//		 BigInteger q, u = BigInteger.ONE, v = BigInteger.ZERO, s = BigInteger.ZERO, t = BigInteger.ONE;
//		 BigInteger aneu, bneu, vneu, sneu, tneu, uneu;
//		 
//		 while(!b.equals(BigInteger.ZERO)) {
//			 q = a.divide(b);
//			 aneu = new BigInteger(b.toString());
//			 bneu = a.divideAndRemainder(b)[1];
//			 uneu = new BigInteger(s.toString());
//			 vneu = new BigInteger(t.toString());
//			 sneu = u.subtract(q.multiply(s));
//			 tneu = v.subtract(q.multiply(t));
//			 a = new BigInteger(aneu.toString());
//			 b = new BigInteger(bneu.toString());
//			 u = new BigInteger(uneu.toString());
//			 v = new BigInteger(vneu.toString());
//			 s = new BigInteger(sneu.toString());
//			 t = new BigInteger(tneu.toString());
//		 }
//		 
//		 return new BigInteger[] {a, u, v};
//		 
//	 }
	
}
