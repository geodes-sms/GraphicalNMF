package utils;


import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileSelectionDialog extends Dialog<Map<String, String>>
{
    private final Stage stage;
    private final HashMap<String, String> paths = new HashMap<>();

    private VBox box;


    public FileSelectionDialog(Stage stage)
    {
        super();
        this.stage = stage;

        init();
    }

    private void init()
    {
        this.setTitle("Select files");
        this.setHeaderText(null);
        this.setGraphic(null);
        this.setResizable(true);

        this.setResultConverter(btnType -> {
            switch(btnType.getButtonData())
            {
                case APPLY:
                case OK_DONE:
                case YES:
                    return paths;

                default:
                    return null;

            }
        });

        this.getDialogPane().setPrefWidth(550);
        this.getDialogPane().setPrefHeight(Region.USE_COMPUTED_SIZE);
        this.getDialogPane().setExpandableContent(null);
        this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Node btnOk = this.getDialogPane().lookupButton(ButtonType.OK);
        if(btnOk instanceof Button)
        {
            btnOk.addEventFilter(ActionEvent.ACTION, event -> {
                if(!isValid())
                {
                    event.consume();

                    Alert alert = new Alert(Alert.AlertType.WARNING, "Missing required values!");
                    alert.setHeaderText(null);
                    alert.setGraphic(null);
                    alert.showAndWait();
                }
            });
        }

        AnchorPane root = new AnchorPane();
        this.getDialogPane().setContent(root);

        box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10);
        box.setFillWidth(true);
        AnchorPane.setTopAnchor(box, 0.0);
        AnchorPane.setRightAnchor(box, 0.0);
        AnchorPane.setBottomAnchor(box, 0.0);
        AnchorPane.setLeftAnchor(box, 0.0);
        root.getChildren().add(box);
    }

    public void addFileSelector(String label, String defaultPath, boolean isDirectory)
    {
        HBox selector = createFileSelector(label, defaultPath, isDirectory);
        box.getChildren().add(selector);
    }

    public void addFileSelector(String label)
    {
        addFileSelector(label, null, false);
    }

    public void addFileSelector(String label, boolean isDirectory)
    {
        addFileSelector(label, null, isDirectory);
    }

    private HBox createFileSelector(String label, String defaultPath, boolean isDirectory)
    {
        //Create the hbox
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        box.setFillHeight(true);
        box.setPadding(new Insets(5));
        box.setSpacing(15);
        box.setPrefHeight(Region.USE_COMPUTED_SIZE);

        //Create the label
        Label lbl = new Label(label);
        lbl.setMinSize(100, 20);
        lbl.setTextAlignment(TextAlignment.RIGHT);
        lbl.setAlignment(Pos.CENTER_RIGHT);
        box.getChildren().add(lbl);

        //Create the text field
        TextField field = new TextField(defaultPath);
        field.setPromptText("select a file");
        field.setPrefSize(225, 25);
        field.setAlignment(Pos.CENTER_LEFT);
        field.setOnAction(event -> {
            paths.put(label, field.getText());
        });
        HBox.setHgrow(field, Priority.ALWAYS);
        box.getChildren().add(field);

        //Create the button
        Button btn = new Button("browse");
        btn.setPrefSize(65, 25);
        btn.setOnAction(event -> {
            File file;

            //Let the user choose a file
            if(isDirectory)
            {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Select the " + label.toLowerCase());
                file = chooser.showDialog(stage);
            }
            else
            {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Select a " + label.toLowerCase());
                file = chooser.showOpenDialog(stage);
            }

            //Register that file
            if(file == null)
            {
                field.setText("");
            }
            else
            {
                field.setText(file.getAbsolutePath());
            }
            field.getOnAction().handle(new ActionEvent());
        });
        HBox.setHgrow(btn, Priority.NEVER);
        box.getChildren().add(btn);

        //Register the default path
        if(defaultPath != null)
        {
            paths.put(label, defaultPath);
        }
        else
        {
            paths.put(label, "");
        }

        return box;
    }

    private boolean isValid()
    {
        boolean isValid = true;

        for(String path : paths.values())
        {
            isValid = !path.isEmpty() && !path.isBlank() && isValid;
        }

        return isValid;
    }
}
