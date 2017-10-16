package semanticAnalyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class for represent procedures with its parameters, to know the parameters of the procedure in question.
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
public class ProcedureSymbol extends Symbol{

    private List<Symbol> parameters;
    public ProcedureSymbol(String name)
    {
        super(name, "procedure");
        parameters = new ArrayList<>();
    }

    public ProcedureSymbol()
    {
        this("identifier");
    }

    /**
     * Take the amount of parameters.
     *
     * @return return amount of parameters.
     */
    public int getParametersSize() {
        return parameters.size();
    }

    /**
     * Get parameter name located at i position.
     *
     * @param i position
     * @return return a name parameter.
     */
    public Symbol getParamater(int i) {
        return parameters.get(i);
    }

    /**
     * Add a parameter to this procedure.
     *
     * @param parameter Symbol
     */
    public void addParameter(Symbol parameter)
    {
        parameters.add(parameter);
    }



}
