import java.util.ArrayList;
import java.util.List;

/**
 * This is a parser of List<String> to List<Token>. To make good use of it, simply instantiate it, call parse() and
 * access the List<Token> tokens.
 *
 * Created on 06/08/17 by
 *
 * Caio Moraes
 * GitHub: MoraesCaio
 * Email: caiomoraes
 *
 * Janyelson Oliveira
 * GitHub: janyelson
 * Email: janyelsonvictor@gmail.com
 */

public class Tokenizer
{
    /*PROPERTIES*/
    public List<String> lines;
    public List<Token> tokens;

    private Integer lineNum;
    private boolean onComment = false;


    /*STRINGBUILDER*/
    private StringBuilder sb;


    /**
     * CONSTRUCTOR
     * @param lines Lines read from the source code file.
     * @throws IllegalArgumentException
     */
    public Tokenizer(List<String> lines) throws IllegalArgumentException
    {
        if(lines == null)
        {
            throw new IllegalArgumentException("\"lines\" cannot be null.");
        }

        this.lines = lines;
        this.tokens = new ArrayList<Token>();
        this.lineNum = 0;
    }


    /**
     * Main method. Iterates through the lines of source code whilst parsing.
     */
    public void parse()
    {
        //Parsing
        for (String line : lines)
        {
            if (line.isEmpty())
            {
                lineNum += 1;
                continue;
            }
            parseLine(line);
            lineNum += 1;
        }
        //Printing
        for (Token token : tokens)
        {
            System.out.println(token);
            System.out.println();
        }
    }


    /**
     * Parse the line into tokens and classifies them with the classes in Enum Token.Classifications.
     * Blank spaces and comments will be ignored.
     * @param line line that will be parsed.
     */
    private void parseLine(String line)
    {
        sb = new StringBuilder();

        int length = line.length();
        for (int i = 0; i < length; i++)
        {
            //Comments
            if ((!onComment && line.charAt(i) == '{') || onComment)
            {
                onComment = true;
                i += parseComment(line.substring(i, length));
                if(onComment) continue;
                //System.out.println("end char: " + line.charAt(i));
            }

            //Words
            if (Character.isLetter(line.charAt(i)))
            {
                i += parseWord(line.substring(i, length));
            }

            if (Character.isDigit(line.charAt(i)) || (line.charAt(i) == '.'))
            {
                i += parseNum(line.substring(i, length));
            }
        }
    }


    /**
     * Ignores the comments.
     * @param substring Part of the line that is not parsed yet.
     * @return int - index for the for loop in parse() method. It's a gimmick to not lose the track.
     */
    private int parseComment(String substring)
    {
        int length = substring.length();
        System.out.println("first char: " + substring.charAt(0));
        for(int i = 0; i < length; i++)
        {
            if(substring.charAt(i) == '}'){
                onComment = false;
                return i;
            }
        }
        return length-1;
    }


    /**
     * Parses identifiers and keywords. Format: [Aa..Zz] ([0..9] | '_' | [Aa..Zz))*
     * @param substring Part of the line that is not parsed yet.
     * @return int - index for the for loop in parse() method. It's a gimmick to not lose the track.
     */
    private int parseWord(String substring)
    {
        sb = new StringBuilder();
        int i = 0;
        int length = substring.length();

        //Extraction
        for (;i < length; i++)
        {
            if(!(Character.isLetterOrDigit(substring.charAt(i)) || substring.charAt(i) == '_'))
            {
                break;
            }
            sb.append(substring.charAt(i));
        }

        //Classification
        if(Token.keywords.contains(sb.toString().toLowerCase())) {
            tokens.add(new Token(sb.toString(), Token.Classifications.KEYWORD, lineNum));
        } else if(sb.toString().toLowerCase().equals(Token.addOP)){
            tokens.add(new Token(sb.toString(), Token.Classifications.ADDITION, lineNum));
        } else if(sb.toString().toLowerCase().equals(Token.multOP)){
            tokens.add(new Token(sb.toString(), Token.Classifications.MULTIPLICATION, lineNum));
        } else {
            tokens.add(new Token(sb.toString(), Token.Classifications.IDENTIFIER, lineNum));
        }

        return (i == length)? length-1 : i;
    }


    /**
     * Parse numbers: reals and integers.
     * @param substring Part of the line that is not parsed yet.
     * @return int - index for the for loop in parse() method. It's a gimmick to not lose the track.
     */
    private int parseNum(String substring)
    {
        sb = new StringBuilder();
        int i = 0;
        int length = substring.length();
        boolean isReal = false;
        boolean isDelimiter = false;

        //Extraction
        for (;i < length; i++)
        {
            if(Character.isDigit(substring.charAt(0))) {
                if(!(Character.isDigit(substring.charAt(i)) || (substring.charAt(i) == '.' && !isReal)))
                {
                    break;
                }

                if(substring.charAt(i) == '.') isReal = true;
                sb.append(substring.charAt(i));
            }
            else {
                
                if(!isReal) {
                    if((((i + 1) != length) && Character.isDigit(substring.charAt(i+1)))) isReal = true;
                    else {
                        isDelimiter = true;
                        i++;
                        sb.append(substring.charAt(0));
                        break;
                    }

                    sb.append(substring.charAt(0));
                    i++;
                }

                if(!(Character.isDigit(substring.charAt(i)) || (substring.charAt(i) == '.' && !isReal)))
                {
                    break;
                }

                sb.append(substring.charAt(i));   
            }
        }

        //Classification
        if(isReal) {
            tokens.add(new Token(sb.toString(), Token.Classifications.REAL, lineNum));
        } else if(isDelimiter){
            tokens.add(new Token(sb.toString(), Token.Classifications.DELIMITER, lineNum));
        } else {
            tokens.add(new Token(sb.toString(), Token.Classifications.INTEGER, lineNum));
        }

        return (i == length)? length-1 : (i-1);
    }


    /**
     * Parse other symbols: _.:;,<>=+-/*()
     * @param substring Part of the line that is not parsed yet.
     * @return int - index for the for loop in parse() method. It's a gimmick to not lose the track.
     */
    private int parseExtras(String substring)
    {

        return 0;
    }


    /**
     * Checks if an character it's an allowed character or not.
     * @param c Character being evaluated.
     * @return boolean - true, if it's an accepted character; false, otherwise.
     */
    private boolean isAllowedChar(char c)
    {
        if(Character.isLetterOrDigit(c)) return true;
        if(Token.accChars.indexOf(c) != -1) return true;
        return false;
    }
}