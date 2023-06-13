package publications.periodicals.dao.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import publications.periodicals.dao.DAO;
import publications.periodicals.dao.exceptions.DAOException;
import publications.periodicals.dao.utils.ResultParser;
import publications.periodicals.entities.periodical.Periodical;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PeriodicalDAO implements DAO<Periodical> {
    private static final Logger logger = LogManager.getLogger(PeriodicalDAO.class);
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("sql");
    private final Connection connection;

    public PeriodicalDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Periodical> findAll() throws DAOException {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(BUNDLE.getString("periodicals.find_all"));
            List<Periodical> result = new ArrayList<>();

            while (resultSet.next()) {
                result.add(ResultParser.getPeriodical(resultSet));
            }

            return result;
        } catch (SQLException e) {
            logger.error("Error while finding all periodicals.", e);
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public Periodical findById(long id) {
        return null;
    }

    @Override
    public Periodical create(Periodical entity) {
        return null;
    }

    @Override
    public boolean update(Periodical entity) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        throw new UnsupportedOperationException("Periodical DAO does not support delete operation.");
    }
}
