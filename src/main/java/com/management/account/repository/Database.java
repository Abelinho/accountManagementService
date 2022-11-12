package com.management.account.repository;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.management.account.constant.AppConstant.INITIAL_DATA_SIZE;

@Component
public class Database<ID, T> {

    private Map<ID, T> storage;

    public Database(){
        this.storage = new ConcurrentHashMap<>(INITIAL_DATA_SIZE);
    }

    public T save(ID key, T item){
        if(Objects.nonNull(key) && Objects.nonNull(item)){
            this.storage.put(key, item);
            return this.<T>read(key);
        }
        return null;
    }

    public T update(ID key, T item){
        return this.save(key, item);
    }

    public T read(ID key){
        if(Objects.nonNull(key)){
            return (T) this.storage.get(key);
        }
        return null;
    }

    public List<T> read(List<ID> accountNumbers){
        return this.storage.entrySet().stream()
                .filter(entry -> accountNumbers.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public List<T> readAll(){
        return new ArrayList<>(this.storage.values());
    }

    public boolean delete(ID key){
        if(Objects.nonNull(key)){
            this.storage.remove(key);
            return true;
        }
        return false;
    }
}
