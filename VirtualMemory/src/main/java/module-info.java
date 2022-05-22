module Classes{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens Classes to javafx.fxml;
    exports Classes;
}