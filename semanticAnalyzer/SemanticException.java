package semanticAnalyzer;

/**
 * For exceptions related to this package.
 * <p>
 * Created on 31/08/17 by
 * <p>
 * Caio Moraes
 * GitHub: MoraesCaio
 * Email: caiomoraes
 * <p>
 * Janyelson Oliveira
 * GitHub: janyelson
 * Email: janyelsonvictor@gmail.com
 */

public class SemanticException extends Exception
{
    public SemanticException()
    {
        super();
    }

    public SemanticException(String message)
    {
        super(message);
    }

    public SemanticException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public SemanticException(Throwable cause)
    {
        super(cause);
    }
}
