import syntaxtree.*;
import visitor.*;
import java.io.*;


class Main {
    public static void main (String [] args) {
		if(args.length < 1){
		    System.err.println("Usage: java Driver <inputFile>");
		    System.exit(1);
		}
		FileInputStream fis = null;
		for(int i=0;i<args.length;i++){
			
			try{
				fis = new FileInputStream(args[i]);
				System.out.println(" _______________________");
				System.out.println("|"+args[i]+" ");
				MiniJavaParser parser = new MiniJavaParser(fis);
				
				Goal root = parser.Goal();
				parser=null;
				System.out.println("|			|");
				System.out.println("|			|");
				System.err.println("|Program syntax parsed  |");
				System.out.println("|			|");
				System.out.println("|			|");
				
				SymbolTable st = new SymbolTable();
				
				ClassVisitor v1 = new ClassVisitor(st);		   
				root.accept(v1);				
				
				FieldMethodVisitor v2 = new FieldMethodVisitor(st);		
				root.accept(v2);
				
				TypeVisitor v3 = new TypeVisitor(st);
				root.accept(v3,null);
				fis.close();
				v1 = null;
				v2 = null;
				v3 = null;
				st = null;
				
				System.err.println("|Program semantic parsed|");
				System.out.println(" _______________________");
			}
			catch(ParseException ex){
				System.out.println(ex.getMessage());
			}
			catch(FileNotFoundException ex){
				System.err.println(ex.getMessage());
			}		
			catch(Exception ex){
				System.err.println(ex.getMessage());
			}	
			finally{
				try{
					if(fis != null) fis.close();
				}
				catch(IOException ex){
					System.err.println(ex.getMessage());
				}
				
			}
		}
    }
}
