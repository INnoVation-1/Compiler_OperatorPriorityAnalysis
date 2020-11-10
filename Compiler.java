import java.io.File;
import java.io.FileInputStream;
import java.util.Stack;
import java.io.IOException;

public class Compiler {
	public Stack<Character> objStack = new Stack<Character>();
	public Stack<Character> operatorStack = new Stack<Character>();
	public Stack<Character> strStack = new Stack<Character>();
	int[][] relationMatrix = {
			{1, 0, 0, 0, 1, 1}, 
			{1, 1, 0, 0, 1, 1}, 
			{1, 1, -1, -1, 1, 1}, 
			{0, 0, 0, 0, 2, -1}, 
			{1, 1, -1, -1, 1, 1}, 
			{0, 0, 0, 0, -1, 2}
			};
	public void fOpen(String inSource) {
		File file = new File(inSource);
		try {
			 FileInputStream input=new FileInputStream(file);
			 int size=input.available();
			 byte[] buffer=new byte[size];
			 input.read(buffer);
			 input.close();
			 String inputString = new String(buffer,"GB2312");
			 this.strStack.push('#');
			 this.operatorStack.push('#');
			 for(int i = inputString.length() - 1; i >= 0; i--) {
				 if(Character.isLetter(inputString.charAt(i)) || inputString.charAt(i) == 'i' || inputString.charAt(i) == '+' || inputString.charAt(i) == '(' || inputString.charAt(i) == ')' || inputString.charAt(i) == '*') {
					 this.strStack.push(inputString.charAt(i));
				 } 
			 }
		} catch (IOException e) {
			
		}
	}
	public char readinChar;
	public boolean getChar() {
		this.readinChar = this.strStack.pop();
		if(search(this.readinChar) == 6) {
			return false;
		}
		return true;
	}
	public static int stackLen(Stack<Character> st) {
		int cnt = 0;
		for(int i = 0; i < st.size(); i++) {
			if(st.get(i) == '(' || st.get(i) == ')') {
				continue;
			}
			cnt ++;
		}
		return cnt;
	}
	public int search(char readinChar) {
		switch(readinChar) {
		case '+':
			return 0;
		case '*':
			return 1;
		case 'i':
			return 2;
		case '(':
			return 3;
		case ')':
			return 4;
		case '#':
			return 5;
		default:
			return 6;
		}
	}

	public static void main(String[] args) {
		Compiler compiler = new Compiler();
		compiler.fOpen(args[0]);
		boolean a = false;
		boolean b = false;
		
		while(!a) {
			if(!compiler.getChar()) {
				System.out.println('E');
				break;
			}
			int sign1 = compiler.search(compiler.readinChar);
			while(!compiler.operatorStack.empty()) {
				b = false;
				int sign2 = compiler.search(compiler.operatorStack.peek());
				//System.out.println(test.relation[sign2][sign1]);
				switch(compiler.relationMatrix[sign2][sign1]) {
				// <
				case 0:
					System.out.println("I" + compiler.readinChar);
					compiler.operatorStack.push(compiler.readinChar);
					b = true;
					break;
				// >
				case 1:
					if(compiler.operatorStack.peek() == 'i') {
						compiler.objStack.push('N');
						compiler.operatorStack.pop();
					}else {
						try {
							if(stackLen(compiler.operatorStack) != compiler.objStack.size()) {
								System.out.println("RE");
								b = true;
								a = true;
								break;
							}
							compiler.objStack.pop();
							compiler.objStack.pop();
							compiler.objStack.push('N');
							compiler.operatorStack.pop();
						}catch(Exception e) {
							System.out.println("RE");
							b = true;
							a = true;
							break;
						}
					}
					System.out.println("R");
					break;
				// =
				case 2:
					if(compiler.readinChar == '#') {
						b = true;
						a = true;
						break;
					}
					System.out.println("I" + compiler.readinChar);
					System.out.println("R");
					compiler.operatorStack.pop();
					b = true;
					break;
				case -1:
					System.out.println("E");
					b = true;
					a = true;
					break;
				}
				if(b) {
					break;
				}
			}
		}
	}
}
