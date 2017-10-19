package semanticAnalyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class for represent symbols table, which will store the symbols, with their type, and check for
 * duplicate declarations or use of undeclared identifiers,  using a stack and a marker to separate scopes.
 * <p>
 * Created on 14/10/17 by
 * <p>
 * Caio Moraes
 * GitHub: MoraesCaio
 * Email: caiomoraes
 * <p>
 * Janyelson Oliveira
 * GitHub: janyelson
 * Email: janyelsonvictor@gmail.com
 */
public class SymbolTable
{
    /*PROPERTIES*/
    private final static String MARK = "$", MARK_TYPE = "markType";
    private List<Symbol> stack;


    /*CONSTRUCTORS*/
    public SymbolTable()
    {
        stack = new ArrayList<Symbol>();
    }


    /*METHODS*/
    /**
     * Given the identifier name, returns its type.
     *
     * @param identifierName identifier name
     * @return type of identifier
     */
    public String getType(String identifierName)
    {
        for (int i = stack.size() - 1; i >= 0; i--)
        {
            if (stack.get(i).getName().equals(identifierName))
            {
                return stack.get(i).getType();
            }
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
    public void exitScope()
    {
        int i;
        for (i = stack.size() - 1; !stack.get(i).getName().equals(MARK); i--)
        {
            stack.remove(i);
        }

        stack.remove(i);
    }


    /**
     * Search for another identifier declaration with same name and same scope
     *
     * @param identifierName name of the identifier that will be searched on the stack
     * @return true if there is an identifier with the same name, otherwise false
     */
    public boolean hasDuplicateDeclaration(String identifierName)
    {
        for (int i = stack.size() - 1; !stack.get(i).getName().equals(MARK); i--)
        {
            if (stack.get(i).getName().equals(identifierName))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Search for another identifier declaration with same name in all stack
     *
     * @param identifierName name of the identifier that will be searched on the stack
     * @return true if there is an identifier with the same name, otherwise false
     */
    public boolean hasIdentifier(String identifierName)
    {
        for (int i = stack.size() - 1; i >= 0; i--)
        {
            if (stack.get(i).getName().equals(identifierName))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Put a identifier on top of the stack
     *
     * @param symbol identifier that will be placed int the stack
     */
    public void addSymbol(Symbol symbol)
    {
        stack.add(symbol);
    }


    /**
     * Assign a type to all identifiers with no types ("void").
     *
     * @param type type will be assign
     */
    public void assignType(String type)
    {
        for (int i = stack.size() - 1; stack.get(i).getType().equals("void"); i--)
        {
            stack.get(i).setType(type);
        }
    }


    /**
     * Assign parameters to the last declared procedure.
     */
    public void assignParameters()
    {
        ProcedureSymbol procedureSymbol = getLastProcedure();
        for (int i = stack.size() - 1; !stack.get(i).getType().equals("procedure"); i--)
        {
            if (!stack.get(i).getType().equals(MARK_TYPE))
            {
                procedureSymbol.addParameter(stack.get(i));
            }
        }
    }


    /**
     * Get the last declared procedure
     *
     * @return a procedure object
     */
    private ProcedureSymbol getLastProcedure()
    {
        int i;
        //searching for procedure's index
        for (i = stack.size() - 1; !stack.get(i).getType().equals("procedure"); i--);
        return (ProcedureSymbol) stack.get(i);
    }


    /**
     * Given the procedure name, returns its object.
     *
     * @param procedureName procedure name
     * @return procedure object
     */
    public ProcedureSymbol getProcedure(String procedureName)
    {
        int i;
        //searching for procedure's index
        for (i = stack.size() - 1;
                !stack.get(i).getName().equals(procedureName) && stack.get(i).getType().equals("procedure");
                i--);

        return (ProcedureSymbol) stack.get(i);
    }


    /**
     * Get the program name in the stack.
     *
     * @return name of program
     */
    public String getProgramName()
    {
        return stack.get(1).getName();
    }

}
