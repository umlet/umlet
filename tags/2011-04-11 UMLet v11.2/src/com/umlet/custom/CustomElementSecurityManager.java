package com.umlet.custom;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomElementSecurityManager extends SecurityManager {

	private static HashMap<Thread, String> threads = new HashMap<Thread, String>();

	// this threads are allowed to write to files with a special extension
	private static HashMap<Thread, String> privilegedthreads = new HashMap<Thread, String>();

	protected static void addThread(Thread t, String key) {
		threads.put(t, key);
	}

	protected static void remThread(Thread t, String key) {
		if (threads.containsKey(t)) {
			if (key.equals(threads.get(t))) threads.remove(t);
		}
	}

	public static void addThreadPrivileges(Thread t, String ext) {
		if (!threads.containsKey(t)) privilegedthreads.put(t, ext);
	}

	public static void remThreadPrivileges(Thread t) {
		privilegedthreads.remove(t);
	}

	private ArrayList<String> allowedPackages;

	public CustomElementSecurityManager() {
		this.allowedPackages = new ArrayList<String>();
		this.allowedPackages.add("java.lang");
		this.allowedPackages.add("java.util");
		this.allowedPackages.add("com.baselet.control");
		this.allowedPackages.add("com.umlet.custom");
		this.allowedPackages.add("java.awt.geom");
		this.allowedPackages.add("java.awt.font");
	}

	private void customElementAccess(String debuginfo) {
		Thread thread = Thread.currentThread();
		if (threads.containsKey(thread)) {
			throw new SecurityException("No security critical tasks allowed in customized elements.");
		}
	}

	@Override
	public void checkAccept(String arg0, int arg1) {
		this.customElementAccess("checkAccept");
	}

	@Override
	public void checkAwtEventQueueAccess() {
		this.customElementAccess("checkAwtEventQueueAccess");
	}

	@Override
	public void checkConnect(String arg0, int arg1, Object arg2) {
		this.customElementAccess("checkConnect");
	}

	@Override
	public void checkConnect(String arg0, int arg1) {
		this.customElementAccess("checkConnect");
	}

	@Override
	public void checkCreateClassLoader() {
		this.customElementAccess("checkCreateClassLoader");
	}

	@Override
	public void checkDelete(String arg0) {
		this.customElementAccess("checkDelete");
	}

	@Override
	public void checkExec(String arg0) {
		this.customElementAccess("checkExec");
	}

	@Override
	public void checkExit(int arg0) {
		this.customElementAccess("checkExit");
	}

	@Override
	public void checkLink(String arg0) {

	}

	@Override
	public void checkListen(int arg0) {
		this.customElementAccess("checkListen");
	}

	@Override
	@Deprecated
	public void checkMulticast(InetAddress arg0, byte arg1) {
		this.customElementAccess("checkMulticast");
	}

	@Override
	public void checkMulticast(InetAddress arg0) {
		this.customElementAccess("checkMulticast");
	}

	@Override
	public void checkPackageAccess(String pack) {
		// System.out.println(pack);
		if (threads.containsKey(Thread.currentThread())) {
			if (!this.allowedPackages.contains(pack)) {
				this.customElementAccess("checkPackageAccess");
			}
		}
	}

	@Override
	public void checkPackageDefinition(String arg0) {
		this.customElementAccess("checkPackageDefinition");
	}

	@Override
	public void checkPrintJobAccess() {
		this.customElementAccess("checkPrintJobAccess");
	}

	@Override
	public void checkPropertiesAccess() {
		this.customElementAccess("checkPropertiesAccess");
	}

	@Override
	public void checkPropertyAccess(String arg0) {
		if (!"sun.awt.exception.handler".equals(arg0)) this.customElementAccess("checkPropertyAccess");
	}

	@Override
	public void checkRead(FileDescriptor arg0) {
		this.customElementAccess("checkRead");
	}

	@Override
	public void checkRead(String arg0, Object arg1) {

	}

	@Override
	public void checkRead(String arg0) {

	}

	@Override
	public void checkSecurityAccess(String arg0) {
		this.customElementAccess("checkSecurityAccess");
	}

	@Override
	public void checkSetFactory() {
		this.customElementAccess("checkSetFactory");
	}

	@Override
	public void checkSystemClipboardAccess() {
		this.customElementAccess("checkSystemClipboardAccess");
	}

	@Override
	public boolean checkTopLevelWindow(Object arg0) {
		try {
			this.customElementAccess("checkTopLevelWindow");
		} catch (SecurityException ex) {
			return false;
		}
		return true;
	}

	@Override
	public void checkWrite(FileDescriptor arg0) {
		this.customElementAccess("checkWrite");
	}

	@Override
	public void checkWrite(String arg0) {
		if (privilegedthreads.containsKey(Thread.currentThread())) if (arg0.equals(privilegedthreads.get(Thread.currentThread()))) return;

		this.customElementAccess("checkWrite");
	}

	@Override
	public void checkAccess(Thread arg0) {
		this.customElementAccess("checkAccess: " + arg0.toString());
	}

	@Override
	public void checkAccess(ThreadGroup arg0) {
		this.customElementAccess("checkAccess: " + arg0.toString());
	}

	@Override
	public void checkMemberAccess(Class<?> arg0, int arg1) {
		this.customElementAccess("memberaccess: " + arg0.toString() + ":" + arg1);
	}

	@Override
	public void checkPermission(Permission arg0, Object arg1) {
		this.customElementAccess("permission: " + arg0.toString() + ":" + arg1.toString());
	}

	@Override
	public void checkPermission(Permission arg0) {
		this.customElementAccess("permission: " + arg0.toString());
	}
}
