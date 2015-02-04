/*
 * CopperCore, an IMS-LD level C engine
 * Copyright (C) 2003 Harrie Martens and Hubert Vogten
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

package org.coppercore.condition;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.Configuration;
import org.coppercore.common.Mailer;
import org.coppercore.common.Parser;
import org.coppercore.common.ParserErrorHandler;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ComponentFactory;
import org.coppercore.component.ExplicitProperty;
import org.coppercore.component.ExpressionElement;
import org.coppercore.component.LearningActivityProperty;
import org.coppercore.exceptions.ComponentDataException;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;
import org.coppercore.parser.IMSLDEmailDataNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The Class Notification implements the IMS LD Notification element.
 */
public class Notification extends ThenAction {
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 42L;

  /** The recipient roles. */
  private ArrayList /* Recipient */recipientRoles = new ArrayList();

  /** The subject. */
  private Subject subject = null;

  /** The activity id. */
  private String activityId = null;

  /** The activity tag. */
  private String activityTag = null;

  /** The Constant DEFAULT_MESSAGE_BODY. */
  private static final String DEFAULT_MESSAGE_BODY = "CopperCore notifcation body. This text should be modified to allow player specific implementation linking to activity. ActivityId is ";

  /** The builder. */
  private static DocumentBuilder builder = null;

  /**
   * The Constructor. Constructs a new Notifaction object based on the passed node.
   * 
   * @param node the node defining the Notification object
   */
  public Notification(Element node) {
    parseXml(node);
  }

  /**
   * The Constructor.
   * 
   * @param xmlData the xml data
   * 
   * @throws ComponentDataException the component data exception
   */
  public Notification(String xmlData) throws ComponentDataException {
    getBuilder();
    try {
      Document root = builder.parse(Parser.inputSourceFromString(xmlData));

      Element notification = root.getDocumentElement();

      parseXml(notification);

      Node child = notification.getFirstChild();

      // build up the sub-components (email-data and subject)
      while ((child != null) && (child.getNodeType() == Node.ELEMENT_NODE)) {
        this.addElement((Element) child);

        child = child.getNextSibling();
      }
    } catch (Exception ex) {
      throw new ComponentDataException(ex);
    }
  }

  /**
   * The Constructor.
   * 
   * @param activityTag the activity tag
   * @param activityId the activity id
   * @param recipients the recipients
   * @param subject the subject
   */
  public Notification(ArrayList recipients, String subject, String activityId, String activityTag) {
    Iterator iter = recipients.iterator();
    while (iter.hasNext()) {
      IMSLDEmailDataNode item = (IMSLDEmailDataNode) iter.next();
      this.recipientRoles.add(new RecipientRole(item));
    }

    this.subject = new Subject(subject);
    this.activityId = activityId;
    this.activityTag = activityTag;
  }

  /**
   * Parses the xml.
   * 
   * @param node the node
   */
  private void parseXml(Element node) {
    // check if there is are referenced activity id
    if (node.hasAttribute("ref")) {
      activityId = node.getAttribute("ref");
    }

    if (node.hasAttribute("type")) {
      activityTag = node.getAttribute("type");
    }
  }

  /* (non-Javadoc)
   * @see org.coppercore.condition.ThenAction#addElement(org.w3c.dom.Element, int)
   */
  public ExpressionElement addElement(Element node, int uolId) throws TypeCastException, PropertyException {

    return addElement(node);
  }

  /**
   * Adds the element.
   * 
   * @param node the node
   * 
   * @return the expression element
   * 
   * @throws PropertyException the property exception
   */
  public ExpressionElement addElement(Element node) throws PropertyException {

    String nodeName = node.getNodeName();

    if (nodeName.equals("cc:email-data")) {
      RecipientRole newRecipientRole = new RecipientRole(node);
      recipientRoles.add(newRecipientRole);
      return newRecipientRole;
    } else if (nodeName.equals("cc:subject")) {
      subject = new Subject(node);
      return subject;
    }
    // a fall trough indicates that an element was not recognized, so throw
    // exception
    throw new ComponentDataException("Invalid notification format encountered: " + Parser.documentToString(node));
  }

  /* (non-Javadoc)
   * @see org.coppercore.condition.ThenAction#performAction(org.coppercore.business.Uol, org.coppercore.business.Run, org.coppercore.business.User, java.util.Collection)
   */
  protected void performAction(Uol uol, Run run, User user, Collection firedActionns) throws TypeCastException,
      PropertyException, ExpressionException {
    performAction(uol, run, user);
  }

  /**
   * Perform action.
   * 
   * @param user the user
   * @param run the run
   * @param uol the uol
   * 
   * @throws TypeCastException the type cast exception
   * @throws PropertyException the property exception
   * @throws ExpressionException the expression exception
   */
  public void performAction(Uol uol, Run run, User user) throws TypeCastException, PropertyException,
      ExpressionException {
    // perform the notification
    HashSet recipients = new HashSet(100);

    // collect all recipients for all roles
    Iterator iter = recipientRoles.iterator();
    while (iter.hasNext()) {
      RecipientRole recipientRole = (RecipientRole) iter.next();
      Collection users = User.findByRoleId(run.getId(), recipientRole.getRoleId());

      // collect recipients for a specific role
      Iterator iter2 = users.iterator();
      while (iter2.hasNext()) {
        User aUser = (User) iter2.next();
        try {
          showActivity(uol, run, aUser);
          
          // show was successfull, now we can add this user to the set of recipients.
          // The Set takes care of avoiding duplicate recipients.
          // See equals operator of Recipient          
          Recipient recipient = createRecipient(uol, run, recipientRole, aUser);
          if (recipient != null) {
            recipients.add(recipient);
          }
        } catch (PropertyException ex) {
          // something went wrong with this user, just try next.
          Logger logger = Logger.getLogger(this.getClass());
          logger.error("Notification failed to make activity visibible", ex);
        }
      }
    }

    //now send the mail
    sendMail(recipients);
  }

  /**
   * Send mail.
   * 
   * @param recipients the recipients
   * 
   * @throws PropertyException the property exception
   */
  private void sendMail(HashSet recipients) throws PropertyException {
    // ok we have all the recipients collected
    String mailAtOnce = System.getProperty("org.coppercore.notification.mailAtOnce", "false");
    // check what the prefered way of mailing is
    if (mailAtOnce.equals("true")) {
      mailAtOnce(recipients);
    } else {
      mailOneByOne(recipients);
    }
  }

  /**
   * Creates the recipient.
   * 
   * @param aUser the a user
   * @param run the run
   * @param uol the uol
   * @param recipientRole the recipient role
   * 
   * @return the recipient
   */
  private Recipient createRecipient(Uol uol, Run run, RecipientRole recipientRole, User aUser) {
    ExplicitProperty userName = null;
    ExplicitProperty emailAddress = null;
    Recipient recipient = null;

    // make code robust against missing property values
    try {
      emailAddress = ComponentFactory.getPropertyFactory().getProperty(uol, run, aUser, recipientRole.getEmailPropId());

      String userNamePropId = recipientRole.getUserNamePropId();
      if (userNamePropId != null && !userNamePropId.equals("")) {
        userName = ComponentFactory.getPropertyFactory()
            .getProperty(uol, run, aUser, recipientRole.getUserNamePropId());
      }

      // check if the email address was filled
      if (emailAddress.getValue() != null) {
        recipient = new Recipient(emailAddress.getValue().toString(), (userName == null) ? null : userName
            .getValue().toString());
      }
    } catch (PropertyException ex) {
      // something went wrong with this user, just try next.
      Logger logger = Logger.getLogger(this.getClass());
      logger.error("Notification mail failed. ", ex);
    }

    return recipient;
  }

  /**
   * Show activity.
   * 
   * @param aUser the a user
   * @param run the run
   * @param uol the uol
   * 
   * @throws PropertyException the property exception
   */
  private void showActivity(Uol uol, Run run, User aUser) throws PropertyException {
    // we now must make the activity visible if there is one
    if (activityId != null && !"".equals(activityId)) {
      if (activityTag != null) {
        // there is an activity defined and we know the type
        LearningActivityProperty activity = null;
        if ("support-activity".equals(activityTag)) {
          // it was a support activity
          activity = ComponentFactory.getPropertyFactory().getSupportActivity(uol, run, aUser, activityId);
        } else {
          // it was a learning activity
          activity = ComponentFactory.getPropertyFactory().getLearningActivity(uol, run, aUser, activityId);
        }
        // make this activity visible
        activity.setVisibility(true);
      }
    }
  }

  /**
   * Mail at once.
   * 
   * @param recipients the recipients
   * 
   * @throws PropertyException the property exception
   */
  private void mailAtOnce(HashSet recipients) throws PropertyException {
    ArrayList to = new ArrayList();
    Iterator iter = recipients.iterator();
    while (iter.hasNext()) {
      Recipient recipient = (Recipient) iter.next();

      if (recipient.userName == null) {
        String[] tuple = { recipient.emailAddress };
        to.add(tuple);
      } else {
        String[] tuple = { recipient.emailAddress, recipient.userName };
        to.add(tuple);
      }
    }

    String theSubject = (subject == null) ? "No-subject provided by the author" : subject.toString();

    try {
      Mailer.getMailer().sendMail(Configuration.getSystemEmail(), to, theSubject, DEFAULT_MESSAGE_BODY + activityId);
    } catch (Exception ex) {
      throw new PropertyException(ex);
    }
  }

  /**
   * Mail one by one.
   * 
   * @param recipients the recipients
   * 
   * @throws PropertyException the property exception
   */
  private void mailOneByOne(HashSet recipients) throws PropertyException {
    String errors = null;
    Iterator iter = recipients.iterator();
    while (iter.hasNext()) {
      Recipient recipient = (Recipient) iter.next();
      ArrayList to = new ArrayList();

      if (recipient.userName == null) {
        String[] tuple = { recipient.emailAddress };
        to.add(tuple);
      } else {
        String[] tuple = { recipient.emailAddress, recipient.userName };
        to.add(tuple);
      }
      String theSubject = (subject == null) ? "No-subject provided by the author" : subject.toString();

      try {
        Mailer.getMailer().sendMail(Configuration.getSystemEmail(), to, theSubject, DEFAULT_MESSAGE_BODY + activityId);
      } catch (Exception ex) {
        Logger logger = Logger.getLogger(this.getClass());
        logger.error(ex);

        // store all error messages for reporting
        if (errors == null) {
          errors = ex.getMessage();
        } else {
          errors += ex.getMessage();
        }
      }
    }

    if (errors != null) {
      // there have been errors while trying to send the email
      throw new PropertyException(errors);
    }
  }

  /* (non-Javadoc)
   * @see org.coppercore.condition.ThenAction#hasLocalScope()
   */
  protected boolean hasLocalScope() {
    // all these actions reflect changes for a complete group and should
    // therefore
    // considered local
    return true;
  }

  /**
   * Create.
   * 
   * @param node the node
   * 
   * @return the notification
   */
  public static Notification create(Element node) {

    return new Notification(node);
  }

  /**
   * Gets the builder.
   * 
   * @return the builder
   */
  protected DocumentBuilder getBuilder() {
    if (builder == null) {
      try {
        builder = Parser.getDocumentBuilder(new ParserErrorHandler());
      } catch (ParserConfigurationException ex) {
        Logger logger = Logger.getLogger(this.getClass());
        logger.error(ex);
        throw new EJBException(ex);
      }
    }
    return builder;
  }

  /* (non-Javadoc)
   * @see org.coppercore.condition.ThenAction#toXml(java.io.PrintWriter)
   */
  protected void toXml(PrintWriter out) {
    XMLTag tag = new XMLTag("cc:notification");

    if (activityId != null) {
      tag.addAttribute("ref", activityId);
    }

    if (activityTag != null) {
      tag.addAttribute("type", activityTag);
    }

    tag.writeOpenTag(out);

    Iterator iter = recipientRoles.iterator();
    while (iter.hasNext()) {
      RecipientRole recipient = (RecipientRole) iter.next();
      recipient.toXml(out);
    }

    if (subject != null) {
      subject.toXml(out);
    }

    tag.writeCloseTag(out);
  }

  /**
   * The Class RecipientRole.
   */
  private class RecipientRole extends ConditionRoot {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 42L;

    /** The email prop id. */
    private String emailPropId = null;

    /** The user name prop id. */
    private String userNamePropId = null;

    /** The role id. */
    private String roleId = null;

/*    private RecipientRole(String emailPropId, String userNamePropId, String roleId) {
      this.emailPropId = emailPropId;
      this.userNamePropId = userNamePropId;
      this.roleId = roleId;
    }*/

    /**
 * The Constructor.
 * 
 * @param data the data
 */
RecipientRole(IMSLDEmailDataNode data) {
      this.emailPropId = data.getEmailPropId();
      this.userNamePropId = data.getUserNamePropId();
      this.roleId = data.getRoleId();
    }

    /**
     * The Constructor.
     * 
     * @param node the node
     */
    RecipientRole(Element node) {
      this.emailPropId = node.getAttribute("email-property-ref");
      this.userNamePropId = node.getAttribute("username-property-ref");
      this.roleId = node.getAttribute("role-ref");
    }

    /* (non-Javadoc)
     * @see org.coppercore.condition.ConditionRoot#addElement(org.w3c.dom.Element, int)
     */
    public ExpressionElement addElement(Element node, int uolId) throws TypeCastException, PropertyException {
      return null;
    }

    /**
     * Gets the email prop id.
     * 
     * @return the email prop id
     */
    protected String getEmailPropId() {
      return emailPropId;
    }

    /**
     * Gets the user name prop id.
     * 
     * @return the user name prop id
     */
    public String getUserNamePropId() {
      return userNamePropId;
    }

    /**
     * Gets the role id.
     * 
     * @return the role id
     */
    public String getRoleId() {
      return roleId;
    }

    /**
     * To xml.
     * 
     * @param out the out
     */
    protected void toXml(PrintWriter out) {
      XMLTag tag = new XMLTag("cc:email-data");
      tag.addAttribute("email-property-ref", emailPropId);
      tag.addAttribute("username-property-ref", userNamePropId);
      tag.addAttribute("role-ref", roleId);
      tag.writeEmptyTag(out);
    }
  }

  /**
   * The Class Subject.
   */
  private class Subject extends ConditionRoot {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 42L;

    /** The subject line. */
    private String subjectLine = null;

    /**
     * The Constructor.
     * 
     * @param subject the subject
     */
    Subject(String subject) {
      this.subjectLine = subject;
    }

    /**
     * The Constructor.
     * 
     * @param node the node
     */
    Subject(Element node) {
      this.subjectLine = Parser.getTextValue(node);
    }

    /* (non-Javadoc)
     * @see org.coppercore.condition.ConditionRoot#addElement(org.w3c.dom.Element, int)
     */
    public ExpressionElement addElement(Element node, int uolId) throws TypeCastException, PropertyException {
      return null;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
      return subjectLine;
    }

    /**
     * To xml.
     * 
     * @param out the out
     */
    protected void toXml(PrintWriter out) {
      XMLTag tag = new XMLTag("cc:subject");
      tag.writeOpenTag(out);
      out.print(subjectLine);
      tag.writeCloseTag(out);
    }
  }

  /**
   * The Class Recipient.
   * 
   * @author Harrie Martens
   * @author Hubert Vogten
   * @version $Revision: 1.15 $, $Date: 2009/12/02 14:00:52 $
   */
  private class Recipient {
    
    /** The email address. */
    protected String emailAddress = null;

    /** The user name. */
    protected String userName = null;

    /**
     * The Constructor.
     * 
     * @param userName the user name
     * @param emailAddress the email address
     */
    Recipient(String emailAddress, String userName) {
      this.emailAddress = emailAddress;
      this.userName = userName;
    }

    /*
     * private boolean equals(Recipient that) { return ( (this.userId == null) ?
     * (that.userId == null) : (this.userId.equals(that.userId)) &&
     * (this.emailAddress == null) ? (that.emailAddress == null) :
     * (this.emailAddress.equals(that.emailAddress)) && (this.userName == null) ?
     * (that.userName == null) : (this.userName.equals(that.userName))); }
     */
  }
}
