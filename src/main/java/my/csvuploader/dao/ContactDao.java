package my.csvuploader.dao;

import my.csvuploader.dto.Contact;

import java.util.List;

public interface ContactDao {

    void insertOrUpdate(Contact contact) throws DaoException;

    List<Contact> getAll(int offset, int numberOfRecords) throws DaoException;

    int getTotalRecords() throws DaoException;

    void closeDao() throws DaoException;
}
