package publications.periodicals.services;

import publications.periodicals.dao.exceptions.DAOException;
import publications.periodicals.dao.impl.UserDAO;
import publications.periodicals.entities.user.User;
import publications.periodicals.services.exceptions.ServiceException;
import publications.periodicals.services.utils.ConnectionPool;

import java.sql.Connection;

public class UserService {
    private static UserService instance = null;

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null)
            instance = new UserService();
        return instance;
    }

    public User loginUser(String username, String password) throws ServiceException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        UserDAO userDAO = new UserDAO(connection);

        try {
            User user = userDAO.findByUsername(username);

            if (user != null && user.getPassword().equals(password)) {
                return user;
            } else {
                return null;
            }
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }
}
