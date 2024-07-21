import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// *~*~* Anne Nelson *~*~* //

public class ChainReactionMain {

    public static void main(String[] args){

        ArrayList<ArrayList<String>> wordSets = new ArrayList<>();
        String filename = "wordList.txt";

        try{
            FileInputStream file = new FileInputStream(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()) {
                String words = scanner.nextLine();
                String[] wordArray = words.split(",");
                ArrayList<String> row = new ArrayList<>(Arrays.asList(wordArray));
                wordSets.add(row);
            }
        }
        catch (FileNotFoundException e){
            System.out.printf("File %s does not exist\n",filename);
        }

        cleanData(wordSets);

        while(true)
        {
            int chainLength = 0;
            int guesses = 0;

            System.out.println("*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*");
            System.out.println("*                               *");
            System.out.println("*       CHAIN REACTION          *");
            System.out.println("*  CAN YOU COMPLETE THE CHAIN?  *");
            System.out.println("*                               *");
            System.out.println("*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*\n");
            System.out.println("Tutorial..................press 0");
            System.out.println("Beginner..................press 1");
            System.out.println("Pro.......................press 2");
            System.out.println("Superstar.................press 3");
            System.out.println("Custom....................press 4\n");
            System.out.print("SELECT DIFFICULTY: ");
            int difficulty = new Scanner(System.in).nextInt();
            switch (difficulty) {
                case 0:{
                    chainLength = 3;
                    guesses = 3;
                    break;
                }
                case 1:{
                    chainLength = 3;
                    guesses = 10;
                    break;
                }
                case 2: {
                    chainLength = 5;
                    guesses = 15;
                    break;
                }
                case 3: {
                    chainLength = 7;
                    guesses = 20;
                    break;
                }
                default: {
                    System.out.print("Enter Chain Length: ");
                    chainLength = new Scanner(System.in).nextInt();
                    System.out.print("Enter Number of Guesses: ");
                    guesses = new Scanner(System.in).nextInt();
                    break;
                }
            }

            ChainReaction c = new ChainReaction(guesses,chainLength, wordSets);
            c.playGame();
            System.out.println("\nPlay Again (y)es or (n)o");
            char option = new Scanner(System.in).next().toLowerCase().charAt(0);
            if(option == 'n'){
                System.out.println("\nTHANK YOU FOR PLAYING!");
                break;
            }
        } // end of while loop (game loop)

    } // end of main


    /*  cleanData works by checking the data file for words that do not
     *  have a possible next link in the chain.  The file is made up of many
     *  lists of possible compound words.  Each list has a starting word,
     *  and one or more compound words.  If an ending word does not have
     *  a starting word elsewhere in the file, it is removed.  Then, if
     *  a starting word does not have any ending words, it is also removed.
     *  But then the entire file needs to be checked again in
     *  case the deleted words were ending words for other chains.
     *
     *  @pre - The file to be cleaned is provided.
     *
     *  @post - The ArrayList holding the words has been cleaned, so that
     *  all words have enough ending words for the chain.  This allows the
     *  code to always find a link of up to seven individual words (making
     *  six linked sets of compound words) in fewer than twenty attempts.
     */

    public static void cleanData
            (ArrayList<ArrayList<String>> wordSets)

    // ** I left in the print statements that I used while
    // figuring out the code so that I can remember it later. ** //

    {
        // ListsOfLengthOne will keep track of how many lists
        // have only one word.  If there are no more lists of
        // only one word, then the list is clean and the loop will stop.
        int ListsOfLengthOne;

        // execute at least once
        do {

            // Keep track of the number of lists with only one item,
            // because if there is a list with only one item, it needs
            // to be removed, and then that word needs to be removed
            // anywhere else is occurs.  This is set at 0 and then
            // incremented anytime there is a list with one word.
            ListsOfLengthOne = 0;

            // loop through the file, looking at each list individually
            for (int i = 0; i < wordSets.size(); i++) {
                //System.out.println();
                ArrayList<String> currentList = wordSets.get(i);
                //System.out.println("checking this set");
                //System.out.println(wordSets.get(i));

                // loop through the list, ignoring the starting word
                // checks each ending word in the list to see
                // if it matches a starting word elsewhere in the file
                for (int j = 1; j < currentList.size(); ++j) {
                    // access the current word in the ArrayLIst
                    String currentWord = currentList.get(j);
                    //System.out.println("checking for " + currentWord);
                    // set up a boolean to keep track of if a matching
                    // word is found
                    boolean foundThisWord = false;


                    // loop through the data file, checking only the
                    // starting words

                    for (ArrayList<String> eachWordSet : wordSets) {
                        String newWord = eachWordSet.get(0);
                        if (newWord.equals(currentWord)) {
                            // System.out.println("found " + currentWord
                            //        + " on line " + (k + 1));
                            foundThisWord = true;
                            break;
                            //System.out.println(wordSets.get(k));
                        }
                    }

                    // if the word that we are checking was never found
                    // as a starting word, remove it from the list
                    if (!foundThisWord) {
                        currentList.remove(currentWord);
                        //System.out.println("REMOVED " + currentWord);
                    }

                }
                //System.out.println("at the end of checking");
                //System.out.println(currentList);

                // if the list has only one word, then it has no
                // possible ending words, so the list can be removed
                // from the file, update the count keeping track of
                // how many lists with only one word
                if (currentList.size() == 1) {
                    ListsOfLengthOne += 1;
                    //System.out.println("REMOVING this list");
                    //System.out.println(currentList);
                    wordSets.remove(currentList);
                    //System.out.println();
                }
            }

        // end the loop when there are no more lists with one item
        } while (ListsOfLengthOne > 0);

        validate(wordSets);

    } // end of CleanData



    public static void validate(ArrayList<ArrayList<String>> wordSets){
        final int wordCountValid = 8033;
        final int wordSetCountValid = 2334;

        int numTotalWords = 0;
        for (ArrayList<String> wordSet : wordSets) {
            for (int j = 0; j < wordSet.size(); j++) {
                numTotalWords++;
            }
        }
        System.out.println("Total Word Count: " + numTotalWords);
        System.out.println("Number of Unique Words: " + wordSets.size());
        String status = "Incomplete";
        if(wordCountValid == numTotalWords && wordSetCountValid == wordSets.size()){
            status = "Complete";
        }
        System.out.println("Dataset Cleaning: " + status);
    }
}