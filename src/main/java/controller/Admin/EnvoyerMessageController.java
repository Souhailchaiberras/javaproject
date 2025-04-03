package controller.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;






public class EnvoyerMessageController {

    @FXML
    private TextArea champMessage;

    @FXML
    private TextField champEmailDestinataire;

    @FXML
    private Button sendButton;

    @FXML
    private Button cancelButton;

    private static final String EMAILJS_SERVICE_ID = "service_h410d6b";
    private static final String EMAILJS_TEMPLATE_ID = "template_bg32h3q";
    private static final String EMAILJS_USER_ID = "skeQt12FrPchtW2We";




    @FXML
    private void envoyerMessage() {
        String message = champMessage.getText();
        String emailDestinataire = champEmailDestinataire.getText();
        String emailExpediteur = "chaiberrassouhail@gmail.com"; // Adresse email de l'expéditeur
        String motDePasseExpediteur = "ctidxqtdlbyjgumg"; // Mot de passe de l'expéditeur (utilisez un mot de passe applicatif si nécessaire)

        if (message.isEmpty()) {
            showAlert(AlertType.WARNING, "Champ de message vide", "Veuillez entrer un message avant d'envoyer.");
        } else if (emailDestinataire.isEmpty()) {
            showAlert(AlertType.WARNING, "Email du destinataire vide", "Veuillez entrer un email pour le destinataire.");
        } else {
            // Propriétés de configuration SMTP
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com"); // Serveur SMTP de Gmail
            props.put("mail.smtp.port", "587"); // Port SMTP

            // Authentification de l'expéditeur
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailExpediteur, motDePasseExpediteur);
                }
            });

            try {
                // Création du message
                Message email = new MimeMessage(session);
                email.setFrom(new InternetAddress(emailExpediteur));
                email.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestinataire));
                email.setSubject("Nouveau message de votre application"); // Sujet de l'email
                email.setText(message); // Corps du message

                // Envoi du message
                Transport.send(email);

                // Afficher une confirmation
                showAlert(AlertType.INFORMATION, "Message envoyé", "Votre message a été envoyé avec succès.");
                champMessage.clear();

            } catch (MessagingException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Erreur d'envoi", "Une erreur est survenue lors de l'envoi de l'email : " + e.getMessage());
            }
        }
    }

    public void setEmailDestinataire(String email) {
        champEmailDestinataire.setText(email);
    }

    @FXML
    private void onCancelButtonClick() {
        champMessage.clear();
    }

    @FXML
    private void onMouseEnterSendButton(MouseEvent event) {
        sendButton.setStyle("-fx-background-color: #45A049;");
    }

    @FXML
    private void onMouseExitSendButton(MouseEvent event) {
        sendButton.setStyle("-fx-background-color: #4CAF50;");
    }

    @FXML
    private void onMouseEnterCancelButton(MouseEvent event) {
        cancelButton.setStyle("-fx-background-color: #F44336;");
    }

    @FXML
    private void onMouseExitCancelButton(MouseEvent event) {
        cancelButton.setStyle("-fx-background-color: #f44336;");
    }


    private void loadScene(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur de chargement",
                    "Impossible de charger la page : " + e.getMessage());
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onStatistiqueButtonClick(ActionEvent event) {
        loadScene("/vues/ADMIN/AdminPageInit.fxml", event);
    }

    @FXML
    private void onGestiondesProfesseurButtonClick(ActionEvent event) {
        loadScene("/vues/ADMIN/GestionProfesseurs/gestionProf.fxml", event);
    }

    @FXML
    private void onGestiondesSecretaireButtonClick(ActionEvent event) {
        loadScene("/vues/ADMIN/gestiondessecretaire.fxml", event);
    }

    @FXML
    private void onGestiondesEtudiantButtonClick(ActionEvent event) {
        loadScene("/vues/ADMIN/etudiantmanagment.fxml", event);
    }

    @FXML
    private void onGestiondesModulesButtonClick(ActionEvent event) {
        loadScene("/vues/ADMIN/GestionModule/GestionModule.fxml", event);
    }

    @FXML
    private void onDesconnected(ActionEvent event) {
        loadScene("/vues/login.fxml", event);
    }
}


