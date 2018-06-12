package data;

import java.security.PublicKey;

import util.CryptoUtil;

public class TransactionOutput {
	
	private String id;
	private PublicKey reciepient; //o novo dono das moedas
	private float value; //the amount of coins they own
	private String parentTransactionId; //the id of the transaction this output was created in
	
	//Constructor
	public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
		this.reciepient = reciepient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = CryptoUtil.applySha256(CryptoUtil.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId);
	}
	
	//Check if coin belongs to you
	public boolean isMine(PublicKey publicKey) {
		return (publicKey == reciepient);
	}
	
	@Override
	public String toString() {
		return "TransactionOutput [id=" + id + ", reciepient=" + reciepient + ", value=" + value
				+ ", parentTransactionId=" + parentTransactionId + "]";
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
	public String getParentTransactionId() {
		return parentTransactionId;
	}
	public void setParentTransactionId(String parentTransactionId) {
		this.parentTransactionId = parentTransactionId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
