package crawlers.webcrawlers;

public class Master {

	//TODO: contact the frontier for url and coordinate Slaves or separate those two??
	//TODO: create pools (queue maybe?) for available and busy Slaves (maybe only available (fallback:not monitoring all?))
	//TODO: master should get page from slave to featured-modules? or slave should directly send it?	
	
	public Slave getFreeSlave() {
		return null;
	}
	
	public boolean allSlavesAreBusy() {
		return false;
	}
	
	//listen if request came from slave?
	
	public void getFreeSlaves() {
		
	}
}
