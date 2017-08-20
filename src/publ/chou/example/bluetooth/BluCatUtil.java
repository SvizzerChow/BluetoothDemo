package publ.chou.example.bluetooth;

import java.lang.reflect.Field;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;

import com.intel.bluetooth.BlueCoveConfigProperties;
import com.intel.bluetooth.BlueCoveImpl;
import com.intel.bluetooth.BlueCoveLocalDeviceProperties;
import com.intel.bluetooth.BluetoothStack;

public class BluCatUtil {

	 public static void doctorDevice() throws BluetoothStateException{

	  if(System.getProperty("os.name").contains("Linux")){

	    System.err.println("Is libbluetooth3 and libbluetooth-dev installed?");

	    System.err.println("run: sudo apt-get install libbluetooth3 libbluetooth-dev");

	  }



	  if (!LocalDevice.isPowerOn()){

	   System.err.println("#There is no Bluetooth Adaptor powered on");
	   System.exit(-1);
	  }




	  System.out.println(" BlueCoveState");
	  try{
	   System.out.println("  ThreadBluetoothStackID = " + BlueCoveImpl.getThreadBluetoothStackID());
	   System.out.println("  CurrentThreadBluetoothStackID = " + BlueCoveImpl.getCurrentThreadBluetoothStackID());
	   System.out.println("  LocalDevicesID = " + BlueCoveImpl.getLocalDevicesID());
	  }catch(Exception e){
	   System.out.println("Error enabling bluecove stack: " + e.getMessage());
	   return;
	  }




	  System.out.println(" BlueCoveConfigProperties");


	  try{
	   String result = "";


	   for (Field f : BlueCoveConfigProperties.class.getDeclaredFields()){

	    if (f.getName().startsWith("PROPERTY")){
	     System.out.print( "  " + f.getName() + " = ");
	     try{
	      result = String.valueOf(LocalDevice.getProperty(String.valueOf(f.get(null))));
	     }catch(Exception e){
	      result = e.getMessage();
	     }catch(IllegalAccessError iae){
	      result = "IllegalAccessError";
	     }
	      System.out.println(result);
	    }
	   }

	  }catch(Exception e){
	   System.out.println("Error getting properties " + e.getMessage());
	   //e.printStackTrace();
	   return;
	  }


	  System.out.println(" LocalDeviceProperties");
	  try{
	   String result = "";


	   String[] deviceprops = {
	     "bluetooth.api.version",
	     "bluetooth.master.switch",
	     "bluetooth.sd.attr.retrievable.max",
	     "bluetooth.connected.devices.max",
	     "bluetooth.l2cap.receiveMTU.max",
	     "bluetooth.sd.trans.max",
	     "bluetooth.connected.inquiry.scan",
	     "bluetooth.connected.page.scan",
	     "bluetooth.connected.inquiry",
	     "bluetooth.connected.page"
	     };

	   for (String prop: deviceprops){

	    System.out.print( "  " + prop + " = ");
	    result = LocalDevice.getProperty(prop);
	    System.out.println(result);

	   }



	   for (Field f : BlueCoveLocalDeviceProperties.class.getDeclaredFields()){

	    if (f.getName().startsWith("LOCAL_DEVICE")){
	     System.out.print( "  " + f.getName() + " = ");
	     result = String.valueOf(LocalDevice.getProperty(String.valueOf(f.get(null))));
	     System.out.println(result);
	    }
	   }

	  }catch(Exception e){
	   System.out.println("Error getting local device properties " + e.getMessage());
	   //e.printStackTrace();
	   return;
	  }


	  System.out.println(" LocalDeviceFeatures");
	  try{
	   String result = "";

	   for (Field f : BluetoothStack.class.getDeclaredFields()){

	    if (f.getName().startsWith("FEATURE")){
	     System.out.print( "  " + f.getName() + " = ");
	     result = String.valueOf(BlueCoveImpl.instance().getLocalDeviceFeature((f.getInt(null))));
	     System.out.println(result);
	    }
	   }

	  }catch(Exception e){
	   System.out.println("Error getting local device features " + e.getMessage());
	   //e.printStackTrace();
	   return;
	  }
	  System.out.println("\n蓝牙驱动没问题");

	 }



	 public static String clean(String str){

	  if (str != null)
	   return str.replace("\"", "''")
	     .replace("\n", " ");
	  else
	   return str;

	 }


	}