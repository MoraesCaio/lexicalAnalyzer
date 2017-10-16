package semanticAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class SymbolsTable 
{

    private final static String MARK = "$";
    private final static String MARK_TYPE = "markType";
    private List<Symbol> stack;

    public SymbolsTable() 
    {
        stack = new ArrayList<Symbol>();
    }

    public String getType(String identifierName) {
    	int i = stack.size() - 1;
        while(i >= 0) {
            if(stack.get(i).getName().equals(identifierName)) return stack.get(i).getType();
            i--;
        }

        return "void";
    }

    /**
     * Adds a tag to the stack, indicating a new scope
     */
    public void enterScope() 
    {
        stack.add(new Symbol(MARK, MARK_TYPE));
    }

    /**
     * Remove all identifiers of the stack until reach a tag
     */
    public void exitScope() {
        int i = stack.size() - 1;
        while(!stack.get(i).getName().equals(MARK)) {
            stack.remove(i);
            i--;
        }

        stack.remove(i);
    }

    /**
     * Search for another indentifier declaration with same name and same scope
     * @param identifierName name of the identifier that will be searched on the stack
     * @return true if there is an identifier with the same name, otherwise false
     */
    public boolean searchDuplicateDeclaration(String identifierName) {

        int i = stack.size() - 1;
        while(!stack.get(i).getName().equals(MARK)) {
            if(stack.get(i).getName().equals(identifierName)) return true;
            i--;
        }

        return false;
    }


    /**
     * Search for another indentifier declaration with same name in all stack
     * @param identifierName name of the identifier that will be searched on the stack
     * @return true if there is an identifier with the same name, otherwise false
     */
    public boolean searchIdentifier(String identifierName) {
        int i = stack.size() - 1;
        while(i >= 0) {
            if(stack.get(i).getName().equals(identifierName)) return true;
            i--;
        }

        return false;
    }

    /**
     * Put a identifier on top of the stack
     * @param symbol identifier that will be placed int the stack
     */
    public void addSymbol(Symbol symbol) {
        stack.add(symbol);
    }

    public void assignType(String type) {
    	int i = stack.size() - 1;

    	while(stack.get(i).getType().equals("void")) {
    		stack.get(i).setType(type);
    		i--;
    	}
    }

    public void assignParameters()
    {
        ProcedureSymbol procedureSymbol = getLastProcedure();
        int i = stack.size() - 1;

        while(!stack.get(i).getType().equals("procedure")) {
            if(!stack.get(i).getType().equals(MARK_TYPE)) procedureSymbol.addParameter(stack.get(i));
            i--;
        }

    }

    private ProcedureSymbol getLastProcedure() {

        int i = stack.size() - 1;

        while(!stack.get(i).getType().equals("procedure")) {
            i--;
        }
        return (ProcedureSymbol) stack.get(i);
    }

    public ProcedureSymbol getProcedure(String procedureName) {
        int i = stack.size() - 1;
        while(!stack.get(i).getName().equals(procedureName) && stack.get(i).getType().equals("procedure") ) {
            i--;
        }

        return (ProcedureSymbol) stack.get(i);
    }

    public String getProgramName() {
        return stack.get(1).getName();
    }

}
