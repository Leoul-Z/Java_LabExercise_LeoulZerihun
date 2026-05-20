import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.io.FileNotFoundException;
import javax.swing.UIManager;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener {

    JFrame myFrame;
    JTextArea textArea;
    JScrollPane scrollPanel;
    JMenuBar MenuBar;
    JMenu fileMenu, editMenu, formatMenu, colorMenu , fontMenu, sizeMenu;
    JMenuItem iNew, iOpen, iSave, iSaveAs,iExit, iRename;
    JMenuItem iRedo, iUndo;
    JMenuItem iWrap, iBlack, iWhite, iBlue, iSepia;
    JMenuItem iFont1,iFont2,iFont3,iFont4,iFont5;
    JMenuItem iSize1,iSize2,iSize3,iSize4,iSize5;

    boolean wordWrapOn=false;

    Function_File File = new Function_File(this);
    Function_Format Format= new Function_Format(this);
    Function_Color Color = new Function_Color(this);
    Function_Edit Edit =new Function_Edit(this);
    KeyHandler kHandler= new KeyHandler(this);

    UndoManager um = new UndoManager();

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new Main();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public Main(){

        createPanel();
        createTextarea();
        createMenuBar();
        createFileMenuItem();
        createEditMenuItem();
        createFormatMenuItem();
        createColorMenuItem();
        createFontMenuItem();
        createSizeMenuItem();

        Format.selectedFont ="Times New Roman";
        Format.font(16);
        Color.setColor("White");
        myFrame.setVisible(true);
    }

    public void createPanel(){
        myFrame = new JFrame("Notepad");
        myFrame.setSize(800, 600);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void createTextarea(){
        textArea = new JTextArea();
        textArea.setMargin(new Insets(5, 5, 5, 5));

        textArea.addKeyListener(kHandler);

        textArea.getDocument().addUndoableEditListener(
                new UndoableEditListener() {
                    @Override
                    public void undoableEditHappened(UndoableEditEvent e) {
                        um.addEdit(e.getEdit());
                    }
                }
        );

        scrollPanel= new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setBorder(BorderFactory.createEmptyBorder());
        myFrame.add(scrollPanel);
    }

    public void createMenuBar(){
        MenuBar = new JMenuBar();
        myFrame.setJMenuBar(MenuBar);

        fileMenu = new JMenu("File");
        MenuBar.add(fileMenu);

        editMenu = new JMenu("Edit");
        MenuBar.add(editMenu);

        formatMenu = new JMenu("Format");
        MenuBar.add(formatMenu);

        colorMenu = new JMenu("Background Color");
        MenuBar.add(colorMenu);
    }

    public void createFileMenuItem(){
        iNew = new JMenuItem("New");
        fileMenu.add(iNew);
        iNew.addActionListener(this);
        iNew.setActionCommand("New");

        iOpen = new JMenuItem("Open");
        fileMenu.add(iOpen);
        iOpen.addActionListener(this);
        iOpen.setActionCommand("Open");

        iSave = new JMenuItem("Save");
        fileMenu.add(iSave);
        iSave.addActionListener(this);
        iSave.setActionCommand("Save");


        iSaveAs = new JMenuItem("Save As");
        fileMenu.add(iSaveAs);
        iSaveAs.addActionListener(this);
        iSaveAs.setActionCommand("SaveAs");

        iRename = new JMenuItem("Rename");
        fileMenu.add(iRename);
        iRename.addActionListener(this);
        iRename.setActionCommand("Rename");

        iExit = new JMenuItem("Exit");
        fileMenu.add(iExit);
        iExit.addActionListener(this);
        iExit.setActionCommand("Exit");
    }
    public void createEditMenuItem(){
        iUndo= new JMenuItem("Undo");
        editMenu.add(iUndo);
        iUndo.addActionListener(this);
        iUndo.setActionCommand("Undo");

        iRedo= new JMenuItem("Redo");
        editMenu.add(iRedo);
        iRedo.addActionListener(this);
        iRedo.setActionCommand("Redo");
    }

    public void createFormatMenuItem(){
        iWrap = new JMenuItem(" Word Wrap: Off");
        formatMenu.add(iWrap);
        iWrap.addActionListener(this);
        iWrap.setActionCommand("Word Wrap");

        fontMenu = new JMenu("Fonts");
        formatMenu.add(fontMenu);

        sizeMenu = new JMenu("Size");
        formatMenu.add(sizeMenu);

    }

    public void createFontMenuItem(){
        iFont1=new JMenuItem("Times New Roman");
        fontMenu.add(iFont1);
        iFont1.addActionListener(this);
        iFont1.setActionCommand("Times New Roman");

        iFont2=new JMenuItem("Verdana");
        fontMenu.add(iFont2);
        iFont2.addActionListener(this);
        iFont2.setActionCommand("Verdana");

        iFont3=new JMenuItem("Impact");
        fontMenu.add(iFont3);
        iFont3.addActionListener(this);
        iFont3.setActionCommand("Impact");

        iFont4=new JMenuItem("Arial");
        fontMenu.add(iFont4);
        iFont4.addActionListener(this);
        iFont4.setActionCommand("Arial");

        iFont5=new JMenuItem("Sans Serif");
        fontMenu.add(iFont5);
        iFont5.addActionListener(this);
        iFont5.setActionCommand("Sans Serif");
    }

    public void createSizeMenuItem(){
        iSize1 = new JMenuItem("8");
        sizeMenu.add(iSize1);
        iSize1.addActionListener(this);
        iSize1.setActionCommand("8");

        iSize2 = new JMenuItem("12");
        sizeMenu.add(iSize2);
        iSize2.addActionListener(this);
        iSize2.setActionCommand("12");

        iSize3 = new JMenuItem("16");
        sizeMenu.add(iSize3);
        iSize3.addActionListener(this);
        iSize3.setActionCommand("16");

        iSize4 = new JMenuItem("20");
        sizeMenu.add(iSize4);
        iSize4.addActionListener(this);
        iSize4.setActionCommand("20");

        iSize5 = new JMenuItem("24");
        sizeMenu.add(iSize5);
        iSize5.addActionListener(this);
        iSize5.setActionCommand("24");
    }

    public void createColorMenuItem(){
        iBlack = new JMenuItem("Black");
        colorMenu.add(iBlack);
        iBlack.addActionListener(this);
        iBlack.setActionCommand("Black");

        iWhite = new JMenuItem("White");
        colorMenu.add(iWhite);
        iWhite.addActionListener(this);
        iWhite.setActionCommand("White");

        iBlue = new JMenuItem("Dark Blue");
        colorMenu.add(iBlue);
        iBlue.addActionListener(this);
        iBlue.setActionCommand("Blue");

        iSepia = new JMenuItem("Sepia");
        colorMenu.add(iSepia);
        iSepia.addActionListener(this);
        iSepia.setActionCommand("Sepia");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String command=e.getActionCommand();

        switch (command){
            case "New":
                 File.newFile();
                break;
            case "Open":
                File.open();
                break;
            case "SaveAs":
                File.saveAs();
                break;
            case "Save":
                File.save();
                break;
            case "Exit":
                File.exit();
                break;
            case "Rename":
                File.rename();
                break;
            case "Undo":
                Edit.Undo();
                break;
            case "Redo":
                Edit.Redo();
                break;
            case "Word Wrap":
                Format.wrap();
                break;
            case "8":
                Format.font(8);
                break;
            case "12":
                Format.font(12);
                break;
            case "16":
                Format.font(16);
                break;
            case "20":
                Format.font(20);
                break;
            case "24":
                Format.font(24);
                break;
            case "Arial":
                Format.setFont("Arial");
                break;
            case "Times New Roman":
                Format.setFont("Times New Roman");
                break;
            case "Impact":
                Format.setFont("Impact");
                break;
            case "Verdana":
                Format.setFont("Verdana");
                break;
            case "Sans Serif":
                Format.setFont("Sans Serif");
                break;
            case "White":
                Color.setColor("White");
                break;
            case "Black":
                Color.setColor("Black");
                break;
            case "Blue":
                Color.setColor("Blue");
                break;
            case "Sepia":
                Color.setColor("Sepia");
                break;
        }
    }
}
