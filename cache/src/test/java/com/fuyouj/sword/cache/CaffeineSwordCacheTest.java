package com.fuyouj.sword.cache;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.fuyouj.sword.scabard.Lists2;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CaffeineSwordCacheTest {
    @Test
    public void should_get_all_keys() throws InterruptedException {
        SwordCache<Object, Object> unboundedCache = SwordCacheFactory.unboundedCache();

        unboundedCache.put("key1", "value1", 100, TimeUnit.MILLISECONDS);
        unboundedCache.put("key2", "value2", 200, TimeUnit.MILLISECONDS);
        unboundedCache.put("key2", "value2-updated", 200, TimeUnit.MILLISECONDS);
        unboundedCache.put("key3", "value3", 300, TimeUnit.MILLISECONDS);
        unboundedCache.put("key4", "value4");
        unboundedCache.put("key5", "value5");

        Thread.sleep(100);

        Map<Object, Object> cacheMap = unboundedCache.getAllPresent(Lists2.items("key1", "key2", "key3", "key4", "key5"));

        assertThat(cacheMap.size()).isEqualTo(4);
        assertThat(cacheMap.get("key2")).isEqualTo("value2-updated");
        assertThat(cacheMap.get("key3")).isEqualTo("value3");
        assertThat(cacheMap.get("key4")).isEqualTo("value4");
        assertThat(cacheMap.get("key5")).isEqualTo("value5");

        Thread.sleep(300);

        cacheMap = unboundedCache.getAllPresent(Lists2.items("key1", "key2", "key3", "key4", "key5"));

        assertThat(cacheMap.size()).isEqualTo(2);
        assertThat(cacheMap.get("key4")).isEqualTo("value4");
        assertThat(cacheMap.get("key5")).isEqualTo("value5");
    }

    @Test
    public void should_get_with_expiry() throws InterruptedException {
        SwordCache<Object, Object> unboundedCache = SwordCacheFactory.unboundedCache();

        unboundedCache.put("key1", "value1", 100, TimeUnit.MILLISECONDS);
        for (int index = 0; index < 100; index++) {
            assertThat(unboundedCache.getIfPresent("key1")).isEqualTo("value1");
        }

        Thread.sleep(100);

        for (int index = 0; index < 100; index++) {
            assertThat(unboundedCache.getIfPresent("key1")).isEqualTo(null);
        }
    }

    @Test
    public void should_get_with_mapping() throws InterruptedException {
        SwordCache<Object, Object> unboundedCache = SwordCacheFactory.unboundedCache();

        assertThat(unboundedCache.get("key1", o -> 1)).isEqualTo(1);
        assertThat(unboundedCache.getIfPresent("key1")).isEqualTo(1);

        unboundedCache.put("key1", "value", 50, TimeUnit.MILLISECONDS);
        Thread.sleep(50);
        assertThat(unboundedCache.getIfPresent("key1")).isEqualTo(null);
        assertThat(unboundedCache.get("key1", o -> "abc")).isEqualTo("abc");
        assertThat(unboundedCache.getIfPresent("key1")).isEqualTo("abc");
    }

    @Test
    public void should_put_to_cache() {
        SwordCache<Object, Object> unboundedCache = SwordCacheFactory.unboundedCache();

        Lists2.items(
                new KeyValue("key1", "value1"),
                new KeyValue("key1", "value2"),
                new KeyValue("key2", "value2"),
                new KeyValue(1, 10),
                new KeyValue(1.1, 10.0),
                new KeyValue(new Object(), 10.0),
                new KeyValue(new Object(), "value"),
                new KeyValue(false, null),
                new KeyValue(false, "value"),
                new KeyValue(true, null),
                new KeyValue(null, null),
                new KeyValue(new KeyValue(null, null), new KeyValue(null, null))
        ).forEach(keyValue -> {
            System.out.println(keyValue);
            unboundedCache.put(keyValue.key, keyValue.value);
            assertThat(unboundedCache.getIfPresent(keyValue.key)).isEqualTo(keyValue.value);
        });
    }

    @Test
    public void should_put_to_cache_with_expiry() throws InterruptedException {
        SwordCache<Object, Object> unboundedCache = SwordCacheFactory.unboundedCache();

        assertThat(unboundedCache.putIfAbsent("key1", "value1", 500, TimeUnit.MILLISECONDS)).isEqualTo(true);
        assertThat(unboundedCache.putIfAbsent("key1", "value2", 100, TimeUnit.MILLISECONDS)).isEqualTo(false);

        Thread.sleep(500);

        assertThat(unboundedCache.putIfAbsent("key1", "value3", 1, TimeUnit.SECONDS)).isEqualTo(true);
    }

    static class KeyValue {
        private Object key;
        private Object value;

        KeyValue(final Object key, final Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "KeyValue{" + "key=" + key + ", value=" + value + '}';
        }
    }
}
