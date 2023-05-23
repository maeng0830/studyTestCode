package sample.cafakiosk.learning;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class GuavaLearningTest {

	@DisplayName("주어진 개수만큼 List를 분리한다")
	@Test
	void partitionLearningTest1() {
	    // given
		List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

		// when
		List<List<Integer>> partition = Lists.partition(integers, 3);

		// then
		assertThat(partition).hasSize(2)
				.isEqualTo(List.of(List.of(1, 2, 3), List.of(4, 5, 6)));
	}

	@DisplayName("기존 리스트의 원소 개수가 주어진 개수로 나머지 없이 나눠지지 않을 경우, 마지막 리스트는 나머지 원소들을 가진다")
	@Test
	void partitionLearningTest2() {
		// given
		List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

		// when
		List<List<Integer>> partition = Lists.partition(integers, 4);

		// then
		assertThat(partition).hasSize(2)
				.isEqualTo(List.of(List.of(1, 2, 3, 4), List.of(5, 6)));
	}

	@DisplayName("multiMap은 1개의 키에 여러 개의 값을 매핑 할 수 있다")
	@Test
	void multiMapLearningTest1() {
	    // given
		Multimap<String, String> multimap = ArrayListMultimap.create();
		multimap.put("커피", "아메리카노");
		multimap.put("커피", "카페라떼");
		multimap.put("커피", "카푸치노");
		multimap.put("베이커리", "크루아상");
		multimap.put("베이커리", "식빵");

		// when
		Collection<String> strings = multimap.get("커피");

		// then
		assertThat(strings).hasSize(3)
				.isEqualTo(List.of("아메리카노", "카페라떼", "카푸치노"));
	}

	@DisplayName("multiMap은 1개의 키에 여러 개의 값을 매핑 할 수 있다")
	@TestFactory
	Collection<DynamicTest> multiMapLearningTest2() {
		// given
		Multimap<String, String> multimap = ArrayListMultimap.create();
		multimap.put("커피", "아메리카노");
		multimap.put("커피", "카페라떼");
		multimap.put("커피", "카푸치노");
		multimap.put("베이커리", "크루아상");
		multimap.put("베이커리", "식빵");

		return List.of(
				DynamicTest.dynamicTest("1개 value 삭제", () -> {
					// when
					multimap.remove("커피", "카푸치노");
					Collection<String> result = multimap.get("커피");

					// then
					assertThat(result).hasSize(2)
							.isEqualTo(List.of("아메리카노", "카페라떼"));
				}),
				DynamicTest.dynamicTest("1개 key 삭제", () -> {
					// when
					multimap.removeAll("커피");
					Collection<String> result = multimap.get("커피");

					// then
					assertThat(result).isEmpty();
				})
		);
	}
}
