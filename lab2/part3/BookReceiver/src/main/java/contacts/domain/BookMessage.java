package contacts.domain;

public class BookMessage {
    private Book book;
    private String operation;

    public BookMessage() {
    }

    public BookMessage(Book book, String operation) {
        this.book = book;
        this.operation = operation;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "BookMessage{" +
                "book=" + book +
                ", operation='" + operation + '\'' +
                '}';
    }
}