package Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlienBattleGame extends JPanel {
    private Player player;
    private ComputerPlayer computer;
    private boolean playerTurn = true; // Player 1 starts the game
    private boolean playerDefenceActive = false; 
    private boolean computerDefenceActive = false;

    private JLabel playerHPLabel;
    private JLabel opponenHPLabel;
    private JLabel messageLabelComputer;

    JButton shieldButton;
    JButton attack1Button;
    JButton attack2Button;
    JButton ultimateButton;

    private ImageIcon attackIcon;
    private ImageIcon defenceIcon;
    private JFrame frame;

    private int ultimateLoaderCT;
    private int playerAttackX; // X position for the player's attack animation
    private int playerAttackY;
    private int computerAttackX; // X position for the computer's attack animation
    private int computerAttackY;
    private boolean isAnimatingPlayerAttack = false; // To check if the player's attack animation is running
    private boolean isAnimatingComputerAttack = false; // To check if the computer's attack animation is running

    public AlienBattleGame() {
        
        // Initialize the frame
        frame = new JFrame("Alien Clash");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        repaint();

        // Initialize player 1 (human) and player 2 (computer)
        player = new Player(100, 20, 10);
        computer = new ComputerPlayer(100, 15, 10);

        // Main game panel
        this.setLayout(null);
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.BLACK);
        

        // Player and computer attack / defense ability icons
        attackIcon = new ImageIcon(getClass().getResource("attackLabel.png"));
        defenceIcon = new ImageIcon(getClass().getResource("shieldLabel.png"));

        // Player HP labels
        playerHPLabel = new JLabel(" HP: " + player.getHP());
        playerHPLabel.setForeground(Color.WHITE); 
        playerHPLabel.setBounds(70, 170, 150, 30); 
        opponenHPLabel = new JLabel(" HP: " + computer.getHP());
        opponenHPLabel.setForeground(Color.WHITE);
        opponenHPLabel.setBounds(530, 30, 150, 30); 
        
        this.add(playerHPLabel);
        this.add(opponenHPLabel);

        

        // Label for Last action of computer
        messageLabelComputer = new JLabel(" ‎ ");
        messageLabelComputer.setForeground(Color.WHITE);
        messageLabelComputer.setBounds(700, 130, 150, 50);
        this.add(messageLabelComputer);
        

        //shield button set-up
        shieldButton = new JButton("Shield");
        shieldButton.setBounds(100, 450, 600, 50); 
        shieldButton.setIcon(new ImageIcon(this.getClass().getResource("defence.png")));
        //attack 1 button set-up
        attack1Button = new JButton("Attack");
        attack1Button.setBounds(100, 510, 180, 50);
        attack1Button.setIcon(new ImageIcon(this.getClass().getResource("attack1.png")));
        //attack 2 button set-up
        attack2Button = new JButton("Attack2");
        attack2Button.setBounds(310, 510, 180, 50);
        attack2Button.setIcon(new ImageIcon(this.getClass().getResource("attack2.png")));
        //ultimate button set-up
        ultimateButton = new JButton("Ultimate");
        ultimateButton.setBounds(520, 510, 180, 50);
        ultimateButton.setIcon(new ImageIcon(this.getClass().getResource("ultimate.png")));
        //ultimate button is not initially available for use
        ultimateButton.setVisible(false);
        

        this.add(attack1Button);
        this.add(shieldButton);
        this.add(attack2Button);
        this.add(ultimateButton);

        // Add the JPanel to the frame
        frame.add(this);  
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Action listeners for buttons
        attack1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerTurn) {
                    startPlayerAttackAnimation();  // Player attack
                }
            }
        });

        shieldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerTurn) {
                    playerAction("shield", player, computer);
                }
            }
        });
    }

    // Start the player attack animation
    private void startPlayerAttackAnimation() {
        //turn off buttons
        toggleButtons(false);
        isAnimatingPlayerAttack = true;
        playerAttackX = 350;  // Start position just after player
        playerAttackY = 0;
        // Timer to animate the player's attack moving towards the target
        Timer timer = new Timer(35, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerAttackX += 10;  // Move attack towards the computer
                playerAttackY += 10;
                // Check for collision with the computer's position
                if (playerAttackX + 50 >= 550) {  // Stop when it reaches the opponent
                    ((Timer) e.getSource()).stop();
                    isAnimatingPlayerAttack = false;

                    // Execute attack logic
                    playerAction("attack", player, computer);
                }

                repaint();  // Repaint to show the animation frame
                
            }
        });
        timer.start();
        
    }

    // Start the computer attack animation
    private void startComputerAttackAnimation() {
        isAnimatingComputerAttack = true;
        computerAttackX = 350;  // Start position just before computer
        computerAttackY = 100;

        // Timer to animate the computer's attack moving towards the player
        Timer timer = new Timer(35, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                computerAttackX -= 10;  // Move attack towards the player
                computerAttackY += 10;
                // Check for collision with the player's position
                if (computerAttackX <= 150) {  // Stop when it reaches the player
                    ((Timer) e.getSource()).stop();
                    isAnimatingComputerAttack = false;

                    
                }

                repaint();  // Repaint to show the animation frame
            }
        });
        timer.start();
    }

    // Method to handle player actions (attack/shield)
    private void playerAction(String action, Player player, Player opponent) {
        int damage;
        //increment of ultimate loader
        ultimateLoaderCT++;
        
        //enhanced defence is only active for one round than it reverts back to base
        if (playerDefenceActive) {
            playerDefenceActive = false;
            player.setDefence(-10);

        }

        if (action.equals("attack")) {
            damage = Math.max(player.getAttackPower() - opponent.getDefense(), 0);
            opponent.takeDamage(damage);
            updateHPLabel();
            checkVictory();
        } else if (action.equals("shield")) {
            toggleButtons(false);
            player.setDefence(10);
            playerDefenceActive = true;

        }

        // Switch to Player 2 (Computer's turn)
        playerTurn = false;
        
        
        
        
        
        computerTurn();
    }

    // Computer's turn logic
    // Computer's turn logic with 2 seconds delay
// Computer's turn logic with 2 seconds delay using Thread
// Computer's turn logic with 2 seconds delay using Swing Timer
private void computerTurn() {
    // Disable player's turn during computer's move
    playerTurn = false;
    messageLabelComputer.setVisible(false);
    
    // Create a Swing Timer to delay the computer's move by 2 seconds (2000 milliseconds)
    Timer computerMoveTimer = new Timer(2500, new ActionListener() {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            // Get the computer's action (either attack or shield)
            String action = computer.getAction();
            toggleButtons(true);
            if (computerDefenceActive) {
                computerDefenceActive = false;
                computer.setDefence(-10);
            }

            // If the computer decides to attack
            if (action.equals("attack")) {
                startComputerAttackAnimation();  // Start the computer's attack animation

                // Execute attack logic
                int damage = Math.max(computer.getAttackPower() - player.getDefense(), 0);
                player.takeDamage(damage);
                updateHPLabel();
                checkVictory();
                messageLabelComputer.setIcon(attackIcon);

            // If the computer decides to shield
            } else {
                messageLabelComputer.setIcon(defenceIcon);
                computer.setDefence(10);
                computerDefenceActive = true;
                
            }

            // Switch back to Player's turn after computer's move
            playerTurn = true;

            // Stop the timer after the computer has made its move
            messageLabelComputer.setVisible(true);
            ((Timer) e.getSource()).stop();
        }
    });

    // Start the timer (after 2 seconds, the computer's move will execute)
    computerMoveTimer.setRepeats(false);  // Ensure it only runs once
    computerMoveTimer.start();
    
}




    // Update HP labels
    private void updateHPLabel() {
        playerHPLabel.setText(" HP: " + player.getHP());
        opponenHPLabel.setText(" HP: " + computer.getHP());
        repaint();  // Repaint to update health bars
    }

    // Check if there's a winner
    private void checkVictory() {
        if (player.getHP() <= 0) {
            disableButtons();
        } else if (computer.getHP() <= 0) {
            computer = new ComputerPlayer(100, 10, 5);
            opponenHPLabel.setText("HP"+ computer.getHP());
            
        }
    }
    

    // Disable buttons after the game ends
    private void disableButtons() {
        Component[] components = this.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                comp.setEnabled(false);
            }
        }
    }

    // Load background
    Image backgroundImage = new ImageIcon(getClass().getResource("background.jpeg")).getImage();
    //Load attack image
    Image missile = new ImageIcon(getClass().getResource("missile.png")).getImage();
    Image missileRotated = new ImageIcon(getClass().getResource("missileRotated.png")).getImage();
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, 800, 600, this);
            g.drawImage(player.getSkin(), 100, 250, 75, 150, this);
            g.drawImage(computer.getSkin(), 550, 100, 75, 150, this);
        }
        
        drawHealthBar(g, player.getHP(), 100, 70, 200, 200, 20, Color.GREEN);
        drawHealthBar(g, computer.getHP(), 100, 530, 60, 200, 20, Color.RED);
        
        // Draw the player's attack animation
        if (isAnimatingPlayerAttack) {
            g.drawImage(missileRotated,playerAttackX,playerAttackY,50,50,null);
        }

        // Draw the computer's attack animation
        if (isAnimatingComputerAttack) {
            g.drawImage(missile, computerAttackX,computerAttackY,50,50,null);
        }
    }

    private void drawHealthBar(Graphics g, int currentHP, int maxHP, int x, int y, int width, int height, Color color) {
        int healthPercentage = (int) ((double) currentHP / maxHP * width);
        
        // Draw the background (empty health bar)
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
    
        // Draw the foreground (filled health bar based on current health)
        g.setColor(color);
        g.fillRect(x, y, healthPercentage, height);
    
        // Draw a border around the health bar
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);
    }

    private void toggleButtons(Boolean val) {
        attack1Button.setVisible(val); 
        attack2Button.setVisible(val);
        shieldButton.setVisible(val);
        if (ultimateLoaderCT == 4 || val == false) {
        ultimateButton.setVisible(val);
        }
    }
    
}
