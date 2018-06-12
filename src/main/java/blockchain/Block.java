package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import data.Transaction;
import singleton.Blockchain;
import util.CryptoUtil;

public class Block {
	
	private String hash;
	private String previousHash;
	
	public String merkleRoot;
	public List<Transaction> transactions; //our data will be a simple message.
	
	private long timestamp;
	
	private int nonce;
	
	public Block() {
		super();
	}
	
	public Block(String previousHash ) {
		this.transactions = new ArrayList<>();
		this.previousHash = previousHash;
		this.timestamp = new Date().getTime();
		this.hash = calculateHash();  //calcula o hash
	}

	/**
	 * Calcula o hash deste bloco
	 * @return Hash do bloco atual
	 */
	public String calculateHash() {
		String calculatedhash = CryptoUtil.applySha256( 
				this.previousHash +
				Long.toString(this.timestamp) +
				Integer.toString(this.nonce) + 
				this.merkleRoot 
				);
		return calculatedhash;
	}
	
	public void mineBlock(int difficulty) {
		String target = new String(new char[difficulty]).replace('\0', '0'); //cria uma String com conteúdo: ( difficulty * "0" )
		
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}
		System.out.println("Bloco mineirado!!! : " + hash);
	}
	
	//Add transactions to this block
	public boolean addTransaction(Transaction transaction) {
		//process transaction and check if valid, unless block is genesis block then ignore.
		if(transaction == null) return false;		
		if((!"0".equals(previousHash))) {
			
			if((Blockchain.get().processTransaction( transaction ) != true)) {
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}
		}

		transactions.add(transaction);
		System.out.println("Transação adicionada ao bloco com sucesso");
		return true;
	}

	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getPreviousHash() {
		return previousHash;
	}
	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	
}
