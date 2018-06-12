package data;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import singleton.Blockchain;

public class Wallet {

	//chave privada da carteira, para 'assinar' as transações
	private PrivateKey privateKey;

	//endereço público da carteira
	private PublicKey publicKey;
	
	
	private Map<String,TransactionOutput> utxos;

	public Wallet(){
		generateKeyPair();	
		this.utxos = new HashMap<>();
	}

	public void generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

			// inicializa e gera um KeyPair
			keyGen.initialize(ecSpec, random);   //256 bytes 
			KeyPair keyPair = keyGen.generateKeyPair();
			
			// Seta as chaves
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public float getBalance() {
		float total = 0;	
        for (Map.Entry<String, TransactionOutput> item: Blockchain.get().getUtxos().entrySet()){
        	TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
            	this.utxos.put(UTXO.getId(),UTXO); //add it to our list of unspent transactions.
            	total += UTXO.getValue(); 
            }
        }  
		return total;
	}
	
	public Transaction sendFunds(PublicKey recipient, float value ) {
		if(getBalance() < value) {
			System.out.println("#Sem fundos para enviar. Transação descartada");
			return null;
		}
		List<TransactionInput> inputs = new ArrayList<>();
		
		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: this.utxos.entrySet()){
			TransactionOutput UTXO = item.getValue();
			total += UTXO.getValue();
			inputs.add(new TransactionInput(UTXO.getId()));
			if(total > value) break;
		}
		
		Transaction newTransaction = new Transaction( this.publicKey, recipient , value, inputs);
		newTransaction.generateSignature(privateKey);
		
		for(TransactionInput input: inputs){
			this.utxos.remove(input.getTransactionOutputId());
		}
		
		return newTransaction;
	}
	
	@Override
	public String toString() {
		return "Wallet [privateKey=" + privateKey + ", publicKey=" + publicKey + "]";
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	public PublicKey getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

}
