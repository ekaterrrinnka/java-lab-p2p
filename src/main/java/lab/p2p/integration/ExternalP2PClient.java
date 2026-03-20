package lab.p2p.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Component
public class ExternalP2PClient {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final String sourceUrl;
    private final String tradeType;
    private final String fiat;
    private final String asset;
    private final int rows;

    public ExternalP2PClient(
            ObjectMapper objectMapper,
            @Value("${integration.binance-p2p.url:https://p2p.binance.com/bapi/c2c/v2/friendly/c2c/adv/search}")
            String sourceUrl,
            @Value("${integration.binance-p2p.trade-type:BUY}") String tradeType,
            @Value("${integration.binance-p2p.fiat:UAH}") String fiat,
            @Value("${integration.binance-p2p.asset:USDT}") String asset,
            @Value("${integration.binance-p2p.rows:20}") int rows
    ) {
        this.objectMapper = objectMapper;
        this.sourceUrl = sourceUrl;
        this.tradeType = tradeType;
        this.fiat = fiat;
        this.asset = asset;
        this.rows = rows;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public List<ExternalP2POfferDto> fetchOffers() {
        List<ExternalP2POfferDto> list = new ArrayList<>();

        String requestBody = """
                {
                  "page": 1,
                  "rows": %d,
                  "payTypes": [],
                  "asset": "%s",
                  "tradeType": "%s",
                  "fiat": "%s",
                  "publisherType": null
                }
                """.formatted(rows, asset, tradeType, fiat);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(sourceUrl))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Accept-Encoding", "gzip")
                .header("User-Agent", "Mozilla/5.0")
                .header("Origin", "https://p2p.binance.com")
                .header("Referer", "https://p2p.binance.com/")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return list;
            }

            String responseBody = decodeResponseBody(response);
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode data = root.path("data");
            if (!data.isArray()) {
                return list;
            }

            for (JsonNode item : data) {
                JsonNode adv = item.path("adv");
                JsonNode advertiser = item.path("advertiser");

                ExternalP2POfferDto dto = new ExternalP2POfferDto();
                dto.setType(tradeType);
                dto.setPrice(readDecimal(adv, "price"));
                dto.setMinLimit(readDecimal(adv, "minSingleTransAmount"));
                dto.setMaxLimit(readDecimal(adv, "dynamicMaxSingleTransAmount"));
                dto.setFiatCurrency(adv.path("fiatUnit").asText(fiat));
                dto.setPaymentMethod(readPaymentMethod(adv.path("tradeMethods")));
                dto.setMerchantName(advertiser.path("nickName").asText("unknown"));
                dto.setMerchantOrdersCount(parseIntegerFromString(advertiser.path("monthOrderCount").asText("0")));
                dto.setMerchantSuccessPercent(parsePercent(advertiser.path("monthFinishRate").asText("0")));

                if (dto.getPrice() != null) {
                    list.add(dto);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return list;
        } catch (IOException e) {
            return list;
        }

        return list;
    }

    private String decodeResponseBody(HttpResponse<byte[]> response) throws IOException {
        String encoding = response.headers().firstValue("Content-Encoding").orElse("");
        byte[] raw = response.body();
        if ("gzip".equalsIgnoreCase(encoding)) {
            try (InputStream is = new GZIPInputStream(new ByteArrayInputStream(raw))) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        }
        return new String(raw, StandardCharsets.UTF_8);
    }

    private BigDecimal readDecimal(JsonNode node, String field) {
        String raw = node.path(field).asText(null);
        if (raw == null || raw.isBlank()) {
            return null;
        }
        return new BigDecimal(raw);
    }

    private String readPaymentMethod(JsonNode tradeMethods) {
        if (tradeMethods.isArray() && !tradeMethods.isEmpty()) {
            return tradeMethods.get(0).path("tradeMethodName").asText("UNKNOWN");
        }
        return "UNKNOWN";
    }

    private Integer parseIntegerFromString(String raw) {
        String value = raw == null ? "" : raw.replaceAll("[^0-9]", "");
        if (value.isBlank()) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    private Double parsePercent(String raw) {
        String value = raw == null ? "" : raw.replace("%", "").trim();
        if (value.isBlank()) {
            return 0.0;
        }
        return Double.parseDouble(value);
    }
}
