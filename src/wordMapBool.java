

public class wordMapBool {
    

//Jericho Jacala jjacala@bu.edu 7/11/2023
    private boolean[][] map;
    private char[] knownList;
    private int knownListLength;

    //initializes wordMap, an array of length 5 which designates which letters are possible in each index
    public wordMapBool(){
        map = new boolean[5][26];
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                map[i][j] = true;
            }
        }
        knownList = new char[5];
        knownListLength = 0;
    }

    public static int hash(int i){
        return i-'a';
    }

    //updates wordMapBool to not include the parameter character in the parameter index
    public void remove(char c, int index){
        map[index][hash(c)] = false;
    }

    //returns true if the character exists within a certain index of the wordMapBool
    public boolean exists(char c, int index){
        return map[index][hash(c)];
    }

    //returns a String format the characters within a wordMapBool; implemented for testing
    public String toString(){
        String result = "";
        for (int i = 0; i < map.length; i++){
            result = result + "Index " + i + ": ";
            for (int j = 0; j < map[i].length; j++){
                if (map[i][j]){
                    result = result + (char)('a' + j);
                }
            }
            result = result + "\n";
        }
        return result;
    }

    //removes every character but the given character; designed to designate green characters to indicies
    public void designateGreen(char c, int index){
        for (int i = 0; i < map[index].length; i++){
                map[index][i] = false;
        }
        map[index][hash(c)] = true;
        addKnown(c);
    }

    //process input of yellow letters by removing them from the index given and adding the letter to the known letters list
    //also checks if the given index's solution is known
    public void designateYellow(char c, int index){
        remove(c, index);
        addKnown(c);
        checkGreen(index);
    }

    //removes letter from all indices of the wordMapBool and checks if any indices of the wordMapBool have a known solution
    public void designateGray(char c){
        for (int i = 0; i < map.length; i++){
            if (!checkGreen(i)){
                remove(c, i);
            }
        }
        checkAllGreen();
    }

    /*removes all words not in the knownList from the wordMap; invoked when all 5 letters of the solution are known */
    public void allKnown(){
        for (char i = 'a'; i <= 'z'; i++){
            if (!known(i)){
                for (int j = 0; j < map.length; j++){
                    remove(i, j);
                }
            }
        }
    }

    //checks if any index in the wordMapBool has a known solution
    public void checkAllGreen(){
        for (int j = 0; j < map.length; j++){
            checkGreen(j);
        }
    }

    /*checks if there is one character in a wordMap; returns true if this is true*/
    public boolean checkGreen(int index){
        int count = 0;
        for (int i = 0; i < map[index].length; i++){
            if (map[index][i]){
                count++;
            }
        }
        return (count == 1);
    }

    //adds the character to the list of known characters if it is valid and not yet known
    public void addKnown(char c){
        if (knownListLength <= 4 && !known(c)){
            knownList[knownListLength] = c;
            knownListLength++;
        }else if (knownListLength > 4){
            throw new IllegalArgumentException("knownList length maximum reached");
        }
        if (knownListLength == 5){
            allKnown();
        }
    }

    //returns true if the character is within the knownList, false otherwise
    public boolean known(char c){
        for (int i = 0; i < knownListLength; i++){
            if (c == knownList[i]){
                return true;
            }
        }
        return false;
    }

    //checks if all letters are known of the solution and removes any other letters from the wordMap
    public boolean hasAllKnown(String line){
        for (int i = 0; i < knownListLength; i++){
            if (!lineHas(knownList[i], line)){
                return false;
            }
        }
        return true;
    }

    //checks if a given String has that character
    public boolean lineHas(char c, String line){
        for (int i = 0; i < line.length(); i++){
            if (c == line.charAt(i)){
                return true;
            }
        }
        return false;
    }
}

