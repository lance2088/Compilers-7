import syntaxtree.*;
import java.util.*;
import visitor.GJNoArguDepthFirst;


public class FieldMethodVisitor extends GJNoArguDepthFirst< String >{
	
	public SymbolTable st;	
	
	String newclass;					// current exploring class
	String newmethod;					// current exploring method
	Map<String,String> newparmap;		// current parameters			
	Map<String,String> newvarmap;		// current variables or fields				
	    
	public FieldMethodVisitor(SymbolTable st){
		this.st = st;
	}	

    public String visit(MainClass n) throws Exception {		
		
		Map<String,Map<String,String>> methmap;
		Map<String,String>	parmap,varmap;
		Set<String> methodset;
		
		newclass = n.f1.f0.toString();
		newmethod = n.f6.toString();
		
		methodset = new HashSet<String>();						// names of methods
		methmap = new HashMap<String,Map<String,String>>();		// method's map	(with parameters)
		parmap = new HashMap<String,String>();					// parameter's map (name and type)
	
		varmap = new HashMap<String,String>();					// empty fields
		st.classfields.put(newclass,varmap);
		
		methodset.add(newmethod);		
		st.classmethods.put(newclass,methodset);		
		
		parmap.put(n.f11.f0.toString(),"String");    			// add parameter to the parameter's map
	
		
		methmap.put(newmethod,parmap);				  			// map method with the parameter's map
		st.methodpars.put(newclass,methmap);			  		// map class  with the method's map
			

		methmap = new HashMap<String,Map<String,String>>();
		varmap  = new HashMap<String,String>();
		
		methmap.put(newmethod,varmap);
		st.methodvars.put(newclass,methmap);
	
		newvarmap = varmap;
		newparmap = parmap;
		
		if(n.f14.present()){
			n.f14.accept(this);
		}
		
		return null;		       
    }  
	

    public String visit(ClassDeclaration n) throws Exception{
								
		Set<String> methodset ;
		Map<String,String> varmap;
		Map<String,String> retmap;
		Map<String,Map<String,String>> methmap;
		
		newclass = n.f1.f0.toString();
		
		methodset = new HashSet<String>();
		st.classmethods.put(newclass,methodset);		
		
	
		varmap = new HashMap<String,String>();		// class fields
		st.classfields.put(newclass,varmap);
		
		newvarmap = varmap;
		newparmap = null;							// distinguishes class fields from parameters
		
		if(n.f3.present()){
			n.f3.accept(this);
		}	
		
		retmap = new HashMap<String,String>();		// maps methods with return types
		st.methodreturns.put(newclass,retmap);
		
		methmap = new HashMap<String,Map<String,String>>();	
		st.methodpars.put(newclass,methmap);
			
		methmap = new HashMap<String,Map<String,String>>();	
		st.methodvars.put(newclass,methmap);	
			
				
		if(n.f4.present()){
			n.f4.accept(this);
		}		

		return null;		       
    } 

	 public String visit(ClassExtendsDeclaration n) throws Exception{
		
		Set<String> methodset ;
		Map<String,String> varmap;
		Map<String,String> retmap;
		Map<String,Map<String,String>> methmap;
		
		newclass = n.f1.f0.toString();
		
		methodset = new HashSet<String>();
		st.classmethods.put(newclass,methodset);	
		
		
		varmap = new HashMap<String,String>();		// class fields
		st.classfields.put(newclass,varmap);
		
		newvarmap = varmap;
		newparmap = null;
		
		if(n.f5.present()){
			n.f5.accept(this);
		}	
		
		retmap = new HashMap<String,String>();		// maps methods with return types
		st.methodreturns.put(newclass,retmap);
		
		
		methmap = new HashMap<String,Map<String,String>>();	
		st.methodpars.put(newclass,methmap);
		
		methmap = new HashMap<String,Map<String,String>>();	
		st.methodvars.put(newclass,methmap);
				
		if(n.f6.present()){
			n.f6.accept(this);
		}			
		
		check_polymorphism();			
		
		return null;	       
    } 
	 

	public String visit(VarDeclaration n) throws Exception {
		
		String type = n.f0.f0.choice.accept(this);
		String name = n.f1.f0.toString();
	
		
		if(!type.equals("int")  && !type.equals("boolean")){
			if(!type.equals("int[]") && !st.classes.contains(type)){
				System.out.println("Local Variable type " + type + " declaration invalid!");
				throw new SemanticException();
			}
		}		
		
		if(newvarmap.containsKey(name)){
			System.out.println("Local Variable name " + name + " declaration exists!");
			throw new SemanticException();
		}
		
		if(newparmap!=null && newparmap.containsKey(name)){
			System.out.println("Local Variable name " + name + " declaration exists in parameters!");
			throw new SemanticException();
		}
	
		newvarmap.put(name,type);

		return null;
	}


     public String visit(MethodDeclaration n) throws Exception{
		
		Set<String> methodset ;
		String rtype ;
		Map<String,String> retmap,parmap,varmap;
		Map<String,Map<String,String>> methmap;
		
		newmethod = n.f2.f0.toString();	
		methodset = st.classmethods.get(newclass);			
		
		if(!methodset.add(newmethod)){
			System.out.println("Multiple Declaration of Method " + newmethod);
			throw new SemanticException();
		}					
		
		rtype = n.f1.f0.choice.accept(this);		
		
		
		if(!rtype.equals("int")  && !rtype.equals("boolean")){
			if(!rtype.equals("int[]") && !st.classes.contains(rtype)){
				System.out.println("Return type " + rtype + " invalid!");
				throw new SemanticException();
			}
		}
		
		retmap = st.methodreturns.get(newclass);
		retmap.put(newmethod,rtype);	
		
				
		parmap  = new LinkedHashMap<String,String>();	
		st.methodpars.get(newclass).put(newmethod,parmap);		
		
		newparmap = parmap;		
		
		if(n.f4.present()){					// parameters
			n.f4.accept(this);
		}				
		
		varmap  = new HashMap<String,String>();		
		st.methodvars.get(newclass).put(newmethod,varmap);
		
		newvarmap = varmap;		
		
		if(n.f7.present()){					// local variables
			n.f7.accept(this);
		}	

		return null;		       
    }     
 

   	public String visit(FormalParameter n) throws Exception{
		
		String type,name;
		
		type = n.f0.f0.choice.accept(this);		
		name = n.f1.f0.toString();				
			
		if(!type.equals("int")  && !type.equals("boolean")){
			if(!type.equals("int[]") && !st.classes.contains(type)){
				System.out.println("Parameter type " + type + " invalid!");
				throw new SemanticException();
			}
		}
		
		if(newparmap.containsKey(name)){
			System.out.println("Parameter name " + name + " already exists!");
			throw new SemanticException();
		}	
		
		newparmap.put(name,type);
		return null;
	}
   
	
	
    public String visit(IntegerType n){
		return n.f0.toString();
	}
    public String visit(BooleanType n){
		return n.f0.toString();
	}
	public String visit(ArrayType n){
		return n.f0.toString() + n.f1.toString() + n.f2.toString() ;
	}
	public String visit(Identifier n){
		return n.f0.toString();
	}
	
	public void check_polymorphism() throws Exception{
		
		Set<String> newclassmethods = st.classmethods.get(newclass);
		Set<String> superclassmethods;
		
		String current = newclass;
		String superclass,rtype1,rtype2;
	
		Map<String,Map<String,String>> methmap1,methmap2;
		Map<String,String> parmap1,parmap2;
		Map<String,String> retmap1,retmap2;
		
		methmap1 = st.methodpars.get(newclass);		
		retmap1 =  st.methodreturns.get(newclass);
	 
		while(st.hasSuperClass.containsKey(current)){
			
			superclass = st.hasSuperClass.get(current);
			superclassmethods = st.classmethods.get(superclass);
			
			methmap2 = st.methodpars.get(superclass);
			retmap2 = st.methodreturns.get(superclass);
		
			for (String m1 : newclassmethods) {
				
				rtype1 = retmap1.get(m1);
				
				for(String m2: superclassmethods){
					if(m1.equals(m2)){
						
						rtype2 = retmap2.get(m2);
						
						if(!rtype2.equals(rtype1)){
							System.out.println("Different Return Types "+rtype1+" and "+rtype2);
							throw new SemanticException();
						}					
						
						parmap1 = methmap1.get(m1);
						parmap2 = methmap2.get(m2);
						
						Set set1 = parmap1.entrySet();
						Set set2 = parmap2.entrySet();
						Iterator i1 = set1.iterator();
						Iterator i2 = set2.iterator();
						
						while(i1.hasNext() || i2.hasNext()) {
							
							if(i1.hasNext() && !i2.hasNext()){
								System.out.println("Different Number of Arguments");
								throw new SemanticException();
							}
							
							if(!i1.hasNext() && i2.hasNext()){
								System.out.println("Different Number of Arguments");								
								throw new SemanticException();
							}							
							
							Map.Entry me1 = (Map.Entry)i1.next();
							Map.Entry me2 = (Map.Entry)i2.next();
							 
							if(!me1.getValue().equals(me2.getValue())){
								System.out.println("Different Parameter Types "+me1.getValue()+" and " +me2.getValue());
								System.out.println("At method "+m1);
								throw new SemanticException();
							}
							
						}			
						
					}					
					
				}				
			}
			current = superclass;		
		}			
	}
}
