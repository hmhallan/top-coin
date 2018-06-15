package singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonBuilder;

import blockchain.Block;
import data.Transaction;
import data.TransactionInput;
import data.TransactionOutput;
import data.Wallet;
import util.Constants;

public class Blockchain {

	private static final Blockchain _INSTANCE = new Blockchain(); 
	
	public static Blockchain get() {
		return _INSTANCE;
	}
	
	private ArrayList<Block> chain;
	private Map<String,TransactionOutput> utxos;

	public Blockchain() {
		super();
		this.chain = new ArrayList<>();
		this.utxos = new HashMap<String,TransactionOutput>();
	} 
	
	
	public void addBlock(Block newBlock) {
		newBlock.mineBlock(Constants.DIFICULDADE);
		this.chain.add(newBlock);
	}

	
	public boolean isValid() {
		Block currentBlock; 
		Block previousBlock;
		
		
		String hashTarget = new String(new char[Constants.DIFICULDADE]).replace('\0', '0');
		
		//loop em toda a cadeia para verificar os hashes
		for(int i=1; i < chain.size(); i++) {
			currentBlock = chain.get(i);
			previousBlock = chain.get(i-1);
			
			//compara o hash registrado e o hash calculado
			if(!currentBlock.getHash().equals(currentBlock.calculateHash()) ){
				System.out.println("Hashes diferentes");			
				return false;
			}
			//compara o hash anterior e o hash registrado anterior 
			if(!previousBlock.getHash().equals(currentBlock.getPreviousHash()) ) {
				System.out.println("Hashes anteriores diferentes");
				return false;
			}
			
			//verifica se o hash foi resolvido
			if(!currentBlock.getHash().substring( 0, Constants.DIFICULDADE).equals(hashTarget)) {
				System.out.println("Bloco não foi mineirado");
				return false;
			}
		}
		return true;
	}
	
	public boolean processTransaction( Transaction t ) {
		
		if(t.verifySignature() == false) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}
				
		//Gathers transaction inputs (Making sure they are unspent):
		for(TransactionInput i : t.getInputs()) {
			i.setUtxo( Blockchain.get().getUtxos().get( (i.getTransactionOutputId() ) ) );
		}

		//Checks if transaction is valid:
		if( t.getInputsValue() < Constants.MINIMUM_TRANSACTION) {
			System.out.println("Transaction Inputs too small: " + t.getInputsValue());
			System.out.println("Please enter the amount greater than " + Constants.MINIMUM_TRANSACTION);
			return false;
		}
		
		//Generate transaction outputs:
		double leftOver = t.getInputsValue() - t.getValue(); //get value of inputs then the left over change:
		t.setTransactionId( t.calulateHash() );
		t.getOutputs().add(new TransactionOutput( t.getReciepient(), t.getValue(),t.getTransactionId())); //send value to recipient
		t.getOutputs().add(new TransactionOutput( t.getSender(), leftOver, t.getTransactionId())); //send the left over 'change' back to sender		
				
		//Add outputs to Unspent list
		for(TransactionOutput o : t.getOutputs()) {
			Blockchain.get().getUtxos().put( o.getId() , o);
		}
		
		//Remove transaction inputs from UTXO lists as spent:
		for(TransactionInput i : t.getInputs()) {
			if(i.getUtxo() == null) continue; //if Transaction can't be found skip it 
			Blockchain.get().getUtxos().remove(i.getUtxo().getId());
		}
		
		return true;
	}
	
	
	/**
	 * inicializa a cadeia (transfere 100 para a carteira passada como argumento)
	 */
	public Block genesis( Wallet walletA, double quantia ) {
		Wallet coinbase = new Wallet();
		
		//create genesis transaction, which sends 100 NoobCoin to walletA: 
		Transaction genesisTransaction = new Transaction(coinbase.getPublicKey(), walletA.getPublicKey(), quantia, null);
		genesisTransaction.generateSignature(coinbase.getPrivateKey());	 //manually sign the genesis transaction	
		
		genesisTransaction.transactionId = "0"; //manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
		Blockchain.get().getUtxos().put(genesisTransaction.outputs.get(0).getId(), genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		
		System.out.println("Criando e Minerando Genesis block... ");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);
		
		return genesis;
	}

	public ArrayList<Block> getChain() {
		return chain;
	}
	
	public Map<String,TransactionOutput> getUtxos() {
		return this.utxos;
	}
	
	public String toJson() {
		return new GsonBuilder().setPrettyPrinting().create().toJson(this.chain);
	}
	
}

