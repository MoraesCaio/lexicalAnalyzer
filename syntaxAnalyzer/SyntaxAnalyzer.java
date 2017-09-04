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
            syntaxError("Keyword 'program' was not found!" );
        }

        //identifier: name of the program
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            syntaxError("Invalid identifier for program!" );
        }

        //identifier: ';'
        currentToken = getNextToken();
        if (!currentToken.getText().equals(";"))
        {
            syntaxError("Symbol ';' was not found!" );
        }

        //control flow
        varDeclaration();
        subProgramsDeclaration();
        compoundCommand();

        //delimiter: '.'
        currentToken = getNextToken();
        if (!currentToken.getText().equals("."))
        {
            syntaxError("Symbol '.' was not found!" );
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
        if (!currentToken.getText().equals(":"))
        {
            syntaxError("Symbol ':' was not found!" );
        }

        type();

        currentToken = getNextToken();
        if (!currentToken.getText().equals(";"))
        {
            syntaxError("Symbol ';' was not found!" );
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
            syntaxError("Invalid identifier!" );
        }

        identifiersListB();
    }

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
            syntaxError("Invalid identifier!" );
        }

        identifiersListB();
    }

    private void type() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!Token.types.contains(currentToken.getClassification())) {
            syntaxError("Invalid type!");
        }

    }

    private void subProgramsDeclaration() throws SyntaxException
    {
        currentToken = getNextToken();
        count--;
        if(currentToken.getText().toLowerCase().equals("procedure"))
        {
            subProgram();
            subProgramsDeclaration();
        }
    }

    private void subProgram() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getText().toLowerCase().equals("procedure"))
        {
            count--;
            syntaxError("Keyword 'procedure' was not found!" );
        }

        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            count--;
            syntaxError("Invalid identifier!" );
        }

        arguments();

        currentToken = getNextToken();
        if (!currentToken.getText().equals(";"))
        {
            count--;
            syntaxError("Symbol ';' was not found!" );
        }

        varDeclaration();
        subProgramsDeclaration();
        compoundCommand();
    }

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
                syntaxError("Symbol ')' was not found!" );
            }
        }
    }

    private void parameterListA() throws SyntaxException
    {
        identifiersListA();

        currentToken = getNextToken();
        if (!currentToken.getText().equals(":"))
        {
            syntaxError("Symbol ':' was not found!" );
        }

        type();
        parameterListB();
    }

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
            syntaxError("Symbol ':' was not found!" );
        }

        type();

        parameterListB();
    }

    private void compoundCommand() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getText().toLowerCase().equals("begin"))
        {
            syntaxError("Keyword 'Begin' was not found!" );
        }

        opCommand();

        currentToken = getNextToken();
        if (!currentToken.getText().toLowerCase().equals("end"))
        {
            syntaxError("Keyword 'End' was not found!" );
        }
    }

    private void opCommand() throws SyntaxException
    {
        currentToken = getNextToken();
        count--;
        if(!currentToken.getText().toLowerCase().equals("end"))
        {
            commandListA();
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
        if(currentToken.getText().equals(";"))
        {
            command();
            commandListB();
        }
        else
        {
            count--;
        }
    }

    private void command() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            currentToken = getNextToken();
            if(currentToken.getText().equals(":="))
            {
                expression();
            }
            else
            {
                count -= 2;
                procedureActivationA();
            }
        }
        else if(currentToken.getText().toLowerCase().equals("if"))
        {
            expression();

            currentToken = getNextToken();
            if (!currentToken.getText().toLowerCase().equals("then"))
            {
                count--;
                syntaxError("Keyword 'Then' was not found!" );
            }

            command();
            partElse();
        }
        else if(currentToken.getText().toLowerCase().equals("while"))
        {
            expression();

            currentToken = getNextToken();
            if (!currentToken.getText().toLowerCase().equals("do"))
            {
                count--;
                syntaxError("Keyword 'do' was not found!" );
            }

            command();
        }
        else if(currentToken.getText().toLowerCase().equals("begin"))
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

    private void partElse() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getText().toLowerCase().equals("else"))
        {
            command();
        }
        else
        {
            count--;
        }
    }

    /*NOT USED*/
    /*private void var() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            syntaxError("Expected Identifier.");
        }

        count--;
    }*/

    private void procedureActivationA() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            syntaxError("Invalid identifier!" );
        }

        expressionListA();
    }

    /*expressionListA is always between parenthesis. Therefore, as procedureActivationB only checks for them,
     * we can replace it by moving the parenthesis check to expressionListA.
     * Factor also has expressionListA within its analysis.
     */
    /*private void procedureActivationB() throws SyntaxException
    {
        expressionListA();
    }*/

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
            simpleExpressionA();
        }
    }

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
        }
        else
        {
            count--;
        }
    }

    private void factor() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            expressionListA();
        }
        else if(currentToken.getText().equals("("))
        {
            expression();
            currentToken = getNextToken();
            if(!currentToken.getText().equals(")"))
            {
                syntaxError("Symbol ')' was not found!");
            }
        }
        else if(currentToken.getText().toLowerCase().equals("not"))
        {
            factor();
        }
        else if (!Token.types.contains(currentToken.getClassification()))
        {
            count--;
            syntaxError("Expected factor.");
        }
    }

    /*NOT USED*/
    /*
    private void sign() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getText().equals("+") && !currentToken.getText().equals("-"))
        {
            syntaxError("Sign was not found!" );
        }
    }

    private void opRelational() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.RELATIONAL.toString()))
        {
            syntaxError("Relational symbol was not found!" );
        }

    }

    private void opAddition() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.ADDITION.toString()))
        {
            syntaxError("Addition symbol was not found!" );
        }
    }

    private void opMultiplication() throws SyntaxException
    {
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.MULTIPLICATION.toString()))
        {
            syntaxError("Multiplicative symbol was not found!" );
        }
    }
    */

    private Token getNextToken() throws SyntaxException
    {
        count++;
        if(count >= tokens.size()){
            syntaxError("No more tokens to be read.");
        }
        return tokens.get(count);
    }

    private void syntaxError(String errorMsg) throws SyntaxException
    {
        if (!this.DEBUG_MODE)
        {
            throw new SyntaxException(errorMsg);
        }
        System.out.println("Syntax Error! Line " + currentToken.getLineNumber() + ":\n" + errorMsg);
    }

}
