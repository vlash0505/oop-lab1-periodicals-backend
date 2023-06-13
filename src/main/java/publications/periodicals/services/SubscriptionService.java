package publications.periodicals.services;

import publications.periodicals.dao.exceptions.DAOException;
import publications.periodicals.dao.impl.ReceiptDAO;
import publications.periodicals.dao.impl.SubscriptionDAO;
import publications.periodicals.entities.receipt.Receipt;
import publications.periodicals.entities.receipt.ReceiptBuilder;
import publications.periodicals.entities.subscription.Subscription;
import publications.periodicals.entities.subscription.SubscriptionBuilder;
import publications.periodicals.services.exceptions.ServiceException;
import publications.periodicals.services.utils.ConnectionPool;
import publications.periodicals.services.utils.TransactionManager;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class SubscriptionService {
    private static SubscriptionService instance = null;

    private SubscriptionService() {}

    public static SubscriptionService getInstance() {
        if (instance == null)
            instance = new SubscriptionService();
        return instance;
    }

    public List<Subscription> findAllSubscriptionsByUserAndStatus(long userId, boolean confirmed)
            throws ServiceException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);

        try {
            return subscriptionDAO.findByUserIdAndStatus(userId, confirmed);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    public Subscription prepareSubscription(long userId, long periodicalId) throws ServiceException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);

        try {
            return subscriptionDAO.create(new SubscriptionBuilder()
                    .setUserId(userId)
                    .setPeriodicalId(periodicalId)
                    .build());
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    public Receipt confirmSubscriptions(List<Subscription> subscriptions, int sum, LocalDateTime paidAt)
            throws ServiceException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);
        ReceiptDAO receiptDAO = new ReceiptDAO(connection);

        try {
            TransactionManager.beginTransaction(connection);
            Receipt receipt = receiptDAO.create(new ReceiptBuilder().setSum(sum).setDoneAt(paidAt).build());
            for (Subscription subscription : subscriptions) {
                subscriptionDAO.update(subscription);
            }
            TransactionManager.commit(connection);

            return receipt;
        } catch (DAOException e) {
            TransactionManager.rollback(connection);
            throw new ServiceException(e.getMessage());
        } finally {
            TransactionManager.finishTransaction(connection);
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }
}
