import lexicalAnalyzer.Token;
import java.util.List;

public class SyntacticAnalyzer {

    private List<Token> tokens;
    private Token token;
    private int count = 0;

    public SyntacticAnalyzer(List<Token> tokens) {
        this.tokens = tokens;
    }

    public SyntacticAnalyzer() {
        this(null);
    }

    public void run() {

        token = tokens.get(count);
        program();
    }

    private void program() {

        if(token.getString().toLowerCase().equals("program")) {
            count++;
            token = tokens.get(count);
            if(token.getClassification().equals(Token.Classifications.IDENTIFIER.toString())) {
                count++;
                token = tokens.get(count);
                if(token.getString().toLowerCase().equals(";")) {

                    varDeclaration();
                    subProgramsDeclarationA();
                    compoundCommand();

                    count++;
                    token = tokens.get(count);
                    if(token.getString().toLowerCase().equals(".")) {
                        //finish
                    }
                }
                else {
                    //Error
                }
            }
            else {
                //Error
            }
        }
        else {
            //Error
        }
    }

    private void varDeclaration() {

        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals("var")) {
            varDeclarationListA();
        } else {
            //Error
        }
    }

    private void varDeclarationListA() {
        indetifiersListA();

        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals(":")) {

            type();

            count++;
            token = tokens.get(count);
            if(token.getString().toLowerCase().equals(";")) {

                varDeclarationListB();
            }
        }
    }

    private void varDeclarationListB() {

        count++;
        token = tokens.get(count);
        if(token.getClassification().equals(Token.Classifications.IDENTIFIER.toString())) {
            indetifiersListA();

            count++;
            token = tokens.get(count);
            if (token.getString().toLowerCase().equals(":")) {

                type();

                count++;
                token = tokens.get(count);
                if (token.getString().toLowerCase().equals(";")) {

                    varDeclarationListB();
                }
            }
        }
    }

    private void indetifiersListA() {

        count++;
        token = tokens.get(count);
        if(token.getClassification().equals(Token.Classifications.IDENTIFIER.toString())) {

            indetifiersListB();

        }
    }

    private void indetifiersListB() {

        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals(",")) {

            count++;
            token = tokens.get(count);
            if(token.getClassification().equals(Token.Classifications.IDENTIFIER.toString())) {

                indetifiersListB();
            }
        }
    }

    private void type() {

        count++;
        token = tokens.get(count);
        if(token.getClassification().equals(Token.Classifications.INTEGER.toString())) {

        } else if(token.getClassification().equals(Token.Classifications.REAL.toString())) {

        } else if(token.getString().toLowerCase().equals("false")) {

        } else if(token.getString().toLowerCase().equals("true")) {

        }
    }

    private void subProgramsDeclarationA() {
        subProgramsDeclarationB();
    }

    private void subProgramsDeclarationB() {

        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals("procedure")) {

            subProgram();
            subProgramsDeclarationB();
        }
    }

    private void subProgram() {

        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals("procedure")){

            count++;
            token = tokens.get(count);
            if(token.getClassification().equals(Token.Classifications.IDENTIFIER.toString())){

                arguments();
                varDeclaration();
                subProgramsDeclarationA();
                compoundCommand();
            }
        }

    }

    private void arguments() {

        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals("(")) {

            parameterListA();

            count++;
            token = tokens.get(count);
            if(token.getString().toLowerCase().equals(")")) {

            }
        }
    }

    private void parameterListA() {
        indetifiersListA();

        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals(":")) {
            type();
            parameterListB();
        }
    }

    private void parameterListB() {
        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals(";")) {
            indetifiersListA();

            count++;
            token = tokens.get(count);
            if(token.getString().toLowerCase().equals(":")) {
                type();
                parameterListB();
            }
        }
    }

    private void compoundCommand() {

        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals("begin")) {
        }

        opCommand();

        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals("end")) {
        }
    }

    private void opCommand() {
        commandListA();
    }

    private void commandListA() {
        command();
        commandListB();
    }

    private void commandListB() {

        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals(";")) {
            command();
            commandListB();
        }
    }

    private void command() {

        count++;
        token = tokens.get(count);
        if(token.getClassification().equals(Token.Classifications.IDENTIFIER.toString())) {

            count++;
            token = tokens.get(count);
            if(token.getString().toLowerCase().equals(":=")) {
                var();
            } else {
                procedureActivationA();
            }
        } else if(token.getString().toLowerCase().equals("if")) {
            expression();
            count++;
            token = tokens.get(count);
            if(token.getString().toLowerCase().equals("if")) {
                command();
                partElse();
            }

        } else if(token.getString().toLowerCase().equals("while")) {
            expression();
            count++;
            token = tokens.get(count);
            if(token.getString().toLowerCase().equals("do")) {
                command();
            }

        } else {
            compoundCommand();
        }
    }

    private void partElse() {
        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals("else")) {
            command();
        }
    }

    private void var() {
        count++;
        token = tokens.get(count);
        if (token.getClassification().equals(Token.Classifications.IDENTIFIER.toString())) {

        }
    }

    private void procedureActivationA() {
        count++;
        token = tokens.get(count);
        if (token.getClassification().equals(Token.Classifications.IDENTIFIER.toString())) {
            procedureActivationB();
        }
    }

    private void procedureActivationB() {
        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals("(")) {
            expressionListA();
        }
    }

    private void expressionListA() {
        expression();
        expressionListB();
    }

    private void expressionListB() {
        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals(",")) {
            expression();
            expressionListB();
        }
    }

    private void expression() {
        simpleExpressionA();

        count++;
        token = tokens.get(count);
        if(token.getClassification().equals(Token.Classifications.RELATIONAL.toString())) {
            simpleExpressionA();
        }
    }

    private void simpleExpressionA() {
        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals("-") || token.getString().toLowerCase().equals("+")) {
            sign();
            termA();
            simpleExpressionB();
        } else {
            termA();
            simpleExpressionB();
        }
    }

    private void simpleExpressionB() {
        count++;
        token = tokens.get(count);
        if(token.getClassification().equals(Token.Classifications.ADDITION.toString())) {
            opAdditive();
            termA();
            simpleExpressionB();
        }
    }

    private void termA() {
        factor();
        termB();
    }

    private void termB() {
        count++;
        token = tokens.get(count);
        if(token.getClassification().equals(Token.Classifications.MULTIPLICATION.toString())) {
            opMultiplicative();
            factor();
            termB();
        }
    }

    private void factor() {
        count++;
        token = tokens.get(count);
        if(token.getClassification().equals(Token.Classifications.IDENTIFIER.toString())) {
            count++;
            token = tokens.get(count);
            if(token.getString().toLowerCase().equals("(")) {
                expressionListA();
                if(token.getString().toLowerCase().equals(")")) {

                }
            }
        } else if(token.getClassification().equals(Token.Classifications.INTEGER.toString())) {

        } else if(token.getClassification().equals(Token.Classifications.REAL.toString())) {

        } else if(token.getClassification().equals(Token.Classifications.INTEGER.toString())) {

        } else if(token.getString().toLowerCase().equals("true")) {

        } else if(token.getString().toLowerCase().equals("false")) {

        } else if(token.getString().toLowerCase().equals("not")) {
            factor();
        }
    }

    private void sign() {
        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals("+") || token.getString().toLowerCase().equals("-")) {

        }
    }

    private void opRelational() {
        count++;
        token = tokens.get(count);
        if(token.getClassification().equals(Token.Classifications.RELATIONAL.toString())) {
        }
    }

    private void opAdditive() {
        count++;
        token = tokens.get(count);
        if(token.getClassification().equals(Token.Classifications.ADDITION.toString())) {
        }
    }

    private void opMultiplicative() {
        count++;
        token = tokens.get(count);
        if(token.getClassification().equals(Token.Classifications.MULTIPLICATION.toString())) {
        }
    }
}
