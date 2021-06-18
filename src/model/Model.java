package model;

public class Model {

    private String path;
    private String name;
    private boolean isSelected=false;

    public Model(String path, String name){
        this.path=path;
        this.name=name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected(){
        return this.isSelected;
    }

    public void setSelected(boolean bool){
        this.isSelected=bool;
    }
}