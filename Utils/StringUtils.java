package Utils;

/**
 * Created by caiomoraes on 08/08/17.
 */
public class StringUtils
{



    public boolean isLastChar(int idx, int length)
    {
        return idx+1 == length;
    }
    

    
    public boolean isLastChar(int idx, String substring)
    {
        return idx+1 == substring.length();
    }

    
    
    public boolean nextCharIsDigit(int idx, String substring)
    {
        return !isLastChar(idx, substring) &&
                Character.isDigit(substring.charAt(idx+1));
    }
    


    public boolean nextCharEquals(int idx, String substring, char c)
    {
        return !isLastChar(idx, substring.length()) &&
                substring.charAt(idx+1) == c;
    }
    


    public boolean nextCharIsIn(int idx, String substring, String chars)
    {
        return !isLastChar(idx, substring.length()) &&
                chars.indexOf(substring.charAt(idx+1)) != -1;
    }
    


    public boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }



    public boolean isLetterOrDigit(char c) {
        return isLetter(c) || (c >= '0' && c <= '9');
    }
}
