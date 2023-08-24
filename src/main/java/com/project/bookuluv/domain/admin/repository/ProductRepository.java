package com.project.bookuluv.domain.admin.repository;


import com.project.bookuluv.domain.admin.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    long countByIsbn(String isbn);



    Page<Product> findByMallType(String book, Pageable pageable);

    @Query("select "
            + "distinct p "
            + "from Product p "
            + "where "
            + "   (p.title like %:kw% "
//            + "   or p.description like %:kw% "
            + "   or p.publisher like %:kw% "
            + "   or p.author like %:kw% "
            + "   or p.categoryName like %:kw%)"
            // mall_type 조건 추가
            + "   and (p.mallType = :book) ")
    Page<Product> findAllByKeyword(@Param("kw") String kw, @Param("book") String mallType, Pageable pageable);
}
