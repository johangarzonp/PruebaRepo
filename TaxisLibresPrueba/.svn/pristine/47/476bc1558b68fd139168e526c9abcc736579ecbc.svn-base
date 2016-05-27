package com.cotech.taxislibres;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class TCPSockets {

	public Socket _socket;
	
	InputStreamReader streamReader;
	BufferedReader reader;
	
	public TCPSockets()
	{

		_socket = new Socket();
	

	}
	
	public void Connect()
	{
		SocketAddress address = new InetSocketAddress("200.91.204.38",11011);
		try {
			_socket.connect(address, 3600);

				
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void Write(String message)
	{
		
		try {
			OutputStream outToServer = _socket.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			out.writeUTF(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String Read()
	{
		
		try {
			
			reader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			String st = null;
            st = reader.readLine();
	         return st;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error: "+ e.getMessage();
		}
		
	}
	public void Close()
	{
		
		try {
			 _socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
