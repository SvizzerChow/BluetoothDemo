package publ.chou.example.bluetooth;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BluetoothService implements Runnable {
	private Boolean stopFlag = false;

	private LocalDevice local = null;

	// 流连接
	private StreamConnection streamConnection = null;
	// 接受数据的字节流
	private byte[] acceptdByteArray = new byte[1024];
	// 输入流
	private DataInputStream inputStream;
	private StreamConnectionNotifier notifier;
	
	private  final static ExecutorService service = Executors.newCachedThreadPool();
	

	public BluetoothService() {
		try {
			BluCatUtil.doctorDevice(); 					// 驱动检查
			RemoteDeviceDiscovery.runDiscovery();		// 搜索附近所有的蓝牙设备
			// System.out.println(RemoteDeviceDiscovery.getDevices());
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			local = LocalDevice.getLocalDevice();

			if (!local.setDiscoverable(DiscoveryAgent.GIAC))
				System.out.println("请将蓝牙设置为可被发现");

			/*Set<RemoteDevice> devicesDiscovered = RemoteDeviceDiscovery.getDevices();		//附近所有的蓝牙设备，必须先执行 runDiscovery
			if (devicesDiscovered.iterator().hasNext()) {									//连接
				RemoteDevice first = devicesDiscovered.iterator().next();
				streamConnection = (StreamConnection) Connector.open("btspp://" + first.getBluetoothAddress() + ":1");
			}*/
			/**
			 * 作为服务端，被请求
			 */
			String url = "btspp://localhost:" +  new UUID(80087355).toString() + ";name=RemoteBluetooth";  
            notifier = (StreamConnectionNotifier)Connector.open(url);

		} catch (IOException e) {
			e.printStackTrace();
		}
		service.submit(this);
	}

	@Override
	public void run() {
		try {
			String inStr = null;
			streamConnection = notifier.acceptAndOpen();				//阻塞的，等待设备连接,这里就没有作中断处理了，如果没有连接该线程就无法关闭
			inputStream = streamConnection.openDataInputStream();
			int length;
			while (true) {
				if ((inputStream.available()) <= 0) {					//不阻塞线程
					if (stopFlag)										//UI停止后，关闭
						break;
					Thread.sleep(800);									//数据间隔比较长，手动堵塞线程
				} else {
					length = inputStream.read(acceptdByteArray);
					if(length>0) {
						inStr = new String(acceptdByteArray,0,length);
						System.out.println(inStr);
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
				if (streamConnection != null)
					streamConnection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public synchronized void stop() {
		stopFlag = true;
		service.shutdown();
	}
}
