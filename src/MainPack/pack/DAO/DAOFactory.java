package MainPack.pack.DAO;

import java.util.List;

public interface DAOFactory<T> {
    T get(String data) throws Exception;
    List<T> getAll() throws Exception;
    boolean insert(T user) throws Exception;
    boolean delete(String data) throws Exception;
    boolean update(T user) throws Exception;
}
