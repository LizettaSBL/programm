import jssc.SerialPort; /*импорт необходимых пакетов*/
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class ArduinoConnection implements Runnable, SerialPortEventListener{/*создание публичного класса, 1-ый поток. Реализациция интерфейса Runnable и SerialPortEventListener*/

    private static SerialPort serialPort;
    static volatile int destenation; /*создание целой переменной для хранения значения датчика. volatile позволяет параллельно обновлять данные в другом потоке */

    @Override
    public void run() {/*Точка входа в первый поток исполнения*/

        serialPort = new SerialPort("COM3");
        try { //обработка исключений, настройка прослушивания порта
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
            serialPort.addEventListener(this::serialEvent);
        } catch (SerialPortException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() > 0) {
            try {
                String data = serialPort.readString(serialPortEvent.getEventValue());
                serialPort.writeString("Get data");
                String trim = data.trim();
                if(trim.length() > 0 && !trim.contains("-")){ //проверка строковго значения на отрицание и лишнии знаки
                    int val = Integer.valueOf(trim); //перевод перменной из строкого в целый тип
                    setDestenation( Math.abs(val)); //выборка положительных значний с платы
                    System.out.println(destenation + " from Arduino"); //вывод полученных с платы значений
                }
            } catch (SerialPortException  ex) {
                System.out.println(ex);
            }
        }
    }

    public int getDestenation() { /*метод, который возвращает значение датчика*/
        return destenation;
    }

    public void setDestenation(int destenation) { /*метод, который передает значение с платы в переменную*/
        this.destenation = destenation;
    }
}
