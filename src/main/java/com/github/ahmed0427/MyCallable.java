package com.github.ahmed0427;

import java.util.List;

interface MyCallable {
    int parametersCount();
    Object call(List<Object> arguments);
}
