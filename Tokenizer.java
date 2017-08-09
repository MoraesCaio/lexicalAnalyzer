import java.util.ArrayList;
import java.util.List;
import Utils.StringUtils;

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
    private boolean onString = false;

    /*STRING*/
    private StringBuilder sb;
    private StringUtils su = new StringUtils();


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
            }

            //Strings
            if ((!onString && line.charAt(i) == '\'') || onString)
            {
                onString = true;
                i += parseString(line.substring(i, length));
                if(onString) continue;
            }

            //Words
            if (su.isLetter(line.charAt(i)))
            {
                i += parseWord(line.substring(i, length));
                continue;
            }

            //Numbers
            if (Character.isDigit(line.charAt(i)) || (line.charAt(i) == '.'))
            {
                i += parseNum(line.substring(i, length));
                continue;
            }

            //Extras
            //All specialChars except for ".{}", as they were parsed before
            if (Token.specialChars.replaceAll("[.{}_]","").indexOf(line.charAt(i)) != -1)
            {
                i += parseExtras(line. substring(i));
                continue;
            }

            //Checking not allowed chars
            if(Token.accChars.indexOf(line.charAt(i)) == -1) {
            	System.out.println("Character not allowed (line" + lineNum +"): " + line.charAt(i));
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
        //System.out.println("first char: " + substring.charAt(0));
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
     * Ignores the strings.
     * @param substring Part of the line that is not parsed yet.
     * @return int - index for the for loop in parse() method. It's a gimmick to not lose the track.
     */
    private int parseString(String substring)
    {
        int length = substring.length();
        //System.out.println("first char: " + substring.charAt(0));
        for(int i = 1; i < length; i++)
        {
            if(substring.charAt(i) == '\''){
                onString = false;
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
            if(!(su.isLetterOrDigit(substring.charAt(i)) || substring.charAt(i) == '_'))
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

        return (i == length)? length-1 : (i-1);
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
        boolean startsWithDigit = Character.isDigit(substring.charAt(0)); //startsWithDigit != startsWithADot
        boolean isReal = false; //flag indicating that there's already a dot, prevents 0.0(.0)+ from being parse as real
        boolean isDelimiter = false;

        //Extraction
        if(startsWithDigit)
        {
            for (; i < length; i++)
            {
                if (!Character.isDigit(substring.charAt(i)) && (substring.charAt(i) != '.' || isReal))
                {
                    break;
                }

                if (substring.charAt(i) == '.')
                {
                    //First dot
                    if (su.nextCharIsDigit(i, substring))
                        isReal = true;
                    else
                        break;
                }

                sb.append(substring.charAt(i));
            }
        }
        //startsWithADot
        else
        {
            for (; i < length; i++)
            {
                //Prevents 0.0(.0)+ from being parsed as real
                if (!isReal)
                {
                    if (su.nextCharIsDigit(i, substring))
                    {
                        isReal = true;
                    }
                    else
                    {
                        isDelimiter = true;
                        sb.append(substring.charAt(0));
                        i++;
                        break;
                    }

                    sb.append(substring.charAt(0));
                    i++;
                }

                if(!Character.isDigit(substring.charAt(i)))
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
     * Parse other symbols: .:;,<>=+-/*()
     * @param substring Part of the line that is not parsed yet.
     * @return int - index for the for loop in parse() method. It's a gimmick to not lose the track.
     */
    private int parseExtras(String substring)
    {
        sb = new StringBuilder();
        Token.Classifications tempClassif = Token.Classifications.DELIMITER;
        int i = 0;
        sb.append(substring.charAt(0));
        switch(substring.charAt(0))
        {
            case '<':
                tempClassif = Token.Classifications.RELATIONAL;
                if (su.nextCharIsIn(i, substring, ">=")){
                    sb.append(substring.charAt(++i));
                }
                break;
            case '>':
                tempClassif = Token.Classifications.RELATIONAL;
                if (su.nextCharEquals(i, substring, '=')){
                    sb.append(substring.charAt(++i));
                }
                break;
            case '=':
                tempClassif = Token.Classifications.RELATIONAL;
                break;
            case ':':
                tempClassif = Token.Classifications.DELIMITER;
                if (su.nextCharEquals(i, substring, '=')){
                    sb.append(substring.charAt(++i));
                    tempClassif = Token.Classifications.ASSIGNMENT;
                }
                break;
            case ',': case ';': case '(': case ')':
                tempClassif = Token.Classifications.DELIMITER;
                break;
            case '+': case '-':
                tempClassif = Token.Classifications.ADDITION;
                break;
            case '*': case '/':
                tempClassif = Token.Classifications.MULTIPLICATION;
                break;
        }
        tokens.add(new Token(sb.toString(), tempClassif, lineNum));
        return i;
    }

}