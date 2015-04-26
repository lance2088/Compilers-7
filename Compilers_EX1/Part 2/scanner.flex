/* JFlex example: part of Java language lexer specification */
import java_cup.runtime.*;
/**
%%
/* -----------------Options and Declarations Section----------------- */

/*
   The name of the class JFlex will create will be Lexer.
   Will write the code to the file Lexer.java.
*/
%class Scanner

/*
  The current line number can be accessed with the variable yyline
  and the current column number with the variable yycolumn.
*/
%line
%column

/*
   Will switch to a CUP compatibility mode to interface with a CUP
   generated parser.
*/
%cup

/*
  Declarations

  Code between %{ and %}, both of which must be at the beginning of a
  line, will be copied letter to letter into the lexer class source.
  Here you declare member variables and functions that are used inside
  scanner actions.
*/

%{
    StringBuffer string = new StringBuffer();
    private Symbol symbol(int type) {
       return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

/*
  Macro Declarations

  These declarations are regular expressions that will be used latter
  in the Lexical Rules Section.
*/


/* A line terminator is a \r (carriage return), \n (line feed), or
   \r\n. */
LineTerminator = \r|\n|\r\n

/* White space is a line terminator, space, tab, or line feed. */
WhiteSpace     = {LineTerminator} | [ \t\f]

/* A literal integer is is a number beginning with a number between
   one and nine followed by zero or more numbers between zero and nine
   or just a zero.  */

dec_int_lit = 0 | [1-9][0-9]*
id_regex =  [a-zA-Z]+(_[a-zA-Z0-9]+|[a-zA-Z0-9]+)*

%%
/* ------------------------Lexical Rules Section---------------------- */

<YYINITIAL> {
/* operators */

 "+"      { return symbol(sym.PLUS); }
 "-"      { return symbol(sym.MINUS); }
 "*"      { return symbol(sym.TIMES); }
 "/"      { return symbol(sym.DIV); }
 "%"      { return symbol(sym.MOD); }
 "("      { return symbol(sym.LPAREN); }
 ")"      { return symbol(sym.RPAREN); }
 "{"      { return symbol(sym.LBRACE); }
 "}"      { return symbol(sym.RBRACE); }

/* conditionals */

 "if"     { return symbol(sym.IF); }
 "else"   { return symbol(sym.ELSE); }

/* logical expressions */

 "=="     { return symbol(sym.EQ); }
 "<"      { return symbol(sym.LT); }
 ">"      { return symbol(sym.GT); }

/* delimiters */

 ","	  { return symbol(sym.COMMA);}

}


{dec_int_lit}  { return symbol(sym.NUMBER, new String(yytext())); }
{id_regex} { return symbol(sym.ID, new String(yytext())); }



{WhiteSpace} { /* just skip what was found, do nothing */ }

/* No token was found for the input so through an error.  Print out an
   Illegal character message with the illegal character that was found. */
[^]                    { throw new Error("Illegal character <"+yytext()+">"); }
