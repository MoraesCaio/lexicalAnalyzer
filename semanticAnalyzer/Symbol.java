package semanticAnalyzer;

/**
 * This is a class for symbols (identifiers) defines its name and type.
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
public class Symbol
{
    /*PROPERTIES*/
    private String name;
    private String type;


    /*CONSTRUCTORS*/
    public Symbol(String name, String type)
    {
        this.name = name;
        this.type = type;
    }

    public Symbol(String name)
    {
        this(name, "void");
    }

    public Symbol()
    {
        this("No identifier specified");
    }


	/*METHODS - GETTERS AND SETTERS*/

    /**
     * Get the name of the identifier.
     *
     * @return String name of the identifier
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the type of the identifier.
     *
     * @return String type of the identifier
     */
    public String getType()
    {
        return type;
    }

    /**
     * Set the type of the identifier.
     *
     * @param type String type of the identifier
     */
    public void setType(String type)
    {
        this.type = type;
    }
}