package service;

import java.util.List;

import javax.ejb.Stateless;

import blockchain.Block;
import data.Wallet;
import singleton.Blockchain;

@Stateless
public class BlockchainService {
	
	//https://github.com/conradoqg/naivecoin
	
	public void genesis( Wallet destino, Double quantia ) {
		Blockchain.get().genesis( destino, quantia );
	}
	
	public List<Block> getBlocks(){
		return Blockchain.get().getChain();
	}

}
