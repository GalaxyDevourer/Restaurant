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
import java.util.*;

public class DirectorController {
    @FXML TableView<Order> orders_table;
    @FXML TableColumn<Order, Integer> orderID_column;
    @FXML TableColumn<Order, String> client_name_column;
    @FXML TableColumn<Order, String> chef_name_column;
    @FXML TableColumn<Order, Integer> price_column;
    @FXML TableColumn<Order, String> rate_column;
    @FXML TableColumn<Order, Date> time_column;

    @FXML TableView<OrderCourse> courses_table;
    @FXML TableColumn<OrderCourse, Integer> course_orderID_column;
    @FXML TableColumn<OrderCourse, String> course_name_column;

    @FXML Label username_field;
    @FXML Label id_field;
    @FXML Label popular_course;
    @FXML Label money_by_period;

    @FXML Button show_by_sorting;
    @FXML Button show_all_button;
    @FXML Button show_all_button1;
    @FXML ChoiceBox<String> rate_picker;
    @FXML DatePicker calendar;
    @FXML DatePicker calendar1;

    private User user = new User();
    private Order order = new Order();
    private OrderCourse orderCourse;
    private OrderDAO orderDAO = new OrderDAO();
    private OrderCourseDAO orderCourseDAO = new OrderCourseDAO();

    private List<Order> orderList = orderDAO.getAllOrdered("Received");
    private ObservableList<Order> obs_orderList = FXCollections.observableArrayList(orderList);

    public DirectorController() throws Exception {
    }

    @FXML
    void initialize() {
        orderID_column.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderID"));
        client_name_column.setCellValueFactory(new PropertyValueFactory<Order, String>("clientName"));
        chef_name_column.setCellValueFactory(new PropertyValueFactory<Order, String>("chefName"));
        price_column.setCellValueFactory(new PropertyValueFactory<Order, Integer>("price"));
        rate_column.setCellValueFactory(new PropertyValueFactory<Order, String>("rate"));
        time_column.setCellValueFactory(new PropertyValueFactory<Order, Date>("time"));

        course_orderID_column.setCellValueFactory(new PropertyValueFactory<OrderCourse, Integer>("orderID"));
        course_name_column.setCellValueFactory(new PropertyValueFactory<OrderCourse, String>("nameCourse"));

        orders_table.setItems(obs_orderList);

        ObservableList<String> rates = FXCollections.observableArrayList("All", "Awful", "Bad", "Good", "Fine", "Perfectly");
        rate_picker.setItems(rates);
        rate_picker.setValue("All");
    }

    void loadUserData(User user) {
        this.user = user;

        username_field.setText(user.getLogin());
        id_field.setText(""+user.getId());
    }

    @FXML
    private void displayOrderCourse() throws Exception {
        int ID = orders_table.getSelectionModel().getSelectedItem().getOrderID();
        List<OrderCourse> orderCourseList = orderCourseDAO.getAllById(ID);
        ObservableList<OrderCourse> obs_orderCourseList = FXCollections.observableArrayList(orderCourseList);

        courses_table.setItems(obs_orderCourseList);
    }

    @FXML
    private void showBySorting() throws Exception {
        // if if the date is indicated
        if(calendar.getValue() != null) {

            // if date is a period of days
            if(calendar1.getValue() != null) {
                if (rate_picker.getValue().equals("All")) {
                    Date date1 = Date.valueOf(calendar.getValue());
                    Date date2 = Date.valueOf(calendar1.getValue());
                    orderList = orderDAO.getByDateMupliple(date1, date2);
                    obs_orderList = FXCollections.observableArrayList(orderList);
                    orders_table.setItems(obs_orderList);
                }
                else {
                    Date date1 = Date.valueOf(calendar.getValue());
                    Date date2 = Date.valueOf(calendar1.getValue());
                    orderList = orderDAO.getByDateAndRateMultiple(date1, date2, rate_picker.getValue());
                    obs_orderList = FXCollections.observableArrayList(orderList);
                    orders_table.setItems(obs_orderList);

                }
            }
            //if the date is simple day
            else {

                if (rate_picker.getValue().equals("All")) {
                    Date date1 = Date.valueOf(calendar.getValue());
                    orderList = orderDAO.getByDate(date1);
                    obs_orderList = FXCollections.observableArrayList(orderList);
                    orders_table.setItems(obs_orderList);
                }
                else {
                    Date date1 = Date.valueOf(calendar.getValue());
                    orderList = orderDAO.getByDateAndRate(date1, rate_picker.getValue());
                    obs_orderList = FXCollections.observableArrayList(orderList);
                    orders_table.setItems(obs_orderList);

                }
            }
        }
        //if the date is not indicated
        else {
            if (rate_picker.getValue().equals("All")) showAllOrders();
            else {
                orderList = orderDAO.getByRate(rate_picker.getValue());
                obs_orderList = FXCollections.observableArrayList(orderList);
                orders_table.setItems(obs_orderList);
            }
        }

    }

    @FXML
    private void showAllOrders() throws Exception {
        orderList = orderDAO.getAll();
        obs_orderList = FXCollections.observableArrayList(orderList);

        orders_table.setItems(obs_orderList);
    }

    // find max frequency and courses with, print it on label
    private void findMaxFrequency(HashMap<String, Integer> courses) {
        int max_value = Collections.max(courses.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();

        popular_course.setText("");
        for (Map.Entry<String, Integer> entry : courses.entrySet()) {
            if (entry.getValue()==max_value) {
                popular_course.setText(popular_course.getText() + " " + entry.getKey());
            }
        }
    }

    @FXML
    private void calculateResult() throws Exception {
        List<OrderCourse> order_courses = new ArrayList<>(); // list with all courses from orders
        List<String> all_courses = new ArrayList<>(); // all courses names
        Set<String> unique_course = new HashSet<>(); // set with unique names of courses
        HashMap<String, Integer> frequency = new HashMap<>(); // list with name courses and frequency

        // calculate price + add all courses in list by ID from Orders table
        int price = 0;
        for (Order oc: orders_table.getItems()) {
            price += oc.getPrice();
            order_courses.addAll(orderCourseDAO.getAllById(oc.getOrderID()));
        }

        // add unique names
        for (OrderCourse oc: order_courses) {
            all_courses.add(oc.getNameCourse());
            unique_course.add(oc.getNameCourse());
        }

        // calculate frequency and add it to hashmap
        for (String s: unique_course) {
            frequency.put(s, Collections.frequency(all_courses, s));
        }

        findMaxFrequency(frequency);
        money_by_period.setText(""+price);

    }

}
