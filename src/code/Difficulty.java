package code;

//A simple enumerator class to return the score value and text associated with difficulties.
public enum Difficulty {
    NONE, EASY, MEDIUM, HARD, VERYHARD;

    public String string(){
        if(this==EASY) return "EASY";
        if(this==MEDIUM) return "MEDIUM";
        if(this==HARD) return "HARD";
        if(this==VERYHARD) return "AAAH!";
        return "";
    }

    public int score(){
        if(this==EASY) return 1;
        if(this==MEDIUM) return 2;
        if(this==HARD) return 3;
        if(this==VERYHARD) return 4;
        return 0;
    }
}
