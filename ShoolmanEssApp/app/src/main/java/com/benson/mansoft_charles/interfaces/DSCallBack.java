package com.benson.mansoft_charles.interfaces;

import com.android.volley.VolleyError;

public interface DSCallBack<T, E> {
    public T onResponse(String response) throws Exception;

    public E onError(VolleyError error) throws Exception;
}
