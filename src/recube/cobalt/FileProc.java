package recube.cobalt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class FileProc
{
	GameView gameView;
	String fileName;
	FileInputStream fis;
	ObjectInputStream ois;
	FileOutputStream fos;
	ObjectOutputStream oos;
	
	public FileProc(GameView _gameView, String name)
	{
		gameView = _gameView;
		fileName = name;
	}
	
	public boolean InitInput()
	{
		try 
		{
			fis = gameView.getContext().openFileInput(fileName);
			ois = new ObjectInputStream(fis);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean InitOutput()
	{
		try
		{
			fos = gameView.getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void DestInput()
	{
		if(fis != null)
		{
			try
			{
				ois.close();
				fis.close();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			ois = null;
			fis = null;
		}
	}
	
	public void DestOutput()
	{
		if(fos != null)
		{
			try
			{
				oos.close();
				fos.close();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			oos = null;
			fos = null;
		}
	}
	
	public void WriteByte(byte d)
	{
		try
		{
			oos.writeByte(d);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void WriteChar(char d)
	{
		try
		{
			oos.writeChar(d);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void WriteShort(short d)
	{
		try
		{
			oos.writeShort(d);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void WriteInteger(int d)
	{
		try
		{
			oos.writeInt(d);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void WriteLong(long d)
	{
		try
		{
			oos.writeLong(d);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void WriteSingle(float d)
	{
		try
		{
			oos.writeFloat(d);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void WriteDouble(double d)
	{
		try
		{
			oos.writeDouble(d);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void WriteString(String d)
	{
		try
		{
			oos.writeChars(d);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void WriteUnicodeString(String d)
	{
		try
		{
			oos.writeUTF(d);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void WriteBoolean(boolean d)
	{
		try
		{
			oos.writeBoolean(d);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void Write(Object d)
	{
		try
		{
			oos.writeObject(d);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public byte ReadByte() throws IOException
	{
		return ois.readByte();
	}
	
	public char ReadChar() throws IOException
	{
		return ois.readChar();
	}
	
	public short ReadShort() throws IOException
	{
		return ois.readShort();
	}
	
	public int ReadInteger() throws IOException
	{
		return ois.readInt();
	}
	
	public long ReadLong() throws IOException
	{
		return ois.readLong();
	}
	
	public float ReadSingle() throws IOException
	{
		return ois.readFloat();
	}
	
	public double ReadDouble() throws IOException
	{
		return ois.readDouble();
	}
	
	public String ReadString() throws IOException
	{
		return ois.readLine();
	}
	
	public String ReadUTF() throws IOException
	{
		return ois.readUTF();
	}
	
	public boolean ReadBoolean() throws IOException
	{
		return ois.readBoolean();
	}

	public Object Read() throws IOException, ClassNotFoundException
	{
		return ois.readObject();
	}
}