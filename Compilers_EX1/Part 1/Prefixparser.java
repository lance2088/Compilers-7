import java.io.InputStream;
import java.io.IOException;

public class Prefixparser{
	
	private int lookaheadToken;	
    private InputStream in;
	
	
    public Prefixparser(InputStream in) throws IOException {
		this.in = in;
		lookaheadToken = in.read();
    }
	
/************************* Parsing Functions **********************************/
					
    private void consume(int symbol) throws IOException, ParseError {
		
		if (lookaheadToken != symbol){			
			throw new ParseError(); 
		}
		System.out.println("Consumed : " + (char)lookaheadToken);
		lookaheadToken = in.read();	
    }
    
/******************************************************************************/
    
	public String parse() throws IOException, ParseError {
		
		String s1 = exp();
		
		if(s1!=null && lookaheadToken == '\n' ){
			System.out.println("Valid\n");
			return s1;
		}else{
			throw new ParseError();	
		}
	
    }
    
/************************* Grammar Functions **********************************/ 
						
						
    public String exp() throws IOException, ParseError {
		
		String s1 = term();				// pass previously found subexpression
		String s2 = exp2(s1);			// as argument for string manipulation										
													
		if(s1!=null && s2!=null){	
			return s2;
		}
		return null;		
	}
	
/******************************************************************************/	

	public String  term() throws IOException, ParseError {
		
		String s1 = factor();
		String s2 = term2(s1);
		
		if(s1!=null && s2!=null){
			return s2;		
		}
		return null;
	}
	
/******************************************************************************/

    public String exp2(String previous) throws IOException, ParseError {	
			
		String subexp;
		
		if(lookaheadToken == '+' || lookaheadToken == '-'){				
			String op = Character.toString((char)lookaheadToken);				
			consume(lookaheadToken);
			
			String s1 = term();		
			
			if(s1 != null){     
				
				// insert subterm into previous subexpression
				          
                if(previous.length() > 1 && previous.charAt(previous.length()-1)!= ')'){
                    subexp = previous.substring(0, previous.length()-2) + "(" + op + previous.charAt(previous.length() -1) + " " + s1 + ")"; 
                }else{
                    subexp = "(" + op + previous + " " + s1 + ")";
				}
                return exp2(subexp);
            }
            else{                
                throw new ParseError();
            }		
		}		
		return previous;		
	}	
   
/******************************************************************************/	

    public String factor() throws IOException, ParseError {
		if(lookaheadToken == '('){	
			
			consume(lookaheadToken);		
			
			String s1 = exp();
			
			if(s1!=null && lookaheadToken == ')'){	
				consume(lookaheadToken);			
				//return "";
				return s1;
			}else{
				throw new ParseError();							
			}
		}
		
		if(lookaheadToken >= '0' && lookaheadToken <= '9'){				
			String s2 = Character.toString((char)lookaheadToken) ;			
			consume(lookaheadToken);
			return s2;
		}
		
		return null;
	}
/******************************************************************************/	
	
    public String term2(String previous) throws IOException, ParseError {
		String subexp;
		
		if(lookaheadToken == '*' || lookaheadToken == '/'){				
			String op = Character.toString((char)lookaheadToken);				
			consume(lookaheadToken);			
			String s1 = factor();		
			
			if(s1 != null){ 
				// recursively create each prefix subexpression and append them 
				// into one 
				              
                if(previous.length() > 1 && previous.charAt(previous.length()-1)!= ')'){
					// append into previous subexpression the current operation					
										
                    subexp = previous.substring(0, previous.length()-2) + "(" + op + previous.charAt(previous.length() -1) + " " + s1 + ")";
                    
                }else{
                    subexp = "(" + op + previous + " " + s1 + ")";  	 
				}
                return term2(subexp);
            }
            else{                
                throw new ParseError();
            }		
		}
		
		return previous;
	}
    
/***************************** Main	*******************************************/    
    
	public static void main(String[] args) {
		try {
			Prefixparser parser = new Prefixparser(System.in);		
			System.out.println(parser.parse());
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
		catch(ParseError err){
			System.err.println(err.getMessage());
		}
	}
}
