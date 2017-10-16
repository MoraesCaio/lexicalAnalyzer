import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import lexicalAnalyzer.*;
import semanticAnalyzer.SemanticException;
import syntaxAnalyzer.*;

/**
 * This is a program analyzer for Pascal. It was created as experience for a better compiler's comprehension.
 * <p>
 * Created on 06/08/17 by
 * <p>
 * Caio Moraes
 * GitHub: MoraesCaio
 * Email: caiomoraes
 * <p>
 * Janyelson Oliveira
 * GitHub: janyelson
 * Email: janyelsonvictor@gmail.com
 */

public class Test
{
    public static void main(String[] args)
    {
        Tokenizer tokenizer;
        SyntaxAnalyzer syntaxAnalyzer;

        try
        {
            tokenizer = new Tokenizer(Files.readAllLines(Paths.get("Test.pascal")), false);
            tokenizer.parse();
            for (Token t : tokenizer.getTokens())
            {
                System.out.println(t + "\n");
            }
            syntaxAnalyzer = new SyntaxAnalyzer(tokenizer.getTokens());
            syntaxAnalyzer.run();
        }
        catch (IllegalArgumentException | IOException iaExc)
        {
            System.out.println("Error: " + iaExc.getMessage());
            iaExc.printStackTrace();
        }
        catch (SyntaxException | SemanticException sEx)
        {
            System.out.println(sEx.getMessage());
            sEx.printStackTrace();
        }

        System.out.println("Program has finished. Press <ENTER> to close.");
        //s.nextLine();
    }
}
