# Java Address Book Project
In this project, you will be creating an address book that can store information about people.
### Overall Goals
The application should be able to do the following:
- Present the user with a list of options to interact with the database and take in their input
- Allow the user to perform the following actions:
  - Add an entry
  - Remove and entry
  - Search for a specific entry
  - Print the contents of the address book
  - Delete the contents of the address book
  - Quit the program

### Entry Structure
Entries should be contained in a class. Each entry should contain the following 
properties: - First name - Last name - Phone number - Email address All variables
should be private and only accessible via getter and setter methods.

You should override the toString() method so that the entries can be printed in 
an easy-to-read-manner.

You can create other methods as you see fit.

### Address Book Structure
The address book should contain an ArrayList of Entry instances. This ArrayList 
can be accessed and modified using methods that accomplish the following:

- Adding entries
  - New entries should contain all of the properties required by an entry. The email address needs to be unique. Other properties do not need to be unique.
- Remove an entry
  - An entry can be removed by searching the database for an email address and then removing the entry with that unique email addres.
- Search for an entry
  - Users can pick which methods they will search by (first name, last name, phone number, or email address). The program will then take in a search query
    and search the address book for an entry that contains the search as a substring (e.g. if a first name search is conducted with 'a,' all entries that have a
    first name starting with 'a' will be returned).
- Printing the address book
  - All of the entries will be printed out
- Deleting the book
  - The address book will be cleared
- Quit
  - The program will stop running