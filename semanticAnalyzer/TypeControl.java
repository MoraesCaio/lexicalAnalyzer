package semanticAnalyzer;

import java.util.ArrayList;
import java.util.List;

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

	public void pushMark() {
	    stackExpression.add(MARK);
    }

    public void popMark() throws SemanticException {
        int i = stackExpression.size()-1;
        String result = stackExpression.get(i);

        stackExpression.remove(i);
        stackExpression.remove(i-1);

        pushType(result);

    }

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

	public void popType()
    {
		stackExpression.remove(stackExpression.size() -1);
	}

	public void refreshStack(String typeResult) throws SemanticException {
		popType();
		popType();
		pushType(typeResult);
	}

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

    public void reset()
    {
        stackExpression.clear();
    }

    public void printStack() {
	    int i = stackExpression.size() - 1;
        System.out.println("Stack:");
	    while(i >= 0) {
            System.out.println(i + ": " + stackExpression.get(i));
            i--;
        }
        System.out.println("\n\n");
    }

    public void pushOperation(String operation) {
	    this.operationStack.add(operation);
    }

    public void popOperation() {
	    operationStack.remove(operationStack.size()-1);
    }

    public String getLastOperation() {
	    return operationStack.get(operationStack.size()-1);
    }

    public void setCallProcedure(boolean b, ProcedureSymbol procedureSymbol) {
	    callProcedure = b;
	    this.procedureSymbol = procedureSymbol;
    }

    public boolean isCallProcedure() {
        return callProcedure;
    }

    public ProcedureSymbol getProcedure() {
        return procedureSymbol;
    }

    public void pushParameter(String type) {
        procedureParametersStack.add(type);
    }

    public String getFirstType() {
        return stackExpression.get(0);
    }

    public void resetProcedureControl() {
        procedureParametersStack.clear();
        stackExpression.clear();
        setCallProcedure(false, null);
    }


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
