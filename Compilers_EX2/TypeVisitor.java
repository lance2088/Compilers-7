import syntaxtree.*;
import java.util.*;
import visitor.GJDepthFirst;



public class TypeVisitor extends GJDepthFirst<String,List<String>>{
	
	public SymbolTable st;	
	
	String newclass;					// current exploring class
	String newmethod;					// current exploring method
	
	Map<String,String> newfldmap;       // current fields	
	Map<String,String> newparmap;		// current parameters			
	Map<String,String> newvarmap;		// current variables	
	

	public TypeVisitor(SymbolTable st){
		this.st = st;
	}
	
	public String visit(MainClass n,List _) throws Exception {
		newclass = n.f1.f0.toString();
		newmethod = "main";	

		newfldmap = st.classfields.get(newclass);		
		newparmap = st.methodpars.get(newclass).get(newmethod);
		newvarmap = st.methodvars.get(newclass).get(newmethod);
	
		if(n.f15.present()){
			n.f15.accept(this,null);
		}	
		return null;
	}
		
	public String visit(ClassDeclaration n,List _) throws Exception{
		
		newclass = n.f1.f0.toString();
		newfldmap = st.classfields.get(newclass);
		
		if(n.f4.present()){
			n.f4.accept(this,null);
		}
			
		return null;
	}	
	
	public String visit(ClassExtendsDeclaration n,List _) throws Exception{
		
		newclass = n.f1.f0.toString();
		newfldmap = st.classfields.get(newclass);
	
		if(n.f6.present()){
			n.f6.accept(this,null);
		}	
		
		return null;
	}	
	public String visit(MethodDeclaration n,List _) throws Exception {
		
		newmethod = n.f2.f0.toString();	
		newparmap = st.methodpars.get(newclass).get(newmethod);
		newvarmap = st.methodvars.get(newclass).get(newmethod);
			
		if(n.f8.present()){					// Statements
			n.f8.accept(this,null);
		}	
		
		String rtype1,rtype2;
		
		rtype1 = st.methodreturns.get(newclass).get(newmethod);
		rtype2 = n.f10.accept(this,null);
		
		if(!rtype1.equals(rtype2)){
			if(check_primitive(rtype1) || check_primitive(rtype2)){
				System.out.println("Return types don't match");
				throw new SemanticException();
			}else if(!check_inheritance(rtype2,rtype1)){
				System.out.println("Return types don't match");
				throw new SemanticException();
			}			
		}
		
		return null;		
	}	
		
	public String visit(AssignmentStatement n,List _) throws Exception {
		
		String type1=null,type2 ;
		
		type1 = n.f0.accept(this,null);	
		type2 = n.f2.accept(this,null);
		
		if(type2 == null){		
			System.out.println("Expression type is invalid");			
			throw new SemanticException();
		}
		
		if(!type1.equals(type2)){			
			if(check_primitive(type1) || check_primitive(type2)){
				System.out.println("Invalid Assignment types : "+type1+" =/= " + type2);			
				throw new SemanticException();
			}else if(!check_inheritance(type2,type1)){
				System.out.println("Invalid Assignment types : "+type1+" =/= " + type2);			
				throw new SemanticException();
			}
		}
		
		return null;
		
	}
	
	public String visit(ArrayAssignmentStatement n ,List _) throws Exception {

		String type1=null,type2,type3 ;
		
		type1 = n.f0.accept(this,null);
		
		if(!type1.equals("int[]")){
			System.out.println("Left operand type is not int[]");			
			throw new SemanticException();
		}
		
		type2 = n.f2.accept(this,null);
		
		if(type2 == null){
			System.out.println("Expression type is invalid");			
			throw new SemanticException();
		}
		
		if(!type2.equals("int")){
			System.out.println("Array index expression is not int");			
			throw new SemanticException();
		}
		
		type3 = n.f5.accept(this,null);
		
		if(!type3.equals(type2)){
			System.out.println("Invalid ArrayAssignment types : "+type2+" =/= " + type3);			
			throw new SemanticException();
		}
		
		return null;
	}
	
	public String visit(Clause  n,List _) throws Exception{
		return n.f0.accept(this,null);
	}
	
	public String visit(NotExpression  n, List _) throws Exception{
		return n.f1.accept(this,null);
	}
	public String visit(PrimaryExpression  n,List _) throws Exception{
		return n.f0.accept(this,null);
	}
	
	public String visit(AndExpression  n,List _) throws Exception{
		
		String type1 = n.f0.accept(this,null);
		String type2 = n.f2.accept(this,null);
		
		if(type1.equals("boolean") && type2.equals("boolean")){
			return type1;
		}else{
			System.out.println("AndExpression not boolean");			
			throw new SemanticException();
		}
		
	}
	
	public String visit(CompareExpression n ,List _) throws Exception{
		
		String type1 = n.f0.accept(this,null);
		String type2 = n.f2.accept(this,null);
		
		if(type1.equals("int") && type2.equals("int")){
			return "boolean";
		}else{
			System.out.println("CompareExpression not boolean");			
			throw new SemanticException();
		}
		
	}
	
	public String visit(PlusExpression  n,List _) throws Exception{
		
		String type1 = n.f0.accept(this,null);
		String type2 = n.f2.accept(this,null);
		
		if(type1.equals("int") && type2.equals("int")){
			return "int";
		}else{
			System.out.println("PlusExpression not int");			
			throw new SemanticException();
		}
		
	}
	public String visit(MinusExpression  n,List _) throws Exception{
		
		String type1 = n.f0.accept(this,null);
		String type2 = n.f2.accept(this,null);
		
		if(type1.equals("int") && type2.equals("int")){
			return "int";
		}else{
			System.out.println("MinusExpression not int");			
			throw new SemanticException();
		}
		
	}
	
	public String visit(TimesExpression  n,List _) throws Exception{
		
		String type1 = n.f0.accept(this,null);
		String type2 = n.f2.accept(this,null);
		
		if(type1.equals("int") && type2.equals("int")){
			return "int";
		}else{
			System.out.println("TimesExpression not int");			
			throw new SemanticException();
		}
		
	}
	
	public String visit(ArrayLookup  n,List _) throws Exception{
		
		String type1 = n.f0.accept(this,null);
		String type2 = n.f2.accept(this,null);
		
		if(type1.equals("int[]") && type2.equals("int")){
			return "int";
		}else if (!type1.equals("int[]")){
			System.out.println("Variable type not int[]");			
			throw new SemanticException();
		}else{
			System.out.println("ArrayLookUp index type not int");			
			throw new SemanticException();
		}
		
	}
	
	public String visit(ArrayLength  n,List _) throws Exception{
		
		String type1 = n.f0.accept(this,null);		
		
		if(type1.equals("int[]")){
			return "int";
		}else{
			System.out.println("Variable is not int[] to produce length");			
			throw new SemanticException();
		}		
	}
	

	public String visit(MessageSend  n,List _) throws Exception{
		
		String type = n.f0.accept(this,null);
		String methodname = n.f2.f0.toString();
		String rtype;
						
		
		// check if caller is of class type
		
		if(type.equals("int[]") || type.equals("int") || type.equals("boolean")){
			System.out.println("Message sender type not a class");			
			throw new SemanticException();
		}
		
		// check in which class the method is defined
		
		String defclass;
		Map<String,String> parmap;
		Set<String> newset = st.classmethods.get(type);
				
		if(newset.contains(methodname)){
			defclass = type;
		}else{			
			defclass = superclass_method(methodname,type);				
		}	
		
		// check if method's parameters match with types
		
		parmap = st.methodpars.get(defclass).get(methodname);
		List<String> newtypelist = new ArrayList<String>(); // parameters of current msg send method
		
		n.f4.accept(this,newtypelist);
		
		if(newtypelist.size() != parmap.size()){
			System.out.println("Number of parameters does not match");			
			throw new SemanticException();
		}else{
			Set set = parmap.entrySet();
			Iterator i1 = set.iterator();
			Iterator<String> i2 = newtypelist.iterator(); 
			
			while(i1.hasNext()) {
				Map.Entry<String,String> me = (Map.Entry)i1.next();
				String type1,type2;
				
				type1 = me.getValue();
				type2 = i2.next();
				
				if(!type1.equals(type2)){
					if(check_primitive(type1) || check_primitive(type2)){
						System.out.println("Types of parameters do not match");			
						throw new SemanticException();
					}else if(!check_inheritance(type2,type1)){
						System.out.println("Types of parameters do not match");			
						throw new SemanticException();
					}					
				}
			}
		}
			
		newtypelist = null;
		rtype = st.methodreturns.get(defclass).get(methodname);
		
		return rtype;
		
	}
	public String visit(ExpressionList  n,List<String> newtypelist) throws Exception{

		String type = n.f0.accept(this,null);
		newtypelist.add(type);	
	
		n.f1.accept(this,newtypelist);
		return null;
	}
	public String visit(ExpressionTail  n,List<String> newtypelist) throws Exception{
		if(n.f0.present()){
			n.f0.accept(this,newtypelist);
		}
		return null;
	}
	public String visit(ExpressionTerm  n,List<String> newtypelist) throws Exception{
		
		String type = n.f1.accept(this,null);
		newtypelist.add(type);
		
		return null;
	}
	public String visit(IntegerLiteral  n,List _){
		return "int";
	}
	public String visit(TrueLiteral  n,List _) {
		return "boolean";
	}
	public String visit(FalseLiteral  n,List _) {
		return "boolean";
	}
	
	public String visit(ThisExpression  n,List _) {
		return newclass;
	}
	
	public String visit(ArrayAllocationExpression  n,List _) throws Exception{
		String type = n.f3.accept(this,null);
		
		if(type.equals("int")){
			return "int[]";
		}else{
			System.out.println("Allocation index not int");			
			throw new SemanticException();
		}
	}
	
	public String visit(AllocationExpression  n,List _) throws Exception{
		String type = n.f1.f0.toString();
		
		if(type.equals("int") || st.classes.contains(type)){
			return type;
		}else{
			System.out.println("Invalid Allocation type");
			throw new SemanticException();
		}
	}
	
	public String visit(BracketExpression  n,List _) throws Exception{
		return n.f1.accept(this,null);
	}
	
	public String visit(Identifier  n,List _) throws Exception{
	
		String name = n.f0.toString();
		
		if(newparmap.containsKey(name)){		// shadowing of local variables
			return newparmap.get(name);
		}else if(newvarmap.containsKey(name)){	// shadowing of local variables
			return newvarmap.get(name);
		}else if(newfldmap.containsKey(name)){  // class field if no shadowed 
			return newfldmap.get(name);
		}else{									// parent class field 
			return superclass_field(name);
		}
	}
	
	public String visit(IfStatement n,List _) throws Exception{
		String type = n.f2.accept(this,null);
		if(!type.equals("boolean")){
			System.out.println("Expression not boolean in if stmt");
			throw new SemanticException();
		}
		n.f4.accept(this,null);
		n.f6.accept(this,null);
		return null;
	}
			
	public String visit(WhileStatement n,List _) throws Exception{
		String type = n.f2.accept(this,null);
		if(!type.equals("boolean")){
			System.out.println("Expression not boolean in while stmt");
			throw new SemanticException();
		}
		n.f4.accept(this,null);
		return null;
	}
	
	public String visit(PrintStatement n,List _) throws Exception{
		String type = n.f2.accept(this,null);
		if(!type.equals("int")){
			System.out.println("Expression not int in print stmt");
			throw new SemanticException();
		}
		return null;
	}
	
	public String superclass_field(String name) throws Exception{
		
		String superclass,current = newclass;		
		Map<String,String> superclassfld ;
		
		while(st.hasSuperClass.containsKey(current)){
			superclass = st.hasSuperClass.get(current);
			superclassfld = st.classfields.get(superclass);
			
			if(superclassfld.containsKey(name)){
				return superclassfld.get(name);
			}
			current = superclass;
		}			
			
		System.out.println("Variable "+name+" not declared");	
		throw new SemanticException();
	}
	
	public String superclass_method(String name,String newclass) throws Exception{
		
		String superclass,current = newclass;		
		Set<String> superclassmtd ;
		
		while(st.hasSuperClass.containsKey(current)){
			superclass = st.hasSuperClass.get(current);
			superclassmtd = st.classmethods.get(superclass);
			
			if(superclassmtd.contains(name)){				
				return superclass;
			}
			current = superclass;
		}			
		System.out.println("Method "+name+" not declared");	
		throw new SemanticException();
	}
	public boolean check_inheritance(String child,String pred){
		
		String superclass,current = child;
		
		if(st.hasSuperClass.containsKey(current)){
			superclass = st.hasSuperClass.get(current);
			if(superclass.equals(pred)){
				return true;
			}else{
				return check_inheritance(superclass,pred);
			}
		}
		return false;
	}
	public boolean check_primitive(String type){
		if(type.equals("int") || type.equals("boolean") || type.equals("int[]")){
			return true;
		}else{
			return false;
		}
	}
}
