package publ.chou.example;

import publ.chou.example.bluetooth.BluetoothService;

public class Application {
	
	
	public static void main(String[] args) {
		
	
		BluetoothService bluetoothService = new BluetoothService();
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		bluetoothService.stop();//一直没有设备接入的情况未做处理
	}
	
}
