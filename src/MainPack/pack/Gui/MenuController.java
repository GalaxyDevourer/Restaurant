package MainPack.pack.Gui;

import MainPack.pack.DAO.*;
import MainPack.pack.Entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuController {
    // object of classes and DAO`s
    private User user = new User();
    private PositionDAO posdao = new PositionDAO();
    private CourseDAO coursedao = new CourseDAO();
    private IngredientDAO ingdao = new IngredientDAO();
    private OrderDAO orderdao = new OrderDAO();
    private OrderCourseDAO ordercoursedao = new OrderCourseDAO();

    // object for Order and list of OrderCourse
    private Order main_order;
    private List<OrderCourse> orderCourseList = new ArrayList<>();

    // object for selected course
    private Course selected_course;

    // left field items
    @FXML Label username_field;
    @FXML Label id_field;
    @FXML Label total_price_field;
    @FXML Label price_field;

    // order field items
    @FXML Button finish_order_button;
    @FXML TextField ing_weight_edit_1;
    @FXML TextField ing_weight_edit_2;
    @FXML TextField ing_weight_edit_3;
    @FXML Label ing_name_1;
    @FXML Label ing_name_2;
    @FXML Label ing_name_3;
    @FXML Label ing_portion_1;
    @FXML Label ing_price_1;
    @FXML Label ing_portion_2;
    @FXML Label ing_price_2;
    @FXML Label ing_portion_3;
    @FXML Label ing_price_3;
    @FXML Button confirm_dial_button;
    @FXML Label price_dial_field;

    // tables
    @FXML TableView<Position> position_table;
    @FXML TableColumn<Position, String> positionNameColumn;

    @FXML TableView<Course> course_table;
    @FXML TableColumn<Course, String> courseNameColumn;
    @FXML TableColumn<Course, String> ing1Column;
    @FXML TableColumn<Course, String> ing2Column;
    @FXML TableColumn<Course, String> ing3Column;

    @FXML TableView<OrderDisplay> order_table;
    @FXML TableColumn<OrderDisplay, String> orderCourseColumn;
    @FXML TableColumn<OrderDisplay, Integer> priceColumn;

    // lists for tables
    private List<Position> positionList = posdao.getAll();
    private ObservableList<Position> obs_pos_list = FXCollections.observableArrayList(positionList);

    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public MenuController() throws Exception {
    }

    @FXML
    void initialize() {
        positionNameColumn.setCellValueFactory(new PropertyValueFactory<Position, String>("positionName"));

        courseNameColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("courseName"));
        ing1Column.setCellValueFactory(new PropertyValueFactory<Course, String>("ingredient1"));
        ing2Column.setCellValueFactory(new PropertyValueFactory<Course, String>("ingredient2"));
        ing3Column.setCellValueFactory(new PropertyValueFactory<Course, String>("ingredient3"));

        orderCourseColumn.setCellValueFactory(new PropertyValueFactory<OrderDisplay, String>("courseName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<OrderDisplay, Integer>("coursePrice"));

        position_table.setItems(obs_pos_list);

        price_field.setText("0");
        price_dial_field.setText("0");
    }

    @FXML
    private void chooseCoursePosition() throws Exception {
        String position = position_table.getSelectionModel().getSelectedItem().getPositionName();

        List<Course> courseList = coursedao.getAllByPos(position);
        ObservableList<Course> obs_course_list = FXCollections.observableArrayList(courseList);

        course_table.setItems(obs_course_list);
    }

    private void changeDialData(Ingredient ing, Label name, Label portion, Label price) {
        name.setText(ing.getIngredientName());
        portion.setText(""+ing.getPortion());
        price.setText(""+ing.getPrice());
    }

    @FXML
    private void finishOrder() throws Exception {
        if (orderCourseList.size() >= 1) {
            long millis = System.currentTimeMillis();
            Date date = new java.sql.Date(millis);

            Order order = new Order(user.getLogin(), "", Integer.parseInt(price_field.getText()), "" +
                    "Ordered", "", date);
            int id = orderdao.insertInt(order);

            RoomController.setID(id);

            for (OrderCourse o: orderCourseList) {
                o.setOrderID(id);
                ordercoursedao.insert(o);
            }

            stage.close();
        }
        else dialogMessage("No order error","You did not order anything :(",
                "You cannot make an empty order! If you want to exit the menu - just close this window.", Alert.AlertType.WARNING);
    }

    private void setTotalPrice() {
        int price = 0;
        for(int i = 0; i < order_table.getItems().size(); i++) {
            price += order_table.getItems().get(i).getCoursePrice();
        }

        price_field.setText(""+price);
    }

    private int calculateCoursePrice() {
        int part1, part2, part3;

        // (user_weight / portion_weight) * portion_price)
        part1 = (Integer.parseInt(ing_weight_edit_1.getText()) / Integer.parseInt(ing_portion_1.getText())) * Integer.parseInt(ing_price_1.getText());
        part2 = (Integer.parseInt(ing_weight_edit_2.getText()) / Integer.parseInt(ing_portion_2.getText())) * Integer.parseInt(ing_price_2.getText());
        part3 = (Integer.parseInt(ing_weight_edit_3.getText()) / Integer.parseInt(ing_portion_3.getText())) * Integer.parseInt(ing_price_3.getText());

        return part1 + part2 + part3;
    }

    @FXML
    private boolean changeDialPrice() {
        try {
            int price = calculateCoursePrice();
            price_dial_field.setText("" + price);

            return true;
        }
        catch (Exception e) {
            dialogMessage("Error in calculating", "It seems that the program was unable to calculate the price of your order.",
                    "Please enter the correct data to calculate the order price. This should be an integer non-negative number without various characters, including letters.", Alert.AlertType.WARNING);

            return false;
        }
    }

    @FXML
    private void confirmDial() {
        boolean correct_calculation = changeDialPrice();

        if ( selected_course != null && correct_calculation) {
            OrderCourse orderCourse = new OrderCourse(0, selected_course.getCourseName(), selected_course.getIngredient1(), Integer.parseInt(ing_weight_edit_1.getText()),
                    selected_course.getIngredient2(), Integer.parseInt(ing_weight_edit_2.getText()), selected_course.getIngredient3(), Integer.parseInt(ing_weight_edit_3.getText()));
            orderCourseList.add(orderCourse);

            int price = Integer.parseInt(price_dial_field.getText());
            OrderDisplay orderDisplay = new OrderDisplay(orderCourse.getNameCourse(), price);
            order_table.getItems().add(orderDisplay);

            setTotalPrice();
        }
    }

    @FXML
    private void chooseIngredientsInCourse() throws Exception {
        // object for selected course
        selected_course = course_table.getSelectionModel().getSelectedItem();

        // object for ingredients
        Ingredient first_ing = ingdao.get(selected_course.getIngredient1());
        Ingredient second_ing = ingdao.get(selected_course.getIngredient2());
        Ingredient third_ing = ingdao.get(selected_course.getIngredient3());

        changeDialData(first_ing, ing_name_1, ing_portion_1, ing_price_1);
        changeDialData(second_ing, ing_name_2, ing_portion_2, ing_price_2);
        changeDialData(third_ing, ing_name_3, ing_portion_3, ing_price_3);
    }

    void loadUser(User thisuser) {
        user = thisuser;

        username_field.setText("Username: " + user.getLogin());
        id_field.setText("ID: " + user.getId());
    }

    private void dialogMessage(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    boolean leaveFromMenu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you want to exit the menu?");
        alert.setContentText("If you exit the menu, the order creation progress will not be saved." +
                " In this case, you will need to create a new order again!");

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null) {
            return true;
        } else if (option.get() == ButtonType.OK) {
            return true;
        } else return option.get() != ButtonType.CANCEL;
    }
}
