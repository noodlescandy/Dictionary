import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Dictionary {
    // tester and has methods and stuff
    private SplayTree tree;
    public static void main(String[] args) {
        // perform a load on each of the 0+ args
        Dictionary dict = new Dictionary();
        for (String cmd : args) {
            dict.load(cmd);
        }

        // then enter interactive prompt mode
        boolean stillEntering = true;
        try (Scanner input = new Scanner(System.in)) {
            while (stillEntering) {
                System.out.println("\nEnter your command:");
                String command = input.nextLine();
                stillEntering = dict.execute(command);
            }
        }
    }

    public Dictionary(){
        tree = new SplayTree();
    }

    // load command
    public void load(String fileName){
        try (BufferedReader r = new BufferedReader(new FileReader(fileName))) {
            String cmd = null;
            while ((cmd = r.readLine()) != null) {
                cmd = cmd.trim();
                execute(cmd);
            }
        }
        catch (Exception e){
            System.err.println("Trouble reading " + fileName);
            System.err.println("Please provide a valid .txt file for loading.");
        }
    }

    public boolean execute(String cmd){
        int middle = cmd.trim().indexOf(" ");
        String command = middle == -1 ? cmd : cmd.substring(0, middle);
        command = command.toLowerCase();
        String args = "";
        if (middle != -1) {
            args = cmd.substring(middle+1).trim();
        }
        switch (command) {
            case "add": // add <word> <definition>
                int sep = args.indexOf(" ");
                String word = args.substring(0, sep);
                String definition = args.substring(sep+1).trim();
                System.out.println("Adding " + word + " with definition " + definition + " to dictionary.");
                tree.add(word, definition);
            break;
            case "find": // find <word>
                System.out.println(tree.find(args));
            break;
            case "list": // could be list <start> <end> or list
                // list
                if (args.equals("")) {
                    // list all words
                    tree.print();
                }

                // list <start> <end>
                //Lists all of the words in the dictionary and their definitions, which are greater or equal to the begin word and less than or equal to the end word.  The words and definitions should be listed in alphabetical order.  Note: the strings will not necessary be words that have been stored in the dictionary.  For example, to see all the words starting with 'F' the user might use "FA" as a begin string and "FZ" as an end string.  Preforming a list on a splay tree will NOT rebalance the tree.
                else{
                    int argsMiddle = args.indexOf(" ");
                    String start = args.substring(0, argsMiddle).trim();
                    String end = args.substring(argsMiddle+1).trim();
                    tree.print(start, end);
                }
                System.out.println();
            break;
            case "tree":
                System.out.println("Implementation: Splay");
                tree.preOrder();
            break;
            case "delete":
                tree.remove(args);
            break;
            case "quit":
                return false;
            case "load":
                load(args);
            break;
            case "":
            break;
            default:
                System.out.println("Unknown command: \"" + cmd +"\"");
            break;
        }
        return true;
    }
}
