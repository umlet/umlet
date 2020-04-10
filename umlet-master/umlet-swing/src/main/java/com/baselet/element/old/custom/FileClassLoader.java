package com.baselet.element.old.custom;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.baselet.control.util.Path;

public class FileClassLoader extends ClassLoader {

	public FileClassLoader() {
		super();
	}

	public FileClassLoader(ClassLoader parent) {
		super(parent);
	}

	@Override
	protected Class<?> findClass(String className) throws ClassNotFoundException {
		Class<?> c = null;
		try {
			byte[] data = loadClassData(className);
			c = defineClass(className, data, 0, data.length);
			if (c == null) {
				throw new ClassNotFoundException(className);
			}
		} catch (IOException e) {
			throw new ClassNotFoundException(className, e);
		}
		return c;
	}

	private byte[] loadClassData(String className) throws IOException {
		File f = new File(Path.temp() + className + ".class");
		byte[] buff = new byte[(int) f.length()];
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		dis.readFully(buff);
		dis.close();
		f.deleteOnExit();
		return buff;
	}
}
