public class Function_Edit {
    Main main;

    public Function_Edit(Main main){
        this.main=main;
    }

    public void Undo(){
        main.um.undo();
    }

    public void Redo(){
        main.um.redo();
    }
}
