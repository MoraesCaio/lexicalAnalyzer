package lexicalAnalyzer;

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
    private String text;
    private Integer lineNumber;
    private String classification;


    /*VALUES*/
    final static List<String> keywords = Arrays.asList("program", "var", "integer", "real", "boolean", "procedure", "begin", "end", "if", "then", "else", "while", "do", "not");
    final static List<String> booleanValues = Arrays.asList("true", "false");
    final static String additionOperator = "or";
    final static String multiplicationOperator = "and";
    final static String accChars = "_.:;,<>=+-/*(){}\' \t\n";
    final static String specialChars = "_.:;,<>=+-/*(){}";


    /**
     * CONSTRUCTORS
     * @param text Text of the token.
     * @param classification Classification of the token.
     * @param lineNumber Number of the line where the token was found.
     */
    private Token(String text, String classification, Integer lineNumber)
    {
        this.text = text;
        this.classification = classification;
        this.lineNumber = lineNumber+1;
    }

    public Token(String text, Classifications classification, Integer lineNumber)
    {
        this(text, classification.toString(), lineNumber);
    }

    public Token(String text, Classifications classification)
    {
        this(text, classification.toString(), 0);
    }

    public Token(String text)
    {
        this(text, Classifications.UNCLASSIFIED);
    }

    public Token(){
        this("");
    }

    /**
     * A simple toString() for tokens.
     * @return String - A string containing its text, its classification and the line where it was found.
     */
    @Override
    public String toString(){
        return "TOKEN: " + text + '\n' +
        "CLASSIFICATION: " + classification + '\n' +
        "LINE: " + lineNumber;
    }

    public String getText() {return text;}
    public String getClassification() {return classification;}
    public Integer getLineNumber() {return lineNumber;}


    /**
     * All possible classifications for tokens in Pascal
     */
    public enum Classifications{
        KEYWORD("keyword"), //any word in keywords list
        IDENTIFIER("identifier"), //[a..z] ([a..z]|[0..9]|'_')*
        INTEGER("integer"), //[0..9]+
        REAL("real"), //[0..9]*.[0..9]+
        BOOLEAN("boolean"),
        COMPLEX("complex"), //[REAL|INTEGER]i[+|-]?[REAL|INTEGER]
        DELIMITER("delimiter"), //,;:.()
        ASSIGNMENT("assignment"), //:=
        RELATIONAL("relational"), //<,>,=,<>,<=,>=
        ADDITION("addition"), //or + -
        MULTIPLICATION("multiplication"), //and * /
        UNCLASSIFIED("unclassified"); //default value

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
