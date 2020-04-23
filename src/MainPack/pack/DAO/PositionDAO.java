package MainPack.pack.DAO;

import MainPack.pack.Entity.Position;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PositionDAO implements DAOFactory<Position> {
    @Override
    public Position get(String position) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("SELECT * FROM Positions WHERE PositionName=?");
            stmt.setString(1, position);

            ResultSet rs = stmt.executeQuery();
            Position pos = new Position();

            if(rs.next()) {
                pos.setPositionName(rs.getString("PositionName"));
                return pos;
            }
            else return pos;

        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    @Override
    public List<Position> getAll() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Position> list = new ArrayList<Position>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM Positions ORDER BY PositionName");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Position position = new Position();
                position.setPositionName(rs.getString("PositionName"));

                list.add(position);
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
    public boolean insert(Position position) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("INSERT INTO Positions(PositionName) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, position.getPositionName());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()) {
                position.setPositionName(rs.getString(1));
                return true;
            }
            else return false;
        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    //###################################################################

    // not exactly needed
    @Override
    public boolean delete(String data) throws Exception {
        return false;
    }

    // not exactly needed
    @Override
    public boolean update(Position user) throws Exception {
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
