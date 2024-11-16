package Entity;

public class Transaction {
    private int transaction_id;
    private String book_id;
    private int user_id;
    private boolean type;
    private String time;
    private int amount;

    public Transaction(int id, String book_id, int user_id, boolean type, int amount) {
        this.transaction_id = id;
        this.book_id = book_id;
        this.user_id = user_id;
        this.type = type;
        this.amount = amount;
    }

    public int getId() {
        return transaction_id;
    }

    public void setId(int id) {
        this.transaction_id = id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
