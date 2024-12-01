package controllers;

import DTO.TransactionDTO;
import entities.*;
import utils.AccountUserUtils;
import utils.BookUtils;
import utils.PopupUtils;
import utils.TransactionUtils;
import database.DatabaseController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class BookReturnController {

    private static final int ROWS_PER_PAGE = 12;

    private int currentPage = 0;
    int totalRows = TransactionUtils.getTotalRowBorrowedBook();
    private int pageCount = (int) Math.ceil((double) totalRows / ROWS_PER_PAGE);

    private List<Transaction> transactionList;

    private ObservableList<TransactionDTO> transactionDTOObservableList;

    @FXML
    private TextField category_TextField;

    @FXML
    private TextField author_TextField;

    @FXML
    private TextField isbn_TextField;

    @FXML
    private TextField searchBar_TextField;

    @FXML
    private TableView<TransactionDTO> searchTable_TableView;

    @FXML
    private TableColumn<TransactionDTO, Boolean> amount_Column;

    @FXML
    private TableColumn<TransactionDTO, String> bookTitle_Column;

    @FXML
    private TableColumn<TransactionDTO, String> bookID_Column;

    @FXML
    private TableColumn<TransactionDTO, String> category_Column;

    @FXML
    private TableColumn<TransactionDTO, String> borrowDate_Column;

    @FXML
    private TableColumn<TransactionDTO, String> status_Column;

    @FXML
    private Button search_Button;

    @FXML
    private Button return_Button;

    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private Future<?> runningTask;

    @FXML
    public void initialize() {
        assignColumnValue();

        amount_Column.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());

        amount_Column.setCellFactory(CheckBoxTableCell.forTableColumn(amount_Column));

        new Thread(this::refresh).start();

        searchTable_TableView.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2) {
                TransactionDTO selectedItem = searchTable_TableView.getSelectionModel().getSelectedItem();
                Book book = BookUtils.getBookByISBN(selectedItem.getBookId());
                if (book != null) {
                    handleDoubleClick(book);
                }
            }
        });

        addTextFieldListener(searchBar_TextField);
        addTextFieldListener(category_TextField);
        addTextFieldListener(isbn_TextField);
        addTextFieldListener(author_TextField);
    }

    private void assignColumnValue() {
        bookTitle_Column.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        bookID_Column.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        category_Column.setCellValueFactory(new PropertyValueFactory<>("category"));
        borrowDate_Column.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        status_Column.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public static List<TransactionDTO> getSelectedTransactions(ObservableList<TransactionDTO> transactions) {
        if (transactions == null) {
            return null;
        }
        return transactions.stream()
                .filter(TransactionDTO::getSelected)
                .collect(Collectors.toList());
    }

    public void refresh() {
        cleanUpData();
        searchBorrowedTransactions();
        showTables();
    }

    private void showTables() {
        searchTable_TableView.setItems(transactionDTOObservableList);
    }

    private void searchBorrowedTransactions() {
        String title = searchBar_TextField.getText().isEmpty() ? null : searchBar_TextField.getText();
        String isbn = isbn_TextField.getText().isEmpty() ? null : isbn_TextField.getText();
        String author = author_TextField.getText().isEmpty() ? null : author_TextField.getText();
        String category = category_TextField.getText().isEmpty() ? null : category_TextField.getText();
        String currentUsername = LibraryManagement.getInstance().getCurrentAccount();
        transactionList = TransactionUtils.getFilteredBorrowTransactions(
                title, author, category, isbn, currentUsername, ROWS_PER_PAGE * currentPage, ROWS_PER_PAGE
        );

        transactionDTOObservableList = TransactionDTO.loadTransactionsForReturn(transactionList);
    }

    private void addTextFieldListener(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (runningTask != null && !runningTask.isDone()) {
                runningTask.cancel(true);
            }

            Task<ObservableList<TransactionDTO>> task = new Task<>() {
                @Override
                protected ObservableList<TransactionDTO> call() throws Exception {
                    try {
                        currentPage = 0;
                        searchBorrowedTransactions();
                        if (isCancelled()) return null;
                        return transactionDTOObservableList;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    transactionDTOObservableList = getValue();
                    Platform.runLater(() -> {
                        if (transactionDTOObservableList != null) {
                            showTables();
                        }
                    });
                }

                @Override
                protected void cancelled() {
                    super.cancelled();
                }
            };

            runningTask = executorService.submit(task);
        });
    }

    @FXML
    private void handleReturn() {
        List<TransactionDTO> transactions = getSelectedTransactions(transactionDTOObservableList);
        if (transactions == null) {
            return;
        }
        if (transactions.isEmpty()) {
            PopupUtils.showAlert("No books are selected");
        }
        List<String> toReturnBookID = new ArrayList<>();
        Person currentUser = AccountUserUtils.getCurrentUser();
        StringBuilder alert = new StringBuilder();
        if (currentUser == null) {
            PopupUtils.showAlert("User is null");
        }
        for (TransactionDTO transactionDTO : transactions) {
            if (transactionDTO.getStatus().equalsIgnoreCase("Returned")) {
                alert.append(transactionDTO.getBookTitle()).append("\n");
            }
        }
        if (alert.isEmpty()) {
            for (TransactionDTO transactionDTO : transactions) {
                toReturnBookID.add(transactionDTO.getBookId());
            }
            TransactionUtils.addReturnTransactions(transactions);
            BookUtils.updateBookAmountAfterBorrowed(toReturnBookID, true);
            PopupUtils.showAlert("Returned " + transactions.size() + " books successfully");
            refresh();
            cleanUpData();
        } else {
            PopupUtils.showAlert(alert.append(" had been returned!").toString());
        }
    }

    @FXML
    private void handlePreviousPageButton() {
        if (currentPage == 0) {
            return;
        }
        currentPage--;
        System.out.println("do previous page");
        searchBorrowedTransactions();
        showTables();
    }

    @FXML
    private void handleNextPageButton() {
        if (currentPage == pageCount - 1) {
            return;
        }
        currentPage++;
        System.out.println("do next page");
        searchBorrowedTransactions();
        showTables();
    }

    private void handleDoubleClick(Book book) {
        PopupUtils.openBookPopup(book, true);
    }

    private void cleanUpData() {
        searchBar_TextField.clear();
        isbn_TextField.clear();
        category_TextField.clear();
        author_TextField.clear();
    }

    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
