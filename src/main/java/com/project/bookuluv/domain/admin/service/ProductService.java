package com.project.bookuluv.domain.admin.service;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.dto.ProductDto;
import com.project.bookuluv.domain.admin.repository.ProductRepository;
import com.project.bookuluv.domain.member.exception.DataNotFoundException;
import com.project.bookuluv.domain.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final RestTemplate restTemplate;

    private final OAuth2AuthorizedClientService clientService;

    private final ProductRepository productRepository;

    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;
    @Value("${custom.originPath}")
    private String originPath;
    @Value("${spring.aladin.api.key}")
    private String apiKey;
    private final String searchUrl = "http://www.aladdin.co.kr/ttb/api/ItemSearch.aspx";
    private final String listUrl = "http://www.aladin.co.kr/ttb/api/ItemList.aspx";

    public List<ProductDto> searchBooks(String query) {
        String url = buildSearchUrl("Keyword", query);
        return getBooksFromApi(url);
    }

    public List<ProductDto> getNewBooks() {
        String url = buildListUrl("ItemNewAll");
        return getBooksFromApi(url);
    }

    public List<ProductDto> getBestsellers() {
        String url = buildListUrl("Bestseller");
        return getBooksFromApi(url);
    }
    private String buildSearchUrl(String queryType, String query) {
        return searchUrl + "?ttbkey=" + apiKey + "&QueryType=" + queryType + "&MaxResults=20" + "&start=1" + "&SearchTarget=Book&Foreign" + "&Cover=Big" + "&output=js" + "&Version=20131101" + (query != null ? "&Query=" + query : "") + "&CategoryId=0";
    }

    private String buildListUrl(String queryType) {

        return listUrl + "?ttbkey=" + apiKey + "&QueryType=" + queryType + "&MaxResults=5" + "&start=1" + "&SearchTarget=Book&Foreign" + "&Cover=Big" + "&output=js" + "&Version=20131101";
    }

    private String domesticBuildListUrl(String queryType) {
        return listUrl + "?ttbkey=" + apiKey + "&QueryType=" + queryType + "&MaxResults=20" + "&start=1" + "&SearchTarget=Book" + "&output=js" + "&Version=20131101";
    }

    private String foreignBuildListUrl(String queryType) {
        return listUrl + "?ttbkey=" + apiKey + "&QueryType=" + queryType + "&MaxResults=20" + "&start=1" + "&SearchTarget=Foreign" + "&output=js" + "&Version=20131101";
    }

    private List<ProductDto> getBooksFromApi(String url) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String responseBody = responseEntity.getBody();
        System.out.println(responseBody);

        ArrayList<ProductDto> results = new ArrayList<>();

        if (responseBody != null) {
            JSONObject responseJson = new JSONObject(responseBody);
            JSONArray items = responseJson.optJSONArray("item");

            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    ProductDto result = ProductDto.builder()
                            .coverImg(item.optString("cover"))
                            .title(item.optString("title"))
                            .link(item.optString("link"))
                            .author(item.optString("author"))
                            .pubDate(item.optString("pubDate"))
                            .description(item.optString("description"))
                            .isbn(item.optString("isbn"))
                            .adult(item.optBoolean("adult"))
                            .categoryName(item.optString("categoryName"))
                            .publisher(item.optString("publisher"))
                            .priceStandard(item.optLong("priceStandard"))
                            .priceSales(item.optLong("priceSales"))
                            .mallType(item.optString("mallType"))
                            .build();

//                    Product product = Product.builder()
//                            .coverImg(result.getCoverImg())
//                            .title(result.getTitle())
//                            .link(result.getLink())
//                            .author(result.getAuthor())
//                            .pubDate(item.optString("pubDate"))
//                            .description(item.optString("description"))
//                            .publisher(result.getPublisher())
//                            .isbn(result.getIsbn())
//                            .adult(result.isAdult())
//                            .categoryName(result.getCategoryName())
//                            .priceStandard(result.getPriceStandard())
//                            .priceSales(result.getPriceSales())
//                            .mallType(result.getMallType())
//                            .createDate(LocalDateTime.now())
//                            .build();
//
//                    if (productRepository.countByIsbn(product.getIsbn()) == 0L) {//productRepository에 isbn이 0개라면 저장해라(0L의 L은 Long 타입이라 사용)
//                        productRepository.save(product);
//                    }
                    results.add(result);
                }
            }
        }
        return results;
    }

    public Product create(ProductDto productDto, MultipartFile file1, MultipartFile file2) throws IOException {
        String SanitizedMallType = "";
        if (productDto.getMallType().equals("국내도서")) {
            SanitizedMallType= "BOOK";
        } else if (productDto.getMallType().equals("외국도서")) {
            SanitizedMallType= "FOREIGN";
        } else {
            productDto.getMallType();
        }

        Product product = Product.builder()
                .title(productDto.getTitle())
                .author(productDto.getAuthor())
                .pubDate(productDto.getPubDate())
                .description(productDto.getDescription())
                .isbn(productDto.getIsbn())
                .isbn13(productDto.getIsbn13())
                .priceStandard(productDto.getPriceStandard())
                .priceSales(productDto.getPriceSales())
                .mallType(SanitizedMallType)
                .publisher(productDto.getPublisher())
                .adult(productDto.isAdult())
                .categoryName(productDto.getCategoryName())
                .createDate(LocalDateTime.now())
                .build();
        this.productRepository.save(product);
        UUID uuid = UUID.randomUUID();

        String coverImgFileName = uuid + "_" + file1.getOriginalFilename();
        String coverImgFilePath = originPath + coverImgFileName;
        File saveFile1 = new File(genFileDirPath, coverImgFileName);

        String contentPdfFileName = uuid + "_" + file2.getOriginalFilename();
        String contentPdfFilePath = originPath + contentPdfFileName;
        File saveFile2 = new File(genFileDirPath, contentPdfFileName);

        file1.transferTo(saveFile1); // 업로드된 파일 저장
        file2.transferTo(saveFile2); // 업로드된 파일 저장

        // 파일업로드를 위한 리빌드
        Product fileUpload = product.toBuilder()
                .coverImg(coverImgFilePath)
                .coverImgName(coverImgFileName)
                .contentsPdf(contentPdfFilePath)
                .contentsPdfName(contentPdfFileName)
                .build();

        // 제품을 데이터베이스에 저장
        this.productRepository.save(fileUpload);
        return product;
    }

    public Product modify(ProductDto productDto, Long id, MultipartFile file1, MultipartFile file2) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        String SanitizedMallType = "";
        if (productDto.getMallType().equals("국내도서")) {
            SanitizedMallType= "BOOK";
        } else if (productDto.getMallType().equals("외국도서")) {
            SanitizedMallType= "FOREIGN";
        } else {
            productDto.getMallType();
        }

        UUID uuid = UUID.randomUUID();
        String coverImgFileName = product.getCoverImgName();
        String coverImgFilePath = product.getCoverImg();
        String contentPdfFileName = product.getContentsPdfName();
        String contentPdfFilePath = product.getContentsPdf();

        if (file1.getOriginalFilename() != null && !file1.getOriginalFilename().isEmpty()) {
            coverImgFileName = uuid + "_" + file1.getOriginalFilename();
            coverImgFilePath = originPath + coverImgFileName;
            File saveFile1 = new File(genFileDirPath, coverImgFileName);
            file1.transferTo(saveFile1); // 업로드된 파일 저장
        }
        if (file2.getOriginalFilename() != null && !file2.getOriginalFilename().isEmpty()) {
            contentPdfFileName = uuid + "_" + file2.getOriginalFilename();
            contentPdfFilePath = originPath + contentPdfFileName;
            File saveFile2 = new File(genFileDirPath, contentPdfFileName);
            file2.transferTo(saveFile2); // 업로드된 파일 저장
        }

        Product modifiedProduct = product.toBuilder()
                .title(productDto.getTitle())
                .author(productDto.getAuthor())
                .pubDate(productDto.getPubDate())
                .description(productDto.getDescription())
                .isbn(productDto.getIsbn())
                .isbn13(productDto.getIsbn13())
                .priceStandard(productDto.getPriceStandard())
                .priceSales(productDto.getPriceSales())
                .mallType(SanitizedMallType)
                .publisher(productDto.getPublisher())
                .adult(productDto.isAdult())
                .categoryName(productDto.getCategoryName())
                .coverImg(coverImgFilePath)
                .coverImgName(coverImgFileName)
                .contentsPdf(contentPdfFilePath)
                .contentsPdfName(contentPdfFileName)
                .modifyDate(LocalDateTime.now())
                .build();

        this.productRepository.save(modifiedProduct);
        return modifiedProduct;
    }



    // 국내, 외국 도서 목록 페이징에 담아서 불러오는 service
    public Page<Product> getProducts(String type, int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page - 1, 20, Sort.by(sorts));
        return this.productRepository.findAllByKeyword(kw, type.toUpperCase(), pageable);
    }

    public Page<Product> getForeignProducts(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page - 1, 20, Sort.by(sorts));
        return this.productRepository.findAllForeignByKeyword(kw, pageable);
    }
    public Page<Product> getDomesticProducts(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page - 1, 20, Sort.by(sorts));
        return this.productRepository.findAllDomesticByKeyword(kw, pageable);
    }
    public void incrementHitCount(Long id) {
        Product product = productRepository.getById(id);
        int currentHit = product.getHit();
        product.setHit(currentHit + 1);
        productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = findById(id);
        this.productRepository.delete(product);
    }

    public List<Product> getAll() {
        return this.productRepository.findAll();
    }

    public Product findById(Long id) {
        return this.productRepository.getById(id);
    }

    public Product getById(Long id) {
        Optional<Product> product = this.productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new DataNotFoundException("product not found");
        }
    }
    public String getRootDomain(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String scheme = uri.getScheme();
        String host = uri.getHost();
        int port = uri.getPort();
        // 기본 포트인 경우 포트 정보는 생략
        String portString = (port == -1 || (scheme.equals("http") && port == 80) || (scheme.equals("https") && port == 443)) ? "" : ":" + port;

        return scheme + "://" + host + portString;
    }

    public void updateAverageRating(Product product) {
        List<Review> reviews = product.getReviews();
        if (!reviews.isEmpty()) {
            double totalRating = 0.0;
            for (Review review : reviews) {
                totalRating += review.getRating();
            }
            double averageRating = totalRating / reviews.size();
            product.setAverageRating(averageRating);
            productRepository.save(product);
        }
    }


//    public double calculateAverageStarRating(Product product) {
//        List<Review> reviews = reviewRepository.findByProduct(product);
//        if (reviews.isEmpty()) {
//            return 0.0; // No reviews, so average rating is 0
//        }
//
//        double totalRating = 0.0;
//        for (Review review : reviews) {
//            totalRating += review.getStarRating();
//        }
//
//        return totalRating / reviews.size();
//    }
}