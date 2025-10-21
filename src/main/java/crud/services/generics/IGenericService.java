package crud.services.generics;

import java.io.Serializable;
import java.util.Collection;

import crud.dao.Persistent;
import crud.exceptions.DAOException;
import crud.exceptions.MoreThanOneRegisterException;
import crud.exceptions.PrimaryKeyNotFound;
import crud.exceptions.TableException;

public interface IGenericService <T extends Persistent, E extends Serializable> {
    public Boolean register(T entity) throws DAOException, PrimaryKeyNotFound;
    public T search(E valor) throws DAOException, PrimaryKeyNotFound, TableException, MoreThanOneRegisterException;
    public Boolean edit(T entity) throws DAOException, PrimaryKeyNotFound;
    public Boolean exclude(E valor) throws DAOException, PrimaryKeyNotFound;
    public Collection<T> all() throws DAOException, PrimaryKeyNotFound;
}
