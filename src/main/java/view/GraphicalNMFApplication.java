package view;

import adapters.IAdapterFactory;
import controllers.DatabaseController;
import controllers.ExecutionResult;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;

public class GraphicalNMFApplication extends Application
{
    public static final String RESSOURCE_PATH = "./src/main/resources/";      //fixme

    private DatabaseView databaseView;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        //Create the Opening Window
        try
        {
            //TODO Move to a launcher class
            OpeningWindow openingWindow = new OpeningWindow(this);
            openingWindow.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, e.getMessage());
            this.stop();
        }
    }

    public void open(IAdapterFactory factory)
    {
        DatabaseController dbController = DatabaseController.initialize(factory);

        //Create the ModelView
        try
        {
            databaseView = new DatabaseView(dbController);
            databaseView.show();

            databaseView.askForConnectionToDatabase();
        }
        catch(FXMLException e)
        {
            showAlert(Alert.AlertType.ERROR, e.getMessage());
        }
    }


    //region Utils

    public static Optional<ButtonType> showAlert(Alert.AlertType type, String message, String title)
    {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setGraphic(null);
        alert.setHeaderText(null);

        return alert.showAndWait();
    }

    public static Optional<ButtonType> showAlert(Alert.AlertType type, String message)
    {
        return showAlert(type, message, type.name());
    }

    public static boolean askForConfirmation(String message)
    {
        Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, message);

        if(result.isPresent())
        {
            ButtonBar.ButtonData data = result.get().getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE;
        }

        return false;
    }

    /**
     * Handle the result of a command execution. <br>
     * Show an alert if the execution was a failure.
     *
     * @param result The result of the execution
     */
    public static void handleExecutionResult(ExecutionResult result)
    {
        if(result.getResult() == ExecutionResult.Result.Failure)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, result.getMessage());
            alert.setGraphic(null);
            alert.setHeaderText(null);

            //FIXME needs better implementation
            if(result.getCause() != null)
            {
                alert.setTitle(result.getCause().getClass().getSimpleName());
                alert.getDialogPane().setExpandableContent(
                        new Label(Arrays.toString(result.getCause().getStackTrace())));

                //Print the stack trace in the console
                result.getCause().printStackTrace();
            }

            alert.showAndWait();
        }
    }

    /**
     * Load an FXML file
     * @param file The file
     * @param controller The controller to use, can be null
     * @return A Parent
     * @throws IOException If the loading fails
     */
    public static Parent loadFXML(File file, Object controller) throws IOException
    {
        URL xml = file.toURI().toURL();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(xml);
        if(controller != null) loader.setController(controller);

        return loader.load();
    }

    //endregion
}
