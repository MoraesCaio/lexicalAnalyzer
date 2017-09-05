package lexicalAnalyzer;

/**
 * For exceptions related to this package.
 * <p>
 * Created on 08/08/17 by
 * <p>
 * Caio Moraes
 * GitHub: MoraesCaio
 * Email: caiomoraes
 * <p>
 * Janyelson Oliveira
 * GitHub: janyelson
 * Email: janyelsonvictor@gmail.com
 */

public class LexicalException extends Exception
{
    public LexicalException()
    {
        super();
    }

    public LexicalException(String message)
    {
        super(message);
    }

    public LexicalException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public LexicalException(Throwable cause)
    {
        super(cause);
    }
}
