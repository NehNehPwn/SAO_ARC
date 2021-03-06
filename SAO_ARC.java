/************************
*      Aidan Smith      *
*  The SAO Arc Project  *
*     June 16th 2015    *
************************/

/*
Welcome to the code of the SAO Arc project.
This is a game developed in Java based on the
popular anime and manga, Sword Art Online (SAO).
It is a text based RPG with over 50 different
standard mobs and 4 extremley rare mobs. Character
stats are saved and when you die, you restart.

The four basic statistcs are:

Strength: Determines how much damage you do.
Defence: Detemines how much damage you are able to negate.
Evasion: How easily you can dodge an attack.
Health: Determines how much damage you can take. Restored at the end of every fight.

When you are in battle, you have 5 options as your moves:

Slash: Deals damage, countered by parry
Stab: Deals damage, countered by block
Parry: Counters slash, deals reduced damage to enemy if proper counter
Block: Counters stab, deals reduced damage to enemy if proper counter
Flee: Run away, has a 50% chance of faliure, you take half damage whenever you try to flee

If the opponent's health reaches zero before yours, you win the fight.
If yours hits 0 first however, you die, and cannot log back in.

After a battle, you have a 30% chance to pick up one of the weapons a mob has dropped.
You can choose if you want to keep or discard this item.

Harder mobs drop better items, and you want to collect the strongest items possible.
You can hold 10 at items at a time, and you can look at them, delete them, or make them your active in the inventory menu on the main screen.

You can also view the rules again by selecting the "Rules" box on the main screen.

You can also save your game from the main menu screen. If you try to exit without saving, and the game data has changed., the game will tell you that you should save,
and you can either just exit or go back to save.

Another option on the menu screen is to look at your statistics, to see how you are doing. It will help you gauge if something is too strong for you.

The final option on the title screen is cheats. I do recommend against cheating, because it loses the fun if you do. If you insist however the cheat codes are:
CheaterFinalBattle  -- Levels you to level 99 and sets you into the fight with godfree
CheaterLevel=  -- Puts you at the level you want to be at.

Your Goal is to defeat as many enemies as possible so that you can become strong enough to face the man who has trapped you in the game, Godfree.

IF you kill Godfree you win the game. You will be notified if your next fight is with Godfree.

As mentioned earlier, all of your data can be saved, and you can log back in using a username and password you create when you create a new game.

Good luck with your adventure, and have a fun time!
							~Aidan Smith
*/

import java.awt.*;
import hsa.Console;
import java.io.*;
import hsa.*;
import java.text.*;

public class SAO_ARC
{
    static Console c; // The output console
    static int strength; //ALL STATS UNDERNEATH BEFORE MAIN ARE CONSTANTLY USED, AND CHANGED, MADE STATIC SO THAT I DONT NEED TO CONSTANTLY PASS THEM
    static int defence;
    static int health;
    static double evasion = 30;
    static int level = 1;
    static int xp = 0;
    static int xp_to_next_level = 100;
    static String player_name = "";
    static double[] mob_stats = {1, 1, 1000, 100, 42};                                  //ARRAY POINTS --- 0 = HP --- 1 = DEFENCE --- 2 = STRENGTH --- 3 = EVADE --- 4 = XP
    static String mob_name = "Missingno"; /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static boolean exit = false;
    static String password;
    static boolean death = false;
    static boolean proper_login = false;
    static boolean godfree_fight = false;
    static boolean finished_game = false;
    static int[] character_gear = {2, 19, 0, 0, 0, 0, 0, 0, 0, 0};

    public static void main (String[] args) throws IOException
    {
	c = new Console (new String ("Sword Art Online ARC"));

	int box_at;
	boolean saved = true;
	int new_player = 0;
	int exit_selected = 0;
	char key_pressed;
	Font small_letters = new Font ("MingLiU", Font.PLAIN, 25);//FONTS FOR WHEN PROGRAM ENDS
	Font title_screen = new Font ("Bauhaus 93", Font.ITALIC, 40);
	Font large_letters = new Font ("MingLiU", Font.PLAIN, 50);
	Font largest_letters = new Font ("MingLiU", Font.BOLD, 100);
	Font death_font = new Font ("MingLiU", Font.BOLD, 75);
	Font end_letters = new Font ("MingLiU", Font.PLAIN, 20);

	while (proper_login == false)//If the character they are trying to play as is dead, it brings them back to them main menu
	{
	    new_player = start_menu ();
	}

	if (new_player == 0)//If the player is new to the game, and displays the rules
	{
	    rules ();
	}

	while (exit == false)//While they havent hit '='
	{
	    box_at = secondary_menu (); //Generate the main menu, depending on where they were when enter was pressed, play the correct method
	    switch (box_at)
	    {
		case 0:
		    { //Battle
			saved = false;
			init_fight ();
			break;
		    }

		case 1:
		    { //Stats
			look_at_stats ();
			break;
		    }

		case 2:
		    { //Cheats
			if (saved == false)
			{
			    cheat_page ();
			}
			
			else
			{
			    saved = cheat_page (); ///////////////////////////////////////CHEATS PAGE RETURNS A TRUE OR FALSE TO TEST IF CHANGES WERE MADE
			}
			break;
		    }

		case 3:
		    { //Inventory
			if (saved == false)
			{
			    inventory_check ();
			}
			
			else
			{
			    saved = inventory_check (); //////////////////////////////////////SAME AS INVENTORY
			}
			break;
		    }

		case 4:
		    { //Rules
			rules ();
			break;
		    }

		case 5:
		    { //Save
			save_game ();
			saved = true;
			c.setFont (largest_letters);
			c.setColor (Color.red);
			c.drawString ("Saved!", 150, 250);
			c.setColor (Color.black);
			delay (500);
			break;
		    }
	    } //Switch
	    c.clear ();


	    if (exit == true)//If they try to leave
	    {
		if (saved == false && death == false)//If they have not saved and arent dead
		{
		    c.setFont (small_letters);
		    c.drawString ("Are you sure you want to quit without saving?", 35, 80);
		    draw_selected_box (175, 100);
		    draw_box (175, 250);

		    c.setFont (title_screen);//Asks them if they want to go back and save
		    c.drawString ("Yes", 265, 162);
		    c.drawString ("No", 275, 315);

		    do
		    {
			key_pressed = c.getChar ();

			if (key_pressed == 'w' || key_pressed == 'W')
			{
			    draw_box (175, 250);
			    draw_selected_box (175, 100);
			    exit_selected = 0;
			}

			else if (key_pressed == 's' || key_pressed == 'S')
			{
			    draw_box (175, 100);
			    draw_selected_box (175, 250);
			    exit_selected = 1;
			}

			c.drawString ("Yes", 265, 162);
			c.drawString ("No", 275, 315);
		    }
		    while (key_pressed != '\n');

		    if (exit_selected == 0)
		    {
			break;
		    }

		    else
		    {
			exit = false;
			continue;
		    }
		}
	    }

	    if (death == true)//If they have died, leave the 2nd menu
	    {
		break;
	    }
	    else if (finished_game == true)//If they have finished the game, leave the second menu
	    {
		break;
	    }
	}

	if (finished_game == true)//Prints the winning sequence if the game is over
	{
	    c.clear ();
	    c.setColor (Color.black);
	    c.setFont (end_letters);
	    c.drawString ("Congratulations " + player_name + ", you have sucsessfuly", 75, 130); 
	    c.drawString ("conqured Aincrad. You have killed the final boss and", 70, 155);
	    c.drawString ("set the world to be free. You are free to keep playing,", 65, 180);
	    c.drawString ("however your work is done here, you may rest in peace.", 70, 205);
	    c.drawString ("Press any key to finish", 220, 255);
	    c.getChar ();
	    
	    c.clear ();
	    
	    c.setColor (Color.black);
	    c.setFont (large_letters);
	    c.drawString ("THANKS FOR PLAYING!", 100, 250);
	}
	
	else if (death == false)//If they have not died, it says thanks for playing
	{
	    c.clear ();
	    c.setColor (Color.black);
	    c.setFont (large_letters);
	    c.drawString ("THANKS FOR PLAYING!", 100, 250);
	}

	else//If they died it says "YOU HAVE DIED"
	{
	    c.clear ();
	    c.setColor (Color.red);
	    c.setFont (death_font);
	    c.drawString ("YOU HAVE DIED", 50, 250);
	}

    } // main method


    public static int start_menu () throws IOException//First menu that selects either to load or create  a new game
    {

	char key_pressed;
	int title_screen_selected = 0;//Variable used to tell what the player selected

	Color light_blue = new Color (0, 191, 255);
	c.setColor (light_blue);//makes background blue
	c.fillRect (0, 0, 640, 500);

	c.setColor (Color.black);
	Font big_letters = new Font ("MingLiU", Font.PLAIN, 50);
	c.setFont (big_letters);
	c.drawString ("Sword Art Online ARC", 60, 60);

	draw_selected_box (175, 100);
	draw_box (175, 250);

	Font title_screen = new Font ("Bauhaus 93", Font.ITALIC, 40);
	c.setFont (title_screen);
	c.drawString ("New Game", 200, 162);
	c.drawString ("Load Game", 200, 315);//Prints words in the boxes
	
	Font controls = new Font ("MingLiU", Font.PLAIN, 15);
	c.setFont (controls);
	
	c.drawString ("Use 'w', 's', and enter to choose.", 180, 90);//Instructions
	
	c.setFont (title_screen);
	
	

	do//Used for movement up and down
	{
	    key_pressed = c.getChar ();

	    if (key_pressed == 'w' || key_pressed == 'W')
	    {
		draw_box (175, 250);
		draw_selected_box (175, 100);
		title_screen_selected = 0;
	    }

	    else if (key_pressed == 's' || key_pressed == 'S')
	    {
		draw_box (175, 100);
		draw_selected_box (175, 250);
		title_screen_selected = 1;
	    }

	    c.drawString ("New Game", 200, 162);
	    c.drawString ("Load Game", 200, 315);
	}
	while (key_pressed != '\n');

	c.clear ();

	if (title_screen_selected == 0)//If they selected new game, calls create_new_user method
	{
	    create_new_user ();
	}

	else if (title_screen_selected == 1)//If they selected load game, calls load_user method
	{
	    load_user ();
	}
	return (title_screen_selected);// Returns what they selected so we know if need to diplay rules for first timers
    }


    public static void draw_box (int x, int y)//Draws the standard box behind the text
    {
	c.setColor (Color.black);
	c.drawRect ((x - 1), (y - 1), 251, 101);
	c.setColor (Color.gray);
	c.fillRect (x, y, 250, 100);
	c.setColor (Color.black);
    }


    public static void draw_selected_box (int x, int y)//Draws a selected box for behind the text
    {
	c.setColor (Color.black);
	c.drawRect ((x - 1), (y - 1), 251, 101);
	c.setColor (Color.lightGray);
	c.fillRect (x, y, 250, 100);
	c.setColor (Color.gray);
	c.fillRect ((x + 10), (y + 10), 230, 80);
	c.setColor (Color.black);
    }


    public static void load_user () throws IOException//Loads a previous user
    {
	String name_on_list;//Used to test if the name they entered is the same as one of the ones on the list
	String input_password;//The password entered by the player
	boolean illegal_pass;//Is true when an illegal chracter is input

	Color light_blue = new Color (0, 191, 255);
	c.setColor (light_blue);
	c.fillRect (0, 0, 640, 500);
	c.setTextBackgroundColor (light_blue);

	c.setColor (Color.black);
	Font title_screen = new Font ("Bauhaus 93", Font.ITALIC, 30);
	c.setFont (title_screen);

	while (1 == 1)///Infinite loop that will only broken out of if a used name is entered
	{
	    BufferedReader read_name_list = new BufferedReader (new FileReader ("name_list.txt")); //Opens the file in a way that can read Null
	    name_on_list = read_name_list.readLine ();

	    c.drawString ("Enter your player name", 135, 100);
	    c.setCursor (8, 33);
	    player_name = c.readLine ();

	    while (name_on_list != null) //Checks if there is nothing let to read
	    {
		if (name_on_list.equals (player_name)) //Checks if the name in the file is the same as the one entered
		{
		    break;
		}

		name_on_list = read_name_list.readLine (); //Gets new word on list
	    }

	    if (name_on_list == null)//If it goes through all the names on the list
	    {
		c.clear ();
		c.setCursor (7, 26);
		c.setTextColor (Color.red);
		c.println ("That username is not in use");
		c.setTextColor (Color.black);
	    }

	    else//Sets up for the password input and closes the file of saved usernames
	    {
		c.setCursor (7, 26);
		c.println ();
		read_name_list.close ();
		break;
	    }
	} //INFINTE WHILE

	TextInputFile player_save_data = new TextInputFile (player_name + ".txt");//Loads the players file
	password = player_save_data.readString ();//Gets the password

	if (!(password.equals ("|")))// IF the password is not saved as |. | is the password when a person has died, and it is not possible to enter | as passowrd
	{
	    while (1 == 1)//Infinite loop
	    {
		c.drawString ("Enter your password", 150, 300);//Prompts password entering

		do //TEST FOR ILLEGAL CHARACTERS
		{
		    c.setCursor (17, 35);
		    input_password = c.readLine ();
		    illegal_pass = false;

		    for (int i = 0 ; i < input_password.length () ; i++) //Used to make sure no illegal characters are entered
		    {
			if ((int) input_password.charAt (i) == 47 || (int) input_password.charAt (i) == 134 || input_password.charAt (i) == ':' || input_password.charAt (i) == '*' || input_password.charAt (i) == '?' || (int) input_password.charAt (i) == 34 || input_password.charAt (i) == '<' || input_password.charAt (i) == '>' || (int)input_password.charAt (i) == 124 || input_password.charAt (i) == ' ')// Checks for any illegal character
			{
			    illegal_pass = true;
			}
		    }

		    if (illegal_pass == true)// If the password is illegal
		    {
			c.setCursor (16, 20);
			c.setTextColor (Color.red);
			c.println ("That password contains illegal characters");
			c.setTextColor (Color.black);
			c.println ();
			input_password = "";
		    }

		}
		while (illegal_pass == true);//Continue to make them enter passwords that contain valid characters

		if (!(input_password.equals (password)))//If the password is not equal to what they enetered
		{
		    c.setCursor (16, 21);
		    c.setTextColor (Color.red);
		    c.println ("That was not the correct password");
		    c.setTextColor (Color.black);
		    c.println ();
		}

		else//Breaks only when eveything is correct
		{
		    break;
		}

	    } //END INFINITE WHILE 2

	    strength = player_save_data.readInt ();///////LOADS ALL OF THE DATA
	    defence = player_save_data.readInt ();
	    health = player_save_data.readInt ();
	    evasion = player_save_data.readDouble ();
	    level = player_save_data.readInt ();
	    xp = player_save_data.readInt ();
	    xp_to_next_level = player_save_data.readInt ();
	    godfree_fight = player_save_data.readBoolean ();

	    for (int i = 0 ; i < character_gear.length ; i++)//LOADS ALL THE CHRACTER GEAR
	    {
		character_gear [i] = player_save_data.readInt ();
	    }

	    player_save_data.close ();//CLOSES INPUT
	    proper_login = true;//TELLS IT WAS A PROPER LOGIN.
	}

	else if ((password.equals ("|")))// IF THE PASSWORD IS |, WOULDNT LET INTO THE BIG IF, IF IT WAS. PRINTS THAT THE PLAYER IS DEAD
	{
	    c.clear ();
	    c.setFont (title_screen);
	    c.setColor (Color.red);
	    c.drawString ("THIS PLAYER IS DEAD", 200, 200);
	    delay (1000);
	    proper_login = false;// TELLS IT WAS A BAD LOGIN
	}

    }


    public static void create_new_user () throws IOException//METHOD TO CREATE NEW USER
    {
	boolean name_taken = false;
	String name_on_list;
	boolean illegal_name = false;
	boolean illegal_pass = false;

	Color light_blue = new Color (0, 191, 255);
	c.setColor (light_blue);
	c.fillRect (0, 0, 640, 500);
	c.setTextBackgroundColor (light_blue);

	c.setColor (Color.black);
	Font title_screen = new Font ("Bauhaus 93", Font.ITALIC, 30);
	c.setFont (title_screen);

	PrintWriter input_name_list = new PrintWriter (new FileWriter ("name_list.txt", true));  //Starts writing at the end

	while (1 == 1)
	{
	    BufferedReader read_name_list = new BufferedReader (new FileReader ("name_list.txt")); //Opens the file in a way that can read Null

	    c.drawString ("Please enter a character name", 100, 100);  //Enters username
	    c.setCursor (8, 37);
	    player_name = c.readLine ();

	    name_on_list = read_name_list.readLine (); //takes the first name on the list

	    while (name_on_list != null) //Checks if there is nothing let to read
	    {
		if (name_on_list.equals (player_name)) //Checks if the name in the file is the same as the one entered
		{
		    break;
		}

		name_on_list = read_name_list.readLine (); //Gets new word on list
	    }

	    for (int i = 0 ; i < player_name.length () ; i++)
	    {
		if ((int) player_name.charAt (i) == 47 || (int) player_name.charAt (i) == 134 || player_name.charAt (i) == ':' || player_name.charAt (i) == '*' || player_name.charAt (i) == '?' || (int) player_name.charAt (i) == 34 || player_name.charAt (i) == '<' || player_name.charAt (i) == '>' || player_name.charAt (i) == '|')
		{
		    illegal_name = true;
		}
	    }

	    if (name_on_list != null) //If the name is taken, the name on the list will not be null
	    {
		c.clear ();
		c.setCursor (7, 28);
		c.setTextColor (Color.red);
		c.println ("That username is taken");
		c.setTextColor (Color.black);
	    }

	    else if (illegal_name == true)//IF THE NAME HAS ILLEGAL CHARACTERS
	    {
		c.clear ();
		c.setCursor (7, 20);
		c.setTextColor (Color.red);
		c.println ("That username contains illegal characters");
		c.setTextColor (Color.black);
		illegal_name = false;
	    }

	    else//IFE EVERYHTING IS OK TO CREATE A FILE
	    {
		input_name_list.println (player_name);//INPUTS THE NEW NAME INTO THE NAMES LIST FILE
		read_name_list.close ();//CLOSES NAMES LIST
		break;
	    }
	    read_name_list.close ();//IF THE WHILE HASNT BROKEN OUT, SINCE IT WILL TRY TO RE OPEN, CLOSE IT
	}

	c.setCursor (7, 1);
	c.println ();
	
	PrintWriter input_save_data = new PrintWriter (new FileWriter (player_name + ".txt"));//CREATES AN NEW FILE FOR THE PLAYER
	c.drawString ("Please enter your password", 120, 300);
	

	do
	{
	    c.setCursor (18, 35);
	    password = c.readLine ();
	    illegal_pass = false;

	    for (int i = 0 ; i < password.length () ; i++) //Used to make sure no illegal characters are entered
	    {
		if ((int) password.charAt (i) == 47 || (int) password.charAt (i) == 134 || password.charAt (i) == ':' || password.charAt (i) == '*' || password.charAt (i) == '?' || (int) password.charAt (i) == 34 || password.charAt (i) == '<' || password.charAt (i) == '>' || password.charAt (i) == '|' || password.charAt (i) == ' ')
		{
		    illegal_pass = true;
		}
	    }

	    if (illegal_pass == true)
	    {
		c.setCursor (17, 20);
		c.setTextColor (Color.red);
		c.println ("That password contains illegal characters");
		c.setTextColor (Color.black);
		c.println ();
		password = "";
	    }

	}
	while (illegal_pass == true);

	input_save_data.println (password);

	strength = (int) (Math.random () * (5) + 10); //Strength between 10 and 15
	defence = (int) (Math.random () * (2) + 5);    //Defence between 5 and 7
	health = (int) (Math.random () * (10) + 25);   //Heath between 25 and 35

	input_save_data.println (strength);
	input_save_data.println (defence);
	input_save_data.println (health);
	input_save_data.println (evasion);
	input_save_data.println (level);
	input_save_data.println (xp);
	input_save_data.println (xp_to_next_level);
	input_save_data.println (godfree_fight);

	for (int i = 0 ; i < character_gear.length ; i++)
	{
	    input_save_data.println (character_gear [i]);
	}

	input_save_data.close ();
	input_name_list.close ();
	proper_login = true;
    }


    public static int secondary_menu ()  //////////////////////////////////////////////SECONDARY MENU
    {
	int box_at = 0;
	char char_pressed;                                               //    00000   33333
	//    11111   44444
	//    22222   55555
	Color light_blue = new Color (0, 191, 255);
	c.setColor (light_blue);                  //Generates and sets Background Color
	c.fillRect (0, 0, 640, 500);

	draw_selected_box_2 (50, 150);  //0
	draw_box_2 (50, 270); //1
	draw_box_2 (50, 390); //2
	draw_box_2 (370, 150); //3
	draw_box_2 (370, 270); //4
	draw_box_2 (370, 390); //5

	Font big_letters = new Font ("MingLiU", Font.PLAIN, 50);
	c.setFont (big_letters);
	c.drawString ("Sword Art Online ARC", 60, 100);

	Font small_letters = new Font ("MingLiU", Font.PLAIN, 15);
	c.setFont (small_letters);
	c.drawString ("Use 'w', 's', 'a', and 'd', to navigate. Press enter to select, '=' to quit.", 55, 130);

	c.setColor (Color.black);
	Font title_screen = new Font ("Bauhaus 93", Font.ITALIC, 40);
	c.setFont (title_screen);

	c.drawString ("Battle", 100, 215); //0
	c.drawString ("Stats", 115, 335); //1
	c.drawString ("Cheats", 95, 450); //2
	c.drawString ("Inventory", 395, 215); //3
	c.drawString ("Rules", 430, 335); //4
	c.drawString ("Save", 440, 450); //5

	do
	{
	    char_pressed = c.getChar ();

	    if (char_pressed == 'w' || char_pressed == 'W')
	    {
		switch (box_at)
		{
		    case 0:
			{
			    box_at = 2;
			    draw_selected_box_2 (50, 390); //2
			    draw_box_2 (50, 150); //0
			    c.drawString ("Battle", 100, 215); //0
			    c.drawString ("Cheats", 95, 450); //2
			    break;
			}

		    case 1:
			{
			    box_at = 0;
			    draw_selected_box_2 (50, 150); //0
			    draw_box_2 (50, 270); //1
			    c.drawString ("Battle", 100, 215); //0
			    c.drawString ("Stats", 115, 335); //1
			    break;
			}

		    case 2:
			{
			    box_at = 1;
			    draw_selected_box_2 (50, 270); //1
			    draw_box_2 (50, 390); //2
			    c.drawString ("Stats", 115, 335); //1
			    c.drawString ("Cheats", 95, 450); //2
			    break;
			}

		    case 3:
			{
			    box_at = 5;
			    draw_selected_box_2 (370, 390); //5
			    draw_box_2 (370, 150); //3
			    c.drawString ("Inventory", 395, 215); //3
			    c.drawString ("Save", 440, 450); //5
			    break;
			}

		    case 4:
			{
			    box_at = 3;
			    draw_selected_box_2 (370, 150); //3
			    draw_box_2 (370, 270); //4
			    c.drawString ("Inventory", 395, 215); //3
			    c.drawString ("Rules", 430, 335); //4
			    break;
			}

		    case 5:
			{
			    box_at = 4;
			    draw_selected_box_2 (370, 270); //4
			    draw_box_2 (370, 390); //5
			    c.drawString ("Rules", 430, 335); //4
			    c.drawString ("Save", 440, 450); //5
			    break;
			}
		}
	    }

	    else if (char_pressed == 's' || char_pressed == 'S')
	    {
		switch (box_at)
		{
		    case 0:
			{
			    box_at = 1;
			    draw_selected_box_2 (50, 270); //1
			    draw_box_2 (50, 150); //0
			    c.drawString ("Battle", 100, 215); //0
			    c.drawString ("Stats", 115, 335); //1
			    break;
			}

		    case 1:
			{
			    box_at = 2;
			    draw_selected_box_2 (50, 390); //2
			    draw_box_2 (50, 270); //1
			    c.drawString ("Stats", 115, 335); //1
			    c.drawString ("Cheats", 95, 450); //2
			    break;
			}

		    case 2:
			{
			    box_at = 0;
			    draw_selected_box_2 (50, 150); //0
			    draw_box_2 (50, 390); //2
			    c.drawString ("Battle", 100, 215); //0
			    c.drawString ("Cheats", 95, 450); //2
			    break;
			}

		    case 3:
			{
			    box_at = 4;
			    draw_selected_box_2 (370, 270); //4
			    draw_box_2 (370, 150); //3
			    c.drawString ("Inventory", 395, 215); //3
			    c.drawString ("Rules", 430, 335); //4
			    break;
			}

		    case 4:
			{
			    box_at = 5;
			    draw_selected_box_2 (370, 390); //5
			    draw_box_2 (370, 270); //4
			    c.drawString ("Rules", 430, 335); //4
			    c.drawString ("Save", 440, 450); //5
			    break;
			}

		    case 5:
			{
			    box_at = 3;
			    draw_selected_box_2 (370, 150); //3
			    draw_box_2 (370, 390); //5
			    c.drawString ("Inventory", 395, 215); //3
			    c.drawString ("Save", 440, 450); //5
			    break;
			}
		}

	    }

	    else if (char_pressed == 'a' || char_pressed == 'A')
	    {
		switch (box_at)
		{
		    case 0:
			{
			    box_at = 3;
			    draw_selected_box_2 (370, 150); //3
			    draw_box_2 (50, 150); //0
			    c.drawString ("Battle", 100, 215); //0
			    c.drawString ("Inventory", 395, 215); //3
			    break;
			}

		    case 1:
			{
			    box_at = 4;
			    draw_selected_box_2 (370, 270); //4
			    draw_box_2 (50, 270); //1
			    c.drawString ("Stats", 115, 335); //1
			    c.drawString ("Rules", 430, 335); //4
			    break;
			}

		    case 2:
			{
			    box_at = 5;
			    draw_selected_box_2 (370, 390); //5
			    draw_box_2 (50, 390); //2
			    c.drawString ("Cheats", 95, 450); //2
			    c.drawString ("Save", 440, 450); //5
			    break;
			}

		    case 3:
			{
			    box_at = 0;
			    draw_selected_box_2 (50, 150); //0
			    draw_box_2 (370, 150); //3
			    c.drawString ("Battle", 100, 215); //0
			    c.drawString ("Inventory", 395, 215); //3
			    break;
			}

		    case 4:
			{
			    box_at = 1;
			    draw_selected_box_2 (50, 270); //1
			    draw_box_2 (370, 270); //4
			    c.drawString ("Stats", 115, 335); //1
			    c.drawString ("Rules", 430, 335); //4
			    break;
			}

		    case 5:
			{
			    box_at = 2;
			    draw_selected_box_2 (50, 390); //2
			    draw_box_2 (370, 390); //5
			    c.drawString ("Cheats", 95, 450); //2
			    c.drawString ("Save", 440, 450); //5
			    break;
			}
		}
	    }

	    else if (char_pressed == 'd' || char_pressed == 'D')
	    {
		switch (box_at)
		{
		    case 0:
			{
			    box_at = 3;
			    draw_selected_box_2 (370, 150); //3
			    draw_box_2 (50, 150); //0
			    c.drawString ("Battle", 100, 215); //0
			    c.drawString ("Inventory", 395, 215); //3
			    break;
			}

		    case 1:
			{
			    box_at = 4;
			    draw_selected_box_2 (370, 270); //4
			    draw_box_2 (50, 270); //1
			    c.drawString ("Stats", 115, 335); //1
			    c.drawString ("Rules", 430, 335); //4
			    break;
			}

		    case 2:
			{
			    box_at = 5;
			    draw_selected_box_2 (370, 390); //5
			    draw_box_2 (50, 390); //2
			    c.drawString ("Cheats", 95, 450); //2
			    c.drawString ("Save", 440, 450); //5
			    break;
			}

		    case 3:
			{
			    box_at = 0;
			    draw_selected_box_2 (50, 150); //0
			    draw_box_2 (370, 150); //3
			    c.drawString ("Battle", 100, 215); //0
			    c.drawString ("Inventory", 395, 215); //3
			    break;
			}

		    case 4:
			{
			    box_at = 1;
			    draw_selected_box_2 (50, 270); //1
			    draw_box_2 (370, 270); //4
			    c.drawString ("Stats", 115, 335); //1
			    c.drawString ("Rules", 430, 335); //4
			    break;
			}

		    case 5:
			{
			    box_at = 2;
			    draw_selected_box_2 (50, 390); //2
			    draw_box_2 (370, 390); //5
			    c.drawString ("Cheats", 95, 450); //2
			    c.drawString ("Save", 440, 450); //5
			    break;
			}
		}
	    }

	    else if (char_pressed == '=' || char_pressed == '+')
	    {
		exit = true;
		box_at = 42;
		break;
	    }

	}
	while (char_pressed != '\n');
	c.clear ();

	return (box_at);
    }


    public static void draw_box_2 (int x, int y)  //////////////DRAWS SMALL BASIC BOX
    {
	c.setColor (Color.black);
	c.drawRect ((x - 1), (y - 1), 221, 101);
	c.setColor (Color.gray);
	c.fillRect (x, y, 220, 100);
	c.setColor (Color.black);
    }


    public static void draw_selected_box_2 (int x, int y)  //////////DRAWS SMALL SELECTED BOX
    {
	c.setColor (Color.black);
	c.drawRect ((x - 1), (y - 1), 221, 101);
	c.setColor (Color.lightGray);
	c.fillRect (x, y, 220, 100);
	c.setColor (Color.gray);
	c.fillRect ((x + 10), (y + 10), 200, 80);
	c.setColor (Color.black);
    }


    public static boolean inventory_check ()
    {
	boolean is_it_saved = true;

	Color light_blue = new Color (0, 191, 255);

	Font largest_letters = new Font ("MingLiU", Font.BOLD, 30);
	
	Font letters_the_size_in_saving = new Font ("MingLiU", Font.BOLD, 100);

	Font title_screen = new Font ("Bauhaus 93", Font.ITALIC, 40);

	Font small_letters = new Font ("MingLiU", Font.PLAIN, 15);

	Font inventory_letters = new Font ("MingLiU", Font.PLAIN, 20);
	
	Font selected_inventory = new Font ("MingLiU", Font.BOLD, 20);

	char key_pressed;

	do
	{
	    c.setFont (largest_letters);
	    c.drawString ("Inventory", 220, 40);

	    c.setFont (small_letters);
	    c.drawString ("Press '=' to exit", 507, 15);
	    c.drawString ("Press 'E' to delete", 507, 30);
	    c.drawString ("Press 'R' to use", 507, 45);

	    c.setFont (inventory_letters);

	    int y = 100;

	    for (int i = 0 ; i < 10 ; i++)
	    {
		c.drawString ("" + gen_weapon_name (character_gear [i]), 180, y); //Prints weapon names
		y += 40;
	    }
	    
	    c.setFont (selected_inventory);
	    c.drawString ("Selected Weapon:", 0, 100);
	    c.drawString ("Selected Armour:", 0, 140);

	    int zeros_in_inventory = 0;

	    for (int i = 0 ; i < character_gear.length ; i++) ///tells how many zeros there are so you cant go to something that doesnt exist
	    {
		if (character_gear [i] == 0)
		{
		    zeros_in_inventory++;
		}
	    }

	    int selected = 0;
	    int p = 80; //Text box position

	    do
	    {
		draw_selection_box_no_fill (175, (p + (40 * selected)));
		key_pressed = c.getChar ();
		clear_selection_box (175, (p + (40 * selected)));

		if ( (key_pressed == 'w' || key_pressed == 'W') && selected == 0) //Cases of nothing happening
		{
		}

		else if ( (key_pressed == 's' || key_pressed == 'S') && selected == (9 - zeros_in_inventory))  //Cases of nothing happening && to catch the bottom
		{
		}

		else if (key_pressed == 'w' || key_pressed == 'W') //Move up
		{
		    selected--;
		}

		else if (key_pressed == 's' || key_pressed == 'S') //Move down-
		{
		    selected++;
		}

	    }
	    while (key_pressed != '=' && key_pressed != 'e' && key_pressed != 'r' && key_pressed != '+' && key_pressed != 'E' && key_pressed != 'R');

	    draw_selection_box_no_fill (145, (p + (40 * selected)));

	    if (key_pressed == 'e' || key_pressed == 'E') //Deleting
	    {
		if (selected != 0 && selected != 1) //YOU Cant delete weapons you are useing!
		{
		    c.clear ();
		    c.drawString ("Are you sure you want to delete this?", 200, 130);

		    draw_selected_box_2 (230, 220);
		    draw_box_2 (230, 350);

		    c.setFont (title_screen);
		    c.drawString ("Yes", 305, 285);
		    c.drawString ("No", 310, 415);

		    int location = 0;
		    char item_select_location;

		    do
		    {
			item_select_location = c.getChar ();

			if (location == 0)
			{
			    if (item_select_location == 'w' || item_select_location == 's' || item_select_location == 'W' || item_select_location == 'S')
			    {
				draw_box_2 (230, 220);
				draw_selected_box_2 (230, 350);
				location = 1;

			    }
			}

			else if (location == 1)
			{
			    if (item_select_location == 'w' || item_select_location == 's' || item_select_location == 'W' || item_select_location == 'S')
			    {
				draw_selected_box_2 (230, 220);
				draw_box_2 (230, 350);
				location = 0;
			    }
			}
			c.drawString ("Yes", 305, 285);
			c.drawString ("No", 310, 415);

		    }
		    while (item_select_location != '\n');

		    c.clear ();

		    if (location == 1) //If the say no go back
		    {
		    }

		    else//they say yes
		    {
			character_gear [selected] = 0;//Remove the item
			
			int last_item;
			
			for (last_item = character_gear.length - 1; last_item > -1; last_item--)//Finds last item on the list
			{
			    if (character_gear [last_item] != 0)
			    {
				break;
			    }
			}
			
			if (selected != 9 && last_item != 1)//nothing extra needed if there is only the last deleted or if there are only 2 items left.
			{
			    if (character_gear [selected + 1] != 0)//the next number after isnt 0.
			    {
				character_gear [selected] = character_gear [last_item];//Makes the deleted item the last item on the list
				character_gear [last_item] = 0;//makes the last item on the list blank
			    }
			}
			
			else//nothing happens if above if is true
			{
			    character_gear [selected] = 0;
			}
			
			is_it_saved = false;
		    }//end of player allows to delete
		}//end of the if you can delete
		
		else//You cant delete selected items
		{
		    c.clear ();
		    c.setFont (letters_the_size_in_saving);
		    c.setColor (Color.red);
		    c.drawString ("You can't", 100, 150);
		    c.drawString ("Delete this!", 0, 250);
		    c.setColor (Color.black);
		    delay (3000);
		}
	    }


	    else if (key_pressed == 'r' || key_pressed == 'R') //Making weapon used
	    {
		int temp_weapon_store = character_gear [selected];//Stores the selected item in a seperate variable so that is can be replaced
		
		if (character_gear [selected] > 0 && character_gear [selected] <= 17)//IF YOU A RELACING A WEAPON
		{
		    character_gear [selected] = character_gear [0];// puts the used to me active item into the selected's spot (WEAPON)
		    character_gear [0] = temp_weapon_store;//The selected item becomes the new active (WEAPON)
		}
		
		else//YOU ARE REPLACING AN ARMOUR
		{
		    character_gear [selected] = character_gear [1];// puts the used to me active item into the selected's spot (ARMOR)
		    character_gear [1] = temp_weapon_store;//The selected item becomes the new active (ARMOUR)
		}
		is_it_saved = false;//Needs to save
	    }//Making selected weapon used
	    
	    c.clear ();

	}while (key_pressed != '=' || key_pressed == '+');

	return (is_it_saved);
    }


    public static void rules ()/////////RULES
    {
	Color light_blue = new Color (0, 191, 255);
	c.setColor (light_blue);                  //Generates and sets Background Color
	c.fillRect (0, 0, 640, 500);
	c.setColor (Color.black);

	Font rules = new Font ("MingLiU", Font.PLAIN, 18);
	c.setFont (rules);
	c.drawString ("Here is your rule breifing " + player_name + ".", 1, 20);
	c.drawString ("The rules are pretty simple. In battle, you have 5 moves.", 1, 40);
	c.drawString ("They are: Slash, Stab, Parry, Block, and Flee.", 1, 60);
	c.drawString ("The amount of damage dealt depends on the attacker's strength, and", 1, 80);
	c.drawString ("the defender's defence. There is also a percentage chance to evade", 1, 100);
	c.drawString ("every turn. Parry counters slash, block counters stab. If a move is", 1, 120);
	c.drawString ("correctly countered, reduced damage will be dealt to the person", 1, 140);
	c.drawString ("whose move was countered takes half damage. When you try to flee,", 1, 160);
	c.drawString ("you have a 50% chance of getting away. No matter if you get away or not,", 1, 180);
	c.drawString ("you will take reduced damage.", 1, 200);

	c.drawString ("There is one more thing to think about during combat, weapons and", 1, 240);
	c.drawString ("armour. Each opponent will have a weapon and a suit of armour. You", 1, 260);
	c.drawString ("will have a chance to pick up their equipment at the end of a fight", 1, 280);
	c.drawString ("and use it for your own. The better your weapons, the stronger you are.", 1, 300);

	c.drawString ("Good luck in the field " + player_name + "!", 1, 340);
	c.drawString ("Press any key to continue.", 1, 380);
	c.getChar ();
	c.clear ();
    }


    public static void look_at_stats ()//LOOKING AT STATS
    {
	c.clear ();

	Font stats = new Font ("MingLiU", Font.PLAIN, 18);
	c.setFont (stats);

	NumberFormat d = new DecimalFormat ("0.00");
	
	c.drawString ("" + player_name, 200, 20);
	c.drawString ("Level: " + level, 1, 40);
	c.drawString ("Health: " + health, 1, 60);
	c.drawString ("Defence: " + defence, 1, 80);
	c.drawString ("Strength: " + strength, 1, 100);
	c.drawString ("Evade Rate: " + (d.format (evasion) ) + "%", 1, 120);
	c.drawString ("XP: " + xp, 1, 140);
	c.drawString ("XP to next level: " + (xp_to_next_level - xp), 1, 160);

	c.drawString ("Press any key to go back.", 150, 200);
	c.getChar ();

    }


    public static boolean cheat_page ()///USED FOR CHEATING, CODES ARE: CheaterLevel= and CheaterFinalBattle
    {
	String code;
	boolean save = false;

	c.clear ();

	Font stats = new Font ("MingLiU", Font.PLAIN, 18);
	c.setFont (stats);
	Font largest_letters = new Font ("MingLiU", Font.BOLD, 100);

	c.drawString ("Enter your cheat code", 150, 20);
	c.setCursor (5, 37);
	code = c.readString ();

	if (code.equals ("CheaterLevel="))
	{
	    int level_cheater;
	    int temp_level = level;

	    do
	    {
		c.clear ();
		c.drawString ("Enter the level that you want to make yourself, between 1, and 99", 0, 20);
		c.setCursor (5, 37);
		level_cheater = c.readInt ();
	    }
	    while (!(level_cheater > 0 && level_cheater < 99));

	    for (int i = 1 ; i < (level_cheater - temp_level + 1) ; i++)//Makes you 
	    {
		level += 1; //ADDS LEVEL
		xp_to_next_level += 100 + (40 * (level - 1)); //GENERATES NEW AMOUNT OF XP NEEDED
		strength += ((int) (Math.random () * (5)) + 5) * level;
		defence += ((int) (Math.random () * (4)) + 4) * level;
		health += ((int) (Math.random () * (2)) + 8) * level;
		evasion += 0.2;
	    } //FOR
	    save = false;

	    c.setFont (largest_letters);
	    c.setColor (Color.red);
	    c.drawString ("CHEATED", 50, 250);
	    c.setColor (Color.black);
	    delay (500);
	} //IF

	else if (code.equals ("CheaterFinalBattle"))//Sets to final level
	{
	    int temp_level = level;

	    for (int i = 1 ; i < (100 - temp_level + 1) ; i++)
	    {
		level += 1; //ADDS LEVEL
		xp_to_next_level += 100 + (40 * (level - 1)); //GENERATES NEW AMOUNT OF XP NEEDED
		strength += ((int) (Math.random () * (5)) + 5) * level;
		defence += ((int) (Math.random () * (4)) + 4) * level;
		health += ((int) (Math.random () * (2)) + 8) * level;
		evasion += 0.2;
	    } //FOR
	    godfree_fight = true;
	    save = false;

	    c.setFont (largest_letters);
	    c.setColor (Color.red);
	    c.drawString ("CHEATED", 50, 250);
	    c.setColor (Color.black);
	    delay (500);
	}

	else
	{
	    c.setFont (largest_letters);
	    c.setColor (Color.red);
	    c.drawString ("Not a Code", 50, 250);
	    c.setColor (Color.black);
	    delay (500);
	    save = true;
	}

	return (save);
    }


    public static void save_game () throws IOException////////SAVES THE GAME
    {
	PrintWriter input_save_data = new PrintWriter (new FileWriter (player_name + ".txt"));

	input_save_data.println (password);
	input_save_data.println (strength);
	input_save_data.println (defence);
	input_save_data.println (health);
	input_save_data.println (evasion);
	input_save_data.println (level);
	input_save_data.println (xp);
	input_save_data.println (xp_to_next_level);
	input_save_data.println (godfree_fight);

	for (int i = 0 ; i < character_gear.length ; i++)
	{
	    input_save_data.println (character_gear [i]);
	}

	input_save_data.close ();
    }


    public static void delay (int x)        ////////////////////////////DELAY
    {
	try
	{
	    Thread.currentThread ().sleep (x);
	}

	catch (Exception e)
	{
	}
    }


    public static void init_fight () throws IOException//STARTS THE FIGHT BY GENERATING THE MOBS STATS AND WEAPON, THEN AFTER THE FIGHTS DETEMINES IF YOU GET A DROP
    {                                                  //AND ASKS IF YOU WANT TO KEEP A DROP IF YOU GET ONE
	int mob_rarity;
	int mob_type = 42;
	int mobs_weapon = 0;
	int mobs_armour = 0;
	boolean winner = false;

	Color light_blue = new Color (0, 191, 255);

	Font largest_letters = new Font ("MingLiU", Font.BOLD, 20);
	c.setFont (largest_letters);

	Font small_letters = new Font ("MingLiU", Font.PLAIN, 15);
	Font inventory_letters = new Font ("MingLiU", Font.PLAIN, 20);

	mob_rarity = (int) (Math.random () * (99) + 1); //Generates a random mob rarity                   //17 Common, 12 uncommon, 7 rares, 4 Legendaries

	if (godfree_fight == true)
	{
	    mob_type = 43;
	    mobs_weapon = (int) (Math.random () * 17) + 1; //Generates 1 - 16
	    mobs_armour = (int) (Math.random () * 12) + 18; //Generates 18 - 29
	}

	else if (level >= 50)
	{
	    if ((mob_rarity >= 1) && (mob_rarity <= 50))   //50% Chance, generates common
	    {
		mob_type = (int) (Math.random () * (16) + 1); //Generates 1 - 17
		mobs_weapon = (int) (Math.random () * 7) + 1; //Generates 1 - 7
		mobs_armour = (int) (Math.random () * 5) + 18; //Generates 18 - 22

	    }

	    else if ((mob_rarity >= 51) && (mob_rarity <= 80))   //30% chance of uncommon
	    {
		mob_type = (int) (Math.random () * (11) + 18); //Generates 18 - 29
		mobs_weapon = (int) (Math.random () * 5) + 8; //Generates 8 - 12
		mobs_armour = (int) (Math.random () * 4) + 23; //Generates 23 - 26
	    }

	    else if ((mob_rarity >= 81) && (mob_rarity <= 95))   //15% chance of rare
	    {
		mob_type = (int) (Math.random () * (6) + 30); //Generates 30 - 36
		mobs_weapon = (int) (Math.random () * 3) + 13; //Generates 13 - 15
		mobs_armour = (int) (Math.random () * 1) + 27; //Generates 27 - 28
	    }

	    else if ((mob_rarity >= 96) && (mob_rarity <= 100))  //LEGENARY GENERATED 5%
	    {
		mob_type = (int) (Math.random () * (3) + 37); //Generates 37 - 40
		mobs_weapon = (int) (Math.random () * 2) + 16;
		mobs_armour = 29; // Gives 29
	    }
	} //if

	else if (level < 50)
	{
	    if ((mob_rarity >= 1) && (mob_rarity <= 53))   //53% Chance, generates common
	    {
		mob_type = (int) (Math.random () * (16) + 1); //Generates 1 - 17
		mobs_weapon = (int) (Math.random () * 7) + 1; //Generates 1 - 7
		mobs_armour = (int) (Math.random () * 5) + 18; //Generates 18 - 22
	    }

	    else if ((mob_rarity >= 54) && (mob_rarity <= 85))   //32% chance of uncommon
	    {
		mob_type = (int) (Math.random () * (11) + 18); //Generates 18 - 29
		mobs_weapon = (int) (Math.random () * 5) + 8; //Generates 8 - 12
		mobs_armour = (int) (Math.random () * 4) + 23; //Generates 23 - 26
	    }

	    else if ((mob_rarity >= 86) && (mob_rarity <= 100))   //15% chance of rare
	    {
		mob_type = (int) (Math.random () * (6) + 30); //Generates 30 - 36
		mobs_weapon = (int) (Math.random () * 3) + 13; //Generates 13 - 15
		mobs_armour = (int) (Math.random () * 1) + 27; //Generates 27 - 28
	    }

	} //ELSE IF

	mob_name = set_mob_name (mob_type);
	mob_stats = mob_stat_set (mob_type, level);

	mob_stats [1] += (add_weapon (mobs_armour) );
	mob_stats [2] += (add_weapon (mobs_weapon) );

	c.setTextBackgroundColor (Color.white);
	c.clear ();
	
	Font selected_inventory = new Font ("MingLiU", Font.BOLD, 20);

	winner = fight (); // Returns if player won the fight and gets a drop

	c.setTextBackgroundColor (light_blue);
	c.clear ();

	char item_select_location;
	int location = 0;

	c.setColor (Color.black);
	Font title_screen = new Font ("Bauhaus 93", Font.ITALIC, 40);

	if (winner == true) //If battle is won
	{
	    if (((int) (Math.random () * 100) + 1) <= 100)  // 40% chance of a drop
	    {
		if (((int) (Math.random () * 100) + 1) <= 70)  // 70% chance of a weapon
		{
		    c.drawString ("You got a drop! It is:", 200, 130);
		    c.drawString ("" + (gen_weapon_name (mobs_weapon)), 230, 150);
		    c.drawString ("Do you want to keep this?", 190, 170);

		    draw_selected_box_2 (230, 220);
		    draw_box_2 (230, 350);

		    c.setFont (title_screen);
		    c.drawString ("Yes", 305, 285);
		    c.drawString ("No", 310, 415);

		    do
		    {
			item_select_location = c.getChar ();

			if (location == 0)
			{
			    if (item_select_location == 'w' || item_select_location == 's' || item_select_location == 'W' || item_select_location == 'S')
			    {
				draw_box_2 (230, 220);
				draw_selected_box_2 (230, 350);
				location = 1;

			    }
			}

			else if (location == 1)
			{
			    if (item_select_location == 'w' || item_select_location == 's' || item_select_location == 'W' || item_select_location == 'S')
			    {
				draw_selected_box_2 (230, 220);
				draw_box_2 (230, 350);
				location = 0;
			    }
			}
			c.drawString ("Yes", 305, 285);
			c.drawString ("No", 310, 415);

		    }
		    while (item_select_location != '\n');

		    if (location == 0) ////if yes
		    {
			c.clear ();
			if (character_gear [9] == 0)
			{
			    int i;
			    for (i = 0 ; i < character_gear.length ; i++) //Finds the first open spot thats open because there is nothing in spot 9
			    {
				if (character_gear [i] == 0)
				{
				    break;
				} //if
			    } //for

			    character_gear [i] = mobs_weapon;

			    c.drawString ("Item added to inventory!", 100, 100);
			    delay (5000);
			} //if not full

			else //////////////////IF INVENTORY IS FULL
			{
			    c.setFont (largest_letters);
			    c.drawString ("Your inventory is full.", 195, 30);
			    c.drawString ("Please choose a item to delete.", 150, 50);

			    c.setFont (small_letters);
			    c.drawString ("Press '=' to cancel", 508, 15);
			    
			    c.setFont (selected_inventory);
			    c.drawString ("Selected Weapon:", 0, 100);
			    c.drawString ("Selected Armour:", 0, 140);

			    c.setFont (inventory_letters);

			    int y = 100;

			    for (int i = 0 ; i < 10 ; i++)
			    {
				c.drawString ("" + gen_weapon_name (character_gear [i]), 170, y);
				y += 40;
			    }

			    int deletion_selected = 0;
			    char key_pressed_delete;
			    int p = 80; //Text box position

			    do
			    {
				draw_selection_box_no_fill (145, (p + (40 * deletion_selected)));
				key_pressed_delete = c.getChar ();
				clear_selection_box (145, (p + (40 * deletion_selected)));

				if ((key_pressed_delete == 'w' || key_pressed_delete == 'W') && deletion_selected == 0 ) //Cases of nothing happening
				{
				}

				else if ( (key_pressed_delete == 's' || key_pressed_delete == 'S') && deletion_selected == 9) //Cases of nothing happening
				{
				}

				else if (key_pressed_delete == 'w' || key_pressed_delete == 'W')
				{
				    deletion_selected--;
				}

				else if (key_pressed_delete == 's' || key_pressed_delete == 'S')
				{
				    deletion_selected++;
				}

			    }
			    while (key_pressed_delete != '\n' && (key_pressed_delete != '=' || key_pressed_delete != '+' && (deletion_selected == 1 || deletion_selected == 0) ) );

			    draw_selection_box_no_fill (145, (p + (40 * deletion_selected)));

			    if (key_pressed_delete == '=' || key_pressed_delete != '+') //exiting
			    {
			    }

			    else if (key_pressed_delete == '\n')
			    {
				character_gear [deletion_selected] = mobs_weapon;
			    }
			}
		    } //yes

		}

		else // 30% chance of an armour
		{
		    c.drawString ("You got a drop! It is:", 200, 130);
		    c.drawString ("" + (gen_weapon_name (mobs_armour)), 230, 150);
		    c.drawString ("Do you want to keep this?", 190, 170);

		    draw_selected_box_2 (230, 220);
		    draw_box_2 (230, 350);

		    c.setFont (title_screen);
		    c.drawString ("Yes", 305, 285);
		    c.drawString ("No", 310, 415);

		    do
		    {
			item_select_location = c.getChar ();

			if (location == 0)
			{
			    if (item_select_location == 'w' || item_select_location == 's' || item_select_location == 'W' || item_select_location == 'S')
			    {
				draw_box_2 (230, 220);
				draw_selected_box_2 (230, 350);
				location = 1;

			    }
			}

			else if (location == 1)
			{
			    if (item_select_location == 'w' || item_select_location == 's' || item_select_location == 'W' || item_select_location == 'S')
			    {
				draw_selected_box_2 (230, 220);
				draw_box_2 (230, 350);
				location = 0;
			    }
			}
			c.drawString ("Yes", 305, 285);
			c.drawString ("No", 310, 415);

		    }
		    while (item_select_location != '\n');

		    if (location == 0)
		    {
			c.clear ();
			if (character_gear [9] == 0)
			{
			    int i;
			    for (i = 0 ; i < character_gear.length ; i++) //Finds the first open spot thats open because there is nothing in spot 9
			    {
				if (character_gear [i] == 0)
				{
				    break;
				} //if
			    } //for

			    character_gear [i] = mobs_armour;

			    c.drawString ("Item added to inventory!", 100, 100);
			    delay (3000);
			} //if not full

			else //////////////////IF INVENTORY IS FULL
			{
			    c.setFont (largest_letters);
			    c.drawString ("Your inventory is full.", 195, 30);
			    c.drawString ("Please choose a item to delete.", 150, 50);

			    c.setFont (small_letters);
			    c.drawString ("Press '=' to cancel", 508, 15);
			    
			    c.setFont (selected_inventory);
			    c.drawString ("Selected Weapon:", 0, 100);
			    c.drawString ("Selected Armour:", 0, 140);

			    c.setFont (inventory_letters);

			    int y = 100;

			    for (int i = 0 ; i < 10 ; i++)
			    {
				c.drawString ("" + gen_weapon_name (character_gear [i]), 170, y);
				y += 40;
			    }

			    int deletion_selected = 0;
			    char key_pressed_delete;
			    int p = 80; //Text box position

			    do
			    {
				draw_selection_box_no_fill (145, (p + (40 * deletion_selected)));
				key_pressed_delete = c.getChar ();
				clear_selection_box (145, (p + (40 * deletion_selected)));

				if ( (key_pressed_delete == 'w' || key_pressed_delete == 'W') && deletion_selected == 0) //Cases of nothing happening
				{
				}

				else if ( (key_pressed_delete == 's' || key_pressed_delete == 'S') && deletion_selected == 9) //Cases of nothing happening
				{
				}

				else if (key_pressed_delete == 'w' || key_pressed_delete == 'W')
				{
				    deletion_selected--;
				}

				else if (key_pressed_delete == 's' || key_pressed_delete == 'S')
				{
				    deletion_selected++;
				}

			    } while (key_pressed_delete != '\n' && (key_pressed_delete != '=' || key_pressed_delete == '+' && (deletion_selected == 1 || deletion_selected == 0) ) );

			    draw_selection_box_no_fill (145, (p + (40 * deletion_selected)));

			    if (key_pressed_delete == '=') //exiting
			    {
			    }

			    else if (key_pressed_delete == '\n')
			    {
				character_gear [deletion_selected] = mobs_armour;
			    }
			} //Inventory is full

		    } //yes

		} //armour

	    } //Get Drop IF

	    else //There is no drop
	    {
		c.drawString ("Sorry, there was no drop", 200, 230);
		c.drawString ("Press any key to continue", 195, 250);
		c.getChar ();
	    } // No Drop Else
	} //Battle winner
    } //method


    public static void draw_selection_box_no_fill (int x, int y)
    {
	c.setColor (Color.black);
	c.drawRect (x, y, 410, 30);
	c.drawRect ((x + 1), (y + 1), 408, 28);
	c.drawRect ((x + 2), (y + 2), 406, 26);
    }


    public static void clear_selection_box (int x, int y)
    {
	Color light_blue = new Color (0, 191, 255);
	c.setColor (light_blue);
	c.drawRect (x, y, 410, 30);
	c.drawRect ((x + 1), (y + 1), 408, 28);
	c.drawRect ((x + 2), (y + 2), 406, 26);
	c.setColor (Color.black);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                    //BATTLE//                                                                           //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static boolean fight () throws IOException
    {
	int player_choice;
	int mob_choice;
	int player_fight_health = health;                                                                ////////////////////////////
	int fight_strength = strength + (add_weapon (character_gear [0]));                               //PERFORM MODIFICATIONS TO//
	int fight_defence = defence + (add_weapon (character_gear [1]));                                 //      STATS HERE        //
	double fight_evasion = evasion;                                                                  ////////////////////////////
	int mob_health = (int) mob_stats [0];
	int mob_evade_roll;
	int player_evade_roll;
	int flee_work;
	boolean killer = false;
	
	////////////////////////////////////////////////
	double mob_health_healthbar = mob_stats [0];  //
	int mob_health_percent;                       //USED FOR
	double player_fight_healthbar = health;       //HEALTH BARS
	int player_health_percent;                    //
	////////////////////////////////////////////////
	
	Font standard_text = new Font ("MingLiU", Font.PLAIN, 20);//FONTS USED
	Font hp_letters = new Font ("MingLiU", Font.BOLD, 15);
	Font controls = new Font ("MingLiU", Font.PLAIN, 15);


	do
	{
	    draw_kirito (-2, 16);//Draws you
	    draw_enemy (70, 0);//Draws the enemy
	    c.setColor (Color.black); //After drawing enemy, it leaves the color "boots"
	    
	    c.setFont (controls);
	    c.drawString ("Use 'w', 's', 'a', 'd', and enter to choose.", 330, 370);
	    ////////////MOBS HEALTHBAR////////////
	    c.setFont (standard_text);
	    c.drawString ("" + mob_name, 1, 22);
	
	    c.setFont (hp_letters);
	    c.drawString (mob_health + "HP", 30, 44);
	
	    mob_health_percent = (int) (( mob_health / mob_health_healthbar) * 100);

	    c.fillRect (120, 34, mob_health_percent, 10);
	    c.drawRect (120, 34, 100, 10);
	    
	    //////////PLAYERS HEALTHBAR///////////
	    c.setFont (standard_text);
	    c.drawString ("" + player_name, 400, 330);
	
	    c.setFont (hp_letters);
	    c.drawString (player_fight_health + "HP", 429, 352);
	
	    player_health_percent = (int) (( player_fight_health / player_fight_healthbar) * 100);
	
	    c.fillRect (519, 342, player_health_percent, 10);
	    c.drawRect (519, 342, 100, 10);

	    player_choice = battle_selection ();// Get the players attack

	    if (player_choice == 5)
	    {
		flee_work = (int) (Math.random () * (99)) + 1;
		if ((flee_work >= 1) && (flee_work <= 50))
		{
		    //Player choice stays
		}

		else
		{
		    player_choice = 10;
		}

	    }

	    mob_choice = (int) (Math.random () * (4)) + 1;

	    mob_evade_roll = (int) (Math.random () * (99)) + 1;
	    player_evade_roll = (int) (Math.random () * (99)) + 1;

	    if ((player_choice != 5) && (player_choice != 10))  //Cant evade and flee, will cancel flee if evades
	    {
		if ((mob_evade_roll >= 1) && (mob_evade_roll <= mob_stats [3]))  //Test for Mob Evade
		{
		    if ((player_evade_roll >= 1) && (player_evade_roll <= fight_evasion))   //Test For Player Evade
		    {
			player_choice = 7;
		    }

		    else
		    {
			mob_choice = 6;
		    }
		}

		else if ((player_evade_roll >= 1) && (player_evade_roll <= fight_evasion))   //Test For Player Evade
		{
		    player_choice = 6;
		}
	    }

	    if ((mob_choice != 6) && (player_choice != 6) && (player_choice != 7) && (player_choice != 5))   //Makes sure no evasions or flee
	    {
		c.print ("The " + mob_name + " chose: ");
		if (mob_choice == 1)
		{
		    c.print ("Slash");
		}

		else if (mob_choice == 2)
		{
		    c.print ("Stab");
		}

		else if (mob_choice == 3)
		{
		    c.print ("Parry");
		}

		else if (mob_choice == 4)
		{
		    c.print ("Block");
		}
	    }

	    else if (player_choice != 5)
	    {
		if (mob_choice != 6) //If the player evaded
		{
		    c.print ("You evaded the enemy attack and hit them for reduced damage.");
		    //MOB TAKES REDUCED DAMAGE AND DEALS NONE
		    //////////////////////////////////////////////////////////////////////////////////////////////////
		    //                          //
		    if (mob_stats [1] >= (int) (fight_strength / 2))                    //                          //
		    { //                          //
			mob_health -= 1;                                                //                          //
		    } //   PLAYER DAMAGE TO MOB   //
		    //         REDUCED          //
		    else                                                                //                          //
		    { //                          //
			mob_health -= ((int) (fight_strength / 2)) - mob_stats [1];     //                          //
		    } //                          //
		    //////////////////////////////////////////////////////////////////////////////////////////////////
		}

		else if (player_choice != 6) //If the mob evaded
		{
		    c.print ("The " + mob_name + " evaded your attack and dealt reduced damage.");
		    //PLAYER TAKES REDUCED DAMAGE AND DEALS NONE
		    ////////////////////////////////////////////////////////////////////////////////////////////////////////
		    //                          //
		    if (fight_defence >= (int) (mob_stats [2] / 2))                           //                          //
		    { //                          //
			player_fight_health -= 1;                                             //                          //
		    } //   MOB DAMAGE TO PLAYER   //
		    //         REDUCED          //
		    else                                                                      //                          //
		    { //                          //
			player_fight_health -= ((int) (mob_stats [2] / 2)) - fight_defence;   //                          //
		    } //                          //
		    ////////////////////////////////////////////////////////////////////////////////////////////////////////
		}

		else // Both evade
		{
		    c.print ("Both you and the mob evaded eachothers attacks so nothing happens.");
		}
	    }

	    c.setCursor (24, 1);

	    if ((player_choice == 1 && mob_choice == 1) || (player_choice == 1 && mob_choice == 2) || (player_choice == 2 && mob_choice == 1) || (player_choice == 2 && mob_choice == 2))
	    {
		//BOTH DEAL FULL DAMAGE
		///////////////////////////////////////////////////////////////////////////////////
		if (mob_stats [1] >= fight_strength)                     //                      //
		{ //                      //
		    mob_health -= 1;                                     //                      //
		} //                      //
		// PLAYER DAMAGE TO MOB //         //ARRAY POINTS --- 0 = HP --- 1 = DEFENCE --- 2 = STRENGTH --- 3 = EVADE --- 4 = XP
		else                                                     //                      //
		{ //                      //
		    mob_health -= fight_strength - mob_stats [1];        //                      //
		} //                      //
		///////////////////////////////////////////////////////////////////////////////////
		//                      //
		if (fight_defence >= mob_stats [2])                      //                      //
		{ //                      //
		    player_fight_health -= 1;                            //                      //
		} // MOB DAMAGE TO PLAYER //
		//                      //
		else                                                     //                      //
		{ //                      //
		    player_fight_health -= mob_stats [2] - fight_defence; //                      //
		} //                      //
		///////////////////////////////////////////////////////////////////////////////////
		c.print ("Both " + mob_name + " and you take swings and deal full damage.");
	    }

	    else if ((player_choice == 3 && mob_choice == 3) || (player_choice == 3 && mob_choice == 4) || (player_choice == 4 && mob_choice == 3) || (player_choice == 4 && mob_choice == 4))
	    {
		c.print ("Both parties were prepared to counter, so no damage is dealt.");
	    }

	    else if ((player_choice == 3 && mob_choice == 2) || (player_choice == 4 && mob_choice == 1))
	    {
		//PLAYER TAKES FULL MOB TAKES NONE
		////////////////////////////////////////////////////////////////////////////////////
		//                      //
		if (fight_defence >= mob_stats [2])                       //                      //
		{ //                      //
		    player_fight_health -= 1;                             //                      //
		} // MOB DAMAGE TO PLAYER //
		//                      //
		else                                                      //                      //
		{ //                      //
		    player_fight_health -= mob_stats [2] - fight_defence; //                      //
		} //                      //
		////////////////////////////////////////////////////////////////////////////////////
		c.print ("Since you performed the wrong counter, you take full damage.");
	    }

	    else if ((player_choice == 1 && mob_choice == 4) || (player_choice == 2 && mob_choice == 3))
	    {
		//PLAYER DEALS FULL TAKES NONE
		//////////////////////////////////////////////////////////////////////////////////
		if (mob_stats [1] >= fight_strength)                    //                      //
		{ //                      //
		    mob_health -= 1;                                    //                      //
		} //                      //
		// PLAYER DAMAGE TO MOB //         //ARRAY POINTS --- 0 = HP --- 1 = DEFENCE --- 2 = STRENGTH --- 3 = EVADE --- 4 = XP
		else                                                    //                      //
		{ //                      //
		    mob_health -= fight_strength - mob_stats [1];       //                      //
		} //                      //
		//////////////////////////////////////////////////////////////////////////////////
		c.print ("Since " + mob_name + " performed the wrong counter, you deal full damage.");
	    }

	    else if ((player_choice == 1 && mob_choice == 3) || (player_choice == 2 && mob_choice == 4))
	    {
		//PLAYER TAKES REDUCED DAMAGE AND DEALS NONE
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		//                          //
		if (fight_defence >= (int) (mob_stats [2] / 2))                           //                          //
		{ //                          //
		    player_fight_health -= 1;                                             //                          //
		} //   MOB DAMAGE TO PLAYER   //
		//         REDUCED          //
		else                                                                      //                          //
		{ //                          //
		    player_fight_health -= ((int) (mob_stats [2] / 2)) - fight_defence;   //                          //
		} //                          //
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		c.print ("The " + mob_name + " properly countered your blow, so you take reduced damage.");
	    }

	    else if ((player_choice == 3 && mob_choice == 1) || (player_choice == 4 && mob_choice == 2))
	    {
		//MOB TAKES REDUCED DAMAGE AND DEALS NONE
		//////////////////////////////////////////////////////////////////////////////////////////////////
		//                          //
		if (mob_stats [1] >= (int) (fight_strength / 2))                    //                          //
		{ //                          //
		    mob_health -= 1;                                                //                          //
		} //   PLAYER DAMAGE TO MOB   //
		//         REDUCED          //
		else                                                                //                          //
		{ //                          //
		    mob_health -= ((int) (fight_strength / 2)) - mob_stats [1];     //                          //
		} //                          //
		//////////////////////////////////////////////////////////////////////////////////////////////////
		c.print ("You properly anticipated the enemy's attack and countered it, dealing reduced damage.");
	    }

	    else if (player_choice == 5 || player_choice == 10)
	    {
		//PLAYER TAKES REDUCED DAMAGE AND DEALS NONE
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		//                          //
		if (fight_defence >= (int) (mob_stats [2] / 2))                           //                          //
		{ //                          //
		    player_fight_health -= 1;                                             //                          //
		} //   MOB DAMAGE TO PLAYER   //
		//         REDUCED          //
		else                                                                      //                          //
		{ //                          //
		    player_fight_health -= ((int) (mob_stats [2] / 2)) - fight_defence;   //                          //
		} //                          //
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		if (player_choice == 5)
		{
		    c.print ("You flee.");
		}

		else if (player_choice == 10)
		{
		    c.print ("You fail to flee and you take reduced damage.");
		}
	    }

	    if ((player_fight_health > 0) && (mob_health > 0))
	    {
		c.setCursor (12, 35);
		c.print ("Press any key to continue.");
		c.getChar ();
	    }
	    c.clear ();
	    c.setCursor (1, 1);

	}
	while ((player_fight_health > 0) && (mob_health > 0) && (player_choice != 5));

	Color light_blue = new Color (0, 191, 255);
	c.setTextBackgroundColor (light_blue);
	c.clear ();

	Font largest_letters = new Font ("MingLiU", Font.BOLD, 20);
	c.setFont (largest_letters);

	if (player_fight_health > 0 && mob_health <= 0)
	{
	    c.drawString ("You swing your sword, hitting the enemy hard", 100, 100);
	    c.drawString ("enough so that it is killed!", 180, 122);
	    killer = true;
	}

	else if (player_fight_health <= 0 && mob_health > 0) //IF YOU DIE
	{
	    if (player_choice == 5 || player_choice == 10)
	    {
		c.drawString ("You died a coward, running away from your enemy!", 70, 100);
	    }

	    else
	    {
		c.drawString ("You have been defeated by the mighty " + mob_name + ".", 30, 100);
	    }

	    PrintWriter input_save_data = new PrintWriter (new FileWriter (player_name + ".txt"));

	    input_save_data.println ("|");
	    input_save_data.println ("");
	    input_save_data.println ("");
	    input_save_data.println ("");
	    input_save_data.println ("");
	    input_save_data.println ("");
	    input_save_data.println ("");
	    input_save_data.println ("");

	    input_save_data.close ();

	    c.drawString ("Press any key to exit.", 200, 122);
	    c.getChar ();
	    death = true;
	}

	else if (player_choice == 5) //IF YOU FLEE
	{
	    c.drawString ("You get away safely", 230, 100);
	    c.drawString ("Press any key to return", 200, 122);
	}

	else //SURVIVE WITH 1 HP
	{
	    c.drawString ("You have barley survived the fight, almost dieing on", 40, 100);
	    c.drawString ("the final swing. Be more careful next time.", 90, 122);
	    killer = true;
	}


	if (killer == true) //If you get the kill
	{
	    if ((level == 99) && (xp_to_next_level <= (xp + mob_stats [4])))    //if your next fight is godfree
	    {
		c.drawString ("The next mob you fight will be the final boss, Godfree.", 20, 144);
		godfree_fight = true;
	    }

	    else if (godfree_fight == true) ///////////GAME IS FINISHED, GODFREE HAS BEEN DEFEATED
	    {
		c.drawString ("Press any key to finish the game", 200, 144);
		finished_game = true;
	    }

	    else
	    {
		xp += mob_stats [4];
		c.drawString ("You have gained " + mob_stats [4] + " XP.", 200, 144);

		if (xp >= xp_to_next_level)
		{
		    level += 1; //ADDS LEVEL
		    xp = 0 + (xp - xp_to_next_level);
		    xp_to_next_level += 100 + (40 * (level - 1)); //GENERATES NEW AMOUNT OF XP NEEDED
		    strength += ((int) (Math.random () * (5)) + 5) * level;
		    defence += ((int) (Math.random () * (4)) + 4) * level;
		    health += ((int) (Math.random () * (2)) + 8) * level;
		    evasion += 0.2;
		    c.drawString ("You have leveled up to level " + level + "!", 150, 166);
		}
	    }

	    c.drawString ("Press any key to loot the mob.", 170, 210);
	    c.getChar ();
	}
	return (killer);
    }
    
    public static int battle_selection ()
    {
	char key_pressed;
	int box_at = 1;
	
	Font selection_text = new Font ("MingLiU", Font.PLAIN, 18);
	c.setFont (selection_text);
	
	c.drawString ("Slash    Stab", 20, 390);
	c.drawString ("Parry    Block    Flee", 20, 415);
	
	c.setCursor (22, 1);
	
	draw_battle_boxes (14, 372);//Slash
	
	do
	{
	    key_pressed = c.getChar ();
	    switch (box_at)
	    {
		case 1:{
			switch (key_pressed)
			{
			    case 'w':
			    case 'W':{
			    //Nothing happens
			    break;
			    }
			    case 's':
			    case 'S':{
			    draw_battle_boxes (14, 399);//Parry
			    clear_battle_boxes (14, 372);//Slash
			    box_at = 3;
			    break;
			    }
			    case 'a':
			    case 'A':{
			    //nothing happens
			    break;
			    }
			    case 'd':
			    case 'D':{
			    draw_battle_boxes (90, 372);//Stab
			    clear_battle_boxes (14, 372);//Slash
			    box_at = 2;
			    break;
			    }
			}//interior switch
			break;
		}//exterior case
		case 2:{
			switch (key_pressed)
			{
			    case 'w':
			    case 'W':{
			    //Nothing happens
			    break;
			    }
			    case 's':
			    case 'S':{
			    draw_battle_boxes (93, 399);//Block
			    clear_battle_boxes (90, 372);//Stab
			    box_at = 4;
			    break;
			    }
			    case 'a':
			    case 'A':{
			    draw_battle_boxes (14, 372);//Slash
			    clear_battle_boxes (90, 372);//Stab
			    box_at = 1;
			    break;
			    }
			    case 'd':
			    case 'D':{
			    //Nothing happens
			    break;
			    }
			}//interior switch
			break;
		}//exterior case
		case 3:{
			switch (key_pressed)
			{
			    case 'w':
			    case 'W':{
			    draw_battle_boxes (14, 372);//Slash
			    clear_battle_boxes (14, 399);//Parry
			    box_at = 1;
			    break;
			    }
			    case 's':
			    case 'S':{
			    //Nothing happens
			    break;
			    }
			    case 'a':
			    case 'A':{
			    //Nothing happens
			    break;
			    }
			    case 'd':
			    case 'D':{
			    draw_battle_boxes (93, 399);//Block
			    clear_battle_boxes (14, 399);//Parry
			    box_at = 4;
			    break;
			    }
			}//interior switch
			break;
		}//exterior case
		case 4:{
			switch (key_pressed)
			{
			    case 'w':
			    case 'W':{
			    draw_battle_boxes (90, 372);//Stab
			    clear_battle_boxes (93, 399);//Block
			    box_at = 2;
			    break;
			    }
			    case 's':
			    case 'S':{
			    //Nothing happens
			    break;
			    }
			    case 'a':
			    case 'A':{
			    draw_battle_boxes (14, 399);//Parry
			    clear_battle_boxes (93, 399);//Block
			    box_at = 3;
			    break;
			    }
			    case 'd':
			    case 'D':{
			    draw_battle_boxes (170, 399);//Flee
			    clear_battle_boxes (93, 399);//Block
			    box_at = 5;
			    break;
			    }
			}//interior switch
			break;
		}//exterior case
		case 5:{
			switch (key_pressed)
			{
			    case 'w':
			    case 'W':{
			    //Nothing happens
			    break;
			    }
			    case 's':
			    case 'S':{
			    //Nothing happens
			    break;
			    }
			    case 'a':
			    case 'A':{
			    draw_battle_boxes (93, 399);//Block
			    clear_battle_boxes (170, 399);//Flee
			    box_at = 4;
			    break;
			    }
			    case 'd':
			    case 'D':{
			    //Nothing happens
			    break;
			    }
			}//interior switch
			break;
		}//exterior case
	    }
	}while (key_pressed != '\n');
	return (box_at);
    }

    public static void draw_battle_boxes (int x, int y)
    {
	c.setColor (Color.black);
	c.drawRect (x, y, 60, 25);
    }
    
    public static void clear_battle_boxes (int x, int y)
    {
	c.setColor (Color.white);
	c.drawRect (x, y, 60, 25);
	c.setColor (Color.black);
    }

    public static String gen_weapon_name (int number)
    {
	String name = "Excalibur";

	switch (number)
	{
	    case 0:
		{
		    name = "";
		    break;
		}
	    case 1:
		{
		    name = "Standard Rapier";
		    break;
		}
	    case 2:
		{
		    name = "Standard Shortsword";
		    break;
		}
	    case 3:
		{
		    name = "Standard Claymore";
		    break;
		}
	    case 4:
		{
		    name = "Standard Longsword";
		    break;
		}
	    case 5:
		{
		    name = "Standard One-Handed Sword";
		    break;
		}
	    case 6:
		{
		    name = "Basic Kunai";
		    break;
		}
	    case 7:
		{
		    name = "Standard Samurai Sword";
		    break;
		}
	    case 8:
		{
		    name = "Greater Rapier";
		    break;
		}
	    case 9:
		{
		    name = "Greater One-Handed Sword";
		    break;
		}
	    case 10:
		{
		    name = "Greater Samurai Sword";
		    break;
		}
	    case 11:
		{
		    name = "Greater Claymore";
		    break;
		}
	    case 12:
		{
		    name = "Greater Longsword";
		    break;
		}
	    case 13:
		{
		    name = "Excellent Claymore of the Blood Oath";
		    break;
		}
	    case 14:
		{
		    name = "Samurai Sword of the Moonlight Cats";
		    break;
		}
	    case 15:
		{
		    name = "Lambent Light";
		    break;
		}
	    case 16:
		{
		    name = "Elucidator";
		    break;
		}
	    case 17:
		{
		    name = "Dark Repulser";
		    break;
		}
	    case 18:
		{
		    name = "Broken Chainmail";
		    break;
		}
	    case 19:
		{
		    name = "Villagers Clothes";
		    break;
		}
	    case 20:
		{
		    name = "Tatterd Leather Armour";
		    break;
		}
	    case 21:
		{
		    name = "Standard Chainmail";
		    break;
		}
	    case 22:
		{
		    name = "Standard Leather Armour";
		    break;
		}
	    case 23:
		{
		    name = "Reinforced Villagers Clothes";
		    break;
		}
	    case 24:
		{
		    name = "Strengthened Leather Armour";
		    break;
		}
	    case 25:
		{
		    name = "Strengthened Chainmail";
		    break;
		}
	    case 26:
		{
		    name = "Standrad Plate Mail";
		    break;
		}
	    case 27:
		{
		    name = "Taloned Plate Mail of the Moonlight Cats";
		    break;
		}
	    case 28:
		{
		    name = "Heavy Armour of the Blood Oath";
		    break;
		}
	    case 29:
		{
		    name = "The Blackwyrm Coat";
		    break;
		}
	}

	return (name);
    }


    public static int add_weapon (int number)
    {
	int addition = 0;

	switch (number)
	{
	    case 1:
		{
		    addition = 2 * level;
		    break;
		}
	    case 2:
		{
		    addition = 2 * level;
		    break;
		}
	    case 3:
		{
		    addition = 2 * level;
		    break;
		}
	    case 4:
		{
		    addition = 3 * level;
		    break;
		}
	    case 5:
		{
		    addition = 3 * level;
		    break;
		}
	    case 6:
		{
		    addition = 3 * level;
		    break;
		}
	    case 7:
		{
		    addition = 4 * level;
		    break;
		}
	    case 8:
		{
		    addition = 3 * level;
		    break;
		}
	    case 9:
		{
		    addition = 3 * level;
		    break;
		}
	    case 10:
		{
		    addition = 4 * level;
		    break;
		}
	    case 11:
		{
		    addition = 4 * level;
		    break;
		}
	    case 12:
		{
		    addition = 5 * level;
		    break;
		}
	    case 13:
		{
		    addition = 6 * level;
		    break;
		}
	    case 14:
		{
		    addition = 7 * level;
		    break;
		}
	    case 15:
		{
		    addition = 8 * level;
		    break;
		}
	    case 16:
		{
		    addition = 10 * level;
		    break;
		}
	    case 17:
		{
		    addition = 11 * level;
		    break;
		}
	    case 18:
		{
		    addition = 1 * level;
		    break;
		}
	    case 19:
		{
		    addition = 1 * level;
		    break;
		}
	    case 20:
		{
		    addition = 1 * level;
		    break;
		}
	    case 21:
		{
		    addition = 2 * level;
		    break;
		}
	    case 22:
		{
		    addition = 2 * level;
		    break;
		}
	    case 23:
		{
		    addition = 2 * level;
		    break;
		}
	    case 24:
		{
		    addition = 3 * level;
		    break;
		}
	    case 25:
		{
		    addition = 3 * level;
		    break;
		}
	    case 26:
		{
		    addition = 3 * level;
		    break;
		}
	    case 27:
		{
		    addition = 4 * level;
		    break;
		}
	    case 28:
		{
		    addition = 5 * level;
		    break;
		}
	    case 29:
		{
		    addition = 7 * level;
		    break;
		}
	}

	return (addition);
    }


    public static double[] mob_stat_set (int mob_type, int level)      //SETS A MOB'S STATS             ARRAY POINTS --- 0 = HP --- 1 = DEFENCE --- 2 = STRENGTH --- 3 = EVADE -- 4 = XP
    {
	double[] mob_stats = new double [5];

	switch (mob_type)
	{
	    case 1:
	    case 2:
	    case 3:
	    case 4:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 19); //Lower HP commons
		    if (level != 1) //These are to make sure you do not level up mob for level 1, continued throughtout method
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 5)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 2); //STANDARD DEF COMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 2)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 7);    //STANDARD STRENGTH
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (4) + 3)) * (i + 1);
			}
		    }

		    mob_stats [3] = 20; //STANDARD EVASION
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.2;
			}
		    }

		    mob_stats [4] = 20 * level;  //COMMON XP
		    break;
		}
	    case 5:
	    case 6:
	    case 7:
	    case 8:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 21);  //Standard HP Commons
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 6)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 1); //LOW DEF COMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 1)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 7);    //STANDARD STRENGTH
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (4) + 3)) * (i + 1);
			}
		    }

		    mob_stats [3] = 20; //STANDARD EVASION
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.2;
			}
		    }

		    mob_stats [4] = 20 * level;  //COMMON XP
		    break;
		}
	    case 9:
	    case 10:
	    case 11:
	    case 12:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 21);  //Standard HP Commons
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 6)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 2); //STANDARD DEF COMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 2)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 6);    //LOW STRENGTH COMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (4) + 2)) * (i + 1);
			}
		    }

		    mob_stats [3] = 20; //STANDARD EVASION
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.2;
			}
		    }

		    mob_stats [4] = 20 * level;  //COMMON XP
		    break;
		}
	    case 13:
	    case 14:
	    case 15:
	    case 16:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 21);  //Standard HP Commons
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 6)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 2); //STANDARD DEF COMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 2)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 7);    //STANDARD STRENGTH
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (4) + 3)) * (i + 1);
			}
		    }

		    mob_stats [3] = 18; //LOWERED EVASION
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.18;
			}
		    }

		    mob_stats [4] = 20 * level;  //COMMON XP
		    break;
		}
	    case 17:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 21);  //Standard HP Commons
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 6)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 2); //STANDARD DEF COMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 2)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 7);    //STANDARD STRENGTH
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (4) + 3)) * (i + 1);
			}
		    }

		    mob_stats [3] = 20; //STANDARD evasion Commons
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.2;
			}
		    }

		    mob_stats [4] = 20 * level;  //COMMON XP
		    break;
		}
		///////////////////////////////////////////////////UNCOMMONS///////////////////////////////////////////////////////////////////////////////////////////////
	    case 18:
	    case 19:
	    case 20:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 21);  //LOWER HP UNCOMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 6)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 4); //STANDARD DEF UNCOMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 3)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 9);    //STANDARD STRENGTH UNCOMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (4) + 4)) * (i + 1);
			}
		    }

		    mob_stats [3] = 25; //STANDARD evasion UNCOMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.2;
			}
		    }

		    mob_stats [4] = 30 * level;  //UNCOMMON XP
		    break;
		}

	    case 21:
	    case 22:
	    case 23:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 23); //Standard HP Uncommons
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 7)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 2); //LOWER DEF UNCOMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 2)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 9);    //STANDARD STRENGTH UNCOMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (4) + 4)) * (i + 1);
			}
		    }

		    mob_stats [3] = 25; //STANDARD evasion UNCOMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.2;
			}
		    }

		    mob_stats [4] = 30 * level;  //UNCOMMON XP
		    break;
		}
	    case 24:
	    case 25:
	    case 26:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 23); //Standard HP Uncommons
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 7)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 4); //STANDARD DEF UNCOMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 3)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 7);    //LOWER STRENGTH UNCOMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (4) + 3)) * (i + 1);
			}
		    }

		    mob_stats [3] = 25; //STANDARD evasion UNCOMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.2;
			}
		    }

		    mob_stats [4] = 30 * level;  //UNCOMMON XP
		    break;
		}
	    case 27:
	    case 28:
	    case 29:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 23); //Standard HP Uncommons
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 7)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 4); //STANDARD DEF UNCOMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 3)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 9);    //STANDARD STRENGTH UNCOMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (4) + 4)) * (i + 1);
			}
		    }

		    mob_stats [3] = 20; //LOWERED evasion UNCOMMONS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.18;
			}
		    }

		    mob_stats [4] = 30 * level;  //UNCOMMON XP
		    break;
		}
		////////////////////////////////////////////////////////RARES/////////////////////////////////////////////////////////////////////
	    case 30:
	    case 31:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 23); //Low HP Rares
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 7)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 5); //STANDARD DEF RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 2)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 10);    //STANDARD STRENGTH RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (5) + 5)) * (i + 1);
			}
		    }

		    mob_stats [3] = 30; //STANDARD evasion RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.2;
			}
		    }

		    mob_stats [4] = 40 * level;  //RARE XP
		    break;
		}

	    case 32:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 25); //STANDARD HP RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (4) + 2)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 4); //LOWERED DEF RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 3)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 10);    //STANDARD STRENGTH RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (5) + 5)) * (i + 1);
			}
		    }

		    mob_stats [3] = 30; //STANDARD evasion RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.2;
			}
		    }

		    mob_stats [4] = 40 * level;  //RARE XP
		    break;
		}
	    case 33:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 25); //STANDARD HP RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (4) + 2)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 5); //STANDARD DEF RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 2)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 9);    //LOWERED STRENGTH RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (4) + 4)) * (i + 1);
			}
		    }

		    mob_stats [3] = 30; //STANDARD evasion RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.2;
			}
		    }

		    mob_stats [4] = 40 * level;  //RARE XP
		    break;
		}
	    case 34:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 25); //STANDARD HP RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (4) + 2)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 5); //STANDARD DEF RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 2)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 10);    //STANDARD STRENGTH RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (5) + 5)) * (i + 1);
			}
		    }

		    mob_stats [3] = 25; //LOWERED evasion RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.18;
			}
		    }

		    mob_stats [4] = 40 * level;  //RARE XP
		    break;
		}
	    case 35:
	    case 36:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 25); //Regular HP Rares
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 8)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 5); //STANDARD DEF RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 2)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 10);    //STANDARD STRENGTH RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (5) + 5)) * (i + 1);
			}
		    }

		    mob_stats [3] = 30; //STANDARD evasion RARES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.2;
			}
		    }

		    mob_stats [4] = 40 * level;  //RARE XP
		    break;
		}
		/////////////////////////////////////////////////LEGENDARIES/////////////////////////////////////////////////////////////////////////
	    case 37:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 27); //Regular HP LEGENDARIES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 9)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 7); //STANDARD DEF LEGEND
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 5)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 12);    //STANDARD STRENGTH LEGENDARIES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (5) + 6)) * (i + 1);
			}
		    }

		    mob_stats [3] = 30; //LOWERED evasion LEGEND
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.18;
			}
		    }

		    mob_stats [4] = 60 * level;  //LEGEND XP
		    break;
		}
	    case 38:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 27); //Regular HP LEGENDARIES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 9)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 7); //STANDARD DEF LEGEND
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 5)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 10);    //LOWERED STRENGTH LEGENDARIES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (5) + 5)) * (i + 1);
			}
		    }

		    mob_stats [3] = 33; //STANDARD evasion LEGENDS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.2;
			}
		    }

		    mob_stats [4] = 60 * level;  //LEGEND XP
		    break;
		}
	    case 39:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 27); //Regular HP LEGENDARIES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 9)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 5); //LOWERED DEF LEGEND
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 2)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 12);    //STANDARD STRENGTH LEGENDARIES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (5) + 6)) * (i + 1);
			}
		    }

		    mob_stats [3] = 33; //STANDARD evasion LEGENDS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.2;
			}
		    }

		    mob_stats [4] = 60 * level;  //LEGEND XP
		    break;
		}

	    case 40:
		{
		    mob_stats [0] = (int) (Math.random () * (10) + 25); //LOW HP LEGEND
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [0] += ((int) (Math.random () * (2) + 8)) * (i + 1);
			}
		    }

		    mob_stats [1] = (int) (Math.random () * (2) + 7); //STANDARD DEF LEGEND
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [1] += ((int) (Math.random () * (4) + 5)) * (i + 1);
			}
		    }

		    mob_stats [2] = (int) (Math.random () * (5) + 12);    //STANDARD STRENGTH LEGENDARIES
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [2] += ((int) (Math.random () * (5) + 6)) * (i + 1);
			}
		    }

		    mob_stats [3] = 33; //STANDARD evasion LEGENDS
		    if (level != 1)
		    {
			for (int i = 1 ; i <= level - 1 ; i++)
			{
			    mob_stats [3] += 0.2;
			}
		    }

		    mob_stats [4] = 60 * level;  //LEGEND XP
		    break;
		}

	    case 42:
		{
		    mob_stats [0] = 1;
		    mob_stats [1] = 1;
		    mob_stats [2] = 1; //Used for if bugs come out, will not happen
		    mob_stats [3] = 100;
		    mob_stats [4] = 1000;
		}
	    case 43:
		{
		    mob_stats [0] = 62067;
		    mob_stats [1] = 34042;
		    mob_stats [2] = 32721; //Used for if bugs come out, will not happen
		    mob_stats [3] = 50;
		    mob_stats [4] = 1;
		}


	}

	return (mob_stats);
    }



    public static String set_mob_name (int mob_number)   //Uses if structures to determine the name based on a number generated in main
    {
	/////////////////////////COMMONS////////////////////////////////////////
	if (mob_number == 1)
	{
	    return ("Blazing Zealot");
	}

	else if (mob_number == 2)
	{
	    return ("Kobold Soldier");
	}

	else if (mob_number == 3)
	{
	    return ("Centaur Archer");
	}

	else if (mob_number == 4)
	{
	    return ("Raging Tusker");
	}

	else if (mob_number == 5)
	{
	    return ("Stable Water Elemental");
	}

	else if (mob_number == 6)
	{
	    return ("Stable Fire Elemental");
	}

	else if (mob_number == 7)
	{
	    return ("Kobold Archer");
	}

	else if (mob_number == 8)
	{
	    return ("Kobold Shaman");
	}

	else if (mob_number == 9)
	{
	    return ("Stable Shadow Elemental");
	}

	else if (mob_number == 10)
	{
	    return ("Stable Light Elemental");
	}

	else if (mob_number == 11)
	{
	    return ("Centaur Soldier");
	}

	else if (mob_number == 12)
	{
	    return ("Centaur Shaman");
	}

	else if (mob_number == 13)
	{
	    return ("Freezing Zealot");
	}

	else if (mob_number == 14)
	{
	    return ("Shadow Zealot");
	}

	else if (mob_number == 15)
	{
	    return ("Light Zealot");
	}

	else if (mob_number == 16)
	{
	    return ("Passive Tusker");
	}

	else if (mob_number == 17)
	{
	    return ("Vampiric Bat");
	}
	/////////////////////////UNCOMMONS//////////////////////////////////////////////
	else if (mob_number == 18)
	{
	    return ("Unstable Water Elemental");
	}

	else if (mob_number == 19)
	{
	    return ("Voidborn Bladesman");
	}

	else if (mob_number == 20)
	{
	    return ("Goblin Changling");
	}

	else if (mob_number == 21)
	{
	    return ("Orc Soldier");
	}

	else if (mob_number == 22)
	{
	    return ("Unstable Water Elemental");
	}

	else if (mob_number == 23)
	{
	    return ("Unstable Shadow Elemental");
	}

	else if (mob_number == 24)
	{
	    return ("Unstable Light Elemental");
	}

	else if (mob_number == 25)
	{
	    return ("Centaur War-Leader");
	}

	else if (mob_number == 26)
	{
	    return ("Kobold War-Leader");
	}

	else if (mob_number == 27)
	{
	    return ("Orc Archer");
	}

	else if (mob_number == 28)
	{
	    return ("Orc Shaman");
	}

	else if (mob_number == 29)
	{
	    return ("Vampiric Swarm");
	}

	//////////////////////////////RARES///////////////////////


	else if (mob_number == 30)
	{
	    return ("Orc War-Leader");
	}

	else if (mob_number == 31)
	{
	    return ("Enraged Giant");
	}

	else if (mob_number == 32)
	{
	    return ("Triton Assasin");
	}

	else if (mob_number == 33)
	{
	    return ("Desta-Ghoul");
	}

	else if (mob_number == 34)
	{
	    return ("Kaiser Frog");
	}

	else if (mob_number == 35)
	{
	    return ("Marg Don");
	}

	else if (mob_number == 36)
	{
	    return ("Air Manta");
	}

	////////////////////////LEGENDARY/////////////////////


	else if (mob_number == 37)
	{
	    return ("Gleam Eyes");
	}

	else if (mob_number == 38)
	{
	    return ("Crystal Dragon");
	}

	else if (mob_number == 39)           //Main bosses from ANIME (Godfree for level 100)
	{
	    return ("Skull Reaper");
	}

	else if (mob_number == 40)
	{
	    return ("Renegade, Nicholas");
	}
	else if (mob_number == 43)
	{
	    return ("Godfree");
	}
	return ("Bad Egg");
    }
    
    public static Color kirito_color;
    
    public static void draw_kirito (int x, int y)
    {
	Color skin = new Color (247, 218, 188);
	
	Color jacket_light = new Color (0, 82, 164);
	
	Color med_jacket = new Color (0, 50, 100);
	
	Color chest_piece = new Color (176, 176, 176);
	
	Color chest_outline = new Color (59, 59, 59);
	
	Color hair = new Color (37, 37, 46);
	
	Color sheath = new Color (80, 65, 52);
	
	Color sheath_outline = new Color (26, 20, 15);
	
	kirito_color = hair;
	
	three3 ( (x + 16), y);//ROW 1
	////////////////ROW 2//////////
	three3 ( (x + 11), (y + 1) );
	three3 ( (x + 12), (y + 1) );
	three3 ( (x + 13), (y + 1) );
	three3 ( (x + 14), (y + 1) );
	three3 ( (x + 15), (y + 1) );
	three3 ( (x + 16), (y + 1) );
	three3 ( (x + 17), (y + 1) );
	/////////////ROW 3///////////////
	three3 ( (x + 9), (y + 2) );
	three3 ( (x + 10), (y + 2) );
	three3 ( (x + 11), (y + 2) );
	three3 ( (x + 12), (y + 2) );
	three3 ( (x + 13), (y + 2) );
	three3 ( (x + 14), (y + 2) );
	three3 ( (x + 15), (y + 2) );
	three3 ( (x + 16), (y + 2) );
	three3 ( (x + 17), (y + 2) );
	three3 ( (x + 18), (y + 2) );
	//////////ROW 4///////////////
	three3 ( (x + 7), (y + 3) );
	three3 ( (x + 8), (y + 3) );
	three3 ( (x + 9), (y + 3) );
	three3 ( (x + 10), (y + 3) );
	three3 ( (x + 11), (y + 3) );
	three3 ( (x + 12), (y + 3) );
	three3 ( (x + 13), (y + 3) );
	three3 ( (x + 14), (y + 3) );
	three3 ( (x + 15), (y + 3) );
	three3 ( (x + 16), (y + 3) );
	three3 ( (x + 17), (y + 3) );
	three3 ( (x + 18), (y + 3) );
	three3 ( (x + 19), (y + 3) );
	/////////ROW 5/////////////////
	three3 ( (x + 8), (y + 4) );
	three3 ( (x + 9), (y + 4) );
	three3 ( (x + 10), (y + 4) );
	three3 ( (x + 11), (y + 4) );
	three3 ( (x + 12), (y + 4) );
	three3 ( (x + 13), (y + 4) );
	three3 ( (x + 14), (y + 4) );
	three3 ( (x + 15), (y + 4) );
	three3 ( (x + 16), (y + 4) );
	three3 ( (x + 17), (y + 4) );
	three3 ( (x + 18), (y + 4) );
	three3 ( (x + 19), (y + 4) );
	three3 ( (x + 20), (y + 4) );
	//////////ROW 6////////////////
	three3 ( (x + 8), (y + 5) );
	three3 ( (x + 9), (y + 5) );
	three3 ( (x + 10), (y + 5) );
	three3 ( (x + 11), (y + 5) );
	three3 ( (x + 12), (y + 5) );
	three3 ( (x + 13), (y + 5) );
	three3 ( (x + 14), (y + 5) );
	three3 ( (x + 15), (y + 5) );
	three3 ( (x + 16), (y + 5) );
	three3 ( (x + 17), (y + 5) );
	three3 ( (x + 18), (y + 5) );
	three3 ( (x + 19), (y + 5) );
	///////////ROW 7/////////////////
	three3 ( (x + 7), (y + 6) );
	three3 ( (x + 8), (y + 6) );
	three3 ( (x + 9), (y + 6) );
	three3 ( (x + 10), (y + 6) );
	three3 ( (x + 11), (y + 6) );
	three3 ( (x + 12), (y + 6) );
	three3 ( (x + 13), (y + 6) );
	three3 ( (x + 14), (y + 6) );
	three3 ( (x + 15), (y + 6) );
	three3 ( (x + 16), (y + 6) );
	three3 ( (x + 17), (y + 6) );
	three3 ( (x + 18), (y + 6) );
	three3 ( (x + 19), (y + 6) );
	/////////Row 8////////////////////
	three3 ( (x + 7), (y + 7) );
	three3 ( (x + 8), (y + 7) );
	three3 ( (x + 9), (y + 7) );
	three3 ( (x + 10), (y + 7) );
	three3 ( (x + 11), (y + 7) );
	three3 ( (x + 12), (y + 7) );
	three3 ( (x + 13), (y + 7) );
	three3 ( (x + 14), (y + 7) );
	three3 ( (x + 15), (y + 7) );
	three3 ( (x + 16), (y + 7) );
	three3 ( (x + 17), (y + 7) );
	three3 ( (x + 18), (y + 7) );
	three3 ( (x + 19), (y + 7) );
	three3 ( (x + 20), (y + 7) );
	////////////ROW 9//////////////
	three3 ( (x + 7), (y + 8) );
	three3 ( (x + 8), (y + 8) );
	three3 ( (x + 9), (y + 8) );
	three3 ( (x + 10), (y + 8) );
	three3 ( (x + 11), (y + 8) );
	three3 ( (x + 12), (y + 8) );
	three3 ( (x + 13), (y + 8) );
	kirito_color = skin;
	three3 ( (x + 14), (y + 8) );
	kirito_color = hair;
	three3 ( (x + 15), (y + 8) );
	three3 ( (x + 16), (y + 8) );
	three3 ( (x + 17), (y + 8) );
	three3 ( (x + 18), (y + 8) );
	three3 ( (x + 19), (y + 8) );
	///////////ROW 10/////////////
	three3 ( (x + 7), (y + 9) );
	three3 ( (x + 8), (y + 9) );
	three3 ( (x + 9), (y + 9) );
	kirito_color = skin;
	three3 ( (x + 10), (y + 9) );
	kirito_color = hair;
	three3 ( (x + 11), (y + 9) );
	kirito_color = Color.black;
	three3 ( (x + 12), (y + 9) );
	kirito_color = hair;
	three3 ( (x + 13), (y + 9) );
	kirito_color = skin;
	three3 ( (x + 14), (y + 9) );
	kirito_color = hair;
	three3 ( (x + 15), (y + 9) );
	three3 ( (x + 16), (y + 9) );
	three3 ( (x + 17), (y + 9) );
	kirito_color = Color.black;
	three3 ( (x + 18), (y + 9) );
	kirito_color = hair;
	/////////////ROW 11//////////
	three3 ( (x + 8), (y + 10) );
	three3 ( (x + 9), (y + 10) );
	kirito_color = skin;
	three3 ( (x + 10), (y + 10) );
	kirito_color = hair;
	three3 ( (x + 11), (y + 10) );
	kirito_color = Color.white;
	three3 ( (x + 12), (y + 10) );
	kirito_color = hair;
	three3 ( (x + 13), (y + 10) );
	kirito_color = Color.black;
	three3 ( (x + 14), (y + 10) );
	kirito_color = skin;
	three3 ( (x + 15), (y + 10) );
	kirito_color = hair;
	three3 ( (x + 16), (y + 10) );
	three3 ( (x + 17), (y + 10) );
	kirito_color = Color.black;
	three3 ( (x + 18), (y + 10) );
	kirito_color = hair;
	////////////ROW 12///////////
	three3 ( (x + 9), (y + 11) );
	kirito_color = Color.black;
	three3 ( (x + 10), (y + 11) );
	kirito_color = Color.white;
	three3 ( (x + 11), (y + 11) );
	three3 ( (x + 12), (y + 11) );
	kirito_color = hair;
	three3 ( (x + 13), (y + 11) );
	kirito_color = skin;
	three3 ( (x + 14), (y + 11) );
	three3 ( (x + 15), (y + 11) );
	three3 ( (x + 16), (y + 11) );
	kirito_color = hair;
	three3 ( (x + 17), (y + 11) );
	kirito_color = Color.black;
	three3 ( (x + 18), (y + 11) );
	kirito_color = Color.black;
	///////////ROW 13//////////////
	three3 ( (x + 3), (y + 12) );
	three3 ( (x + 4), (y + 12) );
	kirito_color = hair;
	three3 ( (x + 9), (y + 12) );
	kirito_color = Color.black;
	three3 ( (x + 10), (y + 12) );
	kirito_color = skin;
	three3 ( (x + 11), (y + 12) );
	three3 ( (x + 12), (y + 12) );
	three3 ( (x + 13), (y + 12) );
	three3 ( (x + 14), (y + 12) );
	three3 ( (x + 15), (y + 12) );
	three3 ( (x + 16), (y + 12) );
	three3 ( (x + 17), (y + 12) );
	kirito_color = Color.black;
	three3 ( (x + 18), (y + 12) );
	////////////ROW 14/////////////
	three3 ( (x + 3), (y + 13) );
	kirito_color = chest_piece;
	three3 ( (x + 4), (y + 13) );
	kirito_color = Color.black;
	three3 ( (x + 5), (y + 13) );
	kirito_color = hair;
	three3 ( (x + 10), (y + 13) );
	kirito_color = Color.black;
	three3 ( (x + 11), (y + 13) );
	kirito_color = skin;
	three3 ( (x + 12), (y + 13) );
	three3 ( (x + 13), (y + 13) );
	three3 ( (x + 14), (y + 13) );
	three3 ( (x + 15), (y + 13) );
	three3 ( (x + 16), (y + 13) );
	three3 ( (x + 17), (y + 13) );
	kirito_color = Color.black;
	three3 ( (x + 18), (y + 13) );
	///////////ROW 15///////////////
	three3 ( (x + 4), (y + 14) );
	kirito_color = chest_piece;
	three3 ( (x + 5), (y + 14) );
	kirito_color = Color.black;
	three3 ( (x + 6), (y + 14) );
	three3 ( (x + 8), (y + 14) );
	three3 ( (x + 9), (y + 14) );
	kirito_color = hair;
	three3 ( (x + 10), (y + 14) );
	kirito_color = Color.black;
	three3 ( (x + 11), (y + 14) );
	three3 ( (x + 12), (y + 14) );
	kirito_color = skin;
	three3 ( (x + 13), (y + 14) );
	three3 ( (x + 14), (y + 14) );
	three3 ( (x + 15), (y + 14) );
	three3 ( (x + 16), (y + 14) );
	kirito_color = Color.black;
	three3 ( (x + 17), (y + 14) );
	//////////ROW 16/////////////////
	three3 ( (x + 5), (y + 15) );
	kirito_color = chest_piece;
	three3 ( (x + 6), (y + 15) );
	kirito_color = Color.black;
	three3 ( (x + 7), (y + 15) );
	kirito_color = chest_piece;
	three3 ( (x + 8), (y + 15) );
	kirito_color = Color.black;
	three3 ( (x + 9), (y + 15) );
	three3 ( (x + 11), (y + 15) );
	kirito_color = skin;
	three3 ( (x + 12), (y + 15) );
	kirito_color = Color.black;
	three3 ( (x + 13), (y + 15) );
	kirito_color = skin;
	three3 ( (x + 14), (y + 15) );
	three3 ( (x + 15), (y + 15) );
	kirito_color = Color.black;
	three3 ( (x + 16), (y + 15) );
	/////////ROw 17////////////////
	three3 ( (x + 6), (y + 16) );
	kirito_color = chest_piece;
	three3 ( (x + 7), (y + 16) );
	three3 ( (x + 8), (y + 16) );
	kirito_color = Color.black;
	three3 ( (x + 9), (y + 16) );
	three3 ( (x + 10), (y + 16) );
	three3 ( (x + 11), (y + 16) );
	kirito_color = skin;
	three3 ( (x + 12), (y + 16) );
	three3 ( (x + 13), (y + 16) );
	kirito_color = Color.black;
	three3 ( (x + 14), (y + 16) );
	three3 ( (x + 15), (y + 16) );
	three3 ( (x + 16), (y + 16) );
	//////////////ROW 18///////////
	three3 ( (x + 5), (y + 17) );
	kirito_color = chest_piece;
	three3 ( (x + 6), (y + 17) );
	three3 ( (x + 7), (y + 17) );
	kirito_color = sheath_outline;
	three3 ( (x + 8), (y + 17) );
	three3 ( (x + 9), (y + 17) );
	kirito_color = med_jacket;
	three3 ( (x + 10), (y + 17) );
	kirito_color = skin;
	three3 ( (x + 11), (y + 17) );
	three3 ( (x + 12), (y + 17) );
	three3 ( (x + 13), (y + 17) );
	three3 ( (x + 14), (y + 17) );
	three3 ( (x + 15), (y + 17) );
	kirito_color = Color.black;
	three3 ( (x + 16), (y + 17) );
	three3 ( (x + 17), (y + 17) );
	three3 ( (x + 18), (y + 17) );
	three3 ( (x + 19), (y + 17) );
	///////////ROW 19/////////
	three3 ( (x + 5), (y + 18) );
	three3 ( (x + 6), (y + 18) );
	kirito_color = sheath_outline;
	three3 ( (x + 7), (y + 18) );
	kirito_color = sheath;
	three3 ( (x + 8), (y + 18) );
	three3 ( (x + 9), (y + 18) );
	kirito_color = sheath_outline;
	three3 ( (x + 10), (y + 18) );
	kirito_color = med_jacket;
	three3 ( (x + 11), (y + 18) );
	kirito_color = skin;
	three3 ( (x + 12), (y + 18) );
	three3 ( (x + 13), (y + 18) );
	three3 ( (x + 14), (y + 18) );
	three3 ( (x + 15), (y + 18) );
	kirito_color = Color.black;
	three3 ( (x + 16), (y + 18) );
	kirito_color = chest_outline;
	three3 ( (x + 17), (y + 18) );
	three3 ( (x + 18), (y + 18) );
	kirito_color = med_jacket;
	three3 ( (x + 19), (y + 18) );
	kirito_color = Color.black;
	three3 ( (x + 20), (y + 18) );
	////////////ROW 20/////////////
	three3 ( (x + 6), (y + 19) );
	kirito_color = med_jacket;
	three3 ( (x + 7), (y + 19) );
	kirito_color = sheath_outline;
	three3 ( (x + 8), (y + 19) );
	kirito_color = sheath;
	three3 ( (x + 9), (y + 19) );
	three3 ( (x + 10), (y + 19) );
	kirito_color = sheath_outline;
	three3 ( (x + 11), (y + 19) );
	kirito_color = med_jacket;
	three3 ( (x + 12), (y + 19) );
	kirito_color = skin;
	three3 ( (x + 13), (y + 19) );
	three3 ( (x + 14), (y + 19) );
	three3 ( (x + 15), (y + 19) );
	three3 ( (x + 16), (y + 19) );
	kirito_color = chest_outline;
	three3 ( (x + 17), (y + 19) );
	kirito_color = chest_piece;
	three3 ( (x + 18), (y + 19) );
	kirito_color = chest_outline;
	three3 ( (x + 19), (y + 19) );
	kirito_color = Color.black;
	three3 ( (x + 20), (y + 19) );
	/////////ROW 21////////////////
	three3 ( (x + 6), (y + 20) );
	kirito_color = jacket_light;
	three3 ( (x + 7), (y + 20) );
	kirito_color = med_jacket;
	three3 ( (x + 8), (y + 20) );
	kirito_color = sheath_outline;
	three3 ( (x + 9), (y + 20) );
	kirito_color = sheath;
	three3 ( (x + 10), (y + 20) );
	three3 ( (x + 11), (y + 20) );
	kirito_color = sheath_outline;
	three3 ( (x + 12), (y + 20) );
	kirito_color = med_jacket;
	three3 ( (x + 13), (y + 20) );
	three3 ( (x + 14), (y + 20) );
	three3 ( (x + 15), (y + 20) );
	three3 ( (x + 16), (y + 20) );
	kirito_color = chest_outline;
	three3 ( (x + 17), (y + 20) );
	kirito_color = chest_piece;
	three3 ( (x + 18), (y + 20) );
	kirito_color = chest_outline;
	three3 ( (x + 19), (y + 20) );
	kirito_color = med_jacket;
	three3 ( (x + 20), (y + 20) );
	kirito_color = Color.black;
	three3 ( (x + 21), (y + 20) );
	/////////ROW 22///////////////
	three3 ( (x + 6), (y + 21) );
	kirito_color = jacket_light;
	three3 ( (x + 7), (y + 21) );
	three3 ( (x + 8), (y + 21) );
	kirito_color = med_jacket;
	three3 ( (x + 9), (y + 21) );
	kirito_color = sheath_outline;
	three3 ( (x + 10), (y + 21) );
	kirito_color = sheath;
	three3 ( (x + 11), (y + 21) );
	three3 ( (x + 12), (y + 21) );
	kirito_color = sheath_outline;
	three3 ( (x + 13), (y + 21) );
	kirito_color = med_jacket;
	three3 ( (x + 14), (y + 21) );
	kirito_color = jacket_light;
	three3 ( (x + 15), (y + 21) );
	three3 ( (x + 16), (y + 21) );
	kirito_color = chest_outline;
	three3 ( (x + 17), (y + 21) );
	kirito_color = chest_piece;
	three3 ( (x + 18), (y + 21) );
	kirito_color = chest_outline;
	three3 ( (x + 19), (y + 21) );
	kirito_color = med_jacket;
	three3 ( (x + 20), (y + 21) );
	kirito_color = Color.black;
	three3 ( (x + 21), (y + 21) );
	/////////////ROW 23///////////
	three3 ( (x + 6), (y + 22) );
	kirito_color = jacket_light;
	three3 ( (x + 7), (y + 22) );
	three3 ( (x + 8), (y + 22) );
	kirito_color = med_jacket;
	three3 ( (x + 9), (y + 22) );
	kirito_color = Color.black;
	three3 ( (x + 10), (y + 22) );
	kirito_color = sheath_outline;
	three3 ( (x + 11), (y + 22) );
	kirito_color = sheath;
	three3 ( (x + 12), (y + 22) );
	three3 ( (x + 13), (y + 22) );
	kirito_color = sheath_outline;
	three3 ( (x + 14), (y + 22) );
	kirito_color = med_jacket;
	three3 ( (x + 15), (y + 22) );
	kirito_color = jacket_light;
	three3 ( (x + 16), (y + 22) );
	kirito_color = chest_outline;
	three3 ( (x + 17), (y + 22) );
	kirito_color = chest_piece;
	three3 ( (x + 18), (y + 22) );
	kirito_color = chest_outline;
	three3 ( (x + 19), (y + 22) );
	kirito_color = med_jacket;
	three3 ( (x + 20), (y + 22) );
	kirito_color = Color.black;
	three3 ( (x + 21), (y + 22) );
	
    }
    
    public static void three3 (int x, int y)
    {
	x = x * 9;
	y = y * 9;
	c.setColor (kirito_color);
	c.fillRect (x, y, 9, 9);
    }
    
    public static Color enemy_color;
    
    public static void draw_enemy (int x, int y)
    {
	Color hair = new Color (229, 196, 18);
	
	Color skin = new Color (220, 183, 138);
	
	Color brown = new Color (76, 42, 27);
	
	Color eyes = new Color (22, 18, 78);
	
	Color boots = new Color (171, 29, 7);
	
	Color hat = new Color (4, 156, 44);
	
	Color dark_green = new Color (2, 87, 24);
	
	enemy_color = Color.black;
	
	//HAT LEFT BLACK
	four4 ( (x + 7), y);
	four4 ( (x + 6), (y + 1) );
	four4 ( (x + 5), (y + 2) );
	four4 ( (x + 4), (y + 3) );
	four4 ( (x + 4), (y + 4) );
	four4 ( (x + 3), (y + 5) );
	
	//HAT RIGHT BLACK
	four4 ( (x + 12), y);
	four4 ( (x + 13), (y + 1) );
	four4 ( (x + 14), (y + 2) );
	four4 ( (x + 15), (y + 3) );
	four4 ( (x + 15), (y + 4) );
	four4 ( (x + 16), (y + 5) );
	//HAT
	//ROW 1
	enemy_color = hat;
	four4 ( (x + 8), y);
	four4 ( (x + 9), y);
	four4 ( (x + 10), y);
	four4 ( (x + 11), y);
	//ROW 2
	four4 ( (x + 7), (y + 1) );
	four4 ( (x + 8), (y + 1) );
	four4 ( (x + 9), (y + 1) );
	four4 ( (x + 10), (y + 1) );
	four4 ( (x + 11), (y + 1) );
	four4 ( (x + 12), (y + 1) );
	//ROW 3
	four4 ( (x + 6), (y + 2) );
	four4 ( (x + 7), (y + 2) );
	four4 ( (x + 8), (y + 2) );
	four4 ( (x + 9), (y + 2) );
	four4 ( (x + 10), (y + 2) );
	four4 ( (x + 11), (y + 2) );
	four4 ( (x + 12), (y + 2) );
	four4 ( (x + 13), (y + 2) );
	//ROW 4
	four4 ( (x + 5), (y + 3) );
	four4 ( (x + 6), (y + 3) );
	four4 ( (x + 7), (y + 3) );
	four4 ( (x + 8), (y + 3) );
	four4 ( (x + 9), (y + 3) );
	four4 ( (x + 10), (y + 3) );
	four4 ( (x + 11), (y + 3) );
	four4 ( (x + 12), (y + 3) );
	four4 ( (x + 13), (y + 3) );
	four4 ( (x + 14), (y + 3) );
	//ROW 5
	four4 ( (x + 5), (y + 4) );
	four4 ( (x + 14), (y + 4) );
	//ROW 6
	four4 ( (x + 4), (y + 5) );
	four4 ( (x + 15), (y + 5) );
	
	//BROWN
	enemy_color = brown;
	//ROW 1
	four4 ( (x + 6), (y + 4) );
	four4 ( (x + 13), (y + 4) );
	//ROW 2
	four4 ( (x + 5), (y + 5) );
	four4 ( (x + 14), (y + 5) );
	//Row 3
	four4 ( (x + 1), (y + 6) );
	four4 ( (x + 3), (y + 6) );
	four4 ( (x + 4), (y + 6) );
	four4 ( (x + 12), (y + 6) );
	four4 ( (x + 15), (y + 6) );
	four4 ( (x + 16), (y + 6) );
	four4 ( (x + 18), (y + 6) );
	//ROW 4
	four4 ( (x + 1), (y + 7) );
	four4 ( (x + 4), (y + 7) );
	four4 ( (x + 6), (y + 7) );
	four4 ( (x + 11), (y + 7) );
	four4 ( (x + 13), (y + 7) );
	four4 ( (x + 15), (y + 7) );
	four4 ( (x + 18), (y + 7) );
	//ROW 5
	four4 ( (x + 2), (y + 8) );
	four4 ( (x + 4), (y + 8) );
	four4 ( (x + 5), (y + 8) );
	four4 ( (x + 10), (y + 8) );
	four4 ( (x + 13), (y + 8) );
	four4 ( (x + 15), (y + 8) );
	four4 ( (x + 17), (y + 8) );
	//ROW 6
	four4 ( (x + 3), (y + 9) );
	four4 ( (x + 5), (y + 9) );
	four4 ( (x + 8), (y + 9) );
	four4 ( (x + 9), (y + 9) );
	four4 ( (x + 14), (y + 9) );
	four4 ( (x + 16), (y + 9) );
	//ROW 7
	four4 ( (x + 5), (y + 10) );
	four4 ( (x + 7), (y + 10) );
	four4 ( (x + 8), (y + 10) );
	four4 ( (x + 12), (y + 10) );
	four4 ( (x + 14), (y + 10) );
	//ROW 8
	four4 ( (x + 5), (y + 11) );
	four4 ( (x + 8), (y + 11) );
	four4 ( (x + 11), (y + 11) );
	four4 ( (x + 14), (y + 11) );
	//ROW 9
	four4 ( (x + 6), (y + 13) );
	four4 ( (x + 13), (y + 13) );
	//ROW 10
	four4 ( (x + 6), (y + 14) );
	four4 ( (x + 7), (y + 14) );
	four4 ( (x + 12), (y + 14) );
	four4 ( (x + 13), (y + 14) );
	//ROW 11
	four4 ( (x + 6), (y + 16) );
	four4 ( (x + 13), (y + 16) );
	//ROW 12
	four4 ( (x + 7), (y + 17) );
	four4 ( (x + 8), (y + 17) );
	four4 ( (x + 11), (y + 17) );
	four4 ( (x + 12), (y + 17) );
	//ROW 13
	four4 ( (x + 6), (y + 20) );
	four4 ( (x + 13), (y + 20) );
	//ROW 14
	four4 ( (x + 7), (y + 21) );
	four4 ( (x + 12), (y + 21) );
	
	//HAIR
	enemy_color = hair;
	//ROW 1
	four4 ( (x + 7), (y + 4) );
	four4 ( (x + 8), (y + 4) );
	four4 ( (x + 9), (y + 4) );
	four4 ( (x + 10), (y + 4) );
	four4 ( (x + 11), (y + 4) );
	four4 ( (x + 12), (y + 4) );
	
	//ROW 2
	four4 ( (x + 6), (y + 5) );
	four4 ( (x + 7), (y + 5) );
	four4 ( (x + 8), (y + 5) );
	four4 ( (x + 9), (y + 5) );
	four4 ( (x + 10), (y + 5) );
	four4 ( (x + 11), (y + 5) );
	four4 ( (x + 12), (y + 5) );
	four4 ( (x + 13), (y + 5) );
	
	//ROW 3
	four4 ( (x + 5), (y + 6) );
	four4 ( (x + 6), (y + 6) );
	four4 ( (x + 7), (y + 6) );
	four4 ( (x + 8), (y + 6) );
	four4 ( (x + 9), (y + 6) );
	four4 ( (x + 10), (y + 6) );
	four4 ( (x + 11), (y + 6) );
	four4 ( (x + 13), (y + 6) );
	four4 ( (x + 14), (y + 6) );
	
	//ROW 5
	four4 ( (x + 5), (y + 7) );
	four4 ( (x + 7), (y + 7) );
	four4 ( (x + 8), (y + 7) );
	four4 ( (x + 9), (y + 7) );
	four4 ( (x + 10), (y + 7) );
	four4 ( (x + 14), (y + 7) );
	
	//ROW 5
	four4 ( (x + 6), (y + 8) );
	four4 ( (x + 7), (y + 8) );
	four4 ( (x + 8), (y + 8) );
	four4 ( (x + 9), (y + 8) );
	four4 ( (x + 14), (y + 8) );
	
	//ROW 6
	four4 ( (x + 4), (y + 9) );
	four4 ( (x + 6), (y + 9) );
	four4 ( (x + 7), (y + 9) );
	four4 ( (x + 15), (y + 9) );
	//ROW 7
	four4 ( (x + 4), (y + 10) );
	four4 ( (x + 15), (y + 10) );
	//ROW 8
	four4 ( (x + 4), (y + 11) );
	four4 ( (x + 15), (y + 11) );
	//ROW 9
	four4 ( (x + 9), (y + 17) );
	four4 ( (x + 10), (y + 17) );
	//ROW 10
	four4 ( (x + 9), (y + 18) );
	four4 ( (x + 10), (y + 18) );
	
	//SKIN
	enemy_color = skin;
	//ROW 1
	four4 ( (x + 2), (y + 6) );
	four4 ( (x + 17), (y + 6) );
	//ROw 2
	four4 ( (x + 2), (y + 7) );
	four4 ( (x + 3), (y + 7) );
	four4 ( (x + 12), (y + 7) );
	four4 ( (x + 17), (y + 7) );
	four4 ( (x + 16), (y + 7) );
	//ROW 3
	four4 ( (x + 3), (y + 8) );
	four4 ( (x + 11), (y + 8) );
	four4 ( (x + 12), (y + 8) );
	four4 ( (x + 16), (y + 8) );
	//ROW 4
	four4 ( (x + 10), (y + 9) );
	four4 ( (x + 11), (y + 9) );
	four4 ( (x + 12), (y + 9) );
	four4 ( (x + 13), (y + 9) );
	//ROW 5
	four4 ( (x + 9), (y + 10) );
	four4 ( (x + 10), (y + 10) );
	four4 ( (x + 11), (y + 10) );
	//ROW 6
	four4 ( (x + 9), (y + 11) );
	four4 ( (x + 10), (y + 11) );
	//ROW 7
	four4 ( (x + 6), (y + 12) );
	four4 ( (x + 9), (y + 12) );
	four4 ( (x + 10), (y + 12) );
	four4 ( (x + 13), (y + 12) );
	//ROW 8
	four4 ( (x + 7), (y + 13) );
	four4 ( (x + 8), (y + 13) );
	four4 ( (x + 9), (y + 13) );
	four4 ( (x + 10), (y + 13) );
	four4 ( (x + 11), (y + 13) );
	four4 ( (x + 12), (y + 13) );
	//ROW 9
	four4 ( (x + 8), (y + 14) );
	four4 ( (x + 9), (y + 14) );
	four4 ( (x + 10), (y + 14) );
	four4 ( (x + 11), (y + 14) );
	//ROW 9
	four4 ( (x + 3), (y + 16) );
	four4 ( (x + 4), (y + 16) );
	four4 ( (x + 15), (y + 16) );
	four4 ( (x + 16), (y + 16) );
	//ROW 10
	four4 ( (x + 3), (y + 17) );
	four4 ( (x + 4), (y + 17) );
	four4 ( (x + 15), (y + 17) );
	four4 ( (x + 16), (y + 17) );
	
	//LOWER BLACK
	enemy_color = Color.black;
	//ROW 1
	four4 ( (x + 3), (y + 10) );
	four4 ( (x + 6), (y + 10) );
	four4 ( (x + 13), (y + 10) );
	four4 ( (x + 16), (y + 10) );
	//ROW 2
	four4 ( (x + 3), (y + 11) );
	four4 ( (x + 16), (y + 11) );
	//ROW 3
	four4 ( (x + 4), (y + 12) );
	four4 ( (x + 5), (y + 12) );
	four4 ( (x + 14), (y + 12) );
	four4 ( (x + 15), (y + 12) );
	//ROW 4
	four4 ( (x + 5), (y + 13) );
	four4 ( (x + 14), (y + 13) );
	//ROW 5
	four4 ( (x + 4), (y + 14) );
	four4 ( (x + 15), (y + 14) );
	//ROW 6
	four4 ( (x + 3), (y + 15) );
	four4 ( (x + 16), (y + 15) );
	//ROW 7
	four4 ( (x + 2), (y + 16) );
	four4 ( (x + 5), (y + 16) );
	four4 ( (x + 14), (y + 16) );
	four4 ( (x + 17), (y + 16) );
	//ROW 8
	four4 ( (x + 2), (y + 17) );
	four4 ( (x + 17), (y + 17) );
	//ROW 9
	four4 ( (x + 3), (y + 18) );
	four4 ( (x + 4), (y + 18) );
	four4 ( (x + 5), (y + 18) );
	four4 ( (x + 14), (y + 18) );
	four4 ( (x + 15), (y + 18) );
	four4 ( (x + 16), (y + 18) );
	//ROW 10
	four4 ( (x + 5), (y + 19) );
	four4 ( (x + 6), (y + 19) );
	four4 ( (x + 14), (y + 19) );
	four4 ( (x + 13), (y + 19) );
	//ROW 12
	four4 ( (x + 4), (y + 20) );
	four4 ( (x + 7), (y + 20) );
	four4 ( (x + 8), (y + 20) );
	four4 ( (x + 9), (y + 20) );
	four4 ( (x + 10), (y + 20) );
	four4 ( (x + 11), (y + 20) );
	four4 ( (x + 12), (y + 20) );
	four4 ( (x + 15), (y + 20) );
	//ROW 13
	four4 ( (x + 4), (y + 21) );
	four4 ( (x + 8), (y + 21) );
	four4 ( (x + 11), (y + 21) );
	four4 ( (x + 15), (y + 21) );
	//ROW 14
	four4 ( (x + 5), (y + 22) );
	four4 ( (x + 6), (y + 22) );
	four4 ( (x + 7), (y + 22) );
	four4 ( (x + 12), (y + 22) );
	four4 ( (x + 13), (y + 22) );
	four4 ( (x + 14), (y + 22) );
	
	//EYES AND OTHER BLUE
	enemy_color = eyes;
	//ROW 1
	four4 ( (x + 7), (y + 11) );
	four4 ( (x + 12), (y + 11) );
	//ROW 2
	four4 ( (x + 7), (y + 12) );
	four4 ( (x + 12), (y + 12) );
	//ROW 4
	four4 ( (x + 6), (y + 15) );
	four4 ( (x + 13), (y + 15) );
	//ROW 5
	four4 ( (x + 5), (y + 17) );
	four4 ( (x + 14), (y + 17) );
	
	//TUNIC LIGHT
	enemy_color = hat;
	//ROW 1
	four4 ( (x + 5), (y + 14) );
	four4 ( (x + 14), (y + 14) );
	//ROW 2
	four4 ( (x + 4), (y + 15) );
	four4 ( (x + 5), (y + 15) );
	four4 ( (x + 14), (y + 15) );
	four4 ( (x + 15), (y + 15) );
	//ROW 3
	four4 ( (x + 6), (y + 17) );
	four4 ( (x + 13), (y + 17) );
	//ROW 4
	four4 ( (x + 6), (y + 18) );
	four4 ( (x + 7), (y + 18) );
	four4 ( (x + 12), (y + 18) );
	four4 ( (x + 13), (y + 18) );
	//ROW 5
	four4 ( (x + 8), (y + 19) );
	four4 ( (x + 9), (y + 19) );
	four4 ( (x + 10), (y + 19) );
	four4 ( (x + 11), (y + 19) );
	
	//DARK TUNIC
	enemy_color = dark_green;
	//ROW 1
	four4 ( (x + 7), (y + 15) );
	four4 ( (x + 8), (y + 15) );
	four4 ( (x + 9), (y + 15) );
	four4 ( (x + 10), (y + 15) );
	four4 ( (x + 11), (y + 15) );
	four4 ( (x + 12), (y + 15) );
	//ROW 2
	four4 ( (x + 7), (y + 16) );
	four4 ( (x + 8), (y + 16) );
	four4 ( (x + 9), (y + 16) );
	four4 ( (x + 10), (y + 16) );
	four4 ( (x + 11), (y + 16) );
	four4 ( (x + 12), (y + 16) );
	//ROW 3
	four4 ( (x + 8), (y + 18) );
	four4 ( (x + 11), (y + 18) );
	//ROW 4
	four4 ( (x + 7), (y + 19) );
	four4 ( (x + 12), (y + 19) );
	
	//BOOTS RED
	enemy_color = boots;
	//ROW 1
	four4 ( (x + 5), (y + 20) );
	four4 ( (x + 14), (y + 20) );
	//ROW 2
	four4 ( (x + 5), (y + 21) );
	four4 ( (x + 6), (y + 21) );
	four4 ( (x + 13), (y + 21) );
	four4 ( (x + 14), (y + 21) );
	
    }
    
    public static void four4 (int x, int y)
    {
	x = x * 7;
	y = y * 7;
	c.setColor (enemy_color);
	c.fillRect (x, y, 7, 7);
    }
} // SAO_ARC class


