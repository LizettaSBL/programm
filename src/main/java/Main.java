
import java.util.concurrent.Exchanger; /*подкючение пакета для класса Exchanger*/


public class Main { /*создание публичного класса Main*/

    public static void main(String[] args){ /*точка входа в программу*/
        new Thread(new ArduinoConnection()).start(); /*запуск 1-ого потока*/
        new Thread(new SocketServer()).start(); /*запуск 2-ого потока*/
    }
}
