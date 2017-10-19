package semanticAnalyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class for types verification, which will store the expressions, separated in:
 * 1- a stack for types, where each expression is divided using a tag, eg 1 + (2 + 3), in the stack it will be
 * [integer, PARENTHESIS, integer, integer].
 * 2- a stack for operations, which push when it reads an operation and pop when it has 2 types in sequence
 * in the type stack, eg 1 + (2 + 3), in the stack it will be [addition, addition]. when reading 3, the operation
 * is performed.
 * 3- a stack for procedures, in which it will contain the types of the resulting parameter of an expression,
 * eg (10 + 10, 3.1), will contain: [integer, real], then compare with the parameters of the procedure called
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
public class TypeControl
{
    private final static String PARENTHESIS = "$", INT = "integer", REAL = "real", BOOLEAN = "boolean";
    private List<String> expressionStack;
    private List<String> operationStack;
    private List<String> procedureParametersStack;

    private boolean procedureCall = false;
    private ProcedureSymbol procedureSymbol;


    /*CONSTRUCTOR*/
    public TypeControl()
    {
        expressionStack = new ArrayList<String>();
        operationStack = new ArrayList<String>();
        procedureParametersStack = new ArrayList<String>();
    }


    /*METHODS*/
    /**
     * Push a PARENTHESIS into the stack of expression. Represent a '(' in expression
     */
    public void pushParenthesis()
    {
        expressionStack.add(PARENTHESIS);
    }


    /**
     * Pop a PARENTHESIS into the stack of expression. Represent a ')' in expression
     * Remove and store a type result of operation and remove a PARENTHESIS ('(')
     *  and put the result type store in the stack.
     * eg:
     * expressionStack: [integer, PARENTHESIS, integer] -> [integer, integer]
     */
    public void popParenthesis() throws SemanticException
    {
        int i = expressionStack.size() - 1;
        String result = expressionStack.get(i);

        expressionStack.remove(i);
        expressionStack.remove(i - 1);

        pushType(result);
    }


    /**
     * Push a type into the stack of expression. If there is two sequence type in the stack, do the operation
     *
     * @param type type will be pushed
     */
    public void pushType(String type) throws SemanticException
    {
        expressionStack.add(type);

        int i = expressionStack.size() - 1;
        if (i < 1)
        {
            return;
        }

        //counting symbols until mark symbol
        int count = 0;
        for (; !expressionStack.get(i).equals(PARENTHESIS); i--, count++);

        int x1 = expressionStack.size() - 1;
        int x2 = x1 - 1;
        if (count == 2)
        {
            doOperation(x1, x2);
        }
    }

    /**
     * Pop a type of the stack of expression
     */
    public void popType()
    {
        expressionStack.remove(expressionStack.size() - 1);
    }


    /**
     * Pop two type in sequence and push the result
     * eg:
     * expressionStack: [integer, PARENTHESIS, integer integer] -> [integer, PARENTHESIS, integer]
     *
     * @param typeResult type result
     */
    public void updateExpressionStack(String typeResult) throws SemanticException
    {
        popType();
        popType();
        pushType(typeResult);
    }


    /**
     * Make operation with two types in sequence and call updateExpressionStack
     *
     * @param firstTypeIdx index of the first operand's type in the stack of expression
     * @param secondTypeIdx index of the second operand's type in the stack of expression
     * @throws SemanticException for errors in type combination
     */
    public void doOperation(int firstTypeIdx, int secondTypeIdx) throws SemanticException
    {
        String firstType = expressionStack.get(firstTypeIdx).toLowerCase();
        String secondType = expressionStack.get(secondTypeIdx).toLowerCase();

        if (firstType.equals(INT) && secondType.equals(INT))
        {
            if (getLastOperation().equals("relational"))
            {
                updateExpressionStack(BOOLEAN);
            }
            else
            {
                updateExpressionStack(INT);
            }
        }
        //real & int; int & real; real & real
        else if ((firstType.equals(INT) && secondType.equals(REAL)) ||
                 (firstType.equals(REAL) && secondType.equals(INT)) ||
                 (firstType.equals(REAL) && secondType.equals(REAL))
                 )
        {
            if (getLastOperation().equals("relational"))
            {
                updateExpressionStack(BOOLEAN);
            }
            else
            {
                updateExpressionStack(REAL);
            }
        }
        //boolean & boolean
        else if (firstType.equals(BOOLEAN) && secondType.equals(BOOLEAN))
        {
            if (!getLastOperation().equals("relational"))
            {
                throw new SemanticException("Error: expected boolean and boolean operands.");
            }

            updateExpressionStack(BOOLEAN);
        }
        else
        {
            throw new SemanticException("Error in types combination!");
        }
        popOperation();
    }

    /**
     * Verify result, comparing the last element int the stack with the expected result of the entire expression.
     *
     * @param typeVar expected result.
     */
    public void verifyResult(String typeVar) throws SemanticException
    {
        String resultType = expressionStack.get(0).toLowerCase();
        String variableType = typeVar.toLowerCase();

        if ((variableType.equals(INT) && resultType.equals(INT)) ||
            (variableType.equals(BOOLEAN) && resultType.equals(BOOLEAN)) ||
            (variableType.equals(REAL) && resultType.equals(INT)) ||
            (variableType.equals(REAL) && resultType.equals(REAL))
            )
        {
            expressionStack.clear();
        }
        else
        {
            expressionStack.clear();
            throw new SemanticException("Error while assigning value to a variable!");
        }
    }


    /**
     * Remove all types in the expressionStack the stack.
     */
    public void reset()
    {
        expressionStack.clear();
    }


    /**
     * Print all elements in the stack of expression in order
     */
    public void printStack()
    {
        System.out.println("Stack:");
        for (int i = expressionStack.size() - 1; i >= 0; i--)
        {
            System.out.println(i + ": " + expressionStack.get(i));
        }
        System.out.println("\n\n");
    }


    /**
     * Push operation in the stack of operation
     * eg:
     * stackOperation: [addition] -> [addition, relational]
     *
     * @param operation opetarion will be pushed
     */
    public void pushOperation(String operation)
    {
        this.operationStack.add(operation);
    }


    /**
     * Pop operation in the stack of operation
     * eg:
     * stackOperation: [addition, relational] -> [addition]
     */
    public void popOperation()
    {
        operationStack.remove(operationStack.size() - 1);
    }


    /**
     * Get the last operation in the stack of operation
     *
     * @return operation
     */
    public String getLastOperation()
    {
        return operationStack.get(operationStack.size() - 1);
    }

    /**
     * Put that will be a check of the arguments of a procedure with the input parameters if 'b' is true and
     * procedureSymbol is the procedure called.
     *
     * @param b set true if is a procedure call and false if the procedure call is over.
     * @param procedureSymbol the procedure called
     */
    public void setProcedureCall(boolean b, ProcedureSymbol procedureSymbol)
    {
        procedureCall = b;
        this.procedureSymbol = procedureSymbol;
    }


    /**
     * Verify if is a procedure call.
     *
     * @return procedureCall value.
     */
    public boolean isProcedureCall()
    {
        return procedureCall;
    }


    /**
     * Add a result of a expression in the stack of procedure parameters.
     *
     * @param type, type will be pushed
     */
    public void pushParameter(String type)
    {
        procedureParametersStack.add(type);
    }


    /**
     * get the last type in the stack of expression
     *
     */
    public String getFirstType()
    {
        return expressionStack.get(0);
    }


    /**
     * If is not anymore a call procedure.
     */
    public void resetProcedureControl()
    {
        procedureParametersStack.clear();
        expressionStack.clear();
        setProcedureCall(false, null);
    }


    /**
     * Only for procedure calls. Verify result, comparing all elements in the procedureParametersStack
     * with all procedure parameters.
     */
    public void verifyResultProcedureCall() throws SemanticException
    {

        if (procedureSymbol.getParametersSize() != procedureParametersStack.size())
        {
            throw new SemanticException("Parameters number error!");
        }

        for (int i = 0; i < procedureParametersStack.size(); i++)
        {
            String resultType = procedureParametersStack.get(i).toLowerCase();
            String parameterType = procedureSymbol.getParameter(i).getType().toLowerCase();

            //if it's not a valid combination, throws an exception
            if ( !(parameterType.equals(BOOLEAN) && resultType.equals(BOOLEAN) ||
                    parameterType.equals(INT) && resultType.equals(INT) ||
                    parameterType.equals(REAL) && resultType.equals(INT) ||
                    parameterType.equals(REAL) && resultType.equals(REAL))
                    )
            {
                throw new SemanticException("Error in assigning value to parameter");
            }
        }

        resetProcedureControl();
    }
}
