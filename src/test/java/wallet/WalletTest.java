package wallet;

import java.security.Security;

import org.junit.Before;
import org.junit.Test;

import data.Transaction;
import data.Wallet;
import util.CryptoUtil;

public class WalletTest {


	@Before
	public void setUp() {
		//Setup Bouncy castle as a Security Provider
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
	}

	@Test
	public void cria_blocos() {
		//Create the new wallets
		Wallet walletA = new Wallet();
		Wallet walletB = new Wallet();
		
		//Test public and private keys
		System.out.println("Private and public keys:");
		System.out.println(CryptoUtil.getStringFromKey(walletA.getPrivateKey()));
		System.out.println(CryptoUtil.getStringFromKey(walletA.getPublicKey()));
		
		//Create a test transaction from WalletA to walletB 
		Transaction transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 5, null);
		transaction.generateSignature(walletA.getPrivateKey());
		
		//Verify the signature works and verify it from the public key
		System.out.println("Is signature verified");
		System.out.println(transaction.verifySignature());

	}

}
