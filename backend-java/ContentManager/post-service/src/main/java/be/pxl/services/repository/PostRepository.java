package be.pxl.services.repository;

import be.pxl.services.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    List<Post> findByDraftFalse();
    List<Post> findByApprovedFalse();
    List<Post> findByApprovedTrue();
}
