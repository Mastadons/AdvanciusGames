package net.advancius.game.commons;

import java.util.Arrays;
import java.util.Random;

public class EquationGenerator {

	static Random randomGenerator = new Random();
    public static char getRandomCharacter(String chars) {
        return chars.charAt(randomGenerator.nextInt(chars.length()));
    }
    
    public static int getRandomNumber(int max) {
        return (int)((Math.random()*max-max/2));
    }

    public static String appendToExpression(String equation, String value1, String value2) {
        String temp = equation;
        temp += value1;
        temp += value2;
        return temp;
    }

    public static boolean isOperator(char x) {
    		return Arrays.asList('+','-','*','/','^').contains(x);
    }
    
    public static String createExpression(int numOfOperators, int range) {
        int lastNumber = 0;
        int nextNumber = getRandomNumber(range);
        char nextOperator = getRandomCharacter("+-");
        String operatorOptions = "";
        String expression = ""+getRandomNumber(range);
        
        int openedParenthesis = 0;
        long exponentTotal = 0;
        
        boolean addOpenedParenthesis = false;
		boolean addClosedParenthesis = false;
		
        for (int i = 0; i < numOfOperators; i++) {
        		expression += nextOperator;	
        		if (addOpenedParenthesis) {
        			expression += '(';
        			openedParenthesis++;
        		}
        		if (addClosedParenthesis) {
        			expression += ')';
        			openedParenthesis--;
        		}
        		expression += nextNumber;
        		lastNumber = nextNumber;
        		
        		addOpenedParenthesis = false;
        		addClosedParenthesis = false;
        		
        		nextNumber = getRandomNumber(range);
        		operatorOptions = "++++----";
        		
        		expression.replace("^-", "^");
        		
        		if (nextNumber != 0 && (double)lastNumber/nextNumber % 1 == 0.0) {
        			operatorOptions += "///";
        		}
        		if (lastNumber*nextNumber < 100) {
        			operatorOptions += "***";
        		}
        		if (Math.pow(lastNumber, nextNumber) < 100 && exponentTotal < 100) {
        			operatorOptions += "^^";
        		}
        		
        		if (Math.random() < 0.2) {
        			if (Math.random() < 0.5) addOpenedParenthesis = true; 
        			else if (openedParenthesis > 0) addClosedParenthesis = true;
        		}
        		nextOperator = getRandomCharacter(operatorOptions);
        		
        		if (nextOperator == '^')
        			exponentTotal += Math.pow(lastNumber, nextNumber);
        }
        while (openedParenthesis > 0) {
        		expression += ')';
        		openedParenthesis--;
        }
        expression = expression.replace("^-", "^");
        return expression;
    }
    
    public static String createVariableExpression(int numOfOperators, int range, char var) {
		int lastNumber = 0;
	    int nextNumber = getRandomNumber(range);
	    char nextOperator = getRandomCharacter("+-");
	    String operatorOptions = "";
	    String expression = getRandomNumber(range)+"";
	    int openedParenthesis = 0;
	    
	    boolean addOpenedParenthesis = false;
		boolean addClosedParenthesis = false;
		
	    for (int i = 0; i < numOfOperators; i++) {
	    		expression += nextOperator;	
	    		if (addOpenedParenthesis) {
	    			expression += '(';
	    			openedParenthesis++;
	    		}
	    		if (addClosedParenthesis) {
	    			expression += ')';
	    			openedParenthesis--;
	    		}
	    		if (nextOperator == '/' && nextNumber == 0) nextNumber = 1;
	    		expression += nextNumber;
	    		if (Math.random() < 0.05) expression += var;
	    		lastNumber = nextNumber;
	    		
	    		addOpenedParenthesis = false;
	    		addClosedParenthesis = false;
	    		
	    		nextNumber = getRandomNumber(range);
	    		operatorOptions = "++++----";
	    		
	    		
	    		
	    		if (nextNumber != 0 && (double)lastNumber/nextNumber % 1 == 0.0) {
	    			operatorOptions += "///";
	    		}
	    		if (lastNumber*nextNumber < 100) {
	    			operatorOptions += "***";
	    		}
	    		
	    		if (Math.random() < 0.2) {
	    			if (Math.random() < 0.5) addOpenedParenthesis = true; 
	    			else if (openedParenthesis > 0) addClosedParenthesis = true;
	    		}
	    		nextOperator = getRandomCharacter(operatorOptions);
	    }
	    while (openedParenthesis > 0) {
	    		expression += ')';
	    		openedParenthesis--;
	    }
	    expression = expression.replace("^-", "^");
	    return expression;
	}
}