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
        int err = 0;
        if(token.getString().toLowerCase().equals("program")) {
            count++;
            token = tokens.get(count);
            if(token.getClassification().equals(Token.Classifications.IDENTIFIER)) {
                count++;
                token = tokens.get(count);
                if(token.getString().toLowerCase().equals(";")) {
                    count++;
                    token = tokens.get(count);

                    varDeclaration();
                    supProgramDeclaration();
                    compoundCommand();

                    count++;
                    token = tokens.get(count);

                    if(token.getString().toLowerCase().equals(".")) {
                        //finish
                    }
                }
                else {}
            }
            else {}
        }
        else { }
    }

    private void varDeclaration() {

    }

    private void supProgramDeclaration() {

    }

    private void compoundCommand() {

    }
}
