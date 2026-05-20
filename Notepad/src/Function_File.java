import java.awt.*;
import java.io.*;
import javax.swing.JOptionPane;

public class Function_File {
    Main main;
    String fileName;
    String fileAddress;

    public  Function_File(Main main){
        this.main=main;
    }

    public void newFile(){
        main. myFrame.setTitle("New");
        main.textArea.setText("");
        fileName=null;
        fileAddress=null;
    }

    public void rename(){
        FileDialog fd = new FileDialog(main.myFrame, "Rename", FileDialog.SAVE);
        fd.setVisible(true);
        if (fd.getFile()!=null){
            fileName=fd.getFile();
            fileAddress= fd.getDirectory();
            main.myFrame.setTitle(fileName);
            try (FileWriter fw = new FileWriter(fileAddress + fileName)) {
                fw.write(main.textArea.getText());
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(main.myFrame, "Failed to rename and save file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void open() {

        FileDialog fd = new FileDialog(main.myFrame, "Open", FileDialog.LOAD);
        fd.setVisible(true);
        if(fd.getFile()!=null){
            fileAddress=fd.getDirectory();
            fileName=fd.getFile();
            main.myFrame.setTitle(fileName);
            try (BufferedReader br = new BufferedReader(new FileReader(fileAddress + fileName))) {
                main.textArea.read(br, null);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(main.myFrame, "Failed to open file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void save(){

        if (fileName ==null){
             saveAs();
        }
        else{
            try (FileWriter fw = new FileWriter(fileAddress + fileName)) {
                fw.write(main.textArea.getText());
                main.myFrame.setTitle(fileName);
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(main.myFrame, "Failed to save file.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }
    public void saveAs(){
       FileDialog fd = new FileDialog(main.myFrame, "SaveAs", FileDialog.SAVE);
       fd.setVisible(true);
        if (fd.getFile()!=null){
            fileName=fd.getFile();
            fileAddress= fd.getDirectory();
            main.myFrame.setTitle(fileName);
            try (FileWriter fw = new FileWriter(fileAddress + fileName)) {
                fw.write(main.textArea.getText());
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(main.myFrame, "Failed to save file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void exit(){
        System.exit(0);
    }
}
