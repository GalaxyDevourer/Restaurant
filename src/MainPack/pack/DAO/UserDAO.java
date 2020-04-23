package MainPack.pack.DAO;

import MainPack.pack.Entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class UserDAO implements DAOFactory<User> {

    @Override
    public User get(String Username) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("SELECT * FROM Restaurant WHERE Username=?");
            stmt.setString(1, Username);

            ResultSet rs = stmt.executeQuery();
            User user = new User();

            if(rs.next()) {
                user.setId(rs.getInt("userID"));
                user.setLogin(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setRole(rs.getString("Role"));

                return user;
            }
            else return user;

        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }

    }

    @Override
    public List<User> getAll() throws Exception {
        return null;
    }

    @Override
    public boolean delete(String data) throws Exception {
        return false;
    }

    @Override
    public boolean update(User user) throws Exception {
        return false;
    }

    @Override
    public boolean insert(User user) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("INSERT INTO Restaurant(Username, Password, Role) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()) user.setId(rs.getInt(1));
            return true;
        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }

    }

    private Connection getConnection() throws IOException, SQLException, ClassNotFoundException {
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