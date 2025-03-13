module cw.tickettrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens cw.tickettrack to javafx.fxml;
    exports cw.tickettrack;
}