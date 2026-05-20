import java.awt.*;

public class Function_Format {
    Main main;
    Font arial, timesNewRoman, impact, verdana, sanSerif;
    String selectedFont;

    public Function_Format(Main main){
        this.main=main;
    }

    public void wrap(){
        if (!main.wordWrapOn){
            main.wordWrapOn=true;
            main.textArea.setLineWrap(true);
            main.textArea.setWrapStyleWord(true);
            main.iWrap.setText("Word Wrap : On");
        }
        else{
            main.wordWrapOn=false;
            main.textArea.setLineWrap(false);
            main.textArea.setWrapStyleWord(false);
            main.iWrap.setText("Word Wrap : Off");
        }
    }

    public void font(int size){
        arial = new Font("Arial", Font.PLAIN, size);
        timesNewRoman = new Font("Times New Roman", Font.PLAIN, size);
        verdana = new Font("Verdana", Font.PLAIN, size);
        impact = new Font("Impact", Font.PLAIN, size);
        sanSerif = new Font("SansSerif", Font.PLAIN, size);

        setFont(selectedFont);

    }
    public void setFont(String font){
        selectedFont=font;

        switch (selectedFont){
            case "Arial":
                main.textArea.setFont(arial);
                break;
            case "Times New Roman":
                main.textArea.setFont(timesNewRoman);
                break;
            case "Verdana":
                main.textArea.setFont(verdana);
                break;
            case "Sans Serif":
                main.textArea.setFont(sanSerif);
                break;
            case "Impact":
                main.textArea.setFont(impact);
                break;

        }
    }
}
