package MainPack.pack.DAO;

import MainPack.pack.Entity.OrderCourse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OrderCourseDAO implements DAOFactory<OrderCourse> {

    @Override
    public List<OrderCourse> getAll() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<OrderCourse> list = new ArrayList<OrderCourse>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM OrderCourse ORDER BY OrderID");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderCourse orderCourse = new OrderCourse();

                orderCourse.setOrderID(rs.getInt("OrderID"));
                orderCourse.setNameCourse(rs.getString("NameCourse"));
                orderCourse.setIngredient1(rs.getString("Ingredient1"));
                orderCourse.setWeight1(rs.getInt("Weight1"));
                orderCourse.setIngredient2(rs.getString("Ingredient2"));
                orderCourse.setWeight2(rs.getInt("Weight2"));
                orderCourse.setIngredient3(rs.getString("Ingredient3"));
                orderCourse.setWeight3(rs.getInt("Weight3"));

                list.add(orderCourse);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeSt(stmt);
            closeCon(conn);
        }

        return list;
    }

    @Override
    public boolean insert(OrderCourse orderCourse) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("INSERT INTO OrderCourse(OrderID, NameCourse, Ingredient1," +
                    " Weight1, Ingredient2, Weight2, Ingredient3, Weight3) VALUES(?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, orderCourse.getOrderID());
            stmt.setString(2, orderCourse.getNameCourse());
            stmt.setString(3, orderCourse.getIngredient1());
            stmt.setInt(4, orderCourse.getWeight1());
            stmt.setString(5, orderCourse.getIngredient2());
            stmt.setInt(6, orderCourse.getWeight2());
            stmt.setString(7, orderCourse.getIngredient3());
            stmt.setInt(8, orderCourse.getWeight3());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()) {
                orderCourse.setOrderID(rs.getInt(1));
                return true;
            }
            else return false;
        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    // get LIST of order courses by id
    public List<OrderCourse> getAllById(Integer id) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<OrderCourse> list = new ArrayList<OrderCourse>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM OrderCourse WHERE OrderID=? ORDER BY OrderID");
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderCourse orderCourse = new OrderCourse();

                orderCourse.setOrderID(rs.getInt("OrderID"));
                orderCourse.setNameCourse(rs.getString("NameCourse"));
                orderCourse.setIngredient1(rs.getString("Ingredient1"));
                orderCourse.setWeight1(rs.getInt("Weight1"));
                orderCourse.setIngredient2(rs.getString("Ingredient2"));
                orderCourse.setWeight2(rs.getInt("Weight2"));
                orderCourse.setIngredient3(rs.getString("Ingredient3"));
                orderCourse.setWeight3(rs.getInt("Weight3"));

                list.add(orderCourse);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeSt(stmt);
            closeCon(conn);
        }

        return list;

    }

    //###################################################################

    // not exactly needed
    @Override
    public OrderCourse get(String data) throws Exception {
        return null;
    }

    // not exactly needed
    @Override
    public boolean delete(String data) throws Exception {
        return false;
    }

    // not exactly needed
    @Override
    public boolean update(OrderCourse user) throws Exception {
        return false;
    }

    private Connection getConnection() throws ClassNotFoundException, IOException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get(
                System.getProperty("user.dir") + "\\database.properties"))){
            props.load(in);
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

        return DriverManager.getConnection(url, username, password);
    }

    private static void closeCon(Connection con) {
        if (con != null) {
            try {
                con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void closeSt(Statement st) {
        if (st != null) {
            try {
                st.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
