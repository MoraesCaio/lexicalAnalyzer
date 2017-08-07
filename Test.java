import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
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
            tokenizer = new Tokenizer(Files.readAllLines(Paths.get("Program1.pascal")));
            tokenizer.parse();
        }
        catch (Exception e)
        {
            System.out.println("Error:");
            e.printStackTrace();
            //System.out.println(System.getProperty("user.dir"));
        }

        System.out.println("Program has finished. Press <ENTER> to close.");
        //s.nextLine();
    }
}
