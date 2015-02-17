/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * CopperCore, an IMS-LD level C engine
 * Copyright (C) 2003, 2004 Harrie Martens and Hubert Vogten
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program (/license.txt); if not,
 * write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 *     Contact information:
 *     Open University of the Netherlands
 *     Valkenburgerweg 177 Heerlen
 *     PO Box 2960 6401 DL Heerlen
 *     e-mail: hubert.vogten@ou.nl or
 *             harrie.martens@ou.nl
 *
 *
 * Open Universiteit Nederland, hereby disclaims all copyright interest
 * in the program CopperCore written by
 * Harrie Martens and Hubert Vogten
 *
 * prof.dr. Rob Koper,
 * director of learning technologies research and development
 *
 */

package org.coppercore.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Implements a singleton send mail component.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2009/07/02 09:11:30 $
 */
public class Mailer {
	private static Mailer mailer = null;
	private String smtpHost = null;
	private Integer smtpPort = new Integer(25);
	private String userName = null;
	private String password = null;

	private Mailer(String smtpHost, Integer smtpPort, String userName, String password) {
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
		this.userName = userName;
		this.password = password;
	}

	/**
	 * Sends a email message to the specified recipients.
	 * 
	 * <p>
	 * Each recipient is described by an String[] where an array of length 1
	 * represents the emailadress of the recipient and an array of length 2
	 * where the first String contains the emailaddress of the recipient and the
	 * second String contains the name of the recipient
	 * 
	 * @param from
	 *            String the emailaddress of sender of the email message
	 * @param to
	 *            ArrayList the list of recipients. Each list entry is a
	 *            String[2] of length 1 which represents the emailadress of the
	 *            recipient or of length 2 where the first String contains the
	 *            emailaddress of the recipient and the second String contains
	 *            the name of the recipient
	 * @param subject
	 *            String the subject of the mail message
	 * @param content
	 *            String the content of the email message
	 * @throws AddressException
	 *             when an invalid address is specified
	 * @throws MessagingException
	 *             when there is a message protocol error
	 * @throws UnsupportedEncodingException
	 *             when an invalid encoding is specified
	 */
	public void sendMail(String from, ArrayList to, String subject, String content) throws AddressException,
			UnsupportedEncodingException, MessagingException {

		Session session = getSession();

		// Construct the message
		javax.mail.Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		Iterator iter = to.iterator();
		while (iter.hasNext()) {
			String[] tuple = (String[]) iter.next();

			String recipient = tuple[0];
			if (tuple.length > 1) {
				msg.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipient, tuple[1]));

			} else {
				msg.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipient));
			}
		}

		msg.setSubject(subject);
		msg.setText(content);

		// Send the message
		Transport.send(msg);
	}

	private Session getSession() {
		Properties properties = new Properties();

		properties.put("mail.smtp.host", smtpHost);
		properties.put("mail.smtp.port", smtpPort.toString());

		if (userName != null && password != null) {
			Authenticator authenticator = new Authenticator(userName, password);

			properties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
			properties.setProperty("mail.smtp.auth", "true");
			return Session.getInstance(properties, authenticator);

		}
		return Session.getInstance(properties, null);

	}

	private class Authenticator extends javax.mail.Authenticator {
		private PasswordAuthentication authentication;

		public Authenticator(final String userName, final String password) {
			authentication = new PasswordAuthentication(userName, password);
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return authentication;
		}
	}

	/**
	 * Creates a single Mailer instance and sets the required parameters to the
	 * specified values.
	 * <p />
	 * If authentication is not required the user name and password parameters
	 * should be null.
	 * 
	 * @param smtpHost
	 *            String the smtp host used by this mailer
	 * @param smtpPort
	 *            Integer the smtp port used by this mailer
	 * @param userName
	 *            the user name used for authenticating the SMTP session
	 * @param password
	 *            the password used for authenticating the SMTP session
	 * @return Mailer the mailer instance to use for sending mail
	 */
	public static Mailer createMailer(String smtpHost, Integer smtpPort, String userName, String password) {
		if (mailer == null) {
			mailer = new Mailer(smtpHost, smtpPort, userName, password);
		}

		return mailer;
	}

	/**
	 * Returns the mailer instance.
	 * 
	 * <p>
	 * Make sure createMailer has been called to create a new mailer before
	 * calling this method. Only one mailer is created per jvm.
	 * 
	 * @return Mailer the singleton mailer instance, or null if createMailer has
	 *         not successfully been called
	 * @see #createMailer
	 */
	public static Mailer getMailer() {
		return mailer;
	}
}
