package syntaxAnalyzer;

import lexicalAnalyzer.Token;
import semanticAnalyzer.*;
import semanticAnalyzer.SymbolsTable;

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

    /*SEMANTIC ANALYZER*/
    private SymbolsTable symbolsTable;
    private TypeControl typeControl;

    /**
     * CONSTRUCTORS
     *
     * @param tokens
     */
    public SyntaxAnalyzer(ArrayList<Token> tokens, boolean DEBUG_MODE)
    {
        this.tokens = tokens;
        this.DEBUG_MODE = DEBUG_MODE;
        this.symbolsTable = new SymbolsTable();
        this.typeControl = new TypeControl();
    }

    public SyntaxAnalyzer(ArrayList<Token> tokens)
    {
        this(tokens, false);
    }

    public SyntaxAnalyzer()
    {
        this(new ArrayList<Token>());
    }


    /*PROGRAM FLOW'S PART, INCLUDES:
    * program -> varDeclaration -> subProgramsDeclaration -> compoundCommand
    * */


    public void run() throws SyntaxException, SemanticException {
        System.out.println("Analyzing syntax...");
        count = 0;
        program();
        System.out.println("Syntax is correct.");
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
    private void program() throws SyntaxException, SemanticException
    {
        //keyword: 'program'
        currentToken = tokens.get(count); //count == 0
        if (!currentToken.getText().toLowerCase().equals("program"))
        {
            syntaxError("Keyword 'program' was not found!");
        }

        //enter into global scope
        symbolsTable.enterScope();

        //identifier: name of the program
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            syntaxError("Invalid identifier for program!");
        }

        symbolsTable.addSymbol(new Symbol(currentToken.getText(), "program"));

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

        //exit scope
        symbolsTable.exitScope();

        //delimiter: '.'
        currentToken = getNextToken();
        if (!currentToken.getText().equals("."))
        {
            syntaxError("Symbol '.' was not found!");
        }
    }


    /*VARIABLES PART, INCLUDES:
    * varDeclaration ->?
    *   varDeclarationListA ->  identifiersListA ->?
    *   varDeclarationListB ->  identifiersListA*
    *
    * identifiersListA -> identifiersListB* -> type
    * */


    /**
     * Var varDeclarationListA() | e
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void varDeclaration() throws SyntaxException, SemanticException {
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
    private void varDeclarationListA() throws SyntaxException, SemanticException {
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
    private void varDeclarationListB() throws SyntaxException, SemanticException {
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
    private void identifiersListA() throws SyntaxException, SemanticException {
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            syntaxError("Invalid identifier!");
        }

        if(symbolsTable.getProgramName().toLowerCase().equals(currentToken.getText().toLowerCase())) semanticError("Identifier has the same program name");
        //Checks whether the current identifier is declared here or elsewhere in the same scope
        if(symbolsTable.hasDuplicateDeclaration(currentToken.getText()))
        {
            semanticError("Duplicate identifier!");
        }


        //If not, put the identifier into stack
        symbolsTable.addSymbol(new Symbol(currentToken.getText()));

        identifiersListB();
    }


    /**
     * (, id)*
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void identifiersListB() throws SyntaxException, SemanticException {
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

        if(symbolsTable.getProgramName().toLowerCase().equals(currentToken.getText().toLowerCase())) semanticError("Identifier has the same program name");
        //Checks whether the current identifier is declared elsewhere in the same scope
        if(symbolsTable.hasDuplicateDeclaration(currentToken.getText()))
        {
            semanticError("Duplicate identifier!");
        }


        //If not, put the identifier into stack
        symbolsTable.addSymbol(new Symbol(currentToken.getText()));

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

        symbolsTable.assignType(currentToken.getText());

    }


    /*SUBPROGRAMS PART, INCLUDES:
    * subProgramsDeclaration ->
    *  (subProgram -> arguments -> varDeclaration -> subProgramsDeclaration -> compoundCommand)*
    *
    * arguments -> parametersListA -> identifiersListA -> identifiersListB*
    * */


    /**
     * procedure subProgram()
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void subProgramsDeclaration() throws SyntaxException, SemanticException {
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
    private void subProgram() throws SyntaxException, SemanticException {
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

        if(symbolsTable.getProgramName().toLowerCase().equals(currentToken.getText().toLowerCase())) semanticError("Identifier has the same program name");
        //Checks whether the current identifier is declared elsewhere in the same scope
        if(symbolsTable.hasDuplicateDeclaration(currentToken.getText()))
        {
            semanticError("Duplicate identifier!");
        }


        //If not, put the identifier into stack
        symbolsTable.addSymbol(new ProcedureSymbol(currentToken.getText()));

        //enter into local scope
        symbolsTable.enterScope();

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

        //exit scope
        symbolsTable.exitScope();

        currentToken = getNextToken();
        if (!currentToken.getText().equals(";"))
        {
            count--;
            syntaxError("Symbol ';' was not found!");
        }
    }


    /**
     * (parameterListA())
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void arguments() throws SyntaxException, SemanticException {
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
    private void parameterListA() throws SyntaxException, SemanticException {
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
    private void parameterListB() throws SyntaxException, SemanticException {
        currentToken = getNextToken();
        if (!currentToken.getText().equals(";"))
        {
            count--;
            symbolsTable.assignParameters();
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


    /*COMMANDS PART, INCLUDES:
    * commandStructure -> (command|compoundCommand)
    *
    * compoundCommand -> optionalCommands ->?
    *     commandListA -> command -> (commandListB -> command)*
    *
    * command -> ((if else)|(while do)|(do while)|(assignment)|(procedure)|(compound command))
    * */


    /**
     * begin optionalCommands() end
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void compoundCommand() throws SyntaxException, SemanticException {
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
    private void optionalCommands() throws SyntaxException, SemanticException {
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
    private void commandListA() throws SyntaxException, SemanticException {
        command();
        commandListB();
    }


    /**
     * (; command())* | ; End
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void commandListB() throws SyntaxException, SemanticException {
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
    private void command() throws SyntaxException, SemanticException {
        currentToken = getNextToken();
        if (currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            if(symbolsTable.getProgramName().toLowerCase().equals(currentToken.getText().toLowerCase().toLowerCase())) semanticError("Program name cannot be used");
            //Checks whether the current identifier is declared elsewhere
            if(!symbolsTable.hasIdentifier(currentToken.getText()))
            {
                semanticError("Using not declared identifier!");
            }


            String resultClassification = symbolsTable.getType(currentToken.getText());

            currentToken = getNextToken();
            if (currentToken.getText().equals(":="))
            {
                typeControl.pushMark();
                expression();
                try{
                    typeControl.popMark();
                    typeControl.verifyResult(resultClassification);
                }catch (SemanticException e) {
                    semanticError(e.getMessage());
                }

            }
            else
            {
                count -= 2;
                procedureActivationA();
                typeControl.reset();
            }
        }
        else if (currentToken.getText().toLowerCase().equals("if"))
        {
            String resultClassification = "boolean";
            typeControl.pushMark();
            expression();

            try{
                typeControl.popMark();
                typeControl.verifyResult(resultClassification);
            }catch (SemanticException e) {
                semanticError(e.getMessage());
            }

            currentToken = getNextToken();
            if (!currentToken.getText().toLowerCase().equals("then"))
            {
                count--;
                syntaxError("Keyword 'Then' was not found!");
            }

            commandStructure();

            elsePart();
        }
        else if (currentToken.getText().toLowerCase().equals("while"))
        {
            String resultClassification = "boolean";
            typeControl.pushMark();
            expression();
            try{
                typeControl.popMark();
                typeControl.verifyResult(resultClassification);
            } catch (SemanticException e) {
                semanticError(e.getMessage());
            }

            currentToken = getNextToken();
            if (!currentToken.getText().toLowerCase().equals("do"))
            {
                count--;
                syntaxError("Keyword 'do' was not found!");
            }

            commandStructure();
        }
        else if (currentToken.getText().toLowerCase().equals("do"))
        {
            commandStructure();

            currentToken = getNextToken();
            if (!currentToken.getText().toLowerCase().equals("while"))
            {
                count--;
                syntaxError("Keyword 'while' was not found!");
            }

            String resultClassification = "boolean";
            typeControl.pushMark();
            expression();
            try{
                typeControl.popMark();
                typeControl.verifyResult(resultClassification);
            } catch (SemanticException e) {
                semanticError(e.getMessage());
            }

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
     * command | compoundCommand
     *
     * @throws SyntaxException
     */
    private void commandStructure() throws SyntaxException, SemanticException {
        currentToken = getNextToken();
        count--;
        if (currentToken.getText().toLowerCase().equals("begin"))
        {
            compoundCommand();
        }
        else
        {
            command();
        }
    }


    /**
     * else command()
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void elsePart() throws SyntaxException, SemanticException {
        currentToken = getNextToken();
        if (currentToken.getText().toLowerCase().equals("else"))
        {
            commandStructure();
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
    private void procedureActivationA() throws SyntaxException, SemanticException {
        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            syntaxError("Invalid identifier!");
        }

        typeControl.setCallProcedure(true, symbolsTable.getProcedure(currentToken.getText()));
        currentToken = getNextToken();
        if (!currentToken.getText().equals("("))
        {
            count--;
            return;
        }

        count--;
        expressionListA();

    }


    /*EXPRESSIONS PART, INCLUDES:
    * expressionListA -> (expression -> expressionListB)*
    *
    * expression -> simpleExpressionA*
    *
    * simpleExpressionA -> (termA -> simpleExpressionB)*
    * */


    /**
     * (expression() expressionListB)
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void expressionListA() throws SyntaxException, SemanticException {
        currentToken = getNextToken();
        if (!currentToken.getText().equals("("))
        {
            count--;
        }
        else
        {

            typeControl.pushMark();
            expression();
            try {
                typeControl.popMark();

                if(typeControl.isCallProcedure()) {
                    typeControl.pushParameter(typeControl.getFirstType());
                    typeControl.reset();
                }
            }  catch (SemanticException e) {
                semanticError(e.getMessage());
            }

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
    private void expressionListB() throws SyntaxException, SemanticException {
        currentToken = getNextToken();
        if (!currentToken.getText().equals(","))
        {
            count--;
            try {
                typeControl.verifyResultProcedureCall();
            } catch (SemanticException e) {
                semanticError(e.getMessage());
            }

            return;
        }

        typeControl.pushMark();
        expression();

        try {
            typeControl.popMark();

            if(typeControl.isCallProcedure()) {
                typeControl.pushParameter(typeControl.getFirstType());
                typeControl.reset();
            }
        } catch (SemanticException e) {
            semanticError(e.getMessage());

        }

        expressionListB();
    }


    /**
     * simpleExpresssionA() (relationalOperator simpleExpressionA())?
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void expression() throws SyntaxException, SemanticException {
        simpleExpressionA();

        currentToken = getNextToken();
        if (!currentToken.getClassification().equals(Token.Classifications.RELATIONAL.toString()))
        {
            count--;
        }
        else
        {
            typeControl.pushOperation("relational");
            simpleExpressionA();
        }
    }


    /**
     * (+|-)? termA() simpleExpressionB()
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void simpleExpressionA() throws SyntaxException, SemanticException {
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
    private void simpleExpressionB() throws SyntaxException, SemanticException {
        currentToken = getNextToken();
        //stop simpleExpressionB's loop
        if (!currentToken.getClassification().equals(Token.Classifications.ADDITION.toString()))
        {
            count--;
        }
        else
        {
            typeControl.pushOperation("addition");
            termA();
            simpleExpressionB();
        }
    }


    /*TERMS PART, INCLUDES:
    * termA -> (factor -> termB)*
    *
    * factor -> (expressionListA | expression | factor)
    * */


    /**
     * factor() termB()
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void termA() throws SyntaxException, SemanticException {
        factor();
        termB();
    }


    /**
     * ((*|/|and) factor())*
     *
     * @throws SyntaxException For more information on the error, use getMessage()
     */
    private void termB() throws SyntaxException, SemanticException {
        currentToken = getNextToken();
        if (currentToken.getClassification().equals(Token.Classifications.MULTIPLICATION.toString()))
        {
            typeControl.pushOperation("multiplication");
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
    private void factor() throws SyntaxException, SemanticException {
        currentToken = getNextToken();
        if (currentToken.getClassification().equals(Token.Classifications.IDENTIFIER.toString()))
        {
            if(symbolsTable.getProgramName().toLowerCase().equals(currentToken.getText().toLowerCase())) semanticError("Program name cannot be used");
            //Checks whether the current identifier is declared elsewhere
            if(!symbolsTable.hasIdentifier(currentToken.getText()))
            {
                semanticError("Using not declared identifier!");
            }


            try {
                typeControl.pushType(symbolsTable.getType(currentToken.getText()));
            } catch (SemanticException e) {
                semanticError(e.getMessage());

            }


            currentToken = getNextToken();
            count--;
            if (currentToken.getText().equals("("))
            {
                expressionListA();
            }
        }
        else if (currentToken.getText().equals("("))
        {
            typeControl.pushMark();
            expression();
            try {
                typeControl.popMark();
            } catch (SemanticException e) {
                semanticError(e.getMessage());
            }

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
        else if (Token.types.contains(currentToken.getClassification()))
        {
            try{
                typeControl.pushType(currentToken.getClassification());
            } catch (SemanticException e) {
                semanticError(e.getMessage());
            }
        }
        else {
            count--;
            syntaxError("Expected factor.");
        }


    }


    /*UTILITIES PART, INCLUDES:
    *   getNextToken & syntaxError
    * */


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
        String str = "Syntax Error! Line " + currentToken.getLineNumber() + ":\n" +
                currentToken.getText() + "\n" +
                errorMsg;
        throw new SyntaxException(str);
    }

    /**
     * Default method for throwing syntaxError exceptions.
     *
     * @param errorMsg Message that explains the error.
     * @throws SemanticException For more information on the error, use getMessage()
     */
    private void semanticError(String errorMsg) throws SemanticException
    {
        String str = "Semantic Error! Line " + currentToken.getLineNumber() + ":\n" +
                currentToken.getText() + "\n" +
                errorMsg;
        throw new SemanticException(str);
    }

}
