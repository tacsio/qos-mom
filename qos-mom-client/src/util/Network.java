package util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Network {

	public static String getLocalIpAddress() {
		String ip = null;
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();
			if (ip.equals("127.0.0.1") || ip.equals("0.0.0.0")) {
				ip = Network.getLocalIpAddressByInterfaces();
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}

	private static String getLocalIpAddressByInterfaces()
			throws SocketException {
		String ip = null;
		Enumeration<NetworkInterface> interfaces = NetworkInterface
				.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface current = interfaces.nextElement();
			if (!current.isUp()
					|| current.isLoopback()
					|| current.isVirtual()
					|| !(current.getDisplayName().equals("eth0") || current
							.getDisplayName().equals("wlan0")))
				continue;
			Enumeration<InetAddress> addresses = current.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress current_addr = addresses.nextElement();
				if (current_addr.isLoopbackAddress())
					continue;
				if (current.getDisplayName().equals("eth0")
						|| current.getDisplayName().equals("wlan0")) {
					ip = current_addr.getHostAddress();
				}
			}
		}
		return ip;
	}

}
