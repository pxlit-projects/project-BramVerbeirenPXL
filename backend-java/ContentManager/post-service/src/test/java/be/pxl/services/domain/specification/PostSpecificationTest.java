package be.pxl.services.domain.specification;

import be.pxl.services.domain.Post;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.*;

class PostSpecificationTest {

    private Root<Post> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder criteriaBuilder;

    @BeforeEach
    void setUp() {
        root = mock(Root.class);
        query = mock(CriteriaQuery.class);
        criteriaBuilder = mock(CriteriaBuilder.class);
    }

    @Test
    void filter_withContentAndAuthor_createsPredicate() {
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<Post> criteriaQuery = mock(CriteriaQuery.class);
        Root<Post> root = mock(Root.class);
        Path<String> contentPath = (Path<String>) mock(Path.class);
        Path<String> authorPath = (Path<String>) mock(Path.class);

        // Mock de get-methode om de Path-objects te retourneren
        doReturn(contentPath).when(root).get("content");
        doReturn(authorPath).when(root).get("author");

        // Mock het gedrag van criteriaBuilder.like()
        Predicate contentPredicate = mock(Predicate.class);
        Predicate authorPredicate = mock(Predicate.class);
        when(criteriaBuilder.like(contentPath, "%filteredContent%")).thenReturn(contentPredicate);
        when(criteriaBuilder.like(authorPath, "%Author%")).thenReturn(authorPredicate);

        Predicate combinedPredicate = mock(Predicate.class);
        when(criteriaBuilder.and(contentPredicate, authorPredicate)).thenReturn(combinedPredicate);

        Specification<Post> specification = PostSpecification.filter("filteredContent", "Author", null);
        Predicate result = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

        assertNotNull(result);  // Controleer dat de Predicate niet null is
        verify(criteriaBuilder).like(contentPath, "%filteredContent%");
        verify(criteriaBuilder).like(authorPath, "%Author%");
    }


    @Test
    void filter_withOnlyContent() {
        Path<String> contentPath = mock(Path.class);
        doReturn(contentPath).when(root).get("content");

        Predicate contentPredicate = mock(Predicate.class);
        when(criteriaBuilder.like(contentPath, "%keyword%")).thenReturn(contentPredicate);

        Specification<Post> specification = PostSpecification.filter("keyword", null, null);
        specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).like(contentPath, "%keyword%");
        verify(root).get("content");
    }

    @Test
    void filter_withOnlyAuthor() {
        Path<String> authorPath = mock(Path.class);
        doReturn(authorPath).when(root).get("author");

        Predicate authorPredicate = mock(Predicate.class);
        when(criteriaBuilder.like(authorPath, "%AuthorName%")).thenReturn(authorPredicate);

        Specification<Post> specification = PostSpecification.filter(null, "AuthorName", null);
        specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).like(authorPath, "%AuthorName%");
        verify(root).get("author");
    }

    @Test
    void filter_withOnlyDate() {
        Path<LocalDate> datePath = mock(Path.class);
        doReturn(datePath).when(root).get("createdDate");

        Predicate datePredicate = mock(Predicate.class);
        when(criteriaBuilder.equal(datePath, LocalDate.of(2023, 12, 25))).thenReturn(datePredicate);

        Specification<Post> specification = PostSpecification.filter(null, null, LocalDate.of(2023, 12, 25));
        specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(datePath, LocalDate.of(2023, 12, 25));
        verify(root).get("createdDate");
    }

    @Test
    void filter_withNoFilters() {
        Specification<Post> specification = PostSpecification.filter(null, null, null);
        Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).conjunction(); // Controleer dat de default "1=1" wordt gebruikt
    }
    @Test
    void filter_withContentAndDate_createsPredicate() {
        Path<String> contentPath = mock(Path.class);
        Path<LocalDate> datePath = mock(Path.class);
        doReturn(contentPath).when(root).get("content");
        doReturn(datePath).when(root).get("createdDate");

        Predicate contentPredicate = mock(Predicate.class);
        Predicate datePredicate = mock(Predicate.class);
        Predicate combinedPredicate = mock(Predicate.class);

        when(criteriaBuilder.like(contentPath, "%testContent%")).thenReturn(contentPredicate);
        when(criteriaBuilder.equal(datePath, LocalDate.of(2023, 12, 25))).thenReturn(datePredicate);
        when(criteriaBuilder.and(contentPredicate, datePredicate)).thenReturn(combinedPredicate);

        Specification<Post> specification = PostSpecification.filter("testContent", null, LocalDate.of(2023, 12, 25));
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).like(contentPath, "%testContent%");
        verify(criteriaBuilder).equal(datePath, LocalDate.of(2023, 12, 25));
        verify(criteriaBuilder).and(contentPredicate, datePredicate);
    }

    @Test
    void filter_withAuthorAndDate_createsPredicate() {
        Path<String> authorPath = mock(Path.class);
        Path<LocalDate> datePath = mock(Path.class);
        doReturn(authorPath).when(root).get("author");
        doReturn(datePath).when(root).get("createdDate");

        Predicate authorPredicate = mock(Predicate.class);
        Predicate datePredicate = mock(Predicate.class);
        Predicate combinedPredicate = mock(Predicate.class);

        when(criteriaBuilder.like(authorPath, "%AuthorTest%")).thenReturn(authorPredicate);
        when(criteriaBuilder.equal(datePath, LocalDate.of(2023, 11, 15))).thenReturn(datePredicate);
        when(criteriaBuilder.and(authorPredicate, datePredicate)).thenReturn(combinedPredicate);

        Specification<Post> specification = PostSpecification.filter(null, "AuthorTest", LocalDate.of(2023, 11, 15));
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).like(authorPath, "%AuthorTest%");
        verify(criteriaBuilder).equal(datePath, LocalDate.of(2023, 11, 15));
        verify(criteriaBuilder).and(authorPredicate, datePredicate);
    }

    @Test
    void filter_withAllFilters_createsCombinedPredicate() {
        Path<String> contentPath = mock(Path.class);
        Path<String> authorPath = mock(Path.class);
        Path<LocalDate> datePath = mock(Path.class);

        doReturn(contentPath).when(root).get("content");
        doReturn(authorPath).when(root).get("author");
        doReturn(datePath).when(root).get("createdDate");

        Predicate contentPredicate = mock(Predicate.class);
        Predicate authorPredicate = mock(Predicate.class);
        Predicate datePredicate = mock(Predicate.class);
        Predicate combinedPredicate1 = mock(Predicate.class);
        Predicate finalPredicate = mock(Predicate.class);

        when(criteriaBuilder.like(contentPath, "%FullTestContent%")).thenReturn(contentPredicate);
        when(criteriaBuilder.like(authorPath, "%FullAuthor%")).thenReturn(authorPredicate);
        when(criteriaBuilder.equal(datePath, LocalDate.of(2023, 10, 31))).thenReturn(datePredicate);
        when(criteriaBuilder.and(contentPredicate, authorPredicate)).thenReturn(combinedPredicate1);
        when(criteriaBuilder.and(combinedPredicate1, datePredicate)).thenReturn(finalPredicate);

        Specification<Post> specification = PostSpecification.filter("FullTestContent", "FullAuthor", LocalDate.of(2023, 10, 31));
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).like(contentPath, "%FullTestContent%");
        verify(criteriaBuilder).like(authorPath, "%FullAuthor%");
        verify(criteriaBuilder).equal(datePath, LocalDate.of(2023, 10, 31));
        verify(criteriaBuilder).and(contentPredicate, authorPredicate);
        verify(criteriaBuilder).and(combinedPredicate1, datePredicate);
    }
}
