package code;

/*This is a complex enumerator class that helps to keep the Game class smaller,
this class can be used as the ultimate reference for what you want from different modes
for integers and strings, for example what speed should the planet spin?, or what
text should the left label have?
 */
public enum Mode {
    SWEEPER, BACTERIA, RESET, SLAMDISCO, TEST, GAMEOVER, STARTSCREEN, RIPPLE, FINISH;

    public String modeString(boolean newhighscore){
      if(this==SWEEPER) return "SWEEPER";
      if(this==BACTERIA) return "BACTERIA";
      if(this==SLAMDISCO) return "SLAMDISCO";
      if(this==RIPPLE) return "RIPPLE";
      if(this==RESET) return "GET";
      if(this==TEST) return "TEST";
      if(this==STARTSCREEN) return "";
      if(this==FINISH) return "YOU";
      if(this==GAMEOVER){
            if(!newhighscore) return "YOU";
            else return "NEW";
        }
      return "";
    }

    public String difficultyString(Difficulty difficulty, boolean newhighscore){
        if(this==RESET) return "READY";
        if(this==TEST) return "TEST";
        if(this==STARTSCREEN) return "";
        if(this==FINISH) return "WIN!";
        if(this==GAMEOVER){
            if(!newhighscore) return "LOSE";
            else return "HIGHSCORE!";
        }
        return difficulty.string();
    }

    public String timeString(int seconds){
        if(this==RESET) return "RESET";
        if(this==GAMEOVER) return "GAME OVER";
        if(this==FINISH) return "GAME FINISHED";
        if(this==STARTSCREEN) return "";
        return "" + seconds;
    }

    //rotation/angle spin for the planet+buildings per game tick.
    public double spinSpeed(Difficulty difficulty){
        if(this==SWEEPER && difficulty==Difficulty.EASY) return 0.35;
        if(this==SWEEPER && difficulty==Difficulty.MEDIUM) return 0.55;
        if(this==SWEEPER && difficulty==Difficulty.HARD) return 1.25;
        if(this==SWEEPER && difficulty==Difficulty.VERYHARD) return 1.8;
        if(this==SLAMDISCO && difficulty==Difficulty.EASY) return 1.3;
        if(this==SLAMDISCO && difficulty==Difficulty.MEDIUM) return 1.6;
        if(this==SLAMDISCO && difficulty==Difficulty.HARD) return 1.9;
        if(this==SLAMDISCO && difficulty==Difficulty.VERYHARD) return 2.3;
        if((this== BACTERIA || this==RIPPLE) && difficulty==Difficulty.EASY) return 0.1;
        if((this== BACTERIA || this==RIPPLE) && difficulty==Difficulty.MEDIUM) return 0.2;
        if((this== BACTERIA || this==RIPPLE) && difficulty==Difficulty.HARD) return 0.3;
        if((this== BACTERIA || this==RIPPLE) && difficulty==Difficulty.VERYHARD) return 0.4;
        if(this==STARTSCREEN) return 0.3;
        return 0;
    }

    //time != 1 second, time is a unit of time that changes with difficulty/mode.
    public double timeSpeed(Difficulty difficulty){
        if(this==SWEEPER && difficulty==Difficulty.EASY) return 45;
        if(this==SWEEPER && difficulty==Difficulty.MEDIUM) return 32;
        if(this==SWEEPER && difficulty==Difficulty.HARD) return 20;
        if(this==SWEEPER && difficulty==Difficulty.VERYHARD) return 15;
        if(this== BACTERIA && difficulty==Difficulty.EASY) return 25;
        if(this== BACTERIA && difficulty==Difficulty.MEDIUM) return 20;
        if(this== BACTERIA && difficulty==Difficulty.HARD) return 15;
        if(this== BACTERIA && difficulty==Difficulty.VERYHARD) return 5;
        if(this== RIPPLE && difficulty==Difficulty.EASY) return 8;
        if(this== RIPPLE && difficulty==Difficulty.MEDIUM) return 6;
        if(this== RIPPLE && difficulty==Difficulty.HARD) return 3;
        if(this== RIPPLE && difficulty==Difficulty.VERYHARD) return 2;
        return 0;
    }
}
