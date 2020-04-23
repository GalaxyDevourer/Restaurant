/*
Створіть слідуючу модель- є ресторан , який має певне меню страв.
Клієнт, знаходячись за столиком ресторану, може замовити певну страву при цьому у нього є можливість корегувати
складові інградієнти страви на свій розсуд(мається на увазі кількість- не 5гр. перцю а 10).
Це повинно впливати на вартість блюда. При цьому кухар може у будь-який час сформувати список замовлень,
які поки ще не отримали клієнти ресторану. Клієнт має можливість оцінити якість обслуговування після того,
як отримав своє замовлення. Директор ресторану має можливість перевірити якість обслуговування за період
по відповідним звітам, знайти найбільш популярну страву за період та скільки грошей за цей період отримав за страви.
*/

package MainPack.pack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage stage = new Stage();

    public static void closeStage() {
        stage.close();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Gui/registration.fxml"));
        stage.setTitle("La-PetraGraves Restaurant");
        stage.setScene(new Scene(root));
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}