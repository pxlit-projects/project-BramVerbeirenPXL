package be.pxl.services.domain.specification;

import be.pxl.services.domain.Post;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class PostSpecification {
    public static Specification<Post> filter(String content, String author, LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = null;

            if (content != null && !content.isEmpty()) {
                predicate = criteriaBuilder.like(root.get("content"), "%" + content + "%");
            }
            if (author != null && !author.isEmpty()) {
                predicate = predicate == null ? criteriaBuilder.like(root.get("author"), "%" + author + "%") :
                        criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("author"), "%" + author + "%"));
            }
            if (date != null) {
                predicate = predicate == null ? criteriaBuilder.equal(root.get("createdDate"), date) :
                        criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("createdDate"), date));
            }

            return predicate != null ? predicate : criteriaBuilder.conjunction();
        };
    }
}