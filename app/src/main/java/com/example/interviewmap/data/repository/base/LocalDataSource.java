package com.example.interviewmap.data.repository.base;

import java.util.List;

public interface LocalDataSource<T> extends SavableDataSource<List<T>> {

    void clearData();

}
