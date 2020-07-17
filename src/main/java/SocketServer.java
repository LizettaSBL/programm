import com.corundumstudio.socketio.Configuration; /*импорт пакета для настройки сервера*/
import com.corundumstudio.socketio.SocketIOServer; /*импорт пакета для socket.io сервера*/

public class SocketServer implements Runnable { /*создание публичного класса Socket.2-ой поток. Реализациция интерфейса Runnable*/
    @Override
    public void run() { /*Точка входа во второй поток исполнения*/
        Configuration config = new Configuration(); /*создание объекта типа configuration*/
        config.setHostname("localhost"); /*устанавливает Ip*/
        config.setPort(9092);/*устанавливается порт 9092 для передачи данных протокол управления передачей данных (TCP)*/
        final SocketIOServer server = new SocketIOServer(config);
        System.out.println("Server SocketIO start on port 9092"); /*вывод строки о работе socket.io*/
        server.start(); /*начало работы сервера*/
        try { /*обработка исключений*/
            while (true) {
                int destenation = new ArduinoConnection().getDestenation();
                System.out.println(destenation + " from socket"); /*проверка работы socket. Вывод значения с платы и  установленной строки*/
                Thread.sleep(1000); /*задержка между выводом данных*/
                server.getBroadcastOperations().sendEvent("chatevent", destenation);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        server.stop(); /*сообщает серверу прекратить прослушивание порта*/
    }

}
