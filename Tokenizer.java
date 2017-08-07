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
    private boolean onString = false;

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
                continue;
            }
            parseLine(line);
            lineNum += 1;
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
                System.out.println("end char: " + line.charAt(i));
            }

        }
    }

    //private int parseIdOrKey(String substring){}
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