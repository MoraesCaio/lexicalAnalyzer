package syntaxAnalyzer;

import lexicalAnalyzer.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SyntaxAnalyzer
{
    /*PROPERTIES*/
    private ArrayList<Token> tokens;
    private Token currentToken;
    private int count = 0;
    private boolean DEBUG_MODE;


    /**
     * CONSTRUCTORS
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


    public void run()
    {
        try
        {
            program();
        }
        catch (SyntaxException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    private void program() throws SyntaxException
    {
        //keyword: 'program'
        currentToken = tokens.get(count); //count == 0
        if (!currentToken.getText().toLowerCase().equals("program"))
        {
            syntaxError("Error line " + currentToken.getLineNumber() + ": Keyword 'program' was not found!" );
        }

        //identifier: name of the program
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            syntaxError("Error line " + currentToken.getLineNumber() + ": Invalid identifier for program!" );
        }

        //identifier: ';'
        currentToken = getNextToken();
        if (!currentToken.getText().toLowerCase().equals(";"))
        {
            syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ';' was not found!" );
        }

        //control flow
        varDeclaration();
        subProgramsDeclarationA();
        compoundCommand();

        //delimiter: '.'
        currentToken = getNextToken();
        if (!currentToken.getText().toLowerCase().equals("."))
        {
            syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol '.' was not found!" );
        }

        //Ending without errors

    }


    private void varDeclaration() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getText().toLowerCase().equals("var"))
        {
            varDeclarationListA();
        }
        else
        {
            count--;
        }
    }


    private void varDeclarationListA() throws SyntaxException
    {
        identifiersListA();

        currentToken = getNextToken();
        if (!currentToken.getText().toLowerCase().equals(":"))
        {
            syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ':' was not found!" );
        }

        type();

        currentToken = getNextToken();
        if (!currentToken.getText().toLowerCase().equals(";"))
        {
            syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ';' was not found!" );
        }

        currentToken = getNextToken();
        if (currentToken.getText().toLowerCase().equals("procedure"))
        {
            count--;
            return;
        }

        varDeclarationListA();
    }

    private void identifiersListA() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Invalid identifier!" );
        }

        identifiersListB();
    }

    private void identifiersListB() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getText().toLowerCase().equals(","))
        {
            count--;
            return;
        }

        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Invalid identifier!" );
        }

        identifiersListB();
    }

    private void type() throws SyntaxException
    {
        currentToken = getNextToken();

        List<String> types = Arrays.asList(
                Token.Classifications.INTEGER.toString(),
                Token.Classifications.REAL.toString(),
                Token.Classifications.BOOLEAN.toString()
        );

        if (!types.contains(currentToken.getClassification())) {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Invalid type!");
        }

    }

    private void subProgramsDeclarationA() throws SyntaxException
    {
        subProgramsDeclarationB();
    }

    private void subProgramsDeclarationB() throws SyntaxException
    {

        currentToken = getNextToken();
        if(currentToken.getText().toLowerCase().equals("procedure"))
        {
            count--;
            subProgram();
            subProgramsDeclarationB();
        } else {
            count--;
        }
    }

    private void subProgram() throws SyntaxException
    {

        currentToken = getNextToken();
        if (!currentToken.getText().toLowerCase().equals("procedure"))
        {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Keyword 'procedure' was not found!" );
        }

        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Invalid identifier!" );
        }

        arguments();

        currentToken = getNextToken();
        if (!currentToken.getText().toLowerCase().equals(";"))
        {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ';' was not found!" );
        }

        varDeclaration();
        subProgramsDeclarationA();
        compoundCommand();
    }

    private void arguments() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getText().toLowerCase().equals("("))
        {
            count--;
        }
        else
        {
            parameterListA();

            currentToken = getNextToken();
            if (!currentToken.getText().toLowerCase().equals(")"))
            {
                count--;
                syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ')' was not found!" );
            }
        }
    }

    private void parameterListA() throws SyntaxException
    {
        identifiersListA();

        currentToken = getNextToken();
        if(currentToken.getText().toLowerCase().equals(":"))
        {
            type();
            parameterListB();
        } else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ':' was not found!" );
        }
    }

    private void parameterListB() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getText().toLowerCase().equals(";"))
        {
            identifiersListA();

            currentToken = getNextToken();
            if(currentToken.getText().toLowerCase().equals(":"))
            {
                type();
                parameterListB();
            } else {
                count--;
                syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ':' was not found!" );
            }
        } else {
            count--;
        }
    }

    private void compoundCommand() throws SyntaxException
    {

        currentToken = getNextToken();
        if(currentToken.getText().toLowerCase().equals("begin"))
        {
            opCommand();

            currentToken = getNextToken();
            if(currentToken.getText().toLowerCase().equals("end"))
            {
            } else {
                count--;
                syntaxError("Error line " + currentToken.getLineNumber() + ": Keyword 'End was not found!" );
            }
        } else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Keyword 'Begin' was not found!" );
        }
    }

    private void opCommand() throws SyntaxException
    {
        currentToken = getNextToken();
        if(!currentToken.getText().toLowerCase().equals("end")) {

            count--;
            commandListA();
        } else {
            count--;
        }
    }

    private void commandListA() throws SyntaxException
    {
        command();
        commandListB();
    }

    private void commandListB() throws SyntaxException
    {

        currentToken = getNextToken();
        if(currentToken.getText().toLowerCase().equals(";"))
        {
            command();
            commandListB();
        } else {
            count--;
        }
    }

    private void command() throws SyntaxException
    {

        currentToken = getNextToken();
        if(currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {

            currentToken = getNextToken();
            if(currentToken.getText().toLowerCase().equals(":="))
            {
                expression();
            }
            else
            {
                count = count - 2;
                procedureActivationA();
            }
        }
        else if(currentToken.getText().toLowerCase().equals("if"))
        {
            expression();
            currentToken = getNextToken();
            if(currentToken.getText().toLowerCase().equals("then"))
            {
                command();
                partElse();
            } else {
                count--;
                syntaxError("Error line " + currentToken.getLineNumber() + ": Keyword 'Then' was not found!" );
            }

        }
        else if(currentToken.getText().toLowerCase().equals("while"))
        {
            expression();
            currentToken = getNextToken();
            if(currentToken.getText().toLowerCase().equals("do"))
            {
                command();
            } else {
                count--;
                syntaxError("Error line " + currentToken.getLineNumber() + ": Keyword 'do' was not found!" );
            }

        }
        else
        {
            count--;
            compoundCommand();
        }
    }

    private void partElse() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getText().toLowerCase().equals("else"))
        {
            command();
        } else {
            count--;
        }
    }

    private void var() throws SyntaxException
    {
        currentToken = getNextToken();
        if (currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {

        } else {
            count--;
        }
    }

    private void procedureActivationA() throws SyntaxException
    {
        currentToken = getNextToken();
        if (currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            procedureActivationB();
        } else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Invalid identifier!" );
        }
    }

    private void procedureActivationB() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getText().toLowerCase().equals("("))
        {
            expressionListA();

            currentToken = getNextToken();
            if(currentToken.getText().toLowerCase().equals(")")) {

            } else {
                count--;
                syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ')' was not found!" );
            }
        } else {
            count--;
        }
    }

    private void expressionListA() throws SyntaxException
    {
        expression();
        expressionListB();
    }

    private void expressionListB() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getText().toLowerCase().equals(","))
        {
            expression();
            expressionListB();
        } else {
            count--;
        }
    }

    private void expression() throws SyntaxException
    {
        simpleExpressionA();

        currentToken = getNextToken();
        if(currentToken.getClassification().equals(Token.Classifications.RELATIONAL.toString()))
        {
            simpleExpressionA();
        } else {
            count--;
        }
    }

    private void simpleExpressionA() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getText().toLowerCase().equals("-") || currentToken.getText().toLowerCase().equals("+"))
        {
            termA();
            simpleExpressionB();
        }
        else
        {
            count--;
            termA();
            simpleExpressionB();
        }
    }

    private void simpleExpressionB() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getClassification().equals(Token.Classifications.ADDITION.toString()))
        {
            termA();
            simpleExpressionB();
        } else {
            count--;
        }
    }

    private void termA() throws SyntaxException
    {
        factor();
        termB();
    }

    private void termB() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getClassification().equals(Token.Classifications.MULTIPLICATION.toString()))
        {
            factor();
            termB();
        } else {
            count--;
        }
    }

    private void factor() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            currentToken = getNextToken();
            if(currentToken.getText().toLowerCase().equals("("))
            {
                expressionListA();

                currentToken = getNextToken();
                if(currentToken.getText().toLowerCase().equals(")"))
                {

                }
                else {
                    count--;
                    syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ')' was not found!" );
                }
            } else {
                count--;
            }
        }
        else if(currentToken.getClassification().equals(Token.Classifications.INTEGER.toString()))
        {

        }
        else if(currentToken.getClassification().equals(Token.Classifications.REAL.toString()))
        {

        }
        else if(currentToken.getClassification().equals(Token.Classifications.INTEGER.toString()))
        {

        }
        else if(currentToken.getClassification().equals(Token.Classifications.BOOLEAN.toString()))
        {

        }
        else if(currentToken.getText().toLowerCase().equals("not"))
        {
            factor();
        } else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": wrong factor" );
        }
    }

    private void sign() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getText().toLowerCase().equals("+") || currentToken.getText().toLowerCase().equals("-"))
        {

        } else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Sign was not found!" );
        }
    }

    private void opRelational() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getClassification().equals(Token.Classifications.RELATIONAL.toString()))
        {
        } else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Relational symbol was not found!" );
        }

    }

    private void opAdditive() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getClassification().equals(Token.Classifications.ADDITION.toString()))
        {
        }  else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Addition symbol was not found!" );
        }
    }

    private void opMultiplicative() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getClassification().equals(Token.Classifications.MULTIPLICATION.toString()))
        {
        }  else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Multiplicative symbol was not found!" );
        }
    }

    private Token getNextToken() throws SyntaxException
    {
        count++;
        if(count >= tokens.size()){
            syntaxError("Error line " + currentToken.getLineNumber() + ": No more tokens to be read.");
        }
        return tokens.get(count);
    }

    private void syntaxError(String errorMsg) throws SyntaxException
    {
        if (!this.DEBUG_MODE)
        {
            throw new SyntaxException(errorMsg);
        }
        System.out.println(errorMsg);
    }

}
