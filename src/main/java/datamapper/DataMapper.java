package datamapper;

import domain.DomainObject;

public abstract class DataMapper {
    /**
     * an abstract function to perform insert action to database
     * @param object
     * @return
     */
    public abstract void insert(DomainObject object);

    /**
     * an abstract function to perform update action by domainObject's id to database.
     * @param object
     * @return
     */
    public abstract void update(DomainObject object);

    /**
     * an abstract function to perform delete action by domainObject's id to database.
     * @param object
     * @return
     */
    public abstract void delete(DomainObject object);

}
