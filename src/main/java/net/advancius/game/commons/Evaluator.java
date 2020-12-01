package net.advancius.game.commons;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.EmptyStackException;
import java.util.Stack;

public class Evaluator { 
	
	public static String quickfix(String expression) {
		expression = expression.replace("+-", "-");
		expression = expression.replace("-+", "+");
		expression = expression.replace("++", "+");
		expression = expression.replace("--", "+");
		expression = expression.replace("*(", "(");
		expression = expression.replace("(-", "(0-");
		for (char c : "+-*/^".toCharArray()) expression = expression.replace(c+")", ")");
		return expression;
	}
	public static BigDecimal evaluateX(String expression, char var, double x, int divisionCap) {
		expression = '0' + quickfix(expression);
		char[] tokens = expression.toCharArray(); 
		//System.out.println(expression);
		
		boolean nextNegative = false;
	    // Stack for numbers: 'values' 
	   Stack<BigDecimal> values = new Stack<BigDecimal>(); 
	
	   // Stack for Operators: 'ops' 
	   Stack<Character> ops = new Stack<Character>(); 
	   for (int i = 0; i < tokens.length; i++) 
	   { 
		   //System.out.println("Tok: " + tokens[i]);
	        // Current token is a whitespace, skip it 
	       if (tokens[i] == ' ' || tokens[i] == ',') 
	           continue; 
	
	       // Current token is a number, push it to stack for numbers 
	       if (tokens[i] >= '0' && tokens[i] <= '9') 
	       { 
	    	   		
	       		//System.out.println(i);
	           StringBuffer sbuf = new StringBuffer();
	           sbuf.append(tokens[i]);
	           // There may be more than one digits in number
	           if (i+1 < tokens.length && i+1 == '.') {
	           		sbuf.append('.');
	           		i++;
	           }
	           while (i+1 < tokens.length && tokens[i+1] >= '0' && tokens[i+1] <= '9') {
	               sbuf.append(tokens[i+1]);
	               i++;
	           }
	           
	           BigDecimal val = new BigDecimal(sbuf.toString());
	           if (nextNegative) {
	           		val = val.multiply(new BigDecimal(-1));
	           		nextNegative = false;
	           }
	           values.push(val);
	           //System.out.println(val.toPlainString());
	           if (i+1 < tokens.length && tokens[i+1] == '(')
	           		ops.push('*');
	       } 
	
	       // Current token is an opening brace, push it to 'ops' 
	       else if (tokens[i] == '(') {
	           ops.push(tokens[i]); 
	       }
	
	       // Closing brace encountered, solve entire brace 
	       else if (tokens[i] == ')') {
	
	       		while (ops.size() != 0 && ops.peek() != '(') {
	           		values.push(applyOp(ops.pop(), values.pop(), values.pop(), divisionCap));
	           }
	           if (ops.size() != 0) ops.pop(); 
	           
	           if (i+1 < tokens.length && tokens[i+1] >= '0' && tokens[i+1] <= '9') {
	           		ops.push('*');
	           }
	       } 
	
	       // Current token is an operator. 
	       else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '÷' ||
	                tokens[i] == '*' || tokens[i] == '/' || tokens[i] == '^') 
	       { 
	       		if (tokens[i] == '-' && (tokens[i-1] == '+' || tokens[i-1] == '-' || tokens[i-1] == '÷' ||
	                   tokens[i-1] == '*' || tokens[i-1] == '/' || tokens[i-1] == '^')) {
	          		nextNegative = true;
	          } else {
	       	    // While top of 'ops' has same or greater precedence to current 
	           // token, which is an operator. Apply operator on top of 'ops' 
	           // to top two elements in values stack 
	       	   		while (!ops.empty() && !values.empty() && hasPrecedence(tokens[i], ops.peek())) 
	       	   			values.push(applyOp(ops.pop(), values.pop(), values.pop(), divisionCap)); 
	
	           
	           // Push current token to 'ops'. 
	           		ops.push(tokens[i]);
	          }
	       } 
	       else if (tokens[i] == 'π') {
	       		values.push(new BigDecimal(Math.PI));
	       } else if (tokens[i] == var) {
	    	   		if (i-1 > -1 && tokens[i-1] >= '0' && tokens[i-1] <= '9') {
	   	   			ops.push('*');
	   	   		}
	    	   		values.push(new BigDecimal(x));
   	   		
   	   		if (i+1 < tokens.length && tokens[i+1] >= '0' && tokens[i+1] <= '9') {
   	   			throw new UnsupportedOperationException("A variable cannot be followed by a number.");
   	   		}
   	   		if (i+1 < tokens.length && tokens[i+1] == '(')
              		ops.push('*');
	       }
	   } 
	
	   // Entire expression has been parsed at this point, apply remaining 
	   // ops to remaining values 
	   
	   while (!ops.empty() && values.size() > 1) { 
	   		char op = ops.pop();
	       values.push(applyOp(op, values.pop(), values.pop(), divisionCap)); 
	   }
	   // Top of 'values' contains result, return it 
	   return values.pop().stripTrailingZeros(); 
	}
	
    public static BigDecimal evaluate(String expression, int divisionCap) throws EmptyStackException {
    		expression = '0' + quickfix(expression);
    		char[] tokens = expression.toCharArray(); 
        
    		boolean nextNegative = false;
         // Stack for numbers: 'values'
        Stack<BigDecimal> values = new Stack<BigDecimal>();
  
        // Stack for Operators: 'ops'
        Stack<Character> ops = new Stack<Character>();
        for (int i = 0; i < tokens.length; i++) 
        { 
             // Current token is a whitespace, skip it 
            if (tokens[i] == ' ' || tokens[i] == ',') 
                continue; 
  
            // Current token is a number, push it to stack for numbers 
            if (tokens[i] >= '0' && tokens[i] <= '9') 
            { 
            		
            		//System.out.println(i);
            	
                StringBuffer sbuf = new StringBuffer();
                sbuf.append(tokens[i]);
                // There may be more than one digits in number
                if (i+1 < tokens.length && i+1 == '.') {
                		sbuf.append('.');
                		i++;
                }
                while (i+1 < tokens.length && tokens[i+1] >= '0' && tokens[i+1] <= '9') {
                    sbuf.append(tokens[i+1]);
                    i++;
                }
                BigDecimal val = new BigDecimal(sbuf.toString());
                if (nextNegative) {
                		val = val.multiply(new BigDecimal(-1));
                		nextNegative = false;
                }
                values.push(val);
                if (i+1 < tokens.length && tokens[i+1] == '(')
                		ops.push('*');
            } 
  
            // Current token is an opening brace, push it to 'ops'
            else if (tokens[i] == '(') {
                ops.push(tokens[i]); 
            }
  
            // Closing brace encountered, solve entire brace
            else if (tokens[i] == ')') {

            		while (ops.size() != 0 && ops.peek() != '(') {
                		values.push(applyOp(ops.pop(), values.pop(), values.pop(), divisionCap));
                }
                if (ops.size() != 0) ops.pop(); 
                
                if (i+1 < tokens.length && tokens[i+1] >= '0' && tokens[i+1] <= '9') {
                		ops.push('*');
                }
            } 
  
            // Current token is an operator.
            else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '÷' ||
                     tokens[i] == '*' || tokens[i] == '/' || tokens[i] == '^') 
            { 
            		if (tokens[i] == '-' && (tokens[i-1] == '+' || tokens[i-1] == '-' || tokens[i-1] == '÷' ||
                        tokens[i-1] == '*' || tokens[i-1] == '/' || tokens[i-1] == '^')) {
               		nextNegative = true;
               } else {
            	    // While top of 'ops' has same or greater precedence to current
                // token, which is an operator. Apply operator on top of 'ops'
                // to top two elements in values stack
            	   		while (!ops.empty() && !values.empty() && hasPrecedence(tokens[i], ops.peek())) 
            	   			values.push(applyOp(ops.pop(), values.pop(), values.pop(), divisionCap)); 
  

                // Push current token to 'ops'.
                		ops.push(tokens[i]);
               }
            } 
            else if (tokens[i] == 'π') {
            		values.push(new BigDecimal(Math.PI));
            }
        } 

        // Entire expression has been parsed at this point, apply remaining
        // ops to remaining values

        while (!ops.empty() && values.size() > 1) { 
        		char op = ops.pop();
            values.push(applyOp(op, values.pop(), values.pop(), divisionCap)); 
        }
        // Top of 'values' contains result, return it
        return values.pop().stripTrailingZeros();
    }

    // Returns true if 'op2' has higher or same precedence as 'op1',
    // otherwise returns false.
    public static boolean hasPrecedence(char op1, char op2) 
    { 
        if (op2 == '(' || op2 == ')') 
            return false;
        if ((op1 == '^') && op2 != '^')
        		return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) 
            return false; 
        else
            return true; 
    }

    // A utility method to apply an operator 'op' on operands 'a'
    // and 'b'. Return the result.
    public static BigDecimal applyOp(char op, BigDecimal b, BigDecimal a, int divisionCap) 
    { 
        switch (op) 
        { 
        case '+': 
        		//System.out.println(a + " + " + b + " = " + a.add(b));
            return a.add(b);
        case '-': 
        		//System.out.println(a + " - " + b + " = " + a.subtract(b));
            return a.subtract(b);
        case '*': 
        		//System.out.println(a + " * " + b + " = " + a.multiply(b));
            return a.multiply(b);
        case '÷':
        case '/': 
            if (b.compareTo(BigDecimal.ZERO) == 0) 
                throw new UnsupportedOperationException("Cannot divide by zero");
            //System.out.println(a + " / " + b + " = " + a.divide(b, divisionCap, RoundingMode.DOWN));
            return a.divide(b, divisionCap, RoundingMode.DOWN);
        case '^':
        		//System.out.println(a + " ^" + b + " = " + a.pow(b.intValue()));
    			if (b.intValue() > 100) return BigDecimal.ZERO;
        		return a.pow(b.intValue(), new MathContext(10));
        } 
        return BigDecimal.ZERO;
    }
}
