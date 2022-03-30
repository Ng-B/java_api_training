package fr.lernejo.navy_battle.game_logic;

public class Cell {

    public Cell(){}

    /*HELPER METHODS*/
    public int convertLetterToInt(String val)
    {
        char firstChar = val.charAt(0);

        int result = -1;
        switch (firstChar)
        {
            case 'A':   result = 0;
                break;
            case 'B':   result = 1;
                break;
            case 'C':   result = 2;
                break;
            case 'D':   result = 3;
                break;
            case 'E':   result = 4;
                break;
            case 'F':   result = 5;
                break;
            case 'G':   result = 6;
                break;
            case 'H':   result = 7;
                break;
            case 'I':   result = 8;
                break;
            case 'J':   result = 9;
                break;
            default:    result = -1;
                break;
        }

        return result;
    }


}
