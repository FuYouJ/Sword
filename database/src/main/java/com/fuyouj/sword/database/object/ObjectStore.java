package com.fuyouj.sword.database.object;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * ObjectStore，对象持久。对象的特点具有灵活的schema，该持久层主要的设计目的是为持久化对象设计的，具有以下几个特征
 * 1. 无需序列化，反序列化
 * 2. 查询可以是基于对象的 //objectStore.find(item -> item.getName() =="indicator1", 10)
 * 3. 小数据量，但是内存膨胀
 * 4. 数据存储堆内
 * 5. 灵活的meta
 * 6. 理论占总体内存量极低
 */
public interface ObjectStore {
    <T> int count(Matcher<T> option, Class<T> tClass);

    <T> int count(Matcher<T> option, String collectionName);

    <T> int count(Class<T> tClass);

    <T> int count(String collectionName);

    <T> T create(T item, Class<T> tClass);

    <T> T create(T item, String collectionName);

    default <T> T createOrUpdate(T item, Class<T> tClass) {
        return this.save(item, tClass);
    }

    default <T> T createOrUpdate(T item, String collectionName) {
        return this.save(item, collectionName);
    }

    <T> boolean delete(T item, Class<T> tClass);

    <T> boolean delete(T item, String collectionName);

    <T> void deleteAll(Class<T> tClass);

    <T> void deleteAll(String collectionName);

    <T> Optional<T> deleteAndGetById(Object id, Class<T> tClass);

    <T> Optional<T> deleteAndGetById(Object id, String collectionName);

    <T> List<T> deleteAndGetMany(Matcher<T> option, Class<T> tClass);

    <T> List<T> deleteAndGetMany(Matcher<T> option, String collectionName);

    <T> boolean deleteById(Object id, Class<T> tClass);

    <T> boolean deleteById(Object id, String collectionName);

    void deleteByIds(Collection<Object> ids, String collectionName);

    void deleteByIds(Collection<Object> ids, Class<?> tClass);

    <T> void deleteMany(Matcher<T> option, Class<T> tClass);

    <T> void deleteMany(Matcher<T> option, String collectionName);

    <T> boolean exists(Matcher<T> option, Class<T> tClass);

    <T> boolean exists(Matcher<T> option, String collectionName);

    <T> boolean existsById(Object id, Class<T> tClass);

    <T> boolean existsById(Object id, String collectionName);

    <T> List<T> find(QueryOption<T> queryOption, Class<T> tClass);

    <T> List<T> find(QueryOption<T> queryOption, String collectionName);

    <T> Optional<T> findById(Object id, Class<T> tClass);

    <T> Optional<T> findById(Object id, String collectionName);

    <T> Optional<T> findOne(QueryOption<T> queryOption, Class<T> tClass);

    <T> Optional<T> findOne(QueryOption<T> queryOption, String collectionName);

    @Deprecated
    void register(Class<?> tClass);

    <T> T save(T item, Class<T> tClass);

    <T> T save(T item, String collectionName);

    <T> T updateById(Object id, T item, String collectionName);

    <T> T updateById(Object id, Updater<T> updater, Class<T> tClass);

    <T> T updateById(Object id, Updater<T> updater, String collectionName);

    <T> void updateMany(Matcher<T> option, Updater<T> updater, Class<T> tClass);

    <T> void updateMany(Matcher<T> option, Updater<T> updater, String collectionName);

    <T> T updateOne(Matcher<T> option, Updater<T> updater, Class<T> tClass);

    <T> T updateOne(Matcher<T> option, Updater<T> updater, String collectionName);
}
