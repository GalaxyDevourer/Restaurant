package MainPack.pack.Gui;

import MainPack.pack.DAO.OrderDAO;
import MainPack.pack.DAO.RoomDAO;
import MainPack.pack.Entity.Order;
import MainPack.pack.Entity.Seat;
import MainPack.pack.Entity.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RoomController {
    @FXML Label table_status;
    @FXML Label username_field;
    @FXML Label id_field;
    @FXML AnchorPane seat_field;
    @FXML Button open_menu_button;
    @FXML Button check_status_button;
    @FXML Button feedback_button;
    @FXML Label order_status_field;
    @FXML Button seat_button_1;
    @FXML Button seat_button_2;
    @FXML Button seat_button_3;
    @FXML Button seat_button_4;
    @FXML Button seat_button_5;
    @FXML Button seat_button_6;
    @FXML Button seat_button_7;
    @FXML Button seat_button_8;

    @FXML Label table_status_1;
    @FXML Label table_status_2;
    @FXML Label table_status_3;
    @FXML Label table_status_4;
    @FXML Label table_status_5;
    @FXML Label table_status_6;
    @FXML Label table_status_7;
    @FXML Label table_status_8;

    @FXML
    private List <Label> labelList;

    private static final String PLACETAKENMESSAGE = "\n" +
            "Sorry, this place is currently taken. Take another or wait!";
    private static final String ALREADYTAKEN = "Sorry, you have already taken one place. " +
            "Please release him (leave the room) to transfer to another!";
    private static final String SEATNOTTACKEN = "Sorry, you did not take up space in the room." +
            " Please sit at a table and then you can order a menu!";

    private User user = new User();
    private Order order;
    private static int orderID;
    private Seat seat;
    private RoomDAO roomDAO = new RoomDAO();
    private OrderDAO orderDAO = new OrderDAO();

    static void setID(int id) {
        orderID = id;
    }

    private void loadMenuWindow(User thisuser) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = loader.load();

        MenuController menu = loader.getController();
        menu.loadUser(user);

        Scene scene = new Scene(root, 1100, 500);
        Stage stage = new Stage();
        menu.setStage(stage);
        stage.setScene(scene);
        stage.setTitle("Your Menu!");
        stage.setOnCloseRequest(event -> {
            try {
                boolean answer = menu.leaveFromMenu();
                if (!answer ) event.consume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        stage.show();

    }

    void initLabel() {
        labelList = Arrays.asList(table_status_1, table_status_2, table_status_3, table_status_4,
                table_status_5, table_status_6, table_status_7, table_status_8);
    }

    void leaveFromRoom() throws Exception {
        if (seat != null) {
            roomDAO.delete(user.getLogin());
        }
    }

    void loadUser(User thisuser) {
        user = thisuser;

        username_field.setText("Username: " + user.getLogin());
        id_field.setText("ID: " + user.getId());
    }

    private void changeTextSeat(Label seat, int place, String status) {
        seat.setText("Table " + place + ": " + status);
    }

    private void dialogMessage(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private void takeASeat(Button button, Integer seat_number) throws Exception {
        if ( roomDAO.get(seat_number) == null) {
            if ( roomDAO.get(user.getLogin()) == null) {
                seat = new Seat(seat_number, user.getLogin());
                roomDAO.insert(seat);

                changeTextSeat(labelList.get(seat_number - 1), seat_number, "[Engaged]");
                table_status.setText("Table status: [Engaged]");
            }
            else dialogMessage("Room Manager", "\n" +
                    "\n" +
                    "You have already taken a place", ALREADYTAKEN);
        }
        else dialogMessage("Room Manager", "\n" +
                "Place taken", PLACETAKENMESSAGE);
    }

    @FXML
    private void openMenu() throws Exception {
        if ( seat != null) {
            loadMenuWindow(user);
        }
        else dialogMessage("Room Manager", "You did not take a seat", SEATNOTTACKEN);
    }

    private void rateService() {
        List<String> choices = new ArrayList<>();
        choices.add("Awful");
        choices.add("Bad");
        choices.add("Good");
        choices.add("Fine");
        choices.add("Perfectly");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Good", choices);
        dialog.setTitle("Rating Menu");
        dialog.setHeaderText("Please rate the service!");
        dialog.setContentText("Use this button:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> {
            order.setRate(s);
            try {
                orderDAO.updateRate(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void giveFeedback() throws Exception {
        if (orderID == 0) {
            dialogMessage("Not ordered","You have not placed an order!",
                    "This function is available only after placing an order!");
        } else {
            order = orderDAO.get(orderID);
            if (order.getStatus().equals("Received")) {
                if (order.getRate().equals("")) {
                    rateService();
                }
                else dialogMessage("Already rated!","The service for this order has already been rated!",
                        "Sorry, service rating is only available once!");
            }
            else dialogMessage("The order is not ready yet!","Wait, your order is still being processed!",
                    "You can rate the service only after receiving your order!");
        }
    }

    @FXML
    private void checkStatus() throws Exception {
        if (orderID == 0) {
            dialogMessage("Not ordered","You have not placed an order!",
                    "This function is available only after placing an order!");
        } else {
            order = orderDAO.get(orderID);
            if (order.getStatus().equals("Received")) {
                order_status_field.setText("Order status: [Received]");
                dialogMessage("Order received!","Your order is ready and will be issued soon!",
                        "If you liked the service, you can rate it by clicking on the button 'Give feedback'");
            }
            else dialogMessage("The order is not ready yet!","Wait, your order is still being processed!",
                    "Please wait for your order. If you want to rate the service, you can click on the button 'Give feedback'");
        }
    }

    @FXML
    private void checkFreeTable() throws Exception {
        for (int i = 0; i < labelList.size(); i++) {
            changeTextSeat(labelList.get(i), i+1, "[Free]");
        }
        loadSeat();
    }

    void loadSeat() throws Exception {
        List<Seat> seatList = roomDAO.getAll();
        for (Seat value : seatList) {
            changeTextSeat(labelList.get(value.getSeat() - 1), value.getSeat(), "[Engaged]");
        }
    }

    @FXML
    private void seatButton1() throws Exception {
        takeASeat(seat_button_1, 1);
    }

    @FXML
    private void seatButton2() throws Exception {
        takeASeat(seat_button_2, 2);
    }

    @FXML
    private void seatButton3() throws Exception {
        takeASeat(seat_button_3, 3);
    }

    @FXML
    private void seatButton4() throws Exception {
        takeASeat(seat_button_4, 4);
    }

    @FXML
    private void seatButton5() throws Exception {
        takeASeat(seat_button_5, 5);
    }

    @FXML
    private void seatButton6() throws Exception {
        takeASeat(seat_button_6, 6);
    }

    @FXML
    private void seatButton7() throws Exception {
        takeASeat(seat_button_7, 7);
    }

    @FXML
    private void seatButton8() throws Exception {
        takeASeat(seat_button_8, 8);
    }
}
