package zd.zdcommons.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Message {
    private String action;
    private String title;
    private List<String> messages;

    @Override
    public String toString() {
        return "Message{" +
                "action='" + action + '\'' +
                ", title='" + title + '\'' +
                ", messages=" + messages +
                '}';
    }
}
