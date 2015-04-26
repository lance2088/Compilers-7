all: compile

compile:
	# Download jtb132.jar from cgi.di.uoa.gr/~thp06
	java -jar jtb132di.jar -te minijava.jj
	javacc -te minijava-jtb.jj
	javac Main.java SemanticException.java
test-error:	
	java Main BadAssign.java BadAssign2.java Classes-error.java 			DoubleDeclaration1.java DoubleDeclaration6.java 		NoMatchingMethod.java Overload1.java test18.java 			test21.java test35.java test52.java test68.java 		UseArgs.java
test-valid:
	java Main Add.java ArrayTest.java CallFromSuper.java Classes.java DerivedCall.java Example1.java FieldAndClassConflict.java  ManyClasses.java OutOfBounds1.java Overload2.java ShadowBaseField.java ShadowField.java test06.java test07.java test15.java test17.java test20.java test62.java test73.java test82.java test93.java test99.java

clean:
	rm -rf syntaxtree visitor jtb.out.jj minijava-jtb.jj *.class MiniJavaParser* Token* ParseExce*
