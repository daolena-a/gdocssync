/*
* Copyright (C) 2003-2009 eXo Platform SAS.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/

package org.gds.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @version $Revision$
 */
public class GDSTray
{
   private final SystemTray systemTray;
   private final URL imageURL;
   private final Image image;
   private final TrayIcon trayIcon;

   public GDSTray()
   {
      systemTray = SystemTray.getSystemTray();
      imageURL = Thread.currentThread().getContextClassLoader().getResource("tray.png");
      image = Toolkit.getDefaultToolkit().getImage(imageURL);
      trayIcon = new TrayIcon(image, "Tray Demo", createPopumMenu());
      trayIcon.setImageAutoSize(true);
   }

   public void show()
   {
      try
      {
         systemTray.add(trayIcon);
      }
      catch (AWTException e)
      {
        System.err.println("TrayIcon could not be added.");
      }
   }

   public void upToDate()
   {

   }

   public void updating()
   {
      
   }

   private PopupMenu createPopumMenu()
   {
      PopupMenu popupMenu = new PopupMenu();
      MenuItem account = new MenuItem("Account");
      {
         account.addActionListener(new ActionListener()
         {
            public void actionPerformed(final ActionEvent actionEvent)
            {
               createAccountFrame();
            }
         });
      }

      MenuItem exitItem = new MenuItem("Exit");
      popupMenu.add(account);
      popupMenu.add(exitItem);
      return popupMenu;
   }

   private JFrame createAccountFrame()
   {
      JFrame accountFrame = new JFrame();
      accountFrame.setTitle("Account configuration");
      accountFrame.setSize(new Dimension(400, 300));
      accountFrame.setVisible(true);
      return accountFrame;
   }
}
