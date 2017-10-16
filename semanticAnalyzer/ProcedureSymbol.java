package semanticAnalyzer;

import java.util.ArrayList;
import java.util.List;

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

    public int getParametersSize() {
        return parameters.size();
    }

    public Symbol getParamater(int i) {
        return parameters.get(i);
    }

    public void addParameter(Symbol parameter)
    {
        parameters.add(parameter);
    }



}
