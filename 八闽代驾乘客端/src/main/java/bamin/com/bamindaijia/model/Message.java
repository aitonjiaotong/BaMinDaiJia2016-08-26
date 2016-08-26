package bamin.com.bamindaijia.model;

/**
 * Created by zjb on 2016/7/12.
 */
public class Message {

    /**
     * title : cancelOrder
     * content : {'orderStatus':'取消订单'}
     */

    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

