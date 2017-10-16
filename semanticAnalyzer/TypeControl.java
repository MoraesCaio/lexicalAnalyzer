package semanticAnalyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class for types verification, which will store the ecpressions, separated in:
 * 1- a stack for types, where each expression is divided using a tag, eg 1 + (2 + 3), in the stack it will be
 * [integer, MARK, integer, integer].
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
    private final static String MARK = "$";
	private List<String> stackExpression;
	private List<String> operationStack;
    private List<String> procedureParametersStack;

    private boolean callProcedure = false;
    private ProcedureSymbol procedureSymbol;

    public TypeControl()
    {
		stackExpression = new ArrayList<String>();
		operationStack = new ArrayList<String>();
        procedureParametersStack = new ArrayList<>();
	}

    /**
     * Push a MARK into the stack of expression. Represent a '(' in expression
     *
     */
	public void pushMark() {
	    stackExpression.add(MARK);
    }

    /**
     * Pop a MARK into the stack of expression. Represent a ')' in expression
     * Remove and store a type result of operation and remove  a MARK ('(')
     * , and put the result type store in the stack.
     * eg:
     *  stackExpression: [integer, MARK, integer] -> [integer, integer]
     *
     */
    public void popMark() throws SemanticException {
        int i = stackExpression.size()-1;
        String result = stackExpression.get(i);

        stackExpression.remove(i);
        stackExpression.remove(i-1);

        pushType(result);

    }

    /**
     * Push a type into the stack of expression. If there two sequence type in the stack, do the operation
     *
     * @param type type will be pushed
     */
	public void pushType(String type) throws SemanticException {
		stackExpression.add(type);

		int i = stackExpression.size()-1;
        if(i < 1) return;

		int count = 0;

        while (!stackExpression.get(i).equals(MARK)) {
            count++;
            i--;
        }

        int x1 = stackExpression.size()-1;
        int x2 = x1-1;
		if(count == 2) makeOperation(x1, x2);
	}

    /**
     * Pop a type of the stack of expression
     *
     */
	public void popType()
    {
		stackExpression.remove(stackExpression.size() -1);
	}

    /**
     * Pop two type in sequence and push the result
     * eg:
     *  stackExpression: [integer, MARK, integer integer] -> [integer, MARK, integer]
     *
     * @param typeResult type result
     */
	public void refreshStack(String typeResult) throws SemanticException {
		popType();
		popType();
		pushType(typeResult);
	}


    /**
     * Make operation with two types in sequence and call refreshStack
     *
     * @param x1,x2 x1 for firt type and x2 second type in the stack of expression
     * @throws SemanticException for errors in type combination
     */
	public void makeOperation(int x1, int x2) throws SemanticException {
        if(stackExpression.get(x1).toLowerCase().equals("integer") && stackExpression.get(x2).toLowerCase().equals("integer")) {
            if(!getLastOperation().equals("relational")) {
                refreshStack("integer");
            } else {
                refreshStack("boolean");
            }

            popOperation();
        }
        else if(stackExpression.get(x1).toLowerCase().equals("integer") && stackExpression.get(x2).toLowerCase().equals("real")) {
            if(!getLastOperation().equals("relational")) {
                refreshStack("real");
            } else {
                refreshStack("boolean");
            }
            popOperation();
        }
        else if(stackExpression.get(x1).toLowerCase().equals("real") && stackExpression.get(x2).toLowerCase().equals("integer")) {
            if(!getLastOperation().equals("relational")) {
                refreshStack("real");
            } else {
                refreshStack("boolean");
            }
            popOperation();
        }
        else if(stackExpression.get(x1).toLowerCase().equals("real") && stackExpression.get(2).toLowerCase().equals("real")) {
            if(!getLastOperation().equals("relational")) {
                refreshStack("real");
            } else {
                refreshStack("boolean");
            }
            popOperation();
        }
        else if(stackExpression.get(x1).toLowerCase().equals("boolean") && stackExpression.get(x2).toLowerCase().equals("boolean")) {
            if(getLastOperation().equals("relational")) {
                refreshStack("boolean");
            }
            else {
                throw new SemanticException("Error in operation");
            }
            popOperation();
        }
        else {
            popOperation();
            throw new SemanticException("Error in types combination!");
        }
    }

    /**
     * Verify result, comparing the last element int the stack with the expected result of the entire expression.
     *
     * @param typeVar, expected result.
     */
	public void verifyResult(String typeVar) throws SemanticException {
        if(stackExpression.get(0).toLowerCase().equals("integer") && typeVar.toLowerCase().equals("integer")) {
            stackExpression.clear();
        }
        else if(stackExpression.get(0).toLowerCase().equals("integer") && typeVar.toLowerCase().equals("real")) {
            stackExpression.clear();
        }
        else if(stackExpression.get(0).toLowerCase().equals("real") && typeVar.toLowerCase().equals("real")) {
            stackExpression.clear();
        }
        else if(stackExpression.get(0).toLowerCase().equals("boolean") && typeVar.toLowerCase().equals("boolean")) {
            stackExpression.clear();
        }
        else {

            stackExpression.clear();
            throw new SemanticException("Error in assigning value to a variable!");
        }
    }

    /**
     * Remove all types in the stackExpression the stack.
     *
     */
    public void reset()
    {
        stackExpression.clear();
    }

    /**
     * Print all elements in the stack of expression in order
     *
     */
    public void printStack() {
	    int i = stackExpression.size() - 1;
        System.out.println("Stack:");
	    while(i >= 0) {
            System.out.println(i + ": " + stackExpression.get(i));
            i--;
        }
        System.out.println("\n\n");
    }

    /**
     * Push operation in the stack of operation
     * eg:
     *  stackOperation: [addition] -> [addition, relational]
     *
     * @param operation opetarion will be pushed
     */
    public void pushOperation(String operation) {
	    this.operationStack.add(operation);
    }

    /**
     * Pop operation in the stack of operation
     * eg:
     *  stackOperation: [addition, relational] -> [addition]
     *
     */
    public void popOperation() {
	    operationStack.remove(operationStack.size()-1);
    }


    /**
     * Get the last operation in the stack of operation
     *
     * @return operation
     */
    public String getLastOperation() {
	    return operationStack.get(operationStack.size()-1);
    }

    /**
     * Put that will be a check of the arguments of a procedure with the input parameters if 'b' is true and
     * procedureSymbol is the procedure called.
     *
     * @param b, set true if is a procedure call and false if the procedure call is over.
     * @param procedureSymbol the procedure called
     */
    public void setCallProcedure(boolean b, ProcedureSymbol procedureSymbol) {
	    callProcedure = b;
	    this.procedureSymbol = procedureSymbol;
    }

    /**
     * Verify if is a procedure call.
     *
     * @return callProcedure value.
     */
    public boolean isCallProcedure() {
        return callProcedure;
    }

    /**
     * Add a result of a expression in the stack of procedure parameters.
     *
     * @param type, type will be pushed
     */
    public void pushParameter(String type) {
        procedureParametersStack.add(type);
    }

    /**
     * get the last type in the stack of expression
     *
     * @param type in the last position
     */
    public String getFirstType() {
        return stackExpression.get(0);
    }

    /**
     * If is not anymore a call procedure.
     *
     */
    public void resetProcedureControl() {
        procedureParametersStack.clear();
        stackExpression.clear();
        setCallProcedure(false, null);
    }


    /**
     * Only for procedure calls. Verify result, comparing all elements in the procedureParametersStack
     * with all procedure parameters.
     *
     */
    public void verifyResultProcedureCall() throws SemanticException {

        if(procedureSymbol.getParametersSize() !=  procedureParametersStack.size())
        {

            throw new SemanticException("Parameters number error!");

        } else
        {
            for (int i = 0; i < procedureParametersStack.size(); i++) {
                if (procedureParametersStack.get(i).toLowerCase().equals("integer") && procedureSymbol.getParamater(i).getType().equals("integer")) {

                } else if (procedureParametersStack.get(i).toLowerCase().equals("integer") && procedureSymbol.getParamater(i).getType().equals("real")) {

                } else if (procedureParametersStack.get(i).toLowerCase().equals("real") && procedureSymbol.getParamater(i).getType().equals("real")) {

                }else if (procedureParametersStack.get(i).toLowerCase().equals("boolean") && procedureSymbol.getParamater(i).getType().equals("boolean")) {

                } else {
                    throw new SemanticException("Error in assigning value to parameter");
                }
            }
        }

        resetProcedureControl();
    }
}
