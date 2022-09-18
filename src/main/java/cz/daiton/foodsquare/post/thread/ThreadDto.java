package cz.daiton.foodsquare.post.thread;

import javax.validation.constraints.NotEmpty;

public class ThreadDto {

    private final String required = "This field is required.";

    @NotEmpty(message = required)
    private String header;

    @NotEmpty(message = required)
    private String content;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
