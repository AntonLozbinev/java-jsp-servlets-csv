package my.csvuploader.dao;

import my.csvuploader.dto.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlContactDao implements ContactDao {

    private Connection connection;
    private PreparedStatement statementForInsertOrUpdate;
    private PreparedStatement statementForSelect;
    private PreparedStatement statementForTotalRecords;
    private final String INSERT_OR_UPDATE_SQL = "INSERT INTO contacts (name, surname, login, email, phone_number) "
                                              + " VALUES (?, ?, ?, ?, ?) "
                                              + "ON DUPLICATE KEY UPDATE name=?, surname=?, email=?, phone_number=?;";
    private final String GET_ALL_SQL = "SELECT name, surname, login, email, phone_number FROM contacts LIMIT ?, ?;";

    public MySqlContactDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertOrUpdate(Contact contact) throws DaoException {
        try {
            statementForInsertOrUpdate = connection.prepareStatement(INSERT_OR_UPDATE_SQL);
            statementForInsertOrUpdate.setString(1, contact.getName());
            statementForInsertOrUpdate.setString(2, contact.getSurname());
            statementForInsertOrUpdate.setString(3, contact.getLogin());
            statementForInsertOrUpdate.setString(4, contact.getEmail());
            statementForInsertOrUpdate.setString(5, contact.getPhoneNumber());
            statementForInsertOrUpdate.setString(6, contact.getName());
            statementForInsertOrUpdate.setString(7, contact.getSurname());
            statementForInsertOrUpdate.setString(8, contact.getEmail());
            statementForInsertOrUpdate.setString(9, contact.getPhoneNumber());
            statementForInsertOrUpdate.execute();
        } catch (SQLException e) {
            throw new DaoException("Can not create or update contact with name" + contact.getName(), e);
        }
    }

    @Override
    public List<Contact> getAll(int offset, int numberOfRecords) throws DaoException {
        List<Contact> contacts;
        ResultSet res = null;
        try {
            statementForSelect = connection.prepareStatement(GET_ALL_SQL);
            statementForSelect.setInt(1, offset);
            statementForSelect.setInt(2, numberOfRecords);
            try {
                res = statementForSelect.executeQuery();
                contacts = parseResultSet(res);
            } finally {
                if (res != null && !res.isClosed()) {
                    res.close();
                }
            }
        } catch (SQLException e) {
           throw new DaoException("Can not select all contacts",e);
        }
        return contacts;
    }

    private List<Contact> parseResultSet(ResultSet rs) throws SQLException {
        List<Contact> result = new ArrayList<>();
        while (rs.next()) {
            Contact contact = new Contact();
            contact.setName(rs.getString("name"));
            contact.setSurname(rs.getString("surname"));
            contact.setLogin(rs.getString("login"));
            contact.setEmail(rs.getString("email"));
            contact.setPhoneNumber(rs.getString("phone_number"));
            result.add(contact);
        }
        return result;
    }

    @Override
    public int getTotalRecords() throws DaoException {
        int totalRecords = 0;
        ResultSet res = null;
        try {
            statementForTotalRecords = connection.prepareStatement("SELECT COUNT(*) FROM contacts");
            try {
                res = statementForTotalRecords.executeQuery();
                while (res.next()) {
                    totalRecords = res.getInt(1);
                }
            } finally {
                if (res != null && !res.isClosed()) {
                    res.close();
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Can not select records count", e);
        }
        return totalRecords;
    }

    @Override
    public void closeDao() throws DaoException {
        try {
            try {
                if (statementForTotalRecords != null && !statementForTotalRecords.isClosed()) {
                    statementForTotalRecords.close();
                }
                if (statementForSelect != null && !statementForSelect.isClosed()) {
                    statementForSelect.close();
                }
                if (statementForInsertOrUpdate != null && !statementForInsertOrUpdate.isClosed()) {
                    statementForInsertOrUpdate.close();
                }
            } finally {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Can not close dao", e);
        }
    }
}
