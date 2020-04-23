package MainPack.pack.Gui;

import MainPack.pack.DAO.UserDAO;
import MainPack.pack.Entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationController {
    private UserDAO userdao = new UserDAO();

    public AnchorPane reg_field;
    public Button reg_button;
    public Label regtext_start;
    public ChoiceBox<String> reg_choice_role;
    public TextField reg_enter_name;
    public TextField sign_enter_name;
    public Button sign_in;
    public PasswordField reg_enter_password;
    public PasswordField sign_enter_password;
    public Label closepanel_text;

    private ObservableList<String> roles = FXCollections.observableArrayList("Visitor", "Chef", "Director");

    private void dialogRegMessage(String title, String header, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private void loadNewUserWindow(User thisuser) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("room.fxml"));
        Parent root = loader.load();

        RoomController room = loader.getController();
        room.loadUser(thisuser);
        room.initLabel();
        room.loadSeat();

        Scene scene = new Scene(root, 800, 500);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Room Menu!");
        stage.setOnHidden(event -> {
            try {
                room.leaveFromRoom();
           } catch (Exception e) {
                e.printStackTrace();
           }
        });
        stage.show();

    }

    private void loadNewChefWindow(User thisuser) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chef.fxml"));
        Parent root = loader.load();

        ChefController chef = loader.getController();
        chef.loadUserData(thisuser);

        Scene scene = new Scene(root, 1000, 500);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Chef Menu!");
        stage.show();

    }

    private void loadNewDirectorWindow(User thisuser) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("director.fxml"));
        Parent root = loader.load();

        DirectorController director = loader.getController();
        director.loadUserData(thisuser);

        Scene scene = new Scene(root, 1000, 500);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Director Menu!");
        stage.show();

    }

    // test function to add some items in DB tables
    private void addSomeItemsToDB () throws Exception {
/*
        Position pos;
        Course course;
        Ingredient ing;

        PositionDAO posdao = new PositionDAO();
        CourseDAO coursedao = new CourseDAO();
        IngredientDAO ingdao = new IngredientDAO();

        ing = new Ingredient("Mozzarella", 10,30);
        ingdao.insert(ing);

        // Salads, Desserts, Main course, Drinks
        pos = new Position("Pizza");
        posdao.insert(pos);

        course = new Course("Salads", "Greek", "Cucumber", "Tomato", "Feta");
        coursedao.insert(course);

        ing = new Ingredient("-", 0,0);
        ingdao.insert(ing);
        */
    }

    @FXML
    private void initialize() throws Exception {
        reg_choice_role.setValue("Visitor");
        reg_choice_role.setItems(roles);

        addSomeItemsToDB();
    }

    @FXML
    private void showReg() {
        reg_button.setVisible(true);
        reg_field.setVisible(true);
        closepanel_text.setVisible(true);
    }

    @FXML
    private void closeReg() {
        reg_button.setVisible(false);
        reg_field.setVisible(false);
        closepanel_text.setVisible(false);
    }

    @FXML
    private void changeViewRegText_enter() {
        if(!reg_button.isVisible()) {
            regtext_start.setUnderline(true);
            regtext_start.setText("       I want to register!       ");
        }
    }

    @FXML
    private void changeViewRegText_exit() {
        regtext_start.setUnderline(false);
        regtext_start.setText(" No account? Register now!");
    }

    @FXML
    private void changeViewCloseText_enter() {
        closepanel_text.setUnderline(true);
        closepanel_text.setText("   Close it!   ");
    }

    @FXML
    private void changeViewCloseText_exit() {
        closepanel_text.setUnderline(false);
        closepanel_text.setText("   Sign up?   ");
    }

    @FXML
    private void signIn() throws Exception {
        User visitor = new User(sign_enter_name.getText(), sign_enter_password.getText());
        User user = userdao.get(sign_enter_name.getText());

        if (visitor.equals(user) && user.getPassword().equals(sign_enter_password.getText())) {
            // open new window

            switch (user.getRole()) {
                case "Visitor":
                    loadNewUserWindow(user);
                    break;

                case "Chef":
                    loadNewChefWindow(user);
                    break;

                case "Director":
                    loadNewDirectorWindow(user);
                    break;
            }
        }
        else dialogRegMessage("Login Error","An error occurred while logging in.",
                "This user does not appear to be registered. Check your login details or use others.");
    }

    @FXML
    private void regAccount() throws Exception {
        String login = reg_enter_name.getText();
        String password = reg_enter_password.getText();
        String role = reg_choice_role.getValue();

        Pattern login_pat = Pattern.compile("(?=.*[a-z])(?=.*[A-Z]).{6,}");
        Pattern password_pat = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9@#$%]).{8,}");
        Matcher login_mat = login_pat.matcher(login);
        Matcher password_mat = password_pat.matcher(password);

        if(password.equals("") || login.equals("")) {
            dialogRegMessage("Warning Message","Registration error. Empty fields.",
                    "Please enter valid data. Fields for entering login or password cannot be empty!");
        }
        else if(password.contains(" ") || login.contains(" ")){
            dialogRegMessage("Warning Message","Registration error. Gaps found!",
                    "Please enter valid data. Fields for entering login or password should not contain spaces!");
        }
        else if(!login_mat.find() || !password_mat.find() || password.length() > 16 || login.length() > 12) {
            dialogRegMessage("Warning Message","Registration error. Incorrect data.",
                    "1. The password must contain at least one small and large letter, as well as a number " +
                            "or a special character (@ # $%) and be no shorter than 8 characters! \n\n" +
                            "2. The login must contain at least one small, capital letter and one number," +
                            " and also must be at least 6 characters!");
        }
        else {
            User newuser = new User(login, password, role);
            User user = userdao.get(login);

            if( newuser.equals(user) ) {
                dialogRegMessage("Error Message","Registration error. Already registered.",
                        "Sorry, this user is already registered in the system! Please use a different login!!");
            }
            else {
                userdao.insert(newuser);
                dialogRegMessage("Congratulation!","Successfully registration!",
                        "Thank you for registering, ! Please remember the username and password," +
                                " you will need it for subsequent logins!");
            }

        }

    }

}