import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is the main class of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game. Users
 * can walk around some scenery. That's all. It should really be extended
 * to make it more interesting!
 * 
 * To play this game, create an instance of this class and call the "play"
 * method.
 * 
 * This main class creates and initialises all the others: it creates all
 * rooms, creates the parser and starts the game. It also evaluates and
 * executes the commands that the parser returns.
 * 
 * @author Felix Zenker
 * @version 1.0 (November 2022)
 */

class Game {
    // ~~~ initialization ~~~
    public static void main(String args[]) {
        Game g = new Game();
        g.play();
    }

    private Parser parser;
    private Room currentRoom;
    private Boss endboss;

    // rooms
    Room lodge, pickNPay, paterNoster, rugbyField, beach;
    private List<Room> roomList;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() {
        roomList = new ArrayList<Room>();
        createRooms();
        parser = new Parser();
        endboss = new Boss("Harald");
        Random zufallsgenerator = new Random();
        int index = zufallsgenerator.nextInt(5);
        endboss.setLocation(roomList.get(index));
        System.out.println(endboss.getLocation().getDescription());

    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms() {
        // create the rooms
        lodge = new Room("at the Lodge, the center of the town Langebaan🏘");
        pickNPay = new Room("at the town mall🛒");
        paterNoster = new Room(
                "in a small village ~15km west with a nice beach and brilliant restaurant where you can eat hake'n'chips🐟🍟");
        rugbyField = new Room("at the rugby field. Are you ready to get rumbled?🏉");
        beach = new Room("At the beach 🌴🏖. Be carefull of the great white shark");
        roomList.add(lodge);
        roomList.add(pickNPay);
        roomList.add(paterNoster);
        roomList.add(rugbyField);
        roomList.add(beach);

        // initialise room exits
        lodge.setExit("east", pickNPay);
        lodge.setExit("south", rugbyField);
        lodge.setExit("west", paterNoster);

        pickNPay.setExit("west", lodge);

        paterNoster.setExit("east", lodge);

        rugbyField.setExit("north", lodge);
        rugbyField.setExit("east", beach);

        beach.setExit("west", rugbyField);

        currentRoom = lodge; // start game outside
    }

    /**
     * Main play routine. Loops until end of play.
     */
    public void play() {
        printWelcome();

        // Enter the main command loop. Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    private void printRoomInfo() {
        System.out.println("Exits: " + currentRoom.getExitsString());
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to Adventure!");
        System.out.println("Adventure is a new, incredibly amazing game. Finde den Boss, um das Spiel zu gewinnen");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println("You are " + currentRoom.getDescription());
        printRoomInfo();

    }

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        if (command.isUnknown()) {
            System.out.println("Formuliere wie ein schlauer Mensch ...IDIOT!");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help"))
            printHelp();
        else if (commandWord.equals("go"))
            goRoom(command);
        else if (commandWord.equals("look"))
            challengeBoss();
        else if (commandWord.equals("quit"))
            wantToQuit = quit(command);

        if (endboss.getGeschlagen() == true) {
            wantToQuit = true;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    private void challengeBoss() {

        if (currentRoom == endboss.getLocation()) {
            System.out.println("Your'e challenging the big boss " + endboss.getName());

            Random zufallsgenerator = new Random();
            int zahl1 = zufallsgenerator.nextInt((10 - 1) + 1) + 1; // gegoogled
            int zahl2 = zufallsgenerator.nextInt((10 - 1) + 1) + 1; // gegoogled

            System.out.println("Du gewinnst das Spiel, indem du das Matherätzel löst: " + zahl1 + "x" + zahl2 + " = ?");
            System.out.println(
                    "Hinweis zur Eingabe: Schreiebe zunächst ein ? und ein Leerzeichen und dann das Ergebnis!");
            Parser antwortParser = new Parser();
            Command antwort = antwortParser.getCommand();
            if (antwort.hasSecondWord()) {
                String antwortString = antwort.getSecondWord();
                int antwortAlsZahl = Integer.parseInt(antwortString);

                if (antwortAlsZahl == (zahl1 * zahl2)) {
                    System.out.println("Glückwunsch, du hast das Spiel gewonnen!");
                    endboss.geschlagen();
                } else {
                    System.out.println(
                            "Das Ergebnis ist leider falsch. Du bist weiter im Spiel gefangen. Der Boss befindet sich nun nicht mehr im aktuellen Raum.");
                    int index = zufallsgenerator.nextInt(roomList.size());
                    endboss.setLocation(roomList.get(index));
                }
            }
            else 
            {
                System.out.println("Falsches Eingabeformat, eine neue 'look' Aufforerung ist erforderlich und die Syntax muss stimmig sein.");
            }

        } else {
            System.out.println("Hier ist kein Boss auffindbar.");
        }
    }

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the
     * command words.
     */
    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.printCommands();
    }

    /**
     * Try to go to one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Mach klare Ansagen");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.gibAusgang(direction);

        if (nextRoom == null)
            System.out.println("HIER GEHTS NICHT WEITER!");
        else {
            currentRoom = nextRoom;
            System.out.println("You are " + currentRoom.getDescription());
            printRoomInfo();
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game. Return true, if this command
     * quits the game, false otherwise.
     */
    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else
            return true; // signal that we want to quit
    }
}
