package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


public class ModelsController {

    @FXML
    public Button idBackButton;
    @FXML
    private TableView<Model> modelsTable;
    @FXML
    private TableColumn<Model, String> models;
    @FXML
    private TableColumn select;

    private ObservableList<Model> listItems;




    @FXML
    public void initialize(){
        List<File> Listfiles = loadModels();
        for (File f : Listfiles){
            System.out.println(f.getName());
            listItems = FXCollections.observableArrayList(new Model(f.getPath(),f.getName()));
        }


        models = new TableColumn<Model, String>("Models");
        select = new TableColumn("Action");
        select.setMinWidth(200);
        select.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Model, CheckBox>, ObservableValue<CheckBox>>() {

            @Override
            public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<Model, CheckBox> modelCheckBoxCellDataFeatures) {
                Model model = modelCheckBoxCellDataFeatures.getValue();

                CheckBox checkBox = new CheckBox();

                checkBox.selectedProperty().setValue(model.isSelected());



                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {


                    public void changed(ObservableValue<? extends Boolean> ov,
                                        Boolean old_val, Boolean new_val) {

                        model.setSelected(new_val);

                    }
                });

                return new SimpleObjectProperty<CheckBox>(checkBox);
            }
        });

        modelsTable.setEditable(true);

        models.setCellValueFactory(new PropertyValueFactory<Model,String>("name"));

        modelsTable.setItems(listItems);
        modelsTable.getColumns().addAll(models,select);

    }


    // Add event handlers
    @FXML
    private void deleteAction(ActionEvent action){
        listItems.forEach((model) -> {
            if(model.isSelected()) {
                try {
                    boolean result = Files.deleteIfExists(Paths.get(model.getPath()));
                    if (result) {
                        System.out.println("File is deleted!");
                    } else {
                        System.out.println("Sorry, unable to delete the file.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    public List<File> loadModels(){
        String dirLocation = "./resources/models/";

        try {
            List<File> files = Files.list(Paths.get(dirLocation))
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            return files;
        } catch (IOException e) {
            System.out.println("directory doesn't exist");
        }
        return null;
    }

    public void backButtonActionPerformed(ActionEvent actionEvent) throws IOException {
        // Get the game scene
        Parent gameRoot = FXMLLoader.load(getClass().getResource("../application/settings.fxml"));
        Scene gameScene = new Scene(gameRoot);

        // Get the stage
        Stage window = (Stage) idBackButton.getScene().getWindow();

        // Set the game scene to the stage
        window.setScene(gameScene);
        window.show();
        window.setTitle("Tic-Tac-Toe");
    }
}
