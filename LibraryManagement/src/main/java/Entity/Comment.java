package Entity;

public class Comment {

    private String comment_id;
    private String book_id;
    private String user_id;
    private String book_comment;
    private Double rate;

    public String getBook_comment() {
        return book_comment;
    }

    public void setBook_comment(String book_comment) {
        this.book_comment = book_comment;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

}
