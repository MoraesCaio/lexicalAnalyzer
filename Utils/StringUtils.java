package Utils;

/**
 * Created by caiomoraes on 08/08/17.
 */
public class StringUtils
{



    public boolean isLastChar(int stringLength, int idx)
    {
        return idx+1 == stringLength;
    }
    

    
    public boolean isLastChar(String string, int idx)
    {
        return idx+1 >= string.length();
    }


    
    public boolean nextCharIsDigit(String string, int idx)
    {
        return !isLastChar(string, idx) &&
                Character.isDigit(string.charAt(idx+1));
    }



    public boolean nextCharEquals(String string, int idx, char c)
    {
        return !isLastChar(string.length(), idx) &&
                string.charAt(idx+1) == c;
    }
    


    public boolean nextCharIsIn(String string, int idx, String chars)
    {
        return !isLastChar(string.length(), idx) &&
                chars.indexOf(string.charAt(idx+1)) != -1;
    }
    


    public boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }



    public boolean isLetterOrDigit(char c) {
        return isLetter(c) || (c >= '0' && c <= '9');
    }
}
