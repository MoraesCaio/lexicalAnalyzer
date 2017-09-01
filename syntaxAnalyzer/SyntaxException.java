package syntaxAnalyzer;

/**
 * For exceptions related to this package.
 *
 * Created on 31/08/17 by
 *
 * Caio Moraes
 * GitHub: MoraesCaio
 * Email: caiomoraes
 *
 * Janyelson Oliveira
 * GitHub: janyelson
 * Email: janyelsonvictor@gmail.com
 */

public class SyntaxException extends Exception {
    public SyntaxException() { super(); }
    public SyntaxException(String message) { super(message); }
    public SyntaxException(String message, Throwable cause) { super(message, cause); }
    public SyntaxException(Throwable cause) { super(cause); }
}
