package data;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.CryptoUtil;

public class Transaction {

	public String transactionId; // também será o hash da transação

	public PublicKey sender; // chave publica da origem
	public PublicKey reciepient; // chave publica do destinatário 
	public float value;
	public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.

	public List<TransactionInput> inputs = new ArrayList<>();
	public List<TransactionOutput> outputs = new ArrayList<>();

	private static int sequence = 0; // contador de transações

	// Constructor: 
	public Transaction(PublicKey from, PublicKey to, float value,  List<TransactionInput> inputs) {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}

	// This Calculates the transaction hash (which will be used as its Id)
	public String calulateHash() {
		sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
		return CryptoUtil.applySha256(
				CryptoUtil.getStringFromKey(sender) +
				CryptoUtil.getStringFromKey(reciepient) +
				Float.toString(value) + sequence
				);
	}
	
	public void generateSignature(PrivateKey privateKey) {
		String data = CryptoUtil.getStringFromKey(sender) + CryptoUtil.getStringFromKey(reciepient) + Float.toString(value)	;
		signature = CryptoUtil.applyECDSASig(privateKey,data);		
	}
	
	public boolean verifySignature() {
		String data = CryptoUtil.getStringFromKey(sender) + CryptoUtil.getStringFromKey(reciepient) + Float.toString(value)	;
		return CryptoUtil.verifyECDSASig(sender, data, signature);
	}

	public float getInputsValue() {
		float total = 0;
		for(TransactionInput i : inputs) {
			if(i.getUtxo() == null) continue; //if Transaction can't be found skip it, This behavior may not be optimal.
			total += i.getUtxo().getValue();
		}
		return total;
	}
	
	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", sender=" + sender + ", reciepient=" + reciepient
				+ ", value=" + value + ", signature=" + Arrays.toString(signature) + ", inputs=" + inputs + ", outputs="
				+ outputs + "]";
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public PublicKey getSender() {
		return sender;
	}

	public void setSender(PublicKey sender) {
		this.sender = sender;
	}

	public PublicKey getReciepient() {
		return reciepient;
	}

	public void setReciepient(PublicKey reciepient) {
		this.reciepient = reciepient;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public List<TransactionInput> getInputs() {
		return inputs;
	}

	public void setInputs(List<TransactionInput> inputs) {
		this.inputs = inputs;
	}

	public List<TransactionOutput> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<TransactionOutput> outputs) {
		this.outputs = outputs;
	}
	
}
