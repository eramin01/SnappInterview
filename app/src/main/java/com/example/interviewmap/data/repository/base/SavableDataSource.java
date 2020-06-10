package com.example.interviewmap.data.repository.base;

import io.reactivex.Completable;

public interface SavableDataSource<T> extends DataSource<T> {

    Completable saveData(T data);

}
