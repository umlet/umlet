package com.baselet.control.config;

public class ConfigMail {
	private static final ConfigMail instance = new ConfigMail();

	public static ConfigMail getInstance() {
		return instance;
	}

	private String mail_smtp = "";
	private boolean mail_smtp_auth = false;
	private String mail_smtp_user = "";
	private boolean mail_smtp_pw_store = false;
	private String mail_smtp_pw = "";
	private String mail_from = "";
	private String mail_to = "";
	private String mail_cc = "";
	private String mail_bcc = "";
	private boolean mail_xml = true;
	private boolean mail_gif = true;
	private boolean mail_pdf = false;

	private ConfigMail() {}

	public String getMail_smtp() {
		return mail_smtp;
	}

	public void setMail_smtp(String mail_smtp) {
		this.mail_smtp = mail_smtp;
	}

	public boolean isMail_smtp_auth() {
		return mail_smtp_auth;
	}

	public void setMail_smtp_auth(boolean mail_smtp_auth) {
		this.mail_smtp_auth = mail_smtp_auth;
	}

	public String getMail_smtp_user() {
		return mail_smtp_user;
	}

	public void setMail_smtp_user(String mail_smtp_user) {
		this.mail_smtp_user = mail_smtp_user;
	}

	public boolean isMail_smtp_pw_store() {
		return mail_smtp_pw_store;
	}

	public void setMail_smtp_pw_store(boolean mail_smtp_pw_store) {
		this.mail_smtp_pw_store = mail_smtp_pw_store;
	}

	public String getMail_smtp_pw() {
		return mail_smtp_pw;
	}

	public void setMail_smtp_pw(String mail_smtp_pw) {
		this.mail_smtp_pw = mail_smtp_pw;
	}

	public String getMail_from() {
		return mail_from;
	}

	public void setMail_from(String mail_from) {
		this.mail_from = mail_from;
	}

	public String getMail_to() {
		return mail_to;
	}

	public void setMail_to(String mail_to) {
		this.mail_to = mail_to;
	}

	public String getMail_cc() {
		return mail_cc;
	}

	public void setMail_cc(String mail_cc) {
		this.mail_cc = mail_cc;
	}

	public String getMail_bcc() {
		return mail_bcc;
	}

	public void setMail_bcc(String mail_bcc) {
		this.mail_bcc = mail_bcc;
	}

	public boolean isMail_xml() {
		return mail_xml;
	}

	public void setMail_xml(boolean mail_xml) {
		this.mail_xml = mail_xml;
	}

	public boolean isMail_gif() {
		return mail_gif;
	}

	public void setMail_gif(boolean mail_gif) {
		this.mail_gif = mail_gif;
	}

	public boolean isMail_pdf() {
		return mail_pdf;
	}

	public void setMail_pdf(boolean mail_pdf) {
		this.mail_pdf = mail_pdf;
	}
}
