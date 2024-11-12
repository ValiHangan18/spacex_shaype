package exception;


import org.springframework.http.HttpStatusCode;

public class MyCustomException extends RuntimeException {

    private HttpStatusCode httpStatusCode;
    private String body;

    public MyCustomException(HttpStatusCode httpStatusCode, String body) {
        this.httpStatusCode = httpStatusCode;
        this.body = body;
    }

    public HttpStatusCode getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public String getBody() {
        return this.body;
    }

}
