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
    private final String baseUrl = "http://www.aladdin.co.kr/ttb/api/ItemSearch.aspx";

    private String buildUrl(String queryType, String query) {
        return baseUrl +
                "?ttbkey=" + apiKey +
                "&QueryType=" + queryType +
                "&MaxResults=10" +
                "&start=1" +
                "&SearchTarget=Book" +
                "&output=js" +
                "&Version=20131101" +
                (query != null ? "&Query=" + query : "") +
                "&CategoryId=0";
    }

    public List<ProductDto> searchBooks(String query) {
        String url = buildUrl("Keyword", query);
        return getBooksFromApi(url);
    }

    public List<ProductDto> getNewBooks() {
        String url = buildUrl("ItemNewAll", null);
        return getNewBooks(url);
    }

    public List<ProductDto> getBestsellers() {
        String url = buildUrl("Bestseller", null);
        return getBestsellers(url);
    }

    private List<ProductDto> getNewBooks(String url) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String responseBody = responseEntity.getBody();
        System.out.println(responseBody);

        ArrayList<ProductDto> newBooks = new ArrayList<>();

        if (responseBody != null) {
            JSONObject responseJson = new JSONObject(responseBody);
            JSONArray items = responseJson.optJSONArray("item");

            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    ProductDto newBook = new ProductDto();
                    newBook.setTitle(item.getString("title"));
                    newBook.setLink(item.getString("link"));
                    newBook.setAuthor(item.getString("author"));
                    newBook.setPubDate(item.getString("pubDate"));
                    newBook.setIsbn(item.getString("isbn"));
                    newBook.setCategoryId(item.getLong("categoryId"));
                    newBook.setCategoryName(item.getString("categoryName"));
                    newBook.setPublisher(item.getString("publisher"));
                    newBook.setPriceStandard(item.getLong("priceStandard"));
                    newBooks.add(newBook);
                }
            } else {
                System.out.println("JSON에서 'item' 키를 찾을 수 없음.");
            }
        } else {
            System.out.println("응답 본문이 null입니다.");
        }

        return newBooks;
    }

    private List<ProductDto> getBestsellers(String url) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String responseBody = responseEntity.getBody();
        System.out.println(responseBody);

        ArrayList<ProductDto> Bestsellers = new ArrayList<>();

        if (responseBody != null) {
            JSONObject responseJson = new JSONObject(responseBody);
            JSONArray items = responseJson.optJSONArray("item");

            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    ProductDto Bestseller = new ProductDto();
                    Bestseller.setTitle(item.getString("title"));
                    Bestseller.setLink(item.getString("link"));
                    Bestseller.setAuthor(item.getString("author"));
                    Bestseller.setPubDate(item.getString("pubDate"));
                    Bestseller.setIsbn(item.getString("isbn"));
                    Bestseller.setCategoryId(item.getLong("categoryId"));
                    Bestseller.setCategoryName(item.getString("categoryName"));
                    Bestseller.setPublisher(item.getString("publisher"));
                    Bestseller.setPriceStandard(item.getLong("priceStandard"));
                    Bestsellers.add(Bestseller);
                }
            } else {
                System.out.println("JSON에서 'item' 키를 찾을 수 없음.");
            }
        } else {
            System.out.println("응답 본문이 null입니다.");
        }

        return Bestsellers;
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
                    result.setTitle(item.getString("title"));
                    result.setLink(item.getString("link"));
                    result.setAuthor(item.getString("author"));
                    result.setPubDate(item.getString("pubDate"));
                    result.setIsbn(item.getString("isbn"));
                    result.setCategoryId(item.getLong("categoryId"));
                    result.setCategoryName(item.getString("categoryName"));
                    result.setPublisher(item.getString("publisher"));
                    result.setPriceStandard(item.getLong("priceStandard"));
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