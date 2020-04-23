package MainPack.pack.DAO;

import MainPack.pack.Entity.Course;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CourseDAO implements DAOFactory<Course> {
    @Override
    public Course get(String course) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("SELECT * FROM Course WHERE CourseName=?");
            stmt.setString(1, course);

            ResultSet rs = stmt.executeQuery();
            Course cour = new Course();

            if(rs.next()) {
                cour.setPositionName(rs.getString("PositionName"));
                cour.setCourseName(rs.getString("CourseName"));
                cour.setIngredient1(rs.getString("Ingredient1"));
                cour.setIngredient2(rs.getString("Ingredient2"));
                cour.setIngredient3(rs.getString("Ingredient3"));

                return cour;
            }
            else return cour;

        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    // get courses by position name
    public List<Course> getAllByPos(String posname) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Course> list = new ArrayList<Course>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM Course WHERE PositionName=? ORDER BY CourseName");
            stmt.setString(1, posname);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Course cour = new Course();

                cour.setPositionName(rs.getString("PositionName"));
                cour.setCourseName(rs.getString("CourseName"));
                cour.setIngredient1(rs.getString("Ingredient1"));
                cour.setIngredient2(rs.getString("Ingredient2"));
                cour.setIngredient3(rs.getString("Ingredient3"));

                list.add(cour);
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
    public boolean insert(Course course) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("INSERT INTO Course(PositionName, CourseName," +
                    " Ingredient1, Ingredient2, Ingredient3) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, course.getPositionName());
            stmt.setString(2, course.getCourseName());
            stmt.setString(3, course.getIngredient1());
            stmt.setString(4, course.getIngredient2());
            stmt.setString(5, course.getIngredient3());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()) {
                course.setPositionName(rs.getString(1));
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
    public boolean delete(String course) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("DELETE FROM Course WHERE CourseName=?");
            stmt.setString(1, course);

            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    //###################################################################

    // not exactly needed
    @Override
    public List<Course> getAll() throws Exception {
        return null;
    }

    // not exactly needed
    @Override
    public boolean update(Course user) throws Exception {
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
