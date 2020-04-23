package MainPack.pack.Gui;

import MainPack.pack.DAO.OrderCourseDAO;
import MainPack.pack.DAO.OrderDAO;
import MainPack.pack.Entity.Order;
import MainPack.pack.Entity.OrderCourse;
import MainPack.pack.Entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class ChefController {
    @FXML Label ingredient_name_1;
    @FXML Label ingredient_weight_1;
    @FXML Label ingredient_name_2;
    @FXML Label ingredient_weight_2;
    @FXML Label ingredient_name_3;
    @FXML Label ingredient_weight_3;

    @FXML Button change_order_status;
    @FXML Button show_all_orders;
    @FXML Button show_not_received;

    @FXML Label username_field;
    @FXML Label id_field;

    @FXML TableView<Order> orders_table;
    @FXML TableColumn<Order, Integer> orderID_column;
    @FXML TableColumn<Order, String>  client_name_column;
    @FXML TableColumn<Order, String>  status_column;
    @FXML TableColumn<Order, Date>  order_time_column;

    @FXML TableView<OrderCourse> course_table;
    @FXML TableColumn<OrderCourse, Integer> course_orderID_column;
    @FXML TableColumn<OrderCourse, String> course_name_column;

    private User user = new User();
    private OrderCourse orderCourse = new OrderCourse();
    private OrderDAO orderDAO = new OrderDAO();
    private OrderCourseDAO orderCourseDAO = new OrderCourseDAO();

    private List<Order> orderList = orderDAO.getAll();
    private ObservableList<Order> obs_orderList = FXCollections.observableArrayList(orderList);

    public ChefController() throws Exception {
    }

    @FXML
    private void initialize() {
        orderID_column.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderID"));
        client_name_column.setCellValueFactory(new PropertyValueFactory<Order, String>("clientName"));
        status_column.setCellValueFactory(new PropertyValueFactory<Order, String>("status"));
        order_time_column.setCellValueFactory(new PropertyValueFactory<Order, Date>("time"));

        course_orderID_column.setCellValueFactory(new PropertyValueFactory<OrderCourse, Integer>("orderID"));
        course_name_column.setCellValueFactory(new PropertyValueFactory<OrderCourse, String>("nameCourse"));

        orders_table.setItems(obs_orderList);
    }

    void loadUserData(User user) {
        this.user = user;

        username_field.setText(user.getLogin());
        id_field.setText(""+user.getId());
    }

    @FXML
    private void displayIngredientsData() {
        OrderCourse orderCourse = course_table.getSelectionModel().getSelectedItem();

        if( orderCourse != null) {
            ingredient_name_1.setText(orderCourse.getIngredient1());
            ingredient_name_2.setText(orderCourse.getIngredient2());
            ingredient_name_3.setText(orderCourse.getIngredient3());

            ingredient_weight_1.setText(""+orderCourse.getWeight1());
            ingredient_weight_2.setText(""+orderCourse.getWeight2());
            ingredient_weight_3.setText(""+orderCourse.getWeight3());
        }
    }

    @FXML
    private void showOrderCourse() throws Exception {
        int ID = orders_table.getSelectionModel().getSelectedItem().getOrderID();
        List<OrderCourse> orderCourseList = orderCourseDAO.getAllById(ID);
        ObservableList<OrderCourse> obs_orderCourseList = FXCollections.observableArrayList(orderCourseList);

        course_table.setItems(obs_orderCourseList);
    }

    @FXML
    private void showAllOrders() throws Exception {
        orderList = orderDAO.getAll();
        obs_orderList = FXCollections.observableArrayList(orderList);

        orders_table.setItems(obs_orderList);
    }

    @FXML
    private void showNotReceived() throws Exception {
        orderList = orderDAO.getAllOrdered("Ordered");
        if( orderList.size() >= 1) {
            obs_orderList = FXCollections.observableArrayList(orderList);
            orders_table.setItems(obs_orderList);
        }
        else dialogMessage("Not data","There is not data :(","No data. Looks like" +
                " the matches in the database do not exist, or they were deleted.", Alert.AlertType.WARNING);
    }

    @FXML
    private void changeStatus() throws Exception {
        if( confirmChange()) {
            Order order = orders_table.getSelectionModel().getSelectedItem();
            if (orderDAO.get(order.getOrderID()).getStatus().equals("Ordered")) {
                order.setStatus("Received");
                order.setChefName(user.getLogin());

                orderDAO.updateStatus(order);
            }
            else dialogMessage("Status Error","This order has already been confirmed.","\n" +
                    "Perhaps the data is out of date, update the table and confirm another order.", Alert.AlertType.WARNING);
        }
    }

    private void dialogMessage(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    private boolean confirmChange() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Change Status");
        alert.setHeaderText("Are you sure you want to confirm the order?");
        alert.setContentText("When you confirm the order, its status changes and notifies" +
                " the visitor that his order is already ready for delivery.");

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null) {
            return true;
        } else if (option.get() == ButtonType.OK) {
            return true;
        } else return option.get() != ButtonType.CANCEL;
    }

}
