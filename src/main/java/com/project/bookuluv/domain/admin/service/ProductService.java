package com.project.bookuluv.domain.admin.service;

import com.project.bookuluv.domain.admin.domain.Product;
import com.project.bookuluv.domain.admin.dto.ProductDto;
import com.project.bookuluv.domain.admin.repository.ProductRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final RestTemplate restTemplate;

    private final OAuth2AuthorizedClientService clientService;

    private final ProductRepository productRepository;

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

    // 국내, 외국 도서 목록 페이징에 담아서 불러오는 service
    public Page<Product> getProducts(String type, int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page - 1, 20, Sort.by(sorts));
        return this.productRepository.findAllByKeyword(kw, type.toUpperCase(), pageable);
    }

    private String buildSearchUrl(String queryType, String query) {
        return searchUrl + "?ttbkey=" + apiKey + "&QueryType=" + queryType + "&MaxResults=20" + "&start=1" + "&SearchTarget=Book" + "&output=js" + "&Version=20131101" + (query != null ? "&Query=" + query : "") + "&CategoryId=0";
    }

    private String buildListUrl(String queryType) {
        return listUrl + "?ttbkey=" + apiKey + "&QueryType=" + queryType + "&MaxResults=5" + "&start=1" + "&SearchTarget=Book&Foreign" + "&output=js" + "&Version=20131101";
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
                            .categoryName(item.optString("categoryName"))
                            .publisher(item.optString("publisher"))
                            .priceStandard(item.optLong("priceStandard"))
                            .priceSales(item.optLong("priceSales"))
                            .mallType(item.optString("mallType"))
                            .build();

                    Product product = Product.builder()
                            .coverImg(result.getCoverImg())
                            .title(result.getTitle())
                            .link(result.getLink())
                            .author(result.getAuthor())
                            .pubDate(item.optString("pubDate"))
                            .description(item.optString("description"))
                            .publisher(result.getPublisher())
                            .isbn(result.getIsbn())
                            .categoryName(result.getCategoryName())
                            .priceStandard(result.getPriceStandard())
                            .priceSales(result.getPriceSales())
                            .mallType(result.getMallType())
                            .build();

                    if (productRepository.countByIsbn(product.getIsbn()) == 0L) {//productRepository에 isbn이 0개라면 저장해라(0L의 L은 Long 타입이라 사용)
                        productRepository.save(product);
                    }
                    results.add(result);
                }
            }
        }
        return results;
    }

    public List<Product> getAll() {
        return this.productRepository.findAll();
    }

    public Product findById(Long id) {
        return this.productRepository.getById(id);
    }

    public Optional<Product> getById(Long id) {
        return this.productRepository.findById(id);
    }
}