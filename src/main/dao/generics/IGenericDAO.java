package main.dao.generics;

import java.io.Serializable;
import java.util.Collection;

import main.dao.Persistent;
import main.exceptions.DAOException;
import main.exceptions.MoreThanOneRegisterException;
import main.exceptions.PrimaryKeyNotFound;
import main.exceptions.TableException;

public interface IGenericDAO <T extends Persistent, E extends Serializable> {
    public Boolean create(T entity) throws PrimaryKeyNotFound, DAOException;
    public T read(E valor) throws PrimaryKeyNotFound, DAOException, TableException, MoreThanOneRegisterException;
    public Boolean update(T entity) throws PrimaryKeyNotFound, DAOException;
    public Boolean delete(E valor) throws PrimaryKeyNotFound, DAOException;
    public Collection<T> all() throws DAOException, PrimaryKeyNotFound;
}
