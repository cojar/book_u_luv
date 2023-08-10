package com.project.bookuluv.app.article.service;

import com.project.bookuluv.app.article.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
        return searchUrl + "?ttbkey=" + apiKey + "&QueryType=" + queryType + "&MaxResults=10" + "&start=1" + "&SearchTarget=Book" + "&output=js" + "&Version=20131101" + (query != null ? "&Query=" + query : "") + "&CategoryId=0";
    }

    private String buildListUrl(String queryType) {
        return listUrl + "?ttbkey=" + apiKey + "&QueryType=" + queryType + "&MaxResults=10" + "&start=1" + "&SearchTarget=Book" + "&output=js" + "&Version=20131101";
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
                    ProductDto result = new ProductDto();
                    result.setTitle(item.optString("title"));
                    result.setLink(item.optString("link"));
                    result.setAuthor(item.optString("author"));
                    result.setPubDate(item.optString("pubDate"));
                    result.setIsbn(item.optString("isbn"));
                    result.setCategoryId(item.optLong("categoryId"));
                    result.setCategoryName(item.optString("categoryName"));
                    result.setPublisher(item.optString("publisher"));
                    result.setPriceStandard(item.optLong("priceStandard"));
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
}