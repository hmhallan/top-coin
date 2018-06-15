package rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import blockchain.Block;
import io.swagger.annotations.Api;
import service.BlockchainService;

@Api(tags="blockchain")
@Path("/blockchain")
public class BlockchainRest {

	@EJB
	BlockchainService service;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Block> getBlocks(){
		return service.getBlocks();
	}
	
	
}
