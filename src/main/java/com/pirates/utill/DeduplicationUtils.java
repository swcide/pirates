package com.pirates.utill;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 객체 안의 멤버 중복 여부를 판단해주는 클래스
 * List 중복제거를 위해 구글링 중 좋은 util 클래스를 구현한 블로그 글이 있어 참고하였습니다.
 * <출처>
 * @author https://sunghs.tistory.com
 * @see https://github.com/sunghs/java-utils
 */
public class DeduplicationUtils {

    public static <T> List<T> deduplication(final List<T> list, Function<? super T, ?> key) {
        return list.stream()
                .filter(deduplication(key))
                .collect(Collectors.toList());
    }

    private static <T> Predicate<T> deduplication(Function<? super T, ?> key) {
        final Set<Object> set = ConcurrentHashMap.newKeySet();
        return predicate -> set.add(key.apply(predicate));
    }
}
