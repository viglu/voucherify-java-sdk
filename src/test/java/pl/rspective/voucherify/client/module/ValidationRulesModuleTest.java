package pl.rspective.voucherify.client.module;

import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.junit.Test;
import pl.rspective.voucherify.client.callback.VoucherifyCallback;
import pl.rspective.voucherify.client.model.validationRules.Conditions;
import pl.rspective.voucherify.client.model.validationRules.IdPair;
import pl.rspective.voucherify.client.model.validationRules.Junction;
import pl.rspective.voucherify.client.model.validationRules.Operator;
import pl.rspective.voucherify.client.model.validationRules.OrderValidationRules;
import pl.rspective.voucherify.client.model.validationRules.ProductValidationRules;
import pl.rspective.voucherify.client.model.validationRules.ValidationRules;
import pl.rspective.voucherify.client.model.validationRules.response.ValidationRulesResponse;
import rx.Observable;

import java.util.LinkedList;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class ValidationRulesModuleTest extends AbstractModuleTest {

  private static final ValidationRules RULES = ValidationRules.builder()
          .junction(Junction.AND)
          .id("some-id")
          .campaignName("campaign")
          .voucherCode("code")
          .orderRules(OrderValidationRules.builder()
                  .junction(Junction.AND)
                  .productsCount(new Conditions<Integer>()
                          .setCondition(Operator.$contains, new LinkedList<Integer>()))
                  .build())
          .productRules(ProductValidationRules.builder()
                  .condition(Operator.$is_not, new LinkedList<IdPair>())
                  .build())
          .build();

  @Test
  public void shouldCreateValidationRules() {
    // given
    enqueueResponse(RULES);

    // when
    ValidationRulesResponse result = client.validationRules().create(RULES);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getProductRules().getConditions().containsKey(Operator.$is_not)).isTrue();
    assertThat(result.getOrderRules().getProductsCount().getConditions().containsKey(Operator.$contains)).isTrue();
    RecordedRequest request = getRequest();
    assertThat(request.getPath()).isEqualTo("/validation-rules");
    assertThat(request.getMethod()).isEqualTo("POST");
  }

  @Test
  public void shouldGetValidationRules() {
    // given
    enqueueResponse(RULES);

    // when
    ValidationRulesResponse result = client.validationRules().get("some-id");

    // then
    assertThat(result).isNotNull();
    assertThat(result.getProductRules().getConditions().containsKey(Operator.$is_not)).isTrue();
    assertThat(result.getOrderRules().getProductsCount().getConditions().containsKey(Operator.$contains)).isTrue();
    RecordedRequest request = getRequest();
    assertThat(request.getPath()).isEqualTo("/validation-rules/some-id");
    assertThat(request.getMethod()).isEqualTo("GET");
  }

  @Test
  public void shouldUpdateValidationRules() {
    // given
    enqueueResponse(RULES);

    // when
    ValidationRulesResponse result = client.validationRules().update(RULES);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getProductRules().getConditions().containsKey(Operator.$is_not)).isTrue();
    assertThat(result.getOrderRules().getProductsCount().getConditions().containsKey(Operator.$contains)).isTrue();
    RecordedRequest request = getRequest();
    assertThat(request.getPath()).isEqualTo("/validation-rules/some-id");
    assertThat(request.getMethod()).isEqualTo("PUT");
  }

  @Test
  public void shouldDeleteValidationRules() {
    // given
    enqueueEmptyResponse();

    // when
    client.validationRules().delete("some-id");

    // then
    RecordedRequest request = getRequest();
    assertThat(request.getPath()).isEqualTo("/validation-rules/some-id");
    assertThat(request.getMethod()).isEqualTo("DELETE");
  }

  @Test
  public void shouldCreateValidationRulesAsync() {
    // given
    enqueueResponse(RULES);
    VoucherifyCallback callback = createCallback();

    // when
    client.validationRules().async().create(RULES, callback);

    // then
    await().atMost(5, SECONDS).until(wasCallbackFired());
    RecordedRequest request = getRequest();
    assertThat(request.getPath()).isEqualTo("/validation-rules");
    assertThat(request.getMethod()).isEqualTo("POST");
  }

  @Test
  public void shouldGetValidationRulesAsync() {
    // given
    enqueueResponse(RULES);
    VoucherifyCallback callback = createCallback();

    // when
    client.validationRules().async().get("some-id", callback);

    // then
    await().atMost(5, SECONDS).until(wasCallbackFired());
    RecordedRequest request = getRequest();
    assertThat(request.getPath()).isEqualTo("/validation-rules/some-id");
    assertThat(request.getMethod()).isEqualTo("GET");
  }

  @Test
  public void shouldUpdateValidationRulesAsync() {
    // given
    enqueueResponse(RULES);
    VoucherifyCallback callback = createCallback();

    // when
    client.validationRules().async().update(RULES, callback);

    // then
    await().atMost(5, SECONDS).until(wasCallbackFired());
    RecordedRequest request = getRequest();
    assertThat(request.getPath()).isEqualTo("/validation-rules/some-id");
    assertThat(request.getMethod()).isEqualTo("PUT");
  }

  @Test
  public void shouldDeleteValidationRulesAsync() {
    // given
    enqueueEmptyResponse();
    VoucherifyCallback callback = createCallback();

    // when
    client.validationRules().async().delete("some-id", callback);

    // then
    await().atMost(5, SECONDS).until(wasCallbackFired());
    RecordedRequest request = getRequest();
    assertThat(request.getPath()).isEqualTo("/validation-rules/some-id");
    assertThat(request.getMethod()).isEqualTo("DELETE");
  }

  @Test
  public void shouldCreateValidationRulesRxJava() {
    // given
    enqueueResponse(RULES);

    // when
    Observable<ValidationRulesResponse> observable = client.validationRules().rx().create(RULES);

    // then
    ValidationRulesResponse result = observable.toBlocking().first();
    assertThat(result).isNotNull();
    assertThat(result.getProductRules().getConditions().containsKey(Operator.$is_not)).isTrue();
    assertThat(result.getOrderRules().getProductsCount().getConditions().containsKey(Operator.$contains)).isTrue();
    RecordedRequest request = getRequest();
    assertThat(request.getPath()).isEqualTo("/validation-rules");
    assertThat(request.getMethod()).isEqualTo("POST");
  }

  @Test
  public void shouldGetValidationRulesRxJava() {
    // given
    enqueueResponse(RULES);

    // when
    Observable<ValidationRulesResponse> observable = client.validationRules().rx().get("some-id");

    // then
    ValidationRulesResponse result = observable.toBlocking().first();
    assertThat(result).isNotNull();
    assertThat(result.getProductRules().getConditions().containsKey(Operator.$is_not)).isTrue();
    assertThat(result.getOrderRules().getProductsCount().getConditions().containsKey(Operator.$contains)).isTrue();
    RecordedRequest request = getRequest();
    assertThat(request.getPath()).isEqualTo("/validation-rules/some-id");
    assertThat(request.getMethod()).isEqualTo("GET");
  }

  @Test
  public void shouldUpdateValidationRulesRxJava() {
    // given
    enqueueResponse(RULES);

    // when
    Observable<ValidationRulesResponse> observable = client.validationRules().rx().update(RULES);

    // then
    ValidationRulesResponse result = observable.toBlocking().first();
    assertThat(result).isNotNull();
    assertThat(result.getProductRules().getConditions().containsKey(Operator.$is_not)).isTrue();
    assertThat(result.getOrderRules().getProductsCount().getConditions().containsKey(Operator.$contains)).isTrue();
    RecordedRequest request = getRequest();
    assertThat(request.getPath()).isEqualTo("/validation-rules/some-id");
    assertThat(request.getMethod()).isEqualTo("PUT");
  }

  @Test
  public void shouldDeleteValidationRulesRxJava() {
    // given
    enqueueEmptyResponse();

    // when
    Observable<Void> observable = client.validationRules().rx().delete("some-id");

    // then
    observable.toBlocking().first();
    RecordedRequest request = getRequest();
    assertThat(request.getPath()).isEqualTo("/validation-rules/some-id");
    assertThat(request.getMethod()).isEqualTo("DELETE");
  }
}
