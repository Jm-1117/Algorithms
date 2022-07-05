import java.util.ArrayList;

public class parser {
	ArrayList<ArrayList<String>> lexemes = new ArrayList<ArrayList<String>>();
	ArrayList<String> expression = new ArrayList<String>();
	public String token;
	public String lexeme;
	public String nextToken;
	public String nextLexeme;
	public int ctr,start,end;
	public int outcomes=1;
	public int ctrOutcomes=0;
	public boolean P,Q,R=false;
	public boolean presentP,presentQ,presentR=false;
	public int identCount=0;
	
	public parser (ArrayList<ArrayList<String>> lexeme) {
		this.lexemes = lexeme;
		this.ctr=-0;
	}
	
	//get next lexeme and token
	public void next() { 
		ctr++;
		lexeme = lexemes.get(ctr).get(0);
        token = lexemes.get(ctr).get(1);	
	}
	
	//see next token but do not use yet
	public String peek() {
		nextLexeme = lexemes.get(ctr+1).get(0);
        nextToken = lexemes.get(ctr+1).get(1);
        
        return nextToken;
	}
	
	public void start() {
		boolean result;
		start = ctr;
		result = sentence();
		String expr =expression.toString();
		expression.clear();
		if (peek()=="SEMICOLON") {
			if (presentP) {
				if(presentQ) {
					if(presentR) {
						if (ctrOutcomes==0) {
							System.out.println("\n  P    Q     R  "  + expr);
							expression.clear();
						}	
						System.out.println( ""+ P + "  "+Q +"  "+ R+"   "+ result);
					}else {
						if (ctrOutcomes==0) {
							System.out.println("\n  P    Q   "  + expr);
							expression.clear();
						}
						System.out.println("" + P +"  "+  Q +"   "+ result);
					}//presentR
				} else {
					if (ctrOutcomes==0) {
						System.out.println("\n  P  "  + expr);
						expression.clear();
					}
					System.out.println("" + P  +"   "+ result);
				}//presentQ
			} else {
				System.out.println("" + result);
			}//presentP
			next();
			ctrOutcomes++;
			end=ctr;
		}
		
		int out=1;
		for(int i=0;i<identCount;i++) {
			out = out*2;
			outcomes=out;		
		}
		
		while(ctrOutcomes<outcomes) {	
			ctr=start;
			
			if(presentP) {
				if (ctrOutcomes>=2 && identCount==2) { // P = T T F F
					P = false;
				} else if (ctrOutcomes>=1 && identCount==1) {  // P = T F
					P = false;
				} else if (ctrOutcomes>=4 && identCount==3) { // P = T T T T F F F F
					P = false;
				}
			
				if(presentQ) {
					if (Q==true && identCount==2) { // P = T F T F
						Q = false;
					} else if (Q==false && identCount==2) { 
						Q = true;
					} else if (ctrOutcomes>=2 && identCount==3) { // P = T T F F 
						Q = false;
					}
					
					if(presentR) {
						if (R==true && identCount==3) { // P = T F T F
							R = false;
						} else if (R==false && identCount==3) { 
							R = true;
						} 
					}
				}
			}
				
			start();
			start=end;
				
		}	
		
		if (ctr<lexemes.size()-1) {
			next();
			System.out.println("\n==================================\n");
			ctrOutcomes=0;
			identCount=0;
			P=Q=R=false;
			presentP=presentQ=presentR=false;
			start();
		} else {
			System.exit(0);
		}
		
	}
	
	
	public boolean sentence() {
		boolean sentenceValue = true;
		
		// if next token TRUE, FALSE, P, Q, R: call atomicSentence()
		if(peek()=="CONSTANT" || peek()=="IDENT") {
			sentenceValue = atomicSentence();
		}
		
		// if next token is left parenthesis, call complexSentence()
		if(peek()=="L_PAREN" || peek()=="NOT") {
			sentenceValue = complexSentence();
		}
		
		// while next token is an operator, evaluate left and right hand side of the operator
		while (peek()=="OP") {
			next();
			expression.add(lexeme); //add lexeme to arraylist to be printed as string
			if (ctrOutcomes==0) {
			System.out.println("Next Lexeme: " + lexeme + " token: " + token);
			}
			if(peek()=="OP"||peek()=="SEMICOLON"||peek()=="R_PAREN") {
				System.out.println("ERROR IN PARSING. <sentence> <connective> <sentence>");
				System.exit(0);
			}
			
			
			String operator = lexeme;
			boolean nextVal = complexSentence();
			// evaluate the expression
			switch(operator) {
			
			case "AND":
				sentenceValue = sentenceValue && nextVal;
				break;
			case "OR":
				sentenceValue = sentenceValue || nextVal;
				break;
			case "EQUIVALENT":
				sentenceValue = (sentenceValue== nextVal);
				break;
			case "IMPLIES":
				if (sentenceValue==true && nextVal==true) {
					sentenceValue=true;
				} else if (sentenceValue==false && nextVal==false){
					sentenceValue = true;
				} else {
					sentenceValue = false;
				}
				break;
			}	

		}
		
		if (peek()=="ERROR") {
			System.out.println("Next Lexeme: " + nextLexeme);
			System.out.println("INCORRECT GRAMMAR");
			System.exit(0);
		}
		
		//if (peek()=="SEMICOLON") {
		//	if (ctrOutcomes==0) {
		//	System.out.println("Next Lexeme: " + nextLexeme + " token: " + nextToken);
		//	}
	//		return sentenceValue;
	//	}
		
		return sentenceValue;
	}
	
	public boolean atomicSentence() {
		boolean value = true;
		next();
		expression.add(lexeme);
		if (ctrOutcomes==0) {
		System.out.println("Next Lexeme: " + lexeme + " token: " + token);
		}
		switch(lexeme) {
		case "TRUE":
			value = true;
			break;
		case "FALSE":
			value = false;
			break;
		case "P":
			if(presentP==false) {
				P =true;
				identCount++;
				presentP=true;
			}
			value = P;
			break;
		case "Q":
			if(presentQ==false) {
				Q=true;
				identCount++;
				presentQ=true;
			}
			value = Q;
			break;
		case "R":
			if(presentR==false) {
				R=true;
				identCount++;
				presentR=true;
			}
			value = R;
			break;
		}
		
		if (peek()=="L_PAREN"||peek()=="NOT"||peek()=="CONSTANT"||peek()=="IDENT") {
			System.out.println("ERROR IN PARSING. <atomicSentence>");
			System.exit(0);
		}
		
		return value;
	}
	
	
	public boolean complexSentence() {
		boolean complexValue = true;
		
		if(peek()=="CONSTANT"||peek()=="IDENT") {
			
			//System.out.println("constant(1): " + lexeme);
			complexValue = sentence();
		}
		
		// '(' <sentence> ')'
		if (peek()=="L_PAREN") {
			next();
			if(peek()=="OP") {
				System.out.println("ERROR IN PARSING. '(' <sentence> ')'");
				System.exit(0);
			}
			expression.add(lexeme);
			
			if (ctrOutcomes==0) {
				System.out.println("Next Lexeme: " + lexeme + " token: " + token);
			}
			
			complexValue = sentence();
			if(peek()!="R_PAREN") {
				System.out.println("ERROR IN PARSING. MISSING ')'.");
				System.exit(0);
			} else {
				next();
				expression.add(lexeme);
				if (ctrOutcomes==0) {
				System.out.println("Next Lexeme: " + lexeme + " token: " + token);
				}
			}
		}
		
		// "NOT" <sentence>
		if (peek()=="NOT") {
			next();
			expression.add(lexeme);
			if (ctrOutcomes==0) {
			System.out.println("Next Lexeme: " + lexeme + " token: " + token);
			}
			if(peek()!="L_PAREN" && peek()!="IDENT" && peek()!="CONSTANT") {
				System.out.println("ERROR IN PARSING. 'NOT' <sentence>");
				System.exit(0);
			}
			
			complexValue = !sentence();
		}
		
		return complexValue;
	}
	
}//end of class parser
