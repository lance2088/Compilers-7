import syntaxtree.*;
import java.util.*;
import visitor.GJNoArguDepthFirst;

public class ClassVisitor extends GJNoArguDepthFirst<String>{

	
	public SymbolTable st;
	
    public ClassVisitor(SymbolTable st){
		this.st = st;
	}
	
	
	public String visit(Goal n) throws Exception{
        n.f0.accept(this);
        if(n.f1.present()){
			return n.f1.accept(this);
		}
		return null;
    }
    
    
	public String visit(MainClass n) {
		String newclass = n.f1.f0.toString();
		st.classes.add(newclass);
		
		return null;		       
    }    

    
    public String visit(ClassDeclaration n) throws Exception{
		String newclass = n.f1.f0.toString();
		if(!st.classes.add(newclass)){
			System.out.println("Multiple Declaration of Class "+newclass);
			throw new SemanticException();
		}
				
		return null;		       
    }   


    public String visit(ClassExtendsDeclaration n) throws Exception{
		String newclass = n.f1.f0.toString();
		String superclass = n.f3.f0.toString();
		
		if(st.classes.contains(newclass)){
			System.out.println("Multiple Declaration of Class "+newclass);
			throw new SemanticException();
		}
		
		if(!st.classes.contains(superclass)){
			System.out.println("Extended Class "+newclass+"not declared!");
			throw new SemanticException();
		}
	
		st.classes.add(newclass);
		st.hasSuperClass.put(newclass,superclass);
		
		return null;
	} 
    
    
}
