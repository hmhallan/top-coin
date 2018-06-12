package util;

import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class CryptoUtil {
	
		/**
		 * Aplica o SHA-256 em uma entrada 
		 * @param input String de entrada
		 * @return entrada com SHA-256
		 */
		public static String applySha256(String input){		
			try {
				
				MessageDigest digest = MessageDigest.getInstance("SHA-256");	        
				byte[] hash = digest.digest(input.getBytes("UTF-8"));	        
				
				StringBuffer hexString = new StringBuffer();
				for (int i = 0; i < hash.length; i++) {
					String hex = Integer.toHexString(0xff & hash[i]);
					if(hex.length() == 1) hexString.append('0');
					hexString.append(hex);
				}
				return hexString.toString();
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}	
		
		/**
		 * Assina um input digitalmente 
		 * @param privateKey chave privada da assinatura
		 * @param input input a ser assinado
		 * @return assinatura
		 */
		public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
			Signature dsa;
			byte[] output = new byte[0];
			try {
				dsa = Signature.getInstance("ECDSA", "BC");
				dsa.initSign(privateKey);
				byte[] strByte = input.getBytes();
				dsa.update(strByte);
				byte[] realSig = dsa.sign();
				output = realSig;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return output;
		}
		
		/**
		 * Verifica se uma assinatura é válida
		 * @param publicKey chave publica da assinatura
		 * @param data dados
		 * @param signature assinatura digital
		 * @return true/false
		 */
		public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
			try {
				Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
				ecdsaVerify.initVerify(publicKey);
				ecdsaVerify.update(data.getBytes());
				return ecdsaVerify.verify(signature);
			}catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		/**
		 * Converte uma chave em String
		 * @param key chave
		 * @return string base 64
		 */
		public static String getStringFromKey(Key key) {
			return Base64.getEncoder().encodeToString(key.getEncoded());
		}

}
