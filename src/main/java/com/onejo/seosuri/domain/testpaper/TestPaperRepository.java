package com.onejo.seosuri.domain.testpaper;

import com.onejo.seosuri.domain.problem.ProbWordObj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestPaperRepository extends JpaRepository<TestPaper, Long> {
    Optional<TestPaper> findById(Long id);
}
