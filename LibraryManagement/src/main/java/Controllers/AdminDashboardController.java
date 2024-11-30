package Controllers;

import DTO.TransactionDTO;
import Entity.LibraryManagement;
import Entity.Transaction;
import Utils.PopupUtils;
import Utils.TransactionUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AdminDashboardController {

    private static final int ROWS_PER_PAGE = 23;

    private int currentPage = 0;
    int totalRows = TransactionUtils.getTotalOverDueTransactions();
    private int pageCount = (int) Math.ceil((double) totalRows / ROWS_PER_PAGE);

    @FXML
    private TableView<TransactionDTO> overdue_TableView;

    @FXML
    private TableColumn<TransactionDTO, Boolean> bookID_Column;

    @FXML
    private TableColumn<TransactionDTO, String> title_Column;

    @FXML
    private TableColumn<TransactionDTO, String> username_Column;

    @FXML
    private TableColumn<TransactionDTO, String> time_Column;

    @FXML
    private TableColumn<TransactionDTO, String> note_Column;

    private ObservableList<TransactionDTO> overdueList;

    @FXML
    private void initialize() {
        assignColumnValue();
        overdue_TableView.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2) {
                TransactionDTO selectedItem = overdue_TableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    handleDoubleClick(selectedItem.getUsername());
                }
            }
        });
        refreshOverdueTableView();
    }

    private void assignColumnValue() {
        time_Column.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        bookID_Column.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        title_Column.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        username_Column.setCellValueFactory(new PropertyValueFactory<>("username"));
        note_Column.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void refreshOverdueTableView() {
        List<Transaction> transactionList = TransactionUtils.getOverdueTransactions(ROWS_PER_PAGE * currentPage, ROWS_PER_PAGE);
        overdueList = TransactionDTO.loadTransactionsForAdminDashboard(transactionList);
        overdue_TableView.setItems(overdueList);
    }

    private void handleDoubleClick(String username) {
        if (username.equals(LibraryManagement.getInstance().getCurrentAccount())) {
            return;
        }
        PopupUtils.openQuickMessage(username);
    }

    @FXML
    private void handlePreviousPageButton() {
        if (currentPage == 0) {
            return;
        }
        currentPage--;
        System.out.println("do previous page");
        refreshOverdueTableView();
    }

    @FXML
    private void handleNextPageButton() {
        if (currentPage == pageCount - 1) {
            return;
        }
        currentPage++;
        System.out.println("do next page");
        refreshOverdueTableView();
    }

    public void cleanUp() {
        refreshOverdueTableView();
    }
}
