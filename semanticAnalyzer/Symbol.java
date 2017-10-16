package semanticAnalyzer;
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

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type){
		this.type = type;
	}
 }