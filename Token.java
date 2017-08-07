import java.util.Arrays;
import java.util.List;

/**
 * A token is will always have: its own text, its classification and its line number (in case of some exception).
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

public class Token {

    /*PROPERTIES*/
    public String token;
    public Integer lineNumber;
    public String classification;


    /*VALUES*/
    final static public List<String> keywords = Arrays.asList("program", "var", "integer", "real", "boolean", "procedure", "begin", "end", "if", "then", "else", "while", "do", "not");
    final static public String addOP = "or";
    final static public String multOP = "and";


    /**
     * CONSTRUCTOR
     * @param token Text of the token.
     * @param classification Classification of the token.
     * @param lineNumber Number of the line where the token was found.
     */
    public Token(String token, String classification, Integer lineNumber)
    {
        this.token = token;
        this.classification = classification;
        this.lineNumber = lineNumber+1;
    }

    public Token(String token, Classifications classification, Integer lineNumber)
    {
        this(token, classification.toString(), lineNumber);
    }


    /**
     * A simple toString() for tokens.
     * @return String - A string containing its text, its classification and the line where it was found.
     */
    @Override
    public String toString(){
        return "TOKEN: " + token + '\n' +
        "CLASSIFICATION: " + classification + '\n' +
        "LINE: " + lineNumber;
    }


    /**
     * All possible classifications for tokens in Pascal
     */
    public enum Classifications{
        KEYWORD("keyword"), //any word in keywords list
        IDENTIFIER("identifier"), //[a..z] ([a..z]|[0..9]|'_')*
        INTEGER("integer"), //[0..9]+
        REAL("real"), //[0..9]*.[0..9]+
        DELIMITER("delimiter"), //,;:.()
        ASSIGNMENT("assignment"), //:=
        RELATIONAL("relational"), //<,>,=,<>,<=,>=
        ADDITION("addition"), //or + -
        MULTIPLICATION("multiplication"); //and * /

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