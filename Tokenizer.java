import java.util.ArrayList;
import java.util.List;

/**
 * Created by caiomoraes on 06/08/17.
 */
public class Tokenizer
{
    public List<String> lines;
    public List<Token> tokens;

    private Integer lineNum;
    private StringBuilder sb;
    private boolean onComment = false;
    private int openParenthesis = 0;

    /*CONSTRUCTOR*/
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

    public void parse()
    {
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
        for (Token token : tokens)
        {
            System.out.println(token);
            System.out.println();
        }
    }

    private void parseLine(String line)
    {
        Token tempToken;
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
                System.out.println("end char: " + line.charAt(i));
            }

            //Words
            if (Character.isLetter(line.charAt(i)))
            {
                i += parseWord(line.substring(i, length));
            }

        }
    }

    private int parseWord(String substring)
    {
        int length = substring.length();
        sb = new StringBuilder();
        int i = 0;

        for (;i < length; i++)
        {
            if(!(Character.isLetterOrDigit(substring.charAt(i)) || substring.charAt(i) == '_'))
            {
                break;
            }
            sb.append(substring.charAt(i));
        }

        if(Token.keywords.contains(sb.toString().toLowerCase())) {
            tokens.add(new Token(sb.toString(), Token.Classifications.KEYWORD, lineNum+1));
        } else if(sb.toString().toLowerCase().equals(Token.addOP)){
            tokens.add(new Token(sb.toString(), Token.Classifications.ADDITION, lineNum+1));
        } else if(sb.toString().toLowerCase().equals(Token.multOP)){
            tokens.add(new Token(sb.toString(), Token.Classifications.MULTIPLICATION, lineNum+1));
        } else {
            tokens.add(new Token(sb.toString(), Token.Classifications.IDENTIFIER, lineNum+1));
        }

        return (i == length)? length-1 : i;
    }
    //private int parseNum(String substring){}
    //private int parseExtras(String substring){}
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

}