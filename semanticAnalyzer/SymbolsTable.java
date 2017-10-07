package semanticAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class SymbolsTable {

    private final static String MARK = "$";
    private List<String> stack;

    public SymbolsTable() {
        stack = new ArrayList<String>();
    }

    public void enterScope() {
        stack.add(MARK);
    }

    public void exitScope() {
        int i = stack.size() - 1;
        while(!stack.get(i).equals(MARK)) {
            stack.remove(i);
            i--;
        }

        stack.remove(i);
    }

    public boolean searchDuplicateDeclaration(String identifierName) {

        int i = stack.size() - 1;
        while(!stack.get(i).equals(MARK)) {
            if(stack.get(i).equals(identifierName)) return true;
            i--;
        }

        return false;
    }

    public boolean searchIdentifier(String identifierName) {
        int i = stack.size() - 1;
        while(i >= 0) {
            if(stack.get(i).equals(identifierName)) return true;
            i--;
        }

        return false;
    }

    public void addSymbol(String identifierName) {
        stack.add(identifierName);
    }
}
