package Entity;

public class PopupElement {

    private String bookName;
    private String errorMessage;

    public PopupElement(String bookName, String errorMessage) {
        this.bookName = bookName;
        this.errorMessage = errorMessage;
    }

    public String getBookName() {
        return bookName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
