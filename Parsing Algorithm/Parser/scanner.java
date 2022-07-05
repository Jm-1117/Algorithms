import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
//import java.util.Arrays;


// scanner scans the pl file and store the lexemes and tokens
// in an arraylist
public class scanner {
	public int token;
	public File file;
	public FileReader fr;
	public char ctr;
	public String lexeme;
	public String tokenFinal;
	public String rsrvWord;
	ArrayList<ArrayList<String>> lexemes = new ArrayList<ArrayList<String>>();
	ArrayList<String> lexToken = new ArrayList<String>();
	String[] sample = {" ", " "};
	
	
	public scanner(String logic) {
		this.token=0;
		this.lexeme="";
		this.tokenFinal = "";
		this.rsrvWord = "";
		this.file = new File(logic);
		try(FileReader test = new FileReader(file)){
			this.fr = test;
			getChar();
			while (this.token != -1) {
				lex();
			}
			parser parse = new parser(lexemes);
			parse.start();
			//System.out.println(parse.sentence());
		} catch (IOException e) {
			e.printStackTrace();
		}
	} //end of scanner
	
	//subprogram to read file character by character
	public void getChar() {
		int content;
        try {
        	if ((content = fr.read()) != -1) {
        		ctr = (char)content;
        		if (Character.isAlphabetic(ctr)) {
        			token = 1; //LETTER
        		} else if (ctr==' ' || ctr=='\n'){ //if whitespace
        			token = 2;
        			
        		} else { 			//if (, ), ;
        			token = 0;
        		}

        	} else {
        		token=-1;
        	}
        	      	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} //end of getChar
	
	//subprogram to lookup for reserved words
	public String stringLookup(String str) {
		switch(str) {
		case "TRUE":
			rsrvWord = "CONSTANT";
			break;
		case "FALSE":
			rsrvWord = "CONSTANT";
			break;
		case "AND":
			rsrvWord = "OP";
			break;
		case "OR":
			rsrvWord = "OP";
			break;
		case "IMPLIES":
			rsrvWord = "OP";
			break;
		case "EQUIVALENT":
			rsrvWord = "OP";
			break;
		case "NOT":
			rsrvWord = "NOT";
			break;
		case "P":
			rsrvWord = "IDENT";
			break;
		case "Q":
			rsrvWord = "IDENT";
			break;
		case "R":
			rsrvWord = "IDENT";
			break;
		case "(":
			rsrvWord = "L_PAREN";
			break;
		case ")":
			rsrvWord = "R_PAREN";
			break;
		case ";":
			rsrvWord = "SEMICOLON";
			break;
		default:
			rsrvWord = "ERROR";
			break;
		}
		return rsrvWord;
	} //end of stringLookup()
 
	
	public void lex() {
		lexeme="";
		if (token==1) {
			while (token==1) {
				lexeme = lexeme + ctr;
				getChar();
			}
			tokenFinal = stringLookup(lexeme);
			
		} else if (token==0) {
			lexeme = lexeme + ctr;
			tokenFinal = stringLookup(lexeme);
			getChar();
		}
		ArrayList<String> lexToken = new ArrayList<String>();
		

		lexToken.add(lexeme);
		lexToken.add(tokenFinal);
		lexemes.add(lexToken);

		
		if (token==2) {
			getChar();
			

		
	} //end of lex
	
	}

}
