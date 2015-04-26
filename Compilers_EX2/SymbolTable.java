import java.util.*;


class SymbolTable {
	
	
	
	public Set<String> classes = new HashSet<String>();
	public Map<String, String> hasSuperClass 	 = new HashMap<String,String>();
	public Map<String, Set<String>> classmethods = new HashMap<String, Set<String>>();

	public Map<String, Map<String,String>>	classfields 		   = new HashMap<String,Map<String, String>>();		// class fields 
	public Map<String, Map<String,String>>	methodreturns		   = new HashMap<String,Map<String, String>>();  	// method returns
	
	public Map<String, Map<String, Map<String,String>>> methodpars = new HashMap<String,Map<String, Map<String,String>>>(); // method parameters
	public Map<String, Map<String, Map<String,String>>> methodvars = new HashMap<String,Map<String, Map<String,String>>>(); // method variables
	
	
	
}
