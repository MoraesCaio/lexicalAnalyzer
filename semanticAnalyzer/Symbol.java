package semanticAnalyzer;

/**
 * This is a class for represent symbols (identifiers) with its name and type.
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
	private String name;
	private String type;

	public Symbol(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public Symbol(String name) {
		this(name, "void");
	}

	public Symbol() {
		this("identifier", "void");
	}

	/**
	 * Get a name of identifier.
	 *
	 * @return name of identifier
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get a type of identifier.
	 *
	 * @return type of identifier
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Add a type to the identifier.
	 *
	 * @param type will be add to identifier
	 */
	public void setType(String type){
		this.type = type;
	}
 }