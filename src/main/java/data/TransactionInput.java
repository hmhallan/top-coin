package data;

public class TransactionInput {
	
	private String transactionOutputId; //Reference to TransactionOutputs -> transactionId
	
	private TransactionOutput utxo; //Contains the Unspent transaction output
	
	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}
	
	@Override
	public String toString() {
		return "TransactionInput [transactionOutputId=" + transactionOutputId + ", utxo=" + utxo + "]";
	}

	public String getTransactionOutputId() {
		return transactionOutputId;
	}
	public void setTransactionOutputId(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}
	public TransactionOutput getUtxo() {
		return utxo;
	}
	public void setUtxo(TransactionOutput utxo) {
		this.utxo = utxo;
	}
	
	
	
	
}