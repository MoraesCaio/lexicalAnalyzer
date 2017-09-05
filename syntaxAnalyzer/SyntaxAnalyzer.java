package syntaxAnalyzer;

import lexicalAnalyzer.Token;

import java.util.ArrayList;

/**
 * Syntax Analyzer for pascal. It reads a ArrayList<Token> looking for syntax errors. This class and package are meant
 * to be used side-by-side with lexicalAnalyzer package. The CFG is explained just before each method. Each method is
 * either a direct representation of an entity contained on the CFG, or the representation of its recursive part.
 * <p>
 * Created on 04/09/17 by
 * <p>
 * Caio Moraes
 * GitHub: MoraesCaio
 * Email: caiomoraes
 * <p>
 * Janyelson Oliveira
 * GitHub: janyelson
 * Email: janyelsonvictor@gmail.com
 */

public class SyntaxAnalyzer
{
    /*PROPERTIES*/
    private ArrayList<Token> tokens;
    private Token currentToken;
    private static int count;
    private boolean DEBUG_MODE;


    /**
     * CONSTRUCTORS
     *
     * @param tokens
     */
    public SyntaxAnalyzer(ArrayList<Token> tokens, boolean DEBUG_MODE)
    {
        this.tokens = tokens;
        this.DEBUG_MODE = DEBUG_MODE;
    }

    public SyntaxAnalyzer(ArrayList<Token> tokens)
    {
        this(tokens, false);
    }

    public SyntaxAnalyzer()
    {
        this(new ArrayList<Token>());
    }


    public void run() throws SyntaxException
    {
        System.out.println("Analyzing syntax...");
        count = 0;
        program();
    }


    /**
     * program programID;
     * VariablesDeclaration()
     * SubProgramsDeclaration()
     * CompoundCommand()
     * .
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void program() throws SyntaxException
    {
        //keyword: 'program'
        currentToken = tokens.get(count); //count == 0
        if (!currentToken.getText().toLowerCase().equals("program"))
        {
            syntaxError("Keyword 'program' was not found!");
        }

        //identifier: name of the program
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            syntaxError("Invalid identifier for program!");
        }

        //identifier: ';'
        currentToken = getNextToken();
        if (!currentToken.getText().equals(";"))
        {
            syntaxError("Symbol ';' was not found!");
        }

        //control flow
        varDeclaration();
        subProgramsDeclaration();
        compoundCommand();

        //delimiter: '.'
        currentToken = getNextToken();
        if (!currentToken.getText().equals("."))
        {
            syntaxError("Symbol '.' was not found!");
        }

        //Ending without errors

    }


    /**
     * Var varDeclarationListA() | e
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void varDeclaration() throws SyntaxException
    {
        currentToken = getNextToken();
        if (currentToken.getText().toLowerCase().equals("var"))
        {
            varDeclarationListA();
        }
        else
        {
            count--;
        }
    }


    /**
     * identifiersListA():type();varDeclarationListB
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void varDeclarationListA() throws SyntaxException
    {
        identifiersListA();

        currentToken = getNextToken();
        if (!currentToken.getText().equals(":"))
        {
            syntaxError("Symbol ':' was not found!");
        }

        type();

        currentToken = getNextToken();
        if (!currentToken.getText().equals(";"))
        {
            syntaxError("Symbol ';' was not found!");
        }

        varDeclarationListB();
    }

    /**
     * (identifiersListA:type;varDeclarationListB)*
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void varDeclarationListB() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            count--;
            return;
        }
        count--;
        identifiersListA();

        currentToken = getNextToken();
        if (!currentToken.getText().equals(":"))
        {
            syntaxError("Symbol ':' was not found!");
        }

        type();

        currentToken = getNextToken();
        if (!currentToken.getText().equals(";"))
        {
            syntaxError("Symbol ';' was not found!");
        }

        varDeclarationListB();
    }


    /**
     * id identifiersListB()
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void identifiersListA() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            syntaxError("Invalid identifier! : ");
        }

        identifiersListB();
    }


    /**
     * (, id)*
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void identifiersListB() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getText().equals(","))
        {
            count--;
            return;
        }

        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            syntaxError("Invalid identifier!");
        }

        identifiersListB();
    }


    /**
     * Accepted types: real, integer and boolean.
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void type() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!Token.types.contains(currentToken.getText().toLowerCase()))
        {
            syntaxError("Invalid type!");
        }

    }


    /**
     * procedure subProgram()
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void subProgramsDeclaration() throws SyntaxException
    {
        currentToken = getNextToken();
        count--;
        if (currentToken.getText().toLowerCase().equals("procedure"))
        {
            subProgram();
            subProgramsDeclaration();
        }
    }


    /**
     * procedure id arguments();
     * variablesDeclaration()
     * subProgramsDeclaration()
     * CompoundCommand()
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void subProgram() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getText().toLowerCase().equals("procedure"))
        {
            count--;
            syntaxError("Keyword 'procedure' was not found!");
        }

        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            count--;
            syntaxError("Invalid identifier!");
        }

        arguments();

        currentToken = getNextToken();
        if (!currentToken.getText().equals(";"))
        {
            count--;
            syntaxError("Symbol ';' was not found!");
        }

        varDeclaration();
        subProgramsDeclaration();
        compoundCommand();
    }


    /**
     * (parameterListA())
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void arguments() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getText().equals("("))
        {
            count--;
        }
        else
        {
            parameterListA();

            currentToken = getNextToken();
            if (!currentToken.getText().equals(")"))
            {
                syntaxError("Symbol ')' was not found!");
            }
        }
    }


    /**
     * identifiersListA():type() parameterListB()
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void parameterListA() throws SyntaxException
    {
        identifiersListA();

        currentToken = getNextToken();
        if (!currentToken.getText().equals(":"))
        {
            syntaxError("Symbol ':' was not found!");
        }

        type();
        parameterListB();
    }


    /**
     * (; identifiersListA():type())*
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void parameterListB() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getText().equals(";"))
        {
            count--;
            return;
        }

        identifiersListA();

        currentToken = getNextToken();
        if (!currentToken.getText().equals(":"))
        {
            count--;
            syntaxError("Symbol ':' was not found!");
        }

        type();

        parameterListB();
    }


    /**
     * begin optionalCommands() end
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void compoundCommand() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getText().toLowerCase().equals("begin"))
        {
            syntaxError("Keyword 'Begin' was not found!");
        }

        optionalCommands();

        currentToken = getNextToken();
        if (!currentToken.getText().toLowerCase().equals("end"))
        {
            syntaxError("Keyword 'End' was not found!");
        }
    }


    /**
     * commandListA()
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void optionalCommands() throws SyntaxException
    {
        currentToken = getNextToken();
        count--;
        if (!currentToken.getText().toLowerCase().equals("end"))
        {
            commandListA();
        }
    }


    /**
     * command() commandListB()
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void commandListA() throws SyntaxException
    {
        command();
        commandListB();
    }

    /**
     * (; command())* | ; End
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void commandListB() throws SyntaxException
    {
        currentToken = getNextToken();
        if (currentToken.getText().equals(";"))
        {
            currentToken = getNextToken();
            if (!currentToken.getText().toLowerCase().equals("end"))
            {
                count--;
                command();
                commandListB();
            }
            else
            {
                count--;
            }
        }
        else
        {
            count--;
        }
    }


    /**
     * id := expression()
     * procedureActivationA()
     * if expression() then command() elsePart()
     * while expression() do command()
     * begin compoundCommand()
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void command() throws SyntaxException
    {
        currentToken = getNextToken();
        if (currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            currentToken = getNextToken();
            if (currentToken.getText().equals(":="))
            {
                expression();
            }
            else
            {
                count -= 2;
                procedureActivationA();
            }
        }
        else if (currentToken.getText().toLowerCase().equals("if"))
        {
            expression();

            currentToken = getNextToken();
            if (!currentToken.getText().toLowerCase().equals("then"))
            {
                count--;
                syntaxError("Keyword 'Then' was not found!");
            }

            command();
            elsePart();
        }
        else if (currentToken.getText().toLowerCase().equals("while"))
        {
            expression();

            currentToken = getNextToken();
            if (!currentToken.getText().toLowerCase().equals("do"))
            {
                count--;
                syntaxError("Keyword 'do' was not found!");
            }

            command();
        }
        else if (currentToken.getText().toLowerCase().equals("begin"))
        {
            //compoundCommand also reads 'begin'
            count--;
            compoundCommand();
        }
        else
        {
            syntaxError("Invalid command.");
        }
    }


    /**
     * else command()
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void elsePart() throws SyntaxException
    {
        currentToken = getNextToken();
        if (currentToken.getText().toLowerCase().equals("else"))
        {
            command();
        }
        else
        {
            count--;
        }
    }

    /**
     * id expressionListA() | void
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void procedureActivationA() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            syntaxError("Invalid identifier!");
        }

        currentToken = getNextToken();
        if (!currentToken.getText().equals("("))
        {
            count--;
            return;
        }

        count--;
        expressionListA();

    }

    /**
     * (expression() expressionListB)
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void expressionListA() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getText().equals("("))
        {
            count--;
        }
        else
        {
            expression();
            expressionListB();

            currentToken = getNextToken();
            if (!currentToken.getText().equals(")"))
            {
                syntaxError("Symbol ')' was not found!");
            }
        }
    }


    /**
     * (, expression())*
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void expressionListB() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getText().equals(","))
        {
            count--;
            return;
        }

        expression();
        expressionListB();
    }


    /**
     * simpleExpresssionA() (relationalOperator simpleExpressionA())?
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void expression() throws SyntaxException
    {
        simpleExpressionA();

        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.RELATIONAL.toString()))
        {
            count--;
        }
        else
        {
            count--;
            simpleExpressionA();
        }
    }


    /**
     * (+|-)? termA() simpleExpressionB()
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void simpleExpressionA() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getText().equals("+") && !currentToken.getText().equals("-"))
        {
            count--;
        }

        termA();
        simpleExpressionB();
    }


    /**
     * ((+|-|or) termA())*
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void simpleExpressionB() throws SyntaxException
    {
        currentToken = getNextToken();
        //stop simpleExpressionB's loop
        if (!currentToken.getClassification().equals(Token.Classifications.ADDITION.toString()))
        {
            count--;
        }
        else
        {
            termA();
            simpleExpressionB();
        }
    }


    /**
     * factor() termB()
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void termA() throws SyntaxException
    {
        factor();
        termB();
    }


    /**
     * ((*|/|and) factor())*
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void termB() throws SyntaxException
    {
        currentToken = getNextToken();
        if (currentToken.getClassification().equals(Token.Classifications.MULTIPLICATION.toString()))
        {
            factor();
            termB();
        }
        else
        {
            count--;
        }
    }


    /**
     * id
     * id expressionListA()
     * (expression())
     * (not factor())*
     * boolean|real|integer
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void factor() throws SyntaxException
    {
        currentToken = getNextToken();
        if (currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            currentToken = getNextToken();
            count--;
            if (currentToken.getText().equals("("))
            {
                expressionListA();
            }
        }
        else if (currentToken.getText().equals("("))
        {
            expression();
            currentToken = getNextToken();
            if (!currentToken.getText().equals(")"))
            {
                count--;
                syntaxError("Symbol ')' was not found!");
            }
        }
        else if (currentToken.getText().toLowerCase().equals("not"))
        {
            factor();
        }
        else if (!Token.types.contains(currentToken.getClassification()))
        {
            count--;
            syntaxError("Expected factor.");
        }
    }


    /**
     * Gets the next token, increasing the token counter and checking if there are still tokens to be read.
     * On DEBUG_MODE, prints on console each token being read.
     *
     * @return Token the next token to be read.
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private Token getNextToken() throws SyntaxException
    {
        count++;
        if (count >= tokens.size())
        {
            count = tokens.size() - 1;
            syntaxError("No more tokens to be read.");
        }
        if (this.DEBUG_MODE)
        {
            System.out.println("Reading token: " + tokens.get(count).getText());
        }
        return tokens.get(count);
    }


    /**
     * Default method for throwing syntaxError exceptions.
     *
     * @param errorMsg Message that explains the error.
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void syntaxError(String errorMsg) throws SyntaxException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Syntax Error! Line " + currentToken.getLineNumber() + ":\n");
        sb.append(currentToken.getText() + "\n");
        sb.append(errorMsg);
        throw new SyntaxException(sb.toString());
    }

}
