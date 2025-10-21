package crud.services.generics;

import java.io.Serializable;
import java.util.Collection;

import crud.dao.Persistent;
import crud.dao.generics.IGenericDAO;
import crud.exceptions.DAOException;
import crud.exceptions.MoreThanOneRegisterException;
import crud.exceptions.PrimaryKeyNotFound;
import crud.exceptions.TableException;

public abstract class GenericService<T extends Persistent, E extends Serializable> implements IGenericService<T, E> {
    protected IGenericDAO<T, E> dao;

    public GenericService(IGenericDAO<T, E> dao) {
        this.dao = dao;
    }

    @Override
    public Boolean register(T entity) throws DAOException, PrimaryKeyNotFound {
        return this.dao.create(entity);
    }

    @Override
    public T search(E valor) throws DAOException, PrimaryKeyNotFound, TableException, MoreThanOneRegisterException {
        return this.dao.read(valor);
    }

    @Override
    public Boolean edit(T entity) throws DAOException, PrimaryKeyNotFound {
        return this.dao.update(entity);
    }

    @Override
    public Boolean exclude(E valor) throws DAOException, PrimaryKeyNotFound {
        return this.dao.delete(valor);
    }

    @Override
    public Collection<T> all() throws DAOException, PrimaryKeyNotFound {
        return this.dao.all();
    }
}
