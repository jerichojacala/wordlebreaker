    import java.awt.*;
    import java.awt.event.*;
    import javax.swing.*;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.io.IOException;
    import java.util.List;

public class userint extends JFrame implements ActionListener{
    private JPanel greenPanel;
    private JPanel yellowPanel;
    private JPanel grayPanel;
    private JPanel submitPanel;
    private JPanel resetPanel;
    private JLabel greenLetterLabel;
    private JLabel yellowLetterLabel;
    private JLabel grayLetterLabel;
    private JTextField greenLetter1;
    private JTextField greenLetter2;
    private JTextField greenLetter3;
    private JTextField greenLetter4;
    private JTextField greenLetter5;
    private JTextField yellowLetter1;
    private JTextField yellowLetter2;
    private JTextField yellowLetter3;
    private JTextField yellowLetter4;
    private JTextField yellowLetter5;
    private JTextField grayLetter;
    private JTextArea solutions;
    private JButton submit;
    private JButton reset;
    JScrollPane scroll;
    wordMapBool map;

    public static void main(String[] args){
        new userint();
    }

    public userint() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("WORDLEBREAKER");
        setSize(400, 400);
        setLayout(new FlowLayout());

        greenPanel = new JPanel();
        greenPanel.setBackground(Color.GREEN);
        greenPanel.setLayout(new FlowLayout());

        yellowPanel = new JPanel();
        yellowPanel.setBackground(Color.YELLOW);
        yellowPanel.setLayout(new FlowLayout());

        greenLetterLabel = new JLabel("", JLabel.LEFT);
        yellowLetterLabel = new JLabel("", JLabel.LEFT);
        grayLetterLabel = new JLabel("", JLabel.LEFT);
        greenLetterLabel.setText("ENTER GREEN LETTERS: ");
        yellowLetterLabel.setText("ENTER YELLOW LETTERS: ");
        grayLetterLabel.setText("ENTER GRAY LETTERS: ");

        greenLetter1 = new JTextField(1);
        greenLetter2 = new JTextField(1);
        greenLetter3 = new JTextField(1);
        greenLetter4 = new JTextField(1);
        greenLetter5 = new JTextField(1);

        yellowLetter1 = new JTextField(1);
        yellowLetter2 = new JTextField(1);
        yellowLetter3 = new JTextField(1);
        yellowLetter4 = new JTextField(1);
        yellowLetter5 = new JTextField(1);

        grayLetter = new JTextField(20);
        

        submit = new JButton("SUBMIT");
        submit.setActionCommand("submit");
        submit.addActionListener(this);
        submitPanel = new JPanel();

        reset = new JButton("RESET");
        reset.setActionCommand("reset");
        reset.addActionListener(this);
        resetPanel = new JPanel();

        solutions = new JTextArea("Solutions will appear here");
        solutions.setLineWrap(true);
        solutions.setWrapStyleWord(true);
        solutions.setBackground(Color.GRAY);

        greenPanel.add(greenLetterLabel);
        greenPanel.add(greenLetter1);
        greenPanel.add(greenLetter2);
        greenPanel.add(greenLetter3);
        greenPanel.add(greenLetter4);
        greenPanel.add(greenLetter5);

        yellowPanel.add(yellowLetterLabel);
        yellowPanel.add(yellowLetter1);
        yellowPanel.add(yellowLetter2);
        yellowPanel.add(yellowLetter3);
        yellowPanel.add(yellowLetter4);
        yellowPanel.add(yellowLetter5);

        grayPanel = new JPanel();
        grayPanel.add(grayLetterLabel);
        grayPanel.add(grayLetter);

        scroll = new JScrollPane(solutions, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new Dimension(300,200));
        submitPanel.add(submit);
        resetPanel.add(reset);

        add(greenPanel);
        add(yellowPanel);
        add(grayPanel);
        add(submitPanel);
        add(resetPanel);
        add(scroll);

        map = new wordMapBool();
        
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();
        if (command == "submit"){
            long startTime = System.currentTimeMillis();
            String[] green = new String[5];
            String[] yellow = new String[5];
            String gray = "";
            green[0] = greenLetter1.getText().toLowerCase();
            green[1] = greenLetter2.getText().toLowerCase();
            green[2] = greenLetter3.getText().toLowerCase();
            green[3] = greenLetter4.getText().toLowerCase();
            green[4] = greenLetter5.getText().toLowerCase();

            yellow[0] = yellowLetter1.getText().toLowerCase();
            yellow[1] = yellowLetter2.getText().toLowerCase();
            yellow[2] = yellowLetter3.getText().toLowerCase();
            yellow[3] = yellowLetter4.getText().toLowerCase();
            yellow[4] = yellowLetter5.getText().toLowerCase();

            gray = gray + grayLetter.getText().toLowerCase();
            if (update(green, yellow, gray)){
                generateSolutions();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Method execution time: " + (endTime-startTime) + " milliseconds");
        }
        if (command == "reset"){
            clearTextFields();
            clearGreenTextFields();
            map = new wordMapBool();
        }
    }

    //cleans the input from the user and updates the wordMap accordingly
    public boolean update(String[] green, String[] yellow, String gray){
        for (int i = 0; i < 5; i++){
            if (green[i].length() > 1 || yellow[i].length() > 1){
                return false;
            }
        }
        for (int j = 0; j < 5; j++){
            if (green[j].length() == 1){
                map.designateGreen(green[j].charAt(0), j);
            }
            if (yellow[j].length() == 1){
                map.designateYellow(yellow[j].charAt(0), j);
            }
        }
        for (int k = 0; k < gray.length(); k++){
            if (!map.known(gray.charAt(k))){
                map.designateGray(gray.charAt(k));
            }
        }
        System.out.println(map);
        return true;
    }

    //sets the solutions panel text to possible solutions based on the wordMap
    public void generateSolutions() {
        clearTextFields();
        submit.setEnabled(false);
        submit.setText("Generating solutions...");
        String fileName = "C:/Users/jerry/cs112/wordlebreaker/lib/words2.txt";
        try{
            Path path = Paths.get(fileName);
            List<String> lines = Files.readAllLines(path);
            for (String line : lines){
                if (checkValid(line)){
                    solutions.setText(solutions.getText() + " " + line);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        submit.setEnabled(true);
        submit.setText("SUBMIT");
    }

    //checks if a word is eligible for the wordMap
    public boolean checkValid(String line){
        if (line.length() != 5){
            throw new IllegalArgumentException("Word length must be 5");
        }
        if (!map.hasAllKnown(line)){
            return false;
        }
        for (int i = 0; i < line.length(); i++){
            if (!map.exists(line.charAt(i), i)){
                return false;
            }
        }
        return true;
    }

    //clears the text fields
    public void clearTextFields(){
        yellowLetter1.setText("");
        yellowLetter2.setText("");
        yellowLetter3.setText("");
        yellowLetter4.setText("");
        yellowLetter5.setText("");
        grayLetter.setText("");
        solutions.setText("");
    }

    public void clearGreenTextFields(){
        greenLetter1.setText("");
        greenLetter2.setText("");
        greenLetter3.setText("");
        greenLetter4.setText("");
        greenLetter5.setText("");
    }

}
