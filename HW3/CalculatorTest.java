import java.io.*;
import java.util.*;

public class CalculatorTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
				System.out.println("ERROR");
			}
		}
	}

	private static void command(String input) {


		if(checkValidSpace(input)&&checkNumInSign(input)) {
			input = input.replaceAll("\\s+", "").replaceAll("\t", "");
			String postfixEx;long result;

			//This part will change the expression and calculate the result
			try {
				postfixEx = infixToPostfix(input);
				if (!postfixEx.equals("ERROR")) {
					result = calculatePostfix(postfixEx.toCharArray());
					System.out.println(postfixEx);
					System.out.println(result);return;
				}
			}

			//The error happens when calculating the result handle at this part
			catch (EmptyStackException | CalculateException e) {
				System.out.println("ERROR");return;
			}
		}

		System.out.println("ERROR");
	}


	/*
	The following two function checks three Error situations
	1. There should be an operator between two numbers & check if the num is next to parenthesis (ex. "1(")
	2. There should be a number between comma and parenthesis & check if the num is next to parenthesis (ex. ")1")

	This function does not check whether the parenthesis is valid. Also does not check if the operator is consecutive.
	*/

	public static boolean checkValidSpace(String expression){
		boolean lastWasOperator=true;

		for(int i=0; i<expression.length(); i++){
			char c = expression.charAt(i);

			if(Character.isDigit(c)&&i!=expression.length()-1){
				char nextChar = expression.charAt(i+1);
				if(nextChar=='('){return false;}
				if(!Character.isDigit(nextChar)) {lastWasOperator = false;}
			}else if(Character.isWhitespace(c)&&i!=expression.length()-1){
				char nextChar = expression.charAt(i+1);
				if(Character.isDigit(nextChar) && !lastWasOperator){
					return false;
				}
			}else if (precedence(c)!=-1){
				lastWasOperator = true;
			}
		}return true;
	}

	public static boolean checkNumInSign(String expression) {
		boolean isNumInsideParen = true;
		boolean isNumInsideComma = true;

		for (int i = 0; i < expression.length(); i++) {
			char c = expression.charAt(i);

			if (Character.isDigit(c)) {
				isNumInsideParen = true;isNumInsideComma = true;
			} else if(c=='('){
				isNumInsideParen = false;
				if(i!=expression.length()-1){
					if(expression.charAt(i+1)==','){return false;}
				}
			}else if(c==','){
				if(i!=expression.length()-1){
					if(expression.charAt(i+1)==')'){return false;}
				}if(!isNumInsideComma){return false;}
				isNumInsideComma = false;
			}else if(c==')'){
				if(!isNumInsideParen){return false;}
				if(i!=expression.length()-1){
					if(Character.isDigit(expression.charAt(i+1))){return false;}
				}
			}
		}return true;
	}

	/*
	Function to change infix expression to postfix expression.
	Helped by ChatGPT and modified.
	*/


	public static String infixToPostfix(String expression) throws EmptyStackException{
		Stack<Character> stack = new Stack<>();

		boolean lastWasOperator = true; // to handle unary minus
		int inParenthesis = 0;

		StringBuilder number = new StringBuilder();
		StringBuilder postfix = new StringBuilder();

		for (int i = 0; i < expression.length(); i++) {
			char c = expression.charAt(i);

			if (Character.isDigit(c)) {
				number.append(c);
				lastWasOperator = false;
			} else {

				if (number.length() > 0) {
					postfix.append(number).append(" ");
					number = new StringBuilder();
				}


				if (c == '(') {
					stack.push(c);
					lastWasOperator = true;inParenthesis++;
				} else if (c == ')') {
					long numForAvg = 0;
					while (!stack.isEmpty() && stack.peek() != '(') {
						if(stack.peek()==','){
							numForAvg++;stack.pop();
						}else {
							postfix.append(stack.pop()).append(" ");
						}
					}
					if (!stack.isEmpty() && stack.peek() != '(') {
						return "ERROR";
					} else {
						stack.pop();
						if(numForAvg!=0){
							postfix.append((numForAvg+1)+" avg ");
						}
					}
					lastWasOperator = false;inParenthesis--;
				}


				else {
					if (lastWasOperator && c == '-') {
						// Handle unary minus by converting it to ~
						c = '~';
					} else if (c == '^') {
						// Handle right-associativity of ^
						while (!stack.isEmpty() && precedence(c) < precedence(stack.peek())) {
							postfix.append(stack.pop()).append(" ");
						}
					}else if(c==','){
						if(inParenthesis<=0){return "ERROR";}
						while(!stack.isEmpty() && stack.peek() != '(' && stack.peek()!=','){
							postfix.append(stack.pop()).append(" ");
						}
					}else {
						while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
							postfix.append(stack.pop()).append(" ");
						}
					}
					stack.push(c);
					lastWasOperator = true;
				}
			}
		}
		if (number.length() > 0) {
			postfix.append(number).append(" ");
		}

		while (!stack.isEmpty()) {
			if (stack.peek() == '(') {
				return "ERROR";
			}
			postfix.append(stack.pop()).append(" ");
		}

		return postfix.toString().trim();
	}


	/*
	Function for real calculating the postfix expression
	Divided into two part
	-reading the number in postfix expression
	-calculate the result using the stack

	Helped by ChatGPT and modified.
	*/

	public static long calculatePostfix(char[] postfix) throws CalculateException {
		Stack<Long> stack = new Stack<>();

		for (int i = 0; i < postfix.length; i++) {
			char c = postfix[i];

			if (Character.isDigit(c)) {
				long num = 0;
				while (i < postfix.length && Character.isDigit(postfix[i])) {
					num = num * 10 + (postfix[i] - '0');
					i++;
				}i--;
				stack.push(num);
			} else if (c == '~') {
				long operand = stack.pop();
				stack.push(-1*operand);
			} else if(c=='a'){
				long num = stack.pop(), sum = 0;
				for(int j=0; j<num; j++){
					sum+=stack.pop();
				}
				stack.push(sum/num);i+=2;
			}else if (!Character.isWhitespace(c)){
				long operand2 = stack.pop();
				long operand1 = stack.pop();
				long result = calculateOperator(operand1,operand2,c);
				stack.push(result);
			}
		}
		return stack.pop();
	}

	/*
	Checking precedence for operators
	unary minus uses the expression '~'
	*/

	private static int precedence(char c) {
		switch (c) {
			case ',':
				return 0;
			case '+':
			case '-':
				return 1;
			case '*':
			case '/':
			case '%':
				return 2;
			case '~': // new operator for unary minus
				return 3;
			case '^':
				return 4;
			default:
				return -1;
		}
	}


	/*
	Function for calculating(used in the function calculatePostfix)
	This function only deals with +,-,*,/,%,^
	Calculating the average will not be handled in this function
	*/

	private static long calculateOperator(long operand1, long operand2, char operator) throws CalculateException {
		switch (operator) {
			case '+':
				return operand1 + operand2;
			case '-':
				return (operand1 - operand2);
			case '*':
				return (operand1 * operand2);
			case '/':
				if (operand2==0){
					throw new CalculateException("ERROR");
				}return (operand1 / operand2);
			case '%':
				if (operand2==0){
					throw new CalculateException("ERROR");
				}return (operand1 % operand2);
			case '^':
				if (operand1==0 && operand2<0){
					throw new CalculateException("ERROR");
				}return ((long) Math.pow(operand1, operand2));
		}
		return 0;
	}

	//Exception will be thrown if the calculating is impossible
	private static class CalculateException extends Exception {
		public CalculateException(String message) {
			super(message);
		}
	}

}
