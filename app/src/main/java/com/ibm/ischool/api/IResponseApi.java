package com.ibm.ischool.api;

public interface IResponseApi<T> {

	void onSuccess(T response);
	
	void onFailure(String msg);
}
