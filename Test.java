import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import lexicalAnalyzer.*;

/**
 * This is a lexical analyzer for Pascal. It was created as experience for a better compiler's comprehension.
 *
 * Created on 06/08/17 by
 *
 * Caio Moraes
 * GitHub: MoraesCaio
 * Email: caiomoraes
 *
 * Janyelson Oliveira
 * GitHub: janyelson
 * Email: janyelsonvictor@gmail.com
 */

public class Test
{
    public static void main(String[] args)
    {
        Tokenizer tokenizer;
        Scanner s = new Scanner(System.in);

        try
        {
            tokenizer = new Tokenizer(Files.readAllLines(Paths.get("Program1.pascal")), true);
            tokenizer.parse();
            for(Token t : tokenizer.getTokens())
            {
                System.out.println(t + "\n");
            }
        }
        catch (IllegalArgumentException iaExc)
        {
            System.out.println("Error: " + iaExc.getMessage());
            iaExc.printStackTrace();
        }
        catch (IOException ioExc)
        {
            System.out.println("Error: " + ioExc.getMessage());
            ioExc.printStackTrace();
        }

        System.out.println("Program has finished. Press <ENTER> to close.");
        //s.nextLine();
    }
}
