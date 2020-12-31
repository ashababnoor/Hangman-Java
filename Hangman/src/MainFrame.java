import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

/**
 * Advanced OOP Lab
 * Assignment 01
 * Section: A
 *
 * Hangman Game using Java Swing
 * @author Ahmed Shabab Noor (011 193 024)
 */

public class MainFrame extends JFrame {

    private static final JPanel bgPanel = addBackgroundPanel();
    private static final ArrayList<JLabel> labels = new ArrayList<>();
    private static final HangmanStatesPanel hangmanStatePanel = new HangmanStatesPanel();
    private static final HashSet<String> usedWords = new HashSet<>();
    private static final ArrayList<String> wordList = new ArrayList<>();
    private static final JButton[] buttons = new JButton[26];
    private static JLabel scoreLabel;

    private static int state = 0;
    private static int foundLetters = 0;
    private static int totalLetters = 0;
    private static int score = 0;
    private static final String scoreText = "       Score: ";

    public MainFrame(int width, int height){
        super("Hangman");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Image icon = Toolkit.getDefaultToolkit().getImage("images/icon-1.png");
        setIconImage(icon);

        addPanelComponents();
        revalidate();

        setContentPane(bgPanel);
        setVisible(true);
    }

    private static void addPanelComponents(){
        JPanel centerPanel = new JPanel();

        JPanel titlePanel = new TitlePanel("images/title-img.png");

        buttonInitializer();
        JPanel buttonPanel = new ButtonPanel();

        initializeDictionary();
        String word = getWordFromDictionary();
        System.out.println(word);
        WordPanel wordPanel = new WordPanel(word);
        totalLetters = word.length();

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.setPreferredSize(new Dimension(300, 50));
        JButton resetButton = new JButton("Reset");
        resetButton.setBackground(Color.cyan);
        resetButton.setForeground(Color.darkGray);
        resetButton.setFont(new Font("Arial", Font.BOLD, 25));

        resetButton.addActionListener(e -> {
            bgPanel.repaint();
            bgPanel.revalidate();
            reset();
        });
        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                bgPanel.repaint();
                bgPanel.revalidate();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                bgPanel.repaint();
                bgPanel.revalidate();
            }
        });

        scoreLabel = new JLabel("");
        scoreLabel.setText(scoreText+score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 25));

        bottomPanel.add(resetButton); bottomPanel.add(scoreLabel);

        centerPanel.add(wordPanel);
        centerPanel.add(buttonPanel);
        centerPanel.add(bottomPanel);

        bgPanel.add(titlePanel, BorderLayout.NORTH);
        bgPanel.add(centerPanel, BorderLayout.CENTER);
        bgPanel.add(hangmanStatePanel, BorderLayout.EAST);
        bgPanel.repaint();
        bgPanel.revalidate();
    }

    static class ButtonPanel extends JPanel{
        ButtonPanel(){
            super(10);
            setLayout(new GridLayout(3,1));
            Dimension size = new Dimension(500, 180);
            setPreferredSize(size);
            setMaximumSize(size);

            JPanel firstRow = new JPanel(new GridLayout(1, 9));
            JPanel secondRow = new JPanel(new GridLayout(1, 9));
            JPanel thirdRow = new JPanel(new GridLayout(1, 8));
            thirdRow.setBorder(new EmptyBorder(0, 25, 0, 25));
            add(firstRow); add(secondRow); add(thirdRow);

            for (int i=0; i<26; i++){
                JButton button = buttons[i];

                button.setBackground(Color.darkGray);
                button.setFont(new Font("Arial", Font.BOLD, 18));
                button.setForeground(Color.cyan);

                button.setOpaque(false);
                button.setFocusPainted(false);
                button.setBorderPainted(false);
                button.setContentAreaFilled(false);

                button.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
                button.setHorizontalTextPosition(JButton.CENTER);
                button.setVerticalTextPosition(JButton.CENTER);

                if (i > 17) thirdRow.add(button);
                else if (i > 8) secondRow.add(button);
                else firstRow.add(button);

                button.addActionListener(e -> {
                    String letter = button.getText();
                    button.setEnabled(false);
                    letterChecker(letter);
                    bgPanel.repaint();
                    bgPanel.revalidate();
                });
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        bgPanel.repaint();
                        bgPanel.revalidate();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        bgPanel.repaint();
                        bgPanel.revalidate();
                    }
                });
            }
        }
    }

    private static void letterChecker(String word){
        boolean correctLetter = false;

        for(JLabel label:labels){
            if (word.equals(label.getText())){
                label.setForeground(new Color(100, 100, 100,255));
                correctLetter = true;
                foundLetters++;
            }
        }
        if (foundLetters == totalLetters)
            gameWon();

        if (!correctLetter){
            state++;
            hangmanStatePanel.setState(state);
            if (state >= 6) gameOver();
        }
        bgPanel.repaint();
        bgPanel.revalidate();
    }

    private static void gameOver(){
        bgPanel.repaint();
        bgPanel.revalidate();
        JOptionPane.showMessageDialog(bgPanel,"Better luck next time! :(");
        reset();
    }
    private static void gameWon(){
        bgPanel.repaint();
        bgPanel.revalidate();
        score++;
        scoreLabel.setText(scoreText+score);

        JOptionPane pane = new JOptionPane();
        /*
        pane.setForeground(new Color(0x4ABF01));
        pane.setFont(new Font("Arial", Font.BOLD, 20));
        */
        pane.showMessageDialog(bgPanel,"You Won! Congratulations!");
        reset();
    }

    private static void reset(){
        foundLetters = 0;
        state = 0;
        for(JLabel label:labels){
            label.setForeground(new Color(0, 0, 0, 0));
        }
        labels.clear();
        hangmanStatePanel.setState(0);
        for (int i=0; i<26; i++){
            buttons[i].setEnabled(true);
        }
        bgPanel.removeAll();
        addPanelComponents();
        bgPanel.repaint();
        bgPanel.revalidate();
    }

    private static void buttonInitializer(){
        Image image = addImage("images/letter-button.png", 50, 50, 4);
        ImageIcon icon = new ImageIcon(image);
        char character = 65;

        for (int i=0; i<26; i++){
            buttons[i] = new JButton(String.valueOf(character++), icon);
        }
    }

    static class WordPanel extends JPanel{
        WordPanel(String word){
            super(10);
            //setBorder(new LineBorder(Color.black));
            setLayout(new GridLayout(1, word.length()));

            char[] letters = word.toCharArray();
            for (char letter:letters){
                JLabel label = new JLabel(String.valueOf(letter));
                label.setForeground(new Color(100, 100, 100,0));
                label.setFont(new Font("Arial", Font.BOLD, 40));

                Image image = addImage("images/letter-holder.png", 40, 40, 4);
                ImageIcon icon = new ImageIcon(image);
                label.setIcon(icon);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.BOTTOM);

                label.setHorizontalTextPosition(JLabel.CENTER);
                label.setVerticalTextPosition(JLabel.CENTER);

                labels.add(label);
            }

            for (JLabel label:labels){
                add(label);
            }

            Dimension size = new Dimension(500, 150);
            setPreferredSize(size);
            setMaximumSize(size);
        }
    }

    static class HangmanStatesPanel extends JPanel{
        JLabel imageLabel = new JLabel();

        HangmanStatesPanel(){
            super(20);
            Dimension size = new Dimension(310, 420);
            setPreferredSize(size);
            setMaximumSize(size);

            setState(0);
            imageLabel.setPreferredSize(size);
            add(imageLabel);
            setVisible(true);
            repaint();
            revalidate();
        }

        void setState(int num){
            if (num > 6) num = 6;
            if (num < 0) num = 0;

            String path = "images/state-"+num+".png";
            Image image = addImage(path, 250, 400, 4);
            ImageIcon icon = new ImageIcon(image);
            imageLabel.setIcon(icon);
            imageLabel.repaint();
            revalidate();
        }
    }

    static class TitlePanel extends JPanel{
        TitlePanel(String pathname){
            super(10);
            setBorder(new EmptyBorder(20, 10, 10, 10));
            Image image = addImage(pathname, 333, 51, 4);
            ImageIcon icon = new ImageIcon(image);

            JLabel titleLabel = new JLabel();
            titleLabel.setIcon(icon);
            add(titleLabel);

            Dimension size = new Dimension(333, 51);
            Dimension panelSize = new Dimension(900, 90);

            titleLabel.setPreferredSize(size);
            titleLabel.setMinimumSize(size);

            setMinimumSize(panelSize);
            setPreferredSize(panelSize);
        }
    }

    private static String getWordFromDictionary(){
        String word = "";
        boolean isFalse = false;

        while (!isFalse){
            Random rand = new Random();
            int index = rand.nextInt(wordList.size());
            word = wordList.get(index);
            if (!usedWords.contains(word)){
                isFalse = true;
                usedWords.add(word);
            }
        }
        return word;
    }

    private static void initializeDictionary(){
        File file = new File("dictionary.txt");
        try{
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()){
                String word = scanner.next();
                wordList.add(word);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private static Image addImage(String pathname, int ... dimension){
        Image image = null;
        try{
            BufferedImage img = ImageIO.read(new File(pathname));
            if (dimension.length == 3)
                image = img.getScaledInstance(dimension[0], dimension[1], dimension[2]);
            else
                image = img;
        }catch (IOException e){
            e.printStackTrace();
        }
        /*
        Image.SCALE_DEFAULT = 1
        Image.SCALE_FAST = 2
        Image.SMOOTH_SCALE = 4
        Image.SCALE_AREA_AVERAGING = 16
        */
        return image;
    }

    private static JPanel addBackgroundPanel(){
        Image image = addImage("images/background-img.png");
        return new BackgroundPanel(image, 0);
    }

    public static void main(String[] args) {
        new MainFrame(900, 600);
    }
}

class JPanel extends javax.swing.JPanel{
    JPanel(){
        super();
        setBackground(new Color(0, 0, 0, 0));
    }

    JPanel(int borderSize){
        super();
        setBackground(new Color(0, 0, 0, 0));
        setBorder(new EmptyBorder(borderSize, borderSize, borderSize, borderSize));
    }
    JPanel(GridLayout layout){
        super(layout);
        setBackground(new Color(0, 0, 0, 0));
    }
}