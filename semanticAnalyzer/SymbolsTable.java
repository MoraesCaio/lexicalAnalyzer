package semanticAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class SymbolsTable {
    
    private final static String MARK = "$";
    private List<String> stack;

    public SymbolsTable() {
        stack = new ArrayList<String>();
    }

    /**
     * Adds a tag to the stack, indicating a new scope
     */
    public void enterScope() {
        stack.add(MARK);
    }

    /**
     * Remove all identifiers of the stack until reach a tag
     */
    public void exitScope() {
        int i = stack.size() - 1;
        while(!stack.get(i).equals(MARK)) {
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
        while(!stack.get(i).equals(MARK)) {
            if(stack.get(i).equals(identifierName)) return true;
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
            if(stack.get(i).equals(identifierName)) return true;
            i--;
        }

        return false;
    }

    /**
     * Put a identifier on top of the stack
     * @param identifierName identifier that will be placed int the stack
     */
    public void addSymbol(String identifierName) {
        stack.add(identifierName);
    }
}
