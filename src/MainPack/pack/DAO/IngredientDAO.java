package MainPack.pack.DAO;

import MainPack.pack.Entity.Ingredient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class IngredientDAO implements DAOFactory<Ingredient> {
    @Override
    public Ingredient get(String ingredient) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("SELECT * FROM Ingredient WHERE IngredientName=?");
            stmt.setString(1, ingredient);

            ResultSet rs = stmt.executeQuery();
            Ingredient ing = new Ingredient();

            if(rs.next()) {
                ing.setIngredientName(rs.getString("IngredientName"));
                ing.setPortion(rs.getInt("Portion"));
                ing.setPrice(rs.getInt("Price"));

                return ing;
            }
            else return ing;

        }
        finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    @Override
    public List<Ingredient> getAll() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Ingredient> list = new ArrayList<Ingredient>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM Ingredient ORDER BY IngredientName");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ingredient ing = new Ingredient();

                ing.setIngredientName(rs.getString("IngredientName"));
                ing.setPortion(rs.getInt("Portion"));
                ing.setPrice(rs.getInt("Price"));

                list.add(ing);
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
    public boolean insert(Ingredient ingredient) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            assert conn != null;
            stmt = conn.prepareStatement("INSERT INTO Ingredient(IngredientName," +
                    " Portion, Price) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, ingredient.getIngredientName());
            stmt.setInt(2, ingredient.getPortion());
            stmt.setInt(3, ingredient.getPrice());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()) {
                ingredient.setIngredientName(rs.getString(1));
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
    public boolean delete(String ingredient) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("DELETE FROM Ingredient WHERE IngredientName=?");
            stmt.setString(1, ingredient);

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
    public boolean update(Ingredient ingredient) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("UPDATE Ingredient SET Portion=? Price=? WHERE IngredientName=?");
            stmt.setInt(1, ingredient.getPortion());
            stmt.setInt(2, ingredient.getPrice());
            stmt.setString(3, ingredient.getIngredientName());

            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            closeSt(stmt);
            closeCon(conn);
        }
    }

    public void updateWithName(Ingredient ingredient) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("UPDATE Ingredient SET IngredientName=? Portion=? Price=? WHERE IngredientName=?");
            stmt.setString(1, ingredient.getIngredientName());
            stmt.setInt(2, ingredient.getPortion());
            stmt.setInt(3, ingredient.getPrice());
            stmt.setString(4, ingredient.getIngredientName());

            stmt.executeUpdate();

        } catch (SQLException e) {
        } finally {
            closeSt(stmt);
            closeCon(conn);
        }
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
