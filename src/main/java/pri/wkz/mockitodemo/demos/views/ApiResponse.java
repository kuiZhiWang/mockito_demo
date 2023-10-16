package pri.wkz.mockitodemo.demos.views;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String msg;
    private T data;

    private ApiResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(int code, String msg, T data) {
        return new ApiResponse<>(code, msg, data);
    }

    public static <T> ApiResponse<T> of(int code, String msg) {
        return new ApiResponse<>(code, msg, null);
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(200, null, null);
    }

    public static <T> ApiResponse<T> ok(T obj) {
        return new ApiResponse<>(200, "", obj);
    }

    public static <T> ApiResponse<T> ok(String msg, T obj) {
        return new ApiResponse<T>(200, msg, obj);
    }

    public static <T> ApiResponse<T> error() {
        return new ApiResponse<>(500, null, null);
    }

    public static <T> ApiResponse<T> error(String msg) {
        return new ApiResponse<>(500, msg, null);
    }
}
