package com.baselet.standalone;

import com.baselet.control.Main;
import com.baselet.control.config.handler.ConfigHandler;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.Utils;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.MailPanel;
import com.baselet.standalone.gui.StandaloneGUI;
import com.icegreen.greenmail.user.GreenMailUser;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MailTest {
    private DiagramHandler diagramToSend;
    private File uxfClassDiagram;

    private final GreenMail mailServer = new GreenMail(ServerSetupTest.SMTP);

    private String subject;

    @Given("An already created class diagram")
    public void loadClassDiagram() {
        Utils.BuildInfo buildInfo = Utils.readBuildInfo();
        Program.init(buildInfo.version, RuntimeType.BATCH);
        ConfigHandler.loadConfig();

        uxfClassDiagram = new File("src/test/resources/cucumber/class_diagram.uxf");
        diagramToSend = new DiagramHandler(uxfClassDiagram);
        CurrentDiagram.getInstance().setCurrentDiagramHandler(diagramToSend);
    }

    @Then("send the email with subject: {string}")
    public void sendMail(String subject) {
        this.subject = subject;

        // GreenMail setup
        mailServer.start();

        System.getProperties().put("mail.smtp.port", "3025");
        String server = mailServer.getSmtp().getServerSetup().getBindAddress();

        // Create a sender
        String mail = "user@localhost";
        String username = "user@localhost";
        String password = "password";

        GreenMailUser sender = mailServer.setUser(mail, username, password);

        // Mail panel setup
        CurrentGui.getInstance().setGui(new StandaloneGUI(Main.getInstance(), uxfClassDiagram));
        StandaloneGUI gui = (StandaloneGUI) CurrentGui.getInstance().getGui();
        gui.initGUI();
        gui.open(diagramToSend);

        MailPanel panel = gui.guiBuilder.getMailPanel();
        panel.tf_to.setText("receiver@localhost");
        panel.tf_subject.setText(subject);
        panel.ta_text.setText("");

        panel.cb_attachGif.setSelected(false);
        panel.cb_attachPdf.setSelected(false);
        panel.cb_attachXml.setSelected(true);

        // SMTP Login fields
        panel.tf_from.setText(sender.getEmail());
        panel.tf_smtp.setText(server);
        panel.cb_smtp_auth.setSelected(true);
        panel.tf_smtpUser.setText(sender.getLogin());
        panel.pf_smtpPW.setText(sender.getPassword());

        panel.sendMail();
    }

    @Then("Verify if the email has been sent with the correct data")
    public void verifyMail() throws MessagingException, IOException {
        assertTrue(mailServer.waitForIncomingEmail(5000, 1));
        MimeMessage mail = mailServer.getReceivedMessages()[0];

        assertEquals(this.subject, mail.getSubject());

        // Test if the attached files are .ufx, .pdf and .gif
        Multipart multiPart = (Multipart) mail.getContent();
        for (int i = 0; i < multiPart.getCount(); i++) {
            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                assertTrue(part.getFileName().endsWith(".uxf") ||
                        part.getFileName().endsWith(".pdf") ||
                        part.getFileName().endsWith(".gif")
                );
            }
        }
    }
}
