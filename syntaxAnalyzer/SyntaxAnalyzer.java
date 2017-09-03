package syntaxAnalyzer;

import lexicalAnalyzer.Token;

import java.util.ArrayList;

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
        currentToken = tokens.get(count);
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
        if(currentToken.getText().toLowerCase().equals("program"))
        {
            count++;
            currentToken = tokens.get(count);
            if(currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
            {
                count++;
                currentToken = tokens.get(count);
                if(currentToken.getText().toLowerCase().equals(";"))
                {
                    varDeclaration();
                    subProgramsDeclarationA();
                    compoundCommand();

                    count++;
                    currentToken = tokens.get(count);
                    if(currentToken.getText().toLowerCase().equals("."))
                    {
                        //finish
                    }
                    else {
                        syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol '.' was not found!" );
                    }
                }
                else
                {
                    syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ';' was not found!" );
                }
            }
            else
            {
                syntaxError("Error line " + currentToken.getLineNumber() + ": Invalid identifier for program!" );
            }
        }
        else
        {
            syntaxError("Error line " + currentToken.getLineNumber() + ": Keyword 'program' was not found!" );
        }
    }


    private void varDeclaration() throws SyntaxException
    {
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("var"))
        {
            varDeclarationListA();
        } else {
            count--;
        }
    }


    private void varDeclarationListA() throws SyntaxException
    {
        identifiersListA();

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals(":"))
        {

            type();

            count++;
            currentToken = tokens.get(count);
            if(currentToken.getText().toLowerCase().equals(";"))
            {

                varDeclarationListB();
            } else {
                count--;
                syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ';' was not found!" );
            }
        } else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ':' was not found!" );
        }
    }

    private void varDeclarationListB() throws SyntaxException
    {

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            count--;
            identifiersListA();

            count++;
            currentToken = tokens.get(count);
            if (currentToken.getText().toLowerCase().equals(":"))
            {

                type();

                count++;
                currentToken = tokens.get(count);
                if (currentToken.getText().toLowerCase().equals(";"))
                {

                    varDeclarationListB();
                } else {
                    count--;
                    syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ';' was not found!" );
                }
            } else {
                count--;
                syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ':' was not found!" );
            }
        } else {
            count--;
        }
    }

    private void identifiersListA() throws SyntaxException
    {

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {

            identifiersListB();

        } else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Invalid identifier!" );
        }
    }

    private void identifiersListB() throws SyntaxException
    {

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals(","))
        {

            count++;
            currentToken = tokens.get(count);
            if(currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
            {

                identifiersListB();
            } else {
                count--;
                syntaxError("Error line " + currentToken.getLineNumber() + ": Invalid identifier!" );
            }
        } else {
            count--;
        }
    }

    private void type() throws SyntaxException
    {

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.INTEGER.toString()))
        {

        }
        else if(currentToken.getClassification().equals(Token.Classifications.REAL.toString()))
        {

        }
        else if(currentToken.getClassification().equals(Token.Classifications.BOOLEAN.toString()))
        {

        } else {
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

        count++;
        currentToken = tokens.get(count);
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

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("procedure"))
        {
            count++;
            currentToken = tokens.get(count);
            if(currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
            {

                arguments();

                count++;
                currentToken = tokens.get(count);
                if(currentToken.getText().toLowerCase().equals(";")) {

                    varDeclaration();
                    subProgramsDeclarationA();
                    compoundCommand();
                } else {
                    count--;
                    syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ';' was not found!" );
                }
            } else {
                count--;
                syntaxError("Error line " + currentToken.getLineNumber() + ": Invalid identifier!" );
            }
        } else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Keyword 'procedure' was not found!" );
        }

    }

    private void arguments() throws SyntaxException
    {

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("("))
        {

            parameterListA();

            count++;
            currentToken = tokens.get(count);
            if(currentToken.getText().toLowerCase().equals(")"))
            {

            } else {
                count--;
                syntaxError("Error line " + currentToken.getLineNumber() + ": Symbol ')' was not found!" );
            }
        } else {
            count--;
        }
    }

    private void parameterListA() throws SyntaxException
    {
        identifiersListA();

        count++;
        currentToken = tokens.get(count);
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
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals(";"))
        {
            identifiersListA();

            count++;
            currentToken = tokens.get(count);
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

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("begin"))
        {
            opCommand();

            count++;
            currentToken = tokens.get(count);
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
        count++;
        currentToken = tokens.get(count);
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

        count++;
        currentToken = tokens.get(count);
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

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {

            count++;
            currentToken = tokens.get(count);
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
            count++;
            currentToken = tokens.get(count);
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
            count++;
            currentToken = tokens.get(count);
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
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("else"))
        {
            command();
        } else {
            count--;
        }
    }

    private void var() throws SyntaxException
    {
        count++;
        currentToken = tokens.get(count);
        if (currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {

        } else {
            count--;
        }
    }

    private void procedureActivationA() throws SyntaxException
    {
        count++;
        currentToken = tokens.get(count);
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
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("("))
        {
            expressionListA();

            count++;
            currentToken = tokens.get(count);
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
        count++;
        currentToken = tokens.get(count);
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

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.RELATIONAL.toString()))
        {
            simpleExpressionA();
        } else {
            count--;
        }
    }

    private void simpleExpressionA() throws SyntaxException
    {
        count++;
        currentToken = tokens.get(count);
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
        count++;
        currentToken = tokens.get(count);
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
        count++;
        currentToken = tokens.get(count);
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
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            count++;
            currentToken = tokens.get(count);
            if(currentToken.getText().toLowerCase().equals("("))
            {
                expressionListA();

                count++;
                currentToken = tokens.get(count);
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
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("+") || currentToken.getText().toLowerCase().equals("-"))
        {

        } else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Sign was not found!" );
        }
    }

    private void opRelational() throws SyntaxException
    {
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.RELATIONAL.toString()))
        {
        } else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Relational symbol was not found!" );
        }

    }

    private void opAdditive() throws SyntaxException
    {
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.ADDITION.toString()))
        {
        }  else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Addition symbol was not found!" );
        }
    }

    private void opMultiplicative() throws SyntaxException
    {
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.MULTIPLICATION.toString()))
        {
        }  else {
            count--;
            syntaxError("Error line " + currentToken.getLineNumber() + ": Multiplicative symbol was not found!" );
        }
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
