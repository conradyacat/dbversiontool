package com.cyacat.data.repository;

import com.cyacat.data.domain.DatabaseVersion;
import com.cyacat.data.domain.DatabaseVersionPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Conrad Yacat on 8/5/2014.
 */
@Repository
public interface DatabaseVersionRepository extends CrudRepository<DatabaseVersion, DatabaseVersionPK> {

    DatabaseVersion findOneOrderByDateExecuted();
}