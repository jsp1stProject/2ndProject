package com.sist.web.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
	private String code;
	private T data;
	private String message;
	
	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>("200", data, "성공");
	}
	
	public static <T> ApiResponse<T> success(T data, String message) {
		return new ApiResponse<>("200", data, message);
	}
	
	public static <T> ApiResponse<T> fail(String code, String message) {
		return new ApiResponse<>(code, null, message);
	}
	
	public static <T> ApiResponse<T> fail(String code, T data, String message) {
		return new ApiResponse<>(code, data, message);
	}
}
