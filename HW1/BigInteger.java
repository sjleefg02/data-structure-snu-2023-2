import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  
  
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "Wrong Input";
  
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("(?<num1>[-+]?[0-9]{1,100})(?<operator>[-+*])(?<num2>[-+]?[0-9]{1,100})");
    public static final Pattern BIGINTEGER_PATTERN = Pattern.compile("^-+?[0-9]{0,100}$");

    public boolean sign;
    public char[] numArr;
    public int numLength;

    public BigInteger(boolean sign, char[] numArr){
        this.sign = sign;
        this.numArr = numArr;
        this.numLength = numArr.length;
    }
    public BigInteger(String s)
    {
        this.sign = s.charAt(0) != '-';
        boolean signCheck = s.matches("^[0-9]{0,100}$");
        this.numLength = signCheck? s.length():s.length()-1;
        this.numArr = new char[this.numLength];

        for(int i=0; i<numLength; i++){
            this.numArr[i]= signCheck ? s.charAt(i):s.charAt(i+1);
        }
    }

    private static boolean isBigger(char[] num1, char[] num2){
        int realLength1 = num1.length; int realLength2 = num2.length;
        int i,j;
        for(i=0; i<num1.length; i++){
            if(num1[i]=='0'){realLength1--;}
            else{break;}
        }
        for(j=0; j<num2.length; j++){
            if(num2[j]=='0'){realLength2--;}
            else{break;}
        }

        if(realLength1==realLength2){
            for(int k=0; k<realLength1; k++){
                if(num1[k+i]!=num2[k+j]){
                    return num1[k+i]>num2[k+j];
                }
            }return true;
        }return num1.length> num2.length;
    }

    private static char[] absAdd(char[] num1, char[] num2){
        char[] longInt = isBigger(num1,num2) ? num1:num2;
        char[] shortInt = isBigger(num1,num2) ? num2:num1;

        int carry = 0; int diff = longInt.length-shortInt.length;
        char[] result = new char[longInt.length+1];

        for(int i=result.length-1; i>0; i--){
            int sum = i-1-diff>=0?carry+longInt[i-1]-'0'+shortInt[i-1-diff]-'0':carry+longInt[i-1]-'0';
            if(sum>=10) {
                result[i] = (char) (sum - 10+'0');
                carry = 1;
            }else{
                result[i] = (char) (sum+'0');
                carry = 0;
            }
        }result[0] = (char)(carry+'0');

        return result;
    }

    private static char[] absSubtract(char[] num1, char[] num2){
        char[] longInt = isBigger(num1,num2) ? num1:num2;
        char[] shortInt = isBigger(num1,num2) ? num2:num1;


        int carry = 0;int diff = longInt.length-shortInt.length;
        char[] result = new char[longInt.length];


        for(int i=result.length-1; i>=0; i--){
            int sum = i-diff>=0?longInt[i]-shortInt[i-diff]-carry:longInt[i]-carry-'0';
            if(sum<0) {
                result[i] = (char) (sum +10+'0');
                carry = 1;
            }else{
                result[i] = (char) (sum+'0');
                carry = 0;
            }
        }
        return result;
    }

    private char[] absMultiply(BigInteger big, int number, int position){
        char[] result = new char[big.numLength+1+position];
        int mulCarry = 0;int zeroInsert = 0;
        for(int j=result.length-1; j>0; j--){
            if(zeroInsert<position){result[j] = '0';zeroInsert++;}
            else {
                result[j] = (char) (((big.numArr[j - 1] - '0') * number + mulCarry) % 10 + '0');
                mulCarry = ((big.numArr[j - 1] - '0') * number + mulCarry) / 10;
            }
        }result[0] = (char)(mulCarry+'0');
        return result;

    }

    public BigInteger add(BigInteger big)
    {
        if(this.sign==big.sign){
            return new BigInteger(this.sign,absAdd(this.numArr, big.numArr));
        }else{
            boolean sign = isBigger(this.numArr,big.numArr) ? this.sign : big.sign;
            return new BigInteger(sign,absSubtract(this.numArr,big.numArr));
        }
    }
  
    public BigInteger subtract(BigInteger big)
    {
        if(this.sign==big.sign){
            boolean sign = isBigger(this.numArr, big.numArr) == this.sign;
            return new BigInteger(sign, absSubtract(this.numArr,big.numArr));
        }else{
            return new BigInteger(this.sign,absAdd(this.numArr, big.numArr));
        }
    }
  
    public BigInteger multiply(BigInteger big)
    {
        boolean sign = this.sign==big.sign;
        char[] result = {'0'};int position = 0;
        for(int i=numLength-1; i>=0; i--){
            int number = this.numArr[i]-'0';
            char[] mulResult = this.absMultiply(big,number,position++);
            result = absAdd(result,mulResult);
        }
        return new BigInteger(sign, result);
    }


  
    @Override
    public String toString()
    {   boolean checkZero=true;int zeroLocation = 0;
        for(int i=0; i<numLength; i++){
            if(numArr[i]!='0'){checkZero=false;zeroLocation = i;break;}
        }
        if(checkZero){
            return "0";
        }
        String str = sign?"":"-";
        return str + new String(Arrays.copyOfRange(numArr,zeroLocation,numLength));
    }

    private static boolean inputValueCheck(String input){
        Matcher matcher = EXPRESSION_PATTERN.matcher(input);
        return matcher.matches();
    }

    private static BigInteger operate(BigInteger num1, BigInteger num2, String operator){
        if(operator.equals("+")){
            return num1.add(num2);
        }else if(operator.equals("-"))
            return num1.subtract(num2);
        else if(operator.equals("*"))
            return num1.multiply(num2);
        else return null;
    }
  
    static BigInteger evaluate(String inputOri) throws IllegalArgumentException
    {
        // implement here
        String input = inputOri.replace(" ","");
        if(!inputValueCheck(input))
            throw new IllegalArgumentException();

        BigInteger bigIntNum1=null,bigIntNum2 = null;String operator="";
        Matcher groupMatcher = EXPRESSION_PATTERN.matcher(input);

        while (groupMatcher.find()){
            bigIntNum1 = new BigInteger(groupMatcher.group("num1"));
            operator = groupMatcher.group("operator");
            bigIntNum2 = new BigInteger(groupMatcher.group("num2"));
        }
        return BigInteger.operate(bigIntNum1,bigIntNum2,operator);
    }

  
    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
  
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
            return false;
        }
    }
  
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
