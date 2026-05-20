import java.awt.*;

public class Function_Color {

    Main main;
    String selectedColor;
    public Function_Color(Main main){
        this.main= main;
    }

    public void setColor(String color){
        selectedColor=color;
        switch (selectedColor){
            case "White":
                main.myFrame.getContentPane().setBackground(Color.white);
                main.textArea.setBackground(Color.white);
                main.textArea.setForeground(Color.black);

                break;
            case "Black":
                main.myFrame.getContentPane().setBackground(Color.black);
                main.textArea.setBackground(Color.black);
                main.textArea.setForeground(Color.white);
                break;
            case "Blue":
                main.myFrame.getContentPane().setBackground(new Color(28, 43, 54));
                main.textArea.setBackground(new Color(28, 43, 54));
                main.textArea.setForeground(Color.white);
                break;
            case "Sepia":
                main.myFrame.getContentPane().setBackground(new Color(244, 236, 216));
                main.textArea.setBackground(new Color(244, 236, 216));
                main.textArea.setForeground(new Color(64, 48, 30));
                break;
        }
    }
}
