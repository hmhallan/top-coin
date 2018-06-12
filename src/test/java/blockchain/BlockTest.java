package blockchain;

import org.junit.Test;

public class BlockTest {
	
	@Test
	public void cria_blocos() {
		Block genesisBlock = new Block("0");
		System.out.println("Hash do bloco 1 : " + genesisBlock.getHash());
		
		Block secondBlock = new Block(genesisBlock.getHash());
		System.out.println("Hash do bloco 2 : " + secondBlock.getHash());
		
		Block thirdBlock = new Block(secondBlock.getHash());
		System.out.println("Hash do bloco 3 : " + thirdBlock.getHash());
	}
}