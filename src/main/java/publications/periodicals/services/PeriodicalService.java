package publications.periodicals.services;

import publications.periodicals.dao.exceptions.DAOException;
import publications.periodicals.dao.impl.PeriodicalDAO;
import publications.periodicals.entities.periodical.Periodical;
import publications.periodicals.services.exceptions.ServiceException;
import publications.periodicals.services.utils.ConnectionPool;

import java.sql.Connection;
import java.util.List;

public class PeriodicalService {
    private static PeriodicalService instance = null;

    private PeriodicalService() {}

    public static PeriodicalService getInstance() {
        if (instance == null)
            instance = new PeriodicalService();
        return instance;
    }

    public List<Periodical> findAllPeriodicals() throws ServiceException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        PeriodicalDAO periodicalDAO = new PeriodicalDAO(connection);

        try {
            return periodicalDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }
}
