package Utils;

/**
 * This class contains some methods contains boolean methods for strings.
 *
 * Created on 08/08/17 by
 *
 * Caio Moraes
 * GitHub: MoraesCaio
 * Email: caiomoraes
 *
 * Janyelson Oliveira
 * GitHub: janyelson
 * Email: janyelsonvictor@gmail.com
 */

public class StringUtils
{

    /**
     * Checks if the index it's the last of the string.
     * @param string A not null String.
     * @param idx Index for testing.
     * @return true, if idx is greater than or equal to the string length; false, otherwise.
     */
    public boolean isLastChar(String string, int idx)
    {
        return idx+1 >= string.length();
    }


    /**
     * Checks on the string if the next char, of the specified index, equals to a char.
     * @param string A not null String.
     * @param idx Index of the CURRENT char.
     * @param c Char to be tested.
     * @return true, if the idx is valid and not the last of the string and the following char equals to char c.
     */
    public boolean nextCharEquals(String string, int idx, char c)
    {
        return !isLastChar(string, idx) &&
                string.charAt(idx+1) == c;
    }


    /**
     * Checks on the string, if the next char equals to any char of another string.
     * @param string A not null String.
     * @param idx Index of the CURRENT char.
     * @param chars A not null String containing all chars to be verified.
     * @return true, if the idx is valid and not the last of the string and the following char is in String chars.
     */
    public boolean nextCharIsIn(String string, int idx, String chars)
    {
        return !isLastChar(string, idx) &&
                chars.indexOf(string.charAt(idx+1)) != -1;
    }


    /**
     * Checks if the char is in [a-z] or [A-Z]. It will return false, if char c contains some diacritic.
     * @param c Char to be tested.
     * @return true, if the character is in [a-z] or [A-Z]; false, otherwise.
     */
    public boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }


    /**
     * Checks if the char is in [a-z] or [A-Z] or is an digit. It will return false, if char c contains some diacritic.
     * @param c Char to be tested.
     * @return true, if the character is in [a-z] or [A-Z]; false, otherwise.
     */
    public boolean isLetterOrDigit(char c) {
        return isLetter(c) || (c >= '0' && c <= '9');
    }
}
