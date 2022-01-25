package geodes.sms.gnmf.application.view;

import geodes.sms.gnmf.application.controllers.ConnectionInfo;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;

public class ConnectionDialog extends Dialog<ConnectionInfo>
{
    private final TextField txtUri;
    private final TextField txtUser;
    private final TextField txtPassword;

    public ConnectionDialog(DialogPane dialogPane)
    {
        super();
        this.setTitle("Connect to Database");
        this.setDialogPane(dialogPane);

        txtUri = (TextField) dialogPane.lookup("#txtUri");
        txtUser = (TextField) dialogPane.lookup("#txtUser");
        txtPassword = (TextField) dialogPane.lookup("#txtPassword");

        Node btnOk = dialogPane.lookupButton(ButtonType.OK);
        if(btnOk instanceof Button)
        {
            btnOk.addEventFilter(ActionEvent.ACTION, event -> {
                if(!areInfoValid())
                {
                    event.consume();

                    Alert alert = new Alert(Alert.AlertType.WARNING, "Missing required values!");
                    alert.setHeaderText(null);
                    alert.setGraphic(null);
                    alert.showAndWait();
                }
            });
        }

        this.setResultConverter(btnType -> {
            switch(btnType.getButtonData())
            {
                case APPLY:
                case OK_DONE:
                    return new ConnectionInfo(txtUri.getText(), txtUser.getText(), txtPassword.getText());

                default:
                    return null;

            }
        });
    }

    public boolean areInfoValid()
    {
        if(txtUri.getText().isEmpty())
        {
            return false;
        }
        else if(txtUser.getText().isEmpty())
        {
            return false;
        }
        else if(txtPassword.getText().isEmpty())
        {
            return false;
        }

        return true;
    }
}
