package pri.wkz.mockitodemo.demos.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnExpression("false")
public interface CommonRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
