package game;

import java.util.ArrayList;

import gui.MonsterBattleGUI;

/**
 * Game - YOUR monster battle game!
 * 
 * Build your game here. Look at GameDemo.java for examples.
 * 
 * Steps:
 * 1. Fill in setupGame() - create monsters, items, set health
 * 2. Fill in the action methods - what happens when player acts?
 * 3. Customize the game loop if you want
 * 4. Add your own helper methods
 * 
 * Run this file to play YOUR game
 */
public class Game {

    // The GUI (I had AI build most of this)
    private MonsterBattleGUI gui;

    // Game state - YOU manage these
    private ArrayList<Monster> monsters;
    private ArrayList<Item> inventory;
    private int playerHealth;
    private int playerSpeed;
    private int playerDamage;
    private int playerHeal;
    private int playerShield;
    private Monster lastAttacked;// store the monster we last attacked so it can attack back
    private double shelidPower = 0.0;

    /**
     * Main method - start YOUR game!
     */
    public static void main(String[] args) {
        Game game = new Game(); // it instantiates a copy of this file. We're not running static
        game.play(); // this extra step is unnecessary AI stuff
    }

    /**
     * Play the game!
     */
    public void play() {
        setupGame();
        gameLoop();
    }

    /**
     * Setup - create the GUI and initial game state
     * 
     * TODO: Customize this! How many monsters? What items? How much health?
     */
    private void setupGame() {
        // Create the GUI
        gui = new MonsterBattleGUI("Monster Battle - Sarahs GAME");

        // CHOOSE DIFFICULTY (number of monsters to face)
        int numMonsters = chooseDifficulty();
        monsters = new ArrayList<>();

        for (int k = 0; k < numMonsters; k++) {
            // TODO: switch to random
            int r = (int)(Math.random() * 3);
            if (r == 20) { // Should we add special abbilities
                // add a monster with a special ability
                monsters.add(new Monster("Vampire"));
            }else if (r==10) {
                
                monsters.add(new Monster("Reflector"));
            }else if (r==2){
                //FIX
                monsters.add(new Monster("Fear 5th"));
            }else{
                monsters.add(new Monster());
           } 
         }
        //}
        gui.updateMonsters(monsters);

        // PICK YOUR CHARACTER BUILD (using the 4 action buttons!)
        pickCharacterBuild();

        // TODO: Create starting items
        inventory = new ArrayList<>();
       // addHealthPotion(:30)
        // Add items here! Look at GameDemo.java for examples
        gui.updateInventory(inventory);

        String[] buttons = { "Attack (" + playerDamage + ")",
                "Defend (" + playerShield + ")",
                "Heal(" + playerHeal + ")",
                "Use Item" };
        gui.setActionButtons(buttons);

        // Welcome message
        gui.displayMessage("Battle Start! Make your move"); 
   
        }
    /**
     * Main game loop
     * 
     * This controls the flow: player turn → monster turn → check game over
     * You can modify this if you want!
     */
    private void gameLoop() {
        // Keep playing while monsters alive and player alive
        while (countLivingMonsters() > 0 && playerHealth > 0) {
            shelidPower = 0; // start of turn lower turn

            // PLAYER'S TURN
            gui.displayMessage("Your turn! HP: " + playerHealth);
            int action = gui.waitForAction(); // Wait for button click (0-3)
            handlePlayerAction(action);
            gui.updateMonsters(monsters);
            gui.pause(500);

            // MONSTER'S TURN (if any alive and player alive)
            if (countLivingMonsters() > 0 && playerHealth > 0) {
                monsterAttack();
                gui.updateMonsters(monsters);
                gui.pause(500);
            }
        }

        // Game over!
        if (playerHealth <= 0) {
            gui.displayMessage("💀 DEFEAT! You have been defeated...");
        } else {
            gui.displayMessage("🎉 VICTORY! You defeated all monsters!");
        }
    }

    /**
     * Let player choose difficulty (number of monsters) using the 4 buttons
     * This demonstrates using the GUI for menu choices!
     */
    private int chooseDifficulty() {
        // Set button labels to difficulty levels
        String[] difficulties = { "Easy (3-4)", "Medium (4-5)", "Hard (6-8)", "Extreme (10-15)" };
        gui.setActionButtons(difficulties);

        // Display choice prompt
        gui.displayMessage("---- CHOOSE DIFFICULTY ----");

        // Wait for player to click a button (0-3)
        int choice = gui.waitForAction();
        int numMonsters = 0;
        switch (choice) {
            case 0: // 3-4 (math.random * range+1) + min
                numMonsters = (int) (Math.random() * 2) + 3;
                break;
            case 1: // 4-5
                numMonsters = (int) (Math.random() * (2) + 4);
                break;
            case 2: // 6-8
                numMonsters = (int) (Math.random() * (3) + 6);
                break;
            case 3: // 10-15
                numMonsters = (int) (Math.random() * (6) + 10);
                break;
            default:
                break;
        }

        gui.displayMessage( "You will face " + numMonsters + " monsters! Good Luck");
        gui.pause(1500);

        return numMonsters;
    }

    /**
     * Handle player's action choice
     * 
     * TODO: What happens for each action?
     */
    private void handlePlayerAction(int action) {
        switch (action) {
            case 0: // Attack button
                attackMonster();
                break;
            case 1: // Defend button
                defend();
                break;
            case 2: // Heal button
                heal();
                break;
            case 3: // Use Item button
                specialAbility();
                break;
        }
    }

    /**
     * Attack a monster
     * 
     * TODO: How does attacking work in your game?
     * - How much damage?
     * - Which monster gets hit?
     * - Special effects?
     */
    private void attackMonster() {
        // TODO: Target more intelligently
        Monster target = getRandomLivingMonster();
        lastAttacked = target;
        int damage = (int) (Math.random() * playerDamage + 1); // 0 - playerDamage
        if (damage == 0) {
            // hurt yourself
            playerHealth -= 5;
            gui.displayMessage("Critical fail! You hit yourself for 5 points");
            gui.updatePlayerHealth(playerHealth);
        } else if (damage == playerDamage) {
            gui.displayMessage("Critical hit! You slayed the monster");
            target.takeDamage(target.health());
        } else {
            target.takeDamage(damage);
            gui.displayMessage("You hit the monster for " + damage + " damage");
        }
        // Show which one we hit
        int index = monsters.indexOf(target);
        gui.highlightMonster(index);
        gui.pause(300);
        gui.highlightMonster(-1);
        // update the list
        gui.updateMonsters(monsters);
    }

    /**
     * Defend
     * 
     * TODO: What does defending do?
     * - Reduce damage?
     * - Block next attack?
     * - Something else?
     */
    private void defend() {
        // TODO: Implement your defend!
        shelidPower = playerShield;

        gui.displayMessage("TODO: Implement defend!");
    }

    /**
     * Heal yourself
     * 
     * 
     */
    private void heal() {
        // TODO: Implement your heal!
        playerHealth += playerHeal;
        gui.updatePlayerHealth(playerHealth);

        gui.displayMessage("You healed" + playerHeal + " health");
    }

    /**
     * Use an item from inventory
     */
    private void specialAbility() {
        if (inventory.isEmpty()) {
            gui.displayMessage("No items in inventory!");
            return;
        }

        // Use first item
        Item item = inventory.remove(0);
        gui.updateInventory(inventory);
        item.use(); // The item knows what to do!
    }

    /**
     * Monster attacks player
     * 
     * TODO: Customize how monsters attack!
     * - How much damage?
     * - Which monster attacks?
     * - Special abilities?
     */
    private void monsterAttack() {
        // build a list of every monster that gets to take a swipe at us
        ArrayList<Monster> attackers = getSpeedMonsters();
        // first check if there is a lastAttacked
        if (lastAttacked != null && lastAttacked.health() > 0 && !attackers.contains(lastAttacked))
            attackers.add(lastAttacked);

        for (Monster monster : attackers) {
            // shoudn't the monster's damage dealt logic be handle in the Monster class?
            int damageTaken = (int) (Math.random() * monster.damage() + 1);
            // Special abbilities
            // After monster deals damage...
            if (!monster.special().isEmpty()) {
                if (monster.special().equals("Vampire")) {
                    monster.heal(damageTaken/2);
                    gui.displayMessage("This Monster is a vampire and healed itself for" + damageTaken/2);
                   

            
                }else if (monster.special().equals("Reflecter")) {
                    playerHealth -= damageTaken *0.3;
                    gui.displayMessage("Oh No! The monster deflects"  + damageTaken * .3);

                }else if (monster.special().equals("Fear 5th")){
                    if(playerHealth %5 == 0){
                        playerHealth /= monster.damage()*5;
                }
                gui.displayMessage(" This monster hates the number 5, your health was a multiple of 5 so your health was divided by 5!!");

                }else if ( monster.special().equals("")){

                }

            // Add more special abilities!
        }
            if (shelidPower > 0) {
                double absorbance = Math.min(damageTaken, shelidPower);
                damageTaken -= absorbance;
                shelidPower -= absorbance;
                gui.displayMessage("You block for " + absorbance + " damage. You have " + shelidPower + " shield left.");
            }
            if (damageTaken > 0) {
                playerHealth -= damageTaken;
                gui.displayMessage("Monster hits you for " + damageTaken + " damage!");
                gui.updatePlayerHealth(playerHealth);
            }

            int index = monsters.indexOf(monster);
            gui.highlightMonster(index);
            gui.pause(300);
            gui.highlightMonster(-1);
        }

    }

    /**
     * Let player pick their character build using the 4 buttons
     * This demonstrates using the GUI for menu choices!
     */
    private void pickCharacterBuild() {
        // Set button labels to character classes
        String[] characterClasses = { "Fighter", "Tank", "Healer", "Ninja" };
        gui.setActionButtons(characterClasses);

        // Display choice prompt
        gui.displayMessage("---- PICK YOUR BUILD ----");

        // Wait for player to click a button (0-3)
        int choice = gui.waitForAction();

        // Initialize default stats
        playerDamage = 50;
        playerShield = 50;
        playerHeal = 50;
        playerSpeed = 10;
        playerHealth = 100;

        // Customize stats based on character choice
        if (choice == 0) {
            // Fighter: high damage, low healing and shield
            gui.displayMessage("You chose Fighter! High damage, but weak defense.");
            playerShield -= (int) (Math.random() * 20 + 1) + 5; // Reduce shield by 6-50
            playerHeal -= (int) (Math.random() * 20 + 1) + 5; // Reduce heal by 5-50

            playerSpeed -= (int) (Math.random() * 6) + 5; // Reduce speed by 1-9
            gui.displayMessage(" Player Stats --  Shield: " + playerShield + "  Healing Power: " + playerHeal);
        } else if (choice == 1) {
            // Tank: high shield, low damage and speed
            gui.displayMessage("You chose Tank! Tough defense, but slow attacks.");
            playerSpeed -= (int) (Math.random() * 9) + 1; // Reduce speed by 1-9
            playerDamage -= (int) (Math.random() * 20 + 1) + 5; // Reduce damage by 100-199
            gui.displayMessage(" Player Stats --  Speed: " + playerSpeed + "  Damage: " + playerDamage);
        } else if (choice == 2) {
            // Healer: high healing, low damage and shield
            gui.displayMessage("You chose Healer! Great recovery, but fragile.");
            playerDamage -= (int) (Math.random() * 21) + 5; // Reduce damage by 5-30
            playerShield -= (int) (Math.random() * 21) + 5; // Reduce shield by 5-50

            playerSpeed -= (int) (Math.random() * 10) + 1; // Reduce speed by 1-9
            gui.displayMessage(" Player Stats --  Speed: " + playerSpeed + "  Damage: " + playerDamage + " Shield: " + playerShield);

        } else {
            // Ninja: high speed, low healing and health
            gui.displayMessage("You chose Ninja! Fast and deadly, but risky.");
            playerHeal -= (int) (Math.random() * 46) + 5; // Reduce heal by 5-50
            playerHealth -= (int) (Math.random() * 21) + 5; // Reduce max health by 5-25

            playerSpeed -= (int) (Math.random() * 6) + 5; // Reduce speed by 1-9
        }
        if (playerHeal < 0)
            playerHeal = 0;
            gui.displayMessage(" Player Stats --  Speed: " + playerSpeed + "  Healing Power: " + playerHeal + " Max Health: " + playerHealth);

        gui.setPlayerMaxHealth(playerHealth);
        gui.updatePlayerHealth(playerHealth);
        // Pause to let player see their choice
        gui.pause(1500);
    }

    // ==================== HELPER METHODS ====================
    // Add your own helper methods here!

    /**
     * Count how many monsters are still alive
     */
    private int countLivingMonsters() {
        int count = 0;
        for (Monster m : monsters) {
            if (m.health() > 0)
                count++;
        }
        return count;
    }

    private ArrayList<Monster> getSpecialMonsters() {
        ArrayList<Monster> result = new ArrayList<>();
        for (Monster m : monsters) {
            if (m.special() != null && m.special().equals("") && m.health() > 0) {
                result.add(m);
            }
        }
        return result;
    }

    private ArrayList<Monster> getSpeedMonsters() {
        ArrayList<Monster> result = new ArrayList<>();
        for (Monster m : monsters) {
            if (m.speed() > playerSpeed && m.health() > 0) {
                result.add(m);
            }
        }
        return result;
    }

    private Monster getRandomLivingMonster() {
        ArrayList<Monster> alive = new ArrayList<>();
        for (Monster m : monsters) {
            if (m.health() > 0)
                alive.add(m);
        }
        if (alive.isEmpty())
            return null;
        return alive.get((int) (Math.random() * alive.size()));
    }

    // Items
//TODO ADD ITEMS
        //private void addHealthPotion(int healAmount) {
       // inventory.add(new Item("Health Potion", "🧪", () -> {
        //    playerHealth = Math.min(maxHealth, playerHealth + healAmount);
        //    gui.updatePlayerHealth(playerHealth);
        //    gui.displayMessage("💚 Used Health Potion! Healed " + healAmount + " HP!");
       // }));

    // TODO: Add more helper methods as you need them!
    // Examples:
    // - Method to find the strongest monster
    // - Method to check if player has a specific item
    // - Method to add special effects
    // - etc.

}
// specil ideas
// - remaove monster speed
// - auto kill all the slow monsters +\

//private Monster livigmonster () {

//}