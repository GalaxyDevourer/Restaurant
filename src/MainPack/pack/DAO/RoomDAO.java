package MainPack.pack.DAO;

import MainPack.pack.Entity.Seat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RoomDAO implements DAOFactory<Seat> {

    @Override // get by username
    public Seat get(String user) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("SELECT * FROM Room WHERE Username=?");
            stmt.setString(1, user);

            ResultSet rs = stmt.executeQuery();
            Seat seat = new Seat();

            if(rs.next()) {
                seat.setSeat(rs.getInt("Seat"));
                seat.setUsername(rs.getString("Username"));

                return seat;
            }
            else return null;

        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    // get by seat number
    public Seat get(Integer seat) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("SELECT * FROM Room WHERE Seat=?");
            stmt.setInt(1, seat);

            ResultSet rs = stmt.executeQuery();
            Seat room = new Seat();

            if(rs.next()) {
                room.setSeat(rs.getInt("Seat"));
                room.setUsername(rs.getString("Username"));

                return room;
            }
            else return null;

        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    @Override
    public List<Seat> getAll() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Seat> list = new ArrayList<Seat>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM Room ORDER BY Seat");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Seat seat = new Seat();
                seat.setSeat(rs.getInt("Seat"));
                seat.setUsername(rs.getString("Username"));

                list.add(seat);
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
    public boolean delete(String user) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("DELETE FROM Room WHERE Username=?");
            stmt.setString(1, user);

            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            closeSt(stmt);
            closeCon(conn);
        }

    }

    @Override
    public boolean insert(Seat user) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("INSERT INTO Room(Seat, Username) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, user.getSeat());
            stmt.setString(2, user.getUsername());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()) {
                user.setSeat(rs.getInt(1));
                return true;
            }
            else return false;
        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    @Override
    public boolean update(Seat user) throws Exception {
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
