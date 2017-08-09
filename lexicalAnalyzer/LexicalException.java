package lexicalAnalyzer;

/**
 * For exceptions related to this package.
 *
 * Created on 08/08/17 by
 *
 * Caio Moraes
 * GitHub: MoraesCaio
 * Email: caiomoraes
 *
 * Janyelson Oliveira
 * GitHub: janyelson
 * Email: janyelsonvictor@gmail.com
 */

public class LexicalException extends Exception {
    public LexicalException() { super(); }
    public LexicalException(String message) { super(message); }
    public LexicalException(String message, Throwable cause) { super(message, cause); }
    public LexicalException(Throwable cause) { super(cause); }
}
