import java.util.Arrays;
import java.util.List;

/**
 * Created by caiomoraes on 06/08/17.
 */
public class Token {
    public String token;
    public Integer lineNumber;
    public String classification;

    final static public List<String> keywords = Arrays.asList("program", "var", "integer", "real", "boolean", "procedure", "begin", "end", "if", "then", "else", "while", "do", "not");
    final static public String addOP = "or";
    final static public String multOP = "and";

    /*CONSTRUCTORS*/
    public Token(String token, String classification, Integer lineNumber)
    {
        this.token = token;
        this.classification = classification;
        this.lineNumber = lineNumber;
    }

    public Token(String token, Classifications classification, Integer lineNumber)
    {
        this(token, classification.toString(), lineNumber);
    }

    @Override
    public String toString(){
        return "TOKEN: " + token + '\n' +
        "CLASSIFICATION: " + classification + '\n' +
        "LINE: " + lineNumber;
    }

    public enum Classifications{
        KEYWORD("keyword"),
        IDENTIFIER("identifier"),
        INTEGER("integer"),
        REAL("real"),
        DELIMITER("delimiter"),
        ASSIGNMENT("assignment"),
        RELATIONAL("relational"),
        ADDITION("addition"),
        MULTIPLICATION("multiplication");

        private final String text;

        Classifications(String text)
        {
            this.text = text;
        }
        @Override
        public String toString()
        {
            return text;
        }

    }
}
