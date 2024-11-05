package App.Features;

public class PopUpElement {
    private String bookName;
    private String errorMessage;

    public PopUpElement(String bookName, String errorMessage) {
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
