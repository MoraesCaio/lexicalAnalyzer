package syntaxAnalyzer;

import lexicalAnalyzer.Token;
import java.util.List;

public class SyntaxAnalyzer
{

    private List<Token> tokens;
    private Token currentToken;
    private int count = 0;

    public SyntaxAnalyzer(List<Token> tokens)
    {
        this.tokens = tokens;
    }

    public SyntaxAnalyzer()
    {
        this(null);
    }

    public void run()
    {

        currentToken = tokens.get(count);
        program();
    }

    private void program()
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
                }

                else
                {
                    //Error
                }
            }

            else
            {
                //Error
            }
        }

        else
        {
            //Error
        }
    }

    private void varDeclaration()
    {

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("var"))
        {
            varDeclarationListA();
        }
        else
        {
            //Error
        }
    }

    private void varDeclarationListA()
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
            }
        }
    }

    private void varDeclarationListB()
    {

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
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
                }
            }
        }
    }

    private void identifiersListA()
    {

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {

            identifiersListB();

        }
    }

    private void identifiersListB()
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
            }
        }
    }

    private void type()
    {

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.INTEGER.toString()))
        {

        }
        else if(currentToken.getClassification().equals(Token.Classifications.REAL.toString()))
        {

        }
        else if(currentToken.getText().toLowerCase().equals("false"))
        {

        }
        else if(currentToken.getText().toLowerCase().equals("true"))
        {

        }
    }

    private void subProgramsDeclarationA()
    {
        subProgramsDeclarationB();
    }

    private void subProgramsDeclarationB()
    {

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("procedure"))
        {

            subProgram();
            subProgramsDeclarationB();
        }
    }

    private void subProgram()
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
                varDeclaration();
                subProgramsDeclarationA();
                compoundCommand();
            }
        }

    }

    private void arguments()
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

            }
        }
    }

    private void parameterListA()
    {
        identifiersListA();

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals(":"))
        {
            type();
            parameterListB();
        }
    }

    private void parameterListB()
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
            }
        }
    }

    private void compoundCommand()
    {

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("begin"))
        {
        }

        opCommand();

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("end"))
        {
        }
    }

    private void opCommand()
    {
        commandListA();
    }

    private void commandListA()
    {
        command();
        commandListB();
    }

    private void commandListB()
    {

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals(";"))
        {
            command();
            commandListB();
        }
    }

    private void command()
    {

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {

            count++;
            currentToken = tokens.get(count);
            if(currentToken.getText().toLowerCase().equals(":="))
            {
                var();
            }
            else
            {
                procedureActivationA();
            }
        }
        else if(currentToken.getText().toLowerCase().equals("if"))
        {
            expression();
            count++;
            currentToken = tokens.get(count);
            if(currentToken.getText().toLowerCase().equals("if"))
            {
                command();
                partElse();
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
            }

        }
        else
        {
            compoundCommand();
        }
    }

    private void partElse()
    {
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("else"))
        {
            command();
        }
    }

    private void var()
    {
        count++;
        currentToken = tokens.get(count);
        if (currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {

        }
    }

    private void procedureActivationA()
    {
        count++;
        currentToken = tokens.get(count);
        if (currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            procedureActivationB();
        }
    }

    private void procedureActivationB()
    {
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("("))
        {
            expressionListA();
        }
    }

    private void expressionListA()
    {
        expression();
        expressionListB();
    }

    private void expressionListB()
    {
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals(","))
        {
            expression();
            expressionListB();
        }
    }

    private void expression()
    {
        simpleExpressionA();

        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.RELATIONAL.toString()))
        {
            simpleExpressionA();
        }
    }

    private void simpleExpressionA()
    {
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("-") || currentToken.getText().toLowerCase().equals("+"))
        {
            sign();
            termA();
            simpleExpressionB();
        }
        else
        {
            termA();
            simpleExpressionB();
        }
    }

    private void simpleExpressionB()
    {
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.ADDITION.toString()))
        {
            opAdditive();
            termA();
            simpleExpressionB();
        }
    }

    private void termA()
    {
        factor();
        termB();
    }

    private void termB()
    {
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.MULTIPLICATION.toString()))
        {
            opMultiplicative();
            factor();
            termB();
        }
    }

    private void factor()
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
                if(currentToken.getText().toLowerCase().equals(")"))
                {

                }
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
        else if(currentToken.getText().toLowerCase().equals("true"))
        {

        }
        else if(currentToken.getText().toLowerCase().equals("false"))
        {

        }
        else if(currentToken.getText().toLowerCase().equals("not"))
        {
            factor();
        }
    }

    private void sign()
    {
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getText().toLowerCase().equals("+") || currentToken.getText().toLowerCase().equals("-"))
        {

        }
    }

    private void opRelational()
    {
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.RELATIONAL.toString()))
        {
        }
    }

    private void opAdditive()
    {
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.ADDITION.toString()))
        {
        }
    }

    private void opMultiplicative()
    {
        count++;
        currentToken = tokens.get(count);
        if(currentToken.getClassification().equals(Token.Classifications.MULTIPLICATION.toString()))
        {
        }
    }
}
