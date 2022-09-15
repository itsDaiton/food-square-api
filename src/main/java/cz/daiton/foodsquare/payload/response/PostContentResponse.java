package cz.daiton.foodsquare.payload.response;

public class PostContentResponse {

    private Long Id;

    private Long appUserId;

    private String message;

    public PostContentResponse(Long id, Long appUserId, String message) {
        Id = id;
        this.appUserId = appUserId;
        this.message = message;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
