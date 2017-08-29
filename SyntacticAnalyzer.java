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
        var();

        count++;
        token = tokens.get(count);
        if(token.getString().toLowerCase().equals(":=")) {

        }

        expression();

        //Parei aqui
    }

    private void partElse() {

    }

    private void var() {

    }

    private void procedureActivationA() {

    }

    private void procedureActivationB() {

    }

    private void expression() {

    }

    private void simpleExpressionA() {

    }

    private void simpleExpressionB() {

    }

    private void termA() {

    }

    private void termB() {

    }

    private void factorA() {

    }

    private void factorB() {

    }

    private void sign() {

    }

    private void opRelational() {

    }

    private void opAdditive() {

    }

    private void opMultiplicative() {

    }
}
