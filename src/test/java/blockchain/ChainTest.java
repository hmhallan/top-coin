package blockchain;

import java.security.Security;

import org.junit.Before;
import org.junit.Test;

import data.Wallet;
import singleton.Blockchain;

public class ChainTest {

	@Before
	public void setUp() {
		//Setup Bouncy castle as a Security Provider
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
	}
	
	@Test
	public void cria_blocos() {
		
		Wallet walletA = new Wallet();
		Wallet walletB = new Wallet();
		
		Block genesis = Blockchain.get().genesis( walletA );
		
		//testing
		Block block1 = new Block(genesis.getHash());
		System.out.println("\nWalletA possui: " + walletA.getBalance());
		System.out.println("\nWalletA transferindo (40) para WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 40f));
		Blockchain.get().addBlock(block1);
		
		System.out.println("\nWalletA possui: " + walletA.getBalance());
		System.out.println("WalletB possui: " + walletB.getBalance());
		
		Block block2 = new Block(block1.getHash());
		System.out.println("\nWalletA tentando enviar mais fundos (1000) do que possui...");
		block2.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 1000f));
		Blockchain.get().addBlock(block2);
		
		System.out.println("\nWalletA possui: " + walletA.getBalance());
		System.out.println("WalletB possui: " + walletB.getBalance());
		
		Block block3 = new Block(block2.getHash());
		System.out.println("\nWalletB transferindo (20) para WalletA...");
		block3.addTransaction(walletB.sendFunds( walletA.getPublicKey(), 20));
		Blockchain.get().addBlock(block3);
		
		System.out.println("\nWalletA possui: " + walletA.getBalance());
		System.out.println("WalletB possui: " + walletB.getBalance());
		
		System.out.println("\nblockchain válida: " + Blockchain.get().isValid() );
		
	}
}
