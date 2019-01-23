package com.carpenter.yan.protobuf;// See README.txt for information and build instructions.

import java.io.FileInputStream;

class ListPeople {
  // Iterates though all people in the AddressBook and prints info about them.
  static void Print(AddressBookProtos.AddressBook addressBook) {
    for (AddressBookProtos.Person person: addressBook.getPeopleList()) {
      System.out.println("Person ID: " + person.getId());
      System.out.println("  Name: " + person.getName());
      if (person.getEmail() != null) {
        System.out.println("  E-mail address: " + person.getEmail());
      }

      for (AddressBookProtos.Person.PhoneNumber phoneNumber : person.getPhonesList()) {
        switch (phoneNumber.getType()) {
          case AddressBookProtos.Person.PhoneType.MOBILE:
            System.out.print("  Mobile phone #: ");
            break;
          case AddressBookProtos.Person.PhoneType.HOME:
            System.out.print("  Home phone #: ");
            break;
          case AddressBookProtos.Person.PhoneType.WORK:
            System.out.print("  Work phone #: ");
            break;
          default:
            System.out.println(" Unknown phone #: ");
            break;
        }
        System.out.println(phoneNumber.getNumber());
      }
    }
  }

  // Main function:  Reads the entire address book from a file and prints all
  //   the information inside.
  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage:  ListPeople ADDRESS_BOOK_FILE");
      System.exit(-1);
    }

    // Read the existing address book.
    AddressBookProtos.AddressBook addressBook =
      AddressBookProtos.AddressBook.parseFrom(new FileInputStream(args[0]));

    Print(addressBook);
  }
}
