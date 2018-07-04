package org.Main;
import java.math.BigInteger;
import org.GUI.Chatfenster;
import org.Security.RsaEncrypt;


public class Main {
	
	public static void main(String[] args) {
		new Chatfenster();
		System.out.println(RsaEncrypt.teilerFremd(BigInteger.valueOf(537946478), BigInteger.valueOf(468571632)));
	}

}
