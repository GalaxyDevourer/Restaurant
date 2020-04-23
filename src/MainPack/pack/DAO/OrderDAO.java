package MainPack.pack.DAO;

import MainPack.pack.Entity.Order;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OrderDAO implements DAOFactory<Order>{

    @Override
    public List<Order> getAll() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Order> list = new ArrayList<Order>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM Orders ORDER BY OrderID");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setClientName(rs.getString("ClientName"));
                order.setChefName(rs.getString("ChefName"));
                order.setPrice(rs.getInt("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setRate(rs.getString("Rate"));
                order.setTime(rs.getDate("OrderTime"));

                list.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeSt(stmt);
            closeCon(conn);
        }

        return list;
    }

    public int insertInt(Order order) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("INSERT INTO Orders(ClientName, ChefName," +
                    " TotalPrice, Status, Rate, OrderTime) VALUES(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, order.getClientName());
            stmt.setString(2, order.getChefName());
            stmt.setInt(3, order.getPrice());
            stmt.setString(4, order.getStatus());
            stmt.setString(5, order.getRate());
            stmt.setDate(6, order.getTime());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()) {
                order.setOrderID(rs.getInt(1));
                return rs.getInt(1);
            }
            else return -1;
        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    @Override
    public boolean insert(Order order) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("INSERT INTO Orders(ClientName, ChefName," +
                    " TotalPrice, Status, Rate, OrderTime) VALUES(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, order.getClientName());
            stmt.setString(2, order.getChefName());
            stmt.setInt(3, order.getPrice());
            stmt.setString(4, order.getStatus());
            stmt.setString(5, order.getRate());
            stmt.setDate(6, order.getTime());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()) {
                order.setOrderID(rs.getInt(1));
                return true;
            }
            else return false;
        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    // get ONE order by id
    public Order get(Integer id) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("SELECT * FROM Orders WHERE OrderID=?");
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            Order order = new Order();

            if(rs.next()) {
                order.setOrderID(rs.getInt("OrderID"));
                order.setClientName(rs.getString("ClientName"));
                order.setChefName(rs.getString("ChefName"));
                order.setPrice(rs.getInt("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setRate(rs.getString("Rate"));
                order.setTime(rs.getDate("OrderTime"));

                return order;
            }
            else return null;

        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    public boolean updateStatus(Order order) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("UPDATE Orders SET Status=?, ChefName=? WHERE OrderID=?");
            stmt.setString(1, order.getStatus());
            stmt.setString(2, order.getChefName());
            stmt.setInt(3, order.getOrderID());

            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    public boolean updateRate(Order order) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("UPDATE Orders SET Rate=? WHERE OrderID=?");
            stmt.setString(1, order.getRate());
            stmt.setInt(2, order.getOrderID());

            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    public List<Order> getAllOrdered(String status) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Order> list = new ArrayList<Order>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM Orders WHERE Status=? ORDER BY OrderID");
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setClientName(rs.getString("ClientName"));
                order.setChefName(rs.getString("ChefName"));
                order.setPrice(rs.getInt("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setRate(rs.getString("Rate"));
                order.setTime(rs.getDate("OrderTime"));

                list.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeSt(stmt);
            closeCon(conn);
        }

        return list;
    }

    public List<Order> getByDateAndRate(Date date, String rate) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Order> list = new ArrayList<Order>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM Orders WHERE Rate=? AND OrderTime=? ORDER BY OrderID");
            stmt.setString(1, rate);
            stmt.setDate(2, date);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setClientName(rs.getString("ClientName"));
                order.setChefName(rs.getString("ChefName"));
                order.setPrice(rs.getInt("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setRate(rs.getString("Rate"));
                order.setTime(rs.getDate("OrderTime"));

                list.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeSt(stmt);
            closeCon(conn);
        }

        return list;
    }

    public List<Order> getByDateAndRateMultiple(Date date1, Date date2, String rate) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Order> list = new ArrayList<Order>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM Orders WHERE Rate=? AND OrderTime>=? AND OrderTime<=? ORDER BY OrderID");
            stmt.setString(1, rate);
            stmt.setDate(2, date1);
            stmt.setDate(3, date2);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setClientName(rs.getString("ClientName"));
                order.setChefName(rs.getString("ChefName"));
                order.setPrice(rs.getInt("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setRate(rs.getString("Rate"));
                order.setTime(rs.getDate("OrderTime"));

                list.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeSt(stmt);
            closeCon(conn);
        }

        return list;
    }

    public List<Order> getByRate(String rate) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Order> list = new ArrayList<Order>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM Orders WHERE Rate=? ORDER BY OrderID");
            stmt.setString(1, rate);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setClientName(rs.getString("ClientName"));
                order.setChefName(rs.getString("ChefName"));
                order.setPrice(rs.getInt("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setRate(rs.getString("Rate"));
                order.setTime(rs.getDate("OrderTime"));

                list.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeSt(stmt);
            closeCon(conn);
        }

        return list;
    }

    public List<Order> getByDate(Date date) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Order> list = new ArrayList<Order>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM Orders WHERE OrderTime=? ORDER BY OrderID");
            stmt.setDate(1, date);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setClientName(rs.getString("ClientName"));
                order.setChefName(rs.getString("ChefName"));
                order.setPrice(rs.getInt("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setRate(rs.getString("Rate"));
                order.setTime(rs.getDate("OrderTime"));

                list.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeSt(stmt);
            closeCon(conn);
        }

        return list;
    }

    public List<Order> getByDateMupliple(Date date1, Date date2) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Order> list = new ArrayList<Order>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM Orders WHERE OrderTime>=? AND OrderTime<=? ORDER BY OrderID");
            stmt.setDate(1, date1);
            stmt.setDate(2, date2);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("OrderID"));
                order.setClientName(rs.getString("ClientName"));
                order.setChefName(rs.getString("ChefName"));
                order.setPrice(rs.getInt("TotalPrice"));
                order.setStatus(rs.getString("Status"));
                order.setRate(rs.getString("Rate"));
                order.setTime(rs.getDate("OrderTime"));

                list.add(order);
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
    public Order get(String data) throws Exception {
        return null;
    }

    // not exactly needed
    @Override
    public boolean update(Order order) throws Exception {
        return true;
    }

    // not exactly needed
    @Override
    public boolean delete(String data) throws Exception {
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
