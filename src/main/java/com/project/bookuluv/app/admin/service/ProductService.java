package com.project.bookuluv.app.admin.service;

import com.project.bookuluv.app.admin.domain.Product;
import com.project.bookuluv.app.admin.dto.ProductDto;
import com.project.bookuluv.app.admin.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    private String buildSearchUrl(String queryType, String query) {
        return searchUrl + "?ttbkey=" + apiKey + "&QueryType=" + queryType + "&MaxResults=12" + "&start=1" + "&SearchTarget=Book" + "&output=js" + "&Version=20131101" + (query != null ? "&Query=" + query : "") + "&CategoryId=0";
    }

    private String buildListUrl(String queryType) {
        return listUrl + "?ttbkey=" + apiKey + "&QueryType=" + queryType + "&MaxResults=4" + "&start=1" + "&SearchTarget=Book" + "&output=js" + "&Version=20131101";
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
                            .isbn(item.optString("isbn"))
                            .categoryName(item.optString("categoryName"))
                            .publisher(item.optString("publisher"))
                            .priceStandard(item.optLong("priceStandard"))
                            .priceSales(item.optLong("priceSales")).build();

                    Product product = Product.builder()
                            .coverImg(result.getCoverImg())
                            .title(result.getTitle())
                            .link(result.getLink())
                            .author(result.getAuthor())
                            .publisher(result.getPublisher())
                            .isbn(result.getIsbn())
                            .categoryName(result.getCategoryName())
                            .priceStandard(result.getPriceStandard())
                            .priceSales(result.getPriceSales())
                            .build();

                    productRepository.save(product);


                    results.add(result);
                }
            } else {
                System.out.println("JSON에서 'item' 키를 찾을 수 없음.");
            }
        } else {
            System.out.println("응답 본문이 null입니다.");
        }

        return results;
    }

    public String search(String query, Authentication authentication) {
        OAuth2AuthorizedClient oAuth2Client = clientService.loadAuthorizedClient("google",
                authentication.getName());
        String accessToken = oAuth2Client.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }

    public List<Product> getAll() {
        return this.productRepository.findAll();
    }
}