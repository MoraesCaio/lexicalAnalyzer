package lexicalAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import Utils.StringUtils;

/**
 * This is a parser of ArrayList<String> to List<Token>. To make good use of it, simply instantiate it, call parse() and
 * access the List<Token> tokens.
 * <p>
 * Created on 06/08/17 by
 * <p>
 * Caio Moraes
 * GitHub: MoraesCaio
 * Email: caiomoraes
 * <p>
 * Janyelson Oliveira
 * GitHub: janyelson
 * Email: janyelsonvictor@gmail.com
 */

public class Tokenizer
{
    /*PROPERTIES*/
    private List<String> lines;
    private ArrayList<Token> tokens;
    private Integer lineNum;

    private boolean DEBUG_MODE;
    private boolean onComment;
    private int lastOpenCommentLine;
    private boolean onString;
    private int lastOpenStringLine;

    /*STRING*/
    private StringBuilder sb;
    private StringUtils su = new StringUtils();


    /**
     * CONSTRUCTOR
     *
     * @param lines Lines read from the source code file.
     * @throws NullPointerException In case, lines is null.
     */
    public Tokenizer(List<String> lines, boolean DEBUG_MODE) throws IllegalArgumentException
    {
        if (lines == null)
        {
            throw new IllegalArgumentException("\"lines\" cannot be null.");
        }

        this.lines = lines;
        this.tokens = new ArrayList<Token>();
        this.lineNum = 0;
        this.DEBUG_MODE = DEBUG_MODE;
    }

    public Tokenizer(List<String> lines)
    {
        this(lines, false);
    }

    public Tokenizer()
    {
        this(new ArrayList<String>());
    }


    /**
     * Getter for tokens.
     *
     * @return ArrayList<Token> The list of tokens parsed.
     */
    public ArrayList<Token> getTokens()
    {
        return tokens;
    }


    /**
     * Main method. Iterates through the lines of source code whilst parsing.
     */
    public void parse()
    {
        try
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

            //Detecting unclosed comments or strings
            if (onComment)
            {
                lexicalError("A comment was not closed.\nLine: " + (lastOpenCommentLine + 1));
            }

            if (onString)
            {
                lexicalError("A string was not closed.\nLine: " + (lastOpenStringLine + 1));
            }
        }
        catch (LexicalException lexExc)
        {
            System.out.println("An error ocurred:\n" + lexExc.getMessage());
            lexExc.printStackTrace();
        }
    }


    /**
     * Parse the line into tokens and classifies them with the classes in Enum Token.Classifications.
     * Blank spaces and comments will be ignored.
     *
     * @param line line that will be parsed.
     */
    private void parseLine(String line) throws LexicalException
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
                if (onComment)
                {
                    continue;
                }
            }

            //Comment line
            if (line.charAt(i) == '/' && su.nextCharEquals(line, i, '/'))
            {
                System.out.println("Line comment on line: " + (lineNum + 1));
                return;
            }

            //Strings
            if ((!onString && line.charAt(i) == '\'') || onString)
            {
                onString = true;
                i += parseString(line.substring(i, length));
                if (onString)
                {
                    continue;
                }
            }

            //Words
            if (su.isLetter(line.charAt(i)))
            {
                i += parseWord(line.substring(i, length));
                continue;
            }

            //Numbers
            if (su.isDigit(line.charAt(i)) || (line.charAt(i) == '.'))
            {
                i += parseNum(line.substring(i, length));
                continue;
            }

            //Extras
            //All specialChars except for ".{}", as they were parsed before
            if (Token.specialChars.replaceAll("[.{}_]", "").indexOf(line.charAt(i)) != -1)
            {
                i += parseExtras(line.substring(i));
                continue;
            }

            //Checking not allowed chars
            if (Token.accChars.indexOf(line.charAt(i)) == -1)
            {
                String errorMsg = "Character not allowed : " + line.charAt(i) + "\nLine: " + (lastOpenCommentLine + 1);
                if (!DEBUG_MODE)
                {
                    throw new LexicalException(errorMsg);
                }

                System.out.println(errorMsg);
            }
        }
    }


    /**
     * Ignores the comments.
     *
     * @param substring Part of the line that is not parsed yet.
     * @return int - index for the for loop in parse() method. It's a gimmick to not lose the track.
     */
    private int parseComment(String substring)
    {
        lastOpenCommentLine = lineNum;
        int length = substring.length();

        for (int i = 0; i < length; i++)
        {
            if (substring.charAt(i) == '}')
            {
                onComment = false;
                return i;
            }
        }

        return length - 1;
    }

    /**
     * Ignores the strings.
     *
     * @param substring Part of the line that is not parsed yet.
     * @return int - index for the for loop in parse() method. It's a gimmick to not lose the track.
     */
    private int parseString(String substring)
    {
        lastOpenStringLine = lineNum;
        int length = substring.length();

        for (int i = 1; i < length; i++)
        {
            if (substring.charAt(i) == '\'')
            {
                onString = false;
                return i;
            }
        }

        return length - 1;
    }


    /**
     * Parses identifiers and keywords. Format: [Aa..Zz] ([0..9] | '_' | [Aa..Zz))*
     *
     * @param substring Part of the line that is not parsed yet.
     * @return int - index for the for loop in parse() method. It's a gimmick to not lose the track.
     */
    private int parseWord(String substring)
    {
        sb = new StringBuilder();
        int i = 0;
        int length = substring.length();

        //Extraction
        for (; i < length; i++)
        {
            if (!(su.isLetterOrDigit(substring.charAt(i)) || substring.charAt(i) == '_'))
            {
                break;
            }
            sb.append(substring.charAt(i));
        }

        //Classification
        if (Token.keywords.contains(sb.toString().toLowerCase()))
        {
            tokens.add(new Token(sb.toString(), Token.Classifications.KEYWORD, lineNum));
        }
        else if (Token.booleanValues.contains(sb.toString().toLowerCase()))
        {
            tokens.add(new Token(sb.toString(), Token.Classifications.BOOLEAN, lineNum));
        }
        else if (sb.toString().toLowerCase().equals(Token.additionOperator))
        {
            tokens.add(new Token(sb.toString(), Token.Classifications.ADDITION, lineNum));
        }
        else if (sb.toString().toLowerCase().equals(Token.multiplicationOperator))
        {
            tokens.add(new Token(sb.toString(), Token.Classifications.MULTIPLICATION, lineNum));
        }
        else
        {
            tokens.add(new Token(sb.toString(), Token.Classifications.IDENTIFIER, lineNum));
        }

        return (i == length) ? length - 1 : (i - 1);
    }


    /**
     * Parse numbers: complex, reals and integers.
     *
     * @param substring Part of the line that is not parsed yet.
     * @return int - index for the for loop in parse() method. It's a gimmick to not lose the track.
     */
    private int parseNum(String substring) throws LexicalException
    {
        //Regex
        //INTEGER
        String regInt = "\\d+";
        Pattern pInt = Pattern.compile(regInt);
        Matcher mInt = pInt.matcher(substring);

        //REAL
        String regReal = regInt + "\\.\\d*";
        Pattern pReal = Pattern.compile(regReal);
        Matcher mReal = pReal.matcher(substring);

        //COMPLEX
        String regComp = "(" + regReal + "|" + regInt + ")i(\\+|-)(" + regReal + "|" + regInt + ")";
        Pattern pComp = Pattern.compile(regComp);
        Matcher mComp = pComp.matcher(substring);

        //Starts with a dot but subsequent characters are not a number. It's treated as a DELIMITER dot
        if (!mComp.lookingAt() && !mReal.lookingAt() && !mInt.lookingAt())
        {
            tokens.add(new Token(substring.charAt(0) + "", Token.Classifications.DELIMITER, lineNum));
            return 0;
        }

        //If matches, captures the matching string into a token and returns its length
        if (mComp.lookingAt())
        {
            tokens.add(new Token(mComp.group(), Token.Classifications.COMPLEX, lineNum));
            return mComp.group().length() - 1;
        }
        else if (mReal.lookingAt())
        {
            tokens.add(new Token(mReal.group(), Token.Classifications.REAL, lineNum));
            return mReal.group().length() - 1;
        }
        else if (mInt.lookingAt())
        {
            tokens.add(new Token(mInt.group(), Token.Classifications.INTEGER, lineNum));
            return mInt.group().length() - 1;
        }

        //In case some unexpected input is read.
        else
        {
            String errorMsg = "Unexpected input: " + substring + "\nline " + (lineNum + 1);
            if (!DEBUG_MODE)
            {
                throw new LexicalException(errorMsg);
            }

            System.out.println(errorMsg);
            return 0;
        }
    }


    /**
     * Parse other symbols: .:;,<>=+-/*()
     *
     * @param substring Part of the line that is not parsed yet.
     * @return int - index for the for loop in parse() method. It's a gimmick to not lose the track.
     */
    private int parseExtras(String substring)
    {
        sb = new StringBuilder();
        int i = 0;
        sb.append(substring.charAt(0));

        //Default classification
        Token.Classifications classification = Token.Classifications.UNCLASSIFIED;

        switch (substring.charAt(0))
        {
            case '<':
                classification = Token.Classifications.RELATIONAL;
                if (su.nextCharIsIn(substring, i, ">="))
                {
                    sb.append(substring.charAt(++i));
                }
                break;
            case '>':
                classification = Token.Classifications.RELATIONAL;
                if (su.nextCharEquals(substring, i, '='))
                {
                    sb.append(substring.charAt(++i));
                }
                break;
            case '=':
                classification = Token.Classifications.RELATIONAL;
                break;
            case ':':
                classification = Token.Classifications.DELIMITER;
                if (su.nextCharEquals(substring, i, '='))
                {
                    sb.append(substring.charAt(++i));
                    classification = Token.Classifications.ASSIGNMENT;
                }
                break;
            case ',': case ';': case '(': case ')':
                classification = Token.Classifications.DELIMITER;
                break;
            case '+': case '-':
                classification = Token.Classifications.ADDITION;
                break;
            case '*': case '/':
                classification = Token.Classifications.MULTIPLICATION;
                break;
        }

        tokens.add(new Token(sb.toString(), classification, lineNum));

        return i;
    }

    /**
     * Throws LexicalException or print errorMsg regarding DEBUG_MODE value.
     *
     * @param errorMsg Message with error's details.
     * @throws LexicalException
     */
    private void lexicalError(String errorMsg) throws LexicalException
    {
        if (!this.DEBUG_MODE)
        {
            throw new LexicalException(errorMsg);
        }

        System.out.println(errorMsg);
    }
}