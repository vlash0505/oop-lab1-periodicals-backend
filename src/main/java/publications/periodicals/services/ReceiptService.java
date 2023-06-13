package publications.periodicals.services;

import publications.periodicals.dao.impl.ReceiptDAO;
import publications.periodicals.dao.exceptions.DAOException;
import publications.periodicals.entities.receipt.Receipt;
import publications.periodicals.services.exceptions.ServiceException;
import publications.periodicals.services.utils.ConnectionPool;

import java.sql.Connection;
import java.util.List;

public class ReceiptService {
    private static ReceiptService instance = null;

    private ReceiptService() {}

    public static ReceiptService getInstance() {
        if (instance == null)
            instance = new ReceiptService();
        return instance;
    }

    public List<Receipt> findAllReceipts() throws ServiceException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        ReceiptDAO receiptDAO = new ReceiptDAO(connection);

        try {
            return receiptDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }
}
