package org.aren_rend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("NullableProblems")
public interface SpendingRepositories extends JpaRepository<SpendingEntity, Long> {

}
