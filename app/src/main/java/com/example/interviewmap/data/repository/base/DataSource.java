package com.example.interviewmap.data.repository.base;

import io.reactivex.Single;

public interface DataSource<T> {

    Single<T> getData();

}
