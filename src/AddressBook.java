import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.io.File; // Import the File class
import java.io.FileWriter; // Import the FileWriter class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.IOException; // Import this class to handle errors

/* *************************************************************************
 * AddressBook class for "Java Project - Address Book"
 *
 * @author Len Lutz
 */
public class AddressBook {
    // set up scanner to gather information from user
    static Scanner scanner = new Scanner(System.in);
    private static ArrayList<AddressInfo> addresses = new ArrayList<>();
    private static boolean dataChanged = false;

    /*
     * *************************************************************************
     * method: main - routine that starts the program
     *
     * @param args - Array of String entered as command line arguments
     * No arguments are expected or checked for in this program
     */
    public static void main(String[] args) {
        int menuChoice;

        // if AddressBook.csv file exists, load the data from the file
        fillBookFromFile();

        // run this code until told to stop!
        while (true) {
            // display menu
            System.out.println("\n*****************************************");
            System.out.println("\n  *** Address Book Application Menu ***");
            System.out.println("\n   What would you like to do?");
            System.out.println("\t1 - Add an entry");
            System.out.println("\t2 - Remove an entry");
            System.out.println("\t3 - Search for a specific entry");
            System.out.println("\t4 - Print the Address Book");
            System.out.println("\t5 - Delete ALL entries");
            System.out.println("\t6 - Quit the Program");

            // get user's choice
            menuChoice = getNumInput("\nPlease enter the number of your menu choice:");
            if (menuChoice == 6) {
                if (dataChanged) {
                    saveBookToFile();
                }
                return; // ends program
            }

            // if menuChoice is not valid, redisplay menu and try again
            if ((menuChoice < 1) || (menuChoice > 6)) {
                System.out.println(" Invalid menu choice.");
                continue;
            }

            // if list is empty, cannot do anything other than add entry (menuChoice 1)
            if ((menuChoice > 1) && (addresses.size() == 0)) {
                System.out.println("The address List is Empty.");
                continue;
            }

            // call selected
            switch (menuChoice) {
                case 1: // add an entry
                    addEntry();
                    break;
                case 2: // remove an entry
                    removeEntry();
                    break;
                case 3: // search for specific entry
                    searchForEntry();
                    break;
                case 4: // print all addresses
                    int count = 0;
                    for (AddressInfo address : addresses) {
                        printEntry(address);
                        count++;
                    }
                    System.out.println("Address Book contains " + count + " entr" + ((count == 1) ? "y." : "ies."));
                    break;
                default: // clear the entire list
                    deleteAll();
                    break;
            }
            // stay in while loop to start over
        }
    }

    /*
     * *************************************************************************
     * Method: getNumInput
     *
     * Purpose: Processes numeric input. If number has not been
     * entered, or if number is invalid, it keeps asking.
     *
     * @param prompt - String with prompt asking user for specific information
     *
     * @return Returns validated number
     *
     */
    public static int getNumInput(String prompt) {
        // create local variables
        int result = 0;
        String userInput;

        // keeps asking for number until valid number has been entered
        while (result == 0) {
            // prompt for input
            System.out.println(prompt);

            // read in line from console
            userInput = scanner.nextLine();
            // if nothing is entered, the following "if" will not execute and
            // the while loop will start over
            if (!userInput.isEmpty()) {
                // invalid number will throw exception, so we check for that here
                try {
                    // if number was entered, store it in result
                    result = Integer.parseInt(userInput);
                } catch (Exception e) {
                    // if what was entered cannot be parsed to a number,
                    // ask for VALID number and stay in while loop
                    System.out.println("Please enter a valid number.");
                }
            }
        }
        return result;
    }

    /*
     * *************************************************************************
     * Method: getStringInput
     *
     * Purpose: Displays prompt asking for specific information, gets and
     * returns response
     *
     * @param prompt - String with prompt asking user for specific information
     *
     * @return Returns entered string
     *
     */
    public static String getStringInput(String prompt) {
        // ask for input and return it
        System.out.println(prompt);

        // read in and returns line from console
        return scanner.nextLine();
    }

    /*
     * *************************************************************************
     * Method: addEntry
     *
     * Purpose: Gather information for new entry and add it to the list
     *
     * @param No parameters are expected for this method
     *
     * @return void
     *
     */
    public static void addEntry() {
        String firstName = "";
        String lastName = "";
        String phone = "";
        String email = "";
        int countEmpties = 0;

        // gather and validate address information (Cannot be empty)

        // get firstName - if the user fails to enter anything 3 times
        // return to menu
        do {
            firstName = getStringInput("Please enter First Name: ");
            if (firstName.isEmpty() && (++countEmpties >= 3)) {
                countEmpties = 0;
                return;
            }
        } while (firstName.isEmpty());

        // get lastName - if the user fails to enter anything 3 times
        // return to menu
        do {
            lastName = getStringInput("Please enter Last Name: ");
            if (lastName.isEmpty() && (++countEmpties >= 3)) {
                countEmpties = 0;
                return;
            }
        } while (lastName.isEmpty());

        // get phone - if the user fails to enter anything 3 times
        // return to menu
        do {
            phone = getStringInput("Please enter Phone Number: ");
            if (phone.isEmpty() && (++countEmpties >= 3)) {
                countEmpties = 0;
                return;
            }
        } while (phone.isEmpty());

        // get email - if the user fails to enter anything 3 times
        // return to menu
        do {
            email = getStringInput("Please enter person's E-mail: ");
            if (email.isEmpty() && (++countEmpties >= 3)) {
                countEmpties = 0;
                return;
            }
        } while (email.isEmpty());

        // check to see if email has already been added
        // If it has, do NOT add it again, inform user and return to menu
        if (addresses.size() > 0) {
            String testEmail = email.toLowerCase();
            for (AddressInfo address : addresses) {
                if (address.getEmail().toLowerCase().equals(testEmail)) {
                    System.out.println("\n'" + email + "'" + " has already been added, entry has been ignored.");
                    return;
                }
            }
        }

        // add address to addresses ArrayList
        AddressInfo address = new AddressInfo(firstName, lastName, phone, email);
        addresses.add(address);
        printEntry(address);
        System.out.println("Has been added to list.\n\n");
        dataChanged = true;
    }

    /*
     * *************************************************************************
     * Method: printEntry
     *
     * Purpose: Print the information for the passed-in address
     *
     * @param address - ArrayList item to be printed
     *
     * @return void
     *
     */
    public static void printEntry(AddressInfo address) {
        System.out.println("\n**********");
        System.out.println("  First Name: " + address.getFirstName());
        System.out.println("   Last Name: " + address.getLastName());
        System.out.println("Phone Number: " + address.getPhone());
        System.out.println("      E-Mail: " + address.getEmail());
        System.out.println("**********\n");

    }

    /*
     * *************************************************************************
     * Method: removeEntry
     *
     * Purpose: Searches for and (if found) removes specified entry
     *
     * @param There are no parameters expected for the method
     *
     * @return void
     *
     */
    public static void removeEntry() {
        boolean found = false;

        // get email of entry userwants to remove
        String email = getStringInput("Please enter E-mail for entry to remove:");

        // convert to lowercase for matching
        String testEmail = email.toLowerCase();

        // Loop through list to find entered email
        for (AddressInfo address : addresses) {
            if (address.getEmail().toLowerCase().equals(testEmail)) {
                found = true;
                // found match, so we display it
                printEntry(address);
            }
        }
        if (!found) {
            System.out.println("'" + email + "' Not Found, nothing removed.");
        } else {
            // Confirm user wants to delete it
            String response = getStringInput("Are you sure you want to remove this entry? (Y/N)");
            if (response.toUpperCase().charAt(0) == 'Y') {
                Iterator<AddressInfo> itr = addresses.iterator();
                while (itr.hasNext()) {
                    AddressInfo address = itr.next();
                    if (address.getEmail().toLowerCase().equals(testEmail)) {
                        itr.remove();
                        System.out.println("'" + email + "' record removed.\n");
                        dataChanged = true;
                    }
                }
            }
        }
        return;
    }

    /*
     * *************************************************************************
     * Method: searchForEntry
     *
     * Purpose: Searh for and print out entries matching provided search string
     *
     * @param There are no parameters expected for the method
     *
     * @return void
     *
     */
    public static void searchForEntry() {
        int menuChoice;

        while (true) {
            System.out.println("\n*****************************************");
            System.out.println("\n\t*** Search Menu ***");
            System.out.println("\nWhich field would you like to search?");
            System.out.println("\t1 - First Name");
            System.out.println("\t2 - Last Name");
            System.out.println("\t3 - Phone");
            System.out.println("\t4 - E-Mail");
            System.out.println("\t5 - Done Searching.");

            // get user's choice
            menuChoice = getNumInput("\nPlease enter the number of your menu Choice:");
            if (menuChoice == 5) {
                return; // Done searching
            }

            // if menuChoice is not valid, redisplay menu and try again
            if ((menuChoice < 1) || (menuChoice > 5)) {
                System.out.println(" Invalid menu choice.");
                continue;
            }

            String srchStr = getStringInput("\nPlease enter what you are looking for:");
            String lowSrchStr = srchStr.toLowerCase();
            int count = 0;
            String field = "";

            // search for whatever user entered
            switch (menuChoice) {
                case 1:
                    for (AddressInfo address : addresses) {
                        if (address.getFirstName().toLowerCase().contains(lowSrchStr)) {
                            printEntry(address);
                            count++;
                            field = "'First Name'";
                        }
                    }
                    break;
                case 2:
                    for (AddressInfo address : addresses) {
                        if (address.getLastName().toLowerCase().contains(lowSrchStr)) {
                            printEntry(address);
                            count++;
                            field = "'Last Name'";
                        }
                    }
                    break;
                case 3:
                    for (AddressInfo address : addresses) {
                        if (address.getPhone().toLowerCase().contains(lowSrchStr)) {
                            printEntry(address);
                            count++;
                            field = "'Phone'";
                        }
                    }
                    break;
                case 4:
                    for (AddressInfo address : addresses) {
                        if (address.getEmail().toLowerCase().contains(lowSrchStr)) {
                            printEntry(address);
                            count++;
                            field = "'E-mail'";
                        }
                    }
                    break;
            }

            if (count == 0) {
                System.out.println("Searching " + field + " for '" + srchStr + "' did not find any matches.");
            } else {
                System.out.println("Searching " + field + " for '" + srchStr + "' found " + count + " match" +
                        ((count == 1) ? "." : "es."));
            }
        }
    }

    /*
     * *************************************************************************
     * Method: deleteAll
     *
     * Purpose: Remove ALL entries from address list
     *
     * @param There are no parameters expected for the method
     *
     * @return void
     *
     */
    public static void deleteAll() {
        // Confirm user wants to delete EVERYTHING
        System.out.println("This action CANNOT be undone!");
        String response = getStringInput("Are you ABOLUTELY sure you want to remove EVERYTHING? (Y/N)");
        if (response.toUpperCase().charAt(0) == 'Y') {
            addresses.clear();
            System.out.println("The address List is now empty.");
            dataChanged = true;
        }
    }

    /*
     * *************************************************************************
     * Method: fillBookFromFile
     *
     * Purpose: Fills address book from .csv file
     *
     * @param There are no parameters expected for the method
     *
     * @return void
     *
     */
    public static void fillBookFromFile() {
        try {
            File bookFile = new File("./AddressBook.csv");
            Scanner myReader = new Scanner(bookFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] fields = data.split(", ");
                AddressInfo address = new AddressInfo(
                    // need to remove quotes from fields
                    fields[0].substring(1, fields[0].length()-1), 
                    fields[1].substring(1, fields[1].length()-1), 
                    fields[2].substring(1, fields[2].length()-1), 
                    fields[3].substring(1, fields[3].length()-1));
                addresses.add(address);
                System.out.println("Data read in from file:");
                printEntry(address);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
        }

    }

    /*
     * *************************************************************************
     * Method: saveBookToFile
     *
     * Purpose: Saves address book to .csv file
     *
     * @param There are no parameters expected for the method
     *
     * @return void
     *
     */
    public static void saveBookToFile() {
        File bookFile = new File("./AddressBook.csv");
        String myData = "";
        // remove current file
        bookFile.delete();

        // create and fill new file
        try {
            if (bookFile.createNewFile()) {
                FileWriter myWriter = new FileWriter("./AddressBook.csv");
                for (AddressInfo address : addresses) {
                    myData = String.format("\"%s\", \"%s\", \"%s\", \"%s\"\n",
                            address.getFirstName(),
                            address.getLastName(),
                            address.getPhone(),
                            address.getEmail());
                    myWriter.write(myData);
                }
                myWriter.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred trying to save file.");
        }
    }
}
